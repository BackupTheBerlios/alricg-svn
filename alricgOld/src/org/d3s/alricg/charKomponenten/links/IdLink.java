/*
 * Created 22. Dezember 2004 / 14:23:17
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten.links;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.store.FactoryFinder;

/**
 * <b>Beschreibung:</b><br>
 * Diese Klasse dient als Bindeglied zwischen zwei (oder mehr) Elementen. Über diesen "Link" können dann Werte, ein Text
 * oder ein weiteres Element angegeben werden (z.B. "Eitelkeit 5" oder "Unfähigkeit Schwimmen" oder "Verpflichtungen
 * gegen Orden"). Ein IdLink hat typischer weise eine Herkunft(Profession, Kultur, Rasse) als Quelle, es kann aber auch
 * eine anderes CharElement sein, z.B. ein Vorteil (Der Vorteil x schließ Voteil y aus).
 * 
 * @author V.Strelow
 */
public class IdLink extends Link {

    private Auswahl auswahl; // Falls dieser IdLink durch eine Auswahl entstand
    private CharElement quelle; // Falls dieser IdLink NICHT durch eine Auswahl entstand

    /*
     * Falls dieser Link im Helden "gespeichtert" ist, wird hier eine Verbindung zwischen Held und Links gehalten
     * private HeldenLink heldenLink;
     */

    /**
     * Konstruktor, initialisiert das Objekt. Nach der Erstellung sollte mit "loadFromId()" oder "loadXmlElement()" der
     * IdLink mit Inhalt gefüllt werden.
     * 
     * @param quelle Von wo der Link "ausgeht". Typischer weise eine Herkunft.
     * @param auswahl Falls der Link zu einer Auswahl gehört, ansonsten "null".
     */
    public IdLink(CharElement quelle, Auswahl auswahl) {
        this.quelle = quelle;
        this.auswahl = auswahl;
    }

    /**
     * Liefert das CharElement, welches diesen IdLink "besitzt"
     * 
     * @return Das CharElement, zu dem der IdLink gehört
     */
    public CharElement getQuellElement() {
        return quelle;
    }

    /**
     * Liefert das CharElement, welches diesen IdLink "besitzt"
     * 
     * @return Das CharElement, zu dem der IdLink gehört
     */
    public Auswahl getQuellAuswahl() {
        return auswahl;
    }

    /*
     * Wenn ein Held einen neuen Wert über ein CharElement erhält, so wird dies hiermit auch dem IdLink mitgeteilt und
     * mit dem Held verbunden. @param link Die Verbindung zum Helden public void setHeldenLink(HeldenLink link) {
     * heldenLink = link; }
     */

    /*
     * @param link @return public HeldenLink getHeldenLink() { return heldenLink; }* /** Initiiert einen simplen IdLink,
     * indem nur die zielId übergeben wird. @param id Die zielID des IdLinks.
     */
    public void loadFromId(String id) {
        this.setZiel(FactoryFinder.find().getData().getCharElement(id));
    }
}
