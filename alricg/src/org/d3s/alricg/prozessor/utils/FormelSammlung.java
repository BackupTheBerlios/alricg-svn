/*
 * Created on 09.03.2005 / 17:59:33
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.controller.Messenger;
import org.d3s.alricg.controller.Notepad;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.ConfigStore;
import org.d3s.alricg.store.ConfigurationException;
import org.d3s.alricg.store.DataStore;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.TextStore;

/**
 * <u>Beschreibung:</u><br>
 * Einfache, statische Formel zur Berechnung, sowie Zugriff und Berechnung über die SKT.
 * 
 * @author V. Strelow
 */
public class FormelSammlung {
	private static Notepad notepad;
	
    /** <code>FormelSammlung</code>'s logger */
    private static final Logger LOG = Logger.getLogger(FormelSammlung.class.getName());
    
	public final static int KEIN_WERT = CharElement.KEIN_WERT;
	public final static String TEXT_SELBSTSTUBIUM= "Selbststudium";
	public final static String TEXT_SKT = "SKT";
	public final static String TEXT_SPEZ_ERF = "Spezielle Erfahrung";
	public final static String TEXT_LEHRM = "Sehr guter Lehrmeister";
	public final static String TEXT_TAL_STUFE11 = "Talent/Selbststudium ab Stufe 11";

	
	/**
	 * SKT in einer HashTable. 
	 * 	skt.get(x)[0] = Aktivierungskosten bei der Generierung
	 * 	skt.get(x)[31] = Kosten für jede Stufe größer als 30 
	 */
	private static Map<KostenKlasse, Integer[]> skt = new HashMap<KostenKlasse, Integer[]>();
 	private final static int maxSktStufe = 31; // Maximale Stufe der SKT
 	
	/**
	 * Bildet die Spalten der SKT ab und bietet rudimentäre Operationen.
	 * @author V. Strelow
	 */
    public enum KostenKlasse { 
    	A_PLUS ("A+"), 
    	A("A"), 
    	B("B"), 
    	C("C"), 
    	D("D"), 
    	E("E"), 
    	F("F"), 
    	G("G"), 
    	H("H"); 
		private String value; // Id des Elements
		
		private KostenKlasse(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		/**
		 * Liefert eine KostenKlasse die um einen schritt "nach Rechts" geschoben
		 * ist, also die KostenKlasse und somit die Kosten um eine Spalte erhöht.
		 * @param kostenK Die bisherige Kostenklasse
		 * 		w
		 * @return Die um eine Spalte erhöhte Kostenklasse, bzw. das Maximum, 
		 *  wenn es nicht höher geht.
		 */
		public KostenKlasse plusEineKk() {
			if ( this.ordinal() == (KostenKlasse.values().length - 1) ) {
				return this; // Es geht nicht höher
			}
			
			return KostenKlasse.values()[this.ordinal()+1];
		}
		
		/**
		 * Liefert eine KostenKlasse die um einen schritt "nach Links" geschoben
		 * ist, also die KostenKlasse und somit die Kosten um eine Spalte erniedrigt.
		 * 
		 * @param kostenK Die bisherige Kostenklasse
		 * @return Die um eine Spalte erniedrigte Kostenklasse, bzw. das Minimum, 
		 * 		wenn es nicht niedriger geht.
		 */
		public KostenKlasse minusEineKk() {
			if ( this.ordinal() == 0 ) {
				return this; // Es geht nicht niedriger
			}
			
			return KostenKlasse.values()[this.ordinal()-1];
		}
		
		/**
		 * Prüft ob diese KostenKlasse "teurer" als eine andere ist.
		 * Beispiel: B ist teurer als A
		 * @param kk Die KostenKlasse zum Vergleich
		 * @return true - Diese Kostenklasse ist teuerer als die KostenKlasse "kk", ansonsten false
		 */
		public boolean isTeurerAls(KostenKlasse kk) {
			return this.ordinal() > kk.ordinal();
		}
		
		/**
		 * Prüft ob diese KostenKlasse "billiger" als eine andere ist.
		 * Beispiel: A ist billiger als B
		 * @param kk Die KostenKlasse zum Vergleich
		 * @return true - Diese Kostenklasse ist billiger als die KostenKlasse "kk", ansonsten false
		 */
		public boolean isBilligerAls(KostenKlasse kk) {
			return this.ordinal() < kk.ordinal();
		}
		
    }
    public enum Lernmethode {
    	selbstStudium, // eine Spalte schwerer, bei Talent ab 10 sogar 2 Spalten schwerer
    	lehrmeister, // wie angegeben
    	spezielleErfahrung, // ein Spalte leichter
    	sehrGuterLehrmeister // ein Spalte leichter
    }
    
