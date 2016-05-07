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
package battleshiprmiserver.threads;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Generic thread pool based on Runnable objects.<br>
 * The settings can be configured real-time and will affect the situation based
 * on the current amount of running threads.<br>
 * WARNING: Do <b>NOT</b> adjust the current running threads too high, as the
 * default is based on the current system.<br>
 * Recommended maximum is 6, as the threads themselves do not actually do a lot,
 * but only acts as async receivers for REST responses. This is also why there
 * is little risk, as the response should be over quite fast and the handling of
 * potential exceptions are taken care of.
 *
 * @author Rudy Alex Kohn <s133235@student.dtu.dk>
 */
public class ThreadPool {

    private LinkedBlockingDeque<Runner> taskQueue = null;
    private final ArrayList<PoolThread> threads = new ArrayList<>();
    private boolean isStopped;

    @SuppressWarnings("CallToThreadStartDuringObjectConstruction")
    public ThreadPool(final int runningThreads, final int defaultNumTasks) {
        taskQueue = new LinkedBlockingDeque(defaultNumTasks);

        for (int i = 0; i < runningThreads; i++) {
            threads.add(new PoolThread(taskQueue));
        }
        threads.stream().forEach((thread) -> {
            thread.start();
        });
    }

    public synchronized void execute(final Runner task) throws Exception {
        if (isStopped) {
            throw new IllegalStateException("ThreadPool is stopped");
        }
        this.taskQueue.add(task);
    }

    public synchronized void stop() {
        this.isStopped = true;
        threads.stream().forEach((thread) -> {
            thread.doStop();
        });
    }

}
