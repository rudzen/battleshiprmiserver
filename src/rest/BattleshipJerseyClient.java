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
package rest;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:GenericResource [res]<br>
 * USAGE:
 * <pre>
 *        BSRestClient client = new BSRestClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Rudy Alex Kohn <s133235@student.dtu.dk>
 */
public class BattleshipJerseyClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://104.46.52.169:8080/BattleshipREST/test/";
    //private static final String BASE_URI = "http://localhost:8080/BattleshipREST/test";

    public BattleshipJerseyClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("res");
    }

    public String getPlayer(String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("get/player/{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String getFreeLobbies() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("get/free_lobbies");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String getBoard(String lobbyid) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("get/board/lobbyid={0}", new Object[]{lobbyid}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String getServerJson() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("serverJson");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String createPlayer(String playerName) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("new/player/playerName={0}", new Object[]{playerName})).request().put(null, String.class);
    }

    public String getBoardShips(String lobbyid, String playerid) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("get/board/ships/lobbyid={0}/playerid={1}", new Object[]{lobbyid, playerid}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String findPlayer(String name) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("find/player/{0}", new Object[]{name}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String joinLobby(String lobbyid, String playerid) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("join_lobby/lobbyid={0}/playerid={1}", new Object[]{lobbyid, playerid})).request().post(null, String.class);
    }

    public String buyUpgrade(String id, String what) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("player={0}/buy={1}", new Object[]{id, what})).request().post(null, String.class);
    }

    public String waitForOpponent(String lobbyid, String playerid) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("lobby={0}/wait/player={1}", new Object[]{lobbyid, playerid}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String getLobbies() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("get/lobbies");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public <T> T getServerXml(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("serverxml");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public String getPlayerIds() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("get/players");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String shoot(String lobbyid, String playerid, String x, String y) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("fire/lobby={0}/playerid={1}/x={2}/y={3}", new Object[]{lobbyid, playerid, x, y}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String newLobby(String playerid, String opponentid) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("new/lobby/playerid={0}/opponent={1}", new Object[]{playerid, opponentid})).request().put(null, String.class);
    }

    public String deployBoard(String lobbyid, String playerid, String type1, String x1, String y1, String horizontal1, String type2, String x2, String y2, String horizontal2, String type3, String x3, String y3, String horizontal3, String type4, String x4, String y4, String horizontal4, String type5, String x5, String y5, String horizontal5) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("deploy_board/lobbyid={0}/playerid={1}/ship1={2}/x={3}/y={4}/horizontal={5}/ship2={6}/x={7}/y={8}/horizontal={9}/ship3={10}/x={11}/y={12}/horizontal={13}/ship4={14}/x={15}/y={16}/horizontal={17}/ship5={18}/x={19}/y={20}/horizontal={21}", new Object[]{lobbyid, playerid, type1, x1, y1, horizontal1, type2, x2, y2, horizontal2, type3, x3, y3, horizontal3, type4, x4, y4, horizontal4, type5, x5, y5, horizontal5})).request().put(null, String.class);
    }
    
    public String deployBoard(String lobbyid, String playerid, String ... data) { // String type1, String x1, String y1, String horizontal1, String type2, String x2, String y2, String horizontal2, String type3, String x3, String y3, String horizontal3, String type4, String x4, String y4, String horizontal4, String type5, String x5, String y5, String horizontal5) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("deploy_board/lobbyid={0}/playerid={1}/ship1={2}/x={3}/y={4}/horizontal={5}/ship2={6}/x={7}/y={8}/horizontal={9}/ship3={10}/x={11}/y={12}/horizontal={13}/ship4={14}/x={15}/y={16}/horizontal={17}/ship5={18}/x={19}/y={20}/horizontal={21}", new Object[]{lobbyid, playerid, data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12], data[13], data[14], data[15], data[16], data[17], data[18], data[19]})).request().put(null, String.class);
    }
    
    public String getMoves(String lobbyid) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("get/moves/lobby={0}", new Object[]{lobbyid}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public void close() {
        client.close();
    }
    
}
