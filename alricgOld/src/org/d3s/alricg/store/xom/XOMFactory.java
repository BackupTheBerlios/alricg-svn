/*
 * Created on 20.06.2005 / 13:14:15
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store.xom;

import org.d3s.alricg.store.AbstractStoreFactory;
import org.d3s.alricg.store.ConfigStore;
import org.d3s.alricg.store.ConfigurationException;
import org.d3s.alricg.store.DataStore;
import org.d3s.alricg.store.TextStore;

/**
 * <code>AbstractStoreFactory</code> auf Basis des xom-Frameworks.
 * 
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
public final class XOMFactory implements AbstractStoreFactory {

    /** Lokalisierte Texte. */
    private final XOMTextStore library;

    /** alricg-Konfiguration. */
    private final XOMConfigStore config;

    /** Alle Regeln zur Charaktererschaffung. */
    private final XOMStore data;

    /** Initialisierungsstatus der Factory. */
    private boolean initialized;

    /** Erzeugt eine neue <code>XOMFactory</code>. */
    public XOMFactory() {
    	config = new XOMConfigStore();
    	library = new XOMTextStore();
        data = new XOMStore();
    }

    // @see org.d3s.alricg.store.AbstractStoreFactory#getData()
    public DataStore getData() {
        return data;
    }

    // @see org.d3s.alricg.store.AbstractStoreFactory#getConfiguration()
    public ConfigStore getConfiguration() {
        return config;
    }

    // @see org.d3s.alricg.store.AbstractStoreFactory#getLibrary()
    public TextStore getLibrary() {
        return library;
    }

    // @see org.d3s.alricg.store.AbstractStoreFactory#initialize()
    public synchronized void initialize() throws ConfigurationException {
        if (initialized) {
            return;
        }
        
        // init stores
        config.init();
        library.init(config.getConfig());
        data.init(config.getConfig());        
        initialized = true;
    }
}
