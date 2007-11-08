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

import org.d3s.alricg.common.logic.FormelSammlung;
import org.d3s.alricg.common.logic.Prozessor;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.links.Link;
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
	private HashMap<EigenschaftEnum, Link> eigenschaftHash;
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
		
		switch (eigen) {
		
		case LEP: return FormelSammlung.berechneLep(
				eigenschaftHash.get(EigenschaftEnum.KO).getWert(),
				eigenschaftHash.get(EigenschaftEnum.KK).getWert()		
			)
			+ eigenschaftHash.get(EigenschaftEnum.LEP).getWert();
	
		case ASP: return FormelSammlung.berechneAsp(
				eigenschaftHash.get(EigenschaftEnum.MU).getWert(),
				eigenschaftHash.get(EigenschaftEnum.IN).getWert(),
				eigenschaftHash.get(EigenschaftEnum.CH).getWert()
			)
			+ eigenschaftHash.get(EigenschaftEnum.ASP).getWert();
			
		case AUP: return FormelSammlung.berechneAup(
				eigenschaftHash.get(EigenschaftEnum.MU).getWert(),
				eigenschaftHash.get(EigenschaftEnum.KO).getWert(),
				eigenschaftHash.get(EigenschaftEnum.GE).getWert()
			)
			+ eigenschaftHash.get(EigenschaftEnum.AUP).getWert();
		
		case AT: return FormelSammlung.berechneAtBasis(
				eigenschaftHash.get(EigenschaftEnum.MU).getWert(),
				eigenschaftHash.get(EigenschaftEnum.GE).getWert(),
				eigenschaftHash.get(EigenschaftEnum.KK).getWert()
			)
			+ eigenschaftHash.get(EigenschaftEnum.AT).getWert();
				
		case PA: return FormelSammlung.berechnePaBasis(
				eigenschaftHash.get(EigenschaftEnum.IN).getWert(),
				eigenschaftHash.get(EigenschaftEnum.GE).getWert(),
				eigenschaftHash.get(EigenschaftEnum.KK).getWert()
			) 
			+ eigenschaftHash.get(EigenschaftEnum.PA).getWert();
		
		case FK: return FormelSammlung.berechneFkBasis(
				eigenschaftHash.get(EigenschaftEnum.IN).getWert(),
				eigenschaftHash.get(EigenschaftEnum.FF).getWert(),
				eigenschaftHash.get(EigenschaftEnum.KK).getWert()
			) 
			+ eigenschaftHash.get(EigenschaftEnum.FK).getWert();
			
		case INI: return FormelSammlung.berechneINI(
				eigenschaftHash.get(EigenschaftEnum.MU).getWert(),
				eigenschaftHash.get(EigenschaftEnum.IN).getWert(),
				eigenschaftHash.get(EigenschaftEnum.GE).getWert()
			) 
			+ eigenschaftHash.get(EigenschaftEnum.INI).getWert();
			
		case MR: return FormelSammlung.berechneMR(
				eigenschaftHash.get(EigenschaftEnum.MU).getWert(),
				eigenschaftHash.get(EigenschaftEnum.KL).getWert(),
				eigenschaftHash.get(EigenschaftEnum.KO).getWert()
			) 
			+ eigenschaftHash.get(EigenschaftEnum.MR).getWert();
			
		default: // Es handelt sich um MU - KK, KE, GS oder SO
			// Da hier nicht errechnet werden muß, werden die Werte direkt geliefert
			return eigenschaftHash.get(eigen).getWert();
		}
	}
	
	/**
	 * @param prozessorHash the prozessorHash to set
	 */
	public void setProzessorHash(HashMap<Class, Prozessor> prozessorHash) {
		this.prozessorHash = prozessorHash;
		
		final List<Link<Eigenschaft>> tmpEig = prozessorHash.get(Eigenschaft.class).getUnmodifiableList();
		eigenschaftHash = new HashMap<EigenschaftEnum, Link>();
		
		for (int i = 0; i < tmpEig.size(); i++) {
			eigenschaftHash.put(tmpEig.get(i).getZiel().getEigenschaftEnum(), tmpEig.get(i));
		}
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
