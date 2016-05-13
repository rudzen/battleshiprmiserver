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
import rest.entities.Lobby;

/**
 *
 * @author Rudy Alex Kohn <s133235@student.dtu.dk>
 */
public class NewLobby extends GetAbstract {

    private final int playerID;
    
    public NewLobby(final IClientListener client, final int playerID) {
        super(client);
        this.playerID = playerID;
    }

    @Override
    public void run() {
        System.out.println("Attempting to run newLobby()");
        final String s = rest.newLobby(Integer.toString(playerID), "2");
        rest.close();
        System.out.println("NewLobby response : " + s);
        Lobby l = new Gson().fromJson(s, Lobby.class);
        try {
            client.setLobbyID(l.getLobbyid());
            client.showMessage("Lobby with ID " + Integer.toString(l.getLobbyid()) + " created.", "New lobby created", JOptionPane.INFORMATION_MESSAGE);
        } catch (RemoteException ex) {
            Logger.getLogger(NewLobby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
