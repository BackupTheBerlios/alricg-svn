/*
 * Created on 13.06.2006 / 22:09:00
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.common.logic;

import java.util.List;

import org.d3s.alricg.common.charakter.ElementBox;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.links.Link;

/**
 * <u>Beschreibung:</u><br> 
 * Enthält Basis-Funktionen zur Verwaltung einer ElementBox innerhalb eines Prozessors.
 * Sollte die Grundlage für alle Prozessoren sein, die Elemente direkt speichern.
 * @author V. Strelow
 */
public abstract class BaseProzessorElementBox<ZIEL extends CharElement, E extends Link> {
	protected ElementBox<E> elementBox;
	
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#getElementBox()
	 */
	public ElementBox<E> getElementBox() {
		return elementBox;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#getUnmodifiableList()
	 */
	public List<E> getUnmodifiableList() {
		return elementBox.getUnmodifiableList();
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#containsLink(org.d3s.alricg.store.charElemente.links.Link)
	 */
	public boolean containsLink(Link link) {
		return elementBox.contiansEqualObject(link);
	}
}
