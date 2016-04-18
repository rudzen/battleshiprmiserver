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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * The Player class for BattleShip.<br>
 *
 * @author rudz
 */
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int MAX_SHIPS = 5;
    private static final int BOARD_SIZE = 10;

    private int id;
    private String name;
    private String token;

    /*
    board defined as :
    0 = empty not shot
    1 = shot, no hit
    2 = shot, hit
    3 = shot, hit, armor rating
    4 = ship location
    5 = ship, sunk
     */
    private int[][] board;

    /**
     * The ships which are available to the player.
     */
    private Ship[] ships = new Ship[5];

    /**
     * Empty constructor
     */
    public Player() {
    }

    public Player(final String name) {
        this.name = name;
    }

    public Player(final Player player) {
        name = player.getName();
        id = player.getId();
        token = player.getToken();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; i++) {
                board[i][j] = player.getBoard()[i][j];
            }
        }
        if (MAX_SHIPS < 25) { // threshold for manual array copy advantage.
            for (int i = 0; i < MAX_SHIPS; i++) {
                ships[i] = player.getShips()[i];
            }
        } else {
            System.arraycopy(player.getShips(), 0, ships, 0, MAX_SHIPS);
        }
    }

    public void boardHit(final int x, final int y) {
        
        // TODO : Needs to be reworked somehow!
        
        // TODO : Attacker and defender needs to get the right message back from here !.
        
        StringBuilder sbAttacker = new StringBuilder();
        StringBuilder sbDefender = new StringBuilder();
        switch (board[x][y]) {
            case 0:
            case 3:
                // board has not been hit here before.
                for (int i = 0; i < MAX_SHIPS; i++) {
                    if (ships[i].isHit(x, y)) { // could be if board[x][y] == 4 if board is updated with ship layouts.
                        // ship appears to be hit.
                        sbAttacker.append("Ship hit");
                        sbDefender.append("The attacker has hit your ").append(ships[i].getShipType());
                        
                        if (!ships[i].isDead()) {
                            // ship is still alive
                            if (ships[i].isHasUpgrade() && ships[i].getUpgrades().getArmor() > 0) {
                                board[x][y] = 3;
                            } else {
                                board[x][y] = 2;
                            }
                            // TODO : Send message to users.
                        } else {
                            // ship is sunk
                            sbAttacker.append(",\nwhich was enough to sink the ").append(ships[i].getShipType());
                            sbDefender.append(",\nand sunk it!");
                            board[x][y] = 5;
                        }
                    }
                }
            case 1: // shot, no hit.
                sbAttacker.append("You have wasted another shot on this spot...");
                sbDefender.append("The opponent has wasted the turn!");
                break;
            case 2:
                sbAttacker.append("Ship has already been hit at this location.");
                sbDefender.append("The opponent has wasted the turn!");
                break;
            case 5:
                sbAttacker.append("Ship has already been sunk!");
                sbDefender.append("The opponent has wasted the turn!");
                break;
        }
        // TODO : send sb.toString(); to the attacker...
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(150);
        sb.append("Name : ").append(name).append("\n");
        sb.append("ID   : ").append(id).append("\n");
        sb.append("------------\n-");
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                sb.append(Integer.toString(board[i][j]));
            }
            sb.append("-\n");
        }
        for (int i = 0; i < MAX_SHIPS; i++) {
            sb.append(ships[i]);
        }
        return sb.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        Player p = new Player();
        p.setId(id);
        p.setBoard(board);
        p.setName(name);
        p.setShips(ships);
        p.setToken(token);
        return p;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            Player p = (Player) obj;
            if (p.id != id) {
                return false;
            }
            if (!p.name.equals(name)) {
                return false;
            }
            if (obj.hashCode() != hashCode()) {
                return false;
            }
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(id);
        hash = 43 * hash + Objects.hashCode(name);
        hash = 43 * hash + Objects.hashCode(token);
        hash = 43 * hash + Arrays.deepHashCode(board);
        hash = 43 * hash + Arrays.deepHashCode(ships);
        return hash;
    }

    /* getters & setters */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public Ship[] getShips() {
        return ships;
    }

    public void setShips(Ship[] ships) {
        this.ships = ships;
    }

}
