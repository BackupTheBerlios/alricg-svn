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
 * Dient den SortableTreeTable und SortableTables als Vorlage für die Darstellung.
 * 
 * In diesem Schema werden spezielle Methoden für die Anzeige für die Spalten der Tabellen zusammengefasst.
 * Da jedes CharElement andere Spalten benötigt, wird für jedes CharElemnt ein neues SpaltenSchema benötigt.
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
		
		// Wenn "GeneratorLinks" mit den Objekten angezeigt werden, für bereist gewählte Links
		// bei der Generierung
		objektLinkGen, 
		
		// Wenn "HeldenLinks" mit den Objekten angezeigt werden, für bereits gewählte Links
		// bei dem Management von Helden
		objektLinkHel, 
		
		// Für den Editor (auch objektDirekt, aber mit anderen Spalten)
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
	 * die Renderer, Editoren, Bilder und ähnliches für die entsprechenden Spalten
	 * gesetzt.
	 * @param table Die Table/ TreeTable die vorbereitet werden soll
	 * @param art Die Art der darzustellenden Spalten
	 */
	public void initTable(Prozessor prozessor, SortableTable table, SpaltenArt art);
		
	/**
	 * Gibt zurück, ob nach einer Spalte sortiert werden kann, also dafür 
	 * entsprechende Pfeile angezeigt werden!
	 * @param column Die Spalte nach der Sortiert werden soll
	 * @return true: Nach dieser Spalte kann Sortiert werden, sonst false
	 */
	public boolean isSortable(Object column);
		
	/**
	 * Liefert den ToolTipText für eine Tabellen Überschrift
	 * @param column Die Spalte auf dessen Titel der Mauszeiger steht
	 * @return Der ToolTipText für den Titel dieser Spalte 
	 */
	public String getHeaderToolTip(Object column);
	
	/**
	 * Liefert für die Anzeige als TreeTable den Namen des ersten Elements, des 
	 * "WurzelKnoten". Typischer weise ist dies für Talente "Talente", Zauber "Zauber" usw.
	 * 
	 * @return Name des RootNodes
	 */
	public String getRootNodeName();
	
	/**
	 * Liefert die Elemente nach denen die TreeTable gefiltert werden kann. 
	 * Bestimmte Elementen sollen je nach Filter nicht angezeigt werden.
	 * Das Elemente "Keiner" gibt es immer, es gehört mit zu dem Elementen 
	 * die hier zurückgeliefert werden!
	 * Beispiel: "Nur Wählbare"
	 * @return Die Elemente zum Filtern der TreeTable (mit "Keiner")
	 */
	public Enum[] getFilterElem(SpaltenArt art);
	
	/**
	 * Liefert die Elemente nach denen die TreeTable geordnet werden kann 
	 * (also nach denen die Elemente der TreeTable in Ordner angeordnet werden).
	 * Das Element "Keine" gibt es immer, wird jedoch durch das Panel hinzugefügt,
	 * taucht hier also NICHT auf!
	 * Beispiel: "Sorte" bei Talenten
	 * @return Die Elemente zum Ordnen der TreeTable (ohne "Keine")
	 */
	public Enum[] getOrdnungElem(SpaltenArt art);
	
	

}