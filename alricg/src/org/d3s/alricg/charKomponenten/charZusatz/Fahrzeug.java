/*
 * Created 20. Januar 2005 / 17:38:35
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
public class Fahrzeug extends Gegenstand {
	private String typ; // Der name, z.B. "Kastenwagen"
	private String aussehen; // Ein allgemeiner Text zur Farbe, Zustand, usw.
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
	 */
	public CharKomponente getCharKomponente() {
		return CharKomponente.fahrzeug;
	}
	
	/**
	 * Konstruktur; id beginnt mit "FAH-" für Fahrzeug
	 * @param id Systemweit eindeutige id
	 */
	public Fahrzeug(String id) {
		setId(id);
	}
	
	/**
	 * @return Liefert das Attribut aussehen.
	 */
	public String getAussehen() {
		return aussehen;
	}
	
	/**
	 * @return Liefert das Attribut bezeichung.
	 */
	public String getTyp() {
		return typ;
	}
	
	
	
	/**
	 * @param aussehen Setzt das Attribut aussehen.
	 */
	public void setAussehen(String aussehen) {
		this.aussehen = aussehen;
	}
	/**
	 * @param typ Setzt das Attribut typ.
	 */
	public void setTyp(String typ) {
		this.typ = typ;
	}
}
