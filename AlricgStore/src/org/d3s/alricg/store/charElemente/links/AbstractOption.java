/*
 * Created 09.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.charElemente.links;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.d3s.alricg.store.charElemente.CharElement;

/**
 * Eine Option bietet Auswahlmöglichkeiten zur Auswahl von CharElementen.
 * Da es recht Modi für die Wahl gibt, wird das Objekt auch je nach Modi
 * konkretisiert. 
 * 
 * @author Vincent
 */
public abstract class AbstractOption<ZIEL extends CharElement> implements Option<ZIEL> {
	/**
	 * Eine Liste mit allen Elementen für diese Option. Wie diese Liste
	 * interpretiert wird, hängt von der Konkreten Option ab.
	 */
    private List<IdLink<ZIEL>> linkList;
    
    /**
     * Eine Alternative zu dieser Option. Es kann auch die Alternative gewählt werden,
     * in dem Fall "verfällt" diese Option. (Eine altervnatie Option kann auch wieder
     * eine Alternative besitzen, so dass eine Liste von Alternativen ensteht)  
     */
    private Option<ZIEL> alternativOption;

    
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.charElemente.links.Option#getAlternativOption()
	 */
	@Override
	@XmlElements( 
			{
				@XmlElement(name = "OptionAnzahl", type = OptionAnzahl.class),
				@XmlElement(name = "OptionListe", type = OptionListe.class),
				@XmlElement(name = "OptionVerteilung", type = OptionVerteilung.class),
				@XmlElement(name = "OptionVoraussetzung", type = OptionVoraussetzung.class)
			}
		)
	public Option<ZIEL> getAlternativOption() {
		return alternativOption;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.charElemente.links.Option#getLinkList()
	 */
	@Override
	@XmlElement(required = true)
	public List<IdLink<ZIEL>> getLinkList() {
		return linkList;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.charElemente.links.Option#setAlternativOption(org.d3s.alricg.store.charElemente.links.Option)
	 */
	@Override
	public void setAlternativOption(Option<ZIEL> alternativOption) {
		this.alternativOption = alternativOption;
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.charElemente.links.Option#setLinkList(java.util.List)
	 */
	@Override
	public void setLinkList(List<IdLink<ZIEL>> linkList) {
		this.linkList = linkList;
	}
	
	/**
	 * Hilfsfunktion für die copyOption Methoden
	 * @param opt Option zum modifizieren
	 */
	protected void copyBasicValues(Option opt) {
		if (opt.getAlternativOption() != null) {
			opt.setAlternativOption(getAlternativOption().copyOption());
		}
		if (opt.getLinkList() != null) {
			List<IdLink> tmpList = new ArrayList<IdLink>();
			tmpList.addAll(opt.getLinkList());
			opt.setLinkList(tmpList);
		}
	}

}
