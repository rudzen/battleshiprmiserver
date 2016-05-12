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

import rest.BattleshipJerseyHelper;
import com.google.gson.Gson;
import dataobjects.Player;
import interfaces.IClientListener;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Rudy Alex Kohn <s133235@student.dtu.dk>
 */
public class GetPlayerID extends GetAbstract implements Runnable {

    private final String name;

    /**
     *
     * @param name
     * @param client
     */
    public GetPlayerID(final IClientListener client, final String name) {
        super(client);
        this.name = name;
    }

    @Override
    public void run() {
        final String s = rest.getPlayer(name);
        rest.close();
        try {
            if (s.equals("\"error\":\"player not found\"")) {
                client.showMessage("Unable to get player from database", "Error", JOptionPane.ERROR);
            } else {
                rest.entities.Player p = new Gson().fromJson(s, rest.entities.Player.class);
                Player newPlayer = BattleshipJerseyHelper.restPlayerToLocal(p);
                client.setPlayer(newPlayer);
                client.showMessage("Player information updated.", "Server message", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(GetPlayerID.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
