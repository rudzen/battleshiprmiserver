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

import args.MainArgsHandler;
import args.intervals.GenericInterval;
import args.intervals.Interval;
import game.data.Player;
import interfaces.IBattleShip;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import interfaces.IClientListener;
import java.util.List;

/**
 *
 * @author rudz
 */
public class BattleshipServerRMI extends UnicastRemoteObject implements IBattleShip, Runnable {

    private static final long serialVersionUID = 3089432827583994107L;

    private PrettyPrint pp;

    private static boolean verbose = true;

    private final CopyOnWriteArrayList<IClientListener> list = new CopyOnWriteArrayList<>();

    private volatile int x, y;

    public BattleshipServerRMI() throws java.rmi.RemoteException {
        // Assign a default setting for the temperature
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
            notifyListeners();
        }
    }

    private static String setArgs(final String[] args) {

        final MainArgsHandler argsHandler = MainArgsHandler.getHandler();
        String retVal = null;

        final Interval<Integer> ZERO_OR_ONE = new GenericInterval<>(0, 1);
        argsHandler.permitVariable("registry", ZERO_OR_ONE, "Set the RMI registry information.");

        try {
            argsHandler.processMainArgs(args);
            List<String> argsReceived;
            argsReceived = argsHandler.getValuesFromVariable("registry");
            if (!argsReceived.isEmpty()) {
                retVal = argsReceived.get(0);
            }
        } catch (final IllegalArgumentException iae) {
            System.out.println(argsHandler.getUsageSummary());
            System.exit(0);
        }
        System.out.println();
        return retVal;
    }

    private void notifyListeners() {
        // Notify every listener in the registered list
        for (int i = 0; i < list.size(); i++) {
            IClientListener listener = (IClientListener) list.get(i);
            try {
                listener.shotFired(x, y, true);
            } catch (RemoteException ex) {
                /* if the exception is caught, the client is most likely not connected, so kill it without mercy */
                Logger.getLogger(BattleshipServerRMI.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("removing listener -" + listener);
                // Remove the listener
                list.remove(listener);
            }
        }
    }

    public static void main(String args[]) {
        System.out.println("Loading battleship server");
        // Only required for dynamic class loading
        //System.setSecurityManager ( new RMISecurityManager() );
        try {

            java.rmi.registry.LocateRegistry.createRegistry(1099); // start i server-JVM

            // Load the service
            BattleshipServerRMI server = new BattleshipServerRMI();
            // Check to see if a registry was specified
            String registry = "localhost";

            if (args.length >= 1) {
                setArgs(args);
                registry = args[0];
            }

            /* update the prettyprinter */
            server.setPp(new PrettyPrint(registry));
            server.getPp().showMenu();

            // Registration format //registry_hostname:port
            // service // Note the :port field is optional
            String registration = "rmi://" + registry + "/Battleship";

            // Register with service so that clients can find us
            Naming.rebind(registration, server);
            // Create a thread, and pass the server.
            // This will activate the run() method, and
            // trigger regular coordinate changes.
            Thread thread = new Thread(server);
            thread.start();
        } catch (RemoteException re) {
            System.err.println("Remote Error - " + re);
        } catch (Exception e) {
            System.err.println("Error - " + e);
        }
    }

    @Override
    public boolean registerClient(IClientListener clientInterface) throws RemoteException {
        System.out.println("adding listener -" + clientInterface);
        return list.add(clientInterface);
    }

    @Override
    public boolean removeClient(IClientListener clientInterface) throws RemoteException {
        System.out.println("removing listener -" + clientInterface);
        return list.remove(clientInterface);
    }

    @Override
    public Player getMe() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Player getOther() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void fireShot(int x, int y, IClientListener client) throws RemoteException {
        System.out.println("Client " + client + " fired at [" + x + ", " + y + "]");
    }

    @Override
    public boolean login(String user, String pw) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
