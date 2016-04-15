/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game.data.Player;

/**
 * The actual game.
 * @author rudz
 */
public class Game {

    private Player[] players = new Player[2];
    
    private volatile boolean isGameLost;
    
    public enum PLAYERS {
        PLAYER_ONE, PLAYER_TWO
    }
    
    public Game() { }
    
    public Game(final Player playerOne, final Player playerTwo) {
        players[0] = playerOne;
        players[1] = playerTwo;
    }
    
    
    
    public Player getPlayer(PLAYERS player) {
        return (player == PLAYERS.PLAYER_ONE) ? players[0] : players[1];
    }
    
    
    
    /* game functionality */
    
    /**
     * Determines if a ship is hit or not.
     * @param x The X
     * @param y The Y
     * @param board The board of the opponent
     * @return true if hit, false otherwise.
     */
    public boolean isHit(int x, int y, int[][] board) {
        return board[x][y] > 1;
    }
    
    /**
     * Will damage the ship if it is hit.
     * @param x The X
     * @param y The Y
     * @param board The board of the opponent
     * @return true if there is no more armour.
     */
    public boolean damageShip(int x, int y, int[][] board) {
        if (isHit(x, y, board)) {
            board[x][y]--;
        }
        return board[x][y] == 0;
    }
    
    /**
     * Determines if a specific ship is sunk.
     * @param x The X start
     * @param y The Y start
     * @param xLen the X length
     * @param yLen the Y length
     * @param board the board
     * @return true if sunk, otherwise false.
     */
    public boolean isShipSunk(int x, int y, int xLen, int yLen, int[][] board) {
        // TODO : Implement.
        return false;
    }

    

    /* getters & setters */

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public boolean isIsGameLost() {
        return isGameLost;
    }

    public void setIsGameLost(boolean isGameLost) {
        this.isGameLost = isGameLost;
    }
    
    
    
}
