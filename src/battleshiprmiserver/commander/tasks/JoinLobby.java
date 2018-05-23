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

package battleshiprmiserver.commander.tasks;

import com.google.gson.Gson;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import rest.entities.Lobby;
import interfaces.IClientRMI;

/**
 *
 * @author Rudy Alex Kohn (s133235@student.dtu.dk)
 */
public class JoinLobby extends GetAbstract {

    private final int lobbyID;
    private final int playerID;

    public JoinLobby(final IClientRMI client, final int lobbyID, final int playerID) {
        super(client);
        this.lobbyID = lobbyID;
        this.playerID = playerID;
    }

    @Override
    public void run() {
        final String s = rest.joinLobby(Integer.toString(lobbyID), Integer.toString(playerID));
        rest.close();
        try {
            if (s.contains("error")) {
                client.showMessage("Unable to join lobby with id : " + Integer.toString(lobbyID), "Lobby join failed.", JOptionPane.ERROR_MESSAGE);
                client.canPlay(false);
                client.setLobbyID(-1);
            } else {
                final Lobby l = new Gson().fromJson(s, Lobby.class);
                client.showMessage("Lobby joined. New lobby id is " + Integer.toString(l.getLobbyid()), "Lobby joined", JOptionPane.INFORMATION_MESSAGE);
                client.setLobbyID(l.getLobbyid());
            }

        } catch (final RemoteException ex) {
            Logger.getLogger(JoinLobby.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
