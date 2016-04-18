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

import game.data.Upgrades.UPGRADES;
import java.io.Serializable;

/**
 * Basic ship class, it defines what ship type it is, and what else there is to
 * know about it.
 *
 * @author rudz
 * @version 1.0
 * @since 16-04-2016
 */
public class Ship implements Serializable {

    private static final long serialVersionUID = -5311711410232026986L;

    /**
     * The type of possible ship types. Patrol = 2 length Destroyer & Submarine
     * = 3 length Cruiser = 4 length Carrier = 5 length
     */
    public enum TYPE {
        PATROL, DESTROYER, SUBMARINE, CRUISER, CARRIER
    }

    /**
     * The possible directions for the ship<br>
     * HORIZONTAL = right-left<br>
     * VERTICAL = up-down
     */
    public enum DIRECTION {
        HORIZONTAL, VERTICAL;
    }

    /**
     * The ship type
     */
    private TYPE type;

    /**
     * The ship direction
     */
    private DIRECTION direction;

    /**
     * The upgrades for the ship.
     */
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

    /**
     * The basic hit index. 0 = not hit, 1 = hit
     */
    private int[] hits;

    /**
     * Default constructor.
     */
    public Ship() {
    }

    /**
     * Constructor without upgrades defined.
     *
     * @param x The X start location
     * @param y The Y start location
     * @param type The type of the ship
     * @param direction The direction of placement
     */
    public Ship(final int x, final int y, final TYPE type, final DIRECTION direction) {
        locStart = new PPoint(x, y);
        length = getLen(type);
        locEnd = setEnd(locStart, length, direction);
        this.type = type;
        this.direction = direction;
        life = length;
        hits = new int[length];
        upgrades = new Upgrades(); // just to make sure, java is picky with it's serialization!
    }

    /**
     * Constructor which contain upgrade(s)
     *
     * @param x The X start location
     * @param y The Y start location
     * @param type The type of the ship
     * @param direction The placement direction
     * @param upgrades The upgrades for the ship.
     */
    public Ship(final int x, final int y, final TYPE type, final DIRECTION direction, final Upgrades upgrades) {
        this(x, y, type, direction);
        hasUpgrade = upgrades.hasUpgrades();
        if (hasUpgrade) {
            this.upgrades = new Upgrades(upgrades);
            if (upgrades.getArmor() > 0) {
                life += upgrades.getArmor();
            }
        }
    }

    /* upgrade helper functions */
    /**
     * Adds an upgrade to the ship (if bought by the player)
     *
     * @param upgradeType The upgrade type.
     */
    public void addUpgrade(final UPGRADES upgradeType) {
        upgrades.addUpgrade(upgradeType);
    }

    /**
     * Removes an upgrade from the ship (wahh, what why?)
     *
     * @param upgradeType The upgrade type.
     */
    public void removeUpgrade(final UPGRADES upgradeType) {
        upgrades.removeUpgrade(upgradeType);
    }

    /* helper methods */
    /**
     * Is the ship dead?
     *
     * @return true if ship if dead, otherwise false
     */
    public boolean isDead() {
        return life == 0;
    }

    /**
     * Hit the ship!! (argh!)
     */
    private void hit(final int location) {
        life--;
        hits[location] = 1;
    }

    /**
     * Determines if the ship has been hit.<br>
     * The ship will loose 1 life if hit.
     *
     * @param x The X coordinate to check
     * @param y The Y coordinate to check
     * @return true if ship is hit, otherwise false.
     */
    public boolean isHit(byte x, byte y) {
        /* check off the bat for direct start & end hit first! */
        if (x == locStart.getX() && y == locStart.getY()) {
            hit(0);
            return true;
        } else if (x == locEnd.getX() && y == locEnd.getY()) {
            hit(length - 1);
            return true;
        }

        // TODO :: WHAT WHAT!!!
        if (direction == DIRECTION.HORIZONTAL) {
            if (x + locStart.getX() < locEnd.getX()) {
                hit(locEnd.getX() - x);
                return true;
            }
        } else if (y + locStart.getY() < locEnd.getY()) {
            hit(locEnd.getY() - y);
            return true;
        }
        return false;
    }

    /**
     * Overload of {@link #isHit(byte x, byte y)} to check for hit with
     * integers.
     *
     * @param x The X coordinate to check
     * @param y The Y coordinate to check
     * @return true if ship is hit, otherwise false.
     */
    public boolean isHit(int x, int y) {
        return isHit((byte) x, (byte) y);
    }

    /**
     * Set the end specified by the start and the type combined with direction.
     *
     * @param start The start PPoint object containing the start coordinates
     * @param type The type of the ship
     * @param direction The direction of the ship
     * @return The end PPoint object
     */
    private static PPoint setEnd(final PPoint start, final int length, final DIRECTION direction) {
        return (direction == DIRECTION.HORIZONTAL) ? new PPoint(start.getX() + length, start.getY()) : new PPoint(start.getX(), start.getY() + length);
    }

    /**
     * Determines the length of the ship based on it's type.
     *
     * @param type The type of the ship
     * @return The length of the ship
     */
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
                // patruljepølse
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

    @Override
    public String toString() {
        return "Ship{" + "type=" + type + ", direction=" + direction + ", upgrades=" + upgrades + ", start=" + locStart + ", end=" + locEnd + ", length=" + length + ", hasUpgrade=" + hasUpgrade + '}';
    }

}
