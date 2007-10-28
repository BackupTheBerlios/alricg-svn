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
 * Diese Klasse �berwacht alle Voraussetzungen die ein Held erf�llen mu�. 
 * Es werden alle Voraussetungen zu einem Helden in dieser Klasse gesammelt 
 * und verwaltet, so dass folgendes abgefragt werden kann:
 * 1. Die min /max Werte aller CharElemente
 * 2. Ob ein Element entfernd oder hinzugef�gt werden kann
 * 3. Ob eine Voraussetzung erf�llt ist
 * 
 * @author V. Strelow
 */
public class VoraussetzungenAdmin {
	private ArrayList<Voraussetzung> voraussetAL = new ArrayList<Voraussetzung>();
	private CharakterDaten held;
	
	/**
	 * Konstruktor
	 * @param CharakterDaten Held der zu dem diesem VoraussetzungenAdmin geh�rt
	 */
	public VoraussetzungenAdmin(CharakterDaten held) {
		this.held = held;
	}
	
	/**
	 * F�gt eine Voraussetzung zum Admin hinzu. �ber diese Voraussetzung verf�gt
	 * der Held nun.
	 * @param voraussetzung Die neue Voraussetzung die der Held nun erf�llen mu�
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
	 * Pr�ft ob es f�r diesen Link Voraussetzungen gibt und liefert den entsprechenden
	 * Wert zur�ck.
	 * @param maxStufe Die bisher bestimmte maximale Stufe
	 * @param link Der Link f�r den der maximale Wert bestimmt werden soll
	 * @return Der Maximal m�gliche Wert
	 */
	public int changeMaxWert(int maxStufe, Link link) {
		// TODO implement
		return maxStufe;
	}
	
	/**
	 * Pr�ft ob es f�r diesen Link Voraussetzungen gibt und liefert den entsprechenden
	 * Wert zur�ck.
	 * @param minStufe Die bisher bestimmte minimale Stufe
	 * @param link Der Link f�r den der minimale Wert bestimmt werden soll
	 * @return Der minimal m�gliche Wert
	 */
	public int changeMinWert(int minStufe, Link link) {
		// TODO implement
		return minStufe;
	}
	
	/**
	 * Pr�ft ob ein Element hinzugef�gt werden kann. Dabei geht es darum, ob 
	 * andere, bereits zum Helden geh�rende CharElemente mit diesem Element
	 * unvereinbar sind, es also verbieten oder anders herum ein Verbot
	 * besteht. 
	 * ACHTUNG: Es werden NICHT die Voraussetzungen des "tmpLink" gepr�ft! Daf�r 
	 * gibt es die Methode "isVoraussetzungErfuellt(Voraussetzung voraussetzung)"
	 * @param canAdd Das Ergebnis der bisherigen Pr�fung
 	 * @param tmpLink Der Link, der gepr�ft werden soll
	 * @return true - Das Element ist nicht unvereinbar mit bereits zum Helden
	 * 		geh�renden Elementen, ansonsten false
	 */
	public boolean changeCanAddElement(boolean canAdd, Link tmpLink) {
		// TODO implement
		return canAdd;
	}
	
	/**
	 * Pr�ft ob ein Element aus dem Helden entfernd werden kann. Dabei geht 
	 * es darum, ob andere, bereits zum Helden geh�rende CharElemente dieses 
	 * Element als Voraussetzung ben�tigen. 
	 * 
	 * @param canRemove Das Ergebnis der bisherigen Pr�fung
 	 * @param tmpLink Der Link, der gepr�ft werden soll
	 * @return true - Das Element ist nicht Voraussetzung f�r andere
	 * 		zum Helden geh�rende Elemnet, ansonsten false
	 */
	public boolean changeCanRemoveElement(boolean canRemove, Link link) {
		// TODO implement
		return canRemove;
	}
	
	/**
	 * �berpr�ft ob oraussetzungen von Held erf�llt werden, also der Held diese 
	 * Voraussetzungen erf�llt. D.h. es wird gepr�ft ob der Held die entsprechenden 
	 * Elemente besitzt, bzw. bestimmte Elemente NICHT besitzt.
	 * 
	 * Bsp.: Talent "Steinmetz" ben�tig Talent "Geisteinskunde 4"; 
	 * 		SF "Defensiver Kampfstil" ben�tigt "GE 12" und SF "Meisterparade" 
	 * 
	 * @param link Die Voraussetzungen die gepr�ft werden
	 * @return true - Die Voraussetzungen sind erf�llt, ansonsten false 
	 */
	public boolean isVoraussetzungErfuellt(Voraussetzung voraussetzung) {
		// TODO implement
		return true;
	}
	
	/**
	 * �berpr�ft ob alle Elemente eines Helden noch ihre Vorraussetzungen erf�llen, OHNE 
	 * das Element "link".
	 * - Aufrufbar mit Instanzen aller CharElementen, au�er Gegenst�nde (TODO �berpr�fen
	 *  ob weitere einschr�nkungen gemacht werden k�nnen) 
	 * 
	 * Bsp.: SF "Defensiver Kampfstil" ben�tigt "GE 12" und SF "Meisterparade" =>
	 * 		Hat der Held SF "Defensiver Kampfs." kann der nicht mehr SF "Meisterparade"
	 * 		aus dem Helden entfernen. 
	 * 
	 * @param link Das Element was �berpr�ft wird, ob ohne diese Element der Held noch
	 * 			alle Voraussetzungen erf�llt
	 * @return true Der Held erf�llt auch ohne "link" alle Voraussetzungen, ansonsten false 
	 *
	public boolean erfuelltVoraussetzungOhne(Link link) {
		
		// TODO implement! 
		
		return true;
	}*/
}
