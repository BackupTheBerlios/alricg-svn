/*
 * Created on 02.05.2005 / 13:45:34
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.generator.prozessor;

import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.Rasse;

/**
 * <u>Beschreibung:</u><br> 
 * Verwaltet alle Auswahlen die den Helden (und somit dem Benutzer) aktuell 
 * zur Verfügung stehen. Wird etwas ausgewählt, so wird dies hier festgehalten, 
 * so daß Auswahlen später wieder angezeigt und geändert werden können.
 * 	
 * @author V. Strelow
 */
public class AuswahlProzessor {

	/*
	 * TODO Hier fehlen noch viele Methoden zu auswählen, zurückliefern, 
	 * und bearbeiten der wählbaren Optionen.  
	 */
	
	public AuswahlProzessor() {
		
	}
	
	/**
	 * Liest alle Auswahlen aus dieser Rasse aus und fügt sie der Klasse hinzu. 
	 * @param rasse Die Rasse aus der die Auswahlen gelesen werden
	 */
	public void setRasse(Rasse rasse) {
		setHerkunft(rasse); 
		// TODO Implement
	}
	
	/**
	 * Liest alle Auswahlen aus dieser Kultur aus und fügt sie der Klasse hinzu. 
	 * @param kultur Die Kultur aus der die Auswahlen gelesen werden
	 */
	public void setKultur(Kultur kultur) {
		setHerkunft(kultur); 
		// TODO Implement		
	}
	
	/**
	 * Liest alle Auswahlen aus dieser Proession aus und fügt sie der Klasse hinzu. 
	 * @param profession Die Profession aus der die Auswahlen gelesen werden
	 */
	public void setProfession(Profession prof) {
		setHerkunft(prof); 
		// TODO Implement		
	}
	
	/**
	 * Liest alle Auswahlen der Herkunft aus und legt sie entsprechend in dieser 
	 * Klasse ab. 
	 * @param herkunft Die Herkunft aus der die Auswahlen ausgelesen werden
	 */
	private void setHerkunft(Herkunft herkunft) {
		// TODO implement
	}

	
}
