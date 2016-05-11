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
import java.awt.Point;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import rest.entities.Fire;

/**
 *
 * @author Rudy Alex Kohn <s133235@student.dtu.dk>
 */
public class FireShot extends GetAbstract {

    private final int lobbyID;
    private final int playerID;
    private final int x;
    private final int y;

    public FireShot(IClientListener client, final int lobbyID, final int playerID, final int x, final int y) {
        super(client);
        this.lobbyID = lobbyID;
        this.playerID = playerID;
        this.x = x;
        this.y = y;
    }

    public FireShot(IClientListener client, final int lobbyID, final int playerID, final Point loc) {
        this(client, lobbyID, playerID, loc.x, loc.y);
    }

    @Override
    public void run() {
        final String s = rest.shoot(Integer.toString(lobbyID), Integer.toString(playerID), Integer.toString(x), Integer.toString(y));
        rest.close();
        Fire f = new Gson().fromJson(s, Fire.class);
        try {
            if (f.getStatus().equals("error")) {
                client.showMessage(f.getError() + "\n" + f.getFire().toString(), "Shot fired", JOptionPane.ERROR_MESSAGE);
            } else {
                client.shotFired(f.getFire().x, f.getFire().y, false); // hmm
                client.showMessage("Shot fired OK at " + f.getFire().toString(), "Shot fired", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(FireShot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
