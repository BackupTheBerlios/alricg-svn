/*
 * Created 20. Januar 2005 / 17:07:50
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten.charZusatz;

import org.d3s.alricg.controller.CharKomponente;

/**
 * <b>Beschreibung:</b><br> TODO Beschreibung einfügen
 * @author V.Strelow
 */
public class FkWaffe extends Waffe {
	private int laden = KEIN_WERT;
	private int reichweite = KEIN_WERT;
	private String reichweiteTpPlus; // Zusätzliche TP durch Reichweite

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
	 */
	public CharKomponente getCharKomponente() {
		return CharKomponente.waffeFk;
	}
	
	/**
	 * Konstruktur; id beginnt mit "FKW-" für Fernkampf-Waffe
	 * @param id Systemweit eindeutige id
	 */
	public FkWaffe(String id) {
		setId(id);
	}
	
	/**
	 * @return Liefert das Attribut laden.
	 */
	public int getLaden() {
		return laden;
	}
	
	/**
	 * @return Liefert das Attribut reichweite.
	 */
	public int getReichweite() {
		return reichweite;
	}
	
	/**
	 * @return Liefert das Attribut reichweiteTpPlus.
	 */
	public String getReichweiteTpPlus() {
		return reichweiteTpPlus;
	}

	/**
	 * @param laden Setzt das Attribut laden.
	 */
	public void setLaden(int laden) {
		this.laden = laden;
	}
	/**
	 * @param reichweite Setzt das Attribut reichweite.
	 */
	public void setReichweite(int reichweite) {
		this.reichweite = reichweite;
	}
	/**
	 * @param reichweiteTpPlus Setzt das Attribut reichweiteTpPlus.
	 */
	public void setReichweiteTpPlus(String reichweiteTpPlus) {
		this.reichweiteTpPlus = reichweiteTpPlus;
	}
}
