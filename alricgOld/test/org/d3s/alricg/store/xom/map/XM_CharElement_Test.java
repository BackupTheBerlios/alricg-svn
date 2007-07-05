/*
 * Created 22. September 2005 / 00:01:02
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.store.xom.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.RegelAnmerkung;
import org.d3s.alricg.controller.CharKomponente;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests für XOMMapper_CharElement
 * 
 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
 */
public class XM_CharElement_Test {

	private XOMMapper<CharElement> mappy;

	@Before public void setUp() throws Exception {
		mappy = new XOMMapper_CharElementBase();
	}

	@Test public void testMapFromXMLSimple() {
		final Element xom = new Element("CharElementBase");

		String name = "ElementName";
		xom.addAttribute(new Attribute("name", name));

		Element aChild = new Element("anzeigen"); // true, false, fehlt
		aChild.addAttribute(new Attribute("anzeigen", "true"));
		String anzeigenText = "frise fräse fröse";
		aChild.appendChild(anzeigenText);
		xom.appendChild(aChild);

		String beschreibung = "Siehe MBK S. 10";
		Element bChild = new Element("beschreibung");
		bChild.appendChild(beschreibung);
		xom.appendChild(bChild);

		String sammelBegriff = "Rammelbegriff!";
		Element cChild = new Element("sammelbegriff");
		cChild.appendChild(sammelBegriff);
		xom.appendChild(cChild);

		CharElement charElement = new CharElementBase();
		mappy.map(xom, charElement);
		assertEquals("Name falsch", name, charElement.getName());
		assertTrue("Anzeigen falsch", charElement.isAnzeigen());
		assertEquals("Anzeigen Text falsch", "", charElement.getAnzeigenText());
		assertEquals("Beschreibung falsch", beschreibung, charElement
				.getBeschreibung());
		assertEquals("Sammelbegriff falsch", sammelBegriff, charElement
				.getSammelBegriff());

		aChild.getAttribute("anzeigen").setValue("false");
		charElement = new CharElementBase();
		mappy.map(xom, charElement);
		assertEquals("Name falsch", name, charElement.getName());
		assertFalse("Anzeigen falsch", charElement.isAnzeigen());
		assertEquals("Anzeigen Text falsch", anzeigenText, charElement
				.getAnzeigenText());
		assertEquals("Beschreibung falsch", beschreibung, charElement
				.getBeschreibung());
		assertEquals("Sammelbegriff falsch", sammelBegriff, charElement
				.getSammelBegriff());

		aChild.getAttribute("anzeigen").setValue("TRUE");
		charElement = new CharElementBase();
		mappy.map(xom, charElement);
		assertEquals("Name falsch", name, charElement.getName());
		assertTrue("Anzeigen falsch", charElement.isAnzeigen());
		assertEquals("Anzeigen Text falsch", "", charElement.getAnzeigenText());
		assertEquals("Beschreibung falsch", beschreibung, charElement
				.getBeschreibung());
		assertEquals("Sammelbegriff falsch", sammelBegriff, charElement
				.getSammelBegriff());

		aChild.getAttribute("anzeigen").setValue("FALSE");
		charElement = new CharElementBase();
		mappy.map(xom, charElement);
		assertEquals("Name falsch", name, charElement.getName());
		assertFalse("Anzeigen falsch", charElement.isAnzeigen());
		assertEquals("Anzeigen Text falsch", anzeigenText, charElement
				.getAnzeigenText());
		assertEquals("Beschreibung falsch", beschreibung, charElement
				.getBeschreibung());
		assertEquals("Sammelbegriff falsch", sammelBegriff, charElement
				.getSammelBegriff());

		aChild.getAttribute("anzeigen").setValue("XXXFALdDSETRUE");
		charElement = new CharElementBase();
		try {
			mappy.map(xom, charElement);
			fail("Exception expected");
		} catch (Throwable t) {
			assertTrue("AssertionException expected",
					t instanceof AssertionError);
		}

		xom.removeChild(aChild);
		charElement = new CharElementBase();
		mappy.map(xom, charElement);
		assertEquals("Name falsch", name, charElement.getName());
		assertTrue("Anzeigen falsch", charElement.isAnzeigen());
		assertEquals("Anzeigen Text falsch", "", charElement.getAnzeigenText());
		assertEquals("Beschreibung falsch", beschreibung, charElement
				.getBeschreibung());
		assertEquals("Sammelbegriff falsch", sammelBegriff, charElement
				.getSammelBegriff());

		xom.removeChild(bChild);
		charElement = new CharElementBase();
		mappy.map(xom, charElement);
		assertEquals("Name falsch", name, charElement.getName());
		assertTrue("Anzeigen falsch", charElement.isAnzeigen());
		assertEquals("Anzeigen Text falsch", "", charElement.getAnzeigenText());
		assertEquals("Beschreibung falsch", "", charElement.getBeschreibung());
		assertEquals("Sammelbegriff falsch", sammelBegriff, charElement
				.getSammelBegriff());

		xom.removeChild(cChild);
		charElement = new CharElementBase();
		mappy.map(xom, charElement);
		assertEquals("Name falsch", name, charElement.getName());
		assertTrue("Anzeigen falsch", charElement.isAnzeigen());
		assertEquals("Anzeigen Text falsch", "", charElement.getAnzeigenText());
		assertEquals("Beschreibung falsch", "", charElement.getBeschreibung());
		assertEquals("Sammelbegriff falsch", "", charElement.getSammelBegriff());
	}

