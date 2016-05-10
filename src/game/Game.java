///*
// * The MIT License
// *
// * Copyright 2016 Rudy Alex Kohn <s133235@student.dtu.dk>.
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in
// * all copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// * THE SOFTWARE.
// */
//package game;
//
//import dataobjects.Player;
//import interfaces.IShip;
//
///**
// * The actual game.<br>
// * Every game session on the server has one instance of this class active,
// * as long as the session exists.
// * @author rudz
// */
//public class Game {
//
//    private Player[] players = new Player[2];
//    
//    private volatile boolean isGameLost;
//    
//    public enum PLAYERS {
//        PLAYER_ONE, PLAYER_TWO
//    }
//    
//    public Game() { }
//    
//    public Game(final Player playerOne, final Player playerTwo) {
//        players[0] = playerOne;
//        players[1] = playerTwo;
//    }
//    
//    public Player getPlayer(PLAYERS which) {
//        return (which == PLAYERS.PLAYER_ONE) ? players[0] : players[1];
//    }
//    
//    public void setPlayer(final PLAYERS which, final Player player) {
//        players[which == PLAYERS.PLAYER_ONE ? 0 : 1] = player;
//    }
//    
//    /* game functionality */
//    
//    /**
//     * Determines if a ship is hit or not.
//     * @param x The X
//     * @param y The Y
//     * @param player
//     * @return true if hit, false otherwise.
//     */
//    public boolean isHit(final int x, final int y, final Player player) {
//        boolean isHit = false;
//        for (IShip ship : player.getShips()) {
//            if (ship.isHit(x, y)) {
//                isHit = true;
//                break;
//            }
//        }
//        return isHit;
//    }
//    
//    /**
//     * Will damage the ship if it is hit.
//     * @param x The X
//     * @param y The Y
//     * @param player
//     * @return true if there is no more armour.
//     */
//    public boolean damageShip(int x, int y, final Player player) {
//        if (isHit(x, y, player)) {
//            player.boardHit(x, y);
//        }
//        return player.getBoard()[x][y] == 0;
//    }
//    
//    /**
//     * Determines if a specific ship is sunk.
//     * @param x The X start
//     * @param y The Y start
//     * @param xLen the X length
//     * @param yLen the Y length
//     * @param player The player who is being shot at.
//     * @return true if sunk, otherwise false.
//     */
//    public boolean isShipSunk(int x, int y, int xLen, int yLen, final Player player) {
//        
//        // TODO : Re-code
//        
//        for (int i = 0; i < 5; i++) {
//        }
//        
//        
//        // TODO : Implement.
//        return false;
//    }
//    
//    
//    
//    
//    
//    
//
//    /* getters & setters */
//
//    public Player[] getPlayers() {
//        return players;
//    }
//
//    public void setPlayers(final Player[] players) {
//        this.players = players;
//    }
//
//    public boolean isIsGameLost() {
//        return isGameLost;
//    }
//
//    public void setIsGameLost(final boolean isGameLost) {
//        this.isGameLost = isGameLost;
//    }
//    
//    
//    
//}
