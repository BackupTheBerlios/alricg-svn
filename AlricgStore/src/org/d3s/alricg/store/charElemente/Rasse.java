/*
 * Created 22. Dezember 2004 / 13:07:57
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;

import org.d3s.alricg.store.charElemente.charZusatz.WuerfelSammlung;


/**
 * <b>Beschreibung:</b><br>
 * TODO Beschreibung einfügen
 * 
 * @author V.Strelow
 */
public class Rasse extends Herkunft<RasseVariante> {
	
	@XmlEnum
	public enum RasseArt {
		menschlich("Menschlich"), 
		nichtMenschlich("Nicht Menschlich");
		
		private String name; 
		
		private RasseArt(String name) {
			this.name = name;
		}
		
		public String toString() {
			return name;
		}
	}
	
	private boolean kulturenSindNegativListe;
    private Kultur[] kulturMoeglich;
    private Kultur[] kulturUeblich;

    private FarbenAngabe[] haarfarbe;
    private FarbenAngabe[] augenfarbe;

    private WuerfelSammlung groesseWuerfel;
    private WuerfelSammlung alterWuerfel;
    private RasseArt art;
    private int gewichtModi;
    private int geschwindigk;
    
    private RasseVariante[] varianten;



	/**
	 * @return the kulturMoeglich
	 */
	@XmlList
	@XmlIDREF
    @XmlSchemaType(name = "IDREF")
	public Kultur[] getKulturMoeglich() {
		return kulturMoeglich;
	}

	/**
	 * @param kulturMoeglich the kulturMoeglich to set
	 */
	public void setKulturMoeglich(Kultur[] kulturMoeglich) {
		this.kulturMoeglich = kulturMoeglich;
		if (kulturMoeglich != null) Arrays.sort(this.kulturMoeglich);
	}
	/**
	 * @return the kulturUeblich
	 */
	@XmlList
	@XmlIDREF
    @XmlSchemaType(name = "IDREF")
	public Kultur[] getKulturUeblich() {
		return kulturUeblich;
	}
	/**
	 * @param kulturUeblich the kulturUeblich to set
	 */
	public void setKulturUeblich(Kultur[] kulturUeblich) {
		this.kulturUeblich = kulturUeblich;
		if (kulturUeblich != null) Arrays.sort(this.kulturUeblich);
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
		
		if (varianten == null) return;
		for (int i = 0; i < varianten.length;i++) {
			varianten[i].setVarianteVon(this);
		}
	}
    
	public static class FarbenAngabe {
		private String farbe;
		private int wahrscheinlichkeit;
		
		public FarbenAngabe() {
			// Empty Consructor for JaxB
		}
		
		public FarbenAngabe(String farbe, int wahrscheinlichkeit) {
			this.farbe = farbe;
			this.wahrscheinlichkeit = wahrscheinlichkeit;
		}
		
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

	/**
	 * @return the art
	 */
	@XmlAttribute
	public RasseArt getArt() {
		return art;
	}

	/**
	 * @param art the art to set
	 */
	public void setArt(RasseArt art) {
		this.art = art;
	}

	/**
	 * @return the kulturenSindNegativListe
	 */
	public boolean isKulturenSindNegativListe() {
		return kulturenSindNegativListe;
	}

	/**
	 * @param kulturenSindNegativListe the kulturenSindNegativListe to set
	 */
	public void setKulturenSindNegativListe(boolean kulturenSindNegativListe) {
		this.kulturenSindNegativListe = kulturenSindNegativListe;
	}
    
}
