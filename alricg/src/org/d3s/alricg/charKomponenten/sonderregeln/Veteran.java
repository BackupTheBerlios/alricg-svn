/**
 * 
 */
package org.d3s.alricg.charKomponenten.sonderregeln;

import org.d3s.alricg.charKomponenten.Sprache;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.Vorteil;
import org.d3s.alricg.charKomponenten.Talent.Art;
import org.d3s.alricg.charKomponenten.Talent.Sorte;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.LinkProzessorFront;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.elementBox.ElementBox;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorTalent;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorVorteil;
import org.d3s.alricg.prozessor.utils.FormelSammlung;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;
import org.d3s.alricg.store.DataStore;
import org.d3s.alricg.store.FactoryFinder;

/**
 * @author Vince
 */
public class Veteran extends SonderregelAdapter {
	
	private static final String BESCHREIBUNG = "Sonderregel für Veteran, siehe AH S. 107";
	
	private static final int MAX_AKTIVIERBARE_TALENTE = 8;
	
	// TODO: Max Talente: Unsauber, muss später noch geändert werden um aus XML auszulesen
	private static final int MAX_AKTIVIERBARE_TALENTE_DEFAULT = 5;
	
	private static final String SR_VETERAN = "SR-Veteran";
	
	private static final String VOR_AUSRUESTUNGSVORTEIL = "VOR-Ausruestungsvorteil";
	private static final String VOR_BESONDERER_BESITZ = "VOR-BesondererBesitz";
	private static final String VOR_AKADEMISCHE_AUSBILDUNG_GELEHRTER = "VOR-AkademischeAusbildungGelehrter";
	private static final String VOR_AKADEMISCHE_AUSBILDUNG_KRIEGER = "VOR-AkademischeAusbildungKrieger";
	
	
	
	private Held held;
	
	private ElementBox<GeneratorLink> vorteileBox;
	
	private String veteranVorteilId;
	
	@Override
	public String getId() {
		return SR_VETERAN;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter#initSonderregel(org.d3s.alricg.held.Held, org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public void initSonderregel(Held held, Link srLink) {
		// Wird aufgerufen, wenn die Sonderregel "aktiviert" wird, also wenn der User
		// "Veteran" gewählt hat
		
		this.held = held;
		this.veteranVorteilId = srLink.getZiel().getId();
		
		LinkProzessorFront<Talent, ExtendedProzessorTalent, GeneratorLink > talenteProzessor = held.getProzessor( CharKomponente.talent );
		
		// 8 aktivierbare Talente 
		talenteProzessor.getExtendedFunctions().setMaxTalentAktivierung( MAX_AKTIVIERBARE_TALENTE );
		
		LinkProzessorFront<Vorteil, ExtendedProzessorVorteil, GeneratorLink> vorteileProzessor = held.getProzessor( CharKomponente.vorteil );
		vorteileBox = vorteileProzessor.getElementBox();
		
		initFinializeSonderregel( vorteileProzessor );
	}
	
	private void initFinializeSonderregel( LinkProzessorFront<Vorteil, ExtendedProzessorVorteil, GeneratorLink> vorteileProzessor ) {
		
		GeneratorLink ausruestungsvorteil = vorteileBox.getObjectById( VOR_AUSRUESTUNGSVORTEIL );
		if ( null != ausruestungsvorteil ) {
			vorteileProzessor.updateKosten( ausruestungsvorteil );
		}
		
		GeneratorLink besondererBesitz = vorteileBox.getObjectById( VOR_BESONDERER_BESITZ );
		if ( null != besondererBesitz ) {
			vorteileProzessor.updateKosten( besondererBesitz );
		}
		
		GeneratorLink akademischeAusbildungKrieger = vorteileBox.getObjectById( VOR_AKADEMISCHE_AUSBILDUNG_GELEHRTER );
		if ( null != akademischeAusbildungKrieger ) {
			// Kampftalente sind zu halben Kosten steigerbar
			held.getProzessor( CharKomponente.talent ).updateAllKosten();
		}
		
		GeneratorLink akademischeAusbildungGelehrter = vorteileBox.getObjectById( VOR_AKADEMISCHE_AUSBILDUNG_KRIEGER );
		if ( null != akademischeAusbildungGelehrter ) {
			// Wissenstalente und Sprachen sind zu halben Kosten steigerbar
			held.getProzessor( CharKomponente.talent ).updateAllKosten();
			held.getProzessor( CharKomponente.sprache ).updateAllKosten();
		}
		
		/* Prüfen ob der Held über "Ausrüstungsvorteil", "Besonderer Besitz" oder "Akademische Ausbildung"
		 * verfügt. Ist dies der Fall --> Kosten neu berechnen (bei A.Ausbildung die Talente)
		 */
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter#changeKosten(int, org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public int changeKosten(int kosten, Link link) {
		
		if ( link.getZiel().getId().equals( VOR_BESONDERER_BESITZ ) ) {
			
			// Mit Veteran kostet Besonderer Besitz nur 5 statt 7 GP.
			kosten -= 2;
			
		} else if ( link.getZiel().getId().equals( VOR_AUSRUESTUNGSVORTEIL ) ) {
			// 15 Dukaten statt, 10 Dukaten pro 1GP
			int dukaten = link.getWert();
			kosten = ( dukaten + 7 ) / 15;
		}
		// Prüfen ob der Link einem Talent entspricht. Dann die Stufe prüfen,
		// ob das Talent unter Aka.Ausbildung fällt und evtl. die Kosten senken
		// Dabei bedenken: Bis Stufe 10 senkt die SR für Akademische Ausbildung bereits die Kosten
		else if ( ( link.getZiel() instanceof Talent ) && ( link.getWert() > 10 ) ) { 
			
			Talent talent = (Talent) link.getZiel();
			
			if ( verbilligtDurchAkademischeAusbildungKrieger( talent ) 
			  || verbilligtDurchAkademischeAusbildungGelehrter( talent ) ) {
				
				int wert = link.getWert();
				
				KostenKlasse kKlasse = talent.getKostenKlasse();
				kKlasse = held.getSonderregelAdmin().changeKostenKlasse( kKlasse, link );
				
				int kostenersparnis = 0;
				for ( int i = 10, n = Math.min( wert, 15 ) ; i < n ; ++i ) {
					kostenersparnis += FormelSammlung.berechneSktKosten( i, i+1, kKlasse ) / 2;
				}
				
				kosten -= kostenersparnis;
			}
			
		} else if ( ( link.getZiel() instanceof Sprache ) && ( link.getWert() > 10 )
				&& ( null != vorteileBox.getObjectById( VOR_AKADEMISCHE_AUSBILDUNG_GELEHRTER ) ) ) {

			Sprache sprache = (Sprache) link.getZiel();
			int wert = link.getWert();
			
			KostenKlasse kKlasse = sprache.getKostenKlasse();
			kKlasse = held.getSonderregelAdmin().changeKostenKlasse( kKlasse, link );
			
			int kostenersparnis = 0;
			for ( int i = 10, n = Math.min( wert, 15 ) ; i < n ; ++i ) {
				kostenersparnis += FormelSammlung.berechneSktKosten( i, i+1, kKlasse ) / 2;
			}
			
			kosten -= kostenersparnis;
		}
		
		return kosten;
	}

