/*
 * Created on 02.07.2005 / 01:28:14
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.generator.prozessor.charElemente;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.charakter.ElementBox;
import org.d3s.alricg.common.charakter.SonderregelAdmin;
import org.d3s.alricg.common.charakter.VerbilligteFertigkeitAdmin;
import org.d3s.alricg.common.logic.Prozessor;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.GeneratorMagieStatusAdmin;
import org.d3s.alricg.generator.prozessor.ProzessorDecorator;
import org.d3s.alricg.generator.prozessor.VoraussetzungenGeneratorAdmin;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorEigenschaft;
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.held.CharakterDaten;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;


public class ProzessorEigenschaftenTest {
    private Charakter charakter;
	
    private ProzessorDecorator<Eigenschaft, GeneratorLink> prozessor;
    private ProzessorDecorator<Talent, GeneratorLink> prozessorTalent;
    private ElementBox<GeneratorLink> box, boxTalent;
    private Kultur k;
    private Profession p;

    private IdLink link, link2;

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
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
    
    @Before public void setUp() throws Exception {
    	
        // initialisieren
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
				Talent.class,
				new ProzessorDecorator(charakter, new ProzessorTalent(charakter)));
		hash.put(
				Zauber.class,
				new ProzessorDecorator(charakter, new ProzessorZauber(charakter)));

		charakter.setProzessorHash(hash);
		prozessor = (ProzessorDecorator) charakter.getProzessor(Eigenschaft.class);
		prozessorTalent = (ProzessorDecorator) charakter.getProzessor(Talent.class);
        
        box = prozessor.getElementBox();
        boxTalent = prozessorTalent.getElementBox();
        
        k = new Kultur();
        k.setId("KUL-jUnit-test-eigen");
        p = new Profession();
        p.setId("PROF-jUnit-test-eigen");
    }
    
    /**
     * Liefert den Link zu der Eigenschaft zurück
     * 
     * @param enu Die gerwünschte Eigenschaft
     * @return Der Link von ProzessorXX zu der Eigenschaft
     */
    private GeneratorLink getLink(EigenschaftEnum enu) {

        return box.getObjectById(enu.getId());

    }

    /*
     * Testet der Basis Funktionen: Das Setzen der Eigenschaften und die Berechnung der Kosten!
     */
    @Test public void testUpdateStufe() {

        prozessor.updateWert(getLink(EigenschaftEnum.KO), 10);
        prozessor.updateWert(getLink(EigenschaftEnum.KK), 11);
        prozessor.updateWert(getLink(EigenschaftEnum.MU), 9);
        prozessor.updateWert(getLink(EigenschaftEnum.IN), 12);
        prozessor.updateWert(getLink(EigenschaftEnum.CH), 14);
        
        prozessor.updateWert(getLink(EigenschaftEnum.ASP), 2);
        prozessor.updateWert(getLink(EigenschaftEnum.LEP), 5);
        
        prozessor.updateWert(getLink(EigenschaftEnum.SO), 2);

        // "Nicht Berechnete" Werte überprüfen
        assertEquals(10, charakter.getEigenschaftsWert(EigenschaftEnum.KO));
        assertEquals(11, charakter.getEigenschaftsWert(EigenschaftEnum.KK));
        assertEquals(9, charakter.getEigenschaftsWert(EigenschaftEnum.MU));
        assertEquals(12, charakter.getEigenschaftsWert(EigenschaftEnum.IN));
        assertEquals(14, charakter.getEigenschaftsWert(EigenschaftEnum.CH));

        // Errechnete Werte überprüfen:
        assertEquals( // (10 + 10 + 11) / 2 = 16 + 5 = 21
                21, charakter.getEigenschaftsWert(EigenschaftEnum.LEP));
        assertEquals( // (9 + 12 + 14) / 2 = 18 + 2 = 20
                20, charakter.getEigenschaftsWert(EigenschaftEnum.ASP));

        // Überprüfen ob die Kosten richtig sind:
        assertEquals( // Stufe = Kosten = 10
                10, getLink(EigenschaftEnum.KO).getKosten(),0);
        assertEquals( // Stufe = Kosten = 11
                11, getLink(EigenschaftEnum.KK).getKosten(),0);
        assertEquals( // Stufe = Kosten = 9
                9, getLink(EigenschaftEnum.MU).getKosten(),0);
        assertEquals( // Stufe = Kosten = 2
                2, getLink(EigenschaftEnum.SO).getKosten(),0);
        assertEquals( // Über SKT - Nur die gekauften Punkte
                306, getLink(EigenschaftEnum.LEP).getKosten(),0);
        assertEquals( // Über SKT - Nur die gekauften Punkte
                26, getLink(EigenschaftEnum.ASP).getKosten(),0);

        // MU = 9 / KL = 8 / IN = 12 / GE = 8 / CH = 14 / FF = 8 / KK = 11 / KO = 10
        // SO = 2
        assertEquals(82, 
        		prozessor.getGesamtKosten(),0);

        // LEP = 306 / ASP = 26 / MR = 0
        assertEquals(332, // GesamtKosten
        		((ExtendedProzessorEigenschaft) prozessor.getExtendedInterface()).getGesamtTalentGpKosten());

        // Sonstiges: Sollte nix kosten!
        assertEquals( 0, getLink(EigenschaftEnum.MR).getKosten(),0);

        // Max Wert IN
        assertEquals( 12, prozessor.getMaxWert(getLink(EigenschaftEnum.KA)));
    }

    /*
     * Testet mehrer Operationen auf der Eigenschaft Mut (Setzen der Eigenschaft, Kosten, Modis)
     */
    @Test public void testMU() {
        // ------------- Test der Eigenschaft MU
        // Kultur setzen
        link = new IdLink(k, EigenschaftEnum.MU.getEigenschaft(), null, 2, null);

        prozessor.updateWert(getLink(EigenschaftEnum.MU), 10);
        prozessor.addModi(link);

        assertEquals( // Max Wert: 14 + 2 = 16
                16, prozessor.getMaxWert(getLink(EigenschaftEnum.MU)));
        assertEquals( // Max Wert: 8 + 2 = 10
                10, prozessor.getMinWert(getLink(EigenschaftEnum.MU)));
        assertEquals( // Gesamtstufe
                10, getLink(EigenschaftEnum.MU).getWert());
        assertEquals( // User Gewähhlt
                8, getLink(EigenschaftEnum.MU).getUserLink().getWert());
        assertEquals( // Modis
                2, getLink(EigenschaftEnum.MU).getWertModis());
        assertEquals( // Kosten
                8, getLink(EigenschaftEnum.MU).getKosten(),0);

        // Neu setzen der Stufe
        prozessor.updateWert(getLink(EigenschaftEnum.MU), 16);

        assertEquals( // Max Wert: 14 + 2 = 16
                16, prozessor.getMaxWert(getLink(EigenschaftEnum.MU)));
        assertEquals( // Max Wert: 8 + 2 = 10
                10, prozessor.getMinWert(getLink(EigenschaftEnum.MU)));
        assertEquals( // Gesamtstufe
                16, getLink(EigenschaftEnum.MU).getWert());
        assertEquals( // User Gewähhlt
                14, getLink(EigenschaftEnum.MU).getUserLink().getWert());
        assertEquals( // Modis
                2, getLink(EigenschaftEnum.MU).getWertModis());
        assertEquals( // Kosten
                14, getLink(EigenschaftEnum.MU).getKosten(),0);

        // MU = 14 / KL = 8 / IN = 8 / GE = 8 / CH = 8 / FF = 8 / KK = 8 / KO = 8
        // SO = 1
        assertEquals(71, // GesamtKosten
                charakter.getProzessor(Eigenschaft.class).getGesamtKosten(),0);

    }

    /*
     * Testet mehrer Operationen auf der Eigenschaft Klugheit (Setzen der Eigenschaft, Kosten, Modis durch 2x Herkunft)
     */
    @Test public void testKL() {
        // Kultur setzen
        link = new IdLink(k, EigenschaftEnum.KL.getEigenschaft(), null, 2, null);

        // Profession setzen
        link2 = new IdLink(p, EigenschaftEnum.KL.getEigenschaft(), null, 2, null);

        prozessor.updateWert(getLink(EigenschaftEnum.KL), 10);
        prozessor.addModi(link);
        prozessor.addModi(link2);

        assertEquals( // Max Wert: 14 + 2 + 2 = 18
                18, prozessor.getMaxWert(getLink(EigenschaftEnum.KL)));
        assertEquals( // Min Wert: 8 + 2 + 2 = 12
                12, prozessor.getMinWert(getLink(EigenschaftEnum.KL)));
        assertEquals( // Gesamtstufe (12 ist der kleinste mögliche Wert)
                12, getLink(EigenschaftEnum.KL).getWert());
        assertEquals( // User Gewählt (Automatisch auf 8 gesetzt)
                8, getLink(EigenschaftEnum.KL).getUserLink().getWert());
        assertEquals( // Modis (Kultur 2 / Profession 2)
                4, getLink(EigenschaftEnum.KL).getWertModis());
        assertEquals( // Kosten = Gewählte Punkte
                8, getLink(EigenschaftEnum.KL).getKosten(),0);

        // Entfernen eines Modis
        prozessor.removeModi(getLink(EigenschaftEnum.KL), link2);

        assertEquals( // Max Wert: 14 + 2 = 16
                16, prozessor.getMaxWert(getLink(EigenschaftEnum.KL)));
        assertEquals( // Min Wert: 8 + 2 = 10
                10, prozessor.getMinWert(getLink(EigenschaftEnum.KL)));
        assertEquals( // Gesamtstufe
                10, getLink(EigenschaftEnum.KL).getWert());
        assertEquals( // User Gewählt (Automatisch auf 8 gesetzt)
                8, getLink(EigenschaftEnum.KL).getUserLink().getWert());
        assertEquals( // Modis (Kultur 2 / Profession 2)
                2, getLink(EigenschaftEnum.KL).getWertModis());
        assertEquals( // Kosten = Gewählte Punkte
                8, getLink(EigenschaftEnum.KL).getKosten(),0);

        // MU = 8 / KL = 8 / IN = 8 / GE = 8 / CH = 8 / FF = 8 / KK = 8 / KO = 8 / SO = 1
        assertEquals(65, // GesamtKosten
        		charakter.getProzessor(Eigenschaft.class).getGesamtKosten(),0);

        // Wieder hinzufügen des Modis
        prozessor.addModi(link2);

        assertEquals( // Max Wert: 14 + 2 + 2 = 18
                18, prozessor.getMaxWert(getLink(EigenschaftEnum.KL)));
        assertEquals( // Min Wert: 8 + 2 + 2 = 12
                12, prozessor.getMinWert(getLink(EigenschaftEnum.KL)));
        assertEquals( // Gesamtstufe (12 ist der kleinste mögliche Wert)
                12, getLink(EigenschaftEnum.KL).getWert());
        assertEquals( // User Gewählt (Automatisch auf 8 gesetzt)
                8, getLink(EigenschaftEnum.KL).getUserLink().getWert());
        assertEquals( // Modis (Kultur 2 / Profession 2)
                4, getLink(EigenschaftEnum.KL).getWertModis());
        assertEquals( // Kosten = Gewählte Punkte
                8, getLink(EigenschaftEnum.KL).getKosten(),0);

        // MU = 8 / KL = 8 / IN = 8 / GE = 8 / CH = 8 / FF = 8 / KK = 8 / KO = 8 / SO = 1
        assertEquals(65, // GesamtKosten
        		charakter.getProzessor(Eigenschaft.class).getGesamtKosten(),0);
    }

    /*
     * Testet mehrer Operationen auf der Berechneten Eigenschaft Lep (Setzen der Eigenschaft, Kosten, Modis)
     */
    @Test public void testLEP() {
        // Setzen eines Modis durch eine Kultur
        link = new IdLink(k, EigenschaftEnum.LEP.getEigenschaft(), null, 4, null);

        // Setzen der Grund-Werte für die Berechnung und des Modis
        prozessor.updateWert(getLink(EigenschaftEnum.KO), 10);
        prozessor.updateWert(getLink(EigenschaftEnum.KK), 11);
        prozessor.updateWert(getLink(EigenschaftEnum.LEP), 7); // = gekauft 3

        prozessor.addModi(link);

        assertEquals( // (10 + 10 + 11) / 2 = 16 + 4 + 3 = 23
                23, charakter.getEigenschaftsWert(EigenschaftEnum.LEP));
        assertEquals( // User Gewählt
                3, getLink(EigenschaftEnum.LEP).getUserLink().getWert());
        assertEquals( // Modis
                4, getLink(EigenschaftEnum.LEP).getWertModis());
        assertEquals( // Kosten ( 3 Punkt auf Spalte H )
                111, getLink(EigenschaftEnum.LEP).getKosten(),0);
    }

    /*
     * Testet mehrer Operationen auf der Berechneten Eigenschaft Asp (Setzen der Eigenschaft, Kosten, Modis)
     */
    @Test public void testASP() {
        // Setzen eines Modis durch Kultur
        link = new IdLink(k, EigenschaftEnum.ASP.getEigenschaft(), null, 1, null);

        // Setzen eines Modis durch Profession
        link2 = new IdLink(p, EigenschaftEnum.ASP.getEigenschaft(), null, 2, null);

        // Setzen der Grund-Werte für die Berechnung und des Modis
        prozessor.updateWert(getLink(EigenschaftEnum.MU), 10);
        prozessor.updateWert(getLink(EigenschaftEnum.IN), 11);
        prozessor.updateWert(getLink(EigenschaftEnum.CH), 11);
        prozessor.updateWert(getLink(EigenschaftEnum.ASP), 5); // = gekauft 2

        prozessor.addModi(link);
        prozessor.addModi(link2);

        assertEquals( // Max: (10 + 11 + 11) / 2 = 16 + 3 + 17 = 36
                36, prozessor.getMaxWert(getLink(EigenschaftEnum.ASP)));
        assertEquals( // Min: (10 + 11 + 11) / 2 = 16 + 3 = 19
                19, prozessor.getMinWert(getLink(EigenschaftEnum.ASP)));
        assertEquals( // Stufe: (10 + 11 + 11) / 2 = 16 + 3 + 2 = 21
                21, charakter.getEigenschaftsWert(EigenschaftEnum.ASP));
        assertEquals( // User Gewählt
                2, getLink(EigenschaftEnum.ASP).getUserLink().getWert());
        assertEquals( // Modis
                3, getLink(EigenschaftEnum.ASP).getWertModis());
        assertEquals( // Kosten ( 3 Punkt auf Spalte G )
                26, getLink(EigenschaftEnum.ASP).getKosten(),0);

        // LEP = 0 / ASP = 0 / MR = 0
        assertEquals(26, // GesamtKosten
                ((ExtendedProzessorEigenschaft) charakter.getProzessor(Eigenschaft.class).getExtendedInterface())
                .getGesamtTalentGpKosten());

        // Entfernen eines Modis:
        prozessor.removeModi(getLink(EigenschaftEnum.ASP), link2);

        assertEquals( // Max: (10 + 11 + 11) / 2 = 16 + 1 + 17 = 34
                34, prozessor.getMaxWert(getLink(EigenschaftEnum.ASP)));
        assertEquals( // Min: (10 + 11 + 11) / 2 = 16 + 1 = 17
                17, prozessor.getMinWert(getLink(EigenschaftEnum.ASP)));
        assertEquals( // Stufe: (10 + 11 + 11) / 2 = 16 + 1 + 2 = 19
                19, charakter.getEigenschaftsWert(EigenschaftEnum.ASP));
        assertEquals( // User Gewählt
                2, getLink(EigenschaftEnum.ASP).getUserLink().getWert());
        assertEquals( // Modis
                1, getLink(EigenschaftEnum.ASP).getWertModis());
        assertEquals( // Kosten ( 3 Punkt auf Spalte G )
                26, getLink(EigenschaftEnum.ASP).getKosten(),0);

        // Modi wieder hinzufügen
        prozessor.addModi(link2);

        assertEquals( // Max: (10 + 11 + 11) / 2 = 16 + 3 + 17 = 36
                36, prozessor.getMaxWert(getLink(EigenschaftEnum.ASP)));
        assertEquals( // Min: (10 + 11 + 11) / 2 = 16 + 3 = 19
                19, prozessor.getMinWert(getLink(EigenschaftEnum.ASP)));
        assertEquals( // Stufe: (10 + 11 + 11) / 2 = 16 + 3 + 2 = 21 --> Wurde gesenkt
                19, charakter.getEigenschaftsWert(EigenschaftEnum.ASP));
        assertEquals( // User Gewählt
                0, getLink(EigenschaftEnum.ASP).getUserLink().getWert());
        assertEquals( // Modis
                3, getLink(EigenschaftEnum.ASP).getWertModis());
        assertEquals( // Kosten ( 0 Punkt auf Spalte G )
                0, getLink(EigenschaftEnum.ASP).getKosten(),0);

        // LEP = 0 / ASP = 0 / MR = 0
        assertEquals(0, // GesamtKosten
                ((ExtendedProzessorEigenschaft) charakter.getProzessor(Eigenschaft.class).getExtendedInterface())
                .getGesamtTalentGpKosten());
    }

    
    @Test public void testNegativ() {
        // Kultur setzen
        link = new IdLink(k, EigenschaftEnum.MU.getEigenschaft(), null, -2, null);

        prozessor.updateWert(getLink(EigenschaftEnum.MU), 8);
        prozessor.addModi(link);

        assertEquals( // Max Wert: 14 - 2 = 12
                12, prozessor.getMaxWert(getLink(EigenschaftEnum.MU)));
        assertEquals( // Min Wert: 8 + 2 = 10
                6, prozessor.getMinWert(getLink(EigenschaftEnum.MU)));
        assertEquals( // Gesamtstufe (da versucht wird die Stufe zu halten)
                8, getLink(EigenschaftEnum.MU).getWert());
        assertEquals( // User Gewähhlt (da versucht wird die Stufe zu halten)
                10, getLink(EigenschaftEnum.MU).getUserLink().getWert());
        assertEquals( // Modis
                -2, getLink(EigenschaftEnum.MU).getWertModis());
        assertEquals( // Kosten (da versucht wird die Stufe zu halten)
                10, getLink(EigenschaftEnum.MU).getKosten(),0);

    }

    /**
     * Testet die Abhängigkeiten für den Minimalen Wert zwischen Talent und Eigeschaft.
     */
    @Test public void testTalentEigenschaft() {
        Talent talent1, talent2;

        // Erzeugen der Talente:
        // Talent 1:
        talent1 = new Talent();
        talent1.setId("TAL-test-1");
        talent1.setDreiEigenschaften(new Eigenschaft[] {
				EigenschaftEnum.MU.getEigenschaft(),
				EigenschaftEnum.KL.getEigenschaft(),
				EigenschaftEnum.GE.getEigenschaft() });
        talent1.setKostenKlasse(KostenKlasse.A);
        talent1.setArt(Talent.Art.spezial);
        talent1.setSorte(Talent.Sorte.koerper);
        talent1.setName("Test Talent 1");

        // Talent 2:
        talent2 = new Talent();
        talent2.setId("TAL-test-2");
        talent2.setDreiEigenschaften(new Eigenschaft[] {
        		EigenschaftEnum.KK.getEigenschaft(),
				EigenschaftEnum.KO.getEigenschaft(),
				EigenschaftEnum.FF.getEigenschaft() });
        talent2.setKostenKlasse(KostenKlasse.D);
        talent2.setArt(Talent.Art.basis);
        talent2.setSorte(Talent.Sorte.handwerk);
        talent2.setName("Test Talent 2");

        // Hinzufügen der Talente
        prozessorTalent.addNewElement(talent1);
        prozessorTalent.addNewElement(talent2);

        // Setzen der Talente
        prozessorTalent.updateWert(boxTalent.getObjectById(talent1), 12);
        prozessorTalent.updateWert(boxTalent.getObjectById(talent2), 14);

        // Setzten der Eigenschaften
        prozessor.updateWert(getLink(EigenschaftEnum.MU), 8);
        prozessor.updateWert(getLink(EigenschaftEnum.KL), 9);
        prozessor.updateWert(getLink(EigenschaftEnum.GE), 10);

        prozessor.updateWert(getLink(EigenschaftEnum.KK), 9);
        prozessor.updateWert(getLink(EigenschaftEnum.KO), 10);
        prozessor.updateWert(getLink(EigenschaftEnum.FF), 11);

        // Da KL und GE als Grundlage dienen können, keine Beschränkung
        assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.MU)));
        assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.KL)));
        assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.GE)));

        // Da nur FF als Grundl. dienen kann, ist es beschränkt
        assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.KK)));
        assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.KO)));
        assertEquals(11, prozessor.getMinWert(getLink(EigenschaftEnum.FF)));

        // Ändern von FF und Talent1
        prozessorTalent.updateWert(boxTalent.getObjectById(talent1), 13);
        prozessor.updateWert(getLink(EigenschaftEnum.FF), 14);

        // Da nur noch GE als Grundl. dienen kann, ist es beschränkt
        assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.MU)));
        assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.KL)));
        assertEquals(10, prozessor.getMinWert(getLink(EigenschaftEnum.GE)));

        // Da nur FF als Grundl. dienen kann, ist es beschränkt
        assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.KK)));
        assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.KO)));
        assertEquals(11, prozessor.getMinWert(getLink(EigenschaftEnum.FF)));

        // Ändern von KK und Talent1
        prozessorTalent.updateWert(boxTalent.getObjectById(talent1), 2);
        prozessor.updateWert(getLink(EigenschaftEnum.KK), 12);

        // Da die Talent-Stufe nur 2 ist, keine Beschränkung
        assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.MU)));
        assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.KL)));
        assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.GE)));

        // Da nun FF und KK als Grundl. dienen könne, keine Beschränkung!
        assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.KK)));
        assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.KO)));
        assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.FF)));

    }
}
