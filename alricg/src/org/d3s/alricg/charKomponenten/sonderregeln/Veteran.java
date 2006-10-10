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
	
	// TODO: Max Talente: Unsauber, muss sp�ter noch ge�ndert werden um aus XML auszulesen
	private static final int MAX_AKTIVIERBARE_TALENTE_DEFAULT = 5;
	
	private static final String BESCHREIBUNG = "Sonderregel f�r Veteran, siehe AH S. 107";
	
	private Held held;
	private String veteranVorteilId;
	
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter#initSonderregel(org.d3s.alricg.held.Held, org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public void initSonderregel(Held held, Link srLink) {
		// Wird aufgerufen, wenn die Sonderregel "aktiviert" wird, also wenn der User
		// "Veteran" gew�hlt hat
		
		this.held = held;
		this.veteranVorteilId = srLink.getZiel().getId();
			
		// 8 aktivierbare Talente 
		((ExtendedProzessorTalent) held
				.getProzessor(CharKomponente.talent).
				getExtendedFunctions()).setMaxTalentAktivierung(MAX_AKTIVIERBARE_TALENTE);
		
		
		/* Pr�fen ob der Held �ber "Ausr�stungsvorteil", "Besonderer Besitz" oder "Akademische Ausbildung"
		 * verf�gt. Ist dies der Fall --> Kosten neu berechnen (bei A.Ausbildung die Talente)
		 */
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter#changeKosten(int, org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public int changeKosten(int kosten, Link link) {
		// Pr�fen ob der Link "Ausr�stungsvorteil" oder "Besonderer Besitz" entspricht. In dem Fall
		// Kosten senken.
		
		// Pr�fen ob der Link einem Talent entspricht. Dann die Stufe pr�fen,
		// ob das Talent unter Aka.Ausbildung f�llt und evtl. die Kosten senken
		// Dabei bedenken: Bis Stufe 10 senkt die SR f�r Akademische Ausbildung bereits die Kosten
		
		return super.changeKosten(kosten, link);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter#changeCanRemoveElement(boolean, org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public boolean changeCanRemoveElement(boolean canRemove, Link link) {
		
		if (link.getZiel().getId().equals(veteranVorteilId)) {
			// Die Sonderregel "Veteran" selbst soll entfernd werden --> Pr�fen ob m�glich
			
			if ( ((ExtendedProzessorTalent) held
					.getProzessor(CharKomponente.talent).
					getExtendedFunctions())
						.getAktivierteTalente().size() > MAX_AKTIVIERBARE_TALENTE_DEFAULT) {
				// Es sind mehr Talente aktiviert als ohne Veteran m�glich --> false
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
		
		/* Pr�fen ob der Held �ber "Ausr�stungsvorteil", "Besonderer Besitz" oder "Akademische Ausbildung"
		 * verf�gt. Ist dies der Fall --> Kosten neu berechnen (bei A.Ausbildung die Talente)
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
