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
 * zur Bearbeitung dieser Liste zu Verf�gung. Der Prozessor dient dabei 
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
	 * Dient dem hinzuf�gen von neuen Elementen. Evtl. �nderungen von Wert, Text oder
	 * ZweitZiel erfolgen duch ein Update.  
	 * Hierbei erfolgt keinerlei Pr�fung.
	 * 
	 * @param ziel Das eigentliche Element
	 */
	public abstract LINK addNewElement(ZIEL ziel);
	
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
	public abstract void removeElement(LINK element);
	
	/**
	 * Pr�ft ob ein Element grunds�tzlich von der ElementListe entfernd werden kann.
	 * Dies ist nicht der Fall wenn das Element durch die Herkunft modifiziert wird oder 
	 * als notwendige Voraussetzung f�r andere Elemente der Liste fungiert.
	 * 
	 * @param element Das Element das gepr�ft wird
	 * @return true - Das Element kann entfernd werden, ansonsten false.
	 */
	public abstract boolean canRemoveElement(LINK element);
	
	/**
	 * Die Liste die hiermit zur�ckgeliefert wird, dient nur der
	 * Darstellung und kann nicht ver�ndert werden, auch die 
	 * Objekte in der Liste d�rfen nicht ge�ndert werden! Bearbeiten
	 * der Objekte NUR �ber den Prozessor!
	 * 
	 * @return Eine Liste aller Elemente die dieser Prozessor verwaltet.
	 * 			Die Liste kann nicht ver�ndert werden.
	 */
	public abstract List<LINK> getUnmodifiableList();
	
	/**
	 * Die ElementBox die hiermit zur�ckgeliefert wird, dient nur der
	 * Darstellung und kann nicht ver�ndert werden, auch die 
	 * Objekte in der Liste d�rfen nicht ge�ndert werden! Bearbeiten
	 * der Objekte NUR �ber den Prozessor!
	 * 
	 * @return Die ElementBox, die alle Objekte enth�lt, die von diesem Prozessor
	 * 			aktuell Verwaltet werden.
	 */
	public abstract ElementBox<LINK> getElementBox();
	
	/**
	 * Wird aufgerufen um zu �berpr�fen ob der Wert eines Elements ge�ndert werden darf. (Wert Ist z.B. bei "Schwerter
	 * 6" die 6). Es geht dabei NICHT um den Wert der �nderung (diese Grenzen werden mit "getMaxWert" / "getMinWert"
	 * festgelegt) sondern nur ob es �nderung grunds�tzlich m�glich ist!
	 * 
	 * @param link Link des Elements, dass �berpr�ft werden soll
	 */
	public abstract boolean canUpdateWert(LINK link);

	/**
	 * Wird aufgerufen um zu �berpr�fen ob der Text eines Elements ge�ndert werden darf. (text ist z.B. bei "Vorurteil
	 * gegen Orks" der String "Orks")
	 * 
	 * @param link Link des Elements, dass �berpr�ft werden soll
	 */
	public abstract boolean canUpdateText(LINK link);

	/**
	 * Wird aufgerufen um zu �berpr�fen ob das ZweitZiel eines Elements ge�ndert werden darf. (ZweitZiel ist z.B. bei
	 * "Unf�higkeit f�r Schwerter" das Talent "Schwerter")
	 * 
	 * @param link Link des Elements, dass gepr�ft werden soll
	 */
	public abstract boolean canUpdateZweitZiel(LINK link, CharElement zweitZiel);

	/**
	 * Ver�ndert die Werte eines Elementes. Es wird keine Pr�fung durchgef�hrt ob diese 
	 * �nderungen m�glich sind!
	 * 
	 * @param link Der Link zu dem Element das geupdatet werden soll
	 * @param wert Die neue (gesamt-)Stufe oder auch "KEIN_WERT"
	 */
	public abstract void updateWert(LINK link, int wert);

	/**
	 * Ver�ndert den Text eines Elementes. Es wird keine Pr�fung durchgef�hrt ob diese 
	 * �nderungen m�glich sind!
	 * 
	 * @param link Der Link zu dem Element das geupdatet werden soll
	 * @param text Der neue Text  (text ist z.B. bei "Vorurteil gegen Orks" der String "Orks")
	 */
	public abstract void updateText(LINK link, String text);
	
	/**
	 * Ver�ndert das Zweitziel eines Elementes des Helden. Es wird keine Pr�fung durchgef�hrt
	 * ob diese �nderungen m�glich sind!
	 * 
	 * @param link Der Link zu dem Element das geupdatet werden soll
	 * @param zweitZiel Das neue zweitZiel (ZweitZiel ist z.B. bei "Unf�higkeit f�r Schwerter" das Talent "Schwerter")
	 */
	public abstract void updateZweitZiel(LINK link, CharElement zweitZiel);
	
	/**
	 * Wird vor allem vom LinkProzessorFront ben�tigt. Wenn ein Modi-Link hinzugef�gt 
	 * wird, wird mit dieser Methode festgestellt, ob das Element schon beim Helden
	 * vorhanden ist, oder nicht.
	 *  
	 * @param link Das Element, das �berpr�ft werden soll
	 * @return true - Ein zu dem Element passendes Element ist beim Helden vorhanden
	 */
	public abstract boolean containsLink(Link link);
	
	/**
	 * Wird immer aufgerufen, wenn von einem Element die maximale Stufe bestimmt wird.
	 * Also die h�chste Stufe, die das Element annehmen kann.
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
	 * Berechnet die Kosten die f�r dieses Element aufgewendet werden m�ssen neu.
	 * (Vor allem f�r Tool-Tips in Verbindung mit Notepade gedacht)
	 * @param link Der Link zu dem Element, f�r das die Kosten berechnet werden
	 */
	public abstract void updateKosten(LINK Link);
	
	/**
	 * Alle Kosten des Prozessors m�ssen neu berechnet werden, da sich die Grundlage der 
	 * Berechnung ge�ndert haben k�nne (z.B. durch eine Sonderregel)
	 */
	public abstract void updateAllKosten();
	
	/**
	 * @return Die zus�tzlichen, spezifischen Methoden f�r einen Prozessor.
	 */
	public abstract Object getExtendedInterface();
	
}
