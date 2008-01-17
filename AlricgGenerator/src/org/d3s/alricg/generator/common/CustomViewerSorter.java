/*
 * Created 31.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.common;

import org.d3s.alricg.editor.common.CustomColumnViewerSorter.CreatableViewerSorter;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.links.Link;

/**
 * @author Vincent
 *
 */
public class CustomViewerSorter {

	public static class LinkWertSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return ((Link) ((TreeOrTableObject) obj).getValue()).getWert();
		}
	}
	
	public static class LinkWertModiSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return ((GeneratorLink) ((TreeOrTableObject) obj).getValue()).getWertModis();
		}
	}
	
	// Sozialstatus einer Herkunft
	public static class HerkunftSOSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return ((Rasse) getCharElement(obj)).getSoMax();
		}
	}
}
