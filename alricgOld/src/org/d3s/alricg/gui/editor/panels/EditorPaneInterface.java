/*
 * Created on 29.03.2005 / 00:01:52
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.editor.panels;

/**
 * <u>Beschreibung:</u><br> 
 *
 * @author V. Strelow
 */
public interface EditorPaneInterface<T> {

	/**
	 * Setzt die Werte des Panels auf die des CharElements
	 * @param charElement Das Element aus dem die Werte des Panels gelesen werden
	 */
	public void loadValues(T element);
	
	/**
	 * Speichert die Werte des Panels in den Speicher, indem die Werte in das
	 * CharElement geschrieben werden.
	 * @param charElement Das Element in das die Werte des Panels geschrieben werden
	 */
	public T saveValues();

	
}
