/*
 * Created on 21.06.2006 / 20:00:23
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.generierung.extended;

import java.util.List;

import org.d3s.alricg.charKomponenten.EigenschaftEnum;
import org.d3s.alricg.prozessor.common.HeldenLink;

/**
 * <u>Beschreibung:</u><br> 
 * Erweiterte Funktionen f�r Zauber. Diese Funktionen bietet der Standard-Prozessor
 * f�r Zauber nicht an, sie werden jedoch ben�tigt. Durch die Deklaration im
 * Interface k�nnen die Funktionen ebenfalls �ber den LinkProzessorFront genutzt werden.

 * @author V. Strelow
 */
public interface ExtendedProzessorZauber {
	/**
	 * Liefert alle Links zu Zaubern, die in der Probe auf mindesten einmal die 
	 * gesuchte Eigenschaft gepr�ft werden. D.h.: In den 3 Eigenschaften der Probe
	 * ist bei diesen Zaubern die gesuchte Eigenschaft enthalten!
	 * (Ist wichtig f�r die Bestimmung des Min-Wertes bei Eigenschaften)
	 * @param eigEnum Die gesuchte Eigenschaft
	 * @return Alle Zauber, die auf die Eigenschaft "eigEnum" geprobt werden
	 */
	public List<? extends HeldenLink> getZauberList(EigenschaftEnum eigEnum);
}
