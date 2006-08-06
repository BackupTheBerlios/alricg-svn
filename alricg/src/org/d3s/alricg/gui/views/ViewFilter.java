/*
 * Created on 06.07.2006 / 09:01:13
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.views;

/**
 * <u>Beschreibung:</u><br> 
 *
 * @author V. Strelow
 */
public interface ViewFilter<E> {

	public boolean matchFilter(E Element);
	
}
