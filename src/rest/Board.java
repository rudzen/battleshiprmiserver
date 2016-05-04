/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import java.io.Serializable;
import java.util.Collection;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author root
 */
public class Board implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer boardid;
    private Lobby lobbyid;
    private Player playerid;
    private Collection<Boardhit> boardhitCollection;

    public Board() {
    }

    public Board(Integer boardid) {
        this.boardid = boardid;
    }
    public Board(Integer boardid,Lobby lobby ,Player player){
        this.boardid = boardid;
        this.lobbyid = lobby;
        this.playerid = player;
    }

    public Integer getBoardid() {
        return boardid;
    }

    public void setBoardid(Integer boardid) {
        this.boardid = boardid;
    }

    public Lobby getLobbyid() {
        return lobbyid;
    }

    public void setLobbyid(Lobby lobbyid) {
        this.lobbyid = lobbyid;
    }

    public Player getPlayerid() {
        return playerid;
    }

    public void setPlayerid(Player playerid) {
        this.playerid = playerid;
    }

    @XmlTransient
    public Collection<Boardhit> getBoardhitCollection() {
        return boardhitCollection;
    }

    public void setBoardhitCollection(Collection<Boardhit> boardhitCollection) {
        this.boardhitCollection = boardhitCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (boardid != null ? boardid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Board)) {
            return false;
        }
        Board other = (Board) object;
        if ((this.boardid == null && other.boardid != null) || (this.boardid != null && !this.boardid.equals(other.boardid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Board[ boardid=" + boardid + " ]";
    }
    
}
