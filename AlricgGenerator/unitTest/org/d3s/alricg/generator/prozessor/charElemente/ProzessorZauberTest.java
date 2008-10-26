package org.d3s.alricg.generator.prozessor.charElemente;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.charakter.ElementBox;
import org.d3s.alricg.common.charakter.CharStatusAdmin;
import org.d3s.alricg.common.charakter.SonderregelAdmin;
import org.d3s.alricg.common.charakter.VerbilligteFertigkeitAdmin;
import org.d3s.alricg.common.logic.Prozessor;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.GeneratorMagieStatusAdmin;
import org.d3s.alricg.generator.prozessor.ProzessorDecorator;
import org.d3s.alricg.generator.prozessor.VoraussetzungenGeneratorAdmin;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorZauber;
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.MagieMerkmal;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.Repraesentation;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.Werte.MagieMerkmalEnum;
import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.held.CharakterDaten;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

public class ProzessorZauberTest {
	
	private Charakter charakter;
	private ProzessorDecorator<Zauber, GeneratorLink> prozessor;
	private ExtendedProzessorZauber extendedProzessor;
	private ElementBox< GeneratorLink > box;
	
	private Repraesentation repraesentation; 
	
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
		charakter.setProzessorHash(hash);
		hash.put(
				Eigenschaft.class,
				new ProzessorDecorator(charakter, new ProzessorEigenschaften(charakter)));
		hash.put(
				Talent.class,
				new ProzessorDecorator(charakter, new ProzessorTalent(charakter)));
		hash.put(
				Zauber.class,
				new ProzessorDecorator(charakter, new ProzessorZauber(charakter)));
		hash.put(
				Sonderfertigkeit.class,
				new ProzessorDecorator(charakter, new ProzessorSonderfertigkeit(charakter)));
		hash.put(
				Vorteil.class,
				new ProzessorDecorator(charakter, new ProzessorVorteil(charakter)));
   
		
		prozessor = (ProzessorDecorator) charakter.getProzessor( Zauber.class );
		box = prozessor.getElementBox();
		extendedProzessor = (ExtendedProzessorZauber) prozessor.getExtendedInterface();
		
		// Repräsentation setzen
		Sonderfertigkeit sf = new Sonderfertigkeit();
		sf.setId("SFK-RepTest");
		
		repraesentation = new Repraesentation();
		repraesentation.setId("REP-test-1");
		charakter.getStatusAdmin().getRepraesentationen().add(repraesentation);
		
		Link link = ((ProzessorDecorator) charakter.getProzessor( Sonderfertigkeit.class )).addNewElement(sf);
		((ProzessorDecorator) charakter.getProzessor( Sonderfertigkeit.class )).updateZweitZiel(link, repraesentation);
		
