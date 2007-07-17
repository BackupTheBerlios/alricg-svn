/*
 * Created on 13.10.2005 / 17:44:43
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente.links;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlTransient;


/**
 * <u>Beschreibung:</u><br>
 * Repräsentiert eine Liste von CharElementen, zwischen denen der User wählen kann.
 * Diese Klasse ist für den Modus "LISTE" zuständig.
 * 
 * In "werteListe" steht eine Liste von Werten, wobei jeder Wert einem "link"
 * zugewiesen werden muß. Es werden soviele Links gewählt, wie es Werte in der
 * Liste gibt. (Das Attribut "anzahl" und "max" wird nicht benutzt)
 * 
 * Beispiel: Aus "Gaukler" S. 87 AH
 * 		Als Text: "Abrichten, Falschspiel, Malen/Zeichen oder Musizieren +4, ein anderes +2"
 * 
 * Im Programm:
 * 		werteListe: "4,2"
 *  	optionen: "Abrichten, Falschspiel, Malen/Zeichen, Musizieren"
 *  
 *  
 * Benutzte Methoden: 
 * 	- "getWerteListe()" 
 *  - "setWerteListe()" 
 *  - "getModus()"
 * 
 * @author V. Strelow
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class OptionListe extends AbstractOption implements Option  {
	private int[] werte;
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#getWerteListe()
	 */
	@Override
	@XmlList
	public int[] getWerteListe() {
		return werte;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#setWerteListe(int[])
	 */
	@Override
	public void setWerteListe(int[] werteListe) {
		this.werte = werteListe;

	}
    
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#getMax()
	 */
	@Override
	@XmlTransient
	public int getMax() {
		throw new UnsupportedOperationException(
				"Diese Methode wird im Modus \"Liste\" nicht unterstützt!"
			);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#setMax(int)
	 */
	@Override
	public void setMax(int max) {
		throw new UnsupportedOperationException(
				"Diese Methode wird im Modus \"Liste\" nicht unterstützt!"
			);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#getAnzahl()
	 */
	@Override
	@XmlTransient
	public int getAnzahl() {
		throw new UnsupportedOperationException(
				"Diese Methode wird im Modus \"Liste\" nicht unterstützt!"
			);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#setAnzahl(int)
	 */
	@Override
	public void setAnzahl(int anzahl) {
		throw new UnsupportedOperationException(
				"Diese Methode wird im Modus \"Liste\" nicht unterstützt!"
			);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#getWert()
	 */
	@Override
	@XmlTransient
	public int getWert() {
		throw new UnsupportedOperationException(
				"Diese Methode wird im Modus \"Liste\" nicht unterstützt!"
			);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#setWert(int)
	 */
	@Override
	public void setWert(int wert) {
		throw new UnsupportedOperationException(
				"Diese Methode wird im Modus \"Liste\" nicht unterstützt!"
			);
	}

}
