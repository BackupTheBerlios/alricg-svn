/*
 * Created 29.07.2008
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.prozessor.charElemente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorNachteil;
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Fertigkeit.AdditionsFamilie;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.held.CharakterDaten;
import org.d3s.alricg.store.held.HeldenLink;
import org.d3s.alricg.store.rules.RegelConfig;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Vincent
 *
 */
public class ProzessorNachteilTest {
	private Rasse rasse;
	private Charakter charakter;
	private ProzessorDecorator<Nachteil, GeneratorLink> prozessorNachteil;
	private ProzessorDecorator<Talent, GeneratorLink> prozessorTalent;
	
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
				Nachteil.class,
				new ProzessorDecorator(charakter, new ProzessorNachteil(charakter)));
		hash.put(
				Talent.class,
				new ProzessorDecorator(charakter, new ProzessorTalent(charakter)));

		charakter.setProzessorHash(hash);
		//prozessorEigenschaft = (ProzessorDecorator) charakter.getProzessor(Eigenschaft.class);
		prozessorNachteil = (ProzessorDecorator) charakter.getProzessor(Nachteil.class);
		prozessorTalent = (ProzessorDecorator) charakter.getProzessor(Talent.class);

		// Rassen erzeugen
		rasse = new Rasse();
		rasse.setId("RAS-test");
	}
	
	@Test
	public void testMinMaxWerte() {
		// Test 1: MinMax schlechte Eigenschaft
		Nachteil nachteil1 = new Nachteil();
		nachteil1.setId("NAC-MaxMin1");
		nachteil1.setSchlechteEigen(true);
		nachteil1.setKostenProStufe(-0.5d);
		
		GeneratorLink genLink = prozessorNachteil.addNewElement(nachteil1);
		
		assertEquals(
				RegelConfig.getInstance().getMaxSchlechtEigenchafWert(), 
				prozessorNachteil.getMaxWert(genLink));
		assertEquals(
				RegelConfig.getInstance().getMinSchlechteEigenchaftWert(), 
				prozessorNachteil.getMinWert(genLink));
		
		IdLink idLink = new IdLink(rasse, nachteil1, null, 7, null);
		prozessorNachteil.addModi(idLink);
		assertEquals(
				RegelConfig.getInstance().getMaxSchlechtEigenchafWert(), 
				prozessorNachteil.getMaxWert(genLink));
		assertEquals(
				7, 
				prozessorNachteil.getMinWert(genLink));
		
		idLink = new IdLink(rasse, nachteil1, null, 3, null);
		prozessorNachteil.addModi(idLink);
		assertEquals(
				RegelConfig.getInstance().getMaxSchlechtEigenchafWert(), 
				prozessorNachteil.getMaxWert(genLink));
		assertEquals(
				10, 
				prozessorNachteil.getMinWert(genLink));
		
		prozessorNachteil.removeModi(genLink, idLink);
		assertEquals(
				RegelConfig.getInstance().getMaxSchlechtEigenchafWert(), 
				prozessorNachteil.getMaxWert(genLink));
		assertEquals(
				7, 
				prozessorNachteil.getMinWert(genLink));
		
		// Test 2: MinMax ohne Stufe
		Nachteil nachteil2 = new Nachteil();
		nachteil2.setId("NAC-MaxMin2");
		nachteil2.setGpKosten(4d);
		
		genLink = prozessorNachteil.addNewElement(nachteil2);
		
		assertEquals(0, prozessorNachteil.getMaxWert(genLink));
		assertEquals(0, prozessorNachteil.getMinWert(genLink));
		
		// Test 3: MinMax ohne Stufe
		Nachteil nachteil3 = new Nachteil();
		nachteil3.setId("NAC-MaxMin3");
		nachteil3.setKostenProStufe(1d);
		nachteil3.setMaxStufe(3);
		nachteil3.setMinStufe(1);
		
		genLink = prozessorNachteil.addNewElement(nachteil3);
		
		assertEquals(3, prozessorNachteil.getMaxWert(genLink));
		assertEquals(1, prozessorNachteil.getMinWert(genLink));
		
		idLink = new IdLink(rasse, nachteil3, null, 3, null);
		prozessorNachteil.addModi(idLink);
		
		assertEquals(3, prozessorNachteil.getMaxWert(genLink));
		assertEquals(3, prozessorNachteil.getMinWert(genLink));
		
		prozessorNachteil.removeModi(genLink, idLink);
		assertEquals(3, prozessorNachteil.getMaxWert(genLink));
		assertEquals(1, prozessorNachteil.getMinWert(genLink));
	}
	
	@Test
	public void testSchlechteEigenGP() {
		// Test 1: Schlechte Eigenschaft
		Nachteil nachteil1 = new Nachteil();
		nachteil1.setId("NAC-SchEig1");
		nachteil1.setSchlechteEigen(true);
		nachteil1.setKostenProStufe(-0.5d);
		
		GeneratorLink genLink = prozessorNachteil.addNewElement(nachteil1);
		ExtendedProzessorNachteil extended = (ExtendedProzessorNachteil) prozessorNachteil.getExtendedInterface();
		
		assertEquals(RegelConfig.getInstance().getMinSchlechteEigenchaftWert(), genLink.getWert());
		assertEquals(RegelConfig.getInstance().getMinSchlechteEigenchaftWert() * -0.5d, genLink.getKosten(),0);
		assertEquals(extended.getGpSchlechteEigenschaft(), genLink.getKosten(),0);
		
		prozessorNachteil.updateWert(genLink, 10);
		assertEquals(-5d, genLink.getKosten(),0);
		assertEquals(extended.getGpSchlechteEigenschaft(), genLink.getKosten(),0);
		prozessorNachteil.updateWert(genLink, 9);
		assertEquals(-4.5d, genLink.getKosten(),0);
		assertEquals(-4.5d, extended.getGpSchlechteEigenschaft(),0);
		assertEquals(-4.5d, prozessorNachteil.getGesamtKosten(),0);
		
		// Test 2: Nachteile, nicht schlechte Eigenschaft
		Nachteil nachteil2 = new Nachteil();
		nachteil2.setId("NAC-SchEig2");
		nachteil2.setGpKosten(-4d);
		
		genLink = prozessorNachteil.addNewElement(nachteil2);
		
		assertEquals(-4.5d, extended.getGpSchlechteEigenschaft(),0);
		assertEquals(-8.5d, prozessorNachteil.getGesamtKosten(),0);
		
		// Test 3: Nochmal schlechte Eigenschaft
		Nachteil nachteil3 = new Nachteil();
		nachteil3.setId("NAC-SchEig3");
		nachteil3.setSchlechteEigen(true);
		nachteil3.setKostenProStufe(-1d);
		
		genLink = prozessorNachteil.addNewElement(nachteil3);
		
		prozessorNachteil.updateWert(genLink, 6);
		assertEquals(-10.5d, extended.getGpSchlechteEigenschaft(),0);
		assertEquals(-14.5d, prozessorNachteil.getGesamtKosten(),0);
		
		// Test 4: Modis für schlechte Eigenschaft
		IdLink idLink1 = new IdLink(rasse, nachteil3, null, 5, null);
		
		prozessorNachteil.addModi(idLink1);
		assertEquals(-5.5d, extended.getGpSchlechteEigenschaft(),0);
		assertEquals(-9.5d, prozessorNachteil.getGesamtKosten(),0);
		
		prozessorNachteil.updateWert(genLink, 8);
		assertEquals(-7.5d, extended.getGpSchlechteEigenschaft(),0);
		assertEquals(-11.5d, prozessorNachteil.getGesamtKosten(),0);
		
		// Test 5: Weiterer Modi
		IdLink idLink2 = new IdLink(rasse, nachteil3, null, 4, null);
		prozessorNachteil.addModi(idLink2);
		
		prozessorNachteil.updateWert(genLink, 8);
		assertEquals(9, genLink.getWert());
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-4.5d, extended.getGpSchlechteEigenschaft(),0);
		assertEquals(-8.5d, prozessorNachteil.getGesamtKosten(),0);
		
		// Test 6: Modis wieder entfernen
		prozessorNachteil.removeModi(genLink, idLink2);
		assertEquals(5, genLink.getWert());
		assertEquals(-4.5d, extended.getGpSchlechteEigenschaft(),0);
		assertEquals(-8.5d, prozessorNachteil.getGesamtKosten(),0);
		
		prozessorNachteil.removeModi(genLink, idLink1);
		assertFalse(prozessorNachteil.containsLink(genLink));
		assertEquals(-4.5d, extended.getGpSchlechteEigenschaft(),0);
		assertEquals(-8.5d, prozessorNachteil.getGesamtKosten(),0);
	}
	
	@Test
	public void testCanX() {
		Nachteil nachteil1 = new Nachteil();
		nachteil1.setId("NAC-CanX1");
		nachteil1.setSchlechteEigen(true);
		nachteil1.setKostenProStufe(-0.5d);
		
		// 1. True - Noch nichte enthalten
		assertTrue(prozessorNachteil.canAddElement(nachteil1));
		
		GeneratorLink genLink = prozessorNachteil.addNewElement(nachteil1);
		
		// 2. Bereits enthalten
		assertFalse(prozessorNachteil.canAddElement(nachteil1));
		assertTrue(prozessorNachteil.canRemoveElement(genLink));
		
		assertFalse(prozessorNachteil.canUpdateText(genLink));
		assertTrue(prozessorNachteil.canUpdateWert(genLink));
		assertFalse(prozessorNachteil.canUpdateZweitZiel(genLink, rasse));
		
		// Entfernen - kann also nicht mehr entfernt werden
		prozessorNachteil.removeElement(genLink);
		assertFalse(prozessorNachteil.canRemoveElement(genLink));
		
		// 3. Wieder hinzufügen, Modis -> kann nicht mehr entfernt werden
		assertTrue(prozessorNachteil.canAddElement(nachteil1));
		genLink = prozessorNachteil.addNewElement(nachteil1);
		
		IdLink idLink = new IdLink(rasse, nachteil1, null, 4, null);
		prozessorNachteil.addModi(idLink);
		
		assertFalse(prozessorNachteil.canRemoveElement(genLink));
		assertTrue(prozessorNachteil.canUpdateWert(genLink));
		
		prozessorNachteil.removeModi(genLink, idLink);
		assertTrue(prozessorNachteil.canRemoveElement(genLink));
		prozessorNachteil.removeElement(genLink);
		
		// 4. Standard
		Nachteil nachteil2 = new Nachteil();
		nachteil2.setId("NAC-canX2");
		nachteil2.setGpKosten(5.0d);
		nachteil2.setBenoetigtZweitZiel(true);
		nachteil2.setMitFreienText(true);
		
		assertTrue(prozessorNachteil.canAddElement(nachteil2));
		genLink = prozessorNachteil.addNewElement(nachteil2);
		
		assertTrue(prozessorNachteil.canUpdateText(genLink));
		assertFalse(prozessorNachteil.canUpdateWert(genLink));
		assertTrue(prozessorNachteil.canUpdateZweitZiel(genLink, rasse));
	}
	
	@Test
	public void testAddRemoveKostenNormal() {
		// Test 1: Kosten mit Stufe
		Nachteil nachteil1 = new Nachteil();
		nachteil1.setId("NAC-StufenKosten");
		nachteil1.setMinStufe(1);
		nachteil1.setMaxStufe(3);
		nachteil1.setKostenProStufe(-4.0d);
		
		GeneratorLink link = prozessorNachteil.addNewElement(nachteil1);
		assertEquals(-4, link.getKosten(),0);
		assertEquals(-4, prozessorNachteil.getGesamtKosten(),0);
		prozessorNachteil.updateWert(link, 3);
		assertEquals(-12, link.getKosten(),0);
		assertEquals(-12, prozessorNachteil.getGesamtKosten(),0);
		
		// Test 2: Festkosten
		Nachteil nachteil2 = new Nachteil();
		nachteil2.setId("NAC-FestKosten");
		nachteil2.setGpKosten(-5.5d);
		
		link = prozessorNachteil.addNewElement(nachteil2);
		assertEquals(-5.5, link.getKosten(),0);
		assertEquals(-17.5, prozessorNachteil.getGesamtKosten(),0);

		// Test 3: Festkosten + Stufe
		Nachteil nachteil3 = new Nachteil();
		nachteil3.setId("NAC-FestKostenUndStufe");
		nachteil3.setGpKosten(-2.0d);
		nachteil3.setMinStufe(1);
		nachteil3.setMaxStufe(5);
		nachteil3.setKostenProStufe(-0.5d);
		
		link = prozessorNachteil.addNewElement(nachteil3);
		assertEquals(-2.5, link.getKosten(),0);
		assertEquals(-20, prozessorNachteil.getGesamtKosten(),0);
		prozessorNachteil.updateWert(link, 2);
		assertEquals(-3, link.getKosten(),0);
		assertEquals(-20.5, prozessorNachteil.getGesamtKosten(),0);
		prozessorNachteil.updateWert(link, 3);
		assertEquals(-3.5, link.getKosten(),0);
		assertEquals(-21, prozessorNachteil.getGesamtKosten(),0);
		
		// Test 4: Entfernen von Nachteil2
		link = prozessorNachteil.getElementBox().getObjectById(nachteil2);
		assertTrue(prozessorNachteil.containsLink(link));
		prozessorNachteil.removeElement(link);
		assertFalse(prozessorNachteil.containsLink(link));
		assertEquals(-15.5, prozessorNachteil.getGesamtKosten(),0);
		
		// Test 5: Entfernen von Nachteil3
		link = prozessorNachteil.getElementBox().getObjectById(nachteil3);
		assertTrue(prozessorNachteil.containsLink(link));
		prozessorNachteil.removeElement(link);
		assertFalse(prozessorNachteil.containsLink(link));
		assertEquals(-12, prozessorNachteil.getGesamtKosten(),0);
		
		// Test 6: Update und Entfernen von Nachteil1
		link = prozessorNachteil.getElementBox().getObjectById(nachteil1);
		prozessorNachteil.updateWert(link, 2);
		assertEquals(-8, prozessorNachteil.getGesamtKosten(),0);
		assertTrue(prozessorNachteil.containsLink(link));
		prozessorNachteil.removeElement(link);
		assertFalse(prozessorNachteil.containsLink(link));
		assertEquals(0, prozessorNachteil.getGesamtKosten(),0);
	}
	
	@Test
	public void testAdditionsFamilie() {
		AdditionsFamilie addFam;
		StoreDataAccessor storeData = StoreDataAccessor.getInstance();
		List<Nachteil> nachteilList = new ArrayList<Nachteil>();
		
		// Vorbereitung
		Nachteil nachteil0 = new Nachteil();
		nachteil0.setId("NAC-NixDamitZuTun");
		nachteil0.setGpKosten(-1.0d);
		nachteilList.add(nachteil0);
		
		Nachteil nachteil1 = new Nachteil();
		nachteil1.setId("NAC-AstraleMed-I");
		nachteil1.setGpKosten(-4.0d);
		addFam = new AdditionsFamilie();
		addFam.setAdditionsID("NAC-AstraleMed");
		addFam.setAdditionsWert(1);
		nachteil1.setAdditionsFamilie(addFam);
		nachteilList.add(nachteil1);
		
		Nachteil nachteil2 = new Nachteil();
		nachteil2.setId("NAC-AstraleMed-II");
		nachteil2.setGpKosten(-4.0d);
		addFam = new AdditionsFamilie();
		addFam.setAdditionsID("NAC-AstraleMed");
		addFam.setAdditionsWert(2);
		nachteil2.setAdditionsFamilie(addFam);
		nachteilList.add(nachteil2);
		
		Nachteil nachteil3 = new Nachteil();
		nachteil3.setId("NAC-AstraleMed-III");
		nachteil3.setGpKosten(-4.0d);
		addFam = new AdditionsFamilie();
		addFam.setAdditionsID("NAC-AstraleMed");
		addFam.setAdditionsWert(3);
		nachteil3.setAdditionsFamilie(addFam);
		nachteilList.add(nachteil3);
		
		storeData.getXmlAccessors().get(0).setNachteilList(nachteilList);
		
		IdLink link1 = new IdLink(rasse, nachteil1, null, Link.KEIN_WERT, null);
		IdLink link2 = new IdLink(rasse, nachteil2, null, Link.KEIN_WERT, null);
		IdLink link3 = new IdLink(rasse, nachteil3, null, Link.KEIN_WERT, null);
		
		// Modis Hinzufügen
		GeneratorLink genLink = (GeneratorLink)  prozessorNachteil.addNewElement(nachteil0);
		assertEquals(-1, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-NixDamitZuTun", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorNachteil.addModi(link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-I", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorNachteil.addModi(link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-II", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorNachteil.addModi(link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-III", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorNachteil.addModi(link1);
		assertEquals(4, genLink.getKosten(),0);
		assertEquals(3, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-III", genLink.getZiel().getId());
		
		// Wieder alle Modis löschen
		
		prozessorNachteil.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-III", genLink.getZiel().getId());
		
		prozessorNachteil.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-II", genLink.getZiel().getId());
		
		prozessorNachteil.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-I", genLink.getZiel().getId());
		
		prozessorNachteil.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertFalse(prozessorNachteil.containsLink(genLink));
		
		// Hinzufügen 2
		genLink = (GeneratorLink) prozessorNachteil.addModi(link2);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-II", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorNachteil.addModi(link3);
		assertEquals(8, genLink.getKosten(),0);
		assertEquals(7, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-III", genLink.getZiel().getId());
		
		// Entfernen 2
		prozessorNachteil.removeModi(genLink, link2);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-III", genLink.getZiel().getId());
		
		prozessorNachteil.removeModi(genLink, link3);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertFalse(prozessorNachteil.containsLink(genLink));
		
		// Hinzufügen 3
		genLink = (GeneratorLink) prozessorNachteil.addModi(link2);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-II", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorNachteil.addModi(link2);
		assertEquals(4, genLink.getKosten(),0);
		assertEquals(3, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-III", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorNachteil.addModi(link1);
		assertEquals(8, genLink.getKosten(),0);
		assertEquals(7, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-III", genLink.getZiel().getId());
		
		// Entfernen & Hinzuügen 2
		prozessorNachteil.removeModi(genLink, link2);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-III", genLink.getZiel().getId());
		
		prozessorNachteil.removeModi(genLink, link2);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-I", genLink.getZiel().getId());
		
		genLink = (GeneratorLink) prozessorNachteil.addModi(link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-II", genLink.getZiel().getId());
		
		prozessorNachteil.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertEquals("NAC-AstraleMed-I", genLink.getZiel().getId());
		
		prozessorNachteil.removeModi(genLink, link1);
		assertEquals(0, genLink.getKosten(),0);
		assertEquals(-1, prozessorNachteil.getGesamtKosten(),0);
		assertFalse(prozessorNachteil.containsLink(genLink));
	}
	
	@Test
	public void testModisOhneStufe() {
		// Test 1: Kosten mit Stufe
		Nachteil nachteil1 = new Nachteil();
		nachteil1.setId("NAC-t1");
		nachteil1.setMinStufe(0);
		nachteil1.setMaxStufe(0);
		nachteil1.setGpKosten(-4.0);
		
		HeldenLink link = prozessorNachteil.addNewElement(nachteil1);
		assertEquals(-4, link.getKosten(),0);
		assertEquals(-4, prozessorNachteil.getGesamtKosten(),0);
		
		// Test 2: Modi hinzufügen
		IdLink linkModi1 = new IdLink(rasse, nachteil1, null, Link.KEIN_WERT, null);
		
		link = prozessorNachteil.addModi(linkModi1);
		assertEquals(0, link.getKosten(),0);
		assertEquals(0, prozessorNachteil.getGesamtKosten(),0);
		
		// Test 3: Modi hinzufügen
		linkModi1 = new IdLink(rasse, nachteil1, null, Link.KEIN_WERT, null);
		
		link = prozessorNachteil.addModi(linkModi1);
		assertEquals(4, link.getKosten(),0);
		assertEquals(4, prozessorNachteil.getGesamtKosten(),0);
		
		// Test 4: Modi wieder löschen
		prozessorNachteil.removeModi((GeneratorLink) link, linkModi1);
		
		assertEquals(0, link.getKosten(),0);
		assertEquals(0, prozessorNachteil.getGesamtKosten(),0);
		
		// Test 5: Modi wieder löschen
		linkModi1 = (IdLink) prozessorNachteil.getElementBox().getObjectById(nachteil1).getLinkModiList().get(0);
		// Test 5: Modi wieder löschen
		prozessorNachteil.removeModi((GeneratorLink) link, linkModi1);
		
		assertEquals(-4, link.getKosten(),0);
		assertEquals(-4, prozessorNachteil.getGesamtKosten(),0);
	}
	
	@Test
	public void testModisOnly() {
		// Vorbereitung
		Nachteil nachteil1 = new Nachteil();
		nachteil1.setId("NAC-StufenKosten");
		nachteil1.setMinStufe(1);
		nachteil1.setMaxStufe(3);
		nachteil1.setKostenProStufe(-4.0d);
		
		// Test 2: Modis hinzufügen
		IdLink linkModi1 = new IdLink(rasse, nachteil1, null, 2, null);
		GeneratorLink link = (GeneratorLink) prozessorNachteil.addModi(linkModi1);
		assertEquals(2, link.getWert());
		assertEquals(0, link.getKosten(),0);
		assertEquals(0, prozessorNachteil.getGesamtKosten(),0);
		assertEquals(2, prozessorNachteil.getMinWert(link));
		assertEquals(3, prozessorNachteil.getMaxWert(link));
		
		// Test 3: Modis hinzufügen
		IdLink linkModi2 = new IdLink(rasse, nachteil1, null, 1, null);
		prozessorNachteil.addModi(linkModi2);
		assertEquals(3, link.getWert());
		assertEquals(0, link.getKosten(),0);
		assertEquals(0, prozessorNachteil.getGesamtKosten(),0);
		
		// Test 4: Modis entfernen
		prozessorNachteil.removeModi(link, linkModi2);
		assertEquals(2, link.getWert(),0);
		assertEquals(0, link.getKosten(),0);
		assertEquals(0, prozessorNachteil.getGesamtKosten(),0);
		assertTrue(prozessorNachteil.getElementBox().contiansEqualObject(link));
		prozessorNachteil.removeModi(link, linkModi1);
		assertFalse(prozessorNachteil.getElementBox().contiansEqualObject(link));
	}
}
