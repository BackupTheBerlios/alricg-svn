/*
 * Created on 24.06.2006 / 22:31:57
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.utils;

/**
 * <u>Beschreibung:</u><br> 
 * Verschiedene Konstanten.
 * 
 * TODO Die werte sollen später durch ein XML-File gesetzt werden können!
 * 
 * @author V. Strelow
 */
public class Konstanten {
	private static GenerierungsKonstanten genKonstanten = new Konstanten.GenerierungsKonstanten();
	
	
	public static GenerierungsKonstanten getGenKonstanten() {
		return genKonstanten;
	}
	
	
	/**
	 * 
	 * <u>Beschreibung:</u><br> 
	 * Diese Klasse stellt einige "Konstanten" zur Verfügung für die Erschaffung von Helden.
	 * VORSICHT: Obwohl in den Konstanten teilweise auch Maximal- und Minimal-Werte 
	 * stehen (Schlechte Eingenschaften, Eigenschaften), dürfen die Max/Min Werte eines 
	 * Elements NUR über "getMaxWert()" und "getMinWert()" abgefragt werden!
	 * Die Konstanten sind in dem Fall nur Berechnungsgrundlage.
	 * 
	 * (Es denkbar diese "Konstanten" auch zur Laufzeit ändern zu können durch ein neues 
	 * einlesen dieser Klasse, aber das sollte nicht BEIM generieren passiern, daher 
	 * Konstanten) 
	 * @author V. Strelow
	 */
	public static class GenerierungsKonstanten {
		
/**		Wieviele GP insgesamt zur Verfügung stehen (normal 110 GP) */ 
		public final int VERFUEGBARE_GP;

/** 	Maximale GP die durch schlechte Eigenschaften gewonnen werden können (normal 30 GP) */
		public final int MAX_SCHLECHTE_EIGEN_GP;
		
/** 	Maximale GP die durch Nachteile gewonnen werden (normal 50 GP) */
		public final int MAX_NACHTEIL_GP; 
		
/**		Maximale GP die auf Eigenschaften (MU, KL, usw) verteilt werdem dürfen (normal 100 GP) */	
		public final int MAX_GP_EIGENSCHAFTEN; 
		
/**		Maximaler Wert von Eigenschaften ohne Modis (Z.B. durch Herkunft, Vorteile) (normal 14) */
		public final int MAX_EIGENSCHAFT_WERT;
		
/**		Minimaler Wert von Eigenschaften ohne Modis (Z.B. durch Herkunft, Nachteile) (normal 8) */
		public final int MIN_EIGENSCHAFT_WERT;
		
/**		Maximaler Wert des Sozialstatus ohne Modis (Z.B. durch Herkunft, Vorteile) (normal 12) */
		public final int MAX_SOZIALSTATUS;
		
/**		Faktor für berechnung der TalentGP; (KL+IN) * TALENT_GP_FAKTOR = TalentGP (normal 20) */
		public final int TALENT_GP_FAKTOR;
		
/**		Maximale Anzahl an Talenten die aktiviert werden können (normal 5) */	
		public final int MAX_TALENT_AKTIVIERUNG;
		
/**		Maximale Anzahl an Zaubern für VOLLzauberer die aktiviert werden können (normal 10)	*/	
		public final int MAX_ZAUBER_AKTIVIERUNG_VZ;
		
/**		Maximale Anzahl an Zaubern für HALBzauberer die aktiviert werden können (normal 10)	*/		
		public final int MAX_ZAUBER_AKTIVIERUNG_HZ;
		
/**		Maximaler Wert von schlechten Eigenschaften (normal 12) */
		public final int MAX_SCHLECHT_EIGENSCHAFT_WERT;
		
/**		Minimaler Wert von schlechten Eigenschaften (normal 5) */
		public final int MIN_SCHLECHT_EIGENSCHAFT_WERT;
		
/**		Wie sich der Wert der Muttersprache in abhängigkeit zur KL errechnet (normal -2) */		
		public final int DIFF_KULGHEIT_MUTTERSPR;
		
/**		Wie sich der Wert der Muttersprache in abhängigkeit zur KL errechnet (normal -4) */	
		public final int DIFF_KULGHEIT_ZWEITSPR;
		
/**		Wie groß darf die Different zwischen verteilten Punkten auf AT und PA max. sein (normal 5) */			
		public final int MAX_DIFF_AT_PA;	
		
//		 Der Minimale Wert des Sozialstatus ist "1", auch als variable?
		
		public GenerierungsKonstanten() {
			// TODO Werte korrekt initialisieren mit auslesen aus einer Config-XML
			VERFUEGBARE_GP = 10;
			MAX_SCHLECHTE_EIGEN_GP = 10;
			MAX_NACHTEIL_GP = 10;
			MAX_GP_EIGENSCHAFTEN = 10;
			MAX_EIGENSCHAFT_WERT = 14;
			MIN_EIGENSCHAFT_WERT = 8;
			MAX_SOZIALSTATUS = 12;
			TALENT_GP_FAKTOR = 10;
			MAX_TALENT_AKTIVIERUNG = 5;
			MAX_ZAUBER_AKTIVIERUNG_VZ = 10;
			MAX_ZAUBER_AKTIVIERUNG_HZ = 10;
			MAX_SCHLECHT_EIGENSCHAFT_WERT = 10;
			MIN_SCHLECHT_EIGENSCHAFT_WERT = 10;
			DIFF_KULGHEIT_MUTTERSPR = -2;
			DIFF_KULGHEIT_ZWEITSPR = -4;
			MAX_DIFF_AT_PA = 5;	
		}
		
	}
}
