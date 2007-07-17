/*
 * Created on 04.06.2005 / 21:20:27
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente.spezial;

import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.RasseVariante;

/**
 * <u>Beschreibung:</u><br> 
 * Erstellt aus einer Rasse und einer RassenVariante eine neue, temporäre Rasse.
 * Wenn der User eine Variante zu seinem Helden auswählt, wird die resultierende
 * Rasse mit dieser Klasse erzeugt.
 * 
 * @author V. Strelow
 */
public class ModifizierteRasse extends Rasse {

	/**
	 * Erstellt aus einer original Rasse und einer Variante von einer Rasse eine
	 * neue Rasse, die auf der original Rasse basiert und durch die Variante
	 * modifiziert wurde.
	 * 
	 * @param original Die Rasse von der die Variante "abstammt"
	 * @param variante Die Variante die das original verändert
	 */
	public ModifizierteRasse(Rasse original, RasseVariante variante) {
		this.setId(variante.getId());
	}

}
