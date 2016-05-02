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
package game;

import dataobjects.Player;
import interfaces.IClientListener;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * This is the main game logic..
 *
 * @author rudz
 */
public class BattleGame extends BattleGameAbstract {

    public BattleGame() {
        super();
    }

    /**
     * Catch for shotFired (from any client).
     *
     * @param x
     * @param y
     * @param player
     */
    public void shotFired(final int x, final int y, final Player player) {

        try {
            final String sessionID = isInSession(player);
            if (sessionID != null) {
                Player otherPlayer = sessions.get(sessionID).getOtherPlayer(player);

                // TODO : Determine if the shot actually hit something here!!!
                boolean hit = true;
                players.get(otherPlayer.getName()).shotFired(x, y, hit);

                // TODO : If hit, determine if the ship is sunk and the name of the sunken ship.
                final String shipHit = hit ? "HIT SHIP" : "NO SHIT";

                players.get(player).showMessage("Shot...", hit ? "You hit the opponents " + shipHit : "You missed... looser.", hit ? JOptionPane.WARNING_MESSAGE : JOptionPane.ERROR_MESSAGE);

                // TODO : 
            } else {
                throw new RemoteException();
            }

        } catch (final RemoteException re) {

        }

    }

    private void clearOldSessions(final boolean notifyClients) throws RemoteException {
        final long NOW = System.currentTimeMillis();
        for (GameSession gs : sessions.values()) {
            if (NOW - gs.getTimeCreated() > 360000) {
                if (notifyClients) {
                    if (gs.getPlayerOne() != null) {
                        players.get(gs.getPlayerOne()).showMessage("Session termineret af server.", "Der er g\u00E5et for lang tid..", 0);
                    }
                    if (gs.getPlayerTwo() != null) {
                        players.get(gs.getPlayerTwo()).showMessage("Session termineret af server.", "Der er g\u00E5et for lang tid..", 0);
                    }
                }
                sessions.remove(gs.getGameSessionID());
            }
        }
    }

    /**
     * Generate session from one single client.
     *
     * @param player
     * @param client The client to generate the session with
     * @return true if session was created, otherwise false.
     */
    public synchronized boolean createSession(final Player player, final IClientListener client) {
        final String sessionID = updateSessionID(player, client);
        if (!sessions.containsKey(sessionID)) {
            final GameSession g = new GameSession(player, client);
            g.setGameSessionID(sessionID);
            sessions.put(sessionID, g);
            return true;
        }
        return false;
    }

    /**
     * Creates a session based on two players.
     *
     * @param playerOne The first player in the session.
     * @param playerTwo The second player in the session
     * @param clientOne The client interface for the first player
     * @param clientTwo The client interface for the second player
     * @return true if session was created, otherwise false.
     */
    public synchronized boolean createSession(final Player playerOne, final Player playerTwo, final IClientListener clientOne, final IClientListener clientTwo) {
        final String sessionID = updateSessionID(playerOne.toString(), clientOne, playerTwo.toString(), clientTwo);
        if (sessionID != null && !sessions.containsKey(sessionID)) {
            final GameSession g = new GameSession(playerOne, clientOne, playerTwo, clientTwo);
            g.setGameSessionID(sessionID);
            sessions.put(sessionID, g);
            return true;
        }
        return false;
    }

    public synchronized boolean joinSession(final Player whichSession, final Player joinee) {
        return false;
    }

    public boolean addPlayer(final String player, final IClientListener client) {
        if (players.put(player, client) != null) {
            System.out.print(player + " added");
            try {
                client.showMessage("Added to server", player + " added to server player index", JOptionPane.INFORMATION_MESSAGE);
            } catch (RemoteException ex) {
                Logger.getLogger(BattleGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
        return false;
    }

    /**
     * Removes a session from the list.
     *
     * @param sessionID The sessionID to remove.
     */
    public void removeSession(final String sessionID) {
        if (sessionID != null) {
            sessions.remove(sessionID);
        }
    }

    public void sendMessageAll(final String sourceplayer, final String text, final String title, final int modal) {

        for (final String target : players.keySet()) {
            if (!target.equals(sourceplayer)) {
                try {
                    IClientListener client = players.get(target);
                    client.showMessage(title, text, modal);
                } catch (RemoteException ex) {
                    Logger.getLogger(BattleGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
