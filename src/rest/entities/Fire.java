/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.entities;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author theis
 */
public class Fire implements Serializable {

    public static final String ERROR = "error";
    private static final long serialVersionUID = 1L;

    private int player;
    private Point fire;
    private String status;
    private String error;
    private ArrayList<JSONShip> ships;

    
    public Fire() {
        
    }
    
    /**
     *
     * @param player
     * @param fire
     * @param status
     * @param error
     * @param ships
     */
    public Fire(final int player, final Point fire, final String status, final String error, final ArrayList<JSONShip> ships) {
        this.player = player;
        this.fire = fire;
        this.status = status;
        this.error = error;
        this.ships = ships;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(final int player) {
        this.player = player;
    }

    public Point getFire() {
        return fire;
    }

    public void setFire(final Point fire) {
        this.fire = fire;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    public ArrayList<JSONShip> getShips() {
        return ships;
    }

    public void setShips(final ArrayList<JSONShip> ships) {
        this.ships = ships;
    }

}