    public static void initFormelSammlung(Notepad note) {

        final TextStore lib = FactoryFinder.find().getLibrary();
        final ConfigStore config= FactoryFinder.find().getConfiguration();
        final DataStore data = FactoryFinder.find().getData();

        try {
    		skt = data.getSkt(); 
    	} catch (ConfigurationException ex) {
            LOG.severe(ex.getMessage());
            ProgAdmin.messenger.showMessage(Messenger.Level.fehler, 
            		lib.getErrorTxt("Fehlerhafte Datei") + "\n" + "  " 
                    + config.getConfig().getProperty(ConfigStore.Key.config_file)+ "\n" 
                    + lib.getErrorTxt("XML Validierungsfehler"));
    	}
    	
    	FormelSammlung.notepad = note;
    }
    
    /**
     * Liefert einen Wert einer Zeile/Spalte der Steigerungskosten Tabelle.
     * @param kKlasse Die gewünschte KostenKlasse
     * @param stufe Die gewünschte Stufe
     * @return Den Wert, der in der SKT in der Zeile "KostenKlasse" und in der 
     * 		Spalte "Stufe". 
     */
    public static int getSktWert(KostenKlasse kKlasse, int stufe) {
    	
    	if (stufe > maxSktStufe) {
    		stufe = maxSktStufe; // Ab 31 kosten alle Stufen gleich viel
    	}
    	
    	if (kKlasse.equals(KostenKlasse.A_PLUS)) {
    		int tmp = (skt.get(KostenKlasse.A)[stufe] - 2);
    		if (tmp < 1) tmp = 1;
    		return tmp;
    	} else {
    		return skt.get(kKlasse)[stufe];
    	}
    	
    }
    

    
	/**
	 * Liefert zu einem XML-Tag die entsprechende Enum zurück.
	 * @param value Id  der KostenKlasse
	 * @return Die Enum KostenKlasse die zu value gehört
	 */
	public static KostenKlasse getKostenKlasseByValue(String value) {
		KostenKlasse[] kostenArray = KostenKlasse.values();
		
		// Suchen des richtigen Elements
		for (int i = 0; i < kostenArray.length; i++) {
			if (value.equals(kostenArray[i].getValue())) {
				return kostenArray[i]; // Gefunden
			}
		}
		
		LOG.severe("XmlValue der SKT konnte nicht gefunden werden!");
		
		return null;
	}
	
