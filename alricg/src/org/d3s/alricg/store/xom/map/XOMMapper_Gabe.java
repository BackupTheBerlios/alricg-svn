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
import org.d3s.alricg.charKomponenten.Gabe;

/**
 * <code>XOMMapper</code> für eine <code>Gabe</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.Gabe
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
class XOMMapper_Gabe extends XOMMapper_Faehigkeit {

    /** <code>XOMMapper_Gabe</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMMapper_Gabe.class.getName());

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {
        super.map(xmlElement, charElement);

        // My mapping
        final Gabe gabe = (Gabe) charElement;

        // Auslesen der Stufengrenzen
        Element current = xmlElement.getFirstChildElement("stufenGrenzen");
        try {
            if (current != null) {
                Attribute a = current.getAttribute("minStufe");
                if (a != null) {
                    gabe.setMinStufe(Integer.parseInt(a.getValue()));
                }
                a = current.getAttribute("maxStufe");
                if (a != null) {
                    gabe.setMaxStufe(Integer.parseInt(a.getValue()));
                }
            }
        } catch (NumberFormatException exc) {
            LOG.log(Level.SEVERE, "String -> int failed", exc);
        }

    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        super.map(charElement, xmlElement);

        // my mapping
        final Gabe gabe = (Gabe) charElement;
        xmlElement.setLocalName("gabe");

        // schreiben der Stufengrenzen
        final int minStufe = gabe.getMinStufe();
        final int maxStufe = gabe.getMaxStufe();
        if (minStufe != 1 || maxStufe != 1) {
            final Element e = new Element("stufenGrenzen");

            if (minStufe != 1) {
                e.addAttribute(new Attribute("minStufe", Integer.toString(minStufe)));
            }
            if (maxStufe != 1) {
                e.addAttribute(new Attribute("maxStufe", Integer.toString(maxStufe)));
            }
            xmlElement.appendChild(e);
        }
    }
}
