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
	 * Dient dem Hinzuf�gen von Elementen durch das setzen der Herkunft o.�.
	 * Die hinzugef�gten Elemente werden als Modifikator behandelt, k�nnen von User
	 * also nicht ver�ndert werden. 
	 * Beispiel: Durch das hinzuf�gen der Profession "Streuner" wird das Talent
	 * "Dolche" mit "+3" modifiziert. Dieser Link wird durch diese Methode zum
	 * Helden hinzugef�gt.
	 * 
	 * Hierbei erfolgt keinerlei Pr�fung ob dies m�glich ist, oder nicht.
	 * 
	 * @param element Der Link der Modifikation, der hinzugef�gt wird
	 */
	public abstract HeldenLink addModi(IdLink element);
	
	/**
	 * Entfernt eine Modi von dem entsprechendem Element. Wird z.B. eingesetzt, wenn eine
	 * Herkunft und die damit zusammenh�ngenden Modifikatoren entfernt wird.
	 * Beispiel: Durch das entfernen der Profession "Streuner" wird das Talent
	 * "Dolche" nicht mehr l�nger mit "+3" modifiziert. Dieser Link wird durch 
	 * diese Methode vom Helden entfernd.
	 * 
	 * Hierbei erfolgt keinerlei Pr�fung ob dies m�glich ist, oder nicht.
	 * 
	 * @param element Der Link der Modifikation, der entfernt wird
	 */
	public abstract void removeModi(LINK heldLink, IdLink element);
	
    /**
     * Pr�ft ob ein Element grunds�tzlich zur ElementListe hinzugef�gt werden kann. 
     * Es werden Dinge �berpr�ft wie:
     * Voraussetzungen erf�llt, Vereinbar mit anderen Elementen. 
     * 
     * Es werden KEINE Kosten �berpr�ft oder Dinge wie Stufe und zus�tzliche Angaben
     * 
     * @param elem Das CharElement das �berpr�ft wird
     */
	public abstract boolean canAddElement(Link link);
	
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
	 * @return Die Summe aller Kosten der Elemente des Prozessors
	 */ 
	public abstract int getGesamtKosten();
	
	/**
	 * Berechnet die Kosten die f�r dieses Element aufgewendet werden m�ssen neu.
	 * (Vor allem f�r Tool-Tips in Verbindung mit Notepade gedacht)
	 * @param link Der Link zu dem Element, f�r das die Kosten berechnet werden
	 */
	public abstract void updateKosten(LINK Link);

}
