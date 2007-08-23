/*
 * Created on 26.04.2005 / 08:59:04
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente.links;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import org.d3s.alricg.store.charElemente.CharElement;


/**
 * <u>Beschreibung:</u><br> 
 * Diese Klasse dient als Bindeglied zwischen zwei (oder mehr) Elementen. Über
 * diesen "Link" können dann Werte, ein Text oder ein weiteres Element angegeben
 * werden (z.B. "Eitelkeit 5" oder "Unfähigkeit Schwimmen" oder "Verpflichtungen
 * gegen Rondrakirche").
 * Der Link bildet die Grundlage für "IDLink" und "HeldenLink".
 *
 * @author V. Strelow
 */
public abstract class Link<ZIEL extends CharElement> {
	public static int KEIN_WERT = CharElement.KEIN_WERT;
	
	private ZIEL ziel; // Das Ziel dieses Links (z.B. eine SF)
    private CharElement zweitZiel; // Ein in Beziehung stehendes Element (z.B. Unfähigkeit "SCHWERT")
    
    private String text; // Ein Text (z.B. Vorurteile gegen "Orks")
    private int wert = KEIN_WERT; // Wert der Beziehung (z.B. Höhenangst 5) / "-100": es gibt keinen Wert
    private boolean leitwert = false; // Für Elfen, verändert kosten / Default = false
	
	/**
	 * Gibt zurück ob das Ziel-Element ein Leitwert ist (wichtig für Elfische-
	 * Weltsicht). 
	 * @return true - Das Ziel-Element ist ein Leitwert, ansonsten false.
	 */
    @XmlAttribute
	public boolean isLeitwert() {
		return leitwert;
	}
	
	/**
	 * Wenn das Ziel-Element mit einem anderen Element verbunden ist, so kann
	 * dieses Link-Element hiermit abgerufen werden. 
	 * Z.B. "Unfähigkeit Schwerter" ("Unfähigkeit" = Ziel, "Schwerter" = link) 
	 * @return Das Link-Element mit dem das Ziel verbunden ist, oder "null"
	 * falls es kein Link-Elemnet gibt. 
	 */
    @XmlAttribute
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
	public CharElement getZweitZiel() {
		return zweitZiel;
	}
	
	/**
	 * Wenn das Ziel-Element mit einem Text verbunden ist, so kann
	 * der Text hiermit abgerufen werden.
	 * Z.B. "Vorurteile gegen Orks" ("Vorurteile gegen" = Ziel, "Orks" = Text)
	 * @return Den anzuzeigenden Text oder "null", falls es keinen Text gibt.
	 */
    @XmlAttribute
	public String getText() {
		return text;
	}
	/**
	 * Wenn das Ziel-Element einen Wert besitzt, so kann der Text Wert 
	 * hiermit abgerufen werden. Liefert stehts den GESAMT-WERT, also
	 * einschließlich 
	 * @return Den Wert der mit der Ziel Id Verbunden ist oder "-100" falls es 
	 * 		keinen wert gibt. .
	 */
    @XmlAttribute
	public int getWert() {
		return wert;
	}
	
	/**
	 * @return true - Das zugehörige CharElement hat einen Wert, sonst false.
	 */
	public boolean hasWert() {
		if (wert == KEIN_WERT) { // -100 meint, das es keinen Wert gibt
			return false;
		}
		return true;
	}
	
	/** 
	 * @return true - Der Link hat Text, ansonsten false
	 */
	public boolean hasText() {
		return (text.length() > 0);
	}
	
	/**
	 * @return true - Der Link hat ein ZweitZiel, ansonsten false
	 */
	public boolean hasZweitZiel() {
		if (zweitZiel == null) { 
			return false;
		}
		return true;
	}
	
	/**
	 * Jeder IdLink hat ein Ziel, dieses wird über die ZielId abgerufen!
	 * @return Liefert das Attribut zielId.
	 */
    @XmlAttribute
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
	public ZIEL getZiel() {
		return ziel;
	}
	
	/**
	 * @param leitwert Setzt das Attribut leitwert.
	 */
	public void setLeitwert(boolean leitwert) {
		this.leitwert = leitwert;
	}
	/**
	 * @param linkId Setzt das Attribut linkId.
	 */
	public void setZweitZiel(CharElement zweitZiel) {
		this.zweitZiel = zweitZiel;
	}
	/**
	 * @param text Setzt das Attribut text.
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @param wert Setzt das Attribut wert ("-100" bedeutet, das es keinen Wert gibt).
	 */
	public void setWert(int wert) {
		this.wert = wert;
	}
	
	/**
	 * @param zielId Setzt das Attribut zielId. (Das Ziel dieses Links, z.B. eine SF)
	 */
	public void setZiel(ZIEL ziel)   {//, CharKomponente zielKomponete) {
		this.ziel = ziel;
	}
	
	/**
	 * Überprüft ob dieser Link "das selbe Element" meint, wie ein andern Link. Dies ist dann
	 * der Fall, wenn zielID, linkID und Text gleich sind. (leitwert und wert müssen
	 * nicht gleich sein).
	 * Bsp: 
	 * Prof mit "Schwerter +2(=Wert)" und Rasse mit "Schwerter +3" >> true
	 * Prof mit "Vorurteil gegen Orks(=Text)" und Rasse mit "Vorurteil gegen Elfen" >> false
	 * Prof mit "Unfähigkeit Dolche(=linkId)" und Rasse mit "Unfähigkeit Dolche" >> true
	 * 
	 * @param link Der zu prüfende link
	 * @return true - Der übergebene link ist das gleiche Element, ansonsten false 
	 */
	public boolean isEqualLink(Link link) {
		if (link == null) return false;
		return isEqualLink(link.getZiel().getId(), link.getText(), link.getZweitZiel());
	}
	
	/**
	 * Überprüft ob die Angaben zu dem Link passen, also "das selbe Element" meint.
	 * 
	 * @param id Die des zu prüfenden Links
	 * @param text Text des zu prüfenden Links (kann null sein)
	 * @param zweitZiel Das ZweitZiel des zu prüfenden Links (kann null sein)
	 * @return true - Die übergebenen Parameter meinen das gleiche Element, 
	 * 					ansonsten false 
	 */
	public boolean isEqualLink(String id, String text, CharElement zweitZiel) {
		// Prüft ob zielId, text und linkId gleich sind, nur dann "true"
		
		// Ziel Prüfen
		if ( !ziel.getId().equals(id) ) {
			return false;
		}
			
		// Text prüfen
		if (text == null && this.text == null) {
			// Noop
		} else if (text == null || this.text == null ) {
			return false;
		} else if (!this.text.equals(text)) {
			return false;
		}
		
		// ZweitZiel Prüfen
		if ( zweitZiel == null && this.zweitZiel == null ) {
			return true; 
		} else if ( zweitZiel == null || this.zweitZiel == null ) {
			return false; 
		} else if ( !zweitZiel.equals(this.zweitZiel) ) {
			return false;
		}
		
		return true;
	}

	@Override
	public String toString() {
		return this.getZiel().toString();
	}
}
