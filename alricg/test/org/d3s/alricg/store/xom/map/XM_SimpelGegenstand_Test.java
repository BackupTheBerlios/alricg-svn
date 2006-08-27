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
import org.d3s.alricg.charKomponenten.RegionVolk;
import org.d3s.alricg.charKomponenten.charZusatz.SimpelGegenstand;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class XM_SimpelGegenstand_Test {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;

	@Before public void setUp() throws Exception {

		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_SimpelGegenstand();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));

	}

	@Test public void testMapFromXML() {
		final Element xom = new Element("simplegegenstand");
		oma.addErhaeltlichBei(xom, "REG-BAY");
		oma.add(CharKomponente.region, "REG-BAY", new RegionVolk("REG-BAY"));

		SimpelGegenstand s = new SimpelGegenstand();
		mappy.map(xom, s);
		assertEquals("Anzahl falsch", 1, s.getAnzahl());
		assertNull("Art falsch", s.getArt());

		// Anzahl
		Attribute a = new Attribute("anzahl", "n/a");
		xom.addAttribute(a);

		s = new SimpelGegenstand();
		mappy.map(xom, s);
		assertEquals("Anzahl falsch", 1, s.getAnzahl());
		assertNull("Art falsch", s.getArt());

		for (int i = 0; i < 100; i++) {
			a.setValue("" + i);
			s = new SimpelGegenstand();
			mappy.map(xom, s);
			assertEquals("Anzahl falsch", i, s.getAnzahl());
			assertNull("Art falsch", s.getArt());
		}
		xom.removeAttribute(a);

		// Art
		a = new Attribute("art", "n/a");
		xom.addAttribute(a);
		for (int i = 0; i < CharKomponente.values().length; i++) {
			a.setValue(CharKomponente.values()[i].getPrefix());
			s = new SimpelGegenstand();
			mappy.map(xom, s);
			assertEquals("Anzahl falsch", 1, s.getAnzahl());
			assertEquals("Art falsch", CharKomponente.values()[i], s.getArt());
		}
	}

	@Ignore("Not implemented yet!") @Test public void testMapToXML() {
		fail("Not implemented yet!");
	}
}
