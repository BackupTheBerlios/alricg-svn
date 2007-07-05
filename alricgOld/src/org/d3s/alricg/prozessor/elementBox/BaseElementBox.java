/*
 * Created on 18.01.2006 / 15:23:33
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.elementBox;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <u>Beschreibung:</u><br> 
 * Basis-Implementation einer ElementBox auf Grundlage einer ArrayList.
 * Alle weiteren ElementBoxen können von dieser Implementierung abgeleitet werden.
 * 
 * @author V. Strelow
 */
public abstract class BaseElementBox<E> extends AbstractCollection<E> implements ElementBox<E> {
	protected ArrayList<E> elementList;
	
	public BaseElementBox() {
		elementList = new ArrayList<E>();
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.elementBoxNEW.ElementBoxAccessor#getUnmodifiableIterator()
	 */
	public Iterator<E> getUnmodifiableIterator() {
		return Collections.unmodifiableList(elementList).iterator();
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.elementBoxNEW.ElementBoxAccessor#getUnmodifiableList()
	 */
	public List<E> getUnmodifiableList() {
		return Collections.unmodifiableList(elementList);
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see java.util.AbstractCollection#add(E)
	 */
	@Override
	public abstract boolean add(E element);

	/* (non-Javadoc) Methode überschrieben
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<E> iterator() {
		return elementList.iterator();
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return elementList.size();
	}
	
	
}
