/*
 * Created 23. Dezember 2004 / 12:52:45
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente;

import javax.xml.bind.annotation.XmlAttribute;


/**
 * Repräsentiert das Element Nachteil.
 * @author V.Strelow
 */
public class Nachteil extends VorNachteil {
	private boolean isSchlechteEigen;
	
	/**
	 * @return Liefert das Attribut isSchlechteEigen.
	 */
	@XmlAttribute
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
