/**
 * 
 */
package org.d3s.alricg.charKomponenten.sonderregeln;

import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorTalent;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;

/**
 * @author Vince
 */
public class Veteran extends SonderregelAdapter {
	private static final int MAX_AKTIVIERBARE_TALENTE = 8;
	
	// TODO: Max Talente: Unsauber, muss später noch geändert werden um aus XML auszulesen
	private static final int MAX_AKTIVIERBARE_TALENTE_DEFAULT = 5;
	
	private static final String BESCHREIBUNG = "Sonderregel für Veteran, siehe AH S. 107";
	
	private Held held;
	private String veteranVorteilId;
	
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter#initSonderregel(org.d3s.alricg.held.Held, org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public void initSonderregel(Held held, Link srLink) {
		// Wird aufgerufen, wenn die Sonderregel "aktiviert" wird, also wenn der User
		// "Veteran" gewählt hat
		
		this.held = held;
		this.veteranVorteilId = srLink.getZiel().getId();
			
		// 8 aktivierbare Talente 
		((ExtendedProzessorTalent) held
				.getProzessor(CharKomponente.talent).
				getExtendedFunctions()).setMaxTalentAktivierung(MAX_AKTIVIERBARE_TALENTE);
		
		
		/* Prüfen ob der Held über "Ausrüstungsvorteil", "Besonderer Besitz" oder "Akademische Ausbildung"
		 * verfügt. Ist dies der Fall --> Kosten neu berechnen (bei A.Ausbildung die Talente)
		 */
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter#changeKosten(int, org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public int changeKosten(int kosten, Link link) {
		// Prüfen ob der Link "Ausrüstungsvorteil" oder "Besonderer Besitz" entspricht. In dem Fall
		// Kosten senken.
		
		// Prüfen ob der Link einem Talent entspricht. Dann die Stufe prüfen,
		// ob das Talent unter Aka.Ausbildung fällt und evtl. die Kosten senken
		// Dabei bedenken: Bis Stufe 10 senkt die SR für Akademische Ausbildung bereits die Kosten
		
		return super.changeKosten(kosten, link);
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
		((ExtendedProzessorTalent) held
				.getProzessor(CharKomponente.talent).
				getExtendedFunctions()).setMaxTalentAktivierung(MAX_AKTIVIERBARE_TALENTE_DEFAULT);
		
		/* Prüfen ob der Held über "Ausrüstungsvorteil", "Besonderer Besitz" oder "Akademische Ausbildung"
		 * verfügt. Ist dies der Fall --> Kosten neu berechnen (bei A.Ausbildung die Talente)
		 */
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
