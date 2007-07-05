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
import org.d3s.alricg.charKomponenten.ZusatzProfession;
import org.d3s.alricg.charKomponenten.links.IdLinkList;

/**
 * <code>XOMMapper</code> für eine <code>ZusatzProfession</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.ZusatzProfession
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
class XOMMapper_ZusatzProfession extends XOMMapper_Profession {

    /** <code>XOMMapper_ZusatzProfession</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMMapper_ZusatzProfession.class.getName());
    
    private final XOMMapper<IdLinkList> idLinkListMapper = new XOMMapper_IdLinkList();

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {
        super.map(xmlElement, charElement);

        // my mapping
        final ZusatzProfession zusatzProfession = (ZusatzProfession) charElement;
        
        // Auslesen der üblichen Professionen
        Element current = xmlElement.getFirstChildElement("professionUeblich");
        if (current != null) {
            final IdLinkList professionUeblich = new IdLinkList(zusatzProfession);
            idLinkListMapper.map(current, professionUeblich);
            zusatzProfession.setProfessionUeblich(professionUeblich);
        }

        // Auslesen der möglichen Professionen
        current = xmlElement.getFirstChildElement("professionMoeglich");
        if (current != null) {
            final IdLinkList professionMoeglich = new IdLinkList(zusatzProfession);
            idLinkListMapper.map(current, professionMoeglich);
            zusatzProfession.setProfessionMoeglich(professionMoeglich);
        }

        // Auslesen der AP-Kosten
        if (xmlElement.getAttribute("apKosten") != null) {
            try {
                zusatzProfession.setApKosten(Integer.parseInt(xmlElement.getAttributeValue("apKosten")));
            } catch (NumberFormatException ex) {
                LOG.log(Level.SEVERE, "String -> int failed", ex);
            }
        } else {
            zusatzProfession.setApKosten(CharElement.KEIN_WERT);
        }

        // Auslesen der Art der Zusatzprofession

        // Sicherstellen des Wertebereichs
        String v = xmlElement.getAttributeValue("zusatzArt");
        assert v.equalsIgnoreCase("spaeteProf") || v.equalsIgnoreCase("zusatzProf");
        zusatzProfession.setZusatzProf(!v.equalsIgnoreCase("spaeteProf"));
    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        super.map(charElement, xmlElement);

        // my mapping
        final ZusatzProfession zusatzProfession = (ZusatzProfession) charElement;
        xmlElement.setLocalName("zusatzProfession");

        // Schreiben der üblichen Professionen
        IdLinkList ids = zusatzProfession.getProfessionUeblich();
        if (ids != null) {
            Element e = new Element("professionUeblich");
            idLinkListMapper.map(ids, e);
            xmlElement.appendChild(e);
        }

        // Schreiben der möglichen Professionen
        ids = zusatzProfession.getProfessionMoeglich();
        if (ids != null) {
            Element e = new Element("professionMoeglich");
            idLinkListMapper.map(ids, e);
            xmlElement.appendChild(e);
        }

        // Schreiben der AP-Kosten
        final int apKosten = zusatzProfession.getApKosten();
        if (apKosten != CharElement.KEIN_WERT) {
            xmlElement.addAttribute(new Attribute("apKosten", Integer.toString(apKosten)));
        }

        // Die Art der Zusatzprofession schreiben
        if (zusatzProfession.isZusatzProf()) {
            xmlElement.addAttribute(new Attribute("zusatzArt", "zusatzProf"));
        } else {
            xmlElement.addAttribute(new Attribute("zusatzArt", "spaeteProf"));
        }

    }

}
