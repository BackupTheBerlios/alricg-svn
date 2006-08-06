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
import org.d3s.alricg.charKomponenten.charZusatz.SchwarzeGabe;

/**
 * <code>XOMMapper</code> für eine <code>SchwarzeGabe</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.charZusatz.SchwarzeGabe
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
class XOMMapper_SchwarzeGabe extends XOMMapper_CharElement {

    /** <code>XOMMapper_SchwarzeGabe</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMMapper_SchwarzeGabe.class.getName());

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {
        super.map(xmlElement, charElement);

        // my mapping
        final SchwarzeGabe schwarzeGabe = (SchwarzeGabe) charElement;

        try {
            // Stufengrenzen auslesen
            Element current = xmlElement.getFirstChildElement("stufenGrenzen");
            if (current != null) {
                schwarzeGabe.setMinStufe(Integer.parseInt(current.getAttributeValue("minStufe")));
                schwarzeGabe.setMaxStufe(Integer.parseInt(current.getAttributeValue("maxStufe")));
            }
            schwarzeGabe.setKosten(Integer.parseInt(xmlElement.getAttributeValue("kosten")));
        } catch (NumberFormatException exc) {
            LOG.log(Level.SEVERE, "String -> int failed", exc);
        }
    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        super.map(charElement, xmlElement);

        // my mapping
        final SchwarzeGabe schwarzeGabe = (SchwarzeGabe) charElement;
        xmlElement.setLocalName("gabe");

        // Schreiben der Stufengrenzen
        final int minStufe = schwarzeGabe.getMinStufe();
        final int maxStufe = schwarzeGabe.getMaxStufe();
        if (minStufe != CharElement.KEIN_WERT || maxStufe != CharElement.KEIN_WERT) {
            final Element e = new Element("stufenGrenzen");
            if (minStufe != CharElement.KEIN_WERT) {
                e.addAttribute(new Attribute("minStufe", Integer.toString(minStufe)));
            }
            if (maxStufe != CharElement.KEIN_WERT) {
                e.addAttribute(new Attribute("maxStufe", Integer.toString(maxStufe)));
            }
            xmlElement.appendChild(e);
        }

        // Schreiben der Kosten
        xmlElement.addAttribute(new Attribute("kosten", Integer.toString(schwarzeGabe.getKosten())));

    }

}
