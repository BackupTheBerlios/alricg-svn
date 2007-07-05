/*
 * Created on 10.10.2005 / 20:52:15
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;

import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class XM_Talent_Test {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;

	@Before public void setUp() throws Exception {
		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_Talent();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));

	}

	@Test public void testMapFromXML() {
		try {
			final Element xom = new Element("talent");
			oma.addProbe(xom);
			oma.addKostenKlasse(xom);

			Element e = new Element("einordnung");
			Attribute art = new Attribute("art", "n/a");
			e.addAttribute(art);
			Attribute sorte = new Attribute("sorte", "n/a");
			e.addAttribute(sorte);
			xom.appendChild(e);

			// nix gesetzt
			final String name = "TAL-LackUndLeder";
			Talent t = new Talent(name);
			mappy.map(xom, t);
			assertNull("Art falsch", t.getArt());
			assertNull("Sorte falsch", t.getSorte());
			assertNull("Spezialisierungen falsch", t.getSpezialisierungen());
			assertNull("Voraussetzungen falsch", t.getVoraussetzung());

			// Art
			Talent.Art[] arten = { Talent.Art.basis, Talent.Art.beruf,
					Talent.Art.spezial };
			for (int i = 0; i < arten.length; i++) {
				art.setValue(arten[i].getValue());
				t = new Talent(name);
				mappy.map(xom, t);
				assertEquals("Art falsch", arten[i], t.getArt());
				assertNull("Sorte falsch", t.getSorte());
				assertNull("Spezialisierungen falsch", t.getSpezialisierungen());
				assertNull("Voraussetzungen falsch", t.getVoraussetzung());
			}
			art.setValue("n/a");

			// Sorte
			Talent.Sorte[] sorten = { Talent.Sorte.kampf, Talent.Sorte.koerper,
					Talent.Sorte.gesellschaft, Talent.Sorte.natur,
					Talent.Sorte.wissen, Talent.Sorte.handwerk };
			for (int i = 0; i < sorten.length; i++) {
				sorte.setValue(sorten[i].getValue());
				t = new Talent(name);
				mappy.map(xom, t);
				assertEquals("Sorte falsch", sorten[i], t.getSorte());
				assertNull("Art falsch", t.getArt());
				assertNull("Spezialisierungen falsch", t.getSpezialisierungen());
				assertNull("Voraussetzungen falsch", t.getVoraussetzung());
			}
			sorte.setValue("n/a");

			// Spezialisierung
			String[] spez = { "Lack", "Leder", "Peitschen", "Diener", "Domina",
					"Hinkebein" };
			e = new Element("spezialisierungen");
			xom.appendChild(e);
			for (int i = 0; i < spez.length; i++) {
				Element e2 = new Element("name");
				e2.appendChild(spez[i]);
				e.appendChild(e2);
				t = new Talent(name);
				mappy.map(xom, t);
				assertEquals("# Spez. falsch", i + 1,
						t.getSpezialisierungen().length);
				assertEquals("Spez. falsch", spez[i],
						t.getSpezialisierungen()[i]);
				assertNull("Art falsch", t.getArt());
				assertNull("Sorte falsch", t.getSorte());
				assertNull("Voraussetzungen falsch", t.getVoraussetzung());
			}
			xom.removeChild(e);
			
			// Voraussetzungen
			e = new Element("voraussetzungTalent");
			xom.appendChild(e);
			t = new Talent(name);
			mappy.map(xom, t);
			assertNull("Art falsch", t.getArt());
			assertNull("Sorte falsch", t.getSorte());
			assertNull("Spezialisierungen falsch", t.getSpezialisierungen());
			assertNull("Voraussetzungen falsch", t.getVoraussetzung());
			
			//Attribute ab = new Attribute("abWert", "n/a");
			//e.addAttribute(ab);
			t = new Talent(name);
			mappy.map(xom, t);
			assertNull("Art falsch", t.getArt());
			assertNull("Sorte falsch", t.getSorte());
			assertNull("Spezialisierungen falsch", t.getSpezialisierungen());
			assertNull("Voraussetzungen falsch", t.getVoraussetzung());
			
			//int abW = 10;
			//ab.setValue("" + abW);
			t = new Talent(name);
			mappy.map(xom, t);
			assertNotNull("Voraussetzungen falsch (1)", t.getVoraussetzung());
			assertEquals("Voraussetzungen falsch (2)", 0, t.getVoraussetzung().getVoraussetzungsAltervativen().length);
			assertEquals("Voraussetzungen falsch (3)", 0, t.getVoraussetzung().getNichtErlaubt().getLinks().length);
			assertNull("Art falsch", t.getArt());
			assertNull("Sorte falsch", t.getSorte());
			assertNull("Spezialisierungen falsch", t.getSpezialisierungen());			

		} catch (Exception e) {
			fail("No exception expected!");
		}
	}

	@Ignore("Not implemented yet!") @Test public void testMapToXML() {
		fail("Not implemented yet!");
	}
}