/*
 * Created on 23.09.2005 / 17:02:16
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.gui.komponenten.table.renderer;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.d3s.alricg.gui.views.SpaltenSchema;

/**
 * <u>Beschreibung:</u><br> 
 * Für die Anzeige eines Buttons in einer Tabelle und das editieren der Tabelle
 * mit dem Button.
 * @author V. Strelow
 */
public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
	protected JButton button;
	protected JLabel label;
	
	/**
	 * Konstruktor
	 */
	public ButtonEditor(JButton button) {
		this.button = button;
		this.label = new JLabel();
		
		label.setOpaque(true);
		button.setOpaque(true);
		button.setMargin(new Insets(1,1,1,1));
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
						boolean isSelected, int row, int column) {
		
		// Prüfen, ob dort überhaupt ein Button hin soll
		if (!value.equals(SpaltenSchema.buttonValue)) {
			// Wenn nicht wird ein Label eingefügt
			if (isSelected) {
				label.setForeground(table.getSelectionForeground());
				label.setBackground(table.getSelectionBackground());
			} else {
				label.setForeground(table.getForeground());
				label.setBackground(table.getBackground());
			}
			return label;
		}
		
		if (isSelected) {
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setForeground(table.getForeground());
			button.setBackground(table.getBackground());
		}

		return button;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		return new String(button.getText());
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.CellEditor#stopCellEditing()
	 */
	public boolean stopCellEditing() {
		return super.stopCellEditing();
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.AbstractCellEditor#fireEditingStopped()
	 */
	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}

}
