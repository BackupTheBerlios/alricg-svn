/*
 * Created on 26.01.2005 / 16:48:15
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GPL licence.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten.charZusatz;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.links.IdLinkList;
import org.d3s.alricg.controller.CharKomponente;


/**
 * <u>Beschreibung:</u><br> 
 *
 * @author V. Strelow
 */
public class DaemonenPakt extends CharElement {
	private String daemonenName;
	private int paktzuschlag;
	private int kosten;
	private IdLinkList verbilligteNachteile;
	private IdLinkList verbilligteVorteile;
	private IdLinkList verbilligteSonderf;
	private IdLinkList verbilligteTalente;
	private IdLinkList verbilligteZauber;
	private IdLinkList zauberMerkmal;
	private IdLinkList schlechteEigenschaften;
	private IdLinkList schwarzeGaben;
	private IdLinkList verbilligteEigenschaften;
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
	 */
	public CharKomponente getCharKomponente() {
		return CharKomponente.daemonenPakt;
	}
	
	/**
	 * Konstruktur; id beginnt mit "DAE-" für DaemonenPakt
	 * @param id Systemweit eindeutige id
	 */
	public DaemonenPakt(String id) {
		setId(id);
	}
	
	/**
	 * @return Liefert das Attribut daemonenName.
	 */
	public String getDaemonenName() {
		return daemonenName;
	}
	/**
	 * @param daemonenName Setzt das Attribut daemonenName.
	 */
	public void setDaemonenName(String daemonenName) {
		this.daemonenName = daemonenName;
	}
	/**
	 * @return Liefert das Attribut kosten.
	 */
	public int getKosten() {
		return kosten;
	}
	/**
	 * @param kosten Setzt das Attribut kosten.
	 */
	public void setKosten(int kosten) {
		this.kosten = kosten;
	}
	/**
	 * @return Liefert das Attribut paktzuschlag.
	 */
	public int getPaktzuschlag() {
		return paktzuschlag;
	}
	/**
	 * @param paktzuschlag Setzt das Attribut paktzuschlag.
	 */
	public void setPaktzuschlag(int paktzuschlag) {
		this.paktzuschlag = paktzuschlag;
	}
	/**
	 * @return Liefert das Attribut schlechteEigenschaften.
	 */
	public IdLinkList getSchlechteEigenschaften() {
		return schlechteEigenschaften;
	}
	/**
	 * @param schlechteEigenschaften Setzt das Attribut schlechteEigenschaften.
	 */
	public void setSchlechteEigenschaften(IdLinkList schlechteEigenschaften) {
		this.schlechteEigenschaften = schlechteEigenschaften;
	}
	/**
	 * @return Liefert das Attribut schwarzeGaben.
	 */
	public IdLinkList getSchwarzeGaben() {
		return schwarzeGaben;
	}
	/**
	 * @param schwarzeGaben Setzt das Attribut schwarzeGaben.
	 */
	public void setSchwarzeGaben(IdLinkList schwarzeGaben) {
		this.schwarzeGaben = schwarzeGaben;
	}
	/**
	 * @return Liefert das Attribut verbilligteEigenschaften.
	 */
	public IdLinkList getVerbilligteEigenschaften() {
		return verbilligteEigenschaften;
	}
	/**
	 * @param verbilligteEigenschaften Setzt das Attribut verbilligteEigenschaften.
	 */
	public void setVerbilligteEigenschaften(IdLinkList verbilligteEigenschaften) {
		this.verbilligteEigenschaften = verbilligteEigenschaften;
	}
	/**
	 * @return Liefert das Attribut verbilligteNachteile.
	 */
	public IdLinkList getVerbilligteNachteile() {
		return verbilligteNachteile;
	}
	/**
	 * @param verbilligteNachteile Setzt das Attribut verbilligteNachteile.
	 */
	public void setVerbilligteNachteile(IdLinkList verbilligteNachteile) {
		this.verbilligteNachteile = verbilligteNachteile;
	}
	/**
	 * @return Liefert das Attribut verbilligteSonderf.
	 */
	public IdLinkList getVerbilligteSonderf() {
		return verbilligteSonderf;
	}
	/**
	 * @param verbilligteSonderf Setzt das Attribut verbilligteSonderf.
	 */
	public void setVerbilligteSonderf(IdLinkList verbilligteSonderf) {
		this.verbilligteSonderf = verbilligteSonderf;
	}
	/**
	 * @return Liefert das Attribut verbilligteTalente.
	 */
	public IdLinkList getVerbilligteTalente() {
		return verbilligteTalente;
	}
	/**
	 * @param verbilligteTalente Setzt das Attribut verbilligteTalente.
	 */
	public void setVerbilligteTalente(IdLinkList verbilligteTalente) {
		this.verbilligteTalente = verbilligteTalente;
	}
	/**
	 * @return Liefert das Attribut verbilligteVorteile.
	 */
	public IdLinkList getVerbilligteVorteile() {
		return verbilligteVorteile;
	}
	/**
	 * @param verbilligteVorteile Setzt das Attribut verbilligteVorteile.
	 */
	public void setVerbilligteVorteile(IdLinkList verbilligteVorteile) {
		this.verbilligteVorteile = verbilligteVorteile;
	}
	/**
	 * @return Liefert das Attribut verbilligteZauber.
	 */
	public IdLinkList getVerbilligteZauber() {
		return verbilligteZauber;
	}
	/**
	 * @param verbilligteZauber Setzt das Attribut verbilligteZauber.
	 */
	public void setVerbilligteZauber(IdLinkList verbilligteZauber) {
		this.verbilligteZauber = verbilligteZauber;
	}
	/**
	 * @return Liefert das Attribut zauberMerkmal.
	 */
	public IdLinkList getZauberMerkmal() {
		return zauberMerkmal;
	}
	/**
	 * @param zauberMerkmal Setzt das Attribut zauberMerkmal.
	 */
	public void setZauberMerkmal(IdLinkList zauberMerkmal) {
		this.zauberMerkmal = zauberMerkmal;
	}
}
