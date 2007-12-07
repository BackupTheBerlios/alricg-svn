/*
 * Created 18.11.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.common.charakter;

import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;

/**
 * @author Vincent
 *
 */
public interface ExtendedProzessorEigenschaftCommon {
	/**
	 * Ermöglicht einen einfachen Zugriff auf die EigenschaftsWerte. Auf die Eigenschaften 
	 * kann auch mittels "getElementBox(...)" zugegriffen werden, es sollte jedoch diese 
	 * Methode benutzt werden.
	 * VORSICHT: Alle Eigenschaften die errechnet werden, werden nur über diese Methode
	 * 			den korrekten wert liefern, ansonsten nur den Wert der Modis.  
	 * 
	 * @param eigen Die gewünschte Eigenschaft
	 * @return Der Aktuelle Wert dieser Eigenschaft
	 */
    public int getEigenschaftsWert(EigenschaftEnum eigen);
}
