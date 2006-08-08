/*
 * Created on 24.06.2006 / 22:37:42
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.generierung;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Eigenschaft;
import org.d3s.alricg.charKomponenten.EigenschaftEnum;
import org.d3s.alricg.charKomponenten.Repraesentation;
import org.d3s.alricg.charKomponenten.Zauber;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.controller.Notepad;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.BaseProzessorElementBox;
import org.d3s.alricg.prozessor.LinkProzessor;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.common.HeldenLink;
import org.d3s.alricg.prozessor.common.SonderregelAdmin;
import org.d3s.alricg.prozessor.common.VerbilligteFertigkeitAdmin;
import org.d3s.alricg.prozessor.elementBox.ElementBoxLink;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorZauber;
import org.d3s.alricg.prozessor.utils.FormelSammlung;
import org.d3s.alricg.prozessor.utils.ProzessorUtilities;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;

/**
 * <u>Beschreibung:</u><br> 
 *
 * @author V. Strelow
 */
public class ProzessorZauber extends BaseProzessorElementBox<Zauber, GeneratorLink> 
							 implements LinkProzessor<Zauber, GeneratorLink>, ExtendedProzessorZauber
{
	private static final String TEXT_AKTIVIEREN = "muss aktiviert werden";
	private static final String TEXT_FREMDE_REPRAESENTATION = "Der Zauber geh\u00F6rt einer fremden Repr\u00E4sentation an.";
	private static final String TEXT_GESAMTKOSTEN = "Gesamtkosten: ";
	private static final String TEXT_KEINE_AKTIVIERUNG = "Keine Zauber-Aktivierungen mehr möglich";
	private static final String TEXT_MODIS = "Modis auf Stufe: ";
	private static final String TEXT_SKT_SPALTE_ORIGINAL = "Original SKT-Spalte: ";
	private static final String TEXT_BESITZT_MODIS = "Zauber besitzt Modifikationen";
	
	protected static final boolean STUFE_ERHALTEN = true;
	
	private static final int MAX_ZAUBER_AKTIVIERUNG_VOLLZAUBERER = 10;
	private static final int MAX_ZAUBER_AKTIVIERUNG_HALBZAUBERER =  5;

	/** 
	 * Haelt alle Zauber nach den Probeneigenschaften sortiert.
	 * Wichtig fuer schnellen Zugriff bei berechnung der Min-Werte bei Eigenschaften.
	 */ 
	private Map< EigenschaftEnum, Set<GeneratorLink> > hashMapNachEigensch;
	
	private final SonderregelAdmin sonderregelAdmin;
	private final VerbilligteFertigkeitAdmin verbFertigkeitenAdmin;
	private final Notepad notepad;
	private final Held held;
	
	private int talentGpKosten;
	
	private final Set< Zauber > aktivierteZauber;
	
	public ProzessorZauber(	Held held, Notepad notepade ) {
		
		this.sonderregelAdmin = held.getSonderregelAdmin();
		this.held = held;
		this.verbFertigkeitenAdmin = held.getVerbFertigkeitenAdmin();
		this.notepad = notepade;
		this.elementBox = new ElementBoxLink<GeneratorLink>();
		
		this.talentGpKosten = 0;
		
		this.aktivierteZauber = new HashSet< Zauber >();
		
		hashMapNachEigensch = new HashMap< EigenschaftEnum, Set<GeneratorLink> >();

		hashMapNachEigensch.put( EigenschaftEnum.MU, new HashSet<GeneratorLink>() );
		hashMapNachEigensch.put( EigenschaftEnum.KL, new HashSet<GeneratorLink>() );
		hashMapNachEigensch.put( EigenschaftEnum.IN, new HashSet<GeneratorLink>() );
		hashMapNachEigensch.put( EigenschaftEnum.CH, new HashSet<GeneratorLink>() );
		hashMapNachEigensch.put( EigenschaftEnum.FF, new HashSet<GeneratorLink>() );
		hashMapNachEigensch.put( EigenschaftEnum.GE, new HashSet<GeneratorLink>() );
		hashMapNachEigensch.put( EigenschaftEnum.KO, new HashSet<GeneratorLink>() );
		hashMapNachEigensch.put( EigenschaftEnum.KK, new HashSet<GeneratorLink>() );
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
	public int getGesamtKosten() {
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
		eigenschaften = zauber.get3Eigenschaften();
		
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
			startStufe = link.getWertModis();
			notepad.writeMessage( TEXT_AKTIVIEREN );
		} else {
			startStufe = CharElement.KEIN_WERT;
			notepad.writeMessage( TEXT_MODIS + startStufe );
		}
		
		int zielStufe = link.getWert();
		
		KostenKlasse kKlasse = zauber.getKostenKlasse();
		notepad.writeMessage( TEXT_SKT_SPALTE_ORIGINAL + kKlasse.getValue() );
		
		// Zauber einer fremden Repraesentation sind 2 Spalten teurer zu steigern.
		if ( ! istEigeneRepraesentation( (Repraesentation) link.getZweitZiel() ) ) {
			kKlasse = kKlasse.plusEineKk().plusEineKk();
			notepad.writeMessage( TEXT_FREMDE_REPRAESENTATION );
		}
		
		// Geanderte Kostenklasse wird in changeKostenKlasse ins Notepad eingetragen.
		kKlasse = sonderregelAdmin.changeKostenKlasse( kKlasse, link );
		
		int kosten = FormelSammlung.berechneSktKosten( startStufe, zielStufe, kKlasse );
		notepad.writeMessage( TEXT_GESAMTKOSTEN + kosten );
		
		// Ändere die Kosten durch Sonderregeln oder Verbilligungen durch Herkunft
		kosten = sonderregelAdmin.changeKosten( kosten, link );
		kosten = verbFertigkeitenAdmin.changeKostenAP( kosten, link );
		
		link.setKosten( kosten );
		talentGpKosten += link.getKosten();
		
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
		
		GeneratorLink link = new GeneratorLink( ziel, null, null, 0 );
		
		elementBox.add( link );
		
		for( Eigenschaft eigenschaft : ziel.get3Eigenschaften() ) {
			hashMapNachEigensch.get( eigenschaft ).add( link );
		}
		
		pruefeZauberAktivierung( link );
		
		updateKosten( link );
		
		return link;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#canAddElement(ZIEL)
	 */
	public boolean canAddElement(Zauber ziel) {
		
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
		
		return true;
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
		
		for ( Eigenschaft eigenschaft : zauber.get3Eigenschaften() ) {
			hashMapNachEigensch.get( eigenschaft ).remove( zauber );
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
	
	public void pruefeZauberAktivierung( GeneratorLink link ) {
		
		Zauber zauber = (Zauber) link.getZiel();
		
		if( 0 == link.getLinkModiList().size() ) {
			
			// Der Zauber besitzt einen Modifikator durch
			aktivierteZauber.remove( zauber );
			
		} else { // Zauber wurde aktiviert
			
			aktivierteZauber.add( zauber );
			
		}
	}
	
	/**
	 * Prueft ob der Held die uebergebene Repraesentationen beherrscht.
	 * 
	 * @param zauberRepraesentation
	 * @return {@code true} wenn der Held die uebergebene Repraesentation besitzt,
	 * 	 		sonst {@code false}.
	 */
	private boolean istEigeneRepraesentation( Repraesentation zauberRepraesentation ) {
		for ( Repraesentation r : held.getRepraesentationen() ) {
			if ( r.equals( zauberRepraesentation ) ) {
				return true;
			}
		}
		
		return false;
	}
}