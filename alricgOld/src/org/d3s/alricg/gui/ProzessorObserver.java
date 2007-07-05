/*
 * Created on 16.01.2006 / 15:30:18
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui;

import java.util.List;

/**
 * <u>Beschreibung:</u><br> 
 * Alle Observer eines Prozessor implementieren dieses Interface.
 * Alle Änderungen durch den Prozessor werden durch die Methoden an den 
 * Observer weitergeleiten
 * @author V. Strelow
 */
public interface ProzessorObserver {
	
	/**
	 * Ein neues Element ist zum Prozessor hinzugefügt worden
	 * @param obj Das neue Element
	 */
	public void addElement(Object obj);
	
	/**
	 * Ein Element wurde von Prozessor entfernd
	 * @param obj Das Element, welches entfernd wurde
	 */
	public void removeElement(Object obj);
	
	/**
	 * Die Werte eines Elements aus dem Prozessor wurde geändert
	 * @param obj Das Element, welches geändert wurde
	 */
	public void updateElement(Object obj);
	
	/**
	 * Alle Elemente des Prozessors wurden neu gesetzt. Alle alten Werte sind
	 * dadurch ungültig.
	 * @param list Eine Liste mit den neuen Werten des Prozessors.
	 */
	public void setData(List list);
}
