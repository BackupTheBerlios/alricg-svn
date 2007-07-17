/*
 * Created on 04.06.2005 / 22:18:46
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente.spezial;

import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.KulturVariante;

/**
 * <u>Beschreibung:</u><br> 
 * Erstellt aus einer Kultur und einer KulturVariante eine neue, temporäre Kultur.
 * Wenn der User eine Variante zu seinem Helden auswählt, wird die resultierende
 * Kultur mit dieser Klasse erzeugt.
 * 
 * @author V. Strelow
 */
public class ModifizierteKultur extends Kultur {

	/**
	 * Erstellt aus einer original Kultur und einer Variante von einer Kultur eine
	 * neue Kultur, die auf der original Kultur basiert und durch die Variante
	 * modifiziert wurde.
	 * 
	 * @param original Die Kultur von der die Variante "abstammt"
	 * @param variante Die Variante die das original verändert
	 */
	public ModifizierteKultur(Kultur original, KulturVariante variante) {
		this.setId(variante.getId());
	}

}
