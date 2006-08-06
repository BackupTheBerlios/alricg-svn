/*
 * Created on 15.06.2005 / 12:13:14
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store;

import java.util.Properties;

/**
 * Konfiguration von alricg. Properties können nur unter einem der in <code>d3s.alricg.store.ConfigStore.Key</code> definierten Schlüssel abgelegt werden. 
 * 
 * @see org.d3s.alricg.store.ConfigStore.Key
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
public class Configuration {

    private final Properties props;

    /**
     * Erzeugt eine neue <code>Configuration</code> mit den in <code>props</code> angegebenen Eigneschaften.
     * @param props
     */
    public Configuration(Properties props) {
        this.props = props;

    }

    /**
     * @see java.util.Properties
     * @see ConfigStore.Key
     * @param key Schlüssel, dessen Wert  gesucht wird.
     * @param defaultValue Defaultwert, falls unter <code>key</code> kein Eintrag vorhanden ist.
     * @return  Der zu <code>key</code> gehörige Wert oder <code>defaultValue</code> falls kein Eintrag vorhanden ist.
     */
    public String getProperty(ConfigStore.Key key, String defaultValue) {
        return props.getProperty(key.toString(), defaultValue);
    }

    /**
     * @see java.util.Properties
     * @see ConfigStore.Key
     * @param key Schlüssel, dessen Wert  gesucht wird.
     * @return  Der zu <code>key</code> gehörige Wert oder <code>null</code> falls kein Eintrag vorhanden ist.
     */
    public String getProperty(ConfigStore.Key key) {
        return props.getProperty(key.toString());
    }
}
