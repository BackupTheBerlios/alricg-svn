/*
 * Created 13.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.common;

import java.util.List;

import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.RegionVolk;
import org.d3s.alricg.store.charElemente.Repraesentation;
import org.d3s.alricg.store.charElemente.SchamanenRitual;
import org.d3s.alricg.store.charElemente.Schrift;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.Sprache;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.charElemente.charZusatz.DaemonenPakt;
import org.d3s.alricg.store.charElemente.charZusatz.Gegenstand;
import org.d3s.alricg.store.charElemente.charZusatz.MagierAkademie;
import org.d3s.alricg.store.charElemente.charZusatz.SchwarzeGabe;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.charElemente.links.OptionVoraussetzung;
import org.d3s.alricg.store.charElemente.links.Voraussetzung;

/**
 * @author Vincent
 */
public class CharElementTextService {
	
	public static String getCharElementName(Class clazz) {
		if (clazz == Eigenschaft.class) {
			return "Eigenschaft";
		} else if (clazz ==  Talent.class) {
			return "Talent";
		} else if (clazz ==  Zauber.class) {
			return "Zauber";
		} else if (clazz ==  Repraesentation.class) {
			return "Repräsentation";
		} else if (clazz ==  Vorteil.class) {
			return "Vorteil";
		} else if (clazz ==  Nachteil.class) {
			return "Nachteil";
		} else if (clazz ==  Sonderfertigkeit.class) {
			return "Sonderfertigkeit";
		} else if (clazz ==  Rasse.class) { //|| clazz == RasseVariante.class) {
			return "Rasse";
		} else if (clazz ==  Kultur.class) { //|| clazz == KulturVariante.class) {
			return "Kultur";
		} else if (clazz ==  Profession.class) { //|| clazz == ProfessionVariante.class) {
			return "Profession";
		} else if (clazz ==  Gottheit.class) {
			return "Gottheit";
		} else if (clazz ==  Liturgie.class) {
			return "Liturgie";
		} else if (clazz ==  RegionVolk.class) {
			return "Region/Volk";
		} else if (clazz ==  Schrift.class) {
			return "Schrift";
		} else if (clazz ==  Sprache.class) {
			return "Sprache";
		} else if (clazz ==  SchwarzeGabe.class) {
			return "Schwarze Gabe";
		} else if (clazz ==  Gegenstand.class) {
			return "Gegenstand";
		} else if (clazz ==  DaemonenPakt.class) {
			return "Dämonen Pakt";
		} else if (clazz ==  MagierAkademie.class) {
			return "Magier Akademie";
		} else if (clazz ==  SchamanenRitual.class) {
			return "Ritual";
		} else {
			throw new IllegalArgumentException("Keine Behandlung für ein Element des Typs " +
					clazz.toString() + " vorhanden.");
		}
	}
	
	/**
	 * Berechnet einen aus einer Voraussetzung einen Text für den User 
	 * @param voraus Die Voraussetzung, deren Text dargestellt werden soll
	 * @return Der Text
	 */
	public static String getVoraussetzungsText(Voraussetzung voraus) {
		if (voraus == null) return "keine";
		
		final String positiv = getVoraussetzungsText(voraus.getPosVoraussetzung());
		String negativ = getVoraussetzungsText(voraus.getNegVoraussetzung());

		if (negativ.length() > 0) {
			negativ = "NICHT: " + negativ;
			if (positiv.length() > 0) {
				negativ = ", " + negativ;
			}
		}
		
		final String returnStr = positiv + negativ; 
		if (returnStr.length() == 0) return "keine";
		
		return returnStr;
	}
	
	/**
	 * Helper für "getVoraussetzungsText(Voraussetzung)"
	 */
	private static String getVoraussetzungsText(List<OptionVoraussetzung> voraus) {
		if (voraus == null) return "";
		final StringBuilder strB = new StringBuilder();
		
		for (int i = 0; i < voraus.size(); i++) {
			if (voraus.size() > 1) {
				strB.append("(");
			}
			strB.append(getVoraussetzungsText(voraus.get(i)));
			if (voraus.size() > 1) {
				strB.append(")");
			}
			if (i+1 < voraus.size()) {
				strB.append(" und ");
			}
		}
		
		return strB.toString();
	}
	
