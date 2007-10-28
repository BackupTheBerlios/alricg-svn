/*
 * Created on 20.07.2005 / 17:44:06
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.common.charakter;

import java.util.ArrayList;

import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.charElemente.links.Voraussetzung;
import org.d3s.alricg.store.held.CharakterDaten;

/**
 * <u>Beschreibung:</u><br> 
 * Diese Klasse überwacht alle Voraussetzungen die ein Held erfüllen muß. 
 * Es werden alle Voraussetungen zu einem Helden in dieser Klasse gesammelt 
 * und verwaltet, so dass folgendes abgefragt werden kann:
 * 1. Die min /max Werte aller CharElemente
 * 2. Ob ein Element entfernd oder hinzugefügt werden kann
 * 3. Ob eine Voraussetzung erfüllt ist
 * 
 * @author V. Strelow
 */
public class VoraussetzungenAdmin {
	private ArrayList<Voraussetzung> voraussetAL = new ArrayList<Voraussetzung>();
	private CharakterDaten held;
	
	/**
	 * Konstruktor
	 * @param CharakterDaten Held der zu dem diesem VoraussetzungenAdmin gehört
	 */
	public VoraussetzungenAdmin(CharakterDaten held) {
		this.held = held;
	}
	
	/**
	 * Fügt eine Voraussetzung zum Admin hinzu. Über diese Voraussetzung verfügt
	 * der Held nun.
	 * @param voraussetzung Die neue Voraussetzung die der Held nun erfüllen muß
	 */
	public void addVoraussetzung(Voraussetzung voraussetzung) {
		voraussetAL.add(voraussetzung);
	}
	
	/**
	 * Entfernt eine Voraussetzung vom Helden.
	 * @param voraussetzung Die Voraussetzung die von Held nun entfernt wird
	 */
	public void removeVoraussetzung(Voraussetzung voraussetzung) {
		voraussetAL.remove(voraussetzung);
	}
	
	/**
	 * Prüft ob es für diesen Link Voraussetzungen gibt und liefert den entsprechenden
	 * Wert zurück.
	 * @param maxStufe Die bisher bestimmte maximale Stufe
	 * @param link Der Link für den der maximale Wert bestimmt werden soll
	 * @return Der Maximal mögliche Wert
	 */
	public int changeMaxWert(int maxStufe, Link link) {
		// TODO implement
		return maxStufe;
	}
	
	/**
	 * Prüft ob es für diesen Link Voraussetzungen gibt und liefert den entsprechenden
	 * Wert zurück.
	 * @param minStufe Die bisher bestimmte minimale Stufe
	 * @param link Der Link für den der minimale Wert bestimmt werden soll
	 * @return Der minimal mögliche Wert
	 */
	public int changeMinWert(int minStufe, Link link) {
		// TODO implement
		return minStufe;
	}
	
	/**
	 * Prüft ob ein Element hinzugefügt werden kann. Dabei geht es darum, ob 
	 * andere, bereits zum Helden gehörende CharElemente mit diesem Element
	 * unvereinbar sind, es also verbieten oder anders herum ein Verbot
	 * besteht. 
	 * ACHTUNG: Es werden NICHT die Voraussetzungen des "tmpLink" geprüft! Dafür 
	 * gibt es die Methode "isVoraussetzungErfuellt(Voraussetzung voraussetzung)"
	 * @param canAdd Das Ergebnis der bisherigen Prüfung
 	 * @param tmpLink Der Link, der geprüft werden soll
	 * @return true - Das Element ist nicht unvereinbar mit bereits zum Helden
	 * 		gehörenden Elementen, ansonsten false
	 */
	public boolean changeCanAddElement(boolean canAdd, Link tmpLink) {
		// TODO implement
		return canAdd;
	}
	
	/**
	 * Prüft ob ein Element aus dem Helden entfernd werden kann. Dabei geht 
	 * es darum, ob andere, bereits zum Helden gehörende CharElemente dieses 
	 * Element als Voraussetzung benötigen. 
	 * 
	 * @param canRemove Das Ergebnis der bisherigen Prüfung
 	 * @param tmpLink Der Link, der geprüft werden soll
	 * @return true - Das Element ist nicht Voraussetzung für andere
	 * 		zum Helden gehörende Elemnet, ansonsten false
	 */
	public boolean changeCanRemoveElement(boolean canRemove, Link link) {
		// TODO implement
		return canRemove;
	}
	
	/**
	 * Überprüft ob oraussetzungen von Held erfüllt werden, also der Held diese 
	 * Voraussetzungen erfüllt. D.h. es wird geprüft ob der Held die entsprechenden 
	 * Elemente besitzt, bzw. bestimmte Elemente NICHT besitzt.
	 * 
	 * Bsp.: Talent "Steinmetz" benötig Talent "Geisteinskunde 4"; 
	 * 		SF "Defensiver Kampfstil" benötigt "GE 12" und SF "Meisterparade" 
	 * 
	 * @param link Die Voraussetzungen die geprüft werden
	 * @return true - Die Voraussetzungen sind erfüllt, ansonsten false 
	 */
	public boolean isVoraussetzungErfuellt(Voraussetzung voraussetzung) {
		// TODO implement
		return true;
	}
	
	/**
	 * Überprüft ob alle Elemente eines Helden noch ihre Vorraussetzungen erfüllen, OHNE 
	 * das Element "link".
	 * - Aufrufbar mit Instanzen aller CharElementen, außer Gegenstände (TODO überprüfen
	 *  ob weitere einschränkungen gemacht werden können) 
	 * 
	 * Bsp.: SF "Defensiver Kampfstil" benötigt "GE 12" und SF "Meisterparade" =>
	 * 		Hat der Held SF "Defensiver Kampfs." kann der nicht mehr SF "Meisterparade"
	 * 		aus dem Helden entfernen. 
	 * 
	 * @param link Das Element was überprüft wird, ob ohne diese Element der Held noch
	 * 			alle Voraussetzungen erfüllt
	 * @return true Der Held erfüllt auch ohne "link" alle Voraussetzungen, ansonsten false 
	 *
	public boolean erfuelltVoraussetzungOhne(Link link) {
		
		// TODO implement! 
		
		return true;
	}*/
}
