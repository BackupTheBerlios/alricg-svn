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
 * zur Bearbeitung dieser Liste zu Verf�gung. Der Prozessor dient dabei 
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
	 * Dient dem hinzuf�gen von neuen Elementen. Evtl. �nderungen von Wert, Text oder
	 * ZweitZiel erfolgen duch ein Update.  
	 * Hierbei erfolgt keinerlei Pr�fung.
	 * 
	 * @param ziel Das eigentliche Element
	 */
	public abstract ELEM addNewElement(ZIEL ziel);
	
    /**
     * Pr�ft ob ein Element grunds�tzlich zur ElementListe hinzugef�gt werden kann. 
     * Es werden Dinge �berpr�ft wie:
     * Voraussetzungen erf�llt, Vereinbar mit anderen Elementen. 
     * 
     * Es werden KEINE Kosten �berpr�ft oder Dinge wie Stufe und zus�tzliche Angaben
     * 
     * @param elem Das CharElement das �berpr�ft wird
     */
	public abstract boolean canAddElement(ZIEL ziel);
	
	/**
	 * Entfernd die vom User hinzugef�gte Komponente eines Elements. Gibt es keine
	 * Modifiaktionen wird das Element vollst�ndig entfernd, gibt es Modifikationen
	 * bleibt das Element weiterhin bestehen.
	 * 
	 * @param element Der Link des CharElements welches entfernt werden soll
	 */
	public abstract void removeElement(ELEM element);
	
	/**
	 * Pr�ft ob ein Element grunds�tzlich von der ElementListe entfernd werden kann.
	 * Dies ist nicht der Fall wenn das Element durch die Herkunft modifiziert wird oder 
	 * als notwendige Voraussetzung f�r andere Elemente der Liste fungiert.
	 * 
	 * @param element Das Element das gepr�ft wird
	 * @return true - Das Element kann entfernd werden, ansonsten false.
	 */
	public abstract boolean canRemoveElement(ELEM element);
	
	/**
	 * Die Liste die hiermit zur�ckgeliefert wird, dient nur der
	 * Darstellung und kann nicht ver�ndert werden, auch die 
	 * Objekte in der Liste d�rfen nicht ge�ndert werden! Bearbeiten
	 * der Objekte NUR �ber den Prozessor!
	 * 
	 * @return Eine Liste aller Elemente die dieser Prozessor verwaltet.
	 * 			Die Liste kann nicht ver�ndert werden.
	 */
	public abstract List<ELEM> getUnmodifiableList();
	
	/**
	 * Die ElementBox die hiermit zur�ckgeliefert wird, dient nur der
	 * Darstellung und kann nicht ver�ndert werden, auch die 
	 * Objekte in der Liste d�rfen nicht ge�ndert werden! Bearbeiten
	 * der Objekte NUR �ber den Prozessor!
	 * 
	 * @return Die ElementBox, die alle Objekte enth�lt, die von diesem Prozessor
	 * 			aktuell Verwaltet werden.
	 */
	public abstract ElementBox<ELEM> getElementBox();
	
}
