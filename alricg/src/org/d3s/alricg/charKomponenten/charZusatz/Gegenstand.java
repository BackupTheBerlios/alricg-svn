/*
 * Created 20. Januar 2005 / 17:01:28
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten.charZusatz;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.RegionVolk;
/**
 * <b>Beschreibung:</b><br>
 * Repr�sentiert alle Gegenst�nde in Alricg.
 * 
 * @author V.Strelow
 */
public abstract class Gegenstand extends CharElement {
	private int gewicht = KEIN_WERT;
	private RegionVolk[] erh�ltlichBei;
	private String einordnung;
	private int wert = KEIN_WERT;
    
    
    /**
     * Die Einordnung ist f�r eine bessere Sortierung da. M�gliche angaben w�hren "Tr�nke", "Kleidung", usw.
     * @return Liefert das Attribut einordnung.
     */
    public String getEinordnung() {
        return einordnung;
    }
    /**
     * @return Liefert die Region wo der Gegenstand typischerweise zu finden ist.
     */
    public RegionVolk[] getErh�ltlichBei() {
        return erh�ltlichBei;
    }
    /**
     * @return Liefert das Gewicht des Gegenstandes in Unzen.
     */
    public int getGewicht() {
        return gewicht;
    }
    /**
     * @return Liefert der Wert des Gegenstandes in Kreuzern.
     */
    public int getWert() {
        return wert;
    }
    
	/**
	 * @param einordnung Setzt das Attribut einordnung.
	 */
	public void setEinordnung(String einordnung) {
		this.einordnung = einordnung;
	}
	/**
	 * @param erh�ltlichBei Setzt das Attribut erh�ltlichBei.
	 */
	public void setErh�ltlichBei(RegionVolk[] erh�ltlichBei) {
		this.erh�ltlichBei = erh�ltlichBei;
	}
	/**
	 * @param gewicht Setzt das Attribut gewicht.
	 */
	public void setGewicht(int gewicht) {
		this.gewicht = gewicht;
	}
	/**
	 * @param wert Setzt das Attribut wert.
	 */
	public void setWert(int wert) {
		this.wert = wert;
	}
}
