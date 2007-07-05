/*
 * Created on 23.01.2005 / 19:47:25
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GPL licence.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten.charZusatz;

import org.d3s.alricg.charKomponenten.Talent;

/**
 * <u>Beschreibung:</u><br>
 * 
 * @author V. Strelow
 */
public abstract class Waffe extends Gegenstand {
    private WuerfelSammlung TP;

    private int laenge = KEIN_WERT;

    private int ini = KEIN_WERT;

    private Talent[] talent;

    /**
     * @return Liefert das Attribut ini.
     */
    public int getIni() {
        return ini;
    }

    /**
     * @return Liefert das Attribut laenge.
     */
    public int getLaenge() {
        return laenge;
    }

    /**
     * @return Liefert das Attribut talent.
     */
    public Talent[] getTalent() {
        return talent;
    }

    /**
     * @return Liefert das Attribut tP.
     */
    public WuerfelSammlung getTP() {
        return TP;
    }

    /**
     * @param ini Setzt das Attribut ini.
     */
    public void setIni(int ini) {
        this.ini = ini;
    }

    /**
     * @param laenge Setzt das Attribut laenge.
     */
    public void setLaenge(int laenge) {
        this.laenge = laenge;
    }

    /**
     * @param talent Setzt das Attribut talent.
     */
    public void setTalent(Talent[] talent) {
        this.talent = talent;
    }

    /**
     * @param tp Setzt das Attribut tP.
     */
    public void setTP(WuerfelSammlung tp) {
        TP = tp;
    }
}
