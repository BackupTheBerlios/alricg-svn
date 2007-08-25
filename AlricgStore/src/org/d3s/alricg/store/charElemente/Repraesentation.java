/*
 * Created on 12.03.2005 / 12:18:22
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente;

import javax.xml.bind.annotation.XmlAttribute;


/**
 * <u>Beschreibung:</u><br>
 * Repr�sentiert eine magische Repr�sentation. Dabei werden auch "unechte" Repr�sentationen mit 
 * aufgenommen, wie Derwische, Tierkrieger, usw. f�r ein besseres Handling
 * 
 * @author V. Strelow
 */
public class Repraesentation extends CharElement {
	// Nicht schamanische Repr�sentationen sind solche, die im Liber stehen.
    private boolean isSchamanischeRep = false; 
    private String abkuerzung;

    /**
     * @return Liefert die Abk�rzung f�r diese Repr�sention, z.B. "Mag" f�r Gildenmagier
     */
	@XmlAttribute
    public String getAbkuerzung() {
        return abkuerzung;
    }

    /**
     * @return Liefert das Attribut isEchteRep.
     */
	@XmlAttribute
    public boolean isSchamanischeRep() {
        return isSchamanischeRep;
    }

    /**
     * @param isSchamanischeRep Setzt das Attribut isEchteRep.
     */
    public void setSchamanischeRep(boolean isSchamanischeRep) {
        this.isSchamanischeRep = isSchamanischeRep;
    }

    /**
     * @param abkuerzung Setzt das Attribut abkuerzung.
     */
    public void setAbkuerzung(String abkuerzung) {
        this.abkuerzung = abkuerzung;
    }

}
