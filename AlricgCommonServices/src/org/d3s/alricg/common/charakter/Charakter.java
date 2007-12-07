/*
 * Created 28.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.common.charakter;

import java.util.HashMap;
import java.util.List;

import org.d3s.alricg.common.logic.Prozessor;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.held.CharakterDaten;

/**
 * 
 * @author Vincent
 */
public class Charakter {
	private CharakterDaten charData;
	private SonderregelAdmin sonderregelAdmin;
	private VerbilligteFertigkeitAdmin verbFertigkeitenAdmin;
	private VoraussetzungenAdmin vorausAdmin;
	
	// Die Eigenschaften, wegen ihrer besonderen Bedeutung nochmal hier
	private HashMap<Class, Prozessor> prozessorHash;
	
	public Charakter(CharakterDaten charData) {
		this.charData = charData;
		
		sonderregelAdmin = new SonderregelAdmin(charData);
		verbFertigkeitenAdmin = new VerbilligteFertigkeitAdmin(charData);
		vorausAdmin = new VoraussetzungenAdmin(charData);
	}
	
	/**
	 * VORSICHT: Manche Eigenschaften werden ausgerechnet und sollten über 
	 * "getEigenschaftsWert" abgerufen werden!
	 * @param clazz Die Klasse des gewünschten Elements (Talent, Zauber, usw.)
	 * @return Liefert eine unmodifizierbare Liste von Links mit den gewünschten
	 * 			 Elementen des Charakters
	 */
	public List getElementListe(Class clazz) {
		return prozessorHash.get(clazz).getUnmodifiableList();
	}
	
	public Prozessor getProzessor(Class clazz) {
		return prozessorHash.get(clazz);
	}
	
	/**
	 * Ermöglicht einen einfachen Zugriff auf die Eigenschaften (alle die in 
	 * der enum "EigenschaftEnum" stehen). Auf die Eigenschaften kann auch mittels
	 * "getElementBox(...)" zugegriffen werden, es sollte jedoch diese Methode benutzt 
	 * werden.
	 * VORSICHT: Alle Eigenschaften die errechnet werden, werden nur über diese Methode
	 * 			den korrekten wert liefern, ansonsten nur den Wert der Modis.  
	 * 
	 * @param eigen Die gewünschte Eigenschaft
	 * @return Der Aktuelle Wert dieser Eigenschaft
	 */
	public int getEigenschaftsWert(EigenschaftEnum eigen) {
		return ((ExtendedProzessorEigenschaftCommon) prozessorHash.get(Eigenschaft.class)).getEigenschaftsWert(eigen);
	}
	
	/**
	 * @param prozessorHash the prozessorHash to set
	 */
	public void setProzessorHash(HashMap<Class, Prozessor> prozessorHash) {
		this.prozessorHash = prozessorHash;
	}

	/**
	 * @return the sonderregelAdmin
	 */
	public SonderregelAdmin getSonderregelAdmin() {
		return sonderregelAdmin;
	}

	/**
	 * @return the verbFertigkeitenAdmin
	 */
	public VerbilligteFertigkeitAdmin getVerbFertigkeitenAdmin() {
		return verbFertigkeitenAdmin;
	}

	/**
	 * @return the vorausAdmin
	 */
	public VoraussetzungenAdmin getVorausAdmin() {
		return vorausAdmin;
	}
	
	
}
