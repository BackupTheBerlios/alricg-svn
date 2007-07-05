/*
 * Created 20. Januar 2005 / 16:04:34
 * This file is part of the project ALRICG. The file is copyright
 * protected an under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import org.d3s.alricg.charKomponenten.links.IdLinkList;
import org.d3s.alricg.controller.CharKomponente;

/**
 * <b>Beschreibung:</b><br>
 * Diese Klasse repräsentiert "späte Professionen" (z.B. Kor-Geweihter) und "zusatz-Professionen" (wie der Elfische
 * Wanderer)
 * 
 * @author V.Strelow
 */
public class ZusatzProfession extends Profession {
    private IdLinkList professionMoeglich; // Ist dies leer, so sind alle möglich

    private IdLinkList professionUeblich;

    private int apKosten; // GP Kosten durch Profession

    private boolean zusatzProf; // ansonsten späteProfession, "spaeteProfession"

    // und "zusatzProfession" schließen sich aus!

    /*
     * (non-Javadoc) Methode überschrieben
     * 
     * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
     */
    public CharKomponente getCharKomponente() {
        return CharKomponente.zusatzProfession;
    }

    /**
     * Konstruktur; id beginnt mit "ZPR-" für ZusatzProfession
     * 
     * @param id Systemweit eindeutige id
     */
    public ZusatzProfession(String id) {
        super(id);
    }

    /**
     * @return Liefert das Attribut apKosten.
     */
    public int getApKosten() {
        return apKosten;
    }

    /**
     * @return Liefert das Attribut professionMoeglich.
     */
    public IdLinkList getProfessionMoeglich() {
        return professionMoeglich;
    }

    /**
     * @return Liefert das Attribut professionUeblich.
     */
    public IdLinkList getProfessionUeblich() {
        return professionUeblich;
    }

    /**
     * @return Liefert das Attribut zusatzProf.
     */
    public boolean isZusatzProf() {
        return zusatzProf;
    }

    /**
     * @param apKosten Setzt das Attribut apKosten.
     */
    public void setApKosten(int apKosten) {
        this.apKosten = apKosten;
    }

    /**
     * @param professionMoeglich Setzt das Attribut professionMoeglich.
     */
    public void setProfessionMoeglich(IdLinkList professionMoeglich) {
        this.professionMoeglich = professionMoeglich;
    }

    /**
     * @param professionUeblich Setzt das Attribut professionUeblich.
     */
    public void setProfessionUeblich(IdLinkList professionUeblich) {
        this.professionUeblich = professionUeblich;
    }

    /**
     * @param zusatzProf Setzt das Attribut zusatzProf.
     */
    public void setZusatzProf(boolean zusatzProf) {
        this.zusatzProf = zusatzProf;
    }
}
