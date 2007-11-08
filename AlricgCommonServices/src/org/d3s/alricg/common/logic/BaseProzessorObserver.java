/*
 * Created on 13.06.2006 / 17:50:12
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.common.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <u>Beschreibung:</u><br> 
 * Grundlage f�r alle Prozessoren die Observer verwalten. Bietet Funkionen zur Verwaltung
 * der Observer an.
 * 
 * @author V. Strelow
 */
public abstract class BaseProzessorObserver<E> {
	protected ArrayList<ProzessorObserver> observerList;
	
	public BaseProzessorObserver() {
		observerList = new ArrayList<ProzessorObserver>();
	}
	
	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#registerObserver(org.d3s.alricg.gui.komponenten.table.ProzessorObserver)
	 */
	public void registerObserver(ProzessorObserver observer) {
		observerList.add(observer);
	}
	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#unregisterObserver(org.d3s.alricg.gui.komponenten.table.ProzessorObserver)
	 */
	public void unregisterObserver(ProzessorObserver observer) {
		observerList.remove(observer);	
	}
	
	/**
	 * Informiert alle registrierten Observer �ber eine �nderung des
	 * Datenbestandes: Element hinzugef�gt 
	 * 
	 * @param element Das Element, welches hinzugef�gt wurde
	 */
	protected void notifyObserverAddElement(E element) {
		Iterator<ProzessorObserver> ite = observerList.iterator();
		
		while (ite.hasNext()) {
			ite.next().addElement(element);
		}
	}
	
	/**
	 * Informiert alle registrierten Observer �ber eine �nderung des
	 * Datenbestandes: Element entfernd.
	 * 
	 * @param element Das Element, welches entfernd wurde
	 */
	protected void notifyObserverRemoveElement(E element) {
		Iterator<ProzessorObserver> ite = observerList.iterator();
		
		while (ite.hasNext()) {
			ite.next().removeElement(element);
		}
	}
	
	/**
	 * Informiert alle registrierten Observer �ber eine �nderung des
	 * Datenbestandes: Element ge�ndert
	 * 
	 * @param element Das Element, welches ge�ndert wurde
	 */
	protected void notifyObserverUpdateElement(E element) {
		Iterator<ProzessorObserver> ite = observerList.iterator();
		
		while (ite.hasNext()) {
			ite.next().updateElement(element);
		}
	}
	
	/**
	 * Informiert alle registrierten Observer �ber eine �nderung des
	 * Datenbestandes: Kompletter Datenbestand ausgetauscht
	 * 
	 * @param List Liste der neuen Element, alle alten Element sind ung�ltig
	 */
	protected void observerSetData(List dataList) {
		Iterator<ProzessorObserver> ite = observerList.iterator();
		
		while (ite.hasNext()) {
			ite.next().setData(dataList);
		}
	}

}
