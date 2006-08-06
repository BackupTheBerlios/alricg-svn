/*
 * Created on 12.02.2005 / 16:36:58
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected an under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.controller;


import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
/**
 * <u>Beschreibung:</u><br> 
 * Nimmt Nachrichten entgegen und leitet sie an alle "interessierten" Objekte (Listener)
 * die sich registriert haben weiter. Listener müssn das interface "MessageListener"
 * implementieren.
 * @author V. Strelow
 * @see org.d3s.alricg.GUI.MessageListener
 */
public class Messenger {
	
    private static final Logger LOG = Logger.getLogger(Messenger.class.getName());
    
	public enum Level { 
		frage, // Es wird eine Eingabe vom Benutzer erwartet (wird evtl. nicht benötigt)
		info, // Eine Info von Programm (laden von Elementen o.ä.)
		regeln,
		warnung, // Etwas ist evtl. fehlerhaft
		fehler, 
		fehlerSchwer
	}
	
//	Die maximale Anzahl an Nachrichten die gespeichert wird
	private final int MAX_NACHRICHTEN = 15; 
	private ArrayList<MessageListener> listenerAL; // registrierte Listener
	private ArrayList<Nachricht> nachrichtenAL;
	
	/**
	 * Konstruktor. Initialisiert das Objekt.
	 */
	public Messenger() {
		listenerAL = new ArrayList<MessageListener>(5);
		nachrichtenAL = new ArrayList<Nachricht>(MAX_NACHRICHTEN);
	}
	
	/**
	 * Ein Listener kann sich hier registrieren. Bei ankunft einer neuen Nachricht 
	 * wird der Listener über seine "neueNachricht(Nachricht)" Methode informiert.
	 * @param listener Der zu registrierende Listener.
	 */
	public void register(MessageListener listener) {
		listenerAL.add(listener);
	}
	
	/**
	 * Ein bereits registrierter Listener wird wieder abgemeldet.
	 * @param listener Der abzumeldene Listener
	 */
	public void unregister(MessageListener listener) {
		listenerAL.remove(listener);
	}
	
	/**
	 * Dient dem Absetzen von Nachrichten. Die Nachrichten werden an "interessierte"
	 * Listener weitergereicht und gespeichert.
	 * @param titel Der Titel der Nachricht
	 * @param level Die Art der Meldung
	 * @param text Die Meldung selbst (sollte zuvor per Library übersetzt sein)
	 */
	public void sendMessage(String titel, Level level, String text)  {
		registerMessage(titel, level, text);
	}
	
	/**
	 * Dient dem Absetzen einer Fehler-Nachrichten. Die Nachrichten werden an "interessierte"
	 * Listener weitergereicht und gespeichert.
	 * @param text Die Meldung selbst (sollte zuvor per Library übersetzt sein)
	 */
	public void sendFehler(String text) {
		registerMessage("Fehler", Level.fehler, text);
	}
	
	/**
	 * Dient dem Absetzen einer Info-Nachrichten. Die Nachrichten werden an "interessierte"
	 * Listener weitergereicht und gespeichert.
	 * @param text Die Meldung selbst (sollte zuvor per Library übersetzt sein)
	 */
	public void sendInfo(String text) {
		registerMessage("Information", Level.info, text);
	}
	
	/**
	 * Speichert die Meldung wie auch mit sendMessage, zusätzlich wird die Meldung jedoch 
	 * in einem Fenster angezeigt. Die Icons und Buttons sind von dem Level abhängig.
	 * @param level Die Art der Meldung
	 * @param text Die Meldung selbst (sollte zuvor per Library übersetzt sein)
	 * @return Der Rückgabewert, wie bei einem JOptionPane
	 * @see javax.swing.JOptionPane
	 */
	public int showMessage(Level level, String text) {
		int messageType, buttons;
		String titel;
		
		switch (level) {
			case frage:
				titel = "Abfrage!?";
				messageType = JOptionPane.QUESTION_MESSAGE;
				buttons = JOptionPane.YES_NO_CANCEL_OPTION;
				break;
			case info:
				titel = "Hinweis!";
				messageType = JOptionPane.INFORMATION_MESSAGE;
				buttons = JOptionPane.OK_CANCEL_OPTION;
				break;
			case warnung:
				titel = "Warnung!";
				messageType = JOptionPane.WARNING_MESSAGE;
				buttons = JOptionPane.OK_CANCEL_OPTION;
				break;
			case fehler:
			case fehlerSchwer:
				titel = "Ein Fehler ist aufgetreten!";
				messageType = JOptionPane.ERROR_MESSAGE;
				buttons = JOptionPane.OK_CANCEL_OPTION;
				break;
			default:
				LOG.severe("Nötiger Case Fall ist nicht eingetreten! ");
				return 0;
		}
		
		registerMessage(titel, level,text);
		
		return JOptionPane.showConfirmDialog(null, text, titel, buttons, messageType);
	}
	
	
	
	/**
	 * TODO Hier muß die Methode noch den Erfordernissen angepasst werden!
	 * @param level
	 * @param anzeigen
	 * @param text
	 * @param buttons
	 * @return
	 */
	private void registerMessage(String titel, Level level, String text)  {

		Nachricht tempNachricht;
		
		// Was anderes als dieser Fall sollte nie eintreten können....
		assert nachrichtenAL.size() <= MAX_NACHRICHTEN;
		
		if (nachrichtenAL.size() == MAX_NACHRICHTEN) {
			// Letzten Eintag entfernen, aufheben für "Object-Recycling"
			// Objekte zu recyclen ist effizienter als neue zu erzeugen!
			tempNachricht =	nachrichtenAL.remove(MAX_NACHRICHTEN-1);
			
		} else {
			tempNachricht = new Nachricht();
		}
		
		// Werte setzen / Objekt neu füllen "Object-Recycling"
		tempNachricht.setVaules(titel, level, text);
		
		// Als neusten Eintrag einfügen
		nachrichtenAL.add(0, tempNachricht);
		
		// Alle Listener informieren
		for (int i = 0; i < listenerAL.size(); i++) {
			listenerAL.get(i).neueNachricht(tempNachricht);
		}

	}

}
