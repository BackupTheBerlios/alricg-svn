/*
 * Created 22. September 2005 / 00:01:02
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.store.xom.map;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Fertigkeit;
import org.d3s.alricg.charKomponenten.Werte;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests für XOMMapper_Fertigkeit
 * 
 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
 */
public class XM_Fertigkeit_Test {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;

	@Before public void setUp() throws Exception {
		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_FertigkeitBase();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));
	}

	@Test public void testMapFromXML() {
		final Element xom = new Element("FertigkeitBase");

		oma.addGPKosten(xom);
		Fertigkeit f = new FertigkeitBase();
		mappy.map(xom, f);
		assertEquals("GP falsch", 15, f.getGpKosten());
		assertEquals("CharArten falsch", 0, f.getFuerWelcheChars().length);
		assertNull("Voraussetzung falsch", f.getVoraussetzung());
		assertTrue("Wählbar falsch", f.isWaehlbar());
		assertNull("Vorschläge falsch", f.getTextVorschlaege());
		assertFalse("Element falsch", f.isElementAngabe());
		assertFalse("Text falsch", f.hasText());
		assertNull("AdditionsID falsch", f.getAdditionsID());
		assertEquals("Additionswert falsch", CharElement.KEIN_WERT, f
				.getAdditionsWert());

		xom.getAttribute("gp").setValue("0");
		f = new FertigkeitBase();
		mappy.map(xom, f);
		assertEquals("GP falsch", 0, f.getGpKosten());

		Element e = new Element("addition");
		String id = "SSS";
		e.addAttribute(new Attribute("id", id));
		e.addAttribute(new Attribute("id", id));
		xom.appendChild(e);
		f = new FertigkeitBase();
		mappy.map(xom, f);
		assertEquals("AdditionsID falsch", id, f.getAdditionsID());
		assertEquals("Additionswert falsch", CharElement.KEIN_WERT, f
				.getAdditionsWert());

		int wert = 366574;
		e.addAttribute(new Attribute("wertigkeit", "nnn" + wert));
		f = new FertigkeitBase();
		mappy.map(xom, f);
		assertEquals("AdditionsID falsch", id, f.getAdditionsID());
		assertEquals("Additionswert falsch", CharElement.KEIN_WERT, f
				.getAdditionsWert());

		e.getAttribute("wertigkeit").setValue("" + wert);
		f = new FertigkeitBase();
		mappy.map(xom, f);
		assertEquals("AdditionsID falsch", id, f.getAdditionsID());
		assertEquals("Additionswert falsch", wert, f.getAdditionsWert());

		String[] bools = { "true", "false", "TRUE", "FALSE" };
		e = new Element("hatText");
		xom.appendChild(e);
		for (int i = 0; i < bools.length; i++) {
			e.appendChild(bools[i]);
			f = new FertigkeitBase();
			mappy.map(xom, f);
			assertEquals("Text falsch", i % 2 == 0, f.hasText());
			e.removeChild(0);
		}
		xom.removeChild(e);

		e = new Element("hatElement");
		xom.appendChild(e);
		for (int i = 0; i < bools.length; i++) {
			e.appendChild(bools[i]);
			f = new FertigkeitBase();
			mappy.map(xom, f);
			assertEquals("Element falsch", i % 2 == 0, f.isElementAngabe());
			e.removeChild(0);
		}
		xom.removeChild(e);

		e = new Element("textVorschlaege");
		xom.appendChild(e);
		String[] texte = { "A", "BB", "CCC", "QWERTZ" };
		for (int i = 0; i < texte.length; i++) {
			Element e2 = new Element("text");
			e2.appendChild(texte[i]);
			e.appendChild(e2);
			f = new FertigkeitBase();
			mappy.map(xom, f);
			assertEquals("Vorschläge falsch", i + 1,
					f.getTextVorschlaege().length);
			assertEquals("Vorschläge falsch", texte[i],
					f.getTextVorschlaege()[i]);
		}
		xom.removeChild(e);

		e = new Element("istWaehlbar");
		xom.appendChild(e);
		for (int i = 0; i < bools.length; i++) {
			e.appendChild(bools[i]);
			f = new FertigkeitBase();
			mappy.map(xom, f);
			assertEquals("Wählbar falsch", i % 2 == 0, f.isWaehlbar());
			e.removeChild(0);
		}
		xom.removeChild(e);

		List<Element> children = new ArrayList<Element>();
		for (Werte.CharArten art : Werte.CharArten.values()) {
			e = new Element("fuerWelcheChars");
			xom.appendChild(e);
			children.add(e);
			e.appendChild(art.getValue());
			f = new FertigkeitBase();
			mappy.map(xom, f);
			int s = children.size();
			assertEquals("CharArten falsch", s, f.getFuerWelcheChars().length);
			assertEquals("CharArten falsch", art, f.getFuerWelcheChars()[s-1]);
		}
		for (Iterator<Element> i = children.iterator(); i.hasNext();) {
			xom.removeChild(i.next());
		}
		children = null;
		
		e = new Element("voraussetzungen");
		xom.appendChild(e);
		f = new FertigkeitBase();
		mappy.map(xom, f);
		assertNotNull("Voraussetzung falsch", f.getVoraussetzung());
		assertEquals("Voraussetzung falsch", f, f.getVoraussetzung().getQuelle());
		assertEquals("Voraussetzung falsch", 0, f.getVoraussetzung().getVoraussetzungsAltervativen().length);
		assertEquals("Voraussetzung falsch", null, f.getVoraussetzung().getNichtErlaubt());

	}

	@Ignore("Not implemented yet!") @Test public void testMapToXML() {
		fail("Not implemented yet!");
	}

	/**
	 * Konkrete "do-nothing-special" Impl. der abtrakten Superklasse
	 * XOMMapper_Fertigkeit
	 * 
	 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
	 */
	private class XOMMapper_FertigkeitBase extends XOMMapper_Fertigkeit {
		public void map(Element xmlElement, CharElement charElement) {
			super.map(xmlElement, charElement);
		}

		public void map(CharElement charElement, Element xmlElement) {
			super.map(charElement, xmlElement);
		}
	}

	/**
	 * Konkrete "do-nothing-special" Impl. der abtrakten Superklasse Fertigkeit
	 * 
	 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
	 */
	private class FertigkeitBase extends Fertigkeit {
		public CharKomponente getCharKomponente() {
			return null;
		}
	}
}