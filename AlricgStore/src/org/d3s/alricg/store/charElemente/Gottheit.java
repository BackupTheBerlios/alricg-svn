/*
 * Created 14.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.charElemente;

import javax.xml.bind.annotation.XmlEnum;

/**
 * Repräsentiert eine Gottheit, zu welcher es Liturgieen gibt
 * @author Vincent
 */
public class Gottheit extends CharElement {
	
	@XmlEnum
	public enum GottheitArt {
		alveranNah("alveranNah"), // Z.B. Angrosch
		zwoelfGoettlich("zwoelfGoettlich"),
		animistisch("animistisch");
		private String value; // ID des Elements

		private GottheitArt(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	private GottheitArt gottheitArt;

	/**
	 * @return the gottheitArt
	 */
	public GottheitArt getGottheitArt() {
		return gottheitArt;
	}

	/**
	 * @param gottheitArt the gottheitArt to set
	 */
	public void setGottheitArt(GottheitArt gottheitArt) {
		this.gottheitArt = gottheitArt;
	}
	
	
}
