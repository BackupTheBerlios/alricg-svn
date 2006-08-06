/*
 * Created on 23.06.2005 / 15:16:17
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store.xom.map;

import java.util.logging.Level;
import java.util.logging.Logger;

import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Sonderfertigkeit;
import org.d3s.alricg.charKomponenten.Sonderfertigkeit.Art;
import org.d3s.alricg.prozessor.utils.FormelSammlung;

/**
 * <code>XOMMapper</code> für eine <code>Sonderfertigkeit</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.Sonderfertigkeit
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
class XOMMapper_Sonderfertigkeit extends XOMMapper_Fertigkeit {

	/** <code>XOMMapper_Sonderfertigkeit</code>'s logger */
	private static final Logger LOG = Logger
			.getLogger(XOMMapper_Sonderfertigkeit.class.getName());

	// @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element,
	// org.d3s.alricg.charKomponenten.CharElement)
	public void map(Element xmlElement, CharElement charElement) {
		super.map(xmlElement, charElement);

		// my mapping
		final Sonderfertigkeit sonderfertigkeit = (Sonderfertigkeit) charElement;

		Element current = null;
		try {
			// Auslesen der permanenten AsP Kosten
			current = xmlElement.getFirstChildElement("permAsP");
			if (current != null) {
				sonderfertigkeit.setPermAsp(Integer
						.parseInt(current.getValue()));
			}
		} catch (NumberFormatException exc) {
			LOG.log(Level.SEVERE, "String -> int failed", exc);
		}

		try {
			// Auslesen der permanenten Karmaenergie Kosten
			current = xmlElement.getFirstChildElement("permKa");
			if (current != null) {
				sonderfertigkeit
						.setPermKa(Integer.parseInt(current.getValue()));
			}
		} catch (NumberFormatException exc) {
			LOG.log(Level.SEVERE, "String -> int failed", exc);
		}

		try {
			// Auslesen der permanenten Lebensenergie Kosten
			current = xmlElement.getFirstChildElement("permLeP");
			if (current != null) {
				sonderfertigkeit.setPermLep(Integer
						.parseInt(current.getValue()));
			}
		} catch (NumberFormatException exc) {
			LOG.log(Level.SEVERE, "String -> int failed", exc);
		}
		try {
			// Auslesen der AP kosten (nur nötig, falls abweichend von GP x 50)
			if (xmlElement.getAttribute("ap") != null) {
				sonderfertigkeit.setApKosten(Integer.parseInt(xmlElement
						.getAttributeValue("ap")));
			}
		} catch (NumberFormatException exc) {
			LOG.log(Level.SEVERE, "String -> int failed", exc);
		}

		// Auslesen der Art der Sonderfertigkeit
		String artValue = xmlElement.getFirstChildElement("art").getValue();
		for (int i = 0; i < Art.values().length; i++) {
			if (Art.values()[i].getValue().equals(artValue)) {
				sonderfertigkeit.setArt(Art.values()[i]);
				break; // Gefunden, abbrechen
			}
		}
	}

	// @see
	// org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement,
	// nu.xom.Element)
	public void map(CharElement charElement, Element xmlElement) {
		super.map(charElement, xmlElement);

		// my mapping
		final Sonderfertigkeit sonderfertigkeit = (Sonderfertigkeit) charElement;
		xmlElement.setLocalName("sonderf");

		// Hinzufügen der AP-Kosten
		final int apKosten = sonderfertigkeit.getApKosten();
		if (apKosten != CharElement.KEIN_WERT
				&& apKosten != FormelSammlung.berechneSfAp(sonderfertigkeit
						.getGpKosten())) {
			xmlElement.addAttribute(new Attribute("ap", Integer
					.toString(apKosten)));
		}

		// Hinzufügen der permanetnen ASP Kosten
		final int permAsp = sonderfertigkeit.getPermAsp();
		if (permAsp != 0) {
			final Element e = new Element("permAsP");
			e.appendChild(Integer.toString(permAsp));
			xmlElement.appendChild(e);
		}

		// Hinzufügen der permanetnen Karmaenergie Kosten
		final int permKa = sonderfertigkeit.getPermKa();
		if (permKa != 0) {
			final Element e = new Element("permKa");
			e.appendChild(Integer.toString(permKa));
			xmlElement.appendChild(e);
		}

		// Hinzufügen der permanetnen Lebensenergie Kosten
		final int permLep = sonderfertigkeit.getPermLep();
		if (permLep != 0) {
			final Element e = new Element("permLeP");
			e.appendChild(Integer.toString(permLep));
			xmlElement.appendChild(e);
		}

		// Hinzufügen der Art dieser Sonderf
		final Element e = new Element("art");
		e.appendChild(sonderfertigkeit.getArt().getValue());
		xmlElement.appendChild(e);
	}
}
