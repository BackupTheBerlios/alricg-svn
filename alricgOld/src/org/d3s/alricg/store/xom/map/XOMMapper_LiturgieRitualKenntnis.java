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
import org.d3s.alricg.charKomponenten.LiturgieRitualKenntnis;

/**
 * <code>XOMMapper</code> für eine <code>LiturgieRitualKenntnis</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper 
 * @see org.d3s.alricg.charKomponenten.LiturgieRitualKenntnis
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
class XOMMapper_LiturgieRitualKenntnis extends XOMMapper_Faehigkeit {

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {
        super.map(xmlElement, charElement);

        // my mapping
        final LiturgieRitualKenntnis lrk = (LiturgieRitualKenntnis) charElement;

        // Auslesen ob es zu einer "Göttlichen" oder "Schamanischen" Tradition gehört
        final Element current = xmlElement.getFirstChildElement("istLiturgieKenntnis");
        if (current != null) {
            // Wertebereich prüfen
            assert current.getValue().equalsIgnoreCase("true") || current.getValue().equalsIgnoreCase("false");
            lrk.setLiturgieKenntnis(Boolean.parseBoolean(current.getValue()));
        }
    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        super.map(charElement, xmlElement);

        // my mapping
        final LiturgieRitualKenntnis lrk = (LiturgieRitualKenntnis) charElement;
        xmlElement.setLocalName("liRiKenntnis");

        // Schreiben ob es zu einer "Göttlichen" oder "Schamanischen" Tradition gehört
        Element e = new Element("istLiturgieKenntnis");
        e.appendChild(Boolean.toString(lrk.isLiturgieKenntnis()));
        xmlElement.appendChild(e);
    }
}
