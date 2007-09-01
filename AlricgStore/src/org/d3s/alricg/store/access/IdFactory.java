/*
 * Created 26.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.access;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.KulturVariante;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.MagieMerkmal;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.ProfessionVariante;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.RasseVariante;
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

/**
 * Diese Klasse managed den Umgang mit IDs
 * @author Vincent
 */
public class IdFactory {
	private HashMap<Class, String> classHash;
	private static IdFactory self;
	
	private IdFactory() {
		classHash = new HashMap<Class, String>();
		
		classHash.put(Eigenschaft.class, "EIG");
		classHash.put(Talent.class, "TAL");
		classHash.put(Zauber.class, "ZAU");
		classHash.put(Repraesentation.class, "REP");
		classHash.put(Vorteil.class, "VOR");
		classHash.put(Nachteil.class, "NAC");
		classHash.put(Sonderfertigkeit.class, "SFK");
		classHash.put(Rasse.class, "RAS");
		classHash.put(Kultur.class, "KUL");
		classHash.put(Profession.class, "PRO");
		classHash.put(Gottheit.class, "GOT");
		classHash.put(Liturgie.class, "LIT");
		classHash.put(RegionVolk.class, "REG");
		classHash.put(Schrift.class, "SRI");
		classHash.put(Sprache.class, "SPR");
		classHash.put(Gegenstand.class, "GEG");
		classHash.put(DaemonenPakt.class, "DAM");
		classHash.put(MagierAkademie.class, "MAG");
		classHash.put(SchwarzeGabe.class, "SGA");	
		classHash.put(MagieMerkmal.class, "MAM");
		classHash.put(SchamanenRitual.class, "SAR");
		
		classHash.put(RasseVariante.class, "VRA");
		classHash.put(KulturVariante.class, "VKU");
		classHash.put(ProfessionVariante.class, "VPO");	
	}
	
	public static IdFactory getInstance() {
		if (self == null) {
			self = new IdFactory();
		}
		return self;
	}
	
	/**
	 * Erstellt eine neue, gültige, eindeutige ID.
	 * @param clazz Klasse des CharElements für welche die ID gedacht ist
	 * @param elementName Name des Elements. Kann auch leer oder null sein, dann wird
	 * 	eine nummerische ID erstellt auf basis des aktuellen TimeStamps 
	 * @return Gültige, eindeutige ID
	 */
	public String getId(Class clazz, String elementName) {
		StringBuilder strB = new StringBuilder();
		
		strB.append(classHash.get(clazz)).append("-");
		if (elementName == null || elementName.trim().length() == 0) {
			return strB.append(new Date().getTime()).toString();
		}
		String name = elementName.toLowerCase().trim().replaceAll("[^a-z|0-9|ä|ö|ü|+|-]", "_").toString();
		
		if (isIdExist(clazz, strB.toString() + name)) {
			return getId(clazz, name + "+");
		}
	
		return strB.append(name).toString();
	}
	
	/**
	 * Sucht zu einer ID die zugehörige Klasse ("Class-Object") heraus
	 * @param id Id die überprüft werden soll
	 * @return Das zugehörige Class-Object, oder null wenn zu der ID keine
	 * 	Klasse gefunden werden kann!
	 */
	public Class getClass(String id) {
		// Nur die ersten 3 Buchstaben sind entscheidend
		id = id.substring(0, 3);
		
		Iterator<Class> ite = classHash.keySet().iterator();
		
		while(ite.hasNext()) {
			Class clazz = ite.next();
			if (classHash.get(clazz).equals(id)) {
				return clazz;
			}
		}
		
		return null;
	}
	
	/**
	 * Prüft ob eine ID schon im System existiert.
	 * @param clazz Zugehörige Class des CharElements 
	 * @param id Zu prüfende ID
	 * @return true - diese ID existiert bereits und darf nicht nocheinmal vergeben werden
	 * 		false - diese ID existiert noch nicht.
	 */
	public boolean isIdExist(Class clazz, String id) {
		final List<? extends CharElement> charList = StoreDataAccessor.getInstance().getMatchingList(clazz);

		for (int i = 0; i < charList.size(); i++) {
			if (charList.get(i).getId().equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Prüft ob eine ID schon im System existiert.
	 * @param clazz Zugehörige Class des CharElements 
	 * @param id Zu prüfende ID
	 * @return true - diese ID existiert bereits und darf nicht nocheinmal vergeben werden
	 * 		false - diese ID existiert noch nicht.
	 */
	public boolean isIdExist(String id) {
		return isIdExist(this.getClass(id), id);
	}
}
