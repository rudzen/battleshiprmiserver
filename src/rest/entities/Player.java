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
        armor = 0;
        decoy = 0;
        weapon = 0;
        sonar = 0;
        point = 1000;
        loss = 0;
        won = 0;
        playername = "DefaultName";
        final Integer playerid1 = -1;
        playerid = playerid1;
        password = "1234";
        salt = "salt";
    }

    public Player(final Integer playerid) {
        this.playerid = playerid;
    }

    public Integer getPlayerid() {
        return playerid;
    }

    public void setPlayerid(final Integer playerid) {
        this.playerid = playerid;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(final String playername) {
        this.playername = playername;
    }

    public Integer getWon() {
        return won;
    }

    public void setWon(final Integer won) {
        this.won = won;
    }

    public Integer getLoss() {
        return loss;
    }

    public void setLoss(final Integer loss) {
        this.loss = loss;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(final Integer point) {
        this.point = point;
    }

    public Integer getWeapon() {
        return weapon;
    }

    public void setWeapon(final Integer weapon) {
        this.weapon = weapon;
    }

    public Integer getArmor() {
        return armor;
    }

    public void setArmor(final Integer armor) {
        this.armor = armor;
    }

    public Integer getDecoy() {
        return decoy;
    }

    public void setDecoy(final Integer decoy) {
        this.decoy = decoy;
    }

    public Integer getSonar() {
        return sonar;
    }

    public void setSonar(final Integer sonar) {
        this.sonar = sonar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(final String salt) {
        this.salt = salt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += playerid != null ? playerid.hashCode() : 0;
        return hash;
    }

    @Override
    public boolean equals(final Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Player)) {
            return false;
        }
        final Player other = (Player) object;
        return !(playerid == null && other.playerid != null || playerid != null && !playerid.equals(other.playerid));
    }

    @Override
    public String toString() {
        return "entity.Player[ playerid=" + playerid + " ]";
    }
    
}
