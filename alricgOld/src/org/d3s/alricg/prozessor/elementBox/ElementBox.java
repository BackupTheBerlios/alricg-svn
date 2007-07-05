/*
 * Created on 17.05.2006 / 13:56:17
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.elementBox;

import java.util.Iterator;
import java.util.List;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.links.Link;

/**
 * <u>Beschreibung:</u><br> 
 * Das Interface ElementBox dient der Speicherung von Daten die von 
 * einem Prozessor verwaltet werden. Im Modus "Generierung" oder 
 * "Management" von Helden sind die Daten Links, im Modus "Editor"
 * sind es teilweise auch direkt "CharElemente".
 * @author V. Strelow
 */
public interface ElementBox<E> {
	
	/**
	 * @return Eine nicht veränderbare Liste mit allen enthaltenen Elementen
	 */
	public List<E> getUnmodifiableList();
	
	/**
	 * @return Einen Iteratpor mit allen enthaltenen Elementen, der die original
	 * 	Liste nicht verändern kann.
	 */
	public Iterator<E> getUnmodifiableIterator();
	
	/**
	 * Mit dieser Methode kann aus dem zum Helden gehörenden Elementen ein bestimmtes 
	 * gesucht werden. Dabei ist diese Methode ungenauer, da hier weder Text noch
	 * ZweitZiel angegeben sind. Es wird lediglich das erste Element mit der passenden
	 * ID gelieftert. Gibt es mehrer so ist unbestimmt welches geliefert wird.
	 * 
	 * @param id Die id zu der das passende Elemente gesucht wird.
	 * @return Das erste Element mit der id "id" oder "null", falls es kein solches Element
	 * gibt
	 */
	public E getObjectById(String id);
	
	/**
	 * Eine Version von "getObjectById(String id)". Hierbei wird anstatt der Id ein CharElement
	 * angegeben, und die Id innerhalb der Methode aus dem CharElement ausgelesen. Diese Methode
	 * ungenau, da hier weder Text noch ZweitZiel angegeben sind. Es wird lediglich das erste 
	 * Element mit der passenden ID gelieftert. 
	 * #Gibt es mehrer so ist unbestimmt welches geliefert wird.
	 * 
	 * @param charElement Das Element, zu dem ein Element mit der gleichen ID gesucht wird.
	 * @return Das erste Element mit der id "id" oder "null", falls es kein solches Element
	 * gibt
	 */
	public E getObjectById(CharElement charElement);
	
	/**
	 * Sucht zu einem Link das gleichartige Gegenstücke aus der ElementBox herraus.
	 * Die Prüfung ob ein Link "gleichartig" ist, erfolgt mit "Link.isEqualLink(..)".
	 * Dies ist dann zutreffend, wenn ziel, text und zweitZiel gleich sind.
	 * 
	 * @param link Der link zu dem ein gleichartige Link gesucht wird
	 * @return Eine Liste von allen Links, auf die die Kriterien zutreffen
	 * @see org.d3s.alricg.charKomponenten.links.Link#isEqualLink(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public List<E> getEqualObjects(Link obj);
	
	/**
	 * Überprüft ob in dieser ElementBox ein gleichartiges Gegenstück zu
	 * dem Link enthalten ist. 	Die Prüfung ob ein Link "gleichartig" ist, 
	 * erfolgt mit "Link.isEqualLink(..)".
	 * Dies ist dann zutreffend, wenn ziel, text und zweitZiel gleich sind.
	 * 
	 * @param link Der link der nach einem gleichartigem Gegenstück überprüft wird
	 * @return true - Es existiert in dieser ElementBox ein gleicharties Gegenstück, 
	 * 				ansonsten false
	 * @see org.d3s.alricg.charKomponenten.links.Link#isEqualLink(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean contiansEqualObject(Link obj);
}
