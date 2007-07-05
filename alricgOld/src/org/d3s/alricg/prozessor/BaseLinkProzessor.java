/**
 * 
 */
package org.d3s.alricg.prozessor;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.links.Link;

/**
 * 
 * @author Vincent
 */
public interface BaseLinkProzessor<ZIEL extends CharElement, LINK extends Link> extends Prozessor<ZIEL, LINK> {

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
	
}
