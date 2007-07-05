/*
 * Created 22. September 2005 / 00:01:02
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.store.xom.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Gottheit;
import org.d3s.alricg.charKomponenten.Ritus;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests für XOMMapper_Ritus
 * 
 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
 */
public class XM_Ritus_Test {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;


	@Before public void setUp() throws Exception {

		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_RitusBase();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));
	}

	@Test public void testMapFromXML() {
		final Element xom = new Element("RitusBase");

		// grad: int
		int grad = 1;
		Element element = new Element("grad");
		element.appendChild("" + grad);
		xom.appendChild(element);

		// additionsid: String
		String aID = "sgdffd";
		element = new Element("additionsId");
		element.appendChild(aID);
		xom.appendChild(element);

		// Gottheiten: Gottheit[]
		String[] gKeys = new String[] { "GOT-PER", "GOT-BOR", "GOT-RAH" };
		Gottheit[] gott = new Gottheit[] { new Gottheit(gKeys[0]),
				new Gottheit(gKeys[1]), new Gottheit(gKeys[2]) };

		Ritus r = new RitusBase();
		for (int i = 0; i < gKeys.length; i++) {
			oma.add(CharKomponente.gottheit, gKeys[i], gott[i]);

			element = new Element("gottheit");
			element.appendChild(gKeys[i]);
			xom.appendChild(element);

			r = new RitusBase();
			mappy.map(xom, r);

			assertEquals("Grad falsch", grad, r.getGrad());
			assertEquals("AdditionsId", aID, r.getAdditionsId());
			assertEquals("Anzahl Götter falsch", i + 1, r.getGottheit().length);
			assertEquals("Gottheit falsch ", gott[i], r.getGottheit()[i]);
		}
	}

	@Ignore("Not implemented yet!") @Test public void testMapToXML() {
		fail("Not implemented yet!");
	}

	/**
	 * Konkrete "do-nothing-special" Impl. der abtrakten Superklasse
	 * XOMMapper_Ritus
	 * 
	 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
	 */
	private class XOMMapper_RitusBase extends XOMMapper_Ritus {
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
	private class RitusBase extends Ritus {
		public CharKomponente getCharKomponente() {
			return null;
		}
	}
}
