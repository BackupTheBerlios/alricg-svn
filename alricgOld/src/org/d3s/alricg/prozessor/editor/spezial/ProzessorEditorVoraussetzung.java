/**
 * 
 */
package org.d3s.alricg.prozessor.editor.spezial;

import java.util.List;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.IdLinkList;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.charKomponenten.links.Voraussetzung;
import org.d3s.alricg.charKomponenten.links.Voraussetzung.VoraussetzungsAlternative;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.prozessor.BaseProzessorObserver;
import org.d3s.alricg.prozessor.editor.BaseProzessorEditorLink;
import org.d3s.alricg.prozessor.editor.EditorUtils;
import org.d3s.alricg.prozessor.elementBox.ElementBox;

/**
 * @author Vince
 *
 */
public class ProzessorEditorVoraussetzung extends BaseProzessorObserver<Link> implements BaseProzessorEditorLink<CharElement, Link>  {
	private CharElement charElementTarget;
	private Voraussetzung target;
	private IdLinkList targetDetail;
	
	/**
	 * Erzeugt einen neues Element mit Initialen Werten, fügt dieses
	 * zur Datenbasis hinzu und Informiert die Observer.
	 * @return Das neu erzeugte Element
	 */
	public Voraussetzung createNewElement(CharElement quelle) {
		Voraussetzung voraus = new Voraussetzung(quelle);
		
		VoraussetzungsAlternative altern = voraus.new VoraussetzungsAlternative();
		altern.setVoraussetzungsAlternativen(new IdLinkList[]{ });
		return voraus;
	}
	
	public void setTarget(CharElement charElement) {
		charElementTarget = charElement;
		target = charElement.getVoraussetzung();
	}
	
	public void setTargetDetail(IdLinkList linkList) {
		targetDetail = linkList;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#addNewElement(ZIEL)
	 */
	public Link addNewElement(CharElement ziel) {
		IdLink link = new IdLink(target.getQuelle(), null);
		link.setZiel(ziel);
		
		// IdLinkList erweitern
		EditorUtils.addLink( targetDetail, link );
		
		// Observer informieren
		this.notifyObserverAddElement(link);
		
		return link;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#canAddElement(ZIEL)
	 */
	public boolean canAddElement(CharElement ziel) {
		
		// Ein Talent, Eigenschaft oder Zauber kann nur einmal pro
		// Alternative enthalten sein
		if (ziel.getCharKomponente().equals(CharKomponente.talent)
				|| ziel.getCharKomponente().equals(CharKomponente.eigenschaft)
				|| ziel.getCharKomponente().equals(CharKomponente.zauber)) {
			return !EditorUtils.containsCharElement( targetDetail, ziel );
		}
		
		// ansonsten ist es potentiel möglich....
		return true;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#canRemoveElement(ELEM)
	 */
	public boolean canRemoveElement(Link element) {
		// ist grundsätzlich immer möglich... (?)
		return true;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#removeElement(ELEM)
	 */
	public void removeElement(Link element) {
		// IdLinkList erweitern
		EditorUtils.removeLink( targetDetail, element );
		
		// Observer informieren
		this.notifyObserverRemoveElement(element);
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#canUpdateText(LINK)
	 */
	public boolean canUpdateText(Link link) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#canUpdateWert(LINK)
	 */
	public boolean canUpdateWert(Link link) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#canUpdateZweitZiel(LINK, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public boolean canUpdateZweitZiel(Link link, CharElement zweitZiel) {
		// Manche CharKomponenten haben  grundsätzlich kein Zweitziel
		if (link.getZweitZiel().getCharKomponente().equals(CharKomponente.talent)
				|| link.getZweitZiel().getCharKomponente().equals(CharKomponente.eigenschaft)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#updateText(LINK, java.lang.String)
	 */
	public void updateText(Link link, String text) {
		link.setText(text);
		this.notifyObserverUpdateElement(link);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#updateWert(LINK, int)
	 */
	public void updateWert(Link link, int wert) {
		link.setWert(wert);
		this.notifyObserverUpdateElement(link);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#updateZweitZiel(LINK, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public void updateZweitZiel(Link link, CharElement zweitZiel) {
		link.setZweitZiel(zweitZiel);
		this.notifyObserverUpdateElement(link);	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.editor.BaseProzessorEditorLink#getTarget()
	 */
	public CharElement getTarget() {
		return this.charElementTarget;
	}


// ----------------- Eigentlich unnötig für diesen Prozessor -------------------------------
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#containsLink(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean containsLink(Link link) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#getElementBox()
	 */
	public ElementBox<Link> getElementBox() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#getUnmodifiableList()
	 */
	public List<Link> getUnmodifiableList() {
		// TODO Auto-generated method stub
		return null;
	}

	



}
