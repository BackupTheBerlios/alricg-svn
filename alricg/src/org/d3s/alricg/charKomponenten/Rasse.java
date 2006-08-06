/*
 * Created 22. Dezember 2004 / 13:07:57
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import org.d3s.alricg.charKomponenten.charZusatz.WuerfelSammlung;
import org.d3s.alricg.charKomponenten.links.IdLinkList;
import org.d3s.alricg.controller.CharKomponente;

/**
 * <b>Beschreibung:</b><br>
 * TODO Beschreibung einfügen
 * 
 * @author V.Strelow
 */
public class Rasse extends Herkunft {
    private IdLinkList kulturMoeglich;

    private IdLinkList kulturUeblich;

    private String[] haarfarbe = new String[20];

    private String[] augenfarbe = new String[20];

    private WuerfelSammlung groesseWuerfel;

    private WuerfelSammlung alterWuerfel;

    private int gewichtModi;

    private int geschwindigk = 8;
    
    private RasseVariante[] varianten;


    /*
     * (non-Javadoc) Methode überschrieben
     * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
     */
    public CharKomponente getCharKomponente() {
        return CharKomponente.rasse;
    }

    /**
     * Konstruktur; id beginnt mit "RAS-" für Rasse
     * 
     * @param id Systemweit eindeutige id
     */
    public Rasse(String id) {
        setId(id);
    }

    /**
     * @return Liefert das Attribut augenfarbe.
     */
    public String[] getAugenfarbe() {
        return augenfarbe;
    }

    /**
     * @return Liefert das Attribut gewichtModi.
     */
    public int getGewichtModi() {
        return gewichtModi;
    }

    /**
     * @return Liefert das Attribut haarfarbe.
     */
    public String[] getHaarfarbe() {
        return haarfarbe;
    }

    /**
     * @return Liefert das Attribut kulturMoeglich.
     */
    public IdLinkList getKulturMoeglich() {
        return kulturMoeglich;
    }

    /**
     * @return Liefert das Attribut kulturUeblich.
     */
    public IdLinkList getKulturUeblich() {
        return kulturUeblich;
    }

    /**
     * Berechnet einen einen korrekten Wert für die Grösse. Dies ist ein gültiger Zufalls-Wert, basierend auf den
     * angegebenen Werten.
     * 
     * @return Einen gültigen größe-Wert für diese Rasse.
     */
    public int getGroesseZufall() {
        return groesseWuerfel.getWuerfelWurf();
    }
    
    /**
     * @return Liefert das Attribut groesseWuerfel.
     */
    public WuerfelSammlung getGroesseWuerfel() {
        return groesseWuerfel;
    }

    /**
     * Berechnet einen einen korrekten Wert für das Alter. Dies ist ein gültiger Zufalls-Wert, basierend auf den
     * angegebenen Werten. ACHTUNG: Das Alter kann noch durch Vor/Nachteile ("Veteran", usw) o.ä. verändert werden! Dies
     * wird hier nicht berücksichtig und ist nur der reine Grundwert der Rasse.
     * 
     * @return Einen gültigen alters-Wert für diese Rasse.
     */
    public int getAlterZufall() {
        return alterWuerfel.getWuerfelWurf();
    }
    
    /**
     * @return Liefert das Attribut alterWuerfel.
     */
    public WuerfelSammlung getAlterWuerfel() {
        return alterWuerfel;
    }

    /**
     * @return Liefert das Attribut geschwindigk.
     */
    public int getGeschwindigk() {
        return geschwindigk;
    }

    /**
     * @param geschwindigk Setzt das Attribut geschwindigk.
     */
    public void setGeschwindigk(int geschwindigk) {
        this.geschwindigk = geschwindigk;
    }

    /**
     * @param alterWuerfel Setzt das Attribut alterWuerfel.
     */
    public void setAlterWuerfel(WuerfelSammlung alterWuerfel) {
        this.alterWuerfel = alterWuerfel;
    }

    /**
     * @param augenfarbe Setzt das Attribut augenfarbe.
     */
    public void setAugenfarbe(String[] augenfarbe) {
        this.augenfarbe = augenfarbe;
    }

    /**
     * @param gewichtModi Setzt das Attribut gewichtModi.
     */
    public void setGewichtModi(int gewichtModi) {
        this.gewichtModi = gewichtModi;
    }

    /**
     * @param groesseWuerfel Setzt das Attribut groesseWuerfel.
     */
    public void setGroesseWuerfel(WuerfelSammlung groesseWuerfel) {
        this.groesseWuerfel = groesseWuerfel;
    }

    /**
     * @param haarfarbe Setzt das Attribut haarfarbe.
     */
    public void setHaarfarbe(String[] haarfarbe) {
        this.haarfarbe = haarfarbe;
    }

    /**
     * @param kulturMoeglich Setzt das Attribut kulturMoeglich.
     */
    public void setKulturMoeglich(IdLinkList kulturMoeglich) {
        this.kulturMoeglich = kulturMoeglich;
    }

    /**
     * @param kulturUeblich Setzt das Attribut kulturUeblich.
     */
    public void setKulturUeblich(IdLinkList kulturUeblich) {
        this.kulturUeblich = kulturUeblich;
    }
    
	/**
	 * @return Die möglichen Varianten zu dieser Rasse.
	 */
	public RasseVariante[] getVarianten() {
		return varianten;
	}
	/**
	 * @param varianten Die möglichen Varianten zu dieser Rasse
	 */
	public void setVarianten(RasseVariante[] varianten) {
		this.varianten = varianten;
	}
}
