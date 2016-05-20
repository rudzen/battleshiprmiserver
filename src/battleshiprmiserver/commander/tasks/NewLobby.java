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
package battleshiprmiserver.commander.tasks;

import com.google.gson.Gson;
import interfaces.IClientListener;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.client.ClientProperties;

import rest.entities.Lobby;

/**
 *
 * @author Rudy Alex Kohn <s133235@student.dtu.dk>
 */
public class NewLobby implements Runnable {

    private final int playerID;
    private final IClientListener client;
    
    public NewLobby(final IClientListener client, final int playerID) {
        this.client = client;
        this.playerID = playerID;
    }

    @Override
    public void run() {
        final Client rest = ClientBuilder.newClient();
        rest.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        final javax.ws.rs.core.Response res = rest.target("http://104.46.52.169:8080/BattleshipREST/test/res/new/lobby/playerid=" + Integer.toString(playerID)).request(MediaType.APPLICATION_JSON).put(Entity.json(""));
        //javax.ws.rs.core.Response res = rest.target("http://localhost:8080/BattleshipREST/test/res/new/lobby/playerid=" + Integer.toString(playerID)).request(MediaType.APPLICATION_JSON).put(Entity.json(""));
        final String response = res.readEntity(String.class);
        System.out.println("newLobby response : " + response);
        final Lobby l = new Gson().fromJson(response, Lobby.class);
        res.close();
        rest.close();
        
        try {
            client.setLobbyID(l.getLobbyid());
            client.showMessage("Lobby with ID : " + Integer.toString(l.getLobbyid()) + " created.", "Lobby created OK", JOptionPane.INFORMATION_MESSAGE);
        } catch (final RemoteException ex) {
            Logger.getLogger(NewLobby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
