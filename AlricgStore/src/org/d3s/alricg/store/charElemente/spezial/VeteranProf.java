/*
 * Created on 01.05.2005 / 02:13:38
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
 * Regeln des Vorteils "Veteran" (siehe AH S. 111 und das Errata von S&H).
 * Nach Au�en kann eine Objekt der Klasse ganz wie eine normale Profession benutz werden.
 * 
 * @author V. Strelow
 */
public class VeteranProf extends Profession {
	private Profession profEins;
	private Profession profZwei;
	
	/**
	 * Konstruktor
	 * @param profEins Die "original" Profession, die eigentlich gew�hlt wurde
	 * @param profZwei DIe Veteran-Profession die gew�hlt wurde
	 */
	public VeteranProf(Profession profEins, Profession profZwei) {
		this.setId(profEins.getId() + " / " + profZwei.getId());
		
		this.profEins = profEins;
		this.profZwei = profZwei;
	}

	/**
	 * @return the profEins
	 */
	public Profession getProfEins() {
		return profEins;
	}

	/**
	 * @return the profZwei
	 */
	public Profession getProfZwei() {
		return profZwei;
	}
	
	/*
	 * 	TODO implement !
	 * 
	 * - Das mei�te sollte �ber die Setter erledigt werden k�nnen, ohne Methoden 
	 * �berschreiben zu m�ssen. Man lie�t einfach die Werte beider Professionen aus,
	 * bearbeitet sie entsprechend und schreibt sie per Setter in die Instanz von 
	 * "VeteranProf".
	 * - Es gibt viele gemeinsamkeiten mit "Breitgef�cherter Bildung". Evtl. kann Code von
	 * beiden Klassen verwendet werden.
	 * - Nach dem Errata von S&H ist es auch M�glich unterschiedliche Professionen
	 * mittels "Veteran" zu verbinden, daher werden auch zwei Professionen angegeben 
	 * (diese k�nnen aber nat�rlich auch gleich sein, was dem klassischen Veteran
	 * entspricht)
	 */
}
