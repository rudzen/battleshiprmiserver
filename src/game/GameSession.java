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
import interfaces.IClientListener;
import java.util.Objects;

/**
 * Represents a game session.<br>
 * Contains:<br>
 * <li>Players</li>
 * <li>Time for last activity</li>
 *
 * Uses :
 * http://www.twmacinta.com/myjava/fast_md5.php
 * 
 * @author rudz
 */
public class GameSession {

    /* name of player one in this session */
    private String playerOne;
    
    /* name of player two in this session */
    private String playerTwo;

    /* player one's client interface */
    private IClientListener clientOne;
    
    /* player two's client interface */
    private IClientListener clientTwo;

    /* the game session ID - this is a MD5 hash (very good for the purpose and very fast!) */
    private String gameSessionID;

    /* when the session was created */
    private long timeCreated;
    
    /* when the last action was performed */
    private long lastAction;

    /* constructor for one player */
    public GameSession(final String playerOne, final IClientListener clientOne) {
        this.playerOne = playerOne;
        this.clientOne = clientOne;
        timeCreated = System.currentTimeMillis();
        lastAction = timeCreated;
    }

    /* constructor for two players */
    public GameSession(final String playerOne, final IClientListener clientOne, final String playerTwo, final IClientListener clientTwo) {
        this(playerOne, clientOne);
        this.playerTwo = playerTwo;
        this.clientTwo = clientTwo;
    }

    /* helper methods */
    
    /**
     * Determine if this session is occupied by two players.
     * @return true if two players exists, otherwise false
     */
    public boolean isFull() {
        updateActionTime();
        return playerOne != null && playerTwo != null;
    }

    /**
     * Determine if a player exists in this session
     * @param player The player's name to check for
     * @return true if the player parsed exists, otherwise false
     */
    public boolean isInSession(final String player) {
        updateActionTime();
        return playerOne.equals(player) || playerTwo.equals(player);
    }

    /**
     * Determine if a client exists in this session
     * @param client The client interface to check for
     * @return true if exists, otherwise false
     */
    public boolean isInSession(final IClientListener client) {
        updateActionTime();
        return clientOne.equals(client) || clientTwo.equals(client);
    }

    /**
     * Retrieves the other player based on the parsed player
     * @param player The "player" requesting the opponent
     * @return The other players name
     */
    public String getOtherPlayer(final String player) {
        updateActionTime();
        return playerOne.equals(player) ? playerTwo : playerOne;
    }

    /**
     * Retrieves the other client based on parsed client interface
     * @param client The client interface requesting the opponent
     * @return The other players client interface
     */
    public IClientListener getOtherPlayer(final IClientListener client) {
        updateActionTime();
        return clientOne.equals(client) ? clientTwo : clientOne;
    }

    /**
     * Updates the last known action time index.<br>
     * This function is called from the helper methods when they are used.
     */
    public void updateActionTime() {
        lastAction = System.currentTimeMillis();
    }

    /* getters & setters */
    public long getLastAction() {
        return lastAction;
    }

    public void setLastAction(long lastAction) {
        this.lastAction = lastAction;
    }

    public void setTimeCreated(final long newTime) {
        timeCreated = newTime;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setGameSessionID(final String newID) {
        gameSessionID = newID;
    }

    public String getGameSessionID() {
        return gameSessionID;
    }

    public String getPlayerOne() {
        return playerOne;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerOne(final String playerOne) {
        this.playerOne = playerOne;
    }

    public void setPlayerTwo(final String playerTwo) {
        this.playerTwo = playerTwo;
    }

    public IClientListener getClientOne() {
        return clientOne;
    }

    public void setClientOne(IClientListener clientOne) {
        this.clientOne = clientOne;
    }

    public IClientListener getClientTwo() {
        return clientTwo;
    }

    public void setClientTwo(IClientListener clientTwo) {
        this.clientTwo = clientTwo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.playerOne);
        hash = 79 * hash + Objects.hashCode(this.playerTwo);
        hash = 79 * hash + Objects.hashCode(this.clientOne);
        hash = 79 * hash + Objects.hashCode(this.clientTwo);
        hash = 79 * hash + (int) (this.timeCreated ^ (this.timeCreated >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameSession other = (GameSession) obj;
        if (this.timeCreated != other.timeCreated) {
            return false;
        }
        if (!Objects.equals(this.gameSessionID, other.gameSessionID)) {
            return false;
        }
        if (!Objects.equals(this.playerOne, other.playerOne)) {
            return false;
        }
        if (!Objects.equals(this.playerTwo, other.playerTwo)) {
            return false;
        }
        if (!Objects.equals(this.clientOne, other.clientOne)) {
            return false;
        }
        if (!Objects.equals(this.clientTwo, other.clientTwo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GameSession{" + "playerOne=" + playerOne + ", playerTwo=" + playerTwo + ", clientOne=" + clientOne + ", clientTwo=" + clientTwo + ", gameSessionID=" + gameSessionID + ", timeCreated=" + timeCreated + ", lastAction=" + lastAction + '}';
    }
    
}
