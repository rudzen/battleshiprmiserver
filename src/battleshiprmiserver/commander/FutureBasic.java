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
package battleshiprmiserver.commander;

import battleshiprmiserver.commander.tasks.DeployShips;
import battleshiprmiserver.commander.tasks.FireShot;
import battleshiprmiserver.commander.tasks.GetFreeLobbys;
import battleshiprmiserver.commander.tasks.GetLobbys;
import battleshiprmiserver.commander.tasks.GetMoves;
import battleshiprmiserver.commander.tasks.Wait;
import dataobjects.Player;
import interfaces.IClientListener;
import java.awt.Point;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Rudy Alex Kohn <s133235@student.dtu.dk>
 */
public class FutureBasic {

    private static final ExecutorService pool = Executors.newFixedThreadPool(100);

    /**
     * Pool submiter for:<br>
     * getLobbys
     * @param client The client interface calling the command
     */
    public static void getLobbys(IClientListener client) {
        pool.submit(new GetLobbys(client));
    }

    public static void getFreeLobbys(IClientListener client) {
        pool.submit(new GetFreeLobbys(client));
    }

    public static void deployBoard(final IClientListener client, final Player player, final int lobbyID) {
        pool.submit(new DeployShips(client, player, lobbyID));
    }
    
    public static void wait(final IClientListener client, final int lobbyID, final int playerID) {
        pool.submit(new Wait(client, lobbyID, playerID));
    }
    
    public static void getMoves(final IClientListener client, final int lobbyID, final int playerID) {
        pool.submit(new GetMoves(client, lobbyID, playerID));
    }
    
    public static void fireShot(final IClientListener client, final int lobbyID, final int playerID, final int x, final int y) {
        pool.submit(new FireShot(client, lobbyID, playerID, x, y));
    }

    public static void fireShot(final IClientListener client, final int lobbyID, final int playerID, final Point coords) {
        pool.submit(new FireShot(client, lobbyID, playerID, coords));
    }
    
    
}
