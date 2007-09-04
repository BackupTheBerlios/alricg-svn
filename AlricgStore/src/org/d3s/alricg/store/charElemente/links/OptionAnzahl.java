/*
 * Created on 13.10.2005 / 17:43:45
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente.links;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;


/**
 * <u>Beschreibung:</u><br> 
 * In "anzahl" steht eine Zahl, die angibt wieviele der "Links" gewählt 
 * werden müssen. Jede option kann einen Wert haben (über den "Link").
 * (Das attribut "wert" und "max" wird	nicht benutzt)
 * 
 * Beispiel I:
 *  	Als Text: "Schwerter +2 oder Dolche + 3"
 *  
 *  Im Programm:	
 *  	anzahl="1" 
 *  	links="Schwerter(wert=2), Dolche(wert=3)"
 * 
 *  Beispiel II:
 *  	Als Text: "Goldgier 5 o. Schulden 1000 Dukaten oder Ablino"
 *  
 *  Im Programm:	
 *  	anzahl="1" 
 *  	links="Goldgier(wert=5), Schulden(wert=1000), Albino" 
 * 
 *  Beispiel III:
 *  	Als Text: "Zwei wählen aus Dolche +2, Schwerter +2 und Degen +3"
 *  
 *  Im Programm:	
 *  	anzahl="2" 
 *  	links="Dolche(wert=2), Schwerter(wert=2), Degen(wert=3)"  
 * @author V. Strelow
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class OptionAnzahl extends AbstractOption implements Option {
    private int anzahl;
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#getAnzahl()
	 */
	@Override
	@XmlAttribute
	public int getAnzahl() {
		return anzahl;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#setAnzahl(int)
	 */
	@Override
	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}
    
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#getMax()
	 */
	@Override
	@XmlTransient
	public int getMax() {
		throw new UnsupportedOperationException(
				"Diese Methode wird im Modus \"Anzahl\" nicht unterstützt!"
			);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#setMax(int)
	 */
	@Override
	public void setMax(int max) {
		throw new UnsupportedOperationException(
				"Diese Methode wird im Modus \"Anzahl\" nicht unterstützt!"
			);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#getWerteListe()
	 */
	@Override
	@XmlTransient
	public int[] getWerteListe() {
		throw new UnsupportedOperationException(
				"Diese Methode wird im Modus \"Anzahl\" nicht unterstützt!"
			);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#setWerteListe(int[])
	 */
	@Override
	public void setWerteListe(int[] werteListe) {
		throw new UnsupportedOperationException(
				"Diese Methode wird im Modus \"Anzahl\" nicht unterstützt!"
			);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#getWert()
	 */
	@XmlTransient
	public int getWert() {
		throw new UnsupportedOperationException(
				"Diese Methode wird im Modus \"Anzahl\" nicht unterstützt!"
			);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#setWert(int[])
	 */
	public void setWert(int wert) {
		throw new UnsupportedOperationException(
				"Diese Methode wird im Modus \"Anzahl\" nicht unterstützt!"
			);
	}

	@Override
	public Option copyOption() {
		Option opt = new OptionAnzahl();
		opt.setAnzahl(anzahl);
		
		this.copyBasicValues(opt);
		
		return opt;
	}

}
