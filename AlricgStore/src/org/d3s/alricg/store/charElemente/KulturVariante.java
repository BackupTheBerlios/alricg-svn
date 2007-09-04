/*
 * Created on 02.06.2005 / 17:47:52
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente;

import org.d3s.alricg.store.charElemente.links.IdLink;


/**
 * <u>Beschreibung:</u><br> 
 * Beschreib eine Variante, alle eine Kultur die eine andere Kultur modifiziert.
 * Im Normalfall werden alle angegeben Werte werden einfach zu der "original-Herkunft" 
 * hinzuaddiert. Stufen also summiert und Vor-/Nachteile, sowie SF u.�. einfach zus�tzlich
 * zu den bestehenden �bernommen. Bei Talenten kommt es oft vor, das ein Talent das 
 * andere ersetzt. Dies wird einfach durch eine negative Modifikaton erreicht.
 * Es ist aber auch m�glich das die Variante ohne Bezug auf das Original auskommt.
 * 
 * @author V. Strelow
 */
public class KulturVariante extends Kultur implements HerkunftVariante {
	/** Gibt die original-Kultur an.*/
	private Kultur varianteVon;

	
	/** Liste von XML-Tags die aus der original-Kultur "entfernt" (also nicht beachtet) 
	 * werden sollen, z.B. sonderfertigkeiten, vorteile.*/
	private String[] entferneXmlTag;
	
	/** Gibt an ob diese Varante mit anderen Varianten zusammen gew�hlt werden kann.
	 * true - Diese Variante kann zus�tzlich zu anderen gew�hlt werden, ansonsten false */
	private boolean isMultibel;
	
	/** Bei einer AdditionsVariante (=true) werden alle Werte/ Elemente der Varinate zu der 
	 * original-Kultur hinzugef�gt (mit den entfernten Elementen). 
	 * Eine NICHT AdditionsVariante (=false) ist eine eigenst�ndige Kultur ohne Abh�ngigkeiten, 
	 * von der "original-Rasse". Alle n�tigen angaben sind dementsprechend enthalten.
	 * true - Es ist eine AdditionsVariante, ansonsten false */
	private boolean isAdditionsVariante;

	/**
	 * @return the varianteVon
	 */
	public Kultur getVarianteVon() {
		return varianteVon;
	}

	/**
	 * @param varianteVon the varianteVon to set
	 */
	public void setVarianteVon(Herkunft varianteVon) {
		this.varianteVon = (Kultur) varianteVon;
	}

	/**
	 * @return the entferneXmlTag
	 */
	public String[] getEntferneXmlTag() {
		return entferneXmlTag;
	}

	/**
	 * @param entferneXmlTag the entferneXmlTag to set
	 */
	public void setEntferneXmlTag(String[] entferneXmlTag) {
		this.entferneXmlTag = entferneXmlTag;
	}

	/**
	 * @return the isMultibel
	 */
	public boolean isMultibel() {
		return isMultibel;
	}

	/**
	 * @param isMultibel the isMultibel to set
	 */
	public void setMultibel(boolean isMultibel) {
		this.isMultibel = isMultibel;
	}

	/**
	 * @return the isAdditionsVariante
	 */
	public boolean isAdditionsVariante() {
		return isAdditionsVariante;
	}

	/**
	 * @param isAdditionsVariante the isAdditionsVariante to set
	 */
	public void setAdditionsVariante(boolean isAdditionsVariante) {
		this.isAdditionsVariante = isAdditionsVariante;
	}
	

}
