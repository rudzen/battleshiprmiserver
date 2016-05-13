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
package battleshiprmiserver;

import battleshiprmiserver.commander.FutureBasic;
import rest.BattleshipJerseyHelper;
import com.css.rmi.ServerTwoWaySocketFactory;
import dataobjects.Player;
import game.BattleGame;
import game.GameSession;
import interfaces.IBattleShip;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import interfaces.IClientListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMISocketFactory;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JOptionPane;
import login.Login;
import rest.BattleshipJerseyClient;

/**
 *
 * @author rudz
 */
@SuppressWarnings("serial")
public class BattleshipServerRMI extends UnicastRemoteObject implements IBattleShip {

    /**
     * the battlegame for future expansion of the server
     *
     * @deprecated
     */
    private static volatile BattleGame bg;

    /* the client interface index based on player names as string */
    private static final ConcurrentHashMap<String, IClientListener> index = new ConcurrentHashMap<>();

    /* the game session(s) based on the sessionID which is uniqly generated on player enter/leave session */
    private static final ConcurrentHashMap<String, GameSession> sessions = new ConcurrentHashMap<>();

    /**
     * player object index based on the player names
     *
     * @deprecated
     */
    private static final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<Integer, IClientListener> id_index = new ConcurrentHashMap<>();

    /* timer to check for dead clients */
    private Timer deadTimer = new Timer(true);

    /* to keep track of how many clients there are connected. */
    private final AtomicInteger clientCount = new AtomicInteger(0);

    /* keep track of how many sessions active */
    private final AtomicInteger sessionCount = new AtomicInteger(0);

    /* locks for session system */
    private final ReentrantLock games_lock = new ReentrantLock();
    private final ReentrantLock login_lock = new ReentrantLock();

    public BattleshipServerRMI() throws RemoteException {
        /* initiate the timer to check for dead clients */
        deadTimer.scheduleAtFixedRate(new DeadTimerMaintenance(), 90000, 90000);
    }