	@Test public void testMapFromXMLRegelAnmerkungen() {
		final Element xom = new Element("CharElementBase");
		xom.addAttribute(new Attribute("name", "ElementName"));

		final Element child = new Element("regelAnmerkungen");
		xom.appendChild(child);

		final String[] texte = { "RegelAnmerkung: Regel 1",
				"RegelAnmerkung: toDo 1", "RegelAnmerkung: Regel 2",
				"RegelAnmerkung: toDo 2" };

		final RegelAnmerkung.Modus[] modi = { RegelAnmerkung.Modus.regel,
				RegelAnmerkung.Modus.todo, RegelAnmerkung.Modus.regel,
				RegelAnmerkung.Modus.todo };

		Element grandChild = new Element("regel");
		grandChild.addAttribute(new Attribute("modus", "regel"));
		grandChild.appendChild(texte[0]);
		child.appendChild(grandChild);

		grandChild = new Element("regel");
		grandChild.addAttribute(new Attribute("modus", "toDo"));
		grandChild.appendChild(texte[1]);
		child.appendChild(grandChild);

		grandChild = new Element("regel");
		grandChild.addAttribute(new Attribute("modus", "regel"));
		grandChild.appendChild(texte[2]);
		child.appendChild(grandChild);

		grandChild = new Element("regel");
		grandChild.addAttribute(new Attribute("modus", "toDo"));
		grandChild.appendChild(texte[3]);
		child.appendChild(grandChild);

		CharElement charElement = new CharElementBase();
		mappy.map(xom, charElement);
		RegelAnmerkung ra = charElement.getRegelAnmerkung();
		assertEquals("Anmerkungszahl falsch", 4, ra.getAnmerkungen().length);
		assertEquals("Modizahl falsch", 4, ra.getModi().length);
		for (int i = 0; i < ra.getAnmerkungen().length; i++) {
			assertEquals("Text falsch", texte[i], ra.getAnmerkungen()[i]);
			assertEquals("Modus falsch", modi[i], ra.getModi()[i]);
		}

		grandChild = new Element("regel");
		grandChild.addAttribute(new Attribute("modus", "FIGGARO"));
		grandChild.appendChild(texte[1]);
		child.appendChild(grandChild);
		try {
			charElement = new CharElementBase();
			mappy.map(xom, charElement);

		} catch (Throwable t) {
			assertTrue("AssertionException expected",
					t instanceof AssertionError);
		}

	}

	@Test public void testMapFromXMLSonderregeln() {
		// Sonderregeln werden z.Zt. nicht gemappt!
		// => Teste auch das, dmits einen Fehler gibt, falls es doch mal impl.
		// wird.

		final Element xom = new Element("CharElementBase");
		xom.addAttribute(new Attribute("name", "ElementName"));

		final Element child = new Element("sonderregel");
		child.addAttribute(new Attribute("id", "eineKomischeID"));
		xom.appendChild(child);

		final CharElement charElement = new CharElementBase();
		mappy.map(xom, charElement);
		assertNull(
				"SonderregelMapping wird noch nicht getestet. Please implement ASAP!",
				charElement.getSonderregel());
		assertFalse(
				"SonderregelMapping wird noch nicht getestet. Please implement ASAP!",
				charElement.hasSonderregel());

	}

	@Ignore("Not implemented yet!") @Test public void testMapToXML() {
		fail("Not implemented yet!");
	}

	/**
	 * Konkrete "do-nothing-special" Impl. der abtrakten Superklasse
	 * XOMMapper_CharElement
	 * 
	 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
	 */
	private class XOMMapper_CharElementBase extends XOMMapper_CharElement {

		public void map(Element xmlElement, CharElement charElement) {
			super.map(xmlElement, charElement);
		}

		public void map(CharElement charElement, Element xmlElement) {
			super.map(charElement, xmlElement);
		}
	}

	/**
	 * Konkrete "do-nothing-special" Impl. der abtrakten Superklasse CharElement
	 * 
	 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
	 */
	private class CharElementBase extends CharElement {

		public CharKomponente getCharKomponente() {
			return null;
		}
	}
}
