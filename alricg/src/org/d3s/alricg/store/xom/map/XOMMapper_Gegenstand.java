/*
 * Created on 23.06.2005 / 15:16:17
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store.xom.map;

import java.util.logging.Level;
import java.util.logging.Logger;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.RegionVolk;
import org.d3s.alricg.charKomponenten.charZusatz.Gegenstand;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.store.FactoryFinder;

/**
 * <code>XOMMapper</code> für einen <code>Gegenstand</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.charZusatz.Gegenstand
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */

abstract class XOMMapper_Gegenstand extends XOMMapper_CharElement {

    /** <code>XOMMapper_Gegenstand</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMMapper_Gegenstand.class.getName());

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {

        // super mapping
        super.map(xmlElement, charElement);

        // my mapping
        final Gegenstand gegenstand = (Gegenstand) charElement;
        Element child = null;
        try {
            // Gewicht (minOcc=0; maxOcc=1)
            child = xmlElement.getFirstChildElement("gewicht");
            if (child != null) {
                gegenstand.setGewicht(Integer.parseInt(child.getValue()));
            }

            // Wert (minOcc=0; maxOcc=1)
            child = xmlElement.getFirstChildElement("wert");
            if (child != null) {
                gegenstand.setWert(Integer.parseInt(child.getValue()));
            }
        } catch (NumberFormatException e) {
            LOG.log(Level.SEVERE, "String -> int failed " + child.getValue(), e);
        }

        // Erhältlich bei (minOcc=1; maxOcc=unbounded)
        final Elements children = xmlElement.getChildElements("erhaeltlichBei");
        final RegionVolk[] erhaeltlichBei = new RegionVolk[children.size()];
        for (int i = 0; i < children.size(); i++) {
            erhaeltlichBei[i] = (RegionVolk) FactoryFinder.find().getData().getCharElement(children.get(i).getValue(),
                    CharKomponente.region);
        }
        gegenstand.setErhältlichBei(erhaeltlichBei);

        // Auslesen der Einordnung des Gegenstandes
        Attribute attribute = xmlElement.getAttribute("einordnung");
        if (attribute != null) {
            gegenstand.setEinordnung(attribute.getValue());
        }
    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {

        // super mapping
        super.map(charElement, xmlElement);

        // my mapping
        final Gegenstand gegenstand = (Gegenstand) charElement;

        // Gewicht
        int intValue = gegenstand.getGewicht();
        if (intValue != CharElement.KEIN_WERT) {
            final Element e = new Element("gewicht");
            e.appendChild(Integer.toString(intValue));
            xmlElement.appendChild(e);
        }

        // Gegenstand
        intValue = gegenstand.getWert();
        if (intValue != CharElement.KEIN_WERT) {
            final Element e = new Element("wert");
            e.appendChild(Integer.toString(intValue));
            xmlElement.appendChild(e);
        }

        // ErhältlichBei
        final RegionVolk[] erhaeltlichBei = gegenstand.getErhältlichBei();
        for (int i = 0; i < erhaeltlichBei.length; i++) {
            final Element e = new Element("erhaeltlichBei");
            e.appendChild(erhaeltlichBei[i].getId());
            xmlElement.appendChild(e);
        }

        // Einordnung
        String stringValue = gegenstand.getEinordnung();
        if (stringValue != null && stringValue.trim().length() > 0) {
            final Attribute a = new Attribute("einordnung", stringValue);
            xmlElement.addAttribute(a);
        }
    }
}
