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
import java.util.Objects;

/**
 * Represents a game session.<br>
 * Contains:<br>
 * <li>Players</li>
 * <li>Time for last activity</li>
 *
 * @author rudz
 */
public class GameSession {

    private Player playerOne;
    private Player playerTwo;

    private IClientListener clientOne;
    private IClientListener clientTwo;
    
    private String gameSessionID; // should be an unique string "hash" based on both players.

    private long timeCreated;
    private long lastAction;

    /* constructors */
    public GameSession(final Player playerOne) {
        this.playerOne = playerOne;
        timeCreated = System.currentTimeMillis();
        lastAction = timeCreated;
    }

    public GameSession(final Player playerOne, final Player playerTwo) {
        this(playerOne);
        this.playerTwo = playerTwo;
    }

    /* helper methods */
    public boolean isFull() {
        return playerOne != null && playerTwo != null;
    }

    public boolean isInSession(final Player player) {
        return playerOne.equals(player) || playerTwo.equals(player);
    }

    public Player getOtherPlayer(final Player player) {
        return playerOne.equals(player) ? playerTwo : playerOne;
    }

    public boolean isInSession(final IClientListener client) {
        return clientOne.equals(client) || clientTwo.equals(client);
    }
    
    public IClientListener getOtherPlayer(final IClientListener client) {
        return clientOne.equals(client) ? clientOne : clientTwo;
    }
    
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

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }
    
    public void setPlayerOne(final Player playerOne) {
        this.playerOne = playerOne;
    }

    public void setPlayerTwo(final Player playerTwo) {
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
}
