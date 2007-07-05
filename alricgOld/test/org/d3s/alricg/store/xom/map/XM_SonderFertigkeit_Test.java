/*
 * Created 22. September 2005 / 00:01:02
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.store.xom.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;

import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Sonderfertigkeit;
import org.d3s.alricg.charKomponenten.Sonderfertigkeit.Art;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests für XOMMapper_SonderFertigkeit
 * 
 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
 */
public class XM_SonderFertigkeit_Test {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;

	@Before public void setUp() throws Exception {
		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_Sonderfertigkeit();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));
	}

	@Test public void testMapFromXML() {
		final Element xom = new Element("FertigkeitBase");
		oma.addGPKosten(xom);

		String name = "SF-ScienceFiction";

		Sonderfertigkeit s = new Sonderfertigkeit(name);
		try {
			mappy.map(xom, s);
			fail("NPE expected");
		} catch (NullPointerException npe) {
			// everything is fine. just continue;
		}
		Element e = new Element("art");
		xom.appendChild(e);
		for (Art art : Art.values()) {
			e.appendChild(art.getValue());
			s = new Sonderfertigkeit(name);
			mappy.map(xom, s);
			assertEquals("permAsp", 0, s.getPermAsp());
			assertEquals("permKa", 0, s.getPermKa());
			assertEquals("permLep", 0, s.getPermLep());
			assertEquals("apKosten", CharElement.KEIN_WERT, s.getApKosten());			
			assertEquals("Art falsch", art, s.getArt());
			e.removeChild(0);
		}

		s = new Sonderfertigkeit(name);
		mappy.map(xom, s);
		assertEquals("permAsp", 0, s.getPermAsp());
		assertEquals("permKa", 0, s.getPermKa());
		assertEquals("permLep", 0, s.getPermLep());
		assertEquals("apKosten", CharElement.KEIN_WERT, s.getApKosten());			
		assertNull("Art falsch", s.getArt());
		
		
		e = new Element("permAsP");
		xom.appendChild(e);
		e.appendChild("n/a");
		s = new Sonderfertigkeit(name);
		mappy.map(xom, s);
		assertEquals("permAsp falsch", 0, s.getPermAsp());
		for(int i = -5; i < 25; i++) {
			e.removeChild(0);
			e.appendChild("" + i);
			s = new Sonderfertigkeit(name);
			mappy.map(xom, s);
			assertEquals("permAsp falsch", i, s.getPermAsp());
		}
		
		e = new Element("permKa");
		xom.appendChild(e);
		e.appendChild("n/a");
		s = new Sonderfertigkeit(name);
		mappy.map(xom, s);
		assertEquals("permKa falsch", 0, s.getPermKa());
		for(int i = -5; i < 25; i++) {
			e.removeChild(0);
			e.appendChild("" + i);
			s = new Sonderfertigkeit(name);
			mappy.map(xom, s);
			assertEquals("permKa falsch", i, s.getPermKa());
		}

		e = new Element("permLeP");
		xom.appendChild(e);
		e.appendChild("n/a");
		s = new Sonderfertigkeit(name);
		mappy.map(xom, s);
		assertEquals("permLep falsch", 0, s.getPermLep());
		for(int i = -5; i < 25; i++) {
			e.removeChild(0);
			e.appendChild("" + i);
			s = new Sonderfertigkeit(name);
			mappy.map(xom, s);
			assertEquals("permLep falsch", i, s.getPermLep());
		}

		Attribute a = new Attribute("ap", "n/a");
		xom.addAttribute(a);
		s = new Sonderfertigkeit(name);
		mappy.map(xom, s);
		assertEquals("ApKosten falsch", CharElement.KEIN_WERT, s.getApKosten());
		for(int i = -5; i < 25; i++) {
			a.setValue("" + i);
			s = new Sonderfertigkeit(name);
			mappy.map(xom, s);
			assertEquals("ApKosten falsch", i, s.getApKosten());
		}

	}

	@Ignore("Not implemented yet!") @Test public void testMapToXML() {
		fail("Not implemented yet!");
	}
}