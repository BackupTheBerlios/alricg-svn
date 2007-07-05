/*
 * Created on 05.06.2006 / 17:55:05
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.generierung.extended;

import java.util.List;

import org.d3s.alricg.charKomponenten.EigenschaftEnum;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.common.HeldenLink;

/**
 * <u>Beschreibung:</u><br> 
 * Erweiterte Funktionen f�r Talente. Diese Funktionen bietet der Standard-Prozessor
 * f�r Talente nicht an, sie werden jedoch ben�tigt. Durch die Deklaration im
 * Interface k�nnen die Funktionen ebenfalls �ber den LinkProzessorFront genutzt werden.
 * 
 * @author V. Strelow
 */
public interface ExtendedProzessorTalent {
	
    /**
	 * @return Liefert die Talente die aktiviert wurden.
	 */
	public List<Talent> getAktivierteTalente();
	
	/**
	 * �berpr�ft ob ein Talent aktiviert wurde!
	 * @param tal Das Talent das gepr�ft wird
	 * @return true - Dieses Talent ist als aktiviert enthalten,
	 * 		false - Dieses Talent ist NICHT aktiviert (egal ob enthalten oder nicht)
	 */
	public boolean isAktiviert(Talent tal);
	
	/**
	 * Aktiviert ein Talent wenn n�tig, bzw. entfernd es von der Liste der 
	 */
	public void pruefeTalentAktivierung(GeneratorLink genLink);
	
	/**
	 * Liefert alle Links zu Talenten, die in der Probe auf mindesten einmal die 
	 * gesuchte Eigenschaft gepr�ft werden. D.h.: In den 3 Eigenschaften der Probe
	 * ist bei diesen Talenten die gesuchte Eigenschaft enthalten!
	 * (Ist wichtig f�r die Bestimmung des Min-Wertes bei Eigenschaften)
	 * @param eigEnum Die gesuchte Eigenschaft
	 * @return Alle Talente, die auf die EIgenschaft "eigEnum" geprobt werden
	 */
	public List<? extends HeldenLink> getTalentList(EigenschaftEnum eigEnum);
	
	/**
	 * Setzt, wie viele Talente aktiviert werden d�rfen. (f�r SR "Veteran")
	 * @param maxAktivierungen Die neue Anzahl der maximal aktivierbaren Talente
	 */
	public void setMaxTalentAktivierung(int maxAktivierungen);

}
