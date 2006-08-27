/*
 * Created on 10.10.2005 / 20:52:15
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;

import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.RegionVolk;
import org.d3s.alricg.charKomponenten.charZusatz.Ausruestung;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class XM_Ausruestung_Test {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;

	@Before public void setUp() throws Exception {

		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_Ausruestung();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));

	}

	@Test public void testMapFromXML() {
		final Element xom = new Element("ausruestung");
		oma.addErhaeltlichBei(xom, "REG-BAY");
		oma.add(CharKomponente.region, "REG-BAY", new RegionVolk("REG-BAY"));

		String name = "AUS-X";
		Ausruestung a = new Ausruestung(name);
		mappy.map(xom, a);
		assertFalse("Behälter falsch", a.isIstBehaelter());
		assertNull("Haltbarkeit falsch", a.getHaltbarkeit());

		// behälter
		Element e = new Element("istBehaelter");
		xom.appendChild(e);
		String[] vals = { "true", "false", "TRUE", "FALSE" };
		for (int i = 0; i < vals.length; i++) {
			e.appendChild(vals[i]);
			a = new Ausruestung(name);
			mappy.map(xom, a);
			assertEquals("Behälter falsch", i % 2 == 0, a.isIstBehaelter());
			assertNull("Haltbarkeit falsch", a.getHaltbarkeit());
			e.removeChild(0);
		}
		xom.removeChild(e);

		// haltbarkeit
		e = new Element("haltbarkeit");
		xom.appendChild(e);
		vals = new String[] { "A1", "b2", "C$%!@" };
		for (int i = 0; i < vals.length; i++) {
			e.appendChild(vals[i]);
			a = new Ausruestung(name);
			mappy.map(xom, a);
			assertFalse("Behälter falsch", a.isIstBehaelter());
			assertEquals("Haltbarkeit falsch", vals[i], a.getHaltbarkeit());
			e.removeChild(0);
		}
	}

	@Ignore("Not implemented yet!") 
	@Test public void testMapToXML() {
		fail("Not implemented yet!");
	}
}
