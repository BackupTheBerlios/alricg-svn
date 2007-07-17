/*
 * Created on 23.01.2005 / 19:37:35
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected an under the GPL licence.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente.charZusatz;

import java.util.Random;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlTransient;

/**
 * <u>Beschreibung:</u><br>
 * Eine Sammlung von Würfel mit variabler Anzahl und Augen der Würfel, sowie
 * einem Wert der fest hinzuaddiert wird. So können würfel-Anweisungen in der
 * art wie "2W6 + 3W20 - 1W3 + 5" realisiert werden. Es kann hier direkt ein
 * ergebniss abgerufen werden!
 * 
 * @author V. Strelow
 */
public class WuerfelSammlung {
	private int festWert;
	private int[] anzahlWuerfel; // Anzahl der Würfel
	private int[] augenWuerfel; // Augenzahl beim gleichen Index wie
									// groesseWuerfel
	private static Random ranGenerator = new Random();;

	/**
	 * Addiert je einen Zufallswert pro Würfel und den Festwert. Dies ergibt
	 * einen gültigen "Würfelwurf".
	 * 
	 * @return Liefert einen gültigen, zufälligen Wert wie eine Würfelergebniss
	 */
	@XmlTransient
	public int getWuerfelWurf() {
		int tmpInt = festWert;
		
		for (int i1 = 0; i1 < anzahlWuerfel.length; i1++) {
			for (int i2 = 0; i2 < anzahlWuerfel[i1]; i2++) {
				tmpInt += 1 + Math.abs(ranGenerator.nextInt())
						% augenWuerfel[i1];
			}
		}

		return tmpInt;
	}

	/**
	 * @return Liefert den minimal möglichen Wert (also wenn alle Würfel eine
	 *         "1" Zeigen)
	 */
	@XmlTransient
	public int getMinWurf() {
		int tmpInt = festWert;

		for (int i1 = 0; i1 < anzahlWuerfel.length; i1++) {
			tmpInt += anzahlWuerfel[i1] * 1;
		}
		return tmpInt;
	}

	/**
	 * @return Liefert den maximal möglichen Wert (also wenn alle Würfel den
	 *         höchsten Wert zeigen)
	 */
	@XmlTransient
	public int getMaxWurf() {
		int tmpInt = festWert;

		for (int i1 = 0; i1 < anzahlWuerfel.length; i1++) {
				tmpInt += anzahlWuerfel[i1] * augenWuerfel[i1];
		}

		return tmpInt;
	}

	/**
	 * Ein fester Wert, der zum Ergebnis gezählt wird (1W6 + -5-)
	 * @return the festWert
	 */
	@XmlAttribute
	public int getFestWert() {
		return festWert;
	}

	/**
	 * @param festWert the festWert to set
	 */
	public void setFestWert(int festWert) {
		this.festWert = festWert;
	}

	/**
	 * Die Anzahl der Würfel (-1-W6 + 5), gleicher Index gehört zusammen
	 * @return the anzahlWuerfel
	 */
	@XmlAttribute
	@XmlList
	public int[] getAnzahlWuerfel() {
		return anzahlWuerfel;
	}

	/**
	 * @param anzahlWuerfel the anzahlWuerfel to set
	 */
	public void setAnzahlWuerfel(int[] anzahlWuerfel) {
		this.anzahlWuerfel = anzahlWuerfel;
	}

	/**
	 * Die Augen der Würfel (1W-6- + 5), gleicher Index gehört zusammen
	 * @return the augenWuerfel
	 */
	@XmlAttribute
	@XmlList
	public int[] getAugenWuerfel() {
		return augenWuerfel;
	}

	/**
	 * @param augenWuerfel the augenWuerfel to set
	 */
	public void setAugenWuerfel(int[] augenWuerfel) {
		this.augenWuerfel = augenWuerfel;
	}


}
