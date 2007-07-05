/*
 * Created 22. September 2005 / 00:01:02
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
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
import org.d3s.alricg.charKomponenten.VorNachteil;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests für XOMMapper_Fertigkeit
 * 
 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
 */
public class XM_VorNachteil_Test {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;

	@Before public void setUp() throws Exception {
		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_VorNachteilBase();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));
	}

	@Test public void testMapFromXML() {
		final Element xom = new Element("VorNachteilBase");
		oma.addGPKosten(xom);

		VorNachteil v = new VorNachteilBase();
		mappy.map(xom, v);
		assertEquals("stufenSchritt falsch", 1, v.getStufenSchritt());
		assertEquals("kostenProSchritt falsch", 1, v.getKostenProSchritt());
		assertEquals("minStufe falsch", 1, v.getMinStufe());
		assertEquals("maxStufe falsch", 1, v.getMaxStufe());
		assertNull("aendertApSf falsch", v.getAendertApSf());
		assertNull("aendertGpVorteil falsch", v.getAendertGpVorteil());
		assertNull("aendertGpNachteil", v.getAendertGpNachteil());

		
		Attribute a = new Attribute("kostenProSchritt", "n/a");
		xom.addAttribute(a);
		v = new VorNachteilBase();
		mappy.map(xom, v);
		assertEquals("kostenProSchritt falsch", 1, v.getKostenProSchritt());
		a.setValue("335679");
		v = new VorNachteilBase();
		mappy.map(xom, v);
		assertEquals("kostenProSchritt falsch", 335679, v.getKostenProSchritt());

		a = new Attribute("stufenSchritt", "n/a");
		xom.addAttribute(a);
		v = new VorNachteilBase();
		mappy.map(xom, v);
		assertEquals("stufenSchritt falsch", 1, v.getStufenSchritt());
		a.setValue("335");
		v = new VorNachteilBase();
		mappy.map(xom, v);
		assertEquals("kostenProSchritt falsch", 335, v.getStufenSchritt());

		Element e = new Element("stufenGrenzen");
		xom.appendChild(e);
		v = new VorNachteilBase();
		mappy.map(xom, v);
		assertEquals("minStufe falsch", 1, v.getMinStufe());
		assertEquals("maxStufe falsch", 1, v.getMaxStufe());
		a = new Attribute("minStufe", "n/a");
		e.addAttribute(a);
		v = new VorNachteilBase();
		mappy.map(xom, v);
		assertEquals("minStufe falsch", 1, v.getMinStufe());
		assertEquals("maxStufe falsch", 1, v.getMaxStufe());
		a.setValue("438");
		v = new VorNachteilBase();
		mappy.map(xom, v);
		assertEquals("minStufe falsch", 438, v.getMinStufe());
		assertEquals("maxStufe falsch", 1, v.getMaxStufe());
		a = new Attribute("maxStufe", "n/a");
		e.addAttribute(a);
		v = new VorNachteilBase();
		mappy.map(xom, v);
		assertEquals("minStufe falsch", 438, v.getMinStufe());
		assertEquals("maxStufe falsch", 1, v.getMaxStufe());
		a.setValue("843");
		v = new VorNachteilBase();
		mappy.map(xom, v);
		assertEquals("minStufe falsch", 438, v.getMinStufe());
		assertEquals("maxStufe falsch", 843, v.getMaxStufe());
		
		e = new Element("aendertApSf");
		xom.appendChild(e);
		v = new VorNachteilBase();
		mappy.map(xom, v);
		assertNotNull("aendertApSf falsch", v.getAendertApSf());
		assertEquals("aendertApSf falsch", v, v.getAendertApSf().getQuelle());
		assertNotNull("aendertApSf falsch", v.getAendertApSf().getLinks());
		assertEquals("aendertApSf falsch", 0, v.getAendertApSf().getLinks().length);
		
		e = new Element("aendertGpVorteil");
		xom.appendChild(e);
		v = new VorNachteilBase();
		mappy.map(xom, v);
		assertNotNull("aendertGpVorteil falsch", v.getAendertGpVorteil());
		assertEquals("aendertGpVorteil falsch", v, v.getAendertGpVorteil().getQuelle());
		assertNotNull("aendertGpVorteil falsch", v.getAendertGpVorteil().getLinks());
		assertEquals("aendertGpVorteil falsch", 0, v.getAendertGpVorteil().getLinks().length);
		
		e = new Element("aendertGpNachteil");
		xom.appendChild(e);
		v = new VorNachteilBase();
		mappy.map(xom, v);
		assertNotNull("aendertGpNachteil falsch", v.getAendertGpNachteil());
		assertEquals("aendertGpNachteil falsch", v, v.getAendertGpNachteil().getQuelle());
		assertNotNull("aendertGpNachteil falsch", v.getAendertGpNachteil().getLinks());
		assertEquals("aendertGpNachteil falsch", 0, v.getAendertGpNachteil().getLinks().length);

	}

	@Ignore("Not implemented yet!") @Test public void testMapToXML() {
		fail("Not implemented yet!");
	}

	/**
	 * Konkrete "do-nothing-special" Impl. der abtrakten Superklasse
	 * XOMMapper_VorNachteil
	 * 
	 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
	 */
	private class XOMMapper_VorNachteilBase extends XOMMapper_VorNachteil {
		public void map(Element xmlElement, CharElement charElement) {
			super.map(xmlElement, charElement);
		}

		public void map(CharElement charElement, Element xmlElement) {
			super.map(charElement, xmlElement);
		}
	}

	/**
	 * Konkrete "do-nothing-special" Impl. der abtrakten Superklasse VorNachteil
	 * 
	 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
	 */
	private class VorNachteilBase extends VorNachteil {
		public CharKomponente getCharKomponente() {
			return null;
		}
	}
}