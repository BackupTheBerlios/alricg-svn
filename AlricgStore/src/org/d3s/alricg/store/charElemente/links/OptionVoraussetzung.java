package org.d3s.alricg.store.charElemente.links;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Diese Erweiterung von "OptionAnzahl" wird nur von Voraussetzungen benutzt und
 * gibt an, welche Elemente Voraussetzung sind.
 * 
 * @author Vincent
 */
public class OptionVoraussetzung extends OptionAnzahl {	
	private int abWert; // Ab welchem Wert diese Voraussetzung gilt (0 = immer)
	
	/**
	 * Erst ab diesem Wert ist diese Voraussetzung "gülutig". Ist der aktuelle Wert
	 * niedriger das dieser, so gilt die Option automatisch als Erfüllt.
	 * 
	 * @return Wert ab dem die Voraussetzung gilt
	 */
	@XmlAttribute
	public int getAbWert() {
		return abWert;
	}

	public void setAbWert(int abWert) {
		this.abWert = abWert;
	}
	
}
