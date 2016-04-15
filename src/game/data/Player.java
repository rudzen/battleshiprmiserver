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

/**
 * The Player class for BattleShip.<br>
 *
 * @author rudz
 */
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int MAX_SHIPS = 5;
    private static final int BOARD_SIZE = 10;

    private String id;
    private String name;
    private String token;

    /*
    board defined as :
    0 = empty not shot
    1 = shot, no hit
    2 = shot, hit
    3 = shot, hit, armor rating
    4 = ship location
    5 = ship sunk
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
        if (MAX_SHIPS < 25) { // threshold for manual array copy advantage in count.
            for (int i = 0; i < MAX_SHIPS; i++) {
                ships[i] = player.getShips()[i];
            }
        } else {
            System.arraycopy(player.getShips(), 0, ships, 0, MAX_SHIPS);
        }
    }

    public void boardHit(final int x, final int y) {
        board[x][y]--;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
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
            if (!p.id.equals(id)) return false;
            if (!p.name.equals(name)) return false;
        }
        return super.equals(obj);
    }
    
    /* getters & setters */
    public String getId() {
        return id;
    }

    public void setId(String id) {
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
