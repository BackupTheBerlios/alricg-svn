/*
 * Created on 11.08.2005 / 15:11:28
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.sonderregeln;

import junit.framework.TestCase;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Eigenschaft;
import org.d3s.alricg.charKomponenten.Rasse;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.Vorteil;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.sonderregeln.BegabungFuerTalent;
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

public class BegabungFuerTalentTest extends TestCase {
	private LinkProzessorFront<Talent, ExtendedProzessorTalent, GeneratorLink> prozessor;
	private ElementBox<GeneratorLink> box, boxEigenschaft;
	private Held held;
	private Vorteil vorteil;
	private IdLink link1, link2, link3;
	private Talent talent1, talent2, talent3;
	private BegabungFuerTalent begabungSR;
    
    private DataStore data;
	
	protected void setUp() throws Exception {
		
        ProgAdmin.main(new String[] { "noScreen" });
    	
        // initialisieren
        FactoryFinder.init();
        data = FactoryFinder.find().getData();
        held = new Held();
        
        held.initGenrierung();
        prozessor = held.getProzessor(CharKomponente.talent);
        box = prozessor.getElementBox();
		
		// SR erzeugen
		begabungSR = new BegabungFuerTalent();
		
		// Vorteil erzeugen
		vorteil = new Vorteil("VOR-jUnit-test");
		vorteil.setSonderregel(begabungSR);
		
		// Erzeugen der Talente:
		// Talent 1:
		talent1 = new Talent("TAL-test-1");
		talent1.setDreiEigenschaften(
				new Eigenschaft[]  {
						(Eigenschaft) data.getCharElement("EIG-MU", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-KL", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-GE", CharKomponente.eigenschaft)
				});
		talent1.setKostenKlasse(KostenKlasse.B);
		talent1.setArt(Talent.Art.spezial);
		talent1.setSorte(Talent.Sorte.koerper);
		talent1.setName("Test Talent 1");
		
		// Talent 2:
		talent2 = new Talent("TAL-test-2");
		talent2.setDreiEigenschaften(
				new Eigenschaft[]  {
						(Eigenschaft) data.getCharElement("EIG-KK", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-KO", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-FF", CharKomponente.eigenschaft)
				});
		talent2.setKostenKlasse(KostenKlasse.D);
		talent2.setArt(Talent.Art.basis);
		talent2.setSorte(Talent.Sorte.handwerk);
		talent2.setName("Test Talent 2");
		
		// Talent 3:
		talent3 = new Talent("TAL-test-3");
		talent3.setDreiEigenschaften(
				new Eigenschaft[]  {
						(Eigenschaft) data.getCharElement("EIG-IN", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-CH", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-FF", CharKomponente.eigenschaft)
				});
		talent3.setKostenKlasse(KostenKlasse.H);
		talent3.setArt(Talent.Art.beruf);
		talent3.setSorte(Talent.Sorte.natur);
		talent3.setName("Test Talent 3");
		
		// Elemente zum Helden hinzufügen
		prozessor.addNewElement(talent1);
		prozessor.addNewElement(talent2);
		prozessor.addNewElement(talent3);
		prozessor.updateWert(getLink(talent2), 1);
		
		// Setzen der Links mit Ziel: Vorteil, ZweitZiel: TalentX
		link1 = new IdLink(null, null);
		link1.setZiel(vorteil);
		link1.setZweitZiel( talent1 );
		
		link2 = new IdLink(null, null);
		link2.setZiel(vorteil);
		link2.setZweitZiel( talent2 );
		
		link3 = new IdLink(null, null);
		link3.setZiel(vorteil);
		link3.setZweitZiel( talent3 );
		
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
		prozessor = null;
		vorteil = null;
		link1 = null;
		link2 = null;
		link3 = null;
		talent1 = null;
		talent2 = null;
		talent3 = null;
	}

	/*
	 * Teste die Methode "canAddSelf()"
	 */
	public void testCanAddSelf() {
		
		talent1.setKostenKlasse(KostenKlasse.A);
		// Nr.1 Sollte nicht gehen, da KostenKlasse A
		assertEquals(false, begabungSR.canAddSelf(held, true, link1));
		assertEquals(true, begabungSR.canAddSelf(held, true, link2));
		assertEquals(true, begabungSR.canAddSelf(held, true, link3));
		
		// Ändern der Kostenklasse
		talent1.setKostenKlasse(KostenKlasse.B);
		assertEquals(true, begabungSR.canAddSelf(held, true, link1));
		
		// Hinzufügen des Vorteils + (Sonderregeln)
		held.getSonderregelAdmin().addSonderregel(link1);
		
		// Da die Sonderregel nur einmal hinzugefügt werden darf, alle false!
		assertEquals(false, begabungSR.canAddSelf(held, true, link1));
		assertEquals(false, begabungSR.canAddSelf(held, true, link2));
		assertEquals(false, begabungSR.canAddSelf(held, true, link3));
		
		// Entfernen der Sonderregel, nun alle Hinzufügbar!
		held.getSonderregelAdmin().removeSonderregel(link1);
		assertEquals(true, begabungSR.canAddSelf(held, true, link1));
		assertEquals(true, begabungSR.canAddSelf(held, true, link2));
		assertEquals(true, begabungSR.canAddSelf(held, true, link3));
	}

	/*
	 * Testet die Änderung der Kostenklasse durch die Sonderregel
	 */
	public void testChangeKostenKlasse() {
		
		prozessor.updateWert(getLink(talent1), 3);
		prozessor.updateWert(getLink(talent2), 5);
		prozessor.updateWert(getLink(talent3), 7);
		
		// Die Stufe wird voll bezahlt!
		assertEquals(14, getLink(talent1).getKosten()); // + aktivierungskosten
		assertEquals(61, getLink(talent2).getKosten()); // Basis-Talent -> keine AK
		assertEquals(631, getLink(talent3).getKosten()); // + aktivierungskosten
		
		// Das Talent 1 verbilligen
		held.getSonderregelAdmin().addSonderregel(link1);
		assertEquals(6, getLink(talent1).getKosten()); // + aktivierungskosten
		assertEquals(61, getLink(talent2).getKosten()); // Basis-Talent -> keine AK
		assertEquals(631, getLink(talent3).getKosten()); // + aktivierungskosten
		
		// Vorteil wieder entfernen, normale Kosten
		held.getSonderregelAdmin().removeSonderregel(link1);
		assertEquals(14, getLink(talent1).getKosten()); // + aktivierungskosten
		assertEquals(61, getLink(talent2).getKosten()); // Basis-Talent -> keine AK
		assertEquals(631, getLink(talent3).getKosten()); // + aktivierungskosten
		
		// Das Talent 2 verbilligen
		held.getSonderregelAdmin().addSonderregel(link2);
		assertEquals(14, getLink(talent1).getKosten()); // + aktivierungskosten
		assertEquals(47, getLink(talent2).getKosten()); // Basis-Talent -> keine AK
		assertEquals(631, getLink(talent3).getKosten()); // + aktivierungskosten
		
		// Vorteil wieder entfernen, normale Kosten
		held.getSonderregelAdmin().removeSonderregel(link2);
		assertEquals(14, getLink(talent1).getKosten()); // + aktivierungskosten
		assertEquals(61, getLink(talent2).getKosten()); // Basis-Talent -> keine AK
		assertEquals(631, getLink(talent3).getKosten()); // + aktivierungskosten
		
		// Das Talent 3 verbilligen
		held.getSonderregelAdmin().addSonderregel(link3);
		assertEquals(14, getLink(talent1).getKosten()); // + aktivierungskosten
		assertEquals(61, getLink(talent2).getKosten()); // Basis-Talent -> keine AK
		assertEquals(308, getLink(talent3).getKosten()); // + aktivierungskosten
		
		// Vorteil wieder entfernen, normale Kosten
		held.getSonderregelAdmin().removeSonderregel(link3);
		assertEquals(14, getLink(talent1).getKosten()); // + aktivierungskosten
		assertEquals(61, getLink(talent2).getKosten()); // Basis-Talent -> keine AK
		assertEquals(631, getLink(talent3).getKosten()); // + aktivierungskosten
	}

	
	public void testChangeKostenModiKlasse() {
		
		// Modifikator (durch eine Rasse) erzeugen
		link2 = new IdLink(new Rasse("RAS-JUnit-test"), null);
		link2.setZiel(talent1);
		link2.setWert(1);
		
		// Stufe setzen und Kosten prüfen!
		prozessor.updateWert(getLink(talent1), 3);
		assertEquals(14, getLink(talent1).getKosten()); // + aktivierungskosten
		
		// Das Talent1 mit SR verbilligen
		held.getSonderregelAdmin().addSonderregel(link1);
		
		// Modis hinzufügen
		prozessor.addModi(link2);
		
		// Kosten mit SR und Modi prüfen
		assertEquals(5, getLink(talent1).getKosten());
		
		// Modi entfernen
		prozessor.removeModi(getLink(talent1), link2);
		
		// Nur noch verbilligte Kosten
		assertEquals(3, getLink(talent1).getWert());
		assertEquals(3, getLink(talent1).getKosten()); // + aktivierungskosten
		
	}
	
	/**
	 * Liefert den Link zu dem CharElement zurück
	 * @param enu Die gewünschte Eigenschaft
	 * @return Der Link von ProzessorXX zu der Eigenschaft
	 */
	private GeneratorLink getLink(CharElement elem) {
		return (GeneratorLink) box.getObjectById(elem);
	}

}
