/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import java.io.Serializable;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author root
 */
public class Lobby implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer lobbyid;
    private Player attackerid;
    private Player deffenderid;
    private Collection<Board> boardCollection;

    public Lobby() {
    }

    public Lobby(Integer lobbyid) {
        this.lobbyid = lobbyid;
    }
    public Lobby(Integer lobbyid, Player deffender, Player attacker){
        this.lobbyid =lobbyid;
        this.deffenderid = deffender;
        this.attackerid = attacker;
    }

    public Integer getLobbyid() {
        return lobbyid;
    }

    public void setLobbyid(Integer lobbyid) {
        this.lobbyid = lobbyid;
    }

    public Player getAttackerid() {
        return attackerid;
    }

    public void setAttackerid(Player attackerid) {
        this.attackerid = attackerid;
    }

    public Player getDeffenderid() {
        return deffenderid;
    }

    public void setDeffenderid(Player deffenderid) {
        this.deffenderid = deffenderid;
    }

    @XmlTransient
    public Collection<Board> getBoardCollection() {
        return boardCollection;
    }

    public void setBoardCollection(Collection<Board> boardCollection) {
        this.boardCollection = boardCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lobbyid != null ? lobbyid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lobby)) {
            return false;
        }
        Lobby other = (Lobby) object;
        if ((this.lobbyid == null && other.lobbyid != null) || (this.lobbyid != null && !this.lobbyid.equals(other.lobbyid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Lobby[ lobbyid=" + lobbyid + " ]";
    }
    
}
