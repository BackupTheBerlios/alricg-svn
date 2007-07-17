/*
 * Created on 26.01.2005 / 16:48:15
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GPL licence.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente.charZusatz;

import java.util.List;

import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.charElemente.Werte.MagieMerkmal;
import org.d3s.alricg.store.charElemente.links.IdLink;


/**
 * <u>Beschreibung:</u><br> 
 *
 * @author V. Strelow
 */
public class DaemonenPakt extends CharElement {
	private String daemonenName;
	private int paktzuschlag;
	private int kosten;
	private List<IdLink<Nachteil>> verbilligteNachteile;
	private List<IdLink<Vorteil>> verbilligteVorteile;
	private List<IdLink<Sonderfertigkeit>> verbilligteSonderf;
	private List<IdLink<Talent>> verbilligteTalente;
	private List<IdLink<Zauber>> verbilligteZauber;
	private List<MagieMerkmal> magieMerkmal;
	private List<IdLink<Nachteil>> schlechteEigenschaften;
	private List<IdLink<SchwarzeGabe>> schwarzeGaben;
	private List<IdLink<Eigenschaft>> verbilligteEigenschaften;
	/**
	 * @return the daemonenName
	 */
	public String getDaemonenName() {
		return daemonenName;
	}
	/**
	 * @param daemonenName the daemonenName to set
	 */
	public void setDaemonenName(String daemonenName) {
		this.daemonenName = daemonenName;
	}
	/**
	 * @return the paktzuschlag
	 */
	public int getPaktzuschlag() {
		return paktzuschlag;
	}
	/**
	 * @param paktzuschlag the paktzuschlag to set
	 */
	public void setPaktzuschlag(int paktzuschlag) {
		this.paktzuschlag = paktzuschlag;
	}
	/**
	 * @return the kosten
	 */
	public int getKosten() {
		return kosten;
	}
	/**
	 * @param kosten the kosten to set
	 */
	public void setKosten(int kosten) {
		this.kosten = kosten;
	}
	/**
	 * @return the verbilligteNachteile
	 */
	public List<IdLink<Nachteil>> getVerbilligteNachteile() {
		return verbilligteNachteile;
	}
	/**
	 * @param verbilligteNachteile the verbilligteNachteile to set
	 */
	public void setVerbilligteNachteile(List<IdLink<Nachteil>> verbilligteNachteile) {
		this.verbilligteNachteile = verbilligteNachteile;
	}
	/**
	 * @return the verbilligteVorteile
	 */
	public List<IdLink<Vorteil>> getVerbilligteVorteile() {
		return verbilligteVorteile;
	}
	/**
	 * @param verbilligteVorteile the verbilligteVorteile to set
	 */
	public void setVerbilligteVorteile(List<IdLink<Vorteil>> verbilligteVorteile) {
		this.verbilligteVorteile = verbilligteVorteile;
	}
	/**
	 * @return the verbilligteSonderf
	 */
	public List<IdLink<Sonderfertigkeit>> getVerbilligteSonderf() {
		return verbilligteSonderf;
	}
	/**
	 * @param verbilligteSonderf the verbilligteSonderf to set
	 */
	public void setVerbilligteSonderf(
			List<IdLink<Sonderfertigkeit>> verbilligteSonderf) {
		this.verbilligteSonderf = verbilligteSonderf;
	}
	/**
	 * @return the verbilligteTalente
	 */
	public List<IdLink<Talent>> getVerbilligteTalente() {
		return verbilligteTalente;
	}
	/**
	 * @param verbilligteTalente the verbilligteTalente to set
	 */
	public void setVerbilligteTalente(List<IdLink<Talent>> verbilligteTalente) {
		this.verbilligteTalente = verbilligteTalente;
	}
	/**
	 * @return the verbilligteZauber
	 */
	public List<IdLink<Zauber>> getVerbilligteZauber() {
		return verbilligteZauber;
	}
	/**
	 * @param verbilligteZauber the verbilligteZauber to set
	 */
	public void setVerbilligteZauber(List<IdLink<Zauber>> verbilligteZauber) {
		this.verbilligteZauber = verbilligteZauber;
	}
	/**
	 * @return the magieMerkmal
	 */
	public List<MagieMerkmal> getMagieMerkmal() {
		return magieMerkmal;
	}
	/**
	 * @param magieMerkmal the magieMerkmal to set
	 */
	public void setMagieMerkmal(List<MagieMerkmal> magieMerkmal) {
		this.magieMerkmal = magieMerkmal;
	}
	/**
	 * @return the schlechteEigenschaften
	 */
	public List<IdLink<Nachteil>> getSchlechteEigenschaften() {
		return schlechteEigenschaften;
	}
	/**
	 * @param schlechteEigenschaften the schlechteEigenschaften to set
	 */
	public void setSchlechteEigenschaften(
			List<IdLink<Nachteil>> schlechteEigenschaften) {
		this.schlechteEigenschaften = schlechteEigenschaften;
	}
	/**
	 * @return the schwarzeGaben
	 */
	public List<IdLink<SchwarzeGabe>> getSchwarzeGaben() {
		return schwarzeGaben;
	}
	/**
	 * @param schwarzeGaben the schwarzeGaben to set
	 */
	public void setSchwarzeGaben(List<IdLink<SchwarzeGabe>> schwarzeGaben) {
		this.schwarzeGaben = schwarzeGaben;
	}
	/**
	 * @return the verbilligteEigenschaften
	 */
	public List<IdLink<Eigenschaft>> getVerbilligteEigenschaften() {
		return verbilligteEigenschaften;
	}
	/**
	 * @param verbilligteEigenschaften the verbilligteEigenschaften to set
	 */
	public void setVerbilligteEigenschaften(
			List<IdLink<Eigenschaft>> verbilligteEigenschaften) {
		this.verbilligteEigenschaften = verbilligteEigenschaften;
	}
	
	
}
