/*
 * Created 20.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.common;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * @author Vincent
 * 
 */
public class CommonUtils {

	/**
	 * Ezeugt aus mehreren Bilder ein neues Bild. Dies wird benötigt um in Tabellen 
	 * mehrere Bilder anzeigen zu können, trotz der "ein Bild" Beschränkung.
	 * 
	 * @param display Display welches diese Bild darstellt.
	 * @param images Bilder die zu einem neuen Bild zusammengefügt werden sollen
	 * @return Ein Bild bestehend aus allen übergebenen Bildern.
	 */
	public static Image createComposedImage(Display display, Image[] images) {
		Image composedImage;
		
		if (images.length == 1) return images[0];
		
		composedImage = createComposedImage(display, images[0], images[1]);
		for (int i = 2; i < images.length; i++) {
			composedImage = createComposedImage(display, composedImage, images[i]);
		}
		
		return composedImage;
	}

	/**
	 * Hilfklasse für "createComposedImage(Display, Image[])". Fügt zwei Bilder zusammen.
	 * 
	 * @see org.d3s.alricg.editor.common.createComposedImage(Display, Image[])
	 * @param display Display welches diese Bild darstellt.
	 * @param image1 
	 * @param image2
	 * @return Bild bestehend aus image1 und image2
	 */
	private static Image createComposedImage(Display display, Image image1, Image image2) {
		final Rectangle size1 = image1.getBounds();
		final Rectangle size2 = image2.getBounds();
		Image image3 = new Image(
				display, 
				size1.width + size2.width, 
				Math.max(size1.height, size2.height));
		final GC gc = new GC(image3);
		gc.drawImage(image1, 0, 0);
		gc.drawImage(image2, size1.width, 0);
		gc.dispose();
		image1.dispose();
		image2.dispose();
		
		return image3;
	}
}
