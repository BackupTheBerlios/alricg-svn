/*
 * Created 22. Dezember 2004 / 13:07:57
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.d3s.alricg.store.charElemente.charZusatz.WuerfelSammlung;
import org.d3s.alricg.store.charElemente.links.IdLink;


/**
 * <b>Beschreibung:</b><br>
 * TODO Beschreibung einfügen
 * 
 * @author V.Strelow
 */
public class Rasse extends Herkunft {
    private List<IdLink<Kultur>> kulturMoeglich;
    private List<IdLink<Kultur>> kulturUeblich;

    private FarbenAngabe[] haarfarbe;
    private FarbenAngabe[] augenfarbe;

    private WuerfelSammlung groesseWuerfel;
    private WuerfelSammlung alterWuerfel;
    private int gewichtModi;
    private int geschwindigk = 8;
    
    private RasseVariante[] varianten;

	/**
	 * @return the kulturMoeglich
	 */
	public List<IdLink<Kultur>> getKulturMoeglich() {
		return kulturMoeglich;
	}

	/**
	 * @param kulturMoeglich the kulturMoeglich to set
	 */
	public void setKulturMoeglich(List<IdLink<Kultur>> kulturMoeglich) {
		this.kulturMoeglich = kulturMoeglich;
	}

	/**
	 * @return the kulturUeblich
	 */
	public List<IdLink<Kultur>> getKulturUeblich() {
		return kulturUeblich;
	}

	/**
	 * @param kulturUeblich the kulturUeblich to set
	 */
	public void setKulturUeblich(List<IdLink<Kultur>> kulturUeblich) {
		this.kulturUeblich = kulturUeblich;
	}

	/**
	 * @return the haarfarbe
	 */
	public FarbenAngabe[] getHaarfarbe() {
		return haarfarbe;
	}

	/**
	 * @param haarfarbe the haarfarbe to set
	 */
	public void setHaarfarbe(FarbenAngabe[] haarfarbe) {
		this.haarfarbe = haarfarbe;
	}

	/**
	 * @return the augenfarbe
	 */
	public FarbenAngabe[] getAugenfarbe() {
		return augenfarbe;
	}

	/**
	 * @param augenfarbe the augenfarbe to set
	 */
	public void setAugenfarbe(FarbenAngabe[] augenfarbe) {
		this.augenfarbe = augenfarbe;
	}

	/**
	 * @return the groesseWuerfel
	 */
	public WuerfelSammlung getGroesseWuerfel() {
		return groesseWuerfel;
	}

	/**
	 * @param groesseWuerfel the groesseWuerfel to set
	 */
	public void setGroesseWuerfel(WuerfelSammlung groesseWuerfel) {
		this.groesseWuerfel = groesseWuerfel;
	}

	/**
	 * @return the alterWuerfel
	 */
	public WuerfelSammlung getAlterWuerfel() {
		return alterWuerfel;
	}

	/**
	 * @param alterWuerfel the alterWuerfel to set
	 */
	public void setAlterWuerfel(WuerfelSammlung alterWuerfel) {
		this.alterWuerfel = alterWuerfel;
	}

	/**
	 * @return the gewichtModi
	 */
	public int getGewichtModi() {
		return gewichtModi;
	}

	/**
	 * @param gewichtModi the gewichtModi to set
	 */
	public void setGewichtModi(int gewichtModi) {
		this.gewichtModi = gewichtModi;
	}

	/**
	 * @return the geschwindigk
	 */
	public int getGeschwindigk() {
		return geschwindigk;
	}

	/**
	 * @param geschwindigk the geschwindigk to set
	 */
	public void setGeschwindigk(int geschwindigk) {
		this.geschwindigk = geschwindigk;
	}

	/**
	 * @return the varianten
	 */
	public RasseVariante[] getVarianten() {
		return varianten;
	}

	/**
	 * @param varianten the varianten to set
	 */
	public void setVarianten(RasseVariante[] varianten) {
		this.varianten = varianten;
	}
    
	public static class FarbenAngabe {
		private String farbe;
		private int wahrscheinlichkeit;
		/**
		 * @return the farbe
		 */
		@XmlAttribute
		public String getFarbe() {
			return farbe;
		}
		/**
		 * @param farbe the farbe to set
		 */
		public void setFarbe(String farbe) {
			this.farbe = farbe;
		}
		/**
		 * Die wie oft auf dem W20 diese Farbe vorkommt 
		 * @return the wahrscheinlichkeit
		 */
		@XmlAttribute
		public int getWahrscheinlichkeit() {
			return wahrscheinlichkeit;
		}
		/**
		 * @param wahrscheinlichkeit the wahrscheinlichkeit to set
		 */
		public void setWahrscheinlichkeit(int wahrscheinlichkeit) {
			this.wahrscheinlichkeit = wahrscheinlichkeit;
		}
		
		
	}
    
}
