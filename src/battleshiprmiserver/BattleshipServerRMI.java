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

import battleshiprmiserver.rest.BattleshipJerseyHelper;
import battleshiprmiserver.threads.Runner;
import battleshiprmiserver.threads.ThreadPool;
import dataobjects.Player;
import game.BattleGame;
import game.GameSession;
import game.Messages;
import interfaces.IBattleShip;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import interfaces.IClientListener;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JOptionPane;
import login.Login;

/**
 *
 * @author rudz
 */
@SuppressWarnings("serial")
public class BattleshipServerRMI extends UnicastRemoteObject implements IBattleShip, Runnable {

    private static volatile boolean rundemo = false;

    public ThreadPool threadpool;

    private PrettyPrint pp;

    private static boolean verbose = true;

    private static volatile BattleGame bg;

    /* the client interface index based on player names as string */
    private static final ConcurrentHashMap<String, IClientListener> index = new ConcurrentHashMap<>();

    /* the game session(s) based on the sessionID which is uniqly generated on player enter/leave session */
    private static final ConcurrentHashMap<String, GameSession> sessions = new ConcurrentHashMap<>();

    /* player object index based on the player names */
    private static final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

//    private final CopyOnWriteArrayList<IClientListener> list = new CopyOnWriteArrayList<>();
//    private final CopyOnWriteArrayList<String> plys = new CopyOnWriteArrayList<>();
//
//    private final CopyOnWriteArrayList<GameSession> games = new CopyOnWriteArrayList<>();

    /* to keep track of how many clients there are connected. */
    private final AtomicInteger clientCount = new AtomicInteger(0);

    /* keep track of how many sessions active */
    private final AtomicInteger sessionCount = new AtomicInteger(0);

    /* locks for session system */
    private final ReentrantLock games_lock = new ReentrantLock();

    private volatile int x, y;

    public BattleshipServerRMI() throws RemoteException {
        System.out.println("Starting threadpool.");
        threadpool = new ThreadPool(25, 25);

        x = 2;
        y = 3;
    }

    public PrettyPrint getPp() {
        return pp;
    }

    public void setPp(PrettyPrint pp) {
        this.pp = pp;
    }

