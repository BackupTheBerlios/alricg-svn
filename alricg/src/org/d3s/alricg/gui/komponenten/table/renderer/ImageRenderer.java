/*
 * Created on 23.09.2005 / 16:55:35
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */

package org.d3s.alricg.gui.komponenten.table.renderer;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.d3s.alricg.gui.komponenten.MultiIcon;

/**
 * <u>Beschreibung:</u><br> 
 *	Für die Anzeige eines Buttons in einer Tabelle.
 * @author V. Strelow
 */
public class ImageRenderer extends DefaultTableCellRenderer {
	protected JLabel label;
	protected MultiIcon multiIcon;
	
	/**
	 * Konstruktor
	 */
	public ImageRenderer() {
		label = new JLabel();
		multiIcon = new MultiIcon();
		
		label.setOpaque(true);
		label.setIcon(multiIcon);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
		
		// Prüfen, ob dort überhaupt ein Bild hin soll
		if (value instanceof Icon[]) {
			multiIcon.replaceAll((Icon[]) value);
			// Wenn nicht wird ein Label eingefügt
			if (isSelected) {
				label.setForeground(table.getSelectionForeground());
				label.setBackground(table.getSelectionBackground());
			} else {
				label.setForeground(table.getForeground());
				label.setBackground(table.getBackground());
			}
		} else {
			return super.getTableCellRendererComponent(table, value, 
									isSelected, hasFocus, row, column);
		}
		
		return label;
	}
}
