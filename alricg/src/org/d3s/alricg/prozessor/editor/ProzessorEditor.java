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
public abstract class ProzessorEditor<ZIEL extends CharElement, ELEM> extends BaseProzessorObserver<ZIEL> implements Prozessor<ZIEL, ELEM> {
	protected BaseElementBox<ELEM> elementBox;
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#getElementBox()
	 */
	public ElementBox<ELEM> getElementBox() {
		return elementBox;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#getUnmodifiableList()
	 */
	public List<ELEM> getUnmodifiableList() {
		return elementBox.getUnmodifiableList();
	}
	
}
