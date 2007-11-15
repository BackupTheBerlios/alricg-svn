/*
 * Created 31.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.common;

import org.d3s.alricg.common.CommonUtils;
import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Talent;
import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * @author Vincent
 */
public class CustomLabelProvider {
	/**
	 * Zeigt Modis für eine Stufe an
	 */
	public static class LinkWertModiProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			if ( ((TreeOrTableObject) element).getValue() instanceof GeneratorLink) {
				final int modi = ((GeneratorLink) ((TreeOrTableObject) element).getValue()).getWertModis();
				if (modi == 0) {
					return "-";
				} else if (modi > 0) {
					return "+" + 
							((GeneratorLink) ((TreeOrTableObject) element).getValue()).getWertModis();
				} else {
					return String.valueOf( 
						((GeneratorLink) ((TreeOrTableObject) element).getValue()).getWertModis() 
					);
				}
			}
			return "";
		}
	}
	
	/**
	 * Zeigt Modis für eine Stufe an
	 */
	public static class LinkKostenProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			if ( ((TreeOrTableObject) element).getValue() instanceof GeneratorLink) {
				return  CommonUtils.doubleToString(
						((GeneratorLink) ((TreeOrTableObject) element).getValue()).getKosten());
			}
			return "";
		}
	}
	
	/**
	 * Liefert die Art eines Talents
	 */
	public static class TalentArtProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem != null) {
				return ((Talent) charElem).getArt().toString();
			}
			return ""; //$NON-NLS-1$
		}
	}
}
