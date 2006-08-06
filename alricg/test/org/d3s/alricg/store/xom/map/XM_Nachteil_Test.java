/*
 * Created 22. September 2005 / 00:01:02
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.store.xom.map;

import java.io.File;

import junit.framework.TestCase;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Nachteil;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;

/**
 * Tests für XOMMapper_Nachteil
 * 
 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
 */
public class XM_Nachteil_Test extends TestCase {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;

	public XM_Nachteil_Test(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_Nachteil();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));
	}

	public void testMapFromXML() {
		final Element xom = new Element("NachteilBase");
		oma.addGPKosten(xom);

		String name = "NAC-NACKT";
		Nachteil n = new Nachteil(name);
		mappy.map(xom, n);
		assertFalse("Schlechte Eigenschaft", n.isSchlechteEigen());
		
		String[] bools = {"true", "false", "TRUE", "FALSE"};
		Element e = new Element("istSchlechteEigen");
		xom.appendChild(e);
		for (int i = 0; i < bools.length; i++) {
			e.appendChild(bools[i]);
			n = new Nachteil(name);
			mappy.map(xom, n);
			assertEquals("Schlechte Eigenschaft falsch", i % 2 == 0, n.isSchlechteEigen());
			e.removeChild(0);
		}
	}

	public void testMapToXML() {
		fail("Not implemented yet!");
	}
}