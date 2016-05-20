/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.entities;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class Lobby implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer lobbyid;
    private Player attacker;
    private Player defender;
    private Board attackerBoard;
    private Board defenderBoard;
    private boolean hasTwoPlayers, isDeployed, attackersTurn;
    public ArrayList<Point> moves = new ArrayList<>();
    public ArrayList<String> chat = new ArrayList<>();

    
    public Lobby() {
        
    }
    
    public Lobby(final Integer lobbyid, final Player defender) {
        this.lobbyid = lobbyid;
        this.defender = defender;
        defenderBoard = new Board(this, defender);
    }

    public Lobby(final Integer lobbyid, final Player defender, final Player attacker) {
        this(lobbyid, defender);
        this.attacker = attacker;
        attackerBoard = new Board(this, attacker);
    }

    public void setDeployed() {
        isDeployed = true;
    }

    public Integer getLobbyid() {
        return lobbyid;
    }

    public Player getAttacker() {
        return attacker;
    }

    public Integer getAttackerid() {
        return attacker.getPlayerid();
    }

    public boolean setAttacker(final Player attacker) {
        if (this.attacker != null || attacker == null) {
            return false;
        }
        this.attacker = attacker;
        attackerBoard = new Board(this, attacker);
        return true;
    }

    public Player getDefender() {
        return defender;
    }

    public Integer getDefenderid() {
        return defender.getPlayerid();
    }

    public Board[] getBoards() {
        return new Board[]{attackerBoard, defenderBoard};
    }

    public Board getBoard(final int playerid) {
        if (playerid == getDefenderid()) {
            return defenderBoard;
        } else if (playerid == getAttackerid()) {
            return attackerBoard;
        } else {
            return null;
        }
    }

    public Board getAttackerBoard() {
        return attackerBoard;
    }

    public Board getDefenderBoard() {
        return defenderBoard;
    }

    public int getActiveId() {
        if (attackersTurn) {
            return getAttackerid();
        } else {
            return getDefenderid();
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += lobbyid != null ? lobbyid.hashCode() : 0;
        return hash;
    }

    @Override
    public boolean equals(final Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lobby)) {
            return false;
        }
        final Lobby other = (Lobby) object;
        return !(lobbyid == null && other.lobbyid != null || lobbyid != null && !lobbyid.equals(other.lobbyid));
    }

    @Override
    public String toString() {
        return "Lobby{" + "lobbyid=" + lobbyid + ", attacker=" + attacker + ", defender=" + defender + ", attackerBoard=" + attackerBoard + ", defenderBoard=" + defenderBoard + ", hasTwoPlayers=" + hasTwoPlayers + ", isDeployed=" + isDeployed + ", attackersTurn=" + attackersTurn + ", moves=" + moves + ", chat=" + chat + '}';
    }

}
