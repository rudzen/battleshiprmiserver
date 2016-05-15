///*
// * The MIT License
// *
// * Copyright 2016 Rudy Alex Kohn <s133235@student.dtu.dk>.
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in
// * all copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// * THE SOFTWARE.
// */
//package battleshiprmiserver.commander.tasks;
//
//import com.google.gson.Gson;
//import rest.BattleshipJerseyClient;
//import rest.BattleshipJerseyHelper;
//import dataobjects.Player;
//import interfaces.IClientListener;
//import java.rmi.RemoteException;
//import java.util.Arrays;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.JOptionPane;
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.core.MediaType;
//import org.glassfish.jersey.client.ClientProperties;
//import rest.entities.Lobby;
//import rest.entities.Ship;
//import rest.entities.ShipList;
//
///**
// * Thread to deploy board on server.
// *
// * @author Rudy Alex Kohn <s133235@student.dtu.dk>
// */
//public class DeployBoard implements Runnable {
//
//    private final Player player;
//    private final int lobbyID;
//    private final IClientListener client;
//
//    public DeployBoard(IClientListener client, final Player player, final int lobbyID) {
//        this.client = client;
//        this.player = player;
//        this.lobbyID = lobbyID;
//    }
//
//    @Override
//    public void run() {
//        try {
//            Client rest = ClientBuilder.newClient();
//            rest.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
//            //javax.ws.rs.core.Response res = rest.target("http://104.46.52.169:8080/BattleshipREST/test/res/new/lobby/playerid=1").request(MediaType.APPLICATION_JSON).put(Entity.json(null));
//            
//
//            
//
//            javax.ws.rs.core.Response res = rest.target(BattleshipJerseyClient.BASE_URI + "res/new/lobby/playerid=" + Integer.toString(playerID)).request(MediaType.APPLICATION_JSON).put(Entity.json(""));
//            Lobby l = new Gson().fromJson(res.readEntity(String.class), Lobby.class);
//            res.close();
//            rest.close();
//
//            final String[] jSonShips = BattleshipJerseyHelper.shipsToString(player.getShips());
//            final String response = rest.deployBoard(Integer.toString(lobbyID), Integer.toString(player.getId()), jSonShips);
//            rest.close();
//            System.out.println(player.getName() + " board deploy response : " + response);
//            //System.out.println(player.getName() + " json board = " + Arrays.toString(jSonShips));
//            try {
//                if (response.equals("succes")) {
//                    client.showMessage("Ships deployed.", "Server message", JOptionPane.INFORMATION_MESSAGE);
//                } else {
//                    client.showMessage("Unable to deploy ships\nReason : Unknown.", "Server message", JOptionPane.ERROR_MESSAGE);
//                }
//            } catch (RemoteException ex) {
//                Logger.getLogger(DeployBoard.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } catch (NumberFormatException nfe) {
//            Logger.getLogger(DeployBoard.class.getName()).log(Level.SEVERE, null, nfe);
//        }
//    }
//
//}
