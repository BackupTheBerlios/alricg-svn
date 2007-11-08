/*
 * Created on 13.06.2006 / 18:32:13
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.generator.prozessor;

import org.d3s.alricg.common.logic.Prozessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.held.HeldenLink;

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
public interface GeneratorProzessor<ZIEL extends CharElement, LINK extends HeldenLink> extends Prozessor<ZIEL, LINK> {


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

}
