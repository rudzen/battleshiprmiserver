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

package login;

import java.rmi.RemoteException;

import interfaces.IClientRMI;

/**
 * Simple SOAP login system.....
 *
 * @author Rudy Alex Kohn (s133235@student.dtu.dk)
 */
public final class Login {

    private final static String ADDRESS = "http://soap.transport.brugerautorisation/";
    private final static String IMPL = "BrugeradminImplService";
    private final static String WSDL = "http://javabog.dk:9901/brugeradmin?wsdl";
    
    public static boolean loginBA(final String userName, final String password, final IClientRMI client) throws RemoteException {
/*
        try {
            QName qname = new QName(ADDRESS, IMPL);
            Service service = Service.create(new URL(WSDL), qname);
            Brugeradmin ba = service.getPort(Brugeradmin.class);

            if (ba.hentBruger(userName, password) != null) {
                ba = null;
                service = null;
                qname = null;
                return true;
            }
        } catch (Exception e) {
            client.showMessage("Kan ikke forbinde til bruger database.", "LoginBA()", 0);
        }
*/
        return false;
    }
    
    public static boolean loginOWNAGE(final String userName, final String password, final IClientRMI client) {
        return false;
    }
    

    private static boolean slowEquals(final byte[] a, final byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}
