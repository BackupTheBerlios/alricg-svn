/*
 * Created on 08.07.2006 / 15:45:16
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.views.talent.komponenten;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.gui.komponenten.table.SortableTableModelInterface;

/**
 * <u>Beschreibung:</u><br> 
 * TableCellEditor für die Spezialisierung bei Talenten. Wird nur für diesen einen Zweck eingesetzt.
 * @author V. Strelow
 */
public class TalentSpezialisierungCellEditor extends AbstractCellEditor implements TableCellEditor {
	TalentSpezialisierungPanel edi;
	
	public TalentSpezialisierungCellEditor() {
		edi = new TalentSpezialisierungPanel(this);
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int col) {

		Object obj = ((SortableTableModelInterface) table.getModel()).getValueAt(row);
		
		if (obj instanceof Talent) {
			edi.initTalent((Talent) obj);
		} else {
			edi.initTalent((Link) obj);
		}

		return edi;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		return edi.getText();
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.DefaultCellEditor#isCellEditable(java.util.EventObject)
	 */
	@Override
	public boolean isCellEditable(EventObject arg0) {
		return true;
	}
}
