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

import game.data.Player;
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

    private IClientListener playerOne;
    private IClientListener playerTwo = null;

    private Player p1;
    private Player p2;

    private String gameSessionID; // should be an unique string "hash" based on both players.

    private long timeCreated;

    /* constructors */
    public GameSession(final IClientListener playerOne) {
        this.playerOne = playerOne;
        timeCreated = System.currentTimeMillis();
    }

    public GameSession(final IClientListener playerOne, final IClientListener playerTwo) {
        this(playerOne);
        this.playerTwo = playerTwo;
    }

    /* helper methods */
    public void setTimeCreated(final long newTime) {
        timeCreated = newTime;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public boolean isFull() {
        return playerTwo != null && playerOne != null;
    }

    public boolean isInSession(final IClientListener client) {
        return playerOne.equals(client) || playerTwo.equals(client);
    }

    public IClientListener getOtherPlayer(final IClientListener client) {
        return (playerOne.equals(client)) ? playerTwo : playerOne;
    }

    /* getters & setters */
    public void setGameSessionID(final String newID) {
        gameSessionID = newID;
    }

    public String getGameSessionID() {
        return gameSessionID;
    }

    public IClientListener getPlayerOne() {
        return playerOne;
    }

    public IClientListener getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerOne(final IClientListener playerOne) {
        this.playerOne = playerOne;
    }

    public void setPlayerTwo(final IClientListener playerTwo) {
        this.playerTwo = playerTwo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.playerOne);
        hash = 79 * hash + Objects.hashCode(this.playerTwo);
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
        if (!Objects.equals(this.p1, other.p1)) {
            return false;
        }
        if (!Objects.equals(this.p2, other.p2)) {
            return false;
        }
        return true;
    }

    // TODO : Add battleship logic here!!
}
