/*
 * Created on 13.05.2005 / 22:12:04
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.sonderregeln;

import junit.framework.TestCase;

import org.d3s.alricg.charKomponenten.Eigenschaft;
import org.d3s.alricg.charKomponenten.EigenschaftEnum;
import org.d3s.alricg.charKomponenten.Rasse;
import org.d3s.alricg.charKomponenten.Vorteil;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.sonderregeln.HerausragendeEigenschaft;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.LinkProzessorFront;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.elementBox.ElementBox;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorEigenschaft;
import org.d3s.alricg.store.DataStore;
import org.d3s.alricg.store.FactoryFinder;

/**
 * <u>Beschreibung:</u><br> 
 *
 * @author V. Strelow
 */
public class HerausragendeEigenschaftTest extends TestCase {
    private LinkProzessorFront<Eigenschaft, ExtendedProzessorEigenschaft, GeneratorLink> prozessor;
    private ElementBox<GeneratorLink> box;
    private Held held;
	private Vorteil v, v2;
	private Rasse r;
	private HerausragendeEigenschaft herausEigen;
	private IdLink link, link2;
    private DataStore data;
	
	/* (non-Javadoc) Methode überschrieben
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
		
		v = new Vorteil("VOR-jUnit-test");
		v2 = new Vorteil("VOR-jUnit-test2");
		r = new Rasse("RAS-jUnit-test");
		herausEigen = new HerausragendeEigenschaft();
	}

	/**
	 * Liefert den Link zu der Eigenschaft zurück
	 * @param enu Die gerwünschte Eigenschaft
	 * @return Der Link von ProzessorXX zu der Eigenschaft
	 */
	private GeneratorLink getLink(EigenschaftEnum enu) {
		return  box.getObjectById(enu.getId());
	}
	
