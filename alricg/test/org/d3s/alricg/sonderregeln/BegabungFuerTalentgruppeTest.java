/*
 * Created on 13.08.2005 / 14:08:54
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.sonderregeln;


import static org.junit.Assert.assertEquals;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Eigenschaft;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.Vorteil;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.sonderregeln.BegabungFuerTalentgruppe;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.LinkProzessorFront;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.elementBox.ElementBox;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorTalent;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;
import org.d3s.alricg.store.DataStore;
import org.d3s.alricg.store.FactoryFinder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BegabungFuerTalentgruppeTest {
	private LinkProzessorFront<Talent, ExtendedProzessorTalent, GeneratorLink> prozessor;
	private ElementBox<GeneratorLink> box, boxEigenschaft;
	private Held held;
    private IdLink link1;
    private Vorteil vorteil;
    private Talent talent1, talent2, talent3, talent4;
    private DataStore data;
    private BegabungFuerTalentgruppe begabungSR;

	@BeforeClass public static void startTestClass() {
	    // Starten des Programms
	    ProgAdmin.main(new String[] { "noScreen" });
	}
    
    @Before public void setUp() throws Exception {
        // initialisieren
        FactoryFinder.init();
        data = FactoryFinder.find().getData();
        held = new Held();
        
        held.initGenrierung();
        prozessor = held.getProzessor(CharKomponente.talent);
        box = prozessor.getElementBox();

        // SR erzeugen
        begabungSR = new BegabungFuerTalentgruppe();

        // Vorteil erzeugen
        vorteil = new Vorteil("VOR-jUnit-test");
        vorteil.setSonderregel(begabungSR);

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
        talent2.setKostenKlasse(KostenKlasse.B);
        talent2.setArt(Talent.Art.basis);
        talent2.setSorte(Talent.Sorte.handwerk);
        talent2.setName("Test Talent 2");

        // Talent 3:
        talent3 = new Talent("TAL-test-3");
        talent3.setDreiEigenschaften(new Eigenschaft[] {
                (Eigenschaft) data.getCharElement("EIG-IN", CharKomponente.eigenschaft),
                (Eigenschaft) data.getCharElement("EIG-CH", CharKomponente.eigenschaft),
                (Eigenschaft) data.getCharElement("EIG-FF", CharKomponente.eigenschaft) });
        talent3.setKostenKlasse(KostenKlasse.C);
        talent3.setArt(Talent.Art.beruf);
        talent3.setSorte(Talent.Sorte.natur);
        talent3.setName("Test Talent 3");

        // Talent 4:
        talent4 = new Talent("TAL-test-4");
        talent4.setDreiEigenschaften(new Eigenschaft[] {
                (Eigenschaft) data.getCharElement("EIG-KK", CharKomponente.eigenschaft),
                (Eigenschaft) data.getCharElement("EIG-KO", CharKomponente.eigenschaft),
                (Eigenschaft) data.getCharElement("EIG-FF", CharKomponente.eigenschaft) });
        talent4.setKostenKlasse(KostenKlasse.D);
        talent4.setArt(Talent.Art.beruf);
        talent4.setSorte(Talent.Sorte.handwerk);
        talent4.setName("Test Talent 4");

    }

    @Test public void testKosten() {
        // Talente hinzufügen
        prozessor.addNewElement(talent1);
        prozessor.addNewElement(talent2);
        prozessor.addNewElement(talent3);

        prozessor.updateWert(getLink(talent1), 1);
        prozessor.updateWert(getLink(talent2), 1);
        prozessor.updateWert(getLink(talent3), 1);
        
        // Normale Kosten
        assertEquals(2, getLink(talent1).getKosten());
        assertEquals(2, getLink(talent2).getKosten()); // Keine Aktivierungskosten
        assertEquals(5, getLink(talent3).getKosten());

        // Sonderregel hinzufügen
        link1 = new IdLink(null, null);
        link1.setZiel(vorteil);
        link1.setText(Talent.Sorte.handwerk.getValue());
        held.getSonderregelAdmin().addSonderregel(link1);

        // Kosten prüfen, alle HandwerksTalente nun billiger!
        assertEquals(2, getLink(talent1).getKosten());
        assertEquals(1, getLink(talent2).getKosten()); // Keine Aktivierungskosten !!SR!!
        assertEquals(5, getLink(talent3).getKosten());

        // Weiteres Handwerkstalent hinzufügen
        prozessor.addNewElement(talent4);
        prozessor.updateWert(getLink(talent4), 3);
        
        // Kosten prüfen
        assertEquals(2, getLink(talent1).getKosten());
        assertEquals(1, getLink(talent2).getKosten()); // Keine Aktivierungskosten !!SR!!
        assertEquals(5, getLink(talent3).getKosten());
        assertEquals(20, getLink(talent4).getKosten()); // !!SR!!

        // Eine Stufe steigern
        prozessor.updateWert(getLink(talent2), 3);

        assertEquals(2, getLink(talent1).getKosten());
        assertEquals(6, getLink(talent2).getKosten()); // Keine Aktivierungskosten !!SR!!
        assertEquals(5, getLink(talent3).getKosten());
        assertEquals(20, getLink(talent4).getKosten()); // !!SR!!

        // Sonderregel wieder entfernen
        held.getSonderregelAdmin().removeSonderregel(link1);

        // Alle Kosten wieder normal
        assertEquals(2, getLink(talent1).getKosten());
        assertEquals(12, getLink(talent2).getKosten()); // Keine Aktivierungskosten
        assertEquals(5, getLink(talent3).getKosten());
        assertEquals(26, getLink(talent4).getKosten());

        // SR wieder hinzufügen, nun mir "körper"
        link1.setText(Talent.Sorte.koerper.getValue());
        held.getSonderregelAdmin().addSonderregel(link1);

        // Nix ändert sich, da talent1 bereits "A" als KK hat!
        assertEquals(2, getLink(talent1).getKosten()); // !!SR!!
        assertEquals(12, getLink(talent2).getKosten()); // Keine Aktivierungskosten
        assertEquals(5, getLink(talent3).getKosten());
        assertEquals(26, getLink(talent4).getKosten());

        // Sonderregel wieder entfernen
        held.getSonderregelAdmin().removeSonderregel(link1);

        // SR wieder hinzufügen, nun mir "natur"
        link1.setText(Talent.Sorte.natur.getValue());
        held.getSonderregelAdmin().addSonderregel(link1);

        // Kosten für Naturtalente nun gesenkt!
        assertEquals(2, getLink(talent1).getKosten());
        assertEquals(12, getLink(talent2).getKosten()); // Keine Aktivierungskosten
        assertEquals(4, getLink(talent3).getKosten()); // !!SR!!
        assertEquals(26, getLink(talent4).getKosten());
    }

    /**
     * Liefert den Link zu dem CharElement zurück
     * 
     * @param enu Die gewünschte Eigenschaft
     * @return Der Link von ProzessorXX zu der Eigenschaft
     */
    private GeneratorLink getLink(CharElement elem) {

        return box.getObjectById(elem);
    }

}
