/*
 * Created on 13.10.2005 / 17:43:45
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten.links;


/**
 * <u>Beschreibung:</u><br> 
 * In "anzahl" steht eine Zahl, die angibt wieviele der "optionen" gewählt 
 * werden müssen. Jede option kann einen Wert haben (über den "Link").
 * (Das attribut "wert" und "max" wird	nicht benutzt)
 * 
 * Beispiel I:
 *  	Als Text: "Schwerter +2 oder Dolche + 3"
 *  
 *  Im Programm:	
 *  	anzahl="1" 
 *  	optionen="Schwerter(wert=2), Dolche(wert=3)"
 * 
 *  Beispiel II:
 *  	Als Text: "Goldgier 5 o. Schulden 1000 Dukaten oder Ablino"
 *  
 *  Im Programm:	
 *  	anzahl="1" 
 *  	optionen="Goldgier(wert=5), Schulden(wert=1000), Albino" 
 *  
 * @author V. Strelow
 */
public class VariableAuswahlAnzahl extends AbstractVariableAuswahl {
    private int anzahl;
    
    protected VariableAuswahlAnzahl(Auswahl auswahl) {
    	super(auswahl);
    }
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#getAnzahl()
	 */
	@Override
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
	@Override
	public int getWert() {
		throw new UnsupportedOperationException(
				"Diese Methode wird im Modus \"Anzahl\" nicht unterstützt!"
			);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#setWert(int[])
	 */
	@Override
	public void setWert(int wert) {
		throw new UnsupportedOperationException(
				"Diese Methode wird im Modus \"Anzahl\" nicht unterstützt!"
			);
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#getModus()
	 */
	@Override
	public Modus getModus() {
		return AbstractVariableAuswahl.Modus.ANZAHL;
	}

}
