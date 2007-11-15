/*
 * Created on 19.01.2006 / 15:20:11
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.common.logic;

import java.util.List;

import org.d3s.alricg.common.charakter.ElementBox;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.links.Link;

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
public interface Prozessor<ZIEL extends CharElement, LINK extends Link> {
	
	/**
	 * Dient dem hinzufügen von neuen Elementen. Evtl. Änderungen von Wert, Text oder
	 * ZweitZiel erfolgen duch ein Update.  
	 * Hierbei erfolgt keinerlei Prüfung.
	 * 
	 * @param ziel Das eigentliche Element
	 */
	public abstract LINK addNewElement(ZIEL ziel);
	
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
	public abstract void removeElement(LINK element);
	
	/**
	 * Prüft ob ein Element grundsätzlich von der ElementListe entfernd werden kann.
	 * Dies ist nicht der Fall wenn das Element durch die Herkunft modifiziert wird oder 
	 * als notwendige Voraussetzung für andere Elemente der Liste fungiert.
	 * 
	 * @param element Das Element das geprüft wird
	 * @return true - Das Element kann entfernd werden, ansonsten false.
	 */
	public abstract boolean canRemoveElement(LINK element);
	
	/**
	 * Die Liste die hiermit zurückgeliefert wird, dient nur der
	 * Darstellung und kann nicht verändert werden, auch die 
	 * Objekte in der Liste dürfen nicht geändert werden! Bearbeiten
	 * der Objekte NUR über den Prozessor!
	 * 
	 * @return Eine Liste aller Elemente die dieser Prozessor verwaltet.
	 * 			Die Liste kann nicht verändert werden.
	 */
	public abstract List<LINK> getUnmodifiableList();
	
	/**
	 * Die ElementBox die hiermit zurückgeliefert wird, dient nur der
	 * Darstellung und kann nicht verändert werden, auch die 
	 * Objekte in der Liste dürfen nicht geändert werden! Bearbeiten
	 * der Objekte NUR über den Prozessor!
	 * 
	 * @return Die ElementBox, die alle Objekte enthält, die von diesem Prozessor
	 * 			aktuell Verwaltet werden.
	 */
	public abstract ElementBox<LINK> getElementBox();
	
	/**
	 * Wird aufgerufen um zu überprüfen ob der Wert eines Elements geändert werden darf. (Wert Ist z.B. bei "Schwerter
	 * 6" die 6). Es geht dabei NICHT um den Wert der Änderung (diese Grenzen werden mit "getMaxWert" / "getMinWert"
	 * festgelegt) sondern nur ob es Änderung grundsätzlich möglich ist!
	 * 
	 * @param link Link des Elements, dass überprüft werden soll
	 */
	public abstract boolean canUpdateWert(LINK link);

	/**
	 * Wird aufgerufen um zu überprüfen ob der Text eines Elements geändert werden darf. (text ist z.B. bei "Vorurteil
	 * gegen Orks" der String "Orks")
	 * 
	 * @param link Link des Elements, dass überprüft werden soll
	 */
	public abstract boolean canUpdateText(LINK link);

	/**
	 * Wird aufgerufen um zu überprüfen ob das ZweitZiel eines Elements geändert werden darf. (ZweitZiel ist z.B. bei
	 * "Unfähigkeit für Schwerter" das Talent "Schwerter")
	 * 
	 * @param link Link des Elements, dass geprüft werden soll
	 */
	public abstract boolean canUpdateZweitZiel(LINK link, CharElement zweitZiel);

	/**
	 * Verändert die Werte eines Elementes. Es wird keine Prüfung durchgeführt ob diese 
	 * Änderungen möglich sind!
	 * 
	 * @param link Der Link zu dem Element das geupdatet werden soll
	 * @param wert Die neue (gesamt-)Stufe oder auch "KEIN_WERT"
	 */
	public abstract void updateWert(LINK link, int wert);

	/**
	 * Verändert den Text eines Elementes. Es wird keine Prüfung durchgeführt ob diese 
	 * Änderungen möglich sind!
	 * 
	 * @param link Der Link zu dem Element das geupdatet werden soll
	 * @param text Der neue Text  (text ist z.B. bei "Vorurteil gegen Orks" der String "Orks")
	 */
	public abstract void updateText(LINK link, String text);
	
	/**
	 * Verändert das Zweitziel eines Elementes des Helden. Es wird keine Prüfung durchgeführt
	 * ob diese Änderungen möglich sind!
	 * 
	 * @param link Der Link zu dem Element das geupdatet werden soll
	 * @param zweitZiel Das neue zweitZiel (ZweitZiel ist z.B. bei "Unfähigkeit für Schwerter" das Talent "Schwerter")
	 */
	public abstract void updateZweitZiel(LINK link, CharElement zweitZiel);
	
	/**
	 * Wird vor allem vom LinkProzessorFront benötigt. Wenn ein Modi-Link hinzugefügt 
	 * wird, wird mit dieser Methode festgestellt, ob das Element schon beim Helden
	 * vorhanden ist, oder nicht.
	 *  
	 * @param link Das Element, das überprüft werden soll
	 * @return true - Ein zu dem Element passendes Element ist beim Helden vorhanden
	 */
	public abstract boolean containsLink(Link link);
	
	/**
	 * Wird immer aufgerufen, wenn von einem Element die maximale Stufe bestimmt wird.
	 * Also die höchste Stufe, die das Element annehmen kann.
	 * 
	 * @param link Der Link zu dem Element
	 * @return Die resultierende maximale Stufe oder KEIN_WERT wenn ein keine Stufe gibt
	 */
	public abstract int getMaxWert(Link link);

	/**
	 * Wird immer aufgerufen, wenn von einem Element die minimale Stufe bestimmt wird.
	 * Also die niedrigste Stufe, die das Element annehmen kann.
	 * 
	 * @param link Der Link zu dem Element
	 * @return Die resultierende minimale Stufe oder KEIN_WERT wenn ein keine Stufe gibt
	 */
	public abstract int getMinWert(Link link);
	
	/**
	 * @return Die Summe aller Kosten der Elemente des Prozessors
	 */ 
	public abstract double getGesamtKosten();
	
	/**
	 * Berechnet die Kosten die für dieses Element aufgewendet werden müssen neu.
	 * (Vor allem für Tool-Tips in Verbindung mit Notepade gedacht)
	 * @param link Der Link zu dem Element, für das die Kosten berechnet werden
	 */
	public abstract void updateKosten(LINK Link);
	
	/**
	 * Alle Kosten des Prozessors müssen neu berechnet werden, da sich die Grundlage der 
	 * Berechnung geändert haben könne (z.B. durch eine Sonderregel)
	 */
	public abstract void updateAllKosten();
	
	/**
	 * @return Die zusätzlichen, spezifischen Methoden für einen Prozessor.
	 */
	public abstract Object getExtendedInterface();
	
}
