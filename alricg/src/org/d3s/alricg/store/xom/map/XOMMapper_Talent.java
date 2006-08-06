/*
 * Created on 23.06.2005 / 15:16:17
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store.xom.map;

import java.util.logging.Logger;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.Talent.Art;
import org.d3s.alricg.charKomponenten.Talent.Sorte;

/**
 * <code>XOMMapper</code> für ein <code>Talent</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.Talent
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
class XOMMapper_Talent extends XOMMapper_Faehigkeit {

    /** <code>XOMMapper_Talent</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMMapper_Talent.class.getName());

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {
        super.map(xmlElement, charElement);

        // my mapping
        final Talent talent = (Talent) charElement;

        // Auslesen der Art
        Element current = xmlElement.getFirstChildElement("einordnung");
        String attValue = current.getAttributeValue("art");
        if (attValue.equals(Art.basis.getValue())) {
            talent.setArt(Art.basis);
        } else if (attValue.equals(Art.spezial.getValue())) {
            talent.setArt(Art.spezial);
        } else if (attValue.equals(Art.beruf.getValue())) {
            talent.setArt(Art.beruf);
        } else {
            LOG.severe("Richtige Art nicht gefunden!");
        }

        // Auslesen der Sorte
        attValue = current.getAttributeValue("sorte");
        if (attValue.equals(Sorte.kampf.getValue())) {
            talent.setSorte(Sorte.kampf);
        } else if (attValue.equals(Sorte.koerper.getValue())) {
            talent.setSorte(Sorte.koerper);
        } else if (attValue.equals(Sorte.gesellschaft.getValue())) {
            talent.setSorte(Sorte.gesellschaft);
        } else if (attValue.equals(Sorte.natur.getValue())) {
            talent.setSorte(Sorte.natur);
        } else if (attValue.equals(Sorte.wissen.getValue())) {
            talent.setSorte(Sorte.wissen);
        } else if (attValue.equals(Sorte.handwerk.getValue())) {
            talent.setSorte(Sorte.handwerk);
        } else {
            LOG.severe("Richtige Sorte nicht gefunden!");
        }

        // Einlesen der spezialisierung
        current = xmlElement.getFirstChildElement("spezialisierungen");
        if (current != null) {
            final Elements children = current.getChildElements("name");
            final String[] spezialisierungen = new String[children.size()];
            for (int i = 0; i < spezialisierungen.length; i++) {
                spezialisierungen[i] = children.get(i).getValue();
            }
            talent.setSpezialisierungen(spezialisierungen);
        }

    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        super.map(charElement, xmlElement);

        // my mapping
        final Talent talent = (Talent) charElement;
        xmlElement.setLocalName("talent");

        // Schreiben der Art & Sorte des Talents
        Element e = new Element("einordnung");
        e.addAttribute(new Attribute("art", talent.getArt().getValue()));
        e.addAttribute(new Attribute("sorte", talent.getSorte().getValue()));
        xmlElement.appendChild(e);

        // Schreiben der Spezialisierungen
        String[] spezialisierungen = talent.getSpezialisierungen();
        if (spezialisierungen != null) {
            e = new Element("spezialisierungen");
            xmlElement.appendChild(e);
            for (int i = 0; i < spezialisierungen.length; i++) {
                e = new Element("name");
                e.appendChild(spezialisierungen[i]);
                xmlElement.getFirstChildElement("spezialisierungen").appendChild(e);
            }
        }
    }
}
