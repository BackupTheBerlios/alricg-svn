/*
 * Created on 12.03.2005 / 12:18:22
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten;

import org.d3s.alricg.controller.CharKomponente;

/**
 * <u>Beschreibung:</u><br>
 * Repr�sentiert eine magische Repr�sentation. Dabei werden auch "unechte" Repr�sentationen mit aufgenommen, wie
 * Derwische, Tierkrieger, usw. f�r ein besseres Handling
 * 
 * @author V. Strelow
 */
public class Repraesentation extends CharElement {
    private boolean isEchteRep = true; // "Echte" Repr�sentationen sind solche, die im Liber stehen.

    private String abkuerzung;

    /*
     * (non-Javadoc) Methode �berschrieben
     * 
     * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
     */
    public CharKomponente getCharKomponente() {
        return CharKomponente.repraesentation;
    }

    /**
     * Konstruktur; id beginnt mit "REP-" f�r Repraesentation
     * 
     * @param id Systemweit eindeutige id
     */
    public Repraesentation(String id) {
        setId(id);
    }

    /**
     * @return Liefert die Abk�rzung f�r diese Repr�sention, z.B. "Mag" f�r Gildenmagier
     */
    public String getAbkuerzung() {
        return abkuerzung;
    }

    /**
     * @return Liefert das Attribut isEchteRep.
     */
    public boolean isEchteRep() {
        return isEchteRep;
    }

    /**
     * @param isEchteRep Setzt das Attribut isEchteRep.
     */
    public void setEchteRep(boolean istEchteRep) {
        this.isEchteRep = istEchteRep;
    }

    /**
     * @param abkuerzung Setzt das Attribut abkuerzung.
     */
    public void setAbkuerzung(String abkuerzung) {
        this.abkuerzung = abkuerzung;
    }

}
