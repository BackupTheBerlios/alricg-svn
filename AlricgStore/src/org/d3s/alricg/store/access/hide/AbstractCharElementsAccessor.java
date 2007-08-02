/*
 * Created 31.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.access.hide;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.d3s.alricg.store.access.CharElementAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
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
 * Diese Klasse ist nur wegen JaxB nötig... 
 * Das Interface "CharElementAccessor" wird als abstrakte Klasse von JaxB nicht akzeptier,
 * diese Methode soll jedoch nur einmal für beide XmlAccessoren implementiert werden, daher 
 * wurde diese Klasse eingeführt
 * @author Vincent
 */
public abstract class AbstractCharElementsAccessor implements CharElementAccessor {
	
	public List<? extends CharElement> getMatchingList(Class clazz) {

		if (clazz == Eigenschaft.class) {
			return getEigenschaftList();
		} else if (clazz ==  Talent.class) {
			return getTalentList();
		} else if (clazz ==  Zauber.class) {
			return getZauberList();
		} else if (clazz ==  Repraesentation.class) {
			return getRepraesentationList();
		} else if (clazz ==  Gabe.class) {
			return getGabeList();
		} else if (clazz ==  Vorteil.class) {
			return getVorteilList();
		} else if (clazz ==  Nachteil.class) {
			return getNachteilList();
		} else if (clazz ==  Sonderfertigkeit.class) {
			return getSonderfList();
		} else if (clazz ==  Rasse.class) { //|| clazz == RasseVariante.class) {
			return getRasseList();
		} else if (clazz ==  Kultur.class) { //|| clazz == KulturVariante.class) {
			return getKulturList();
		} else if (clazz ==  Profession.class) { //|| clazz == ProfessionVariante.class) {
			return getProfessionList();
		} else if (clazz ==  Gottheit.class) {
			return getGottheitList();
		} else if (clazz ==  Liturgie.class) {
			return getLiturgieList();
		} else if (clazz ==  RegionVolk.class) {
			return getRegionVolkList();
		} else if (clazz ==  RitualKenntnis.class) {
			return getRitualkenntnisList();
		} else if (clazz ==  Schrift.class) {
			return getSchriftList();
		} else if (clazz ==  Sprache.class) {
			return getSpracheList();
		} else if (clazz ==  SchwarzeGabe.class) {
			return getSchwarzeGabeList();
		} else if (clazz ==  Gegenstand.class) {
			return getGegenstandList();
		} else if (clazz ==  DaemonenPakt.class) {
			return getDaemonenPaktList();
		} else if (clazz ==  MagierAkademie.class) {
			return getMagierAkademieList();
		} else {
			throw new IllegalArgumentException("Keine Behandlung für ein Element des Typs " +
					clazz.toString() + " vorhanden.");
		}
	}
}
