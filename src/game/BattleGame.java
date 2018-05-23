/*
  The MIT License

  Copyright 2016, 2017, 2018 Rudy Alex Kohn.

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
*/

package game;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import dataobjects.Player;
import interfaces.IClientRMI;

/**
 * This is the main game logic..
 *
 * @author rudz
 */
public class BattleGame extends BattleGameAbstract {

    public BattleGame(final ConcurrentHashMap<String, IClientRMI> players, final ConcurrentHashMap<String, GameSession> sessions) {
        super(players, sessions);
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
                final Player otherPlayer = sessions.get(sessionID).getOtherPlayer(player);

                // TODO : Determine if the shot actually hit something here!!!
                final boolean hit = true;
                players.get(otherPlayer.getName()).shotFired(x, y, hit);

                // TODO : If hit, determine if the ship is sunk and the name of the sunken ship.
                final String shipHit = hit ? "HIT SHIP" : "NO SHIT";

                players.get(player.getName()).showMessage("Shot...", hit ? "You hit the opponents " + shipHit : "You missed... looser.", hit ? JOptionPane.WARNING_MESSAGE : JOptionPane.ERROR_MESSAGE);

                // TODO : 
            } else {
                throw new RemoteException();
            }

        } catch (final RemoteException re) {

        }

    }

    private void clearOldSessions(final boolean notifyClients) throws RemoteException {
        final long NOW = System.currentTimeMillis();
        for (final GameSession gs : sessions.values()) {
            if (NOW - gs.getTimeCreated() > 360000) {
                if (notifyClients) {
                    if (gs.getPlayerOne() != null) {
                        players.get(gs.getPlayerOne().getName()).showMessage("Session termineret af server.", "Der er g\u00E5et for lang tid..", 0);
                    }
                    if (gs.getPlayerTwo() != null) {
                        players.get(gs.getPlayerTwo().getName()).showMessage("Session termineret af server.", "Der er g\u00E5et for lang tid..", 0);
                    }
                }
                sessions.remove(gs.getGameSessionID());
            }
        }
    }

    /**
     * Removes Game Sessions without Clients attached.
     */
    public synchronized void deleteEmptySessions() {
        new Runnable() {
            @Override
            public void run() {
                sessions.keySet().parallelStream().filter(s -> (sessions.get(s).getClientOne() == null && sessions.get(s).getClientTwo() == null)).forEach(s -> {
                    sessions.remove(s);
                });
            }
        }.run();
    }

    /**
     * Generate session from one single client.
     *
     * @param player
     * @param client The client to generate the session with
     * @return true if session was created, otherwise false.
     */
    public synchronized boolean createSession(final Player player, final IClientRMI client) {
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
    public synchronized boolean createSession(final Player playerOne, final Player playerTwo, final IClientRMI clientOne, final IClientRMI clientTwo) {
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

    /**
     * Joins the first availble game, returns the game session ID
     *
     * @param player the player to join session
     * @return Session ID
     */
    public synchronized String joinGame(final Player player) {
        String sessionID = null;
        if (sessions.isEmpty()) {
            if (createSession(player, players.get(player.getName()))) {
                sessionID = updateSessionID(player, players.get(player.getName()));
            }
        } else {
            for (final GameSession gs : sessions.values()) {
                if (!gs.isFull()) {
                    gs.setClientTwo(players.get(player.getName()));
                    gs.setPlayerTwo(player);
                    sessionID = updateSessionID(gs);
                    gs.setGameSessionID(sessionID);
                    gs.getPlayerOne().setToken(sessionID);
                    gs.getPlayerTwo().setToken(sessionID);
                    try {
                        gs.getClientOne().showMessage(sessionID, sessionID, 0);
                    } catch (final RemoteException ex) {
                        Logger.getLogger(BattleGame.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }

            final GameSession gs = new GameSession(player, players.get(player.getName()));
            final String id = updateSessionID(player, players.get(player.getName()));

            gs.updateActionTime();
        }
        return sessionID;
    }

    public boolean addPlayer(final String player, final IClientRMI client) {
        if (players.put(player, client) != null) {
            System.out.print(player + " added");
            try {
                client.showMessage("Added to server", player + " added to server player index", JOptionPane.INFORMATION_MESSAGE);
            } catch (final RemoteException ex) {
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

        players.keySet().parallelStream().filter(target -> !target.equals(sourceplayer)).forEach(target -> {
            try {
                final IClientRMI client = players.get(target);
                client.showMessage(title, text, modal);
            } catch (final RemoteException ex) {
                Logger.getLogger(BattleGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}
