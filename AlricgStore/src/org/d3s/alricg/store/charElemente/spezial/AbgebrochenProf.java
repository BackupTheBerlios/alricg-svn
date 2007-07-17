/*
 * Created on 01.05.2005 / 02:14:00
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente.spezial;

import org.d3s.alricg.store.charElemente.Profession;

/**
 * <u>Beschreibung:</u><br> 
 * Diese Klasse hat die Aufgabe aus zwei Professionen eine neue Profession zu erstellen 
 * und diese zu Repr�sentieren. Die Erstellung der neuen Profession geschieht nach den
 * Regeln der "Abgebrochenen Ausbildung" (siehe AH S. 124).
 * 
 * Nach Au�en kann eine Objekt der Klasse ganz wie eine normale Profession benutz werden.
 * 
 * @author V. Strelow
 */
public class AbgebrochenProf extends Profession {
	private Profession orginalProf;
	
	/**
	 * Konstruktor
	 * @param prof Die Profession die Abgebrochen wurde
	 */
	public AbgebrochenProf(Profession prof) {
		this.setId(prof.getId() + "-abgeb");
		
		this.orginalProf = prof;
	}
	
	/*
	 * 	TODO implement !
	 * 
	 * - Das mei�te sollte �ber die Setter erledigt werden k�nnen, ohne Methoden 
	 * �berschreiben zu m�ssen. Man lie�t einfach die Werte beider Professionen aus,
	 * bearbeitet sie entsprechend und schreibt sie per Setter in die Instanz von 
	 * "AbgebrochenProf".
	 */
}
