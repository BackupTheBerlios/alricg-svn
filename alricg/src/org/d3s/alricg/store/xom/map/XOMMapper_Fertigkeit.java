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
import nu.xom.Elements;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Fertigkeit;
import org.d3s.alricg.charKomponenten.Werte;
import org.d3s.alricg.charKomponenten.Werte.CharArten;

/**
 * Abstrakter <code>XOMMapper</code> für eine <code>Fertigkeit</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.Fertigkeit
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
abstract class XOMMapper_Fertigkeit extends XOMMapper_CharElement {

    /** <code>XOMMapper_Fertigkeit</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMMapper_Fertigkeit.class.getName());

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {
        super.map(xmlElement, charElement);

        // my mapping
        final Fertigkeit fertigkeit = (Fertigkeit) charElement;

        // Auslesen der Additions-Werte
        Element current = xmlElement.getFirstChildElement("addition");
        if (current != null) {
            fertigkeit.setAdditionsID(current.getAttributeValue("id"));
            try {
                fertigkeit.setAdditionsWert(Integer.parseInt(current.getAttributeValue("wertigkeit")));
            } catch (NumberFormatException exc) {
                LOG.log(Level.SEVERE, "String -> int failed", exc);
            }
        }

        // Auslesen, ob diese Fertigkeit noch Text benötigt
        current = xmlElement.getFirstChildElement("hatText");
        if (current != null) {

            // Sicherstellen des Wertebereiches
            assert current.getValue().equalsIgnoreCase("true") || current.getValue().equalsIgnoreCase("false");

            fertigkeit.setHasText(Boolean.parseBoolean(current.getValue()));
        }

        // Auslesen, ob diese Fertigkeit noch die Angabe eines Elements benötigt
        current = xmlElement.getFirstChildElement("hatElement");
        if (current != null) {

            // Sicherstellen des Wertebereiches
            assert current.getValue().equalsIgnoreCase("true") || current.getValue().equalsIgnoreCase("false");
            fertigkeit.setElementAngabe(Boolean.parseBoolean(current.getValue()));
        }

        // Einlesen der vorgeschlagenen Texte
        current = xmlElement.getFirstChildElement("textVorschlaege");
        if (current != null) {
            final Elements children = current.getChildElements("text");
            final String[] textVorschlaege = new String[children.size()];
            for (int i = 0; i < textVorschlaege.length; i++) {
                textVorschlaege[i] = children.get(i).getValue();
            }
            fertigkeit.setTextVorschlaege(textVorschlaege);
        }

        // Auslesen, ob normal Wählbar oder nur über Herkunft o.ä. zu bekommen
        current = xmlElement.getFirstChildElement("istWaehlbar");
        if (current != null) {
            assert current.getValue().equalsIgnoreCase("true") || current.getValue().equalsIgnoreCase("false");

            fertigkeit.setWaehlbar(Boolean.parseBoolean(current.getValue()));
        }

        // Auslesen für welche CharArten die Fertigkeit ist
        final Elements children = xmlElement.getChildElements("fuerWelcheChars");
        Werte.CharArten[] fuerWelcheChars = new Werte.CharArten[children.size()];
        for (int i = 0; i < children.size(); i++) {
            fuerWelcheChars[i] = Werte.getCharArtenByValue(children.get(i).getValue());
        }
        fertigkeit.setFuerWelcheChars(fuerWelcheChars);

        // Auslesen der GP Kosten
        try {
            fertigkeit.setGpKosten(Integer.parseInt(xmlElement.getAttributeValue("gp")));
        } catch (NumberFormatException exc) {
            LOG.log(Level.SEVERE, "String -> int failed", exc);
        }
    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        super.map(charElement, xmlElement);

        // my mapping
        final Fertigkeit fertigkeit = (Fertigkeit) charElement;

        // Schreiben der additions-Werte
        final int additionsWert = fertigkeit.getAdditionsWert();
        final String additionsID = fertigkeit.getAdditionsID();
        if (additionsWert != CharElement.KEIN_WERT) {
            final Element e = new Element("addition");
            e.addAttribute(new Attribute("id", additionsID));
            e.addAttribute(new Attribute("wertigkeit", Integer.toString(additionsWert)));
            xmlElement.appendChild(e);
        }

        // Schreiben, ob die Fertigkeit einen Text benötigt
        {
            final Element e = new Element("hatText");
            e.appendChild(fertigkeit.hasText() ? "true" : "false");
            xmlElement.appendChild(e);
        }

        // Schreiben, ob die Fertigkeit die Angabe eines Elements benötigt
        {
            final Element e = new Element("hatElement");
            e.appendChild(fertigkeit.isElementAngabe() ? "true" : "false");
            xmlElement.appendChild(e);
        }

        final String[] textVorschlaege = fertigkeit.getTextVorschlaege();
        if (textVorschlaege != null) {
            final Element e = new Element("textVorschlaege");
            xmlElement.appendChild(e);
            for (int i = 0; i < textVorschlaege.length; i++) {
                final Element ee = new Element("text");
                ee.appendChild(textVorschlaege[i]);
                e.appendChild(ee);
            }
        }

        // Schreiben, ob das Element normal wählbar ist
        {
            final Element e = new Element("istWaehlbar");
            e.appendChild(fertigkeit.isWaehlbar() ? "true" : "false");
            xmlElement.appendChild(e);
        }

        // Schreiben der zulässigen CharArten
        final CharArten[] fuerWelcheChars = fertigkeit.getFuerWelcheChars();
        for (int i = 0; i < fuerWelcheChars.length; i++) {
            final Element e = new Element("fuerWelcheChars");
            e.appendChild(fuerWelcheChars[i].getValue());
            xmlElement.appendChild(e);
        }

        // Schreiben der GP Kosten
        xmlElement.addAttribute(new Attribute("gp", Integer.toString(fertigkeit.getGpKosten())));
    }
}
