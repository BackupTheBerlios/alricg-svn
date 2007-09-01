/*
 * Created 14.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.charElemente.charZusatz;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;

import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.RegionVolk;

/**
 * Simples CharElement um einen Gegenstand anzugeben.
 * @author Vincent
 */
public class Gegenstand extends CharElement{
	@XmlEnum
	public enum GegenstandArt {
		kleidung("Kleidung"), 
		waffen("Waffen"), 
		ruestung("Rüstung"),
		schmuck("Schmuck"),
		tinkturen("Tinkturen und Kräuter"),
		nahrung("Nahrung"),
		instrumente("Instrumente"),
		reisebedarf("Reisebedarf"), // z.B. Seil, Fakel, usw.
		fahrzeuge("Fahrzeuge"),
		tiere("Tiere"),
		sonstiges("Sonstiges");
		
		private String name; 
		
		private GegenstandArt(String name) {
			this.name = name;
		}
		
		public String toString() {
			return name;
		}
	}
	
	private GegenstandArt art;
	private RegionVolk[] herkunft;
	private int wertInKreuzern;
	/**
	 * @return the art
	 */
	public GegenstandArt getArt() {
		return art;
	}
	/**
	 * @param art the art to set
	 */
	public void setArt(GegenstandArt art) {
		this.art = art;
	}
	/**
	 * @return the herkunft
	 */
	@XmlList
	@XmlIDREF
    @XmlSchemaType(name = "IDREF")
	public RegionVolk[] getHerkunft() {
		return herkunft;
	}
	/**
	 * @param herkunft the herkunft to set
	 */
	public void setHerkunft(RegionVolk[] herkunft) {
		this.herkunft = herkunft;
	}
	/**
	 * @return the wertInHellern
	 */
	public int getWertInKreuzern() {
		return wertInKreuzern;
	}
	/**
	 * @param wertInHellern the wertInHellern to set
	 */
	public void setWertInKreuzern(int wertInKreuzern) {
		this.wertInKreuzern = wertInKreuzern;
	}
	

}
