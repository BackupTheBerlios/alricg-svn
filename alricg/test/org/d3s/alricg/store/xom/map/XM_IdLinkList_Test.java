/*
 * Created on 26.10.2005 / 17:14:55
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
import org.d3s.alricg.charKomponenten.Gabe;
import org.d3s.alricg.charKomponenten.Rasse;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.Vorteil;
import org.d3s.alricg.charKomponenten.Zauber;
import org.d3s.alricg.charKomponenten.links.IdLinkList;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.xom.XOMStoreObjectMother;

public class XM_IdLinkList_Test extends TestCase {

	private XOMStoreObjectMother oma;
	private XOMMapper<IdLinkList> mappy;

	protected void setUp() throws Exception {
		super.setUp();
		oma = new XOMStoreObjectMother();
		mappy = new XOMMapper_IdLinkList();
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));
	}

	public void testMapFromXML() {

		Element e = new Element("idlinklist");

		String[] ids = {"TAL-AAA", "TAL-BBB","ZAU-CCC", "RAS-DDD", "VOR-EEE", "GAB-FFF" };
		CharElement[] ziele = { new Talent(ids[0]), new Talent(ids[1]),
				new Zauber(ids[2]), new Rasse(ids[3]), new Vorteil(ids[4]), new Gabe(ids[5]) };
		oma.add(CharKomponente.talent, ids[0], ziele[0]);
		oma.add(CharKomponente.talent, ids[1], ziele[1]);
		oma.add(CharKomponente.zauber, ids[2], ziele[2]);
		oma.add(CharKomponente.rasse, ids[3], ziele[3]);
		oma.add(CharKomponente.vorteil, ids[4], ziele[4]);
		oma.add(CharKomponente.gabe, ids[5], ziele[5]);

		Rasse q = new Rasse("RAS-TUL");
		IdLinkList idLinkList = new IdLinkList(q);
		mappy.map(e, idLinkList);
		assertNotNull("Links falsch", idLinkList.getLinks());
		assertEquals("Links falsch", 0, idLinkList.getLinks().length);
		assertEquals("Quelle falsch", q, idLinkList.getQuelle());

		String idss = ids[0] + " " + ids[1] + " " + ids[2];
		e.addAttribute(new Attribute("ids", idss));
		idLinkList = new IdLinkList(q);
		mappy.map(e, idLinkList);
		assertNotNull("Links falsch", idLinkList.getLinks());
		assertEquals("Links falsch", 3, idLinkList.getLinks().length);
		assertEquals("Quelle falsch", q, idLinkList.getQuelle());
		for (int i = 0; i < 3; i++) {
			assertEquals("IdLink-Quelle falsch", q, idLinkList.getLinks()[i]
					.getQuellElement());
			assertEquals("IdLink-Ziel falsch", ziele[i],
					idLinkList.getLinks()[i].getZiel());
		}
		
		for(int i = 0; i < 3; i++) {
			Element e2 = new Element("linkId");
			e2.addAttribute(new Attribute("id", ids[i + 3]));
			e.appendChild(e2);
			idLinkList = new IdLinkList(q);
			mappy.map(e, idLinkList);
			assertNotNull("Links falsch", idLinkList.getLinks());
			assertEquals("Links falsch", i + 4, idLinkList.getLinks().length);
			assertEquals("Quelle falsch", q, idLinkList.getQuelle());
			for (int j = 0; j < i + 4; j++) {
				assertEquals("IdLink-Quelle falsch", q, idLinkList.getLinks()[j].getQuellElement());
				assertEquals("IdLink-Ziel falsch", ziele[j], idLinkList.getLinks()[j].getZiel());
			}
		}
	}
}
