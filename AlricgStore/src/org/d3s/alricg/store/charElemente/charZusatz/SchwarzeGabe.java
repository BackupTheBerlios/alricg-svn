/*
 * Created on 26.01.2005 / 16:42:51
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected an under the GPL licence.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente.charZusatz;

import org.d3s.alricg.store.charElemente.CharElement;

/**
 * <u>Beschreibung:</u><br>
 * 
 * @author V. Strelow
 */
public class SchwarzeGabe extends CharElement {
    private int kosten = KEIN_WERT;
    private int minStufe = KEIN_WERT;
    private int maxStufe = KEIN_WERT;
	/**
	 * @return the kosten
	 */
	public int getKosten() {
		return kosten;
	}
	/**
	 * @param kosten the kosten to set
	 */
	public void setKosten(int kosten) {
		this.kosten = kosten;
	}
	/**
	 * @return the minStufe
	 */
	public int getMinStufe() {
		return minStufe;
	}
	/**
	 * @param minStufe the minStufe to set
	 */
	public void setMinStufe(int minStufe) {
		this.minStufe = minStufe;
	}
	/**
	 * @return the maxStufe
	 */
	public int getMaxStufe() {
		return maxStufe;
	}
	/**
	 * @param maxStufe the maxStufe to set
	 */
	public void setMaxStufe(int maxStufe) {
		this.maxStufe = maxStufe;
	}

}
