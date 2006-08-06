/*
 * Created 22. Dezember 2004 / 02:25:53
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import org.d3s.alricg.charKomponenten.Werte.Geschlecht;
import org.d3s.alricg.charKomponenten.links.Auswahl;
import org.d3s.alricg.charKomponenten.links.IdLinkList;

/**
 * <b>Beschreibung: </b> <br>
 * Superklasse für Rasse, Kultur und Profession. Fasst Gemeinsamkeiten zusammen.
 * Elemente vom Typ "Herkunft" werden nach einer Initiierung nicht mehr
 * verändert.
 * 
 * @author V.Strelow
 */
public abstract class Herkunft extends CharElement {
	
	public static int SO_MIN_DEFAULT = 0;
	public static int SO_MAX_DEFAULT = 100;
	private int gpKosten;
    private boolean kannGewaehltWerden = true; // Eine Herkunft kann auch nur als Basis
                                        // für Varianten dienen
    private Geschlecht geschlecht = Geschlecht.mannFrau;
    private int soMin = SO_MIN_DEFAULT;
    private int soMax = SO_MAX_DEFAULT;
    /**
     * Beispiel: An der Stelle Eigenschaft.MU im Array steht der Wert für Mut.
     */
    /*private int[] eigenschaftVoraussetzungen = new int[Eigenschaften
            .getAnzahlEigenschaften()];
    private int[] eigenschaftModis = new int[Eigenschaften
            .getAnzahlEigenschaften()];*/
    
    // TODO: Es gibt doch auch hier ne Auswahl??
    private Auswahl eigenschaftModis;

    private Repraesentation repraesentation;
    
    private Auswahl vorteileAuswahl;
    private Auswahl nachteileAuswahl;
    private Auswahl sfAuswahl;
    private Auswahl liturgienAuswahl;
    private Auswahl ritualeAuswahl;
    private IdLinkList empfVorteile;
    private IdLinkList empfNachteile;
    private IdLinkList ungeVorteile;
    private IdLinkList ungeNachteile;
    private IdLinkList verbilligteVort;
    private IdLinkList verbilligteNacht;
    private IdLinkList verbilligteSonderf;
    private IdLinkList verbilligteLiturgien;
    private IdLinkList verbilligteRituale;
    private Auswahl talente;
    private Auswahl zauber;
    private Auswahl hauszauber;
    private Auswahl aktivierbareZauber;
    private IdLinkList ZauberNichtBeginn;
    
    //protected Herkunft varianteVon;
    
    
    public abstract HerkunftVariante[] getVarianten();
    
    /**
     * @return true - Diese Herkunft ist Variante einer anderen Herkunft
     *
    public boolean isVariante() {
    	return false;
    }*/
    
    /**
     * Für manche Herkunft ist das Geschlecht wichtig. In dem Fall wird hier das
     * Geschlecht angegeben.
     * 
     * @return Liefert das vorgeschriebene Geschlecht für diese Herkunft.
     */
    public Geschlecht getGeschlecht() {
        return geschlecht;
    }

    /**
     * @return Liefert die Kosten in Generierungs-Punkten.
     */
    public int getGpKosten() {
        return gpKosten;
    }

    /**
     * Eine Herkunft die nicht gewählt werden kann, dient als Basis für
     * Varianten und kann nicht direkt gewählt werden. Aber durchaus indirekt.
     * 
     * @return Liefert das Attribut kannGewaehltWerden
     */
    public boolean isKannGewaehltWerden() {
        return kannGewaehltWerden;
    }

    /**
     * @return Liefert den Maximal zulässigen SozialStatus / 100 ist "Default"
     */
    public int getSoMax() {
        return soMax;
    }

    /**
     * @return Liefert den Minimal zulässigen SozialStatus / 0 ist "Default"
     */
    public int getSoMin() {
        return soMin;
    }

    /**
     * Mit dieser Methode werden die Modifikationen auf Eigenschaften (MU, KL,.. ),
     * LeP, AsP, AuP, Ka, SO, INI und MR ausgelesen.
     * 
     * @param eigenschaft Die Enum aus der Klasse "Eigenschaften"
     * @return Der Modifikator-Wert für diese Eigenschaft
     * @see org.d3s.alricg.CharComponenten.EigenschaftEnum
     */
    public int getEigenschaftModi(EigenschaftEnum eigenschaft) {
        // TODO implement getModi mit enum
        return 0; // eigenschaftModis[modiId];
    }

