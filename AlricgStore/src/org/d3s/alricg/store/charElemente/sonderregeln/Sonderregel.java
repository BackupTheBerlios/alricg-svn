/*
 * Created on 30.04.2005 / 23:07:24
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente.sonderregeln;

import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.held.Held;

/**
 * <u>Beschreibung:</u><br> 
 * Interface welches von allen Sonderfertigkeiten erfüllt werden muß.
 * Über die hier definierten Methoden kann eine Sonderregel die wichtigen abläufe 
 * bei der Helden Generierung/ Bearbeitung beeinflussen. Für jede Sonderregel existiert 
 * eine eingende Klasse die sich nur um die erfüllung dieser Sonderregel kümmert.
 * 
 * Sobald eine Sonderregel zum Helden hinzugefügt wurde, wird jede Methode dieser 
 * Sonderregel stehts bei der, in der jeweiligen Methde beschriebenen, Aktion aufgerufen. 
 * 
 * Im Unterschied zum "BasisSonderregelInterface" sind die hier aufgeführten Methoden
 * kein Bestandteil des Sonderregel Admins.
 * 
 * (Bisherige Implementierungen: "HerrausragendeEigenschaft")
 * 
 * @author V. Strelow
 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter
 */
public interface Sonderregel extends BasisSonderregel {
	
	/**
	 * Wird für die Methode "changeAnzeigeText" benötigt. 
	 * @author Vincent
	 */
	public enum ChangeTextContex {
		VeraendertKosten; // Wird benutzt in der Klasse "VorNachteilVerbilligtProvider"
	}
	
	
	/**
	 * Wird aufgerufen um festzustellen ob diese Sonderregel zum Helden
	 * hinzugefügt werden kann. (vorher werden die üblichen "standart" 
	 * Prüfungen durchgeführt)
	 * 
	 * @param prozessor Der HeldenProzessor für die Bearbeitung
	 * @param ok Das Ergebnis der "standart" Prüfung
	 * @param srLink Der Link zu dem CharElement welches diese Sonderregel enthält. 
	 * 		Bei der Sonderregel "Herausragende Eigenschaft" währe dies der Link
	 * 		mit dem Vorteil "Herausragende Eigenschaft". Aus diesem können evtl.
	 * 		Wert, Text oder zweitZiel ausgelesen werden
	 * @return true - Die SR kann zum Helden hinzugefügt werden, andernfalls false.
	 */
	public boolean canAddSelf(Held held, boolean ok, Link srLink);
	
	/**
	 * Wird aufgerufen wenn diese Sonderregel zum Helden hinzugefügt wurde.
	 * 
	 * @param held Der HeldenProzessor für die Bearbeitung
	 * @param srLink Der Link zu dem CharElement welches diese Sonderregel enthält. 
	 * 		Bei der Sonderregel "Herausragende Eigenschaft" währe dies der Link
	 * 		mit dem Vorteil "Herausragende Eigenschaft". Aus diesem können evtl.
	 * 		Wert, Text oder zweitZiel ausgelesen werden.
	 */
	public void initSonderregel(Held held, Link srLink);
	// Evtl. Änderungen von Werten sollten hier vorgenommen werden
	
	/**
	 * Wird aufgerufen wenn eine Sonderregel wieder von einem Held entfernd wird.
	 * @param srLink Der Link zu dem CharElement welches diese Sonderregel enthält. 
	 * 		Bei der Sonderregel "Herausragende Eigenschaft" währe dies der Link
	 * 		mit dem Vorteil "Herausragende Eigenschaft". Aus diesem können evtl.
	 * 		Wert, Text oder zweitZiel ausgelesen werden.
	 */
	public void finalizeSonderregel(Link srLink);
	// Evtl. Änderungen von Werten sollten hier rückgängig gemacht werden
	
	/**
	 * Ermöglicht das überprüfen, ob es sich um eine bestimmte SR handelt. Neben
	 * der ID wird außerdem ein Text und ein zweitZiel benötigt, da es verschiedene
	 * Varianten von SR geben kann. (z.B. "Herausragende Eigenschaft" mit zweitZiel "KK",
	 * und "Herausr. E." mit zweitZiel "MU")
	 * @param id Die ID der SR, nach der abgefragt wird.
	 * @param text Der Text der SR, nach der abgefragt wird (kann null sein)
	 * @param zweitZiel Das zweitZiel, nach dem abgefragt wird (kann null sein)
	 * @return true - Diese SR hat die id "id", den text "text" und das zweitZiel "zweitZiel"
	 * 		false - Diese SR ist NICHT die abgefragte SR
	 */
	public boolean isSonderregel(String id, String text, CharElement zweitZiel);
	
	/**
	 * Liefert zurück ob diese Sonderregel für die Generierung angewendet wird
	 * @return true - Diese Sonderregel wird zur Generierung angewendet, ansonsten false
	 */
	public boolean isForGenerierung();
	
	/**
	 * Liefert zurück ob diese Sonderregel für das Management (nach der Generierung) angewendet wird
	 * @return true - Diese Sonderregel wird zum Management angewendet, ansonsten false
	 */
	public boolean isForManagement();
	
	/**
	 * Ermögliches es die Anzeige von Tabellen o.ä. zu ändern. Z.B. bei 
	 * "Akademische Ausbildung" kann so der Text für die Anzeige, welche Kosten geändert
	 * werden geändert werden
	 * @param contex Der Contex in dem der Text benötigt wird
	 * @return Der anzuzeigende Text oder "null" falls diese Sonderregel den Text nicht 
	 * ändert
	 */
	public String changeAnzeigeText(ChangeTextContex contex);
	
	/**
	 * @return Liefert den Namen der Sonderregel
	 */
	public String getName();
	
}