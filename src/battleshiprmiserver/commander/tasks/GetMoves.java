/*
 * The MIT License
 *
 * Copyright 2016 Rudy Alex Kohn (s133235@student.dtu.dk).
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
package battleshiprmiserver.commander.tasks;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.awt.Point;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import interfaces.IClientRMI;

/**
 * DOES NOT WORK.. LIMITED SERVER RESPONSE
 * @author Rudy Alex Kohn (s133235@student.dtu.dk)
 */
public class GetMoves extends GetAbstract {

    private final int lobbyID;
    private final int playerID;
    
    public GetMoves(final IClientRMI client, final int lobbyID, final int playerID) {
        super(client);
        this.lobbyID = lobbyID;
        this.playerID = playerID;
    }

    @Override
    public void run() {
        final String s = rest.getMoves(Integer.toString(lobbyID));
        rest.close();
        final ArrayList<Point> fromServer = new Gson().fromJson(s, new ArrayListTypeToken().getType());
        //ArrayList<Point> fromServer = new Gson().fromJson(s, new TypeToken<ArrayList<Point>>() {}.getType());
        final int[][] board = new int[10][10];
        fromServer.parallelStream().forEach(p -> {
            board[p.x][p.y] = 1;
        });
        try {
            client.updateOpponentBoard(board);
            client.canPlay(true);
        } catch (final RemoteException ex) {
            Logger.getLogger(Wait.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private static class ArrayListTypeToken extends TypeToken<ArrayList<Point>> {}
}
