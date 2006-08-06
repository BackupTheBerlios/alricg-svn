/**
 * 
 */
package org.d3s.alricg.gui.views.talent.komponenten;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.gui.komponenten.table.SortableTableModelInterface;
import org.d3s.alricg.prozessor.LinkProzessor;

/**
 * @author Vincent
 *
 */
public class WertComboboxCellEditor extends AbstractCellEditor implements TableCellEditor {
	private LinkProzessor prozessor;
	private JComboBox coBox = new JComboBox();
	
	public WertComboboxCellEditor(LinkProzessor prozessor) {
		this.prozessor = prozessor;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int col) {

		Object obj = ((SortableTableModelInterface) table.getModel()).getValueAt(row);
		int minWert = prozessor.getMinWert((Link) obj);
		int maxWert = prozessor.getMaxWert((Link) obj);
		
		coBox.removeAllItems();
		if (obj instanceof Link) {
			for (int i = minWert; i <= maxWert; i++) {
				coBox.addItem(i);
			}
		} 

		return coBox;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		return coBox.getSelectedItem();
	}

}
