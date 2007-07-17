/*
 * Created on 01.05.2005 / 01:39:02
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente.spezial;

import org.d3s.alricg.store.charElemente.Herkunft;

/**
 * <u>Beschreibung:</u><br> 
 * Dient als Hilf-Klasse f�r "Kind zweier Welten" (siehe AH S. 124).
 * Da f�r "ZweiWeltenKultur" und "ZweiWeltenRasse" jeweils die Werte der Herkunft
 * bestimmt werden m�ssen und dies in beiden F�llen nach dem selbten schema passiert
 * ist bearbeitung daf�r in diese seperate Klasse ausgelagert, die von "ZweiWeltenKultur"
 * und "ZweiWeltenRasse" benutzt werden k�nnen.
 * 
 * @author V. Strelow
 */
public abstract class ZweiWeltenUtil {

	/**
	 * 
	 * @param resultat Dies ist die Herkunft die erzeugt wird, hier werden 
	 * 		aslo die Ergebnise der Berechungen gespeichert.
	 * @param herkunftEins
	 * @param herkunftZwei
	 */
	static void prozess(Herkunft resultat, Herkunft herkunftEins, Herkunft herkunftZwei) {
		/* TODO implement!
		 * 
		 * Es k�nnen alle Werte der Herkunft entsprechend den Regel zu "Kind zweier Welten"
		 * gesetzt werden.
		 */
	}

}
