/*
 * Created on 24.06.2006 / 15:40:18
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.generierung.extended;

/**
 * <u>Beschreibung:</u><br> 
 * Erweiterte Funktionen für Eigenschaften. Diese Funktionen bietet der Standard-Prozessor
 * für Eigenschaften nicht an, sie werden jedoch benötigt. Durch die Deklaration im
 * Interface können die Funktionen ebenfalls über den LinkProzessorFront genutzt werden.
 *  
 * @author V. Strelow
 */
public interface ExtendedProzessorEigenschaft {
	
    /**
     * Die Kosten für die Talent-GP
     * 
     * @return Die Kosten die mit Talent GP bezahlt werden (ASP, LEP, AUP)
     * @see getGesamtKosten()
     */
    public int getGesamtTalentGpKosten();
}
