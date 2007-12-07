/*
 * Created on 24.06.2006 / 15:40:18
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.generator.prozessor.extended;

import org.d3s.alricg.common.charakter.ExtendedProzessorEigenschaftCommon;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;

/**
 * <u>Beschreibung:</u><br> 
 * Erweiterte Funktionen f�r Eigenschaften. Diese Funktionen bietet der Standard-Prozessor
 * f�r Eigenschaften nicht an, sie werden jedoch ben�tigt. Durch die Deklaration im
 * Interface k�nnen die Funktionen ebenfalls �ber den LinkProzessorFront genutzt werden.
 *  
 * @author V. Strelow
 */
public interface ExtendedProzessorEigenschaft extends ExtendedProzessorEigenschaftCommon {
	
    /**
     * Die Kosten f�r die Talent-GP
     * 
     * @return Die Kosten die mit Talent GP bezahlt werden (ASP, LEP, AUP)
     * @see getGesamtKosten()
     */
    public int getGesamtTalentGpKosten();
    
    /**
     * @param eigen EigenschaftEnum der gew�nschten Eigenschaft
     * @return Liefert zu einer EigenschaftEnum den passenden Link
     */
    public GeneratorLink<Eigenschaft> getEigenschaftsLink(EigenschaftEnum eigen);
    
    /**
     * Berechnet wie viele Punkte zu einem berechneten Wert maximal hinzugekauft werden 
     * d�rfen. Ist nur f�r Lep, Aup, Asp m�glich.
     * @param eigen Die EigenschaftEnum der gew�nschten Eigenschaft
     * @return Maximal m�glicher Zukauf
     */
    public int getMaxZukauf(EigenschaftEnum eigen);
    
    /**
     * Berechnet wie viele Punkte zu einem berechneten Wert minimal hinzugekauft werden 
     * m�ssen. Ist nur f�r Lep, Aup, Asp m�glich.
     * @param eigen Die EigenschaftEnum der gew�nschten Eigenschaft
     * @return Minimal m�glicher Zukauf
     */
    public int getMinZukauf(EigenschaftEnum eigen);
}
