/*
 * Created on 20.02.2005 / 23:26:55
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected an under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten;

import java.util.logging.Logger;

import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.store.FactoryFinder;


/**
 * <u>Beschreibung:</u><br> 
 * Wrapper Klasse um eine "Eigenschaften-Enum" in ein CharElement zu mappen. 
 * Sinnvoll um eine Eigenschaft z.B. in ein LinkID Objekt zu packen, und so auch in 
 * eine Auswahl.
 * @author V. Strelow
 */
public class Eigenschaft extends CharElement {
    
    /** <code>Eigenschaft</code>'s logger */
    private static final Logger LOG = Logger.getLogger(Eigenschaft.class.getName());
    
	private EigenschaftEnum eigenschaft;
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
	 */
	public CharKomponente getCharKomponente() {
		return CharKomponente.eigenschaft;
	}
	
	
	/**
	 * Konstruktur; id beginnt mit "EIG-" für Eigenschaft
	 * @param id Systemweit eindeutige id
	 */
	public Eigenschaft(String id) {
		eigenschaft = getEigenschaftEnum(id);
	}
	
	/**
	 * @return Liefert die zugehörige EigenschaftEnum / Jede Eigenschaft ist 
	 * mit einer EigenschaftEnum verbunden.
	 */
	public EigenschaftEnum getEigenschaftEnum() {
		return eigenschaft;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getId()
	 */
	public String getId() {
		return eigenschaft.getValue();
	}

    /* (non-Javadoc) Methode überschrieben
     * @see org.d3s.alricg.charKomponenten.CharElement#getName()
     */
    public String getName() {
        return eigenschaft.getBezeichnung();
    }
    
    /* (non-Javadoc) Methode überschrieben
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	return eigenschaft.getBezeichnung();
    }
	
	/**
	 * Findet zu einer Id die zugehörige Eigenschaft-Enum. Setzt außerdem den Text
	 * der Schreibung auf den Beschreibungstext der zugehörigen Enum.
	 * @param id Die ID der Eigenschaft
	 * @return Die enum der Eigenschaft
	 */
	private EigenschaftEnum getEigenschaftEnum(String id) {
		EigenschaftEnum[] eigenArray = EigenschaftEnum.values();
		
		for (int i = 0; i < eigenArray.length; i++) {
			if ( eigenArray[i].getValue().equals(id) ) {
				setBeschreibung(FactoryFinder.find().getLibrary().getLongTxt("Beschreibung " + eigenArray[i].getValue()));
				return eigenArray[i];
			}
		}
		
		LOG.severe("Die ID einer Eigenschaft wurde nicht gefunden!");
		return null;
	}

}
