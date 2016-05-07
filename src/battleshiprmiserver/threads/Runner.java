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
package battleshiprmiserver.threads;

import battleshiprmiserver.rest.BattleshipJerseyClient;
import battleshiprmiserver.rest.BattleshipJerseyHelper;
import com.google.gson.Gson;
import dataobjects.Player;
import game.Messages;
import interfaces.IClientListener;
import java.awt.Point;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import rest.Lobby;

/**
 * Overload of Runnable to allow the data structure to exist inside it! ;-)
 * @author Rudy Alex Kohn <s133235@student.dtu.dk>
 */
public class Runner implements Runnable {

    private final IClientListener client;
    private final Player player;
    private Point shotDest = null;
    
    private final Messages.MessageType type;
    
    public Runner(final IClientListener client, final Player player, final Messages.MessageType type) {
        this.client = client;
        this.player = player;
        this.type = type;
    }
    
    public Runner(final IClientListener client, final Player player, final Messages.MessageType type, final Point shotDest) {
        this(client, player, type);
        this.shotDest = shotDest;
    }
    
    public Runner(final IClientListener client, final Player player, final Messages.MessageType type, final int xShot, final int yShot) {
        this(client, player, type, new Point(xShot, yShot));
    }
    
    @Override
    public void run() {
        System.out.print("Sending REST for " + player.getName() + ", type : ");
        BattleshipJerseyClient rest = new BattleshipJerseyClient();
        if (type == Messages.MessageType.DEPLOY_SHIPS) {
            try {
                client.showMessage(rest.deployBoard("1", "1", BattleshipJerseyHelper.shipsToString(player.getShips())), "Response", 0);
            } catch (RemoteException ex) {
                Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("deploy ships.");
        } else if (type == Messages.MessageType.GAME_TIMEOUT) {
            System.out.println("game timeout.");
        } else if (type == Messages.MessageType.SHOT_FIRED) {
            System.out.println("shot fired.");
        } else if (type == Messages.MessageType.GET_LOBBYS) {
            final BattleshipJerseyClient restClient = new BattleshipJerseyClient();
            final String s = restClient.getLobbies();
            restClient.close();
            System.out.println("Lobbys from server : " + s);
            Gson g = new Gson();
            Lobby l = g.fromJson(s, Lobby.class);
            System.out.println("Lobby object : " + l.toString());
            try {
                client.showMessage(s, "Lobbys", 0);
            } catch (RemoteException ex) {
                Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
            }
        }
        try {
            client.showMessage("From Runner", client.toString(), JOptionPane.WARNING_MESSAGE);
        } catch (RemoteException ex) {
            /* TODO : add removal of the client from the main mapping HERE!! */
            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
