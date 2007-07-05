/*
 * Created on 11.06.2005 / 15:31:36
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten;

import org.d3s.alricg.charKomponenten.links.IdLinkList;

/**
 * <u>Beschreibung:</u><br> 
 * Dieses Interface Beschreibt die Strucktur der Varianten von Rasse, Kultur und
 * Profession. Alle drei Varianten-Klassen implementieren dieses interface. 
 *
 * @author V. Strelow
 */
public interface HerkunftVariante {
	
	/**
	 * @return Liste von Elementen die aus der original-Kultur "entfernt" (also nicht beachtet) 
	 * werden sollen, z.B. SF, Vorteile, Nachteile. 
	 * KEINE Elemente die in einer Auswahl stehen!.
	 */
	public IdLinkList getEntferneElement();
	
	/**
	 * @param entferneElement Liste von Elementen die aus der original-Kultur "entfernt"
	 * (also nicht beachtet) werden sollen, z.B. SF, Vorteile, Nachteile. 
	 * KEINE Elemente die in einer Auswahl stehen!.
	 */
	public void setEntferneElement(IdLinkList entferneElement);
	
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
