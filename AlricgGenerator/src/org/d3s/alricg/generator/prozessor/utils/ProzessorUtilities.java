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
 * Eine Sammlung von Methoden die für mehrer Prozessoren sinnvoll sind und so nicht mehrfach 
 * implementiert werden müssen.
 * 
 * @author V. Strelow
 */
public class ProzessorUtilities {
    
    /** <code>ProzessorUtilities</code>'s logger */
    private static final Logger LOG = Logger.getLogger(ProzessorUtilities.class.getName());
	
	/**
	 * Versucht überprüft ob der Wert des Elements "link" innerhalb der möglichen Grenzen ist.
	 * Wenn nicht wird versucht den Wert entsprechend zu setzten. Diese Methode wird
	 * beim Ändern der Herkunft benötigt.
	 *  
	 * @param link Der Link der überprüft werden soll
	 * @param prozessor Der ProzessorXX mit dem der Link überprüft wird
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
	 * Überprüft ob ein Link grundsätzlich zum Held hinzufügbar ist, dabei wird 
	 * lediglich das Attribut "isWaehlbar" überprüft. Ist "isWaehlbar" = false und 
	 * der link NICHT durch eine Herkunft hinzugefügt, so ist das Element auch nicht 
	 * wählbar.
	 * - Aufruf ist nur sinnvoll für Instanzen von "Fertigkeit", ansonsten wird stehts "true"
	 * geliefert.
	 * 
	 * Bsp.: Die Sonderfertigkeit "Natürlicher RS" kann nicht gewählt werden, sondern
	 * 		nur durch die Herkunft erworben werden.
	 * 
	 * @param link Der Link der zu überprüfen ist. Das Ziel des Links sollte eine Instanz von 
	 * 		"Fertigkeit" sein, sonst ist diese Überprüfung überflüssig.
	 * @return true wenn der Link grundsätzlich zum Helden hinzugefügt werden kann, ansonsten
	 * 		flase
	 */
	public static boolean isWaehlbar(IdLink link) {
		
		if (link.getZiel() instanceof Fertigkeit) {
			if ( !((Fertigkeit)link.getZiel()).isAnzeigen() 
					&& link.getQuelle() == null) {
				// Element ist nicht wählbar und nicht durch eine Herkunft hinzugefügt
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
	 * Liefert zu einem Array von Eigenschaften die Eigenschaft mit dem höchsten Wert!
	 * @param eigenschaftArray Das Array mit den Eigenschaften
	 * @param prozessor Der HeldProzessor zur Verabeitung des Helden
	 * @return Die Eigenschaft mit dem größten Wert innerhalb des Arrays
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
	 * minimal Möglichen Wert für die übergebende Eigenschaft. (Die Stufe von Talenten und 
	 * Zaubern darf die höchste beteiligte Eigenschaft nur um 3 übersteigen!) 
	 * 
	 * @param list Eine List mit HeldenLinks. Die Heldenlinks dürfen nur Faehigkeiten (Talente, Zauber)
	 * 		beinhalten!
	 * @param eigenschaft Die Eigenschaft für die der minimal Mögliche Wert berechnet wird
	 * @param prozessor Der HeldProzessor zur Verabeitung des Helden
	 * @param minMoeglicherWert Der bisher bestimmte minimal mögliche Wert
	 * @return Der Minimal Mögliche Wert der Eigenschaft "eigenschaft" nach der Liste "list" 
	 * 			von Faehigkeiten
	 */
	public static int getMinEigenschaftWert(List<HeldenLink> list, Eigenschaft eigenschaft, Charakter held, int minMoeglicherWert) {
		Eigenschaft[] tmpEigenArray;

		// Gibt es überhaupt was zum prüfen?
		if (list == null) return minMoeglicherWert;
		
		// Alle Fähigkeiten durchgehen
		for (int i  = 0; i < list.size(); i++) {
			// Ist die Stufe überhaupt so hoch, dass es in Frage kommt
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
	 * Hilfmethode für die Methode "getMinWert". Nimmt ein Array von Eigenschaften und prüft
	 * ob der Wert NUR auf der Eigenschaft "eigenschaft" gründen kann, oder ob auch andere
	 * Eigenschaften aus dem Array möglich sind.
	 * 
	 * @param eigenArray Das array von Eigenschaften (typischer Weise die 3 Eigenschaften eines
	 * 		Talents oder eines Zaubers)
	 * @param eigenschaft Die zu prüfende Eigenschaft!
	 * @param wert Der Wert der Fähigkeit
	 * @param prozessor Der HeldProzessor zur verarbeitung des Helden
	 * @return Der auf diesen Array gründene Minimale Wert der geprüften Eigenschaft "eigenschaft"
	 */
	private static int getMinWertHelp(Eigenschaft[] eigenArray, Eigenschaft eigenschaft, int wert, Charakter held) {
		
		// Alle Eigenschaften durchgehen
		for (int i = 0; i < eigenArray.length; i++) {
			// Die Eigenschaft die überprüft wird, wird übersprungen
			if (eigenArray[i].equals(eigenschaft)) {
				continue;
			}
			
			if ( held.getEigenschaftsWert(eigenArray[i].getEigenschaftEnum()) >= (wert - 3) ) {
				// Es gibt eine andere Eigenschaft, auf der diese Fähgkeit gründen kann,
				// somit ist der Wert der geprüften Eigenschaft nicht beschränkt
				return 0;
			}
		}
		// Die Fähigkeit gründet auf der geprüften Eigenschaft
		return (wert - 3);
	}

	/*
	 * Überprüft ob ein Wert der einer Eigenschaft der maximale aus einem Array von Eigenschaften ist
	 * Gibt es einen gleichwertigen Wert in dem Array, so ist der Wrt NICHT maximal.
	 * @param eigenschaft Die Eigenschaft, dessen Wert überprüft wird
	 * @param eigenschaftArray Das Array, das verglichen wird
	 * @param prozessor Der HeldProzessor mit dem er Held bearbeitet wird
	 * @return true - Der Wert von eigenschaft ist größer als alle  
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
	 * Überprüft ob ein Link mit einem Ziel das Voraussetzungen besitzen kann, diese 
	 * Voraussetzungen erfüllt. D.h. es wird geprüft ob der Held die entsprechenden 
	 * Elemente besitzt. 
	 * Geprüft wird hier die das Objekt der Klasse "Voraussetzung", die in den 
	 * entsprechenden Elementen vorhanden ist, nichts anderes! Dies beinhaltet 
	 * also NICHT: Unvereinbarkeit von Vor/ Nachteilen, Magie & Gottheit
	 * 
	 * - Aufrufbar mit instanzen von "Herkunft", "Fertigkeit", "Talent" 
	 * 
	 * Bsp.: Talent "Steinmetz" benötig Talent "Geisteinskunde 4"; 
	 * 		SF "Defensiver Kampfstil" benötigt "GE 12" und SF "Meisterparade" 
	 * 
	 * @param link Die Voraussetzungen dieses Links werden geprüft
	 * @return true - Die Voraussetzungen von "link" sind erfüllt, ansonsten false 
	 *
	public boolean erfuelltVoraussetzung(Link link) {
		
		// TODO implement!
		
		return true;
	}*/


	
}
