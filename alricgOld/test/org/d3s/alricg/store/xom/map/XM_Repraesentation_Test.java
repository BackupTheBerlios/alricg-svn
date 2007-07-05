/*
 * Created on 11.10.2005 / 13:36:19
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Repraesentation;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class XM_Repraesentation_Test {

	private XOMMapper<CharElement> mappy;

	@Before public void setUp() throws Exception {
		mappy = new XOMMapper_Repraesentation();
	}

	@Test public void testMapFromXML() {
		try {
			final Element xom = new Element("repraesentation");
			final String name = "REP-Antimagie";

			// abkuerzung
			String abkuerzung = "abk.abk.abk.";
			xom.addAttribute(new Attribute("abkuerzung", abkuerzung));

			Repraesentation r = new Repraesentation(name);
			mappy.map(xom, r);
			assertEquals("Abkürzung falsch", abkuerzung, r.getAbkuerzung());
			assertEquals("Echte Rep. falsch", true, r.isEchteRep());

			String[] echt = new String[] { "true", "false", "TRUE", "FALSE",
					"TrUe", "FaLsE", "tRuE", "fAlSe" };
			Element echteR = new Element("isEchteRep");
			xom.appendChild(echteR);

			for (int i = 0; i < echt.length; i++) {
				echteR.appendChild(echt[i]);
				r = new Repraesentation(name);
				mappy.map(xom, r);
				assertEquals("Abkürzung falsch", abkuerzung, r.getAbkuerzung());
				assertEquals("Echte Rep. falsch", i % 2 == 0, r.isEchteRep());
				echteR.removeChild(0);
			}
		} catch (Exception e) {
			fail("No exception expected!");
		}
	}

	@Ignore("Not implemented yet!") @Test public void testMapToXML() {
		fail("Not implemented yet!");
	}

}
