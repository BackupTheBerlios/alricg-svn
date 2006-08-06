/*
 * Created on 12.08.2005 / 17:20:22
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.sonderregeln;

import junit.framework.TestCase;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Eigenschaft;
import org.d3s.alricg.charKomponenten.EigenschaftEnum;
import org.d3s.alricg.charKomponenten.Nachteil;
import org.d3s.alricg.charKomponenten.Rasse;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.sonderregeln.Stubenhocker;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.LinkProzessorFront;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.elementBox.ElementBox;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorEigenschaft;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorTalent;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;
import org.d3s.alricg.store.DataStore;
import org.d3s.alricg.store.FactoryFinder;

/**
 * <u>Beschreibung:</u><br> 
 * Testet die Sonderregel "Stubenhocker".
 * @author V. Strelow
 */
public class StubenhockerTest extends TestCase {
	private Talent talent1, talent2, talent3, talent4, talent5, talent6, 
				talentBasis1, talentBasis2, talentBasis3;
	private LinkProzessorFront<Talent, ExtendedProzessorTalent, GeneratorLink> prozessorTalent;
	private LinkProzessorFront<Eigenschaft, ExtendedProzessorEigenschaft, GeneratorLink> prozessorEigenschaft;

	private ElementBox<GeneratorLink> boxTalent;
	private IdLink link1, link2, link3;
	private Rasse ras;
	private Nachteil nachteil;
	private Held held;
	private Stubenhocker stubenSR;
	private DataStore data;
	
