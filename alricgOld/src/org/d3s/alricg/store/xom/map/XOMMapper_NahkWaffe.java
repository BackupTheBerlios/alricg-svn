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
import org.d3s.alricg.charKomponenten.charZusatz.NahkWaffe;

/**
 * <code>XOMMapper</code> für eine <code>NahkWaffe</code> (Nahkampfwaffe).
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.charZusatz.NahkWaffe
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
class XOMMapper_NahkWaffe extends XOMMapper_Waffe {

    /** <code>XOMMapper_NahkWaffe</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMMapper_NahkWaffe.class.getName());

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {
        super.map(xmlElement, charElement);

        // my mapping
        NahkWaffe waffe = (NahkWaffe) charElement;
        try {
            Element e = xmlElement.getFirstChildElement("tp");

            // Auslesen der TP-Zusätze für NahKampfwaffen
            if (e != null) {

                if (e.getAttribute("kk-ab") != null) {
                    waffe.setKkAb(Integer.parseInt(e.getAttributeValue("kk-ab")));
                }
                if (e.getAttribute("kk-stufe") != null) {
                    waffe.setKkStufe(Integer.parseInt(e.getAttributeValue("kk-stufe")));
                }
            }

            // Auslesen der weiteren Eigenschaften
            e = xmlElement.getFirstChildElement("eigenschaften");

            // Auslesen des Bruchfaktors
            if (e != null && e.getAttribute("bf") != null) {
                waffe.setBf(Integer.parseInt(e.getAttributeValue("bf")));
            }

            // Auslesen der Distanzklasse
            if (e != null && e.getAttribute("dk") != null) {
                waffe.setDk(e.getAttributeValue("dk"));
            }

            // Auslesen der WaffenModis
            e = xmlElement.getFirstChildElement("wm");

            // Auslesen des Bruchfaktors
            if (e != null && e.getAttribute("at") != null) {
                waffe.setWmAT(Integer.parseInt(e.getAttributeValue("at")));
            }

            // Auslesen der Distanzklasse
            if (e != null && e.getAttribute("pa") != null) {
                waffe.setWmPA(Integer.parseInt(e.getAttributeValue("pa")));
            }
        } catch (NumberFormatException exc) {
            LOG.log(Level.SEVERE, "String -> int failed", exc);
        }

    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        super.map(charElement, xmlElement);

        // my mapping
        NahkWaffe waffe = (NahkWaffe) charElement;
        xmlElement.setLocalName("nkWaffe");

        Element e = xmlElement.getFirstChildElement("tp");

        // Schreiben der "Schwelle" für den KK -Zuschlag
        int kkAb = waffe.getKkAb();
        if (kkAb != CharElement.KEIN_WERT) {
            if (e == null) {
                e = new Element("tp");
                xmlElement.appendChild(e);
            }
            e.addAttribute(new Attribute("kk-ab", Integer.toString(kkAb)));
        }

        // Schreiben der Stufe für den KK-Zuschlag
        int kkStufe = waffe.getKkStufe();
        if (kkStufe != CharElement.KEIN_WERT) {
            if (e == null) {
                e = new Element("tp");
                xmlElement.appendChild(e);
            }
            e.addAttribute(new Attribute("kk-stufe", Integer.toString(kkStufe)));
        }

        e = xmlElement.getFirstChildElement("eigenschaften");

        // Schreiben des Bruchfaktors
        int bf = waffe.getBf();
        if (bf != CharElement.KEIN_WERT) {
            if (e == null) {
                e = new Element("eigenschaften");
                xmlElement.appendChild(e);
            }
            e.addAttribute(new Attribute("bf", Integer.toString(bf)));
        }

        // Schreiben der Distanzklasse
        String dk = waffe.getDk();
        if (dk != null && dk.trim().length() > 0) {
            if (e == null) {
                e = new Element("eigenschaften");
                xmlElement.appendChild(e);
            }
            e.addAttribute(new Attribute("dk", dk));
        }

        // Schreiben der Waffen-Modis
        int wmAT = waffe.getWmAT();
        int wmPA = waffe.getWmPA();
        if (wmAT != CharElement.KEIN_WERT || wmPA != CharElement.KEIN_WERT) {
            e = new Element("wm");
            if (wmAT != CharElement.KEIN_WERT) {
                e.addAttribute(new Attribute("at", Integer.toString(wmAT)));
            }
            if (wmPA != CharElement.KEIN_WERT) {
                e.addAttribute(new Attribute("pa", Integer.toString(wmPA)));
            }
            xmlElement.appendChild(e);
        }
    }
}