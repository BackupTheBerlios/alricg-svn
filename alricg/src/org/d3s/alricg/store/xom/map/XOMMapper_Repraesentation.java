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
import org.d3s.alricg.charKomponenten.Repraesentation;

/**
 * <code>XOMMapper</code> für eine <code>Repraesentation</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.Repraesentation
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
class XOMMapper_Repraesentation extends XOMMapper_CharElement {

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {
        super.map(xmlElement, charElement);

        // my mapping
        final Repraesentation repraesentation = (Repraesentation) charElement;

        // Auslesen, ob es eine "echte" Repräsentation ist
        final Element current = xmlElement.getFirstChildElement("isEchteRep");
        if (current != null) {
            assert current.getValue().equalsIgnoreCase("true") || current.getValue().equalsIgnoreCase("false");
            repraesentation.setEchteRep(Boolean.parseBoolean(current.getValue()));
        }

        // Auslesen der Abkürzung
        repraesentation.setAbkuerzung(xmlElement.getAttributeValue("abkuerzung"));

    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        super.map(charElement, xmlElement);

        // my mapping
        final Repraesentation repraesentation = (Repraesentation) charElement;
        xmlElement.setLocalName("repraesentation");

        // Schreiben ob es eine "echte" Repräsentation ist
        final Element e = new Element("isEchteRep");
        e.appendChild(Boolean.toString(repraesentation.isEchteRep()));
        xmlElement.appendChild(e);

        // Schreiben des Attributs Abkürzung
        xmlElement.addAttribute(new Attribute("abkuerzung", repraesentation.getAbkuerzung()));

    }

}
