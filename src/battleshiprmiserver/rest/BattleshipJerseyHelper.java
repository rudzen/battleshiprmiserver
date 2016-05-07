/*
 * The MIT License
 *
 * Copyright 2016 Rudy Alex Kohn <s133235@student.dtu.dk>.
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
 */
package battleshiprmiserver.rest;

import dataobjects.PPoint;
import dataobjects.Ship;
import interfaces.IShip;
import java.util.StringTokenizer;

/**
 * - Convert java classes to parameters accepted by the web-server.<br>
 * - Convert received responses from the server back to native format too !<br>
 *
 * @author Rudy Alex Kohn <s133235@student.dtu.dk>
 */
public final class BattleshipJerseyHelper {

    /**
     * Convert a single ship to string data.
     *
     * @param ship The ship to convert
     * @return The string result based on the ship data.
     */
    public static String[] shipToString(final IShip ship) {

//            public String deployBoard(String lobbyid, String playerid, String type1, String x1, String y1, String horizontal1, String type2, String x2, String y2, String horizontal2, String type3, String x3, String y3, String horizontal3, String type4, String x4, String y4, String horizontal4, String type5, String x5, String y5, String horizontal5) throws ClientErrorException {
//        return webTarget.path(java.text.MessageFormat.format("deploy_board/lobbyid={0}/playerid={1}/ship1={2}/x={3}/y={4}/horizontal={5}/ship2={6}/x={7}/y={8}/horizontal={9}/ship3={10}/x={11}/y={12}/horizontal={13}/ship4={14}/x={15}/y={16}/horizontal={17}/ship5={18}/x={19}/y={20}/horizontal={21}", new Object[]{lobbyid, playerid, type1, x1, y1, horizontal1, type2, x2, y2, horizontal2, type3, x3, y3, horizontal3, type4, x4, y4, horizontal4, type5, x5, y5, horizontal5})).request().put(null, String.class);
//    }
        final String[] r = new String[4];

        r[0] = shipTypeToString(ship.getType());
        r[1] = Integer.toString(ship.getLocStart().getX());
        r[2] = Integer.toString(ship.getLocStart().getY());
        r[3] = ship.getDirection() == IShip.DIRECTION.HORIZONTAL ? "1" : "0";
        return r;
    }

    /**
     * Convert ALL ships to string based information to send to REST server.
     *
     * @param ships The ship array to convert.
     * @return
     */
    public static String[] shipsToString(final IShip[] ships) {
        if (ships.length > 0) {
            final String[] r = new String[ships.length * 4];
            int posR = 0;
            for (final IShip s : ships) {
                r[posR++] = shipTypeToString(s.getType());
                r[posR++] = Integer.toString(s.getLocStart().getX());
                r[posR++] = Integer.toString(s.getLocStart().getY());
                r[posR++] = s.getDirection() == IShip.DIRECTION.HORIZONTAL ? "1" : "0";
            }
            return r;
        }
        return null;
    }

    private static String shipTypeToString(IShip.TYPE t) {
        if (t == IShip.TYPE.CARRIER) {
            return "0";
        } else if (t == IShip.TYPE.CRUISER) {
            return "1";
        } else if (t == IShip.TYPE.DESTROYER) {
            return "2";
        } else if (t == IShip.TYPE.SUBMARINE) {
            return "3";
        } else {
            return "4";
        }
    }

    private static IShip.TYPE shipStringToType(final String type) {
        if (type.endsWith("0")) {
            return IShip.TYPE.CARRIER;
        } else if (type.endsWith("1")) {
            return IShip.TYPE.CRUISER;
        } else if (type.endsWith("2")) {
            return IShip.TYPE.DESTROYER;
        } else if (type.endsWith("3")) {
            return IShip.TYPE.SUBMARINE;
        } else {
            return IShip.TYPE.PATROL;
        }
    }

    public static IShip stringToShip(final String string) {
        StringTokenizer tokenizer = new StringTokenizer(string, "/");
        Ship s = new Ship();
        PPoint start;
        String tmp[];
        int len;
        
        s.setType(shipStringToType(tokenizer.nextToken()));
        
        tmp = tokenizer.nextToken().split("=");
        start = new PPoint();
        start.setX(Byte.parseByte(tmp[1]));
        tmp = tokenizer.nextToken().split("=");
        start.setY(Byte.parseByte(tmp[1]));
        s.setLocStart(start);
        
        return s;
    }

}
