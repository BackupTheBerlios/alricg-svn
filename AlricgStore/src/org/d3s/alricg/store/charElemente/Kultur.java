/*
 * Created 22. Dezember 2004 / 13:07:48
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente;

import java.util.List;

import org.d3s.alricg.store.charElemente.charZusatz.Gegenstand;
import org.d3s.alricg.store.charElemente.links.Auswahl;

/**
 * Eine Kultur!
 * 
 * @author V.Strelow
 */
public class Kultur extends Herkunft {
    private List<Profession> professionMoeglich;
    private List<Profession> professionUeblich;
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
	public List<Profession> getProfessionMoeglich() {
		return professionMoeglich;
	}
	/**
	 * @param professionMoeglich the professionMoeglich to set
	 */
	public void setProfessionMoeglich(List<Profession> professionMoeglich) {
		this.professionMoeglich = professionMoeglich;
	}
	/**
	 * @return the professionUeblich
	 */
	public List<Profession> getProfessionUeblich() {
		return professionUeblich;
	}
	/**
	 * @param professionUeblich the professionUeblich to set
	 */
	public void setProfessionUeblich(List<Profession> professionUeblich) {
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
	}
    
    
}
