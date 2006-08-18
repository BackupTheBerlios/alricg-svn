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
public interface LinkProzessor<ZIEL extends CharElement, LINK extends HeldenLink> extends BaseLinkProzessor<ZIEL, LINK> {


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
	public abstract int getGesamtKosten();
	
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

}
