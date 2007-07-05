/*
 * Created 26. Dezember 2004 / 23:45:31
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import org.d3s.alricg.charKomponenten.links.IdLinkList;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;

/**
 * <b>Beschreibung:</b><br>
 * Repräsentiert eine Sprache.
 * 
 * @author V.Strelow
 */
public class Sprache extends SchriftSprache {
    private int komplexNichtMutterSpr;

    private KostenKlasse kostenNichtMutterSpr;

    private IdLinkList zugehoerigeSchrift;

    /*
     * (non-Javadoc) Methode überschrieben
     * 
     * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
     */
    public CharKomponente getCharKomponente() {
        return CharKomponente.sprache;
    }

    /**
     * Konstruktur; id beginnt mit "SPR-" für Sprache
     * 
     * @param id Systemweit eindeutige id
     */
    public Sprache(String id) {
        setId(id);
    }

    /**
     * @return Liefert das Attribut zugehoerigeSchrift.
     */
    public IdLinkList getZugehoerigeSchrift() {
        return zugehoerigeSchrift;
    }

    /**
     * @return Liefert das Attribut komplexNichtMutterSpr.
     */
    public int getKomplexNichtMutterSpr() {
        return komplexNichtMutterSpr;
    }

    /**
     * @return Liefert das Attribut kostenNichtMutterSpr.
     */
    public KostenKlasse getKostenNichtMutterSpr() {
        return kostenNichtMutterSpr;
    }

    /**
     * @param komplexNichtMutterSpr Setzt das Attribut komplexNichtMutterSpr.
     */
    public void setKomplexNichtMutterSpr(int komplexNichtMutterSpr) {
        this.komplexNichtMutterSpr = komplexNichtMutterSpr;
    }

    /**
     * @param kostenNichtMutterSpr Setzt das Attribut kostenNichtMutterSpr.
     */
    public void setKostenNichtMutterSpr(KostenKlasse kostenNichtMutterSpr) {
        this.kostenNichtMutterSpr = kostenNichtMutterSpr;
    }

    /**
     * @param zugehoerigeSchrift Setzt das Attribut zugehoerigeSchrift.
     */
    public void setZugehoerigeSchrift(IdLinkList zugehoerigeSchrift) {
        this.zugehoerigeSchrift = zugehoerigeSchrift;
    }
}
