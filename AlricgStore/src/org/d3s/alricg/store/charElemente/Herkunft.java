/*
 * Created 22. Dezember 2004 / 02:25:53
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente;

import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;

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
public abstract class Herkunft<VARIANTE extends HerkunftVariante> extends CharElement implements HerkunftInterface<VARIANTE> {
	public static int SO_MIN_DEFAULT = 1;
	public static int SO_MAX_DEFAULT = 21;
	
	// Daf�r gibt es das Feld beim CharElement
    //private boolean kannGewaehltWerden; // Eine Herkunft kann auch nur als Basis
										  // f�r Varianten dienen
	
	private int gpKosten;
    private Geschlecht geschlecht;
    private int soMin = SO_MIN_DEFAULT;
    private int soMax = SO_MAX_DEFAULT;
    
    private Auswahl<Eigenschaft> eigenschaftModis;
    private Auswahl<Vorteil> vorteile;
    private Auswahl<Nachteil> nachteile;
    private Auswahl<Sonderfertigkeit> sonderfertigkeiten;
    private Auswahl<Liturgie> liturgien;
    private Auswahl<Talent> talente;
    private IdLink<Vorteil>[] empfVorteile;
    private IdLink<Nachteil>[] empfNachteile;
    private IdLink<Vorteil>[] ungeVorteile;
    private IdLink<Nachteil>[] ungeNachteile;
    private IdLink<Sonderfertigkeit>[] verbilligteSonderf;
    private IdLink<Liturgie>[] verbilligteLiturgien;
    private MagieEigenschaften magieEigenschaften;
    private AlternativMagieEigenschaften alternativMagieEigenschaften;
    
    /**
	 * @return the alternativMagieEigenschaften
	 */
	public AlternativMagieEigenschaften getAlternativMagieEigenschaften() {
		return alternativMagieEigenschaften;
	}


	/**
	 * @param alternativMagieEigenschaften the alternativMagieEigenschaften to set
	 */
	public void setAlternativMagieEigenschaften(
			AlternativMagieEigenschaften alternativMagieEigenschaften) {
		this.alternativMagieEigenschaften = alternativMagieEigenschaften;
	}


	//public abstract void setVarianten(VARIANTE[] varianten);
	
    /**
     * Eine alternative Methode zum angeben der Zauber!
     * Siehe:
     * - WDH S.163, gilt fuer alle elfischische Professionen
     * - WDH S.172, Kristallomant
     * @author Vincent
     */
    public static class AlternativMagieEigenschaften {
    	 private int anzZauberGesamt; // Wie viele k�nnen insgesamt gew�hlt werden
    	 private int anzHauszauber; // Davon Hauszauber
    	 private int anzLeitzauber; // Davon Leitzauber
    	 private int punkte;
    	 private Zauber[] empfHauszauber;
    	 private Zauber[] empfZauber;
    	 private Zauber[] moeglichZauber;
		/**
		 * @return the anzZauberGesamt
		 */
		public int getAnzZauberGesamt() {
			return anzZauberGesamt;
		}
		/**
		 * @param anzZauberGesamt the anzZauberGesamt to set
		 */
		public void setAnzZauberGesamt(int anzZauberGesamt) {
			this.anzZauberGesamt = anzZauberGesamt;
		}
		/**
		 * @return the punkte
		 */
		public int getPunkte() {
			return punkte;
		}
		/**
		 * @param punkte the punkte to set
		 */
		public void setPunkte(int punkte) {
			this.punkte = punkte;
		}
		/**
		 * @return the anzHauszauber
		 */
		public int getAnzHauszauber() {
			return anzHauszauber;
		}
		/**
		 * @param anzHauszauber the anzHauszauber to set
		 */
		public void setAnzHauszauber(int anzHauszauber) {
			this.anzHauszauber = anzHauszauber;
		}
		/**
		 * @return the anzLeitzauber
		 */
		public int getAnzLeitzauber() {
			return anzLeitzauber;
		}
		/**
		 * @param anzLeitzauber the anzLeitzauber to set
		 */
		public void setAnzLeitzauber(int anzLeitzauber) {
			this.anzLeitzauber = anzLeitzauber;
		}
		/**
		 * @return the empfHauszauber
		 */
		@XmlList
		@XmlIDREF
	    @XmlSchemaType(name = "IDREF")
		public Zauber[] getEmpfHauszauber() {
			return empfHauszauber;
		}
		/**
		 * @param empfHauszauber the empfHauszauber to set
		 */
		public void setEmpfHauszauber(Zauber[] empfHauszauber) {
			this.empfHauszauber = empfHauszauber;
		}
		/**
		 * @return the empfZauber
		 */
		@XmlList
		@XmlIDREF
	    @XmlSchemaType(name = "IDREF")
		public Zauber[] getEmpfZauber() {
			return empfZauber;
		}
		/**
		 * @param empfZauber the empfZauber to set
		 */
		public void setEmpfZauber(Zauber[] empfZauber) {
			this.empfZauber = empfZauber;
		}
		/**
		 * @return the moeglichZauber
		 */
		@XmlList
		@XmlIDREF
	    @XmlSchemaType(name = "IDREF")
		public Zauber[] getMoeglichZauber() {
			return moeglichZauber;
		}
		/**
		 * @param moeglichZauber the moeglichZauber to set
		 */
		public void setMoeglichZauber(Zauber[] moeglichZauber) {
			this.moeglichZauber = moeglichZauber;
		}

    }
    
    public static class MagieEigenschaften {
	    private Auswahl<Zauber> zauber;
	    private Auswahl<Zauber> hauszauber;
	    
	    // I.d.r. k�nnen alle Zuaber mit einer Bekanntheit von 4 oder 
	    // mehr in der Repr�sentation gelernt werden. Abweichungen davon
	    // werden hier festgehalten
	    private IdLink<Zauber>[] zusaetzlichAktivierbareZauber;
	    private IdLink<Zauber>[] fehlendeAktivierbareZauber;
		/**
		 * @return the zauber
		 */
		public Auswahl<Zauber> getZauber() {
			return zauber;
		}
		/**
		 * @param zauber the zauber to set
		 */
		public void setZauber(Auswahl<Zauber> zauber) {
			this.zauber = zauber;
		}
		/**
		 * @return the hauszauber
		 */
		public Auswahl<Zauber> getHauszauber() {
			return hauszauber;
		}
		/**
		 * @param hauszauber the hauszauber to set
		 */
		public void setHauszauber(Auswahl<Zauber> hauszauber) {
			this.hauszauber = hauszauber;
		}
		/**
		 * @return the zusaetzlichAktivierbareZauber
		 */
		public IdLink<Zauber>[] getZusaetzlichAktivierbareZauber() {
			return zusaetzlichAktivierbareZauber;
		}
		/**
		 * @param zusaetzlichAktivierbareZauber the zusaetzlichAktivierbareZauber to set
		 */
		public void setZusaetzlichAktivierbareZauber(
				IdLink<Zauber>[] zusaetzlichAktivierbareZauber) {
			this.zusaetzlichAktivierbareZauber = zusaetzlichAktivierbareZauber;
		}
		/**
		 * @return the fehlendeAktivierbareZauber
		 */
		public IdLink<Zauber>[] getFehlendeAktivierbareZauber() {
			return fehlendeAktivierbareZauber;
		}
		/**
		 * @param fehlendeAktivierbareZauber the fehlendeAktivierbareZauber to set
		 */
		public void setFehlendeAktivierbareZauber(
				IdLink<Zauber>[] fehlendeAktivierbareZauber) {
			this.fehlendeAktivierbareZauber = fehlendeAktivierbareZauber;
		}
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
	 *
	public boolean isKannGewaehltWerden() {
		return kannGewaehltWerden;
	}


	/**
	 * @param kannGewaehltWerden the kannGewaehltWerden to set
	 *
	public void setKannGewaehltWerden(boolean kannGewaehltWerden) {
		this.kannGewaehltWerden = kannGewaehltWerden;
	}*/


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
	 * @return the vorteile
	 */
	public Auswahl<Vorteil> getVorteile() {
		return vorteile;
	}


	/**
	 * @param vorteile the vorteile to set
	 */
	public void setVorteile(Auswahl<Vorteil> vorteile) {
		this.vorteile = vorteile;
	}


	/**
	 * @return the nachteile
	 */
	public Auswahl<Nachteil> getNachteile() {
		return nachteile;
	}


	/**
	 * @param nachteile the nachteile to set
	 */
	public void setNachteile(Auswahl<Nachteil> nachteile) {
		this.nachteile = nachteile;
	}


	/**
	 * @return the sonderfertigkeiten
	 */
	public Auswahl<Sonderfertigkeit> getSonderfertigkeiten() {
		return sonderfertigkeiten;
	}


	/**
	 * @param sonderfertigkeiten the sonderfertigkeiten to set
	 */
	public void setSonderfertigkeiten(Auswahl<Sonderfertigkeit> sonderfertigkeiten) {
		this.sonderfertigkeiten = sonderfertigkeiten;
	}


	/**
	 * @return the liturgien
	 */
	public Auswahl<Liturgie> getLiturgien() {
		return liturgien;
	}


	/**
	 * @param liturgien the liturgien to set
	 */
	public void setLiturgien(Auswahl<Liturgie> liturgien) {
		this.liturgien = liturgien;
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
