/*
 * Created on 07.04.2005 / 19:59:20
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.komponenten.table;

import javax.swing.table.TableModel;

import org.d3s.alricg.gui.ProzessorObserver;
import org.d3s.alricg.gui.views.SpaltenSchema;
import org.d3s.alricg.gui.views.TypSchema;

/**
 * <u>Beschreibung:</u><br> 
 * Diese Interfaxe wird von allem Tabellen-Models implementiert, die zu einer 
 * sortierbaren Tabelle gehören. Es enthält alle über TableModel hinaus 
 * benötigten Methoden.
 * @author V. Strelow
 */
public interface SortableTableModelInterface extends TableModel, ProzessorObserver {

	/**
	 * Prüft ob eine Spalte sortierbar ist.
	 * Wird benutzt um Listener anzumelden und Pfeile einzublenden.
	 * @param column Die Spalte die überprüft werden soll
	 * @return true: Die Spalte mit der Nummer "column" ist sortierbar, sonst false
	 */
	public abstract boolean isSortable(int column);

	/**
	 * Sortiert den gesamten TreeTable nach der übergebenen Spalte
	 * @param column Die Spalte nach der Sortiert werden soll
	 */
	public abstract void sortTableByColumn(int column);
	
	/**
	 * Prüft ob die Reihenfolge "umgedreht" ist (sortiert von "A-Z" oder 
	 * von "Z-A")
	 * @param column Die Spalte die überprüft werden soll
	 * @return true: Die Spalte "column" ist "umgedreht" sortiert, ansonsten false
	 */
	public abstract boolean isSortColumnDesc(int column);

	/**
	 * Liefert den ToolTip Text für die Zeile "row" und Spalte "column".
	 * Der ToolTip Text kann somit vom Wert der Zeile/ Spalte abhängen
	 * @param row Die Zeile des gewünschten ToolTip-Textes
	 * @param column Die Spalte des gewünschten ToolTip-Textes
	 * @return Der ToolTip Text an der Stelle row/ column
	 */
	public abstract String getToolTip(int row, int column);

	/**
	 * Liefert das Object an der stelle "row" zurück. Im Gegensatz zu "getValueAt(row,colum)"
	 * wird hier kein String, sondern das CharElemnet bzw. der Link zurückgeliefert.
	 *
	 */
	public abstract Object getValueAt(int row);
	
	/**
	 * Liefert den ToolTip Text für die Überschrift der Splate "column".
	 * @param column  Die Spalte des gewünschten ToolTip-Textes
	 * @return Der ToolTip- Text an der Stelle column
	 */
	public abstract String getHeaderToolTip(int column);
	
	/**
	 * Liefert das ViewSchema zurück, auf das diese Tabelle aufbaut
	 * @return Das benutze ViewSchema
	 */
	public abstract SpaltenSchema getSpaltenSchema();
	
	/**
	 * liefert das ViewSchema zurück, auf das diese Tabelle aufbaut
	 * @return Das benutze TypSchema
	 */
	public abstract TypSchema getTypSchema();
}