	/**
	 * Prueft ob der Held Akademische Ausbildung Gelehrter besitzt und ob das
	 * Talent aus der Talentgruppe Wissen stammt.
	 * 
	 * @param talent fuer das geprueft werden soll ob es durch AAG verbilligt 
	 * 		wird.
	 * @return {@code true} wenn das Talent durch AAG verbilligt wird, sonst
	 * 		{@code false}.
	 */
	private boolean verbilligtDurchAkademischeAusbildungGelehrter(Talent talent) {
		return ( null != vorteileBox.getObjectById( VOR_AKADEMISCHE_AUSBILDUNG_GELEHRTER ) )
				&& ( Sorte.wissen == talent.getSorte() );
	}

	/**
	 * Prueft ob der Held Akademische Ausbildung Krieger besitzt und ob das
	 * Talent aus der Talentgruppe Kampf stammt.
	 * 
	 * @param talent fuer das geprueft werden soll ob es durch AAK verbilligt 
	 * 		wird.
	 * @return {@code true} wenn das Talent durch AAK verbilligt wird, sonst
	 * 		{@code false}.
	 */
	private boolean verbilligtDurchAkademischeAusbildungKrieger(Talent talent) {
		return ( null != vorteileBox.getObjectById( VOR_AKADEMISCHE_AUSBILDUNG_KRIEGER ) )
				&& ( Sorte.kampf == talent.getSorte() );
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter#changeCanRemoveElement(boolean, org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public boolean changeCanRemoveElement(boolean canRemove, Link link) {
		
		if (link.getZiel().getId().equals(veteranVorteilId)) {
			// Die Sonderregel "Veteran" selbst soll entfernd werden --> Prüfen ob möglich
			
			if ( ((ExtendedProzessorTalent) held
					.getProzessor(CharKomponente.talent).
					getExtendedFunctions())
						.getAktivierteTalente().size() > MAX_AKTIVIERBARE_TALENTE_DEFAULT) {
				// Es sind mehr Talente aktiviert als ohne Veteran möglich --> false
				// TODO evtl. noch eine entsprechende Meldung einbauen...
				return false;
			}
		}
		
		return canRemove;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter#finalizeSonderregel(org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public void finalizeSonderregel(Link link) {
		// Aktivierbare Talente wieder auf standard setzen
		LinkProzessorFront<Talent, ExtendedProzessorTalent, GeneratorLink > talenteProzessor = held.getProzessor( CharKomponente.talent );
		talenteProzessor.getExtendedFunctions().setMaxTalentAktivierung( MAX_AKTIVIERBARE_TALENTE_DEFAULT );
		
		LinkProzessorFront<Vorteil, ExtendedProzessorVorteil, GeneratorLink> vorteileProzessor = held.getProzessor( CharKomponente.vorteil );
		initFinializeSonderregel( vorteileProzessor );
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter#getBeschreibung()
	 */
	@Override
	public String getBeschreibung() {
		return BESCHREIBUNG;
	}



	/* (non-Javadoc)
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter#isForManagement()
	 */
	@Override
	public boolean isForManagement() {
		// Diese Sonderregel spielt nur bei der Generierung eine Rolle
		return false;
	}

}
