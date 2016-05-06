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
package battleshiprmiserver;

import args.MainArgsHandler;
import args.intervals.GenericInterval;
import args.intervals.Interval;
import java.util.List;

/**
 * Generic helper class for main server class.
 *
 * @author Rudy Alex Kohn <s133235@student.dtu.dk>
 */
public class BattleshipServerRMIHelper {

    public static String setArgs(final String[] args) {

        final MainArgsHandler argsHandler = MainArgsHandler.getHandler();
        String retVal = null;

        final Interval<Integer> ZERO_OR_ONE = new GenericInterval<>(0, 1);
        argsHandler.permitVariable("registry", ZERO_OR_ONE, "Set the RMI registry information.");

        try {
            argsHandler.processMainArgs(args);
            List<String> argsReceived;
            argsReceived = argsHandler.getValuesFromVariable("registry");
            if (!argsReceived.isEmpty()) {
                retVal = argsReceived.get(0);
            }
        } catch (final IllegalArgumentException iae) {
            System.out.println(argsHandler.getUsageSummary());
            System.exit(0);
        }
        System.out.println();
        return retVal;
    }
    
}
