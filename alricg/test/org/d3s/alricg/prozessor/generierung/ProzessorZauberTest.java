package org.d3s.alricg.prozessor.generierung;

import org.d3s.alricg.charKomponenten.Zauber;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.LinkProzessorFront;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorZauber;
import org.d3s.alricg.store.DataStore;
import org.d3s.alricg.store.FactoryFinder;
import org.junit.Before;

import junit.framework.TestCase;

public class ProzessorZauberTest extends TestCase {
	
	LinkProzessorFront<Zauber, ExtendedProzessorZauber, GeneratorLink> prozessor;
	ExtendedProzessorZauber extendedProzessor;
	
	@Before public void setUp() throws Exception {
	    // Starten des Programms (sollte eigentlich in eine "@BeforeClass" Methode)
		ProgAdmin.main(new String[] { "noScreen" });
		
		FactoryFinder.init();
        DataStore data = FactoryFinder.find().getData();
        Held held = new Held();
		held.initGenrierung();
		
		prozessor = held.getProzessor( CharKomponente.zauber );
		extendedProzessor = prozessor.getExtendedFunctions();
	}
	
	public void testHauszauberZuBeginnLeer() {
		
		int anzahlHauszauber = extendedProzessor.getHauszauber().size();
		
		assertEquals( 0, anzahlHauszauber );
	}

}
