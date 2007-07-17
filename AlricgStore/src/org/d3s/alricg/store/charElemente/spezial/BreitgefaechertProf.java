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
 * und diese zu Repräsentieren. Die Erstellung der neuen Profession geschieht nach den
 *  Regeln des Vorteils "Breitgefächerte Bildung" (siehe AH S. 108 und das Errata von S&H).
 * Nach Außen kann eine Objekt der Klasse ganz wie eine normale Profession benutz werden.
 *
 * @author V. Strelow
 */
public class BreitgefaechertProf extends Profession {
	private Profession profEins;
	private Profession profZwei;
	
	/**
	 * Konstruktor
	 * @param profEins Die "original" Profession, die eigentlich gewählt wurde
	 * @param profZwei Die Profession die durch "Breitgefächerte Bildung" dazu kommt 
	 */
	public BreitgefaechertProf(Profession profEins, Profession profZwei) {
		this.setId(profEins.getId() + " / " + profZwei.getId());
		
		this.profEins = profEins;
		this.profZwei = profZwei;
	}
	
	/*
	 * TODO implementieren!
	 * 
	 * - Das meißte sollte über die Setter erledigt werden können, ohne Methoden 
	 * überschreiben zu müssen. Man ließt einfach die Werte beider Professionen aus,
	 * bearbeitet sie entsprechend und schreibt sie per Setter in die Instanz von 
	 * "BreitgefaechertProf".
	 * - Es gibt viele gemeinsamkeiten mit "Veteran". Evtl. kann Code von
	 * beiden Klassen verwendet werden.
	 * - Nach dem Errata von S&H ist auch eine Breitgefächerte Bildung mit 3 Professionen 
	 * möglich. Dies kann einfach nachgebildet werden:
	 * Prof A,B,C sollen kombiniert werden. Erst wird Prof A&B kombiniert
	 * Dann wird diesen Ergebnis von A&B wieder mit C kombiniert, also ((A&B)&C).
	 * Damit dies funktioniert muß gewährleistet sein das gilt:
	 * Ist A eine Erstprofession und B eine Erstprofession, so ist auch (A&B) eine
	 * Erstprofession
	 */

}
