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
public class Fire implements Serializable{

    public static final String ERROR = "error";
    
    
    private static final long serialVersionUID = 5984944541713491612L;
    private int player;
    private Point fire;
    private String status;
    private String error;
    private ArrayList<JSONShip> ships;

    /**
     *
     * @param player
     * @param fire
     * @param status
     * @param error
     * @param ships
     */
    public Fire(int player, Point fire, String status, String error, ArrayList<JSONShip> ships) {
        this.player = player;
        this.fire = fire;
        this.status = status;
        this.error = error;
        this.ships = ships;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public Point getFire() {
        return fire;
    }

    public void setFire(Point fire) {
        this.fire = fire;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<JSONShip> getShips() {
        return ships;
    }

    public void setShips(ArrayList<JSONShip> ships) {
        this.ships = ships;
    }


    
}
