/*
 * Created 20. Januar 2005 / 17:07:37
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
public class NahkWaffe extends Waffe {
	private int kkAb = KEIN_WERT; // Ab diesem Wert gibt es TP Zuschlag
	private int kkStufe = KEIN_WERT; // Ab diesem Wert gibt es weitere TP Zuschlag
	private int bf = KEIN_WERT; // Bruchfaktor
	private String dk; // Distanzklasse
	private int wmAT = KEIN_WERT; // Waffenmodifikator / AT
	private int wmPA = KEIN_WERT; // Waffenmodifikator / PA
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
	 */
	public CharKomponente getCharKomponente() {
		return CharKomponente.waffeNk;
	}
	
	/**
	 * Konstruktur; id beginnt mit "NKW-" für Nahkampf-Waffe
	 * @param id Systemweit eindeutige id
	 */
	public NahkWaffe(String id) {
		setId(id);
	}
	
	/**
	 * @return Liefert den Bruchfaktor
	 */
	public int getBf() {
		return bf;
	}
	/**
	 * @return Liefert die Distanzklasse(n).
	 */
	public String getDk() {
		return dk;
	}
	/**A b diesem Wert gibt es TP-Zuschlag.
	 * @return Liefert das Attribut kkAb.
	 */
	public int getKkAb() {
		return kkAb;
	}
	/** Ab getKkAb()+ diesen Wert gibt es einen weiteren TP-Zuschlag.
	 * @return Liefert das Attribut kkStufe.
	 */
	public int getKkStufe() {
		return kkStufe;
	}
	/**
	 * @return Liefert den Waffenmodifikator / AT.
	 */
	public int getWmAT() {
		return wmAT;
	}
	/**
	 * @return Liefert den Waffenmodifikator / PA
	 */
	public int getWmPA() {
		return wmPA;
	}
	
	/**
	 * @param bf Setzt das Attribut bf.
	 */
	public void setBf(int bf) {
		this.bf = bf;
	}
	/**
	 * @param dk Setzt das Attribut dk.
	 */
	public void setDk(String dk) {
		this.dk = dk;
	}
	/**
	 * @param kkAb Setzt das Attribut kkAb.
	 */
	public void setKkAb(int kkAb) {
		this.kkAb = kkAb;
	}
	/**
	 * @param kkStufe Setzt das Attribut kkStufe.
	 */
	public void setKkStufe(int kkStufe) {
		this.kkStufe = kkStufe;
	}
	/**
	 * @param wmAT Setzt das Attribut wmAT.
	 */
	public void setWmAT(int wmAT) {
		this.wmAT = wmAT;
	}
	/**
	 * @param wmPA Setzt das Attribut wmPA.
	 */
	public void setWmPA(int wmPA) {
		this.wmPA = wmPA;
	}
}
