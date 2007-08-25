/*
 * Created 23. Dezember 2004 / 14:53:48
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;


/**
 * Repräsentiert ein Talent
 * @author V.Strelow
 */
public class Talent extends Faehigkeit {
	@XmlEnum
	public enum Art {
		basis("basis"), 
		spezial("spezial"), 
		beruf("beruf");
		private String name; // XML-Tag des Elements
		
		private Art(String name) {
			this.name = name;
		}
		
		public String toString() {
			return name;
		}
	}
	
	@XmlEnum
	public enum Sorte {
		kampf("kampf"), 
		koerper("koerper"), 
		gesellschaft("gesellschaft"), 
		natur("natur"), 
		wissen("wissen"), 
		handwerk("handwerk"),
		spezial("spezial");
		private String name; // XML-Tag des Elements
		
		private Sorte(String name) {
			this.name = name;
		}
		
		public String toString() {
			return name;
		}
	}
	
	private Art art;
    private Sorte sorte;
	private String[] spezialisierungen;

	/**
	 * @return Liefert das Attribut art.
	 */
	@XmlElement(required = true)
	public Art getArt() {
		return art;
	}
	/**
	 * @return Liefert das Attribut sorte.
	 */
	@XmlElement(required = true)
	public Sorte getSorte() {
		return sorte;
	}
	
	/**
	 * @return Liefert das Attribut spezialisierungen.
	 */
	public String[] getSpezialisierungen() {
		return spezialisierungen;
	}
	/**
	 * @param spezialisierungen Setzt das Attribut spezialisierungen.
	 */
	public void setSpezialisierungen(String[] spezialisierungen) {
		this.spezialisierungen = spezialisierungen;
	}
	/**
	 * @param art Setzt das Attribut art.
	 */
	public void setArt(Art art) {
		this.art = art;
	}
	/**
	 * @param sorte Setzt das Attribut sorte.
	 */
	public void setSorte(Sorte sorte) {
		this.sorte = sorte;
	}
}
