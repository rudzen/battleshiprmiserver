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

import game.data.PWdto;
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
     * Registers a client at the server.<br>
     * Used when creating a new session.
     * @param clientInterface The client's interface.
     * @return true if okay, else false.
     * @throws RemoteException 
     */
    boolean registerClient(final IClientListener clientInterface) throws RemoteException;

    /**
     * Registers a client at the server with a specific session.<br>
     * Used when the client knows the sessionID to join.
     * @param clientInterface The client's interface.
     * @param sessionID
     * @return true if okay, else false.
     * @throws RemoteException 
     */
    boolean registerClient(final IClientListener clientInterface, final String sessionID) throws RemoteException;

    
    /**
     * Removes a client registration at the server.
     * @param clientInterface The player to remove
     * @return true if removed, otherwise false.
     * @throws RemoteException 
     */
    boolean removeClient(final IClientListener clientInterface) throws RemoteException;

    /**
     * Removes a client registration at the server.<br>
     * @param clientInterface The player to remove
     * @param sessionID If the player is currently in a session, let the server know which session it is.
     * @return true if removed, otherwise false.
     * @throws RemoteException 
     */
    boolean removeClient(final IClientListener clientInterface, final String sessionID) throws RemoteException;
    
    /**
     * Get the opponent information object.<br>
     * @param clientInterface The player to remove
     * @param sessionID The current session
     * @return The Player object which contains public information about the opponent (without token etc)
     * @throws RemoteException 
     */
    Player getOther(final IClientListener clientInterface, final String sessionID) throws RemoteException;

    /**
     * Let the server know at what location you attempted to fire a shot at.
     * @param x the X
     * @param y the Y
     * @param clientInterface Who the fuck is actually shooting?! :-)
     * @param sessionID The sessionID of the game currently being played
     * @throws RemoteException 
     */
    void fireShot(int x, int y, final IClientListener clientInterface, final String sessionID) throws RemoteException;
    
    /**
     * Login attempt
     * @param user username
     * @param pw password
     * @return true/false depending on success.
     * @throws RemoteException 
     */
    boolean login(final String user, final String pw) throws RemoteException;
    

    /**
     * Sends a PWdto to the RMI server, which will then split it apart.
     * @param dto The object to send
     * @return true if the login was ok, otherwise false.
     * @throws RemoteException 
     */
    boolean login(final PWdto dto) throws RemoteException;
    
    
    /**
     * User has timed out, this method is ONLY called through the timeout timer.
     * @param player The player who timed out
     * @param sessionID The sessionID of the game currently being played
     * @throws RemoteException 
     */
    void forefeit(final Player player, final String sessionID) throws RemoteException;
    
    
}
