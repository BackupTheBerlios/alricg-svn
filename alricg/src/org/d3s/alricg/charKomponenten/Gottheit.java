/*
 * Created on 12.03.2005 / 12:23:19
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
 * Jede Liturgie und jedes Ritual ist einer Gottheit zugeordent. Auch geweihte Sonderfertigkeiten sind mit einer
 * Gottheit verbunden.
 * 
 * @author V. Strelow
 */
public class Gottheit extends CharElement {

    public enum KenntnisArt {
        liturgie("liturgie"), ritual("ritual");
        private String value; // IDdes Elements

        private KenntnisArt(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum GottheitArt {
        nichtAlveranisch("nichtAlveranisch"), zwoelfGoettlich("zwoelfGoettlich"), animistisch("animistisch");
        private String value; // ID des Elements

        private GottheitArt(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private KenntnisArt kenntnisArt;

    private GottheitArt gottheitArt;

    /*
     * (non-Javadoc) Methode überschrieben
     * 
     * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
     */
    public CharKomponente getCharKomponente() {
        return CharKomponente.gottheit;
    }

    /**
     * Konstruktur; id beginnt mit "GOT-" für Gottheit
     * 
     * @param id Systemweit eindeutige id
     */
    public Gottheit(String id) {
        setId(id);
    }

    /**
     * @return Liefert das Attribut gottheitArt.
     */
    public GottheitArt getGottheitArt() {
        return gottheitArt;
    }

    /**
     * @param gottheitArt Setzt das Attribut gottheitArt.
     */
    public void setGottheitArt(GottheitArt gottheitArt) {
        this.gottheitArt = gottheitArt;
    }

    /**
     * @return Liefert das Attribut kenntnisArt.
     */
    public KenntnisArt getKenntnisArt() {
        return kenntnisArt;
    }

    /**
     * @param kenntnisArt Setzt das Attribut kenntnisArt.
     */
    public void setKenntnisArt(KenntnisArt kenntnisArt) {
        this.kenntnisArt = kenntnisArt;
    }

}