	protected void setUp() throws Exception {
		super.setUp();
		
        // initialisieren
        FactoryFinder.init();
        data = FactoryFinder.find().getData();
        held = new Held();
        
        held.initGenrierung();
        prozessorTalent = held.getProzessor(CharKomponente.talent);
        prozessorEigenschaft = held.getProzessor(CharKomponente.eigenschaft);
        boxTalent = prozessorTalent.getElementBox();
        
		// Sonderregel erzeugen
		stubenSR = new Stubenhocker();
		
		// Rasse erzeugen
		ras = new Rasse("RAS-test");
		
		// Nachteil Erzeugen
		nachteil = new Nachteil("NAC-test");
		nachteil.setSonderregel(stubenSR);
		
		// Erzeugen der Talente:
		// Talent 1:
		talent1 = new Talent("TAL-test-1");
		talent1.setDreiEigenschaften(
				new Eigenschaft[]  {
						(Eigenschaft) data.getCharElement("EIG-MU", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-KL", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-GE", CharKomponente.eigenschaft)
				});
		talent1.setKostenKlasse(KostenKlasse.A);
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
		talent2.setKostenKlasse(KostenKlasse.B);
		talent2.setArt(Talent.Art.spezial);
		talent2.setSorte(Talent.Sorte.kampf);
		talent2.setName("Test Talent 2");
		
		// Talent 3:
		talent3 = new Talent("TAL-test-3");
		talent3.setDreiEigenschaften(
				new Eigenschaft[]  {
						(Eigenschaft) data.getCharElement("EIG-IN", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-CH", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-FF", CharKomponente.eigenschaft)
				});
		talent3.setKostenKlasse(KostenKlasse.C);
		talent3.setArt(Talent.Art.beruf);
		talent3.setSorte(Talent.Sorte.koerper);
		talent3.setName("Test Talent 3");
		
		// Talent 4:
		talent4 = new Talent("TAL-test-4");
		talent4.setDreiEigenschaften(
				new Eigenschaft[]  {
						(Eigenschaft) data.getCharElement("EIG-IN", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-CH", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-FF", CharKomponente.eigenschaft)
				});
		talent4.setKostenKlasse(KostenKlasse.D);
		talent4.setArt(Talent.Art.beruf);
		talent4.setSorte(Talent.Sorte.kampf);
		talent4.setName("Test Talent 4");
		
		// Talent 5:
		talent5 = new Talent("TAL-test-5");
		talent5.setDreiEigenschaften(
				new Eigenschaft[]  {
						(Eigenschaft) data.getCharElement("EIG-IN", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-CH", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-FF", CharKomponente.eigenschaft)
				});
		talent5.setKostenKlasse(KostenKlasse.E);
		talent5.setArt(Talent.Art.beruf);
		talent5.setSorte(Talent.Sorte.koerper);
		talent5.setName("Test Talent 5");
		
		// Talent 6:
		talent6 = new Talent("TAL-test-6");
		talent6.setDreiEigenschaften(
				new Eigenschaft[]  {
						(Eigenschaft) data.getCharElement("EIG-IN", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-CH", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-FF", CharKomponente.eigenschaft)
				});
		talent6.setKostenKlasse(KostenKlasse.F);
		talent6.setArt(Talent.Art.beruf);
		talent6.setSorte(Talent.Sorte.kampf);
		talent6.setName("Test Talent 6");
		
		// Basis Talent 1:
		talentBasis1 = new Talent("TAL-test-basis-1");
		talentBasis1.setDreiEigenschaften(
				new Eigenschaft[]  {
						(Eigenschaft) data.getCharElement("EIG-MU", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-KL", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-GE", CharKomponente.eigenschaft)
				});
		talentBasis1.setKostenKlasse(KostenKlasse.A);
		talentBasis1.setArt(Talent.Art.basis);
		talentBasis1.setSorte(Talent.Sorte.koerper);
		talentBasis1.setName("Test Basis Talent 1");
		
		// Basis Talent 2:
		talentBasis2 = new Talent("TAL-test-basis-2");
		talentBasis2.setDreiEigenschaften(
				new Eigenschaft[]  {
						(Eigenschaft) data.getCharElement("EIG-MU", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-KL", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-GE", CharKomponente.eigenschaft)
				});
		talentBasis2.setKostenKlasse(KostenKlasse.A);
		talentBasis2.setArt(Talent.Art.basis);
		talentBasis2.setSorte(Talent.Sorte.kampf);
		talentBasis2.setName("Test Basis Talent 2");
		
		// Basis Talent 3:
		talentBasis3 = new Talent("TAL-test-basis-3");
		talentBasis3.setDreiEigenschaften(
				new Eigenschaft[]  {
						(Eigenschaft) data.getCharElement("EIG-MU", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-KL", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-GE", CharKomponente.eigenschaft)
				});
		talentBasis3.setKostenKlasse(KostenKlasse.A);
		talentBasis3.setArt(Talent.Art.basis);
		talentBasis3.setSorte(Talent.Sorte.koerper);
		talentBasis3.setName("Test Basis Talent 3");
	}

	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Testet ob die Methode "canAddSelf" richtig funktioniert, die überprüft ob die SR zum
	 * Helden hinzugefügt werden kann.
	 */
	public void testCanAddSelf() {
		prozessorEigenschaft.updateWert(getLinkEigenschaft(EigenschaftEnum.GE), 9);
		prozessorEigenschaft.updateWert(getLinkEigenschaft(EigenschaftEnum.KO), 10);
		prozessorEigenschaft.updateWert(getLinkEigenschaft(EigenschaftEnum.KK), 11);
		
		// Link zu dem Nachteil erzeugen
		link1 = new IdLink(null, null);
		link1.setZiel(nachteil);
		
		// Prüfen ob hinzugefügt werden darf
		assertTrue(stubenSR.canAddSelf(held, true, link1));
		
		// Ändern der KK auf unzulässigen Wert
		prozessorEigenschaft.updateWert(getLinkEigenschaft(EigenschaftEnum.KK), 12);
		assertFalse(stubenSR.canAddSelf(held, true, link1));
		
		// Wieder auf zulässigen Wert setzen
		prozessorEigenschaft.updateWert(getLinkEigenschaft(EigenschaftEnum.KK), 11);
		assertTrue(stubenSR.canAddSelf(held, true, link1));
		
		// 5 körperliche/ Kampf-Talente, noch zulässig
		prozessorTalent.addNewElement(talent1);
		prozessorTalent.addNewElement(talent2);
		prozessorTalent.addNewElement(talent3);
		prozessorTalent.addNewElement(talent4);
		prozessorTalent.addNewElement(talent5);
		prozessorTalent.updateWert(getLinkTalent(talent1), 1);
		prozessorTalent.updateWert(getLinkTalent(talent2), 1);
		prozessorTalent.updateWert(getLinkTalent(talent3), 1);
		prozessorTalent.updateWert(getLinkTalent(talent4), 1);
		prozessorTalent.updateWert(getLinkTalent(talent5), 1);
		
		prozessorTalent.addNewElement(talentBasis1); // Egal, da keine Kosten
		prozessorTalent.addNewElement(talentBasis2); // Egal, da keine Kosten
		
		assertTrue(stubenSR.canAddSelf(held, true, link1));
		
		// Jetzt nicht mehr zulässig
		prozessorTalent.addNewElement(talent6); // Auch Null kostet, da Aktivierungskosten
		assertFalse(stubenSR.canAddSelf(held, true, link1));
	}
	
