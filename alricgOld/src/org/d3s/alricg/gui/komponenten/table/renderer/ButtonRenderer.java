/*
 * Created on 23.09.2005 / 16:57:30
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.gui.komponenten.table.renderer;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import org.d3s.alricg.gui.views.SpaltenSchema;

/**
 * <u>Beschreibung:</u><br> 
 *	Für die Anzeige eines Buttons in einer Tabelle.
 * @author V. Strelow
 */
public class ButtonRenderer implements TableCellRenderer {

	protected JButton button;
	protected JLabel label;
	
	/**
	 * Konstruktor
	 */
	public ButtonRenderer(JButton button) {
		this.button = button;
		this.label = new JLabel();
		
		label.setOpaque(true);
		button.setOpaque(true);
		button.setMargin(new Insets(1,1,1,1));
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
		
		// Prüfen, ob dort überhaupt ein Button hin soll und hin darf
		if (!value.equals(SpaltenSchema.buttonValue) || !table.getModel().isCellEditable(row, column)) {
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
			button.setBackground(UIManager.getColor("Button.background"));
		}

		
		return button;
	}

}
