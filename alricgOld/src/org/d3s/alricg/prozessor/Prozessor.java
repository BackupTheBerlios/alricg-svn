/*
 * Created on 19.01.2006 / 15:20:11
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor;

import java.util.List;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.prozessor.elementBox.ElementBox;

/**
 * <u>Beschreibung:</u><br> 
 * Ein Prozesser verwaltet eine Liste von Elementen und stellt Operationen 
 * zur Bearbeitung dieser Liste zu Verfügung. Der Prozessor dient dabei 
 * als Schnittstelle zwischen der GUI und der Programmlogik.  
 * 
 * Dieses Interface stellen den gemeinsamen Grundstock von Operationen aller Prozessoren da, 
 * je nach Art der Elemente gibt es verschiedene Spezialisierungen dieses Interfaces,
 * welche weitere Methoden anbieten. 
 * 
 * @author V. Strelow
 */
public interface Prozessor<ZIEL extends CharElement, ELEM> {
	
	/**
	 * Dient dem hinzufügen von neuen Elementen. Evtl. Änderungen von Wert, Text oder
	 * ZweitZiel erfolgen duch ein Update.  
	 * Hierbei erfolgt keinerlei Prüfung.
	 * 
	 * @param ziel Das eigentliche Element
	 */
	public abstract ELEM addNewElement(ZIEL ziel);
	
    /**
     * Prüft ob ein Element grundsätzlich zur ElementListe hinzugefügt werden kann. 
     * Es werden Dinge überprüft wie:
     * Voraussetzungen erfüllt, Vereinbar mit anderen Elementen. 
     * 
     * Es werden KEINE Kosten überprüft oder Dinge wie Stufe und zusätzliche Angaben
     * 
     * @param elem Das CharElement das überprüft wird
     */
	public abstract boolean canAddElement(ZIEL ziel);
	
	/**
	 * Entfernd die vom User hinzugefügte Komponente eines Elements. Gibt es keine
	 * Modifiaktionen wird das Element vollständig entfernd, gibt es Modifikationen
	 * bleibt das Element weiterhin bestehen.
	 * 
	 * @param element Der Link des CharElements welches entfernt werden soll
	 */
	public abstract void removeElement(ELEM element);
	
	/**
	 * Prüft ob ein Element grundsätzlich von der ElementListe entfernd werden kann.
	 * Dies ist nicht der Fall wenn das Element durch die Herkunft modifiziert wird oder 
	 * als notwendige Voraussetzung für andere Elemente der Liste fungiert.
	 * 
	 * @param element Das Element das geprüft wird
	 * @return true - Das Element kann entfernd werden, ansonsten false.
	 */
	public abstract boolean canRemoveElement(ELEM element);
	
	/**
	 * Die Liste die hiermit zurückgeliefert wird, dient nur der
	 * Darstellung und kann nicht verändert werden, auch die 
	 * Objekte in der Liste dürfen nicht geändert werden! Bearbeiten
	 * der Objekte NUR über den Prozessor!
	 * 
	 * @return Eine Liste aller Elemente die dieser Prozessor verwaltet.
	 * 			Die Liste kann nicht verändert werden.
	 */
	public abstract List<ELEM> getUnmodifiableList();
	
	/**
	 * Die ElementBox die hiermit zurückgeliefert wird, dient nur der
	 * Darstellung und kann nicht verändert werden, auch die 
	 * Objekte in der Liste dürfen nicht geändert werden! Bearbeiten
	 * der Objekte NUR über den Prozessor!
	 * 
	 * @return Die ElementBox, die alle Objekte enthält, die von diesem Prozessor
	 * 			aktuell Verwaltet werden.
	 */
	public abstract ElementBox<ELEM> getElementBox();
	
}
