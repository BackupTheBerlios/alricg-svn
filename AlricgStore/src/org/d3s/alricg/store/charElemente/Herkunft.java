/*
 * Created 22. Dezember 2004 / 02:25:53
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente;

import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.Werte.Geschlecht;
import org.d3s.alricg.store.charElemente.links.Auswahl;
import org.d3s.alricg.store.charElemente.links.IdLink;

/**
 * <b>Beschreibung: </b> <br>
 * Superklasse f�r Rasse, Kultur und Profession. Fasst Gemeinsamkeiten zusammen.
 * Elemente vom Typ "Herkunft" werden nach einer Initiierung nicht mehr
 * ver�ndert.
 * 
 * @author V.Strelow
 */
public abstract class Herkunft extends CharElement {
	
	public static int SO_MIN_DEFAULT = 0;
	public static int SO_MAX_DEFAULT = 100;
	private int gpKosten;
    private boolean kannGewaehltWerden = true; // Eine Herkunft kann auch nur als Basis
                                        // f�r Varianten dienen
    private Geschlecht geschlecht = Geschlecht.unbekannt;
    private int soMin = SO_MIN_DEFAULT;
    private int soMax = SO_MAX_DEFAULT;
    private Auswahl<Eigenschaft> eigenschaftModis;
    private Auswahl<Vorteil> vorteileAuswahl;
    private Auswahl<Nachteil> nachteileAuswahl;
    private Auswahl<Sonderfertigkeit> sfAuswahl;
    private Auswahl<Liturgie> liturgienAuswahl;
    private Auswahl<Talent> talente;
    private IdLink<Vorteil>[] empfVorteile;
    private IdLink<Nachteil>[] empfNachteile;
    private IdLink<Vorteil>[] ungeVorteile;
    private IdLink<Nachteil>[] ungeNachteile;
    private IdLink<Vorteil>[] verbilligteVort;
    private IdLink<Nachteil>[] verbilligteNacht;
    private IdLink<Sonderfertigkeit>[] verbilligteSonderf;
    private IdLink<Liturgie>[] verbilligteLiturgien;
    private MagieEigenschaften magieEigenschaften;
    
    public abstract HerkunftVariante[] getVarianten();
    
    public static class MagieEigenschaften {
	    private Auswahl zauber;
	    private Auswahl hauszauber;
	    private Auswahl aktivierbareZauber;
	    private IdLink<Zauber> ZauberNichtBeginn;
		/**
		 * @return the zauber
		 */
		public Auswahl getZauber() {
			return zauber;
		}
		/**
		 * @param zauber the zauber to set
		 */
		public void setZauber(Auswahl zauber) {
			this.zauber = zauber;
		}
		/**
		 * @return the hauszauber
		 */
		public Auswahl getHauszauber() {
			return hauszauber;
		}
		/**
		 * @param hauszauber the hauszauber to set
		 */
		public void setHauszauber(Auswahl hauszauber) {
			this.hauszauber = hauszauber;
		}
		/**
		 * @return the aktivierbareZauber
		 */
		public Auswahl getAktivierbareZauber() {
			return aktivierbareZauber;
		}
		/**
		 * @param aktivierbareZauber the aktivierbareZauber to set
		 */
		public void setAktivierbareZauber(Auswahl aktivierbareZauber) {
			this.aktivierbareZauber = aktivierbareZauber;
		}
		/**
		 * @return the zauberNichtBeginn
		 */
		public IdLink<Zauber> getZauberNichtBeginn() {
			return ZauberNichtBeginn;
		}
		/**
		 * @param zauberNichtBeginn the zauberNichtBeginn to set
		 */
		public void setZauberNichtBeginn(IdLink<Zauber> zauberNichtBeginn) {
			ZauberNichtBeginn = zauberNichtBeginn;
		}

    }

