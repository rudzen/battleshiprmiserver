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
package battleshiprmiserver;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import battleshiprmiserver.commander.FutureBasic;
import dataobjects.Player;
import game.BattleGame;
import game.GameSession;
import interfaces.IBattleShip;
import interfaces.IChatClient;
import interfaces.IClientRMI;
import java.util.ArrayList;

import javax.jws.WebService;

/**
 *
 * @author rudz
 */
@WebService(endpointInterface = "interfaces.IBattleshipSOAP")
public class BattleshipServerRMI extends UnicastRemoteObject implements IBattleShip {

    /**
     * the battlegame for future expansion of the server
     *
     * @deprecated
     */
    private static volatile BattleGame bg;

    /* the client interface index based on player names as string */
    private static final ConcurrentHashMap<String, IClientRMI> index = new ConcurrentHashMap<>();

    /* the game session(s) based on the sessionID which is uniqly generated on player enter/leave session */
    private static final ConcurrentHashMap<String, GameSession> sessions = new ConcurrentHashMap<>();

    /**
     * player object index based on the player names
     *
     * @deprecated
     */
    private static final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<Integer, IClientRMI> id_index = new ConcurrentHashMap<>();
    private static final long serialVersionUID = 1L;

    /* chat stuff (testing) */
    private static final ConcurrentHashMap<String, IChatClient> chat_index = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> chat_messages = new ConcurrentHashMap<>();


    /* end of chat stuff */
 /* timer to check for dead clients */
    private final Timer deadTimer = new Timer(true);

    /* to keep track of how many clients there are connected. */
    private final AtomicInteger clientCount = new AtomicInteger(0);

    /* keep track of how many sessions active */
    private final AtomicInteger sessionCount = new AtomicInteger(0);

    /* locks for session system */
    private final ReentrantLock games_lock = new ReentrantLock();
    private final ReentrantLock login_lock = new ReentrantLock();

