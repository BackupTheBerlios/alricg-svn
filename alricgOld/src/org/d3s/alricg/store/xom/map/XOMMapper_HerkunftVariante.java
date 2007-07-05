/*
 * Created on 18.10.2005 / 10:56:21
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom.map;

import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.Herkunft;
import org.d3s.alricg.charKomponenten.HerkunftVariante;
import org.d3s.alricg.charKomponenten.links.IdLinkList;

class XOMMapper_HerkunftVariante implements XOMMapper<HerkunftVariante> {

	private final XOMMapper<IdLinkList> idLinkListMapper = new XOMMapper_IdLinkList();
	public void map(Element from, HerkunftVariante to) {
        // my mapping

        // Auslesen der Elemente die von dem "original" entfernd werden sollen
        Element current = from.getFirstChildElement("entferneElement");
        if (current != null) {
            final IdLinkList ids = new IdLinkList((Herkunft) to);
            idLinkListMapper.map(current, ids);
            to.setEntferneElement(ids);
        }

        // Auslesen der XML-Tags die von dem "original" entfernd werden sollen
        current = from.getFirstChildElement("entferneXmlTag");
        if (current != null) {
            String[] strAr = current.getValue().split(",");
            for (int i = 0; i < strAr.length; i++) {
                strAr[i] = strAr[i].trim();
            }
            to.setEntferneXmlTag(strAr);
        }

        // Auslesen des Attribus "isMultibel"
        Attribute a = from.getAttribute("isMultibel");

        // Sicherstellen, das der Wert gültig ist
        assert a.getValue().equals("false") || a.getValue().equals("true");
        to.setMultibel(a.getValue().equals("true"));

        // Auslesen des Attribus "isAdditionsVariante"
        a = from.getAttribute("isAdditionsVariante");

        // Sicherstellen, das der Wert gültig ist
        assert a.getValue().equals("false") || a.getValue().equals("true");
        to.setAdditionsVariante(a.getValue().equals("true"));
    }


	public void map(HerkunftVariante from, Element to) {
        
		StringBuffer strBuffer = new StringBuffer();

        // my mapping
        to.setLocalName("variante");

        // Schreiben der Elemente, die aus dem "original" entfernd werden sollen
        IdLinkList ids = from.getEntferneElement();
        if (ids != null) {
            final Element e = new Element("entferneElement");
            idLinkListMapper.map(ids, e);
            to.appendChild(e);
        }

        // Schreiben der XML-Tags die entfernd werden sollen
        String[] stringAr = from.getEntferneXmlTag();
        if (stringAr != null) {
            for (int i = 0; i < stringAr.length - 1; i++) {
                strBuffer.append(stringAr[i]).append(",");
            }
            to.appendChild(strBuffer.toString());
        }

        // Schreiben ob Multibel, wenn nicht default
        if (from.isMultibel()) {
            to.addAttribute(new Attribute("isMultibel", "true"));
        }

        // Schreiben ob Multibel, wenn nicht default
        if (!from.isAdditionsVariante()) {
            to.addAttribute(new Attribute("isAdditionsVariante", "false"));
        }
	}

}
