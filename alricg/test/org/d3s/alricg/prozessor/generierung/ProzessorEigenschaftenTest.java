/*
 * Created on 02.07.2005 / 01:28:14
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.generierung;

import junit.framework.TestCase;

import org.d3s.alricg.charKomponenten.Eigenschaft;
import org.d3s.alricg.charKomponenten.EigenschaftEnum;
import org.d3s.alricg.charKomponenten.Kultur;
import org.d3s.alricg.charKomponenten.Profession;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.LinkProzessorFront;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.elementBox.ElementBox;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorEigenschaft;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;
import org.d3s.alricg.store.DataStore;
import org.d3s.alricg.store.FactoryFinder;

public class ProzessorEigenschaftenTest extends TestCase {
    private Held held;
    private LinkProzessorFront<Eigenschaft, ExtendedProzessorEigenschaft, GeneratorLink> prozessor;
    private LinkProzessorFront<Talent, ExtendedProzessorEigenschaft, GeneratorLink> prozessorTalent;
    private ElementBox<GeneratorLink> box, boxTalent;
    private Kultur k;
    private Profession p;
    private DataStore data;

    private IdLink link, link2;

    /*
     * (non-Javadoc) Methode überschrieben
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
    	
        // initialisieren
        FactoryFinder.init();
        data = FactoryFinder.find().getData();
        held = new Held();
        
        held.initGenrierung();
        prozessor = held.getProzessor(CharKomponente.eigenschaft);
        box = prozessor.getElementBox();
        prozessorTalent = held.getProzessor(CharKomponente.talent);
        boxTalent = prozessorTalent.getElementBox();
        
        k = new Kultur("KUL-jUnit-test-eigen");
        p = new Profession("PROF-jUnit-test-eigen");
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

    /**
     * Liefert den Link zu dem CharElement zurück (für Talente gedacht)
     * 
     * @param enu Die gewünschte Eigenschaft
     * @return Der Link von ProzessorXX zu der Eigenschaft
     *
    private GeneratorLink getLinkTalent(CharElement elem) {

        return (GeneratorLink) prozessor.getLinkByCharElement(elem, null, null);
    }

    /*
     * Testet der Basis Funktionen: Das Setzen der Eigenschaften und die Berechnung der Kosten!
     */
    public void testUpdateStufe() {

        prozessor.updateWert(getLink(EigenschaftEnum.KO), 10);
        prozessor.updateWert(getLink(EigenschaftEnum.KK), 11);
        prozessor.updateWert(getLink(EigenschaftEnum.MU), 9);
        prozessor.updateWert(getLink(EigenschaftEnum.IN), 12);
        prozessor.updateWert(getLink(EigenschaftEnum.CH), 14);
        
        prozessor.updateWert(getLink(EigenschaftEnum.ASP), 2);
        prozessor.updateWert(getLink(EigenschaftEnum.LEP), 5);
        
        prozessor.updateWert(getLink(EigenschaftEnum.SO), 2);

        // "Nicht Berechnete" Werte überprüfen
        assertEquals(10, held.getEigenschaftsWert(EigenschaftEnum.KO));
        assertEquals(11, held.getEigenschaftsWert(EigenschaftEnum.KK));
        assertEquals(9, held.getEigenschaftsWert(EigenschaftEnum.MU));
        assertEquals(12, held.getEigenschaftsWert(EigenschaftEnum.IN));
        assertEquals(14, held.getEigenschaftsWert(EigenschaftEnum.CH));

        // Errechnete Werte überprüfen:
        assertEquals( // (10 + 10 + 11) / 2 = 16 + 5 = 21
                21, held.getEigenschaftsWert(EigenschaftEnum.LEP));
        assertEquals( // (9 + 12 + 14) / 2 = 18 + 2 = 20
                20, held.getEigenschaftsWert(EigenschaftEnum.ASP));

        // Überprüfen ob die Kosten richtig sind:
        assertEquals( // Stufe = Kosten = 10
                10, getLink(EigenschaftEnum.KO).getKosten());
        assertEquals( // Stufe = Kosten = 11
                11, getLink(EigenschaftEnum.KK).getKosten());
        assertEquals( // Stufe = Kosten = 9
                9, getLink(EigenschaftEnum.MU).getKosten());
        assertEquals( // Stufe = Kosten = 2
                2, getLink(EigenschaftEnum.SO).getKosten());
        assertEquals( // Über SKT - Nur die gekauften Punkte
                306, getLink(EigenschaftEnum.LEP).getKosten());
        assertEquals( // Über SKT - Nur die gekauften Punkte
                26, getLink(EigenschaftEnum.ASP).getKosten());

        // MU = 9 / KL = 8 / IN = 12 / GE = 8 / CH = 14 / FF = 8 / KK = 11 / KO = 10
        // SO = 2
        assertEquals(82, 
        		prozessor.getGesamtKosten());

        // LEP = 306 / ASP = 26 / MR = 0
        assertEquals(332, // GesamtKosten
        		prozessor.getExtendedFunctions().getGesamtTalentGpKosten());

        // Sonstiges: Sollte nix kosten!
        assertEquals( 0, getLink(EigenschaftEnum.MR).getKosten());

        // Max Wert IN
        assertEquals( 12, prozessor.getMaxWert(getLink(EigenschaftEnum.KA)));
    }

    /*
     * Testet mehrer Operationen auf der Eigenschaft Mut (Setzen der Eigenschaft, Kosten, Modis)
     */
    public void testMU() {
        // ------------- Test der Eigenschaft MU
        // Kultur setzen
        link = new IdLink(k, null);
        link.setWert(2);
        link.setZiel(data.getCharElement(EigenschaftEnum.MU.getId()));

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
                8, getLink(EigenschaftEnum.MU).getKosten());

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
                14, getLink(EigenschaftEnum.MU).getKosten());

        // MU = 14 / KL = 8 / IN = 8 / GE = 8 / CH = 8 / FF = 8 / KK = 8 / KO = 8
        // SO = 1
        assertEquals(71, // GesamtKosten
                held.getProzessor(CharKomponente.eigenschaft).getGesamtKosten());

    }

    /*
     * Testet mehrer Operationen auf der Eigenschaft Klugheit (Setzen der Eigenschaft, Kosten, Modis durch 2x Herkunft)
     */
    public void testKL() {
        // Kultur setzen
        link = new IdLink(k, null);
        link.setWert(2);
        link.setZiel(data.getCharElement(EigenschaftEnum.KL.getId()));
        // Profession setzen
        link2 = new IdLink(p, null);
        link2.setWert(2);
        link2.setZiel(data.getCharElement(EigenschaftEnum.KL.getId()));

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
                8, getLink(EigenschaftEnum.KL).getKosten());

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
                8, getLink(EigenschaftEnum.KL).getKosten());

        // MU = 8 / KL = 8 / IN = 8 / GE = 8 / CH = 8 / FF = 8 / KK = 8 / KO = 8 / SO = 1
        assertEquals(65, // GesamtKosten
        		held.getProzessor(CharKomponente.eigenschaft).getGesamtKosten());

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
                8, getLink(EigenschaftEnum.KL).getKosten());

        // MU = 8 / KL = 8 / IN = 8 / GE = 8 / CH = 8 / FF = 8 / KK = 8 / KO = 8 / SO = 1
        assertEquals(65, // GesamtKosten
        		held.getProzessor(CharKomponente.eigenschaft).getGesamtKosten());
    }

    /*
     * Testet mehrer Operationen auf der Berechneten Eigenschaft Lep (Setzen der Eigenschaft, Kosten, Modis)
     */
    public void testLEP() {
        // Setzen eines Modis durch eine Kultur
        link = new IdLink(k, null);
        link.setWert(4);
        link.setZiel(data.getCharElement(EigenschaftEnum.LEP.getId()));

        // Setzen der Grund-Werte für die Berechnung und des Modis
        prozessor.updateWert(getLink(EigenschaftEnum.KO), 10);
        prozessor.updateWert(getLink(EigenschaftEnum.KK), 11);
        prozessor.updateWert(getLink(EigenschaftEnum.LEP), 7); // = gekauft 3

        prozessor.addModi(link);

        assertEquals( // (10 + 10 + 11) / 2 = 16 + 4 + 3 = 23
                23, held.getEigenschaftsWert(EigenschaftEnum.LEP));
        assertEquals( // User Gewählt
                3, getLink(EigenschaftEnum.LEP).getUserLink().getWert());
        assertEquals( // Modis
                4, getLink(EigenschaftEnum.LEP).getWertModis());
        assertEquals( // Kosten ( 3 Punkt auf Spalte H )
                111, getLink(EigenschaftEnum.LEP).getKosten());
    }

    /*
     * Testet mehrer Operationen auf der Berechneten Eigenschaft Asp (Setzen der Eigenschaft, Kosten, Modis)
     */
    public void testASP() {
        // Setzen eines Modis durch Kultur
        link = new IdLink(k, null);
        link.setWert(1);
        link.setZiel(data.getCharElement(EigenschaftEnum.ASP.getId()));
        // Setzen eines Modis durch Profession
        link2 = new IdLink(p, null);
        link2.setWert(2);
        link2.setZiel(data.getCharElement(EigenschaftEnum.ASP.getId()));

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
                21, held.getEigenschaftsWert(EigenschaftEnum.ASP));
        assertEquals( // User Gewählt
                2, getLink(EigenschaftEnum.ASP).getUserLink().getWert());
        assertEquals( // Modis
                3, getLink(EigenschaftEnum.ASP).getWertModis());
        assertEquals( // Kosten ( 3 Punkt auf Spalte G )
                26, getLink(EigenschaftEnum.ASP).getKosten());

        // LEP = 0 / ASP = 0 / MR = 0
        assertEquals(26, // GesamtKosten
                ((ExtendedProzessorEigenschaft) held.getProzessor(CharKomponente.eigenschaft).getExtendedFunctions())
                .getGesamtTalentGpKosten());

        // Entfernen eines Modis:
        prozessor.removeModi(getLink(EigenschaftEnum.ASP), link2);

        assertEquals( // Max: (10 + 11 + 11) / 2 = 16 + 1 + 17 = 34
                34, prozessor.getMaxWert(getLink(EigenschaftEnum.ASP)));
        assertEquals( // Min: (10 + 11 + 11) / 2 = 16 + 1 = 17
                17, prozessor.getMinWert(getLink(EigenschaftEnum.ASP)));
        assertEquals( // Stufe: (10 + 11 + 11) / 2 = 16 + 1 + 2 = 19
                19, held.getEigenschaftsWert(EigenschaftEnum.ASP));
        assertEquals( // User Gewählt
                2, getLink(EigenschaftEnum.ASP).getUserLink().getWert());
        assertEquals( // Modis
                1, getLink(EigenschaftEnum.ASP).getWertModis());
        assertEquals( // Kosten ( 3 Punkt auf Spalte G )
                26, getLink(EigenschaftEnum.ASP).getKosten());

        // Modi wieder hinzufügen
        prozessor.addModi(link2);

        assertEquals( // Max: (10 + 11 + 11) / 2 = 16 + 3 + 17 = 36
                36, prozessor.getMaxWert(getLink(EigenschaftEnum.ASP)));
        assertEquals( // Min: (10 + 11 + 11) / 2 = 16 + 3 = 19
                19, prozessor.getMinWert(getLink(EigenschaftEnum.ASP)));
        assertEquals( // Stufe: (10 + 11 + 11) / 2 = 16 + 3 + 2 = 21 --> Wurde gesenkt
                19, held.getEigenschaftsWert(EigenschaftEnum.ASP));
        assertEquals( // User Gewählt
                0, getLink(EigenschaftEnum.ASP).getUserLink().getWert());
        assertEquals( // Modis
                3, getLink(EigenschaftEnum.ASP).getWertModis());
        assertEquals( // Kosten ( 0 Punkt auf Spalte G )
                0, getLink(EigenschaftEnum.ASP).getKosten());

        // LEP = 0 / ASP = 0 / MR = 0
        assertEquals(0, // GesamtKosten
                ((ExtendedProzessorEigenschaft) held.getProzessor(CharKomponente.eigenschaft).getExtendedFunctions())
                .getGesamtTalentGpKosten());
    }

    
    public void testNegativ() {
        // Kultur setzen
        link = new IdLink(k, null);
        link.setWert(-2);
        link.setZiel(data.getCharElement(EigenschaftEnum.MU.getId()));

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
                10, getLink(EigenschaftEnum.MU).getKosten());

    }

    /**
     * Testet die Abhängigkeiten für den Minimalen Wert zwischen Talent und Eigeschaft.
     */
    public void testTalentEigenschaft() {
        Talent talent1, talent2;

        // Erzeugen der Talente:
        // Talent 1:
        talent1 = new Talent("TAL-test-1");
        talent1.setDreiEigenschaften(new Eigenschaft[] {
                (Eigenschaft) data.getCharElement("EIG-MU", CharKomponente.eigenschaft),
                (Eigenschaft) data.getCharElement("EIG-KL", CharKomponente.eigenschaft),
                (Eigenschaft) data.getCharElement("EIG-GE", CharKomponente.eigenschaft) });
        talent1.setKostenKlasse(KostenKlasse.A);
        talent1.setArt(Talent.Art.spezial);
        talent1.setSorte(Talent.Sorte.koerper);
        talent1.setName("Test Talent 1");

        // Talent 2:
        talent2 = new Talent("TAL-test-2");
        talent2.setDreiEigenschaften(new Eigenschaft[] {
                (Eigenschaft) data.getCharElement("EIG-KK", CharKomponente.eigenschaft),
                (Eigenschaft) data.getCharElement("EIG-KO", CharKomponente.eigenschaft),
                (Eigenschaft) data.getCharElement("EIG-FF", CharKomponente.eigenschaft) });
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
