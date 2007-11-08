/*
 * Created on 28.04.2005 / 00:24:18
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.generator.prozessor.utils;

import java.util.List;
import java.util.logging.Logger;

import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.GeneratorProzessor;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Faehigkeit;
import org.d3s.alricg.store.charElemente.Fertigkeit;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.held.HeldenLink;

/**
 * <u>Beschreibung:</u><br> 
 * Eine Sammlung von Methoden die f�r mehrer Prozessoren sinnvoll sind und so nicht mehrfach 
 * implementiert werden m�ssen.
 * 
 * @author V. Strelow
 */
public class ProzessorUtilities {
    
    /** <code>ProzessorUtilities</code>'s logger */
    private static final Logger LOG = Logger.getLogger(ProzessorUtilities.class.getName());
	
	/**
	 * Versucht �berpr�ft ob der Wert des Elements "link" innerhalb der m�glichen Grenzen ist.
	 * Wenn nicht wird versucht den Wert entsprechend zu setzten. Diese Methode wird
	 * beim �ndern der Herkunft ben�tigt.
	 *  
	 * @param link Der Link der �berpr�ft werden soll
	 * @param prozessor Der ProzessorXX mit dem der Link �berpr�ft wird
	 */
	public static void inspectWert(GeneratorLink link, GeneratorProzessor prozessor) {		
		// TODO Meldungen einbauen!
		
		if ( link.getWert() > prozessor.getMaxWert(link) ) {
			link.setUserGesamtWert(prozessor.getMaxWert(link));
		} else if ( link.getWert() < prozessor.getMinWert(link) ) {
			link.setUserGesamtWert(prozessor.getMinWert(link));
		}
		
	}
	
	/**
	 * �berpr�ft ob ein Link grunds�tzlich zum Held hinzuf�gbar ist, dabei wird 
	 * lediglich das Attribut "isWaehlbar" �berpr�ft. Ist "isWaehlbar" = false und 
	 * der link NICHT durch eine Herkunft hinzugef�gt, so ist das Element auch nicht 
	 * w�hlbar.
	 * - Aufruf ist nur sinnvoll f�r Instanzen von "Fertigkeit", ansonsten wird stehts "true"
	 * geliefert.
	 * 
	 * Bsp.: Die Sonderfertigkeit "Nat�rlicher RS" kann nicht gew�hlt werden, sondern
	 * 		nur durch die Herkunft erworben werden.
	 * 
	 * @param link Der Link der zu �berpr�fen ist. Das Ziel des Links sollte eine Instanz von 
	 * 		"Fertigkeit" sein, sonst ist diese �berpr�fung �berfl�ssig.
	 * @return true wenn der Link grunds�tzlich zum Helden hinzugef�gt werden kann, ansonsten
	 * 		flase
	 */
	public static boolean isWaehlbar(IdLink link) {
		
		if (link.getZiel() instanceof Fertigkeit) {
			if ( !((Fertigkeit)link.getZiel()).isAnzeigen() 
					&& link.getQuelle() == null) {
				// Element ist nicht w�hlbar und nicht durch eine Herkunft hinzugef�gt
				return false;
			}
		} else {
			LOG.warning("Diese Methode ist nur von Instanzen von Fertigkeit nutzbar," 
					+ " wurde jedoch von einer anderen Instanz aufgerufen! Klasse: " 
					+ link.getZiel().getClass().toString());
		}
		
		return true;
	}
	
	/**
	 * Liefert zu einem Array von Eigenschaften die Eigenschaft mit dem h�chsten Wert!
	 * @param eigenschaftArray Das Array mit den Eigenschaften
	 * @param prozessor Der HeldProzessor zur Verabeitung des Helden
	 * @return Die Eigenschaft mit dem gr��ten Wert innerhalb des Arrays
	 */
	public static Eigenschaft getMaxEigenschaft(Eigenschaft[] eigenschaftArray, Charakter held) {
		int maxIndex = 0;
		
		for (int i = 1; i < eigenschaftArray.length; i++) {
			if ( held.getEigenschaftsWert(eigenschaftArray[i].getEigenschaftEnum() ) 
					> held.getEigenschaftsWert(	eigenschaftArray[maxIndex].getEigenschaftEnum() ) 
				) {
					maxIndex = i;
			}
		}
		
		return eigenschaftArray[maxIndex];
	}
	
