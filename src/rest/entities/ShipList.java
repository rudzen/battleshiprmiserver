/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.entities;

import com.google.gson.Gson;
import java.util.ArrayList;
import rest.entities.JSONShip;

/**
 *
 * @author theis
 */
public class ShipList extends ArrayList<Ship>{
    
    public final static String[] shipnames = {
        "Aircraft carrier", "Battleship", "Submarine", "Destroyer", "Patrol boat"
    };
    public final static int[] shiplengths = {
        5, 4, 3, 3, 2
    };
    protected boolean deployed = false;
    
    public ShipList(){
        super();
    }
    
    public boolean isDeployed(){
        return deployed;
    }
    
    public String newShip(int x, int y, int type, boolean dir){
        return " "+this.size() + " " + this.add(new Ship(shipnames[type], shiplengths[type], x, y, dir));
    }
    
    public String getJSON(){
        ArrayList<JSONShip> r = new ArrayList<>();
        this.stream().forEach((ship) -> {
            r.add(ship.j);
        });
        return "\"ships\":"+new Gson().toJson(r);
    }
    
    @Override
    public boolean add(Ship e){
        if(super.size() > 5) return false;
        else if(super.size() == 4) this.deployed = true;
        return super.add(e);
    }
    
    @Override
    public void add(int i, Ship e){
        add(e);
    }
    
    @Override
    public void clear(){
        if(deployed) return;
        else super.clear();
    }
    
//    public class Point {
//        public int x, y;
//        public Point(int x, int y){
//            this.x = x;
//            this.y = y;
//        }
//    }
}
