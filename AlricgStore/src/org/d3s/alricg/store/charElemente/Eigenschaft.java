/*
 * Created on 20.02.2005 / 23:26:55
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected an under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente;

import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;


/**
 * <u>Beschreibung:</u><br> 
 * Wrapper Klasse um eine "Eigenschaften-Enum" in ein CharElement zu mappen. 
 * Sinnvoll um eine Eigenschaft z.B. in ein LinkID Objekt zu packen, und so auch in 
 * eine Auswahl.
 * @author V. Strelow
 */
public class Eigenschaft extends CharElement {
	private EigenschaftEnum eigenschaftEnum;

	/**
	 * @return Liefert die zugehörige EigenschaftEnum / Jede Eigenschaft ist 
	 * mit einer EigenschaftEnum verbunden.
	 */
	public EigenschaftEnum getEigenschaftEnum() {
		return eigenschaftEnum;
	}
	
	/**
	 * @param eigenschaftEnum the eigenschaftEnum to set
	 */
	public void setEigenschaftEnum(EigenschaftEnum eigenschaftEnum) {
		this.eigenschaftEnum = eigenschaftEnum;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getId()
	 */
	@Override
	public String getId() {
		return eigenschaftEnum.getValue();
	}

    /* (non-Javadoc) Methode überschrieben
     * @see org.d3s.alricg.charKomponenten.CharElement#getName()
     */
	@Override
    public String getName() {
        return eigenschaftEnum.getBezeichnung();
    }
    
    /* (non-Javadoc) Methode überschrieben
     * @see java.lang.Object#toString()
     */
	@Override
    public String toString() {
    	return eigenschaftEnum.getBezeichnung();
    }

}
