/*
 * Created on 13.10.2005 / 17:22:09
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten.links;

import java.util.logging.Logger;

/**
 * <u>Beschreibung:</u><br> 
 * Wenn der Benutzer zwischen mehrern Optionen auswählen kann, wird eine 
 * Opbjekt dieser Klasse zur repräsentation benutzt.
 * @author V. Strelow
 */
public abstract class AbstractVariableAuswahl {
    /** <code>AbstractVariableAuswahl</code>'s logger */
    private static final Logger LOG = Logger.getLogger(AbstractVariableAuswahl.class.getName());
	
    private IdLink[] optionen;
    private IdLink[][] optionsGruppe;
    private Auswahl auswahl;
    
	/**
	 * Gibt den Modus der Auswahl an. Der Modus ist nur für VariableAuswahlen wichtigt
	 * und gibt an in welcher Art die Optionen ausgewählt werden können. 
	 *
	 * LISTE - In "werteListe" steht eine Liste von Werten, wobei jeder Wert einer "option"
	 *   zugewiesen werden muß. Es werden soviele optionen gewählt, wie es werte gibt. 
	 *  (Das Attribut "anzahl" und "max" wird nicht benutzt)
	 *  
	 * ANZAHL - In "anzahl" steht eine Zahl, die angibt wieviele der "optionen" gewählt 
	 *  werden müssen. Jede option kann einen Wert haben (über den "idLinkTyp").
	 *  (Das attribut "wert" und "max" wird	nicht benutzt)
	 *  
	 * VERTEILUNG - Der Wert in "wert" kann auf soviele der Optionen verteilt werden, wie
	 *  im Attribut "anzahl" angegeben. D.h. erst werden die Optionen ausgewählt, dann der
	 *  "wert" beliebig auf die gewählten optionen verteilt. 
	 *  (Siehe "Elfische Siedlung" S. 37 im AZ( In "max" steht wie hoch die Stufe
	 *   jeder gewählten option sein darf.
	 *   
	 *   Beispiele sind bei den Klassen der Modi angegeben!
	 */
	public enum Modus {
		LISTE("LISTE"), ANZAHL("ANZAHL"), VERTEILUNG("VERTEILUNG");
		
		private String value;
		
		/* Konstruktor
		 */
		private Modus(String value) {
			this.value = value;
		}
		
		/**
		 * @return Den Bezeichner, wie er im XML Dokument auftaucht
		 */
		public String getValue() {
			return value;
		}
	}
    
    /**
     * Konstruktor
     * @param auswahl Die Auswahl, zu der diese VariableAuswahl gehört
     */
	protected AbstractVariableAuswahl(Auswahl auswahl) {
		this.auswahl = auswahl;
	}
	
	/**
	 * Mit Hilfe dieser Methode werden instancen einer Variablen Auswahl erzeugt
	 * 
	 * @param auswahl Die Auswahl, zu der die VaribaleAuswahl gehören soll
	 * @param modus Der XML-Wert des Modus dieser Variablen Auswahl
	 * @return Die erzeugte VaraibleAuswahl im angegebenen Modus
	 */
	public static AbstractVariableAuswahl createAuswahl(Auswahl auswahl, String modus) {
		
    	if ( modus.equals(Modus.ANZAHL.getValue()) ) {
    		return new VariableAuswahlAnzahl(auswahl);
    		
    	} else if (modus.equals(Modus.LISTE.getValue()) ) {
    		return new VariableAuswahlListe(auswahl);
    		
    	} else if (modus.equals(Modus.VERTEILUNG.getValue()) ) {
    		return new VariableAuswahlVerteilung(auswahl);
    		
    	} else {
    		LOG.severe("Modus nicht gefunden!");
    		return null;
    	}
	}
	
	/**
	 * Mit Hilfe dieser Methode werden instancen einer Variablen Auswahl erzeugt
	 * 
	 * @param auswahl Die Auswahl, zu der die VaribaleAuswahl gehören soll
	 * @param modus Der Modus dieser Variablen Auswahl
	 * @return Die erzeugte VaraibleAuswahl im angegebenen Modus
	 */
	public static AbstractVariableAuswahl createAuswahl(Auswahl auswahl, Modus modus) {
		
    	if ( modus.equals(Modus.ANZAHL) ) {
    		return new VariableAuswahlAnzahl(auswahl);
    		
    	} else if (modus.equals(Modus.LISTE) ) {
    		return new VariableAuswahlListe(auswahl);
    		
    	} else if (modus.equals(Modus.VERTEILUNG) ) {
    		return new VariableAuswahlVerteilung(auswahl);
    		
    	} else {
    		LOG.severe("Modus nicht gefunden!");
    		return null;
    	}
	}
	
