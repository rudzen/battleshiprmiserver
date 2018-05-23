/*
  The MIT License

  Copyright 2016, 2017, 2018 Rudy Alex Kohn.

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
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