    public static void main(String args[]) {
        System.out.println("Loading battleship server, please wait.");
        try {

            if (args.length >= 1) {
                BattleshipServerRMIHelper.setArgs(args);
                System.out.println("Command line argument values changed to : " + Args.all());
            }

            /* set new socket through custom two-way socket factory */
            RMISocketFactory.setSocketFactory(new ServerTwoWaySocketFactory());

            /* export the registry from the same JVM */
            LocateRegistry.createRegistry(Args.port);

            /* set the REST address to be used */
            BattleshipJerseyClient.BASE_URI = Args.game_address;

            /* Load the service */
            BattleshipServerRMI server = new BattleshipServerRMI();

            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
                //System.setProperty("java.rmi.server.hostname", myIP);
                System.setProperty("java.rmi.server.hostname", "localhost");
                System.out.println("SecurityManager created.");
            }

            /* Print the basic information about the server */
            new PrettyPrint(Args.ip, Args.port, Args.game_address).showMenu();

            String registration = "rmi://" + Args.registry() + "/Battleship";
            System.out.println("Using registry : " + registration);

            /* Register with service so that clients can find us */
            Naming.rebind(registration, server);

            /* configure battlegame object */
            bg = new BattleGame(index, sessions);

        } catch (RemoteException re) {
            System.err.println("Remote Error - " + re);
        } catch (Exception e) {
            System.err.println("Error - " + e);
        }
    }

    public void updateUser() {
        clientCount.set(index.size());
        System.out.println("Clients connected : " + clientCount.get());
    }

    /**
     * The login function does just that.<br>
     *
     * @param user
     * @param pw
     * @param client
     * @throws RemoteException
     */
    @Override
    public void login(String user, String pw, IClientListener client) throws RemoteException {
        System.out.println("Attempted login by : " + user);
//        for (;;) {
//            FutureBasic.login(client, Double.toString(Math.random()), pw);
//        }

        if (login_lock.isLocked()) {
            client.showMessage("Queued for login", "Login information", JOptionPane.WARNING_MESSAGE);
        }
        try {
            login_lock.lock();
            boolean loginOK = Login.loginBA(user, pw, client);
            if (loginOK) {
                // proceed to secondary login
            }
            System.out.println("User : " + user + (loginOK ? " has logged in." : " failed to login."));
            client.loginstatus(loginOK);
            Player p = client.getPlayer();
            p.setName(user);
            client.setPlayer(p);
            /* replace old entry in client mapping */
            index.keySet().parallelStream().filter((s) -> (index.get(s).equals(client))).map((s) -> {
                index.remove(s);
                return s;
            }).forEach((_item) -> {
                index.put(user, client);
            });
            players.put(user, p);
        } catch (final RemoteException re) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, re);
        } finally {
            login_lock.unlock();
        }
    }

    @Override
    public boolean registerClient(final IClientListener clientInterface, final String playerName) throws RemoteException {
        new Runnable() {
            @Override
            public void run() {
                try {
                    if (index.contains(playerName)) {
                        System.out.println("User updated to index : " + playerName);
                        index.get(playerName).showMessage("You have been unregistered in the server index.", "Server notification", JOptionPane.WARNING_MESSAGE);
                        clientInterface.showMessage("You have been updated in the server index", "Server notification", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        System.out.println("User added to index : " + playerName);
                        clientInterface.showMessage("You have been added to the server index", "Server notification", JOptionPane.INFORMATION_MESSAGE);
                    }
                    index.put(playerName, clientInterface);
                    players.put(playerName, clientInterface.getPlayer());
                    Player p = clientInterface.getPlayer();
                    GameSession gs = new GameSession(p, clientInterface);
                    String ID = bg.updateSessionID(gs);
                    gs.setGameSessionID(ID);
                    sessions.put(ID, gs);
                    clientInterface.updateSessionID(ID);
                    try {
                        FutureBasic.getLobbys(clientInterface);
                    } catch (Exception ex) {
                        Logger.getLogger(BattleshipServerRMI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (final RemoteException re) {
                    Logger.getLogger(BattleshipServerRMI.class.getName()).log(Level.SEVERE, "Client could not be reached {0}", clientInterface);
                    Logger.getLogger(BattleshipServerRMI.class.getName()).log(Level.SEVERE, null, re);
                    index.remove(playerName);
                }

            }
        }.run();
        updateUser();
        return index.contains(clientInterface);
    }

    @Override
    public boolean removeClient(IClientListener clientInterface, String playerName, String sessionID) throws RemoteException {
        System.out.println("Disconnected manually -> " + playerName);
        index.remove(playerName);
        updateUser();
        return false;
    }

    @Override
    public Player getOther(Player playerOne) throws RemoteException {
        games_lock.lock();
        Player returnPlayer = null;
        boolean found = false;
        try {
            for (GameSession gs : bg.getSessions().values()) {
                if (gs.isInSession(playerOne)) {
                    returnPlayer = gs.getOtherPlayer(playerOne);
                    found = true;
                    break;
                }
            }
            games_lock.unlock();
        } catch (final Exception e) {
            System.err.println("Error while " + playerOne.getName() + " attempted to get opponent.");
        } finally {
            games_lock.unlock();
        }
        return returnPlayer;
    }

    @Override
    public void fireShot(final IClientListener client, final int lobbyID, final int playerID, final int x, final int y) throws RemoteException {
        FutureBasic.fireShot(client, lobbyID, playerID, x, y);
    }

    @Override
    public boolean logout(String player) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deployShips(final IClientListener client, final int lobbyID, final Player player) throws RemoteException {
        System.out.println(player + " -> deployShips() :\n" + Arrays.toString(BattleshipJerseyHelper.shipsToString(player.getShips())));
        FutureBasic.deployBoard(client, lobbyID, player);
    }

    @Override
    public void requestPlayers(String player) throws RemoteException {
        index.get(player).setPlayer(players.get(player));
    }

    @Override
    public void updatePlayer(String newPlayer) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void publicMessage(String origin, String message, String title, int modal) throws RemoteException {
        new Runnable() {
            @Override
            public void run() {
                bg.getPlayers().keySet().stream().filter((s) -> (!s.equals(origin))).forEach((s) -> {
                    try {
                        bg.getPlayers().get(s).showMessage(message, title, modal);
                    } catch (RemoteException ex) {
                        Logger.getLogger(BattleshipServerRMI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        }.run();
    }

    @Override
    public boolean updatePlayerObject(String seesionID, Player playerObject) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String requestSessionID(String currentSessionID, Player playerObject) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean messageOpponent(String title, String message) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void requestOtherPlayer(IClientListener client) throws RemoteException {
        FutureBasic.getLobbys(client);
    }

    @Override
    public void requestFreeLobbies(IClientListener client) throws RemoteException {
        System.out.println(client.getPlayer().getName() + " requesting free lobbies.");
        FutureBasic.getFreeLobbys(client);
    }

    @Override
    public void requestLobbyID(IClientListener client, int playerID) throws RemoteException {
        FutureBasic.newLobby(client, playerID);
    }

    @Override
    public void ping(IClientListener client, long time) throws RemoteException {
        client.ping(time);
    }

    @Override
    public void requestAllPlayerIDs(IClientListener client) throws RemoteException {
        FutureBasic.getAllPlayerIDs(client);
    }

    @Override
    public void wait(IClientListener client) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void joinLobby(IClientListener cliet, int lobbyID, final int playerID) throws RemoteException {
        FutureBasic.joinLobby(cliet, lobbyID, playerID);
    }

    @Override
    public void requestAllLobbies(IClientListener client) throws RemoteException {
        FutureBasic.getLobbys(client);
    }

    private class DeadTimerMaintenance extends TimerTask {

        @Override
        public void run() {
            if (!index.isEmpty()) {
                System.out.println("Maintenance : Dead client elimination in progress..");
                int count = 0;
                for (final String s : index.keySet()) {
                    try {
                        index.get(s).hello();
                    } catch (final RemoteException re) {
                        count++;
                        System.out.println("Maintenance : " + s + " removed from index..");
                        index.remove(s);
                        players.remove(s);
                    }
                }
                System.out.println("Maintenance : Removed " + count + " clients from index.");

                count = 0;
                for (final String s : sessions.keySet()) {
                    GameSession gs = sessions.get(s);
                    if (gs.getPlayerOne() != null && index.get(gs.getPlayerOne().getName()) == null) {
                        if (gs.getPlayerTwo() != null && index.get(gs.getPlayerTwo().getName()) != null) {
                            try {
                                index.get(gs.getPlayerTwo().getName()).showMessage("Session terminated", "Maintenance", JOptionPane.INFORMATION_MESSAGE);
                            } catch (final RemoteException ex) {
                            } finally {
                                count++;
                                sessions.remove(s);
                            }
                        }
                    }
                }
                System.out.println("Maintenance : Removed " + count + " sessions.");
            }
            updateUser();
        }
    }

}
