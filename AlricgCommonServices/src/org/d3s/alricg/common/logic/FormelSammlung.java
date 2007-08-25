/*
 * Created 21.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.common.logic;

/**
 * @author Vincent
 *
 */
public class FormelSammlung {
	public final static int GP_ZU_AP_FAKTOR = 50;
	
	/**
	 * @param Ap (Generierungs-) Abenteuerpunkte
	 * @return Die Anzahl GP, welche den AP entsprechen
	 */
	public static double getGpFromAp(int ap) {
		return (new Double(ap).doubleValue() / GP_ZU_AP_FAKTOR);
	}
	
	/**
	 * @param gp Generierungspunkte
	 * @return Die Anzahl (Generierungs-) Abenteuerpunkte, welche den GP entsprechen
	 */
	public static int getApFromGp(double gp) {
		return (int) (gp * GP_ZU_AP_FAKTOR);
	}
	
	/**
	 * @param gp Generierungspunkte
	 * @return Die Anzahl (Generierungs-) Abenteuerpunkte, welche den GP entsprechen
	 */
	public static int getApFromGp(int gp) {
		return gp * GP_ZU_AP_FAKTOR;
	}
	
}
