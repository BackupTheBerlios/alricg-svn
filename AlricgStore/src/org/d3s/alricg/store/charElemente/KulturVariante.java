/*
 * Created on 02.06.2005 / 17:47:52
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
 * Beschreib eine Variante, alle eine Kultur die eine andere Kultur modifiziert.
 * Im Normalfall werden alle angegeben Werte werden einfach zu der "original-Herkunft" 
 * hinzuaddiert. Stufen also summiert und Vor-/Nachteile, sowie SF u.ä. einfach zusätzlich
 * zu den bestehenden übernommen. Bei Talenten kommt es oft vor, das ein Talent das 
 * andere ersetzt. Dies wird einfach durch eine negative Modifikaton erreicht.
 * Es ist aber auch möglich das die Variante ohne Bezug auf das Original auskommt.
 * 
 * @author V. Strelow
 */
public class KulturVariante extends Kultur implements HerkunftVariante<Kultur> {
	/** Gibt die original-Kultur an.*/
	private Kultur varianteVon;

	// Tags für "entferneXmlTag"
	public final static String PROF_MOEGLICH = "Profession möglich";
	public final static String PROF_UEBLICH = "Profession üblich";
	public final static String MUTTERSPR = "Muttersprache";
	public final static String ZWEITSPR = "Zweitsprache";
	public final static String LEHRSPR = "Lehrsprache";
	public final static String SPRACHEN = "Sprachen";
	public final static String SCHRIFTEN = "Schriften";
	public final static String AUSRUESTUNG = "Ausrüstung";
	public final static String[] ALLE_TAGS = new String[] {
		PROF_MOEGLICH, PROF_UEBLICH, MUTTERSPR, ZWEITSPR, LEHRSPR,
		SPRACHEN, SCHRIFTEN, AUSRUESTUNG};
	
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
	 * @return the varianteVon
	 */
	@XmlTransient
	public Kultur getVarianteVon() {
		return varianteVon;
	}

	/**
	 * @param varianteVon the varianteVon to set
	 */
	public void setVarianteVon(Kultur varianteVon) {
		this.varianteVon = varianteVon;
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

	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.charElemente.Kultur#getArt()
	 */
	@Override
	public KulturArt getArt() {
		return varianteVon.getArt();
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.charElemente.Kultur#isProfessionenSindNegativListe()
	 */
	@Override
	public boolean isProfessionenSindNegativListe() {
		return varianteVon.isProfessionenSindNegativListe();
	}
	
	

}
