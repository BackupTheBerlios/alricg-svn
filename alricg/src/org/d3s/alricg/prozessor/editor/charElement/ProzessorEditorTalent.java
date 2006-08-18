/**
 * 
 */
package org.d3s.alricg.prozessor.editor.charElement;

import java.util.List;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.prozessor.BaseProzessorObserver;
import org.d3s.alricg.prozessor.Prozessor;
import org.d3s.alricg.prozessor.elementBox.ElementBox;

/**
 * @author Vincent
 *
 */
public class ProzessorEditorTalent extends BaseProzessorObserver<Talent> implements Prozessor<Talent, Talent> {

	
	public CharElement createNew() {
		// Erzeugt neue Voraussetzung
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#addNewElement(ZIEL)
	 */
	public Talent addNewElement(Talent ziel) {
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
	public boolean canRemoveElement(Talent element) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#getElementBox()
	 */
	public ElementBox<Talent> getElementBox() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#getUnmodifiableList()
	 */
	public List<Talent> getUnmodifiableList() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#removeElement(ELEM)
	 */
	public void removeElement(Talent element) {
		// TODO Auto-generated method stub
		
	}


}
