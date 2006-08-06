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
import org.d3s.alricg.charKomponenten.Sprache;
import org.d3s.alricg.charKomponenten.links.IdLinkList;
import org.d3s.alricg.prozessor.utils.FormelSammlung;

/**
 * <code>XOMMapper</code> für eine <code>Sprache</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.Sprache
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
class XOMMapper_Sprache extends XOMMapper_SchriftSprache {

    /** <code>XOMMapper_Sprache</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMMapper_Sprache.class.getName());

    private final XOMMapper<IdLinkList> idLinkListMapper = new XOMMapper_IdLinkList();
    
    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {
        super.map(xmlElement, charElement);

        // my mapping
        final Sprache sprache = (Sprache) charElement;

        // Auslesen der Kostenklasse der Sprache
        Element current = xmlElement.getFirstChildElement("nichtMuttersprache");
        String kostenklasse = current.getAttributeValue("kostenKlasse");
        sprache.setKostenNichtMutterSpr(FormelSammlung.getKostenKlasseByValue(kostenklasse));

        // Auslesen der Kompläxität der Sprache
        try {
            sprache.setKomplexNichtMutterSpr(Integer.parseInt(current.getAttributeValue("komplexitaet")));
        } catch (NumberFormatException exc) {
            LOG.log(Level.SEVERE, "String -> int failed", exc);
        }

        // Auslesen der dazugehörigen Schrift(en)
        current = xmlElement.getFirstChildElement("schriften");
        if (current != null) {
            final IdLinkList zugehoerigeSchrift = new IdLinkList(sprache);
            idLinkListMapper.map(current, zugehoerigeSchrift);
            sprache.setZugehoerigeSchrift(zugehoerigeSchrift);
        }
    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        super.map(charElement, xmlElement);

        // my mapping
        final Sprache sprache = (Sprache) charElement;
        xmlElement.setLocalName("sprache");

        // Schreiben der Kostenklasse
        Element e = new Element("nichtMuttersprache");
        xmlElement.appendChild(e);
        String kosten = sprache.getKostenNichtMutterSpr().getValue();
        e.addAttribute(new Attribute("kostenKlasse", kosten));

        // Schreiben der Komplexität
        String komplexitaet = Integer.toString(sprache.getKomplexNichtMutterSpr());
        e.addAttribute(new Attribute("komplexitaet", komplexitaet));

        // Schreiben der dazugehörigen Schrift(en)
        final IdLinkList zugehoerigeSchrift = sprache.getZugehoerigeSchrift();
        if (zugehoerigeSchrift != null) {
            e = new Element("schriften");
            idLinkListMapper.map(zugehoerigeSchrift, e);
            xmlElement.appendChild(e);
        }

    }

}
