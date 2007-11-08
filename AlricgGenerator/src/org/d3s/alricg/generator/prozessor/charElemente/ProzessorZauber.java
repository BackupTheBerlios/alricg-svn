/*
 * Created on 24.06.2006 / 22:37:42
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.generator.prozessor.charElemente;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.d3s.alricg.common.Notepad;
import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.charakter.ElementBox;
import org.d3s.alricg.common.logic.BaseProzessorElementBox;
import org.d3s.alricg.common.logic.FormelSammlung;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.GeneratorProzessor;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorZauber;
import org.d3s.alricg.generator.prozessor.utils.ProzessorUtilities;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Repraesentation;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.held.HeldenLink;

/**
 * <u>Beschreibung:</u><br> 
 *
 * @author V. Strelow
 */
public class ProzessorZauber extends BaseProzessorElementBox<Zauber, GeneratorLink> 
							 implements GeneratorProzessor<Zauber, GeneratorLink>, ExtendedProzessorZauber
{
	private static final String TEXT_AKTIVIEREN = "muss aktiviert werden";
	private static final String TEXT_BESITZT_MODIS = "Zauber besitzt Modifikationen";
	private static final String TEXT_FREMDE_REPRAESENTATION = "Zauber einer fremden Repr\u00E4sentation";
	private static final String TEXT_GESAMTKOSTEN = "Gesamtkosten: ";
	private static final String TEXT_HAUSZAUBER =  "Zauber ist Hauszauber";
	private static final String TEXT_KEINE_AKTIVIERUNG = "Keine Zauber-Aktivierungen mehr möglich";
	private static final String TEXT_MODIS = "Modis auf Stufe: ";
	private static final String TEXT_REPRAESENTATION_GEAENDERT = "Rep\u00E4sentation von Zauber %1$s zu %2$s ge\u00E4ndert.";
	private static final String TEXT_REPRAESENTATION_UNBESTIMMT = "Die Repraesentation fuer %1$s konnte nicht ermittelt werden.";
	private static final String TEXT_SKT_SPALTE_ORIGINAL = "Original SKT-Spalte: ";
	private static final String TEXT_ZAUBER_ENTFERNT = "Zauber entfernt: ";
	
	protected static final boolean STUFE_ERHALTEN = true;
	
	private static final int MAX_ZAUBER_AKTIVIERUNG_VOLLZAUBERER = 10;
	private static final int MAX_ZAUBER_AKTIVIERUNG_HALBZAUBERER =  5;

	/** 
	 * Haelt alle Zauber nach den Probeneigenschaften sortiert.
	 * Wichtig fuer schnellen Zugriff bei berechnung der Min-Werte bei Eigenschaften.
	 */ 
	private Map< EigenschaftEnum, Set<GeneratorLink> > hashMapNachEigensch;
	private final Notepad notepad;
	private final Charakter held;
	
	private int talentGpKosten;
	
	private final Set< Zauber > aktivierteZauber;

	private final Collection< Link > hauszauber;

	private final HashMap< Zauber, Repraesentation > hauszauberMap;

	private final Collection< Link > moeglicheZauber;

	private final HashMap< Zauber, Repraesentation > moeglicheZauberMap;
	
	public ProzessorZauber(	Charakter held, Notepad notepade ) {
		this.held = held;
		this.notepad = notepade;
		this.elementBox = new ElementBox<GeneratorLink>();
		
		this.talentGpKosten = 0;
		
		this.aktivierteZauber = new HashSet< Zauber >();

		this.hauszauber = new HashSet< Link >();
		this.hauszauberMap = new HashMap< Zauber, Repraesentation >();

		this.moeglicheZauber = new HashSet< Link >();
		this.moeglicheZauberMap = new HashMap< Zauber, Repraesentation >();
		
		this.hashMapNachEigensch = new HashMap< EigenschaftEnum, Set<GeneratorLink> >();
		this.hashMapNachEigensch.put( EigenschaftEnum.MU, new HashSet<GeneratorLink>() );
		this.hashMapNachEigensch.put( EigenschaftEnum.KL, new HashSet<GeneratorLink>() );
		this.hashMapNachEigensch.put( EigenschaftEnum.IN, new HashSet<GeneratorLink>() );
		this.hashMapNachEigensch.put( EigenschaftEnum.CH, new HashSet<GeneratorLink>() );
		this.hashMapNachEigensch.put( EigenschaftEnum.FF, new HashSet<GeneratorLink>() );
		this.hashMapNachEigensch.put( EigenschaftEnum.GE, new HashSet<GeneratorLink>() );
		this.hashMapNachEigensch.put( EigenschaftEnum.KO, new HashSet<GeneratorLink>() );
		this.hashMapNachEigensch.put( EigenschaftEnum.KK, new HashSet<GeneratorLink>() );
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#addModi(org.d3s.alricg.charKomponenten.links.IdLink)
	 */
	public HeldenLink addModi(IdLink element) {
		
		GeneratorLink link = elementBox.getObjectById( element.getZiel().getId() );
		
		if ( STUFE_ERHALTEN && ( 0 != link.getWert() ) ) {
			int wert = link.getWert();
			link.addLink( element );
			link.setUserGesamtWert( wert );
		} else {
			link.addLink( element );
		}
		
		ProzessorUtilities.inspectWert( link, this );
		
		// evtl. Status als aktivierter Zauber entziehen. 
		pruefeZauberAktivierung( link );
		
		updateKosten( link );
		
		return link;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canAddElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canAddElement(Link link) {
		return canAddElement( (Zauber)link.getZiel() );
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateText(E)
	 */
	public boolean canUpdateText(GeneratorLink link) {
		return link.hasText();
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateWert(E)
	 */
	public boolean canUpdateWert(GeneratorLink link) {
		// Der Wert eines Zaubers kann grundsaetzlich geaendert werden.
		// Minimal- und Maximalwerte werden an anderer Stelle ueberprueft.
		return true;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateZweitZiel(E, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public boolean canUpdateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// Zweitziel ist bei Zaubern die Repraesentation.
		return ( 0 != link.getWertModis() );
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#containsLink(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean containsLink(Link link) {
		
		String id = link.getZiel().getId();
		
		return ( null != elementBox.getObjectById( id ) );
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getGesamtKosten()
	 */
	public double getGesamtKosten() {
		return talentGpKosten;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMaxWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMaxWert(Link link) {
		final Eigenschaft[] eigenschaften;
		int maxEigenschaft = 0;
		
		Zauber zauber = (Zauber) link.getZiel();
		
		// Die 3 Eigeschaften holen
		eigenschaften = zauber.getDreiEigenschaften();
		
		// Hoechste Eigenschaft Bestimmen
		for ( Eigenschaft eigenschaft : eigenschaften ) {
			
			int eigenschaftWert = held.getEigenschaftsWert( eigenschaft.getEigenschaftEnum() ); 
			
			maxEigenschaft = Math.max( maxEigenschaft, eigenschaftWert );
		}
		
		// Hoechste Eigenschaft +3 fuer Zauber der eigenen Repraesentation
		// oder hoechste Eigenschaft / Siehe MWW S. 12
		if ( istEigeneRepraesentation( (Repraesentation) link.getZweitZiel() ) ) {
			return ( maxEigenschaft + 3 );
		} else {
			return maxEigenschaft;
		}
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMinWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMinWert(Link link) {
		return ((GeneratorLink) link).getWertModis();
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#removeModi(E, org.d3s.alricg.charKomponenten.links.IdLink)
	 */
	public void removeModi(GeneratorLink link, IdLink element) {

		link.removeLink( element );
		
		// Stufe ggf. neu setzen
		ProzessorUtilities.inspectWert( link, this );

		pruefeZauberAktivierung( link );
		
		updateKosten( link );
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateKosten(E)
	 */
	public void updateKosten(GeneratorLink link) {

		talentGpKosten -= link.getKosten();
		
		Zauber zauber = (Zauber) link.getZiel();
		
		int startStufe;
		if ( aktivierteZauber.contains( zauber ) ) {
			startStufe = CharElement.KEIN_WERT;
			notepad.writeMessage( TEXT_MODIS + startStufe );
		} else {
			startStufe = link.getWertModis();
			notepad.writeMessage( TEXT_AKTIVIEREN );
		}
		
		int zielStufe = link.getWert();
		
		KostenKlasse kKlasse = zauber.getKostenKlasse();
		notepad.writeMessage( TEXT_SKT_SPALTE_ORIGINAL + kKlasse.getValue() );
		
		// Zauber einer fremden Repraesentation sind 2 Spalten teurer zu steigern.
		if ( ! istEigeneRepraesentation( (Repraesentation) link.getZweitZiel() ) ) {
			kKlasse = kKlasse.plusEineKk().plusEineKk();
			notepad.writeMessage( TEXT_FREMDE_REPRAESENTATION );
		}
		
		// Hauszauber sind eine Kostenklasse guenstiger zu steigern.
		if ( istHauszauber( zauber ) ) {
			kKlasse = kKlasse.minusEineKk();
			notepad.writeMessage( TEXT_HAUSZAUBER );
		}
		
		// Geanderte Kostenklasse wird in changeKostenKlasse ins Notepad eingetragen.
		kKlasse = held.getSonderregelAdmin().changeKostenKlasse( kKlasse, link );
		
		int kosten = FormelSammlung.berechneSktKosten( startStufe, zielStufe, kKlasse );
		notepad.writeMessage( TEXT_GESAMTKOSTEN + kosten );
		
		// Ändere die Kosten durch Sonderregeln oder Verbilligungen durch Herkunft
		kosten = (int) held.getSonderregelAdmin().changeKosten( new Double(kosten), link );
		kosten = held.getVerbFertigkeitenAdmin().changeKostenAP( kosten, link );
		
		link.setKosten( kosten );
		talentGpKosten += link.getKosten();
		
	}

	public void updateAllKosten() {
		for ( GeneratorLink zauber : elementBox ) {
			updateKosten( zauber );
		}
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateText(E, java.lang.String)
	 */
	public void updateText(GeneratorLink link, String text) {
		if ( text != null ) {
			link.setText( text );
		}
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateWert(E, int)
	 */
	public void updateWert(GeneratorLink link, int wert) {
		
		if ( Link.KEIN_WERT != wert ) {
			link.setUserGesamtWert( wert );
		}
		
		pruefeZauberAktivierung( link );
		
		updateKosten( link );
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateZweitZiel(E, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public void updateZweitZiel( GeneratorLink link, CharElement zweitZiel ) {

		// Das Zweitziel ist die Repraesentation des Zaubers.
		link.setZweitZiel( zweitZiel );
		
		// Der Maximalwert fuer Zauber eigener Repraesentation ist anders
		// als fuer Zauber einer fremden Repraesentation.
		ProzessorUtilities.inspectWert( link, this );
		
		// Die Kosten koennen sich geaendert haben.
		// Zauber fremder Repraesentation sind um 2 Spalten schwerer zu 
		// steigern.
		updateKosten( link );
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#addNewElement(ZIEL)
	 */
	public GeneratorLink addNewElement(Zauber ziel) {
		
		Repraesentation repraesentation;
		if ( istMoeglicherZauber( ziel ) ) {
			repraesentation = moeglicheZauberMap.get( ziel );
		} else if ( istHauszauber( ziel ) ) {
			repraesentation = hauszauberMap.get( ziel );
		} else {
			repraesentation = null;
			notepad.writeMessage( String.format( TEXT_REPRAESENTATION_UNBESTIMMT, ziel ) );
		}
		
		GeneratorLink link = new GeneratorLink( ziel, null, repraesentation, 0 );
		
		elementBox.add( link );
		
		for( Eigenschaft eigenschaft : ziel.getDreiEigenschaften() ) {
			hashMapNachEigensch.get( eigenschaft.getEigenschaftEnum() ).add( link );
		}
		
		pruefeZauberAktivierung( link );
		
		updateKosten( link );
		
		return link;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#canAddElement(ZIEL)
	 */
	public boolean canAddElement(Zauber ziel) {
		return false;
		/*
		 TODO Auskommentiert bis "isVollzauberer" usw. wieder implementiert sind
		if ( ( ! held.isVollzauberer() ) && ( ! held.isHalbzauberer() ) ) {
			// Nur Voll- und Halbzauberer koennen Zauber aktivieren.
			return false;
		}
		
		if ( held.isVollzauberer() && ( aktivierteZauber.size() >= MAX_ZAUBER_AKTIVIERUNG_VOLLZAUBERER )
	      ||(held.isHalbzauberer() && ( aktivierteZauber.size() >= MAX_ZAUBER_AKTIVIERUNG_HALBZAUBERER ) ) ) {
			
			//Es kann kein weiterer Zauber mehr aktiviert werden.
			notepad.writeMessage( TEXT_KEINE_AKTIVIERUNG );
			return false;
		}
		
		if ( elementBox.getObjectById( ziel.getId() ) != null ) {
			// Der Held besitzt den Zauber schon
			return false;
		}
		
		return istMoeglicherZauber( ziel );
		*/
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#canRemoveElement(ELEM)
	 */
	public boolean canRemoveElement(GeneratorLink element) {
		
		if ( 0 != element.getLinkModiList().size() ) {
			
			notepad.writeMessage(TEXT_BESITZT_MODIS);
			
			return false;
		}
		
		return true;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#removeElement(ELEM)
	 */
	public void removeElement(GeneratorLink element) {
		
		Zauber zauber = (Zauber) element.getZiel();
		
		// Zauber aus der Liste der aktivierten Zauber entfernen.
		// Wenn der Zauber nicht in der Liste ist, macht der Aufruf von remove nichts. 
		aktivierteZauber.remove( zauber );
		
		elementBox.remove( element );
		
		for ( Eigenschaft eigenschaft : zauber.getDreiEigenschaften() ) {
			hashMapNachEigensch
			.get( eigenschaft.getEigenschaftEnum() )
			.remove( zauber );
		}
		
		talentGpKosten -= element.getKosten();
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.generierung.ExtendedProzessorZauber#getZauberList(org.d3s.alricg.charKomponenten.EigenschaftEnum)
	 */
	public List< ? extends HeldenLink> getZauberList( EigenschaftEnum eigenschaft ) {
		
		Set< GeneratorLink > zauber = hashMapNachEigensch.get( eigenschaft );
		
		if ( zauber == null ) {
			zauber = new HashSet< GeneratorLink >();
		}
		
		return Collections.unmodifiableList( new ArrayList<GeneratorLink>( zauber ) );
	}
	
	private void pruefeZauberAktivierung( GeneratorLink link ) {
		
		Zauber zauber = (Zauber) link.getZiel();
		
		if( besitztModifikator( link ) ) {

			// Der Zauber besitzt einen Modifikator
			aktivierteZauber.remove( zauber );
			
		} else { // Zauber wurde aktiviert

			aktivierteZauber.add( zauber );
			
		}
	}

	public void addHauszauber( Link neuerHauszauber ) {

		hauszauber.add( neuerHauszauber );
		
		Zauber zauber = (Zauber) neuerHauszauber.getZiel();
		
		hauszauberMap.put( zauber, (Repraesentation) neuerHauszauber.getZweitZiel() );

		GeneratorLink zauberLink = elementBox.getObjectById( zauber );
		
		if ( null != zauberLink ) {

			ueberpruefeRepraesentation( zauberLink, hauszauberMap.get( zauber ) );
		
		}
	}
	
	public void removeHauszauber( Link alterHauszauber ) {
		
		hauszauber.remove( alterHauszauber );
		
		Zauber hauszauberZiel = (Zauber) alterHauszauber.getZiel();
		
		for ( GeneratorLink zauberLink : elementBox ) {
			if ( zauberLink.getZiel().equals( hauszauberZiel ) ) {
				updateKosten( zauberLink );
				break;
			}
		}
	}
	
	public Collection<Link> getHauszauber() {
		return new HashSet< Link >( hauszauber );
	}
	
	public void setHauszauber(Collection<Link> neueHauszauber) {
		
		hauszauber.clear();
		hauszauber.addAll( neueHauszauber );
		
		hauszauberMap.clear();
		for ( Link zauber : neueHauszauber ) {
			hauszauberMap.put( (Zauber) zauber.getZiel() , (Repraesentation) zauber.getZweitZiel() );
		}
		
		// Die Repraesentationen der Zauber die der Benutzer schon gewaehlt hat
		// auf die Repraesentationen umstellen, die bei den Hauszaubern genannt
		// sind.
		for ( GeneratorLink zauberLink : elementBox ) {
			
			Zauber zauber = (Zauber) zauberLink.getZiel();
			
			if ( istHauszauber( zauber ) ) {
			
				ueberpruefeRepraesentation( zauberLink, hauszauberMap.get( zauber ) );
					
			}
		}
		
	}

	public Collection<Link> getMoeglicheZauber() {
		return new HashSet< Link >( moeglicheZauber );
	}

	public void setMoeglicheZauber( Collection<Link> neueMoeglicheZauber ) {
		 
		moeglicheZauber.clear();
		moeglicheZauber.addAll( neueMoeglicheZauber );
		
		moeglicheZauberMap.clear();
		for ( Link zauber : neueMoeglicheZauber ) {
			moeglicheZauberMap.put( (Zauber) zauber.getZiel() , (Repraesentation) zauber.getZweitZiel() );
		}
		
		// Alle Zauber aussortieren die nicht moeglich sind. 
		for ( GeneratorLink zauberLink : elementBox  ) {
			
			Zauber zauber = (Zauber) zauberLink.getZiel();
			
			if ( istKeinMoeglicherZauber( zauber )
			  && besitztKeinenModifikator( zauberLink ) ) {
				
				removeElement( zauberLink );
				notepad.writeMessage( TEXT_ZAUBER_ENTFERNT + zauber );
				
			} else {
				// Es handelt sich (wahrscheinlich) um einen moeglichen Zauber. 
				// Der Zauber kann also in der ElementBox bleiben.
				// Jetzt muss evtl. noch die Repraesentation angepasst werden.
				if ( moeglicheZauberMap.containsKey( zauber ) ) {
					ueberpruefeRepraesentation( zauberLink, moeglicheZauberMap.get( zauber ) );
				}
				
			}
		}
		
	}

	/**
	 * Prueft ob ein Zauber fuer einen Helden zur Menge der moeglichen Zauber gehoert.
	 * Siehe "Aventurische Zauberer" S21.
	 *  
	 * @param zauber der zu ueberpruefen ist.
	 * @return {@code true}, wenn der Zauber nicht zu der Menge der moeglichen Zauber gehoert,
	 * 			sonst {@code false}. Siehe AZ S21.
	 */
	private boolean istMoeglicherZauber( Zauber zauber ) {
		
		return moeglicheZauberMap.containsKey( zauber );
	}

	/**
	 * Prueft ob ein Zauber von einem Helden NICHT gewaehlt werden kann.
	 *  
	 * @param zauber der zu ueberpruefen ist.
	 * @return {@code true}, wenn der Zauber nicht zu der Menge der moeglichen Zauber gehoert,
	 * 			sonst {@code false}. Siehe AZ S21.
	 */
	private boolean istKeinMoeglicherZauber( Zauber zauber ) {
		
		return ! istMoeglicherZauber( zauber );
	}

	/**
	 * Prueft ob ein Zauber ein Hauszauber ist.
	 *  
	 * @param zauber der zu ueberpruefen ist.
	 * @return {@code true} wenn der Zauber ein Hauszauber ist, sonst {@code false}.
	 */
	private boolean istHauszauber( Zauber zauber ) {
		
		return hauszauberMap.containsKey( zauber );
	}
	
	/**
	 * Prueft ob der Held die uebergebene Repraesentationen beherrscht.
	 * 
	 * @param zauberRepraesentation
	 * @return {@code true} wenn der Held die uebergebene Repraesentation besitzt,
	 * 	 		sonst {@code false}.
	 */
	private boolean istEigeneRepraesentation( Repraesentation zauberRepraesentation ) {
		return false;
		/*
		 TODO Auskommentiert bis "getRepraesentationen" wieder implementiert sind
		for ( Repraesentation r : held.getRepraesentationen() ) {
			if ( r.equals( zauberRepraesentation ) ) {
				return true;
			}
		}
		
		return false;
		*/
	}

	private boolean besitztModifikator( GeneratorLink link ) {
		return 0 != link.getLinkModiList().size();
	}

	private boolean besitztKeinenModifikator( GeneratorLink link ) {
		return ! besitztModifikator( link );
	}
	
	/**
	 * Prueft ob ein Zauber eine bestimmte Repraesentation besitzt und aendert 
	 * die Repraesentation ggf.
	 * Bei einer Aenderung der Repraesentation wird eine Nachricht ins notepad 
	 * eingetragen. Weiter werden die Kosten neu berechnet.
	 * 
	 * @param zauberLink Der zu ueberpruefende Zauber.
	 * @param zuPruefendeRepraesentation Die Repraesentation die der Zauber 
	 * 			besitzen soll.
	 */
	private void ueberpruefeRepraesentation( GeneratorLink zauberLink, Repraesentation zuPruefendeRepraesentation ) {

		Repraesentation repraesentation = (Repraesentation) zauberLink.getZweitZiel();
			
		if ( ! zuPruefendeRepraesentation.equals( repraesentation ) ) {
		
			updateZweitZiel( zauberLink, zuPruefendeRepraesentation );
			notepad.writeMessage( String.format( TEXT_REPRAESENTATION_GEAENDERT, zauberLink.getZiel(), zuPruefendeRepraesentation ) );
			updateKosten( zauberLink );

		}	
	
	}

}