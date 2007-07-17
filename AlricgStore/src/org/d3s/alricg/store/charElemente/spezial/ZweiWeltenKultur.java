/*
 * Created on 01.05.2005 / 01:39:32
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente.spezial;

import org.d3s.alricg.store.charElemente.Kultur;

/**
 * <u>Beschreibung:</u><br> 
 * Diese Klasse hat die Aufgabe aus zwei Kulturen eine neue kultur zu erstellen 
 * und diese zu Repräsentieren. Die Erstellung der neuen Kultur geschieht nach
 * den Regeln von "Kind zweier Welten" (siehe AH S. 124).
 * 
 * Nach Außen kann eine Objekt der Klasse ganz wie eine normale Profession benutz werden.
 * 
 * @author V. Strelow
 */
public class ZweiWeltenKultur extends Kultur {
	private Kultur kulturEins;
	private Kultur kulturZwei;
	
	/**
	 * Konstruktor
	 * @param kulturEins Die erste Kultur für "Kind zweiter Welten"
	 * @param kulturZwei Die zweite Kultur für "Kind zweiter Welten"
	 */
	public ZweiWeltenKultur(Kultur kulturEins, Kultur kulturZwei) {
		this.setId(kulturEins.getId() + " / " + kulturZwei.getId());
		ZweiWeltenUtil.prozess(this, kulturEins, kulturZwei);
		
		this.kulturEins = kulturEins;
		this.kulturZwei = kulturZwei;
		
	}
	
	/* TODO implement!
	 * 
	 * - Das meißte sollte über die Setter erledigt werden können, ohne Methoden 
	 * überschreiben zu müssen. Man ließt einfach die Werte beider Professionen aus,
	 * bearbeitet sie entsprechend und schreibt sie per Setter in die Instanz von 
	 * "ZweiWeltenKultur".
	 */
}
