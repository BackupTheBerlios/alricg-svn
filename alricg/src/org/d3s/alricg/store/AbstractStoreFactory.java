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
 * Abstract Factory f�r die Initialisierung und den Zugriff auf die von alricg ben�tigten persistenten Daten.
 * <p>
 * Es gibt 3 Klassen von Stores in alricg:
 * <ul>
 * <li>DataStore: Regeln.</li>
 * <li>ConfigStore: Konfiguration, v.a. f�r interne Daten.</li>
 * <li>TextStore: lokalisierbare Texte.</li>
 * </ul>
 * Verhalten/Anforderungen:
 * <ul>
 * <li>Eine StoreFactory muss erst nach Aufruf von <code>initialize()</code> nicht-leere Stores zurc�kliefern.</li>
 * <li>Klassen, die dieses Interface implementieren, m�ssen einen "no argument"-Konstruktor zur Verf�gung stellen.</li>
 * </ul> 
 * </p>
 * 
 * @see org.d3s.alricg.store.FactoryFinder
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
public interface AbstractStoreFactory {

    /**
     * Initialisiert die <code>AbstractStoreFactory</code>.
     * 
     * @throws ConfigurationException Falls ein Fehler w�hrend der Initialisierung auftritt.
     */
    void initialize() throws ConfigurationException;

    /**
     * Gibt den <code>DataStore</code> zur�ck.
     * 
     * @return Der zur Factory geh�rige <code>DataStore</code>.
     */
    DataStore getData();

    /**
     * Gibt den <code>ConfigStore</code> zur�ck.
     * 
     * @return Der zur Factory geh�rige <code>ConfigStore</code>.
     */
    ConfigStore getConfiguration();

    /**
     * Gibt den <code>TextStore</code> zur�ck.
     * 
     * @return Der zur Factory geh�rige <code>TextStore</code>.
     */
    TextStore getLibrary();
}
