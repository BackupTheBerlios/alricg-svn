/*
 * Created on 29.07.2006 / 14:25:40
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.generierung;

import java.util.List;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Vorteil;
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
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorVorteil;

/**
 * <u>Beschreibung:</u><br> 
 * Prozessor für das Bearbeiten von Vorteilen. Alle Änderungen die an Vorteilen
 * durchgeführt werden, werden über diesen Prozessor durchgeführt.
 * 
 * Zu beachtene Unterschiede zum Talent-, Zauber-, und EigenschaftProzessor:
 * 	- Ein Vorteil kann ggf. auch mehrfach zum Charakter hinzugefügt werden. (z.B. "herausragender Sinn...")
 * 	- Vorteile können, müssen aber nicht: einen Wert, Text oder ein ZweitZiel besitzen.
 *  - Bestimmte Vorteile können nur von bestimmten CharArten gewählt werden (z.B. Zauberer, geweihte)
 *  - Die Änderungen der Kosten, auch durch Vorteile, wird im Prozessor nicht berechnet. Dies
 *  	übernimmt der "VerbilligteKostenAdmin"
 *   
 * @author name
 */
public class ProzessorVorteil extends BaseProzessorElementBox<Vorteil, GeneratorLink> 
					implements LinkProzessor<Vorteil, GeneratorLink>, ExtendedProzessorVorteil {
	
	private final SonderregelAdmin sonderregelAdmin;
	private final VerbilligteFertigkeitAdmin verbFertigkeitenAdmin;
	private final Notepad notepade;
	private final Held held;
	
	
	public ProzessorVorteil(Held held, Notepad notepade) {
		this.held = held;
		this.notepade = notepade;
		this.sonderregelAdmin = held.getSonderregelAdmin();
		this.verbFertigkeitenAdmin = held.getVerbFertigkeitenAdmin();
		this.elementBox = new ElementBoxLink<GeneratorLink>();
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#canUpdateText(LINK)
	 */
	public boolean canUpdateText(GeneratorLink link) {
		/*
		 * Ein Text kann dann gesetzt (und geupdated) werden, wenn 
		 * - Vorteil.hasFreienText = true 
		 * - oder Vorteil.textVorschlaege.length > 0
		 * (bzw. wenn der Link bereits einen text besitzt)
		 */
		
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#canUpdateWert(LINK)
	 */
	public boolean canUpdateWert(GeneratorLink link) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#canUpdateZweitZiel(LINK, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public boolean canUpdateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#containsLink(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean containsLink(Link link) {
		// Hier reicht es nicht die ID zu überprüfen wie bei Talenten. 
		// Es muß mit "elementBox.contiansEqualObject" geprüft werden, um auch Text und ZweitZiel zu überprüfen
		
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#updateText(LINK, java.lang.String)
	 */
	public void updateText(GeneratorLink link, String text) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#updateWert(LINK, int)
	 */
	public void updateWert(GeneratorLink link, int wert) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#updateZweitZiel(LINK, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public void updateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#addModi(org.d3s.alricg.charKomponenten.links.IdLink)
	 */
	public HeldenLink addModi(IdLink element) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canAddElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canAddElement(Link link) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getGesamtKosten()
	 */
	public int getGesamtKosten() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMaxWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMaxWert(Link link) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMinWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMinWert(Link link) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#removeModi(LINK, org.d3s.alricg.charKomponenten.links.IdLink)
	 */
	public void removeModi(GeneratorLink heldLink, IdLink element) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateAllKosten()
	 */
	public void updateAllKosten() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateKosten(LINK)
	 */
	public void updateKosten(GeneratorLink Link) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#addNewElement(ZIEL)
	 */
	public GeneratorLink addNewElement(Vorteil ziel) {
		/*
		 * Beim hinzufügen eines neuen Vorteils muß ein Link erzeugt werden. Hierfür müssen
		 * auch alle notwendigen Angaben des Links gemacht werden. Hierbei gilt folgendes:
		 * - ist "Vorteil.hasFreienText == true", wird der Text auf etwas wie "Bitte Text eingeben" gesetzt
		 * - ist "Vorteil.hasFreienText == false" und "textVorschlaege" ist gesetzt, wird der erste Eintrag 
		 * 		als Standard gesetzt
		 * - ist "Vorteil.hasElementAngabe == true" wird ebenfalls das erste Element der Liste gesetzt
		 * - Die Stufe wird auf den minimalWert gesetzt
		 */
		
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#canAddElement(ZIEL)
	 */
	public boolean canAddElement(Vorteil ziel) {
		// Kosten sind hierbei egal. 
		// Elemente die sich gegenseitig ausschließen ("Gutaussehend", "Widerwärtiges Aussehen") werden
		// ebenfalls nicht überprüft
		
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#canRemoveElement(ELEM)
	 */
	public boolean canRemoveElement(GeneratorLink element) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#removeElement(ELEM)
	 */
	public void removeElement(GeneratorLink element) {
		// TODO Auto-generated method stub
		
	}

	// -------------------- Methoden aus dem Extended Interface ---------------------------------
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorVorteil#addMoeglicheZweitZiele(org.d3s.alricg.charKomponenten.Vorteil, java.util.List)
	 */
	public void addMoeglicheZweitZiele(Vorteil vorteil, List<CharElement> zweitZiele) {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorVorteil#getMoeglicheZweitZiele(org.d3s.alricg.charKomponenten.Vorteil)
	 */
	public List<CharElement> getMoeglicheZweitZiele(Vorteil vorteil) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorVorteil#removeMoeglicheZweitZiele(org.d3s.alricg.charKomponenten.Vorteil)
	 */
	public void removeMoeglicheZweitZiele(Vorteil vorteil) {
		// TODO Auto-generated method stub
		
	}

}
