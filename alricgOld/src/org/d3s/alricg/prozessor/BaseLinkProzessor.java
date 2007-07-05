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
	
}
