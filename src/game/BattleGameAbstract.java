/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
