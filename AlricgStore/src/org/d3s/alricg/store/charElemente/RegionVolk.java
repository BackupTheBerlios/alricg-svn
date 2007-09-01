/*
 * Created 20. Januar 2005 / 15:31:48
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package  org.d3s.alricg.store.charElemente;

import javax.xml.bind.annotation.XmlEnum;


/**
 * Beschreibt bestimmte Eigenschaften einer Region/Volkes. 
 * 
 * @author V.Strelow
 */
public class RegionVolk extends CharElement {
	
	@XmlEnum
	public enum RegionVolkArt {
		menschlich("Menschlich"), 
		nichtMenschlich("Nicht Menschlich");
		
		private String name; 
		
		private RegionVolkArt(String name) {
			this.name = name;
		}
		
		public String toString() {
			return name;
		}
	}
	
	private String abk; // Abk. für die Region/Volk
	private RegionVolkArt art;
	
    private String bindeWortMann; // Wort zwischen Vor- und Nachnamen ("ibn")
    private String bindeWortFrau; // Wort zwischen Vor- und Nachnamen ("saba")
    private String[] vornamenMann;
    private String[] vornamenFrau;
    private String[] nachnamen;
    private String[] nachnamenEndung; // Wörter die an den Nachnamen gehangen werden
    private String endWortMann; // Wort das hinten an den Nachamen gehängt wird ("son")
    private String endWortFrau; // Wort das hinten an den Nachamen gehängt wird ("dotter")
    
    private boolean vornamenSindNachnamen; // Z.B. bei Thorwallern
    
    /**
	 * @return the bindeWortMann
	 */
	public String getBindeWortMann() {
		return bindeWortMann;
	}
	/**
	 * @param bindeWortMann the bindeWortMann to set
	 */
	public void setBindeWortMann(String bindeWortMann) {
		this.bindeWortMann = bindeWortMann;
	}
	/**
	 * @return the bindeWortFrau
	 */
	public String getBindeWortFrau() {
		return bindeWortFrau;
	}
	/**
	 * @param bindeWortFrau the bindeWortFrau to set
	 */
	public void setBindeWortFrau(String bindeWortFrau) {
		this.bindeWortFrau = bindeWortFrau;
	}
	/**
	 * @return the vornamenMann
	 */
	public String[] getVornamenMann() {
		return vornamenMann;
	}
	/**
	 * @param vornamenMann the vornamenMann to set
	 */
	public void setVornamenMann(String[] vornamenMann) {
		this.vornamenMann = vornamenMann;
	}
	/**
	 * @return the vornamenFrau
	 */
	public String[] getVornamenFrau() {
		return vornamenFrau;
	}
	/**
	 * @param vornamenFrau the vornamenFrau to set
	 */
	public void setVornamenFrau(String[] vornamenFrau) {
		this.vornamenFrau = vornamenFrau;
	}
	/**
	 * @return the nachnamen
	 */
	public String[] getNachnamen() {
		return nachnamen;
	}
	/**
	 * @param nachnamen the nachnamen to set
	 */
	public void setNachnamen(String[] nachnamen) {
		this.nachnamen = nachnamen;
	}
	/**
	 * @return the nachnamenEndung
	 */
	public String[] getNachnamenEndung() {
		return nachnamenEndung;
	}
	/**
	 * @param nachnamenEndung the nachnamenEndung to set
	 */
	public void setNachnamenEndung(String[] nachnamenEndung) {
		this.nachnamenEndung = nachnamenEndung;
	}
	/**
	 * @return the abk
	 */
	public String getAbk() {
		return abk;
	}
	/**
	 * @param abk the abk to set
	 */
	public void setAbk(String abk) {
		this.abk = abk;
	}
	/**
	 * @return the endWortMann
	 */
	public String getEndWortMann() {
		return endWortMann;
	}
	/**
	 * @param endWortMann the endWortMann to set
	 */
	public void setEndWortMann(String endWortMann) {
		this.endWortMann = endWortMann;
	}
	/**
	 * @return the endWortFrau
	 */
	public String getEndWortFrau() {
		return endWortFrau;
	}
	/**
	 * @param endWortFrau the endWortFrau to set
	 */
	public void setEndWortFrau(String endWortFrau) {
		this.endWortFrau = endWortFrau;
	}
	/**
	 * @return the vornamenSindNachnamen
	 */
	public boolean isVornamenSindNachnamen() {
		return vornamenSindNachnamen;
	}
	/**
	 * @param vornamenSindNachnamen the vornamenSindNachnamen to set
	 */
	public void setVornamenSindNachnamen(boolean vornamenSindNachnamen) {
		this.vornamenSindNachnamen = vornamenSindNachnamen;
	}
	/**
	 * @return the art
	 */
	public RegionVolkArt getArt() {
		return art;
	}
	/**
	 * @param art the art to set
	 */
	public void setArt(RegionVolkArt art) {
		this.art = art;
	}

}