	/**
	 * Helper für "getVoraussetzungsText(Voraussetzung)"
	 */
	private static String getVoraussetzungsText(OptionVoraussetzung voraus) {
		if (voraus == null) return "";
		final StringBuilder strB = new StringBuilder();
		
		// Optionales Prefix:
		if (voraus.getWert() > 0) {
			strB.append("Ab Stufe ").append(voraus.getWert());
		} 
		if (voraus.getAnzahl() > 0) {
			if (strB.length() > 0) strB.append(", ");
			strB.append(voraus.getAnzahl()).append(" aus");
		}
		if (strB.length() > 0) {
			strB.append(": ");
		}
		
		// Text der Links berechnen
		for (int i = 0; i < voraus.getLinkList().size(); i++) {
			strB.append(getLinkText((Link) voraus.getLinkList().get(i)));
			if (i+1 < voraus.getLinkList().size()) {
				strB.append(",");
			}
		}
		
		// Alternativen
		if (voraus.getAlternativOption() != null) {
			strB.insert(0, "(");
			strB.append(") oder (");
			strB.append(getVoraussetzungsText((OptionVoraussetzung) voraus.getAlternativOption()));
			strB.append(")");
		}
		
		return strB.toString();
	}
	
	/**
	 * Berechnet zu einem Link einen Text, der dem User angezeigt werden kann.
	 * - Text und Zweitziel werden in Klammern hinter dem Namen geschrieben
	 * - Eigenschaften werden abgekürzt
	 * @param link Der Link
	 * @return Text zu Link
	 */
	public static String getLinkText(Link link) {
		// Name und Wert berechnen
		String returnStr;
		if (link.getZiel() instanceof Eigenschaft) { // Bei Eigenschafte nur die Kurzform
			returnStr = ((Eigenschaft)link.getZiel()).getEigenschaftEnum().getAbk();
		} else {
			returnStr = link.getZiel().getName();
		}
		
		String wert;
		if (link.getWert() != Link.KEIN_WERT) {
			wert = " " + Integer.toString(link.getWert());
		} else {
			wert = "";
		}
		
		// Dies reicht für den "standard-Fall"
		if (link.getText() == null && link.getZweitZiel() == null) {
			return returnStr + wert;
		}
		
		// Falls es noch einen Text oder ZweitZiel gibt
		final StringBuilder strB = new StringBuilder();
		if (link.getText() != null && link.getText().length() > 0) {
			strB.append("(").append(link.getText());
		}
		if (link.getZweitZiel() != null) {
			if (strB.length() == 0) {
				strB.append("(");
			} else {
				strB.append(",");
			}
			strB.append(link.getZweitZiel().getName());
		}
		if (strB.length() > 0) {
			strB.append(")");
		}
		
		return returnStr + strB.toString() + wert;
	}
	
	/**
	 * Liefert den Text zu einer Sammlung von Münzen
	 * @param muenzen 
	 * 	[0] - Kreuzer 
	 * 	[1] - Heller
	 * 	[2] - Silbertaler
	 * 	[3] - Dukaten
	 * @param isAbk true - Es soll nur eine Akürzung angezeigt werden, ansonsten false
	 * @return Ein Text werde die Münzen wiedergibt
	 */
	public static String getMuenzenString(int[] muenzen, boolean isAbk) {
		StringBuilder strB = new StringBuilder();
		
		for (int i = 0; i < muenzen.length; i++) {
			if (muenzen[i] != 0) {
				if (strB.length() > 0) strB.append(" ");
				strB.append(muenzen[i]).append(" ").append(getMuenzenWaehrung(i, isAbk));
			}
		}
		
		if (strB.length() == 0) {
			return "0";
		}
		return strB.toString();
	}
	
	/**
	 * Hilfmethode für "getMuenzenString"
	 * @param idx
	 * 	[0] - Kreuzer 
	 * 	[1] - Heller
	 * 	[2] - Silbertaler
	 * 	[3] - Dukaten
	 * @param isAbk true - Es soll nur eine Akürzung angezeigt werden, ansonsten false
	 * @return Text für eine Waehrung
	 */
	private static String getMuenzenWaehrung(int idx, boolean isAbk) {
		if (isAbk) {
			switch (idx) {
			case 0: return "K";
			case 1: return "H";
			case 2: return "S";
			case 3: return "D";
			default: throw new IllegalArgumentException("Unerwarteter Index");
			}
		}
		switch (idx) {
		case 0: return "Kreuzer";
		case 1: return "Heller";
		case 2: return "Silbertaler";
		case 3: return "Dukaten";
		default: throw new IllegalArgumentException("Unerwarteter Index");
		}
	}
}
