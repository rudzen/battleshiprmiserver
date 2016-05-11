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

import battleshiprmiserver.rest.BattleshipJerseyClient;
import battleshiprmiserver.rest.BattleshipJerseyHelper;
import battleshiprmiserver.threads.Runner;
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
public class DeployShips extends GetAbstract implements Runnable {

    private final Player player;
    private final int lobbyID;

    public DeployShips(IClientListener client, final Player player, final int lobbyID) {
        super(client);
        this.player = player;
        this.lobbyID = lobbyID;
    }

    @Override
    public void run() {
        try {
            BattleshipJerseyClient restClient = new BattleshipJerseyClient();
            final String response = restClient.deployBoard("1", "1", BattleshipJerseyHelper.shipsToString(player.getShips()));
            restClient.close();
            System.out.println("Response : " + response);
            try {
                if (response.equals("succes")) {
                    client.showMessage("Ships deployed.", "Server message", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    client.showMessage("Unable to deploy ships\nReason : Unknown.", "Server message", JOptionPane.ERROR_MESSAGE);
                }
            } catch (RemoteException ex) {
                Logger.getLogger(DeployShips.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NumberFormatException nfe) {
            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, nfe);
        }
    }

}
