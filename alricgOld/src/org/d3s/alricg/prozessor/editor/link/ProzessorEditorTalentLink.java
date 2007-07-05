/**
 * 
 */
package org.d3s.alricg.prozessor.editor.link;

import java.util.List;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.prozessor.BaseLinkProzessor;
import org.d3s.alricg.prozessor.BaseProzessorObserver;
import org.d3s.alricg.prozessor.elementBox.ElementBox;

/**
 * @author Vincent
 */
public class ProzessorEditorTalentLink extends BaseProzessorObserver<Link> implements BaseLinkProzessor<Talent, Link> {

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#canUpdateText(LINK)
	 */
	public boolean canUpdateText(Link link) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#canUpdateWert(LINK)
	 */
	public boolean canUpdateWert(Link link) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#canUpdateZweitZiel(LINK, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public boolean canUpdateZweitZiel(Link link, CharElement zweitZiel) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#containsLink(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean containsLink(Link link) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#updateText(LINK, java.lang.String)
	 */
	public void updateText(Link link, String text) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#updateWert(LINK, int)
	 */
	public void updateWert(Link link, int wert) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#updateZweitZiel(LINK, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public void updateZweitZiel(Link link, CharElement zweitZiel) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#addNewElement(ZIEL)
	 */
	public Link addNewElement(Talent ziel) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#canAddElement(ZIEL)
	 */
	public boolean canAddElement(Talent ziel) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#canRemoveElement(ELEM)
	 */
	public boolean canRemoveElement(Link element) {
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

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#removeElement(ELEM)
	 */
	public void removeElement(Link element) {
		// TODO Auto-generated method stub
		
	}


}
