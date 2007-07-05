/*
 * Created on 26.01.2005 / 16:42:51
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected an under the GPL licence.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten.charZusatz;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.controller.CharKomponente;

/**
 * <u>Beschreibung:</u><br>
 * 
 * @author V. Strelow
 */
public class SchwarzeGabe extends CharElement {
    private int kosten = KEIN_WERT;

    private int minStufe = KEIN_WERT;

    private int maxStufe = KEIN_WERT;

    /*
     * (non-Javadoc) Methode überschrieben
     * 
     * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
     */
    public CharKomponente getCharKomponente() {
        return CharKomponente.schwarzeGabe;
    }

    /**
     * Konstruktur; id beginnt mit "SGA-" für Schwarze-Gabe
     * 
     * @param id Systemweit eindeutige id
     */
    public SchwarzeGabe(String id) {
        setId(id);
    }

    /**
     * @return Liefert das Attribut kosten.
     */
    public int getKosten() {
        return kosten;
    }

    /**
     * @param kosten Setzt das Attribut kosten.
     */
    public void setKosten(int kosten) {
        this.kosten = kosten;
    }

    /**
     * @return Liefert das Attribut maxStufe.
     */
    public int getMaxStufe() {
        return maxStufe;
    }

    /**
     * @param maxStufe Setzt das Attribut maxStufe.
     */
    public void setMaxStufe(int maxStufe) {
        this.maxStufe = maxStufe;
    }

    /**
     * @return Liefert das Attribut minStufe.
     */
    public int getMinStufe() {
        return minStufe;
    }

    /**
     * @param minStufe Setzt das Attribut minStufe.
     */
    public void setMinStufe(int minStufe) {
        this.minStufe = minStufe;
    }

}
