/*
 * Created on 23.09.2005 / 17:55:43
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.gui.komponenten.table.renderer;

import javax.swing.Icon;

/**
 * Dient als Wapper für den "ImageTextRenderer". Dieser benötigt als Parameter eine Objekt dieser 
 * Klasse.
 * @author Vincent
 */
public class ImageTextObject {
	private String text;
	private Icon icon;
	
	/**
	 * Konstruktor
	 */
	public ImageTextObject() {
		
	}
	
	/**
	 * Konstruktor
	 * @param text Der Text
	 * @param icon Das Icon zu dem Text, kann auch "null" sein.
	 */
	public ImageTextObject(String text, Icon icon) {
		this.text = text;
		this.icon = icon;
	}
	
	/**
	 * Setzt den Text neu
	 * @param text Neuer Wert für den Text
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Setzt das Icon neu
	 * @param icon Das neue Icon-Objekt (kann auch "null" sein, wenn es kein Icon gibt)
 	 */
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	
	/**
	 * @return Der Text dieses Objekts
	 */
	public String getText() {
		return this.text;
	}
	
	/**
	 * @retun Das Icon dieses Objekts, kann auch "null" sein wenn kein Icon existiert
	 */
	public Icon getIcon() {
		return this.icon;
	}
}
