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

package exceptions;

/**
 * Simple exception to handle potential errors with this Server or connected Client part
 * @author Rudy Alex Kohn (s133235@student.dtu.dk)
 */
public class BattleshipException extends Exception {

    private static final long serialVersionUID = 5665271627436053973L;

    String error;

    public BattleshipException(final String error) {
        this.error = error;
    }

    public BattleshipException(final String error, final String message) {
        super(message);
        this.error = error;
    }

    public BattleshipException(final String error, final String message, final Throwable cause) {
        super(message, cause);
        this.error = error;
    }

    public BattleshipException(final String error, final Throwable cause) {
        super(cause);
        this.error = error;
    }

    public BattleshipException(final String error, final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.error = error;
    }
}