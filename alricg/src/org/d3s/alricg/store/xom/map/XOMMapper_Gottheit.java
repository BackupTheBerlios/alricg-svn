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
import org.d3s.alricg.charKomponenten.Gottheit;
import org.d3s.alricg.charKomponenten.Gottheit.GottheitArt;
import org.d3s.alricg.charKomponenten.Gottheit.KenntnisArt;

/**
 * <code>XOMMapper</code> für eine <code>Gottheit</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.Gottheit
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
class XOMMapper_Gottheit extends XOMMapper_CharElement {

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {
        super.map(xmlElement, charElement);

        // my mapping
        final Gottheit gottheit = (Gottheit) charElement;

        // Auslesen der Art der Kenntnis
        // Prüfen des Wertebereiches
        String currentValue = xmlElement.getFirstChildElement("kenntnisArt").getValue();
        assert currentValue.equalsIgnoreCase(KenntnisArt.liturgie.getValue())
                || currentValue.equalsIgnoreCase(KenntnisArt.ritual.getValue());

        if (currentValue.equalsIgnoreCase(KenntnisArt.liturgie.getValue())) {
            gottheit.setKenntnisArt(KenntnisArt.liturgie);
        } else {
            gottheit.setKenntnisArt(KenntnisArt.ritual);
        }

        // Auslesen der Art des Gottes/Ritus
        // Prüfen des Wertebereiches
        currentValue = xmlElement.getFirstChildElement("gottheitArt").getValue();
        assert currentValue.equalsIgnoreCase(GottheitArt.zwoelfGoettlich.getValue())
                || currentValue.equalsIgnoreCase(GottheitArt.nichtAlveranisch.getValue())
                || currentValue.equalsIgnoreCase(GottheitArt.animistisch.getValue());

        if (currentValue.equalsIgnoreCase(GottheitArt.zwoelfGoettlich.getValue())) {
            gottheit.setGottheitArt(GottheitArt.zwoelfGoettlich);
        } else if (currentValue.equalsIgnoreCase(GottheitArt.nichtAlveranisch.getValue())) {
            gottheit.setGottheitArt(GottheitArt.nichtAlveranisch);
        } else {
            gottheit.setGottheitArt(GottheitArt.animistisch);
        }
    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        super.map(charElement, xmlElement);

        // my mapping
        final Gottheit gottheit = (Gottheit) charElement;
        xmlElement.setLocalName("gottheit");

        // Schreiben der KenntnisArt
        Element e = new Element("kenntnisArt");
        e.appendChild(gottheit.getKenntnisArt().getValue());
        xmlElement.appendChild(e);

        // Schreiben der Gottheit
        e = new Element("gottheitArt");
        e.appendChild(gottheit.getGottheitArt().getValue());
        xmlElement.appendChild(e);
    }

}
