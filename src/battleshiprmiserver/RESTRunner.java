/*
 * The MIT License
 *
 * Copyright 2016 rudz.
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
package battleshiprmiserver;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import interfaces.IClientListener;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Starts a specific REST command, and sends the result automaticly back to the
 * client.
 *
 * @author rudz
 */
public class RESTRunner {

    private IClientListener client;

    public static void test(final String player, final IClientListener client) {
        System.out.println("Attempting to test REST for : \n" + player + "\n" + client.toString());
        
        Future<HttpResponse<JsonNode>> future = Unirest.post("http://httpbin.org/post")
                .header("accept", "application/json")
                .field("param1", "value1")
                .field("param2", "value2")
                .asJsonAsync(new Callback<JsonNode>() {

                    @Override
                    public void failed(UnirestException e) {
                        try {
                            client.showMessage("Message failed.", "REST test", JOptionPane.ERROR_MESSAGE);
                        } catch (RemoteException ex) {
                            Logger.getLogger(RESTRunner.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("The request has failed");
                    }

                    @Override
                    public void completed(HttpResponse<JsonNode> response) {
                        int code = response.getStatus();
                        JsonNode body = response.getBody();
                        InputStream rawBody = response.getRawBody();
                        System.out.println("Rest completed : " + code);
                        try {
                            client.showMessage("OK! " + player, "Code : " + Integer.toString(code), JOptionPane.INFORMATION_MESSAGE);
                        } catch (RemoteException ex) {
                            Logger.getLogger(RESTRunner.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    @Override
                    public void cancelled() {
                        
                        System.out.println("The request has been cancelled");
                    }
                });
    }

}
