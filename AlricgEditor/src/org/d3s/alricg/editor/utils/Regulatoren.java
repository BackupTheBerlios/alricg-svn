/*
 * Created 02.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.utils;

import java.util.List;

import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Talent;

/**
 * @author Vincent
 *
 */
public class Regulatoren {
	/**
	 * Schnittstelle welche für Objekte benötigt wird, mit die Methode "buildViewTree"
	 * verarbeitet werden sollen.
	 * @see org.d3s.alricg.editor.common.ViewUtils.buildViewTree(Iterator, Regulator)
	 * @author Vincent
	 */
	public static interface Regulator {
		public Object[] getFirstCategory(CharElement charElement);
		public List<? extends CharElement> getListFromAccessor(XmlAccessor accessor);
	}
	
	public static final Regulator talentRegulator = 
		new Regulator() {
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				return new Object[] { ((Talent) charElement).getSorte() };
			}
	
			@Override
			public List<? extends CharElement> getListFromAccessor(
					XmlAccessor accessor) {
				return accessor.getTalentList();
			}
		};
}
