/*
 * Created on 29.06.2005 / 23:32:09
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.controller;

import org.d3s.alricg.held.Held;

/**
 * <u>Beschreibung:</u><br> 
 * Diese Klasse verwaltet die geladenen Helden und übernimmt die Erzeugung/ das Einladen von 
 * Helden. 
 * @author V. Strelow
 */
public class HeldenAdmin {
	private Held aktHeld; // Der zum bearbeiten geöffnete Held 
	
	/**
	 * @return Der Held der aktuell im Programm zum Bearbeiten geöffnet ist
	 */
	public Held getActiveHeld() {
		return aktHeld;
	}

}
