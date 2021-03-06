/*
 * Created 26. Dezember 2004 / 22:58:17
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente;

import javax.xml.bind.annotation.XmlSchemaType;

import org.d3s.alricg.store.charElemente.links.IdLink;


/**
 * Fast gemeinsamkeiten von Vor- und Nachteilen zusammen und bildet die Grundlage f�r diese.
 * 
 * --> Die Kosten�nderungen werde nun komplett �ber Sonderregeln gehandhabt
 * Die Eingenschaften "�ndert..." geben an, dass die Wahl dieses VorNachteils die 
 * Kosten anderer VorNachteile oder Sonderfertigkeiten beeinflussen kann.
 * Der Link gibt als Ziel das zu �ndernde CharElement an.   
 * 
 * @author V.Strelow
 */
public abstract class VorNachteil extends Fertigkeit {
	//private int stufenSchritt = 0; // In welchen Schritten gesteigert werden darf
	private double kostenProStufe = 0; // wieviele GP pro Stufe (+ feste GPKosten)
	private int minStufe = 0;
	private int maxStufe = 0;

	/*
	private IdLink<Sonderfertigkeit>[] aendertApSf; // Die Kosten�nderung hier muss als "AP" angegeben werden!
	private IdLink<Nachteil>[] aendertGpNachteil;
	private IdLink<Vorteil>[] aendertGpVorteil;
*/
	
    /**
     * @return Liefert das Attribut maxStufe.
     */
    @XmlSchemaType(name = "nonNegativeInteger")
    public int getMaxStufe() {
        return maxStufe;
    }

    /**
     * @return Liefert das Attribut minStufe.
     */
    @XmlSchemaType(name = "nonNegativeInteger")
    public int getMinStufe() {
        return minStufe;
    }

    /**
     * @param maxStufe Setzt das Attribut maxStufe.
     */
    public void setMaxStufe(int maxStufe) {
        this.maxStufe = maxStufe;
    }

    /**
     * @param minStufe Setzt das Attribut minStufe.
     */
    public void setMinStufe(int minStufe) {
        this.minStufe = minStufe;
    }

	/**
	 * @return the kostenProStufe
	 */
	@XmlSchemaType(name = "nonNegativeInteger")
	public double getKostenProStufe() {
		return kostenProStufe;
	}

	/**
	 * @param kostenProStufe the kostenProStufe to set
	 */
	public void setKostenProStufe(double kostenProSchritt) {
		this.kostenProStufe = kostenProSchritt;
	}
	
    /*
     * @see org.d3s.alricg.store.charElemente.VorNachteil
	 * @return the aendertGpNachteil
	 *
	public IdLink<Nachteil>[] getAendertGpNachteil() {
		return aendertGpNachteil;
	}

	/*
	 * @see org.d3s.alricg.store.charElemente.VorNachteil
	 * @param aendertGpNachteil the aendertGpNachteil to set
	 *
	public void setAendertGpNachteil(IdLink<Nachteil>[] aendertGpNachteil) {
		this.aendertGpNachteil = aendertGpNachteil;
	}

	/*
	 * @see org.d3s.alricg.store.charElemente.VorNachteil
	 * @return the aendertGpVorteil
	 *
	public IdLink<Vorteil>[] getAendertGpVorteil() {
		return aendertGpVorteil;
	}

	/*
	 * @see org.d3s.alricg.store.charElemente.VorNachteil
	 * @param aendertGpVorteil the aendertGpVorteil to set
	 *
	public void setAendertGpVorteil(IdLink<Vorteil>[] aendertGpVorteil) {
		this.aendertGpVorteil = aendertGpVorteil;
	}
	
	/*
     * @see org.d3s.alricg.store.charElemente.VorNachteil
     * @param aendertApSf Setzt das Attribut aendertApSf.
     *
    public void setAendertApSf(IdLink<Sonderfertigkeit>[] aendertApSf) {
        this.aendertApSf = aendertApSf;
    }

	/*
     * @see org.d3s.alricg.store.charElemente.VorNachteil
     * @return Liefert das Attribut aendertApSf.
     *
    public IdLink<Sonderfertigkeit>[] getAendertApSf() {
        return aendertApSf;
    }*/
    
	/*
	 * @return the stufenSchritt
	 *
    @XmlSchemaType(name = "nonNegativeInteger")
	public int getStufenSchritt() {
		return stufenSchritt;
	}*/

	/*
	 * @param stufenSchritt the stufenSchritt to set
	 *
	public void setStufenSchritt(int stufenSchritt) {
		this.stufenSchritt = stufenSchritt;
	}*/


}
