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
import com.google.gson.reflect.TypeToken;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import rest.entities.Lobby;
import interfaces.IClientRMI;

/**
 *
 * @author Rudy Alex Kohn (s133235@student.dtu.dk)
 */
public class GetLobbys extends GetAbstract implements Runnable {

    private final int playerID;
    
    public GetLobbys(final IClientRMI client, final int playerID) {
        super(client);
        this.playerID = playerID;
    }

    @Override
    public void run() {
        final String s = rest.getLobbies(Integer.toString(playerID));
        rest.close();
        final Gson g = new Gson();
        final HashMap<String, Lobby> fromServer = g.fromJson(s, new HashMapTypeToken().getType());
        final ArrayList<String> names = new ArrayList<>();
        fromServer.values().stream().forEach(l -> {
            names.add(Integer.toString(l.getLobbyid()) + " : " + l.getDefender().getPlayername());
        });
        names.add("Osten");
        try {
            client.playerList(names);
            //System.out.println("Lobby object : " + l.toString());
        } catch (final RemoteException ex) {
            Logger.getLogger(GetLobbys.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static class HashMapTypeToken extends TypeToken<HashMap<String, Lobby>> {}
}
