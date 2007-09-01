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
	
	/**
	 * Rechnet einen Wert in Kreuzern in die entsprechenden Münzen um
	 * @param wertInKreuzern
	 * @return Ein Array mit den entsprechenden Münzen mit dem Gegenwert
	 * 		von "wertInKreuzern" 
	 * [0] - Kreuzer 
	 * [1] - Heller
	 * [2] - Silbertaler
	 * [3] - Dukaten
	 */
	public static int[] getWertInMuenzen(int wertInKreuzern) {
		int[] muenzen = new int[4];
		
		// Dukaten
		muenzen[3] = (int) (wertInKreuzern / 1000);
		wertInKreuzern -= muenzen[3] * 1000;
		muenzen[2] = (int) (wertInKreuzern / 100);
		wertInKreuzern -= muenzen[2] * 100;
		muenzen[1] = (int) (wertInKreuzern / 10);
		wertInKreuzern -= muenzen[1] * 10;
		muenzen[0] = wertInKreuzern;
		
		return muenzen;
	}
	
	/**
	 * Rechnet eine Sammlung von Münzen in den entsprechenden Gegenwert von Kreuzern um
	 * @param wertInMuenzen 
	 * [0] - Kreuzer 
	 * [1] - Heller
	 * [2] - Silbertaler
	 * [3] - Dukaten
	 * @return Der Gegenwert der Münzen in Kreuzern
	 */
	public static int getWertInKreuzern(int[] wertInMuenzen) {
		int kreuzer = 0;
		
		kreuzer += wertInMuenzen[3] * 1000; // Dukaten
		kreuzer += wertInMuenzen[2] * 100; // Silbertaler
		kreuzer += wertInMuenzen[1] * 10; // Heller
		kreuzer += wertInMuenzen[0]; 	 // Kreuzer
		
		return kreuzer;
	}
}
