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

import nu.xom.Element;
import nu.xom.Elements;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Gottheit;
import org.d3s.alricg.charKomponenten.Ritus;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.store.FactoryFinder;

/**
 * Abstrakter <code>XOMMapper</code> für einen <code>Ritus</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.Ritus
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
abstract class XOMMapper_Ritus extends XOMMapper_CharElement {

    /** <code>XOMMapper_Ritus</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMMapper_Ritus.class.getName());

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {
        super.map(xmlElement, charElement);

        // my mapping
        final Ritus ritus = (Ritus) charElement;

        // Auslesen des Grades der Liturgie/ Ritual
        try {
            ritus.setGrad(Integer.parseInt(xmlElement.getFirstChildElement("grad").getValue()));
        } catch (NumberFormatException exc) {
            LOG.log(Level.SEVERE, "String -> int failed", exc);
        }

        // Auslesen der AdditionsId
        ritus.setAdditionsId(xmlElement.getFirstChildElement("additionsId").getValue());

        // Auslesen der Gottheiten
        final Elements children = xmlElement.getChildElements("gottheit");
        final Gottheit[] gottheit = new Gottheit[children.size()];
        for (int i = 0; i < children.size(); i++) {
            final String val = children.get(i).getValue();
            gottheit[i] = (Gottheit) FactoryFinder.find().getData().getCharElement(val, CharKomponente.gottheit);
        }
        ritus.setGottheit(gottheit);
    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        super.map(charElement, xmlElement);

        // my mapping
        final Ritus ritus = (Ritus) charElement;

        // Schreiben des Grades der Liturgie/ Ritual
        Element e = new Element("grad");
        e.appendChild(Integer.toString(ritus.getGrad()));
        xmlElement.appendChild(e);

        // Schreiben der AdditionsID
        e = new Element("additionsId");
        e.appendChild(ritus.getAdditionsId());
        xmlElement.appendChild(e);

        // Schreiben der gottheit
        final Gottheit[] gottheit = ritus.getGottheit();
        for (int i = 0; i < gottheit.length; i++) {
            e = new Element("gottheit");
            e.appendChild(gottheit[i].getId());
            xmlElement.appendChild(e);
        }
    }
}
