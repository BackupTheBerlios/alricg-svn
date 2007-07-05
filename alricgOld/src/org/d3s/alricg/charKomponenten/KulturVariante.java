/*
 * Created on 02.06.2005 / 17:47:52
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten;

import org.d3s.alricg.charKomponenten.links.IdLinkList;
import org.d3s.alricg.controller.CharKomponente;

/**
 * <u>Beschreibung:</u><br> 
 * Beschreib eine Variante, alle eine Kultur die eine andere Kultur modifiziert.
 * Im Normalfall werden alle angegeben Werte werden einfach zu der "original-Herkunft" 
 * hinzuaddiert. Stufen also summiert und Vor-/Nachteile, sowie SF u.ä. einfach zusätzlich
 * zu den bestehenden übernommen. Bei Talenten kommt es oft vor, das ein Talent das 
 * andere ersetzt. Dies wird einfach durch eine negative Modifikaton erreicht.
 * Es ist aber auch möglich das die Variante ohne Bezug auf das Original auskommt.
 * 
 * @author V. Strelow
 */
public class KulturVariante extends Kultur implements HerkunftVariante {
	/** Gibt die original-Kultur an.*/
	private Kultur varianteVon;
	
	/** Liste von Elementen die aus der original-Kultur "entfernt" (also nicht beachtet) 
	 * werden sollen, z.B. SF, Vorteile, Nachteile. 
	 * KEINE Elemente die in einer Auswahl stehen! */
	private IdLinkList entferneElement;
	
	/** Liste von XML-Tags die aus der original-Kultur "entfernt" (also nicht beachtet) 
	 * werden sollen, z.B. sonderfertigkeiten, vorteile.*/
	private String[] entferneXmlTag;
	
	/** Gibt an ob diese Varante mit anderen Varianten zusammen gewählt werden kann.
	 * true - Diese Variante kann zusätzlich zu anderen gewählt werden, ansonsten false */
	private boolean isMultibel;
	
	/** Bei einer AdditionsVariante (=true) werden alle Werte/ Elemente der Varinate zu der 
	 * original-Kultur hinzugefügt (mit den entfernten Elementen). 
	 * Eine NICHT AdditionsVariante (=false) ist eine eigenständige Kultur ohne Abhängigkeiten, 
	 * von der "original-Rasse". Alle nötigen angaben sind dementsprechend enthalten.
	 * true - Es ist eine AdditionsVariante, ansonsten false */
	private boolean isAdditionsVariante;
	

	/**
	 * Konstruktur; id beginnt mit "KUV-" für KulturVariante
     * @param id Systemweit eindeutige id */
	public KulturVariante(String id) {
		super(id);
	}
	
    /*
     * (non-Javadoc) Methode überschrieben
     * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
     */
    public CharKomponente getCharKomponente() {
        return CharKomponente.kulturVariante;
    }
	
	/**
	 * @return Liefert das Attribut entferneElement.
	 */
	public IdLinkList getEntferneElement() {
		return entferneElement;
	}
	/**
	 * @param entferneElement Setzt das Attribut entferneElement.
	 */
	public void setEntferneElement(IdLinkList entferneElement) {
		this.entferneElement = entferneElement;
	}
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
	public Kultur getVarianteVon() {
		return varianteVon;
	}
	/**
	 * @param varianteVon Setzt das Attribut varianteVon.
	 */
	public void setVarianteVon(Herkunft varianteVon) {
		this.varianteVon = (Kultur) varianteVon;
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
