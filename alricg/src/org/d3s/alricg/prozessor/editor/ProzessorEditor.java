/*
 * Created on 31.05.2006 / 14:53:59
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.editor;

import java.util.List;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.prozessor.BaseProzessorObserver;
import org.d3s.alricg.prozessor.Prozessor;
import org.d3s.alricg.prozessor.elementBox.BaseElementBox;
import org.d3s.alricg.prozessor.elementBox.ElementBox;

/**
 * <u>Beschreibung:</u><br> 
 * Die Oberklasse für alle Prozessoren, die Editor-Funktionen übernehmen.
 * Die abgeleiteten Klassen spezialisieren sich auf bestimmte Aufgaben,
 * wie die Editierung von Rassen, Kulturen, Talenten, SFs aber auch 
 * Auswahlen, Voraussetzungen.
 * 
 * Im Fall eines Editors gilt: ZIEL = E
 * 
 * @author V. Strelow
 */
public abstract class ProzessorEditor<E extends CharElement> 
								extends BaseProzessorObserver<E>
								implements Prozessor<E, E>
{
	protected BaseElementBox<E> elementBox;
//	protected ArrayList<ProzessorObserver> observerList;
	protected E prozessorZiel;
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessorNEW.Prozessor#getElementBox()
	 */
	public ElementBox<E> getElementBox() {
		return elementBox;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessorNEW.Prozessor#getUnmodifiableList()
	 */
	public List<E> getUnmodifiableList() {
		return elementBox.getUnmodifiableList();
	}
	
	// ---------- Zu sätzliche Methodes für den Editor -----------

	/**
	 * Überprüft ob dieses CharElement so in Ordnung ist und übernommen werden kann
	 * @param element
	 */
	public void checkCharElement(E element) {

	}
	
	/**
	 * Erzeugt einen neues Element mit Initialen Werten, fügt dieses
	 * zur Datenbasis hinzu und Informiert die Observer.
	 * @return Das neu erzeugte Element
	 */
	public abstract E createNewElement();
	
	/**
	 * Löscht ein Element aus der Datenbasis. 
	 * @param element Das zu Löschende Element
	 */
	public abstract void deleteElement(E element);
	
	/**
	 * Prüft, ob ein Element benutzt wird. Dies ist der Fall, wenn das Element
	 * in einer Beziehung zu einem anderen Element steht.
	 * @return
	 */
	public abstract boolean isElementUsed();
	
	/**
	 * Alle Operationen die mit Hilfe des Editor ausgeführt werden können, können 
	 * natürlich für verschiedene CharElemente durchgeführt werden. 
	 * Wird z.B. mit einer "addElement" Methode Talent-Modi zu einer Rasse 
	 * hinzugefügt, so muß auch die genaue Rasse angegeben werden. Dies geschieht
	 * mit dieser Methode
	 * 
	 * Dem entsprechend ist das setzen des Ziels ist nur beim EditorProzessor nötig, 
	 * da das Ziel ansonsten klar ist: Nämlich der Held!
	 * 
	 * @param element Das Element das bearbeitet werden soll.
	 */
	public void setProzessorZiel(E element) {
		prozessorZiel = element;
	}
	
	/**
	 * Liefert das momentane Ziel des Editors, also das Element welches 
	 * berabeitet wird.
	 * @return
	 * @see setProzessorZiel(Object)
	 */
	public E getProzessorZiel() {
		return prozessorZiel;
	}
}
