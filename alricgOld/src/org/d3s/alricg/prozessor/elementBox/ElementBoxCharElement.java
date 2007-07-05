/*
 * Created on 06.07.2006 / 12:39:47
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.elementBox;

import java.util.List;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.links.Link;

/**
 * <u>Beschreibung:</u><br> 
 *
 * @author V. Strelow
 */
public class ElementBoxCharElement<E extends CharElement> extends BaseElementBox<E> {

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.elementBox.BaseElementBox#add(E)
	 */
	@Override
	public boolean add(E element) {
		return elementList.add(element);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.elementBox.ElementBox#contiansEqualObject(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean contiansEqualObject(Link obj) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.elementBox.ElementBox#getEqualObjects(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public List<E> getEqualObjects(Link obj) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.elementBox.ElementBox#getObjectById(org.d3s.alricg.charKomponenten.CharElement)
	 */
	public E getObjectById(CharElement charElement) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.elementBox.ElementBox#getObjectById(java.lang.String)
	 */
	public E getObjectById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
