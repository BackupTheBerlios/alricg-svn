/*
 * Created on 04.06.2005 / 18:48:54
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
 * Beschreib eine Variante, alle eine Profession die eine andere Profession modifiziert.
 * Im Normalfall werden alle angegeben Werte werden einfach zu der "original-Herkunft" 
 * hinzuaddiert. Stufen also summiert und Vor-/Nachteile, sowie SF u.ä. einfach zusätzlich
 * zu den bestehenden übernommen. Bei Talenten kommt es oft vor, das ein Talent das 
 * andere ersetzt. Dies wird einfach durch eine negative Modifikaton erreicht.
 * Es ist aber auch möglich das die Variante ohne Bezug auf das Original auskommt.
 * 
 * @author V. Strelow
 */

public class ProfessionVariante extends Profession implements HerkunftVariante<Profession> {
	/** Gibt die original-Profession an.*/
	private Profession varianteVon;
	
	public final static String SPRACHEN = "Sprachen";
	public final static String SCHRIFTEN = "Schriften";
	public final static String AUSRUESTUNG = "Ausrüstung";
	public final static String BESOND_BESITZ = "Ausrüstung";
	public final static String[] ALLE_TAGS = new String[] {
		SPRACHEN, SCHRIFTEN, AUSRUESTUNG, BESOND_BESITZ};
	
	/** Liste von XML-Tags die aus der original-Profession "entfernt" (also nicht beachtet) 
	 * werden sollen, z.B. sonderfertigkeiten, vorteile.*/
	private String[] entferneXmlTag;
	
	/** Gibt an ob diese Varante mit anderen Varianten zusammen gewählt werden kann.
	 * true - Diese Variante kann zusätzlich zu anderen gewählt werden, ansonsten false */
	private boolean isMultibel;

	/** Bei einer AdditionsVariante (=true) werden alle Werte/ Elemente der Varinate zu der 
	 * original-Profession hinzugefügt (mit den entfernten Elementen). 
	 * Eine NICHT AdditionsVariante (=false) ist eine eigenständige Profession ohne Abhängigkeiten, 
	 * von der "original-Profession". Alle nötigen Angaben sind dementsprechend enthalten.
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
	public Profession getVarianteVon() {
		return varianteVon;
	}
	/**
	 * @param varianteVon Setzt das Attribut varianteVon.
	 */
	public void setVarianteVon(Profession varianteVon) {
		this.varianteVon = varianteVon;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.HerkunftVariante#isAdditionsVariante()
	 */
	public boolean isAdditionsVariante() {
		return isAdditionsVariante;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.HerkunftVariante#setAdditionsVariante(boolean)
	 */
	public void setAdditionsVariante(boolean isAdditionsVariante) {
		this.isAdditionsVariante = isAdditionsVariante;
	}
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.charElemente.Profession#getArt()
	 */
	@Override
	public ProfArt getArt() {
		return varianteVon.getArt();
	}
	
	
}
