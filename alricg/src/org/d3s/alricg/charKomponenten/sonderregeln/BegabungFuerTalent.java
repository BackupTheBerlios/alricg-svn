/*
 * Created on 31.07.2005 / 20:52:32
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten.sonderregeln;

import java.util.logging.Logger;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.charKomponenten.sonderregeln.principle.Sonderregel;
import org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.LinkProzessorFront;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.common.HeldenLink;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorTalent;
import org.d3s.alricg.prozessor.utils.FormelSammlung;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;

/**
 * <u>Beschreibung:</u><br> 
 * Beschreibt die Sonderregel "Begabung für [Talent]" Siehe AH, Seite 107.
 * TODO NICHT kumultativ mit "Akademische Ausbildung"! (ersetzt diesen Vorteil)
 * TODO NICHT wählbar mit einer entsprechenden Unfähigkeit!
 * TODO Kostenberechnung. Hier oder im Vorteil?
 * 
 * Erledigt:
 * NICHT vereinbar mit "Begabung für TalentGruppe" in derselben Kategorie!
 * 
 * @author V. Strelow
 */
public class BegabungFuerTalent extends SonderregelAdapter {
    
    /** <code>BegabungFuerTalent</code>'s logger */
    private static final Logger LOG = Logger.getLogger(BegabungFuerTalent.class.getName());
    
	private Talent begabtFuer;
	private Held held;
	private LinkProzessorFront<Talent, ExtendedProzessorTalent, HeldenLink> prozessor;
	
	/**
	 * Konstruktor
	 */
	public BegabungFuerTalent() {
		this.setId("SR-BegabungFuerTalent");
	}
	
	@Override
	public String getBeschreibung() {
		// TODO implement
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelAdapter#canAddSelf(org.d3s.alricg.prozessor.HeldProzessor, boolean, org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public boolean canAddSelf(Held held, boolean ok, Link srLink) {
		final Sonderregel sr;
		
		if ( srLink.getZweitZiel() == null)  {
			// Es wird ein ZweitZiel benötigt!
			LOG.warning("Es wird ein ZweitZiel benötigt, es ist jedoch keins angegeben!");
			return false;
		} else if ( !srLink.getZweitZiel().getCharKomponente().equals(CharKomponente.talent) ) {
			//	Nur Talente
			return false; 
		} else if (held.getSonderregelAdmin().hasSonderregel(this.getId(), null, null)) {
			// Darf nur einmal gewählt werden!
			return false;
		} else if (held.getSonderregelAdmin().hasSonderregel(this.getId(), null, null)) {
			// Darf nur einmal gewählt werden!
			return false;
		} else if ( ((Talent) srLink.getZweitZiel()).getKostenKlasse()
						.equals(FormelSammlung.KostenKlasse.A) ) 
		{
			// Talente mit "A" können nicht gewählt werden
			return false; 
		}
		
		// Prüfen ob "Begabt für Talentgruppe" mit der selben Sorte --> Nicht erlaubt!
		sr = held.getSonderregelAdmin().getSonderregel("SR-BegabungFuerTalentgruppe", null, null);

		if (sr != null) {
			if ( ((BegabungFuerTalentgruppe) sr).getZweitZiel().equals(
					((Talent) srLink.getZweitZiel()).getSorte()) )
			{
				return false;
			}
		}
		
		return ok;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelAdapter#changeKostenKlasse(org.d3s.alricg.prozessor.FormelSammlung.KostenKlasse, org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public KostenKlasse changeKostenKlasse(KostenKlasse klasse, Link link) {
		
		if ( link.getZiel().getId().equals(begabtFuer.getId()) ) {
			return klasse.minusEineKk();
		}
		
		return klasse;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter#changeKosten(int, org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public int changeKosten(int kosten, Link link) {
		final Talent tmpTalent;
		KostenKlasse kKlasse;
		int maxStufeKosten, modiStufeKosten;
		
		if ( !link.getZiel().getId().equals(begabtFuer.getId()) || held.isManagement() ) {
			// Wenn es sich nicht um das verbilligte Talent handelt, oder 
			// der Held nicht generiert wird betrifft es den Char nicht
			return kosten;
		}
		
		// Sorgt dafür, dass der Held den "letzen" Stufenpunkt umsonst bekommt,
		// dafür muss die Berechung nachgebessert werden!
		// TODO notepad einbauen!
		
		// Bestimme Talent
		tmpTalent = (Talent) link.getZiel();
		
		// Bestimme die Kostenklasse
		kKlasse = tmpTalent.getKostenKlasse();
		
		// Evtl. Änderungen der Kostenklasse (notepade wird innerhalb gesetzt)
		kKlasse = held.getSonderregelAdmin().changeKostenKlasse(kKlasse, link);
		
		maxStufeKosten = FormelSammlung.getSktWert(kKlasse, link.getWert());
		modiStufeKosten = FormelSammlung.getSktWert(kKlasse, ((GeneratorLink) link).getWertModis());
		
		kosten = kosten - maxStufeKosten + modiStufeKosten;
		
		return kosten;
	}

	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelAdapter#finalizeSonderregel(org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public void finalizeSonderregel(Link link) {
		
