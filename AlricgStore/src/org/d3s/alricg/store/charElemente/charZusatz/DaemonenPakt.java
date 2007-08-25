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
import org.d3s.alricg.store.charElemente.Werte.MagieMerkmalEnum;
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
	private IdLink<Nachteil>[] verbilligteNachteile;
	private IdLink<Vorteil>[] verbilligteVorteile;
	private IdLink<Sonderfertigkeit>[] verbilligteSonderf;
	private IdLink<Talent>[] verbilligteTalente;
	private IdLink<Zauber>[] verbilligteZauber;
	private MagieMerkmalEnum[] magieMerkmal;
	private IdLink<SchwarzeGabe>[] schwarzeGaben;
	private IdLink<Eigenschaft>[] verbilligteEigenschaften;
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
	public IdLink<Nachteil>[] getVerbilligteNachteile() {
		return verbilligteNachteile;
	}
	/**
	 * @param verbilligteNachteile the verbilligteNachteile to set
	 */
	public void setVerbilligteNachteile(IdLink<Nachteil>[] verbilligteNachteile) {
		this.verbilligteNachteile = verbilligteNachteile;
	}
	/**
	 * @return the verbilligteVorteile
	 */
	public IdLink<Vorteil>[] getVerbilligteVorteile() {
		return verbilligteVorteile;
	}
	/**
	 * @param verbilligteVorteile the verbilligteVorteile to set
	 */
	public void setVerbilligteVorteile(IdLink<Vorteil>[] verbilligteVorteile) {
		this.verbilligteVorteile = verbilligteVorteile;
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
	 * @return the verbilligteTalente
	 */
	public IdLink<Talent>[] getVerbilligteTalente() {
		return verbilligteTalente;
	}
	/**
	 * @param verbilligteTalente the verbilligteTalente to set
	 */
	public void setVerbilligteTalente(IdLink<Talent>[] verbilligteTalente) {
		this.verbilligteTalente = verbilligteTalente;
	}
	/**
	 * @return the verbilligteZauber
	 */
	public IdLink<Zauber>[] getVerbilligteZauber() {
		return verbilligteZauber;
	}
	/**
	 * @param verbilligteZauber the verbilligteZauber to set
	 */
	public void setVerbilligteZauber(IdLink<Zauber>[] verbilligteZauber) {
		this.verbilligteZauber = verbilligteZauber;
	}
	/**
	 * @return the magieMerkmal
	 */
	public MagieMerkmalEnum[] getMagieMerkmal() {
		return magieMerkmal;
	}
	/**
	 * @param magieMerkmal the magieMerkmal to set
	 */
	public void setMagieMerkmal(MagieMerkmalEnum[] magieMerkmal) {
		this.magieMerkmal = magieMerkmal;
	}
	/**
	 * @return the schwarzeGaben
	 */
	public IdLink<SchwarzeGabe>[] getSchwarzeGaben() {
		return schwarzeGaben;
	}
	/**
	 * @param schwarzeGaben the schwarzeGaben to set
	 */
	public void setSchwarzeGaben(IdLink<SchwarzeGabe>[] schwarzeGaben) {
		this.schwarzeGaben = schwarzeGaben;
	}
	/**
	 * @return the verbilligteEigenschaften
	 */
	public IdLink<Eigenschaft>[] getVerbilligteEigenschaften() {
		return verbilligteEigenschaften;
	}
	/**
	 * @param verbilligteEigenschaften the verbilligteEigenschaften to set
	 */
	public void setVerbilligteEigenschaften(
			IdLink<Eigenschaft>[] verbilligteEigenschaften) {
		this.verbilligteEigenschaften = verbilligteEigenschaften;
	}
}
