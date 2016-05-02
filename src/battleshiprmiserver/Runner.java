package battleshiprmiserver;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Runner extends Thread {
    
    ExecutorService executor = Executors.newFixedThreadPool(1000);

    /**
     * Adds a callable object to the list.
     * @param runnable
     * @return 
     */
    public boolean addToList(final Runnable runnable) {
        
        executor.submit(runnable);
    }
    
    public boolean removeFromList(final Callable callable) {
        
    }
    
    public void runme() {
    }
    
    
}