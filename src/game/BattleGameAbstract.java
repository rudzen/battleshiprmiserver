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
package game;

import interfaces.IClientListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author rudz
 */
public abstract class BattleGameAbstract {
    
     /**
     * Contains the game sessions themselves, the game uses these to find something valuable :-)
     */
    protected final HashMap<String, GameSession> sessions;

    protected BattleGameAbstract() {
        sessions = new HashMap<>();
    }
    
    protected String isInSession(final IClientListener client) {
        
        for (GameSession gs : sessions.values()) {
            if (!gs.isFull() && gs.isInSession(client)) {
                return gs.getGameSessionID();
            }
        }
        return null;
    }
    
    protected final String createSessionID(final IClientListener client) {
        return client.toString();
    } 
    
    protected ArrayList<String> getFreePlayerNames() {
        final ArrayList<String> list = new ArrayList<>();
        for (GameSession gs : sessions.values()) {
            if (!gs.isFull()) {
                if (gs.getPlayerTwo() == null) {
                    list.add(gs.getPlayerTwo().toString());
                }
            }
        }
        return list;
    }
    
}
