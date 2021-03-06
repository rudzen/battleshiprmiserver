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

package battleshiprmiserver;

import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utility.Statics;

/**
 * Simple argument wrapper class
 *
 * @author Rudy Alex Kohn (s133235@student.dtu.dk)
 */
public final class Args {

    public static String ip;
    public static int port;
    public static String game_address;

    static {
        try {
            ip = Statics.getFirstNonLoopbackAddress(true, false).getHostAddress();
        } catch (SocketException ex) {
            Logger.getLogger(Args.class.getName()).log(Level.SEVERE, null, ex);
            ip = "localhost";
        }
        //ip = "localhost";
        port = 6769;
        //game_address = "http://localhost:8080/BattleshipREST/test/";
        //game_address = "http://104.46.52.169:8080/BattleshipREST/test/";
        game_address = "http://ubuntu4.javabog.dk:6004/BattleshipREST/test/";

    }

    public static String all() {
        return "Args{" + "ip=" + ip + ", port=" + port + ", game_address=" + game_address + '}';
    }

    public static String registry() {
        return ip + ":" + Integer.toString(port);
    }

}
