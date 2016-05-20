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

import com.twmacinta.util.MD5;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;

import dataobjects.Player;
import interfaces.IClientListener;

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
    protected final ConcurrentHashMap<String, GameSession> sessions; // the game session..

    /**
     * The players mapping for the server. K = Player name V = Player's client
     * interface for callback
     */
    protected final ConcurrentHashMap<String, IClientListener> players; // player's name + the players client interface

    /**
     * The md5 object used to calculate sessionIDs with
     */
    private final MD5 md5;

    /**
     * Lock object for md5 functionality (don't wanna mess with this one - it's
     * VITAL!) This is mainly because the SAME md5 object is used in several
     * methods.
     */
    private final ReentrantLock md5_lock;

    protected BattleGameAbstract(final ConcurrentHashMap<String, IClientListener> players, final ConcurrentHashMap<String, GameSession> sessions) {
        this.sessions = sessions;
        this.players = players; 
        md5_lock = new ReentrantLock(true);
        MD5.initNativeLibrary(true);
        md5 = new MD5();
    }

    protected String isInSession(final IClientListener client) {

        for (final GameSession gs : sessions.values()) {
            if (!gs.isFull() && gs.isInSession(client)) {
                return gs.getGameSessionID();
            }
        }
        return null;
    }

    protected String isInSession(final Player player) {
        for (final GameSession gs : sessions.values()) {
            if (gs.isInSession(player)) {
                return gs.getGameSessionID();
            }
        }
        return null;
    }

    protected ArrayList<String> getFreePlayerNames() {
        final ArrayList<String> list = new ArrayList<>();
        sessions.values().parallelStream().filter(gs -> (!gs.isFull())).forEach(gs -> {
            list.add(gs.getPlayerOne().getName());
        });
        return list;
    }

    protected void endOldSessions() {

        final long now = System.currentTimeMillis();
        sessions.values().parallelStream().filter(gs -> (now - gs.getTimeCreated() > TIME_LIMIT)).forEach(gs -> {
            try {
                gs.getClientOne().showMessage(Messages.MSG_GAME_TERMINATED, Messages.TIME_LIMIT_MSG, JOptionPane.ERROR_MESSAGE);
                gs.getClientOne().showMessage(Messages.MSG_GAME_TERMINATED, Messages.TIME_LIMIT_MSG, JOptionPane.ERROR_MESSAGE);
            } catch (final RemoteException re) {
                // whoops..
            }
        });
    }

    /**
     * Generate a sessionID based on the parsed objects
     *
     * @param player The player name
     * @param client The players client interface callback
     * @return The new session ID
     */
    public String updateSessionID(final Player player, final IClientListener client) {
        md5_lock.lock();

        md5.Init();
        md5.Update(player.toString());
        md5.Update(client.toString());
        md5.Update(Long.toOctalString(System.currentTimeMillis()));

        /* try/catch magic to unlock */
        String md = null;
        try {
            md = md5.asHex();
        } catch (final Exception e) {
            // muahaha
        } finally {
            md5_lock.unlock();
        }
        return md;
    }

    public String updateSessionID(final String playerOne, final IClientListener clientOne, final String playerTwo, final IClientListener clientTwo) {
        md5_lock.lock();

        md5.Init();
        md5.Update(playerOne);
        md5.Update(clientOne.toString());
        md5.Update(playerTwo);
        md5.Update(clientTwo.toString());
        md5.Update(Long.toOctalString(System.currentTimeMillis()));

        /* try/catch magic to unlock */
        String md = null;
        try {
            md = md5.asHex();
        } catch (final Exception e) {
            // muahaha
        } finally {
            md5_lock.unlock();
        }
        return md;
    }
    
    public String updateSessionID(final GameSession gs) {
        if (gs.getClientTwo() == null || gs.getPlayerTwo() == null) {
            return updateSessionID(gs.getPlayerOne(), gs.getClientOne());
        }
        return updateSessionID(gs.getPlayerOne().getName(), gs.getClientOne(), gs.getPlayerTwo().getName(), gs.getClientTwo());
    }
    
    
    
    public Map<String, GameSession> getSessions() {
        return sessions;
    }

    public Map<String, IClientListener> getPlayers() {
        return players;
    }
}
