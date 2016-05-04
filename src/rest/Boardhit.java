/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import java.io.Serializable;

/**
 *
 * @author root
 */
public class Boardhit implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer rownum;
    private Integer colnum;
    private Integer state;
    private Integer boardhitid;
    private Integer boardid;

    public Boardhit() {
    }

    public Boardhit(Integer boardhitid) {
        this.boardhitid = boardhitid;
    }

    public Integer getRownum() {
        return rownum;
    }

    public void setRownum(Integer rownum) {
        this.rownum = rownum;
    }

    public Integer getColnum() {
        return colnum;
    }

    public void setColnum(Integer colnum) {
        this.colnum = colnum;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getBoardhitid() {
        return boardhitid;
    }

    public void setBoardhitid(Integer boardhitid) {
        this.boardhitid = boardhitid;
    }

    public Integer getBoardid() {
        return boardid;
    }

    public void setBoardid(int boardid) {
        this.boardid = boardid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (boardhitid != null ? boardhitid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Boardhit)) {
            return false;
        }
        Boardhit other = (Boardhit) object;
        if ((this.boardhitid == null && other.boardhitid != null) || (this.boardhitid != null && !this.boardhitid.equals(other.boardhitid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Boardhit[ boardhitid=" + boardhitid + " ]";
    }
    
}
