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
import nu.xom.Elements;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.RegionVolk;

/**
 * <code>XOMMapper</code> für eine <code>RegionVolk</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.RegionVolk
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
class XOMMapper_RegionVolk extends XOMMapper_CharElement {

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {
        super.map(xmlElement, charElement);

        // my mapping
        final RegionVolk regionVolk = (RegionVolk) charElement;

        // Auslesen des Bindewortes
        Element current = xmlElement.getFirstChildElement("bindeWort");
        if (current != null) {
            regionVolk.setBindeWortMann(current.getAttributeValue("mann"));
            regionVolk.setBindeWortFrau(current.getAttributeValue("frau"));
        }

        // Auslesen der männlichen Vornamen
        current = xmlElement.getFirstChildElement("vornamenMann");
        if (current != null) {
            final Elements children = current.getChildElements("name");
            final String[] vornamenMann = new String[children.size()];
            for (int i = 0; i < vornamenMann.length; i++) {
                vornamenMann[i] = children.get(i).getValue();
            }
            regionVolk.setVornamenMann(vornamenMann);
        }

        // Auslesen der weiblichen Vornamen
        current = xmlElement.getFirstChildElement("vornamenFrau");
        if (current != null) {
            final Elements children = xmlElement.getFirstChildElement("vornamenFrau").getChildElements("name");
            final String[] vornamenFrau = new String[children.size()];
            for (int i = 0; i < vornamenFrau.length; i++) {
                vornamenFrau[i] = children.get(i).getValue();
            }
            regionVolk.setVornamenFrau(vornamenFrau);
        }

        // Auslesen der Nachnamen
        current = xmlElement.getFirstChildElement("nachnamen");
        if (current != null) {
            final Elements children = current.getChildElements("name");
            final String[] nachnamen = new String[children.size()];
            for (int i = 0; i < nachnamen.length; i++) {
                nachnamen[i] = children.get(i).getValue();
            }
            regionVolk.setNachnamen(nachnamen);
        }

        // Auslesen der Nachnamen
        current = xmlElement.getFirstChildElement("nachnamenEndung");
        if (current != null) {
            final Elements children = current.getChildElements("endung");
            final String[] nachnamenEndung = new String[children.size()];
            for (int i = 0; i < nachnamenEndung.length; i++) {
                nachnamenEndung[i] = children.get(i).getValue();
            }
            regionVolk.setNachnamenEndung(nachnamenEndung);
        }
    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        super.map(charElement, xmlElement);

        // my mapping
        final RegionVolk regionVolk = (RegionVolk) charElement;
        xmlElement.setLocalName("region");

        // Schreiben der Bindeworte
        final String bindeWortMann = regionVolk.getBindeWortMann();
        final String bindeWortFrau = regionVolk.getBindeWortFrau();
        if (bindeWortMann != null) {
            final Element e = new Element("bindeWort");
            e.addAttribute(new Attribute("mann", bindeWortMann));
            e.addAttribute(new Attribute("frau", bindeWortFrau));
            xmlElement.appendChild(e);
        }

        // Schreiben der männlichen Vornamen
        final String[] vornamenMann = regionVolk.getVornamenMann();
        if (vornamenMann != null) {
            final Element e = new Element("vornamenMann");
            for (int i = 0; i < vornamenMann.length; i++) {
                final Element ee = new Element("name");
                ee.appendChild(vornamenMann[i].trim());
                e.appendChild(ee);
            }
            xmlElement.appendChild(e);
        }

        // Schreiben der weiblicher Vornamen
        final String[] vornamenFrau = regionVolk.getVornamenFrau();
        if (vornamenFrau != null) {
            final Element e = new Element("vornamenFrau");
            for (int i = 0; i < vornamenFrau.length; i++) {
                final Element ee = new Element("name");
                ee.appendChild(vornamenFrau[i].trim());
                e.appendChild(ee);
            }
            xmlElement.appendChild(e);
        }

        // Schreiben der Nachnamen
        final String[] nachnamen = regionVolk.getNachnamen();
        if (nachnamen != null) {
            final Element e = new Element("nachnamen");
            for (int i = 0; i < nachnamen.length; i++) {
                final Element ee = new Element("name");
                ee.appendChild(nachnamen[i].trim());
                e.appendChild(ee);
            }
            xmlElement.appendChild(e);
        }

        // Schreiben der Endungen des Nachnamens
        final String[] nachnamenEndung = regionVolk.getNachnamenEndung();
        if (nachnamenEndung != null) {
            final Element e = new Element("nachnamenEndung");
            for (int i = 0; i < nachnamenEndung.length; i++) {
                final Element ee = new Element("endung");
                ee.appendChild(nachnamenEndung[i].trim());
                e.appendChild(ee);
            }
            xmlElement.appendChild(e);
        }
    }

}
