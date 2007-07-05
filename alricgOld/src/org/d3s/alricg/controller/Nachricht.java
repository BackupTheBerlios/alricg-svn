/*
 * Created on 10.06.2005 / 16:36:58
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected an under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.controller;

import org.d3s.alricg.controller.Messenger.Level;

/**
 * <u>Beschreibung:</u><br> 
 * Beinhaltet den Inhalt einer Nachricht für den Benutzer, um sie zu speichern und
 * Weiterzugeben.
 * @author V. Strelow
 */
public class Nachricht {
	String titel;
	Level level;
	String text;
	
	/** Leerer Konstruktor */ 
	public Nachricht() {
		// Noop!
	}
	/** 
	 * Konstruktr
	 * @param titel Der Titel für die Nachricht
	 * @param level Die "Art" der Nachricht
	 * @param text Der Text er Nachricht
	 */
	public Nachricht (String titel, Level level, String text) {
		this.titel = titel;
		this.level = level;
		this.text  = text;
	}
	
	/**
	 * Hiermit können alle Werte der Nachricht gesetzt werden (Sinnvoll 
	 * für das recycling von Objekten)
	 * @param titel Der Titel für die Nachricht
	 * @param level Die "Art" der Nachricht
	 * @param text Der Text er Nachricht
	 */
	public void setVaules(String titel, Level level, String text) {
		this.titel = titel;
		this.level = level;
		this.text  = text;
	}
	
	/**
	 * @return Liefert das Attribut level.
	 */
	public Level getLevel() {
		return level;
	}
	
	/**
	 * @return Der Titel der Nachricht
	 */
	public String getTitel() {
		return titel;
	}
	
	/**
	 * @return Liefert das Attribut text.
	 */
	public String getText() {
		return text;
	}
}