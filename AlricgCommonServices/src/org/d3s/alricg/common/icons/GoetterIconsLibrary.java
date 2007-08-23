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
import java.util.List;

import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Werte.MagieMerkmal;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * @author Vincent
 */
public class GoetterIconsLibrary extends AbstractIconsLibrary<Gottheit> {
	private static final String PATH = ImageService.BASE_IMAGEPATH + "charElemente/goetterSymbole/";
	
	private static GoetterIconsLibrary self = new GoetterIconsLibrary();
	
	// Protected Constructor
	private GoetterIconsLibrary() {}
	
	public static GoetterIconsLibrary getInstance() {
		return self;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.icons.AbstractIconsLibrary#getImageDescriptor16(java.lang.Object)
	 */
	@Override
	protected ImageDescriptor getImageDescriptor16(Gottheit element) {
		return ImageService.loadImage(GoetterIconsLibrary.PATH + element.getGottheitImage() + "_16.png");
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.icons.AbstractIconsLibrary#getImageDescriptor24(java.lang.Object)
	 */
	@Override
	public ImageDescriptor getImageDescriptor24(Gottheit element) {
		return ImageService.loadImage(GoetterIconsLibrary.PATH + element.getGottheitImage() + "_24.png");
	}

	
}
