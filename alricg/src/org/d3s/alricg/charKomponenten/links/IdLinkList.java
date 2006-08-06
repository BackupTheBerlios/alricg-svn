/*
 * Created on 25.02.2005 / 19:13:45
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten.links;

import org.d3s.alricg.charKomponenten.CharElement;

/**
 * <u>Beschreibung:</u><br> 
 * Beschreibt eine Liste von mehreren IdLinks. Im Gegensatz zu der Auswahl sind hier alle
 * IDs "gleichberechtigt".
 * @author V. Strelow
 */
public class IdLinkList {
	IdLink[] links;
	CharElement quelle;
	
	/**
	 * Konstruktor.
	 * @param quelle Das Element, welches die IdLinkListe "besitzt"
	 */
	public IdLinkList(CharElement quelle) {
		this.quelle = quelle;
	}
	
	/**
	 * @return Liefert das Attribut links.
	 */
	public IdLink[] getLinks() {
		return links;
	}
	
	/**
	 * @param links Setzt das Attribut links.
	 */
	public void setLinks(IdLink[] links) {
		this.links = links;
	}
	
	/**
	 * @return Liefert das Attribut quelle.
	 */
	public CharElement getQuelle() {
		return quelle;
	}
	
	/**
	 * @param quelle Setzt das Attribut quelle.
	 */
	public void setQuelle(CharElement quelle) {
		this.quelle = quelle;
	}

}
