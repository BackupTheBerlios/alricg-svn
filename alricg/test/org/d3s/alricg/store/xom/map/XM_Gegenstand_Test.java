/*
 * Created on 10.10.2005 / 20:52:15
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom.map;

import java.io.File;

import junit.framework.TestCase;
import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.RegionVolk;
import org.d3s.alricg.charKomponenten.charZusatz.Gegenstand;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;

public class XM_Gegenstand_Test extends TestCase {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;

	public XM_Gegenstand_Test(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();

		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_GegenstandBase();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));

	}

	public void testMapFromXML() {
		final Element xom = new Element("GegenstandBase");
		Element element = null;

		// erhaeltlichBei: RegionVolk[] /min: 1
		String[] rvKeys = new String[] { "REG-B", "REG-M", "REG-K", "REG-G" };

		RegionVolk[] rv = new RegionVolk[] { new RegionVolk(rvKeys[0]),
				new RegionVolk(rvKeys[1]), new RegionVolk(rvKeys[2]),
				new RegionVolk(rvKeys[3]) };

		Gegenstand g = new GegenstandBase();
		for (int i = 0; i < rvKeys.length; i++) {
			oma.add(CharKomponente.region, rvKeys[i], rv[i]);
			oma.addErhaeltlichBei(xom, rvKeys[i]);

			g = new GegenstandBase();
			mappy.map(xom, g);

			assertEquals("# Regionen falsch", i + 1,
					g.getErhältlichBei().length);

			assertEquals("Region falsch", rv[i], g.getErhältlichBei()[i]);

			assertEquals("Gewicht falsch", CharElement.KEIN_WERT, g
					.getGewicht());
			assertEquals("Wert falsch", CharElement.KEIN_WERT, g.getWert());
			assertEquals("Einordnung falsch", null, g.getEinordnung());
		}

		// gewicht: int/optional
		int gewicht = 564463;
		element = new Element("gewicht");
		element.appendChild("" + gewicht);
		xom.appendChild(element);

		g = new GegenstandBase();
		mappy.map(xom, g);
		assertEquals("Gewicht falsch", gewicht, g.getGewicht());
		assertEquals("Wert falsch", CharElement.KEIN_WERT, g.getWert());
		assertEquals("Einordnung falsch", null, g.getEinordnung());

		// einordnung: String/optional
		String einordnung = "Verschimmeltes Essen";
		xom.addAttribute(new Attribute("einordnung", einordnung));

		g = new GegenstandBase();
		mappy.map(xom, g);
		assertEquals("Gewicht falsch", gewicht, g.getGewicht());
		assertEquals("Wert falsch", CharElement.KEIN_WERT, g.getWert());
		assertEquals("Einordnung falsch", einordnung, g.getEinordnung());

		// wert: int/optional
		int wert = 3432;
		element = new Element("wert");
		element.appendChild("" + wert);
		xom.appendChild(element);

		g = new GegenstandBase();
		mappy.map(xom, g);
		assertEquals("Gewicht falsch", gewicht, g.getGewicht());
		assertEquals("Wert falsch", wert, g.getWert());
		assertEquals("Einordnung falsch", einordnung, g.getEinordnung());
	}

	public void testMapToXML() {
		fail("Not implemented yet!");
	}

	/**
	 * Konkrete "do-nothing-special" Impl. der abtrakten Superklasse
	 * XOMMapper_Gegenstand
	 * 
	 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
	 */
	private class XOMMapper_GegenstandBase extends XOMMapper_Gegenstand {
		public void map(Element xmlElement, CharElement charElement) {
			super.map(xmlElement, charElement);
		}

		public void map(CharElement charElement, Element xmlElement) {
			super.map(charElement, xmlElement);
		}
	}

	/**
	 * Konkrete "do-nothing-special" Impl. der abtrakten Superklasse Gegenstand
	 * 
	 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
	 */
	private class GegenstandBase extends Gegenstand {
		public CharKomponente getCharKomponente() {
			return null;
		}
	}
}
