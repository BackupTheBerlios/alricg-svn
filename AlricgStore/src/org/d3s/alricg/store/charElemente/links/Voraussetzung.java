/*
 * Created 26. Dezember 2004 / 23:10:42
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente.links;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;

import org.d3s.alricg.store.charElemente.CharElement;

/**
 * Beschreibt Bedingungen die erfüllt sein müssen, damit ein Element, das diese 
 * Voraussetzug besitzt, zum Helden hinzugefügt werden kann. Voraussetzungen können
 * von vielen CharElementen enthalten sein.
 * 
 * @author V.Strelow
 */
public class Voraussetzung {
	
	private List<OptionVoraussetzung> posVoraussetzung; // Die "muss haben" Voraussetzungen
	private List<OptionVoraussetzung> negVoraussetzungen; // Die "darf nicht" Voraussetzungen
	
	/**
	 * Gibt die "muss haben" Voraussetzungen an. Eine
	 * Auswahl gilt als erfüllt, wenn der Held über die Werte verfügt, wie ein
	 * User sonst auswählen könnte.
	 * Bsp:
	 * 		Dolche 3 oder Schwerter 3 -> Ist genau dann erfüllt, wenn der Held
	 * 			über Dolche 3 oder Schwerter 3 verfügt.
	 * 
	 * @return Die Auswahl mit zu erfüllenden Optionen oder null
	 */
	public List<OptionVoraussetzung> getPosVoraussetzung() {
		return posVoraussetzung;
	}
	public void setPosVoraussetzung(List<OptionVoraussetzung> posVoraussetzung) {
		this.posVoraussetzung = posVoraussetzung;
	}
	
	/**
	 * Es gilt das gleiche wie bei "getPositiveVoraussetzung()", jedoch mit einem 
	 * "negavtiven Vorzeichen". Alle angegeben Werte müssen kleiner sein als
	 * angegeben oder wenn keine Werte angegeben werden darf das Element garnicht
	 * vorhanden sein.
	 * 
	 * @see getPositiveVoraussetzung()
	 * @return
	 */
	public List<OptionVoraussetzung> getNegVoraussetzungen() {
		return negVoraussetzungen;
	}
	public void setNegVoraussetzungen(List<OptionVoraussetzung> negVoraussetzungen) {
		this.negVoraussetzungen = negVoraussetzungen;
	}

	







	
}
