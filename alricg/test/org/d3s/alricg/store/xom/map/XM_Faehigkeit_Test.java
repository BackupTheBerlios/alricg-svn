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
import org.d3s.alricg.charKomponenten.EigenschaftEnum;
import org.d3s.alricg.charKomponenten.Faehigkeit;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests für XOMMapper_Faehigkeit
 * 
 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
 */
public class XM_Faehigkeit_Test {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;

	@Before public void setUp() throws Exception {
		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_FaehigkeitBase();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));
	}

	@Test public void testMapFromXML() {
		final Element xom = new Element("FaehigkeitBase");
		oma.addProbe(xom);
		oma.addKostenKlasse(xom);

		Faehigkeit f = new FaehigkeitBase();
		mappy.map(xom, f);

		assertEquals("Eigenschaft 1 falsch", EigenschaftEnum.MU, f
				.get3Eigenschaften()[0].getEigenschaftEnum());
		assertEquals("Eigenschaft 2 falsch", EigenschaftEnum.FF, f
				.get3Eigenschaften()[1].getEigenschaftEnum());
		assertEquals("Eigenschaft 3 falsch", EigenschaftEnum.KK, f
				.get3Eigenschaften()[2].getEigenschaftEnum());
		assertEquals("KostenKlasse falsch", KostenKlasse.A, f.getKostenKlasse());
		assertEquals("KostenKlasse falsch", "A", f.getKostenKlasse().getValue());
	}

	@Ignore("Not implemented yet!") @Test public void testMapToXML() {
		fail("Not implemented yet!");
	}

	/**
	 * Konkrete "do-nothing-special" Impl. der abtrakten Superklasse
	 * XOMMapper_Faehigkeit
	 * 
	 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
	 */
	private class XOMMapper_FaehigkeitBase extends XOMMapper_Faehigkeit {
		public void map(Element xmlElement, CharElement charElement) {
			super.map(xmlElement, charElement);
		}

		public void map(CharElement charElement, Element xmlElement) {
			super.map(charElement, xmlElement);
		}
	}

	/**
	 * Konkrete "do-nothing-special" Impl. der abtrakten Superklasse Faehigkeit
	 * 
	 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
	 */
	private class FaehigkeitBase extends Faehigkeit {
		public CharKomponente getCharKomponente() {
			return null;
		}
	}
}
