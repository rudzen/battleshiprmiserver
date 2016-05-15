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
import dataobjects.Player;
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
import rest.entities.ShipResponse;
import rest.entities.Result;

/**
 *
 * @author Rudy Alex Kohn <s133235@student.dtu.dk>
 */
public class DeployShips extends GetAbstract {

    private final Player player;
    private final int lobbyID;

    public DeployShips(IClientListener client, final int lobbyID, final Player player) {
        super(client);
        this.player = player;
        this.lobbyID = lobbyID;
    }

    @Override
    public void run() {
        final Gson g = new Gson();
        final ShipResponse[] sl = new ShipResponse[5];
        int pos = 0;

        ShipResponse r;
        for (dataobjects.Ship pShip : player.getShips()) {
            sl[pos] = new ShipResponse();
            sl[pos].name = pShip.getShipType();
            sl[pos].l = pShip.getLength();
            sl[pos].x = pShip.getStartX();
            sl[pos].y = pShip.getStartY();
            sl[pos++].h = pShip.isHorizontal();
        }
        
        Client rest = ClientBuilder.newClient();
        rest.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        String responseJSon = g.toJson(sl, ShipResponse[].class);
        String thething = "http://104.46.52.169:8080/BattleshipREST/test/res/deploy_ships/" + Integer.toString(lobbyID) + "/" + Integer.toString(player.getId()) + "/" + responseJSon;
        //final String thething = "http://localhost:8080/BattleshipREST/test/res/deploy_ships/" + Integer.toString(lobbyID) + "/" + Integer.toString(player.getId()) + "/" + responseJSon;
        //thething = thething.replace(" ", "%20");
        System.out.println("deploy() url " + thething);
        javax.ws.rs.core.Response res = rest.target(thething).request(MediaType.APPLICATION_JSON).put(Entity.json(null));
        String response = res.readEntity(String.class);
        Result result = g.fromJson(response, Result.class);
        System.out.println("deploy() res " + response);
        res.close();
        rest.close();

//        final String s = rest.deployShips(Integer.toString(lobbyID), Integer.toString(player.getId()), g.toJson(sl));
//        rest.close();
//        Result result = g.fromJson(s, Result.class);

        try {
            if (result.succes) {
                client.deployed(true, "Opponent");
                client.showMessage("Ships deployed correctly", "Server message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                client.deployed(false, "Fail");
                client.showMessage("Failed to deploy correctly", "Server message", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(DeployShips.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