    /**
     * Mit dieser Methode werden die Modifikationen auf Eigenschaften (MU, KL,.. ),
     * LeP, AsP, AuP, Ka, SO, INI und MR ausgelesen.
     * 
     * @param eigenschaft Die Enum aus der Klasse "Eigenschaften"
     * @return Der Modifikator-Wert f�r diese Eigenschaft
     * @see org.d3s.alricg.CharComponenten.EigenschaftEnum
     */
    public int getEigenschaftModi(EigenschaftEnum eigenschaft) {
        // TODO implement oder L�schen!
        return 0; // eigenschaftModis[modiId];
    }


	/**
	 * @return the gpKosten
	 */
	public int getGpKosten() {
		return gpKosten;
	}


	/**
	 * @param gpKosten the gpKosten to set
	 */
	public void setGpKosten(int gpKosten) {
		this.gpKosten = gpKosten;
	}


	/**
	 * @return the kannGewaehltWerden
	 */
	public boolean isKannGewaehltWerden() {
		return kannGewaehltWerden;
	}


	/**
	 * @param kannGewaehltWerden the kannGewaehltWerden to set
	 */
	public void setKannGewaehltWerden(boolean kannGewaehltWerden) {
		this.kannGewaehltWerden = kannGewaehltWerden;
	}


	/**
	 * @return the geschlecht
	 */
	public Geschlecht getGeschlecht() {
		return geschlecht;
	}


	/**
	 * @param geschlecht the geschlecht to set
	 */
	public void setGeschlecht(Geschlecht geschlecht) {
		this.geschlecht = geschlecht;
	}


	/**
	 * @return the soMin
	 */
	public int getSoMin() {
		return soMin;
	}


	/**
	 * @param soMin the soMin to set
	 */
	public void setSoMin(int soMin) {
		this.soMin = soMin;
	}


	/**
	 * @return the soMax
	 */
	public int getSoMax() {
		return soMax;
	}


	/**
	 * @param soMax the soMax to set
	 */
	public void setSoMax(int soMax) {
		this.soMax = soMax;
	}


	/**
	 * @return the eigenschaftModis
	 */
	public Auswahl<Eigenschaft> getEigenschaftModis() {
		return eigenschaftModis;
	}


	/**
	 * @param eigenschaftModis the eigenschaftModis to set
	 */
	public void setEigenschaftModis(Auswahl<Eigenschaft> eigenschaftModis) {
		this.eigenschaftModis = eigenschaftModis;
	}


	/**
	 * @return the vorteileAuswahl
	 */
	public Auswahl<Vorteil> getVorteileAuswahl() {
		return vorteileAuswahl;
	}


	/**
	 * @param vorteileAuswahl the vorteileAuswahl to set
	 */
	public void setVorteileAuswahl(Auswahl<Vorteil> vorteileAuswahl) {
		this.vorteileAuswahl = vorteileAuswahl;
	}


	/**
	 * @return the nachteileAuswahl
	 */
	public Auswahl<Nachteil> getNachteileAuswahl() {
		return nachteileAuswahl;
	}


	/**
	 * @param nachteileAuswahl the nachteileAuswahl to set
	 */
	public void setNachteileAuswahl(Auswahl<Nachteil> nachteileAuswahl) {
		this.nachteileAuswahl = nachteileAuswahl;
	}


	/**
	 * @return the sfAuswahl
	 */
	public Auswahl<Sonderfertigkeit> getSfAuswahl() {
		return sfAuswahl;
	}


	/**
	 * @param sfAuswahl the sfAuswahl to set
	 */
	public void setSfAuswahl(Auswahl<Sonderfertigkeit> sfAuswahl) {
		this.sfAuswahl = sfAuswahl;
	}


	/**
	 * @return the liturgienAuswahl
	 */
	public Auswahl<Liturgie> getLiturgienAuswahl() {
		return liturgienAuswahl;
	}


	/**
	 * @param liturgienAuswahl the liturgienAuswahl to set
	 */
	public void setLiturgienAuswahl(Auswahl<Liturgie> liturgienAuswahl) {
		this.liturgienAuswahl = liturgienAuswahl;
	}


