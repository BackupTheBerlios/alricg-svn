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
 * Interface welches von allen Sonderfertigkeiten erf�llt werden mu�.
 * �ber die hier definierten Methoden kann eine Sonderregel die wichtigen abl�ufe 
 * bei der Helden Generierung/ Bearbeitung beeinflussen. F�r jede Sonderregel existiert 
 * eine eingende Klasse die sich nur um die erf�llung dieser Sonderregel k�mmert.
 * 
 * Sobald eine Sonderregel zum Helden hinzugef�gt wurde, wird jede Methode dieser 
 * Sonderregel stehts bei der, in der jeweiligen Methde beschriebenen, Aktion aufgerufen. 
 * 
 * Im Unterschied zum "BasisSonderregelInterface" sind die hier aufgef�hrten Methoden
 * kein Bestandteil des Sonderregel Admins.
 * 
 * (Bisherige Implementierungen: "HerrausragendeEigenschaft")
 * 
 * @author V. Strelow
 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter
 */
public interface Sonderregel extends BasisSonderregel {
	
	/**
	 * Wird f�r die Methode "changeAnzeigeText" ben�tigt. 
	 * @author Vincent
	 */
	public enum ChangeTextContex {
		VeraendertKosten; // Wird benutzt in der Klasse "VorNachteilVerbilligtProvider"
	}
	
	
	/**
	 * Wird aufgerufen um festzustellen ob diese Sonderregel zum Helden
	 * hinzugef�gt werden kann. (vorher werden die �blichen "standart" 
	 * Pr�fungen durchgef�hrt)
	 * 
	 * @param prozessor Der HeldenProzessor f�r die Bearbeitung
	 * @param ok Das Ergebnis der "standart" Pr�fung
	 * @param srLink Der Link zu dem CharElement welches diese Sonderregel enth�lt. 
	 * 		Bei der Sonderregel "Herausragende Eigenschaft" w�hre dies der Link
	 * 		mit dem Vorteil "Herausragende Eigenschaft". Aus diesem k�nnen evtl.
	 * 		Wert, Text oder zweitZiel ausgelesen werden
	 * @return true - Die SR kann zum Helden hinzugef�gt werden, andernfalls false.
	 */
	public boolean canAddSelf(Held held, boolean ok, Link srLink);
	
	/**
	 * Wird aufgerufen wenn diese Sonderregel zum Helden hinzugef�gt wurde.
	 * 
	 * @param held Der HeldenProzessor f�r die Bearbeitung
	 * @param srLink Der Link zu dem CharElement welches diese Sonderregel enth�lt. 
	 * 		Bei der Sonderregel "Herausragende Eigenschaft" w�hre dies der Link
	 * 		mit dem Vorteil "Herausragende Eigenschaft". Aus diesem k�nnen evtl.
	 * 		Wert, Text oder zweitZiel ausgelesen werden.
	 */
	public void initSonderregel(Held held, Link srLink);
	// Evtl. �nderungen von Werten sollten hier vorgenommen werden
	
	/**
	 * Wird aufgerufen wenn eine Sonderregel wieder von einem Held entfernd wird.
	 * @param srLink Der Link zu dem CharElement welches diese Sonderregel enth�lt. 
	 * 		Bei der Sonderregel "Herausragende Eigenschaft" w�hre dies der Link
	 * 		mit dem Vorteil "Herausragende Eigenschaft". Aus diesem k�nnen evtl.
	 * 		Wert, Text oder zweitZiel ausgelesen werden.
	 */
	public void finalizeSonderregel(Link srLink);
	// Evtl. �nderungen von Werten sollten hier r�ckg�ngig gemacht werden
	
	/**
	 * Erm�glicht das �berpr�fen, ob es sich um eine bestimmte SR handelt. Neben
	 * der ID wird au�erdem ein Text und ein zweitZiel ben�tigt, da es verschiedene
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
	 * Liefert zur�ck ob diese Sonderregel f�r die Generierung angewendet wird
	 * @return true - Diese Sonderregel wird zur Generierung angewendet, ansonsten false
	 */
	public boolean isForGenerierung();
	
	/**
	 * Liefert zur�ck ob diese Sonderregel f�r das Management (nach der Generierung) angewendet wird
	 * @return true - Diese Sonderregel wird zum Management angewendet, ansonsten false
	 */
	public boolean isForManagement();
	
	/**
	 * Erm�gliches es die Anzeige von Tabellen o.�. zu �ndern. Z.B. bei 
	 * "Akademische Ausbildung" kann so der Text f�r die Anzeige, welche Kosten ge�ndert
	 * werden ge�ndert werden
	 * @param contex Der Contex in dem der Text ben�tigt wird
	 * @return Der anzuzeigende Text oder "null" falls diese Sonderregel den Text nicht 
	 * �ndert
	 */
	public String changeAnzeigeText(ChangeTextContex contex);
	
	/**
	 * @return Liefert den Namen der Sonderregel
	 */
	public String getName();
	
}