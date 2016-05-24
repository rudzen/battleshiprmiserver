/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.entities;

import com.google.gson.Gson;
import java.util.ArrayList;

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
    private static final long serialVersionUID = 1L;
    protected boolean deployed;
    
    public ShipList(){
        super();
    }
    
    public boolean isDeployed(){
        return deployed;
    }
    
    public String newShip(final int x, final int y, final int type, final boolean dir){
        return " "+ size() + " " + add(new Ship(shipnames[type], shiplengths[type], x, y, dir));
    }
    
    public String getJSON(){
        final ArrayList<JSONShip> r = new ArrayList<>();
        parallelStream().forEach(ship -> {
            r.add(ship.j);
        });
        return "\"ships\":"+new Gson().toJson(r);
    }
    
    @Override
    public boolean add(final Ship e){
        if(size() > 5) return false;
        else if(size() == 4) deployed = true;
        return super.add(e);
    }
    
    @Override
    public void add(final int i, final Ship e){
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