		//charakter.setRepraesentationen( new Repraesentation[]{ repraesentation } );
		
	}
	
	/** 
	 * Prueft ob die Liste der Hauszauber nach dem erzeugen des Helden leer 
	 * ist. 
	 */
	@Test public void testHauszauberZuBeginnLeer() {
		
		int anzahlHauszauber = extendedProzessor.getHauszauber().size();
		
		assertEquals( 0, anzahlHauszauber );
	}
	
	/**
	 * Legt mittels der setHauszauber Methode eine Anzahl Hauszauber fest.
	 */
	@Test public void testHauszauberSetzen() {
		
		final int ANZAHL_HAUSZAUBER = 13;
		
		Collection< Link > hauszauber = erzeugeZauberLinks( ANZAHL_HAUSZAUBER );
		
		extendedProzessor.setHauszauber( hauszauber );
		
		int anzahlHauszauber = extendedProzessor.getHauszauber().size();
		
		assertEquals( ANZAHL_HAUSZAUBER, anzahlHauszauber );
		for ( Link link : extendedProzessor.getHauszauber() ) {
			assertTrue( hauszauber.contains( link ) );
		}
	}

	/**
	 * Legt mittels der setHauszauber Methode eine Anzahl Hauszauber fest, 
	 * nachdem zuvor schon einemal die Hauszauber festgelegt wurden.
	 */
	@Test public void testHauszauberNeuSetzen() {
		
		final int ANZAHL_HAUSZAUBER = 13;
		
		final int ANZAHL_HAUSZAUBER_NEU = 10;
		
		Collection< Link > hauszauber = erzeugeZauberLinks( ANZAHL_HAUSZAUBER );
		extendedProzessor.setHauszauber( hauszauber );
		
		Collection< Link > hauszauberNeu = erzeugeZauberLinks( ANZAHL_HAUSZAUBER_NEU );
		
		extendedProzessor.setHauszauber( hauszauberNeu );
		
		int anzahlHauszauberNeu = extendedProzessor.getHauszauber().size();
		
		assertEquals( ANZAHL_HAUSZAUBER_NEU, anzahlHauszauberNeu );
		for ( Link link : extendedProzessor.getHauszauber() ) {
			assertTrue( hauszauberNeu.contains( link ) );
		}
	}
	
	/**
	 * Fuegt mit der addHauszauber-Methode einen einzelnen Zauber hinzu.
	 */
	@Test public void testAddHauszauber() {

		final int ANZAHL_HAUSZAUBER = 22;
		
		Collection< Link > hauszauber = erzeugeZauberLinks( ANZAHL_HAUSZAUBER );
		extendedProzessor.setHauszauber( hauszauber );
		
		Link zauberLink = erzeugeZauberLink( erzeugeZauber( "ZAU-add" ), "REP-add" );
		
		extendedProzessor.addHauszauber( zauberLink );
		
		assertTrue( extendedProzessor.getHauszauber().contains( zauberLink ) );
	}

	/**
	 * Entfernt mit der removeHauszauber-Methode einen einzelnen Zauber.
	 */
	@Test public void testRemoveHauszauber() {

		final int ANZAHL_HAUSZAUBER = 22;
		
		Collection< Link > hauszauber = erzeugeZauberLinks( ANZAHL_HAUSZAUBER );
		extendedProzessor.setHauszauber( hauszauber );
		
		Link zauberLink = erzeugeZauberLink( erzeugeZauber( "ZAU-remove" ), "REP-test" );
		
		extendedProzessor.addHauszauber( zauberLink );
		
		extendedProzessor.removeHauszauber( zauberLink );
		
		assertFalse( extendedProzessor.getHauszauber().contains( zauberLink ) );
	}
	
	/**
	 * Setzt die moeglichen Zauber mittels der setMoeglicheZauber-Methode.
	 */
	@Test public void testMoeglicheZauberSetzen() {
		
		final int ANZAHL_MOEGLICHE_ZAUBER = 10;
		
		Collection< Link > moeglicheZauber = erzeugeZauberLinks( ANZAHL_MOEGLICHE_ZAUBER );
		extendedProzessor.setMoeglicheZauber( moeglicheZauber );
		
		assertEquals( ANZAHL_MOEGLICHE_ZAUBER, extendedProzessor.getMoeglicheZauber().size() );
		for ( Link zauber : moeglicheZauber ) {
			assertTrue( extendedProzessor.getMoeglicheZauber().contains( zauber ) );
		}
	}
	
	/**
	 * Aendert die moeglichen Zauber mittels der setMoeglicheZauber-Methode.
	 */
	@Test public void testMoeglicheZauberNeuSetzen() {
		
		final int ANZAHL_MOEGLICHE_ZAUBER = 10;
		
		final int ANZAHL_MOEGLICHE_ZAUBER_NEU = 14;
		
		Collection< Link > moeglicheZauber = erzeugeZauberLinks( ANZAHL_MOEGLICHE_ZAUBER );
		extendedProzessor.setMoeglicheZauber( moeglicheZauber );

		Collection< Link > moeglicheZauberNeu = erzeugeZauberLinks( ANZAHL_MOEGLICHE_ZAUBER_NEU );
		extendedProzessor.setMoeglicheZauber( moeglicheZauberNeu );
		
		assertEquals( ANZAHL_MOEGLICHE_ZAUBER_NEU, extendedProzessor.getMoeglicheZauber().size() );
		for ( Link zauber : moeglicheZauberNeu ) {
			assertTrue( extendedProzessor.getMoeglicheZauber().contains( zauber ) );
		}
	}
	
	/**
	 * Prueft ob ein neuer Zauber korrekt eingefuegt wird.
	 */
	@Test public void testZauberEinfuegen() {
		
		final int ANZAHL_MOEGLICHE_ZAUBER = 17;
		
		final Zauber zauber1 = erzeugeZauber( "ZAU-test-1", KostenKlasse.A, MagieMerkmalEnum.antimagie.getMagieMerkmal() );

		Link zauberLink1 = new IdLink( null, zauber1, repraesentation, Link.KEIN_WERT, null );
		// Evtl. auch Wert = 0?
		
		Collection< Link > moeglicheZauber = erzeugeZauberLinks( ANZAHL_MOEGLICHE_ZAUBER );
		moeglicheZauber.add( zauberLink1 );
		extendedProzessor.setMoeglicheZauber( moeglicheZauber );
		
		prozessor.addNewElement( zauber1 );
		
		assertTrue( prozessor.containsLink( zauberLink1 ) );
		assertEquals( 0, box.getEqualObjects( zauberLink1 ).get( 0 ).getWert() );
		// Aktivierungskosten 1 AP
		assertEquals( 1, box.getEqualObjects( zauberLink1 ).get( 0 ).getKosten() );
		assertTrue( box.getEqualObjects( zauberLink1 ).get( 0 ).getZweitZiel() == repraesentation );
	}

	/**
	 * Prueft ob sich die Stufe aendern laesst und die Kosten richtig berechnet werden.
	 */
	@Test public void testStufeAendern() {
		
		final int ANZAHL_MOEGLICHE_ZAUBER = 17;
		
		Zauber zauber1 = erzeugeZauber( "ZAU-test-1", KostenKlasse.B, MagieMerkmalEnum.antimagie.getMagieMerkmal() );
		
		Link zauberLink1 = new IdLink( null, zauber1, repraesentation, Link.KEIN_WERT, null );
		
		Collection< Link > moeglicheZauber = erzeugeZauberLinks( ANZAHL_MOEGLICHE_ZAUBER );
		moeglicheZauber.add( zauberLink1 );
		extendedProzessor.setMoeglicheZauber( moeglicheZauber );
		
		prozessor.addNewElement( zauber1 );
		
		prozessor.updateWert( box.getObjectById(zauber1), 5 );
		
		assertEquals( 33, box.getEqualObjects( zauberLink1 ).get( 0 ).getKosten() );
	}

	/**
	 * Prueft ob sich ein Modifikator setzen laesst und ob die Kosten korrekt
	 * berechnet werden.
	 */
	@Test public void testModiSetzen() {
		
		final int ANZAHL_MOEGLICHE_ZAUBER = 17;
		
		Zauber zauber1 = erzeugeZauber( "ZAU-test-1", KostenKlasse.B, MagieMerkmalEnum.antimagie.getMagieMerkmal());
		
		Link zauberLink1 = new IdLink( null, zauber1, repraesentation, Link.KEIN_WERT, null );
		
		Collection< Link > moeglicheZauber = erzeugeZauberLinks( ANZAHL_MOEGLICHE_ZAUBER );
		moeglicheZauber.add( zauberLink1 );
		extendedProzessor.setMoeglicheZauber( moeglicheZauber );
		
		prozessor.addNewElement( zauber1 );
		
		Rasse rasse = new Rasse();
		rasse.setId("RAS-test");
		IdLink modi = new IdLink( rasse, zauber1, null, 5, null );
		prozessor.addModi( modi );
		
		assertEquals( 5, box.getEqualObjects( zauberLink1 ).get( 0 ).getWert() );
		assertEquals( 0, box.getEqualObjects( zauberLink1 ).get( 0 ).getKosten() );
		
		
		
		Zauber zauber2 = erzeugeZauber( "ZAU-test-2", KostenKlasse.B, MagieMerkmalEnum.antimagie.getMagieMerkmal());
		IdLink modi2 = new IdLink( rasse, zauber2, repraesentation, 3, null );
		
		GeneratorLink genLink = (GeneratorLink) prozessor.addModi( modi2 );
		
		assertEquals( 3, genLink.getWert() );
		assertEquals( 0, genLink.getKosten() );

		
		
	}
	
	/**
	 * Prueft ob die Kosten korrekt berechnet werden wenn eine Stufe und ein
	 * Modifikator gesetzt sind.
	 */
	@Test public void testModiUndStufeSetzen() {

		final int ANZAHL_MOEGLICHE_ZAUBER = 17;
		
		Zauber zauber1 = erzeugeZauber( "ZAU-test-1", KostenKlasse.C, MagieMerkmalEnum.antimagie.getMagieMerkmal() );
		
		Repraesentation rep = new Repraesentation(  );
		rep.setId("REP-test-2");
		Link zauberLink1 = new IdLink( null, zauber1, rep, Link.KEIN_WERT, null );
		
		Collection< Link > moeglicheZauber = erzeugeZauberLinks( ANZAHL_MOEGLICHE_ZAUBER );
		moeglicheZauber.add( zauberLink1 );
		extendedProzessor.setMoeglicheZauber( moeglicheZauber );
		
		prozessor.addNewElement( zauber1 );

		prozessor.updateWert( box.getObjectById(zauber1), 7 );
		
		Rasse rasse = new Rasse();
		rasse.setId("RAS-test");
		IdLink modi = new IdLink( rasse, zauber1, null, 3, null );
		prozessor.addModi( modi );
		
		// Als Zauber fremder Repraesentation wird nach Spalte E gesteigert.
		assertEquals( 124, box.getEqualObjects( zauberLink1 ).get( 0 ).getKosten() );
	}
	
	/**
	 * Prueft ob der Maximal- und Minmalwert fuer die Zauber korrekt bestimmt
	 * werden.
	 */
	@Test public void testMinMaxWert() {

		final int ANZAHL_MOEGLICHE_ZAUBER = 17;
		
		// Wert der Eigenschaften festlegen. 
		ProzessorDecorator<Eigenschaft, GeneratorLink> prozessorEigenschaft;
		prozessorEigenschaft = (ProzessorDecorator) charakter.getProzessor( Eigenschaft.class );

		ElementBox<GeneratorLink> boxEigenschaft = prozessorEigenschaft.getElementBox();;
		
		prozessorEigenschaft.updateWert(
				boxEigenschaft.getObjectById( EigenschaftEnum.MU.getId() ),
				10 );
		prozessorEigenschaft.updateWert(
				boxEigenschaft.getObjectById( EigenschaftEnum.KL.getId() ),
				12 );
		prozessorEigenschaft.updateWert(
				boxEigenschaft.getObjectById( EigenschaftEnum.IN.getId() ),
				14 );
		
		// Zauber erzeugen
		Zauber zauber1 = erzeugeZauber( "ZAU-eigeneRepraesentation", KostenKlasse.C, MagieMerkmalEnum.antimagie.getMagieMerkmal() );
		zauber1.setDreiEigenschaften( new Eigenschaft[]{
				EigenschaftEnum.MU.getEigenschaft(),
				EigenschaftEnum.KL.getEigenschaft(),
				EigenschaftEnum.IN.getEigenschaft()
		} );

		Zauber zauber2 = erzeugeZauber( "ZAU-fremdeRepraesentation", KostenKlasse.C, MagieMerkmalEnum.antimagie.getMagieMerkmal() );
		zauber2.setDreiEigenschaften( new Eigenschaft[]{
				EigenschaftEnum.MU.getEigenschaft(),
				EigenschaftEnum.KL.getEigenschaft(),
				EigenschaftEnum.IN.getEigenschaft()
		} );
		
		// Moegliche Zauber erzeugen.
		Link zauberLink1 = new IdLink( null, zauber1, repraesentation, Link.KEIN_WERT, null );
		zauberLink1.setZiel( zauber1 );
		zauberLink1.setZweitZiel( repraesentation );
		
		repraesentation = new Repraesentation(  );
		repraesentation.setId("REP-fremdeRepraesentation");
		Link zauberLink2 = new IdLink( null, zauber2, repraesentation, Link.KEIN_WERT, null );
		
		Collection< Link > moeglicheZauber = erzeugeZauberLinks( ANZAHL_MOEGLICHE_ZAUBER );
		moeglicheZauber.add( zauberLink1 );
		moeglicheZauber.add( zauberLink2 );
		extendedProzessor.setMoeglicheZauber( moeglicheZauber );
		
		prozessor.addNewElement( zauber1 );
		prozessor.addNewElement( zauber2 );

		assertEquals( 17, prozessor.getMaxWert( box.getObjectById( zauber1 )));
		assertEquals(  0, prozessor.getMinWert( box.getObjectById( zauber1 )));

		assertEquals( 14, prozessor.getMaxWert( box.getObjectById( zauber2 )));
		assertEquals(  0, prozessor.getMinWert( box.getObjectById( zauber2 )));
		
		// Modifikator durch Rasse erzeugen
		Rasse rasse = new Rasse();
		rasse.setId("RAS-Rasse");
		IdLink link = new IdLink(rasse, zauber2, null, 4, null);
		prozessor.addModi( link );
		
		assertEquals( 14, prozessor.getMaxWert( box.getObjectById( zauber2 )));
		assertEquals(  4, prozessor.getMinWert( box.getObjectById( zauber2 )));
	}
	
	/**
	 * Testet ob die maximale Anzahl aktivierbare Zauber eingehalten wird.
	 * Nichtzauberer und Viertelzauberer duerfen keine Zauber aktivieren.
	 * Halbzauberer duerfen 5 Zauber aktivieren.
	 * Vollzauberer duerfen 10 Zauber aktivieren.
	 */
	@Test public void testCanAddElementMaximum() {
		
		Zauber zauberZuViel = erzeugeZauber( "ZAU-ZuViel" );
		
		List< Link > zauberlinks = erzeugeZauberLinks( 10 );
		zauberlinks.add( erzeugeZauberLink( zauberZuViel, "REP-test" ) );
		extendedProzessor.setMoeglicheZauber( zauberlinks );
		
		assertFalse( prozessor.canAddElement( zauberZuViel ) );
		
		// Halbzauberer testen. 
		// Testen ob nur 5 Zauber aktiviert werden koennen.
		macheZuHalbzauberer( charakter );
		
		assertTrue( prozessor.canAddElement( zauberZuViel ) );
		
		for ( int i = 0 ; i < 5 ; i++ ) {
			Zauber zauber = (Zauber) zauberlinks.get( i ).getZiel();
			assertTrue( prozessor.canAddElement( zauber ) );
			prozessor.addNewElement( zauber );
		}
		assertFalse( prozessor.canAddElement( zauberZuViel ) );
		
		// Vollzauberer testen.
		// Testen ob nur 10 Zauber aktiviert werden koennen.
		macheZuVollzauberer( charakter );

		for ( int i = 5 ; i < 10 ; i++ ) {
			Zauber zauber = (Zauber) zauberlinks.get( i ).getZiel();
			assertTrue( prozessor.canAddElement( zauber ) );
			prozessor.addNewElement( zauber );
		}
		assertFalse( prozessor.canAddElement( zauberZuViel ) );
	}
	
	/**
	 * Test: Ein Zauber den ein Held schon besitzt kann nicht hinzugefuegt 
	 * werden.
	 */
	@Test public void testCanAddElementSchonAktivert() {
		
		Collection< Link > moeglicheZauber = erzeugeZauberLinks( 5 );
		
		extendedProzessor.setMoeglicheZauber( moeglicheZauber );
		
		macheZuVollzauberer( charakter );
		
		for ( Link zauberlink : moeglicheZauber ) {
			Zauber zauber = (Zauber) zauberlink.getZiel();
			assertTrue( prozessor.canAddElement( zauber ) );
			prozessor.addNewElement( zauber ); 
			assertFalse( prozessor.canAddElement( zauber ) );
		}
	}
	
	/**
	 * Testet ob nur moegliche Zauber hinzugefuegt werden koennen. 
	 */
	@Test public void testCanAddElementMoeglicheZauber() {
		
		Collection< Link > moeglicheZauber = erzeugeZauberLinks( 7 );
		
		extendedProzessor.setMoeglicheZauber( moeglicheZauber );
		
		macheZuVollzauberer( charakter );
		
		for ( Link zauberlink : moeglicheZauber ) {
			assertTrue( prozessor.canAddElement( (Zauber) zauberlink.getZiel() ) );
		}
		
		Zauber nichtMoeglich = erzeugeZauber( "ZAU-nicht moeglich" );
		assertFalse( prozessor.canAddElement( nichtMoeglich ) );
	}
	
	@Test public void testCanRemoveElement() {
		
		List< Link > moeglicheZauber = erzeugeZauberLinks( 4 );
		
		extendedProzessor.setMoeglicheZauber( moeglicheZauber );
		
		macheZuVollzauberer( charakter );
		
		List< GeneratorLink > generatorLinks = new ArrayList< GeneratorLink >();
		
		for ( Link zauberlink : moeglicheZauber ) {
			generatorLinks.add( prozessor.addNewElement( (Zauber) zauberlink.getZiel() ) );
		}

		Zauber zauber = (Zauber) generatorLinks.get( 0 ).getZiel();
		
		// Modifikator durch Rasse erzeugen
		Rasse rasse = new  Rasse();
		rasse.setId("RAS-Rasse");
		IdLink link = new IdLink(rasse, zauber, null, 4, null);
		prozessor.addModi( link );
		
		assertFalse( prozessor.canRemoveElement( generatorLinks.get( 0 ) ) );
		
		for ( int i = 1 ; i < generatorLinks.size() ; i++ ) {
			assertTrue( prozessor.canRemoveElement( generatorLinks.get( i ) ) );
		}
	}
	
	@Test public void testContainsLink() {

		List< Link > zauberlinks = erzeugeZauberLinks( 12 );
		
		extendedProzessor.setMoeglicheZauber( zauberlinks );
		
		for ( int i = 0 ; i < 6 ; i++ ) {
			prozessor.addNewElement( (Zauber) zauberlinks.get( i ).getZiel() );
			assertTrue( prozessor.containsLink( zauberlinks.get( i ) ) );
			assertFalse( prozessor.containsLink( zauberlinks.get( i+6 ) ) );
		}
		
	}
	
	@Test public void testGetGesamtkosten() {
		
		Zauber zauber1 = erzeugeZauber( "ZAU-test-1", KostenKlasse.A, MagieMerkmalEnum.antimagie.getMagieMerkmal() );
		Zauber zauber2 = erzeugeZauber( "ZAU-test-2", KostenKlasse.B, MagieMerkmalEnum.antimagie.getMagieMerkmal() );
		Zauber zauber3 = erzeugeZauber( "ZAU-test-3", KostenKlasse.C, MagieMerkmalEnum.antimagie.getMagieMerkmal() );
		
		Link zauberLink1 = new IdLink( null, zauber1, repraesentation, Link.KEIN_WERT, null );
		Link zauberLink2 = new IdLink( null, zauber2, repraesentation, Link.KEIN_WERT, null );
		Link zauberLink3 = new IdLink( null, zauber3, repraesentation, Link.KEIN_WERT, null );

		Collection< Link > moeglicheZauber = erzeugeZauberLinks( 10 );
		moeglicheZauber.add( zauberLink1 );
		moeglicheZauber.add( zauberLink2 );
		moeglicheZauber.add( zauberLink3 );
		extendedProzessor.setMoeglicheZauber( moeglicheZauber );

		assertEquals( 0, prozessor.getGesamtKosten() );
		
		prozessor.addNewElement( zauber1 );
		prozessor.addNewElement( zauber2 );
		prozessor.addNewElement( zauber3 );

		assertEquals( 6, prozessor.getGesamtKosten() );
		
		prozessor.updateWert( box.getObjectById( zauber1 ), 5 );
		assertEquals( 22, prozessor.getGesamtKosten() );
		
		prozessor.updateWert( box.getObjectById( zauber2 ), 3 );
		assertEquals( 34, prozessor.getGesamtKosten() );
		
		prozessor.updateWert( box.getObjectById( zauber3 ), 7 );
		assertEquals( 127, prozessor.getGesamtKosten() );
	}
	
	/** Testet die Methode removeElement( GeneratorLink ) */
	@Test public void testRemoveElement() {
		
		Collection< Zauber > zauber = erzeugeZauberliste( 13 );
		
		for ( Zauber z : zauber ) {
			prozessor.addNewElement( z );
		}
		
		for ( GeneratorLink link : new ArrayList<GeneratorLink>( prozessor.getUnmodifiableList() ) ) {
			assertTrue( prozessor.containsLink( link ) );
			prozessor.removeElement( link );
			assertFalse( prozessor.containsLink( link ) );
		}
	}

	/** Testet die Methode removeModi( GeneratorLink, IdLink ). */
	@Test public void testRemoveModi() {
		
		final Zauber zauber = erzeugeZauber( "ZAU-test" );
		
		GeneratorLink link = prozessor.addNewElement( zauber );
		
		// Modifkator durch Rasse erzeugen.
		Rasse rasse = new Rasse();
		rasse.setId("RAS-Rasse");
		IdLink modi1 = new IdLink(rasse, zauber, null , 4, null);
		prozessor.addModi( modi1 );
		
		// Modifkator durch Rasse erzeugen.
		rasse = new Rasse();
		rasse.setId("RAS-Rasse");
		IdLink modi2 = new IdLink(rasse, zauber, null, 3, null);
		prozessor.addModi( modi2 );
		
		assertEquals( 7, link.getWert() );
		
		prozessor.removeModi( link, modi1 );
		
		assertEquals( 3, link.getWert() );
	}
	
	/**
	 * Erzeugt eine Liste von Zaubern
	 */
	private List< Link > erzeugeZauberLinks( final int anzahl ) {
		List< Link > zauberlinks = new ArrayList< Link >();

		for( Zauber zauber : erzeugeZauberliste( anzahl ) ) {
			
			zauberlinks.add( erzeugeZauberLink( zauber, "REP-test" ) );
		}
		
		return zauberlinks;
	}
	
	/**
	 * Erzeugt einen Link fuer einen Zauber mit einer bestimmten 
	 * Repraesentation.
	 * 
	 * @param zauber fuer das Ziel des Links.
	 * @param repraesentationId fuer das Zweit-Ziel des Links.
	 * @return einen Link, mit dem {@code zauber} als Ziel und einer 
	 * 			Repraesentation als Zweit-Ziel. 
	 */
	private Link erzeugeZauberLink( final Zauber zauber, final String repraesentationId ) {
		
		Repraesentation rep = new Repraesentation();
		rep.setId( repraesentationId );
		Link link = new IdLink( null, zauber, rep, Link.KEIN_WERT, null );
		
		return link;
	}
	
	/**
	 * Erzeugt eine Liste von Zauber, die alle eine id der Form
	 * "ZAU-test-" + i haben, wobei i aus 0 .. anzahl-1.
	 * 
	 * @param anzahl der zu erzeugenden Zauber
	 * @return eine Liste der Zauber.
	 */
	private Collection< Zauber > erzeugeZauberliste( int anzahl ) {
		
		Collection< Zauber > zauberliste = new HashSet< Zauber >( anzahl );
		
		for( int i = 0 ; i < anzahl ; i++ ) {
			
			zauberliste.add( erzeugeZauber( "ZAU-test-" + i ) );
			
		}
		
		return zauberliste;
	}

	/**
	 * @param id z.B. "ZAU-test-1"
	 * @param kostenKlasse des Zaubers.
	 * @param magieMerkmale des Zaubers.
	 * @return ein Zauber mit drei zuefaellig gewaehlten Probe-Eigenschaften.
	 */
	private Zauber erzeugeZauber( 
			final String id, 
			final KostenKlasse kostenKlasse, 
			final MagieMerkmal ... magieMerkmale ) {

		Zauber zauber = new Zauber();
		zauber.setId(id);
		zauber.setName( id );
		zauber.setKostenKlasse( kostenKlasse );
		zauber.setMerkmale( magieMerkmale );

		zauber.setDreiEigenschaften( new Eigenschaft[] {
				getEigenschaft(), getEigenschaft(), getEigenschaft()
		} );
		
		return zauber;
	}
	
	/**
	 * @param id z.B. "ZAU-test-1"
	 * @return ein Zauber mit drei zuefaellig gewaehlten Probe-Eigenschaften,
	 * 			Kostenklase A und Merkmal Antimagie.
	 */
	private Zauber erzeugeZauber( final String id ) {

		return erzeugeZauber( id, KostenKlasse.A, MagieMerkmalEnum.antimagie.getMagieMerkmal()  );
	}

	/**
	 * @return eine zufaellig gewaehlte Eigenschaft.
	 */
	private Eigenschaft getEigenschaft() {
		Random random = new Random();
		
		String eigenschaft;
		switch( random.nextInt( 8 ) ) {
		case 0:
			return EigenschaftEnum.MU.getEigenschaft();
			
		case 1:
			return EigenschaftEnum.KL.getEigenschaft();
			
		case 2:
			return EigenschaftEnum.IN.getEigenschaft();
			
		case 3:
			return EigenschaftEnum.CH.getEigenschaft();
			
		case 4:
			return EigenschaftEnum.FF.getEigenschaft();
			
		case 5:
			return EigenschaftEnum.GE.getEigenschaft();
			
		case 6:
			return EigenschaftEnum.KO.getEigenschaft();
			
		default:
			return EigenschaftEnum.KK.getEigenschaft();
			
		}
	}
	
	private void macheZuHalbzauberer( Charakter held ) {
		held.getStatusAdmin().setMagieStatus(CharStatusAdmin.MagieStatus.halbzauberer);
	}
	
	private void macheZuVollzauberer( Charakter held ) {
		held.getStatusAdmin().setMagieStatus(CharStatusAdmin.MagieStatus.vollmagier);
	}
	
}
