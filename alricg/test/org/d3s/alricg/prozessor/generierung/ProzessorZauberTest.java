package org.d3s.alricg.prozessor.generierung;

import org.d3s.alricg.charKomponenten.Zauber;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.LinkProzessorFront;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorZauber;
import org.d3s.alricg.store.DataStore;
import org.d3s.alricg.store.FactoryFinder;

import junit.framework.TestCase;

public class ProzessorZauberTest extends TestCase {
	
	LinkProzessorFront<Zauber, ExtendedProzessorZauber, GeneratorLink> prozessor;
	ExtendedProzessorZauber extendedProzessor;
	
	protected void setUp() throws Exception {
		super.setUp();
		
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
