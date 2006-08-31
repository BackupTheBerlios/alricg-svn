package org.d3s.alricg.prozessor.generierung;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import junit.framework.TestCase;

import org.d3s.alricg.charKomponenten.Eigenschaft;
import org.d3s.alricg.charKomponenten.Repraesentation;
import org.d3s.alricg.charKomponenten.Zauber;
import org.d3s.alricg.charKomponenten.Werte.MagieMerkmal;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.LinkProzessorFront;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.elementBox.ElementBox;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorZauber;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;
import org.d3s.alricg.store.DataStore;
import org.d3s.alricg.store.FactoryFinder;
import org.junit.Before;

public class ProzessorZauberTest extends TestCase {
	
	private DataStore data;
	
	private Held held;
	private LinkProzessorFront<Zauber, ExtendedProzessorZauber, GeneratorLink> prozessor;
	private ExtendedProzessorZauber extendedProzessor;
	private ElementBox< GeneratorLink > box;
	
	@Before public void setUp() throws Exception {
		ProgAdmin.main(new String[] { "noScreen" });
			
		FactoryFinder.init();
        data = FactoryFinder.find().getData();
        held = new Held();
		held.initGenrierung();
		
		prozessor = held.getProzessor( CharKomponente.zauber );
		extendedProzessor = prozessor.getExtendedFunctions();
		box = prozessor.getElementBox();
	}
	
	/** 
	 * Prueft ob die Liste der Hauszauber nach dem erzeugen des Helden leer 
	 * ist. 
	 */
	public void testHauszauberZuBeginnLeer() {
		
		int anzahlHauszauber = extendedProzessor.getHauszauber().size();
		
		assertEquals( 0, anzahlHauszauber );
	}
	
	/**
	 * Legt mittels der setHauszauber Methode eine Anzahl Hauszauber fest.
	 */
	public void testHauszauberSetzen() {
		
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
	public void testHauszauberNeuSetzen() {
		
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
	public void testAddHauszauber() {

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
	public void testRemoveHauszauber() {

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
	public void testMoeglicheZauberSetzen() {
		
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
	public void testMoeglicheZauberNeuSetzen() {
		
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
	
	public void testZauberEinfuegen() {
		
		final int ANZAHL_MOEGLICHE_ZAUBER = 17;
		
		Repraesentation repraesentation = new Repraesentation( "REP-test-1" );
		held.setRepraesentationen( new Repraesentation[]{ repraesentation } );
		
		final Zauber zauber1 = erzeugeZauber( "ZAU-test-1", KostenKlasse.A, MagieMerkmal.antimagie );

		Link zauberLink1 = new IdLink( null, null );
		zauberLink1.setZiel( zauber1 );
		zauberLink1.setZweitZiel( repraesentation );
		
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

	public void testStufeAendern() {
		
		final int ANZAHL_MOEGLICHE_ZAUBER = 17;
		
		Repraesentation repraesentation = new Repraesentation( "REP-test-1" );
		held.setRepraesentationen( new Repraesentation[]{ repraesentation } );
		
		Zauber zauber1 = erzeugeZauber( "ZAU-test-1", KostenKlasse.B, MagieMerkmal.antimagie );
		
		Link zauberLink1 = new IdLink( null, null );
		zauberLink1.setZiel( zauber1 );
		zauberLink1.setZweitZiel( repraesentation );
		
		Collection< Link > moeglicheZauber = erzeugeZauberLinks( ANZAHL_MOEGLICHE_ZAUBER );
		moeglicheZauber.add( zauberLink1 );
		extendedProzessor.setMoeglicheZauber( moeglicheZauber );
		
		prozessor.addNewElement( zauber1 );
		
		prozessor.updateWert( box.getObjectById(zauber1), 5 );
		
		assertEquals( 33, box.getEqualObjects( zauberLink1 ).get( 0 ).getKosten() );
	}

	public void testModiSetzen() {
		
		final int ANZAHL_MOEGLICHE_ZAUBER = 17;
		
		Repraesentation repraesentation = new Repraesentation( "REP-test-1" );
		held.setRepraesentationen( new Repraesentation[]{ repraesentation } );
		
		Zauber zauber1 = erzeugeZauber( "ZAU-test-1", KostenKlasse.B, MagieMerkmal.antimagie );
		
		Link zauberLink1 = new IdLink( null, null );
		zauberLink1.setZiel( zauber1 );
		zauberLink1.setZweitZiel( repraesentation );
		
		Collection< Link > moeglicheZauber = erzeugeZauberLinks( ANZAHL_MOEGLICHE_ZAUBER );
		moeglicheZauber.add( zauberLink1 );
		extendedProzessor.setMoeglicheZauber( moeglicheZauber );
		
		prozessor.addNewElement( zauber1 );
		
		IdLink modi = new IdLink( null, null );
		modi.setZiel( zauber1 );
		modi.setWert( 5 );
		prozessor.addModi( modi );
		
		assertEquals( 0, box.getEqualObjects( zauberLink1 ).get( 0 ).getKosten() );
	}
	
	/**
	 * Erzeugt eine Liste von Zaubern
	 */
	private Collection< Link > erzeugeZauberLinks( final int anzahl ) {
		Collection< Link > zauberlinks = new HashSet< Link >();

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
		
		Link link = new IdLink( null, null );
		
		link.setZiel( zauber );
		link.setZweitZiel( new Repraesentation( repraesentationId ) );
		
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

		Zauber zauber = erzeugeZauber( id );
		zauber.setKostenKlasse( kostenKlasse );
		zauber.setMerkmale( magieMerkmale );
		
		return zauber;
	}
	
	/**
	 * @param id z.B. "ZAU-test-1"
	 * @return ein Zauber mit drei zuefaellig gewaehlten Probe-Eigenschaften.
	 */
	private Zauber erzeugeZauber( final String id ) {

		final Zauber zauber = new Zauber( id );
		
		System.out.println( "Zauber: " + zauber );

		zauber.setDreiEigenschaften( new Eigenschaft[] {
				getEigenschaft(), getEigenschaft(), getEigenschaft()
		} );
		
		return zauber;
	}

	/**
	 * @return eine zufaellig gewaehlte Eigenschaft.
	 */
	private Eigenschaft getEigenschaft() {
		Random random = new Random();
		
		String eigenschaft;
		switch( random.nextInt( 8 ) ) {
		case 0:
			eigenschaft = "EIG-MU";
			break;
			
		case 1:
			eigenschaft = "EIG-KL";
			break;
			
		case 2:
			eigenschaft = "EIG-IN";
			break;
			
		case 3:
			eigenschaft = "EIG-CH";
			break;
			
		case 4:
			eigenschaft = "EIG-FF";
			break;
			
		case 5:
			eigenschaft = "EIG-GE";
			break;
			
		case 6:
			eigenschaft = "EIG-KO";
			break;
			
		default:
			eigenschaft = "EIG-KK";
			break;
			
		}
		
		return (Eigenschaft) data.getCharElement( eigenschaft, CharKomponente.eigenschaft );
	}
}
