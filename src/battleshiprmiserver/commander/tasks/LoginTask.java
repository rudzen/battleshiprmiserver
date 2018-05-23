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

import org.glassfish.jersey.client.ClientProperties;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dataobjects.Player;
import rest.BattleshipJerseyHelper;
import interfaces.IClientRMI;

/**
 * Login thread for RMI klient using REST interface.<br>
 * If the user does not exist, user is created and will still be send back.
 *
 * @author Rudy Alex Kohn (s133235@student.dtu.dk)
 */
public class LoginTask implements Runnable {

    private final IClientRMI client;
    private final String u;
    private final String p;

    private static final String wrong = "Wrong password";
    private static final String login = "Login";

    public LoginTask(final IClientRMI client, final String u, final String p) {
        this.client = client;
        this.u = u;
        this.p = p;
    }

    @Override
    public void run() {
        final Client rest = ClientBuilder.newClient();
        rest.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        //rest.property(ClientProperties.CONNECT_TIMEOUT, 2000);
        //rest.property(ClientProperties.READ_TIMEOUT, 2000);

        //Response res = rest.target("http://localhost:8080/BattleshipREST/test/database/login/BA/playerid=" + BattleshipJerseyHelper.fixString(u) + "/password=" + BattleshipJerseyHelper.fixString(p)).request(MediaType.APPLICATION_JSON).get();
        Response res = rest.target("http://ubuntu4.javabog.dk:6004/BattleshipREST/test/database/login/BA/playerid=" + u + "/password=" + p).request(MediaType.APPLICATION_JSON).get();
//        Response res = rest.target("http://104.46.52.169:8080/BattleshipREST/test/database/login/BA/playerid=" + BattleshipJerseyHelper.fixString(u) + "/password=" + BattleshipJerseyHelper.fixString(p)).request(MediaType.APPLICATION_JSON).get();

        String s = res.readEntity(String.class);
        //System.out.println(s);

        //DatabaseJerseyClient rest = new DatabaseJerseyClient();
        //String s = rest.loginBA(u, p);
        //System.out.println("Login response : " + s);
        try {
            if (s == null || s.isEmpty()) {
                client.showMessage("Unable to perform login.", "Login error", JOptionPane.ERROR_MESSAGE);
            } else if (s.contains(wrong)) {
                /* wrong password answer from server, let the client know */
                client.showMessage(wrong, login, JOptionPane.ERROR_MESSAGE);
            } else if (s.contains("com.mysql.jdbc.exceptions.jdbc4.MySQL")) {
                /* sql exception */
                @SuppressWarnings("ThrowableResultIgnored")
                final SQLException se = new Gson().fromJson(s, SQLException.class);
                client.showMessage("SQL Error : " + se.getMessage(), login, JOptionPane.ERROR_MESSAGE);
            } else if ("1".equals(s)) {
                /* server returns "1" if the player was created */
                res = rest.target("http://104.46.52.169:8080/BattleshipREST/test/database/login/BA/playerid=s144868/password=xxx").request(MediaType.APPLICATION_JSON).get();
                s = res.readEntity(String.class);
                //s = rest.loginBA(u, p);
                final Player p1 = BattleshipJerseyHelper.restPlayerToLocal(new Gson().fromJson(s, rest.entities.Player.class));
                client.setPlayer(p1, false);
                client.showMessage("Player created and Logged in as (ID : NAME) " + Integer.toString(p1.getId()) + " : " + p1.getName(), login, JOptionPane.INFORMATION_MESSAGE);
            } else {
                final Player p1 = BattleshipJerseyHelper.restPlayerToLocal(new Gson().fromJson(s, rest.entities.Player.class));
                client.setPlayer(p1, false);
                client.showMessage("Logged in as (ID : NAME) " + Integer.toString(p1.getId()) + " : " + p1.getName(), login, JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (final RemoteException ex) {
            Logger.getLogger(LoginTask.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rest.close();
        }
    }

}
