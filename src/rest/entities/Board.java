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

import java.io.Serializable;

public class Board implements Serializable {

    private static final long serialVersionUID = 1L;


    private int lobbyid;
    private int playerid;
    private final int[][] hits = new int[10][10];

    public Board() {
        
    }
    
    public Board(final Lobby lobby, final Player player) {
        lobbyid = lobby.getLobbyid();
        playerid = player.getPlayerid();
    }

    public int getLobbyid() {
        return lobbyid;
    }

    public int getPlayerid() {
        return playerid;
    }

    public int[][] gethits() {
        return hits.clone();
    }

    public int getHit(final int col, final int row) {
        return hits[col][row];
    }

    public boolean setHit(final int col, final int row, final int state) {
        if (hits[col][row] >= state) {
            return false;
        }
        hits[col][row] = state;
        return true;
    }
}
