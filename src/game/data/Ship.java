/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data;

import java.io.Serializable;

/**
 * Basic ship class, it defines what ship type it is, and what else there is to know about it.
 * @author rudz
 */
public class Ship implements Serializable {

    private static final long serialVersionUID = -5311711410232026986L;

    /**
     * The type of ship.
     * Patrol = 2 length
     * Destroyer & Submarine = 3 length
     * Cruiser = 4 length
     * Carrier = 5 length
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
     * Upper-left coordinates of the ship
     */
    private int x;
    private int y;

    /**
     * The size of the ship
     */
    private int size;
    
    /**
     * If the ship has _any_ type of upgrade, this is set to true
     */
    private boolean hasUpgrade;
    
    
    public Ship() { }
    
    public Ship(final int x, final int y, final TYPE type, final DIRECTION direction) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.direction = direction;
    }

    public Ship(final int x, final int y, final TYPE type, final DIRECTION direction, final Upgrades upgrades) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.direction = direction;
        this.upgrades = upgrades;
    }
    
    /* upgrade helper functions */
    public void addUpgrade(final UPGRADES upgradeType) {
        upgrades.addUpgrade(upgradeType);
    }

    public void removeUpgrade(final UPGRADES upgradeType) {
        upgrades.removeUpgrade(upgradeType);
    }
    
    /* getters & setters */
    
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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
    
    
    public class Upgrades implements Serializable {

        private static final long serialVersionUID = -6735966214424190784L;
        
        public Upgrades() { }
        
        private int armor;
        private int sonar;
        private int power;

        
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
    
}
