/*
 * Created on 05.06.2006 / 17:16:03
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor;

import java.util.List;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.common.HeldenLink;
import org.d3s.alricg.prozessor.common.SonderregelAdmin;
import org.d3s.alricg.prozessor.common.VoraussetzungenAdmin;
import org.d3s.alricg.prozessor.elementBox.ElementBox;

/**
 * <u>Beschreibung:</u><br> 
 * Diese Klasse Kapselt einen LinkProzessor und übernimmt "Standard"-Aufgaben, welche 
 * alle LinkProzessoren benötigen. Dazu gehört: Management der Sonderregeln, Voraussetungen,
 * Listener, benachrichtigung der Listener. Ein LinkProzessor darf nie direkt aufgerufen werden,
 * sondern immer über über einen LinkProzessorFront.
 * 
 * ZIEL - Das CharElement, welches als Link-Ziel verwaltete wird.
 * EXTENDED - Interface der erweiterten Funktionen, die der unter dem LinkProzessorFront
 * 		liegende LinkProzessor anbietet.
 * LINK - Die Art der Links, die verwaltet wird.
 * 
 * @author V. Strelow
 */
public class LinkProzessorFront<ZIEL extends CharElement, EXTENDED, LINK extends HeldenLink> 
										extends BaseProzessorObserver<Link> 
										implements LinkProzessor<ZIEL, LINK> {
	
//	protected ArrayList<ProzessorObserver> observerList;
	private SonderregelAdmin sonderregelAdmin;
	private VoraussetzungenAdmin voraussetzungenAdmin;
    
	protected LinkProzessor<ZIEL, LINK> prozessor;
	
	public LinkProzessorFront(Held held, LinkProzessor<ZIEL, ? extends HeldenLink> proz) {
		this.sonderregelAdmin = held.getSonderregelAdmin();
		this.voraussetzungenAdmin = held.getVoraussetzungAdmin();
		this.prozessor = (LinkProzessor<ZIEL, LINK>) proz;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#addNewElement(org.d3s.alricg.charKomponenten.CharElement, java.lang.String, org.d3s.alricg.charKomponenten.CharElement, int)
	 */
	public LINK addNewElement(ZIEL ziel) {
		LINK link;
		
		// Sonderregeln aufrufen
		if (ziel.hasSonderregel()) {
			ziel.createSonderregel().processBeforAddAsNewElement(ziel);
		}
		
		link = prozessor.addNewElement(ziel);
		addNewSonderregelUndVoraussetzung(link);
		
		// Observer zum Schluss, wenn alle Werte geändert sind!
		this.notifyObserverAddElement(link);
		
		return link;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#addModi(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public HeldenLink addModi(IdLink link) {
		HeldenLink heldLink;
		
		if (!prozessor.containsLink(link)) {
			// Element ist noch nicht vorhanden, und muss erst erzeugt werden

			// Sonderregeln aufrufen
			if (link.getZiel().hasSonderregel()) {
				link.getZiel().createSonderregel().processBeforAddAsNewElement(link.getZiel());
			}
			
			prozessor.addNewElement((ZIEL) link.getZiel());
			addNewSonderregelUndVoraussetzung(link); // Sonderregeln und Voraussetzungen
			
			heldLink = prozessor.addModi(link); // Modis setzen
			
			// Observer zum Schluss, wenn alle Werte geändert sind!
			this.notifyObserverAddElement(heldLink);
		} else {
			// Element ist bereits vorhanden, wird nur ergänzt
			
			heldLink = prozessor.addModi(link);
			this.notifyObserverUpdateElement(heldLink);
		}
		
		return heldLink;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#removeElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public void removeElement(LINK link) {
		final CharElement charElement = link.getZiel();
		
		prozessor.removeElement(link);
		sonderregelAdmin.processRemoveElement(link);
		
		// Falls dieses Element eine Sonderregel besitzt, diese entfernen
		if (charElement.hasSonderregel()) {
			sonderregelAdmin.removeSonderregel(link);
		}
		
		// Voraussetzungen entfernen, falls vorhanden
		if (charElement.getVoraussetzung() != null) {
			voraussetzungenAdmin.removeVoraussetzung(
					charElement.getVoraussetzung()
				);
		}
		
		this.notifyObserverRemoveElement(link);
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#removeModi(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public void removeModi(LINK heldLink, IdLink element) {
		final LINK genLink;
		
		// Generator-Link holen
		genLink = prozessor.getElementBox().getObjectById(element.getZiel().getId());
		
		prozessor.removeModi(genLink, element);
		
		// Es gibt keine Modis mehr, der Held hat keine Stufe gewählt,
		// das Talent wird daher vom Helden entfernd
		if (genLink.getWert() == 0) {
			removeElement(genLink);
			return;
		}
		
		this.notifyObserverUpdateElement(genLink);
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#containsLink(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean containsLink(Link link) {
		return prozessor.containsLink(link);
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canAddElement(org.d3s.alricg.charKomponenten.CharElement, java.lang.String, org.d3s.alricg.charKomponenten.CharElement, int)
	 */
	public boolean canAddElement(ZIEL ziel) {
		boolean ok; 
		IdLink tmpLink = new IdLink(null, null);
		
		tmpLink.setZiel(ziel);
		
		ok = prozessor.canAddElement(ziel);
		ok = voraussetzungenAdmin.changeCanAddElement(ok, tmpLink);
		ok = sonderregelAdmin.changeCanAddElement(ok, tmpLink);
		
		return ok;
	}

	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canAddElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canAddElement(Link element) {
		boolean ok; 
		
		ok = prozessor.canAddElement(element);
		ok = voraussetzungenAdmin.changeCanAddElement(ok, element);
		ok = sonderregelAdmin.changeCanAddElement(ok, element);
		
		return ok;
	}
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canRemoveElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canRemoveElement(LINK element) {
		boolean ok; 
		
		ok = prozessor.canRemoveElement(element);
		ok = voraussetzungenAdmin.changeCanRemoveElement(ok, element);
		ok = sonderregelAdmin.changeCanRemoveElement(ok, element);
		
		return ok;
		
	}
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateStufe(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canUpdateWert(LINK link) {
		boolean ok; 
		
		ok = prozessor.canUpdateWert(link);
		ok = sonderregelAdmin.changeCanUpdateWert(ok, link);

		return ok;
	}
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateText(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canUpdateText(LINK link) {
		boolean ok; 
		
		ok = prozessor.canUpdateText(link);
		ok = sonderregelAdmin.changeCanUpdateText(ok, link);
		
		return ok;
	}
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateZweitZiel(org.d3s.alricg.charKomponenten.links.Link, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public boolean canUpdateZweitZiel(LINK link, CharElement zweitZiel) {
		boolean ok; 
		
		ok = prozessor.canUpdateZweitZiel(link, zweitZiel);
		ok = sonderregelAdmin.changeCanUpdateZweitZiel(ok, link, zweitZiel);
		
		return ok;
	}
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMaxWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMaxWert(Link link) {
		int wert;
		
		wert = prozessor.getMaxWert(link);
		wert = voraussetzungenAdmin.changeMaxWert(wert, link);
		wert = sonderregelAdmin.changeMaxWert(wert, link);
		
		return wert;
	}
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMinWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMinWert(Link link) {
		int wert; 
		wert = prozessor.getMinWert(link);
		wert = voraussetzungenAdmin.changeMinWert(wert, link);
		wert = sonderregelAdmin.changeMinWert(wert, link);
		
		return wert;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateText(org.d3s.alricg.charKomponenten.links.Link, java.lang.String)
	 */
	public void updateText(LINK link, String text) {
		prozessor.updateText(link, text);
		this.notifyObserverUpdateElement(link);
	}
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateWert(org.d3s.alricg.charKomponenten.links.Link, int)
	 */
	public void updateWert(LINK link, int wert) {
		prozessor.updateWert(link, wert);
		this.notifyObserverUpdateElement(link);
	}
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateZweitZiel(org.d3s.alricg.charKomponenten.links.Link, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public void updateZweitZiel(LINK link, CharElement zweitZiel) {
		prozessor.updateZweitZiel(link, zweitZiel);
		this.notifyObserverUpdateElement(link);
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateKosten(E)
	 */
	public void updateKosten(LINK link) {
		prozessor.updateKosten(link);
		this.notifyObserverUpdateElement(link);
		// KEINE Sonderregel, da diese bei der Berechnung ausgeführt wird
	}

	/*
	 * (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateAllKosten()
	 */
	public void updateAllKosten() {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getGesamtKosten()
	 */
	public int getGesamtKosten() {
		return prozessor.getGesamtKosten();
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#getElementBox()
	 */
	public ElementBox<LINK> getElementBox() {
		return prozessor.getElementBox();
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#getUnmodifiableList()
	 */
	public List<LINK> getUnmodifiableList() {
		return prozessor.getUnmodifiableList();
	}

	/**
	 * Liefert das Interface zu erweiterten Funktionen des Prozessors. 
	 * Dies dient dazu, durch den LinkProzessorFront hindurch spezielle Funktionen nach 
	 * außen hin nutzen zu können
	 * @return Erweiterte Funktionen nur für diesen Prozessor
	 */
	public EXTENDED getExtendedFunctions() {
		return (EXTENDED) prozessor;
	}
	
// ----------------------- private Methoden ----------------------	
	

	/**
	 * Fügt die Sonderregel und Voraussetzung zum jeweiligen Admin hinzu 
	 * und ruft die Sonderregel auf.
	 */
	private void addNewSonderregelUndVoraussetzung(Link link) {
		
		// Voraussetzungen hinzufügen, falls vorhanden
		if (link.getZiel().getVoraussetzung() != null) {
			voraussetzungenAdmin.addVoraussetzung(
					link.getZiel().getVoraussetzung()
				);
		}
		
		// Sonderregel hinzufügen, falls vorhanden
		if (link.getZiel().hasSonderregel()) {
			sonderregelAdmin.addSonderregel(link);
		}
		
		// Sonderregeln aufrufen
		sonderregelAdmin.processAddAsNewElement(link);
	}
}
