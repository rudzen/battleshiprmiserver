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
