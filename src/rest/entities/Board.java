/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.entities;

import java.io.Serializable;

public class Board implements Serializable {

    private static final long serialVersionUID = 1L;


    private int lobbyid;
    private int playerid;
    private final int[][] hits = new int[10][10];

    public Board() {
        
    }
    
    public Board(final Lobby lobby, final Player player) {
        lobbyid = lobby.getLobbyid();
        playerid = player.getPlayerid();
    }

    public int getLobbyid() {
        return lobbyid;
    }

    public int getPlayerid() {
        return playerid;
    }

    public int[][] gethits() {
        return hits.clone();
    }

    public int getHit(final int col, final int row) {
        return hits[col][row];
    }

    public boolean setHit(final int col, final int row, final int state) {
        if (hits[col][row] >= state) {
            return false;
        }
        hits[col][row] = state;
        return true;
    }
}
