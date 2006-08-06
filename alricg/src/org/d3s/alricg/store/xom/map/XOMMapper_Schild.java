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
import org.d3s.alricg.charKomponenten.charZusatz.Schild;

/**
 * <code>XOMMapper</code> für ein <code>Schild</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.charZusatz.Schild
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
class XOMMapper_Schild extends XOMMapper_Gegenstand {

    /** <code>XOMMapper_Schild</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMMapper_Schild.class.getName());

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {
        super.map(xmlElement, charElement);

        // my mapping
        final Schild schild = (Schild) charElement;

        // Schild-Typ
        Element child = xmlElement.getFirstChildElement("typ");
        if (child != null) {
            schild.setTyp(child.getValue());
        }
        try {
            // Eigenschaften
            child = xmlElement.getFirstChildElement("eigenschaften");
            if (child != null) {

                // bf
                Attribute attr = child.getAttribute("bf");
                if (attr != null) {
                    schild.setBf(Integer.parseInt(attr.getValue()));
                }

                // ini
                attr = child.getAttribute("ini");
                if (attr != null) {
                    schild.setIni(Integer.parseInt(attr.getValue()));
                }
            }

            // WM
            child = xmlElement.getFirstChildElement("wm");
            if (child != null) {

                // at
                Attribute attr = child.getAttribute("at");
                if (attr != null) {
                    schild.setWmAT(Integer.parseInt(attr.getValue()));
                }

                // pa
                attr = child.getAttribute("pa");
                if (attr != null) {
                    schild.setWmPA(Integer.parseInt(attr.getValue()));
                }
            }
        } catch (NumberFormatException exc) {
            LOG.log(Level.SEVERE, "string -> int failed ", exc);
        }
    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        super.map(charElement, xmlElement);

        // my mapping
        final Schild schild = (Schild) charElement;
        xmlElement.setLocalName("schild");

        // typ des Schildes Schreiben
        String typ = schild.getTyp();
        if (typ != null && typ.trim().length() > 0) {
            final Element e = new Element("typ");
            e.appendChild(typ.trim());
            xmlElement.appendChild(e);
        }

        // Eigenschaften
        final int bf = schild.getBf();
        final int ini = schild.getIni();
        if (bf != CharElement.KEIN_WERT || ini != CharElement.KEIN_WERT) {
            final Element e = new Element("eigenschaften");

            // bf
            if (bf != CharElement.KEIN_WERT) {
                e.addAttribute(new Attribute("bf", Integer.toString(bf)));
            }

            // ini
            if (ini != CharElement.KEIN_WERT) {
                e.addAttribute(new Attribute("ini", Integer.toString(ini)));
            }
            xmlElement.appendChild(e);
        }

        // Waffen Modis
        final int wmAT = schild.getWmAT();
        final int wmPA = schild.getWmPA();
        if (wmAT != CharElement.KEIN_WERT || wmPA != CharElement.KEIN_WERT) {
            final Element e = new Element("wm");

            // at
            if (wmAT != CharElement.KEIN_WERT) {
                e.addAttribute(new Attribute("at", Integer.toString(wmAT)));
            }

            // pa
            if (wmPA != CharElement.KEIN_WERT) {
                e.addAttribute(new Attribute("pa", Integer.toString(wmPA)));
            }
            xmlElement.appendChild(e);
        }
    }
}
