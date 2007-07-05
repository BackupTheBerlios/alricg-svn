/*
 * Created on 01.03.2005 / 13:39:16
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten.charZusatz;

import org.d3s.alricg.controller.CharKomponente;

/**
 * <u>Beschreibung:</u><br> 
 * Eine simple Implementierung der abstrakten Klasse "Gegenstand".
 * Diese Klasse diend zur repräsentation von Gegenstanden, ohne die für bestimmte
 * Gegenstände typischen zusätzlichen Attribute (TP, RS). Dient vor allem um in den
 * Kulturen / Professionen direkt angegebene Gegenstande abzubilden (Tag "ausruestungNeu").  
 * @author V. Strelow
 */
public class SimpelGegenstand extends Gegenstand {
	public static final String TEMP_ID = "SIM-temp"; 
//	 Legt die art des Gegenstandes Fest, da dies aus der Klasse nicht erkennbar ist
	private CharKomponente art;
	private int anzahl; 
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
	 */
	public CharKomponente getCharKomponente() {
		return art;
	}
	
	/**
	 * Konstruktor
	 */
	public SimpelGegenstand() {
		this.setId(TEMP_ID);
	}

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
	 * @return Liefert das Attribut art.
	 */
	public CharKomponente getArt() {
		return art;
	}
	/**
	 * @param art Setzt das Attribut art.
	 */
	public void setArt(CharKomponente art) {
		this.art = art;
	}
}
