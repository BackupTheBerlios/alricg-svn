/*
 * Created on 18.10.2005 / 09:39:21
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom.map;

import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.Voraussetzung.IdLinkVoraussetzung;

class XOMMapper_IdLinkVoraussetzung implements
		XOMMapper<IdLinkVoraussetzung> {
	
	private final XOMMapper<IdLink> idLinkMapper = new XOMMapper_IdLink();

	public void map(Element from, IdLinkVoraussetzung to) {
	
		idLinkMapper.map(from, to);

        // Guard
        Attribute a = from.getAttribute("wertGrenze");
        if (a == null) {
            return;
        }

        // Prüfen ob der Wertebereich gültig ist!
        assert a.getValue().equalsIgnoreCase("max") || a.getValue().equalsIgnoreCase("min");

        // Setzen des Wertes, "true" ist Default
        to.setMinimum(a.getValue().equalsIgnoreCase("max"));

	}

	public void map(IdLinkVoraussetzung from, Element to) {
		idLinkMapper.map(from, to);

        // Hinzufügen der "wertGrenze", falls nicht Default wert
        to.addAttribute(new Attribute("wertGrenze", from.isMinimum() ? "min" : "max"));

	}

}
