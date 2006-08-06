/*
 * Created on 14.06.2006 / 20:39:11
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.editor;

import org.d3s.alricg.charKomponenten.Rasse;
import org.d3s.alricg.charKomponenten.Sonderfertigkeit;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.Zauber;
import org.d3s.alricg.prozessor.Prozessor;

public class ProzessorEditorRasse extends ProzessorEditor<Rasse> {
	Prozessor<Talent, Talent> talProzessor;
	Prozessor<Zauber, Zauber> zauberProzessor;
	Prozessor<Sonderfertigkeit, Sonderfertigkeit> sfProzessor;
	// .... weitere
	
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.editor.ProzessorEditor#createNewElement()
	 */
	@Override
	public Rasse createNewElement() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.editor.ProzessorEditor#deleteElement(E)
	 */
	@Override
	public void deleteElement(Rasse element) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.editor.ProzessorEditor#isElementUsed()
	 */
	@Override
	public boolean isElementUsed() {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#addNewElement(ZIEL)
	 */
	public Rasse addNewElement(Rasse ziel) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#canAddElement(ZIEL)
	 */
	public boolean canAddElement(Rasse ziel) {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#canRemoveElement(ELEM)
	 */
	public boolean canRemoveElement(Rasse element) {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#removeElement(ELEM)
	 */
	public void removeElement(Rasse element) {
		// TODO Auto-generated method stub
		
	}
	
	
}
