/*
 * Created 14.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.charElemente;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;

/**
 * 
 * @author Vincent
 */
public class Liturgie extends Faehigkeit {
	private Gottheit[] gottheit;
	private int maxStrufe;
	
	/**
	 * Gibt an, zu welchen Gottheiten diese Liturgie ursprünglch gehört.
	 * @return the gottheit
	 */
	public Gottheit[] getGottheit() {
		return gottheit;
	}

	/**
	 * @param gottheit the gottheit to set
	 */
	public void setGottheit(Gottheit[] gottheit) {
		this.gottheit = gottheit;
	}

	/**
	 * Die maximale Stufe die erreicht werden kann
	 * @return the maxStrufe
	 */
	@XmlAttribute
	@XmlSchemaType(name = "nonNegativeInteger")
	public int getMaxStrufe() {
		return maxStrufe;
	}

	/**
	 * @param maxStrufe the maxStrufe to set
	 */
	public void setMaxStrufe(int maxStrufe) {
		this.maxStrufe = maxStrufe;
	}
	
}
