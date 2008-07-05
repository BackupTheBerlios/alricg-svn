/*
 * Created on 01.05.2005 / 01:39:44
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente.spezial;

import org.d3s.alricg.store.charElemente.Rasse;
;

/**
 * <u>Beschreibung:</u><br> 
 * Diese Klasse hat die Aufgabe aus zwei Rassen eine neue Rasse zu erstellen 
 * und diese zu Repräsentieren. Die Erstellung der neuen Rasse geschieht nach
 * den Regeln von "Kind zweier Welten" (siehe AH S. 124).
 * 
 * Nach Außen kann eine Objekt der Klasse ganz wie eine normale Profession benutz werden.
 * 
 * @author V. Strelow
 */
public class ZweiWeltenRasse extends Rasse {
	private Rasse rasseEins;
	private Rasse rasseZwei;
	
	/**
	 * Konstruktor
	 * @param rasseEins Die erste Rasse für "Kind zweiter Welten"
	 * @param rasseZwei Die zweite Rasse für "Kind zweiter Welten"
	 */
	public ZweiWeltenRasse(Rasse rasseEins, Rasse rasseZwei) {
		this.setId(rasseEins.getId() + " / " + rasseZwei.getId());
		
		this.rasseEins = rasseEins;
		this.rasseZwei = rasseZwei;
	}

	public Rasse getRasseEins() {
		return rasseEins;
	}
	
	public Rasse getRasseZwei() {
		return rasseZwei;
	}
	
	/* TODO implement!
	 * 
	 * - Das meißte sollte über die Setter erledigt werden können, ohne Methoden 
	 * überschreiben zu müssen. Man ließt einfach die Werte beider Professionen aus,
	 * bearbeitet sie entsprechend und schreibt sie per Setter in die Instanz von 
	 * "ZweiWeltenRasse".
	 */

}
