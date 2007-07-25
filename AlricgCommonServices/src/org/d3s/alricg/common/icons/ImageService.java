/*
 * Created 23.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.common.icons;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import org.d3s.alricg.common.Activator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * @author Vincent
 *
 */
public class ImageService {
	protected static String BASE_IMAGEPATH;
	private static final ImageService self = new ImageService();
	public static ImageDescriptor testBild;
	
	private ImageService() {
		
		BASE_IMAGEPATH = Platform.getInstanceLocation().getURL() + "icons/";
		
		try {
			testBild = ImageDescriptor.createFromURL(new URL(BASE_IMAGEPATH + "controls/table_relationship.png"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ImageService getImageService() {
		return self;
	}
	
	protected static ImageDescriptor loadImage(String fileUrl) {
		
		try {
			URL url = new URL(fileUrl);
			
			if (!new File(url.getFile()).exists()) {
				throw new FileNotFoundException(
						"File konnte nicht gefunden werden: " + fileUrl);
			}
			
			return ImageDescriptor.createFromURL(url);
			
		} catch (Exception e) {
			Activator.logger.log( 
					Level.SEVERE, 
					"Folgendes Image konnte nicht geladen werden: " +
					"\"" + fileUrl + "\"" +
					" Lade default-Image.", e);
			return PlatformUI.getWorkbench().getSharedImages().
					getImageDescriptor(ISharedImages.IMG_OBJS_WARN_TSK);
		}
	}
}
