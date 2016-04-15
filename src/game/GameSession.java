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
    private IClientListener playerTwo;
    
    private Player p1;
    private Player p2;
    
    private String gameSessionID; // should be an unique string "hash" based on both players.
    
    private long timeCreated;
    
    /* constructors */
    
    public GameSession(final IClientListener playerOne) {    
        this.playerOne = playerOne;
        playerTwo = null;
    }
    
    public GameSession(final IClientListener playerOne, final IClientListener playerTwo) {
        this.playerOne = playerOne;
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
    
    // TODO : Add battleship logic here!!
    
    
}
