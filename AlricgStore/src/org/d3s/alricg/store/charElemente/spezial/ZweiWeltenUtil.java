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
 * Dient als Hilf-Klasse für "Kind zweier Welten" (siehe AH S. 124).
 * Da für "ZweiWeltenKultur" und "ZweiWeltenRasse" jeweils die Werte der Herkunft
 * bestimmt werden müssen und dies in beiden Fällen nach dem selbten schema passiert
 * ist bearbeitung dafür in diese seperate Klasse ausgelagert, die von "ZweiWeltenKultur"
 * und "ZweiWeltenRasse" benutzt werden können.
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
		 * Es können alle Werte der Herkunft entsprechend den Regel zu "Kind zweier Welten"
		 * gesetzt werden.
		 */
	}

}
