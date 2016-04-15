/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author rudz
 */
public class GameHelpers {

    private Random rnd;

    /**
     * Re-seed the random object.
     */
    public void reseed() {
        rnd = new Random(System.currentTimeMillis());
    }

    /**
     * Generate number of player who goes first.<br>
     * @return 0 = Player 1... 1 = Player 2.
     */
    private int whoPlayFirst() {
        return rnd.nextInt(1000) % 2;
    }
    
    
    
}
