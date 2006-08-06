/*
 * Created on 26.01.2005 / 00:44:45
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten.charZusatz;

import org.d3s.alricg.controller.CharKomponente;

/**
 * <u>Beschreibung:</u><br>
 * 
 * @author V. Strelow
 */
public class Schild extends Gegenstand {
    private String typ;

    private int bf = KEIN_WERT; // Bruchfaktor

    private int ini = KEIN_WERT; // Bruchfaktor

    private int wmAT = KEIN_WERT; // Waffenmodifikator / AT

    private int wmPA = KEIN_WERT; // Waffenmodifikator / PA

    /*
     * (non-Javadoc) Methode überschrieben
     * 
     * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
     */
    public CharKomponente getCharKomponente() {
        return CharKomponente.schild;
    }

    /**
     * Konstruktur; id beginnt mit "SLD-" für Schild
     * 
     * @param id Systemweit eindeutige id
     */
    public Schild(String id) {
        setId(id);
    }

    /**
     * @return Liefert den Bruchfaktor.
     */
    public int getBf() {
        return bf;
    }

    /**
     * @return Liefert den initiative Modi.
     */
    public int getIni() {
        return ini;
    }

    /**
     * @return Liefert den Waffenmodifikator / AT.
     */
    public int getWmAT() {
        return wmAT;
    }

    /**
     * @return Liefert den Waffenmodifikator / PA.
     */
    public int getWmPA() {
        return wmPA;
    }

    /**
     * @return Liefert das Attribut typ.
     */
    public String getTyp() {
        return typ;
    }

    /**
     * @param typ Setzt das Attribut typ.
     */
    public void setTyp(String typ) {
        this.typ = typ;
    }

    /**
     * @param bf Setzt das Attribut bf.
     */
    public void setBf(int bf) {
        this.bf = bf;
    }

    /**
     * @param ini Setzt das Attribut ini.
     */
    public void setIni(int ini) {
        this.ini = ini;
    }

    /**
     * @param wmAT Setzt das Attribut wmAT.
     */
    public void setWmAT(int wmAT) {
        this.wmAT = wmAT;
    }

    /**
     * @param wmPA Setzt das Attribut wmPA.
     */
    public void setWmPA(int wmPA) {
        this.wmPA = wmPA;
    }
}
