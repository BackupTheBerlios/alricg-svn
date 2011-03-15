/*
 * Created 26.02.2009
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.prozessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.charakter.ElementBox;
import org.d3s.alricg.common.charakter.SonderregelAdmin;
import org.d3s.alricg.common.charakter.VerbilligteFertigkeitAdmin;
import org.d3s.alricg.common.logic.Prozessor;
import org.d3s.alricg.generator.prozessor.charElemente.ProzessorEigenschaften;
import org.d3s.alricg.generator.prozessor.charElemente.ProzessorTalent;
import org.d3s.alricg.generator.prozessor.charElemente.ProzessorVorteil;
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.charElemente.links.OptionVoraussetzung;
import org.d3s.alricg.store.charElemente.links.Voraussetzung;
import org.d3s.alricg.store.held.CharakterDaten;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Vincent
 */
public class VoraussetzungenAdmin_SimpleTest {
	private GeneratorLink linkTalent1, linkTalent2, linkVorteil1, linkVorteil2;
	private Talent talent1, talent2;
	private Vorteil vorteil1, vorteil2;
	private Rasse ras;
	private Charakter charakter;
	private ProzessorDecorator<Vorteil, GeneratorLink> prozessorVorteil;
	private ProzessorDecorator<Talent, GeneratorLink> prozessorTalent;
	private ProzessorDecorator<Eigenschaft, GeneratorLink> prozessorEigenschaft;
	private ElementBox<GeneratorLink> boxEigenschaft;
	private VoraussetzungenGeneratorAdmin voraussAdmin;
	
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Before public void setUp() throws Exception {
		// Charakter erzeugen
		charakter = new Charakter(new CharakterDaten());
		voraussAdmin = new VoraussetzungenGeneratorAdmin(charakter);
		charakter.initCharakterAdmins(
				new SonderregelAdmin(charakter),
				new VerbilligteFertigkeitAdmin(charakter),
				voraussAdmin,
				new GeneratorMagieStatusAdmin(charakter));
		
		// Alle Prozessoren erzeugen
		HashMap<Class, Prozessor> hash = new HashMap<Class, Prozessor>();
		charakter.setProzessorHash(hash);
		hash.put(
				Eigenschaft.class,
				new ProzessorDecorator(charakter, new ProzessorEigenschaften(charakter)));
		hash.put(
				Talent.class,
				new ProzessorDecorator(charakter, new ProzessorTalent(charakter)));
		hash.put(
				Vorteil.class,
				new ProzessorDecorator(charakter, new ProzessorVorteil(charakter)));

		charakter.setProzessorHash(hash);
		prozessorEigenschaft = (ProzessorDecorator) charakter.getProzessor(Eigenschaft.class);
		prozessorTalent = (ProzessorDecorator) charakter.getProzessor(Talent.class);
		prozessorVorteil = (ProzessorDecorator) charakter.getProzessor(Vorteil.class);
		
		// Rassen erzeugen
		ras = new Rasse();
		ras.setId("RAS-test");
		
		// Erzeugen der Talente:
		// Talent 1:
		talent1 = createTalent("Talent 1");
		
		// Talent 2:
		talent2 = createTalent("Talent 2");
		
		// Vorteil 1:
		vorteil1 = new Vorteil();
		vorteil1.setId("VOR-test1");
		vorteil1.setMinStufe(1);
		vorteil1.setMaxStufe(10);
		vorteil1.setKostenProStufe(1.0d);
		
		// SF 2:
		vorteil2 = new Vorteil();
		vorteil2.setId("VOR-test2");
		vorteil2.setMinStufe(1);
		vorteil2.setMaxStufe(10);
		vorteil2.setKostenProStufe(1.0d);
		
		// Elemente zum Helden hinzufügen
		linkTalent1  = prozessorTalent.addNewElement(talent1);
		linkTalent2  = prozessorTalent.addNewElement(talent2);
		linkVorteil1 = prozessorVorteil.addNewElement(vorteil1);
		linkVorteil2 = prozessorVorteil.addNewElement(vorteil1);
		
		boxEigenschaft = prozessorEigenschaft.getElementBox();
	}
	
