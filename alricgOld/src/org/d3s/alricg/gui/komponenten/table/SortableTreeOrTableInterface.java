/*
 * Created on 05.07.2006 / 23:35:10
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.komponenten.table;

import org.d3s.alricg.gui.ProzessorObserver;

/**
 * <u>Beschreibung:</u><br> 
 * Fasst Methoden aus dem TreeModel und TableModel zusammen, um eine einheitliche
 * Schnittstelle zu schaffen. Wird vom TabellenPanel benötigt.
 * @author V. Strelow
 */
public interface SortableTreeOrTableInterface extends ProzessorObserver {
	
	/**
	 * Prüft ob eine Spalte sortierbar ist.
	 * Wird benutzt um Listener anzumelden und Pfeile einzublenden.
	 * @param column Die Spalte die überprüft werden soll
	 * @return true: Die Spalte mit der Nummer "column" ist sortierbar, sonst false
	 */
	public boolean isSortable(int column);
	
	/**
	 * Sortiert den gesamten TreeTable nach der übergebenen Spalte
	 * @param column Die Spalte nach der Sortiert werden soll
	 */
	public void sortTableByColumn(int column);
	
	/**
	 * Setzt einen neuen Filter, nach dem die Elemente innerhalb einer Tabelle/Tree sortiert
	 * werden sollen. Der Filter muss aus dem entsprechenden Schema stammen.
	 * @param filter Der Bezeichner für den neuen Filter
	 */
	public void setFilter(Enum filter);
}