	/**
	 * Testet ob die Beschränkungen im Bezug auf die Eigenschaften richtig funktionieren.
	 */
	public void testEigenschaften() {
		// Link zu dem Nachteil erzeugen
		link1 = new IdLink(null, null);
		link1.setZiel(nachteil);
		
		// Sonderregel hinzufügen
		held.getSonderregelAdmin().addSonderregel(link1);
		
		// Jetzt sind die Max Wert (KO, KK, GE) auf 11 begrenzt
		assertEquals(11, prozessorEigenschaft.getMaxWert(getLinkEigenschaft(EigenschaftEnum.GE)));
		assertEquals(11, prozessorEigenschaft.getMaxWert(getLinkEigenschaft(EigenschaftEnum.KO)));
		assertEquals(11, prozessorEigenschaft.getMaxWert(getLinkEigenschaft(EigenschaftEnum.KK)));
		assertEquals(14, prozessorEigenschaft.getMaxWert(getLinkEigenschaft(EigenschaftEnum.FF)));
		
		// Sonderregel entfernen
		held.getSonderregelAdmin().removeSonderregel(link1);
		
		// Normale Begrenzung mehr
		assertEquals(14, prozessorEigenschaft.getMaxWert(getLinkEigenschaft(EigenschaftEnum.GE)));
		assertEquals(14, prozessorEigenschaft.getMaxWert(getLinkEigenschaft(EigenschaftEnum.KO)));
		assertEquals(14, prozessorEigenschaft.getMaxWert(getLinkEigenschaft(EigenschaftEnum.KK)));
		assertEquals(14, prozessorEigenschaft.getMaxWert(getLinkEigenschaft(EigenschaftEnum.FF)));
	}
	
