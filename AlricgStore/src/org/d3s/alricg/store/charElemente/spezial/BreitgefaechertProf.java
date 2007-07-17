/*
 * Created on 01.05.2005 / 02:01:53
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
 *  Regeln des Vorteils "Breitgef�cherte Bildung" (siehe AH S. 108 und das Errata von S&H).
 * Nach Au�en kann eine Objekt der Klasse ganz wie eine normale Profession benutz werden.
 *
 * @author V. Strelow
 */
public class BreitgefaechertProf extends Profession {
	private Profession profEins;
	private Profession profZwei;
	
	/**
	 * Konstruktor
	 * @param profEins Die "original" Profession, die eigentlich gew�hlt wurde
	 * @param profZwei Die Profession die durch "Breitgef�cherte Bildung" dazu kommt 
	 */
	public BreitgefaechertProf(Profession profEins, Profession profZwei) {
		this.setId(profEins.getId() + " / " + profZwei.getId());
		
		this.profEins = profEins;
		this.profZwei = profZwei;
	}
	
	/*
	 * TODO implementieren!
	 * 
	 * - Das mei�te sollte �ber die Setter erledigt werden k�nnen, ohne Methoden 
	 * �berschreiben zu m�ssen. Man lie�t einfach die Werte beider Professionen aus,
	 * bearbeitet sie entsprechend und schreibt sie per Setter in die Instanz von 
	 * "BreitgefaechertProf".
	 * - Es gibt viele gemeinsamkeiten mit "Veteran". Evtl. kann Code von
	 * beiden Klassen verwendet werden.
	 * - Nach dem Errata von S&H ist auch eine Breitgef�cherte Bildung mit 3 Professionen 
	 * m�glich. Dies kann einfach nachgebildet werden:
	 * Prof A,B,C sollen kombiniert werden. Erst wird Prof A&B kombiniert
	 * Dann wird diesen Ergebnis von A&B wieder mit C kombiniert, also ((A&B)&C).
	 * Damit dies funktioniert mu� gew�hrleistet sein das gilt:
	 * Ist A eine Erstprofession und B eine Erstprofession, so ist auch (A&B) eine
	 * Erstprofession
	 */

}