	/**
	 * Liefert die Ap-Kosten für die entsprechende Stufe und KostenKlasse mittels SKT
	 * @param startStufe Die bisherige Stufe des Elements; KEIN_WERT oder ein negativer 
	 * 			Wert bedeutet das es nicht aktiviert wurde!
	 * @param zielStufe Die gewünschte Stufe
	 * @param heldenStufe Die Stufe des Helden (nur bei aktivierung wichtig) 
	 * 				 "0" Bedeutet: Generierung = Wert aus der Tabelle
	 * 				 Negative Werte sind nicht erlaubt! 
	 * @param methode Die gewünschte Lehrnmethode
	 * @param klasse Die gewünschte Kostenklasse
	 * @param isTalent Ob es sich um ein Talent handelt oder nicht.
	 * 			(Talente besitzen kein A* und es gil: Selbststudium + Stufe > 10 = kk+1)
	 * @return Die AP-Kosten für die Steigerung (inkl. evtl. aktivierung)
	 */
	public static int berechneSktKosten(int startStufe, int zielStufe, 
								   int heldenStufe, Lernmethode methode, 
								   KostenKlasse kKlasse, boolean isTalent)  {
		KostenKlasse tmpKK = kKlasse;
		int tmpKosten = 0;
		
		// Um endlosschleifen zu verhindern:
		if (startStufe > zielStufe) {
			throw new ArithmeticException("startStufe (" 
					+ startStufe + ") muss kleiner sein als die Zielstufe("
					+ zielStufe + ") !");
		}
		
		// Änderung der Kostenklasse durch die Lernmethode
		switch (methode) {
			case selbstStudium: 
				tmpKK = tmpKK.plusEineKk(); 
				notepad.writeMessage(TEXT_SELBSTSTUBIUM + ": " + TEXT_SKT + " +1");
				break;
			case spezielleErfahrung:  
				tmpKK = tmpKK.minusEineKk(); 
				notepad.writeMessage(TEXT_SPEZ_ERF + ": " + TEXT_SKT + " -1");
				break;
			case sehrGuterLehrmeister:
				tmpKK = tmpKK.minusEineKk(); 
				notepad.writeMessage(TEXT_LEHRM + ": " + TEXT_SKT + " -1");
				break;
		}
		
		// Für Talente gibt es kein A+ als KostenKlasse
		if ( tmpKK.equals(KostenKlasse.A_PLUS) && isTalent) {
			tmpKK = KostenKlasse.A;
		}
		
		// Falls es ein Talent zuvor nicht gab, ist es KEIN_WERT, zählt aber wie "0"
		if (startStufe == CharElement.KEIN_WERT) {
			startStufe = -1;
		}
		
		// Aktivierungskosten ausrechenen, wenn Stufe < 0
		if (startStufe < 0) {
			while (startStufe < 0 && startStufe < zielStufe) {
				tmpKosten += getSktWert(tmpKK, heldenStufe);
				startStufe++;
			}
		}
		
		// Unterscheiden, ob im Laufe der Berechnung die Kosten Kategorie steigt
		if ( isTalent && zielStufe > 10 && methode.equals(Lernmethode.selbstStudium)) {
			// Errechnen der Kosten / Kategorie ändert sich
			while (startStufe < zielStufe) {
				startStufe++;
				
				// Steigerung der Kategorie bei überschreiben der Stufe 10
				if (startStufe == 11) {
                    final TextStore lib = FactoryFinder.find().getLibrary();
					tmpKK = tmpKK.plusEineKk();
					notepad.writeMessage(TEXT_TAL_STUFE11 + ": " + TEXT_SKT + " -1");
				}

				tmpKosten += getSktWert(tmpKK, startStufe);
			}
			
		} else {
		
			// Errechnen der Kosten / Kategorie ändert sich nicht
			while (startStufe < zielStufe) {
				startStufe++;
				tmpKosten += getSktWert(tmpKK, startStufe);
			}
		}
		
		return tmpKosten;
	}
	
	/**
	 * Vereinfachte Version von "getSktKosten" für die Generierung. Die Heldenstufe ist
	 * stehts "0" und die Lernmethode ist stehts "lehrmeister".
	 * 
	 * @param startStufe Die bisherige Stufe des Elements; KEIN_WERT oder ein negativer 
	 * 			Wert bedeutet das es nicht aktiviert wurde!
	 * @param zielStufe Die gewünschte Stufe
	 * @param klasse Die gewünschte Kostenklasse
	 * @return Die AP-Kosten für die Steigerung (inkl. evtl. aktivierung)
	 */
	public static int berechneSktKosten(int startStufe, int zielStufe, KostenKlasse kKlasse) {
		return berechneSktKosten(startStufe, zielStufe, 0, 
				Lernmethode.lehrmeister, kKlasse, false);
		// Es wird Talent einfach angenommen, da es für die Lernmethode "Lehrmeister" egal ist
	}
	
	/**
	 * Berechnet die Kosten zum Senken der Schlechten Eigenschaften
	 * 
	 * @param startStufe Die momentane Stufe
	 * @param zielStufe Die Stufe auf die gesenkt werden soll
	 * @return Die AP Kosten zum senken der startStufe auf zielStufe
	 */
	public static int berechneSchEigSenkenKosten(int startStufe, int zielStufe) {
		int tmpKosten = 0;
		
		if (zielStufe == CharElement.KEIN_WERT) {
			zielStufe = 0;
		}
		
		// Um endlosschleifen zu verhindern:
		if (zielStufe > startStufe) {
			throw new ArithmeticException("startStufe muss größer sein als die Zielstufe!");
		}
		
//		21 minus neue Stufe in Kategorie G
		while (startStufe > zielStufe) {
			startStufe--;
			tmpKosten += getSktWert(KostenKlasse.G, (21 - startStufe));
		}
		 
		return tmpKosten;
	}
	
