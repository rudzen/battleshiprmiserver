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

import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.LinkedList;


/**
 * Provides a simple producer/consumer pool of sockets.
 *
 * @author Tim Taylor  -- tttaylor@cssassociates.com
 */
public  class SocketPool {
    private final LinkedList<Socket> socketList = new LinkedList<>();
    
    public synchronized Socket getSocket() throws InterruptedIOException {
        try {
            while (socketList.isEmpty()) {
                this.wait();
            }
        }
        catch (InterruptedException e) {
            throw new InterruptedIOException();
        }
        
        return (Socket) socketList.removeFirst();
    }
    
    public synchronized void addSocket(Socket socket) {
        socketList.add(socket);
        this.notifyAll();
    }
}
    