	/**
	 * @return the talente
	 */
	public Auswahl<Talent> getTalente() {
		return talente;
	}


	/**
	 * @param talente the talente to set
	 */
	public void setTalente(Auswahl<Talent> talente) {
		this.talente = talente;
	}


	/**
	 * @return the empfVorteile
	 */
	public IdLink<Vorteil>[] getEmpfVorteile() {
		return empfVorteile;
	}


	/**
	 * @param empfVorteile the empfVorteile to set
	 */
	public void setEmpfVorteile(IdLink<Vorteil>[] empfVorteile) {
		this.empfVorteile = empfVorteile;
	}


	/**
	 * @return the empfNachteile
	 */
	public IdLink<Nachteil>[] getEmpfNachteile() {
		return empfNachteile;
	}


	/**
	 * @param empfNachteile the empfNachteile to set
	 */
	public void setEmpfNachteile(IdLink<Nachteil>[] empfNachteile) {
		this.empfNachteile = empfNachteile;
	}


	/**
	 * @return the ungeVorteile
	 */
	public IdLink<Vorteil>[] getUngeVorteile() {
		return ungeVorteile;
	}


	/**
	 * @param ungeVorteile the ungeVorteile to set
	 */
	public void setUngeVorteile(IdLink<Vorteil>[] ungeVorteile) {
		this.ungeVorteile = ungeVorteile;
	}


	/**
	 * @return the ungeNachteile
	 */
	public IdLink<Nachteil>[] getUngeNachteile() {
		return ungeNachteile;
	}


	/**
	 * @param ungeNachteile the ungeNachteile to set
	 */
	public void setUngeNachteile(IdLink<Nachteil>[] ungeNachteile) {
		this.ungeNachteile = ungeNachteile;
	}


	/**
	 * @return the verbilligteVort
	 */
	public IdLink<Vorteil>[] getVerbilligteVort() {
		return verbilligteVort;
	}


	/**
	 * @param verbilligteVort the verbilligteVort to set
	 */
	public void setVerbilligteVort(IdLink<Vorteil>[] verbilligteVort) {
		this.verbilligteVort = verbilligteVort;
	}


	/**
	 * @return the verbilligteNacht
	 */
	public IdLink<Nachteil>[] getVerbilligteNacht() {
		return verbilligteNacht;
	}


	/**
	 * @param verbilligteNacht the verbilligteNacht to set
	 */
	public void setVerbilligteNacht(IdLink<Nachteil>[] verbilligteNacht) {
		this.verbilligteNacht = verbilligteNacht;
	}


	/**
	 * @return the verbilligteSonderf
	 */
	public IdLink<Sonderfertigkeit>[] getVerbilligteSonderf() {
		return verbilligteSonderf;
	}


	/**
	 * @param verbilligteSonderf the verbilligteSonderf to set
	 */
	public void setVerbilligteSonderf(IdLink<Sonderfertigkeit>[] verbilligteSonderf) {
		this.verbilligteSonderf = verbilligteSonderf;
	}


	/**
	 * @return the verbilligteLiturgien
	 */
	public IdLink<Liturgie>[] getVerbilligteLiturgien() {
		return verbilligteLiturgien;
	}


	/**
	 * @param verbilligteLiturgien the verbilligteLiturgien to set
	 */
	public void setVerbilligteLiturgien(IdLink<Liturgie>[] verbilligteLiturgien) {
		this.verbilligteLiturgien = verbilligteLiturgien;
	}


	/**
	 * @return the magieEigenschaften
	 */
	public MagieEigenschaften getMagieEigenschaften() {
		return magieEigenschaften;
	}


	/**
	 * @param magieEigenschaften the magieEigenschaften to set
	 */
	public void setMagieEigenschaften(MagieEigenschaften magieEigenschaften) {
		this.magieEigenschaften = magieEigenschaften;
	}
}