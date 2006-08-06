/*
 * Created on 23.09.2005 / 17:34:23
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
import javax.swing.table.TableCellRenderer;

/**
 * <u>Beschreibung:</u><br> 
 * Für die Anzeige eines Textes mit einem optionalen Icon. Das Icon kann vor oder hinter dem 
 * Text stehen.
 * @author Vincent
 */
public class ImageTextRenderer implements TableCellRenderer {
	protected JLabel label;
	protected Icon icon;
	
	/**
	 * Konstruktor
	 */
	public ImageTextRenderer(boolean isIconLinks) {
		this.label = new JLabel();
		
		if (isIconLinks) {
			label.setHorizontalTextPosition(JLabel.RIGHT);
		} else {
			label.setHorizontalTextPosition(JLabel.LEFT);
		}
		
		label.setOpaque(true);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		// Prüfen, ob dort überhaupt ein Bild hin soll
		if (value instanceof ImageTextObject) {
			label.setIcon( ((ImageTextObject) value).getIcon() );
			label.setText( ((ImageTextObject) value).getText() );
		} else { // normalerweise ein String
			label.setIcon( null );
			label.setText( value.toString() );
		}
		
		// Prüfen ob selektiert
		if (isSelected) {
			label.setForeground(table.getSelectionForeground());
			label.setBackground(table.getSelectionBackground());
		} else {
			label.setForeground(table.getForeground());
			label.setBackground(table.getBackground());
		}
		
		return label;

	}

}
