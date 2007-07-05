/*
 * Created on 21.06.2006 / 11:33:02
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.controller;

import org.d3s.alricg.charKomponenten.sonderregeln.principle.Sonderregel;

/**
 * <u>Beschreibung:</u><br> 
 * Diese Klasse dient der Speicherung von Meldungen die zu einer Nachricht zusammen
 * gesetzt werden. 
 * Diese Klasse ist vor allem dafür gedacht, die Verarbeitung eines Helden für den User
 * transparent zu gestalten. Bei bestimmten operationen (Kostenberechnung, ob ein Element
 * hinzugefügt/entfernt werden kann) werden durch den Helden Prozessor die
 * einzelnen Schritte der Berechnung über das Notepad festgehalten. 
 * Diese können dann vom User angezeigt werden
 * 
 * @author V. Strelow
 */
public class Notepad {
	/*	 Ist das Notepad aktiv? Ansonsten werden keine Nachrichen gespeichert
	 * (bessere performance)*/
	private boolean isActive; 
	private StringBuilder messageBuf; // Sammelt die Texte
	
	/**
	 * Macht das Notepad bereit für den erhalt einer neuen Message.
	 * Wird von der GUI aufgerufen, wenn der User eine Nachricht sehen möchte.
	 */
	public void startMessage() {
		isActive = true;
		
		messageBuf = new StringBuilder();
		messageBuf.append("<html>");
	}
	
	/**
	 * Schließt eine Message ab. Wird von der GUI aufgerufen, wenn die gewünschte 
	 * Berechnung abgeschlossen ist.
	 * @return Der aufgezeichnet Text zu der Nachricht.
	 */
	public String endMessage() {
		isActive = false;
		
		// Löschen des letzten "<br>"
		messageBuf.delete(messageBuf.length()- 4, messageBuf.length() + 1);
		messageBuf.append("</html>");
		
		return messageBuf.toString();
	}
	
	/**
	 * @return true - Das Notepad zeichnet gerade Nachrichten auf, ansonsten false
	 * Zeichnet das Notepad KEINE Nachrichten auf, so werden die "write"-Methoden
	 * ignoriert
	 */
	public boolean hasActiveMessage() {
		return isActive;
	}
	
	/**
	 * @return Der aufgezeichnet Text zu der letzten Nachricht.
	 */
	public String getActiveMessage() {
		return messageBuf.toString();
	}
	
	/**
	 * Wenn Sonderregeln eine Änderung vornehmen, wird dies über diese Methode
	 * aufgezeichnet.
	 * 
	 * @param sr Die Sonderregel, welche die Änderung vorgenommen hat
	 * @param text Der Text zu der Änderung
	 */
	public void writeSonderregelMessage(Sonderregel sr, String text) {
		if (!isActive) return;
		
		messageBuf.append("&#8226; ")
					.append("SR ")
					.append(sr.getName())
					.append(": ")
					.append(text)
					.append("<br>");
	}
	
	/**
	 * Fügt einen Text zur Message hinzu.
	 * 
	 * @param text Text der hinzugefügt werden soll
	 */
	public void writeMessage(String text) {
		if (!isActive) return;
		
		messageBuf.append("&#8226; ").append(text).append("<br>");
	}
	
	/**
	 * TODO implement
	 */
	public void writeVoraussetzungMessage() {
		if (!isActive) return;
		
		// TODO implement		
	}
}
