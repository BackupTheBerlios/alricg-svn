/*
 * Created 23.07.2007
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

import org.d3s.alricg.store.charElemente.Werte.MagieMerkmal;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * @author Vincent
 */
public class MagieIconsLibrary extends AbstractIconsLibrary<MagieMerkmal> {
	private static final String PATH = ImageService.BASE_IMAGEPATH + "charElemente/zauberSymbole/";
	
	private static MagieIconsLibrary self = new MagieIconsLibrary();
	
	// Protected Constructor
	private MagieIconsLibrary() {}
	
	public static MagieIconsLibrary getInstance() {
		return self;
	}

	/**
	 * @return ImageDescriptor eines 24x24 Bildes für das gewünscht Merkmal
	 */
	public ImageDescriptor getImageDescriptor24(MagieMerkmal merkmal) {
		return getMatchingIcon(merkmal).getImage24();
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.icons.AbstractIconsLibrary#getImageDescriptor16(java.lang.Object)
	 */
	@Override
	protected ImageDescriptor getImageDescriptor16(MagieMerkmal merkmale) {
		return getMatchingIcon(merkmale).getImage16();
	}

	protected Icons getMatchingIcon(MagieMerkmal merkmale) {
		switch(merkmale) {
			case antimagie: return Icons.antimagie;
			case beschwoerung: return Icons.beschwoerung;
			case daemonisch:
			case daemonischBlakharaz:
			case daemonischBelhalhar:
			case daemonischCharyptoroth:
			case daemonischLolgramoth:
			case daemonischThargunitoth:
			case daemonischAmazeroth:
			case daemonischBelshirash:
			case daemonischAsfaloth:
			case daemonischTasfarelel:
			case daemonischBelzhorash:
			case daemonischAgrimoth:
			case daemonischBelkelel: return Icons.daemonisch;
			case eigenschaften: return Icons.eigenschaften;
			case einfluss: return Icons.einfluss;
			case elementar: return Icons.elementar;
			case elementarFeuer: return Icons.elementarFeuer;
			case elementarWasser: return Icons.elementarWasser;
			case elementarLuft: return Icons.elementarLuft;
			case elementarErz: return Icons.elementarErz;
			case elementarHumus: return Icons.elementarHumus;
			case elementarEis: return Icons.elementarEis;
			case form: return Icons.form;
			case geisterwesen: return Icons.geisterwesen;
			case heilung: return Icons.heilung;
			case hellsicht: return Icons.hellsicht;
			case herbeirufung: return Icons.herbeirufung;
			case herrschaft: return Icons.herrschaft;
			case illusion: return Icons.illusion;
			case kraft: return Icons.kraft;
			case limbus: return Icons.limbus;
			case metamagie: return Icons.metamagie;
			case objekt: return Icons.objekt;
			case schaden: return Icons.schaden;
			case telekinese: return Icons.telekinese;
			case temporal: return Icons.temporal;
			case umwelt: return Icons.umwelt;
			case verstaendigung: return Icons.verstaendigung;
			default:
				throw new IllegalArgumentException("Konnte MagieMerkmal nicht finden!");
			
		}
	}
	
	private enum Icons {
		// TODO MagieMerkmal korregieren
		antimagie("antimagie"),
		beschwoerung("beschwoerung"),
		daemonisch("daemonisch"),
		eigenschaften("eigenschaften"),
		einfluss("einfluss"),
		elementar("elementar"),
		elementarFeuer("elementFeuer"),
		elementarWasser("elementWasser"),
		elementarLuft("elementLuft"),
		elementarErz("elementErz"),
		elementarHumus("elementHumus"),
		elementarEis("elementEis"),
		form("form"),
		geisterwesen("geisterwesen"),
		heilung("heilung"),
		hellsicht("hellsicht"),
		herbeirufung("herbeirufung"),
		herrschaft("herrschaft"),
		illusion("illusion"),
		kraft("kraft"),
		limbus("limbus"),
		metamagie("metamagie"),
		objekt("objekt"),
		schaden("schaden"),
		telekinese("telekinese"),
		temporal("temporal"),
		umwelt("umwelt"),
		verstaendigung("verstaendigung");		
		

		private ImageDescriptor icon16;
		private ImageDescriptor icon24;

		private Icons(String fileName) {
			icon16 = ImageService.loadImage(MagieIconsLibrary.PATH + fileName + "_16.png");
			icon24 = ImageService.loadImage(MagieIconsLibrary.PATH + fileName + "_24.png");
		}

		/**
		 * @return Das zum Merkmal zugehörige Icon in 16x16 Pixeln
		 */
		public ImageDescriptor getImage16() {
			return icon16;
		}

		/**
		 * @return Das zum Merkmal zugehörige Icon in 24x24 Pixeln
		 */
		public ImageDescriptor getImage24() {
			return icon24;
		}
	}
}
