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
    private int[][] hits = new int[10][10];

    public Board() {
        
    }
    
    public Board(Lobby lobby, Player player) {
        this.lobbyid = lobby.getLobbyid();
        this.playerid = player.getPlayerid();
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

    public int getHit(int col, int row) {
        return hits[col][row];
    }

    public boolean setHit(int col, int row, int state) {
        if (hits[col][row] >= state) {
            return false;
        }
        hits[col][row] = state;
        return true;
    }
}
