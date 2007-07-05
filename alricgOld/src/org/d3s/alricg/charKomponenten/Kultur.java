/*
 * Created 22. Dezember 2004 / 13:07:48
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import org.d3s.alricg.charKomponenten.links.Auswahl;
import org.d3s.alricg.charKomponenten.links.AuswahlAusruestung;
import org.d3s.alricg.charKomponenten.links.IdLinkList;
import org.d3s.alricg.controller.CharKomponente;

/**
 * <b>Beschreibung: </b> <br>
 * TODO Beschreibung einfügen
 * 
 * @author V.Strelow
 */
public class Kultur extends Herkunft {
    private IdLinkList professionMoeglich;
    private IdLinkList professionUeblich;
    private Auswahl muttersprache;
    private Auswahl zweitsprache;
    private Auswahl lehrsprache;

    private Auswahl sprachen;
    private Auswahl schriften;
    private AuswahlAusruestung ausruestung;
    private RegionVolk regionVolk;
    private KulturVariante[] varianten;
    
    //private Kultur varianteVon; 
    
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
	 */
	public CharKomponente getCharKomponente() {
		return CharKomponente.kultur;
	}

	/**
	 * Konstruktur; id beginnt mit "KUL-" für Kultur
	 * @param id Systemweit eindeutige id
	 */
	public Kultur(String id) {
		setId(id);
	}
    
    /**
     * @return Liefert das Attribut ausruestung.
     */
    public AuswahlAusruestung getAusruestung() {
        return ausruestung;
    }

    /**
     * @return Liefert das Attribut muttersprache.
     */
    public Auswahl getMuttersprache() {
        return muttersprache;
    }

    /**
     * @return Liefert das Attribut professionMoeglich.
     */
    public IdLinkList getProfessionMoeglich() {
        return professionMoeglich;
    }

    /**
     * @return Liefert das Attribut professionUeblich.
     */
    public IdLinkList getProfessionUeblich() {
        return professionUeblich;
    }

    /**
     * @return Liefert das Attribut schriften.
     */
    public Auswahl getSchriften() {
        return schriften;
    }

    /**
     * @return Liefert das Attribut sprachen.
     */
    public Auswahl getSprachen() {
        return sprachen;
    }

    /**
     * @return Liefert das Attribut zweitsprache.
     */
    public Auswahl getZweitsprache() {
        return zweitsprache;
    }
    
	/**
	 * @return Liefert das Attribut lehrsprache.
	 */
	public Auswahl getLehrsprache() {
		return lehrsprache;
	}
    
	/**
	 * @return Liefert das Attribut regionVolk.
	 */
	public RegionVolk getRegionVolk() {
		return regionVolk;
	}
	/**
	 * @param regionVolk Setzt das Attribut regionVolk.
	 */
	public void setRegionVolk(RegionVolk regionVolk) {
		this.regionVolk = regionVolk;
	}
	/**
	 * @param ausruestung Setzt das Attribut ausruestung.
	 */
	public void setAusruestung(AuswahlAusruestung ausruestung) {
		this.ausruestung = ausruestung;
	}
	/**
	 * @param muttersprache Setzt das Attribut muttersprache.
	 */
	public void setMuttersprache(Auswahl muttersprache) {
		this.muttersprache = muttersprache;
	}
	
	/**
	 * @param lehrsprache Setzt das Attribut lehrsprache.
	 */
	public void setLehrsprache(Auswahl lehrsprache) {
		this.lehrsprache = lehrsprache;
	}
	
	/**
	 * @param professionMoeglich Setzt das Attribut professionMoeglich.
	 */
	public void setProfessionMoeglich(IdLinkList professionMoeglich) {
		this.professionMoeglich = professionMoeglich;
	}
	/**
	 * @param professionUeblich Setzt das Attribut professionUeblich.
	 */
	public void setProfessionUeblich(IdLinkList professionUeblich) {
		this.professionUeblich = professionUeblich;
	}
	/**
	 * @param schriften Setzt das Attribut schriften.
	 */
	public void setSchriften(Auswahl schriften) {
		this.schriften = schriften;
	}
	/**
	 * @param sprachen Setzt das Attribut sprachen.
	 */
	public void setSprachen(Auswahl sprachen) {
		this.sprachen = sprachen;
	}
	/**
	 * @param zweitsprache Setzt das Attribut zweitsprache.
	 */
	public void setZweitsprache(Auswahl zweitsprache) {
		this.zweitsprache = zweitsprache;
	}
	/**
	 * Die möglichen Varianten zu dieser Kultur
	 * @return Liefert das Attribut varianten.
	 */
	public KulturVariante[] getVarianten() {
		return varianten;
	}
	/**
	 * Die möglichen Varianten zu dieser Kultur
	 * @param varianten Setzt das Attribut varianten.
	 */
	public void setVarianten(KulturVariante[] varianten) {
		this.varianten = varianten;
	}
}
