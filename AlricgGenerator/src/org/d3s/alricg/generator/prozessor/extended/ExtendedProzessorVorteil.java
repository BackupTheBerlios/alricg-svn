/**
 * 
 */
package org.d3s.alricg.generator.prozessor.extended;

import java.util.List;

import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Vorteil;

/**
 * Erweiterte Funktionen f�r Vorteile. Diese Funktionen bietet der Standard-Prozessor
 * f�r Vorteile nicht an, sie werden jedoch ben�tigt. Durch die Deklaration im
 * Interface k�nnen die Funktionen ebenfalls �ber den LinkProzessorFront genutzt werden.
 * 
 * @author Vincent
 */
public interface ExtendedProzessorVorteil {

	/**
	 * Diese Methode liefert die Liste von m�glichen ZweitZielen f�r den 
	 * �bergebenen Vorteil zur�ck.
	 * @param vorteil Der Vorteil
	 * @return Liste von m�glichen ZweitZielen oder "null" wenn kein ZweitZiel m�glich ist.
	 */
	public List<CharElement> getMoeglicheZweitZiele(Vorteil fertigkeit);
	
	/**
	 * Diese Methode setzt zu einem Vorteil die Liste der m�glichen ZweitZiele. Diese Methode wird durch
	 * die Sonderregel aufgerufen, bevor der Vorteil zum Charakter hinzugef�gt wird. Wird der Vorteil 
	 * wieder vom Helden entfernt, so soll auch die Liste wieder entfernt werden.
	 * @param vorteil Der Vorteil
	 * @param zweitZiele Die Liste der m�glichen CharElemente zu dem Vorteil
	 */
	//public void addMoeglicheZweitZiele(Vorteil vorteil, List<CharElement> zweitZiele);
	
	/**
	 * Diese Methode entfernt die List der m�glichen ZweitZiele eines Vorteils. Die Methode wird 
	 * vom SonderregelProzessor nach entfernen des Vorteils aufgerufen.
	 * @param vorteil Der Vorteil
	 */
	//public void removeMoeglicheZweitZiele(Vorteil vorteil);
	
}
