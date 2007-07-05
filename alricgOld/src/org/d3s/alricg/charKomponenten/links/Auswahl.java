/*
 * Created 22. Dezember 2004 / 14:23:42
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten.links;

import org.d3s.alricg.charKomponenten.CharElement;

/**
 * <b>Beschreibung:</b><br>
 * Oftmals werden Werte von CharElementen modifiziert, die geschieht über die
 * Auswahl. In einer Auswahl kann einerseits eine Liste von festen Modifikationen
 * stehten und außerdem kann eine Liste von variablen Modifikationen 
 * enthalten sein.
 * 
 *  Beispiel von festen Elementen einer Auswahl:
 * - "Schwerter +3, Klettern +2, Schwimmen +4". Dies ist eine feste Liste von 
 * Modis. Es ist einfach eine Auflistung der Modifikationen ohne das der
 * Benutzer etwas wählen kann. Diese Liste kann als Array mit der 
 * Methode "getFesteAuswahl()" abgerufen werden
 * 
 * 
 * Wenn es möglich ist, das der Benutzer unter mehreren Möglichtkeiten wählt, so
 * wird ebenfalls diese Klasse zur Speicherung der Elemente benutzt.
 * 
 * Beispiel von variablen Elementen einer Auswahl:
 * - "Talent Schwerter +2 oder Talent Dolche +3" (Modus ANZAHL) 
 * - "Die Werte +3, +2 beliebig auf die Talente Schwerter, Dolche, 
 * 		Klettern verteilen" (Modus LISTE)
 * - "Fünf Punkte beliebig verteilen auf die Talente Schwerter und Dolche"
 * (Modus VERTEILUNG)
 * 
 * Ein Array aller variablen Auswahlen kann über die Methode 
 * "getVarianteAuswahl()" abgerufen werden. Wie oben angegeben hat jede v. Auswahl
 * einen Modus, der angibt wie die Werte zu interpretieren sind.
 * 
 * @author V.Strelow
 */
public class Auswahl {
	
	/**
	 * Ein Array aller variablen Auswahlen bei denen der User wählen kann, 
	 * was er zu seinem Char hinzufügen möchte. Wie oben angegeben hat 
	 * jede variable Auswahl einen Modus, der angibt wie die Werte zu interpretieren 
	 * sind.
	 * 
	 * 	Beispiele:
	 * - "Talent Schwerter +2 oder Talent Dolche +3" (Modus ANZAHL) 
	 * - "Die Werte +3, +2, +1 beliebig auf die Talente Schwerter, Dolche, 
	 * 		Klettern verteilen" (Modus LISTE)
	 * - "Fünf Punkte beliebig verteilen auf die Talente Schwerter und Dolche"
	 * (Modus VERTEILUNG)
	 */
	private AbstractVariableAuswahl[] varianteAuswahl;
	
	/**
	 * Das CharElement, von dem die Auswahl kommt
	 */
    private CharElement herkunft;
    
    /**
     * Dies ist eine feste Liste von Modis. Es ist einfach eine Auflistung der
	 * Modifikationen ohne das der Benutzer etwas wählen kann.
	 * Beispiel:
	 * 	"Schwerter +3, Klettern +2, Schwimmen +4" 
     */
	protected IdLink[] festeAuswahl; // Die unveränderlichen Werte
	
	
	/**
	 * Konstruktor
	 * @param herkunft Die "quelle" dieser Auswahl
	 */
	public Auswahl(CharElement herkunft) {
		this.herkunft = herkunft;
	}
	
	/**
	 * Dies ist eine feste Liste von Modis. Es ist einfach eine Auflistung der
	 * Modifikationen ohne das der Benutzer etwas wählen kann.
	 * Beispiel:
	 * 	"Schwerter +3, Klettern +2, Schwimmen +4" 
	 *   
	 * @return Array von festen Elementen, die das auf jedenfall zu dieser
	 * Auswahl gehören (also nicht gewählt werden).
	 */
	public IdLink[] getFesteAuswahl() {
		return festeAuswahl;
	}
	
	/**
	 * @return true - Es gibt eine "feste Auswahl", d.h. es gibt eine Liste 
	 * 	von Werten bei denen der User nicht wählen kann. 
	 * 		false - Es gibt KEINE solche Liste.
	 */
	public boolean hasFesteAuswahl() {
		return (festeAuswahl != null && festeAuswahl.length != 0);
	}
	
	/**
	 * Jede Auswahl gehört zu einem Element, von dem diese Auswahl stammt.
	 * @return Liefert das CharElement, von dem diese Auswahl stammt.
	 */
	public CharElement getHerkunft() {
		return herkunft;
	}
	
	/**
	 * Ein Array aller variablen Auswahlen bei denen der User wählen kann, 
	 * was er zu seinem Char hinzufügen möchte. Wie oben angegeben hat 
	 * jede variable Auswahl einen Modus, der angibt wie die Werte zu interpretieren 
	 * sind.
	 * 
	 * 	Beispiele:
	 * - "Talent Schwerter +2 oder Talent Dolche +3" (Modus ANZAHL) 
	 * - "Die Werte +3, +2, +1 beliebig auf die Talente Schwerter, Dolche, 
	 * 		Klettern verteilen" (Modus LISTE)
	 * - "Fünf Punkte beliebig verteilen auf die Talente Schwerter und Dolche"
	 * (Modus VERTEILUNG)
	 * 
	 * @return Liefert das Attribut varianteAuswahl.
	 */
	public AbstractVariableAuswahl[] getVariableAuswahl() {
		return varianteAuswahl;
	}
	
	/**
	 * @return true - Es gibt eine "variante Auswahl", d.h. es gibt eine Liste 
	 * 	von Werten bei denen der User wählen kann welche Optionen der haben 
	 *  möchte und welche nicht. 
	 * 		false - Es gibt KEINE solche Liste.
	 */
	public boolean hasVarianteAuswahl() {
		return (varianteAuswahl != null && varianteAuswahl.length != 0);
	}
	
	/**
	 * Ein Array aller variablen Auswahlen kann. Wie oben angegeben hat 
	 * jede variable Auswahl einen Modus, der angibt wie die Werte zu interpretieren 
	 * sind.
	 * 
	 * 	Beispiele:
	 * - "Talent Schwerter +2 oder Talent Dolche +3" (Modus ANZAHL) 
	 * - "Die Werte +3, +2, +1 beliebig auf die Talente Schwerter, Dolche, 
	 * 		Klettern verteilen" (Modus LISTE)
	 * - "Fünf Punkte beliebig verteilen auf die Talente Schwerter und Dolche"
	 * (Modus VERTEILUNG)
	 * 
	 * @param varianteAuswahl Setzt das Attribut varianteAuswahl.
	 */
	public void setVariableAuswahl(AbstractVariableAuswahl[] varianteAuswahl) {
		this.varianteAuswahl = varianteAuswahl;
	}
	
	/**
	 * Dies ist eine feste Liste von Modis. Es ist einfach eine Auflistung der
	 * Modifikationen ohne das der Benutzer etwas wählen kann.
	 * Beispiel:
	 * 	"Schwerter +3, Klettern +2, Schwimmen +4"
	 *  
	 * @param festeAuswahl Setzt das Attribut festeAuswahl.
	 */
	public void setFesteAuswahl(IdLink[] festeAuswahl) {
		this.festeAuswahl = festeAuswahl;
	}
	
	/**
	 * @param herkunft Setzt das Attribut herkunft.
	 */
	public void setHerkunft(CharElement herkunft) {
		this.herkunft = herkunft;
	}
}
