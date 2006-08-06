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

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.SchriftSprache;
import org.d3s.alricg.prozessor.utils.FormelSammlung;

/**
 * Abstrakter <code>XOMMapper</code> für eine <code>SchriftSprache</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.SchriftSprache
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
abstract class XOMMapper_SchriftSprache extends XOMMapper_CharElement {

    /** <code>XOMMapper_SchriftSprache</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMMapper_SchriftSprache.class.getName());

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {
        super.map(xmlElement, charElement);

        // my mapping
        final SchriftSprache schriftSprache = (SchriftSprache) charElement;

        // Auslesen der Kostenklasse der Schrift/ Sprache
        Element current = xmlElement.getFirstChildElement("daten");
        String attVal = current.getAttributeValue("kostenKlasse");
        schriftSprache.setKostenKlasse(FormelSammlung.getKostenKlasseByValue(attVal));

        // Auslesen der Kompläxität der Schrift/ Sprache
        try {
            schriftSprache.setKomplexitaet(Integer.parseInt(current.getAttributeValue("komplexitaet")));
        } catch (NumberFormatException exc) {
            LOG.log(Level.SEVERE, "String -> int failed", exc);
        }
    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        super.map(charElement, xmlElement);

        // my mapping
        final SchriftSprache schriftSprache = (SchriftSprache) charElement;

        // Schreiben der Kostenklasse
        final Element e = new Element("daten");
        xmlElement.appendChild(e);
        e.addAttribute(new Attribute("kostenKlasse", schriftSprache.getKostenKlasse().getValue()));

        // Schreiben der Komplexität
        e.addAttribute(new Attribute("komplexitaet", Integer.toString(schriftSprache.getKomplexitaet())));
    }

}
