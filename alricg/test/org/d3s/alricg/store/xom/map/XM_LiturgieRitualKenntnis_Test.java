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
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.LiturgieRitualKenntnis;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;

public class XM_LiturgieRitualKenntnis_Test extends TestCase {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;

	public XM_LiturgieRitualKenntnis_Test(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_LiturgieRitualKenntnis();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));

	}

	public void testMapFromXML() {
		try {
			final Element xom = new Element("liRiKenntnis");
			oma.addProbe(xom);
			oma.addKostenKlasse(xom);
			
			final String name = "LRK-SadoMaso";

			LiturgieRitualKenntnis liri = new LiturgieRitualKenntnis(name);

			String[] kenntnis = new String[] { "true", "false", "TRUE",
					"FALSE", "TrUe", "FaLsE", "tRuE", "fAlSe" };
			Element kenntnisE = new Element("istLiturgieKenntnis");
			xom.appendChild(kenntnisE);

			for (int i = 0; i < kenntnis.length; i++) {
				kenntnisE.appendChild(kenntnis[i]);
				liri = new LiturgieRitualKenntnis(name);
				mappy.map(xom, liri);
				assertEquals("liturgie kenntnis falsch", i % 2 == 0, liri
						.isLiturgieKenntnis());
				kenntnisE.removeChild(0);
			}
		} catch (Exception e) {
			fail("No exception expected!");
		}
	}

	public void testMapToXML() {
		fail("Not implemented yet!");
	}
}