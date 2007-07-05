/**
 * 
 */
package org.d3s.alricg.prozessor.editor;

import org.d3s.alricg.charKomponenten.CharElement;

/**
 * @author Vincent
 *
 */
public abstract class BaseProzessorEditorCharElement<ELEM extends CharElement> extends ProzessorEditor<ELEM, ELEM> {

	/**
	 * Erzeugt einen neues Element mit Initialen Werten, fügt dieses
	 * zur Datenbasis hinzu und Informiert die Observer.
	 * @return Das neu erzeugte Element
	 */
	public abstract ELEM createNewElement();
	
	/**
	 * Prüft, ob ein Element benutzt wird. Dies ist der Fall, wenn das Element
	 * in einer Beziehung zu einem anderen Element steht.
	 * @param element TODO
	 * @return
	 */
	public abstract boolean isElementUsed(ELEM element);
}
