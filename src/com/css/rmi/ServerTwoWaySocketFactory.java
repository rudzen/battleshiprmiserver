/*
 * Copyright 2000 Computer System Services, Inc.
 *
 * Permission to use this software for any purpose is granted provided that
 * this copyright notice is preserved.
 *
 * This software is provided as-is and without warranty as to its
 * fitness for any purpose.  In other words, Computer System Services,
 * Inc. does not guarantee that this software works.  It is provided
 * only in the hope that it may be found useful by someone.
 *
 * Please e-mail tttaylor@cssassociates.com if you find any errors
 * or want to request changes/enhancements.
 */
package com.css.rmi;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Server side socket factory which allows the server to make callbacks to a
 * client whose address is unreachable from the server (as long as the server is
 * reachable from the client).
 *
 * @author Tim Taylor
 * 
 * 09.May.2016
 * -----------
 * @author Rudy Alex Kohn <s133235@student.dtu.dk>
 * - Reworked syntax for modern Java (8).
 * - Added usage of Java 8 concurrency library for better performance.
 */
public class ServerTwoWaySocketFactory extends RMISocketFactory {

    /**
     * Socket pools where sockets requested from client are returned.
     */
    //private final Map socketPools = Collections.synchronizedMap(new HashMap());
    private final ConcurrentHashMap<String, SocketPool> socketPools = new ConcurrentHashMap<>();

    /**
     * Map of request streams to clients.
     */
//    private final Map requestStreams = Collections.synchronizedMap(new HashMap());
    private final ConcurrentHashMap<String, DataOutputStream> requestStreams = new ConcurrentHashMap<>();

    /**
     * Add a request <code>socket</code> to the factory. The factory will use
     * this <code>socket</code> to request a callback socket from the host of
     * the given <code>address</code>.
     * <p>
     *
     * @param address Address of the client
     * @param socket Socket on which to request callback sockets from the
     * client.
     * @throws java.io.IOException
     */
    public void addRequestSocket(final byte[] address, Socket socket) throws IOException {

        final DataOutputStream requestOutStream = new DataOutputStream(socket.getOutputStream());
        final DataInputStream requestInStream = new DataInputStream(socket.getInputStream());

        // Thread for receiving notifications of client exports.
        // We need the port to know which request stream to use
        // in the event of multiple client VMs calling from
        // a single host.
        new Thread() {
            @Override
            public void run() {
                try {
                    for (;;) {
                        // Read port from data input stream
                        int port = requestInStream.readInt();
                        String endpoint = EndpointInfo.getEndpointString(address, port);
                        requestStreams.put(endpoint, requestOutStream);
                    }
                } catch (IOException e) {
                }
            }
        }.start();
    }

    /**
     * Create a socket connectoin to the host of the given <code>address
     * </code> and <code>port</code>. If the <code>address</code> is for a host
     * that has provided a callback socket request signalling channel, then that
     * channel is used to request that the client create the socket. Otherwise a
     * socket is created directly to the client from the server.
     *
     * @param address Address for the socket.
     * @param port Port for the socket.
     * @return 
     * @throws java.io.IOException
     */
    @Override
    public Socket createSocket(final String address, final int port) throws IOException {
        String endpoint = EndpointInfo.getEndpointString(address, port);

        DataOutputStream requestStream = (DataOutputStream) requestStreams.get(endpoint);

        if (requestStream == null) {
            return new Socket(address, port);
        }

        SocketPool requestPool;

        synchronized (socketPools) {
            requestPool = (SocketPool) socketPools.get(endpoint);

            if (requestPool == null) {
                requestPool = new SocketPool();
                socketPools.put(endpoint, requestPool);
            }
        }

        try {
            synchronized (requestStream) {
                requestStream.writeInt(TwoWay.PROTOCOL_MAGIC);
                requestStream.writeInt(TwoWay.REQUEST_CALLBACK_SOCKET);
                requestStream.writeInt(port);
                requestStream.flush();
            }
        } catch (final Exception e) {
            socketPools.remove(endpoint);
            requestStreams.remove(endpoint);
            return new Socket(address, port);
        }

        return requestPool.getSocket();
    }

    /**
     * Private socket class with a buffered input stream. This allows us to use
     * <code>mark</code> and <code>reset</code> in order to run signalling, RMI
     * client sockets, and callback sockets through the same sets of listening
     * ports.
     */
    private static class BufferedSocket extends Socket {

        Socket sock;
        InputStream in;

        BufferedSocket() throws IOException {
            super();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (in == null) {
                in = new BufferedInputStream(super.getInputStream());
            }
            return in;
        }

        @Override
        public synchronized void close() throws IOException {
            super.close();
        }
    }

    /**
     * Server socket that handles the tunnelling protocol. Clients can connect
     * to the specified <code>port</code> using either the tunnelling protocol
     * or standard RMI client connections. This allows the signalling for the
     * protocol and the RMI calls themselves to be handled on a single port.
     */
    class TwoWayServerSocket extends ServerSocket {

        public TwoWayServerSocket(final int port) throws IOException {
            super(port);
        }

        @Override
        public Socket accept() throws IOException {
            Socket sock = null;

            for (;;) {
                sock = new BufferedSocket();
                implAccept(sock);

                InputStream in = sock.getInputStream();
                in.mark(TwoWay.MAX_MESSAGE_LENGTH);

                DataInputStream dis = new DataInputStream(in);
                DataOutputStream dos = new DataOutputStream(sock.getOutputStream());

                int magic = dis.readInt();
                if (magic != TwoWay.PROTOCOL_MAGIC) {
                    in.reset();
                    break;
                }

                int opcode = dis.readInt();

                if (opcode == TwoWay.REGISTER_CALLBACK_SOCKET_SOURCE) {
                    byte[] address = new byte[4];
                    dis.read(address, 0, 4);

                    addRequestSocket(address, sock);

                    // Tell the client our real address and port
                    // which may be different from the address
                    // and port used to contact us if we are
                    // on the other side of a firewall.
                    dos.writeInt(TwoWay.RETURN_SERVER_ENDPOINT_INFO);
                    dos.write(InetAddress.getLocalHost().getAddress());
                    dos.writeInt(sock.getLocalPort());
                    dos.flush();
                } else if (opcode == TwoWay.RETURN_CALLBACK_SOCKET) {
                    byte[] address = new byte[4];
                    dis.read(address, 0, 4);
                    int port = dis.readInt();

                    SocketPool pool = (SocketPool) socketPools.get(EndpointInfo.getEndpointString(address, port));
                    pool.addSocket(sock);
                }

            }

            return sock;
        }
    }

    /**
     * Returns a server socket for handling the tunneling protocol.
     *
     * @param port Port on which the socket will listen.
     * @return 
     * @throws java.io.IOException
     */
    @Override
    public ServerSocket createServerSocket(final int port) throws IOException {
        return new TwoWayServerSocket(port);
    }

    /**
     * @return a hashcode for this factory
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**
     * @param that
     * @return true if the two socket factories in question return
     * interchangeable sockets.
     */
    @Override
    public boolean equals(final Object that) {
        return (that != null) && (that.getClass().equals(this.getClass()));
    }
}
