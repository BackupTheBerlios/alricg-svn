/*
 * Created 26. Dezember 2004 / 22:58:17
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import org.d3s.alricg.charKomponenten.links.IdLinkList;

/**
 * <b>Beschreibung:</b><br>
 * Fast gemeinsamkeiten von Vor- und Nachteilen zusammen und bildet die Grundlage für diese.
 * 
 * @author V.Strelow
 */
public abstract class VorNachteil extends Fertigkeit {
	private int stufenSchritt = 1; // In welchen Schritten gesteigert werden darf
	private int kostenProSchritt = 1; // wieviele GP pro Stufe (+ feste GPKosten)
	private int minStufe = 1;
	private int maxStufe = 1;
	//private IdLinkList verbietetVorteil; 
	//private IdLinkList verbietetNachteil;
	private IdLinkList aendertApSf;
	private IdLinkList aendertGpVorteil;
	private IdLinkList aendertGpNachteil;

    /**
     * @return Liefert das Attribut aendertApSf.
     */
    public IdLinkList getAendertApSf() {
        return aendertApSf;
    }

    /**
     * @return Liefert das Attribut aendertGpNachteil.
     */
    public IdLinkList getAendertGpNachteil() {
        return aendertGpNachteil;
    }

    /**
     * @return Liefert das Attribut aendertGpVorteil.
     */
    public IdLinkList getAendertGpVorteil() {
        return aendertGpVorteil;
    }
    
    
    /**
     * @return Liefert das Attribut maxStufe.
     */
    public int getMaxStufe() {
        return maxStufe;
    }

    public int getStufenSchritt() {
        return stufenSchritt;
    }

    public void setStufenSchritt(int stufenSchritt) {
        this.stufenSchritt = stufenSchritt;
    }
    

    /**
     * @return Liefert das Attribut minStufe.
     */
    public int getMinStufe() {
        return minStufe;
    }

    /**
     * @param aendertApSf Setzt das Attribut aendertApSf.
     */
    public void setAendertApSf(IdLinkList aendertApSf) {
        this.aendertApSf = aendertApSf;
    }

    /**
     * @param aendertGpNachteil Setzt das Attribut aendertGpNachteil.
     */
    public void setAendertGpNachteil(IdLinkList aendertGpNachteil) {
        this.aendertGpNachteil = aendertGpNachteil;
    }

    /**
     * @param aendertGpVorteil Setzt das Attribut aendertGpVorteil.
     */
    public void setAendertGpVorteil(IdLinkList aendertGpVorteil) {
        this.aendertGpVorteil = aendertGpVorteil;
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

    public int getKostenProSchritt() {
        return kostenProSchritt;
    }

    public void setKostenProSchritt(int kostenProSchritt) {
        this.kostenProSchritt = kostenProSchritt;
    }
}