	/**
	 * Ermittelt anhand einer Liste von HeldenLink mit Faehigkeiten (Talente, Zauber) den 
	 * minimal M�glichen Wert f�r die �bergebende Eigenschaft. (Die Stufe von Talenten und 
	 * Zaubern darf die h�chste beteiligte Eigenschaft nur um 3 �bersteigen!) 
	 * 
	 * @param list Eine List mit HeldenLinks. Die Heldenlinks d�rfen nur Faehigkeiten (Talente, Zauber)
	 * 		beinhalten!
	 * @param eigenschaft Die Eigenschaft f�r die der minimal M�gliche Wert berechnet wird
	 * @param prozessor Der HeldProzessor zur Verabeitung des Helden
	 * @param minMoeglicherWert Der bisher bestimmte minimal m�gliche Wert
	 * @return Der Minimal M�gliche Wert der Eigenschaft "eigenschaft" nach der Liste "list" 
	 * 			von Faehigkeiten
	 */
	public static int getMinEigenschaftWert(List<HeldenLink> list, Eigenschaft eigenschaft, Charakter held, int minMoeglicherWert) {
		Eigenschaft[] tmpEigenArray;

		// Gibt es �berhaupt was zum pr�fen?
		if (list == null) return minMoeglicherWert;
		
		// Alle F�higkeiten durchgehen
		for (int i  = 0; i < list.size(); i++) {
			// Ist die Stufe �berhaupt so hoch, dass es in Frage kommt
			if ( (list.get(i).getWert() - 3) <= minMoeglicherWert  ) {
				continue;
			}
			
			// Den neuen Wert bestimmen
			minMoeglicherWert =
				Math.max(minMoeglicherWert, 
						getMinWertHelp(
								((Faehigkeit) list.get(i).getZiel()).getDreiEigenschaften(),
								eigenschaft,
								list.get(i).getWert(),
								held
						)
				);
		}
		
		return minMoeglicherWert;
	}
	
	/**
	 * Hilfmethode f�r die Methode "getMinWert". Nimmt ein Array von Eigenschaften und pr�ft
	 * ob der Wert NUR auf der Eigenschaft "eigenschaft" gr�nden kann, oder ob auch andere
	 * Eigenschaften aus dem Array m�glich sind.
	 * 
	 * @param eigenArray Das array von Eigenschaften (typischer Weise die 3 Eigenschaften eines
	 * 		Talents oder eines Zaubers)
	 * @param eigenschaft Die zu pr�fende Eigenschaft!
	 * @param wert Der Wert der F�higkeit
	 * @param prozessor Der HeldProzessor zur verarbeitung des Helden
	 * @return Der auf diesen Array gr�ndene Minimale Wert der gepr�ften Eigenschaft "eigenschaft"
	 */
	private static int getMinWertHelp(Eigenschaft[] eigenArray, Eigenschaft eigenschaft, int wert, Charakter held) {
		
		// Alle Eigenschaften durchgehen
		for (int i = 0; i < eigenArray.length; i++) {
			// Die Eigenschaft die �berpr�ft wird, wird �bersprungen
			if (eigenArray[i].equals(eigenschaft)) {
				continue;
			}
			
			if ( held.getEigenschaftsWert(eigenArray[i].getEigenschaftEnum()) >= (wert - 3) ) {
				// Es gibt eine andere Eigenschaft, auf der diese F�hgkeit gr�nden kann,
				// somit ist der Wert der gepr�ften Eigenschaft nicht beschr�nkt
				return 0;
			}
		}
		// Die F�higkeit gr�ndet auf der gepr�ften Eigenschaft
		return (wert - 3);
	}

	/*
	 * �berpr�ft ob ein Wert der einer Eigenschaft der maximale aus einem Array von Eigenschaften ist
	 * Gibt es einen gleichwertigen Wert in dem Array, so ist der Wrt NICHT maximal.
	 * @param eigenschaft Die Eigenschaft, dessen Wert �berpr�ft wird
	 * @param eigenschaftArray Das Array, das verglichen wird
	 * @param prozessor Der HeldProzessor mit dem er Held bearbeitet wird
	 * @return true - Der Wert von eigenschaft ist gr��er als alle  
	 *
	public static boolean isMaxEigenschaft(Eigenschaft eigenschaft, Eigenschaft[] eigenschaftArray, HeldProzessor prozessor) {
		final int wert;
		
		wert = prozessor.getHeld().getEigenschaftsWert(eigenschaft.getEigenschaftEnum());
		
		for (int i = 0; i < eigenschaftArray.length; i++) {
			if (!eigenschaft.equals(eigenschaftArray[i])) {
				if ( prozessor.getHeld().getEigenschaftsWert(
						eigenschaftArray[i].getEigenschaftEnum()
					  )
					  >= wert )
				{
					return false;
				}
			}
		}
		
		return true;
	}*/
	
	/**
	 * �berpr�ft ob ein Link mit einem Ziel das Voraussetzungen besitzen kann, diese 
	 * Voraussetzungen erf�llt. D.h. es wird gepr�ft ob der Held die entsprechenden 
	 * Elemente besitzt. 
	 * Gepr�ft wird hier die das Objekt der Klasse "Voraussetzung", die in den 
	 * entsprechenden Elementen vorhanden ist, nichts anderes! Dies beinhaltet 
	 * also NICHT: Unvereinbarkeit von Vor/ Nachteilen, Magie & Gottheit
	 * 
	 * - Aufrufbar mit instanzen von "Herkunft", "Fertigkeit", "Talent" 
	 * 
	 * Bsp.: Talent "Steinmetz" ben�tig Talent "Geisteinskunde 4"; 
	 * 		SF "Defensiver Kampfstil" ben�tigt "GE 12" und SF "Meisterparade" 
	 * 
	 * @param link Die Voraussetzungen dieses Links werden gepr�ft
	 * @return true - Die Voraussetzungen von "link" sind erf�llt, ansonsten false 
	 *
	public boolean erfuelltVoraussetzung(Link link) {
		
		// TODO implement!
		
		return true;
	}*/


	
}
