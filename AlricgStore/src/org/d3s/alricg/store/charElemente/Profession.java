/*
 * Created 22. Dezember 2004 / 13:07:41
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente;

import javax.xml.bind.annotation.XmlEnum;

import org.d3s.alricg.store.charElemente.charZusatz.Gegenstand;
import org.d3s.alricg.store.charElemente.charZusatz.MagierAkademie;
import org.d3s.alricg.store.charElemente.links.Auswahl;

/**
 * Repräsentiert eine Profession, speichert alle nötigen Daten.
 * 
 * @author V.Strelow
 */
public class Profession extends Herkunft {

	@XmlEnum
	public enum ProfArt {
		handwerklich("handwerklich"),
		kriegerisch("kriegerisch"),
		gesellschaftlich("gesellschaftlich"),
		wildnis("wildnis"),
		magisch("magisch"),
		geweiht("geweiht"),
		schamanisch("schamanisch");

		private String value; // VALUE des Elements

		private ProfArt(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	@XmlEnum
	public enum Aufwand {
		erstprof("erstprofession"),
		zeitaufw("zeitaufwendig"),
		normal("-");

		private String value; // Value des Elements

		private Aufwand(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	private Aufwand aufwand;
	private ProfArt art;
	private Auswahl<Sprache> sprachen;
	private Auswahl<Schrift> schriften;
	private Auswahl<Gegenstand> ausruestung;
	private Auswahl<Gegenstand> besondererBesitz;
	private ProfessionVariante[] varianten;
	private MagierAkademie magierAkademie;
	
	/**
	 * @return the aufwand
	 */
	public Aufwand getAufwand() {
		return aufwand;
	}

	/**
	 * @param aufwand the aufwand to set
	 */
	public void setAufwand(Aufwand aufwand) {
		this.aufwand = aufwand;
	}


	/**
	 * @return the art
	 */
	public ProfArt getArt() {
		return art;
	}


	/**
	 * @param art the art to set
	 */
	public void setArt(ProfArt art) {
		this.art = art;
	}
	
	
	/**
	 * @return the magierAkademie
	 */
	public MagierAkademie getMagierAkademie() {
		return magierAkademie;
	}


	/**
	 * @param magierAkademie the magierAkademie to set
	 */
	public void setMagierAkademie(MagierAkademie magierAkademie) {
		this.magierAkademie = magierAkademie;
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
	 * @return the besondererBesitz
	 */
	public Auswahl<Gegenstand> getBesondererBesitz() {
		return besondererBesitz;
	}


	/**
	 * @param besondererBesitz the besondererBesitz to set
	 */
	public void setBesondererBesitz(Auswahl<Gegenstand> besondererBesitz) {
		this.besondererBesitz = besondererBesitz;
	}


	/**
	 * @return the varianten
	 */
	public ProfessionVariante[] getVarianten() {
		return varianten;
	}


	/**
	 * @param varianten the varianten to set
	 */
	public void setVarianten(ProfessionVariante[] varianten) {
		this.varianten = varianten;
	}
	
	
}
