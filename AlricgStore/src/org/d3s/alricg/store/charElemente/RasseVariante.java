/*
 * Created on 04.06.2005 / 18:48:37
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente;

import javax.xml.bind.annotation.XmlTransient;




/**
 * <u>Beschreibung:</u><br> 
 * Beschreib eine Variante, alle eine Rasse die eine andere Rasse modifiziert.
 * Im Normalfall werden alle angegeben Werte werden einfach zu der "original-Herkunft" 
 * hinzuaddiert. Stufen also summiert und Vor-/Nachteile, sowie SF u.�. einfach zus�tzlich
 * zu den bestehenden �bernommen. Bei Talenten kommt es oft vor, das ein Talent das 
 * andere ersetzt. Dies wird einfach durch eine negative Modifikaton erreicht.
 * Es ist aber auch m�glich das die Variante ohne Bezug auf das Original auskommt.
 * 
 * @author V. Strelow
 */
public class RasseVariante extends Rasse implements HerkunftVariante<Rasse> {
	/** Gibt die original-Rasse an.*/
	private Rasse varianteVon;
	
	// Tags f�r "entferneXmlTag"
	public final static String KULTUR_UEBLICH = "Kultur �blich";
	public final static String KULTUR_MOEGLICH = "Kultur m�glich";
	public final static String[] ALLE_TAGS = new String[] {
		KULTUR_UEBLICH, KULTUR_MOEGLICH };
	
	/** Liste von XML-Tags die aus der original-Rasse "entfernt" (also nicht beachtet) 
	 * werden sollen, z.B. sonderfertigkeiten, vorteile.*/
	private String[] entferneXmlTag;
	
	/** Gibt an ob diese Varante mit anderen Varianten zusammen gew�hlt werden kann.
	 * true - Diese Variante kann zus�tzlich zu anderen gew�hlt werden, ansonsten false */
	private boolean isMultibel;
	
	/** Bei einer AdditionsVariante (=true) werden alle Werte/ Elemente der Varinate zu der 
	 * original-Rasse hinzugef�gt (mit den entfernten Elementen). 
	 * Eine NICHT AdditionsVariante (=false) ist eine eigenst�ndige Rasse ohne Abh�ngigkeiten, 
	 * von der "original-Rasse". Alle n�tigen angaben sind dementsprechend enthalten.
	 * true - Es ist eine AdditionsVariante, ansonsten false	 */
	private boolean isAdditionsVariante;
	
	/**
	 * @return Liefert das Attribut entferneXmlTag.
	 */
	public String[] getEntferneXmlTag() {
		return entferneXmlTag;
	}
	
	/**
	 * @param entferneXmlTag Setzt das Attribut entferneXmlTag.
	 */
	public void setEntferneXmlTag(String[] entferneXmlTag) {
		this.entferneXmlTag = entferneXmlTag;
	}
	
	/**
	 * @return Liefert das Attribut isMultibel.
	 */
	public boolean isMultibel() {
		return isMultibel;
	}
	/**
	 * @param isMultibel Setzt das Attribut isMultibel.
	 */
	public void setMultibel(boolean isMultibel) {
		this.isMultibel = isMultibel;
	}
	/**
	 * @return Liefert das Attribut varianteVon.
	 */
	@XmlTransient
	public Rasse getVarianteVon() {
		return varianteVon;
	}
	/**
	 * @param varianteVon Setzt das Attribut varianteVon.
	 */
	public void setVarianteVon(Rasse varianteVon) {
		this.varianteVon = varianteVon;
	}
	/**
	 * @return Liefert das Attribut isAdditionsVariante.
	 */
	public boolean isAdditionsVariante() {
		return isAdditionsVariante;
	}
	/**
	 * @param isAdditionsVariante Setzt das Attribut isAdditionsVariante.
	 */
	public void setAdditionsVariante(boolean isAdditionsVariante) {
		this.isAdditionsVariante = isAdditionsVariante;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.charElemente.Rasse#getArt()
	 */
	@Override
	public RasseArt getArt() {
		return varianteVon.getArt();
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.charElemente.Rasse#isKulturenSindNegativListe()
	 */
	@Override
	public boolean isKulturenSindNegativListe() {
		return varianteVon.isKulturenSindNegativListe();
	}
	
}
