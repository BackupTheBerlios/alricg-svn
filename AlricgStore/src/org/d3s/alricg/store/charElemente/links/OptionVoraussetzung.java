package org.d3s.alricg.store.charElemente.links;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.d3s.alricg.store.charElemente.CharElement;

/**
 * Diese Erweiterung von "OptionAnzahl" wird nur von Voraussetzungen benutzt und
 * gibt an, welche Elemente Voraussetzung sind.
 * 
 * @author Vincent
 */
public class OptionVoraussetzung extends AbstractOption implements Option {	
	private int abWert = CharElement.KEIN_WERT; // Ab welchem Wert diese Voraussetzung gilt (0 = immer)
	private int anzahl; // Wie viele Links müssen erfüllt sein? (0 = alle)
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#getWerteListe()
	 */
	@Override
	@XmlAttribute
	public int getWert() {
		return abWert;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl#setWerteListe(int[])
	 */
	@Override
	public void setWert(int wert) {
		this.abWert = wert;
	}
	
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

	@Override
	public Option copyOption() {
		Option opt = new OptionVoraussetzung();
		opt.setAnzahl(anzahl);
		opt.setWert(abWert);
		
		this.copyBasicValues(opt);
		
		return opt;
	}
	
}
