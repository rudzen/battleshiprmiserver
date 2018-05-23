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

package battleshiprmiserver;

import com.css.rmi.ServerTwoWaySocketFactory;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMISocketFactory;

/**
 *
 * @author Rudy Alex Kohn (s133235@student.dtu.dk)
 */
public final class Main {

    private static final boolean runRMI = true;
    private static final boolean runSOAP = false; // not implemented yet.. requires runRMI = true

    public static void main(final String[] args) {
        System.out.println("Loading battleship server, please wait.");
        try {

            if (args.length >= 1) {
                BattleshipServerRMIHelper.setArgs(args);
                System.out.println("Command line argument values changed to : " + Args.all());
            }

            if (runRMI) {
                /* set new socket through custom two-way socket factory */
                //RMISocketFactory.setSocketFactory(new ServerTwoWaySocketFactory());

                /* export the registry from the same JVM */
                LocateRegistry.createRegistry(Args.port);

                /* set the REST address to be used */
                if (System.getSecurityManager() == null) {
                    System.setSecurityManager(new SecurityManager());
                    System.setProperty("java.rmi.server.hostname", Args.ip);
                    System.out.println("SecurityManager created.");
                }

                /* Print the basic information about the server */
                new PrettyPrint(Args.ip, Args.port, Args.game_address).showMenu();

                final String registration = "rmi://" + Args.registry() + "/Battleship";

                /* Load the service */
                final BattleshipServerRMI server = new BattleshipServerRMI();

                /* Register with service so that clients can find us */
                Naming.rebind(registration, server);
                System.out.println("RMI Initialized.. " + registration + ' ');

//                if (runSOAP) {
//                    System.out.print("Initializing SOAP webservice.. ");
//
//                    Args.port++;
//                    boolean portOK = false;
//
//                    //IBattleShipSOAP soapServer = new BattleshipServerRMI();
//                    while (Args.port <= 65535) {
//                        try {
//                            Endpoint.publish(String.format("http://localhost/Battleship/soap", Args.ip, String.valueOf(Args.port)), server);
//                            portOK = true;
//                            break;
//                        } catch (final Exception e) {
//                            System.out.println(Arrays.toString(e.getStackTrace()));
//                            //System.out.println(e.getMessage());
//                            break;
//                            //System.out.println(String.format("\nFailed for port %s.\n Trying port %s", Args.port, ++Args.port));
//                            //System.out.print("Initializing SOAP webservice.. ");
//                        }
//                    }
//                }
            }
            System.out.print("OK.\n");
        } catch (final RemoteException re) {
            System.err.println("RMI Remote Error - " + re);
        } catch (final Exception e) {
            System.err.println("Error - " + e);
        }
    }

}
