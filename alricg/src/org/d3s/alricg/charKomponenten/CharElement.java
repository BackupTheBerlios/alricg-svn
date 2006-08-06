/*
 * Created 22. Dezember 2004 / 01:07:12
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import java.util.logging.Logger;

import org.d3s.alricg.charKomponenten.links.Voraussetzung;
import org.d3s.alricg.charKomponenten.sonderregeln.principle.Sonderregel;
import org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter;
import org.d3s.alricg.controller.CharKomponente;

/**
 * <b>Beschreibung: </b> <br>
 * Dies ist die super-Klasse für alle Charakter-Elemente. Alle Elemente eines Charakters in Objekte von diesem Typ. TODO
 * Sonderregel sollten mit hilfe von Reflections geladen werden!
 * 
 * @author V.Strelow
 */
abstract public class CharElement implements Comparable<CharElement> {

    /** <code>CharElement</code>'s logger */
    private static final Logger LOG = Logger.getLogger(CharElement.class.getName());

    public static int KEIN_WERT = -100;

    private String id; // Programmweit eindeutige ID

    private String name; // Name des Element

    private String sammelBegriff = ""; // Zur besseren Sortierung

    private String beschreibung = ""; // text, z.B. "Siehe MBK S. 10"

    private RegelAnmerkung regelAnmerkung; // Anmerkungen für den User!

    private SonderregelAdapter sonderregel; // Zu beachtende Sonderreglen

    private Voraussetzung voraussetzung; // Voraussetzungen die erfüllt sein müssen, um diese CharElement hinzuzufügen
    
    /**
     * Soll das Element angezeigt werden? Sinnvoll für und VorNachteile die in der Gui anders verwendet werden, wie z.B.
     * Herausragende Eigenschaft. Diese soll unter den Vorteilen nicht wählbar sein, da sie bei den Eigenschaften
     * berücksichtigt wird. Das Element wird grau dargestellt,will man es wählen erscheint der Text ("Kann nur bei ...
     * gewählt werden.")
     */
    private boolean anzeigen = true; // Default Wert

    private String anzeigenText = "";

    /**
     * @return Die CharKomponente zu der dieses CharElement gehört
     */
    public abstract CharKomponente getCharKomponente();

    public void setAnzeigen(boolean anzeigen) {
        this.anzeigen = anzeigen;
    }

    /**
     * @param anzeigenText Setzt den Text der angezeigt wird, wenn das Element nicht wählbar ist.
     */
    public void setAnzeigenText(String anzeigenText) {
        this.anzeigenText = anzeigenText;
    }

    /**
     * @param regelAnmerkung Eine regelAnmerkung für dieses Element.
     */
    public void setRegelAnmerkung(RegelAnmerkung regelAnmerkung) {
        this.regelAnmerkung = regelAnmerkung;
    }

    /**
     * @param name Der neue Bezeichner für dieses Element
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param beschreibung Die Beschreibung für das Element, meist ein Verweis zu einem Regelbuch
     */
    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    /**
     * @param sammelBegriff Der Sammelbegriff für das Element, dient der Zuordnung mehrer Elemente und der
     *            Struckturierung
     */
    public void setSammelberiff(String sammelBegriff) {
        this.sammelBegriff = sammelBegriff;
    }

    /**
     * @return Der Text, der ausgegeben wird, wenn das Element nicht für die Anzeige bestimmt ist. Gilt es solch einen
     *         Text nicht, dann ""
     */
    public String getAnzeigenText() {
        return anzeigenText;
    }

    /**
     * @param sonderregel Setzt eine Sonderregel für dieses Element.
     */
    public void setSonderregel(SonderregelAdapter sonderRegel) {
        this.sonderregel = sonderRegel;
    }

    /**
     * Setzt die ID des Elements neu. Vor allem Benutzt von abgeleiteten Klassen
     * 
     * @param id Neue Id des Elements
     */
    protected void setId(String id) {
        assert id != null;

        this.id = id;
    }

    /**
     * Wird von allen Konstruktoren aufgerufen
     * 
     * @return Die eindeutige, einmalige ID des Elements
     */
    public String getId() {
        return id;
    }

