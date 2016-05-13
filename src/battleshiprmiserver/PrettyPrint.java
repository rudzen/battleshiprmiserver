/*
 * The MIT License
 *
 * Copyright 2015-6 Rudy Alex Kohn <s133235@student.dtu.dk>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */package battleshiprmiserver;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple class to print out stuff to console that looks nice!
 * @author rudz
 */
public final class PrettyPrint {

    private final int MAX_LEN = 78;
    private final int MAX_HEI = 20;

    private final String dot = "\u00AA";

    private final String SEP = replicate(dot, MAX_LEN);

    private final String TITEL = "BattleShip RMI Server v1";
    private final String TITEL2 = "";//Configure port with --hangman-port=<port>";
    private final String INFO = "R.A.Kohn s133235";

    private final int TITLE_LEN = TITEL.length() + TITEL2.length();

    private final int port;
    private int pos;
    private String title;
    private String registry;
    private String rest;

    private final ArrayList<String> menu = new ArrayList<>(41);

    public PrettyPrint(final String registry, final int port, final String rest_address) {
        this.registry = registry;
        this.port = port;
        this.rest = rest_address;
        pos = getTop(-1);
        pos = getStatus(pos);
        pos = getButtom(pos);
    }

    /**
     * Prints out the current menu to console.
     */
    public void showMenu() {
        menu.stream().forEach((s) -> {
            System.out.println(s);
        });
    }

    /**
     * Generate the top part of all menus
     *
     * @param pos : the current pos in the menu generating process
     */
    private int getTop(final int pos) {
        int p = pos;
        menu.add(++p, SEP);
        menu.add(++p, makeFilledBorder(TITEL));
        menu.add(++p, makeFilledBorder(TITEL2));
        menu.add(++p, SEP);
        menu.add(++p, makeSingleBorderCentered(INFO));
        return p;
    }

    /**
     * Generates the status part of the menu screen.
     *
     * @param pos : The current row position.
     * @return the row position when done.
     */
    private int getStatus(final int pos) {
        String IP = getIPString();
        int p = pos;
        menu.add(++p, makeSingleBordered(""));
        menu.add(++p, makeFilledBorder("Status"));
        menu.add(++p, makeSingleBordered("Date started             : " + new Date().toString().trim()));
        menu.add(++p, makeSingleBordered("Server Local IP          : " + ((IP != null) ? IP : "<No NIC detected.>")));
        menu.add(++p, makeSingleBordered("RMI Registry             : " + registry));
        menu.add(++p, makeSingleBordered("Port                     : " + Integer.toString(port)));
        menu.add(++p, makeSingleBordered("REST Game Server         : " + rest));
        menu.add(++p, makeSingleBordered("Clients Connected        : " + "N/A"));
        return p;
    }

    /**
     * Makes the last bit of the menu screen.
     *
     * @param pos : current row pos
     * @return The current row pos when done.
     */
    private int getButtom(final int pos) {
        int p = pos;
        
        /* Not used atm, because this is actually not a menu YET! */
//        while (p < MAX_HEI) {
//            this.menu.add(++p, dot + space(MAX_LEN - 2) + dot);
//        }
        this.menu.add(++p, overwrite(SEP, " < powered by con-dynmenu v0.2 by rudz > ", 30));
        return p;
    }

