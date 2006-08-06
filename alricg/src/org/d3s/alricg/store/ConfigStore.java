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
 * Abstraktion der Konfiguration von alricg.
 * 
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
public interface ConfigStore {

	/** Enumeration der gültigen keys */
	public enum Key {
		config_file("config.file"), d3s_schema_path("d3s.schema.path"), d3s_library_path(
				"d3s.library.path"), d3s_img_path("d3s.img.path"), d3s_data_path(
				"d3s.data.path"), user_data_path("user.data.path");

		/** Wert des keys */
		private final String wert;

		/** Erzeugt eine neue Instanz mit dem übergebenen Wert */
		private Key(String wert) {
			this.wert = wert;
		}

		// @see java.lang.Object#toString()
		public String toString() {
			return wert;
		}
	};

	/**
	 * Enthält Konfigurationsdaten für alricg. Insbesondere Konfigurationen
	 * allgemeiner Art wie z.B. Pfadangaben von Resourcen etc.
	 * 
	 * @see ConfigStore.Key
	 * @return die gültige alricg-Konfiguration.
	 */
	Configuration getConfig();
}
