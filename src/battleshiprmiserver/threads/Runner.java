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

import rest.BattleshipJerseyClient;
import rest.BattleshipJerseyHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dataobjects.Player;
import game.GameSession;
import game.Messages;
import interfaces.IClientListener;
import java.awt.Point;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import login.Login;
import rest.entities.Lobby;

/**
 * Overload of Runnable to allow the data structure to exist inside it! ;-)
 *
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
        StringBuilder response = new StringBuilder();
        if (type == Messages.MessageType.DEPLOY_SHIPS) {
            
        } else if (type == Messages.MessageType.GAME_TIMEOUT) {
            System.out.println("game timeout.");
        } else if (type == Messages.MessageType.SHOT_FIRED) {
            rest.shoot("1", Integer.toString(shotDest.x), Integer.toString(shotDest.y), Integer.toString(player.getId()));
            System.out.println("shot fired.");
        } else if (type == Messages.MessageType.GET_LOBBYS) {
            final BattleshipJerseyClient restClient = new BattleshipJerseyClient();
            final String s = restClient.getLobbies();
            restClient.close();
            //System.out.println("Lobbys from server : " + s);
            Gson g = new Gson();
            //HashMap<Integer, Lobby> fromServer = g.fromJson(s, new TypeToken<HashMap<Integer, Lobby>>(){}.getType());
            HashMap<String, Lobby> fromServer = g.fromJson(s, new TypeToken<HashMap<String, Lobby>>(){}.getType());
            GameSession gs;
            ArrayList<String> names = new ArrayList<>();
            fromServer.values().stream().forEach((l) -> {
                //System.out.println("Printing new lobby : " + l.getActiveId());
                //System.out.println(BattleshipJerseyHelper.convertLobby(l).toString());
                names.add(Integer.toString(l.getLobbyid()) + " : " + l.getDefender().getPlayername());
//                System.out.println(l.toString());
            });
            try {
                client.playerList(names);
                //System.out.println("Lobby object : " + l.toString());
            } catch (RemoteException ex) {
                Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (type == Messages.MessageType.LOGIN) {


        }

    }

}
