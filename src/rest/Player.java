/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author root
 */
@Entity
@Table(name = "player")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Player.findAll", query = "SELECT p FROM Player p"),
    @NamedQuery(name = "Player.findByPlayerid", query = "SELECT p FROM Player p WHERE p.playerid = :playerid"),
    @NamedQuery(name = "Player.findByPlayername", query = "SELECT p FROM Player p WHERE p.playername = :playername"),
    @NamedQuery(name = "Player.findByWon", query = "SELECT p FROM Player p WHERE p.won = :won"),
    @NamedQuery(name = "Player.findByLoss", query = "SELECT p FROM Player p WHERE p.loss = :loss"),
    @NamedQuery(name = "Player.findByPoint", query = "SELECT p FROM Player p WHERE p.point = :point"),
    @NamedQuery(name = "Player.findByWeapon", query = "SELECT p FROM Player p WHERE p.weapon = :weapon"),
    @NamedQuery(name = "Player.findByArmor", query = "SELECT p FROM Player p WHERE p.armor = :armor"),
    @NamedQuery(name = "Player.findByDecoy", query = "SELECT p FROM Player p WHERE p.decoy = :decoy"),
    @NamedQuery(name = "Player.findBySonar", query = "SELECT p FROM Player p WHERE p.sonar = :sonar"),
    @NamedQuery(name = "Player.findByPassword", query = "SELECT p FROM Player p WHERE p.password = :password"),
    @NamedQuery(name = "Player.findBySalt", query = "SELECT p FROM Player p WHERE p.salt = :salt")})
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "playerid")
    private Integer playerid;
    @Size(max = 45)
    @Column(name = "playername")
    private String playername;
    @Column(name = "won")
    private Integer won;
    @Column(name = "loss")
    private Integer loss;
    @Column(name = "point")
    private Integer point;
    @Column(name = "weapon")
    private Integer weapon;
    @Column(name = "armor")
    private Integer armor;
    @Column(name = "decoy")
    private Integer decoy;
    @Column(name = "sonar")
    private Integer sonar;
    @Size(max = 45)
    @Column(name = "password")
    private String password;
    @Size(max = 45)
    @Column(name = "salt")
    private String salt;
    /*
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attackerid")
    private Collection<Lobby> lobbyCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deffenderid")
    private Collection<Lobby> lobbyCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "playerid")
    private Collection<Board> boardCollection;
    */

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

    /*
    @XmlTransient
    public Collection<Lobby> getLobbyCollection() {
        return lobbyCollection;
    }

    public void setLobbyCollection(Collection<Lobby> lobbyCollection) {
        this.lobbyCollection = lobbyCollection;
    }

    @XmlTransient
    public Collection<Lobby> getLobbyCollection1() {
        return lobbyCollection1;
    }

    public void setLobbyCollection1(Collection<Lobby> lobbyCollection1) {
        this.lobbyCollection1 = lobbyCollection1;
    }

    @XmlTransient
    public Collection<Board> getBoardCollection() {
        return boardCollection;
    }

    public void setBoardCollection(Collection<Board> boardCollection) {
        this.boardCollection = boardCollection;
    }
    */
    
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
        if ((this.playerid == null && other.playerid != null) || (this.playerid != null && !this.playerid.equals(other.playerid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Player[ playerid=" + playerid + " ]";
    }
    
}
