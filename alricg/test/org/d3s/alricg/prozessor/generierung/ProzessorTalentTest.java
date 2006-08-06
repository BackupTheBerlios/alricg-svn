/*
 * Created on 27.06.2005 / 20:44:42
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
import org.d3s.alricg.charKomponenten.Rasse;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.ProgAdmin;
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
 * 
 * @author V. Strelow
 */
public class ProzessorTalentTest extends TestCase {
	private IdLink link1, link2, link3;
	private Talent talent1, talent2, talent3;
	private Rasse ras;
	private Held held;
	private LinkProzessorFront<Talent, ExtendedProzessorTalent, GeneratorLink> prozessor;
	private LinkProzessorFront<Eigenschaft, ExtendedProzessorEigenschaft, GeneratorLink> prozessorEigenschaft;
	private ElementBox<GeneratorLink> box, boxEigenschaft;
	private DataStore data;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		GeneratorLink tmpLink;
		
        // initialisieren
        FactoryFinder.init();
        data = FactoryFinder.find().getData();
        held = new Held();
        held.initGenrierung();
        
        // Nötige Prozessoren "holen"
        prozessor = held.getProzessor(CharKomponente.talent);
        box = prozessor.getElementBox();
        prozessorEigenschaft = held.getProzessor(CharKomponente.eigenschaft);
        boxEigenschaft = prozessorEigenschaft.getElementBox();
        
		// Rassen erzeugen
		ras = new Rasse("RAS-test");
		
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
		tmpLink = prozessor.addNewElement(talent2);
		prozessor.addNewElement(talent3);
		
		prozessor.updateWert(tmpLink, 1);
		
