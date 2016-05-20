/*
 * The MIT License
 *
 * Copyright 2016 Rudy Alex Kohn <s133235@student.dtu.dk>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package rest.entities;

import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author Rudy Alex Kohn <s133235@student.dtu.dk>
 */
public class Ship implements Serializable {

    private static final long serialVersionUID = 1L;

    public String name;
    public int length;
    public int x;
    public int y;
    public int hit;
    boolean hor;
    JSONShip j;

    public Ship() {
        
    }
    
    public Ship(final String name, final int length, final int x, final int y, final boolean hor) {
        this.name = name;
        this.length = length;
        this.x = x;
        this.y = y;
        this.hor = hor;
        j = new JSONShip(name, length);
    }

    public boolean fire(final int x, final int y) {
        if (hor) {
            if (this.y != y) {
                return false;
            }
            if (this.x > x || this.x + length - 1 < x) {
                return false;
            }
            hit |= 1 << x - this.x;
            return true;
        } else {
            if (this.x != x) {
                return false;
            }
            if (this.y > y || this.y + length - 1 < y) {
                return false;
            }
            hit |= 1 << y - this.y;
            return true;
        }
    }

    public boolean isDestroyed() {
        for (int i = 0; i < length; i++) {
            if ((hit & 1 << i) == 0) {
                return false;
            }
        }
        j.isDestroyed = true;
        j.horizontal = hor;
        j.row = x;
        j.col = y;
        j.cordinates = getCords();
        return true;
    }

    public Point[] getCords() {
        if (!j.isDestroyed) {
            return null;
        }
        final Point[] cordinates = new Point[length];
        if (hor) {
            for (int i = 0; i < length; i++) {
                cordinates[i] = new Point(x + i, y);
            }
        } else {
            for (int i = 0; i < length; i++) {
                cordinates[i] = new Point(x, y + i);
            }
        }
        return cordinates;
    }

}
