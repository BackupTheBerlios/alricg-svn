/*
 * Created 05.06.2008
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
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.Fertigkeit.AdditionsFamilie;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.held.CharakterDaten;
import org.d3s.alricg.store.held.HeldenLink;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Vincent
 *
 */
public class ProzessorVorteilTest {
	private Rasse rasse;
	private Charakter charakter;
	private ProzessorDecorator<Vorteil, GeneratorLink> prozessorVorteil;
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
			// TODO Auto-generated catch block
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
		hash.put(
				Eigenschaft.class,
				new ProzessorDecorator(charakter, new ProzessorEigenschaften(charakter)));
		hash.put(
				Vorteil.class,
				new ProzessorDecorator(charakter, new ProzessorVorteil(charakter)));
		hash.put(
				Talent.class,
				new ProzessorDecorator(charakter, new ProzessorTalent(charakter)));

		charakter.setProzessorHash(hash);
		//prozessorEigenschaft = (ProzessorDecorator) charakter.getProzessor(Eigenschaft.class);
		prozessorVorteil = (ProzessorDecorator) charakter.getProzessor(Vorteil.class);
		prozessorTalent = (ProzessorDecorator) charakter.getProzessor(Talent.class);

		// Rassen erzeugen
		rasse = new Rasse();
		rasse.setId("RAS-test");
	}
	
	@Test
	public void testAddRemoveKostenNormal() {
		// Test 1: Kosten mit Stufe
		Vorteil vorteil1 = new Vorteil();
		vorteil1.setId("VOR-StufenKosten");
		vorteil1.setMinStufe(1);
		vorteil1.setMaxStufe(3);
		vorteil1.setKostenProStufe(4.0d);
		
		GeneratorLink link = prozessorVorteil.addNewElement(vorteil1);
		assertEquals(4, link.getKosten(),0);
		assertEquals(4, prozessorVorteil.getGesamtKosten(),0);
		prozessorVorteil.updateWert(link, 3);
		assertEquals(12, link.getKosten(),0);
		assertEquals(12, prozessorVorteil.getGesamtKosten(),0);
		
		// Test 2: Festkosten
		Vorteil vorteil2 = new Vorteil();
		vorteil2.setId("VOR-FestKosten");
		vorteil2.setGpKosten(5.5d);
		
		link = prozessorVorteil.addNewElement(vorteil2);
		assertEquals(5.5, link.getKosten(),0);
		assertEquals(17.5, prozessorVorteil.getGesamtKosten(),0);

		// Test 3: Festkosten + Stufe
		Vorteil vorteil3 = new Vorteil();
		vorteil3.setId("VOR-FestKostenUndStufe");
		vorteil3.setGpKosten(2.0d);
		vorteil3.setMinStufe(1);
		vorteil3.setMaxStufe(5);
		vorteil3.setKostenProStufe(0.5d);
		
		link = prozessorVorteil.addNewElement(vorteil3);
		assertEquals(2.5, link.getKosten(),0);
		assertEquals(20, prozessorVorteil.getGesamtKosten(),0);
		prozessorVorteil.updateWert(link, 2);
		assertEquals(3, link.getKosten(),0);
		assertEquals(20.5, prozessorVorteil.getGesamtKosten(),0);
		prozessorVorteil.updateWert(link, 3);
		assertEquals(3.5, link.getKosten(),0);
		assertEquals(21, prozessorVorteil.getGesamtKosten(),0);
		
		// Test 4: Entfernen von Vorteil2
		link = prozessorVorteil.getElementBox().getObjectById(vorteil2);
		assertTrue(prozessorVorteil.containsLink(link));
		prozessorVorteil.removeElement(link);
		assertFalse(prozessorVorteil.containsLink(link));
		assertEquals(15.5, prozessorVorteil.getGesamtKosten(),0);
		
		// Test 5: Entfernen von Vorteil3
		link = prozessorVorteil.getElementBox().getObjectById(vorteil3);
		assertTrue(prozessorVorteil.containsLink(link));
		prozessorVorteil.removeElement(link);
		assertFalse(prozessorVorteil.containsLink(link));
		assertEquals(12, prozessorVorteil.getGesamtKosten(),0);
		
		// Test 6: Update und Entfernen von Vorteil1
		link = prozessorVorteil.getElementBox().getObjectById(vorteil1);
		prozessorVorteil.updateWert(link, 2);
		assertEquals(8, prozessorVorteil.getGesamtKosten(),0);
		assertTrue(prozessorVorteil.containsLink(link));
		prozessorVorteil.removeElement(link);
		assertFalse(prozessorVorteil.containsLink(link));
		assertEquals(0, prozessorVorteil.getGesamtKosten(),0);
	}
	
	@Test
	public void testModisOhneStufe() {
		// Test 1: Kosten mit Stufe
		Vorteil vorteil1 = new Vorteil();
		vorteil1.setId("VOR-t1");
		vorteil1.setMinStufe(0);
		vorteil1.setMaxStufe(0);
		vorteil1.setGpKosten(4.0);
		
		HeldenLink link = prozessorVorteil.addNewElement(vorteil1);
		assertEquals(4, link.getKosten(),0);
		assertEquals(4, prozessorVorteil.getGesamtKosten(),0);
		
		// Test 2: Modi hinzuf�gen
		IdLink linkModi1 = new IdLink(rasse, vorteil1, null, Link.KEIN_WERT, null);
		
		link = prozessorVorteil.addModi(linkModi1);
		assertEquals(0, link.getKosten(),0);
		assertEquals(0, prozessorVorteil.getGesamtKosten(),0);
		
		// Test 3: Modi hinzuf�gen
		linkModi1 = new IdLink(rasse, vorteil1, null, Link.KEIN_WERT, null);
		
		link = prozessorVorteil.addModi(linkModi1);
		assertEquals(-4, link.getKosten(),0);
		assertEquals(-4, prozessorVorteil.getGesamtKosten(),0);
		
		// Test 4: Modi wieder l�schen
		prozessorVorteil.removeModi((GeneratorLink) link, linkModi1);
		
		assertEquals(0, link.getKosten(),0);
		assertEquals(0, prozessorVorteil.getGesamtKosten(),0);
		
		// Test 5: Modi wieder l�schen
		linkModi1 = (IdLink) prozessorVorteil.getElementBox().getObjectById(vorteil1).getLinkModiList().get(0);
		// Test 5: Modi wieder l�schen
		prozessorVorteil.removeModi((GeneratorLink) link, linkModi1);
		
		assertEquals(4, link.getKosten(),0);
		assertEquals(4, prozessorVorteil.getGesamtKosten(),0);
	}
	
	@Test
	public void testUserPlusModis() {
		// Test 1: Kosten mit Stufe
		Vorteil vorteil1 = new Vorteil();
		vorteil1.setId("VOR-StufenKosten");
		vorteil1.setMinStufe(1);
		vorteil1.setMaxStufe(3);
		vorteil1.setKostenProStufe(4.0d);
		
		GeneratorLink link = prozessorVorteil.addNewElement(vorteil1);
		assertEquals(4, link.getKosten(),0);
		assertEquals(4, prozessorVorteil.getGesamtKosten(),0);
		prozessorVorteil.updateWert(link, 3);
		assertEquals(12, link.getKosten(),0);
		assertEquals(12, prozessorVorteil.getGesamtKosten(),0);
		assertEquals(1, prozessorVorteil.getMinWert(link));
		assertEquals(3, prozessorVorteil.getMaxWert(link));
		
		// Test 2: Modis hinzuf�gen
		IdLink linkModi1 = new IdLink(rasse, vorteil1, null, 2, null);
		prozessorVorteil.addModi(linkModi1);
		assertEquals(3, link.getWert());
		assertEquals(4, link.getKosten(),0);
		assertEquals(4, prozessorVorteil.getGesamtKosten(),0);
		assertEquals(2, prozessorVorteil.getMinWert(link));
		assertEquals(3, prozessorVorteil.getMaxWert(link));
		
		// Test 3: Modis hinzuf�gen
		IdLink linkModi2 = new IdLink(rasse, vorteil1, null, 1, null);
		prozessorVorteil.addModi(linkModi2);
		assertEquals(3, link.getWert());
		assertEquals(0, link.getKosten(),0);
		assertEquals(0, prozessorVorteil.getGesamtKosten(),0);
		assertEquals(3, prozessorVorteil.getMinWert(link));
		assertEquals(3, prozessorVorteil.getMaxWert(link));
		
		// Test 4: Modis entfernen
		prozessorVorteil.removeModi(link, linkModi2);
		assertEquals(2, link.getWert());
		assertEquals(0, link.getKosten(),0);
		assertEquals(0, prozessorVorteil.getGesamtKosten(),0);
		prozessorVorteil.removeModi(link, linkModi1);
		assertEquals(1, link.getWert());
		assertEquals(4, link.getKosten(),0);
		assertTrue(prozessorVorteil.getElementBox().contiansEqualObject(link));
		
		// Test entferne UserLink
		prozessorVorteil.removeElement(link);
		assertFalse(prozessorVorteil.getElementBox().contiansEqualObject(link));
	}
	
	@Test
	public void testModisOnly() {
		// Vorbereitung
		Vorteil vorteil1 = new Vorteil();
		vorteil1.setId("VOR-StufenKosten");
		vorteil1.setMinStufe(1);
		vorteil1.setMaxStufe(3);
		vorteil1.setKostenProStufe(4.0d);
		
		// Test 2: Modis hinzuf�gen
		IdLink linkModi1 = new IdLink(rasse, vorteil1, null, 2, null);
		GeneratorLink link = (GeneratorLink) prozessorVorteil.addModi(linkModi1);
		assertEquals(2, link.getWert());
		assertEquals(0, link.getKosten(),0);
		assertEquals(0, prozessorVorteil.getGesamtKosten(),0);
		assertEquals(2, prozessorVorteil.getMinWert(link));
		assertEquals(3, prozessorVorteil.getMaxWert(link));
		
		// Test 3: Modis hinzuf�gen
		IdLink linkModi2 = new IdLink(rasse, vorteil1, null, 1, null);
		prozessorVorteil.addModi(linkModi2);
		assertEquals(3, link.getWert());
		assertEquals(0, link.getKosten(),0);
		assertEquals(0, prozessorVorteil.getGesamtKosten(),0);
		
		// Test 4: Modis entfernen
		prozessorVorteil.removeModi(link, linkModi2);
		assertEquals(2, link.getWert());
		assertEquals(0, link.getKosten(),0);
		assertEquals(0, prozessorVorteil.getGesamtKosten(),0);
		assertTrue(prozessorVorteil.getElementBox().contiansEqualObject(link));
		prozessorVorteil.removeModi(link, linkModi1);
		assertFalse(prozessorVorteil.getElementBox().contiansEqualObject(link));
	}
	
	@Test
	public void testCan() {
		Vorteil vorteil1 = new Vorteil();
		vorteil1.setId("VOR-Test");
		vorteil1.setMinStufe(1);
		vorteil1.setMaxStufe(3);
		vorteil1.setKostenProStufe(4.0d);
		
		// Test 1: Neu hinzuf�gen
		assertTrue(prozessorVorteil.canAddElement(vorteil1));
		IdLink IdLink1 = new IdLink(rasse, vorteil1, null, 2, null);
		assertTrue(prozessorVorteil.canAddElement(IdLink1));
		
		// Test 2: Hinzugef�gt, doppelt geht nicht
		GeneratorLink link = (GeneratorLink) prozessorVorteil.addModi(IdLink1);
		assertFalse(prozessorVorteil.canAddElement(vorteil1));
		IdLink1 = new IdLink(rasse, vorteil1, null, 1, null);
		assertFalse(prozessorVorteil.canAddElement(IdLink1));
		
		assertFalse(prozessorVorteil.canRemoveElement(link));
		assertFalse(prozessorVorteil.canUpdateText(link));
		assertFalse(prozessorVorteil.canUpdateZweitZiel(link, vorteil1));
		assertTrue(prozessorVorteil.canUpdateWert(link));
		
		// Test 3: 
		Vorteil vorteil2 = new Vorteil();
		vorteil2.setId("VOR-Text");
		vorteil2.setGpKosten(5.0d);
		vorteil2.setMitFreienText(true);
		
		assertTrue(prozessorVorteil.canAddElement(vorteil2));
		link = (GeneratorLink) prozessorVorteil.addNewElement(vorteil2);
		assertTrue(prozessorVorteil.canUpdateText(link));
		prozessorVorteil.updateText(link, "Test1");
		
		assertTrue(prozessorVorteil.canAddElement(vorteil2)); // Wegen dem Freitext
		assertTrue(prozessorVorteil.canRemoveElement(link));
		assertFalse(prozessorVorteil.canUpdateZweitZiel(link, vorteil1));
		assertFalse(prozessorVorteil.canUpdateWert(link));
		
		IdLink1 = new IdLink(null, vorteil2, null, Link.KEIN_WERT, "Test1");
		assertFalse(prozessorVorteil.canAddElement(IdLink1)); // Wegen gleichem Text
		IdLink1.setText("Text2");
		assertTrue(prozessorVorteil.canAddElement(IdLink1)); // Wegen gleichem Text
		
		// Test 4: 
		Vorteil vorteil3 = new Vorteil();
		vorteil3.setId("VOR-TextZwei");
		vorteil3.setGpKosten(5.0d);
		vorteil3.setTextVorschlaege(new String[] {"eins", "zwei"});
		
		// Test 5: 
		Vorteil vorteil4 = new Vorteil();
		vorteil4.setId("VOR-ZweitZiel");
		vorteil4.setGpKosten(5.0d);
		vorteil4.setBenoetigtZweitZiel(true);

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
		
		Vorteil vorteil1 = new Vorteil();
		vorteil1.setId("VOR-AutoTal");
		vorteil1.setGpKosten(5.0d);
		vorteil1.setAutomatischesTalent(talent1);
		
		// .1 Hinzuf�gen von Vorteil & automatisch Talent
		GeneratorLink link = prozessorVorteil.addNewElement(vorteil1);
		GeneratorLink linkTalent = prozessorTalent.getElementBox().getObjectById(talent1);

		assertNotNull(linkTalent);
		assertEquals(3, linkTalent.getWert());
		assertEquals(0, linkTalent.getKosten(),0);
		assertEquals(0, prozessorTalent.getGesamtKosten(),0);
		assertEquals(3, prozessorTalent.getMinWert(linkTalent));
		assertFalse(prozessorTalent.canRemoveElement(linkTalent));
		assertFalse(prozessorTalent.canUpdateText(linkTalent));
		assertFalse(prozessorTalent.canUpdateZweitZiel(linkTalent, vorteil1));
		assertTrue(prozessorTalent.canUpdateWert(linkTalent));
		
		// 2. Entfernen von Vorteil & automatisch Talent
		prozessorVorteil.removeElement(link);
		linkTalent = prozessorTalent.getElementBox().getObjectById(talent1);
		assertNull(linkTalent);
		
		// 3. Erneutes Hinzuf�gen
		link = prozessorVorteil.addNewElement(vorteil1);
		linkTalent = prozessorTalent.getElementBox().getObjectById(talent1);
		
		assertNotNull(linkTalent);
		assertEquals(3, linkTalent.getWert());
		assertEquals(0, linkTalent.getKosten(),0);
		assertEquals(3, prozessorTalent.getMinWert(linkTalent));
		assertFalse(prozessorTalent.canRemoveElement(linkTalent));
		assertFalse(prozessorTalent.canUpdateText(linkTalent));
		assertFalse(prozessorTalent.canUpdateZweitZiel(linkTalent, vorteil1));
		assertTrue(prozessorTalent.canUpdateWert(linkTalent));
		
		// 4. Modifizieren des automatischen Talents
		prozessorTalent.updateWert(linkTalent, 5);
		assertEquals(5, linkTalent.getWert());
		assertEquals(19, linkTalent.getKosten(),0);
		assertEquals(19, prozessorTalent.getGesamtKosten(),0);
		
		prozessorTalent.updateWert(linkTalent, 3);
		assertEquals(3, linkTalent.getWert());
		assertEquals(0, linkTalent.getKosten(),0);
		assertEquals(0, prozessorTalent.getGesamtKosten(),0);
		
		prozessorTalent.updateWert(linkTalent, 4);
		assertEquals(4, linkTalent.getWert());
		assertEquals(8, linkTalent.getKosten(),0);
		assertEquals(8, prozessorTalent.getGesamtKosten(),0);
		
		// 5. Wieder entfernen von Vorteil & automatisch Talent
		prozessorVorteil.removeElement(link);
		linkTalent = prozessorTalent.getElementBox().getObjectById(talent1);
		assertNull(linkTalent);
		
		assertEquals(0, prozessorTalent.getGesamtKosten(),0);
	}
		
	@Test
	public void testAdditionsFamilie() {
		AdditionsFamilie addFam;
		StoreDataAccessor storeData = StoreDataAccessor.getInstance();
		List<Vorteil> vorteilList = new ArrayList<Vorteil>();
		
		// Vorbereitung
		Vorteil vorteil0 = new Vorteil();
		vorteil0.setId("VOR-NixDamitZuTun");
		vorteil0.setGpKosten(1.0d);
		vorteilList.add(vorteil0);
		
		Vorteil vorteil1 = new Vorteil();
		vorteil1.setId("VOR-AstraleMed-I");
		vorteil1.setGpKosten(4.0d);
		addFam = new AdditionsFamilie();
		addFam.setAdditionsID("VOR-AstraleMed");
		addFam.setAdditionsWert(1);
		vorteil1.setAdditionsFamilie(addFam);
		vorteilList.add(vorteil1);
		
		Vorteil vorteil2 = new Vorteil();
		vorteil2.setId("VOR-AstraleMed-II");
		vorteil2.setGpKosten(4.0d);
		addFam = new AdditionsFamilie();
		addFam.setAdditionsID("VOR-AstraleMed");
		addFam.setAdditionsWert(2);
		vorteil2.setAdditionsFamilie(addFam);
		vorteilList.add(vorteil2);
		
		Vorteil vorteil3 = new Vorteil();
		vorteil3.setId("VOR-AstraleMed-III");
		vorteil3.setGpKosten(4.0d);
		addFam = new AdditionsFamilie();
		addFam.setAdditionsID("VOR-AstraleMed");
		addFam.setAdditionsWert(3);
		vorteil3.setAdditionsFamilie(addFam);
		vorteilList.add(vorteil3);
		
		storeData.getXmlAccessors().get(0).setVorteilList(vorteilList);
		
		IdLink link1 = new IdLink(rasse, vorteil1, null, Link.KEIN_WERT, null);
		IdLink link2 = new IdLink(rasse, vorteil2, null, Link.KEIN_WERT, null);
		IdLink link3 = new IdLink(rasse, vorteil3, null, Link.KEIN_WERT, null);
		
		// Modis Hinzuf�gen
		GeneratorLink genLink = (GeneratorLink)  prozessorVorteil.addNewElement(vorteil0);
		assertEquals(1, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-NixDamitZuTun", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorVorteil.addModi(link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-I", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorVorteil.addModi(link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-II", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorVorteil.addModi(link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-III", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorVorteil.addModi(link1);
		assertEquals(-4, genLink.getKosten(),0);
		assertEquals(-3, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-III", genLink.getZiel().getId());
		
		// Wieder alle Modis l�schen
		
		prozessorVorteil.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-III", genLink.getZiel().getId());
		
		prozessorVorteil.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-II", genLink.getZiel().getId());
		
		prozessorVorteil.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-I", genLink.getZiel().getId());
		
		prozessorVorteil.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertFalse(prozessorVorteil.containsLink(genLink));
		
		// Hinzuf�gen 2
		genLink = (GeneratorLink) prozessorVorteil.addModi(link2);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-II", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorVorteil.addModi(link3);
		assertEquals(-8, genLink.getKosten(),0);
		assertEquals(-7, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-III", genLink.getZiel().getId());
		
		// Entfernen 2
		prozessorVorteil.removeModi(genLink, link2);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-III", genLink.getZiel().getId());
		
		prozessorVorteil.removeModi(genLink, link3);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertFalse(prozessorVorteil.containsLink(genLink));
		
		// Hinzuf�gen 3
		genLink = (GeneratorLink) prozessorVorteil.addModi(link2);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-II", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorVorteil.addModi(link2);
		assertEquals(-4, genLink.getKosten(),0);
		assertEquals(-3, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-III", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorVorteil.addModi(link1);
		assertEquals(-8, genLink.getKosten(),0);
		assertEquals(-7, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-III", genLink.getZiel().getId());
		
		// Entfernen & Hinzu�gen 2
		prozessorVorteil.removeModi(genLink, link2);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-III", genLink.getZiel().getId());
		
		prozessorVorteil.removeModi(genLink, link2);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-I", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorVorteil.addModi(link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-II", genLink.getZiel().getId());
		
		prozessorVorteil.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertEquals("VOR-AstraleMed-I", genLink.getZiel().getId());
		
		prozessorVorteil.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(1, prozessorVorteil.getGesamtKosten(),0);
		assertFalse(prozessorVorteil.containsLink(genLink));
	}
	
}
