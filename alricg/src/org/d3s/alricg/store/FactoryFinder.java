/*
 * Created on 15.06.2005 / 12:13:14
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Eine Finderklasse zur Auswahl der konkreten <code>AbstractStoreFactory</code>.
 * <p>
 * Der <code>FactoryFinder</code> wählt anhand einer Konfigurationsdatei aus,
 * welche konkrete <code>AbstractStoreFactory</code> verwendet werden soll.
 * <br>
 * <h4>Verwendung</h4>
 * 
 * <pre>
 * AbstractStoreFactory factory = FactoryFinder.init(); // initialisiert den FactoryFinder und gibt die konkrete Factory zurück.
 * 
 * //... do something ...
 * 
 * AbstractStoreFactory factorz = FactoryFinder.find(); // gibt die konkrete Factory zurück.
 * </pre>
 * 
 * Die <code>init</code>-Methode muss ausgeführt werden, bevor die
 * <code>find</code>-Methode zum ersten Mal aufgerufen wird.
 * <h4>Initialisierung</h4>
 * Der <code>FactoryFinder</code> sucht in der Datei
 * <code>ressourcen/factory.properties</code>, in der angegebenen
 * Reihenfolge, nach folgenden Schlüsseln:
 * <ol>
 * <li>data.store.factory.impl</li>
 * <li>data.store.factory.default</li>
 * </ol>
 * Der Eintrag zum erstne gefundenen Schlüssel, wird als Klassenname
 * interpretiert und versucht über Reflection zu laden. Die Klasse muss das
 * Interface {@link org.d3s.alricg.store.AbstractStoreFactory} implementieren.
 * </p>
 * 
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
public final class FactoryFinder {

	/** <code>FactoryFinder</code>'s logger */
	private static final Logger LOG = Logger.getLogger(FactoryFinder.class
			.getName());

	/**
	 * Die zu verwendende <code>AbstractStoreFactory</code>.
	 */
	private static AbstractStoreFactory factoryInstance;

	/**
	 * Gibt die Factory zurück.
	 * 
	 * @return Die zu verwendende <code>AbstractStoreFactory</code>.
	 * @throws NullPointerException
	 *             Falls <code>factoryInstance==null</code> ist.
	 */
	public static final AbstractStoreFactory find() {
		if (factoryInstance != null) {
			return factoryInstance;
		}
		throw new NullPointerException(
				"AbstractStoreFactory is not initialised!");
	}

	/**
	 * Initialisiert die zu verwendende <code>AbstractStoreFactory</code>,
	 * sofern das noch nicht geschehen ist und gibt sie zurück.
	 * 
	 * @param factoryFile
	 *            Die Datei aus der die <code>AbstractStoreFactory</code>
	 *            Implementierung gelesen werden soll.
	 * @return Die zu verwendende <code>AbstractStoreFactory</code>.
	 * @throws ConfigurationException
	 *             Falls während der Initialisierung der Facotry ein Fehler
	 *             auftritt.
	 */
	public static final AbstractStoreFactory init(File factoryFile)
			throws ConfigurationException {
		if (factoryInstance != null) {
			return factoryInstance;
		}

		synchronized (FactoryFinder.class) {
			try {
				String classname = "org.d3s.alricg.store.xom.XOMFactory";
				if (factoryFile.exists() && factoryFile.canRead()) {
					classname = getClassName(factoryFile);
				}
				final Class< ? > clazz = Class.forName(classname);
				factoryInstance = (AbstractStoreFactory) clazz.newInstance();
			} catch (Exception e) {
				factoryInstance = null;
				LOG.log(Level.SEVERE,
						"AbstractStoreFactory instantiation failed!", e);
				throw new ConfigurationException(
						"AbstractStoreFactory instantiation failed!", e);
			}
			factoryInstance.initialize();
			return factoryInstance;
		}

	}

	private static String getClassName(File factoryFile)
			throws FileNotFoundException, IOException {
		ResourceBundle rb = new PropertyResourceBundle(new FileInputStream(
				factoryFile));
		try {
			return rb.getString("data.store.factory.impl");
		} catch (NullPointerException npe) {
			try {
				return rb.getString("data.store.factory.default");
			} catch (NullPointerException npe2) {
				return "org.d3s.alricg.store.xom.XOMFactory";
			}
		}
	}

	/**
	 * Initialisiert die zu verwendende <code>AbstractStoreFactory</code>,
	 * sofern das noch nicht geschehen ist und gibt sie zurück.
	 * 
	 * @see #init(File)
	 * @return Die zu verwendende <code>AbstractStoreFactory</code>.
	 * @throws ConfigurationException
	 *             Falls während der Initialisierung der Facotry ein Fehler
	 *             auftritt.
	 */
	public static final AbstractStoreFactory init()
			throws ConfigurationException {
		return init(new File("ressourcen/factory.properties"));
	}

	/**
	 * Setzt die zu verwendende <code>AbstractStoreFactory</code> auf
	 * <code>null</code>.
	 */
	static final void reset() {
		synchronized (FactoryFinder.class) {
			factoryInstance = null;
		}
	}
}
