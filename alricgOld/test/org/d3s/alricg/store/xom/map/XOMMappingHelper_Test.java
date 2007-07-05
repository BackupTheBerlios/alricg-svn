/*
 * Created on 26.10.2005 / 16:12:59
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom.map;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.junit.Before;
import org.junit.Test;

public class XOMMappingHelper_Test {

	@Before public void setUp() throws Exception {
		ProgAdmin.messenger = new MessengerMock();
		FactoryFinder.init(new File(
				"test/org/d3s/alricg/store/factory.properties"));
	}

	@Test public void testChooseXOMMapper() {

		XOMMapper< ? > mappy = XOMMappingHelper.instance().chooseXOMMapper(
				CharKomponente.ausruestung);
		assertTrue("Falsche Mapper-Instanz",
				mappy instanceof XOMMapper_Ausruestung);

		assertTrue(
				"Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.daemonenPakt) instanceof XOMMapper_DaemonenPakt);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.fahrzeug) instanceof XOMMapper_Fahrzeug);

		assertTrue("Falsche Mapper-Instanz", XOMMappingHelper.instance()
				.chooseXOMMapper(CharKomponente.gabe) instanceof XOMMapper_Gabe);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.gottheit) instanceof XOMMapper_Gottheit);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.kultur) instanceof XOMMapper_Kultur);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.liturgie) instanceof XOMMapper_Liturgie);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.nachteil) instanceof XOMMapper_Nachteil);

		assertTrue(
				"Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.profession) instanceof XOMMapper_Profession);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.rasse) instanceof XOMMapper_Rasse);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.region) instanceof XOMMapper_RegionVolk);

		assertTrue(
				"Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.repraesentation) instanceof XOMMapper_Repraesentation);

		assertTrue(
				"Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.ritLitKenntnis) instanceof XOMMapper_LiturgieRitualKenntnis);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.ritual) instanceof XOMMapper_Ritual);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.ruestung) instanceof XOMMapper_Ruestung);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.schild) instanceof XOMMapper_Schild);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.schrift) instanceof XOMMapper_Schrift);

		assertTrue(
				"Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.schwarzeGabe) instanceof XOMMapper_SchwarzeGabe);

		assertTrue(
				"Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.sonderfertigkeit) instanceof XOMMapper_Sonderfertigkeit);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.sprache) instanceof XOMMapper_Sprache);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.talent) instanceof XOMMapper_Talent);

		assertTrue("Falsche Mapper-Instanz", XOMMappingHelper.instance()
				.chooseXOMMapper(CharKomponente.tier) instanceof XOMMapper_Tier);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.vorteil) instanceof XOMMapper_Vorteil);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.waffeFk) instanceof XOMMapper_FkWaffe);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.waffeNk) instanceof XOMMapper_NahkWaffe);

		assertTrue("Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.zauber) instanceof XOMMapper_Zauber);
		assertTrue(
				"Falsche Mapper-Instanz",
				XOMMappingHelper.instance().chooseXOMMapper(
						CharKomponente.zusatzProfession) instanceof XOMMapper_ZusatzProfession);

		assertNull("Falsche Mapper-Instanz", XOMMappingHelper.instance()
				.chooseXOMMapper(CharKomponente.eigenschaft));

		assertNull("Falsche Mapper-Instanz", XOMMappingHelper.instance()
				.chooseXOMMapper(CharKomponente.sonderregel));

	}
}
