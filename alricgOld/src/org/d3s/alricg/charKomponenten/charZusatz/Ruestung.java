/*
 * Created 20. Januar 2005 / 17:08:35
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten.charZusatz;

import org.d3s.alricg.controller.CharKomponente;

/**
 * <b>Beschreibung:</b><br> TODO Beschreibung einfügen
 * @author V.Strelow
 */
public class Ruestung extends Gegenstand {
	private int gRs = KEIN_WERT; //gesamte RS
	private int gBe = KEIN_WERT; // Gesamter BF
	
	// Zonen RS
	private int zoneKo = KEIN_WERT;
	private int zoneBr = KEIN_WERT;
	private int zoneRue = KEIN_WERT;
	private int zoneBa = KEIN_WERT;
	private int zoneLa = KEIN_WERT;
	private int zoneRa = KEIN_WERT;
	private int zoneLb = KEIN_WERT;
	private int zoneRb = KEIN_WERT;
	private int zoneGes = KEIN_WERT;
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
	 */
	public CharKomponente getCharKomponente() {
		return CharKomponente.ruestung;
	}
	
	/**
	 * Konstruktur; id beginnt mit "RUE-" für Ruestung
	 * @param id Systemweit eindeutige id
	 */
	public Ruestung(String id) {
		setId(id);
	}
	
	/**
	 * @return Liefert den gesamt Bruchfaktor.
	 */
	public int getGBe() {
		return gBe;
	}
	/**
	 * @return Liefert den gesamt Rüstungsschutz.
	 */
	public int getGRs() {
		return gRs;
	}
	
    public int getZoneBa() {
        return zoneBa;
    }
    

    public void setZoneBa(int zoneBa) {
        this.zoneBa = zoneBa;
    }
    

    public int getZoneBr() {
        return zoneBr;
    }
    

    public void setZoneBr(int zoneBr) {
        this.zoneBr = zoneBr;
    }
    

    public int getZoneGes() {
        return zoneGes;
    }
    

    public void setZoneGes(int zoneGes) {
        this.zoneGes = zoneGes;
    }
    

    public int getZoneKo() {
        return zoneKo;
    }
    

    public void setZoneKo(int zoneKo) {
        this.zoneKo = zoneKo;
    }
    

    public int getZoneLa() {
        return zoneLa;
    }
    

    public void setZoneLa(int zoneLa) {
        this.zoneLa = zoneLa;
    }
    

    public int getZoneLb() {
        return zoneLb;
    }
    

    public void setZoneLb(int zoneLb) {
        this.zoneLb = zoneLb;
    }
    

    public int getZoneRa() {
        return zoneRa;
    }
    

    public void setZoneRa(int zoneRa) {
        this.zoneRa = zoneRa;
    }
    

    public int getZoneRb() {
        return zoneRb;
    }
    

    public void setZoneRb(int zoneRb) {
        this.zoneRb = zoneRb;
    }
    

    public int getZoneRue() {
        return zoneRue;
    }
    

    public void setZoneRue(int zoneRue) {
        this.zoneRue = zoneRue;
    }
    

    public void setGBe(int be) {
        gBe = be;
    }
    

    public void setGRs(int rs) {
        gRs = rs;
    }
    
	
}
