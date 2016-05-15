/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.entities;

import java.io.Serializable;

public class Player implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer playerid;
    private String playername;
    private Integer won;
    private Integer loss;
    private Integer point;
    private Integer weapon;
    private Integer armor;
    private Integer decoy;
    private Integer sonar;
    private String password;
    private String salt;
    
    
    public Player() {
        setArmor(0);
        setDecoy(0);
        setWeapon(0);
        setSonar(0);
        setPoint(1000);
        setLoss(0);
        setWon(0);
        setPlayername("DefaultName");
        setPlayerid(-1);
        setPassword("1234");
        setSalt("salt");
    }

    public Player(Integer playerid) {
        this.playerid = playerid;
    }

    public Integer getPlayerid() {
        return playerid;
    }

    public void setPlayerid(Integer playerid) {
        this.playerid = playerid;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public Integer getWon() {
        return won;
    }

    public void setWon(Integer won) {
        this.won = won;
    }

    public Integer getLoss() {
        return loss;
    }

    public void setLoss(Integer loss) {
        this.loss = loss;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getWeapon() {
        return weapon;
    }

    public void setWeapon(Integer weapon) {
        this.weapon = weapon;
    }

    public Integer getArmor() {
        return armor;
    }

    public void setArmor(Integer armor) {
        this.armor = armor;
    }

    public Integer getDecoy() {
        return decoy;
    }

    public void setDecoy(Integer decoy) {
        this.decoy = decoy;
    }

    public Integer getSonar() {
        return sonar;
    }

    public void setSonar(Integer sonar) {
        this.sonar = sonar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (playerid != null ? playerid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Player)) {
            return false;
        }
        Player other = (Player) object;
        return !((this.playerid == null && other.playerid != null) || (this.playerid != null && !this.playerid.equals(other.playerid)));
    }

    @Override
    public String toString() {
        return "entity.Player[ playerid=" + playerid + " ]";
    }
    
}
