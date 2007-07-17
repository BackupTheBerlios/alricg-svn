/*
 * Created 22. Dezember 2004 / 14:23:42
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente.links;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;

import org.d3s.alricg.store.charElemente.CharElement;


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
public class Auswahl<ZIEL extends CharElement> {
	
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
	private List<Option> optionen;
	
	
	/**
	 * Das CharElement, von dem die Auswahl kommt
	 */
    private CharElement herkunft;
	
	
	/**
	 * Jede Auswahl gehört zu einem Element, von dem diese Auswahl stammt.
	 * @return Liefert das CharElement, von dem diese Auswahl stammt.
	 */
    @XmlAttribute
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
	public CharElement getHerkunft() {
		return herkunft;
	}
	
	/**
	 * @param herkunft Setzt das Attribut herkunft.
	 */
	public void setHerkunft(CharElement herkunft) {
		this.herkunft = herkunft;
	}

	/**
	 * Liste mit allen Optionen dieser Auswahl. 
	 * Jede Option gilt einzeln und muß berücksichtigt werden. 
	 * 
	 * @see org.d3s.alricg.store.charElemente.links.Option
	 * @return Liste mit Optionen
	 */
	@XmlElements( 
		{
			@XmlElement(name = "OptionAnzahl", type = OptionAnzahl.class),
			@XmlElement(name = "OptionListe", type = OptionListe.class),
			@XmlElement(name = "OptionVerteilung", type = OptionVerteilung.class)
		}
	)
	public List<Option> getOptionen() {
		return optionen;
	}

	/**
	 * Liste mit allen Optionen dieser Auswahl. 
	 * Jede Option gilt einzeln und muß berücksichtigt werden. 
	 * 
	 * @see org.d3s.alricg.store.charElemente.links.Option
	 * @return Liste mit Optionen
	 */
	public void setOptionen(List<Option> optionen) {
		this.optionen = optionen;
	}

}
