/*
 * Created on 10.10.2005 / 09:59:08
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom;

import java.io.File;
import java.util.Properties;

import nu.xom.Element;

import org.d3s.alricg.store.ConfigStore;
import org.d3s.alricg.store.Configuration;
import org.d3s.alricg.store.ConfigurationException;

public class XOMConfigStoreMock implements ConfigStore {

	/** Pfad und Name der Konfigurationsdatei */
	private static final String CONFIG_FILE = "ressourcen/config.xml";

	/** Pfad zu den xml-Schemata */
	private static final String D3S_SCHEMA_PATH = "ressourcen/daten/schema/";

	/** Pfad zu den lokalisierten Texten */
	private static final String D3S_LIBRARY_PATH = "ressourcen/daten/text/";

	/** Pfad zu den Bildchen */
	private static final String D3S_IMG_PATH = "ressourcen/img/";

	/** Pfad zu den "original" Daten */
	private static final String D3S_DATA_PATH = "ressourcen/daten/basisDaten/";

	/** Pfad zu den "anwenderspezifischen" Daten */
	private static final String USER_DATA_PATH = "ressourcen/userDaten/";

	/** Speichert die konfigurationsrelevanten Einstellungen */
	private final Properties props = new Properties();

	// @see org.d3s.alricg.store.ConfigStore#getConfig()
	public Configuration getConfig() {
		return new Configuration(props);
	}

	void init() {
		try {
			// Validity check
			final String configFile = CONFIG_FILE;
			final Element configRoot = XOMHelper.getRootElementNoLog(new File(
					configFile));
			if (configRoot == null) {
				throw new ConfigurationException("Config file " + configFile
						+ " is invalid.");
			}

			// Mapping
			final Properties result = new Properties();
			result.setProperty(ConfigStore.Key.config_file.toString(),
					CONFIG_FILE);
			result.setProperty(ConfigStore.Key.d3s_schema_path.toString(),
					D3S_SCHEMA_PATH);
			result.setProperty(ConfigStore.Key.d3s_library_path.toString(),
					D3S_LIBRARY_PATH);
			result.setProperty(ConfigStore.Key.d3s_img_path.toString(),
					D3S_IMG_PATH);
			result.setProperty(ConfigStore.Key.d3s_data_path.toString(),
					D3S_DATA_PATH);
			result.setProperty(ConfigStore.Key.user_data_path.toString(),
					USER_DATA_PATH);

			props.putAll(result);
		} catch (Exception e) {
			throw new RuntimeException("Configuration failed!", e);
		}
	}
}
