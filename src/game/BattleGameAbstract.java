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

import interfaces.IClientListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author rudz
 */
public abstract class BattleGameAbstract {

    private final static long TIME_LIMIT = 1000000;
    private final static String TIME_LIMIT_MSG = "Your current game has been terminated due to age";

    /**
     * Contains the game sessions themselves, the game uses these to find
     * something valuable :-)
     */
    protected final Map<String, GameSession> sessions;

    protected BattleGameAbstract() {
        sessions = new ConcurrentHashMap<>();
    }

    protected String isInSession(final IClientListener client) {

        for (GameSession gs : sessions.values()) {
            if (!gs.isFull() && gs.isInSession(client)) {
                return gs.getGameSessionID();
            }
        }
        return null;
    }

    protected final String createSessionID(final IClientListener client) {
        return client.toString();
    }

    protected ArrayList<String> getFreePlayerNames() {
        final ArrayList<String> list = new ArrayList<>();
        for (GameSession gs : sessions.values()) {
            if (!gs.isFull()) {
                if (gs.getPlayerTwo() == null) {
                    list.add(gs.getPlayerTwo().toString());
                }
            }
        }
        return list;
    }

    protected void endOldSessions() {
        final long now = System.currentTimeMillis();
        for (GameSession gs : sessions.values()) {
            if (now - gs.getTimeCreated() > TIME_LIMIT) {
                try {
                    gs.getPlayerOne().showMessage(Messages.MSG_GAME_TERMINATED, Messages.TIME_LIMIT_MSG, JOptionPane.ERROR_MESSAGE);
                    gs.getPlayerTwo().showMessage(Messages.MSG_GAME_TERMINATED, Messages.TIME_LIMIT_MSG, JOptionPane.ERROR_MESSAGE);
                } catch (final RemoteException re) {
                    // whoops..
                }
                

            }
        }
    }

}