		if (held.isGenerierung()) {
			// Suche und entferne den Link aus dem Element
			GeneratorLink tmpLink = (GeneratorLink) prozessor.getElementBox().getObjectById(begabtFuer);
			tmpLink.removeLinkByQuelle(this);
			
			// Korregiere evtl. Aktivierung
			prozessor.getExtendedFunctions().pruefeTalentAktivierung((GeneratorLink) tmpLink);
		}
		
		// Da die SKT nun anders ist, sollten auch die Kosten neu berechnet werden
		if ( prozessor.getElementBox().getObjectById(begabtFuer.getId()) != null) {
			prozessor.updateKosten(	
					prozessor.getElementBox().getObjectById(begabtFuer.getId()) 
			);
		}
		
		begabtFuer = null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * NUR mit gültigem ZweitZiel aufrufen (das ZweitZiel MUSS ein Talent sein)
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelAdapter#initSonderregel(org.d3s.alricg.prozessor.HeldProzessor, org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public void initSonderregel(Held held, Link srLink) {
		HeldenLink tmpLink;
		begabtFuer = (Talent) srLink.getZweitZiel();
		this.prozessor = held.getProzessor(CharKomponente.talent);
		this.held = held; 
		
		tmpLink = prozessor.getElementBox().getObjectById(begabtFuer.getId());
		
		// Erhöht die Talent-Stufe um 1
		if ( held.isGenerierung() ) {
			if ( tmpLink == null) {
				prozessor.addNewElement(begabtFuer);
			}
			
			// Link erstellen der die Eigenschaft um den gewünschten Wert erhöht  
			IdLink modiLink = new IdLink(this, null);
			modiLink.setWert(1);
			
			((GeneratorLink) tmpLink).addLink(modiLink);
			
			// Korregiere evtl. Aktivierung
			prozessor.getExtendedFunctions().pruefeTalentAktivierung((GeneratorLink) tmpLink);
		}
		
		// Da die SKT nun anders ist, sollten auch die Kosten neu berechnet werden
		if ( prozessor.getElementBox().getObjectById(begabtFuer.getId()) != null) {
			prozessor.updateKosten(	
					prozessor.getElementBox().getObjectById(begabtFuer.getId()) 
			);
		}
		
	}

	/** 
	 * Wird das Zweitziel angegeben, so wird es auch geprüft (als Talent). Wird es nicht
	 * angegeben, so wird auch nur die ID überprüft!
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter#isSonderregel(java.lang.String, java.lang.String, org.d3s.alricg.charKomponenten.CharElement)
	 */
	@Override
	public boolean isSonderregel(String id, String text, CharElement zweitZiel) {
		
		// Wenn ein zweitziel angebenen ist, so muß dieses auch stimmen
		if (zweitZiel != null) {
			if ( !zweitZiel.equals(begabtFuer) ) {
				return false;
			}
		}

		// Ansonsten wird nur die ID überprüft
		return super.isSonderregel(id, text, zweitZiel);
	}
	
	// ------------------------------------------------------------------------------
	
	/**
	 * @return Die ID des ZweitZiels, also des Elements für das die Begabung gilt
	 */
	public Talent getZweitZiel() {
		return begabtFuer;
	}

}
