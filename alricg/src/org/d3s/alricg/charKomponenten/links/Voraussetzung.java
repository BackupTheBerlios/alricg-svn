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
    private IdLinkVoraussetzung[] festeVoraussetzung; // Die unveränderlichen Werte
    private IdLinkList nichtErlaubt; // Diese Elemente sind NICHT erlaubt
    private CharElement quelle; // Das CharElement, das diese Voraussetzung besitzt
    private int abWert; // Ab welchem Wert diese Voraussetzung gilt (0 = immer)
    
    // Pro Array muß mindesten eine LinkVoraussetzung erfüllt sein
    private IdLinkVoraussetzung[][] auswahlVoraussetzung;

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
    public IdLinkVoraussetzung[] getFesteVoraussetzung() {
        return festeVoraussetzung;
    }

    /**
     * @param festeVoraussetzung Setzt das Attribut festeVoraussetzung.
     */
    public void setFesteVoraussetzung(IdLinkVoraussetzung[] festeVoraussetzung) {
        this.festeVoraussetzung = festeVoraussetzung;
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
     * Liefert alle Voraussetzungen, die Alternativen besitzen
     * Pro 1 Dimension des Arrays muß mindestens eine Voraussetzung erfüllt sein.
     * @return Alle Voraussetzungen mit Alternativen.
     */
    public IdLinkVoraussetzung[][] getAuswahlVoraussetzung() {
        return auswahlVoraussetzung;
    }

    /**
     * Setzt alle Voraussetzungen, die Alternativen besitzen.
     * Pro 1 Dimension des Arrays muß mindestens eine Voraussetzung erfüllt sein.
     * @param auswahlVoraussetzung Alle Voraussetzungen mit Alternativen.
     */
    public void setAuswahlVoraussetzung(IdLinkVoraussetzung[][] auswahlVoraussetzung) {
        this.auswahlVoraussetzung = auswahlVoraussetzung;
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
    
    public void setAbWert(int wert) {
    	this.abWert = wert;
    }
    
    public int getAbWert() {
    	return abWert;
    }
    
    /**
     * <u>Beschreibung:</u><br>
     * Beschreib eine Verbindung zwischen einer Voraussetzung und den Elementen, die vorausgesetzt werden. IdLink wird
     * hierbei erweitert um ein flag, ob ein Grenzwert ein Minimum ist, oder ein Maximum.
     * 
     * @author V. Strelow
     */
    public class IdLinkVoraussetzung extends IdLink {
        private boolean isMinimum = true; // Ob der Grenzwert ein Minimum oder maximum ist

        public IdLinkVoraussetzung(CharElement quelle) {
            super(quelle, null);
        }

        /**
         * @return Liefert das Attribut isMinimum.
         */
        public boolean isMinimum() {
            return isMinimum;
        }

        /**
         * @param isMinimum Setzt das Attribut isMinimum.
         */
        public void setMinimum(boolean isMinimum) {
            this.isMinimum = isMinimum;
        }
    }

}
