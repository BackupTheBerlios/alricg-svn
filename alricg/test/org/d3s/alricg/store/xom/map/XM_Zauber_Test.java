/*
 * Created on 10.10.2005 / 20:52:15
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;

import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Repraesentation;
import org.d3s.alricg.charKomponenten.Zauber;
import org.d3s.alricg.charKomponenten.Werte.MagieMerkmal;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class XM_Zauber_Test {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;

	@Before public void setUp() throws Exception {
		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_Zauber();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));

	}

	@Test public void testMapFromXML() {
		try {
			final Element xom = new Element("zauber");
			oma.addProbe(xom);
			oma.addKostenKlasse(xom);
			
			String dauer = "666 h";
			Element e = new Element("zauberdauer");
			e.appendChild(dauer);
			xom.appendChild(e);

			String kosten = "42 ASP (+2 pASP)";
			e = new Element("aspKosten");
			e.appendChild(kosten);
			xom.appendChild(e);

			String ziel = "Der Bösewicht";
			e = new Element("ziel");
			e.appendChild(ziel);
			xom.appendChild(e);

			String reichweite = "1000 km";
			e = new Element("reichweite");
			e.appendChild(reichweite);
			xom.appendChild(e);

			String wirkungsdauer = "Permanent";
			e = new Element("wirkungsdauer");
			e.appendChild(wirkungsdauer);
			xom.appendChild(e);

			final String name = "ZAU-Ignisphaero";
			Zauber z = new Zauber(name);
			mappy.map(xom, z);
			assertEquals("Merkmale falsch", 0, z.getMerkmale().length);
			assertEquals("Verbreitung falsch", 0, z.getVerbreitung().length);
			assertNull("Proben-Mod. falsch", z.getProbenModi());
			assertEquals("Dauer falsch", dauer, z.getZauberdauer());
			assertEquals("Kosten falsch", kosten, z.getAspKosten());
			assertEquals("Ziel falsch", ziel, z.getZiel());
			assertEquals("Reichweite falsch", reichweite, z.getReichweite());
			assertEquals("Wirkungsdauer falsch", wirkungsdauer, z
					.getWirkungsdauer());

			// merkmale
			int merkmalSize = 0;
			for (MagieMerkmal m : MagieMerkmal.values()) {
				e = new Element("merkmale");
				e.appendChild(m.getValue());
				xom.appendChild(e);
				z = new Zauber(name);
				mappy.map(xom, z);
				assertEquals("Merkmale falsch", ++merkmalSize, z.getMerkmale().length);
				assertEquals("Merkmal falsch", m, z.getMerkmale()[merkmalSize-1]);
				assertEquals("Verbreitung falsch", 0, z.getVerbreitung().length);
				assertNull("Proben-Mod. falsch", z.getProbenModi());
				assertEquals("Dauer falsch", dauer, z.getZauberdauer());
				assertEquals("Kosten falsch", kosten, z.getAspKosten());
				assertEquals("Ziel falsch", ziel, z.getZiel());
				assertEquals("Reichweite falsch", reichweite, z.getReichweite());
				assertEquals("Wirkungsdauer falsch", wirkungsdauer, z
						.getWirkungsdauer());
			}

			// verbreitung
			String[] bekanntKeys =  {"REP-111", "REP-222"};
			Repraesentation[] bekanntVals = {new Repraesentation(bekanntKeys[0]), new Repraesentation(bekanntKeys[1])};
			oma.add(CharKomponente.repraesentation, bekanntKeys[0], bekanntVals[0]);
			oma.add(CharKomponente.repraesentation, bekanntKeys[1], bekanntVals[1]);
			String[] repKeys =  {"REP-987", "REP-654"};
			Repraesentation[] repVals = {new Repraesentation(repKeys[0]), new Repraesentation(repKeys[1])};
			oma.add(CharKomponente.repraesentation, repKeys[0], repVals[0]);
			oma.add(CharKomponente.repraesentation, repKeys[1], repVals[1]);
			int[] wert = {100, 21000};

			e = new Element("verbreitung");
			e.addAttribute(new Attribute("wert", "" + wert[0]));
			xom.appendChild(e);
			z = new Zauber(name);
			mappy.map(xom, z);
			assertEquals("Verbreitung falsch", 1, z.getVerbreitung().length);
			assertEquals("Verbreitung falsch", wert[0], z.getVerbreitung()[0].getWert());
			assertNull("Verbreitung falsch", z.getVerbreitung()[0].getBekanntBei());
			assertNull("Verbreitung falsch", z.getVerbreitung()[0].getRepraesentation());
			assertEquals("Merkmale falsch", merkmalSize, z.getMerkmale().length);
			assertNull("Proben-Mod. falsch", z.getProbenModi());
			assertEquals("Dauer falsch", dauer, z.getZauberdauer());
			assertEquals("Kosten falsch", kosten, z.getAspKosten());
			assertEquals("Ziel falsch", ziel, z.getZiel());
			assertEquals("Reichweite falsch", reichweite, z.getReichweite());
			assertEquals("Wirkungsdauer falsch", wirkungsdauer, z
					.getWirkungsdauer());
			
			for (int i = 1; i <= bekanntKeys.length; i++) {
				e = new Element("verbreitung");
				e.addAttribute(new Attribute("wert", "" + wert[i-1]));
				e.addAttribute(new Attribute("bekanntBei", bekanntKeys[i-1]));
				e.addAttribute(new Attribute("repraesentation", repKeys[i-1]));
				xom.appendChild(e);
				z = new Zauber(name);
				mappy.map(xom, z);
				assertEquals("Verbreitung falsch", i+1, z.getVerbreitung().length);
				assertEquals("Verbreitung falsch", wert[i-1], z.getVerbreitung()[i].getWert());
				assertEquals("Verbreitung falsch", bekanntVals[i-1], z.getVerbreitung()[i].getBekanntBei());
				assertEquals("Verbreitung falsch", repVals[i-1], z.getVerbreitung()[i].getRepraesentation());
				
				assertEquals("Merkmale falsch", merkmalSize, z.getMerkmale().length);
				assertNull("Proben-Mod. falsch", z.getProbenModi());
				assertEquals("Dauer falsch", dauer, z.getZauberdauer());
				assertEquals("Kosten falsch", kosten, z.getAspKosten());
				assertEquals("Ziel falsch", ziel, z.getZiel());
				assertEquals("Reichweite falsch", reichweite, z.getReichweite());
				assertEquals("Wirkungsdauer falsch", wirkungsdauer, z
						.getWirkungsdauer());
			}

			// Proben-Modi
			String pm = "Probe erleichert um die MR des Opfers.";
			e = new Element("probenModi");
			e.appendChild(pm);
			xom.appendChild(e);
				
			z = new Zauber(name);
			mappy.map(xom, z);
			assertEquals("Proben-Mod. falsch", pm, z.getProbenModi());
			assertEquals("Merkmale falsch", merkmalSize, z.getMerkmale().length);
			assertEquals("Verbreitung falsch", 3, z.getVerbreitung().length);
			assertEquals("Dauer falsch", dauer, z.getZauberdauer());
			assertEquals("Kosten falsch", kosten, z.getAspKosten());
			assertEquals("Ziel falsch", ziel, z.getZiel());
			assertEquals("Reichweite falsch", reichweite, z.getReichweite());
			assertEquals("Wirkungsdauer falsch", wirkungsdauer, z
					.getWirkungsdauer());

		} catch (Exception e) {
			fail("No exception expected!");
		}
	}

	@Ignore("Not implemented yet!") @Test public void testMapToXML() {
		fail("Not implemented yet!");
	}
}