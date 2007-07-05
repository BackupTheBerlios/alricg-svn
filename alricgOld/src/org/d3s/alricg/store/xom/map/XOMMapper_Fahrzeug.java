/*
 * Created on 23.06.2005 / 15:16:17
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store.xom.map;

import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.charZusatz.Fahrzeug;

/**
 * <code>XOMMapper</code> für ein <code>Fahrzeug</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.charZusatz.Fahrzeug
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
class XOMMapper_Fahrzeug extends XOMMapper_Gegenstand {

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {

        super.map(xmlElement, charElement);

        // my mapping
        final Fahrzeug fahrzeug = (Fahrzeug) charElement;

        // Aussehen
        Element child = xmlElement.getFirstChildElement("aussehen");
        if (child != null) {
            fahrzeug.setAussehen(child.getValue());
        }

        // Fahrzeugart
        Attribute a = xmlElement.getAttribute("fahrzeugArt");
        if (a != null) {
            fahrzeug.setTyp(a.getValue());
        }
    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        
        super.map(charElement, xmlElement);

        // my mapping
        final Fahrzeug fahrzeug = (Fahrzeug) charElement;
        xmlElement.setLocalName("fahrzeug");

        // Aussehen
        String sValue = fahrzeug.getAussehen();
        if (sValue != null && sValue.trim().length() > 0) {
            final Element e = new Element("aussehen");
            e.appendChild(sValue);
            xmlElement.appendChild(e);
        }

        // Typ/Art des Fahrzeugs
        sValue = fahrzeug.getTyp();
        if (sValue != null && sValue.trim().length() > 0) {
            xmlElement.addAttribute(new Attribute("fahrzeugArt", sValue));
        }
    }

}
