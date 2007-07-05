/*
 * Created 27. Dezember 2004 / 01:38:24
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;

/**
 * <b>Beschreibung:</b><br>
 * Oberklasse von Talenten und Zaubern, faßt Gemeinsamkeiten zusammen.
 * @author V.Strelow
 */
public abstract class Faehigkeit extends CharElement {
	// Die 3 Eigenschaften, auf das eine Probe abgelegt wird
	private Eigenschaft[] dreiEigenschaften = new Eigenschaft[3];
	private KostenKlasse kostenKlasse; // Die Kostenklasse für das Elememt
	
    /**
     * @see Klasse org.d3s.alricg.charKomponenten.Eigenschaften
	 * @return Liefert die drei Eigenschaften, auf die die Probe abgelegt werden muß.
	 */
	public Eigenschaft[] get3Eigenschaften() {
		return dreiEigenschaften;
	}
	
	public String get3EigenschaftenString() {
		return dreiEigenschaften[0].getEigenschaftEnum().getAbk()
				+ "/" 
				+ dreiEigenschaften[1].getEigenschaftEnum().getAbk()
				+ "/"
				+ dreiEigenschaften[2].getEigenschaftEnum().getAbk();
	}
	
    /**
	 * @return Liefert die Kosten-Klasse nach der SKT.
	 */
	public KostenKlasse getKostenKlasse() {
		return kostenKlasse;
	}

	/**
	 * @param dreiEigenschaften Setzt das Attribut dreiEigenschaften.
	 */
	public void setDreiEigenschaften(Eigenschaft[] dreiEigenschaften) {
		this.dreiEigenschaften = dreiEigenschaften;
	}
	/**
	 * @param kostenKlasse Setzt das Attribut kostenKlasse.
	 */
	public void setKostenKlasse(KostenKlasse kostenKlasse) {
		this.kostenKlasse = kostenKlasse;
	}
}