		// Erzeugen der Links
		link1 = new IdLink(null, null);
		link2 = new IdLink(null, null);
		link3 = new IdLink(null, null);
	}
	
	/**
	 * Testet ob das "SetUp" richtig funktioniert hat!
	 */
	public void testRichtigHinzugefuegt() {
		
		link1.setZiel(talent1);
		link2.setZiel(talent2);
		link3.setZiel(talent3);
		
		// Sind die CharElemente im Helden vorhanden?
		assertEquals(true,
				held.getProzessor(CharKomponente.talent).containsLink(link1) 
			);
		assertEquals(true,
				held.getProzessor(CharKomponente.talent).containsLink(link2) 
			);
		assertEquals(true,
				held.getProzessor(CharKomponente.talent).containsLink(link3) 
			);
		
		// Ist die Stufe korrekt übernommen worden?
		assertEquals(0,
				box.getEqualObjects(link1).get(0).getWert()
			);
		assertEquals(1,
				box.getEqualObjects(link2).get(0).getWert() 
			);
		assertEquals(0,
				box.getEqualObjects(link3).get(0).getWert() 
			);
	}
	/**
	 * Testet ob die Kosten für die Talente richtig berechnet werden, sowie das setzen der
	 * Stufen und die Bestimmung der aktivierten Talente.
	 */
	public void testStufeSetzenKostenBerechnen() {
		final ExtendedProzessorTalent extProz;
		extProz = (ExtendedProzessorTalent) prozessor.getExtendedFunctions();
		
		prozessor.updateWert(box.getObjectById(talent1), 3);
		prozessor.updateWert(box.getObjectById(talent2), 5);
		prozessor.updateWert(box.getObjectById(talent3), 7);
		
		// Die Stufe wird voll bezahlt!
		assertEquals(7, box.getObjectById(talent1).getKosten()); // + aktivierungskosten
		assertEquals(61, box.getObjectById(talent2).getKosten()); // Basis-Talent -> keine AK
		assertEquals(631, box.getObjectById(talent3).getKosten()); // + aktivierungskosten
		assertEquals(2, extProz.getAktivierteTalente().size()); // Aktivierte Talente
		
		// Gesamtkosten
		assertEquals(699, prozessor.getGesamtKosten());
		
		// Modifikator (durch eine Rasse) erzeugen
		link1 = new IdLink(ras, null);
		link1.setZiel(talent1);
		link1.setWert(1);
		link2 = new IdLink(ras, null);
		link2.setZiel(talent2);
		link2.setWert(5);
		link3 = new IdLink(ras, null);
		link3.setZiel(talent3);
		link3.setWert(5);
		
		// Modis hinzufügen
		prozessor.addModi(link1);
		prozessor.addModi(link2);
		prozessor.addModi(link3);
		
		assertEquals(3, box.getObjectById(talent1).getWert()); // Stufe
		assertEquals(5, box.getObjectById(talent1).getKosten()); // Kosten mit Modi
		assertEquals(5, box.getObjectById(talent2).getWert()); // Stufe
		assertEquals(0, box.getObjectById(talent2).getKosten()); // Kosten mit Modi
		assertEquals(7, box.getObjectById(talent3).getWert()); // Stufe
		assertEquals(305, box.getObjectById(talent3).getKosten()); // Kosten mit Modi
		assertEquals(0, extProz.getAktivierteTalente().size()); // Aktivierte Talente
		
		// Gesamtkosten
		assertEquals(310, prozessor.getGesamtKosten());
		
		// Stufe neu setzen
		prozessor.updateWert(box.getObjectById(talent1), 4);
		prozessor.updateWert(box.getObjectById(talent2), 7); 
		prozessor.updateWert(box.getObjectById(talent3), 3);
		
		assertEquals(4, box.getObjectById(talent1).getWert()); // Stufe
		assertEquals(9, box.getObjectById(talent1).getKosten()); // Kosten mit Modi
		assertEquals(7, box.getObjectById(talent2).getWert()); // Stufe
		assertEquals(60, box.getObjectById(talent2).getKosten()); // Kosten mit Modi
		assertEquals(5, box.getObjectById(talent3).getWert()); // Stufe (min möglich)
		assertEquals(0, box.getObjectById(talent3).getKosten()); // Kosten mit Modi
		assertEquals(0, extProz.getAktivierteTalente().size()); // Aktivierte Talente
		
		// Gesamtkosten
		assertEquals(69, prozessor.getGesamtKosten());
		
		// Modis wieder entfernen
		prozessor.removeModi(box.getObjectById(talent1), link1);
		prozessor.removeModi(box.getObjectById(talent2), link2);
		prozessor.removeModi(box.getObjectById(talent3), link3);
		
		assertEquals(3, box.getObjectById(talent1).getWert()); // Stufe 
		assertEquals(7, box.getObjectById(talent1).getKosten()); // Kosten + Aktivierungskosten!
		assertEquals(2, box.getObjectById(talent2).getWert()); // Stufe
		assertEquals(10, box.getObjectById(talent2).getKosten()); // Kosten (Basis, keine AK)
		assertNull(box.getObjectById(talent3)); // entfernt
		assertEquals(1, extProz.getAktivierteTalente().size()); // Aktivierte Talente
		
		// Gesamtkosten
		assertEquals(17, prozessor.getGesamtKosten());
		
		// Enfternen von Talent1, Stufe auf "0" setzen bei Talent2
		prozessor.removeElement(box.getObjectById(talent1));
		prozessor.updateWert(box.getObjectById(talent2), 0);
		
		assertNull(box.getObjectById(talent1)); // entfernt
		assertEquals(0, box.getObjectById(talent2).getWert()); // Stufe
		assertEquals(0, box.getObjectById(talent2).getKosten()); // Kosten (Basis, keine AK)
		assertNull(box.getObjectById(talent3)); // entfernt
		assertEquals(0, extProz.getAktivierteTalente().size()); // Aktivierte Talente
		
		// Gesamtkosten
		assertEquals(0, prozessor.getGesamtKosten());
		
		// Hinzufügen von Modis
		prozessor.addModi(link1);
		prozessor.addModi(link2);
		link3.setWert(-3);
		prozessor.addModi(link3);
		
		assertEquals(1, box.getObjectById(talent1).getWert()); // Stufe
		assertEquals(0, box.getObjectById(talent1).getKosten()); // Kosten
		assertEquals(5, box.getObjectById(talent2).getWert()); // Stufe
		assertEquals(0, box.getObjectById(talent2).getKosten()); // Kosten (Basis, keine AK)
		assertEquals(-3, box.getObjectById(talent3).getWert()); // Stufe
		assertEquals(0, box.getObjectById(talent3).getKosten()); // Kosten
		assertEquals(0, extProz.getAktivierteTalente().size()); // Aktivierte Talente
		
		// Gesamtkosten
		assertEquals(0, prozessor.getGesamtKosten());
		
		// Stufen neu setzen
		prozessor.updateWert(box.getObjectById(talent1), 3);
		prozessor.updateWert(box.getObjectById(talent2), 7);
		prozessor.updateWert(box.getObjectById(talent3), 1);
		
		assertEquals(3, box.getObjectById(talent1).getWert()); // Stufe
		assertEquals(5, box.getObjectById(talent1).getKosten()); // Kosten
		assertEquals(7, box.getObjectById(talent2).getWert()); // Stufe
		assertEquals(60, box.getObjectById(talent2).getKosten()); // Kosten (Basis, keine AK)
		assertEquals(1, box.getObjectById(talent3).getWert()); // Stufe
		assertEquals(76, box.getObjectById(talent3).getKosten()); // Kosten (von -3)
		assertEquals(1, extProz.getAktivierteTalente().size()); // Aktivierte Talente
		
		// Gesamtkosten
		assertEquals(141, prozessor.getGesamtKosten() );
		
		// Negative Zahlen 
		prozessor.updateWert(box.getObjectById(talent3), 0);
		
		assertEquals(0, box.getObjectById(talent3).getWert()); // Stufe
		assertEquals(60, box.getObjectById(talent3).getKosten()); // Kosten (von -3)
		assertEquals(1, extProz.getAktivierteTalente().size()); // Aktivierte Talente
		
		prozessor.updateWert(box.getObjectById(talent3), -1);
		
		assertEquals(-1, box.getObjectById(talent3).getWert()); // Stufe
		assertEquals(40, box.getObjectById(talent3).getKosten()); // Kosten (von -3)
		assertEquals(0, extProz.getAktivierteTalente().size()); // Aktivierte Talente
		
		prozessor.updateWert(box.getObjectById(talent3), -3);
		
		assertEquals(-3, box.getObjectById(talent3).getWert()); // Stufe
		assertEquals(0, box.getObjectById(talent3).getKosten()); // Kosten (von -3)
		assertEquals(0, extProz.getAktivierteTalente().size()); // Aktivierte Talente
	}
	
	/**
	 * Testet ob die minimalen und maximalen Werte für ein Talent richtig berechnet werden!
	 */
	public void testMinMaxWerte() {
		
		prozessorEigenschaft.updateWert( // MU auf 10 setzen
				boxEigenschaft.getObjectById(EigenschaftEnum.MU.getId()),
				10);
		prozessorEigenschaft.updateWert( // KL auf 11 setzen
				boxEigenschaft.getObjectById(EigenschaftEnum.KL.getId()),
				11);
		prozessorEigenschaft.updateWert( // GE auf 12 setzen
				boxEigenschaft.getObjectById(EigenschaftEnum.GE.getId()),
				12);
		
		assertEquals(15, prozessor.getMaxWert(box.getObjectById(talent1)));
		assertEquals(0, prozessor.getMinWert(box.getObjectById(talent1)));
		
		// Modifikator (durch eine Rasse) erzeugen
		link1 = new IdLink(ras, null);
		link1.setZiel(talent1);
		link1.setWert(3);
		
		// Modi hinzufügen
		prozessor.addModi(link1);
		
		assertEquals(15, prozessor.getMaxWert(box.getObjectById(talent1)));
		assertEquals(3, prozessor.getMinWert(box.getObjectById(talent1)));
		
		prozessorEigenschaft.updateWert( // GE auf 9 setzen
				boxEigenschaft.getObjectById(EigenschaftEnum.GE.getId()),
				9);
		
		assertEquals(14, prozessor.getMaxWert(box.getObjectById(talent1)));
		assertEquals(3, prozessor.getMinWert(box.getObjectById(talent1)));
		
		// Modi wieder entfernen
		prozessor.removeModi(box.getObjectById(talent1), link1);
		prozessor.addNewElement(talent1);
		
		assertEquals(14, prozessor.getMaxWert(box.getObjectById(talent1)));
		assertEquals(0, prozessor.getMinWert(box.getObjectById(talent1)));
	}
	
	/**
	 * Testet ob richtig bestimmt wird ob ein Talent hinzugefügt werden darf!
	 */
	public void testCanAddAsNewElement() {
		Talent tmpTalent;
		
		// 3. nicht Basis Talent hinzufügen
		tmpTalent = new Talent("TAL-test-4");
		tmpTalent.setDreiEigenschaften(
				new Eigenschaft[]  {
						(Eigenschaft) data.getCharElement("EIG-MU", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-KL", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-GE", CharKomponente.eigenschaft)
				});
		tmpTalent.setKostenKlasse(KostenKlasse.A);
		tmpTalent.setArt(Talent.Art.spezial);
		tmpTalent.setSorte(Talent.Sorte.koerper);
		tmpTalent.setName("Test Talent 4");
		
		// Darf das Talent hinzugefügt werden?
		assertTrue(prozessor.canAddElement(tmpTalent));
		
		prozessor.addNewElement(tmpTalent);
		
		// Jetzt darf es nicht mehr hinzugefügt werden, da schon vorhanden!
		assertFalse(prozessor.canAddElement(tmpTalent));
		
		// 4. nicht Basis Talent hinzufügen
		tmpTalent = new Talent("TAL-test-5");
		tmpTalent.setDreiEigenschaften(
				new Eigenschaft[]  {
						(Eigenschaft) data.getCharElement("EIG-MU", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-KL", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-GE", CharKomponente.eigenschaft)
				});
		tmpTalent.setKostenKlasse(KostenKlasse.A);
		tmpTalent.setArt(Talent.Art.spezial);
		tmpTalent.setSorte(Talent.Sorte.koerper);
		tmpTalent.setName("Test Talent 5");
		
		prozessor.addNewElement(tmpTalent);
		
		// 5. nicht Basis Talent hinzufügen
		tmpTalent = new Talent("TAL-test-6");
		tmpTalent.setDreiEigenschaften(
				new Eigenschaft[]  {
						(Eigenschaft) data.getCharElement("EIG-MU", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-KL", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-GE", CharKomponente.eigenschaft)
				});
		tmpTalent.setKostenKlasse(KostenKlasse.A);
		tmpTalent.setArt(Talent.Art.spezial);
		tmpTalent.setSorte(Talent.Sorte.koerper);
		tmpTalent.setName("Test Talent 6");
		
		// Darf das Talent hinzugefügt werden?
		assertTrue(prozessor.canAddElement(tmpTalent));
		
		prozessor.addNewElement(tmpTalent);
		
		// 6. nicht Basis Talent hinzufügen
		tmpTalent = new Talent("TAL-test-6");
		tmpTalent.setDreiEigenschaften(
				new Eigenschaft[]  {
						(Eigenschaft) data.getCharElement("EIG-MU", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-KL", CharKomponente.eigenschaft),
						(Eigenschaft) data.getCharElement("EIG-GE", CharKomponente.eigenschaft)
				});
		tmpTalent.setKostenKlasse(KostenKlasse.A);
		tmpTalent.setArt(Talent.Art.spezial);
		tmpTalent.setSorte(Talent.Sorte.koerper);
		tmpTalent.setName("Test Talent 6");
		
		// Es dürfen nur 5 Talente aktiviert werden, daher dies nicht mehr!
		assertFalse(prozessor.canAddElement(tmpTalent));
	}
	
}
