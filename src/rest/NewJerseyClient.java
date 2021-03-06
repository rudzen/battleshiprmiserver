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

package rest;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:GenericResource [res]<br>
 * USAGE:
 * <pre>
 *        NewJerseyClient client = new NewJerseyClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Rudy Alex Kohn (s133235@student.dtu.dk)
 */
public class NewJerseyClient {

    private final WebTarget webTarget;
    private final Client client;
    private static final String BASE_URI = "http://localhost:8080/BattleshipREST/test";

    public NewJerseyClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("res");
    }

    public String newLobbyWithOpponent(final String playerid, final String opponentid) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("new/lobby/playerid={0}/opponent={1}", new Object[]{playerid, opponentid})).request().put(null, String.class);
    }

    public String getPlayer(final String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("get/player/{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String waitForDeploy(final String lobby) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("wait/deploy/{0}", new Object[]{lobby}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String getLobby(final String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("get/lobby/{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String getFreeLobbies(final String player) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("get/free_lobbies/{0}", new Object[]{player}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String getBoard(final String lobbyid) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("get/board/lobbyid={0}", new Object[]{lobbyid}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String getServerJson() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("serverJson");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String createPlayer(final String playerName) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("new/player/playerName={0}", new Object[]{playerName})).request().put(null, String.class);
    }

    public String getBoardShips(final String lobbyid, final String playerid) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("get/board/ships/lobbyid={0}/playerid={1}", new Object[]{lobbyid, playerid}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String deployShipsRMI(final String lobbyid, final String player) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("deploy_ships/lobby={0}/player={1}", new Object[]{lobbyid, player})).request().post(null, String.class);
    }

    public String findPlayer(final String name) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("find/player/{0}", new Object[]{name}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String joinLobby(final String lobbyid, final String playerid) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("join_lobby/lobbyid={0}/playerid={1}", new Object[]{lobbyid, playerid})).request().post(null, String.class);
    }

    public String buyUpgrade(final String id, final String what) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("player={0}/buy={1}", new Object[]{id, what})).request().post(null, String.class);
    }

    public String waitForOpponent(final String lobbyid, final String playerid) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("lobby={0}/wait/player={1}", new Object[]{lobbyid, playerid}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String getLobbies(final String player) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("get/lobbies/{0}", new Object[]{player}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public <T> T getServerXml(final Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("serverxml");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public String getPlayerIds() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("get/players");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String shoot(final String lobbyid, final String playerid, final String x, final String y) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("fire/lobby={0}/playerid={1}/x={2}/y={3}", new Object[]{lobbyid, playerid, x, y}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String newLobby(final String playerid) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("new/lobby/playerid={0}", new Object[]{playerid})).request().put(null, String.class);
    }

    public String deployBoard(final String lobbyid, final String playerid, final String type1, final String x1, final String y1, final String horizontal1, final String type2, final String x2, final String y2, final String horizontal2, final String type3, final String x3, final String y3, final String horizontal3, final String type4, final String x4, final String y4, final String horizontal4, final String type5, final String x5, final String y5, final String horizontal5) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("deploy_board/lobbyid={0}/playerid={1}/ship1={2}/x={3}/y={4}/horizontal={5}/ship2={6}/x={7}/y={8}/horizontal={9}/ship3={10}/x={11}/y={12}/horizontal={13}/ship4={14}/x={15}/y={16}/horizontal={17}/ship5={18}/x={19}/y={20}/horizontal={21}", new Object[]{lobbyid, playerid, type1, x1, y1, horizontal1, type2, x2, y2, horizontal2, type3, x3, y3, horizontal3, type4, x4, y4, horizontal4, type5, x5, y5, horizontal5})).request().put(null, String.class);
    }

    public String getMoves(final String lobbyid) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("get/moves/lobby={0}", new Object[]{lobbyid}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String deployShips(final String lobbyid, final String playerid, final String ships) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("deploy_ships/{0}/{1}/{2}", new Object[]{lobbyid, playerid, ships})).request().put(null, String.class);
    }

    public void close() {
        client.close();
    }
    
}