	/**
	 * Testet das eine Herausragende Eigenschaft NICHT hinzugefügt werden darf, 
	 * wenn bereits die Eigenschaft einen negativen Modi besitzt.
	 */
	public void testCanAddSelf() {
		prozessor.updateWert(getLink(EigenschaftEnum.KO), 10);
		
		// Der Link der die Sonderregel enthält
		v.setSonderregel( herausEigen );
		link = new IdLink(null, null);
		link.setZiel(v);
		link.setZweitZiel( data.getCharElement(EigenschaftEnum.KO.getId()) );
		
		// Simulation einer Rasse mit Modiikation von "-1" auf KO
		link2 = new IdLink(r, null);
		link2.setZiel(data.getCharElement(EigenschaftEnum.KO.getId()));
		link2.setWert(-1);
		
		// Prüfung durch die SR selbst
		assertTrue( herausEigen.canAddSelf(held, true, link) );
		
		// MaxWert und MinWert sind normal
		assertEquals(14, prozessor.getMaxWert(getLink(EigenschaftEnum.KO)));
		assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.KO)));
		
		// Link mit negativ Modi hinzufügen, nun darf die SR nicht mehr hinzugefügt werden!
		getLink(EigenschaftEnum.KO).addLink(link2);
		
		// Prüfung durch die SR selbst
		assertFalse( herausEigen.canAddSelf( held, true, link) );
		
		// MaxWert und MinWert um eins verringert
		assertEquals(13, prozessor.getMaxWert(getLink(EigenschaftEnum.KO)));
		assertEquals(7, prozessor.getMinWert(getLink(EigenschaftEnum.KO)));
		
		// Entfernen des Modi, nun darf die SR wieder hinzugefügt werden
		getLink(EigenschaftEnum.KO).removeLinkByQuelle(r);
		
		// MaxWert und MinWert sind normal
		assertEquals(14, prozessor.getMaxWert(getLink(EigenschaftEnum.KO)));
		assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.KO)));
		
		// Prüfung durch die SR selbst
		assertTrue( herausEigen.canAddSelf(held, true, link) );
		
		// MaxWert und MinWert sind normal
		assertEquals(14, prozessor.getMaxWert(getLink(EigenschaftEnum.KO)));
		assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.KO)));
	}

	/**
	 * Testet das hinzufügen und entfernen der Sonderregel.
	 */
	public void testSonderregelMitAdmin() {
		prozessor.updateWert(getLink(EigenschaftEnum.KL), 10);
		prozessor.updateWert(getLink(EigenschaftEnum.IN), 9);
		
		// Der Link der die Sonderregel enthält
		v.setSonderregel( herausEigen );
		link = new IdLink(null, null);
		link.setZiel(v);
		link.setWert(2); // Wert 2 -> Also Eigenschaft +2
		link.setZweitZiel( data.getCharElement(EigenschaftEnum.KL.getId()) );
		
		// MaxWert und MinWert sind normal; Aktuell 10
		assertEquals(14, prozessor.getMaxWert(getLink(EigenschaftEnum.KL)));
		assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.KL)));
		assertEquals(10, getLink(EigenschaftEnum.KL).getWert());
		
		// SR kann zum Helden hinzugefügt werden
		assertTrue( herausEigen.canAddSelf(held, true, link) );
		
		// Sonderregel zum Helden hinzufügen
		held.getSonderregelAdmin().addSonderregel(link);
		
		// SR kann nun nichtmehr zum helden hinzugefügt werden, da schon vorhanden
		assertFalse( herausEigen.canAddSelf(held, true, link) );
		
		// MaxWert und MinWert sind nun 16; Aktuell 16
		assertEquals(16, prozessor.getMaxWert(getLink(EigenschaftEnum.KL)));
		assertEquals(16, prozessor.getMinWert(getLink(EigenschaftEnum.KL)));
		assertEquals(16, getLink(EigenschaftEnum.KL).getWert());
		
		// Prüfen ob andere Eigenschaften unbeinflußt bleiben
		assertEquals(14, prozessor.getMaxWert(getLink(EigenschaftEnum.IN)));
		assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.IN)));
		assertEquals(9, getLink(EigenschaftEnum.IN).getWert());
		
		// Sonderregel wieder entfernen
		held.getSonderregelAdmin().removeSonderregel(link);
		
		// MaxWert und MinWert sind normal; Aktuell 14
		assertEquals(14, prozessor.getMaxWert(getLink(EigenschaftEnum.KL)));
		assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.KL)));
		assertEquals(14, getLink(EigenschaftEnum.KL).getWert());
		
		// Es sollte nun keine Sonderregel mehr aktiv sein
		assertEquals(0, held.getSonderregelAdmin().countSonderregeln());
		
	}

	/**
	 * Testet das Hinzufügen und entfernen von zwei Sonderregeln
	 */
	public void testZweiSonderregelMitAdmin() {
		prozessor.updateWert(getLink(EigenschaftEnum.MU), 10);
		prozessor.updateWert(getLink(EigenschaftEnum.FF), 9);
		
		// Der Link der die Sonderregel enthält
		v.setSonderregel( herausEigen );
		link = new IdLink(null, null);
		link.setZiel(v);
		link.setWert(1); // Wert 1 -> Also Eigenschaft +1
		link.setZweitZiel( data.getCharElement(EigenschaftEnum.MU.getId()) );
		
		// Der Link2 der die Sonderregel enthält
		v2.setSonderregel( herausEigen );
		link2 = new IdLink(null, null);
		link2.setZiel(v);
		link2.setWert(2); // Wert 2 -> Also Eigenschaft 2
		link2.setZweitZiel(data.getCharElement(EigenschaftEnum.FF.getId()) );
		
		// MaxWert und MinWert sind normal
		assertEquals(14, prozessor.getMaxWert(getLink(EigenschaftEnum.MU)));
		assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.MU)));
		assertEquals(10, getLink(EigenschaftEnum.MU).getWert());
		assertEquals(14, prozessor.getMaxWert(getLink(EigenschaftEnum.FF)));
		assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.FF)));
		assertEquals(9, getLink(EigenschaftEnum.FF).getWert());
		
		// Sonderregel zum Helden hinzufügen
		held.getSonderregelAdmin().addSonderregel(link);
		held.getSonderregelAdmin().addSonderregel(link2);
		
		// Es sollte nun zwei Sonderregeln aktiv sein
		assertEquals(2, held.getSonderregelAdmin().countSonderregeln());
		
		// MaxWert und MinWert sind nun 15 bzw. 16;
		assertEquals(15, prozessor.getMaxWert(getLink(EigenschaftEnum.MU)));
		assertEquals(15, prozessor.getMinWert(getLink(EigenschaftEnum.MU)));
		assertEquals(15, getLink(EigenschaftEnum.MU).getWert());
		assertEquals(16, prozessor.getMaxWert(getLink(EigenschaftEnum.FF)));
		assertEquals(16, prozessor.getMinWert(getLink(EigenschaftEnum.FF)));
		assertEquals(16, getLink(EigenschaftEnum.FF).getWert());
		
		// Entfernen der Sonderregel auf Mut
		held.getSonderregelAdmin().removeSonderregel(link);
		
		// Es sollte nun eine Sonderregeln aktiv sein
		assertEquals(1, held.getSonderregelAdmin().countSonderregeln());

		// MaxWert und MinWert sind nun normal bzw. 16;
		assertEquals(14, prozessor.getMaxWert(getLink(EigenschaftEnum.MU)));
		assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.MU)));
		assertEquals(14, getLink(EigenschaftEnum.MU).getWert());
		assertEquals(16, prozessor.getMaxWert(getLink(EigenschaftEnum.FF)));
		assertEquals(16, prozessor.getMinWert(getLink(EigenschaftEnum.FF)));
		assertEquals(16, getLink(EigenschaftEnum.FF).getWert());
		
		// Wieder hinzufügen der Sonderregel, diesmal mit "+3"
		link.setWert(3);
		held.getSonderregelAdmin().addSonderregel(link);
		
		// MaxWert und MinWert sind nun 17 bzw. 16;
		assertEquals(17, prozessor.getMaxWert(getLink(EigenschaftEnum.MU)));
		assertEquals(17, prozessor.getMinWert(getLink(EigenschaftEnum.MU)));
		assertEquals(17, getLink(EigenschaftEnum.MU).getWert());
		assertEquals(16, prozessor.getMaxWert(getLink(EigenschaftEnum.FF)));
		assertEquals(16, prozessor.getMinWert(getLink(EigenschaftEnum.FF)));
		assertEquals(16, getLink(EigenschaftEnum.FF).getWert());
		
		// Die Stufen können nun nicht mehr geändert werden
		assertFalse(prozessor.canUpdateWert(getLink(EigenschaftEnum.MU)));
		assertFalse(prozessor.canUpdateWert(getLink(EigenschaftEnum.FF)));
		
		// Entfernen der beiden Sonderregeln
		held.getSonderregelAdmin().removeSonderregel(link);
		held.getSonderregelAdmin().removeSonderregel(link2);
		
		// Es sollte nun keine Sonderregeln aktiv sein
		assertEquals(0, held.getSonderregelAdmin().countSonderregeln());
		
		// MaxWert und MinWert sind normal
		assertEquals(14, prozessor.getMaxWert(getLink(EigenschaftEnum.MU)));
		assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.MU)));
		assertEquals(14, getLink(EigenschaftEnum.MU).getWert());
		assertEquals(14, prozessor.getMaxWert(getLink(EigenschaftEnum.FF)));
		assertEquals(8, prozessor.getMinWert(getLink(EigenschaftEnum.FF)));
		assertEquals(14, getLink(EigenschaftEnum.FF).getWert());
		
		// Die Stufen können wieder geändert werden
		assertEquals(true,	prozessor.canUpdateWert(getLink(EigenschaftEnum.MU)));
		assertEquals(true,	prozessor.canUpdateWert(getLink(EigenschaftEnum.FF)));
		
		// Setzen der Werte
		prozessor.updateWert(getLink(EigenschaftEnum.MU), 10);
		prozessor.updateWert(getLink(EigenschaftEnum.FF), 9);
		
		// Werte überprüfen
		assertEquals(10, getLink(EigenschaftEnum.MU).getWert());
		assertEquals(9, getLink(EigenschaftEnum.FF).getWert());
	}

}
