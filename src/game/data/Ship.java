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
package game.data;

import java.awt.Point;
import java.io.Serializable;

/**
 * Basic ship class, it defines what ship type it is, and what else there is to
 * know about it.
 *
 * @author rudz
 */
public class Ship implements Serializable {

    private static final long serialVersionUID = -5311711410232026986L;

    /**
     * The type of ship. Patrol = 2 length Destroyer & Submarine = 3 length
     * Cruiser = 4 length Carrier = 5 length
     */
    public enum TYPE {
        PATROL, DESTROYER, SUBMARINE, CRUISER, CARRIER
    }

    /**
     * The direction for which the ship is located, up-down or right-left
     */
    public enum DIRECTION {
        HORIZONTAL, VERTICAL;
    }

    private TYPE type;
    private DIRECTION direction;

    public enum UPGRADES {
        ARMOR, SONAR, POWER
    }

    private Upgrades upgrades;

    /**
     * Points for ship location locStart = start locEnd = end
     */
    private PPoint locStart;
    private PPoint locEnd;

    /**
     * The length of the ship
     */
    private int length;

    /**
     * The life of the ship. This is modified by armor upgrade(s).
     */
    private int life;

    /**
     * If the ship has _any_ type of upgrade, this is set to true
     */
    private boolean hasUpgrade;

    
    private int[] hits;
    
    public Ship() {
    }

    /**
     * Constructor without upgrades defined.
     * @param x The X start location
     * @param y The Y start location
     * @param type The type of the ship
     * @param direction The direction of placement
     */
    public Ship(final int x, final int y, final TYPE type, final DIRECTION direction) {
        locStart = new PPoint(x, y);
        this.type = type;
        this.direction = direction;
        length = getLen(type);
        life = length;
        hits = new int[length];
    }

    /**
     * Constructor which contain upgrade(s)
     * @param x The X start location
     * @param y The Y start location
     * @param type The type of the ship
     * @param direction The placement direction
     * @param upgrades The upgrades for the ship.
     */
    public Ship(final int x, final int y, final TYPE type, final DIRECTION direction, final Upgrades upgrades) {
        this(x, y, type, direction);
        this.upgrades = upgrades;
        if (upgrades.armor > 0) { // add armor to life (mef mef)
            life += upgrades.armor;
        }
    }

    /* upgrade helper functions */
    
    /**
     * Adds an upgrade to the ship (if bought by the player)
     * @param upgradeType The upgrade type.
     */
    public void addUpgrade(final UPGRADES upgradeType) {
        upgrades.addUpgrade(upgradeType);
    }

    /**
     * Removes an upgrade from the ship (wahh, what why?)
     * @param upgradeType The upgrade type.
     */
    public void removeUpgrade(final UPGRADES upgradeType) {
        upgrades.removeUpgrade(upgradeType);
    }

    /* helper methods */
    
    /**
     * Is the ship dead?
     * @return true if ship if dead, otherwise false
     */
    public boolean isDead() {
        return life == 0;
    }
    
    /**
     * Hit the ship!! (argh!)
     */
    private void hit() {
        life--;
    }

    
    public boolean isHit(int x, int y) {
        return isHit((byte) x, (byte) y);
    }
    
    public boolean isHit(byte x, byte y) {
        /* check off the bat for direct start & end hit first! */
        if ((x == locStart.getX() && y == locStart.getY()) ||
             (x == locEnd.getX()   && y == locEnd.getY())) {
            hit();
            return true;
        }
        
        // TODO :: WHAT WHAT!!!
        if (direction == DIRECTION.HORIZONTAL) {
            if (x + locStart.getX() < locEnd.getX()) {
                
            }
        } else {
            if (y + locStart.getY() < locEnd.getX()) {
                
            }
        }
        return false;
    }

    private static PPoint setEnd(final Point start, final TYPE type, final DIRECTION direction) {
        int len = 0;
        return (direction == DIRECTION.HORIZONTAL) ? new PPoint(start.x + len, start.y) : new PPoint(start.x, start.y + len);
    }

    private static int getLen(final TYPE type) {
        switch (type) {
            case DESTROYER:
            case SUBMARINE:
                return 3;
            case CARRIER:
                return 5;
            case CRUISER:
                return 4;
            default:
                // patrol boat
                return 2;
        }
    }

    /* getters & setters */
    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public PPoint getLocStart() {
        return locStart;
    }

    public void setLocStart(PPoint locStart) {
        this.locStart = locStart;
    }

    public PPoint getLocEnd() {
        return locEnd;
    }

    public void setLocEnd(PPoint locEnd) {
        this.locEnd = locEnd;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isHasUpgrade() {
        return hasUpgrade;
    }

    public void setHasUpgrade(boolean hasUpgrade) {
        this.hasUpgrade = hasUpgrade;
    }

    public Upgrades getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(Upgrades upgrades) {
        this.upgrades = upgrades;
    }

    /* helper classes */
    public class Upgrades implements Serializable {

        private static final long serialVersionUID = -6735966214424190784L;

        public Upgrades() {
        }

        private int armor;
        private int sonar;
        private int power;

        @Override
        public String toString() {
            return "Upgrades{" + "armor=" + armor + ", sonar=" + sonar + ", power=" + power + '}';
        }

        public void addUpgrade(final UPGRADES upgrade) {
            switch (upgrade) {
                case ARMOR:
                    armor++;
                    break;
                case POWER:
                    power++;
                    break;
                case SONAR:
                    sonar++;
                    break;
            }
        }

        public void removeUpgrade(final UPGRADES upgrade) {
            switch (upgrade) {
                case ARMOR:
                    armor--;
                    break;
                case POWER:
                    power--;
                    break;
                case SONAR:
                    sonar--;
                    break;
            }
        }

        public int getArmor() {
            return armor;
        }

        public void setArmor(int armor) {
            this.armor = armor;
        }

        public int getSonar() {
            return sonar;
        }

        public void setSonar(int sonar) {
            this.sonar = sonar;
        }

        public int getPower() {
            return power;
        }

        public void setPower(int power) {
            this.power = power;
        }
    }

    @Override
    public String toString() {
        return "Ship{" + "type=" + type + ", direction=" + direction + ", upgrades=" + upgrades + ", start=" + locStart + ", end=" + locEnd + ", length=" + length + ", hasUpgrade=" + hasUpgrade + '}';
    }

}
