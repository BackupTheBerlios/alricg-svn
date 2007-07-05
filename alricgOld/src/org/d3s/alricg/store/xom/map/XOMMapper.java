/*
 * Created on 23.06.2005 / 15:16:17
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store.xom.map;

import nu.xom.Element;

/**
 * Mapping eines (xom-xml) <code>Element</code> in ein
 * <code>CharElement</code> und zurück.
 * 
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
interface XOMMapper<T> {

		/**
		 * Bildet die Daten des xom-Modells in Instanzen von <code>T</code> ab.
		 * Wird auch als Vorwärtsmapping bezeichnet.
		 * 
		 * @param from
		 *            Das in ein <code>T</code>-Objekt abzubildende
		 *            <code>Element</code>.
		 * @param to
		 *            Das zu befüllende <code>T</code>-Objekt.
		 */
		void map(Element from, T to);

		/**
		 * Bildet die Daten einer Instanz von <code>T</code> in eine entsprechende
		 * Struktur des xom-Modells ab. Wird auch als Rückwärtsmapping bezeichnet.
		 * 
		 * @param from
		 *            Das in ein <code>Element</code> abzubildende
		 *            <code>T</code>-Objekt.
		 * @param to
		 *            Das zu befüllende <code>Element</code>.
		 */
		void map(T from, Element to);
}
