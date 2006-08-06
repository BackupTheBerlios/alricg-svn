/*
 * Created on 26.10.2005 / 16:34:26
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
import org.d3s.alricg.charKomponenten.Rasse;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.Zauber;
import org.d3s.alricg.charKomponenten.links.Auswahl;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;

public class XM_IdLink_Test extends TestCase {

	private XOMStoreObjectMother oma;
	private XOMMapper<IdLink> mappy;

	public XM_IdLink_Test(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_IdLink();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));
	}

	public void testMapFromXML() {
		Element e = new Element("idlink");

		String zielID = "TAL-zielID";
		e.addAttribute(new Attribute("id", zielID));

		Talent z = new Talent(zielID);
		oma.add(CharKomponente.talent, zielID, z);

		Rasse q = new Rasse("RAS-TUL");

		IdLink idLink = new IdLink(q, null);
		mappy.map(e, idLink);
		assertEquals("Quelle falsch", q, idLink.getQuellElement());
		assertNull("Auswahl falsch", idLink.getQuellAuswahl());
		assertEquals("Ziel falsch", z, idLink.getZiel());
		assertNull("Zweitziel falsch", idLink.getZweitZiel());
		assertEquals("Text falsch", "", idLink.getText());
		assertEquals("Wert falsch", CharElement.KEIN_WERT, idLink.getWert());
		assertFalse("Leitwert falsch", idLink.isLeitwert());

		String text = "bla bla ghfrti ÄÜOJSDJ jh f fff;";
		e.addAttribute(new Attribute("text", text));
		idLink = new IdLink(q, null);
		mappy.map(e, idLink);
		assertEquals("Quelle falsch", q, idLink.getQuellElement());
		assertNull("Auswahl falsch", idLink.getQuellAuswahl());
		assertEquals("Ziel falsch", z, idLink.getZiel());
		assertNull("Zweitziel falsch", idLink.getZweitZiel());
		assertEquals("Text falsch", text, idLink.getText());
		assertEquals("Wert falsch", CharElement.KEIN_WERT, idLink.getWert());
		assertFalse("Leitwert falsch", idLink.isLeitwert());

		int wert = 37465886;
		e.addAttribute(new Attribute("wert", "" + wert));
		idLink = new IdLink(q, null);
		mappy.map(e, idLink);
		assertEquals("Quelle falsch", q, idLink.getQuellElement());
		assertNull("Auswahl falsch", idLink.getQuellAuswahl());
		assertEquals("Ziel falsch", z, idLink.getZiel());
		assertNull("Zweitziel falsch", idLink.getZweitZiel());
		assertEquals("Text falsch", text, idLink.getText());
		assertEquals("Wert falsch", wert, idLink.getWert());
		assertFalse("Leitwert falsch", idLink.isLeitwert());

		String[] leitwerte = { "true", "false", "TRUE", "FALSE" };
		Attribute a = new Attribute("leitwert", "n/a");
		e.addAttribute(a);
		for (int i = 0; i < leitwerte.length; i++) {
			a.setValue(leitwerte[i]);
			idLink = new IdLink(q, null);
			mappy.map(e, idLink);
			assertEquals("Quelle falsch", q, idLink.getQuellElement());
			assertNull("Auswahl falsch", idLink.getQuellAuswahl());
			assertEquals("Ziel falsch", z, idLink.getZiel());
			assertNull("Zweitziel falsch", idLink.getZweitZiel());
			assertEquals("Text falsch", text, idLink.getText());
			assertEquals("Wert falsch", wert, idLink.getWert());
			assertEquals("Leitwert falsch", i % 2 == 0, idLink.isLeitwert());
		}
		
		String zweitZiel = "ZAU-AAA";
		Zauber zz = new Zauber(zweitZiel);
		zz.setName("NAME VON " + zweitZiel);
		oma.add(CharKomponente.zauber, zweitZiel, zz);
		
		e.addAttribute(new Attribute("linkId", zweitZiel));
		idLink = new IdLink(q, null);
		mappy.map(e, idLink);
		assertEquals("Quelle falsch", q, idLink.getQuellElement());
		assertNull("Auswahl falsch", idLink.getQuellAuswahl());
		assertEquals("Ziel falsch", z, idLink.getZiel());
		assertEquals("Zweitziel falsch", zz, idLink.getZweitZiel());
		assertEquals("Text falsch", text, idLink.getText());
		assertEquals("Wert falsch", wert, idLink.getWert());
		assertFalse("Leitwert falsch", idLink.isLeitwert());

		Auswahl auswahl = new Auswahl(q);
		idLink = new IdLink(null, auswahl);
		mappy.map(e, idLink);
		assertNull("Quelle falsch", idLink.getQuellElement());
		assertEquals("Auswahl falsch", auswahl, idLink.getQuellAuswahl());
		assertEquals("Ziel falsch", z, idLink.getZiel());
		assertEquals("Zweitziel falsch", zz, idLink.getZweitZiel());
		assertEquals("Text falsch", text, idLink.getText());
		assertEquals("Wert falsch", wert, idLink.getWert());
		assertFalse("Leitwert falsch", idLink.isLeitwert());

	
	}
}
