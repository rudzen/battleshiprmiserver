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

package battleshiprmiserver.commander;

import battleshiprmiserver.commander.tasks.LoginTask;
import battleshiprmiserver.commander.tasks.DeployShips;
import battleshiprmiserver.commander.tasks.FireShot;
import battleshiprmiserver.commander.tasks.GetAllPlayerIDS;
import battleshiprmiserver.commander.tasks.GetFreeLobbys;
import battleshiprmiserver.commander.tasks.GetLobbys;
import battleshiprmiserver.commander.tasks.GetMoves;
import battleshiprmiserver.commander.tasks.GetPlayer;
import battleshiprmiserver.commander.tasks.GetPlayerID;
import battleshiprmiserver.commander.tasks.JoinLobby;
import battleshiprmiserver.commander.tasks.NewLobby;
import battleshiprmiserver.commander.tasks.Wait;
import dataobjects.Player;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import interfaces.IClientRMI;

/**
 *
 * @author Rudy Alex Kohn (s133235@student.dtu.dk)
 */
public class FutureBasic {

    private static final ExecutorService POOL = Executors.newCachedThreadPool();

//    /**
//     * Pool submiter for:<br>
//     * deployBoard
//     *
//     * @param client The client interface calling the command
//     * @param lobbyID The lobbyID to deploy in
//     * @param player The player object deploying
//     */
//    public static void deployBoard(final IClientListener client, final int lobbyID, final Player player) {
//        POOL.submit(new DeployBoard(client, player, lobbyID));
//    }
    
    
    /**
     * Deploys ships
     * @param client the client
     * @param lobbyID the lobbyID to deploy in
     * @param player The player who is deploying
     */
    public static void deployShips(final IClientRMI client, final int lobbyID, final Player player) {
        POOL.submit(new DeployShips(client, lobbyID, player));
    }

    /**
     * Pool submiter for: fireShot
     *
     * @param client The client interface calling the command
     * @param lobbyID The lobby ID
     * @param playerID The player ID
     * @param x
     * @param y
     */
    public static void fireShot(final IClientRMI client, final int lobbyID, final int playerID, final int x, final int y) {
        POOL.submit(new FireShot(client, lobbyID, playerID, x, y));
    }

    /**
     * Pool submiter for: getAllPlayerIDs
     *
     * @param client The client requesting the list
     */
    public static void getAllPlayerIDs(final IClientRMI client) {
        POOL.submit(new GetAllPlayerIDS(client));
    }

    /**
     * Pool submiter for: getFreeLobbys
     *
     * @param client The client interface calling the command
     * @param playerID The player ID requesting the lobbies
     */
    public static void getFreeLobbys(final IClientRMI client, final int playerID) {
        POOL.submit(new GetFreeLobbys(client, playerID));
    }

    /**
     * Pool submiter for: getLobbys
     *
     * @param client The client interface calling the command
     * @param playerID The player ID requesting the lobbies
     */
    public static void getLobbys(final IClientRMI client, final int playerID) {
        POOL.submit(new GetLobbys(client, playerID));
    }

    public static void getPlayer() {
        POOL.submit(new GetPlayer());
    }

    public static void getPlayerID(final IClientRMI client, final String name) {
        POOL.submit(new GetPlayerID(client, name));
    }

    /**
     * Pool submiter for: getMoves
     *
     * @param client The client interface calling the command
     * @param lobbyID The lobby ID
     * @param playerID The player ID
     */
    public static void getMoves(final IClientRMI client, final int lobbyID, final int playerID) {
        POOL.submit(new GetMoves(client, lobbyID, playerID));
    }

    public static void joinLobby(final IClientRMI client, final int lobbyID, final int playerID) {
        POOL.submit(new JoinLobby(client, lobbyID, playerID));
    }

    /**
     * Pool submiter for: Wait
     *
     * @param client The client interface calling the command
     * @param lobbyID The lobby ID
     * @param playerID The player ID
     */
    public static void wait(final IClientRMI client, final int lobbyID, final int playerID) {
        POOL.submit(new Wait(client, lobbyID, playerID));
    }

    public static void login(final IClientRMI client, final String u, final String p) {
        POOL.submit(new LoginTask(client, u, p));
    }

    public static void newLobby(final IClientRMI client, final int playerID) {
//        Random rnd = new Random();
//        for (;;) {
//            POOL.submit(new NewLobby(client, rnd.nextInt(4)));
//        }
        POOL.submit(new NewLobby(client, playerID));
    }

}
