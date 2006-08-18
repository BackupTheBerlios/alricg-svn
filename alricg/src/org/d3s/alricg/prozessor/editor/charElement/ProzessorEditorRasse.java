/*
 * Created on 14.06.2006 / 20:39:11
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.editor.charElement;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Rasse;
import org.d3s.alricg.charKomponenten.Sonderfertigkeit;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.Zauber;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.prozessor.Prozessor;
import org.d3s.alricg.prozessor.editor.BaseProzessorEditorCharElement;
import org.d3s.alricg.prozessor.editor.BaseProzessorEditorLink;

public class ProzessorEditorRasse extends BaseProzessorEditorCharElement<Rasse> {
	BaseProzessorEditorLink<Talent, Link> talProzessor;
	Prozessor<Zauber, Zauber> zauberProzessor;
	Prozessor<Sonderfertigkeit, Sonderfertigkeit> sfProzessor;
	// .... weitere
	
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.editor.BaseProzessorEditorCharElement#createNewElement()
	 */
	@Override
	public Rasse createNewElement() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void addElement(CharElement elem) {
		
	}
	
	public void removeElement(CharElement elem) {
		
	}
	
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.editor.BaseProzessorEditorCharElement#isElementUsed()
	 */
	@Override
	public boolean isElementUsed(Rasse element) {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#addNewElement(ZIEL)
	 */
	public Rasse addNewElement(Rasse ziel) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#canAddElement(ZIEL)
	 */
	public boolean canAddElement(Rasse ziel) {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#canRemoveElement(ELEM)
	 */
	public boolean canRemoveElement(Rasse element) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#removeElement(ELEM)
	 */
	public void removeElement(Rasse element) {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
