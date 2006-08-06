/*
 * Created on 13.06.2006 / 22:09:00
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor;

import java.util.List;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.prozessor.elementBox.BaseElementBox;
import org.d3s.alricg.prozessor.elementBox.ElementBox;

/**
 * <u>Beschreibung:</u><br> 
 * Enthält Basis-Funktionen zur Verwaltung einer ElementBox innerhalb eines Prozessors.
 * Sollte die Grundlage für alle Prozessoren sein, die Elemente direkt speichern.
 * @author V. Strelow
 */
public abstract class BaseProzessorElementBox<ZIEL extends CharElement, E> {
	protected BaseElementBox<E> elementBox;
	
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
}
