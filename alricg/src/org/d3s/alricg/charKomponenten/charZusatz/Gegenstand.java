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
 * Repräsentiert alle Gegenstände in Alricg.
 * 
 * @author V.Strelow
 */
public abstract class Gegenstand extends CharElement {
	private int gewicht = KEIN_WERT;
	private RegionVolk[] erhältlichBei;
	private String einordnung;
	private int wert = KEIN_WERT;
    
    
    /**
     * Die Einordnung ist für eine bessere Sortierung da. Mögliche angaben währen "Tränke", "Kleidung", usw.
     * @return Liefert das Attribut einordnung.
     */
    public String getEinordnung() {
        return einordnung;
    }
    /**
     * @return Liefert die Region wo der Gegenstand typischerweise zu finden ist.
     */
    public RegionVolk[] getErhältlichBei() {
        return erhältlichBei;
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
	 * @param erhältlichBei Setzt das Attribut erhältlichBei.
	 */
	public void setErhältlichBei(RegionVolk[] erhältlichBei) {
		this.erhältlichBei = erhältlichBei;
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
