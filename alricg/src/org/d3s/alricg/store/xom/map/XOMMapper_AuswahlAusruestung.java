/*
 * Created on 18.10.2005 / 09:36:28
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom.map;

import java.util.logging.Level;
import java.util.logging.Logger;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Herkunft;
import org.d3s.alricg.charKomponenten.charZusatz.SimpelGegenstand;
import org.d3s.alricg.charKomponenten.links.AuswahlAusruestung;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.AuswahlAusruestung.HilfsAuswahl;

class XOMMapper_AuswahlAusruestung implements XOMMapper<AuswahlAusruestung> {

	/** <code>XOMMapper_AuswahlAusruestung</code>'s logger */
	private static final Logger LOG = Logger
			.getLogger(XOMMapper_AuswahlAusruestung.class.getName());
	
	private final XOMMapper<IdLink> idLinkMapper = new XOMMapper_IdLink();

	
	public void map(AuswahlAusruestung from, Element to) {

		// Schreiben der "festen" Elemente
		HilfsAuswahl festeAuswahl = from.getFesteAuswahl();
		if (festeAuswahl != null) {
			final Element e = new Element("fest");
			mapHilfsauswahl(festeAuswahl, e);
			to.appendChild(e);
		}

		// Schreiben der "wählbaren" Elemente
		HilfsAuswahl[] variableAuswahl = from.getVariableAuswahl();
		for (int i = 0; i < variableAuswahl.length; i++) {
			Element e = new Element("auswahl");
			mapHilfsauswahl(variableAuswahl[i], e);
			to.appendChild(e);
		}

	}

	public void map(Element from, AuswahlAusruestung to) {

		// Auslesen der festen Gegenstände
		Element current = from.getFirstChildElement("fest");
		if (current != null) {
			final HilfsAuswahl festeAuswahl = to.new HilfsAuswahl();
			mapHilfsauswahl(current, festeAuswahl, to.getHerkunft());
			to.setFesteAuswahl(festeAuswahl);
		}

		// Auslesen der Auswahl
		final Elements children = from.getChildElements("auswahl");
		HilfsAuswahl[] variableAuswahl = new HilfsAuswahl[children.size()];
		for (int i = 0; i < variableAuswahl.length; i++) {
			variableAuswahl[i] = to.new HilfsAuswahl();
			mapHilfsauswahl(children.get(i), variableAuswahl[i], to
					.getHerkunft());
		}
		to.setVariableAuswahl(variableAuswahl);

	}

	/**
	 * Bildet ein xml-Element in eine Hilfsauswahl ab und verknüpft diese mit
	 * herkunft.
	 * 
	 * @param xmlElement
	 *            Das xml-Element mit den Daten der hilfsauswahl.
	 * @param hilfsauswahl
	 *            Die HilfsAuswahl, die verändert werden soll.
	 * @param herkunft
	 *            Die Herkunft mit der die Links der Auwahl verknüft werden
	 *            sollen
	 */
	private void mapHilfsauswahl(Element xmlElement, HilfsAuswahl hilfsauswahl,
			Herkunft herkunft) {

		// Die Anzahl auslesen, falls angegeben?
		if (xmlElement.getAttribute("anzahl") != null) {
			try {
				hilfsauswahl.setAnzahl(Integer.parseInt(xmlElement
						.getAttributeValue("anzahl")));
			} catch (NumberFormatException exc) {
				LOG.log(Level.SEVERE, "String -> umwandeln failed", exc);
			}
		}

		// Auslesen der Gegenstände die verlinkt werden!
		Elements children = xmlElement.getChildElements("ausruestungLink");
		final IdLink[] links = new IdLink[children.size()];
		for (int i = 0; i < children.size(); i++) {

			// ACHTUNG Die "null" an der Stelle könnte zu Problemen führen!!
			links[i] = new IdLink(herkunft, null);
			idLinkMapper.map(children.get(i), links[i]);
		}
		hilfsauswahl.setLinks(links);

		// Auslesen der simplen Gegenstände, die direkt angegeben werden
		children = xmlElement.getChildElements("ausruestungNeu");
		final SimpelGegenstand[] simpGegenstand = new SimpelGegenstand[children
				.size()];
		for (int i = 0; i < children.size(); i++) {
			simpGegenstand[i] = new SimpelGegenstand();
			final XOMMapper<CharElement> mappy = new XOMMapper_SimpelGegenstand();
			mappy.map(children.get(i), simpGegenstand[i]);
		}
		hilfsauswahl.setSimpGegenstand(simpGegenstand);
	}

	/**
	 * Bildet eine HilfsAuswahl in ein xml-Element ab.
	 * 
	 * @param hilfsauswahl
	 *            Die Hilfsauswahl, die ausgelesen werden soll.
	 * @param xmlElement
	 *            Das xml-Element, das geschrieben werden soll.
	 */
	private void mapHilfsauswahl(HilfsAuswahl hilfsauswahl, Element xmlElement) {

		// Schreiben der Anzahl
		final int anzahl = hilfsauswahl.getAnzahl();
		if (anzahl != 1) {
			xmlElement.addAttribute(new Attribute("anzahl", Integer
					.toString(anzahl)));
		}

		// Schreiben der Links
		final IdLink[] links = hilfsauswahl.getLinks();
		for (int i = 0; i < links.length; i++) {
			final Element e = new Element("ausruestungLink");
			idLinkMapper.map(links[i], e);
			xmlElement.appendChild(e);
		}

		// Schreiben der simplen Gegenstände, die direkt angegeben werden
		final SimpelGegenstand[] simpGegenstand = hilfsauswahl
				.getSimpGegenstand();
		for (int i = 0; i < simpGegenstand.length; i++) {
			final Element e = new Element("ausruestungNeu");
			final XOMMapper<CharElement> mappy = new XOMMapper_SimpelGegenstand();
			mappy.map(simpGegenstand[i], e);
			xmlElement.appendChild(e);
		}
	}

}
