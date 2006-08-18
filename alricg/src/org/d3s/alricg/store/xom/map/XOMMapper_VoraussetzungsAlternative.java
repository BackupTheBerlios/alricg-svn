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
import nu.xom.Elements;

import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.IdLinkList;
import org.d3s.alricg.charKomponenten.links.Voraussetzung.VoraussetzungsAlternative;

class XOMMapper_VoraussetzungsAlternative implements
		XOMMapper<VoraussetzungsAlternative> {

	private final XOMMapper<IdLinkList> idLinkListMapper = new XOMMapper_IdLinkList();

	public void map(Element from, VoraussetzungsAlternative to) {
	
		// Auslesen des "abWert" -Wertes
		if (from.getAttributeValue("abWert") != null) {
			to.setAbWert(
				Integer.parseInt(from.getAttributeValue("abWert"))
			);
		}
		
		// Auslesen der "nichtErlaubt" Elemente
		Elements children = from.getChildElements("alternative");
		final IdLinkList[] alternativen = new IdLinkList[children.size()];
		for (int i = 0; i < children.size(); i++) {
			alternativen[i] = new IdLinkList(to.getParentVoraussetzung().getQuelle());
			idLinkListMapper.map(children.get(i), alternativen[i]);
		}
		to.setVoraussetzungsAlternativen(alternativen);
	}

	public void map(VoraussetzungsAlternative from, Element to) {
		
        // Schreiben ab wann die Voraussetzung gilt
        if (from.getAbWert() != 0) {
        	to.addAttribute(new Attribute("abWert", Integer.toString(from.getAbWert())));
        }
        
        final IdLinkList[] alternativen = from.getVoraussetzungsAlternativen();
        for (int i = 0; i < alternativen.length; i++) {
            final Element e = new Element("alternative");
            idLinkListMapper.map(alternativen[i], e);
            to.appendChild(e);
        }
        
		//idLinkMapper.map(from, to);

        // Hinzufügen der "wertGrenze", falls nicht Default wert
       // to.addAttribute(new Attribute("wertGrenze", from.isMinimum() ? "min" : "max"));

	}

}
