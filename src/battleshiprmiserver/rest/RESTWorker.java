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
//package battleshiprmiserver.rest;
//
//import dataobjects.PPoint;
//import dataobjects.Player;
//import interfaces.IClientListener;
//import interfaces.IRESTRunner;
//
///**
// * The core of the RMI server communication to the REST server.<br>
// * Main purpose :<br>
// * - Runs a specific Callable object that will contact the REST server and wait
// * for the response from it. - Convert the needed information received from the
// * REST server back to usable object information which the RMI client
// * understands and send it back through the containing RMI client object.
// * <br><br>
// * If no information is needed to be send, nothing will happen.
// *
// * @author Rudy Alex Kohn <s133235@student.dtu.dk>
// */
//public class RESTWorker implements IRESTRunner {
//
//    private final IClientListener client;
//    private final Player player;
//    private final PPoint target;
//    
//    private volatile boolean stop;
//    
//    public RESTWorker(final IClientListener client, final Player player, final PPoint target) {
//        this.client = client;
//        this.player = player;
//        this.target = target;
//    }
//
//    @Override
//    public void run() {
//        try {
//            /* TODO : Convert stuff to something the REST server can understand. */
//        } catch (final Exception e) {
//            
//        }
//            
//    }
//    
//    
//    
//    /**
//     * Converts the ship into server ship format and then to JSON.
//     * @param player The player object containing the ships.
//     * @return 
//     */
//    private static String deployShips(final Player player) {
//        StringBuilder sb = new StringBuilder(150);
//        
//        
//        
//        return sb.toString();
//    }
//    
//    
//    
//    
//    /**
//     * Convert the attempt to fire a shot to an Object that is identical to the REST server object.
//     * @param playerID
//     * @param opponentID
//     * @param x
//     * @param y
//     * @return 
//     */
//    private static String fireShot(final int playerID, final int opponentID, final int x, final int y) {
//        
//        
//        
//        return "";
//        
//    }
//    
//    private static String convertToJSON(final int playerID, final int opponentID, final PPoint targetShot) {
//        StringBuilder sb = new StringBuilder(150);
//        if (targetShot == null) {
//            /* ok, so this is clearly not an attempt to shoot! */
//        } else {
//            /* this is a shot, so take action */
//            
//        }
//        
//        return sb.toString();
//    }
//    
//}
