/*
 * Created 26. Dezember 2004 / 23:10:42
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten.links;

import org.d3s.alricg.charKomponenten.CharElement;

/**
 * <b>Beschreibung:</b><br>
 * Beschreibt Bedingungen die erfüllt sein müssen, damit ein Element, das diese 
 * Voraussetzug besitzt, zum Helden hinzugefügt werden kann. Voraussetzungen können
 * von vielen CharElementen enthalten sein.
 * 
 * @author V.Strelow
 */
public class Voraussetzung {
	private IdLinkList nichtErlaubt; // Diese Elemente sind NICHT erlaubt
	private VoraussetzungsAlternative[] voraussetzungsAltervativen; // Die Voraussetzungen Werte
	private CharElement quelle; // Das CharElement, das diese Voraussetzung besitzt
	
	// Pro "VoraussetzungsAlternative" muß mindesten eine auswahlVoraussetzung erfüllt sein
	public class VoraussetzungsAlternative {
		private IdLinkList[] voraussetzungsAlternativen;
		private int abWert; // Ab welchem Wert diese Voraussetzung gilt (0 = immer)

		/**
		 * @return the abWert
		 */
		public int getAbWert() {
			return abWert;
		}

		/**
		 * @param abWert the abWert to set
		 */
		public void setAbWert(int abWert) {
			this.abWert = abWert;
		}

		/**
		 * @return the auswahlVoraussetzung
		 */
		public IdLinkList[] getVoraussetzungsAlternativen() {
			return voraussetzungsAlternativen;
		}

		/**
		 * @param auswahlVoraussetzung the auswahlVoraussetzung to set
		 */
		public void setVoraussetzungsAlternativen(IdLinkList[] alternativen) {
			this.voraussetzungsAlternativen = alternativen;
		}
		
		/**
		 * @return Liefert das Objekt Voraussetzung, welches diese VoraussetzungsAlternative "besitzt"
		 */
		public Voraussetzung getParentVoraussetzung() {
			return Voraussetzung.this;
		}
	}
	
	
    /**
     * Konstruktor
     * 
     * @param quelle Das CharElement, bei dem diese Voraussetzung erfüllt sein muß
     */
    public Voraussetzung(CharElement quelle) {
        this.quelle = quelle;
    }

    /**
     * @return Liefert das Attribut festeVoraussetzung.
     */
    public VoraussetzungsAlternative[] getVoraussetzungsAltervativen() {
        return voraussetzungsAltervativen;
    }

    /**
     * @param festeVoraussetzung Setzt das Attribut festeVoraussetzung.
     */
    public void setVoraussetzungsAltervativen(VoraussetzungsAlternative[] voraussetzungen) {
        this.voraussetzungsAltervativen = voraussetzungen;
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

    /**
     * Liefert die CharElemente, die NICHT erlaubt sind, also der Held nicht 
     * haben / wählen darf.
     * @return Eine IdLinkList mit allen Elementen die nicht erlaubt sind
     */
    public IdLinkList getNichtErlaubt() {
        return nichtErlaubt;
    }

    /**
     * Setzt die CharElemente, die NICHT erlaubt sind, also der Held nicht 
     * haben / wählen darf.
     * @param nichtErlaubt Eine IdLinkList mit allen Elementen die nicht erlaubt sind
     */
    public void setNichtErlaubt(IdLinkList nichtErlaubt) {
        this.nichtErlaubt = nichtErlaubt;
    }

}
