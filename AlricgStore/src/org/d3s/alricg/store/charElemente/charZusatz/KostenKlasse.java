/*
 * Created 09.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.charElemente.charZusatz;

/**
 * Bildet die Spalten der SKT ab und bietet rudimentäre Operationen.
 * @author V. Strelow
 */
public enum KostenKlasse { 
	A_PLUS ("A+"), 
	A("A"), 
	B("B"), 
	C("C"), 
	D("D"), 
	E("E"), 
	F("F"), 
	G("G"), 
	H("H"); 
	private String value; // Id des Elements
	
	private KostenKlasse(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	/**
	 * Liefert eine KostenKlasse die um einen schritt "nach Rechts" geschoben
	 * ist, also die KostenKlasse und somit die Kosten um eine Spalte erhöht.
	 * @param kostenK Die bisherige Kostenklasse
	 * 		w
	 * @return Die um eine Spalte erhöhte Kostenklasse, bzw. das Maximum, 
	 *  wenn es nicht höher geht.
	 */
	public KostenKlasse plusEineKk() {
		if ( this.ordinal() == (KostenKlasse.values().length - 1) ) {
			return this; // Es geht nicht höher
		}
		
		return KostenKlasse.values()[this.ordinal()+1];
	}
	
	/**
	 * Liefert eine KostenKlasse die um einen schritt "nach Links" geschoben
	 * ist, also die KostenKlasse und somit die Kosten um eine Spalte erniedrigt.
	 * 
	 * @param kostenK Die bisherige Kostenklasse
	 * @return Die um eine Spalte erniedrigte Kostenklasse, bzw. das Minimum, 
	 * 		wenn es nicht niedriger geht.
	 */
	public KostenKlasse minusEineKk() {
		if ( this.ordinal() == 0 ) {
			return this; // Es geht nicht niedriger
		}
		
		return KostenKlasse.values()[this.ordinal()-1];
	}
	
	/**
	 * Prüft ob diese KostenKlasse "teurer" als eine andere ist.
	 * Beispiel: B ist teurer als A
	 * @param kk Die KostenKlasse zum Vergleich
	 * @return true - Diese Kostenklasse ist teuerer als die KostenKlasse "kk", ansonsten false
	 */
	public boolean isTeurerAls(KostenKlasse kk) {
		return this.ordinal() > kk.ordinal();
	}
	
	/**
	 * Prüft ob diese KostenKlasse "billiger" als eine andere ist.
	 * Beispiel: A ist billiger als B
	 * @param kk Die KostenKlasse zum Vergleich
	 * @return true - Diese Kostenklasse ist billiger als die KostenKlasse "kk", ansonsten false
	 */
	public boolean isBilligerAls(KostenKlasse kk) {
		return this.ordinal() < kk.ordinal();
	}
}	

