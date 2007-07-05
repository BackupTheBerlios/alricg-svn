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
 * Zeigt an, dass während der Konfigurationsphase oder durch fehlerhafte Konfiguration von alricg ein Fehler aufgetreten
 * ist.
 * 
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
public class ConfigurationException extends Exception {

    private static final long serialVersionUID = -1469348845404562863L;

    /**
     * Erzeugt eine neue <code>ConfigurationException</code>
     * 
     * @see Exception
     */
    public ConfigurationException() {
        super();
    }

    /**
     * Erzeugt eine neue <code>ConfigurationException</code>
     * 
     * @see Exception
     * @param msg Die Nachricht, die mit dieser Ausnahme angezeigt werden soll.
     */
    public ConfigurationException(String msg) {
        super(msg);
    }

    /**
     * Erzeugt eine neue <code>ConfigurationException</code> durch "umhüllen" einer anderen.
     * 
     * @see Exception
     * @param t Die zu umhüllende Ausnahme
     */
    public ConfigurationException(Throwable t) {
        super(t);
    }

    /**
     * Erzeugt eine neue <code>ConfigurationException</code>
     * 
     * @see Exception
     * @param msg Die Nachricht, die mit dieser Ausnahme angezeigt werden soll.
     * @param t Die zu umhüllende Ausnahme
     */
    public ConfigurationException(String msg, Throwable t) {
        super(msg, t);
    }
}
