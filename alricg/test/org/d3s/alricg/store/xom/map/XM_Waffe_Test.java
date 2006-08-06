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
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.charZusatz.Waffe;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;

public class XM_Waffe_Test extends TestCase {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;

	public XM_Waffe_Test(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();

		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_WaffeBase();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));

	}

	public void testMapFromXML() {
		final Element xom = new Element("WaffeBase");
		oma.addErhaeltlichBei(xom, "REG-B");
		oma.add(CharKomponente.region, "REG-B", new RegionVolk("REG-B"));

		Waffe w = new WaffeBase();
		mappy.map(xom, w);
		assertNull("TP falsch", w.getTP());
		assertEquals("Länge falsch", CharElement.KEIN_WERT, w.getLaenge());
		assertEquals("Ini falsch", CharElement.KEIN_WERT, w.getIni());
		assertEquals("Talente falsch", 0, w.getTalent().length);
		
		Element e = new Element("tp");
		xom.appendChild(e);
		w = new WaffeBase();
		mappy.map(xom, w);
		assertNotNull("TP falsch", w.getTP());
		assertEquals("TP falsch", 0, w.getTP().getAnzahlWuerfel().length);
		assertEquals("TP falsch", 0, w.getTP().getAugenWuerfel().length);
		assertEquals("TP falsch", 0, w.getTP().getFestWert());
		assertEquals("Länge falsch", CharElement.KEIN_WERT, w.getLaenge());
		assertEquals("Ini falsch", CharElement.KEIN_WERT, w.getIni());
		assertEquals("Talente falsch", 0, w.getTalent().length);
				
		int w6 = 3;
		e.addAttribute(new Attribute("W6", "" + w6));
		w = new WaffeBase();
		mappy.map(xom, w);
		assertNotNull("TP falsch", w.getTP());
		assertEquals("TP falsch", 1, w.getTP().getAnzahlWuerfel().length);
		assertEquals("TP falsch", w6, w.getTP().getAnzahlWuerfel()[0].intValue());
		assertEquals("TP falsch", 1, w.getTP().getAugenWuerfel().length);
		assertEquals("TP falsch", 6, w.getTP().getAugenWuerfel()[0].intValue());
		assertEquals("TP falsch", 0, w.getTP().getFestWert());

		int w20 = 8;
		e.addAttribute(new Attribute("W20", "" + w20));
		w = new WaffeBase();
		mappy.map(xom, w);
		assertNotNull("TP falsch", w.getTP());
		assertEquals("TP falsch", 2, w.getTP().getAnzahlWuerfel().length);
		assertEquals("TP falsch", w6, w.getTP().getAnzahlWuerfel()[0].intValue());
		assertEquals("TP falsch", w20, w.getTP().getAnzahlWuerfel()[1].intValue());
		assertEquals("TP falsch", 2, w.getTP().getAugenWuerfel().length);
		assertEquals("TP falsch", 6, w.getTP().getAugenWuerfel()[0].intValue());
		assertEquals("TP falsch", 20, w.getTP().getAugenWuerfel()[1].intValue());
		assertEquals("TP falsch", 0, w.getTP().getFestWert());

		int plus = 67;
		e.addAttribute(new Attribute("plus", "" + plus));
		w = new WaffeBase();
		mappy.map(xom, w);
		assertNotNull("TP falsch", w.getTP());
		assertEquals("TP falsch", 2, w.getTP().getAnzahlWuerfel().length);
		assertEquals("TP falsch", w6, w.getTP().getAnzahlWuerfel()[0].intValue());
		assertEquals("TP falsch", w20, w.getTP().getAnzahlWuerfel()[1].intValue());
		assertEquals("TP falsch", 2, w.getTP().getAugenWuerfel().length);
		assertEquals("TP falsch", 6, w.getTP().getAugenWuerfel()[0].intValue());
		assertEquals("TP falsch", 20, w.getTP().getAugenWuerfel()[1].intValue());
		assertEquals("TP falsch", plus, w.getTP().getFestWert());

		e = new Element("eigenschaften");
		xom.appendChild(e);
		w = new WaffeBase();
		mappy.map(xom, w);
		assertEquals("Länge falsch", CharElement.KEIN_WERT, w.getLaenge());
		assertEquals("Ini falsch", CharElement.KEIN_WERT, w.getIni());
		
		int laenge = 135;
		int ini = 13;
		e.addAttribute(new Attribute("laenge", "" + laenge));
		e.addAttribute(new Attribute("ini", "" + ini));
		w = new WaffeBase();
		mappy.map(xom, w);
		assertEquals("Länge falsch", laenge, w.getLaenge());
		assertEquals("Ini falsch", ini, w.getIni());
		assertEquals("Talente falsch", 0, w.getTalent().length);
		
		for (int i = 0; i < 10; i++) {
			Talent t = new Talent("TAL-" + i);
			oma.add(CharKomponente.talent, "TAL-" + i, t);
			e = new Element("talentId");
			xom.appendChild(e);
			e.appendChild("TAL-" + i);
			w = new WaffeBase();
			mappy.map(xom, w);
			assertEquals("Talente falsch", i+1, w.getTalent().length);
			assertEquals("Talente falsch", t, w.getTalent()[i]);
		}
		
		w = new WaffeBase();
		mappy.map(xom, w);
		assertNotNull("TP falsch", w.getTP());
		assertEquals("TP falsch", 2, w.getTP().getAnzahlWuerfel().length);
		assertEquals("TP falsch", w6, w.getTP().getAnzahlWuerfel()[0].intValue());
		assertEquals("TP falsch", w20, w.getTP().getAnzahlWuerfel()[1].intValue());
		assertEquals("TP falsch", 2, w.getTP().getAugenWuerfel().length);
		assertEquals("TP falsch", 6, w.getTP().getAugenWuerfel()[0].intValue());
		assertEquals("TP falsch", 20, w.getTP().getAugenWuerfel()[1].intValue());
		assertEquals("TP falsch", plus, w.getTP().getFestWert());
		assertEquals("Länge falsch", laenge, w.getLaenge());
		assertEquals("Ini falsch", ini, w.getIni());
		assertEquals("Talente falsch", 10, w.getTalent().length);
		for(int i = 0; i < 10; i++) {
		assertEquals("Talente falsch", "TAL-" + i, w.getTalent()[i].getId());
		}
	}

	public void testMapToXML() {
		fail("Not implemented yet!");
	}

	/**
	 * Konkrete "do-nothing-special" Impl. der abtrakten Superklasse
	 * XOMMapper_Waffe
	 * 
	 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
	 */
	private class XOMMapper_WaffeBase extends XOMMapper_Waffe {
		public void map(Element xmlElement, CharElement charElement) {
			super.map(xmlElement, charElement);
		}

		public void map(CharElement charElement, Element xmlElement) {
			super.map(charElement, xmlElement);
		}
	}

	/**
	 * Konkrete "do-nothing-special" Impl. der abtrakten Superklasse Waffe
	 * 
	 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
	 */
	private class WaffeBase extends Waffe {
		public CharKomponente getCharKomponente() {
			return null;
		}
	}
}
