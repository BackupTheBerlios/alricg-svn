/*
 * Created on 04.06.2005 / 18:48:37
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
 * Beschreib eine Variante, alle eine Rasse die eine andere Rasse modifiziert.
 * Im Normalfall werden alle angegeben Werte werden einfach zu der "original-Herkunft" 
 * hinzuaddiert. Stufen also summiert und Vor-/Nachteile, sowie SF u.ä. einfach zusätzlich
 * zu den bestehenden übernommen. Bei Talenten kommt es oft vor, das ein Talent das 
 * andere ersetzt. Dies wird einfach durch eine negative Modifikaton erreicht.
 * Es ist aber auch möglich das die Variante ohne Bezug auf das Original auskommt.
 * 
 * @author V. Strelow
 */
public class RasseVariante extends Rasse implements HerkunftVariante {
	/** Gibt die original-Rasse an.*/
	private Rasse varianteVon;
	
	/** Liste von XML-Tags die aus der original-Rasse "entfernt" (also nicht beachtet) 
	 * werden sollen, z.B. sonderfertigkeiten, vorteile.*/
	private String[] entferneXmlTag;
	
	/** Gibt an ob diese Varante mit anderen Varianten zusammen gewählt werden kann.
	 * true - Diese Variante kann zusätzlich zu anderen gewählt werden, ansonsten false */
	private boolean isMultibel;
	
	/** Bei einer AdditionsVariante (=true) werden alle Werte/ Elemente der Varinate zu der 
	 * original-Rasse hinzugefügt (mit den entfernten Elementen). 
	 * Eine NICHT AdditionsVariante (=false) ist eine eigenständige Rasse ohne Abhängigkeiten, 
	 * von der "original-Rasse". Alle nötigen angaben sind dementsprechend enthalten.
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
	public Rasse getVarianteVon() {
		return varianteVon;
	}
	/**
	 * @param varianteVon Setzt das Attribut varianteVon.
	 */
	public void setVarianteVon(Herkunft varianteVon) {
		this.varianteVon = (Rasse) varianteVon;
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
}
