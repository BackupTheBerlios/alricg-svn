/*
 * Created 21.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.common.icons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * @author Vincent
 *
 */
public abstract class AbstractIconsLibrary<T> {
	protected final List<Object> consumerList = new ArrayList<Object>();
	protected final HashMap<T, Image> imageHash = new HashMap<T, Image>();

	/**
	 * Jedes Object das Bilder von den MagieIconsLibrary nutzt, muss sich hier anmelden
	 * @param consumer
	 */
	public void addConsumer(Object consumer) {
		consumerList.add(consumer);
	}
	
	/**
	 * Entfernd einen Consumer von der Liste. Gibt entsprechende Bild-Resourcen wieder frei.
	 * @param consumer
	 */
	public void removeConsumer(Object consumer) {
		consumerList.remove(consumer);
		
		// Wenn kein Benutzer mehr für die Bilder angemeldet ist, alle disposen
		if (consumerList.size() == 0) {
			Iterator<Image> imgIt = imageHash.values().iterator();
			
			while (imgIt.hasNext()) {
				imgIt.next().dispose();
			}
		}
		imageHash.clear();
	}
	
	/**
	 * @return ImageDescriptor eines 24x24 Bildes für das gewünscht Merkmal
	 */
	public abstract ImageDescriptor getImageDescriptor24(T element);
	/**
	 * @return Image des gewünschten Merkmals
	 */
	public Image getImage16(T element, Object consumer) {
		if (!consumerList.contains(consumer)) {
			throw new IllegalArgumentException("Consumer müssen sich erst anmelden!");
		}
		
		if (!imageHash.containsKey(element)) {
			imageHash.put(element, getImageDescriptor16(element).createImage());
		}
		
		return imageHash.get(element);
	}
	
	
	protected abstract ImageDescriptor getImageDescriptor16(T element);
}
