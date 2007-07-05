/*
 * Created on 23.06.2005 / 15:16:17
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store.xom.map;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.charZusatz.Waffe;
import org.d3s.alricg.charKomponenten.charZusatz.WuerfelSammlung;
import org.d3s.alricg.store.FactoryFinder;

/**
 * Abstrakter <code>XOMMapper</code> für eine <code>Waffe</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.charZusatz.Waffe
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
abstract class XOMMapper_Waffe extends XOMMapper_Gegenstand {

	/** <code>XOMMapper_Waffe</code>'s logger */
	private static final Logger LOG = Logger.getLogger(XOMMapper_Waffe.class
			.getName());

	// @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element,
	// org.d3s.alricg.charKomponenten.CharElement)
	public void map(Element xmlElement, CharElement charElement) {

		super.map(xmlElement, charElement);

		// my mapping
		final Waffe waffe = (Waffe) charElement;

		// Trefferpunkte
		try {
			Element child = xmlElement.getFirstChildElement("tp");
			final List<Integer> augenWuerfel = new ArrayList<Integer>();
			final List<Integer> anzahlWuerfel = new ArrayList<Integer>();
			if (child != null) {
				Attribute attr = child.getAttribute("W6");
				if (attr != null) {
					anzahlWuerfel.add(Integer.valueOf(attr.getValue()));
					augenWuerfel.add(6);
				}
				attr = child.getAttribute("W20");
				if (attr != null) {
					anzahlWuerfel.add(Integer.valueOf(attr.getValue()));
					augenWuerfel.add(20);
				}

				attr = child.getAttribute("plus");
				int plus = 0;
				if (attr != null) {
					plus = Integer.parseInt(attr.getValue());
				}

				Integer[] anzahl = anzahlWuerfel.toArray(new Integer[0]);
				Integer[] augen = augenWuerfel.toArray(new Integer[0]);
				waffe.setTP(new WuerfelSammlung(plus, anzahl, augen));
			}

			// Länge
			child = xmlElement.getFirstChildElement("eigenschaften");
			if (child != null) {
				Attribute attr = child.getAttribute("laenge");
				if (attr != null) {
					waffe.setLaenge(Integer.parseInt(attr.getValue()));
				}

				// ini
				attr = child.getAttribute("ini");
				if (attr != null) {
					waffe.setIni(Integer.parseInt(attr.getValue()));
				}
			}
		} catch (NumberFormatException exc) {
			LOG.log(Level.SEVERE, "String -> int failed", exc);
		}

		// Talente, mit denen die Waffe geführt werden kann
		Elements children = xmlElement.getChildElements("talentId");
		Talent[] talente = new Talent[children.size()];
		for (int i = 0; i < talente.length; i++) {
			final String value = children.get(i).getValue();
			talente[i] = (Talent) FactoryFinder.find().getData()
					.getCharElement(
							value,
							FactoryFinder.find().getData().getCharKompFromId(
									value));
		}
		waffe.setTalent(talente);
	}

	// @see
	// org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement,
	// nu.xom.Element)
	public void map(CharElement charElement, Element xmlElement) {
		super.map(charElement, xmlElement);

		// my mapping
		final Waffe waffe = (Waffe) charElement;

		// Trefferpunkte der Waffe
		WuerfelSammlung tp = waffe.getTP();
		if (tp != null) {
			Element e = new Element("tp");
			for (int i = 0; i < tp.getAugenWuerfel().length; i++) {
				if (tp.getAugenWuerfel()[i] == 6) {
					final String value = Integer
							.toString(tp.getAnzahlWuerfel()[i]);
					final Attribute a = new Attribute("W6", value);
					e.addAttribute(a);

				} else if (tp.getAugenWuerfel()[i] == 20) {
					final String value = Integer
							.toString(tp.getAnzahlWuerfel()[i]);
					final Attribute a = new Attribute("W20", value);
					e.addAttribute(a);
				}
			}
			final String value = Integer.toString(tp.getFestWert());
			e.addAttribute(new Attribute("plus", value));
			xmlElement.appendChild(e);
		}

		// länge der Waffe
		int laenge = waffe.getLaenge();
		Element e = null;
		if (laenge != CharElement.KEIN_WERT) {
			e = new Element("eigenschaften");
			e.addAttribute(new Attribute("laenge", Integer.toString(laenge)));
		}

		// Ini Bouns
		int ini = waffe.getIni();
		if (ini != CharElement.KEIN_WERT) {
			if (e == null) {
				e = new Element("eigenschaften");
			}
			e.addAttribute(new Attribute("ini", Integer.toString(ini)));
		}

		if (e != null) {
			xmlElement.appendChild(e);
		}

		// Talente
		Talent[] talente = waffe.getTalent();
		for (int i = 0; i < talente.length; i++) {
			Element e2 = new Element("talentId");
			e2.appendChild(talente[i].getId());
			xmlElement.appendChild(e2);
		}
	}
}
