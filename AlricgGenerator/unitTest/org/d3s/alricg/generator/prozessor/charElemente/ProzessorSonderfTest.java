/*
 * Created 12.07.2008
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.prozessor.charElemente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.charakter.SonderregelAdmin;
import org.d3s.alricg.common.charakter.VerbilligteFertigkeitAdmin;
import org.d3s.alricg.common.logic.Prozessor;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.GeneratorMagieStatusAdmin;
import org.d3s.alricg.generator.prozessor.ProzessorDecorator;
import org.d3s.alricg.generator.prozessor.VoraussetzungenGeneratorAdmin;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorSonderfertigkeit;
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.Fertigkeit.AdditionsFamilie;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.held.CharakterDaten;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Vincent
 */
public class ProzessorSonderfTest {
	private Rasse rasse;
	private Charakter charakter;
	private ProzessorDecorator<Sonderfertigkeit, GeneratorLink> prozessorSonderf;
	private ExtendedProzessorSonderfertigkeit extendedProzessor;
	private ProzessorDecorator<Talent, GeneratorLink> prozessorTalent;
	//private ProzessorDecorator<Eigenschaft, GeneratorLink> prozessorEigenschaft;
	
	@BeforeClass public static void startTestClass() {
		// Lade Test-Daten
		String dir = System.getProperty("user.dir") 
					+ File.separator + "unitTest" 
					+ File.separator + "TestFiles";
		StoreAccessor.getInstance().setPaths(
				dir + File.separator + "org", 
				dir + File.separator + "user",
				dir + File.separator + "chars",
				dir + File.separator + "rulesConfig.xml");
		try {
			StoreAccessor.getInstance().loadFiles();
			StoreAccessor.getInstance().loadRegelConfig();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@Before public void setUp() throws Exception {
		// Charakter erzeugen
		charakter = new Charakter(new CharakterDaten());
		charakter.initCharakterAdmins(
				new SonderregelAdmin(charakter),
				new VerbilligteFertigkeitAdmin(charakter),
				new VoraussetzungenGeneratorAdmin(charakter),
				new GeneratorMagieStatusAdmin(charakter));
		
		// Alle Prozessoren erzeugen
		HashMap<Class, Prozessor> hash = new HashMap<Class, Prozessor>();
		charakter.setProzessorHash(hash);
		hash.put(
				Eigenschaft.class,
				new ProzessorDecorator(charakter, new ProzessorEigenschaften(charakter)));
		hash.put(
				Sonderfertigkeit.class,
				new ProzessorDecorator(charakter, new ProzessorSonderfertigkeit(charakter)));
		hash.put(
				Talent.class,
				new ProzessorDecorator(charakter, new ProzessorTalent(charakter)));

		//prozessorEigenschaft = (ProzessorDecorator) charakter.getProzessor(Eigenschaft.class);
		prozessorSonderf = (ProzessorDecorator) charakter.getProzessor(Sonderfertigkeit.class);
		prozessorTalent = (ProzessorDecorator) charakter.getProzessor(Talent.class);
		extendedProzessor = (ExtendedProzessorSonderfertigkeit) prozessorSonderf.getExtendedInterface();
			
		// Rassen erzeugen
		rasse = new Rasse();
		rasse.setId("RAS-test");
	}

	@Test
	public void testAddRemoveKostenNormal() {
		assertFalse(extendedProzessor.getMitApBezahlt());
		
		// Test 1: Standard GP
		Sonderfertigkeit sf1 = new Sonderfertigkeit();
		sf1.setId("SFK-Standart1");
		sf1.setGpKosten(5.0);
		
		GeneratorLink link = prozessorSonderf.addNewElement(sf1);
		assertEquals(5, link.getKosten());
		assertEquals(5, prozessorSonderf.getGesamtKosten());
		assertEquals(5, extendedProzessor.getGpGesamtKosten());
		assertEquals(0, extendedProzessor.getApGesamtKosten());
		
		// Test 2: Standard GP
		Sonderfertigkeit sf2 = new Sonderfertigkeit();
		sf2.setId("SFK-Standart2");
		sf2.setGpKosten(3.0);
		
		link = prozessorSonderf.addNewElement(sf2);
		assertEquals(3, link.getKosten());
		assertEquals(8, prozessorSonderf.getGesamtKosten());
		assertEquals(8, extendedProzessor.getGpGesamtKosten());
		assertEquals(0, extendedProzessor.getApGesamtKosten());
		
		// Test 3: Standard AP
		extendedProzessor.setMitApBezahlt(true); // <-- Ab jetzt mit AP bezahlen
		Sonderfertigkeit sf3 = new Sonderfertigkeit();
		sf3.setId("SFK-Standart3");
		sf3.setGpKosten(2.0);
		
		link = prozessorSonderf.addNewElement(sf3);
		assertEquals(100, link.getKosten());
		assertEquals(10, prozessorSonderf.getGesamtKosten());
		assertEquals(8, extendedProzessor.getGpGesamtKosten());
		assertEquals(100, extendedProzessor.getApGesamtKosten());
		
		// Test 4: Standard AP
		Sonderfertigkeit sf4 = new Sonderfertigkeit();
		sf4.setId("SFK-Standart4");
		sf4.setGpKosten(3.5);
		
		link = prozessorSonderf.addNewElement(sf4);
		assertEquals(175, link.getKosten());
		assertEquals(13.5, prozessorSonderf.getGesamtKosten());
		assertEquals(8, extendedProzessor.getGpGesamtKosten());
		assertEquals(275, extendedProzessor.getApGesamtKosten());
		
		// Test 5: Bezahlung wechseln
		extendedProzessor.changeMitAPBezahlt(link, false);
		
		assertEquals(3.5, link.getKosten());
		assertEquals(13.5, prozessorSonderf.getGesamtKosten());
		assertEquals(11.5, extendedProzessor.getGpGesamtKosten());
		assertEquals(100, extendedProzessor.getApGesamtKosten());
		
		// Test 6: Bezahlung wechseln
		extendedProzessor.changeMitAPBezahlt(link, true);
		
		assertEquals(175, link.getKosten());
		assertEquals(13.5, prozessorSonderf.getGesamtKosten());
		assertEquals(8, extendedProzessor.getGpGesamtKosten());
		assertEquals(275, extendedProzessor.getApGesamtKosten());
		
		// Test 7: link entfernen
		link = prozessorSonderf.getElementBox().getObjectById(sf1);
		
		assertTrue(prozessorSonderf.containsLink(link));
		prozessorSonderf.removeElement(link);
		assertFalse(prozessorSonderf.containsLink(link));
		assertEquals(8.5, prozessorSonderf.getGesamtKosten());
		assertEquals(3, extendedProzessor.getGpGesamtKosten());
		assertEquals(275, extendedProzessor.getApGesamtKosten());
		
		// Test 8: link entfernen
		link = prozessorSonderf.getElementBox().getObjectById(sf3);
		
		assertTrue(prozessorSonderf.containsLink(link));
		prozessorSonderf.removeElement(link);
		assertFalse(prozessorSonderf.containsLink(link));
		assertEquals(6.5, prozessorSonderf.getGesamtKosten());
		assertEquals(3, extendedProzessor.getGpGesamtKosten());
		assertEquals(175, extendedProzessor.getApGesamtKosten());
		
		// Test 8: Alle restlichen entfernen
		link = prozessorSonderf.getElementBox().getObjectById(sf2);
		prozessorSonderf.removeElement(link);
		link = prozessorSonderf.getElementBox().getObjectById(sf4);
		prozessorSonderf.removeElement(link);
		
		assertEquals(0, prozessorSonderf.getGesamtKosten());
		assertEquals(0, extendedProzessor.getGpGesamtKosten());
		assertEquals(0, extendedProzessor.getApGesamtKosten());
		
		// Test 9: Standard GP
		link = prozessorSonderf.addNewElement(sf1);
		assertEquals(250, link.getKosten());
		extendedProzessor.setMitApBezahlt(false);
		link = prozessorSonderf.addNewElement(sf3);
		assertEquals(2, link.getKosten());
		
		assertEquals(7, prozessorSonderf.getGesamtKosten());
		assertEquals(2, extendedProzessor.getGpGesamtKosten());
		assertEquals(250, extendedProzessor.getApGesamtKosten());
		
	}
	
	@Test
	public void testAddRemoveKostenModi() {
		// Test 1: Standard GP
		Sonderfertigkeit sf1 = new Sonderfertigkeit();
		sf1.setId("SFK-Standart1");
		sf1.setGpKosten(5.0);
		
		GeneratorLink link = prozessorSonderf.addNewElement(sf1);
		assertEquals(5, link.getKosten());
		assertEquals(5, prozessorSonderf.getGesamtKosten());
		assertEquals(5, extendedProzessor.getGpGesamtKosten());
		assertEquals(0, extendedProzessor.getApGesamtKosten());
		
		// Test 2: Modi hinzufügen
		IdLink linkModi1 = new IdLink(rasse, sf1, null, Link.KEIN_WERT, null);
		
		link = (GeneratorLink) prozessorSonderf.addModi(linkModi1);
		assertEquals(0, link.getKosten());
		assertEquals(0, prozessorSonderf.getGesamtKosten());
		assertEquals(0, extendedProzessor.getGpGesamtKosten());
		assertEquals(0, extendedProzessor.getApGesamtKosten());
		
		// Test 3: Modi entfernen
		prozessorSonderf.removeModi(link, linkModi1);
		
		assertEquals(5, link.getKosten());
		assertEquals(5, prozessorSonderf.getGesamtKosten());
		assertEquals(5, extendedProzessor.getGpGesamtKosten());
		assertEquals(0, extendedProzessor.getApGesamtKosten());
		
		// Test 4: Mehrfach Modi
		extendedProzessor.setMitApBezahlt(true); // Sollte keine Auswirkung haben
		linkModi1 = new IdLink(rasse, sf1, null, Link.KEIN_WERT, null);
		prozessorSonderf.addModi(linkModi1);
		linkModi1 = new IdLink(rasse, sf1, null, Link.KEIN_WERT, null);
		link = (GeneratorLink) prozessorSonderf.addModi(linkModi1);
		
		assertEquals(-5, link.getKosten());
		assertEquals(-5, prozessorSonderf.getGesamtKosten());
		assertEquals(-5, extendedProzessor.getGpGesamtKosten());
		assertEquals(0, extendedProzessor.getApGesamtKosten());
		
		// Test 5: Link einfach entfernen
		prozessorSonderf.removeElement(link);
		
		assertFalse(prozessorSonderf.getElementBox().contains(link));
		assertEquals(0, prozessorSonderf.getGesamtKosten());
		assertEquals(0, extendedProzessor.getGpGesamtKosten());
		assertEquals(0, extendedProzessor.getApGesamtKosten());
	}
	
	@Test
	public void testCanX() {
		// Test 1
		Sonderfertigkeit sf1 = new Sonderfertigkeit();
		sf1.setId("SFK-Standart1");
		sf1.setGpKosten(5.0);
		
		assertTrue(	prozessorSonderf.canAddElement(sf1) );
		GeneratorLink link = prozessorSonderf.addNewElement(sf1);
		assertFalse( prozessorSonderf.canAddElement(sf1) );
		
		assertFalse( prozessorSonderf.canUpdateWert(link) );
		assertFalse( prozessorSonderf.canUpdateText(link) );
		assertFalse( prozessorSonderf.canUpdateZweitZiel(link, sf1) );
		
		// Test 2
		Sonderfertigkeit sf2 = new Sonderfertigkeit();
		sf2.setTextVorschlaege(new String[] {"1", "2"});
		sf2.setId("SFK-Standart2");
		sf2.setGpKosten(5.0);
		
		assertTrue(	prozessorSonderf.canAddElement(sf2) );
		GeneratorLink link2 = prozessorSonderf.addNewElement(sf2);
		assertTrue( prozessorSonderf.canAddElement(sf2) ); // Wegen dem Text
		
		assertFalse( prozessorSonderf.canUpdateWert(link2) );
		assertTrue( prozessorSonderf.canUpdateText(link2) );
		assertFalse( prozessorSonderf.canUpdateZweitZiel(link2, sf1) );
		
		// Test 3
		Sonderfertigkeit sf3 = new Sonderfertigkeit();
		sf3.setMitFreienText(true);
		sf3.setId("SFK-Standart3");
		sf3.setGpKosten(5.0);
		
		assertTrue(	prozessorSonderf.canAddElement(sf3) );
		GeneratorLink link3 = prozessorSonderf.addNewElement(sf3);
		assertTrue( prozessorSonderf.canAddElement(sf2) ); // Wegen dem Text
		
		assertFalse( prozessorSonderf.canUpdateWert(link3) );
		assertTrue( prozessorSonderf.canUpdateText(link3) );
		assertFalse( prozessorSonderf.canUpdateZweitZiel(link3, sf1) );
		
		// Test 4
		prozessorSonderf.removeElement(link);
		assertTrue(	prozessorSonderf.canAddElement(sf1) );
		prozessorSonderf.addNewElement(sf1);
		assertFalse( prozessorSonderf.canAddElement(sf1) );
		
	}
	
	@Test
	public void testPermanenteKosten() {
		((ProzessorEigenschaften) charakter.getProzessor(Eigenschaft.class).getExtendedInterface()).STUFE_ERHALTEN = false;
		charakter.getProzessor(Eigenschaft.class).updateWert(
				charakter.getProzessor(Eigenschaft.class).getElementBox().getObjectById(EigenschaftEnum.KA.getEigenschaft()), 
				6);
		
		// Test 1: Hinzufügen
		Sonderfertigkeit sf1 = new Sonderfertigkeit();
		sf1.setId("SFK-Standart1");
		sf1.setGpKosten(5.0);
		sf1.setPermAsp(1);
		sf1.setPermKep(2);
		sf1.setPermLep(3);
		
		int asp = charakter.getEigenschaftsWert(EigenschaftEnum.ASP);
		int kep = charakter.getEigenschaftsWert(EigenschaftEnum.KA);
		int lep = charakter.getEigenschaftsWert(EigenschaftEnum.LEP);
		
		GeneratorLink link1 = prozessorSonderf.addNewElement(sf1);
		
		assertEquals(asp-1, charakter.getEigenschaftsWert(EigenschaftEnum.ASP));
		assertEquals(kep-2, charakter.getEigenschaftsWert(EigenschaftEnum.KA));
		assertEquals(lep-3, charakter.getEigenschaftsWert(EigenschaftEnum.LEP));
		
		// Test 2: Nochmal hinzufügen
		Sonderfertigkeit sf2 = new Sonderfertigkeit();
		sf2.setId("SFK-Standart2");
		sf2.setGpKosten(5.0);
		sf2.setPermAsp(1);
		sf2.setPermKep(2);
		sf2.setPermLep(3);
		
		GeneratorLink link2 = prozessorSonderf.addNewElement(sf2);
		
		assertEquals(asp-2, charakter.getEigenschaftsWert(EigenschaftEnum.ASP));
		assertEquals(kep-4, charakter.getEigenschaftsWert(EigenschaftEnum.KA));
		assertEquals(lep-6, charakter.getEigenschaftsWert(EigenschaftEnum.LEP));
		
		// Test 3: Wieder entfernen
		prozessorSonderf.removeElement(link1);
		
		assertEquals(asp-1, charakter.getEigenschaftsWert(EigenschaftEnum.ASP));
		assertEquals(kep-2, charakter.getEigenschaftsWert(EigenschaftEnum.KA));
		assertEquals(lep-3, charakter.getEigenschaftsWert(EigenschaftEnum.LEP));
		
		prozessorSonderf.removeElement(link2);
		
		assertEquals(asp, charakter.getEigenschaftsWert(EigenschaftEnum.ASP));
		assertEquals(kep, charakter.getEigenschaftsWert(EigenschaftEnum.KA));
		assertEquals(lep, charakter.getEigenschaftsWert(EigenschaftEnum.LEP));
		
		// Test 4: Hinzufügen und entfernen von 2x gleiche SF mit Text
		Sonderfertigkeit sf3 = new Sonderfertigkeit();
		sf3.setId("SFK-Standart3");
		sf3.setGpKosten(5.0);
		sf3.setPermAsp(1);
		sf3.setPermKep(2);
		sf3.setMitFreienText(true);
		
		GeneratorLink link3_1 = prozessorSonderf.addNewElement(sf3);
		prozessorSonderf.updateText(link3_1, "eins");
		GeneratorLink link3_2 = prozessorSonderf.addNewElement(sf3);
		prozessorSonderf.updateText(link3_2, "zwei");
		
		assertEquals(asp-2, charakter.getEigenschaftsWert(EigenschaftEnum.ASP));
		assertEquals(kep-4, charakter.getEigenschaftsWert(EigenschaftEnum.KA));
		
		prozessorSonderf.removeElement(link3_2);
		assertEquals(asp-1, charakter.getEigenschaftsWert(EigenschaftEnum.ASP));
		assertEquals(kep-2, charakter.getEigenschaftsWert(EigenschaftEnum.KA));
		
		prozessorSonderf.removeElement(link3_1);
		assertEquals(asp, charakter.getEigenschaftsWert(EigenschaftEnum.ASP));
		assertEquals(kep, charakter.getEigenschaftsWert(EigenschaftEnum.KA));
	}
	
	@Test
	public void testAutomatischesTalent() {
		Talent talent1 = new Talent();
		talent1.setId("TAL-auto-1");
		talent1.setDreiEigenschaften(
				new Eigenschaft[]  {
						EigenschaftEnum.MU.getEigenschaft(),
						EigenschaftEnum.KL.getEigenschaft(),
						EigenschaftEnum.GE.getEigenschaft()
				});
		talent1.setKostenKlasse(KostenKlasse.B);
		talent1.setArt(Talent.Art.spezial);
		talent1.setSorte(Talent.Sorte.spezial);
		talent1.setName("Gabe X");
		
		Sonderfertigkeit sf1 = new Sonderfertigkeit();
		sf1.setId("SFK-Standart3");
		sf1.setGpKosten(5.0d);
		sf1.setAutomatischesTalent(talent1);
		
		// .1 Hinzufügen von Vorteil & automatisch Talent
		GeneratorLink link = prozessorSonderf.addNewElement(sf1);
		GeneratorLink linkTalent = prozessorTalent.getElementBox().getObjectById(talent1);

		assertNotNull(linkTalent);
		assertEquals(3, linkTalent.getWert());
		assertEquals(0, linkTalent.getKosten());
		assertEquals(0, prozessorTalent.getGesamtKosten());
		assertEquals(3, prozessorTalent.getMinWert(linkTalent));
		assertFalse(prozessorTalent.canRemoveElement(linkTalent));
		assertFalse(prozessorTalent.canUpdateText(linkTalent));
		assertFalse(prozessorTalent.canUpdateZweitZiel(linkTalent, sf1));
		assertTrue(prozessorTalent.canUpdateWert(linkTalent));
		
		// 2. Entfernen von Vorteil & automatisch Talent
		prozessorSonderf.removeElement(link);
		linkTalent = prozessorTalent.getElementBox().getObjectById(talent1);
		assertNull(linkTalent);
		
		// 3. Erneutes Hinzufügen
		link = prozessorSonderf.addNewElement(sf1);
		linkTalent = prozessorTalent.getElementBox().getObjectById(talent1);
		
		assertNotNull(linkTalent);
		assertEquals(3, linkTalent.getWert());
		assertEquals(0, linkTalent.getKosten());
		assertEquals(3, prozessorTalent.getMinWert(linkTalent));
		assertFalse(prozessorTalent.canRemoveElement(linkTalent));
		assertFalse(prozessorTalent.canUpdateText(linkTalent));
		assertFalse(prozessorTalent.canUpdateZweitZiel(linkTalent, sf1));
		assertTrue(prozessorTalent.canUpdateWert(linkTalent));
		
		// 4. Modifizieren des automatischen Talents
		prozessorTalent.updateWert(linkTalent, 5);
		assertEquals(5, linkTalent.getWert());
		assertEquals(19, linkTalent.getKosten());
		assertEquals(19, prozessorTalent.getGesamtKosten());
		
		prozessorTalent.updateWert(linkTalent, 3);
		assertEquals(3, linkTalent.getWert());
		assertEquals(0, linkTalent.getKosten());
		assertEquals(0, prozessorTalent.getGesamtKosten());
		
		prozessorTalent.updateWert(linkTalent, 4);
		assertEquals(4, linkTalent.getWert());
		assertEquals(8, linkTalent.getKosten());
		assertEquals(8, prozessorTalent.getGesamtKosten());
		
		// 5. Wieder entfernen von Vorteil & automatisch Talent
		prozessorSonderf.removeElement(link);
		linkTalent = prozessorTalent.getElementBox().getObjectById(talent1);
		assertNull(linkTalent);
		
		assertEquals(0, prozessorTalent.getGesamtKosten());
	}
	
	@Test
	public void testAdditionsFamilie() {
		AdditionsFamilie addFam;
		StoreDataAccessor storeData = StoreDataAccessor.getInstance();
		List<Sonderfertigkeit> sfList = new ArrayList<Sonderfertigkeit>();
		
		// Vorbereitung
		Sonderfertigkeit sf0 = new Sonderfertigkeit();
		sf0.setId("SFK-NixDamitZuTun");
		sf0.setGpKosten(1.0d);
		sfList.add(sf0);
		
		Sonderfertigkeit sf1 = new Sonderfertigkeit();
		sf1.setId("SFK-AstraleMed-I");
		sf1.setGpKosten(4.0d);
		addFam = new AdditionsFamilie();
		addFam.setAdditionsID("SFK-AstraleMed");
		addFam.setAdditionsWert(1);
		sf1.setAdditionsFamilie(addFam);
		sfList.add(sf1);
		
		Sonderfertigkeit sf2 = new Sonderfertigkeit();
		sf2.setId("SFK-AstraleMed-II");
		sf2.setGpKosten(4.0d);
		addFam = new AdditionsFamilie();
		addFam.setAdditionsID("SFK-AstraleMed");
		addFam.setAdditionsWert(2);
		sf2.setAdditionsFamilie(addFam);
		sfList.add(sf2);
		
		Sonderfertigkeit sf3 = new Sonderfertigkeit();
		sf3.setId("SFK-AstraleMed-III");
		sf3.setGpKosten(4.0d);
		addFam = new AdditionsFamilie();
		addFam.setAdditionsID("SFK-AstraleMed");
		addFam.setAdditionsWert(3);
		sf3.setAdditionsFamilie(addFam);
		sfList.add(sf3);
		
		storeData.getXmlAccessors().get(0).setSonderfList(sfList);
		
		IdLink link1 = new IdLink(rasse, sf1, null, Link.KEIN_WERT, null);
		IdLink link2 = new IdLink(rasse, sf2, null, Link.KEIN_WERT, null);
		IdLink link3 = new IdLink(rasse, sf3, null, Link.KEIN_WERT, null);
		
		// Modis Hinzufügen
		GeneratorLink genLink = (GeneratorLink)  prozessorSonderf.addNewElement(sf0);
		assertEquals(1, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-NixDamitZuTun", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorSonderf.addModi(link1);
		assertEquals(0, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-I", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorSonderf.addModi(link1);
		assertEquals(0, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-II", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorSonderf.addModi(link1);
		assertEquals(0, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-III", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorSonderf.addModi(link1);
		assertEquals(-4, genLink.getKosten());
		assertEquals(-3, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-III", genLink.getZiel().getId());
		
		// Wieder alle Modis löschen
		
		prozessorSonderf.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-III", genLink.getZiel().getId());
		
		prozessorSonderf.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-II", genLink.getZiel().getId());
		
		prozessorSonderf.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-I", genLink.getZiel().getId());
		
		prozessorSonderf.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertFalse(prozessorSonderf.containsLink(genLink));
		
		// Hinzufügen 2
		genLink = (GeneratorLink) prozessorSonderf.addModi(link2);
		assertEquals(0, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-II", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorSonderf.addModi(link3);
		assertEquals(-8, genLink.getKosten());
		assertEquals(-7, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-III", genLink.getZiel().getId());
		
		// Entfernen 2
		prozessorSonderf.removeModi(genLink, link2);
		assertEquals(0, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-III", genLink.getZiel().getId());
		
		prozessorSonderf.removeModi(genLink, link3);
		assertEquals(0, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertFalse(prozessorSonderf.containsLink(genLink));
		
		// Hinzufügen 3
		genLink = (GeneratorLink) prozessorSonderf.addModi(link2);
		assertEquals(0, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-II", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorSonderf.addModi(link2);
		assertEquals(-4, genLink.getKosten());
		assertEquals(-3, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-III", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorSonderf.addModi(link1);
		assertEquals(-8, genLink.getKosten());
		assertEquals(-7, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-III", genLink.getZiel().getId());
		
		// Entfernen & Hinzuügen 2
		prozessorSonderf.removeModi(genLink, link2);
		assertEquals(0, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-III", genLink.getZiel().getId());
		
		prozessorSonderf.removeModi(genLink, link2);
		assertEquals(0, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-I", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorSonderf.addModi(link1);
		assertEquals(0, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-II", genLink.getZiel().getId());
		
		prozessorSonderf.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertEquals("SFK-AstraleMed-I", genLink.getZiel().getId());
		
		prozessorSonderf.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten());
		assertEquals(1, prozessorSonderf.getGesamtKosten());
		assertFalse(prozessorSonderf.containsLink(genLink));
	}
}