    /**
     * Simple helper method to get the IP of the current system.<br>
     * If an IPv4 address couldn't be located, an IPv6 address is attempted.<br>
     * If neither, an empty string is returned.
     *
     * @return the IP as string.
     */
    @SuppressWarnings({"static-access", "null"})
    private static String getIPString() {
        InetAddress localIP = null;
        String returnString = null;
        try {
            // priority for IPv4 addresses, if it fails, try get a IPv6
            localIP = Inet4Address.getLocalHost();
            returnString = localIP.toString().trim();
        } catch (java.net.UnknownHostException ex) {
            Logger.getLogger(PrettyPrint.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (localIP == null || localIP.toString().length() == 0) {
                try {
                    localIP = Inet6Address.getLocalHost();
                    returnString = localIP.toString().trim();
                } catch (java.net.UnknownHostException ex) {
                    Logger.getLogger(PrettyPrint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (localIP == null) {
                returnString = "";
            } else if (returnString.contains("/")) {
                returnString = returnString.substring(returnString.indexOf("/"));
            }
        }
        return returnString;
    }

    /**
     * Encapsulates a specified string with filled "border" of current
     * 'dot'.<br>
     * The parsed string will be centred.
     *
     * @param s : the string to encapsulate
     * @return the completed string
     */
    private String makeFilledBorder(final String s) {
        final int sLen = s.length();
        StringBuilder sb = new StringBuilder(80);
        if (sLen < this.MAX_LEN - 2) {
            if (sLen == this.MAX_LEN - 4) {
                sb.append(this.dot).append(' ').append(s).append(' ').append(dot);
            } else {
                sb.append(replicate(dot, ((MAX_LEN - (sLen % 2)) - 2 - sLen) >> 1));
                sb.append(' ').append(s).append(' ');
                sb.append(replicate(dot, ((MAX_LEN - (sLen % 2)) - 2 - sLen) >> 1));
                if (sLen % 2 != 0) {
                    sb.append(replicate(dot, sLen % 2));
                }
            }
        } else {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Generates a line containing a single 'dot' at the beginning and end.<b>
     * The rest is filled with spaces and the parsed string is centred.
     *
     * @param s : the string to centre
     * @return : the resulting string
     */
    private String makeSingleBorderCentered(final String s) {
        final int sLen = s.length();
        if (sLen > MAX_LEN - 2) {
            return s;
        }
        StringBuilder sb = new StringBuilder(80);
        if (sLen == MAX_LEN - 4) {
            sb.append(dot).append(' ').append(s).append(' ').append(dot);
        } else if (sLen < MAX_LEN - 4) {
            sb.append(dot);
            sb.append(space(((MAX_LEN - (sLen % 2)) - 4 - sLen) >> 1));
            sb.append(' ').append(s).append(' ');
            if (sLen % 2 != 0) {
                sb.append(space(sLen % 2));
            }
            sb.append(space(((MAX_LEN - (sLen % 2)) - 4 - sLen) >> 1));
            sb.append(dot);
        } else {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Generates a line containing a single 'dot' at the beginning and end.<b>
     * The string will follow after a space from the left, filled with spaces up
     * until the last 'dot'.
     *
     * @param s : the string to encapsulate.
     * @return : the resulting string
     */
    private String makeSingleBordered(final String s) {
        final int sLen = s.length();
        if (sLen > this.MAX_LEN - 2) {
            return s;
        }
        StringBuilder sb = new StringBuilder(80);
        sb.append(dot).append(' ').append(s);
        sb.append(space(MAX_LEN - 3 - sLen)).append(dot);
        return sb.toString();
    }

    /**
     * Creates a string containing spaces
     *
     * @param amount : how many spaces to make
     * @return the string containing amount spaces
     */
    private static String space(final int amount) {
        return replicate(' ', amount);
    }

    /**
     * Replicate a char
     *
     * @param c : the char to replicate
     * @param amount : how many time to replicate
     * @return the string containing amount char
     */
    private static String replicate(final char c, final int amount) {
        StringBuilder sb = new StringBuilder(1 + amount);
        for (int i = 1; i <= amount; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    private static String replicate(final String c, final int amount) {
        StringBuilder sb = new StringBuilder(1 + amount * c.length());
        for (int i = 1; i <= amount; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Replaces a piece of an existing string with another string.<br>
     *
     * @param into : The string to overwrite in
     * @param toInsert : The string which is put in the into string.
     * @param startPos : Start position where it should be inserted at.
     * @return The resulting string.
     */
    private static String overwrite(final String into, final String toInsert, final int startPos) {
        final int len = into.length();
        if (len == 0) {
            return toInsert;
        }
        int lenInsert = toInsert.length();
        if (lenInsert == 0) {
            return into;
        }
        StringBuilder sb = new StringBuilder(len);
        // no fault check from here!
        sb.append(into.substring(0, startPos - 1));
        sb.append(toInsert);
        if (startPos - 1 + lenInsert <= len - 1) {
            sb.append(into.substring(sb.length(), into.length()));
        }
        return sb.toString();
    }
}