    /**
     * @return Auswahl an Vorteilen, die gewählt werden können,
     */
    public Auswahl getVorteileAuswahl() {
        return vorteileAuswahl;
    }

    /**
     * @return Auswahl an Nachteilen, die gewählt werden können
     */
    public Auswahl getNachteileAuswahl() {
        return nachteileAuswahl;
    }
    
    /**
     * @return Auswahl an Sonderfertigkeiten, die gewählt werden können
     */
    public Auswahl getSfAuswahl() {
        return sfAuswahl;
    }
    
    /**
     * @return Auswahl an Liturgien, die gewählt werden können
     */
    public Auswahl getLiturgienAuswahl() {
        return liturgienAuswahl;
    }

    public Auswahl getRitualeAuswahl() {
        return ritualeAuswahl;
    }

    public IdLinkList getEmpfVorteil() {
        return empfVorteile;
    }

    public IdLinkList getEmpfNachteile() {
        return empfNachteile;
    }

    public IdLinkList getUngeVorteile() {
        return ungeVorteile;
    }

    public IdLinkList getUngeNachteile() {
        return ungeNachteile;
    }

    public IdLinkList getVerbilligteSonderf() {
        return verbilligteSonderf;
    }

    public IdLinkList getVerbilligteLiturien() {
        return verbilligteLiturgien;
    }

    public IdLinkList getVerbilligteRituale() {
        return verbilligteRituale;
    }

    public Auswahl getTalente() {
        return talente;
    }

    public Auswahl getZauber() {
        return zauber;
    }

    public Auswahl getHauszauber() {
        return hauszauber;
    }

    public Auswahl getAktivierbareZauber() {
        return aktivierbareZauber;
    }
    
