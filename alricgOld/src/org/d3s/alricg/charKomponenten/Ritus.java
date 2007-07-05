/*
 * Created 27. Dezember 2004 / 01:48:37
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

/**
 * <b>Beschreibung:</b><br>
 * Fasst Gemeinsamkeiten von Liturgie und Ritual zusammen und bildet die Grundlage für diese.
 * 
 * @author V.Strelow
 */
public abstract class Ritus extends CharElement {
    private int grad;

    private String additionsId;

    private Gottheit[] gottheit;

    /**
     * @return Liefert das Attribut grad.
     */
    public int getGrad() {
        return grad;
    }

    /**
     * @return Liefert das Attribut herkunft.
     */
    public Gottheit[] getGottheit() {
        return gottheit;
    }

    /**
     * Id von gleichartigen Liturgien, diese ist wichtig da bei zusammengehörigen Liturgien kosten anderes berechnet
     * werden. Gleiche AdditionsIds zeigen zusammengehörigkeit an.
     * 
     * @return Liefert das Attribut additionsId.
     */
    public String getAdditionsId() {
        return additionsId;
    }

    /**
     * @param additionsId Setzt das Attribut additionsId.
     */
    public void setAdditionsId(String additionsId) {
        this.additionsId = additionsId;
    }

    /**
     * @param gottheit Setzt das Attribut gottheit.
     */
    public void setGottheit(Gottheit[] gottheit) {
        this.gottheit = gottheit;
    }

    /**
     * @param grad Setzt das Attribut grad.
     */
    public void setGrad(int grad) {
        this.grad = grad;
    }
}
