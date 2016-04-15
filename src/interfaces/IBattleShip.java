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
package interfaces;

import game.data.Player;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The server's interface.
 * Called by the client.
 * @author rudz
 */
public interface IBattleShip extends Remote {

    /**
     * Registers a client at the server.
     * @param clientInterface The client's interface.
     * @return true if okay, else false.
     * @throws RemoteException 
     */
    boolean registerClient(IClientListener clientInterface) throws RemoteException;
    
    /**
     * Removes a client registration at the server.
     * @param clientInterface The client interface to remove
     * @return true if removed, otherwise false.
     * @throws RemoteException 
     */
    boolean removeClient(IClientListener clientInterface) throws RemoteException;

    /**
     * Get server information about me!
     * @return The player object for the client.
     * @throws RemoteException 
     */
    Player getMe() throws RemoteException; // PlayerDTO
    
    /**
     * Get the opponent information object.
     * @return The Player object which contains public information about the opponent.
     * @throws RemoteException 
     */
    Player getOther() throws RemoteException;

    /**
     * Let the server know at what location you attempted to fire a shot at.
     * @param x the X
     * @param y the Y
     * @param client Who the fuck is actually firering! :-)
     * @throws RemoteException 
     */
    void fireShot(int x, int y, IClientListener client) throws RemoteException;
    
    /**
     * Login attempt
     * @param user username
     * @param pw password
     * @return true/false depending on success.
     * @throws RemoteException 
     */
    boolean login(final String user, final String pw) throws RemoteException;
    
    
}
