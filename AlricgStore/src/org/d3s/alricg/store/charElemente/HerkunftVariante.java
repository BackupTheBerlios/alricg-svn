/*
 * Created on 11.06.2005 / 15:31:36
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente;

import org.d3s.alricg.store.charElemente.links.Auswahl;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Voraussetzung;


/**
 * <u>Beschreibung:</u><br> 
 * Dieses Interface Beschreibt die Strucktur der Varianten von Rasse, Kultur und
 * Profession. Alle drei Varianten-Klassen implementieren dieses Interface. 
 *
 * @author V. Strelow
 */
public interface HerkunftVariante {
	
	public final static String VORAUSS = "Voraussetzungen";
	public final static String EIGEN_MODIS = "Modis Eigenschaften";
	public final static String VORTEILE = "Vorteile";
	public final static String NACHTEILE = "Nachteile";
	public final static String SONDERF = "Sonderfertigkeiten";
	public final static String LITURGIEN = "Liturgien";
	public final static String EMPF_VORTEILE = "Empf. Vorteile";
	public final static String EMPF_NACHTEILE = "Empf. Nachteile";
	public final static String UNGE_VORTEILE = "Unge. Vorteile";
	public final static String UNGE_NACHTEILE = "Unge. Nachteil";
	public final static String VERB_SONDERF = "Verb. Sonderfertigkeiten";
	public final static String VERB_LITURGIEN = "Verb. Liturgien";
	public final static String ZAUBER = "Zauber";
	public final static String HAUSZAUBER = "Hauszauber";
	public final static String ZUS_AKT_ZAUBER = "Zus. aktivierbare Zauber";
	public final static String NICHT_AKT_ZAUBER = "Nicht aktivierbare Zauber";
	
	/**
	 * @return Liste von XML-Tags die aus der original-Kultur "entfernt" (also nicht beachtet) 
	 * werden sollen, z.B. "sonderfertigkeiten", "vorteile" (ins Schema schauen für Namen).
	 * Natürlich soll nicht wirklich etwas aus den CharElement entfernt werden, sondern nur
	 * nicht übernommen in das neue Objekt.
	 */
	public String[] getEntferneXmlTag();
	
	/**
	 * @param entferneXmlTag Liste von XML-Tags die aus der original-Kultur "entfernt" 
	 * (also nicht beachtet) werden sollen, z.B. "sonderfertigkeiten", "vorteile" 
	 * (ins Schema schauen für Namen) atürlich soll nicht wirklich etwas aus den 
	 * CharElement entfernt werden, sondern nur nicht übernommen in das neue Objekt
	 */
	public void setEntferneXmlTag(String[] entferneXmlTag);
	
	/** Gibt an ob diese Varante mit anderen Varianten zusammen gewählt werden kann.
	 * @return true - Diese Variante kann zusätzlich zu anderen gewählt werden, 
	 * ansonsten false
	 */
	public boolean isMultibel();
	
	/** Gibt an ob diese Varante mit anderen Varianten zusammen gewählt werden kann.
	 * Ob immer nur eine Variante aktiv sein kann, oder mehrere gleichzeitig.
	 * @param isMultibel true - Diese Variante kann zusätzlich zu anderen gewählt werden, 
	 * ansonsten false
	 */
	public void setMultibel(boolean isMultibel);
	
	/**
	 * @return Die Herkunft, von der diese "abstammt". Kann auch wiederum eine Variante sein.
	 */
	public Herkunft getVarianteVon();
	
	/**
	 * @param varianteVon Die Herkunft, von der diese "abstammt". Kann auch wiederum eine Variante sein.
	 */
	public void setVarianteVon(Herkunft varianteVon);
	
	/**
	 * Bei einer AdditionsVariante (=true) werden alle Werte/ Elemente der Varinate zu der 
	 * original-Kultur hinzugefügt (mit den entfernten Elementen). 
	 * Eine NICHT AdditionsVariante (=false) ist eine eigenständige Kultur ohne Abhängigkeiten, 
	 * von der "original-Rasse". Alle nötigen angaben sind dementsprechend enthalten.
	 * true - Es ist eine AdditionsVariante, ansonsten false 
	 * @return true - diese Variante ist eine additionsvariante, ansonsten false.
	 */
	public boolean isAdditionsVariante();
	
	/**
	 * Bei einer AdditionsVariante (=true) werden alle Werte/ Elemente der Varinate zu der 
	 * original-Kultur hinzugefügt (mit den entfernten Elementen). 
	 * Eine NICHT AdditionsVariante (=false) ist eine eigenständige Kultur ohne Abhängigkeiten, 
	 * von der "original-Rasse". Alle nötigen angaben sind dementsprechend enthalten.
	 * true - Es ist eine AdditionsVariante, ansonsten false 
	 * @param isAdditionsVariante Setzt das Attribut isAdditionsVariante.
	 */
	public void setAdditionsVariante(boolean isAdditionsVariante);
	
}
