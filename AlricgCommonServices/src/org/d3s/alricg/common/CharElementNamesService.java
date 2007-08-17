/*
 * Created 13.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.common;

import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Gabe;
import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.RegionVolk;
import org.d3s.alricg.store.charElemente.Repraesentation;
import org.d3s.alricg.store.charElemente.RitualKenntnis;
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

/**
 * @author Vincent
 */
public class CharElementNamesService {
	
	public static String getCharElementName(Class clazz) {
		if (clazz == Eigenschaft.class) {
			return "Eigenschaft";
		} else if (clazz ==  Talent.class) {
			return "Talent";
		} else if (clazz ==  Zauber.class) {
			return "Zauber";
		} else if (clazz ==  Repraesentation.class) {
			return "Repräsentation";
		} else if (clazz ==  Gabe.class) {
			return "Gabe";
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
		} else if (clazz ==  RitualKenntnis.class) {
			return "Ritualkenntnis";
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
		} else {
			throw new IllegalArgumentException("Keine Behandlung für ein Element des Typs " +
					clazz.toString() + " vorhanden.");
		}
	}
}
