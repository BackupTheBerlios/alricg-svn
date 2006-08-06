/*
 * Created on 13.04.2005 / 16:43:28
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.komponenten;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * <u>Beschreibung: </u> <br>
 * Ermöglicht es mehrere Icons auf einen JLabel/JButton/cell-renderer darzustellen.
 * 
 * @author Jan Bösenberg (www.javalobby.org)
 * @author V. Strelow
 */
public class MultiIcon implements Icon {
	public static final int ALIGNMENT_HORIZONTAL = 1;
	public static final int ALIGNMENT_VERTICAL = 2;
	public static final int ALIGNMENT_STACKED = 3;

	private int gap = 3; // will be ignored for stacked alignment
	private Icon[] icons;
	private int alignment;

	private int width = 0;
	private int height = 0;

	/**
	 * Konstruktur, wählt defaultmäßig "ALIGNMENT_HORIZONTAL" als alignment
	 */
	public MultiIcon() {
		this(ALIGNMENT_HORIZONTAL);
	}

	/**
	 * Konstruktor
	 * 
	 * @param alignment Einer der alignment-Konstanten für die anordnung der Icons
	 */
	public MultiIcon(int alignment) {
		this.alignment = alignment;
	}

	/**
	 * @return Die Anzahl der im MulitIcon enthaltenen Bilder
	 */
	public int getIconCount() {
		if (icons == null) {
			return 0;
		} else {
			return icons.length;
		}
	}

	/**
	 * @param index Index des gewünschten Icons
	 * @return Liefert das Icon an der Stelle "index"
	 */
	public Icon getIconAt(int index) {
		return icons[index];
	}

	/**
	 * Ersetzt ein Icon
	 * @param index Index des alten, zu ersetzenen Icons
	 * @param newIcon Das neue Icon
	 */
	public void replaceIconAt(int index, Icon newIcon) {
		icons[index] = newIcon;
		width = calculateIconWidth();
		height = calculateIconHeight();
	}

	/**
	 * Löscht alle bisherigen Icons und ersetzt diese durch neue Icons
	 * @param newIcons Die neuen Icons des MulitIcons
	 * @author V. Strelow
	 */
	public void replaceAll(Icon[] newIcons) {
		if (newIcons == null || newIcons.length == 0) {
			icons = null;
		} else {
			icons = newIcons;
		}
		
		width = calculateIconWidth();
		height = calculateIconHeight();
	}
	
	/**
	 * Fügt zu MultiIcon ein weiteres Icon hinzu.
	 * @param icon  Das weiter Icon
	 */
	public void addIcon(Icon icon) {
		if (icon == null) {
			return;
		} else {
			if (icons == null) {
				icons = new Icon[] { icon };
			} else {
				Icon[] newIcons = new Icon[icons.length + 1];
				System.arraycopy(icons, 0, newIcons, 0, icons.length);
				newIcons[newIcons.length - 1] = icon;
				icons = newIcons;
			}
			width = calculateIconWidth();
			height = calculateIconHeight();
		}
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (icons == null) {
			return;
		} else if (alignment == ALIGNMENT_VERTICAL) {
			int yIcon = y;
			for (int i = 0; i < icons.length; i++) {
				Icon icon = icons[i];
				int xIcon = x + (width - icon.getIconWidth()) / 2;
				icon.paintIcon(c, g, xIcon, yIcon);
				yIcon += icon.getIconHeight() + gap;
			}
		} else if (alignment == ALIGNMENT_STACKED) {
			for (int i = 0; i < icons.length; i++) {
				Icon icon = icons[i];
				int xIcon = x + (width - icon.getIconWidth()) / 2;
				int yIcon = y + (width - icon.getIconHeight()) / 2;
				icon.paintIcon(c, g, xIcon, yIcon);
			}
		} else {
			assert alignment == ALIGNMENT_HORIZONTAL;
			int xIcon = x;
			for (int i = 0; i < icons.length; i++) {
				Icon icon = icons[i];
				int yIcon = y + (height - icon.getIconHeight()) / 2;
				icon.paintIcon(c, g, xIcon, yIcon);
				xIcon += icon.getIconWidth() + gap;
			}
		}
	}

	/**
	 * Errechnet die width des "gesamt-Icons", also aller Icons zusammen.
	 * @return width des "gesamt-Icons"
	 */
	public int calculateIconWidth() {
		if (icons == null) {
			return 0;
		} else if (alignment == ALIGNMENT_HORIZONTAL) {
			int width = 0;
			for (int i = 0; i < icons.length; i++) {
				width += icons[i].getIconWidth();
			}
			width += gap * (icons.length - 1);
			return width;
		} else {
			int width = 0;
			for (int i = 0; i < icons.length; i++) {
				width = Math.max(width, icons[i].getIconHeight());
			}
			return width;
		}
	}
	
	/**
	 * Errechnet die height des "gesamt-Icons", also aller Icons zusammen.
	 * @return height des "gesamt-Icons"
	 */
	public int calculateIconHeight() {
		if (icons == null) {
			return 0;
		} else if (alignment == ALIGNMENT_VERTICAL) {
			int height = 0;
			for (int i = 0; i < icons.length; i++) {
				height += icons[i].getIconWidth();
			}
			height += gap * (icons.length - 1);
			return height;
		} else {
			int height = 0;
			for (int i = 0; i < icons.length; i++) {
				height = Math.max(height, icons[i].getIconHeight());
			}
			return height;
		}
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.Icon#getIconWidth()
	 */
	public int getIconWidth() {
		return width;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.Icon#getIconHeight()
	 */
	public int getIconHeight() {
		return height;
	}

}
