/*
 * Created on 13.08.2006 / 22:15:16
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".*
 */
package org.d3s.alricg.gui.views.simpel;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

import org.d3s.alricg.gui.komponenten.table.SortableTable;
import org.d3s.alricg.gui.views.SpaltenSchema;
import org.d3s.alricg.prozessor.Prozessor;

/**
 * @author Vincent
 *
 */
public class LinkListSpalten implements SpaltenSchema {

	public enum Spalten {
		name("Name"),
		wert("Wert"),
		text("Text"),
		zweitZiel("Ziel"),
		leitwert("Leitw."),
		minus("-");
		private String bezeichner; // Name der Angezeigt wird
		
		/** Konstruktur
		 * @param value Der Tag um den bezeichner aus der Library zu laden
		 */
		private Spalten(String value) {
			if (value.equals("+") || value.equals("-") || value.equals("*")) {
				bezeichner = value;
			} else {
				bezeichner = value;
			}
		}
		
		public String toString() {
			return bezeichner;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#getFilterElem(org.d3s.alricg.gui.views.SpaltenSchema.SpaltenArt)
	 */
	public Enum[] getFilterElem(SpaltenArt art) {
		// Keine Filter
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#getHeaderToolTip(java.lang.Object)
	 */
	public String getHeaderToolTip(Object column) {
		// Keine HeaderToolTip
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#getOrdnungElem(org.d3s.alricg.gui.views.SpaltenSchema.SpaltenArt)
	 */
	public Enum[] getOrdnungElem(SpaltenArt art) {
		// Keine Ordnung
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#getRootNodeName()
	 */
	public String getRootNodeName() {
		// Keine Root
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#getSpalten(org.d3s.alricg.gui.views.SpaltenSchema.SpaltenArt)
	 */
	public Enum[] getSpalten(SpaltenArt art) {
		return Spalten.values();
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#initTable(org.d3s.alricg.prozessor.Prozessor, org.d3s.alricg.gui.komponenten.table.SortableTable, org.d3s.alricg.gui.views.SpaltenSchema.SpaltenArt)
	 */
	public void initTable(Prozessor prozessor, SortableTable table, SpaltenArt art) {
		
		JComboBox box = new JComboBox();
		for (int i = 0; i < 21; i++) {
			box.addItem(i);
		}
		
		table.getColumn(Spalten.wert.toString()).setCellEditor(new DefaultCellEditor(box));
		table.setColumnButton(Spalten.minus.toString(), Spalten.minus.toString());
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#isSortable(java.lang.Object)
	 */
	public boolean isSortable(Object column) {
		// Bei allen Spalten ist Sortierung möglich
		return true;
	}

}
