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
 *   zugewiesen werden muß. Es werden soviele optionen gewählt, wie es werte gibt. 
 *  (Das Attribut "anzahl" und "max" wird nicht benutzt)
 *  
 * ANZAHL - In "anzahl" steht eine Zahl, die angibt wieviele der "Links" gewählt 
 *  werden müssen. Jeder Link kann einen Wert haben (über den "idLinkTyp").
 *  (Das attribut "wert" und "max" wird	nicht benutzt)
 *  
 * VERTEILUNG - Der Wert in "wert" kann auf soviele der Links verteilt werden, wie
 *  im Attribut "anzahl" angegeben. D.h. erst werden die Links ausgewählt, dann der
 *  "wert" beliebig auf die gewählten Links verteilt. 
 *  (Siehe "Elfische Siedlung" S. 37 im AZ( In "max" steht wie hoch der Wert
 *   jedes gewählten Link sein darf.)
 *   
 *   Beispiele sind bei den Klassen der verschiedenen Modi angegeben!
 *   
 * @author V. Strelow
 */
public interface Option<ZIEL extends CharElement> {
	
	/**
	 * Die Liste mit Links zu dieser Option. Wie die Liste interpretiert wird, 
	 * hängt von dem der konkreten Klasse der Option ab
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
     * Eine Alternative zu dieser Option. Es kann auch die Alternative gewählt werden,
     * in dem Fall "verfällt" diese Option. (Eine altervnatie Option kann auch wieder
     * eine Alternative besitzen, so dass eine Liste von Alternativen ensteht)  
     */
	/**
     * Eine Alternative zu dieser Option. Ist zu einer Option eine Alternative vorhanden,
     * so kann statt der aktuellen Option auch die Alternative gewählt werden. 
     * Eine altervnatie Option kann auch wieder eine Alternative besitzen, so können
     * mehrere Optionen per "oder" verknüpft werden.
     * Ist ist damit ist auch Vorgendes möglich:
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
	 * ANZAHL - In "anzahl" steht eine Zahl, die angibt wieviele der "Links" gewählt
	 *  werden müssen. Jede option kann einen Wert haben (über den "idLinkTyp").
	 * VERTEILUNG - Der Wert in "wert" kann auf soviele der Links verteilt werden,
	 *  wie im Attribut "anzahl" angegeben. D.h. erst werden die Links ausgewählt, 
	 *  dann der "wert" beliebig auf die gewählten Links verteilt. (Siehe "Elfische 
	 *  Siedlung" S. 37 im AZ) In "max" steht wie hoch die Stufe jeder 
	 *  gewählten option sein darf.
	 * 
	 * Ist "anzahl = 0", so gibt es keine Begrenzung!
	 * @return Liefert das Attribut anzahl.
	 */
	public abstract int getAnzahl();
	
	/**
	 * Nur im Modus "ANZAHL" & "VERTEILUNG" wichtig!
	 * 
	 * ANZAHL - In "anzahl" steht eine Zahl, die angibt wieviele der "Links" gewählt
	 *  werden müssen. Jede option kann einen Wert haben (über den "idLinkTyp").
	 * VERTEILUNG - Der Wert in "wert" kann auf soviele der Links verteilt werden,
	 *  wie im Attribut "anzahl" angegeben. D.h. erst werden die Links ausgewählt, 
	 *  dann der "wert" beliebig auf die gewählten Links verteilt. (Siehe "Elfische 
	 *  Siedlung" S. 37 im AZ) In "max" steht wie hoch die Stufe jeder 
	 *  gewählten option sein darf.
	 * 
	 * Ist "anzahl = 0", so gibt es keine Begrenzung!
	 * @param Liefert das Attribut anzahl.
	 */
	public abstract void setAnzahl(int anzahl);
	
	/**
	 * Nur für "LISTE" wichtig!
	 * In "werteListe "steht eine Liste von Werten, wobei jeder Wert einem
	 * "link" zugewiesen werden muß. Es werden soviele optionen gewählt, wie
	 * 	es Werte gibt. 
	 */
	public abstract int[] getWerteListe();
	
	/**
	 * Nur für "LISTE" wichtig!
	 * In "werteListe "steht eine Liste von Werten, wobei jeder Wert einem
	 * "link" zugewiesen werden muß. Es werden soviele optionen gewählt, wie
	 * 	es Werte gibt. 
	 */
	public abstract void setWerteListe(int[] werteListe);
	
	/**
	 * Nur für "VERTEILUNG" wichtig!
	 * 	Der Wert in "wert" kann auf soviele der Links verteilt werden,
	 *  wie im Attribut "anzahl" angegeben. D.h. erst werden die Links ausgewählt, 
	 *  dann der "wert" beliebig auf die gewählten Links verteilt. (Siehe 
	 *  "Elfische Siedlung" S. 37 im AZ)
	 *  In "max" steht wie hoch die Stufe jedes gewählten links sein darf
	 */
	public abstract int getWert();
	
	/**
	 * Nur für "VERTEILUNG" wichtig!
	 * 	Der Wert in "wert" kann auf soviele der Links verteilt werden,
	 *  wie im Attribut "anzahl" angegeben. D.h. erst werden die Links ausgewählt, 
	 *  dann der "wert" beliebig auf die gewählten Links verteilt. (Siehe 
	 *  "Elfische Siedlung" S. 37 im AZ)
	 *  In "max" steht wie hoch die Stufe jedes gewählten links sein darf
	 */
	public abstract void setWert(int wert);

	/**
	 * Erstellt eine neue Option mit allen Werten dieser Option
	 * @return Kopie dieser Option
	 */
	public abstract Option copyOption();

}
