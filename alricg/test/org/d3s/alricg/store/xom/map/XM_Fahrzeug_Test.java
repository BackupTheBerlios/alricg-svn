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
import org.d3s.alricg.charKomponenten.charZusatz.Fahrzeug;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;

public class XM_Fahrzeug_Test extends TestCase {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;

	public XM_Fahrzeug_Test(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();

		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_Fahrzeug();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));

	}

	public void testMapFromXML() {
		final Element xom = new Element("fahrzeug");
		oma.addErhaeltlichBei(xom, "REG-BAY");
		oma.add(CharKomponente.region, "REG-BAY", new RegionVolk("REG-BAY"));

		String name = "FAH-X";
		Fahrzeug f = new Fahrzeug(name);
		mappy.map(xom, f);
		assertNull("Aussehen falsch", f.getAussehen());
		assertNull("Typ falsch", f.getTyp());

		// Aussehen
		Element e = new Element("aussehen");
		xom.appendChild(e);
		String[] vals = new String[] { "A1", "b2", "C$%!@" };
		for (int i = 0; i < vals.length; i++) {
			e.appendChild(vals[i]);
			f = new Fahrzeug(name);
			mappy.map(xom, f);
			assertEquals("Aussehen falsch", vals[i], f.getAussehen());
			assertNull("Typ falsch", f.getTyp());
			e.removeChild(0);
		}
		xom.removeChild(e);

		// Typ
		Attribute a = new Attribute("fahrzeugArt", "n/a");
		xom.addAttribute(a);
		for (int i = 0; i < vals.length; i++) {
			a.setValue(vals[i]);
			f = new Fahrzeug(name);
			mappy.map(xom, f);
			assertNull("Aussehen falsch", f.getAussehen());
			assertEquals("Typ falsch", vals[i], f.getTyp());
		}
	}

	public void testMapToXML() {
		fail("Not implemented yet!");
	}
}
