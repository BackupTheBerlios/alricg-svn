/*
 * Created 14.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.charElemente.charZusatz;

import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.Werte.Gilde;
import org.d3s.alricg.store.charElemente.Werte.MagieMerkmal;

/**
 * <u>Beschreibung:</u><br>
 * Repräsentiert die üblichen Angaben die für eine Magier-Akedemie benötigt
 * werden, wenn diese als Profession gewählt werden kann.
 * 
 * @author V. Strelow
 */
public class MagierAkademie extends CharElement {
	private Gilde gilde;
	private MagieMerkmal merkmale[];
	private boolean zweitStudiumMoeglich = true;
	private boolean drittStudiumMoeglich = false;
	private String anmerkung;
	private Profession herkunft;
	/**
	 * @return the gilde
	 */
	public Gilde getGilde() {
		return gilde;
	}
	/**
	 * @param gilde the gilde to set
	 */
	public void setGilde(Gilde gilde) {
		this.gilde = gilde;
	}
	/**
	 * @return the merkmale
	 */
	public MagieMerkmal[] getMerkmale() {
		return merkmale;
	}
	/**
	 * @param merkmale the merkmale to set
	 */
	public void setMerkmale(MagieMerkmal[] merkmale) {
		this.merkmale = merkmale;
	}
	/**
	 * @return the zweitStudiumMoeglich
	 */
	public boolean isZweitStudiumMoeglich() {
		return zweitStudiumMoeglich;
	}
	/**
	 * @param zweitStudiumMoeglich the zweitStudiumMoeglich to set
	 */
	public void setZweitStudiumMoeglich(boolean zweitStudiumMoeglich) {
		this.zweitStudiumMoeglich = zweitStudiumMoeglich;
	}
	/**
	 * @return the drittStudiumMoeglich
	 */
	public boolean isDrittStudiumMoeglich() {
		return drittStudiumMoeglich;
	}
	/**
	 * @param drittStudiumMoeglich the drittStudiumMoeglich to set
	 */
	public void setDrittStudiumMoeglich(boolean drittStudiumMoeglich) {
		this.drittStudiumMoeglich = drittStudiumMoeglich;
	}
	/**
	 * @return the anmerkung
	 */
	public String getAnmerkung() {
		return anmerkung;
	}
	/**
	 * @param anmerkung the anmerkung to set
	 */
	public void setAnmerkung(String anmerkung) {
		this.anmerkung = anmerkung;
	}
	/**
	 * @return the herkunft
	 */
	public Profession getHerkunft() {
		return herkunft;
	}
	/**
	 * @param herkunft the herkunft to set
	 */
	public void setHerkunft(Profession herkunft) {
		this.herkunft = herkunft;
	}
}
