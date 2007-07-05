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
 * Erweiterte Funktionen für Talente. Diese Funktionen bietet der Standard-Prozessor
 * für Talente nicht an, sie werden jedoch benötigt. Durch die Deklaration im
 * Interface können die Funktionen ebenfalls über den LinkProzessorFront genutzt werden.
 * 
 * @author V. Strelow
 */
public interface ExtendedProzessorTalent {
	
    /**
	 * @return Liefert die Talente die aktiviert wurden.
	 */
	public List<Talent> getAktivierteTalente();
	
	/**
	 * Überprüft ob ein Talent aktiviert wurde!
	 * @param tal Das Talent das geprüft wird
	 * @return true - Dieses Talent ist als aktiviert enthalten,
	 * 		false - Dieses Talent ist NICHT aktiviert (egal ob enthalten oder nicht)
	 */
	public boolean isAktiviert(Talent tal);
	
	/**
	 * Aktiviert ein Talent wenn nötig, bzw. entfernd es von der Liste der 
	 */
	public void pruefeTalentAktivierung(GeneratorLink genLink);
	
	/**
	 * Liefert alle Links zu Talenten, die in der Probe auf mindesten einmal die 
	 * gesuchte Eigenschaft geprüft werden. D.h.: In den 3 Eigenschaften der Probe
	 * ist bei diesen Talenten die gesuchte Eigenschaft enthalten!
	 * (Ist wichtig für die Bestimmung des Min-Wertes bei Eigenschaften)
	 * @param eigEnum Die gesuchte Eigenschaft
	 * @return Alle Talente, die auf die EIgenschaft "eigEnum" geprobt werden
	 */
	public List<? extends HeldenLink> getTalentList(EigenschaftEnum eigEnum);
	
	/**
	 * Setzt, wie viele Talente aktiviert werden dürfen. (für SR "Veteran")
	 * @param maxAktivierungen Die neue Anzahl der maximal aktivierbaren Talente
	 */
	public void setMaxTalentAktivierung(int maxAktivierungen);

}
