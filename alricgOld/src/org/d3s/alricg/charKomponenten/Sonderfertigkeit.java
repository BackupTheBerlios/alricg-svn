/*
 * Created 23. Dezember 2004 / 12:53:19
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import org.d3s.alricg.controller.CharKomponente;

/**
 * <b>Beschreibung:</b><br>
 * Repräsentiert eine Sonderfertigkeit.
 * @author V.Strelow
 */
public class Sonderfertigkeit extends Fertigkeit {
	
	public enum Art {
		allgemein("allgemein"),
		waffenloskampf("waffenlosKampf"), 
		bewaffnetkampf("bewaffnetKampf"), 
		magisch("magisch"), 
		geweiht("geweiht"), 
		schamanisch("schamanisch"), 
		sonstige("sonstige");
		private String value; // Value des Elements
		
		private Art(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}

	private int apKosten = KEIN_WERT; // falls sich AP nicht aus GP berechnen lassen
	private Art art;
	private int permAsp = 0, permKa = 0, permLep = 0; // Permanente Kosten

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
	 */
	public CharKomponente getCharKomponente() {
		return CharKomponente.sonderfertigkeit;
	}
	
	/**
	 * Konstruktur; id beginnt mit "SF-" für Sonderfertigkeit
	 * @param id Systemweit eindeutige id
	 */
	public Sonderfertigkeit(String id) {
		setId(id);
	}
	
	/**
	 * @return Liefert das Attribut ap - die Kosten für diese SF in
	 * Abenteuerpunkten.
	 */
	public int getApKosten() {
		return apKosten;
	}	
	
	/**
	 * @return Liefert das Attribut art.
	 */
	public Art getArt() {
		return art;
	}

	
	
	/**
	 * @return Liefert das Attribut permAsp.
	 */
	public int getPermAsp() {
		return permAsp;
	}
	/**
	 * @param permAsp Setzt das Attribut permAsp.
	 */
	public void setPermAsp(int permAsp) {
		this.permAsp = permAsp;
	}
	/**
	 * @return Liefert das Attribut permKa.
	 */
	public int getPermKa() {
		return permKa;
	}
	/**
	 * @param permKa Setzt das Attribut permKa.
	 */
	public void setPermKa(int permKa) {
		this.permKa = permKa;
	}
	/**
	 * @return Liefert das Attribut permLep.
	 */
	public int getPermLep() {
		return permLep;
	}
	/**
	 * @param permLep Setzt das Attribut permLep.
	 */
	public void setPermLep(int permLep) {
		this.permLep = permLep;
	}
	/**
	 * @param apKosten Setzt das Attribut apKosten.
	 */
	public void setApKosten(int apKosten) {
		this.apKosten = apKosten;
	}
	/**
	 * @param art Setzt das Attribut art.
	 */
	public void setArt(Art art) {
		this.art = art;
	}

}
