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

/**
 * Stores information about socket endpoint (address/port pair).
 *
 * @author Tim Taylor -- tttaylor@cssassociates.com
 */
public class EndpointInfo {

    private final String host;
    private final int port;

    /**
     * Constructor.
     * @param host
     * @param port
     */
    public EndpointInfo(final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Utility method for creating an endpoint string of the form address:port.
     *
     * @param address The address part of the endpoint string.
     * @param port The port part of the endpoint string.
     * @return The endpoint string.
     */
    public static String getEndpointString(final String address, final int port) {
        return address + ":" + port;
    }

    /**
     * Utility method for creating an endpoint string of the form address:port.
     *
     * @param address The address part of the endpoint string.
     * @param port The port part of the endpoint string.
     * @return The endpoint string.
     */
    public static String getEndpointString(final byte[] address, final int port) {
        return getEndpointString(getAddressString(address), port);
    }

    /**
     * Utility method to convert an IP address to a string.
     *
     * @param address The four bytes of an IP address.
     * @return The corresponding string (a.b.c.d format).
     */
    public static String getAddressString(final byte[] address) {
        return (address[0] & 0xff) + "."
                + (address[1] & 0xff) + "."
                + (address[2] & 0xff) + "."
                + (address[3] & 0xff);
    }

    /**
     * @return host of this endpoint.
     */
    public String getHost() {
        return host;
    }

    /**
     * @return port of this endpoint.
     */
    public int getPort() {
        return port;
    }
}