	/**
	 * Testet ob die Stufen der Talente richtige gesetzt/ begrenzt werden
	 */
	public void testTalentStufen() {
		// Link zu dem Nachteil erzeugen
		link1 = new IdLink(null, null);
		link1.setZiel(nachteil);
		
		// Sonderregel hinzufügen
		held.getSonderregelAdmin().addSonderregel(link1);
		
		// Prüfen ob hinzufügbar
		assertTrue(prozessorTalent.canAddElement(talent1));
		
		// Körperliche/Kampf-Talente hinzufügen noch zulässig
		prozessorTalent.addNewElement(talent1);
		prozessorTalent.addNewElement(talent2);
		prozessorTalent.updateWert(getLinkTalent(talent1), 1);
		prozessorTalent.updateWert(getLinkTalent(talent2), 1);
		
		// Maximalwerte Testen
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent1)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent2)));
		
		// Modifikationen hinzufügen
		// Modifikator (durch eine Rasse) erzeugen
		link2 = new IdLink(ras, null);
		link2.setZiel(talent1);
		link2.setWert(3);

		// Modifikator hinzufügen
		prozessorTalent.addModi(link2);
		
		// Maximalwerte Testen
		assertEquals(5, prozessorTalent.getMaxWert(this.getLinkTalent(talent1)));
		assertEquals(2, prozessorTalent.getMaxWert(this.getLinkTalent(talent2)));
		
		// Weitere Körper/Kampf Talente hinzufügen
		prozessorTalent.addNewElement(talent3);
		prozessorTalent.addNewElement(talent4);
		prozessorTalent.addNewElement(talent5);
		prozessorTalent.updateWert(getLinkTalent(talent3), 1);
		prozessorTalent.updateWert(getLinkTalent(talent4), 1);
		prozessorTalent.updateWert(getLinkTalent(talent5), 1);
		prozessorTalent.addNewElement(talentBasis1); // Egal, da keine Kosten
		prozessorTalent.addNewElement(talentBasis2); // Egal, da keine Kosten
		
		// talent1, talentBasis1, talentBasis2 kosten nix! Daher alle anderen Steigerbar!
		assertEquals(5, prozessorTalent.getMaxWert(getLinkTalent(talent1)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent2)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent3)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent4)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent5)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talentBasis1)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talentBasis2)));
		
		// Stufe ändern um 5 Talente mit Kosten zu haben
		prozessorTalent.updateWert(getLinkTalent(talentBasis1), 1);
		
		// Nur die 5 Talente steigerbar, die bereits etwas kosten
		assertEquals(3, prozessorTalent.getMaxWert(getLinkTalent(talent1)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent2)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent3)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent4)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent5)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talentBasis1)));
		assertEquals(0, prozessorTalent.getMaxWert(getLinkTalent(talentBasis2)));

		// Stufen ändern, sollte aber nix am ergebnis verändern!
		prozessorTalent.updateWert(getLinkTalent(talent2), 0);
		prozessorTalent.updateWert(getLinkTalent(talent3), 0);
		
		// Nur die 5 Talente steigerbar, die bereits etwas kosten
		assertEquals(3, prozessorTalent.getMaxWert(getLinkTalent(talent1)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent2)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent3)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent4)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent5)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talentBasis1)));
		assertEquals(0, prozessorTalent.getMaxWert(getLinkTalent(talentBasis2)));
		
		// Modifikator (durch eine Rasse) erzeugen
		link2 = new IdLink(ras, null);
		link2.setZiel(talent6);
		link2.setWert(3);
		
		// Link der KEIN modi ist erzeugen
		link3 = new IdLink(null, null);
		link3.setZiel(talent6);
		link3.setWert(3);		
		
		// Prüfen ob die Links hinzugefügt werden können
		assertTrue(prozessorTalent.canAddElement(link2));
		assertFalse(prozessorTalent.canAddElement(link3));
		
		// Prüfen von "canUpdate"
		assertFalse(prozessorTalent.canUpdateWert(getLinkTalent(talent1)));
		assertTrue(prozessorTalent.canUpdateWert(getLinkTalent(talent2)));
		assertTrue(prozessorTalent.canUpdateWert(getLinkTalent(talentBasis1)));
		assertFalse(prozessorTalent.canUpdateWert(getLinkTalent(talentBasis2)));

		// Ein Talent entfernen, wieder alle Talente um zwei steigerbar!
		prozessorTalent.removeElement(getLinkTalent(talent2));

		// Alle Talente wieder um zwei steigerbar!
		assertEquals(5, prozessorTalent.getMaxWert(getLinkTalent(talent1)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent3)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent4)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent5)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talentBasis1)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talentBasis2)));
		assertTrue(prozessorTalent.canAddElement(link2));
		assertTrue(prozessorTalent.canAddElement(link3));
		
		// Stufen ändern, jetzt kosten wieder 5 Talente etwas!
		prozessorTalent.updateWert(getLinkTalent(talent1), 4);
		
		// Alle Talente wieder um zwei steigerbar!
		assertEquals(5, prozessorTalent.getMaxWert(getLinkTalent(talent1)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent3)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent4)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talent5)));
		assertEquals(2, prozessorTalent.getMaxWert(getLinkTalent(talentBasis1)));
		assertEquals(0, prozessorTalent.getMaxWert(getLinkTalent(talentBasis2)));
		
		// Prüfen von "canUpdate"
		assertTrue(prozessorTalent.canUpdateWert(getLinkTalent(talent3)));
		assertTrue(prozessorTalent.canUpdateWert(getLinkTalent(talentBasis1)));
		assertFalse(prozessorTalent.canUpdateWert(getLinkTalent(talentBasis2)));
	}
	
	/**
	 * Testet ob die Kosten für ein Talent richtig berechnet werden.
	 */
	public void testTalentKosten() {
		// Link zu dem Nachteil erzeugen
		link1 = new IdLink(null, null);
		link1.setZiel(nachteil);
		
		// Körperliche/Kampf-Talente hinzufügen
		prozessorTalent.addNewElement(talent1);
		prozessorTalent.addNewElement(talent2);
		prozessorTalent.addNewElement(talent3);
		prozessorTalent.updateWert(getLinkTalent(talent1), 1);
		prozessorTalent.updateWert(getLinkTalent(talent2), 1);
		prozessorTalent.updateWert(getLinkTalent(talent3), 1);
		
		// Normale Kosten
		assertEquals(2, getLinkTalent(talent1).getKosten());
		assertEquals(4, getLinkTalent(talent2).getKosten());
		assertEquals(5, getLinkTalent(talent3).getKosten());
		
		// Sonderregel hinzufügen
		held.getSonderregelAdmin().addSonderregel(link1);
		
		// Kosten um eine Klasse erhöht
		assertEquals(4, getLinkTalent(talent1).getKosten());
		assertEquals(5, getLinkTalent(talent2).getKosten());
		assertEquals(7, getLinkTalent(talent3).getKosten());
		
		// Talent hinzufügen
		prozessorTalent.addNewElement(talent4);
		prozessorTalent.updateWert(getLinkTalent(talent4), 1);
		assertEquals(9, getLinkTalent(talent4).getKosten());
		
		// Sonderregel entfernen
		held.getSonderregelAdmin().removeSonderregel(link1);
		
		// Normale Kosten
		assertEquals(2, getLinkTalent(talent1).getKosten());
		assertEquals(4, getLinkTalent(talent2).getKosten());
		assertEquals(5, getLinkTalent(talent3).getKosten());
		assertEquals(7, getLinkTalent(talent4).getKosten());

	}
	
// ---------------------------------------------------------------------------------------
	
	/**
	 * Liefert den Link zu dem CharElement zurück
	 * @param enu Die gewünschte Eigenschaft
	 * @return Der Link von ProzessorXX zu der Eigenschaft
	 */
	private GeneratorLink getLinkTalent(CharElement elem) {
		
		return boxTalent.getObjectById(elem);
	}
	
	/**
	 * Liefert den Link zu der Eigenschaft zurück
	 * @param enu Die gerwünschte Eigenschaft
	 * @return Der Link von ProzessorXX zu der Eigenschaft
	 */
	private GeneratorLink getLinkEigenschaft(EigenschaftEnum enu) {
		
		return prozessorEigenschaft.getElementBox().getObjectById(enu.getId());
		
	}
}
