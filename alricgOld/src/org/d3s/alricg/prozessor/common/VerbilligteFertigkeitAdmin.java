/*
 * Created on 02.05.2005 / 13:48:17
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.common;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Fertigkeit;
import org.d3s.alricg.charKomponenten.Herkunft;
import org.d3s.alricg.held.Held;

/**
 * <u>Beschreibung:</u><br> 
 * Verwaltet alle Fertigkeiten(Vor-/Nachteile, SF) die durch die Herkunft 
 * (Rasse, Kultur, Profession),  durch Vor-/Nachteile oder durch Sonderregeln 
 * ver�nderte Kosten besitzen.
 * 
 * TODO Es mu� sowohl eine Ver�nderung durch Halbieren sowie eine �nderung als 
 * Festwert eingeplant werden.
 * 
 * @author V. Strelow
 */
public class VerbilligteFertigkeitAdmin {
	
	
	public VerbilligteFertigkeitAdmin(Held held) {
		
	}
	
	/**
	 * Liest alle verbilligten Fertigkeiten aus dieser Herkunft (Rasse, Kultur,
	 * Profession) aus und f�gt sie der Klasse hinzu. 
	 * Auswahlen (x oder y) werden nicht ausgelesen, dies wird  bei der Wahl 
	 * einer Fertigkeit �ber "addFertigkeit(..)" hinzugef�gt.
	 * @param herkunft Die Herkunft aus der die Fertigkeiten ausgelesen werden
	 */
	public void setHerkunft(Herkunft herkunft) {
		// TODO implement
	}
	
	/**
	 * F�gt eine Fertigkeit zu der Liste der verbilligten Fertigkeiten hinzu.
	 * @param fertigkeit Diese Fertigkeit soll ab jetzt verbilligt werden
	 * @param quelle Die Verbilligung wurde von diesem Element hervorgerufen 
	 * 		(normalerweise: Rasse, Kultur, Profession, Vor-/Nachteil, SF 
	 * 						oder Sonderregel)
	 */
	public void addFertigkeit(Fertigkeit fertigkeit, CharElement quelle) {
		// TODO implement
		// TODO Es mu� nat�rlich auch die Art/ H�he der Verbilligung sinnvoll 
		// als Parameter �bergeben werden
	}
	
	/**
	 * Entfernt eine Fertigkeit von der Liste der verbilligten Fertigkeiten
	 * @param fertigkeit Diese Fertigkeit wird entfernt
	 * @param quelle Die Verbilligung wurde von diesem Element hervorgerufen 
	 * 		(normalerweise: Rasse, Kultur, Profession, Vor-/Nachteil, SF 
	 * 						oder Sonderregel)
	 */
	public void removeFertigkeit(Fertigkeit fertigkeit, CharElement quelle) {
		// TODO implement		
	}
	
	/**
	 * Entfernd alle Fertigkeiten von der Liste der verbilligten Fertigkeiten,
	 * die �ber diese quelle (normalerweise: Rasse, Kultur, Profession, Vor-/Nachteil, 
	 * SF oder Sonderregel) hinzugef�gt werden.
	 * @param quelle Die Quelle, alle Fertigkeiten mit dieser Quelle werden entfernd
	 */
	public void removeFertigkeitByQuelle(CharElement quelle) {
		// TODO implement		
	}
	
	/**
	 * Liefert die GP-Kosten f�r diese Fertigkeit unter ber�cksichtigung der Verbilligungen.
	 * @param kosten Die bisher errechneten Kosten
	 * @param fertigkeit Die Fertigkeit f�r die die Kosten errechnet werden
	 * @return Die GP-Kosten f�r die Fertigkeit
	 */
	public int changeKostenGP(int kosten, Fertigkeit fertigkeit) {
		// TODO implement
		return kosten;
	}
	
	/**
	 * Liefert die AP-Kosten (=Talent-GP) f�r diese Fertigkeit unter ber�cksichtigung 
	 * der Verbilligungen.
	 * @param kosten Die bisher errechneten Kosten
	 * @param fertigkeit Die Fertigkeit f�r die die Kosten errechnet werden
	 * @return Die AP-Kosten (=Talent-GP) f�r diese Fertigkeit
	 */
	public int changeKostenAP(int kosten, HeldenLink fertigkeit) {
		// TODO implement
		return kosten;
	}
	
	/**
	 * Pr�ft, ob eine Fertigkeit durch den VerbilligteFertigkeitAdmin verbilligt wird.
	 * @param fertigkeit Die zu �berpr�fende Fertigkeit
	 * @return true - Die Fertigkeit "fertigkeit" wird durch den VF-Admin verbilligt
	 */
	public boolean isVerbilligt(Fertigkeit fertigkeit) {
		// TODO implement
		return false;
	}
}
