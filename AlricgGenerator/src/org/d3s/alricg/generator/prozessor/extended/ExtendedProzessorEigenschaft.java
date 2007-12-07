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
 * Erweiterte Funktionen für Eigenschaften. Diese Funktionen bietet der Standard-Prozessor
 * für Eigenschaften nicht an, sie werden jedoch benötigt. Durch die Deklaration im
 * Interface können die Funktionen ebenfalls über den LinkProzessorFront genutzt werden.
 *  
 * @author V. Strelow
 */
public interface ExtendedProzessorEigenschaft extends ExtendedProzessorEigenschaftCommon {
	
    /**
     * Die Kosten für die Talent-GP
     * 
     * @return Die Kosten die mit Talent GP bezahlt werden (ASP, LEP, AUP)
     * @see getGesamtKosten()
     */
    public int getGesamtTalentGpKosten();
    
    /**
     * @param eigen EigenschaftEnum der gewünschten Eigenschaft
     * @return Liefert zu einer EigenschaftEnum den passenden Link
     */
    public GeneratorLink<Eigenschaft> getEigenschaftsLink(EigenschaftEnum eigen);
    
    /**
     * Berechnet wie viele Punkte zu einem berechneten Wert maximal hinzugekauft werden 
     * dürfen. Ist nur für Lep, Aup, Asp möglich.
     * @param eigen Die EigenschaftEnum der gewünschten Eigenschaft
     * @return Maximal möglicher Zukauf
     */
    public int getMaxZukauf(EigenschaftEnum eigen);
    
    /**
     * Berechnet wie viele Punkte zu einem berechneten Wert minimal hinzugekauft werden 
     * müssen. Ist nur für Lep, Aup, Asp möglich.
     * @param eigen Die EigenschaftEnum der gewünschten Eigenschaft
     * @return Minimal möglicher Zukauf
     */
    public int getMinZukauf(EigenschaftEnum eigen);
}
