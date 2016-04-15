/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import interfaces.IClientListener;
import java.rmi.RemoteException;
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
     * @param x
     * @param y
     * @param client 
     */
    public void shotFired(final int x, final int y, IClientListener client) {

        try {
            final String sessionID = isInSession(client);
            if (sessionID != null) {
                IClientListener otherPlayer = sessions.get(sessionID).getOtherPlayer(client);

                // TODO : Determine if the shot actually hit something here!!!
                boolean hit = true;
                otherPlayer.shotFired(x, y, hit);

                // TODO : If hit, determine if the ship is sunk and the name of the sunken ship.
                final String shipHit = hit ? "HIT SHIP" : "NO SHIT";

                client.showMessage("Shot...", hit ? "You hit the opponents " + shipHit : "You missed... looser.", hit ? JOptionPane.WARNING_MESSAGE : JOptionPane.ERROR_MESSAGE);

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
                        gs.getPlayerOne().showMessage("Session termineret af server.", "Der er g\u00E5et for lang tid..", 0);
                    }
                    if (gs.getPlayerTwo() != null) {
                        gs.getPlayerOne().showMessage("Session termineret af server.", "Der er g\u00E5et for lang tid..", 0);
                    }
                }
                sessions.remove(gs.getGameSessionID());
            }
        }
    }
    
    
    /**
     * Generate session from one single client.
     *
     * @param client The client to generate the session with
     * @return true if session was created, otherwise false.
     */
    public boolean createSession(final IClientListener client) {
        boolean returnValue;
        final String sessionID = createSessionID(client);
        if (!sessions.containsKey(sessionID)) {
            sessions.put(sessionID, new GameSession(client));
            returnValue = true;
        } else {
            returnValue = false;
        }
        return returnValue;
    }

    /**
     * Creates a session based on two players.
     * @param playerOne The first player in the session.
     * @param playerTwo The second player in the session
     * @return true if session was created, otherwise false.
     */
    public boolean createSession(final IClientListener playerOne, final IClientListener playerTwo) {
        boolean returnValue = createSession(playerOne);
        if (returnValue) {
            sessions.get(createSessionID(playerOne)).setPlayerTwo(playerTwo);
        }
        return returnValue;   
    }

    /**
     * Removes a session from the list.
     * @param sessionID The sessionID to remove.
     */
    private void removeSession(final String sessionID) {
        if (sessionID != null) {
            sessions.remove(sessionID);
        }
    }

}
