/*
 * Created on 19.01.2006 / 12:02:41
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.elementBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.store.FactoryFinder;

/**
 * <u>Beschreibung:</u><br> 
 * Eine Implementierung der ElementBox für Links. Diese ElementBox 
 * soll immer verwendet werden, wenn mit Links gearbeitet wird. 
 * 
 * @author V. Strelow
 */
public class ElementBoxLink<E extends Link> extends BaseElementBox<E> {
	private LinkLinkComparator<Link> compLinkLink;
	private LinkStringComparator compLinkString;
	
	public ElementBoxLink() {
		compLinkLink = new LinkLinkComparator<Link>();
		compLinkString = new LinkStringComparator();
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.ElementBox#getObjectById(java.lang.String)
	 */
	public E getObjectById(String id) {
		int index;
		
		index = Collections.binarySearch(elementList, id, compLinkString);
		
		if (index < 0) {
			return null; // Ein Link mit dieser ID ist nicht enthalten
		}
		
		return elementList.get(index);
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.elementBoxNEW.ElementBoxAccessor#getObjectById(org.d3s.alricg.charKomponenten.CharElement)
	 */
	public E getObjectById(CharElement charElement) {
		return getObjectById(charElement.getId());
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.ElementBox#getSimilarObjects(E)
	 */
	public List<E> getEqualObjects(Link link) {
		int index, tmpIdx;
		ArrayList<E> list;
		
		index = Collections.binarySearch(elementList, link, compLinkLink);
		
		if (index < 0) {
			return null; // Ein Link mit dieser ID ist nicht enthalten
		}
		list = new ArrayList<E>();
		
		// Es können mehrer gleiche Elemente existieren, binarySearch liefert nur ein "zufälliges"
		// Element aus der möglichen Menge. Daher müssen nach "oben" und "unten" die Elemente
		// abgesucht werden.
		
		// Nach "oben" die Elemente prüfen
		tmpIdx = index;
		while(tmpIdx < elementList.size() && 
				compLinkLink.compare(elementList.get(tmpIdx), link) == 0) {
			if ( link.isEqualLink(elementList.get(tmpIdx)) ) {
				list.add(elementList.get(tmpIdx));
			}
			tmpIdx++;
		}

		// Nach "unten" die Elemente prüfen
		tmpIdx = index -1;
		while(tmpIdx >= 0 && 
				compLinkLink.compare(elementList.get(tmpIdx), link) == 0) {
			if ( link.isEqualLink(elementList.get(tmpIdx)) ) {
				list.add(elementList.get(tmpIdx));
			}
			tmpIdx--;
		}
		
		return list;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.elementBoxNEW.ElementBoxAccessor#contiansSimilarObject(E)
	 */
	public boolean contiansEqualObject(Link link) {
		
		if ( getEqualObjects(link).size() > 0) {
			return true;
		}
		
		return false;
	}

	/**
     * Mit dieser Methode kann aus dem zum Helden gehörenden Elementen bestimmte gesucht werden. 
     * Beispiele: 
     * - Gesucht "Vorurteile gegen Orks": Es wird die ID von "Vorurteile gegen", dessen CharKomponente (="Nachteil") und
     * der Text "Orks" benötigt. "Vorurteile gegen Orks" ist etwas anderes als "Vorurteile gegen Zwerge" 
     * - Gesucht "Begabung Schwerter": Es wird die ID von "Begabung", dessen CharKomponente (="Vorteil") und das zweitZiel 
     * Talent "Schwerter" benötigt. "Begabung Schwerter" ist etwas anderes als "Begabung Reiten" 
     * - Gesucht "Schwerter 6": Es wird die ID von "Schwerter" und dessen CharKomponente (="Talent") benötigt.
     * 
     * @param id Die ID des CharElements, welches als Ziel im gesuchten Link steht.
     * @param text Der Text des gesuchten Links; kann null sein
     * @param zweitZiel Das zweitZiel des gesuchten Links; kann null sein
     * @return Eine Liste von allen Links, auf die die Kriterien zutreffen
     */
    public List<E> getEqualLinks(String id, String text, CharElement zweitZiel) {
    	Link link = new IdLink(null, null);
    	
    	// Temporären Link erzeugen
    	link.setZiel(FactoryFinder.find().getData().getCharElement(id));
    	link.setText(text);
    	link.setZiel(zweitZiel);
    	
        return getEqualObjects(link);
    }

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.elementBoxNEW.ElementBox#add(E)
	 */
	@Override
	public boolean add(E element) {
		int index;
		
		// Korrekte Position bestimmen (wichtig für spätere zugriffe)
		index = Collections.binarySearch(elementList, element, compLinkLink);
		if (index < 0) {
			index = Math.abs(index + 1);
		}
		
		// Element dort einfügen (sortierung erleichtert das suchen)
		elementList.add(index, element);
		
		return true;
	}
    
	//----------------- Comperatoren
	
	/**
	 * <u>Beschreibung:</u><br> 
	 * Klasse zum Vergleich von zwei Links miteinander.
	 * @author V. Strelow
	 */
    class LinkLinkComparator<LINK_TYPE extends Link> implements Comparator<LINK_TYPE> {
		/* (non-Javadoc) Methode überschrieben
		 * @see java.util.Comparator#compare(T, T)
		 */
		public int compare(LINK_TYPE link1, LINK_TYPE link2) {
			return  link1.getZiel().getId()
						.compareTo(link2.getZiel().getId());
		}
    }
    
	/**
	 * <u>Beschreibung:</u><br> 
	 * Klasse zum Vergleich von zwei Links miteinander.
	 * @author V. Strelow
	 */
    class LinkStringComparator implements Comparator {
    	
		/* (non-Javadoc) Methode überschrieben
		 * @see java.util.Comparator#compare(T, T)
		 */
		public int compare(Object link1, Object link2) {
			
			return ((Link) link1).getZiel().getId()
								.compareTo(link2.toString());
		}
    }

}
