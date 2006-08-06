/*
 * Created on 11.10.2005 / 13:36:19
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom.map;

import junit.framework.TestCase;
import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.RegionVolk;

public class XM_RegionVolk_Test extends TestCase {

	private XOMMapper<CharElement> mappy;

	public XM_RegionVolk_Test(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mappy = new XOMMapper_RegionVolk();
	}

	public void testMapFromXML() {
		try {
			final Element xom = new Element("region");
			final String name = "REG-MHA";
			final String[] elementBase = { "A", "B", "C", "D", "e", "f", "g",
					"hahaha" };
			final String[] zusatz = { "vorMann", "vorFrau", "nach", "nachEnd" };

			RegionVolk r = new RegionVolk(name);
			mappy.map(xom, r);
			assertEquals("Bindewort Mann falsch", null, r.getBindeWortMann());
			assertEquals("Bindewort Frau falsch", null, r.getBindeWortFrau());
			assertEquals("Vornamen Mann falsch", null, r.getVornamenMann());
			assertEquals("Vornamen Frau falsch", null, r.getVornamenFrau());
			assertEquals("Nachnamen falsch", null, r.getNachnamen());
			assertEquals("Nachnamen Endungen falsch", null, r
					.getNachnamenEndung());

			// bindewort
			String bm = "bm";
			String bf = "bf";
			Element e = new Element("bindeWort");
			e.addAttribute(new Attribute("mann", bm));
			e.addAttribute(new Attribute("frau", bf));
			xom.appendChild(e);

			r = new RegionVolk(name);
			mappy.map(xom, r);
			assertEquals("Bindewort Mann falsch", bm, r.getBindeWortMann());
			assertEquals("Bindewort Frau falsch", bf, r.getBindeWortFrau());
			assertEquals("Vornamen Mann falsch", null, r.getVornamenMann());
			assertEquals("Vornamen Frau falsch", null, r.getVornamenFrau());
			assertEquals("Nachnamen falsch", null, r.getNachnamen());
			assertEquals("Nachnamen Endungen falsch", null, r
					.getNachnamenEndung());

			// vornamen mann
			e = new Element("vornamenMann");
			xom.appendChild(e);
			for (int i = 0; i < elementBase.length; i++) {
				Element sub = new Element("name");
				sub.appendChild(elementBase[i] + "_" + zusatz[0]);
				e.appendChild(sub);
			}
			r = new RegionVolk(name);
			mappy.map(xom, r);
			assertEquals("Bindewort Mann falsch", bm, r.getBindeWortMann());
			assertEquals("Bindewort Frau falsch", bf, r.getBindeWortFrau());
			assertEquals("Vornamen Frau falsch", null, r.getVornamenFrau());
			assertEquals("Nachnamen falsch", null, r.getNachnamen());
			assertEquals("Nachnamen Endungen falsch", null, r
					.getNachnamenEndung());
			assertEquals("Vornamen Mann falsch", elementBase.length, r
					.getVornamenMann().length);
			for (int i = 0; i < elementBase.length; i++) {
				assertEquals("Vorname Mann falsch", elementBase[i] + "_"
						+ zusatz[0], r.getVornamenMann()[i]);
			}

			// vornamen frau
			e = new Element("vornamenFrau");
			xom.appendChild(e);
			for (int i = 0; i < elementBase.length; i++) {
				Element sub = new Element("name");
				sub.appendChild(elementBase[i] + "_" + zusatz[1]);
				e.appendChild(sub);
			}
			r = new RegionVolk(name);
			mappy.map(xom, r);
			assertEquals("Bindewort Mann falsch", bm, r.getBindeWortMann());
			assertEquals("Bindewort Frau falsch", bf, r.getBindeWortFrau());
			assertEquals("Nachnamen falsch", null, r.getNachnamen());
			assertEquals("Nachnamen Endungen falsch", null, r
					.getNachnamenEndung());
			assertEquals("Vornamen Mann falsch", elementBase.length, r
					.getVornamenMann().length);
			assertEquals("Vornamen Frau falsch", elementBase.length, r
					.getVornamenFrau().length);

			for (int i = 0; i < elementBase.length; i++) {
				assertEquals("Vorname Mann falsch", elementBase[i] + "_"
						+ zusatz[0], r.getVornamenMann()[i]);
				assertEquals("Vorname Frau falsch", elementBase[i] + "_"
						+ zusatz[1], r.getVornamenFrau()[i]);
			}

			// nachnamen
			e = new Element("nachnamen");
			xom.appendChild(e);
			for (int i = 0; i < elementBase.length; i++) {
				Element sub = new Element("name");
				sub.appendChild(elementBase[i] + "_" + zusatz[2]);
				e.appendChild(sub);
			}
			r = new RegionVolk(name);
			mappy.map(xom, r);
			assertEquals("Bindewort Mann falsch", bm, r.getBindeWortMann());
			assertEquals("Bindewort Frau falsch", bf, r.getBindeWortFrau());
			assertEquals("Nachnamen Endungen falsch", null, r
					.getNachnamenEndung());
			assertEquals("Vornamen Mann falsch", elementBase.length, r
					.getVornamenMann().length);
			assertEquals("Vornamen Frau falsch", elementBase.length, r
					.getVornamenFrau().length);
			assertEquals("Nachnamen falsch", elementBase.length, r
					.getNachnamen().length);

			for (int i = 0; i < elementBase.length; i++) {
				assertEquals("Vorname Mann falsch", elementBase[i] + "_"
						+ zusatz[0], r.getVornamenMann()[i]);
				assertEquals("Vorname Frau falsch", elementBase[i] + "_"
						+ zusatz[1], r.getVornamenFrau()[i]);
				assertEquals("Nachname falsch", elementBase[i] + "_"
						+ zusatz[2], r.getNachnamen()[i]);
			}

			// nachnamen-Endungen
			e = new Element("nachnamenEndung");
			xom.appendChild(e);
			for (int i = 0; i < elementBase.length; i++) {
				Element sub = new Element("endung");
				sub.appendChild(elementBase[i] + "_" + zusatz[3]);
				e.appendChild(sub);
			}
			r = new RegionVolk(name);
			mappy.map(xom, r);
			assertEquals("Bindewort Mann falsch", bm, r.getBindeWortMann());
			assertEquals("Bindewort Frau falsch", bf, r.getBindeWortFrau());
			assertEquals("Vornamen Mann falsch", elementBase.length, r
					.getVornamenMann().length);
			assertEquals("Vornamen Frau falsch", elementBase.length, r
					.getVornamenFrau().length);
			assertEquals("Nachnamen falsch", elementBase.length, r
					.getNachnamen().length);
			assertEquals("Nachnamen Endungen falsch", elementBase.length, r
					.getNachnamenEndung().length);

			for (int i = 0; i < elementBase.length; i++) {
				assertEquals("Vorname Mann falsch", elementBase[i] + "_"
						+ zusatz[0], r.getVornamenMann()[i]);
				assertEquals("Vorname Frau falsch", elementBase[i] + "_"
						+ zusatz[1], r.getVornamenFrau()[i]);
				assertEquals("Nachname falsch", elementBase[i] + "_"
						+ zusatz[2], r.getNachnamen()[i]);
				assertEquals("Nachnamen Endungen  falsch", elementBase[i] + "_"
						+ zusatz[3], r.getNachnamenEndung()[i]);
			}
		} catch (Exception e) {
			fail("No exception expected!");
		}
	}

	public void testMapToXML() {
		fail("Not implemented yet!");
	}

}
