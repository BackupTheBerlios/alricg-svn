/*
 * Created on 23.06.2005 / 15:16:17
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store.xom.map;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.controller.CharKomponente;

/**
 * Hilfklasse (als Singleton implementiert) für das Mapping von und nach xml.
 * 
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
final class XOMMappingHelper {

	/** Die Singleton-Instanz */
	private static final XOMMappingHelper instance = new XOMMappingHelper();

	/** <code>XOMMappingHelper</code>'s logger */
	private static final Logger LOG = Logger.getLogger(XOMMappingHelper.class
			.getName());

	/** Gibt die Singleton-Instanz zurück */
	public static XOMMappingHelper instance() {
		return instance;
	}

	/**
	 * Wählt den korrekten <code>XOMMapper</code> zu einer
	 * <code>CharKomponente</code>.
	 * 
	 * @param charKomp
	 *            Die Komponente wozu ein passender <code>XOMMapper</code>
	 *            gesucht wird.
	 * @return Der passende Mapper, oder <code>null</code>, falls kein
	 *         passender gefunden werden konnte.
	 */
	public XOMMapper<CharElement> chooseXOMMapper(CharKomponente charKomp) {
		XOMMapper<CharElement> mappy = null;
		switch (charKomp) {
		// >>>>>>>>>>>>>>> Herkunft
		case rasse:
			mappy = new XOMMapper_Rasse();
			break;
		case kultur:
			mappy = new XOMMapper_Kultur();
			break;
		case profession:
			mappy = new XOMMapper_Profession();
			break;
		case zusatzProfession:
			mappy = new XOMMapper_ZusatzProfession();
			break;

		// >>>>>>>>>>>>>>> Fertigkeiten & Fähigkeiten
		case vorteil:
			mappy = new XOMMapper_Vorteil();
			break;
		case gabe:
			mappy = new XOMMapper_Gabe();
			break;
		case nachteil:
			mappy = new XOMMapper_Nachteil();
			break;
		case sonderfertigkeit:
			mappy = new XOMMapper_Sonderfertigkeit();
			break;
		case ritLitKenntnis:
			mappy = new XOMMapper_LiturgieRitualKenntnis();
			break;
		case talent:
			mappy = new XOMMapper_Talent();
			break;
		case zauber:
			mappy = new XOMMapper_Zauber();
			break;

		// >>>>>>>>>>>>>>> Sprachen
		case sprache:
			mappy = new XOMMapper_Sprache();
			break;
		case schrift:
			mappy = new XOMMapper_Schrift();
			break;

		// >>>>>>>>>>>>>>> Götter
		case liturgie:
			mappy = new XOMMapper_Liturgie();
			break;
		case ritual:
			mappy = new XOMMapper_Ritual();
			break;

		// >>>>>>>>>>>>>>> Ausrüstung
		case ausruestung:
			mappy = new XOMMapper_Ausruestung();
			break;
		case fahrzeug:
			mappy = new XOMMapper_Fahrzeug();
			break;
		case waffeNk:
			mappy = new XOMMapper_NahkWaffe();
			break;
		case waffeFk:
			mappy = new XOMMapper_FkWaffe();
			break;
		case ruestung:
			mappy = new XOMMapper_Ruestung();
			break;
		case schild:
			mappy = new XOMMapper_Schild();
			break;

		// >>>>>>>>>>>>>>> Zusätzliches
		case daemonenPakt:
			mappy = new XOMMapper_DaemonenPakt();
			break;
		case schwarzeGabe:
			mappy = new XOMMapper_SchwarzeGabe();
			break;
		case tier:
			mappy = new XOMMapper_Tier();
			break;
		case region:
			mappy = new XOMMapper_RegionVolk();
			break;
		case gottheit:
			mappy = new XOMMapper_Gottheit();
			break;
		case repraesentation:
			mappy = new XOMMapper_Repraesentation();
			break;
		case eigenschaft:
			mappy = null;
			// Eigenschaften werden nicht aus XML gelesen.
			// mappy = new XOMMapper_Eigenschaft();
			// for (int i = 0; i < kategorien.size(); i++) {
			// final Element child = kategorien.get(i);
			// final String id = child.getAttributeValue("id");
			// final CharElement charEl = eigenschaftMap.get(id);
			// mappy.map(child, charEl);
			// }
			break;
		case sonderregel:
			mappy = null;
			break; // Gibt es nicht!

		// >>>>>>>>>>>>>>> DEFAULT
		default:
			mappy = null;
			LOG.logp(Level.SEVERE, "CharKompAdmin", "initCharKomponents",
					"Ein CharKomp wurde nicht gefunden: " + charKomp);
		}
		return mappy;
	}

	/**
	 * Erzeugt eine neue Helper-Instanz. <code>private</code>, da nur
	 * statische Methoden vorhanden sind.
	 */
	private XOMMappingHelper() {

	}
}
