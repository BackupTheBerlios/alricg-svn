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
import org.d3s.alricg.charKomponenten.charZusatz.FkWaffe;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;

public class XM_FkWaffe_Test extends TestCase {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;

	public XM_FkWaffe_Test(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();

		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_FkWaffe();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));

	}

	public void testMapFromXML() {
		final Element xom = new Element("fkWaffe");
		oma.addErhaeltlichBei(xom, "REG-BAY");
		oma.add(CharKomponente.region, "REG-BAY", new RegionVolk("REG-BAY"));

		String name = "FKW-Fernkraftwerk";
		FkWaffe f = new FkWaffe(name);
		mappy.map(xom, f);
		assertEquals("laden falsch", CharElement.KEIN_WERT, f.getLaden());
		assertEquals("reichweite falsch", CharElement.KEIN_WERT, f
				.getReichweite());
		assertNull("reichweiteTpPlus flasch", f.getReichweiteTpPlus());

		Element e = new Element("tp");
		xom.appendChild(e);

		String plusRW = "gollum/smeagol/deagol/my preciousssss/!!!/+5";
		e.addAttribute(new Attribute("plus-reichweite", plusRW));
		f = new FkWaffe(name);
		mappy.map(xom, f);
		assertEquals("laden falsch", CharElement.KEIN_WERT, f.getLaden());
		assertEquals("reichweite falsch", CharElement.KEIN_WERT, f
				.getReichweite());
		assertEquals("reichweiteTpPlus flasch", plusRW, f.getReichweiteTpPlus());

		e = new Element("eigenschaften");
		xom.appendChild(e);
		f = new FkWaffe(name);
		mappy.map(xom, f);
		assertEquals("laden falsch", CharElement.KEIN_WERT, f.getLaden());
		assertEquals("reichweite falsch", CharElement.KEIN_WERT, f
				.getReichweite());
		assertEquals("reichweiteTpPlus flasch", plusRW, f.getReichweiteTpPlus());

		int laden = 445;
		e.addAttribute(new Attribute("laden", "" + laden));
		f = new FkWaffe(name);
		mappy.map(xom, f);
		assertEquals("laden falsch", laden, f.getLaden());
		assertEquals("reichweite falsch", CharElement.KEIN_WERT, f
				.getReichweite());
		assertEquals("reichweiteTpPlus flasch", plusRW, f.getReichweiteTpPlus());

		int rw = 554;
		e.addAttribute(new Attribute("reichweite", "" + rw));
		f = new FkWaffe(name);
		mappy.map(xom, f);
		assertEquals("laden falsch", laden, f.getLaden());
		assertEquals("reichweite falsch", rw, f.getReichweite());
		assertEquals("reichweiteTpPlus flasch", plusRW, f.getReichweiteTpPlus());

	}

	public void testMapToXML() {
		fail("Not implemented yet!");
	}
}
