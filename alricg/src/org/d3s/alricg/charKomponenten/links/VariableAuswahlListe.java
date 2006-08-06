/*
 * Created on 13.10.2005 / 17:44:43
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten.links;


/**
 * <u>Beschreibung:</u><br>
 * Repräsentiert eine Liste von CharElementen, zwischen denen der User wählen kann.
 * Diese Klasse ist für den Modus "LISTE" zuständig.
 * 
 * In "werteListe" steht eine Liste von Werten, wobei jeder Wert einer "option"
 * zugewiesen werden muß. Es werden soviele optionen gewählt, wie es werte in der
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
public class VariableAuswahlListe extends AbstractVariableAuswahl {
	private int[] werte;
	
    protected VariableAuswahlListe(Auswahl auswahl) {
    	super(auswahl);
    }
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#getWerteListe()
	 */
	@Override
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
	
	@Override
	public Modus getModus() {
		return AbstractVariableAuswahl.Modus.LISTE;
	}

}
