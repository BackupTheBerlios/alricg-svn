/*
 * Created on 08.04.2005 / 00:23:01
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.gui.views;

import org.d3s.alricg.gui.komponenten.table.SortableTable;
import org.d3s.alricg.prozessor.Prozessor;

/**
 * <u>Beschreibung:</u><br> 
 * Dient den SortableTreeTable und SortableTables als Vorlage f�r die Darstellung.
 * 
 * In diesem Schema werden spezielle Methoden f�r die Anzeige f�r die Spalten der Tabellen zusammengefasst.
 * Da jedes CharElement andere Spalten ben�tigt, wird f�r jedes CharElemnt ein neues SpaltenSchema ben�tigt.
 * In jeder Implementierung des Spaltenschemas wird per Enum angegeben, welche Spalten zu
 * dem CharElement existieren. Per "getSpalten" werden dann jeweils die passenden Spalten geliefert.
 * 
 * @author V. Strelow
 */
public interface SpaltenSchema {
	String buttonValue = "button";

	
	public enum SpaltenArt {
		// Wenn die Objekte direkt angezeigt werden z.B. "Talente"
		objektDirekt, 
		
		// Wenn "GeneratorLinks" mit den Objekten angezeigt werden, f�r bereist gew�hlte Links
		// bei der Generierung
		objektLinkGen, 
		
		// Wenn "HeldenLinks" mit den Objekten angezeigt werden, f�r bereits gew�hlte Links
		// bei dem Management von Helden
		objektLinkHel, 
		
		// F�r den Editor (auch objektDirekt, aber mit anderen Spalten)
		editorAuswahl,
		editorGewaehlt;
	}
	
	/**
	 * Liefert ein Enum mit den Spalten die in einer Table/ TreeTable von der 
	 * entsprechenden Art von Spalten angezeigt werden.
	 * @param art Die Art der Spalten
	 * @return Ein Array mit den Enums aller anzuzeigenden Spalten
	 */
	public Enum[] getSpalten(SpaltenArt art);
	
	/**
	 * Bereitet eine Tabelle (auch TreeTable) auf die Benutzung vor. Dabei werden
	 * die Renderer, Editoren, Bilder und �hnliches f�r die entsprechenden Spalten
	 * gesetzt.
	 * @param table Die Table/ TreeTable die vorbereitet werden soll
	 * @param art Die Art der darzustellenden Spalten
	 */
	public void initTable(Prozessor prozessor, SortableTable table, SpaltenArt art);
		
	/**
	 * Gibt zur�ck, ob nach einer Spalte sortiert werden kann, also daf�r 
	 * entsprechende Pfeile angezeigt werden!
	 * @param column Die Spalte nach der Sortiert werden soll
	 * @return true: Nach dieser Spalte kann Sortiert werden, sonst false
	 */
	public boolean isSortable(Object column);
		
	/**
	 * Liefert den ToolTipText f�r eine Tabellen �berschrift
	 * @param column Die Spalte auf dessen Titel der Mauszeiger steht
	 * @return Der ToolTipText f�r den Titel dieser Spalte 
	 */
	public String getHeaderToolTip(Object column);
	
	/**
	 * Liefert f�r die Anzeige als TreeTable den Namen des ersten Elements, des 
	 * "WurzelKnoten". Typischer weise ist dies f�r Talente "Talente", Zauber "Zauber" usw.
	 * 
	 * @return Name des RootNodes
	 */
	public String getRootNodeName();
	
	/**
	 * Liefert die Elemente nach denen die TreeTable gefiltert werden kann. 
	 * Bestimmte Elementen sollen je nach Filter nicht angezeigt werden.
	 * Das Elemente "Keiner" gibt es immer, es geh�rt mit zu dem Elementen 
	 * die hier zur�ckgeliefert werden!
	 * Beispiel: "Nur W�hlbare"
	 * @return Die Elemente zum Filtern der TreeTable (mit "Keiner")
	 */
	public Enum[] getFilterElem(SpaltenArt art);
	
	/**
	 * Liefert die Elemente nach denen die TreeTable geordnet werden kann 
	 * (also nach denen die Elemente der TreeTable in Ordner angeordnet werden).
	 * Das Element "Keine" gibt es immer, wird jedoch durch das Panel hinzugef�gt,
	 * taucht hier also NICHT auf!
	 * Beispiel: "Sorte" bei Talenten
	 * @return Die Elemente zum Ordnen der TreeTable (ohne "Keine")
	 */
	public Enum[] getOrdnungElem(SpaltenArt art);
	
	

}