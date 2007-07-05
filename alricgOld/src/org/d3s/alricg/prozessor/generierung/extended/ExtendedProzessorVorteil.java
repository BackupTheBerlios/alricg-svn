/**
 * 
 */
package org.d3s.alricg.prozessor.generierung.extended;

import java.util.List;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Vorteil;

/**
 * Erweiterte Funktionen für Vorteile. Diese Funktionen bietet der Standard-Prozessor
 * für Vorteile nicht an, sie werden jedoch benötigt. Durch die Deklaration im
 * Interface können die Funktionen ebenfalls über den LinkProzessorFront genutzt werden.
 * 
 * @author Vincent
 */
public interface ExtendedProzessorVorteil {

	/**
	 * Diese Methode liefert die Liste von möglichen ZweitZielen für den 
	 * übergebenen Vorteil zurück.
	 * @param vorteil Der Vorteil
	 * @return Liste von möglichen ZweitZielen oder "null" wenn kein ZweitZiel möglich ist.
	 */
	public List<CharElement> getMoeglicheZweitZiele(Vorteil vorteil);
	
	/**
	 * Diese Methode setzt zu einem Vorteil die Liste der möglichen ZweitZiele. Diese Methode wird durch
	 * die Sonderregel aufgerufen, bevor der Vorteil zum Charakter hinzugefügt wird. Wird der Vorteil 
	 * wieder vom Helden entfernt, so soll auch die Liste wieder entfernt werden.
	 * @param vorteil Der Vorteil
	 * @param zweitZiele Die Liste der möglichen CharElemente zu dem Vorteil
	 */
	public void addMoeglicheZweitZiele(Vorteil vorteil, List<CharElement> zweitZiele);
	
	/**
	 * Diese Methode entfernt die List der möglichen ZweitZiele eines Vorteils. Die Methode wird 
	 * vom SonderregelProzessor nach entfernen des Vorteils aufgerufen.
	 * @param vorteil Der Vorteil
	 */
	public void removeMoeglicheZweitZiele(Vorteil vorteil);
	
}