	/**
	 * Nur im Modus "VERTEILUNG" wichtig!
	 * Gibt die maximal Stufe der gewählten CharElemente an. "0" ist hier
	 * gleichbedeutent mit keiner Begrenzung
	 * 
	 * @return Liefert das Attribut max.
	 */
	public abstract int getMax();

	/**
	 * Nur im Modus "VERTEILUNG" wichtig!
	 * Gibt die maximal Stufe der gewählten CharElemente an. "0" ist hier
	 * gleichbedeutent mit keiner Begrenzung
	 * 
	 * @param max Setzt das Attribut max.
	 */
	public abstract void setMax(int max);

	
	/**
	 * Nur im Modus "ANZAHL" & "VERTEILUNG" wichtig!
	 * 
	 * ANZAHL - In "anzahl" steht eine Zahl, die angibt wieviele der "optionen" gewählt
	 *  werden müssen. Jede option kann einen Wert haben (über den "idLinkTyp").
	 * VERTEILUNG - Der Wert in "wert" kann auf soviele der Optionen verteilt werden,
	 *  wie im Attribut "anzahl" angegeben. D.h. erst werden die Optionen ausgewählt, 
	 *  dann der "wert" beliebig auf die gewählten optionen verteilt. (Siehe "Elfische 
	 *  Siedlung" S. 37 im AZ) In "max" steht wie hoch die Stufe jeder 
	 *  gewählten option sein darf 
	 * 
	 * Ist "anzahl = 0", so gibt es keine Begrenzung!
	 * @return Liefert das Attribut anzahl.
	 */
	public abstract int getAnzahl();
	
	/**
	 * Nur im Modus "ANZAHL" & "VERTEILUNG" wichtig!
	 * 
	 * ANZAHL - In "anzahl" steht eine Zahl, die angibt wieviele der "optionen" gewählt
	 *  werden müssen. Jede option kann einen Wert haben (über den "idLinkTyp").
	 * VERTEILUNG - Der Wert in "wert" kann auf soviele der Optionen verteilt werden,
	 *  wie im Attribut "anzahl" angegeben. D.h. erst werden die Optionen ausgewählt, 
	 *  dann der "wert" beliebig auf die gewählten optionen verteilt. (Siehe "Elfische 
	 *  Siedlung" S. 37 im AZ) In "max" steht wie hoch die Stufe jeder 
	 *  gewählten option sein darf 
	 *  
	 * Ist "anzahl = 0", so gibt es keine Begrenzung!
	 * 
	 * @param anzahl Setzt das Attribut anzahl.
	 */
	public abstract void setAnzahl(int anzahl);
	
	/**
	 * Nur für "LISTE" wichtig!
	 * In "werteListe "steht eine Liste von Werten, wobei jeder Wert einer
	 * "option" zugewiesen werden muß. Es werden soviele optionen gewählt, wie
	 * 	es werte gibt. 
	 * 
	 * @return Liefert das Attribut werte.
	 */
	public abstract int[] getWerteListe();
	
	/**
	 * Nur für "LISTE" wichtig!
	 * In "wertListe"steht eine Liste von Werten, wobei jeder Wert einer
	 * "option" zugewiesen werden muß. Es werden soviele optionen gewählt, wie
	 *  es werte gibt.
	 * 
	 * @param werte Setzt das Attribut werte.
	 */
	public abstract void setWerteListe(int[] werteListe);
	
	/**
	 * Nur für "VERTEILUNG" wichtig!
	 * 	Der Wert in "wert" kann auf soviele der Optionen verteilt werden,
	 *  wie im Attribut "anzahl" angegeben. D.h. erst werden die Optionen ausgewählt, 
	 *  dann der "wert" beliebig auf die gewählten optionen verteilt. (Siehe 
	 *  "Elfische Siedlung" S. 37 im AZ)
	 *  In "max" steht wie hoch die Stufe jeder gewählten option sein darf
	 *  
	 * @return
	 */
	public abstract int getWert();
	
	/**
	 * Nur für "VERTEILUNG" wichtig!
	 * 	Der Wert in "wert" kann auf soviele der Optionen verteilt werden,
	 *  wie im Attribut "anzahl" angegeben. D.h. erst werden die Optionen ausgewählt, 
	 *  dann der "wert" beliebig auf die gewählten optionen verteilt. (Siehe 
	 *  "Elfische Siedlung" S. 37 im AZ)
	 * 	In "max" steht wie hoch die Stufe jeder gewählten option sein darf
	 * 
	 * @param wert
	 */
	public abstract void setWert(int wert);

