/*
 * Created 14.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.charElemente;

/**
 * Dieses Interface wurde aus der Herkunft-Klasse heerausgelöst, um es mit JaxB
 * verarbeitun zu können.
 * @author Vincent
 */
public interface HerkunftInterface<VARIANTE extends HerkunftVariante> {
	
	/**
	 * @return Liefert alle Varianten dieser Herkunft
	 */
	public abstract VARIANTE[] getVarianten();
	public abstract void setVarianten(VARIANTE[] variante);
}
