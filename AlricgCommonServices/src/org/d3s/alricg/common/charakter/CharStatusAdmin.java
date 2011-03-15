/*
 * Created 03.06.2008
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.common.charakter;

import java.util.ArrayList;
import java.util.List;

import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Repraesentation;

/**
 * @author Vincent
 *
 */
public class CharStatusAdmin {
	// Nicht Magisch 
	// Viertezauberer / Halbzauberer / Vollmagier => Nur durch Vorteil möglich
	// Halb-Geweiht / Voll-Geweiht / Hochschamane => Vorteil "Geweiht" oder SF "Spätweihe"
	// Repräsentation => SF Repärsentation
	// 
	
	protected Charakter charakter;
	private MagieStatus magieStatus; // Wird beim hinzufügen über sonderregeln gesetzt
	private GeweihtStatus geweihtStatus; // Wird beim hinzufügen über sonderregeln gesetzt
	private List<Repraesentation> repList;
	private List<Gottheit> gottheitList;
	
	public enum MagieStatus {
		nichtMagisch,
		viertezauberer, 
		halbzauberer, 
		vollmagier
	}
	
	public enum GeweihtStatus {
		nichtGeweiht,
		halbgeweiht, 
		vollgeweiht,
		hochschamane
	}
	
	public CharStatusAdmin(Charakter charakter) {
		this.charakter = charakter;
		this.repList = new ArrayList<Repraesentation>();
		this.gottheitList = new ArrayList<Gottheit>();
		magieStatus = MagieStatus.nichtMagisch;
		geweihtStatus  = GeweihtStatus.nichtGeweiht;
	}

	/**
	 * @return the charakter
	 */
	public Charakter getCharakter() {
		return charakter;
	}
	
	public List<Repraesentation> getRepraesentationen() {
		return repList;
	}
	
	public void addRepraesentation(Repraesentation rep) {
		repList.add(rep);
	}
	
	public void removeRepraesentation(Repraesentation rep) {
		repList.remove(rep);
	}

	/**
	 * @return the magieStatus
	 */
	public MagieStatus getMagieStatus() {
		return magieStatus;
	}

	/**
	 * @param magieStatus the magieStatus to set
	 */
	public void setMagieStatus(MagieStatus magieStatus) {
		this.magieStatus = magieStatus;
	}
	
	public boolean isMagieStatus(MagieStatus magieStatus) {
		return this.magieStatus.equals(magieStatus);
	}
	
	public boolean isGeweihtStatus(GeweihtStatus geweihtStatus) {
		return this.getGeweihtStatus().equals(geweihtStatus);
	}

	/**
	 * @return the geweihtStatus
	 */
	public GeweihtStatus getGeweihtStatus() {
		return geweihtStatus;
	}

	/**
	 * @return Alle Gottheiten, denen der Charakter geweiht ist
	 */
	public List<Gottheit> getGottheitenGeweiht() {
		return gottheitList;
	}
	
	public void addGottheit(Gottheit gott) {
		gottheitList.add(gott);
	}
	
	public void removeGottheit(Gottheit gott) {
		gottheitList.remove(gott);
	}
	
	/**
	 * @param geweihtStatus the geweihtStatus to set
	 */
	public void setGeweihtStatus(GeweihtStatus geweihtStatus) {
		this.geweihtStatus = geweihtStatus;
	}
	
}
