/*
 * Created 26.10.2008
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.prozessor.charElemente;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;

import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.charakter.SonderregelAdmin;
import org.d3s.alricg.common.charakter.VerbilligteFertigkeitAdmin;
import org.d3s.alricg.common.charakter.CharStatusAdmin.GeweihtStatus;
import org.d3s.alricg.common.logic.Prozessor;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.GeneratorMagieStatusAdmin;
import org.d3s.alricg.generator.prozessor.ProzessorDecorator;
import org.d3s.alricg.generator.prozessor.VoraussetzungenGeneratorAdmin;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorLiturgie;
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Gottheit.GottheitArt;
import org.d3s.alricg.store.charElemente.Liturgie.LiturgieArt;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;
import org.d3s.alricg.store.held.CharakterDaten;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Vincent
 *
 */
public class ProzessorLiturgieTest {
	private Talent talLitKenntnis;
	private GeneratorLink<Talent> linkLitKenntnis;
	private Rasse rasse;
	private Charakter charakter;
	private Gottheit gottA, gottB, gottC, gottD;
	private GeneratorMagieStatusAdmin statusAdmin;
	private ProzessorDecorator<Liturgie, GeneratorLink> prozessorLiturgie;
	private ProzessorDecorator<Talent, GeneratorLink> prozessorTalent;
	private ProzessorDecorator<Eigenschaft, GeneratorLink> prozessorEigenschaft;
	//private ProzessorDecorator<Sonderfertigkeit, GeneratorLink> prozessorSonderfertigkeit;
	
	
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
		statusAdmin = new GeneratorMagieStatusAdmin(charakter);
		statusAdmin.setGeweihtStatus(GeweihtStatus.nichtGeweiht);
		charakter.initCharakterAdmins(
				new SonderregelAdmin(charakter),
				new VerbilligteFertigkeitAdmin(charakter),
				new VoraussetzungenGeneratorAdmin(charakter),
				statusAdmin);
		
		// Alle Prozessoren erzeugen
		HashMap<Class, Prozessor> hash = new HashMap<Class, Prozessor>();
		hash.put(
				Eigenschaft.class,
				new ProzessorDecorator(charakter, new ProzessorEigenschaften(charakter)));
		hash.put(
				Liturgie.class,
				new ProzessorDecorator(charakter, new ProzessorLiturgie(charakter)));
		hash.put(
				Talent.class,
				new ProzessorDecorator(charakter, new ProzessorTalent(charakter)));
		//hash.put(
		//		Sonderfertigkeit.class,
		//		new ProzessorDecorator(charakter, new ProzessorSonderfertigkeit(charakter)));
		
		charakter.setProzessorHash(hash);
		prozessorEigenschaft = (ProzessorDecorator) charakter.getProzessor(Eigenschaft.class);
		prozessorLiturgie = (ProzessorDecorator) charakter.getProzessor(Liturgie.class);
		prozessorTalent = (ProzessorDecorator) charakter.getProzessor(Talent.class);
		//prozessorSonderfertigkeit = (ProzessorDecorator) charakter.getProzessor(Sonderfertigkeit.class);
		
		// Erzeugen des Talentes für Liturgiekenntnis:
		talLitKenntnis = new Talent();
		talLitKenntnis.setId(ProzessorLiturgie.LITURGIEKENNTNIS_ID);
		talLitKenntnis.setDreiEigenschaften(
				new Eigenschaft[]  {
						EigenschaftEnum.MU.getEigenschaft(),
						EigenschaftEnum.KL.getEigenschaft(),
						EigenschaftEnum.GE.getEigenschaft()
				});
		talLitKenntnis.setKostenKlasse(KostenKlasse.A);
		talLitKenntnis.setArt(Talent.Art.spezial);
		talLitKenntnis.setSorte(Talent.Sorte.koerper);
		talLitKenntnis.setName("Liturgiekenntnis");
		
		// Erzeugen der Links
		linkLitKenntnis = prozessorTalent.addNewElement(talLitKenntnis);
		
		// Gottheiten erstellen
		gottA = new Gottheit();
		gottA.setId("GOT-A");
		gottA.setName("Gottheit A");
		gottA.setGottheitArt(GottheitArt.zwoelfGoettlich);

		gottB = new Gottheit();
		gottB.setId("GOT-B");
		gottB.setName("Gottheit B");
		gottB.setGottheitArt(GottheitArt.alveranNah);
		
		gottC = new Gottheit();
		gottC.setId("GOT-C");
		gottC.setName("Gottheit C");
		gottC.setGottheitArt(GottheitArt.animistisch);
		
