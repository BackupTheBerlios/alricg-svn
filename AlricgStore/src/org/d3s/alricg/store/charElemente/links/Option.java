/*
 * Created on 13.10.2005 / 17:22:09
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente.links;

import java.util.List;

import org.d3s.alricg.store.charElemente.CharElement;



/**
 * <u>Beschreibung:</u><br> 
 * Interface welches alle unterschiedlichen Arten von Optionen zusammenfasst.
 *
 * LISTE - In "werteListe" steht eine Liste von Werten, wobei jeder Wert einem "link"
 *   zugewiesen werden mu�. Es werden soviele optionen gew�hlt, wie es werte gibt. 
 *  (Das Attribut "anzahl" und "max" wird nicht benutzt)
 *  
 * ANZAHL - In "anzahl" steht eine Zahl, die angibt wieviele der "Links" gew�hlt 
 *  werden m�ssen. Jeder Link kann einen Wert haben (�ber den "idLinkTyp").
 *  (Das attribut "wert" und "max" wird	nicht benutzt)
 *  
 * VERTEILUNG - Der Wert in "wert" kann auf soviele der Links verteilt werden, wie
 *  im Attribut "anzahl" angegeben. D.h. erst werden die Links ausgew�hlt, dann der
 *  "wert" beliebig auf die gew�hlten Links verteilt. 
 *  (Siehe "Elfische Siedlung" S. 37 im AZ( In "max" steht wie hoch der Wert
 *   jedes gew�hlten Link sein darf.)
 *   
 *   Beispiele sind bei den Klassen der verschiedenen Modi angegeben!
 *   
 * @author V. Strelow
 */
public interface Option<ZIEL extends CharElement> {
	
	/**
	 * Die Liste mit Links zu dieser Option. Wie die Liste interpretiert wird, 
	 * h�ngt von dem der konkreten Klasse der Option ab
	 * @see Option
	 * @return Liste mit Links zu dieser Option
	 */
	
	public List<IdLink<ZIEL>> getLinkList();
	/**
	 * @see getLinkList()
	 * @param optionen Liste mit Links zu dieser Option
	 */
	public void setLinkList(List<IdLink<ZIEL>> optionen);
	
    /**
     * Eine Alternative zu dieser Option. Es kann auch die Alternative gew�hlt werden,
     * in dem Fall "verf�llt" diese Option. (Eine altervnatie Option kann auch wieder
     * eine Alternative besitzen, so dass eine Liste von Alternativen ensteht)  
     */
	/**
     * Eine Alternative zu dieser Option. Ist zu einer Option eine Alternative vorhanden,
     * so kann statt der aktuellen Option auch die Alternative gew�hlt werden. 
     * Eine altervnatie Option kann auch wieder eine Alternative besitzen, so k�nnen
     * mehrere Optionen per "oder" verkn�pft werden.
     * Ist ist damit ist auch Vorgendes m�glich:
     * 	(Hiebwaffen +3 und Schwerter +2) oder (Degen +3 und Wurfwaffen +3)
     * 
     * Die erste Klammer ist eine OptionAnzahl mit einer Liste von zwei Elementen
     * und einer Alternativen Option.
     * 
	 * @return Die alternative Optionzu dieser, oder null 
	 */
	public Option<ZIEL> getAlternativOption();
	
	/**
	 * @see getAlternativOption()
	 * @param alternativOption Die alternative Optionzu dieser, oder null 
	 */
	public void setAlternativOption(Option<ZIEL> alternativOption);
	
	
	
	/**
	 * Nur im Modus "VERTEILUNG" wichtig!
	 * Gibt die maximal Stufe der gew�hlten CharElemente an. "0" ist hier
	 * gleichbedeutent mit keiner Begrenzung
	 * 
	 * @return Liefert das Attribut max.
	 */
	public abstract int getMax();

