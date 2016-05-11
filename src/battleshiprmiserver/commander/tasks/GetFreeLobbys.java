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
import battleshiprmiserver.threads.Runner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import interfaces.IClientListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import rest.Lobby;

/**
 *
 * @author Rudy Alex Kohn <s133235@student.dtu.dk>
 */
public class GetFreeLobbys extends GetAbstract implements Runnable {
    
    public GetFreeLobbys(final IClientListener client) {
        super(client);
    }

    @Override
    public void run() {
        final BattleshipJerseyClient restClient = new BattleshipJerseyClient();
        final String s = restClient.getFreeLobbies();
        restClient.close();
        HashMap<String, Lobby> fromServer = new Gson().fromJson(s, new TypeToken<HashMap<String, Lobby>>() {}.getType());
        ArrayList<String> freeLobbys = new ArrayList<>();
        fromServer.values().stream().forEach((l) -> {
            freeLobbys.add(Integer.toString(l.getLobbyid()) + ":" + l.getDefender().getPlayername());
        });
        try {
            client.setFreeLobbies(freeLobbys);
        } catch (RemoteException ex) {
            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
