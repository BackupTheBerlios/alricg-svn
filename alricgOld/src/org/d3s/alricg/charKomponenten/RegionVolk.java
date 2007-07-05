/*
 * Created 20. Januar 2005 / 15:31:48
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import org.d3s.alricg.controller.CharKomponente;

/**
 * <b>Beschreibung:</b><br>
 * TODO Beschreibung einfügen
 * 
 * @author V.Strelow
 */
public class RegionVolk extends CharElement {
    private String bindeWortMann; // Wort zwischen Vor- und Nachnamen

    private String bindeWortFrau; // Wort zwischen Vor- und Nachnamen

    private String[] vornamenMann;

    private String[] vornamenFrau;

    private String[] nachnamen;

    private String[] nachnamenEndung; // Wörter die an den Nachnamen gehangen werden

    /*
     * (non-Javadoc) Methode überschrieben
     * 
     * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
     */
    public CharKomponente getCharKomponente() {
        return CharKomponente.region;
    }

    /**
     * Konstruktur; id beginnt mit "REG-" für Region
     * 
     * @param id Systemweit eindeutige id
     */
    public RegionVolk(String id) {
        setId(id);
    }

    /**
     * @param bindeWortFrau Setzt das Attribut bindeWortFrau.
     */
    public void setBindeWortFrau(String bindeWortFrau) {
        this.bindeWortFrau = bindeWortFrau;
    }

    /**
     * @param bindeWortMann Setzt das Attribut bindeWortMann.
     */
    public void setBindeWortMann(String bindeWortMann) {
        this.bindeWortMann = bindeWortMann;
    }

    /**
     * @param nachnamen Setzt das Attribut nachnamen.
     */
    public void setNachnamen(String[] nachnamen) {
        this.nachnamen = nachnamen;
    }

    public String getBindeWortFrau() {
        return bindeWortFrau;
    }

    public String getBindeWortMann() {
        return bindeWortMann;
    }

    public String[] getNachnamen() {
        return nachnamen;
    }

    public String[] getNachnamenEndung() {
        return nachnamenEndung;
    }

    public String[] getVornamenFrau() {
        return vornamenFrau;
    }

    public String[] getVornamenMann() {
        return vornamenMann;
    }

    /**
     * @param nachnamenEndung Setzt das Attribut nachnamenEndung.
     */
    public void setNachnamenEndung(String[] nachnamenEndung) {
        this.nachnamenEndung = nachnamenEndung;
    }

    /**
     * @param vornamenFrau Setzt das Attribut vornamenFrau.
     */
    public void setVornamenFrau(String[] vornamenFrau) {
        this.vornamenFrau = vornamenFrau;
    }

    /**
     * @param vornamenMann Setzt das Attribut vornamenMann.
     */
    public void setVornamenMann(String[] vornamenMann) {
        this.vornamenMann = vornamenMann;
    }

}
