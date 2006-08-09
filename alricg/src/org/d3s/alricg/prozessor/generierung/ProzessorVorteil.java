/*
 * Created on 29.07.2006 / 14:25:40
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.generierung;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Vorteil;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.prozessor.BaseProzessorElementBox;
import org.d3s.alricg.prozessor.LinkProzessor;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.common.HeldenLink;

/**
 * <u>Beschreibung:</u><br> 
 *
 * @author V. Strelow
 */
public class ProzessorVorteil extends BaseProzessorElementBox<Vorteil, GeneratorLink> implements
		LinkProzessor<Vorteil, GeneratorLink> {

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#addModi(org.d3s.alricg.charKomponenten.links.IdLink)
	 */
	public HeldenLink addModi(IdLink element) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canAddElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canAddElement(Link link) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateText(LINK)
	 */
	public boolean canUpdateText(GeneratorLink link) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateWert(LINK)
	 */
	public boolean canUpdateWert(GeneratorLink link) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateZweitZiel(LINK, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public boolean canUpdateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#containsLink(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean containsLink(Link link) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getGesamtKosten()
	 */
	public int getGesamtKosten() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMaxWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMaxWert(Link link) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMinWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMinWert(Link link) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#removeModi(LINK, org.d3s.alricg.charKomponenten.links.IdLink)
	 */
	public void removeModi(GeneratorLink heldLink, IdLink element) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateKosten(LINK)
	 */
	public void updateKosten(GeneratorLink Link) {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateAllKosten()
	 */
	public void updateAllKosten() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateText(LINK, java.lang.String)
	 */
	public void updateText(GeneratorLink link, String text) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateWert(LINK, int)
	 */
	public void updateWert(GeneratorLink link, int wert) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateZweitZiel(LINK, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public void updateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#addNewElement(ZIEL)
	 */
	public GeneratorLink addNewElement(Vorteil ziel) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#canAddElement(ZIEL)
	 */
	public boolean canAddElement(Vorteil ziel) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#canRemoveElement(ELEM)
	 */
	public boolean canRemoveElement(GeneratorLink element) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#removeElement(ELEM)
	 */
	public void removeElement(GeneratorLink element) {
		// TODO Auto-generated method stub
		
	}


}
