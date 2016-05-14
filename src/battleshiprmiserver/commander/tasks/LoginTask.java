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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import rest.BattleshipJerseyHelper;
import rest.DatabaseJerseyClient;

/**
 *
 * @author Rudy Alex Kohn <s133235@student.dtu.dk>
 */
public class LoginTask implements Runnable {

    private final IClientListener client;
    private final String u;
    private final String p;

    private static final String wrong = "Wrong password";
    private static final String login = "Login";

    public LoginTask(IClientListener client, final String u, final String p) {
        this.client = client;
        this.u = u;
        this.p = p;
    }

    @Override
    public void run() {
        DatabaseJerseyClient rest = new DatabaseJerseyClient();
        String s = rest.loginBA(u, p);
        System.out.println("Login response : " + s);
        try {
            if (s.contains(wrong)) {
                /* wrong password answer from server, let the client know */
                client.showMessage(wrong, login, JOptionPane.ERROR_MESSAGE);
            } else if (s.contains("com.mysql.jdbc.exceptions.jdbc4.MySQL")) {
                /* sql exception */
                @SuppressWarnings("ThrowableResultIgnored")
                SQLException se = new Gson().fromJson(s, SQLException.class);
                client.showMessage("SQL Error : " + se.getMessage(), login, JOptionPane.ERROR_MESSAGE);
            } else if (s.equals("1")) {
                /* server returns "1" if the player was created */
                s = rest.loginBA(u, p);
                Player p1 = BattleshipJerseyHelper.restPlayerToLocal(new Gson().fromJson(s, rest.entities.Player.class));
                client.setPlayer(p1);
                client.showMessage("Player created and Logged in as (ID : NAME) " + Integer.toString(p1.getId()) + " : " + p1.getName(), login, JOptionPane.INFORMATION_MESSAGE);
            } else {
                Player p1 = BattleshipJerseyHelper.restPlayerToLocal(new Gson().fromJson(s, rest.entities.Player.class));
                client.setPlayer(p1);
                client.showMessage("Logged in as (ID : NAME) " + Integer.toString(p1.getId()) + " : " + p1.getName(), login, JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(LoginTask.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rest.close();
        }
    }

}
