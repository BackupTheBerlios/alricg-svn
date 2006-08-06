/*
 * Created 20. Januar 2005 / 17:17:22
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten.charZusatz;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.controller.CharKomponente;

/**
 * <b>Beschreibung:</b><br> TODO Beschreibung einf�gen
 * @author V.Strelow
 */
public class Tier extends CharElement{

	// TODO Die Felder f�r das Tier anlegen!
    // TODO XOMMapper_Tier anpassen!
	
	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
	 */
	public CharKomponente getCharKomponente() {
		return CharKomponente.tier;
	}
	
	/**
	 * Konstruktur; id beginnt mit "TIE-" f�r Tier
	 * @param id Systemweit eindeutige id
	 */
	public Tier(String id) {
		setId(id);
	}
}
