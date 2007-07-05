/*
 * Created 23. Dezember 2004 / 14:53:48
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.store.FactoryFinder;

/**
 * <b>Beschreibung:</b><br> TODO Beschreibung einfügen
 * @author V.Strelow
 */
public class Talent extends Faehigkeit {
	
	public enum Art {
		basis("basis"), 
		spezial("spezial"), 
		beruf("beruf");
		private String value; // XML-Tag des Elements
		private String bezeichner; // Name der Angezeigt wird
		
		private Art(String value) {
			this.value = value;
			bezeichner = FactoryFinder.find().getLibrary().getShortTxt(value);
		}
		
		public String getValue() {
			return value;
		}
		
		public String toString() {
			return bezeichner;
		}
	}
	public enum Sorte {
		kampf("kampf"), 
		koerper("koerper"), 
		gesellschaft("gesellschaft"), 
		natur("natur"), 
		wissen("wissen"), 
		handwerk("handwerk");
		private String value; // XML-Tag des Elements
		private String bezeichner; // Name der Angezeigt wird
		
		private Sorte(String value) {
			this.value = value;
			bezeichner = FactoryFinder.find().getLibrary().getShortTxt(value);
		}
		
		public String getValue() {
			return value;
		}
		
		public String toString() {
			return bezeichner;
		}
	}
	
	private String[] spezialisierungen;
	private Art art;
    private Sorte sorte;
    
    
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
	 */
	public CharKomponente getCharKomponente() {
		return CharKomponente.talent;
	}
    
	/**
	 * Konstruktur id; beginnt mit "TAL-" für Talent
	 * @param id Systemweit eindeutige id
	 */
	public Talent(String id) {
		setId(id);
	}
    
	/**
	 * @return Liefert das Attribut art.
	 */
	public Art getArt() {
		return art;
	}
	/**
	 * @return Liefert das Attribut sorte.
	 */
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