	/**
	 * @return Liefert das Attribut modus.
	 */
	public abstract Modus getModus();
	
	/**
	 * @return Liefert die Auswahl, zu der diese VariableAuswahl gehört
	 */
	public Auswahl getAuswahl() {
		return auswahl;
	}
	/**
	 * @param auswahl Die Auswahl, zu der diese VariableAuswahl gehört
	 */
	public void setAuswahl(Auswahl auswahl) {
		this.auswahl = auswahl;
	}
	
	/**
	 * Eine Option ist eine möglichkeit die gewählt werden kann. Jede
	 * Option ist ein Link und somit auch ein CharElement.
	 * Diese Methode liefert ein Array aller möglichen Optionen, jede 
	 * Arraystelle ist eine Option. Wie die Optionen ausgewählt werden,
	 * wird durch den Modus angegeben.
	 * 
	 * Beispiel:
	 * - "Schwerter +5 oder Dolche +5" (modus ANZAHL)
	 * 		Optionen sind:
	 * 			Link mit Ziel "Schwerter" und Wert "5"
	 * 			Link mit Ziel "Dolche" und Wert "5"
	 * - "3 Punkte beliebig auf Schwerter und Dolche" (Modus VERTEILUNG)
	 * 		Optionen sind:
	 * 			Link mit Ziel "Schwerter"
	 * 			Link mit Ziel "Dolche"
	 * 			(Werte werden in Auswahl angegeben, nicht in dem Link)
	 * 
	 * @see getOptionsGruppe()
	 * @return Liefert ein Array aller (ausgenommen Optionsgruppen) wählbareren Optionen.
	 */
	public IdLink[] getOptionen() {
		return optionen;
	}
	
	/**
	 * @return true - Es gibt eine Liste von wählbaren "einstelligen"
	 * 	Optionen, ansonsten false (es gibt keine einstelligen Optionenen sondern
	 *  nur evtl. Optionsgruppen).
	 */
	public boolean hasOptionen() {
		return (optionen != null && optionen.length != 0);
	}
	
	/**
	 * @return true - Es gibt eine Liste von wählbaren Gruppen von
	 * 	Optionen, ansonsten false.
	 */
	public boolean hasOptionsgruppen() {
		return (optionsGruppe != null && optionsGruppe.length != 0);
	}
	
	/**
	 * @see getOptionen()
	 * @param optionen Alle wählbaren Optionen
	 */
	public void setOptionen(IdLink[] optionen) {
		this.optionen = optionen;
	}
	
	/**
	 * Eine Optionsgruppe ist erstmal eine gleichberechtigte Option
	 * wie auch jene, die per "getOptionen()" geliefert werden. In einer 
	 * Optionsgruppe sind jedoch mehrer Link (und somit CharElemente) enthalten,
	 * die alle nur gemeinsam gewählt oder nicht gewählt werden können.
	 * 
	 * Beispiel:
	 * 	- "Schwerter +5 oder Dolche +2 und Klettern +3"
	 * In diesem Beispiel ist eine normale Option enthalten (Schwerter)
	 * und eine Gruppe (Dolche und Klettern) die nur gemeinsam gewählt 
	 * werden können. 
	 * 
	 * @see getOptionen()
	 * @param optionen Setzt das Attribut optionen.
	 */
	public IdLink[][] getOptionsGruppe() {
		return optionsGruppe;
	}
	
	/**
	 * Eine Optionsgruppe ist erstmal eine gleichberechtigte Option
	 * wie auch jene, die per "getOptionen()" geliefert werden. In einer 
	 * Optionsgruppe sind jedoch mehrer Link (und somit CharElemente) enthalten,
	 * die alle nur gemeinsam gewählt oder nicht gewählt werden können.
	 * 
	 * Beispiel:
	 * 	- "Schwerter +5 oder Dolche +2 und Klettern +3"
	 * In diesem Beispiel ist eine normale Option enthalten (Schwerter)
	 * und eine Gruppe (Dolche und Klettern) die nur gemeinsam gewählt 
	 * werden können. 
	 * 
	 * @see getOptionen()
	 * @param optionsGruppe Setzt das Attribut optionsGruppe.
	 */
	public void setOptionsGruppe(IdLink[][] optionsGruppe) {
		this.optionsGruppe = optionsGruppe;
	}
}
