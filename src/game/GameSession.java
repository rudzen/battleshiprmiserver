/*
 * The MIT License
 *
 * Copyright 2016 Rudy Alex Kohn (s133235@student.dtu.dk).
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

import java.util.Objects;

import dataobjects.Player;
import interfaces.IClientRMI;

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
    private Player playerOne;
    
    /* name of player two in this session */
    private Player playerTwo;

    /* player one's client interface */
    private IClientRMI clientOne;
    
    /* player two's client interface */
    private IClientRMI clientTwo;

    /* the game session ID - this is a MD5 hash (very good for the purpose and very fast!) */
    private String gameSessionID;

    /* when the session was created */
    private long timeCreated;
    
    /* when the last action was performed */
    private long lastAction;

    /* is player one ready ? */
    private boolean playerOneReady;
    
    /* is player two ready ? */
    private boolean playerTwoReady;
    
    /* lobby id from database */
    private int lobbyID;
    
    /* active id from database */
    private int activeID;
    
    /* constructor for one player */
    public GameSession(final Player playerOne, final IClientRMI clientOne) {
        this.playerOne = playerOne;
        this.clientOne = clientOne;
        timeCreated = System.currentTimeMillis();
        lastAction = timeCreated;
    }

    /* constructor for two players */
    public GameSession(final Player playerOne, final IClientRMI clientOne, final Player playerTwo, final IClientRMI clientTwo) {
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
    public boolean isInSession(final Player player) {
        updateActionTime();
        return playerOne.getName().equals(player.getName()) || playerTwo.getName().equals(player.getName());
    }

    /**
     * Determine if a client exists in this session
     * @param client The client interface to check for
     * @return true if exists, otherwise false
     */
    public boolean isInSession(final IClientRMI client) {
        updateActionTime();
        return clientOne.equals(client) || clientTwo.equals(client);
    }

    /**
     * Retrieves the other player based on the parsed player
     * @param player The "player" requesting the opponent
     * @return The other players name
     */
    public Player getOtherPlayer(final Player player) {
        updateActionTime();
        return playerOne.getName().equals(player.getName()) ? playerTwo : playerOne;
    }

    /**
     * Retrieves the other client based on parsed client interface
     * @param client The client interface requesting the opponent
     * @return The other players client interface
     * @deprecated 
     */
    public IClientRMI getOtherPlayer(final IClientRMI client) {
        updateActionTime();
        return clientOne.equals(client) ? clientTwo : clientOne;
    }

    /**
     * Retrieves the other client based on parsed player name
     * @param playerName The player requesting the opponent
     * @return The other players client interface.
     */
    public IClientRMI getOtherPlayer(final String playerName) {
        updateActionTime();
        return playerOne.getName().equals(playerName) ? clientTwo : clientOne;
    }
    
    /**
     * Updates the last known action time index.<br>
     * This function is called from the helper methods when they are used.
     */
    public void updateActionTime() {
        lastAction = System.currentTimeMillis();
    }

    public void setPlayerOneAll(final Player playerOne, final IClientRMI clientOne) {
        this.playerOne = playerOne;
        this.clientOne = clientOne;
    }

    public void setPlayerTwoAll(final Player playerTwo, final IClientRMI clientTwo) {
        this.playerTwo = playerTwo;
        this.clientTwo = clientTwo;
    }
    
    public Player getPlayerByName(final String playerName) {
        return playerName.equals(playerOne.getName()) ? playerOne : playerTwo;
    }
    
    public IClientRMI getClientByName(final String playerName) {
        return playerName.equals(playerOne.getName()) ? clientOne : clientTwo;
    }
    
    /**
     * Determine if both players in session are ready to play.
     * @return true if both players are ready to play, otherwise false.
     */
    public boolean isGameReady() {
        return playerOneReady && playerTwoReady;
    }
    
    /* getters & setters */

    public int getActiveID() {
        return activeID;
    }

    public void setActiveID(final int activeID) {
        this.activeID = activeID;
    }
    
    public int getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(final int lobbyID) {
        this.lobbyID = lobbyID;
    }

    public boolean isPlayerOneReady() {
        return playerOneReady;
    }

    public void setPlayerOneReady(final boolean playerOneReady) {
        this.playerOneReady = playerOneReady;
    }

    public boolean isPlayerTwoReady() {
        return playerTwoReady;
    }

    public void setPlayerTwoReady(final boolean playerTwoReady) {
        this.playerTwoReady = playerTwoReady;
    }
    
    public long getLastAction() {
        return lastAction;
    }

    public void setLastAction(final long lastAction) {
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

    public IClientRMI getClientOne() {
        return clientOne;
    }

    public void setClientOne(final IClientRMI clientOne) {
        this.clientOne = clientOne;
    }

    public IClientRMI getClientTwo() {
        return clientTwo;
    }

    public void setClientTwo(final IClientRMI clientTwo) {
        this.clientTwo = clientTwo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(playerOne);
        hash = 97 * hash + Objects.hashCode(playerTwo);
        hash = 97 * hash + Objects.hashCode(clientOne);
        hash = 97 * hash + Objects.hashCode(clientTwo);
        hash = 97 * hash + Objects.hashCode(gameSessionID);
        hash = 97 * hash + (int) (timeCreated ^ timeCreated >>> 32);
        hash = 97 * hash + (playerOneReady ? 1 : 0);
        hash = 97 * hash + (playerTwoReady ? 1 : 0);
        hash = 97 * hash + lobbyID;
        hash = 97 * hash + activeID;
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
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
        if (timeCreated != other.timeCreated) {
            return false;
        }
        if (!Objects.equals(gameSessionID, other.gameSessionID)) {
            return false;
        }
        if (!Objects.equals(playerOne, other.playerOne)) {
            return false;
        }
        if (!Objects.equals(playerTwo, other.playerTwo)) {
            return false;
        }
        if (!Objects.equals(clientOne, other.clientOne)) {
            return false;
        }
        if (lobbyID != other.lobbyID) {
            return false;
        }
        if (activeID != other.activeID) {
            return false;
        }
        return Objects.equals(clientTwo, other.clientTwo);
    }

    @Override
    public String toString() {
        return "GameSession {" +
                "lobbyID=" + lobbyID +
                "\nactiveID=" + activeID +
                "\nplayerOne=" + playerOne +
                "\nplayerTwo=" + playerTwo +
                "\nclientOne=" + clientOne +
                "\nclientTwo=" + clientTwo +
                "\ngameSessionID=" + gameSessionID +
                "\ntimeCreated=" + timeCreated +
                "\nlastAction=" + lastAction +
                "\n}";
    }
}
