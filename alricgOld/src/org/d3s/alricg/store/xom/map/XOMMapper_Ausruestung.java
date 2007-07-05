/*
 * Created on 23.06.2005 / 15:16:17
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store.xom.map;

import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.charZusatz.Ausruestung;

/**
 * <code>XOMMapper</code> für <code>Ausruestung</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.charZusatz.Ausruestung
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
class XOMMapper_Ausruestung extends XOMMapper_Gegenstand {

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {

        super.map(xmlElement, charElement);

        // my mapping
        final Ausruestung ausruestung = (Ausruestung) charElement;

        // Behälter
        Element child = xmlElement.getFirstChildElement("istBehaelter");
        if (child != null) {
            final String value = child.getValue().toLowerCase();
            assert "true".equals(value) || "false".equals(value);

            ausruestung.setIstBehaelter(Boolean.parseBoolean(value));
        }

        // Haltbarkeit
        child = xmlElement.getFirstChildElement("haltbarkeit");
        if (child != null) {
            ausruestung.setHaltbarkeit(child.getValue());
        }
    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {

        super.map(charElement, xmlElement);

        // my mapping
        final Ausruestung ausruestung = (Ausruestung) charElement;
        xmlElement.setLocalName("ausruestung");

        // Behälter
        if (!ausruestung.istBehaelter()) {
            final Element e = new Element("istBehaelter");
            e.appendChild("false");
            xmlElement.appendChild(e);
        }

        // Haltbarkeit
        String haltbarkeit = ausruestung.getHaltbarkeit();
        if (haltbarkeit != null && haltbarkeit.trim().length() > 0) {
            final Element e = new Element("haltbarkeit");
            e.appendChild(haltbarkeit);
            xmlElement.appendChild(e);
        }
    }

}
