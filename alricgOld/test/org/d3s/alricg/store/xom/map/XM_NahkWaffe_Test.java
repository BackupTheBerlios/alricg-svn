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
import org.d3s.alricg.charKomponenten.charZusatz.NahkWaffe;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class XM_NahkWaffe_Test {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;

	@Before public void setUp() throws Exception {
		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_NahkWaffe();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));
	}

	@Test public void testMapFromXML() {
		final Element xom = new Element("nkWaffe");
		oma.addErhaeltlichBei(xom, "REG-BAY");
		oma.add(CharKomponente.region, "REG-BAY", new RegionVolk("REG-BAY"));

		String name = "NKW-Doppelkhunchomer";
		NahkWaffe n = new NahkWaffe(name);
		mappy.map(xom, n);
		assertNull("Distanzklasse flasch", n.getDk());
		assertEquals("KKAb falsch", CharElement.KEIN_WERT, n.getKkAb());
		assertEquals("KKStufe falsch", CharElement.KEIN_WERT, n.getKkStufe());
		assertEquals("BF falsch", CharElement.KEIN_WERT, n.getBf());
		assertEquals("WM-AT falsch", CharElement.KEIN_WERT, n.getWmAT());
		assertEquals("WM-PA falsch", CharElement.KEIN_WERT, n.getWmPA());

		Element e = new Element("tp");
		xom.appendChild(e);
		n = new NahkWaffe(name);
		mappy.map(xom, n);
		assertNull("Distanzklasse flasch", n.getDk());
		assertEquals("KKAb falsch", CharElement.KEIN_WERT, n.getKkAb());
		assertEquals("KKStufe falsch", CharElement.KEIN_WERT, n.getKkStufe());
		assertEquals("BF falsch", CharElement.KEIN_WERT, n.getBf());
		assertEquals("WM-AT falsch", CharElement.KEIN_WERT, n.getWmAT());
		assertEquals("WM-PA falsch", CharElement.KEIN_WERT, n.getWmPA());

		int kkab = 485;
		e.addAttribute(new Attribute("kk-ab", "" + kkab));
		n = new NahkWaffe(name);
		mappy.map(xom, n);
		assertNull("Distanzklasse flasch", n.getDk());
		assertEquals("KKAb falsch", kkab, n.getKkAb());
		assertEquals("KKStufe falsch", CharElement.KEIN_WERT, n.getKkStufe());
		assertEquals("BF falsch", CharElement.KEIN_WERT, n.getBf());
		assertEquals("WM-AT falsch", CharElement.KEIN_WERT, n.getWmAT());
		assertEquals("WM-PA falsch", CharElement.KEIN_WERT, n.getWmPA());

		int kkstufe = 32;
		e.addAttribute(new Attribute("kk-stufe", "" + kkstufe));
		n = new NahkWaffe(name);
		mappy.map(xom, n);
		assertNull("Distanzklasse flasch", n.getDk());
		assertEquals("KKAb falsch", kkab, n.getKkAb());
		assertEquals("KKStufe falsch", kkstufe, n.getKkStufe());
		assertEquals("BF falsch", CharElement.KEIN_WERT, n.getBf());
		assertEquals("WM-AT falsch", CharElement.KEIN_WERT, n.getWmAT());
		assertEquals("WM-PA falsch", CharElement.KEIN_WERT, n.getWmPA());

		int bf = 3;
		e = new Element("eigenschaften");
		xom.appendChild(e);
		n = new NahkWaffe(name);
		mappy.map(xom, n);
		assertEquals("BF falsch", CharElement.KEIN_WERT, n.getBf());

		e.addAttribute(new Attribute("bf", "" + bf));
		n = new NahkWaffe(name);
		mappy.map(xom, n);
		assertEquals("BF falsch", bf, n.getBf());
		assertNull("Distanzklasse flasch", n.getDk());

		String dk = "hossa, hossa!";
		e.addAttribute(new Attribute("dk", dk));
		n = new NahkWaffe(name);
		mappy.map(xom, n);
		assertEquals("BF falsch", bf, n.getBf());
		assertEquals("Distanzklasse flasch", dk, n.getDk());

		int wmat = 7;
		int wmpa = -12;
		e = new Element("wm");
		xom.appendChild(e);
		n = new NahkWaffe(name);
		mappy.map(xom, n);
		assertEquals("BF falsch", bf, n.getBf());
		assertEquals("WM-AT falsch", CharElement.KEIN_WERT, n.getWmAT());
		assertEquals("WM-PA falsch", CharElement.KEIN_WERT, n.getWmPA());

		e.addAttribute(new Attribute("at", "" + wmat));
		n = new NahkWaffe(name);
		mappy.map(xom, n);
		assertEquals("BF falsch", bf, n.getBf());
		assertEquals("WM-AT falsch", wmat, n.getWmAT());
		assertEquals("WM-PA falsch", CharElement.KEIN_WERT, n.getWmPA());

		e.addAttribute(new Attribute("pa", "" + wmpa));
		n = new NahkWaffe(name);
		mappy.map(xom, n);
		assertEquals("BF falsch", bf, n.getBf());
		assertEquals("WM-AT falsch", wmat, n.getWmAT());
		assertEquals("WM-PA falsch", wmpa, n.getWmPA());
	}

	@Ignore("Not implemented yet!") @Test public void testMapToXML() {
		fail("Not implemented yet!");
	}
}
