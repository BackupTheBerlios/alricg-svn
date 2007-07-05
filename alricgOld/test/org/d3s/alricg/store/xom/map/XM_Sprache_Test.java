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
import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Sprache;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests für XOMMapper_SchriftSprache
 * 
 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
 */
public class XM_Sprache_Test {

	private XOMStoreObjectMother oma;
	private XOMMapper<CharElement> mappy;


	@Before public void setUp() throws Exception {
		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_Sprache();
		ProgAdmin.messenger = new MessengerMock();
	}

	@Test public void testMapFromXML() {
		final Element xom = new Element("SchriftSpracheBase");
		oma.addSchriftSprachDaten(xom);

		Element e = new Element("nichtMuttersprache");
		xom.appendChild(e);
		Attribute a = new Attribute("kostenKlasse", "A");
		e.addAttribute(a);

		String name = "SPR-Porusski";
		Sprache s = new Sprache(name);		
		mappy.map(xom, s);
		assertEquals("komplexNichtMutterSpr falsch", 0, s.getKomplexNichtMutterSpr());
		assertEquals("kostenNichtMutterSpr falsch", KostenKlasse.A, s.getKostenNichtMutterSpr());
		assertNull("zugehoerigeSchrift falsch", s.getZugehoerigeSchrift());
		
		a = new Attribute("komplexitaet", "n/a");
		e.addAttribute(a);
		s = new Sprache(name);		
		mappy.map(xom, s);
		assertEquals("komplexNichtMutterSpr falsch", 0, s.getKomplexNichtMutterSpr());
		assertEquals("kostenNichtMutterSpr falsch", KostenKlasse.A, s.getKostenNichtMutterSpr());
		assertNull("zugehoerigeSchrift falsch", s.getZugehoerigeSchrift());
		a.setValue("45");
		s = new Sprache(name);		
		mappy.map(xom, s);
		assertEquals("komplexNichtMutterSpr falsch", 45, s.getKomplexNichtMutterSpr());
		assertEquals("kostenNichtMutterSpr falsch", KostenKlasse.A, s.getKostenNichtMutterSpr());
		assertNull("zugehoerigeSchrift falsch", s.getZugehoerigeSchrift());

		e = new Element("schriften");
		xom.appendChild(e);
		s = new Sprache(name);		
		mappy.map(xom, s);
		assertNotNull("zugehoerigeSchrift falsch", s.getZugehoerigeSchrift());
		assertEquals("zugehoerigeSchrift falsch", s, s.getZugehoerigeSchrift().getQuelle());
		assertNotNull("zugehoerigeSchrift falsch", s.getZugehoerigeSchrift().getLinks());
		assertEquals("zugehoerigeSchrift falsch", 0, s.getZugehoerigeSchrift().getLinks().length);
	}

	@Ignore("Not implemented yet!") @Test public void testMapToXML() {
		fail("Not implemented yet!");
	}
}
