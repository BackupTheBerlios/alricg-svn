/*
 * Created 20. Januar 2005 / 16:24:31
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import org.d3s.alricg.controller.CharKomponente;

/**
 * <b>Beschreibung:</b><br>
 * Repräsentiert eine Gabe. Gaben sind laut Regelwerk Sonderf, von den Eigenschaften
 * her jedoch eher als Faehigkeit einzuordnen.
 * @author V.Strelow
 */
public class Gabe extends Faehigkeit {
	private int minStufe = KEIN_WERT;
	private int maxStufe = KEIN_WERT;
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
	 */
	public CharKomponente getCharKomponente() {
		return CharKomponente.gabe;
	}
	
	/**
	 * Konstruktur; id beginnt mit "GAB-" für Gabe
	 * @param id Systemweit eindeutige id
	 */
	public Gabe(String id) {
		setId(id);
	}
    
	/**
	 * @return Liefert das Attribut maxStufe.
	 */
	public int getMaxStufe() {
		return maxStufe;
	}
	
	/**
	 * @param maxStufe Setzt das Attribut maxStufe.
	 */
	public void setMaxStufe(int maxStufe) {
		this.maxStufe = maxStufe;
	}
	
	/**
	 * @return Liefert das Attribut minStufe.
	 */
	public int getMinStufe() {
		return minStufe;
	}
	
	/**
	 * @param minStufe Setzt das Attribut minStufe.
	 */
	public void setMinStufe(int minStufe) {
		this.minStufe = minStufe;
	}
}
