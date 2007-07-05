/*
 * Created on 18.10.2005 / 09:37:36
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom.map;

import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.store.DataStore;
import org.d3s.alricg.store.FactoryFinder;

class XOMMapper_IdLink implements XOMMapper<IdLink> {

	public void map(Element from, IdLink to) {

		// Typs des Ziels (Talent, Zauber, usw.); muß ein Idlink enthalten
		final String idValue = from.getAttributeValue("id");
		final DataStore data = FactoryFinder.find().getData();
		final CharKomponente charKomp = data.getCharKompFromId(idValue);

		// Ziel ID; muß ein Idlink enthalten
		to.setZiel(data.getCharElement(idValue, charKomp));

		// Optionaler Text
		Attribute attribute = from.getAttribute("text");
		if (attribute != null) {
			to.setText(attribute.getValue().trim());
		}

		// Optionaler Wert
		attribute = from.getAttribute("wert");
		if (attribute != null) {
			to.setWert(Integer.parseInt(attribute.getValue().trim()));
		}

		// Optional Leitwert (Default ist false)
		attribute = from.getAttribute("leitwert");
		if (attribute != null) {

			// Gültigkeit des Wertes überprufen
			final String value = attribute.getValue().toLowerCase();
			assert value.equals("true") || value.equals("false");

			to.setLeitwert(Boolean.parseBoolean(value));
		}

		// Optionale Link-ID
		attribute = from.getAttribute("linkId");
		if (attribute != null) {
			final String value = attribute.getValue();
			to.setZweitZiel(FactoryFinder.find().getData().getCharElement(
					value,
					FactoryFinder.find().getData().getCharKompFromId(value)));
		}

	}

	public void map(IdLink from, Element to) {

		// MUSS: id
		to.addAttribute(new Attribute("id", from.getZiel().getId()));

		// Optional: Zweitziel
		CharElement charEl = from.getZweitZiel();
		if (charEl != null) {
			to.addAttribute(new Attribute("linkId", charEl.getId()));
		}

		// Optional: Text
		String value = from.getText();
		if (value.length() > 0) {
			to.addAttribute(new Attribute("text", value));
		}

		// Optional: Wert
		int wert = from.getWert();
		if (wert != IdLink.KEIN_WERT) {
			to.addAttribute(new Attribute("wert", Integer.toString(wert)));
		}

		// Optional: Leitwert (false ist default)
		to.addAttribute(new Attribute("leitwert", Boolean.toString(from
				.isLeitwert())));

	}

}
