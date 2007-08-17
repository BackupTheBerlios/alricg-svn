/*
 * Created 22. Dezember 2004 / 14:23:17
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente.links;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import org.d3s.alricg.store.charElemente.CharElement;


/**
 * <b>Beschreibung:</b><br>
 * Diese Klasse dient als Bindeglied zwischen zwei (oder mehr) Elementen. Über diesen "Link" können dann Werte, ein Text
 * oder ein weiteres Element angegeben werden (z.B. "Eitelkeit 5" oder "Unfähigkeit Schwimmen" oder "Verpflichtungen
 * gegen Orden"). Ein IdLink hat typischer weise eine Herkunft(Profession, Kultur, Rasse) als Quelle, es kann aber auch
 * eine anderes CharElement sein, z.B. ein Vorteil (Der Vorteil x schließ Voteil y aus).
 * 
 * @author V.Strelow
 */
public class IdLink<ZIEL extends CharElement> extends Link<ZIEL> {
    private CharElement quelle; // Falls dieser IdLink NICHT durch eine Auswahl entstand

    /*
     * Falls dieser Link im Helden "gespeichtert" ist, wird hier eine Verbindung zwischen Held und Links gehalten
     * private HeldenLink heldenLink;
     */
    
    public IdLink() {
    	// Konstruktor für JaxB
    }
    
    public IdLink(CharElement quelle, ZIEL ziel, CharElement zweitZiel, int wert, String text) {
    	this.setQuelle(quelle);
    	this.setZiel(ziel);
    	this.setZweitZiel(zweitZiel);
    	this.setWert(wert);
    	this.setText(text);
    }
    
    
    /**
     * Liefert das CharElement, welches diesen IdLink "besitzt"
     * 
     * @return Das CharElement, zu dem der IdLink gehört
     */
    @XmlAttribute
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
	public CharElement getQuelle() {
		return quelle;
	}
	/**
	 * @param quelle Von wo der Link "ausgeht". Typischer weise eine Herkunft.
	 */
	public void setQuelle(CharElement quelle) {
		this.quelle = quelle;
	}

	/**
	 * Erstellt eine Kopie des Links
	 * @return Eine Kopie: Anderes Object, gleicht werte!
	 */
    public IdLink copyLink() {
    	IdLink copy =  new IdLink(this.quelle, getZiel(), getZweitZiel(), getWert(), getText());
    	copy.setLeitwert(isLeitwert());
    	return copy;
    }
    
}
