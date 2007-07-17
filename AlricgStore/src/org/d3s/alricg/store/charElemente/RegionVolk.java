/*
 * Created 20. Januar 2005 / 15:31:48
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package  org.d3s.alricg.store.charElemente;


/**
 * Beschreibt bestimmte Eigenschaften einer Region/Volkes. 
 * 
 * @author V.Strelow
 */
public class RegionVolk extends CharElement {
    private String bindeWortMann; // Wort zwischen Vor- und Nachnamen
    private String bindeWortFrau; // Wort zwischen Vor- und Nachnamen
    private String[] vornamenMann;
    private String[] vornamenFrau;
    private String[] nachnamen;
    private String[] nachnamenEndung; // Wörter die an den Nachnamen gehangen werden
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

 
}
