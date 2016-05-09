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

import com.google.gson.Gson;
import dataobjects.PPoint;
import dataobjects.Player;
import dataobjects.Ship;
import dataobjects.Upgrades;
import game.GameSession;
import interfaces.IShip;
import java.util.StringTokenizer;
import rest.Board;
import rest.Lobby;

/**
 * - Convert java classes to parameters accepted by the web-server.<br>
 * - Convert received responses from the server back to native format too (except ships, which isn't used. !<br>
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
            final String[] r = new String[ships.length << 2];
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

    /**
     * Converts an IShip.TYPE to string based name
     *
     * @param t The type to convert
     * @return The name of the ship based on the type
     */
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

    /**
     * Convert the String name of a ship to a TYPE
     *
     * @param type The name to convert
     * @return The
     */
    private static IShip.TYPE shipStringToType(final String type) {
        if (type.endsWith("0")) {
            return IShip.TYPE.CARRIER;
        } else if (type.endsWith("1")) {
            return IShip.TYPE.CRUISER;
        } else if (type.endsWith("2")) {
            return IShip.TYPE.DESTROYER;
        } else if (type.endsWith("3")) {
            return IShip.TYPE.SUBMARINE;
        } else if (type.endsWith("4")) {
            return IShip.TYPE.PATROL;
        }
        return null;
    }

    /**
     * Idiotic manual convertion from String to ship.
     *
     * @param string The response from the server representing a ship.
     * @return The IShip object for the java clients.
     */
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

    /**
     * Convert a REST based player data structure to Local java data
     * structure.<br>
     * <b>IMPORTANT</b>. Do this <b>as the first thing</b>, otherwise ship
     * upgrades will be lost!
     *
     * @param restPlayer The rest player to convert
     * @return The new player object
     */
    public static Player restPlayerToLocal(rest.Player restPlayer) {
        Player p = new Player(restPlayer.getPlayername());
        p.initShips(); // this is just so there wont be a NPE at some point!

        /* set up the upgrades for the ships */
        IShip[] ships = p.getShips();
        for (IShip ship : ships) {
            if (restPlayer.getArmor() > 0) {
                ship.addUpgrade(Upgrades.UPGRADES.ARMOR, restPlayer.getArmor());
            }
            if (restPlayer.getDecoy() > 0) {
                ship.addUpgrade(Upgrades.UPGRADES.DECOY, restPlayer.getDecoy());
            }
            if (restPlayer.getWeapon() > 0) {
                ship.addUpgrade(Upgrades.UPGRADES.POWER, restPlayer.getWeapon());
            }
            if (restPlayer.getSonar() > 0) {
                ship.addUpgrade(Upgrades.UPGRADES.SONAR, restPlayer.getSonar());
            }
        }
        p.setShips(ships);

        p.setId(restPlayer.getPlayerid());

        return p;
    }

    /**
     * Pupolate a single ship with upgrades
     *
     * @param theShip The ship
     * @param amount The amount of upgrades
     * @param upgradeType The upgrade type
     * @return The ship
     * @deprecated Not used anymore
     */
    private static IShip populateUpgrade(IShip theShip, final Integer amount, Upgrades.UPGRADES upgradeType) {
        if (amount > 0) {
            for (int i = 0; i < amount; i++) {
                theShip.addUpgrade(upgradeType);
            }
        }
        return theShip;
    }

    /**
     * Convert an entire Lobby object to a RMI based GameSession.<br>
     * This includes Players.<br>
     * Note that this function should be the first to be called in case the response contains a lobby.
     *
     * @param l The Lobby to convert
     * @return The gamesession. Note that the ClientInterfaces is NOT PRESENT!,
     * these needs to be copied over from the old one if applicable.
     */
    public static GameSession convertLobby(final Lobby l) {
//        Gson g = new Gson();
//        Lobby l = g.fromJson(lobby, Lobby.class);
//        g = null; // help the GC on the way!
        
        rest.Player rP = l.getDefender();

        /* convert player one */
        Player p1 = restPlayerToLocal(rP);

        /* set player two if exists */
        Player p2 = null;
        if (l.getAttacker() != null) {
            p2 = restPlayerToLocal(l.getAttacker());
        }

        /* convert the boards */
        Board[] boards = l.getBoards();

        /*
    board defined as :
    0 = empty not shot
    1 = shot, no hit
    2 = shot, hit
    3 = shot, hit, armor rating
    4 = ship location
    5 = ship, sunk
         */
        int[][] board;

        for (int i = 0; i < boards.length; i++) {
            board = new int[10][10];
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    if (boards[i].getHit(j, k) == 1) {
                        board[j][k] = 2;
                    } else if (boards[i].getHit(j, k) == 2) {
                        board[j][k] = 5;
                    }
                }
            }
            if (i == 0) {
                p1.setBoard(board);
            } else if (p2 != null) {
                p2.setBoard(board);
            }
        }

        /* put the data in the GameSession object */
        GameSession gs = new GameSession(null, null);

        /* convert both ids */
        gs.setLobbyID(l.getLobbyid());
        gs.setActiveID(l.getActiveId());

        /* update game session with players and client interfaces.
          Note that the interfaces are ALWAYS null, this is because
          they are going to be copied manually after this function
          has returned the new gamesession from the old gamesession.
        */
        gs.setClientOne(null);
        gs.setClientTwo(null);
        gs.setPlayerOne(p1);
        gs.setPlayerTwo(p2);

        /* because i'm a bastard! */
        gs.updateActionTime();
        
        return gs;
    }

    
    public String convertGameSession(final GameSession gs) {
        
        Gson g = new Gson();
        
        /* convert all the shit back again!!!! :) */
        
        
        
        return null;
    }
    
    
}
