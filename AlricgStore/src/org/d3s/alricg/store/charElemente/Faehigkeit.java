/*
 * Created 12.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.charElemente;

import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;

import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;

/**
 * Oberklasse von Talenten und Zaubern, fasst Gemeinsamkeiten zusammen.
 * 
 * @author Vincent
 */
public class Faehigkeit extends CharElement {
	// Die 3 Eigenschaften, auf das eine Probe abgelegt wird
	private Eigenschaft[] dreiEigenschaften = new Eigenschaft[3];
	private KostenKlasse kostenKlasse; // Die Kostenklasse für das Elememt
	
    /**
     * @see Klasse org.d3s.alricg.charKomponenten.Eigenschaften
	 * @return Liefert die drei Eigenschaften, auf die die Probe abgelegt werden muß.
	 */
    @XmlList
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
	public Eigenschaft[] getDreiEigenschaften() {
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
