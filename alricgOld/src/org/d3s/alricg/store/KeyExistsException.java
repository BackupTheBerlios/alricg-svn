/*
 * Created on 15.06.2005 / 12:13:14
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store;

/**
 * Zeigt an, dass im DataStore ein Schlüssel mehrfach verwendet wird.
 * 
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
public class KeyExistsException extends Exception {

    private static final long serialVersionUID = 7504444038267831753L;

    /**
     * Erzeugt eine neue <code>KeyExistsException</code>
     * 
     * @see Exception
     */
    public KeyExistsException() {
        super();
    }

    /**
     * Erzeugt eine neue <code>KeyExistsException</code>
     * 
     * @see Exception
     * @param msg Die Nachricht, die mit dieser Ausnahme angezeigt werden soll.
     */
    public KeyExistsException(String msg) {
        super(msg);
    }

    /**
     * Erzeugt eine neue <code>KeyExistsException</code> durch "umhüllen" einer anderen.
     * 
     * @see Exception
     * @param t Die zu umhüllende Ausnahme
     */
    public KeyExistsException(Throwable t) {
        super(t);
    }

    /**
     * Erzeugt eine neue <code>KeyExistsException</code>
     * 
     * @see Exception
     * @param msg Die Nachricht, die mit dieser Ausnahme angezeigt werden soll.
     * @param t Die zu umhüllende Ausnahme
     */
    public KeyExistsException(String msg, Throwable t) {
        super(msg, t);
    }
}
