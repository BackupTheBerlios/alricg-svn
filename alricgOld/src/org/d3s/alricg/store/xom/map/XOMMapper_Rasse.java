/*
 * Created on 23.06.2005 / 15:16:17
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store.xom.map;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.HerkunftVariante;
import org.d3s.alricg.charKomponenten.Rasse;
import org.d3s.alricg.charKomponenten.RasseVariante;
import org.d3s.alricg.charKomponenten.charZusatz.WuerfelSammlung;
import org.d3s.alricg.charKomponenten.links.IdLinkList;
import org.d3s.alricg.store.FactoryFinder;

/**
 * <code>XOMMapper</code> für eine <code>Rasse</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.Rasse
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
class XOMMapper_Rasse extends XOMMapper_Herkunft {

    /** <code>XOMMapper_Rasse</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMMapper_Rasse.class.getName());
    
    private final XOMMapper<IdLinkList> idLinkListMapper = new XOMMapper_IdLinkList();
    
    private final XOMMapper<HerkunftVariante> variantenMapper = new XOMMapper_HerkunftVariante();

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element, org.d3s.alricg.charKomponenten.CharElement)
    public void map(Element xmlElement, CharElement charElement) {
        super.map(xmlElement, charElement);

        // my mapping
        final Rasse rasse = (Rasse) charElement;

        try {
            Element current;

            // Übliche Kulturen einlesen
            current = xmlElement.getFirstChildElement("kulturUeblich");
            if (current != null) {
                final IdLinkList kulturUeblich = new IdLinkList(rasse);
                idLinkListMapper.map(current, kulturUeblich);
                rasse.setKulturUeblich(kulturUeblich);
            }

            // Mögliche Kulturen einlesen
            current = xmlElement.getFirstChildElement("kulturMoeglich");
            if (current != null) {
                final IdLinkList kulturMoeglich = new IdLinkList(rasse);
                idLinkListMapper.map(current, kulturMoeglich);
                rasse.setKulturMoeglich(kulturMoeglich);
            }

            // Einlesen der Geschwindigkeit
            current = xmlElement.getFirstChildElement("geschwindigkeit");
            if (current != null) {
                rasse.setGeschwindigk(Integer.parseInt(current.getValue()));
            }

            // Einlesen des Start-Alters
            current = xmlElement.getFirstChildElement("alter");
            Elements children = current.getChildElements("wuerfel");
            Integer[] anzahlWuerfel = new Integer[children.size()];
            Integer[] augenWuerfel = new Integer[children.size()];
            for (int i = 0; i < children.size(); i++) {
                anzahlWuerfel[i] = Integer.parseInt(children.get(i).getAttributeValue("anzWuerfel"));
                augenWuerfel[i] = Integer.parseInt(children.get(i).getAttributeValue("augenWuerfel"));
            }

            final int wert = Integer.parseInt(current.getAttributeValue("wert"));
            rasse.setAlterWuerfel(new WuerfelSammlung(wert, anzahlWuerfel, augenWuerfel));

            // Einlesen der Größe
            current = xmlElement.getFirstChildElement("groesse");
            children = current.getChildElements("wuerfel");
            anzahlWuerfel = new Integer[children.size()];
            augenWuerfel = new Integer[children.size()];
            for (int i = 0; i < children.size(); i++) {
                anzahlWuerfel[i] = Integer.parseInt(children.get(i).getAttributeValue("anzWuerfel"));
                augenWuerfel[i] = Integer.parseInt(children.get(i).getAttributeValue("augenWuerfel"));
            }

            final int groesse = Integer.parseInt(xmlElement.getFirstChildElement("groesse").getAttributeValue("wert"));
            rasse.setGroesseWuerfel(new WuerfelSammlung(groesse, anzahlWuerfel, augenWuerfel));

            // Einlesen des Modis für das Gewicht
            rasse.setGewichtModi(Integer.parseInt(xmlElement.getFirstChildElement("gewichtModi").getAttributeValue(
                    "wert")));

            // Einlesen der Haarfarbe
            current = xmlElement.getFirstChildElement("haarfarbe");
            farbenFromXML(current.getChildElements("farbe"), rasse.getHaarfarbe());

            // Einlesen der Augenfarbe
            current = xmlElement.getFirstChildElement("augenfarbe");
            farbenFromXML(current.getChildElements("farbe"), rasse.getAugenfarbe());

            // Einlesen der Varianten
            ArrayList<RasseVariante> arList = new ArrayList<RasseVariante>();
            current = xmlElement.getFirstChildElement("varianten");
            if (current != null) {
                Elements varianten = current.getChildElements("variante");
                for (int i = 0; i < varianten.size(); i++) {
                    final RasseVariante variante = (RasseVariante) FactoryFinder.find().getData().getCharElement(
                            varianten.get(i).getAttributeValue("id"));
                    
                    // Setzen von welcher Herkunft dies eine Variante ist
    				variante.setVarianteVon(rasse);
                    map(varianten.get(i), variante);
                    variantenMapper.map(varianten.get(i), variante);
                    arList.add(variante);
                }
                rasse.setVarianten(arList.toArray(new RasseVariante[arList.size()]));
            }

        } catch (NumberFormatException exc) {
            LOG.log(Level.SEVERE, "String -> int failed", exc);
        }

    }

    // @see org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement, nu.xom.Element)
    public void map(CharElement charElement, Element xmlElement) {
        super.map(charElement, xmlElement);

        // my mapping
        final Rasse rasse = (Rasse) charElement;
        xmlElement.setLocalName("rasse");

        /*
         * "varianteVon" schreiben if (rasse.getVarianteVon() != null) { // hierfür muß die richtige Position bestimmt
         * werden: idx = xmlElement.indexOf(xmlElement.getFirstChildElement("gp")); final Element e = new
         * Element("varianteVon"); e.appendChild(rasse.getVarianteVon().getId()); // einfügen nach dem "gp" Element!
         * xmlElement.insertChild(e, idx + 1); }
         */

        // Übliche Kulturen schreiben
        IdLinkList ids = rasse.getKulturUeblich();
        if (ids != null) {
            final Element e = new Element("kulturUeblich");
            idLinkListMapper.map(ids, e);
            xmlElement.appendChild(e);
        }

        // Mögliche Kulturen einlesen
        ids = rasse.getKulturMoeglich();
        if (ids != null) {
            final Element e = new Element("kulturMoeglich");
            idLinkListMapper.map(ids, e);
            xmlElement.appendChild(e);
        }

        // Geschwindigkeit schreiben
        Element e = new Element("geschwindigkeit");
        e.appendChild(Integer.toString(rasse.getGeschwindigk()));
        xmlElement.appendChild(e);

        // Alter schreiben
        e = new Element("alter");
        WuerfelSammlung ws = rasse.getAlterWuerfel();
        e.addAttribute(new Attribute("wert", Integer.toString(ws.getFestWert())));
        for (int i = 0; i < ws.getAnzahlWuerfel().length; i++) {
            final Element ee = new Element("wuerfel");
            ee.addAttribute(new Attribute("anzWuerfel", Integer.toString(ws.getAnzahlWuerfel()[i])));
            ee.addAttribute(new Attribute("augenWuerfel", Integer.toString(ws.getAugenWuerfel()[i])));
            e.appendChild(ee);
        }
        xmlElement.appendChild(e);

        // Größe schreiben
        e = new Element("groesse");
        ws = rasse.getGroesseWuerfel();
        e.addAttribute(new Attribute("wert", Integer.toString(ws.getFestWert())));
        for (int i = 0; i < ws.getAnzahlWuerfel().length; i++) {
            final Element ee = new Element("wuerfel");
            ee.addAttribute(new Attribute("anzWuerfel", Integer.toString(ws.getAnzahlWuerfel()[i])));
            ee.addAttribute(new Attribute("augenWuerfel", Integer.toString(ws.getAugenWuerfel()[i])));
            e.appendChild(ee);
        }
        xmlElement.appendChild(e);

        // Modifikation des Gewichtes schreiben
        e = new Element("gewichtModi");
        e.addAttribute(new Attribute("wert", Integer.toString(rasse.getGewichtModi())));
        xmlElement.appendChild(e);

        // Haarfarbe schreiben
        e = new Element("haarfarbe");
        farbenToXML(e, rasse.getHaarfarbe());
        xmlElement.appendChild(e);

        // Augenfarbe schreiben
        e = new Element("augenfarbe");
        farbenToXML(e, rasse.getAugenfarbe());
        xmlElement.appendChild(e);

        // Schreiben der Varianten
        RasseVariante[] varianten = rasse.getVarianten();
        if (varianten != null) {
            e = new Element("varianten");

            for (int i = 0; i < varianten.length; i++) {
                final Element variante = new Element("variante");
                map(varianten[i], variante);
                variantenMapper.map(varianten[i], variante);
                e.appendChild(variante);
            }
            xmlElement.appendChild(e);
        }
    }

    /**
     * Liest die Farben (maximal 20) aus xml-Elementen
     * 
     * @param elements Die xml-elemente mit den angaben
     * @param array Das Array, in das die Farb-Angaben geschrieben werden
     */
    private void farbenFromXML(Elements elements, String[] array) {
        int idx = 0;
        for (int i = 0; i < elements.size(); i++) {
            final int anteil = Integer.parseInt(elements.get(i).getAttributeValue("anteil"));

            for (int ii = 0; ii < anteil; ii++) {
                array[idx] = elements.get(i).getAttributeValue("farbe");
                idx++;
            }
        }

        if (idx < 20) {
            LOG.warning("Index zu klein: Sollwert von 20 wurde nicht erreicht!");
        }
    }

    /**
     * Schreibt die Farben in ein xml-Element
     * 
     * @param xmlElement Als xml-Element, zu dem geschrieben wird.
     * @param array Das array mit allen Farben (max 20) als Array
     */
    private void farbenToXML(Element xmlElement, String[] array) {
        String currentFarbe = array[0];
        int idx = 0;

        for (int i = 0; i < array.length; i++) {
            if (!currentFarbe.equals(array[i])) {
                final Element element = new Element("farbe");
                element.addAttribute(new Attribute("farbe", currentFarbe));
                element.addAttribute(new Attribute("anteil", Integer.toString(idx)));
                xmlElement.appendChild(element);

                idx = 1; // Für nächsten Durchlauf
                currentFarbe = array[i]; // Für nächsten Durchlauf,
            } else {
                idx++;
            }
        }

        // Letztes Element "nachtragen"
        final Element element = new Element("farbe");
        element.addAttribute(new Attribute("farbe", currentFarbe));
        element.addAttribute(new Attribute("anteil", Integer.toString(idx)));
        xmlElement.appendChild(element);
    }
}
