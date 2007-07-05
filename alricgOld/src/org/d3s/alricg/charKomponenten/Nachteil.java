/*
 * Created 23. Dezember 2004 / 12:52:45
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import org.d3s.alricg.controller.CharKomponente;

/**
 * <b>Beschreibung:</b><br
 * Repräsentiert das Element Nachteil.
 * @author V.Strelow
 */
public class Nachteil extends VorNachteil {
	private boolean isSchlechteEigen;
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
	 */
	public CharKomponente getCharKomponente() {
		return CharKomponente.nachteil;
	}
	
	/**
	 * Konstruktur; id beginnt mit "NAC-" für Nachteil
	 * @param id Systemweit eindeutige id
	 */
	public Nachteil(String id) {
		setId(id);
	}
	
	/**
	 * @return Liefert das Attribut isSchlechteEigen.
	 */
	public boolean isSchlechteEigen() {
		return isSchlechteEigen;
	}
	
	/**
	 * @param isSchlechteEigen Setzt das Attribut isSchlechteEigen.
	 */
	public void setSchlechteEigen(boolean isSchlechteEigen) {
		this.isSchlechteEigen = isSchlechteEigen;
	}	
}
