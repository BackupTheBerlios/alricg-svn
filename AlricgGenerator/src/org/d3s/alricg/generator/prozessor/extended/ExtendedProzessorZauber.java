/*
 * Created on 21.06.2006 / 20:00:23
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.generator.prozessor.extended;

import java.util.Collection;
import java.util.List;

import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.held.HeldenLink;

/**
 * <u>Beschreibung:</u><br> 
 * Erweiterte Funktionen für Zauber. Diese Funktionen bietet der Standard-Prozessor
 * für Zauber nicht an, sie werden jedoch benötigt. Durch die Deklaration im
 * Interface können die Funktionen ebenfalls über den LinkProzessorFront genutzt werden.

 * @author V. Strelow
 */
public interface ExtendedProzessorZauber {

	/**
	 * Liefert alle Links zu Zaubern, die in der Probe auf mindesten einmal die 
	 * gesuchte Eigenschaft geprüft werden. D.h.: In den 3 Eigenschaften der Probe
	 * ist bei diesen Zaubern die gesuchte Eigenschaft enthalten!
	 * (Ist wichtig für die Bestimmung des Min-Wertes bei Eigenschaften)
	 * @param eigEnum Die gesuchte Eigenschaft
	 * @return Alle Zauber, die auf die Eigenschaft "eigEnum" geprobt werden
	 */
	public List<? extends HeldenLink> getZauberList(EigenschaftEnum eigEnum);
	
	/**
	 * Fuegt einen Zauber in die Liste der Hauszauber ein.
	 * 
	 * @param hauszauber der neu zur Liste hinzukommen soll.
	 */
	public void addHauszauber( Link hauszauber );
	
	/**
	 * Entfernt einen Hauszauber aus der Liste der Hauszauber.
	 * 
	 * @param hauszauber der enfernt werden soll.
	 */
	public void removeHauszauber( Link hauszauber );
	
	/**
	 * @return Die Hauszauber eines Helden.
	 */
	public Collection< Link > getHauszauber();
	
	/**
	 * Bestimmt die Hauszauber fuer einen Helden.
	 * 
	 * @param zauber Alle Hauszauber eines Helden.
	 */
	public void setHauszauber( Collection< Link > zauber );
	
	/**
	 * @return alle Zauber die ein Held bei der Erschaffung mittels Talent-GP aktivieren kann. 
	 * 			Das sind meist alle Zauber mit eine Verbreitung von 4 und mehr besitzen.
	 * 			Eine Liste aller moeglichen Zauber ist bei jeder Profession angegeben.
	 * 			Siehe "Aventurische Zauberer" S 21.  
	 */
	public Collection< Link > getMoeglicheZauber();
	
	/**
	 * Bestimmt die Zauber die bei der Erschaffung mittels Talent-GP aktiviert werden koennen. 
	 * 			Das sind meist alle Zauber mit eine Verbreitung von 4 und mehr besitzen.
	 * 			Eine Liste aller moeglichen Zauber ist bei jeder Profession angegeben.
	 * 			Siehe "Aventurische Zauberer" S 21.  
	 *  
	 * @param zauber Alle Zauber die mittels Talent-GP aktiviert werden duerfen.
	 */
	public void setMoeglicheZauber( Collection< Link > zauber );
}
