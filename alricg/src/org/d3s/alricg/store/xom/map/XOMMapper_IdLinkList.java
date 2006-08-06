/*
 * Created on 18.10.2005 / 09:38:24
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom.map;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.IdLinkList;

class XOMMapper_IdLinkList implements XOMMapper<IdLinkList> {

	private final XOMMapper<IdLink> idLinkMapper = new XOMMapper_IdLink();
	
	public void map(Element from, IdLinkList to) {
		final List<IdLink> links = new ArrayList<IdLink>();

        // Auslesen der Tags
        Attribute a = from.getAttribute("ids");
        if (a != null) {
            String[] ids = a.getValue().split(" ");

            // Einlesen der IdLinks aus dem Attribut
            for (int i = 0; i < ids.length; i++) {
                final IdLink link = new IdLink(to.getQuelle(), null);
                link.loadFromId(ids[i]);
                links.add(link);
            }

        }

        // Einlesen der IdLinks mit weiteren Eigenschaften
        final Elements children = from.getChildElements("linkId");
        for (int i = 0; i < children.size(); i++) {
            final IdLink link = new IdLink(to.getQuelle(), null);
            idLinkMapper.map(children.get(i), link);
            links.add(link);
        }
        to.setLinks(links.toArray(new IdLink[0]));

	}

	public void map(IdLinkList from, Element to) {
		if (from == null)
            return;

        final StringBuffer buffy = new StringBuffer();

        // Schreiben des Attributes
        IdLink[] links = from.getLinks();
        if (links == null)
            return;

        for (int i = 0; i < links.length; i++) {

            if (links[i].getZweitZiel() != null || links[i].getText() != null || links[i].getWert() != IdLink.KEIN_WERT
                    || links[i].isLeitwert() != false) {

                // Schreiben in Option, zusätzliche Angaben nötig

                final Element e = new Element("linkId");
                idLinkMapper.map(links[i], e);
                to.appendChild(e);

            } else { // Schreiben in Attribut, nur Id nötig
                buffy.append(links[i].getZiel().getId());
                buffy.append(" ");
            }
        }

        // Attribut hinzufügen, falls mindestens ein element vorhanden
        if (buffy.length() > 0) {
            final Attribute a = new Attribute("ids", buffy.toString().trim());
            to.addAttribute(a);
        }

	}

}