		gottD = new Gottheit();
		gottD.setId("GOT-D");
		gottD.setName("Gottheit D");
		gottD.setGottheitArt(GottheitArt.animistisch);
		
		// Rassen erzeugen
		rasse = new Rasse();
		rasse.setId("RAS-test");
	}
	
	
	@Test
	public void testCanAdd() {
		// Vorbereiten
		Liturgie liturgieA = new Liturgie();
		liturgieA.setId("LIT-A");
		liturgieA.setArt(LiturgieArt.allgemein);
		liturgieA.setGottheit(new Gottheit[] {gottA} );
		liturgieA.setGrad(new int[] {1, 2, 5});
		
		Liturgie liturgieB = new Liturgie();
		liturgieB.setId("LIT-B");
		liturgieB.setArt(LiturgieArt.speziell);
		liturgieB.setGottheit(new Gottheit[] {gottA, gottB} );
		liturgieB.setGrad(new int[] {6});
		
		Liturgie liturgieC = new Liturgie();
		liturgieC.setId("LIT-C");
		liturgieC.setArt(LiturgieArt.hochschamanen);
		liturgieC.setGottheit(new Gottheit[] {gottC} );
		liturgieC.setGrad(new int[] {1, 2, 5});
		
		// Nicht der nötige Geweiht-Status!
		assertFalse(prozessorLiturgie.canAddElement(liturgieA));
		assertFalse(prozessorLiturgie.canAddElement(liturgieB));
		assertFalse(prozessorLiturgie.canAddElement(liturgieC)); 
		
		statusAdmin.setGeweihtStatus(GeweihtStatus.vollgeweiht);
		statusAdmin.addGottheit(gottD);
		
		// Nicht nötige Liturgie-Kenntnis
		assertFalse(prozessorLiturgie.canAddElement(liturgieA));
		assertFalse(prozessorLiturgie.canAddElement(liturgieB));
		assertFalse(prozessorLiturgie.canAddElement(liturgieC)); 
		
		prozessorTalent.updateWert(linkLitKenntnis, 6);
		
		// Falscher Gott, aber A ist "allgemein", C: Richtiger Gott aber kein Hochschamene
		assertTrue(prozessorLiturgie.canAddElement(liturgieA)); 
		assertFalse(prozessorLiturgie.canAddElement(liturgieB));
		assertFalse(prozessorLiturgie.canAddElement(liturgieC)); 
		
		statusAdmin.setGeweihtStatus(GeweihtStatus.hochschamane);
		
		// Jetzt Hochschamane, daher geht C
		assertTrue(prozessorLiturgie.canAddElement(liturgieA)); 
		assertFalse(prozessorLiturgie.canAddElement(liturgieB));
		assertTrue(prozessorLiturgie.canAddElement(liturgieC)); 
		
		statusAdmin.setGeweihtStatus(GeweihtStatus.vollgeweiht);
		statusAdmin.removeGottheit(gottC);
		statusAdmin.addGottheit(gottA);
		
		// Richtiger Gott für A & B, für B zu niedrige Litrugiekenntnis
		assertTrue(prozessorLiturgie.canAddElement(liturgieA)); 
		assertFalse(prozessorLiturgie.canAddElement(liturgieB));
		assertFalse(prozessorLiturgie.canAddElement(liturgieC)); 
		
		prozessorTalent.updateWert(linkLitKenntnis, 18);
		
		// Richtiger Gott für A & B
		assertTrue(prozessorLiturgie.canAddElement(liturgieA)); 
		assertTrue(prozessorLiturgie.canAddElement(liturgieB));
		assertFalse(prozessorLiturgie.canAddElement(liturgieC)); 
	}
	
	@Test
	public void testAddRemove() {
		// Vorbereiten
		Liturgie liturgieA = new Liturgie();
		liturgieA.setId("LIT-A");
		liturgieA.setArt(LiturgieArt.allgemein);
		liturgieA.setGottheit(new Gottheit[] {gottA} );
		liturgieA.setGrad(new int[] {1, 2, 5});
		
		Liturgie liturgieB = new Liturgie();
		liturgieB.setId("LIT-B");
		liturgieB.setArt(LiturgieArt.speziell);
		liturgieB.setGottheit(new Gottheit[] {gottA, gottB} );
		liturgieB.setGrad(new int[] {2, 6});
		
		Liturgie liturgieC = new Liturgie();
		liturgieC.setId("LIT-C");
		liturgieC.setArt(LiturgieArt.allgemein);
		liturgieC.setGottheit(new Gottheit[] {gottC, gottB} );
		liturgieC.setGrad(new int[] {1, 2, 5});
		
		statusAdmin.setGeweihtStatus(GeweihtStatus.vollgeweiht);
		statusAdmin.addGottheit(gottB);
		prozessorTalent.updateWert(linkLitKenntnis, 6);
		
		// Add Liturgien
		GeneratorLink link = prozessorLiturgie.addNewElement(liturgieB);
		assertEquals(2, link.getWert());
		assertEquals(100, link.getKosten());
		assertEquals(100, prozessorLiturgie.getGesamtKosten());
		
		link = prozessorLiturgie.addNewElement(liturgieA);
		assertEquals(1, link.getWert());
		assertEquals(100, link.getKosten()); // Weil andere Gottheit = wie ein Grad höher
		assertEquals(200, prozessorLiturgie.getGesamtKosten());
		
		assertEquals(2, prozessorLiturgie.getElementBox().size());
		
		// Entfernen von Litgurgien
		prozessorLiturgie.removeElement(link);
		
		assertEquals(100, prozessorLiturgie.getGesamtKosten());
		assertEquals(1, prozessorLiturgie.getElementBox().size());
		
		// Neue Litgurie erneut hinzufügen
		link = prozessorLiturgie.addNewElement(liturgieC);
		assertEquals(1, link.getWert());
		assertEquals(50, link.getKosten()); // Weil andere Gottheit = wie ein Grad höher
		assertEquals(150, prozessorLiturgie.getGesamtKosten());
		
		assertEquals(2, prozessorLiturgie.getElementBox().size());
		
		// Alle entfernen
		link = prozessorLiturgie.getElementBox().getObjectById(liturgieB);
		prozessorLiturgie.removeElement(link);
		link = prozessorLiturgie.getElementBox().getObjectById(liturgieC);
		prozessorLiturgie.removeElement(link);
		
		assertEquals(0, prozessorLiturgie.getGesamtKosten());
		assertEquals(0, prozessorLiturgie.getElementBox().size());
	}
	
	@Test
	public void testMinMaxWerte() {
		// Vorbereiten
		Liturgie liturgieA = new Liturgie();
		liturgieA.setId("LIT-A");
		liturgieA.setArt(LiturgieArt.allgemein);
		liturgieA.setGottheit(new Gottheit[] {gottA} );
		liturgieA.setGrad(new int[] {1, 2, 5});
		
		Liturgie liturgieB = new Liturgie();
		liturgieB.setId("LIT-B");
		liturgieB.setArt(LiturgieArt.speziell);
		liturgieB.setGottheit(new Gottheit[] {gottA, gottB} );
		liturgieB.setGrad(new int[] {2, 6});
		
		Liturgie liturgieC = new Liturgie();
		liturgieC.setId("LIT-C");
		liturgieC.setArt(LiturgieArt.allgemein);
		liturgieC.setGottheit(new Gottheit[] {gottC, gottB} );
		liturgieC.setGrad(new int[] {1, 3, 5, 8});
		
		statusAdmin.setGeweihtStatus(GeweihtStatus.vollgeweiht);
		statusAdmin.addGottheit(gottB);
		prozessorTalent.updateWert(linkLitKenntnis, 3);
		
		// Hinzufügen der Liturgien
		GeneratorLink linkA = prozessorLiturgie.addNewElement(liturgieA);
		GeneratorLink linkB = prozessorLiturgie.addNewElement(liturgieB);
		GeneratorLink linkC = prozessorLiturgie.addNewElement(liturgieC);
		
		// Testen
		assertEquals(1, prozessorLiturgie.getMaxWert(linkA));
		assertEquals(0, prozessorLiturgie.getMaxWert(linkB));
		assertEquals(1, prozessorLiturgie.getMaxWert(linkC));
		assertEquals(1, prozessorLiturgie.getMinWert(linkA));
		assertEquals(0, prozessorLiturgie.getMinWert(linkB));
		assertEquals(1, prozessorLiturgie.getMinWert(linkC));
		
		// LitKentnis ändern
		prozessorTalent.updateWert(linkLitKenntnis, 7);
		
		assertEquals(2, prozessorLiturgie.getMaxWert(linkA));
		assertEquals(2, prozessorLiturgie.getMaxWert(linkB));
		assertEquals(1, prozessorLiturgie.getMaxWert(linkC));
		assertEquals(1, prozessorLiturgie.getMinWert(linkA));
		assertEquals(2, prozessorLiturgie.getMinWert(linkB));
		assertEquals(1, prozessorLiturgie.getMinWert(linkC));
		
		// LitKentnis ändern
		prozessorTalent.updateWert(linkLitKenntnis, 10);
		
		assertEquals(2, prozessorLiturgie.getMaxWert(linkA));
		assertEquals(2, prozessorLiturgie.getMaxWert(linkB));
		assertEquals(3, prozessorLiturgie.getMaxWert(linkC));
		assertEquals(1, prozessorLiturgie.getMinWert(linkA));
		assertEquals(2, prozessorLiturgie.getMinWert(linkB));
		assertEquals(1, prozessorLiturgie.getMinWert(linkC));
		
		// LitKentnis ändern
		prozessorTalent.updateWert(linkLitKenntnis, 24);
		
		assertEquals(5, prozessorLiturgie.getMaxWert(linkA));
		assertEquals(6, prozessorLiturgie.getMaxWert(linkB));
		assertEquals(8, prozessorLiturgie.getMaxWert(linkC));
		assertEquals(1, prozessorLiturgie.getMinWert(linkA));
		assertEquals(2, prozessorLiturgie.getMinWert(linkB));
		assertEquals(1, prozessorLiturgie.getMinWert(linkC));
		
		// LitKentnis ändern
		prozessorTalent.updateWert(linkLitKenntnis, 15);
		
		assertEquals(5, prozessorLiturgie.getMaxWert(linkA));
		assertEquals(2, prozessorLiturgie.getMaxWert(linkB));
		assertEquals(5, prozessorLiturgie.getMaxWert(linkC));
		assertEquals(1, prozessorLiturgie.getMinWert(linkA));
		assertEquals(2, prozessorLiturgie.getMinWert(linkB));
		assertEquals(1, prozessorLiturgie.getMinWert(linkC));
	}
	
	@Test
	public void testMoeglicheWerte() {
		// Vorbereiten
		ExtendedProzessorLiturgie extPro = (ExtendedProzessorLiturgie) prozessorLiturgie.getExtendedInterface();
		
		Liturgie liturgieA = new Liturgie();
		liturgieA.setId("LIT-A");
		liturgieA.setArt(LiturgieArt.allgemein);
		liturgieA.setGottheit(new Gottheit[] {gottA} );
		liturgieA.setGrad(new int[] {1, 2, 5});
		
		Liturgie liturgieB = new Liturgie();
		liturgieB.setId("LIT-B");
		liturgieB.setArt(LiturgieArt.speziell);
		liturgieB.setGottheit(new Gottheit[] {gottA, gottB} );
		liturgieB.setGrad(new int[] {2, 6});
		
		Liturgie liturgieC = new Liturgie();
		liturgieC.setId("LIT-C");
		liturgieC.setArt(LiturgieArt.allgemein);
		liturgieC.setGottheit(new Gottheit[] {gottC, gottB} );
		liturgieC.setGrad(new int[] {1, 3, 5, 8});
		
		statusAdmin.setGeweihtStatus(GeweihtStatus.vollgeweiht);
		statusAdmin.addGottheit(gottB);
		prozessorTalent.updateWert(linkLitKenntnis, 3);
		
		// Hinzufügen der Liturgien
		GeneratorLink linkA = prozessorLiturgie.addNewElement(liturgieA);
		GeneratorLink linkB = prozessorLiturgie.addNewElement(liturgieB);
		GeneratorLink linkC = prozessorLiturgie.addNewElement(liturgieC);
		
		// Testen
		assertArrayEquals(new Integer[] {1}, extPro.getPossibleLiturgieWerte(linkA));
		assertNull(extPro.getPossibleLiturgieWerte(linkB));
		assertArrayEquals(new Integer[] {1}, extPro.getPossibleLiturgieWerte(linkC));
		
		// LitKentnis ändern
		prozessorTalent.updateWert(linkLitKenntnis, 7);
		
		assertArrayEquals(new Integer[] {1 , 2}, extPro.getPossibleLiturgieWerte(linkA));
		assertArrayEquals(new Integer[] {2}, extPro.getPossibleLiturgieWerte(linkB));
		assertArrayEquals(new Integer[] {1}, extPro.getPossibleLiturgieWerte(linkC));
		
		// LitKentnis ändern
		prozessorTalent.updateWert(linkLitKenntnis, 24);
		
		assertArrayEquals(new Integer[] {1 , 2, 5}, extPro.getPossibleLiturgieWerte(linkA));
		assertArrayEquals(new Integer[] {2, 6}, extPro.getPossibleLiturgieWerte(linkB));
		assertArrayEquals(new Integer[] {1, 3, 5, 8}, extPro.getPossibleLiturgieWerte(linkC));
		
		// Liturgie ändern
		liturgieC.setArt(LiturgieArt.hochschamanen);
		
		assertNull(extPro.getPossibleLiturgieWerte((Liturgie) linkC.getZiel()));
	}
}
