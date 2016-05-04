/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import java.awt.Point;
import java.util.ArrayList;
import rest.ShipList.Ship.JSONShip;

/**
 *
 * @author theis
 */
public class ShipList extends ArrayList<ShipList.Ship>{
    
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
    
    public String newShip(int x, int y, int type, boolean dir){
        return " "+this.size() + " " + this.add(new Ship(shipnames[type], shiplengths[type], x, y, dir));
    }
    
    public String fire(int x, int y){
        for(Ship s : this){
            if(s.fire(x,y)) return "succes"; // TODO
        }
        return "miss";
    }
    
    public String getJSON(){
        ArrayList<JSONShip> r = new ArrayList<>();
        this.stream().forEach((ship) -> {
            r.add(ship.j);
        });
        return "\"ships\":"+new Gson().toJson(r);
    }
    
    class Ship{
        String name;
        int length, x, y, hit = 0;
        boolean hor;
        JSONShip j;
        public Ship(String name, int length, int x, int y, boolean hor){
            this.name = name;
            this.length = length;
            this.x = x; this.y = y;
            this.hor = hor;
            j = toJSON();
        }
        
        public boolean fire(int x, int y){
            if(hor){
                if (this.y != y) return false;
                if(this.x > x  || this.x + this.length - 1 < x) return false;
                hit = hit | (1<<(x-this.x));
                return true;
            } else {
                if (this.x != x) return false;
                if(this.y > y  || this.y + this.length - 1 < y) return false;
                hit = hit | (1<<(y-this.y));
                return true;
            }
        }
        
        public boolean isDestroyed(){
            for(int i = 0; i < length; i++)
                if((hit&(1<<i)) == 0) return false;
            j = toJSON();
            return true;
        }
        class JSONShip{
            String shipname;
            Integer length, row, col;
            boolean horizontal;
            boolean isDestroyed;
            Point[] cordinates;
            JSONShip (String name, int length, boolean horizontal){
                this.shipname = name; this.length = length; this.horizontal = horizontal;
            }
            
        }
        public JSONShip toJSON(){
            JSONShip j = new JSONShip(name, length, hor);
            if(isDestroyed()){
                j.row = this.x;
                j.col = this.y;
                j.cordinates = this.getCords();
                j.isDestroyed = true;
            }
            return j;
        }
        protected Point[] getCords(){
            Point[] cordinates = new Point[length];
            if(hor) for(int i = 0; i<length; i++) cordinates[i] = new Point(x+i, y);
            else for(int i = 0; i<length; i++) cordinates[i] = new Point(x,y+i);
            return cordinates;
        }
            
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
}
