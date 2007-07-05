/*
 * Created 22. September 2005 / 00:01:02
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.store.xom.map;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.RegelAnmerkung;
import org.d3s.alricg.charKomponenten.RegelAnmerkung.Modus;
import org.d3s.alricg.charKomponenten.links.Voraussetzung;
import org.d3s.alricg.charKomponenten.sonderregeln.principle.Sonderregel;
import org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter;

/**
 * Abstrakter <code>XOMMapper</code> für ein <code>CharElement</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.CharElement
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
abstract class XOMMapper_CharElement implements XOMMapper<CharElement> {

    private final XOMMapper<Voraussetzung> vorausMapper = new XOMMapper_Voraussetzung();
	
    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {

        // Auslesen des Namen (minOcc=1; maxOcc=1)
        charElement.setName(xmlElement.getAttributeValue("name"));

        // Sonderregel (minOcc=0; maxOcc=1)
        Element child = xmlElement.getFirstChildElement("sonderregel");
        if (child != null) {

            // TODO Laden von Sonderregeln einbauen! Am besten mit dynamischen laden der Klasse
            /*
             * this.sonderRegel = new Sonderregel(
             * xmlElement.getFirstChildElement("sonderregel").getAttributeValue("id"),
             * xmlElement.getFirstChildElement("sonderregel").getValue().trim() );
             */

        }

        // Anzeigen - Boolean + Text (minOcc=0; maxOcc=1)
        child = xmlElement.getFirstChildElement("anzeigen");
        if (child != null) {

            final String value = child.getAttributeValue("anzeigen").toLowerCase();

            // Prüfung, ob der text auch nur "true" und "false" enthält
            assert "true".equals(value) || "false".equals(value);

            charElement.setAnzeigen(Boolean.parseBoolean(value));
            if (!charElement.isAnzeigen()) {
                charElement.setAnzeigenText(child.getValue().trim());
            }
        }

        // Regelanmerkungen (minOcc=0; maxOcc=1)
        child = xmlElement.getFirstChildElement("regelAnmerkungen");
        if (child != null) {
            final RegelAnmerkung rAnmerkung = new RegelAnmerkung(); // RegelAnmerkung erzeugen
            final Elements rAnmerkungen = child.getChildElements("regel");

            // einzelnen RegelAnmerkungen (minOcc=1; maxOcc=unbounded)
            for (int i = 0; i < rAnmerkungen.size(); i++) {
                final Element e = rAnmerkungen.get(i);
                rAnmerkung.add(e.getValue().trim(), e.getAttributeValue("modus"));
            }
            charElement.setRegelAnmerkung(rAnmerkung);
        }

        // Beschreibung (minOcc=0; maxOcc=1)
        child = xmlElement.getFirstChildElement("beschreibung");
        if (child != null) {
            charElement.setBeschreibung(child.getValue().trim());
        }

        // Sammelbegriff (minOcc=0; maxOcc=1)
        child = xmlElement.getFirstChildElement("sammelbegriff");
        if (child != null) {
            charElement.setSammelberiff(child.getValue().trim());
        }
        
        child = xmlElement.getFirstChildElement("voraussetzungen");
        if (child != null) {
        	final Voraussetzung voraussetzung = new Voraussetzung(charElement);
            vorausMapper.map(child, voraussetzung);
            charElement.setVoraussetzung(voraussetzung);
        }

    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {

        xmlElement.addAttribute(new Attribute("id", charElement.getId()));
        xmlElement.addAttribute(new Attribute("name", charElement.getName()));

        // Beschreibung
        String sValue = charElement.getBeschreibung();
        if (sValue.length() > 0) {
            final Element e = new Element("beschreibung");
            e.appendChild(sValue);
            xmlElement.appendChild(e);
        }

        // Anzeigen
        sValue = charElement.getAnzeigenText();
        if (sValue != null) {
            final Element e = new Element("anzeigen");
            final Attribute a = new Attribute("anzeigen", Boolean.toString(charElement.isAnzeigen()));
            e.addAttribute(a);
            e.appendChild(sValue);
            xmlElement.appendChild(e);
        }

        // Sonderregel
        if (charElement.hasSonderregel()) {
            final Sonderregel sonderregel = charElement.createSonderregel();
            final Element e = new Element("sonderregel");
            final Attribute a = new Attribute("id", ((SonderregelAdapter)sonderregel).getId());
            e.addAttribute(a);
            e.appendChild( ((SonderregelAdapter)sonderregel).getBeschreibung() );
            xmlElement.appendChild(e);
        }

        // Regelanmerkungen
        RegelAnmerkung regelAnmerkung = charElement.getRegelAnmerkung();
        if (regelAnmerkung != null) {
            final Element e = new Element("regelAnmerkungen");
            final String[] anmerkungen = regelAnmerkung.getAnmerkungen();
            final Modus[] modi = regelAnmerkung.getModi();

            // Jede Regel
            for (int i = 0; i < anmerkungen.length; i++) {
                final Element regel = new Element("regel");
                final Attribute mode = new Attribute("modus", modi[i].getValue());
                regel.addAttribute(mode);
                regel.appendChild(anmerkungen[i]);
                e.appendChild(regel);
            }
            xmlElement.appendChild(e);
        }

        // Sammelbegriff
        sValue = charElement.getSammelBegriff();
        if (sValue.length() > 0) {
            final Element e = new Element("sammelbegriff");
            e.appendChild(sValue);
            xmlElement.appendChild(e);
        }
        
        // Schreiben der Voraussetzungen
        Voraussetzung voraussetzung = charElement.getVoraussetzung();
        if (voraussetzung != null) {
        	final Element e = new Element("voraussetzungen");
            vorausMapper.map(voraussetzung, e);
            xmlElement.appendChild(e);
        }
    }
}
