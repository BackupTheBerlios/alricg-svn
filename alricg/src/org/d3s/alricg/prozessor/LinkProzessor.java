/*
 * Created on 13.06.2006 / 18:32:13
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.prozessor.common.HeldenLink;

/**
 * <u>Beschreibung:</u><br> 
 * Dieses Interface beschreibt die Funktionen die ein Prozesser zur verarbeitung von 
 * Links implementieren sollte.
 * 
 * ZIEL - Das CharElement, welches als Link-Ziel verwaltete wird.
 * LINK - Die Art der Links, welche verwaltet wird.
 * 
 * @author V. Strelow
 */
public interface LinkProzessor<ZIEL extends CharElement, LINK extends HeldenLink> extends Prozessor<ZIEL, LINK> {


	/**
	 * Dient dem Hinzufügen von Elementen durch das setzen der Herkunft o.ä.
	 * Die hinzugefügten Elemente werden als Modifikator behandelt, können von User
	 * also nicht verändert werden. 
	 * Beispiel: Durch das hinzufügen der Profession "Streuner" wird das Talent
	 * "Dolche" mit "+3" modifiziert. Dieser Link wird durch diese Methode zum
	 * Helden hinzugefügt.
	 * 
	 * Hierbei erfolgt keinerlei Prüfung ob dies möglich ist, oder nicht.
	 * 
	 * @param element Der Link der Modifikation, der hinzugefügt wird
	 */
	public abstract HeldenLink addModi(IdLink element);
	
	/**
	 * Entfernt eine Modi von dem entsprechendem Element. Wird z.B. eingesetzt, wenn eine
	 * Herkunft und die damit zusammenhängenden Modifikatoren entfernt wird.
	 * Beispiel: Durch das entfernen der Profession "Streuner" wird das Talent
	 * "Dolche" nicht mehr länger mit "+3" modifiziert. Dieser Link wird durch 
	 * diese Methode vom Helden entfernd.
	 * 
	 * Hierbei erfolgt keinerlei Prüfung ob dies möglich ist, oder nicht.
	 * 
	 * @param element Der Link der Modifikation, der entfernt wird
	 */
	public abstract void removeModi(LINK heldLink, IdLink element);
	
    /**
     * Prüft ob ein Element grundsätzlich zur ElementListe hinzugefügt werden kann. 
     * Es werden Dinge überprüft wie:
     * Voraussetzungen erfüllt, Vereinbar mit anderen Elementen. 
     * 
     * Es werden KEINE Kosten überprüft oder Dinge wie Stufe und zusätzliche Angaben
     * 
     * @param elem Das CharElement das überprüft wird
     */
	public abstract boolean canAddElement(Link link);
	
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
	 * @return Die Summe aller Kosten der Elemente des Prozessors
	 */ 
	public abstract int getGesamtKosten();
	
	/**
	 * Berechnet die Kosten die für dieses Element aufgewendet werden müssen neu.
	 * (Vor allem für Tool-Tips in Verbindung mit Notepade gedacht)
	 * @param link Der Link zu dem Element, für das die Kosten berechnet werden
	 */
	public abstract void updateKosten(LINK Link);

}
