/*
 * Created 14.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.charElemente;

/**
 * Repräsentiert die Ritualkenntnis von Magiern und Schamenen.
 * Wirklich nötig?
 *  Wie wäre es mit:
 *  	- RitualK als SF mit Repraesentation als Z.Z.?
 *  	- Einziges Problem: Hat eine Stufe und beginnt mit "3"
 * 	    - Bei "Fertigkeit" sollte dann evtl. noch etwas 
 * 			wie "StartStufe" aufgenommen werden (??)
 * 	
 * => Wahrscheinlich ist RK unnötig!!
 * @author Vincent
 */
public class RitualKenntnis extends Faehigkeit {
	private Repraesentation[] repraesentation;

	/**
	 * Gibt an zu welcher Repräsentation (auch Schamanische) diese RitualKenntnis
	 * ursprünglich gehört.
	 * @return the repraesentation
	 */
	public Repraesentation[] getRepraesentation() {
		return repraesentation;
	}

	/**
	 * @param repraesentation the repraesentation to set
	 */
	public void setRepraesentation(Repraesentation[] repraesentation) {
		this.repraesentation = repraesentation;
	}
}
