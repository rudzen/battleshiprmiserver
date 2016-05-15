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
import com.google.gson.reflect.TypeToken;
import interfaces.IClientListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Get all playerID's from the REST server and pass them back to the RMI client.
 * @author Rudy Alex Kohn (s133235@student.dtu.dk)
 */
public class GetAllPlayerIDS extends GetAbstract {

    public GetAllPlayerIDS(final IClientListener client) {
        super(client);
    }

    @Override
    public void run() {
        final String s = rest.getPlayerIds();
        rest.close();
        if (s != null) {
            HashMap<String, rest.entities.Player> fromServer = new Gson().fromJson(s, new TypeToken<HashMap<String, rest.entities.Player>>() {}.getType());
            try {
                if (!fromServer.isEmpty()) {
                    final ArrayList<String> toClient = new ArrayList<>();
                    fromServer.keySet().stream().forEach((l) -> {
                        toClient.add(l + ":" + fromServer.get(l).getPlayername());
                    });
                    client.playerList(toClient);
                    fromServer.clear();
                    toClient.clear();
                    //client.showMessage("All player ids :\n" + fromServer.toString(), "Server message", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    client.showMessage("No player IDs found", "Server message", JOptionPane.ERROR_MESSAGE);
                }
            } catch (RemoteException ex) {
                Logger.getLogger(GetAllPlayerIDS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
