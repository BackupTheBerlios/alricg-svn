/*
 * Created 22. Dezember 2004 / 13:07:48
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;

import org.d3s.alricg.store.charElemente.charZusatz.Gegenstand;
import org.d3s.alricg.store.charElemente.links.Auswahl;

/**
 * Eine Kultur!
 * 
 * @author V.Strelow
 */
public class Kultur extends Herkunft<KulturVariante> {
	
	@XmlEnum
	public enum KulturArt {
		menschlich("Menschlich"), 
		nichtMenschlich("Nicht Menschlich");
		
		private String name; 
		
		private KulturArt(String name) {
			this.name = name;
		}
		
		public String toString() {
			return name;
		}
	}
	
	private KulturArt art;
	private boolean professionenSindNegativListe;
    private Profession[] professionMoeglich;
    private Profession[] professionUeblich;
    private Auswahl<Sprache> muttersprache;
    private Auswahl<Sprache> zweitsprache;
    private Auswahl<Sprache> lehrsprache;
    private Auswahl<Sprache> sprachen;
    private Auswahl<Schrift> schriften;
    private Auswahl<Gegenstand> ausruestung; // Vom Typ Gegenstand
    private RegionVolk regionVolk;
    private KulturVariante[] varianten;


	/**
	 * @return the professionMoeglich
	 */
	@XmlList
	@XmlIDREF
    @XmlSchemaType(name = "IDREF")
	public Profession[] getProfessionMoeglich() {
		return professionMoeglich;
	}
	/**
	 * @param professionMoeglich the professionMoeglich to set
	 */
	public void setProfessionMoeglich(Profession[] professionMoeglich) {
		this.professionMoeglich = professionMoeglich;
	}
	/**
	 * @return the professionUeblich
	 */
	@XmlList
	@XmlIDREF
    @XmlSchemaType(name = "IDREF")
	public Profession[] getProfessionUeblich() {
		return professionUeblich;
	}
	/**
	 * @param professionUeblich the professionUeblich to set
	 */
	public void setProfessionUeblich(Profession[] professionUeblich) {
		this.professionUeblich = professionUeblich;
	}
	/**
	 * @return the muttersprache
	 */
	public Auswahl<Sprache> getMuttersprache() {
		return muttersprache;
	}
	/**
	 * @param muttersprache the muttersprache to set
	 */
	public void setMuttersprache(Auswahl<Sprache> muttersprache) {
		this.muttersprache = muttersprache;
	}
	/**
	 * @return the zweitsprache
	 */
	public Auswahl<Sprache> getZweitsprache() {
		return zweitsprache;
	}
	/**
	 * @param zweitsprache the zweitsprache to set
	 */
	public void setZweitsprache(Auswahl<Sprache> zweitsprache) {
		this.zweitsprache = zweitsprache;
	}
	/**
	 * @return the lehrsprache
	 */
	public Auswahl<Sprache> getLehrsprache() {
		return lehrsprache;
	}
	/**
	 * @param lehrsprache the lehrsprache to set
	 */
	public void setLehrsprache(Auswahl<Sprache> lehrsprache) {
		this.lehrsprache = lehrsprache;
	}
	/**
	 * @return the sprachen
	 */
	public Auswahl<Sprache> getSprachen() {
		return sprachen;
	}
	/**
	 * @param sprachen the sprachen to set
	 */
	public void setSprachen(Auswahl<Sprache> sprachen) {
		this.sprachen = sprachen;
	}
	/**
	 * @return the schriften
	 */
	public Auswahl<Schrift> getSchriften() {
		return schriften;
	}
	/**
	 * @param schriften the schriften to set
	 */
	public void setSchriften(Auswahl<Schrift> schriften) {
		this.schriften = schriften;
	}
	/**
	 * @return the ausruestung
	 */
	public Auswahl<Gegenstand> getAusruestung() {
		return ausruestung;
	}
	/**
	 * @param ausruestung the ausruestung to set
	 */
	public void setAusruestung(Auswahl<Gegenstand> ausruestung) {
		this.ausruestung = ausruestung;
	}
	/**
	 * @return the regionVolk
	 */
	@XmlIDREF
    @XmlSchemaType(name = "IDREF")
	public RegionVolk getRegionVolk() {
		return regionVolk;
	}
	/**
	 * @param regionVolk the regionVolk to set
	 */
	public void setRegionVolk(RegionVolk regionVolk) {
		this.regionVolk = regionVolk;
	}
	
	/**
	 * @return the varianten
	 */
	public KulturVariante[] getVarianten() {
		return varianten;
	}
	/**
	 * @param varianten the varianten to set
	 */
	public void setVarianten(KulturVariante[] varianten) {
		this.varianten = varianten;
		if (varianten == null) return;
		for (int i = 0; i < varianten.length;i++) {
			varianten[i].setVarianteVon(this);
		}
	}
	/**
	 * @return the art
	 */
	@XmlAttribute
	public KulturArt getArt() {
		return art;
	}
	/**
	 * @param art the art to set
	 */
	public void setArt(KulturArt art) {
		this.art = art;
	}
	/**
	 * @return the professionenSindNegativListe
	 */
	public boolean isProfessionenSindNegativListe() {
		return professionenSindNegativListe;
	}
	/**
	 * @param professionenSindNegativListe the professionenSindNegativListe to set
	 */
	public void setProfessionenSindNegativListe(boolean professionenSindNegativListe) {
		this.professionenSindNegativListe = professionenSindNegativListe;
	}
    
    
}
