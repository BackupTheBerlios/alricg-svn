/*
 * Created on 07.04.2005 / 17:57:24
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.komponenten.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.d3s.alricg.gui.views.SpaltenSchema;
import org.d3s.alricg.gui.views.TypSchema;
import org.d3s.alricg.gui.views.SpaltenSchema.SpaltenArt;

/**
 * <u>Beschreibung:</u><br> 
 * Dient als Model zur Darstellung von Daten in einer JTable (SortableTable). 
 * In dieser Klasse werden alle Grundfunktionen dafür gehalten, um auf die Daten zu 
 * zugreifen und diese zu verwalten.
 * Die spezifischen Funktionen, die von der Art der Daten (Talent, Zauber, Rasse, usw.)
 * abhängen, werden in einem Schema gehalten. Während dieses Modell speziell für die
 * Darstellung in einer JTable gemacht ist, ist das Schema unabhängig von der Art der
 * Darstellung und kann somit auch für die TreeTable verwendet werden.
 * 
 * @author V. Strelow
 */
public class SortableTableModel<E> extends AbstractTableModel 
								implements SortableTableModelInterface, SortableTreeOrTableInterface {
	
	private final ArrayList<E> dataList = new ArrayList<E>();
	private final Enum[] columns;
	private final SpaltenSchema spaltenSchema; // Spezifische Methoden für die Spalten
	private final TypSchema typSchema; // Spezifische Methoden für die Zeilen
	private final boolean[] lastAscSorted;
	private int columnToSort = 0;
	
	/**
	 * Konstruktor.
	 * 
	 * @param sSchema Das SpaltenSchema, mit dem die Spalten und die Struktur der Table aufgebaut wird
	 * @param tSchema Das TypSchema, mit dem die Logik zur Verarbeitung der Elemente festgelegt wird
	 * @param art Die Art der Spalten, welche die gewünschte Nutzung bestimmt
	 */
	public SortableTableModel(SpaltenSchema sSchema, TypSchema tSchema, SpaltenArt art) {
		
		this.spaltenSchema = sSchema;
		this.typSchema = tSchema;
		this.columns = spaltenSchema.getSpalten(art);
		lastAscSorted = new boolean[columns.length];
		
		Arrays.fill(lastAscSorted, false); // Damit überall ein Wert steht
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return dataList.size();
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return columns.length;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int colIdx) {
		return columns[colIdx].toString();
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.komponenten.table.SortableTableModelInterface#getHeaderToolTip(int)
	 */
	public String getHeaderToolTip(int colIdx) {
		return spaltenSchema.getHeaderToolTip(columns[colIdx]);
	}
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.komponenten.table.SortableTableModelInterface#getToolTip(int, int)
	 */
	public String getToolTip(int rowIdx, int colIdx) {
		return typSchema.getToolTip(dataList.get(rowIdx), columns[colIdx]);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIdx, int colIdx) {
		return typSchema.getCellValue(dataList.get(rowIdx), columns[colIdx]);
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.komponenten.table.SortableTableModelInterface#getValueAt(int)
	 */
	public Object getValueAt(int row) {
		return dataList.get(row);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object aValue, int rowIdx, int colIdx) {
		typSchema.setCellValue(aValue, dataList.get(rowIdx), columns[colIdx]);
	}
 
	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIdx, int colIdx) {
    	Object obj = dataList.get(rowIdx);
    	
		if (obj instanceof Enum || obj instanceof String) {
			return false;
		}
		
		return typSchema.isCellEditable(obj, columns[colIdx]);
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.komponenten.table.SortableTreeOrTableInterface#setFilter(java.lang.Enum)
	 */
	public void setFilter(Enum filter) {
		setData(
				typSchema.doFilterElements(
						filter, 
						typSchema.getElementBox().getUnmodifiableList())
			);
	}
	
//	 ----------- Methoden aus dem SortableTableModelInterface Interface --------------
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.komponenten.table.SortableTableModelInterface#sortTableByColumn(int)
	 */
	public void sortTableByColumn(int colIdx) {
		
		// Comparator setzen
		if ( lastAscSorted[colIdx] ) {
			Collections.sort(dataList, typSchema.getComparator(columns[colIdx]));
		} else {
			// Umgedrehte reihenfolge
			Collections.sort(dataList, 
					Collections.reverseOrder( typSchema.getComparator(columns[colIdx])) 
				);
		}
		
		// Sortierung "umdrehen" und beim Comparator setzen
		lastAscSorted[colIdx] = !lastAscSorted[colIdx];
		
		// speichert die column, nach der als letztes sortiert wurde
		columnToSort = colIdx;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.komponenten.table.SortableTableModelInterface#isSortable(int)
	 */
	public boolean isSortable(int colIdx) {
		return spaltenSchema.isSortable(columns[colIdx]);
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.komponenten.table.SortableTableModelInterface#isSortColumnDesc(int)
	 */
	public boolean isSortColumnDesc(int colIdx) {
		return !lastAscSorted[colIdx];
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.komponenten.table.SortableTableModelInterface#getSpaltenSchema()
	 */
	public SpaltenSchema getSpaltenSchema() {
		return spaltenSchema;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.komponenten.table.SortableTableModelInterface#getTypSchema()
	 */
	public TypSchema getTypSchema() {
		return typSchema;
	}

// ----------- Methoden aus dem Observer Interface --------------	
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.komponenten.table.ProzessorObserver#addElement(java.lang.Object)
	 */
	public void addElement(Object obj) {
		int idx;
		
		// Richtige Position zum einfügen finden
		idx = Collections.binarySearch(dataList, obj, typSchema.getComparator(columns[columnToSort]));
		
		if (idx < 0) {
			idx = Math.abs(idx + 1);
		}
		
		dataList.add(idx, (E) obj);
		
		this.fireTableRowsInserted(idx, idx);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.komponenten.table.ProzessorObserver#removeElement(java.lang.Object)
	 */
	public void removeElement(Object obj) {
		final int idx;
		
		idx = dataList.indexOf(obj);
		dataList.remove((E) obj);
		
		this.fireTableRowsDeleted(idx, idx);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.komponenten.table.ProzessorObserver#updateElement(java.lang.Object)
	 */
	public void updateElement(Object obj) {
		final int idx;
		
		idx = dataList.indexOf(obj);
		
		this.fireTableRowsUpdated(idx, idx);
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.komponenten.table.ProzessorObserver#setData(java.util.List)
	 */
	public void setData(List list) {
		dataList.clear();
		dataList.addAll(list);
		
		// Sortieren der Elemente
		sortTableByColumn(this.columnToSort);
		
		this.fireTableDataChanged();
	}

}



