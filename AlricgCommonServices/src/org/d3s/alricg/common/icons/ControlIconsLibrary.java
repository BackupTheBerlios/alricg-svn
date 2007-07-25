/*
 * Created 23.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.common.icons;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author Vincent
 *
 */
public enum ControlIconsLibrary {

	info("information.png"),
	
// Tabellen
	swapTree_Table("table_relationship.png"),
	filterTable("table_sort.png"),
	table("table.png");
	
	private ImageDescriptor icon16;
	private static final String PATH = "controls/"; // "ImageService.BASE_IMAGEPATH" ging leider nicht
	
	private ControlIconsLibrary(String fileName) {
		icon16 = ImageService.loadImage(ImageService.BASE_IMAGEPATH + PATH + fileName);
	}

	/**
	 * @return Das zum Merkmal zugehörige Icon in 16x16 Pixeln
	 */
	public ImageDescriptor getImageDescriptor() {
		return icon16;
	}

}