    @Override
    public void run() {
        Random r = new Random();
        for (;;) {
            try {
                // Sleep for a random amount of time
                int duration = r.nextInt() % 10000 + 2000;
                // Check to see if negative, if so, reverse
                if (duration < 0) {
                    duration *= -1;
                }
                Thread.sleep(duration);
            } catch (InterruptedException ie) {
            }
            // Get a number, to see if value goes up or down
            int num = r.nextInt();
            if (num < 0) {
                x--;
                y--;
            } else {
                x++;
                y++;
            }
            // Notify registered listeners
            //notifyListeners();
        }
    }

//    private void notifyListeners() {
//        // Notify every listener in the registered list
//        for (int i = 0; i < list.size(); i++) {
//            IClientListener listener = (IClientListener) list.get(i);
//            try {
//                listener.shotFired(x, y, true);
//                listener.showMessage(Integer.toString(i), Integer.toString(x), 0);
//            } catch (RemoteException ex) {
//                /* if the exception is caught, the client is most likely not connected, so kill it without mercy */
//                Logger.getLogger(BattleshipServerRMI.class.getName()).log(Level.SEVERE, null, ex);
//                System.out.println("removing listener -" + listener);
//                // Remove the listener
//                list.remove(listener);
//            }
//        }
//    }
    public static void main(String args[]) {
        System.out.println("Loading battleship server, please wait.");
        try {

            final int port = 5000;
            final String myIP = "212.60.120.4";

            java.rmi.registry.LocateRegistry.createRegistry(1099);
            //java.rmi.registry.LocateRegistry.createRegistry(1099);

            // Load the service
            BattleshipServerRMI server = new BattleshipServerRMI();

            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
                //System.setProperty("java.rmi.server.hostname", myIP);
                System.setProperty("java.rmi.server.hostname", "localhost");
                System.out.println("SecurityManager created.");
            }

            // Check to see if a registry was specified
            String registry;

            if (args.length >= 1) {
                registry = BattleshipServerRMIHelper.setArgs(args);
                System.out.println("Registry changed through command-line to " + registry);
            } else {
                //registry = "212.60.120.4";
                registry = "localhost";
            }

            /* update the prettyprinter */
            server.setPp(new PrettyPrint(registry, port));
            server.getPp().showMenu();

            // Registration format //registry_hostname:port
            // service // Note the :port field is optional
            String registration = "rmi://" + registry + "/Battleship";

            // Register with service so that clients can find us
            Naming.rebind(registration, server);
            // Create a thread, and pass the server.
            // This will activate the run() method, and
            // trigger regular coordinate changes.

            /* configure battlegame object */
            bg = new BattleGame(index, sessions);

            if (rundemo) {
                Thread thread = new Thread(server);
                thread.start();
            }
        } catch (RemoteException re) {
            System.err.println("Remote Error - " + re);
        } catch (Exception e) {
            System.err.println("Error - " + e);
        }
    }

    @Override
    public void login(String user, String pw, IClientListener client) throws RemoteException {
        System.out.println("Attempted login by : " + user);
        boolean loginOK = Login.login(user, pw, client);
        System.out.println("User : " + user + (loginOK ? " has logged in." : " failed to login."));
        try {
            client.loginstatus(loginOK);
            Player p = new Player(user);
            p.initShips();
            client.setPlayer(p);
            /* replace old entry in client mapping */
            index.keySet().stream().filter((s) -> (index.get(s).equals(client))).map((s) -> {
                index.remove(s);
                return s;
            }).forEach((_item) -> {
                index.put(user, client);
            });
            players.put(user, p);
        } catch (final RemoteException re) {
            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, re);
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
                    try {
                        threadpool.execute(new Runner(clientInterface, clientInterface.getPlayer(), Messages.MessageType.GET_LOBBYS));
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

        return index.contains(clientInterface);

//        if (list.contains(clientInterface) || plys.contains(player)) {
//            System.out.println("Player : " + player + " was rejected, already connected.");
//            return false;
//        }
//        System.out.println("Connected > " + player);
//        try {
//            clientInterface.showMessage(player + ".\nYou are connected to the server.", "Server message", JOptionPane.INFORMATION_MESSAGE);
//            if (list.add(clientInterface) && plys.add(player)) {
//                System.out.println("Number of clients connected : " + clientCount.incrementAndGet());
//                RESTRunner.test(player, clientInterface);
//                return true;
//            }
//        } catch (final RemoteException re) {
//            System.out.println("Unable to contact client");
//        }
//        return false;
    }

    @Override
    public boolean removeClient(IClientListener clientInterface, String playerName, String sessionID) throws RemoteException {
        System.out.println("Disconnected manually -> " + playerName);
        new Runnable() {
            @Override
            public void run() {

                /* if the player is indexed (might not be?! :) */
                if (index.containsKey(playerName)) {
                    index.remove(playerName);
                }
                if (sessionID != null && sessionID.length() > 0) {

                }
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.run();

//        if (list.remove(clientInterface) && plys.remove(playerName)) {
//            System.out.println("Number of clients connected " + clientCount.decrementAndGet());
//            return true;
//        }
//        System.out.println("Error while removing client : " + clientInterface.toString());
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
    public void fireShot(int x, int y, String player, String sessionID) throws RemoteException {
        // tmp code :
        new Runnable() {
            @Override
            public void run() {
                GameSession gs = bg.getSessions().get(sessionID);
                try {
                    threadpool.execute(new Runner(gs.getOtherPlayer(player), gs.getPlayerByName(player), Messages.MessageType.SHOT_FIRED));
                } catch (final NullPointerException npe) {
                    System.out.println("Seems like the other player is null!");
                } catch (Exception ex) {
                    System.out.println("Error while attempting to shoot at other player....");
                    Logger.getLogger(BattleshipServerRMI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.run();
    }

    @Override
    public boolean logout(String player) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void pong(String player) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deployShips(final Player player, String sessionID) throws RemoteException {
        new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Deploy ships : " + player.getName() + "\n" + Arrays.toString(BattleshipJerseyHelper.shipsToString(player.getShips())));
                    GameSession gs = bg.getSessions().get(sessionID);
                    threadpool.execute(new Runner(gs.getClientByName(player.getName()), gs.getPlayerByName(player.getName()), Messages.MessageType.DEPLOY_SHIPS));
                } catch (Exception ex) {
                    Logger.getLogger(BattleshipServerRMI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.run();
    }

    @Override
    public void requestPlayers(String player) throws RemoteException {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

}
