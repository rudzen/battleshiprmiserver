/*
  The MIT License

  Copyright 2016, 2017, 2018 Rudy Alex Kohn.

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
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
