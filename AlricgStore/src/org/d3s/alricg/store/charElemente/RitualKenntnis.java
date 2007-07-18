/*
 * Created 14.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.charElemente;

/**
 * Repr�sentiert die Ritualkenntnis von Magiern und Schamenen.
 * @author Vincent
 */
public class RitualKenntnis extends Faehigkeit {
	private Repraesentation[] repraesentation;

	/**
	 * Gibt an zu welcher Repr�sentation (auch Schamanische) diese RitualKenntnis
	 * urspr�nglich geh�rt.
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