	/**
	 * Berechnet die Kosten die Nötig sind um einen Nachteil (keine schlechte 
	 * Eigenschaft) abzubauen.
	 * 
	 * @param gp Die GP die dieser Nachteil kostet (negativ)
	 * @return Die AP die aufgewendet werden müssen um einen Nachteil mit den kosten
	 * 		"gp" abzubauen
	 */
	public static int berechneNachteilAbbauen(int gp) {
		return Math.abs(gp) * 100;
	}
	
	/**
	 * Berechnet die AP die für eine Sonderfertigkeit gezahlt werden muß.
	 * Dafür werden die GP der SF genommen und mit 50 Multipliziert.
	 * @param gp Die GP-Kosten einer Sonderfertigkeit
	 * @return Die AP-Kosten für diese Sonderfertigkeit
	 */
	public static int berechneSfAp(int gp) {
		return (gp * 50);
	}
	
// ------------------ Berechnung der Grundwerte -----------------------
	/**
	 * Berechnet die Magieresistenz OHNE Modifikatoren
	 * @param MU Mut des Helden
	 * @param KL Klugheit des Helden
	 * @param KO Konstitution des Helden
	 * @return Die errechnete Magieresistenz
	 */
	public static int berechneMR(int MU, int KL, int KO) {
		return (int) Math.round( (MU + KL + KO) / 5d );
	}
	
	/**
	 * Berechnet die Initiative OHNE Modifikatoren
	 * @param MU Mut 
	 * @param IN Intuition
	 * @param GE Gewandheit
	 * @return Die errechnete Initiative
	 */
	public static int berechneINI(int MU, int IN, int GE) {
		return (int) Math.round( ((MU*2) + IN + GE) / 5d );
	}
	
	/**
	 * Berechnet den Basis Attackewert OHNE Modifikatoren
	 * @param MU Mut 
	 * @param GE Gewandheit
	 * @param KK Körperkraft
	 * @return Die errechnete AT Basis
	 */
	public static int berechneAtBasis(int MU, int GE, int KK) {
		return (int) Math.round( (MU + GE + KK) / 5d );
	}
	
	/**
	 * Berechnet den Basis Paradewert OHNE Modifikatoren
	 * @param IN Intuition 
	 * @param GE Gewandheit
	 * @param KK Körperkraft
	 * @return Die errechnete PA Basis
	 */
	public static int berechnePaBasis(int IN, int GE, int KK) {
		return (int) Math.round( (IN + GE + KK) / 5d );
	}
	
	/**
	 * Berechnet den Basis Fernkampfwert OHNE Modifikatoren
	 * @param IN Intuition 
	 * @param FF Fingerfertigkeit
	 * @param KK Körperkraft
	 * @return Die errechnete FK Basis
	 */
	public static int berechneFkBasis(int IN, int FF, int KK) {
		return (int) Math.round( (IN + FF + KK) / 5d );
	}
	
	/**
	 * Berechnet den Basis Lebenspunkte OHNE Modifikatoren
	 * @param KO Konstitution
	 * @param KK Körperkraft
	 * @return Die errechneten Lebenspunkte
	 */
	public static int berechneLep(int KO, int KK) {
		return (int) Math.round( ((KO*2) + KK) / 2d );
	}
	
	/**
	 * Berechnet den Basis Ausdauer OHNE Modifikatoren
	 * @param MU Mut 
	 * @param KO Konstitution
	 * @param GE Gewandheit
	 * @return Die errechnete Ausdauer
	 */
	public static int berechneAup(int MU, int KO, int GE) {
		return (int) Math.round( (MU + KO + GE) / 2d );
	}
	
	/**
	 * Berechnet den Basis Astralpunkte OHNE Modifikatoren
	 * @param MU Mut 
	 * @param IN Intuition
	 * @param CH Charisma
	 * @return Die errechneten Astralpunkte
	 */
	public static int berechneAsp(int MU, int IN, int CH) {
		return (int) Math.round( (MU + IN + CH) / 2d );
	}
}
