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
import org.d3s.alricg.charKomponenten.charZusatz.Schild;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;

public class XM_Schild_Test extends TestCase {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;

	public XM_Schild_Test(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();

		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_Schild();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));

	}

	public void testMapFromXML() {
		final Element xom = new Element("schild");
		oma.addErhaeltlichBei(xom, "REG-BAY");
		oma.add(CharKomponente.region, "REG-BAY", new RegionVolk("REG-BAY"));

		String name = "SLD-Langschild";
		Schild s = new Schild(name);
		mappy.map(xom, s);
		assertNull("Typ falsch", s.getTyp());
		assertEquals("BF falsch", CharElement.KEIN_WERT, s.getBf());
		assertEquals("INI falsch", CharElement.KEIN_WERT, s.getIni());
		assertEquals("WM-AT falsch", CharElement.KEIN_WERT, s.getWmAT());
		assertEquals("WM-PA falsch", CharElement.KEIN_WERT, s.getWmPA());

		Element e = new Element("typ");
		String typ = "magischer Langschild";
		e.appendChild(typ);
		s = new Schild(name);
		xom.appendChild(e);
		mappy.map(xom, s);
		assertEquals("Typ falsch", typ, s.getTyp());
		assertEquals("BF falsch", CharElement.KEIN_WERT, s.getBf());
		assertEquals("INI falsch", CharElement.KEIN_WERT, s.getIni());
		assertEquals("WM-AT falsch", CharElement.KEIN_WERT, s.getWmAT());
		assertEquals("WM-PA falsch", CharElement.KEIN_WERT, s.getWmPA());

		int bf = 3;
		int ini = 9;
		e = new Element("eigenschaften");
		xom.appendChild(e);
		s = new Schild(name);
		mappy.map(xom, s);
		assertEquals("Typ falsch", typ, s.getTyp());
		assertEquals("BF falsch", CharElement.KEIN_WERT, s.getBf());
		assertEquals("INI falsch", CharElement.KEIN_WERT, s.getIni());

		e.addAttribute(new Attribute("bf", "" + bf));
		s = new Schild(name);
		mappy.map(xom, s);
		assertEquals("Typ falsch", typ, s.getTyp());
		assertEquals("BF falsch", bf, s.getBf());
		assertEquals("INI falsch", CharElement.KEIN_WERT, s.getIni());

		e.addAttribute(new Attribute("ini", "" + ini));
		s = new Schild(name);
		mappy.map(xom, s);
		assertEquals("Typ falsch", typ, s.getTyp());
		assertEquals("BF falsch", bf, s.getBf());
		assertEquals("INI falsch", ini, s.getIni());
		
		int wmat = 7;
		int wmpa = -12;			
		e = new Element("wm");
		xom.appendChild(e);
		s = new Schild(name);
		mappy.map(xom, s);
		assertEquals("Typ falsch", typ, s.getTyp());
		assertEquals("BF falsch", bf, s.getBf());
		assertEquals("INI falsch", ini, s.getIni());
		assertEquals("WM-AT falsch", CharElement.KEIN_WERT, s.getWmAT());
		assertEquals("WM-PA falsch", CharElement.KEIN_WERT, s.getWmPA());
		
		e.addAttribute(new Attribute("at", "" + wmat));
		s = new Schild(name);
		mappy.map(xom, s);
		assertEquals("Typ falsch", typ, s.getTyp());
		assertEquals("BF falsch", bf, s.getBf());
		assertEquals("INI falsch", ini, s.getIni());
		assertEquals("WM-AT falsch", wmat, s.getWmAT());
		assertEquals("WM-PA falsch", CharElement.KEIN_WERT, s.getWmPA());
		
		e.addAttribute(new Attribute("pa", "" + wmpa));
		s = new Schild(name);
		mappy.map(xom, s);
		assertEquals("Typ falsch", typ, s.getTyp());
		assertEquals("BF falsch", bf, s.getBf());
		assertEquals("INI falsch", ini, s.getIni());
		assertEquals("WM-AT falsch", wmat, s.getWmAT());
		assertEquals("WM-PA falsch", wmpa, s.getWmPA());	
	}

	public void testMapToXML() {
		fail("Not implemented yet!");
	}
}
