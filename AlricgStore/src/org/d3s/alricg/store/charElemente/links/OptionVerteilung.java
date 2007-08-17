/*
 * Created on 13.10.2005 / 17:27:35
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente.links;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;


/**
 * <u>Beschreibung:</u><br> 
 * Der Wert in "wert" kann auf soviele der Links verteilt werden, wie
 * im Attribut "anzahl" angegeben. D.h. erst werden die Links ausgewählt, dann der
 * "wert" beliebig auf die gewählten Links verteilt. Max gibt an, wie viele Punkte
 * maximal auf einen Link verteilt werden darf.
 * Siehe "Elfische Siedlung" S. 37 im AZ( In "max" steht wie hoch die Stufe
 * jeder gewählten option sein darf.)
 *   
 *  Beispiel I: "Elfische Siedlung" S. 37 AZ
 *   	Als Text: "6 Punkte auf 2 Talente aus folgender Liste: Abrichten, Bogenbau, ..."
 *   
 *  Im Programm:
 *    	wert="6" 
 *  	anzahl="2"
 *  	max = "6" (auch "0" für keine keine Begrenzung währe ok)
 *  	optionen="Abrichten, Bogenbau, ..."
 *  
 *  Beispiel II: "Schiffbauer" S. 97 AH
 *  	Als Text: "7 Punkte auf Leberarbeiten, Seiler, Webkunst, ... (je max. +2)"
 * 
 *  Im Programm:
 *    	wert="7" 
 *  	anzahl="0" (für keine Begrenzung)
 *  	max = "2"
 *  	optionen="Leberarbeiten, Seiler, Webkunst, ..."
 *  
 *   
 * Benutzte Methoden:  
 * 	- "getWert()" 
 *  - "setWert()"
 *  - "getMax()"
 *  - "setMax()"
 *  - "getAnzahl()"
 *  - "setAnzahl()"
 *  - "getModus()"
 *  
 * @author V. Strelow
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class OptionVerteilung extends AbstractOption implements Option  {
    private int max;
    private int anzahl;
    private int wert;
    
	@Override
	@XmlAttribute
	public int getMax() {
		return max;
	}

	@Override
	public void setMax(int max) {
		this.max = max;
	}

	@Override
	@XmlAttribute
	public int getAnzahl() {
		return anzahl;
	}

	@Override
	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}

	@Override
	@XmlAttribute
	public int getWert() {
		return wert;
	}

	@Override
	public void setWert(int wert) {
		this.wert = wert;
	}

	
	@Override
	@XmlTransient
	public int[] getWerteListe() {
		throw new UnsupportedOperationException(
				"Diese Methode wird im Modus \"Verteilung\" nicht unterstützt!"
			);
	}

	@Override
	public void setWerteListe(int[] werteListe) {
		throw new UnsupportedOperationException(
				"Diese Methode wird im Modus \"Verteilung\" nicht unterstützt!"
			);
	}
	
	@Override
	public Option copyOption() {
		Option opt = new OptionVerteilung();
		opt.setMax(max);
		opt.setAnzahl(anzahl);
		opt.setWert(wert);
		
		this.copyBasicValues(opt);
		
		return opt;
	}

}
