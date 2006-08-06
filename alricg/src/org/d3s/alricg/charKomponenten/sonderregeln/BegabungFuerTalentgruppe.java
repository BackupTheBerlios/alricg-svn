/*
 * Created on 12.08.2005 / 22:54:43
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten.sonderregeln;

import java.util.List;
import java.util.logging.Logger;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.charKomponenten.sonderregeln.principle.Sonderregel;
import org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.LinkProzessor;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.common.HeldenLink;
import org.d3s.alricg.prozessor.utils.FormelSammlung;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;

/**
 * <u>Beschreibung:</u><br> 
 * Diese Sonderregel implementiert das Verhalten wie im Vorteil "Begabung f�r [Talentgruppe]"
 * beschrieben, siehe AH S. 107
 * TODO NICHT kumultativ mit "Akademische Ausbildung"! (ersetzt diesen Vorteil)
 * TODO NICHT w�hlbar mit einer entsprechenden Unf�higkeit!
 * TODO Kostenberechnung. Hier oder im Vorteil?
 * 
 * Erledigt:
 * NICHT vereinbar mit "Begabung f�r Talent" in derselben Kategorie!
 * 
 * @author V. Strelow
 */
public class BegabungFuerTalentgruppe extends SonderregelAdapter {
    
    /** <code>BegabungFuerTalentgruppe</code>'s logger */
    private static final Logger LOG = Logger.getLogger(BegabungFuerTalentgruppe.class.getName());
    
	private Talent.Sorte sorte;
	private LinkProzessor<Talent, HeldenLink> prozessor;
	
	
	public BegabungFuerTalentgruppe() {
		this.setId("SR-BegabungFuerTalentgruppe");
	}
	
	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelAdapter#getBeschreibung()
	 */
	@Override
	public String getBeschreibung() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelAdapter#canAddSelf(org.d3s.alricg.prozessor.HeldProzessor, boolean, org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public boolean canAddSelf(Held held, boolean ok, Link srLink) {
		final Sonderregel sr;
		
		// Pr�fen ob "Begabt f�r Talent" mit der selben Sorte --> Nicht erlaubt!
		sr = held.getSonderregelAdmin().getSonderregel("SR-BegabungFuerTalent", null, null);

		if (sr != null) {
			if ( ((BegabungFuerTalent) sr).getZweitZiel().getSorte()
						.getValue().equals( srLink.getText() ) )
			{
				return false;
			}
		}
		
		return ok;
	}

	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelAdapter#changeKostenKlasse(org.d3s.alricg.prozessor.FormelSammlung.KostenKlasse, org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public KostenKlasse changeKostenKlasse(KostenKlasse klasse, Link link) {
		
		// Wenn es sich um kein Talent handelt, ist es auch nicht betroffen
		if ( !link.getZiel().getCharKomponente().equals(CharKomponente.talent) ) {
			return klasse;
		}
		
		// Kostenklasse des Talents �ndern, aber nicht unter A
		if ( ((Talent) link.getZiel()).getSorte().equals(sorte)
				&& !klasse.equals(FormelSammlung.KostenKlasse.A) ) 
		{
			return klasse.minusEineKk();
		}
		
		return klasse;
	}

	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelAdapter#finalizeSonderregel(org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public void finalizeSonderregel(Link link) {
		final List<HeldenLink> list;
		
		// Alle Kosten der Talente updaten
		list = prozessor.getUnmodifiableList();
		
		for (int i = 0; i < list.size(); i++) {
			if ( list.get(i).getKosten() > 0 && 
					( ((Talent) list.get(i).getZiel()).getSorte().equals(sorte) ) )
			{
				prozessor.updateKosten(list.get(i));
			}
		}
		
		sorte = null;
	}

	/* (non-Javadoc) Methode �berschrieben
	 * Im Text des Links wird angegeben f�r welche Sorte von Talenten diese SR gilt!
	 * Es gilt der value des Enums!
	 * 
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelAdapter#initSonderregel(org.d3s.alricg.prozessor.HeldProzessor, org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public void initSonderregel(Held held, Link srLink) {
		final List<HeldenLink> list;
		
		this.prozessor = held.getProzessor(CharKomponente.talent);
		sorte = null;
		
		// Nach der richtigen Sorte suchen. Die sorte wird als Text im Link �bergeben
		// Der Text mu� mit dem "getValue" der Sorte �bereinstimmen
		for (int i = 0; i < Talent.Sorte.values().length; i++) {
			if ( srLink.getText().equals(Talent.Sorte.values()[i].getValue()) ) {
				sorte = Talent.Sorte.values()[i];
				break;
			}
		}
		
		if (sorte == null) {
			LOG.severe("Konnte in SR nicht die erwartete Talent-Sorte finden!");
		}
		
		// Alle Kosten der Talente updaten
		list = prozessor.getUnmodifiableList();
		
		for (int i = 0; i < list.size(); i++) {
			if ( list.get(i).getKosten() > 0 && 
					( ((Talent) list.get(i).getZiel()).getSorte().equals(sorte) ) )
			{
				prozessor.updateKosten(list.get(i));
			}
		}

	}

	/** Methode �berschrieben
	 * Wird ein Text angegeben, so wird der Text auch gepr�ft (als Value einer Talent.Sorte). 
	 * Wird es nicht angegeben, so wird auch nur die ID �berpr�ft!
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter#isSonderregel(java.lang.String, java.lang.String, org.d3s.alricg.charKomponenten.CharElement)
	 */
	@Override
	public boolean isSonderregel(String id, String text, CharElement zweitZiel) {
		
		// Wenn ein text angebenen ist, so mu� dieser auch stimmen
		if (text != null) {
			if ( !text.equals(sorte.getValue()) ) {
				return false;
			}
		}

		// Ansonsten wird nur die ID �berpr�ft
		return super.isSonderregel(id, text, zweitZiel);
	}
	
	// ------------------------------------------------------------------------------
	/**
	 * @return Die Talent-Sorte f�r die diese SR gilt
	 */
	public Talent.Sorte getZweitZiel() {
		return sorte;
	}

}
