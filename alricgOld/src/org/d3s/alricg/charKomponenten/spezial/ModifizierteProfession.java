/*
 * Created on 04.06.2005 / 22:19:04
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten.spezial;

import org.d3s.alricg.charKomponenten.Profession;
import org.d3s.alricg.charKomponenten.ProfessionVariante;

/**
 * <u>Beschreibung:</u><br> 
 * Erstellt aus einer Profession und einer ProfessionVariante eine neue, temporäre Profession.
 * Wenn der User eine Variante zu seinem Helden auswählt, wird die resultierende
 * Profession mit dieser Klasse erzeugt.
 * 
 * @author V. Strelow
 */
public class ModifizierteProfession extends Profession {

	/**
	 * Erstellt aus einer original Profession und einer Variante von einer Profession eine
	 * neue Profession, die auf der original Profession basiert und durch die Variante
	 * modifiziert wurde.
	 * 
	 * @param original Die Profession von der die Variante "abstammt"
	 * @param variante Die Variante die das original verändert
	 */
	public ModifizierteProfession(Profession original, ProfessionVariante variante) {
		super(original.getId());
	}

}
