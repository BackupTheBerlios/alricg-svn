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
 * und diese zu Repräsentieren. Die Erstellung der neuen Profession geschieht nach den
 * Regeln des Vorteils "Veteran" (siehe AH S. 111 und das Errata von S&H).
 * Nach Außen kann eine Objekt der Klasse ganz wie eine normale Profession benutz werden.
 * 
 * @author V. Strelow
 */
public class VeteranProf extends Profession {
	private Profession profEins;
	private Profession profZwei;
	
	/**
	 * Konstruktor
	 * @param profEins Die "original" Profession, die eigentlich gewählt wurde
	 * @param profZwei DIe Veteran-Profession die gewählt wurde
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
	 * - Das meißte sollte über die Setter erledigt werden können, ohne Methoden 
	 * überschreiben zu müssen. Man ließt einfach die Werte beider Professionen aus,
	 * bearbeitet sie entsprechend und schreibt sie per Setter in die Instanz von 
	 * "VeteranProf".
	 * - Es gibt viele gemeinsamkeiten mit "Breitgefächerter Bildung". Evtl. kann Code von
	 * beiden Klassen verwendet werden.
	 * - Nach dem Errata von S&H ist es auch Möglich unterschiedliche Professionen
	 * mittels "Veteran" zu verbinden, daher werden auch zwei Professionen angegeben 
	 * (diese können aber natürlich auch gleich sein, was dem klassischen Veteran
	 * entspricht)
	 */
}
