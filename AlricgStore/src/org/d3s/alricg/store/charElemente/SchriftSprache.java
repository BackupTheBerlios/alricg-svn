/*
 * Created 20. Januar 2005 / 16:48:58
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente;

import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;

/**
 * <b>Beschreibung:</b><br>
 * Faﬂt gemeinsame Eigenschaften von Schrift und Sprache zusammen.
 * 
 * @author V.Strelow
 */
abstract public class SchriftSprache extends CharElement {
    private int komplexitaet;
    private KostenKlasse kostenKlasse;

    /**
     * @return Liefert das Attribut komplexitaet.
     */
    public int getKomplexitaet() {
        return komplexitaet;
    }

    /**
     * @return Liefert das Attribut kostenKlasse.
     */
    public KostenKlasse getKostenKlasse() {
        return kostenKlasse;
    }

    /**
     * @param komplexitaet Setzt das Attribut komplexitaet.
     */
    public void setKomplexitaet(int komplexitaet) {
        this.komplexitaet = komplexitaet;
    }

    /**
     * @param kostenKlasse Setzt das Attribut kostenKlasse.
     */
    public void setKostenKlasse(KostenKlasse kostenKlasse) {
        this.kostenKlasse = kostenKlasse;
    }
}
