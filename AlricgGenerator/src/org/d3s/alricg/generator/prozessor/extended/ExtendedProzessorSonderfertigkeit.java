/*
 * Created 12.07.2008
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.prozessor.extended;

import org.d3s.alricg.generator.prozessor.GeneratorLink;

/**
 * @author Vincent
 *
 */
public interface ExtendedProzessorSonderfertigkeit {
	
	/**
	 * Pr�ft, ob ein Link mit AP oder GP bezahlt wurde
	 * @param link Zu pr�fender Link
	 * @return true - mit AP bezahlt, ansonsten mit GP bezahlt
	 */
	public boolean isLinkMitApBezahlt(GeneratorLink link);
	
	/**
	 * Setzt ob die n�chsten hinzugef�gten SF mit AP oder GP bezahlt werden
	 * @param mitAP true - ab jetzt mit AP bezajlen, ansonsten mit GP
	 */
	public void setMitApBezahlt(boolean mitAP);
	
	/**
	 * @return true - neue SF werden mit AP bezahlt, ansonsten false
	 */
	public boolean getMitApBezahlt();
	
	
	/**
	 * �ndert die Art der Bezahlung f�r eine SF
	 * @param link Der Link dessen Bezahlung ge�ndert werden soll
	 * @param mitAP Die neue Art der Bezhalung
	 */
	public void changeMitAPBezahlt(GeneratorLink link, boolean mitAP);
	
	/**
	 * @return Die gesamten GP-Kosten f�r Sonderfertigkeiten, die mit GP bezahlt wurden
	 */
	public int getApGesamtKosten();
	
	/**
	 * @return Die gesamten AP-Kosten f�r Sonderfertigkeiten, die mit AP bezahlt wurden
	 */
	public double getGpGesamtKosten();
	
}
