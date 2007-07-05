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
import org.d3s.alricg.charKomponenten.Herkunft;
import org.d3s.alricg.charKomponenten.Repraesentation;
import org.d3s.alricg.charKomponenten.Werte.Geschlecht;
import org.d3s.alricg.charKomponenten.links.Auswahl;
import org.d3s.alricg.charKomponenten.links.IdLinkList;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.store.FactoryFinder;

/**
 * Abstrakter <code>XOMMapper</code> für eine <code>Herkunft</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.Herkunft
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
abstract class XOMMapper_Herkunft extends XOMMapper_CharElement {

    /** <code>XOMMapper_Herkunft</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMMapper_Herkunft.class.getName());
    
    private final XOMMapper<Auswahl> auswahlMapper = new XOMMapper_Auswahl();
    private final XOMMapper<IdLinkList> idLinkListMapper = new XOMMapper_IdLinkList();

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {
        super.map(xmlElement, charElement);

        // my mapping
        final Herkunft herkunft = (Herkunft) charElement;

        // Auslesen der GP
        try {
            herkunft.setGpKosten(Integer.parseInt(xmlElement.getFirstChildElement("gp").getValue()));

            // Auslesen vom "kannGewaehltWerden"-Tag
            Element current = xmlElement.getFirstChildElement("kannGewaehltWerden");
            if (current != null) {

                // Sicherstellen, das der Wert gültig ist
                assert current.getValue().equalsIgnoreCase("false") || current.getValue().equalsIgnoreCase("true");
                herkunft.setKannGewaehltWerden(Boolean.parseBoolean(current.getValue()));
            }

            // Auslesen des Geschlechts
            current = xmlElement.getFirstChildElement("geschlecht");
            if (current != null) {

                // Sicherstellen, das der Wert gültig ist
                String value = current.getValue().toUpperCase();
                assert value.equals("M") || value.equals("W") || value.equals("MW");
                if (value.equals("M")) {
                    herkunft.setGeschlecht(Geschlecht.mann);
                } else if (value.equals("W")) {
                    herkunft.setGeschlecht(Geschlecht.frau);
                } else { // ... .getValue().equals("MW")
                    herkunft.setGeschlecht(Geschlecht.mannFrau);
                }
            }

            // Auslesen der eigenschaftModi
            current = xmlElement.getFirstChildElement("eigenschaftModi");
            if (current != null) {
                final Auswahl eigenschaftModis = new Auswahl(herkunft);
                auswahlMapper.map(current, eigenschaftModis);
                herkunft.setEigenschaftModis(eigenschaftModis);
            }

            // Auslesen der Grenzen für den Sozialstatus
            current = xmlElement.getFirstChildElement("soGrenzen");
            if (current != null) {

                if (current.getAttribute("soMin") != null) {
                    herkunft.setSoMin(Integer.parseInt(current.getAttributeValue("soMin")));
                }
                if (current.getAttribute("soMax") != null) {
                    herkunft.setSoMax(Integer.parseInt(current.getAttributeValue("soMax")));
                }
            }

            // Auslesen der Vorteile
            current = xmlElement.getFirstChildElement("vorteile");
            if (current != null) {
                final Auswahl auswahl = new Auswahl(herkunft);
                auswahlMapper.map(current, auswahl);
                herkunft.setVorteileAuswahl(auswahl);
            }

            // Auslesen der Nachteile
            current = xmlElement.getFirstChildElement("nachteile");
            if (current != null) {
                final Auswahl auswahl = new Auswahl(herkunft);
                auswahlMapper.map(current, auswahl);
                herkunft.setNachteileAuswahl(auswahl);
            }

            // Auslesen der Sonderfertigkeiten
            current = xmlElement.getFirstChildElement("sonderf");
            if (current != null) {
                final Auswahl auswahl = new Auswahl(herkunft);
                auswahlMapper.map(current, auswahl);
                herkunft.setSfAuswahl(auswahl);
            }

            // Auslesen der Liturgien
            current = xmlElement.getFirstChildElement("liturgien");
            if (current != null) {
                final Auswahl auswahl = new Auswahl(herkunft);
                auswahlMapper.map(current, auswahl);
                herkunft.setLiturgienAuswahl(auswahl);
            }

            // Auslesen der goetterRituale
            current = xmlElement.getFirstChildElement("goetterRituale");
            if (current != null) {
                final Auswahl auswahl = new Auswahl(herkunft);
                auswahlMapper.map(current, auswahl);
                herkunft.setRitualeAuswahl(auswahl);
            }

            // Auslesen der empfohlenen Vorteile
            current = xmlElement.getFirstChildElement("empfVorteile");
            if (current != null) {
                final IdLinkList auswahl = new IdLinkList(herkunft);
                idLinkListMapper.map(current, auswahl);
                herkunft.setEmpfVorteile(auswahl);
            }

            // Auslesen der empfohlenen Nachteile
            current = xmlElement.getFirstChildElement("empfNachteile");
            if (current != null) {
                final IdLinkList auswahl = new IdLinkList(herkunft);
                idLinkListMapper.map(current, auswahl);
                herkunft.setEmpfNachteile(auswahl);
            }

            // Auslesen der ungeeigneten Vorteile
            current = xmlElement.getFirstChildElement("ungeVorteile");
            if (current != null) {
                final IdLinkList auswahl = new IdLinkList(herkunft);
                idLinkListMapper.map(current, auswahl);
                herkunft.setUngeVorteile(auswahl);
            }

            // Auslesen der ungeeigneten Nachteile
            current = xmlElement.getFirstChildElement("ungeNachteile");
            if (current != null) {
                final IdLinkList auswahl = new IdLinkList(herkunft);
                idLinkListMapper.map(current, auswahl);
                herkunft.setUngeNachteile(auswahl);
            }

            // Auslesen der verbilligten Vorteile
            current = xmlElement.getFirstChildElement("verbilligteVorteile");
            if (current != null) {
                final IdLinkList auswahl = new IdLinkList(herkunft);
                idLinkListMapper.map(current, auswahl);
                herkunft.setVerbilligteVort(auswahl);
            }

            // Auslesen der verbilligten Nachteile
            current = xmlElement.getFirstChildElement("verbilligteNachteile");
            if (current != null) {
                final IdLinkList auswahl = new IdLinkList(herkunft);
                idLinkListMapper.map(current, auswahl);
                herkunft.setVerbilligteNacht(auswahl);
            }

            // Auslesen der verbilligten Sonderfertigkeiten
            current = xmlElement.getFirstChildElement("verbilligteSonderf");
            if (current != null) {
                final IdLinkList auswahl = new IdLinkList(herkunft);
                idLinkListMapper.map(current, auswahl);
                herkunft.setVerbilligteSonderf(auswahl);
            }

            // Auslesen der verbilligten Liturgien
            current = xmlElement.getFirstChildElement("verbilligteLiturgien");
            if (current != null) {
                final IdLinkList auswahl = new IdLinkList(herkunft);
                idLinkListMapper.map(current, auswahl);
                herkunft.setVerbilligteLiturgien(auswahl);
            }

            // Auslesen der verbilligten Rituale
            current = xmlElement.getFirstChildElement("verbilligteRituale");
            if (current != null) {
                final IdLinkList auswahl = new IdLinkList(herkunft);
                idLinkListMapper.map(current, auswahl);
                herkunft.setVerbilligteRituale(auswahl);
            }

            // Auslesen der Talente
            current = xmlElement.getFirstChildElement("talente");
            if (current != null) {
                final Auswahl auswahl = new Auswahl(herkunft);
                auswahlMapper.map(current, auswahl);
                herkunft.setTalente(auswahl);
            }

            // Auslesen der magischen Werte
            current = xmlElement.getFirstChildElement("magie");
            if (current != null) {

                // Auslesen der Art der Magischen repräsentation
                if (current.getAttribute("repraesentId") != null) {
                    String attVal = current.getAttributeValue("repraesentId");
                    herkunft.setRepraesentation((Repraesentation) FactoryFinder.find().getData().getCharElement(attVal,
                            CharKomponente.repraesentation));
                }

                // Auslesen der Hauszauber
                Element e = current.getFirstChildElement("hauszauber");
                if (e != null) {
                    final Auswahl auswahl = new Auswahl(herkunft);
                    auswahlMapper.map(e, auswahl);
                    herkunft.setHauszauber(auswahl);
                }

                // Auslesen der Zauber
                e = current.getFirstChildElement("zauber");
                if (e != null) {
                    final Auswahl auswahl = new Auswahl(herkunft);
                    auswahlMapper.map(e, auswahl);
                    herkunft.setZauber(auswahl);
                }

                // Auslesen der Zauber
                e = current.getFirstChildElement("aktivierbareZauber");
                if (e != null) {
                    final Auswahl auswahl = new Auswahl(herkunft);
                    auswahlMapper.map(e, auswahl);
                    herkunft.setAktivierbareZauber(auswahl);
                }

                // Auslesen der Zauber, die nicht zu Beginn wählbar sind
                e = current.getFirstChildElement("zauberNichtZuBegin");
                if (e != null) {
                    final IdLinkList auswahl = new IdLinkList(herkunft);
                    idLinkListMapper.map(e, auswahl);
                    herkunft.setZauberNichtBeginn(auswahl);
                }
            }

        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "String -> int failed", ex);
        }

    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        super.map(charElement, xmlElement);

        // my mapping
        final Herkunft herkunft = (Herkunft) charElement;

        // Hinzufügen der GP-Kosten
        Element e = new Element("gp");
        e.appendChild(Integer.toString(herkunft.getGpKosten()));
        xmlElement.appendChild(e);

        // Hinzufügen des "varianteVon" Tags wird in Rasse, Kultur bzw. Profession erledigt!

        // Hinzufügen des "kannGewaehltWerden"-Tags
        e = new Element("kannGewaehltWerden");
        e.appendChild(Boolean.toString(herkunft.isKannGewaehltWerden()));
        xmlElement.appendChild(e);

        // Hinzufügen des Geschlechts geschlecht
        if (herkunft.getGeschlecht() == Geschlecht.mann) {
            e = new Element("geschlecht");
            e.appendChild("M");
            xmlElement.appendChild(e);
        } else if (herkunft.getGeschlecht() == Geschlecht.frau) {
            e = new Element("geschlecht");
            e.appendChild("W");
            xmlElement.appendChild(e);
        } // else ... ist Default und brauch nicht beachtet zu werden

        // Hinzufügen der Eigenschaft-Modis
        Auswahl auswahl = herkunft.getEigenschaftModis();
        map(auswahl, "eigenschaftModi", xmlElement);

        // Hinzufügen der SO-Grenze
        final int soMax = herkunft.getSoMax();
        final int soMin = herkunft.getSoMin();
        if (soMax != Herkunft.SO_MAX_DEFAULT || soMin != Herkunft.SO_MIN_DEFAULT) {
            e = new Element("soGrenzen");

            if (soMin != Herkunft.SO_MIN_DEFAULT) {
                e.addAttribute(new Attribute("soMin", Integer.toString(soMin)));
            }
            if (soMax != Herkunft.SO_MAX_DEFAULT) {
                e.addAttribute(new Attribute("soMax", Integer.toString(soMax)));
            }
            xmlElement.appendChild(e);
        }

        // Hinzufügen der Vorteile
        auswahl = herkunft.getVorteileAuswahl();
        map(auswahl, "vorteile", xmlElement);

        // Hinzufügen der Nachteile
        auswahl = herkunft.getNachteileAuswahl();
        map(auswahl, "nachteile", xmlElement);

        // Hinzufügen der Sonderfertigkeiten
        auswahl = herkunft.getSfAuswahl();
        map(auswahl, "sonderf", xmlElement);

        // Hinzufügen der Liturgien
        auswahl = herkunft.getLiturgienAuswahl();
        map(auswahl, "liturgien", xmlElement);

        // Hinzufügen der Rituale
        auswahl = herkunft.getRitualeAuswahl();
        map(auswahl, "goetterRituale", xmlElement);

        // Hinzufügen der empfohlenen Vorteile
        IdLinkList ids = herkunft.getEmpfVorteil();
        map(ids, "empfVorteile", xmlElement);

        // Hinzufügen der empfohlenen Nachteile
        ids = herkunft.getEmpfNachteile();
        map(ids, "empfNachteile", xmlElement);

        // Hinzufügen der ungeeigneten Vorteile
        ids = herkunft.getUngeVorteile();
        map(ids, "ungeVorteile", xmlElement);

        // Hinzufügen der ungeeigneten Nachteile
        ids = herkunft.getUngeNachteile();
        map(ids, "ungeNachteile", xmlElement);

        // Hinzufügen der verbilligten Vorteile
        ids = herkunft.getVerbilligteVort();
        map(ids, "verbilligteVorteile", xmlElement);

        // Hinzufügen der verbilligten Nachteile
        ids = herkunft.getVerbilligteNacht();
        map(ids, "verbilligteNachteile", xmlElement);

        // Hinzufügen der verbilligten Sonderfertigkeiten
        ids = herkunft.getVerbilligteSonderf();
        map(ids, "verbilligteSonderf", xmlElement);

        // Hinzufügen der verbilligten Liturgien
        ids = herkunft.getVerbilligteLiturgien();
        map(ids, "verbilligteLiturgien", xmlElement);

        // Hinzufügen der verbilligten Rituale
        ids = herkunft.getVerbilligteRituale();
        map(ids, "verbilligteRituale", xmlElement);

        // Hinzufügen der Talente
        auswahl = herkunft.getTalente();
        map(auswahl, "talente", xmlElement);

        // Hinzufügen der Magie
        final Repraesentation rep = herkunft.getRepraesentation();
        final Auswahl hausz = herkunft.getHauszauber();
        final Auswahl zauber = herkunft.getZauber();
        final Auswahl aktivierbareZauber = herkunft.getAktivierbareZauber();
        final IdLinkList zauberNichtBeginn = herkunft.getZauberNichtBeginn();
        if (rep != null || hausz != null || zauber != null || aktivierbareZauber != null || zauberNichtBeginn != null) {
            e = new Element("magie");

            // Schreiben der Repraesentation
            if (rep != null) {
                e.addAttribute(new Attribute("repraesentId", rep.getId()));
            }

            // Hinzufügen der Hauszauber
            map(hausz, "hauszauber", e);

            // Hinzufügen der Zauber
            map(zauber, "zauber", e);

            // Hinzufügen der aktivierbaren Zauber
            map(aktivierbareZauber, "aktivierbareZauber", e);

            // Hinzufügen der nicht zu beginn wählbaren Zauber
            map(zauberNichtBeginn, "zauberNichtZuBegin", e);

            xmlElement.appendChild(e);
        }
    }

    /**
     * Fügt <code>auswahl</code> unter <code>tagname</code> zu <code>parent</code> hinzu.
     * 
     * @param auswahl Die hinzuzufügenden Wahlmöglichkeiten.
     * @param tagname Die Bezeichnung des xml-Elements.
     * @param parent Das xml-Element worunter die Auswahl hinzugefügt werden soll.
     */
    private void map(Auswahl auswahl, String tagname, Element parent) {
        if (auswahl != null) {
            final Element e = new Element(tagname);
            new XOMMapper_Auswahl().map(auswahl, e);
            parent.appendChild(e);
        }
    }

    /**
     * Fügt <code>ids</code> unter <code>tagname</code> zu <code>parent</code> hinzu.
     * 
     * @param ids Die hinzuzufügenden <code>CharElement</code>-IDs.
     * @param tagname Die Bezeichnung des xml-Elements.
     * @param parent Das xml-Element worunter die link-Liste hinzugefügt werden soll.
     */
    private void map(IdLinkList ids, String tagname, Element parent) {
        if (ids != null) {
            final Element e = new Element(tagname);
            idLinkListMapper.map(ids, e);
            parent.appendChild(e);
        }

    }
}