	/**
	 * Hinzufügen von Talenten, einfache Voraussetzungen
	 */
	@Test
	public void canAddElementPositiv() {
		List<IdLink> listLinks = new ArrayList<IdLink>();
		List<OptionVoraussetzung> listOpVor = new ArrayList<OptionVoraussetzung>();
		
		// Erzeugen eines Talents:
		// Talent A:
		Talent talentA = createTalent("Voraussetzung");
		Voraussetzung vorausA = new Voraussetzung();
		talentA.setVoraussetzung(vorausA);
		IdLink linkA = new IdLink(null, talentA, null, 3, null);
		
		// 1. Mindestens talent1 auf Wert 5
		listLinks.clear();
		listOpVor.clear();
		
		listLinks.add(new IdLink(talentA, talent1, null, 5, null));
		OptionVoraussetzung optVor = new OptionVoraussetzung();
		optVor.setLinkList(listLinks);
		listOpVor.add(optVor);
		vorausA.setPosVoraussetzung(listOpVor);
		
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorTalent.updateWert(linkTalent1, 5);
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorTalent.updateWert(linkTalent1, 4);
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorTalent.updateWert(linkTalent1, 6);
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));

		// 2. Mindestens talent1 auf Wert 5 und talent2 auf Wert 6 (ListLinks)
		listLinks.add(new IdLink(talentA, talent2, null, 6, null));
		
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorTalent.updateWert(linkTalent2, 6);
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorTalent.updateWert(linkTalent1, 2);
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorTalent.updateWert(linkTalent1, 8);
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		
		// 3. Mindestens talent1: 5, talent2: 6, Talent3: 7 + ZweitZiel und Text
		listLinks.clear();
		listOpVor.clear();
		
		listLinks.add(new IdLink(talentA, vorteil1, talent1, 7, "test"));
		OptionVoraussetzung optVor2 = new OptionVoraussetzung();
		optVor2.setLinkList(listLinks);
		listOpVor.add(optVor2);
		vorausA.setPosVoraussetzung(listOpVor);
		
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorVorteil.updateWert(linkVorteil1, 7);
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorVorteil.updateZweitZiel(linkVorteil1, talent1);
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorVorteil.updateText(linkVorteil1, "test");
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorVorteil.updateText(linkVorteil1, "test2");
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorTalent.updateText(linkVorteil1, "test");
		prozessorVorteil.updateZweitZiel(linkVorteil1, talent2);
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorVorteil.updateZweitZiel(linkVorteil1, talent1);
		prozessorVorteil.updateWert(linkVorteil1, 6);
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorVorteil.updateWert(linkVorteil1, 7);
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		
	}
	
	
	@Test
	public void canAddElementNegativ() {
		List<IdLink> listLinks = new ArrayList<IdLink>();
		List<OptionVoraussetzung> listOpVor = new ArrayList<OptionVoraussetzung>();
		
		// Erzeugen eines Talents:
		// Talent A:
		Talent talentA = createTalent("Voraussetzung");
		Voraussetzung vorausA = new Voraussetzung();
		talentA.setVoraussetzung(vorausA);
		IdLink linkA = new IdLink(null, talentA, null, 3, null);
		
		// 1. NICHT talent1 auf Wert 5 oder mehr
		listLinks.add(new IdLink(talentA, talent1, null, 5, null));
		OptionVoraussetzung optVor = new OptionVoraussetzung();
		optVor.setLinkList(listLinks);
		listOpVor.add(optVor);
		vorausA.setNegVoraussetzung(listOpVor);
		
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorTalent.updateWert(linkTalent1, 5);
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorTalent.updateWert(linkTalent1, 4);
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorTalent.updateWert(linkTalent1, 6);
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		
		// 2. talent1 NICHT auf Wert 5+ und talent2 NICHT auf Wert 6+ => talent1 nicht erfüllt
		listLinks.add(new IdLink(talentA, talent2, null, 6, null));
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		
		// 3. talent1 NICHT auf Wert 5+ und talent2 NICHT auf Wert 6+ => Beide nicht erfüllt
		prozessorTalent.updateWert(linkTalent2, 6);
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		
		// 4. talent2 NICHT auf Wert 6+ => Nicht erfüllt
		prozessorTalent.updateWert(linkTalent1, 4);
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));		
		
		// 5. Beide erfüllt
		prozessorTalent.updateWert(linkTalent2, 5);
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		
		// 6. Beide erfüllt
		prozessorTalent.updateWert(linkTalent1, 1);
		prozessorTalent.updateWert(linkTalent2, 1);
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		
		// 7.  talent1 NICHT auf Wert 5+ => Nicht erfüllt
		listLinks.add(new IdLink(talentA, talent1, null, 5, null));
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
	}
	
	
	/**
	 * Kann etwas hinzugefügt werden, dass mit anderen Elementen nicht eineinbar ist?
	 */
	@Test
	public void canAddUnvereinbaresElement() {
		List<IdLink> listLinks = new ArrayList<IdLink>();
		List<OptionVoraussetzung> listOpVor = new ArrayList<OptionVoraussetzung>();
		
		// Erzeugen eines Talents:
		// Talent A:
		Talent talentA = createTalent("Voraussetzung A");
		Voraussetzung vorausA = new Voraussetzung();
		talentA.setVoraussetzung(vorausA);
		IdLink linkA = new IdLink(null, talentA, null, 3, null);
		
		// Talent B:
		Talent talentB = createTalent("Voraussetzung B");
		IdLink linkB = new IdLink(null, talentB, null, 1, null);
		
		// Voraussetzung für Talent 1
		Voraussetzung voraus1 = new Voraussetzung();
		talent1.setVoraussetzung(voraus1);
		
		// 1. Ohne Voraussetzung: kann hinzugefügt werden
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		
		// 2. Mit Voraussetzung: talent1 unvereinbar mit talentA, kann nicht hinzugefügt werden
		listLinks.clear();
		listOpVor.clear();
		
		listLinks.add(new IdLink(talentA, talent1, null, CharElement.KEIN_WERT, null));
		OptionVoraussetzung optVor = new OptionVoraussetzung();
		optVor.setLinkList(listLinks);
		listOpVor.add(optVor);
		vorausA.setNegVoraussetzung(listOpVor);
		
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		
		// 3. talent1 unvereinbar mit talentB, kann nicht hinzugefügt werden
		listLinks = new ArrayList<IdLink>();
		listOpVor = new ArrayList<OptionVoraussetzung>();

		listLinks.add(new IdLink(talent1, talentB, null, CharElement.KEIN_WERT, null));
		optVor = new OptionVoraussetzung();
		optVor.setLinkList(listLinks);
		listOpVor.add(optVor);
		voraus1.setNegVoraussetzung(listOpVor);
		voraussAdmin.addVoraussetzungLink(linkTalent1);
		
		assertFalse(voraussAdmin.changeCanAddElement(false, linkB));
		assertFalse(voraussAdmin.changeCanAddElement(true, linkB));
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		
		// 4. talent1 unvereinbar mit talentA (beide Richtungen) & talentB, kann nicht hinzugefügt werden
		listLinks.add(new IdLink(talent1, talentA, null, CharElement.KEIN_WERT, null));
		
		assertFalse(voraussAdmin.changeCanAddElement(false, linkB));
		assertFalse(voraussAdmin.changeCanAddElement(true, linkB));
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		
		// 5. talent1 nicht mehr unvereinbar mit TalentB
		listLinks.remove(0);
		
		assertFalse(voraussAdmin.changeCanAddElement(false, linkB));
		assertTrue(voraussAdmin.changeCanAddElement(true, linkB));
	}
	
	/**
	 * Ist es möglich ein Element zu entfernen?
	 */
	@Test
	public void canRemoveElement() {
		List<IdLink> listLinks = new ArrayList<IdLink>();
		List<OptionVoraussetzung> listOpVor = new ArrayList<OptionVoraussetzung>();
		
		// Erzeugen eines Talents:
		// Talent A:
		Talent talentA = createTalent("Voraussetzung");
		Voraussetzung vorausA = new Voraussetzung();
		talentA.setVoraussetzung(vorausA);
		IdLink linkA = new IdLink(null, talentA, null, 3, null);
		
		// Talent A: Benötigt talent1
		listLinks.add(new IdLink(talentA, talent1, null, Link.KEIN_WERT, null));
		OptionVoraussetzung optVor = new OptionVoraussetzung();
		optVor.setLinkList(listLinks);
		listOpVor.add(optVor);
		vorausA.setPosVoraussetzung(listOpVor);
		
		// 1. Talent1 kann entfernd werden, da keine abhängigkeiten
		assertFalse(voraussAdmin.changeCanRemoveElement(false, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertTrue(voraussAdmin.changeCanRemoveElement(true, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		
		// 2. Talent A hinzufügen, jetzt kann talent1 NICHT mehr entfernd werden
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		prozessorTalent.addNewElement(talentA);
		
		assertFalse(voraussAdmin.changeCanRemoveElement(false, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertFalse(voraussAdmin.changeCanRemoveElement(true, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		
		// 3. Talent A entfernt, jetzt kann talent1 wieder entfernd werden
		prozessorTalent.removeElement(prozessorTalent.getElementBox().getObjectById(talentA));
		
		assertFalse(voraussAdmin.changeCanRemoveElement(false, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertTrue(voraussAdmin.changeCanRemoveElement(true, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		
		// Talent A: Benötigt talent1 und talent 2
		listLinks.add(new IdLink(talentA, talent2, null, Link.KEIN_WERT, null));

		// Talent A wieder drin, Talent 1 kann entfernt werden, weil talent 2 noch verfügbar
		prozessorTalent.addNewElement(talentA);
		
		assertFalse(voraussAdmin.changeCanRemoveElement(true, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertFalse(voraussAdmin.changeCanRemoveElement(true, this.prozessorTalent.getElementBox().getObjectById(talent2)));

		// Benötigt talent1 ODER talent 2
		optVor.setAnzahl(1); // Einer wird benötigt
		
		assertTrue(voraussAdmin.changeCanRemoveElement(true, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertTrue(voraussAdmin.changeCanRemoveElement(true, this.prozessorTalent.getElementBox().getObjectById(talent2)));

		// Talent 1 entfernen, jetzt kann talent 2 nicht mehr entfernd werden wegen Talent A
		prozessorTalent.removeElement(prozessorTalent.getElementBox().getObjectById(talent1));
		
		assertFalse(voraussAdmin.changeCanRemoveElement(true, this.prozessorTalent.getElementBox().getObjectById(talent2)));
	}
	
	/**
	 * Abhängigkeiten von einem Element zu einem andere mit Text-Wert.
	 */
	@Test
	public void canUpdateText() {
		List<IdLink> listLinks = new ArrayList<IdLink>();
		List<OptionVoraussetzung> listOpVor = new ArrayList<OptionVoraussetzung>();
		
		// Talent A:
		Talent talentA = createTalent("Update-Test A");
		Voraussetzung vorausA = new Voraussetzung();
		talentA.setVoraussetzung(vorausA);
		IdLink linkA = new IdLink(null, talentA, null, 3, null);
		
		// Talent A: Benötigt talent1 mit Wert "Test XXX"
		listLinks.add(new IdLink(talentA, talent1, null, Link.KEIN_WERT, "Test XXX"));
		OptionVoraussetzung optVor = new OptionVoraussetzung();
		optVor.setLinkList(listLinks);
		listOpVor.add(optVor);
		vorausA.setPosVoraussetzung(listOpVor);
		
		// 1. TalentA kann nicht hinzugefügt werden, da der Text fehlt
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		
		// 2. Talent1 kann entfernt werden
		assertFalse(voraussAdmin.changeCanRemoveElement(false, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertTrue(voraussAdmin.changeCanRemoveElement(true, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		
		// 3. Talent1 kann Text geupdated werden
		assertFalse(voraussAdmin.changeCanUpdateText(false, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertTrue(voraussAdmin.changeCanUpdateText(true, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		
		// 4. Talent1 auf richtigen Text ändern nun kann TalentA hinzugefügt werden
		prozessorTalent.updateText(this.prozessorTalent.getElementBox().getObjectById(talent1), "Test XXX");
		
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		
		this.prozessorTalent.addNewElement(talentA);
		
		// 5. Talent1 kann nicht mehr entfernt werden, wird als voraussetzung für TalentA benötigt
		assertFalse(voraussAdmin.changeCanRemoveElement(false, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertFalse(voraussAdmin.changeCanRemoveElement(true, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		
		//6. Talent1 kann nicht mehr geupdated werden
		assertFalse(voraussAdmin.changeCanUpdateText(false, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertFalse(voraussAdmin.changeCanUpdateText(true, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		
		//7. TalentA entfernen, Talent 1 kann wieder geupdated und entfernd werden
		this.prozessorTalent.removeElement(this.prozessorTalent.getElementBox().getObjectById(talentA));
		
		assertTrue(voraussAdmin.changeCanRemoveElement(true, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertTrue(voraussAdmin.changeCanUpdateText(true, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		
		// 8. Talent 1 Text ändern, TalentA könnte nicht mehr hinzugefügt werden
		this.prozessorTalent.updateText(this.prozessorTalent.getElementBox().getObjectById(talent1), null);
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
	}
	
	/**
	 * Abhängigkeiten von einem Element zu einem andere mit Zweitziel.
	 */
	@Test
	public void canUpdateZweitziel() {
		List<IdLink> listLinks = new ArrayList<IdLink>();
		List<OptionVoraussetzung> listOpVor = new ArrayList<OptionVoraussetzung>();
		
		// Vorteil A:
		Vorteil vorteilA = new Vorteil();
		vorteilA.setId("VOR-test-A");
		vorteilA.setMinStufe(1);
		vorteilA.setMaxStufe(10);
		vorteilA.setKostenProStufe(1.0d);
		
		Voraussetzung vorausA = new Voraussetzung();
		vorteilA.setVoraussetzung(vorausA);
		IdLink linkA = new IdLink(null, vorteilA, null, 3, null);
		
		// Vorteil A: Benötigt vorteil1 mit Zweitziel "talent1"
		listLinks.add(new IdLink(vorteilA, vorteil1, talent1, Link.KEIN_WERT, null));
		OptionVoraussetzung optVor = new OptionVoraussetzung();
		optVor.setLinkList(listLinks);
		listOpVor.add(optVor);
		vorausA.setPosVoraussetzung(listOpVor);
		
		// 1. TalentA kann nicht hinzugefügt werden, da das Zweitziel fehlt
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		
		// 2. Talent1 kann entfernt werden
		assertFalse(voraussAdmin.changeCanRemoveElement(false, this.prozessorVorteil.getElementBox().getObjectById(vorteil1)));
		assertTrue(voraussAdmin.changeCanRemoveElement(true, this.prozessorVorteil.getElementBox().getObjectById(vorteil1)));
		
		// 3. Talent1 kann Zweitziel geupdated werden
		assertFalse(voraussAdmin.changeCanUpdateZweitZiel(false, this.prozessorVorteil.getElementBox().getObjectById(vorteil1), vorteil1));
		assertTrue(voraussAdmin.changeCanUpdateZweitZiel(true, this.prozessorVorteil.getElementBox().getObjectById(vorteil1), vorteil1));
		
		// 4. Talent1 auf richtigen Text ändern nun kann TalentA hinzugefügt werden
		prozessorVorteil.updateZweitZiel(this.prozessorVorteil.getElementBox().getObjectById(vorteil1), talent1);
		
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		
		this.prozessorVorteil.addNewElement(vorteilA);
		
		// 5. Talent1 kann nicht mehr entfernt werden, wird als voraussetzung für TalentA benötigt
		assertFalse(voraussAdmin.changeCanRemoveElement(false, this.prozessorVorteil.getElementBox().getObjectById(vorteil1)));
		assertFalse(voraussAdmin.changeCanRemoveElement(true, this.prozessorVorteil.getElementBox().getObjectById(vorteil1)));
		
		//6. Talent1 kann nicht mehr geupdated werden
		assertFalse(voraussAdmin.changeCanUpdateText(false, this.prozessorVorteil.getElementBox().getObjectById(vorteil1)));
		assertFalse(voraussAdmin.changeCanUpdateText(true, this.prozessorVorteil.getElementBox().getObjectById(vorteil1)));
		
		//7. TalentA entfernen, Talent 1 kann wieder geupdated und entfernd werden
		this.prozessorVorteil.removeElement(this.prozessorVorteil.getElementBox().getObjectById(vorteilA));
		
		assertTrue(voraussAdmin.changeCanRemoveElement(true, this.prozessorVorteil.getElementBox().getObjectById(vorteil1)));
		assertTrue(voraussAdmin.changeCanUpdateText(true, this.prozessorVorteil.getElementBox().getObjectById(vorteil1)));
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		
		// 8. Talent 1 Text ändern, TalentA könnte nicht mehr hinzugefügt werden
		this.prozessorVorteil.updateZweitZiel(this.prozessorVorteil.getElementBox().getObjectById(vorteil1), talent2);
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
	}
	
	/**
	 * Ein minimum ist durch eine Voraussetzung angegeben
	 */
	@Test
	public void canChangeMinWert() {
		List<IdLink> listLinks = new ArrayList<IdLink>();
		List<OptionVoraussetzung> listOpVor = new ArrayList<OptionVoraussetzung>();
		
		// Talent A:
		Talent talentA = createTalent("Update-Test A");
		Voraussetzung vorausA = new Voraussetzung();
		talentA.setVoraussetzung(vorausA);
		IdLink linkA = new IdLink(null, talentA, null, 3, null);
		
		// Talent A: Benötigt talent1 mit Wert "Test XXX"
		listLinks.add(new IdLink(talentA, talent1, null, 5, null));
		OptionVoraussetzung optVor = new OptionVoraussetzung();
		optVor.setLinkList(listLinks);
		listOpVor.add(optVor);
		vorausA.setPosVoraussetzung(listOpVor);
		
		// 1. TalentA kann nicht hinzugefügt werden, da talent1 nicht auf 5
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		
		this.prozessorTalent.updateWert(this.prozessorTalent.getElementBox().getObjectById(talent1), 7);
		
		// 2. TalentA kann hinzugefügt werden, da talent1 auf 7
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		
		this.prozessorTalent.addNewElement(talentA);

		// 3. Nach oben normal, aber nach unten eine Grenze von 5
		assertEquals(10, voraussAdmin.changeMaxWert(10, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertEquals(5, voraussAdmin.changeMinWert(3, this.prozessorTalent.getElementBox().getObjectById(talent1)));

		assertEquals(5, voraussAdmin.changeMinWert(0, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertEquals(5, voraussAdmin.changeMinWert(4, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertEquals(5, voraussAdmin.changeMinWert(5, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertEquals(6, voraussAdmin.changeMinWert(6, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertEquals(7, voraussAdmin.changeMinWert(7, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		
		assertFalse(voraussAdmin.changeCanRemoveElement(true, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		
		// 4. wieder entfernen, 
		this.prozessorTalent.removeElement(this.prozessorTalent.getElementBox().getObjectById(talentA));
		
		assertEquals(10, voraussAdmin.changeMaxWert(10, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertEquals(3, voraussAdmin.changeMinWert(3, this.prozessorTalent.getElementBox().getObjectById(talent1)));
	}
	
	/**
	 * Ein Maximum ist durch eine Voraussetzung angegeben
	 */
	@Test
	public void canChangeMaxWert() {
		List<IdLink> listLinks = new ArrayList<IdLink>();
		List<OptionVoraussetzung> listOpVor = new ArrayList<OptionVoraussetzung>();
		
		// Talent A:
		Talent talentA = createTalent("Update-Test A");
		Voraussetzung vorausA = new Voraussetzung();
		talentA.setVoraussetzung(vorausA);
		IdLink linkA = new IdLink(null, talentA, null, 3, null);
		
		// Talent A: Benötigt talent1 mit Wert KLEINER als 5
		listLinks.add(new IdLink(talentA, talent1, null, 5, null));
		OptionVoraussetzung optVor = new OptionVoraussetzung();
		optVor.setLinkList(listLinks);
		listOpVor.add(optVor);
		vorausA.setNegVoraussetzung(listOpVor);
		
		// 1. TalentA kann nicht hinzugefügt werden, da talent1 weniger als 5
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		
		// 2. TalentA kann nicht hinzugefügt werden, da talent1 auf 7
		this.prozessorTalent.updateWert(this.prozessorTalent.getElementBox().getObjectById(talent1), 7);
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertFalse(voraussAdmin.changeCanAddElement(true, linkA));
		
		// 3. TalentA kann hinzugefügt werden, da talent1 genau 4
		this.prozessorTalent.updateWert(this.prozessorTalent.getElementBox().getObjectById(talent1), 4);
		assertFalse(voraussAdmin.changeCanAddElement(false, linkA));
		assertTrue(voraussAdmin.changeCanAddElement(true, linkA));
		
		this.prozessorTalent.addNewElement(talentA);

		// 3. Nach oben normal, aber nach unten eine Grenze von 5
		assertEquals(5, voraussAdmin.changeMaxWert(10, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertEquals(3, voraussAdmin.changeMinWert(3, this.prozessorTalent.getElementBox().getObjectById(talent1)));

		assertEquals(0, voraussAdmin.changeMaxWert(0, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertEquals(4, voraussAdmin.changeMaxWert(4, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertEquals(5, voraussAdmin.changeMaxWert(5, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertEquals(5, voraussAdmin.changeMaxWert(6, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertEquals(5, voraussAdmin.changeMaxWert(7, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		
		assertTrue(voraussAdmin.changeCanRemoveElement(true, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		
		// 4. wieder entfernen, 
		this.prozessorTalent.removeElement(this.prozessorTalent.getElementBox().getObjectById(talentA));
		
		assertEquals(10, voraussAdmin.changeMaxWert(10, this.prozessorTalent.getElementBox().getObjectById(talent1)));
		assertEquals(3, voraussAdmin.changeMinWert(3, this.prozessorTalent.getElementBox().getObjectById(talent1)));
	}
	
	/**
	 * Ein Charelement mit bestimmten Text / Zweitziel ist verboten. Geht Add/ Update? 
	 */
	
	
	
	private Talent createTalent(String ID) {
		Talent talent = new Talent();
		talent.setId("TAL-test-" + ID);
		talent.setDreiEigenschaften(
				new Eigenschaft[]  {
						EigenschaftEnum.MU.getEigenschaft(),
						EigenschaftEnum.KL.getEigenschaft(),
						EigenschaftEnum.GE.getEigenschaft()
				});
		talent.setKostenKlasse(KostenKlasse.A);
		talent.setArt(Talent.Art.spezial);
		talent.setSorte(Talent.Sorte.koerper);
		talent.setName("test-" + ID);
		
		return talent;
	}
	
}
