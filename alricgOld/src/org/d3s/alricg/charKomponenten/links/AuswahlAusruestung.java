/*
 * Created on 01.03.2005 / 13:48:04
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten.links;

import org.d3s.alricg.charKomponenten.Herkunft;
import org.d3s.alricg.charKomponenten.charZusatz.SimpelGegenstand;

/**
 * <u>Beschreibung:</u><br> 
 * Repräsentiert eine Auswahl von Ausrüstungsgegenständen für Kultur / Professionen
 * @author V. Strelow
 */
public class AuswahlAusruestung {
	
	private Herkunft herkunft;
	private HilfsAuswahl festeAuswahl; // Die unveränderlichen Werte
	private HilfsAuswahl[] variableAuswahl;
	
	/**
	 * Konstruktor
	 * @param herkunft Die "quelle" dieser Auswahl
	 */
	public AuswahlAusruestung(Herkunft herkunft) {
		this.herkunft = herkunft;
	}
	

	/**
	 * @return Liefert das Attribut festeAuswahl.
	 */
	public HilfsAuswahl getFesteAuswahl() {
		return festeAuswahl;
	}
	/**
	 * @param festeAuswahl Setzt das Attribut festeAuswahl.
	 */
	public void setFesteAuswahl(HilfsAuswahl festeAuswahl) {
		this.festeAuswahl = festeAuswahl;
	}
	/**
	 * @return Liefert das Attribut herkunft.
	 */
	public Herkunft getHerkunft() {
		return herkunft;
	}
	/**
	 * @param herkunft Setzt das Attribut herkunft.
	 */
	public void setHerkunft(Herkunft herkunft) {
		this.herkunft = herkunft;
	}
	/**
	 * @return Liefert das Attribut variableAuswahl.
	 */
	public HilfsAuswahl[] getVariableAuswahl() {
		return variableAuswahl;
	}
	/**
	 * @param variableAuswahl Setzt das Attribut variableAuswahl.
	 */
	public void setVariableAuswahl(HilfsAuswahl[] variableAuswahl) {
		this.variableAuswahl = variableAuswahl;
	}
    /**
     * <u>Beschreibung:</u><br> 
     *  Hilfklasse für die Datenhaltung
     * @author V. Strelow
     */
    public class HilfsAuswahl  {
    	private int anzahl = 1;
    	private IdLink[] links;
    	private SimpelGegenstand[] simpGegenstand;
    	
		/**
		 * @return Liefert das Attribut anzahl.
		 */
		public int getAnzahl() {
			return anzahl;
		}
		/**
		 * @param anzahl Setzt das Attribut anzahl.
		 */
		public void setAnzahl(int anzahl) {
			this.anzahl = anzahl;
		}
		/**
		 * @return Liefert das Attribut links.
		 */
		public IdLink[] getLinks() {
			return links;
		}
		/**
		 * @param links Setzt das Attribut links.
		 */
		public void setLinks(IdLink[] links) {
			this.links = links;
		}
		/**
		 * @return Liefert das Attribut simpGegenstand.
		 */
		public SimpelGegenstand[] getSimpGegenstand() {
			return simpGegenstand;
		}
		/**
		 * @param simpGegenstand Setzt das Attribut simpGegenstand.
		 */
		public void setSimpGegenstand(SimpelGegenstand[] simpGegenstand) {
			this.simpGegenstand = simpGegenstand;
		}
    }
}
