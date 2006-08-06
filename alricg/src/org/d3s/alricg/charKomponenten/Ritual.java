/*
 * Created 23. Dezember 2004 / 13:17:28
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
public class Ritual extends Ritus {

    /*
     * (non-Javadoc) Methode überschrieben
     * 
     * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
     */
    public CharKomponente getCharKomponente() {
        return CharKomponente.ritual;
    }

    /**
     * Konstruktur; id beginnt mit "RIT-" für Ritual
     * 
     * @param id Systemweit eindeutige id
     */
    public Ritual(String id) {
        setId(id);
    }
}
