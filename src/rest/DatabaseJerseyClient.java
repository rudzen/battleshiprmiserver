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
//package rest;
//
//import javax.ws.rs.ClientErrorException;
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.WebTarget;
//
///**
// * Jersey REST client generated for REST resource:DatabaseREST [database]<br>
// * USAGE:
// * <pre>
// *        DatabaseJerseyClient client = new DatabaseJerseyClient();
// *        Object response = client.XXX(...);
// *        // do whatever with response
// *        client.close();
// * </pre>
// *
// * @author Rudy Alex Kohn <s133235@student.dtu.dk>
// */
//public class DatabaseJerseyClient {
//
//    private final WebTarget webTarget;
//    private final Client client;
//    private static final String BASE_URI = "http://localhost:8080/BattleshipREST/test";
//    //public static String BASE_URI = "http://104.46.52.169:8080/BattleshipREST/test";
//
//    public DatabaseJerseyClient() {
//        client = javax.ws.rs.client.ClientBuilder.newClient();
//        webTarget = client.target(BASE_URI).path("database");
//    }
//
//    public String getPlayerDB(String id) throws ClientErrorException {
//        WebTarget resource = webTarget;
//        resource = resource.path(java.text.MessageFormat.format("get/playerDB/{0}", new Object[]{id}));
//        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
//    }
//
//    public String updateWeapon(String id, String weapon, String point) throws ClientErrorException {
//        return webTarget.path(java.text.MessageFormat.format("update/playerid={0}/weapon={1}/point={2}", new Object[]{id, weapon, point})).request().put(null, String.class);
//    }
//
//    public String loginCreate(String id, String password) throws ClientErrorException {
//        WebTarget resource = webTarget;
//        resource = resource.path(java.text.MessageFormat.format("login/create/playerid={0}/password={1}", new Object[]{id, password}));
//        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
//    }
//
//    public String login(String id, String password) throws ClientErrorException {
//        WebTarget resource = webTarget;
//        resource = resource.path(java.text.MessageFormat.format("login/playerid={0}/password={1}", new Object[]{id, password}));
//        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
//    }
//
//    public String updateDecoy(String id, String decoy, String point) throws ClientErrorException {
//        return webTarget.path(java.text.MessageFormat.format("update/playerid={0}/decoy={1}/point={2}", new Object[]{id, decoy, point})).request().put(null, String.class);
//    }
//
//    public String loginBA(String id, String password) throws ClientErrorException {
//        WebTarget resource = webTarget;
//        resource = resource.path(java.text.MessageFormat.format("login/BA/playerid={0}/password={1}", new Object[]{id, password}));
//        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
//    }
//
//    public String incrementDecoy(String id) throws ClientErrorException {
//        return webTarget.path(java.text.MessageFormat.format("update/increment_decoy/playerid={0}", new Object[]{id})).request().put(null, String.class);
//    }
//
//    public String incrementWeapon(String id) throws ClientErrorException {
//        return webTarget.path(java.text.MessageFormat.format("update/increment_weapon/playerid={0}", new Object[]{id})).request().put(null, String.class);
//    }
//
//    public String incrementSonar(String id) throws ClientErrorException {
//        return webTarget.path(java.text.MessageFormat.format("update/increment_sonar/playerid={0}", new Object[]{id})).request().put(null, String.class);
//    }
//
//    public String updateLoss(String id, String loss) throws ClientErrorException {
//        return webTarget.path(java.text.MessageFormat.format("update/playerid={0}/loss={1}", new Object[]{id, loss})).request().put(null, String.class);
//    }
//
//    public String incrementWon(String id) throws ClientErrorException {
//        return webTarget.path(java.text.MessageFormat.format("update/increment_won/playerid={0}", new Object[]{id})).request().put(null, String.class);
//    }
//
//    public String updatePoint(String id, String point) throws ClientErrorException {
//        return webTarget.path(java.text.MessageFormat.format("update/playerid={0}/point={1}", new Object[]{id, point})).request().put(null, String.class);
//    }
//
//    public String incrementLoss(String id) throws ClientErrorException {
//        return webTarget.path(java.text.MessageFormat.format("update/increment_loss/playerid={0}", new Object[]{id})).request().put(null, String.class);
//    }
//
//    public String incrementArmor(String id) throws ClientErrorException {
//        return webTarget.path(java.text.MessageFormat.format("update/increment_armor/playerid={0}", new Object[]{id})).request().put(null, String.class);
//    }
//
//    public String updateWon(String id, String won) throws ClientErrorException {
//        return webTarget.path(java.text.MessageFormat.format("update/playerid={0}/won={1}", new Object[]{id, won})).request().put(null, String.class);
//    }
//
//    public String updateArmor(String id, String armor, String point) throws ClientErrorException {
//        return webTarget.path(java.text.MessageFormat.format("update/playerid={0}/armor={1}/point={2}", new Object[]{id, armor, point})).request().put(null, String.class);
//    }
//
//    public String updateSonar(String id, String sonar, String point) throws ClientErrorException {
//        return webTarget.path(java.text.MessageFormat.format("update/playerid={0}/sonar={1}/point={2}", new Object[]{id, sonar, point})).request().put(null, String.class);
//    }
//
//    public void close() {
//        client.close();
//    }
//    
//}
