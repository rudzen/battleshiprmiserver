/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