	/**
	 * Nur im Modus "VERTEILUNG" wichtig!
	 * Gibt die maximal Stufe der gew�hlten CharElemente an. "0" ist hier
	 * gleichbedeutent mit keiner Begrenzung
	 * 
	 * @param max Setzt das Attribut max.
	 */
	public abstract void setMax(int max);

	
	/**
	 * Nur im Modus "ANZAHL" & "VERTEILUNG" wichtig!
	 * 
	 * ANZAHL - In "anzahl" steht eine Zahl, die angibt wieviele der "Links" gew�hlt
	 *  werden m�ssen. Jede option kann einen Wert haben (�ber den "idLinkTyp").
	 * VERTEILUNG - Der Wert in "wert" kann auf soviele der Links verteilt werden,
	 *  wie im Attribut "anzahl" angegeben. D.h. erst werden die Links ausgew�hlt, 
	 *  dann der "wert" beliebig auf die gew�hlten Links verteilt. (Siehe "Elfische 
	 *  Siedlung" S. 37 im AZ) In "max" steht wie hoch die Stufe jeder 
	 *  gew�hlten option sein darf.
	 * 
	 * Ist "anzahl = 0", so gibt es keine Begrenzung!
	 * @return Liefert das Attribut anzahl.
	 */
	public abstract int getAnzahl();
	
	/**
	 * Nur im Modus "ANZAHL" & "VERTEILUNG" wichtig!
	 * 
	 * ANZAHL - In "anzahl" steht eine Zahl, die angibt wieviele der "Links" gew�hlt
	 *  werden m�ssen. Jede option kann einen Wert haben (�ber den "idLinkTyp").
	 * VERTEILUNG - Der Wert in "wert" kann auf soviele der Links verteilt werden,
	 *  wie im Attribut "anzahl" angegeben. D.h. erst werden die Links ausgew�hlt, 
	 *  dann der "wert" beliebig auf die gew�hlten Links verteilt. (Siehe "Elfische 
	 *  Siedlung" S. 37 im AZ) In "max" steht wie hoch die Stufe jeder 
	 *  gew�hlten option sein darf.
	 * 
	 * Ist "anzahl = 0", so gibt es keine Begrenzung!
	 * @param Liefert das Attribut anzahl.
	 */
	public abstract void setAnzahl(int anzahl);
	
	/**
	 * Nur f�r "LISTE" wichtig!
	 * In "werteListe "steht eine Liste von Werten, wobei jeder Wert einem
	 * "link" zugewiesen werden mu�. Es werden soviele optionen gew�hlt, wie
	 * 	es Werte gibt. 
	 */
	public abstract int[] getWerteListe();
	
	/**
	 * Nur f�r "LISTE" wichtig!
	 * In "werteListe "steht eine Liste von Werten, wobei jeder Wert einem
	 * "link" zugewiesen werden mu�. Es werden soviele optionen gew�hlt, wie
	 * 	es Werte gibt. 
	 */
	public abstract void setWerteListe(int[] werteListe);
	
	/**
	 * Nur f�r "VERTEILUNG" wichtig!
	 * 	Der Wert in "wert" kann auf soviele der Links verteilt werden,
	 *  wie im Attribut "anzahl" angegeben. D.h. erst werden die Links ausgew�hlt, 
	 *  dann der "wert" beliebig auf die gew�hlten Links verteilt. (Siehe 
	 *  "Elfische Siedlung" S. 37 im AZ)
	 *  In "max" steht wie hoch die Stufe jedes gew�hlten links sein darf
	 */
	public abstract int getWert();
	
	/**
	 * Nur f�r "VERTEILUNG" wichtig!
	 * 	Der Wert in "wert" kann auf soviele der Links verteilt werden,
	 *  wie im Attribut "anzahl" angegeben. D.h. erst werden die Links ausgew�hlt, 
	 *  dann der "wert" beliebig auf die gew�hlten Links verteilt. (Siehe 
	 *  "Elfische Siedlung" S. 37 im AZ)
	 *  In "max" steht wie hoch die Stufe jedes gew�hlten links sein darf
	 */
	public abstract void setWert(int wert);

	/**
	 * Erstellt eine neue Option mit allen Werten dieser Option
	 * @return Kopie dieser Option
	 */
	public abstract Option copyOption();

}