    public BattleshipServerRMI() throws RemoteException {
        super(Registry.REGISTRY_PORT);

        /* configure battlegame object */
        bg = new BattleGame(index, sessions);

        /* initiate the timer to check for dead clients */
        deadTimer.scheduleAtFixedRate(new DeadTimerMaintenance(), 90000, 90000);
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
    public void login(final IClientRMI client, final String user, final String pw) throws RemoteException {
        System.out.println("Attempted login by : " + user);
//        for (;;) {
//            FutureBasic.login(client, Double.toString(Math.random()), pw);
//        }

        FutureBasic.login(client, user, pw);

//        if (login_lock.isLocked()) {
//            client.showMessage("Queued for login", "Login information", JOptionPane.WARNING_MESSAGE);
//        }
//        try {
//            login_lock.lock();
//            boolean loginOK = Login.loginBA(user, pw, client);
//            if (loginOK) {
//                // proceed to secondary login
//            }
//            System.out.println("User : " + user + (loginOK ? " has logged in." : " failed to login."));
//            client.loginstatus(loginOK);
//            Player p = client.getPlayer();
//            p.setName(user);
//            client.setPlayer(p);
//            /* replace old entry in client mapping */
//            index.keySet().parallelStream().filter((s) -> (index.get(s).equals(client))).map((s) -> {
//                index.remove(s);
//                return s;
//            }).forEach((_item) -> {
//                index.put(user, client);
//            });
//            players.put(user, p);
//        } catch (final RemoteException re) {
//            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, re);
//        } finally {
//            login_lock.unlock();
//        }
    }

    @Override
    public boolean registerClient(final IClientRMI clientInterface, final String playerName) throws RemoteException {
        new RegisterClient(playerName, clientInterface).run();
        updateUser();
        return index.contains(clientInterface);
    }

    @Override
    public boolean removeClient(final IClientRMI clientInterface, final String playerName, final String sessionID) throws RemoteException {
        System.out.println("Disconnected manually -> " + playerName);
        index.remove(playerName);
        updateUser();
        return false;
    }

    @Override
    public Player getOther(final Player playerOne) throws RemoteException {
        games_lock.lock();
        Player returnPlayer = null;
        try {
            for (final GameSession gs : bg.getSessions().values()) {
                if (gs.isInSession(playerOne)) {
                    returnPlayer = gs.getOtherPlayer(playerOne);
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
    public void fireShot(final IClientRMI client, final int lobbyID, final int playerID, final int x, final int y) throws RemoteException {
        FutureBasic.fireShot(client, lobbyID, playerID, x, y);
    }

    @Override
    public boolean logout(final String player) throws RemoteException {
        System.out.println(player + " attempted to log out, not supported yet!! :)");
        return true;
    }

    @Override
    public void deployShips(final IClientRMI client, final int lobbyID, final Player player) throws RemoteException {
        //System.out.println(player + " -> deployShips() :\n" + Arrays.toString(BattleshipJerseyHelper.shipsToString(player.getShips())));
        FutureBasic.deployShips(client, lobbyID, player);
    }

    @Override
    public void updatePlayer(final IClientRMI client, final Player player) throws RemoteException {
        /* this functionality is NOT allowed to be run by multiple users at the same time!!!! */
        try {
            games_lock.lock();
            index.keySet().parallelStream().filter(s -> (index.get(s).equals(client))).map(s -> {
                index.remove(s);
                return s;
            }).forEach(_item -> {
                index.put(player.getName(), client);
            });
        } catch (final Exception e) {

        } finally {
            games_lock.unlock();
        }
    }

    @Override
    public void publicMessage(final String origin, final String message, final String title, final int modal) throws RemoteException {
        new PublicMessage(origin, message, title, modal).run();
    }

    @Override
    public boolean updatePlayerObject(final String seesionID, final Player playerObject) throws RemoteException {
        System.out.println("Player : " + playerObject.getName() + " is attempting to update player (session) (not supported yet).");
        return true;
    }

    @Override
    public String requestSessionID(final String currentSessionID, final Player playerObject) throws RemoteException {
        System.out.println("Player : " + playerObject.getName() + " is attempting session ID retrieval (not supported yet).");
        if (index.containsKey(playerObject.getName())) {
            return bg.updateSessionID(playerObject, index.get(playerObject.getName()));
        }
        return "";
    }

    @Override
    public boolean messageOpponent(final String title, final String message) throws RemoteException {
        System.out.println("Attempt to message opp : " + title + ":" + message + " (not supported yet).");
        return true;
    }

    @Override
    public void requestOtherPlayers(final IClientRMI client, final int playerID) throws RemoteException {
        System.out.println("Player : " + client.getPlayer().getName() + " requesting other players.");
        FutureBasic.getLobbys(client, playerID);
    }

    @Override
    public void requestFreeLobbies(final IClientRMI client, final int playerID) throws RemoteException {
        System.out.println("Player : " + client.getPlayer().getName() + " requesting free lobbies.");
        FutureBasic.getFreeLobbys(client, playerID);
    }

    @Override
    public void requestLobbyID(final IClientRMI client, final int playerID) throws RemoteException {
        System.out.println("Player (ID) : " + playerID + " is requesting lobby ID");
        FutureBasic.newLobby(client, playerID);
    }

    @Override
    public void ping(final IClientRMI client, final long time) throws RemoteException {
        client.ping(time);
    }

    @Override
    public void requestAllPlayerIDs(final IClientRMI client) throws RemoteException {
        System.out.println("Player : " + client.getPlayer().getName() + " is requesting all player IDs");
        FutureBasic.getAllPlayerIDs(client);
    }

    @Override
    public void wait(final IClientRMI client, final int lobbyID, final int playerID) throws RemoteException {
        FutureBasic.wait(client, lobbyID, playerID);
    }

    @Override
    public void joinLobby(final IClientRMI cliet, final int lobbyID, final int playerID) throws RemoteException {
        FutureBasic.joinLobby(cliet, lobbyID, playerID);
    }

    @Override
    public void requestAllLobbies(final IClientRMI client, final int playerID) throws RemoteException {
        FutureBasic.getLobbys(client, playerID);
    }

    @Override
    public void debug_CreateLobbies(final IClientRMI client, final int amount) throws RemoteException {
        games_lock.lock();
        System.out.println("DEBUG: Creating " + amount + " lobbies. (DEBUG FUNCTIONALITY IS NOW LOCKED!)");
        final Random rnd = new Random();
        try {
            for (int i = 0; i < 1000; i++) {
                FutureBasic.newLobby(client, rnd.nextInt(3));
            }
        } catch (final Exception e) {

        } finally {
            games_lock.unlock();
        }

    }

    @Override
    public void registerChatClient(IChatClient client, String name) throws RemoteException {
        chat_index.put(name, client);
        System.out.println("User joined chat : " + name);
        final ArrayList<String> returnList = new ArrayList<>(chat_index.size());
        chat_index.keySet().stream().forEach((s) -> {
            returnList.add(s);
            try {
                chat_index.get(s).newMessage("<Server>", name + " joined chat.");
            } catch (RemoteException ex) {
                Logger.getLogger(BattleshipServerRMI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        returnList.sort((String o1, String o2) -> {
            return o1.compareTo(o2);
        });

        chat_index.keySet().stream().forEach((s) -> {
            try {
                chat_index.get(s).getAllUsers(returnList);
            } catch (RemoteException ex) {
                Logger.getLogger(BattleshipServerRMI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    @Override
    public void removeChatClient(IChatClient client, String name) throws RemoteException {
        chat_index.remove(name);
        System.out.println("User left chat : " + name);
        index.get(name).showMessage("Disconnected from chat.", "Server message", JOptionPane.INFORMATION_MESSAGE);
        ArrayList<String> returnList = new ArrayList<>(chat_index.size());
        chat_index.keySet().stream().forEach((s) -> {
            returnList.add(s);
        });

        returnList.sort((String o1, String o2) -> {
            return o1.compareTo(o2);
        });

        // in case the user switched login name and still are in chat.
        client.newMessage("<Server>", "Disconnected from chat.");

        chat_index.keySet().stream().forEach((s) -> {
            try {
                chat_index.get(s).newMessage("<Server>", name + " disconnected.");
                chat_index.get(s).getAllUsers(returnList);
            } catch (RemoteException ex) {
                Logger.getLogger(BattleshipServerRMI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @Override
    public void updateChatClient(IChatClient client, String oldUser, String newUser) throws RemoteException {
        if (chat_index.containsKey(oldUser)) {
            IChatClient c = chat_index.get(oldUser);
            System.out.println("User chat ID updated : " + oldUser + " > " + newUser);
            removeChatClient(client, oldUser);
            registerChatClient(c, newUser);
        } else {
            System.out.println("User " + oldUser + " was not in chat index, adding as new user.");
            registerChatClient(client, newUser);
        }
    }

    @Override
    public void sendMessage(IChatClient client, String name, String message) throws RemoteException {
        chat_messages.put(name, message);
        for (IChatClient c : chat_index.values()) {
            c.newMessage(name, message);
        }
    }

    private static class PublicMessage implements Runnable {

        private final String origin;
        private final String message;
        private final String title;
        private final int modal;

        public PublicMessage(final String origin, final String message, final String title, final int modal) {
            this.origin = origin;
            this.message = message;
            this.title = title;
            this.modal = modal;
        }

        @Override
        public void run() {
            System.out.println("Player : " + origin + " is sending public RMI message");
            bg.getPlayers().keySet().parallelStream().filter(s -> (!s.equals(origin))).forEach(s -> {
                try {
                    bg.getPlayers().get(s).showMessage(message, title, modal);
                } catch (final RemoteException ex) {
                    Logger.getLogger(BattleshipServerRMI.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    private static class RegisterClient implements Runnable {

        private final String playerName;
        private final IClientRMI clientInterface;

        public RegisterClient(final String playerName, final IClientRMI clientInterface) {
            this.playerName = playerName;
            this.clientInterface = clientInterface;
        }

        @Override
        public void run() {
            try {
                if (index.contains(playerName)) {
                    System.out.println("User updated to index : " + playerName);
                    index.get(playerName).showMessage("You have been unregistered in the server index.", "Server notification", JOptionPane.WARNING_MESSAGE);
                    clientInterface.showMessage("You have been updated in the server index", "Server notification", JOptionPane.INFORMATION_MESSAGE);
                } else if (index.containsValue(clientInterface)) {
                    /* get the key and kill it */
                    index.keySet().parallelStream().filter(key -> (index.get(key).equals(clientInterface))).forEach(key -> {
                        index.remove(key);
                    });
                } else {
                    System.out.println("User added to index : " + playerName);
                    //clientInterface.showMessage("You have been added to the server index", "Server notification", JOptionPane.INFORMATION_MESSAGE);
                }
                index.put(playerName, clientInterface);
                players.put(playerName, clientInterface.getPlayer());
                final Player p = clientInterface.getPlayer();
                final GameSession gs = new GameSession(p, clientInterface);
                final String ID = bg.updateSessionID(gs);
                gs.setGameSessionID(ID);
                sessions.put(ID, gs);
                clientInterface.updateSessionID(ID);
            } catch (final RemoteException re) {
                Logger.getLogger(BattleshipServerRMI.class.getName()).log(Level.SEVERE, "Client could not be reached {0}", clientInterface);
                Logger.getLogger(BattleshipServerRMI.class.getName()).log(Level.SEVERE, null, re);
                index.remove(playerName);
            }

        }
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
                        chat_index.remove(s);
                    }
                }
                if (index.size() != clientCount.get()) {
                    updateUser();
                }
                System.out.println("Maintenance : Removed " + count + " clients from index.");

                count = 0;
                for (final String s : sessions.keySet()) {
                    final GameSession gs = sessions.get(s);
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

                if (!chat_index.isEmpty()) {
                    final ArrayList<String> list = new ArrayList<>();
                    list.addAll(chat_index.keySet());
                    list.sort((String o1, String o2) -> {
                        return o1.compareTo(o2);
                    });
                    chat_index.keySet().stream().forEach((s) -> {
                        try {
                            chat_index.get(s).getAllUsers(list);
                        } catch (RemoteException ex) {
                            Logger.getLogger(BattleshipServerRMI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }

                System.out.println("Maintenance : Removed " + count + " sessions.");
            }
        }
    }
}