    /**
     * @return True - Wenn das CharElement als wählbar angezeigt werden soll (Default), ansonsten ist das Element nicht
     *         wählbar und kann an anderer Stelle abgerufen werden (Hinweise liefert der TEXT)
     */
    public boolean isAnzeigen() {
        return anzeigen;
    }

    /**
     * "beschreibung" ist ein allgemeiner Text des CharElements
     * 
     * @return Liefert eine Beschreibung des Elements, meißt einen Verweis auf ein Regelbuch.
     */
    public String getBeschreibung() {
        return beschreibung;
    }

    /**
     * @return Der Name des Elements, der auch angezeigt werden soll.
     */
    public String getName() {
        return name;
    }

    /**
     * "regelAnmerkung" treten auf, wenn ALRICG eine Regel nicht automatisch umsetzen kann. Bei der Wahl des Elements
     * wird dann die Regelanmerkung angezeigt.
     * 
     * @return Liefert die regelAnmerkung. Gibt es keine Anmerkungen, liefert die Methode "null".
     */
    public RegelAnmerkung getRegelAnmerkung() {
        return regelAnmerkung;
    }

	/**
	 * @return Liefert das Attribut voraussetzung.
	 */
	public Voraussetzung getVoraussetzung() {
		return voraussetzung;
	}
	/**
	 * @param voraussetzung Setzt das Attribut voraussetzung.
	 */
	public void setVoraussetzung(Voraussetzung voraussetzung) {
		this.voraussetzung = voraussetzung;
	}
    
    /**
     * Der "sammelBegriff" dient einer besseren Struckturierung, zu können z.B. mehrer Zwergenrassen unter "Zwerge"
     * gesammelt werden.
     * 
     * @return Liefert das Attribut sammelBegriff.
     */
    public String getSammelBegriff() {
        return sammelBegriff;
    }
    
    
    /**
     * @return true Dieses Element verfügt über einen Sammelbegriff, sonst false.
     */
    public boolean hasSammelBegriff() {
        return (!sammelBegriff.equals(""));
    }

    /**
     * Falls dieses Element eine besondere Behandlung durch das Programm benötigt, so besitzt es eine "Sonderregel".
     * Andernfalls liefert die Methode "null". WICHTIG: Es wird bei jedem Aufruf eine neue Instance der Sonderregel
     * geliefert!
     * 
     * @return Liefert ein neues Object der Sonderregel, oder null falls es keine Sonderregel gibt.
     */
    public Sonderregel createSonderregel() {

        // Prüfen ob es überhaupt eine SR gibt
        if (sonderregel == null) {
            return null;
        }

        try {
            // Neue Instanz der Sonderregel erzeugen und zurückliefern
            return sonderregel.getClass().newInstance();

        } catch (InstantiationException e) {
            LOG.severe("Sonderregel konnte nicht instantziert werden!" + "\n " + e.getMessage());

        } catch (IllegalAccessException e) {
            LOG.severe("Sonderregel konnte nicht instantziert werden!" + "\n " + e.getMessage());
        }
        return null;
    }

    /**
     * @return true - Dieses CharElement verfügt über eine Sonderregel, ansonsten false
     */
    public boolean hasSonderregel() {
        // Prüfen ob es überhaupt eine SR gibt
        if (sonderregel == null) {
            return false;
        }
        return true;
    }

    /**
     * Methode überschrieben
     * 
     * @see java.lang.Object#toString()
     * @return Den namen das Elements für die Anzeige in der GUI
     */
    public String toString() {
        return name;
    }

    /**
     * Ermöglicht einen vergleich zwischen CharElementen. Dieser Vergelich wird anhand der ID-Strings durchgeführt. Vor
     * allem wichtig um Arrays mit CharElementen sortieren zu können!
     * 
     * @see java.lang.compareTo()
     * @param ce Das CharElement, mit dem verglichen werden soll
     * @return "a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     *         the specified object."
     */
    public int compareTo(CharElement ce) {
        return id.compareTo(ce.getId());
    }

    public Sonderregel getSonderregel() {
        return sonderregel;
    }

}
