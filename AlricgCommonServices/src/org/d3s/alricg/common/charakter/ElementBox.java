/*
 * Created on 19.01.2006 / 12:02:41
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.common.charakter;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Fertigkeit;
import org.d3s.alricg.store.charElemente.links.Link;

/**
 * <u>Beschreibung:</u><br> 
 * Eine Implementierung der ElementBox für Links. Diese ElementBox 
 * soll immer verwendet werden, wenn mit Links gearbeitet wird. 
 * 
 * @author V. Strelow
 */
public class ElementBox<E extends Link> extends AbstractCollection<E>{
	private final LinkLinkComparator<Link> compLinkLink;
	private final LinkStringComparator compLinkString;
	private final ArrayList<E> elementList;
	
	public ElementBox() {
		elementList = new ArrayList<E>();
		compLinkLink = new LinkLinkComparator<Link>();
		compLinkString = new LinkStringComparator();
	}

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
	
	/**
	 * @return Einen Iteratpor mit allen enthaltenen Elementen, der die original
	 * 	Liste nicht verändern kann.
	 */
	public Iterator<E> getUnmodifiableIterator() {
		return Collections.unmodifiableList(elementList).iterator();
	}

	/**
	 * @return Eine nicht veränderbare Liste mit allen enthaltenen Elementen
	 */
	public List<E> getUnmodifiableList() {
		return Collections.unmodifiableList(elementList);
	}
	
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
	public E getObjectById(String id) {
		int index;
		
		index = Collections.binarySearch(elementList, id, compLinkString);
		
		if (index < 0) {
			return null; // Ein Link mit dieser ID ist nicht enthalten
		}
		
		return elementList.get(index);
	}
	
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
	public E getObjectById(CharElement charElement) {
		return getObjectById(charElement.getId());
	}
	
	/**
	 * Sucht zu einem CharElement alle Links mit dem gleichen CharElement als Ziel heraus.
	 * DInge wie Text oder Zweitziel spielen keine Rolle.
	 * 
	 * @param CharElement Das CharElement nach dem gesucht wird
	 * @return Eine Liste von allen Links, auf die die Kriterien zutreffen
	 */
	public List<E> getObjectsById(CharElement charElement) {
		int index, tmpIdx;
		ArrayList<E> list;
		
		index = Collections.binarySearch(elementList, charElement.getId(), compLinkString);
		
		list = new ArrayList<E>();
		if (index < 0) {
			return list; // Ein Link mit dieser ID ist nicht enthalten
		}
		
		// Es können mehrer gleiche Elemente existieren, binarySearch liefert nur ein "zufälliges"
		// Element aus der möglichen Menge. Daher müssen nach "oben" und "unten" die Elemente
		// abgesucht werden.
		
		// Nach "oben" die Elemente prüfen
		tmpIdx = index;
		while(tmpIdx < elementList.size() && 
				compLinkString.compare(elementList.get(tmpIdx), charElement.getId()) == 0) {
			list.add(elementList.get(tmpIdx));
			tmpIdx++;
		}

		// Nach "unten" die Elemente prüfen
		tmpIdx = index -1;
		while(tmpIdx >= 0 && 
				compLinkString.compare(elementList.get(tmpIdx), charElement.getId()) == 0) {
			list.add(elementList.get(tmpIdx));
			tmpIdx--;
		}
		
		return list;
	}

	/**
	 * Such ein Element mit der gleichen AdditionsID wie "fertigkeit". Bei mehreren Vorkommen
	 * wird das erste gefundene geliefert.
	 * @param fertigkeit Fertigkeit nach dessen AdditionsID gesucht wird
	 * @return Link mit der Fertigkeit als Ziel, oder null fall kein solches Element existiert
	 */
	public E getEqualAdditionsFamilie(Fertigkeit fertigkeit) {
		if (fertigkeit.getAdditionsFamilie() == null) return null;
		
		for(E element : elementList) {
			if ( !element.getZiel().getClass().equals(fertigkeit.getClass()) ) continue;
			if ( ((Fertigkeit)element.getZiel()).getAdditionsFamilie() == null) continue;
			
			if ( ((Fertigkeit)element.getZiel()).getAdditionsFamilie().getAdditionsID().equals(
					fertigkeit.getAdditionsFamilie().getAdditionsID()) ) {
				return element;
			}
		}
		return null;
	}
	
	/**
	 * Sucht zu einem Link das gleichartige Gegenstücke aus der ElementBox herraus.
	 * Die Prüfung ob ein Link "gleichartig" ist, erfolgt mit "Link.isEqualLink(..)".
	 * Dies ist dann zutreffend, wenn ziel, text und zweitZiel gleich sind.
	 * 
	 * @param link Der link zu dem ein gleichartige Link gesucht wird
	 * @return Eine Liste von allen Links, auf die die Kriterien zutreffen
	 * @see org.d3s.alricg.charKomponenten.links.Link#isEqualLink(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public List<E> getEqualObjects(Link link) {
		List<E> list = getObjectsById(link.getZiel());
		
		Iterator<E> ite = list.iterator();
		while(ite.hasNext()) {
			if (!ite.next().isEqualLink(link)) ite.remove();
		}
		
		return list;
		
		/*
		int index, tmpIdx;
		ArrayList<E> list;
		
		index = Collections.binarySearch(elementList, link, compLinkLink);
		
		list = new ArrayList<E>();
		if (index < 0) {
			return list; // Ein Link mit dieser ID ist nicht enthalten
		}
		
		
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
		
		return list;*/
	}
	
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
     *
    public List<E> getEqualLinks(String id, String text, CharElement zweitZiel) {
    	Link link = new IdLink(null, null);
    	
    	// Temporären Link erzeugen
    	link.setZiel(FactoryFinder.find().getData().getCharElement(id));
    	link.setText(text);
    	link.setZiel(zweitZiel);
    	
        return getEqualObjects(link);
    }*/
    
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
    private class LinkStringComparator implements Comparator {
    	
		/* (non-Javadoc) Methode überschrieben
		 * @see java.util.Comparator#compare(T, T)
		 */
		public int compare(Object link1, Object link2) {
			
			return ((Link) link1).getZiel().getId()
								.compareTo(link2.toString());
		}
    }

}
