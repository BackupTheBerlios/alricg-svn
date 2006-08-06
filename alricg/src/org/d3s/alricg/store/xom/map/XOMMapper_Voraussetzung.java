/*
 * Created on 18.10.2005 / 09:41:59
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom.map;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

import org.d3s.alricg.charKomponenten.links.IdLinkList;
import org.d3s.alricg.charKomponenten.links.Voraussetzung;
import org.d3s.alricg.charKomponenten.links.Voraussetzung.IdLinkVoraussetzung;

class XOMMapper_Voraussetzung implements XOMMapper<Voraussetzung> {

	private final XOMMapper<IdLinkVoraussetzung> idLinkVMapper = new XOMMapper_IdLinkVoraussetzung();
	private final XOMMapper<IdLinkList> idLinkListMapper = new XOMMapper_IdLinkList();
	
	public void map(Element from, Voraussetzung to) {
		
		// Auslesen des "abWert" -Wertes
		if (from.getAttributeValue("abWert") != null) {
			to.setAbWert(
				Integer.parseInt(from.getAttributeValue("abWert"))
			);
		}
		
		// Auslesen der "festen" Elemente
		Elements children = from.getChildElements("fest");
		IdLinkVoraussetzung[] festeVoraussetzung = new IdLinkVoraussetzung[children
				.size()];
		for (int i = 0; i < children.size(); i++) {
			festeVoraussetzung[i] = to.new IdLinkVoraussetzung(to.getQuelle());
			idLinkVMapper.map(children.get(i), festeVoraussetzung[i]);
		}
		to.setFesteVoraussetzung(festeVoraussetzung);

		// Auslesen der "nichtErlaubt" Elemente
		Element current = from.getFirstChildElement("nichtErlaubt");
		if (current != null) {
			final IdLinkList nichtErlaubt = new IdLinkList(to.getQuelle());
			idLinkListMapper.map(current, nichtErlaubt);
			to.setNichtErlaubt(nichtErlaubt);
		}

		// Aulesen der "Auswahl", also wo nur eines aus einer Gruppe erfüllt
		// sein muß
		children = from.getChildElements("auswahl");
		final IdLinkVoraussetzung[][] auswahlVoraussetzung = new IdLinkVoraussetzung[children
				.size()][];
		for (int i = 0; i < children.size(); i++) {
			final Elements options = children.get(i).getChildElements("option");
			auswahlVoraussetzung[i] = new IdLinkVoraussetzung[options.size()];
			for (int ii = 0; ii < options.size(); ii++) {
				auswahlVoraussetzung[i][ii] = to.new IdLinkVoraussetzung(to
						.getQuelle());
				idLinkVMapper.map(options.get(ii),
						auswahlVoraussetzung[i][ii]);
			}
		}
		to.setAuswahlVoraussetzung(auswahlVoraussetzung);

		
	}

	public void map(Voraussetzung from, Element to) {
		
		// Alle "festen" Elemente hinzufügen
        final IdLinkVoraussetzung[] festeVoraussetzung = from.getFesteVoraussetzung();
        for (int i = 0; i < festeVoraussetzung.length; i++) {
            final Element e = new Element("fest");
            idLinkVMapper.map(festeVoraussetzung[i], e);
            to.appendChild(e);
        }

        // Hinzufügen der "nichtErlaubt" Elemente
        final IdLinkList nichtErlaubt = from.getNichtErlaubt();
        if (nichtErlaubt != null) {
            final Element e = new Element("nichtErlaubt");
            idLinkListMapper.map(nichtErlaubt, e);
            to.appendChild(e);
        }

        // Alle Elemente der "Auswahl" hinzufügen
        final IdLinkVoraussetzung[][] auswahlVoraussetzung = from.getAuswahlVoraussetzung();
        for (int i = 0; i < auswahlVoraussetzung.length; i++) {
            final Element e = new Element("auswahl");
            for (int ii = 0; ii < auswahlVoraussetzung[i].length; ii++) {
                final Element ee = new Element("option");
                idLinkVMapper.map(auswahlVoraussetzung[i][ii], ee);
                e.appendChild(ee);
            }
            to.appendChild(e);
        }
        
        // Schreiben ab wann die Voraussetzung gilt
        if (from.getAbWert() != 0) {
        	to.addAttribute(new Attribute("abWert", Integer.toString(from.getAbWert())));
        }
	}

}