	/**
	 * @return Liefert das Attribut eigenschaftModis.
	 */
	public Auswahl getEigenschaftModis() {
		return eigenschaftModis;
	}
	/**
	 * @param eigenschaftModis Setzt das Attribut eigenschaftModis.
	 */
	public void setEigenschaftModis(Auswahl eigenschaftModis) {
		this.eigenschaftModis = eigenschaftModis;
	}
	/**
	 * @return Liefert das Attribut empfVorteile.
	 */
	public IdLinkList getEmpfVorteile() {
		return empfVorteile;
	}
	/**
	 * @param empfVorteile Setzt das Attribut empfVorteile.
	 */
	public void setEmpfVorteile(IdLinkList empfVorteile) {
		this.empfVorteile = empfVorteile;
	}
	/**
	 * @return Liefert das Attribut repraesentation.
	 */
	public Repraesentation getRepraesentation() {
		return repraesentation;
	}
	/**
	 * @param repraesentation Setzt das Attribut repraesentation.
	 */
	public void setRepraesentation(Repraesentation repraesentation) {
		this.repraesentation = repraesentation;
	}
	/**
	 * @return Liefert das Attribut verbilligteLiturgien.
	 */
	public IdLinkList getVerbilligteLiturgien() {
		return verbilligteLiturgien;
	}
	/**
	 * @param verbilligteLiturgien Setzt das Attribut verbilligteLiturgien.
	 */
	public void setVerbilligteLiturgien(IdLinkList verbilligteLiturgien) {
		this.verbilligteLiturgien = verbilligteLiturgien;
	}
	/**
	 * @return Liefert das Attribut verbilligteNacht.
	 */
	public IdLinkList getVerbilligteNacht() {
		return verbilligteNacht;
	}
	/**
	 * @param verbilligteNacht Setzt das Attribut verbilligteNacht.
	 */
	public void setVerbilligteNacht(IdLinkList verbilligteNacht) {
		this.verbilligteNacht = verbilligteNacht;
	}
	/**
	 * @return Liefert das Attribut verbilligteVort.
	 */
	public IdLinkList getVerbilligteVort() {
		return verbilligteVort;
	}
	/**
	 * @param verbilligteVort Setzt das Attribut verbilligteVort.
	 */
	public void setVerbilligteVort(IdLinkList verbilligteVort) {
		this.verbilligteVort = verbilligteVort;
	}
	/**
	 * @return Liefert das Attribut zauberNichtBeginn.
	 */
	public IdLinkList getZauberNichtBeginn() {
		return ZauberNichtBeginn;
	}
	/**
	 * @param zauberNichtBeginn Setzt das Attribut zauberNichtBeginn.
	 */
	public void setZauberNichtBeginn(IdLinkList zauberNichtBeginn) {
		ZauberNichtBeginn = zauberNichtBeginn;
	}
	/**
	 * @param aktivierbareZauber Setzt das Attribut aktivierbareZauber.
	 */
	public void setAktivierbareZauber(Auswahl aktivierbareZauber) {
		this.aktivierbareZauber = aktivierbareZauber;
	}
	/**
	 * @param empfNachteile Setzt das Attribut empfNachteile.
	 */
	public void setEmpfNachteile(IdLinkList empfNachteile) {
		this.empfNachteile = empfNachteile;
	}
										/**
										 * @param geschlecht Setzt das Attribut geschlecht.
										 */
										public void setGeschlecht(
												Geschlecht geschlecht) {
											this.geschlecht = geschlecht;
										}
	/**
	 * @param gpKosten Setzt das Attribut gpKosten.
	 */
	public void setGpKosten(int gpKosten) {
		this.gpKosten = gpKosten;
	}
	/**
	 * @param hauszauber Setzt das Attribut hauszauber.
	 */
	public void setHauszauber(Auswahl hauszauber) {
		this.hauszauber = hauszauber;
	}
	/**
	 * @param kannGewaehltWerden Setzt das Attribut kannGewaehltWerden.
	 */
	public void setKannGewaehltWerden(boolean kannGewaehltWerden) {
		this.kannGewaehltWerden = kannGewaehltWerden;
	}
	/**
	 * @param liturgienAuswahl Setzt das Attribut liturgienAuswahl.
	 */
	public void setLiturgienAuswahl(Auswahl liturgienAuswahl) {
		this.liturgienAuswahl = liturgienAuswahl;
	}
	/**
	 * @param nachteileAuswahl Setzt das Attribut nachteileAuswahl.
	 */
	public void setNachteileAuswahl(Auswahl nachteileAuswahl) {
		this.nachteileAuswahl = nachteileAuswahl;
	}
	/**
	 * @param ritualeAuswahl Setzt das Attribut ritualeAuswahl.
	 */
	public void setRitualeAuswahl(Auswahl ritualeAuswahl) {
		this.ritualeAuswahl = ritualeAuswahl;
	}
	/**
	 * @param sfAuswahl Setzt das Attribut sfAuswahl.
	 */
	public void setSfAuswahl(Auswahl sfAuswahl) {
		this.sfAuswahl = sfAuswahl;
	}
	/**
	 * @param soMax Setzt das Attribut soMax.
	 */
	public void setSoMax(int soMax) {
		this.soMax = soMax;
	}
	/**
	 * @param soMin Setzt das Attribut soMin.
	 */
	public void setSoMin(int soMin) {
		this.soMin = soMin;
	}
	/**
	 * @param talente Setzt das Attribut talente.
	 */
	public void setTalente(Auswahl talente) {
		this.talente = talente;
	}
	/**
	 * @param ungeNachteile Setzt das Attribut ungeNachteile.
	 */
	public void setUngeNachteile(IdLinkList ungeNachteile) {
		this.ungeNachteile = ungeNachteile;
	}
	/**
	 * @param ungeVorteile Setzt das Attribut ungeVorteile.
	 */
	public void setUngeVorteile(IdLinkList ungeVorteile) {
		this.ungeVorteile = ungeVorteile;
	}

	/**
	 * @param verbilligteRituale Setzt das Attribut verbilligteRituale.
	 */
	public void setVerbilligteRituale(IdLinkList verbilligteRituale) {
		this.verbilligteRituale = verbilligteRituale;
	}
	/**
	 * @param verbilligteSonderf Setzt das Attribut verbilligteSonderf.
	 */
	public void setVerbilligteSonderf(IdLinkList verbilligteSonderf) {
		this.verbilligteSonderf = verbilligteSonderf;
	}
	/**
	 * @param vorteileAuswahl Setzt das Attribut vorteileAuswahl.
	 */
	public void setVorteileAuswahl(Auswahl vorteileAuswahl) {
		this.vorteileAuswahl = vorteileAuswahl;
	}
	/**
	 * @param zauber Setzt das Attribut zauber.
	 */
	public void setZauber(Auswahl zauber) {
		this.zauber = zauber;
	}

}
