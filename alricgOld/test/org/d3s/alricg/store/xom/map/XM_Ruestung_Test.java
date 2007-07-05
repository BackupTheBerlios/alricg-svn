/*
 * Created on 10.10.2005 / 20:52:15
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.RegionVolk;
import org.d3s.alricg.charKomponenten.charZusatz.Ruestung;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class XM_Ruestung_Test {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;


	@Before public void setUp() throws Exception {
		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_Ruestung();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));

	}

	@Test public void testMapFromXML() {
		final Element xom = new Element("ruestung");
		oma.addErhaeltlichBei(xom, "REG-BAY");
		oma.add(CharKomponente.region, "REG-BAY", new RegionVolk("REG-BAY"));

		String name = "RUE-Lederruestung";
		Ruestung r = new Ruestung(name);
		try {
			mappy.map(xom, r);
			fail("Exception expected");
		} catch (NullPointerException npe) {
			// everything's ok;
		}

		int gRs = 23;
		Element e = new Element("gRs");
		e.appendChild("" + gRs);
		xom.appendChild(e);
		try {
			r = new Ruestung(name);
			mappy.map(xom, r);
			fail("Exception expected");
		} catch (NullPointerException npe) {
			// everything's ok;
		}
		
		int gBe = 5;
		e = new Element("gBe");
		e.appendChild("" + gBe);
		xom.appendChild(e);

		r = new Ruestung(name);
		mappy.map(xom, r);
		assertEquals("ko falsch", CharElement.KEIN_WERT, r.getZoneKo());
		assertEquals("br falsch", CharElement.KEIN_WERT, r.getZoneBr());
		assertEquals("rue falsch", CharElement.KEIN_WERT, r.getZoneRue());
		assertEquals("ba falsch", CharElement.KEIN_WERT, r.getZoneBa());
		assertEquals("la falsch", CharElement.KEIN_WERT, r.getZoneLa());
		assertEquals("ra falsch", CharElement.KEIN_WERT, r.getZoneRa());
		assertEquals("lb falsch", CharElement.KEIN_WERT, r.getZoneLb());
		assertEquals("rb falsch", CharElement.KEIN_WERT, r.getZoneRb());
		assertEquals("ges falsch", CharElement.KEIN_WERT, r.getZoneGes());
		assertEquals("gRs falsch", gRs, r.getGRs());
		assertEquals("gBe falsch", gBe, r.getGBe());
		
		e = new Element("zonen");
		xom.appendChild(e);
		int i = 0;
		Attribute a = new Attribute("ko", "" + (++i));
		e.addAttribute(a);
		a = new Attribute("br", "" + (++i));
		e.addAttribute(a);
		a = new Attribute("rue", "" + (++i));
		e.addAttribute(a);
		a = new Attribute("ba", "" + (++i));
		e.addAttribute(a);
		a = new Attribute("la", "" + (++i));
		e.addAttribute(a);
		a = new Attribute("ra", "" + (++i));
		e.addAttribute(a);
		a = new Attribute("lb", "" + (++i));
		e.addAttribute(a);
		a = new Attribute("rb", "" + (++i));
		e.addAttribute(a);
		a = new Attribute("ges", "" + (++i));
		e.addAttribute(a);

		r = new Ruestung(name);
		mappy.map(xom, r);
		i = 0;
		assertEquals("ko falsch", ++i, r.getZoneKo());
		assertEquals("br falsch", ++i, r.getZoneBr());
		assertEquals("rue falsch", ++i, r.getZoneRue());
		assertEquals("ba falsch", ++i, r.getZoneBa());
		assertEquals("la falsch", ++i, r.getZoneLa());
		assertEquals("ra falsch", ++i, r.getZoneRa());
		assertEquals("lb falsch", ++i, r.getZoneLb());
		assertEquals("rb falsch", ++i, r.getZoneRb());
		assertEquals("ges falsch", ++i, r.getZoneGes());
		assertEquals("gRs falsch", gRs, r.getGRs());
		assertEquals("gBe falsch", gBe, r.getGBe());
	}

	@Ignore("Not implemented yet!") @Test public void testMapToXML() {
		fail("Not implemented yet!");
	}
}
