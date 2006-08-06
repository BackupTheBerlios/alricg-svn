/*
 * Created 22. September 2005 / 00:01:02
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.store.xom.map;

import junit.framework.TestCase;
import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.SchriftSprache;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.prozessor.utils.FormelSammlung;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;

/**
 * Tests für XOMMapper_SchriftSprache
 * 
 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
 */
public class XM_SchriftSprache_Test extends TestCase {

	private XOMMapper<CharElement> mappy;

	public XM_SchriftSprache_Test(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		ProgAdmin.messenger = new MessengerMock();
		mappy = new XOMMapper_SchriftSpracheBase();
	}

	public void testMapFromXML() {
		final Element xom = new Element("SchriftSpracheBase");

		final Element daten = new Element("daten");

		// Kostenklasse: A, B, ..., H, A+
		String kostenk = "A";
		daten.addAttribute(new Attribute("kostenKlasse", kostenk));

		// int
		String komplexitaet = "1";
		daten.addAttribute(new Attribute("komplexitaet", komplexitaet));

		xom.appendChild(daten);

		SchriftSprache s = new SchriftSpracheBase();
		mappy.map(xom, s);

		assertEquals("Kostenklasse falsch", KostenKlasse.A, s.getKostenKlasse());
		assertEquals("Komplexität falsch", 1, s.getKomplexitaet());

		final String[] kostenklassen = new String[] { "A", "B", "C", "D", "E",
				"F", "G", "H", "A+" };
		for (int i = 0; i < kostenklassen.length; i++) {

			daten.getAttribute("kostenKlasse").setValue(kostenklassen[i]);
			daten.getAttribute("komplexitaet").setValue("" + (i + 1));

			s = new SchriftSpracheBase();
			mappy.map(xom, s);

			assertEquals("Kostenklasse falsch", FormelSammlung
					.getKostenKlasseByValue(kostenklassen[i]), s
					.getKostenKlasse());
			assertEquals("Komplexität falsch", i + 1, s.getKomplexitaet());
		}

		try {
			daten.getAttribute("kostenKlasse").setValue("I");
			daten.getAttribute("komplexitaet").setValue("0");

			s = new SchriftSpracheBase();
			mappy.map(xom, s);

			assertEquals("Kostenklasse falsch", null, s.getKostenKlasse());
			assertEquals("Komplexität falsch", 0, s.getKomplexitaet());

			daten.getAttribute("kostenKlasse").setValue("A+");
			daten.getAttribute("komplexitaet").setValue("XX.XX");

			s = new SchriftSpracheBase();
			mappy.map(xom, s);

			assertEquals("Kostenklasse falsch", KostenKlasse.A_PLUS, s
					.getKostenKlasse());
			assertEquals("Komplexität falsch", 0, s.getKomplexitaet());

		} catch (Exception e) {
			fail("No exception expected");
		}
	}

	public void testMapToXML() {
		fail("Not implemented yet!");
	}

	/**
	 * Konkrete "do-nothing-special" Impl. der abtrakten Superklasse
	 * XOMMapper_SchriftSprache
	 * 
	 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
	 */
	private class XOMMapper_SchriftSpracheBase extends XOMMapper_SchriftSprache {
		public void map(Element xmlElement, CharElement charElement) {
			super.map(xmlElement, charElement);
		}

		public void map(CharElement charElement, Element xmlElement) {
			super.map(charElement, xmlElement);
		}
	}

	/**
	 * Konkrete "do-nothing-special" Impl. der abtrakten Superklasse
	 * SchriftFaehigkeit
	 * 
	 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
	 */
	private class SchriftSpracheBase extends SchriftSprache {
		public CharKomponente getCharKomponente() {
			return null;
		}
	}
}
