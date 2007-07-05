/*
 * Created on 11.10.2005 / 13:36:19
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom.map;

import static org.junit.Assert.*;

import java.io.File;

import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Gabe;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class XM_Gabe_Test {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;


	@Before public void setUp() throws Exception {
		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_Gabe();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));
	}

	@Test public void testMapFromXML() {
		try {
			final Element xom = new Element("gabe");
			oma.addProbe(xom);
			oma.addKostenKlasse(xom);

			final String name = "GAB-Durchblick";

			Gabe g = new Gabe(name);
			mappy.map(xom, g);
			assertEquals("MinStufe falsch", CharElement.KEIN_WERT, g
					.getMinStufe());
			assertEquals("Max Stufe falsch", CharElement.KEIN_WERT, g
					.getMaxStufe());

			// minstufe int
			// maxstufe int
			int minstufe = 3;
			int maxstufe = 13;
			final Element stufengrenzen = new Element("stufenGrenzen");
			stufengrenzen
					.addAttribute(new Attribute("minStufe", "" + minstufe));
			stufengrenzen
					.addAttribute(new Attribute("maxStufe", "" + maxstufe));
			xom.appendChild(stufengrenzen);

			g = new Gabe(name);
			mappy.map(xom, g);
			assertEquals("MinStufe falsch", minstufe, g.getMinStufe());
			assertEquals("Max Stufe falsch", maxstufe, g.getMaxStufe());
		} catch (Exception e) {
			fail("No exception expected!");
		}
	}

	@Ignore("Not implemented yet!") @Test public void testMapToXML() {
		fail("Not implemented yet!");
	}

}
