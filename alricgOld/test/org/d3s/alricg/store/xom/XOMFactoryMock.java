/*
 * Created 26. September 2005 / 21:42:43
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.xom;

import org.d3s.alricg.store.ConfigStore;
import org.d3s.alricg.store.ConfigurationException;
import org.d3s.alricg.store.DataStore;
import org.d3s.alricg.store.AbstractStoreFactory;
import org.d3s.alricg.store.TextStore;

/**
 * Mock-Object für die XOMFactory
 * 
 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
 * 
 */
public class XOMFactoryMock implements AbstractStoreFactory {

    private final XOMTextStoreMock library = new XOMTextStoreMock();

    private final XOMConfigStoreMock config = new XOMConfigStoreMock();

    private final XOMStoreMock data = new XOMStoreMock();

    private boolean initialized;

	// @see org.d3s.alricg.store.AbstractStoreFactory#initialize()
	public void initialize() throws ConfigurationException {
		if (initialized) {
			return;
		}

		// init stores
		config.init();
		library.init(config.getConfig());
		data.init(config.getConfig());
		initialized = true;

		System.out.println("initialized");
	}

	public DataStore getData() {
		return data;
	}

	public ConfigStore getConfiguration() {
		return config;
	}

	public TextStore getLibrary() {
		return library;
	}
}
