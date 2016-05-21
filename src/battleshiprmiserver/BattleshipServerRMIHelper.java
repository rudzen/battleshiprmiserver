/*
 * The MIT License
 *
 * Copyright 2016 Rudy Alex Kohn (s133235@student.dtu.dk).
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
 * @author Rudy Alex Kohn (s133235@student.dtu.dk)
 */
public class BattleshipServerRMIHelper {

    public static void setArgs(final String[] args) {
        /* configure arrays for easy access, 0 = variable name, 1 = description */
        final String[] VAR_ADDRESS = {"address", "Set the RMI registry address (hostname)."};
        final String[] VAR_PORT = {"port", "Set the RMI registry port."};
        final String[] VAR_REST = {"rest_address", "The address to use for the REST game server."};

        final MainArgsHandler argsHandler = MainArgsHandler.getHandler();

        final Interval<Integer> ZERO_OR_ONE = new GenericInterval<>(0, 1);
        argsHandler.permitVariable(VAR_ADDRESS[0], ZERO_OR_ONE, VAR_ADDRESS[1]);
        argsHandler.permitVariable(VAR_PORT[0], ZERO_OR_ONE, VAR_PORT[1]);
        argsHandler.permitVariable(VAR_REST[0], ZERO_OR_ONE, VAR_REST[1]);

        try {
            argsHandler.processMainArgs(args);
            List<String> argsReceived;

            // address
            argsReceived = argsHandler.getValuesFromVariable(VAR_ADDRESS[0]);
            if (!argsReceived.isEmpty()) {
                Args.ip = argsReceived.get(0);
            }

            // port
            argsReceived = argsHandler.getValuesFromVariable(VAR_PORT[0]);
            if (!argsReceived.isEmpty()) {
                try {
                    final int val = Integer.parseInt(argsReceived.get(0));
                    if (val > 1024 && val <= 65536) {
                        Args.port = val;
                    } else {
                        /* port is not in a valid range */
                        throw new IllegalArgumentException("Invalid range : " + Integer.toString(val) + ", only between 1024 and 65536 is allowed.");
                    }

                } catch (final NumberFormatException nfe) {
                    throw new IllegalArgumentException("Invalid port : only integers allowed.");
                }
            }

            argsReceived = argsHandler.getValuesFromVariable(VAR_REST[0]);
            if (!argsReceived.isEmpty()) {
                try {
                    Args.game_address = argsReceived.get(0);
                } catch (final NumberFormatException nfe) {
                    throw new IllegalArgumentException(VAR_REST[0] + " must be an Integer.");
                }
            }

        } catch (final IllegalArgumentException iae) {
            System.out.println(argsHandler.getUsageSummary());
            System.exit(0);
        }
    }

}
