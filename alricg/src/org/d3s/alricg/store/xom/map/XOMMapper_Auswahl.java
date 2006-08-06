/*
 * Created on 18.10.2005 / 09:33:36
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom.map;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl;
import org.d3s.alricg.charKomponenten.links.Auswahl;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl.Modus;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

class XOMMapper_Auswahl implements XOMMapper<Auswahl> {
	
	/** <code>XOMMapper_Auswahl</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMMapper_Auswahl.class.getName());
	
	private final XOMMapper<IdLink> idLinkMapper = new XOMMapper_IdLink();

	public void map(Element from, Auswahl to) {
		
		// Auslesen der unveränderlichen, "festen" Elemente der Auswahl
		Elements children = from.getChildElements("fest");
		final IdLink[] festeAuswahl = new IdLink[children.size()];
		for (int i = 0; i < festeAuswahl.length; i++) {
			festeAuswahl[i] = new IdLink(to.getHerkunft(), to);
			idLinkMapper.map(children.get(i), festeAuswahl[i]);
		}
		to.setFesteAuswahl(festeAuswahl);

		// Auslesen der Auswahlmöglichkeiten
		children = from.getChildElements("auswahl");

		final AbstractVariableAuswahl[] variableAuswahl = new AbstractVariableAuswahl[children
				.size()];
		for (int i = 0; i < variableAuswahl.length; i++) {

			variableAuswahl[i] = AbstractVariableAuswahl.createAuswahl(to,
					children.get(i).getAttributeValue("modus"));

			mapVariableAuswahl(children.get(i), variableAuswahl[i]);
		}
		to.setVariableAuswahl(variableAuswahl);

	}

	public void map(Auswahl from, Element to) {
		
		// Schreiben der festen Elemente
		final IdLink[] festeAuswahl = from.getFesteAuswahl();
		for (int i = 0; i < festeAuswahl.length; i++) {
			final Element e = new Element("fest");
			idLinkMapper.map(festeAuswahl[i], e);
			to.appendChild(e);
		}

		// Schreiben der "variablen" Elemente
		final AbstractVariableAuswahl[] varianteAuswahl = from
				.getVariableAuswahl();
		for (int i = 0; i < varianteAuswahl.length; i++) {
			final Element e = new Element("auswahl");
			mapVariableAuswahl(varianteAuswahl[i], e);
			to.appendChild(e);
		}
	}
	
    /**
     * Bildet ein xml-Element in eine VariableAuswahl ab.
     * 
     * @param xmlElement Das xml-Element mit der variablen Auswahl.
     * @param variableAuswahl Die VariableAuswahl, der verändert werden soll.
     */
    private void mapVariableAuswahl(Element xmlElement, AbstractVariableAuswahl variableAuswahl) {

        // Überprüfung oder der Modus-Wert gültig ist:
        final String attValue = xmlElement.getAttributeValue("modus");
        assert attValue.equals(Modus.LISTE.getValue()) || attValue.equals(Modus.ANZAHL.getValue())
                || attValue.equals(Modus.VERTEILUNG.getValue());

        /* Nicht mehr nötig, da im Konstruktor der Auswahl schon gesetzt
        // Einlesen des Modus
        if (attValue.equals(Modus.LISTE.getValue())) {
            variableAuswahl.setModus(Modus.LISTE);
        } else if (attValue.equals(Modus.ANZAHL.getValue())) {
            variableAuswahl.setModus(Modus.ANZAHL);
        } else { // ... .equals(Modus.VERTEILUNG.getValue())
            variableAuswahl.setModus(Modus.VERTEILUNG);
        }
        */

        try {
        	// Einlesen der Werte / optional
            // --- Für den Modus Verteilung
            if (variableAuswahl.getModus().equals(Modus.VERTEILUNG)) {
            	variableAuswahl.setWert(Integer.parseInt(xmlElement.getAttributeValue("werte")));
            }
            
            // --- Für den Modus Liste
            if (variableAuswahl.getModus().equals(Modus.LISTE)) {
                final String[] attValues = xmlElement.getAttributeValue("werte").split(" ");
                final int[] werte = new int[attValues.length];
                for (int i = 0; i < attValues.length; i++) {
                    werte[i] = Integer.parseInt(attValues[i]);
                }
                variableAuswahl.setWerteListe(werte);
            }

            // Einlesen des optionalen Feldes Anzahl
            if ( (variableAuswahl.getModus().equals(Modus.VERTEILUNG) || 
            		variableAuswahl.getModus().equals(Modus.ANZAHL))
            		&& xmlElement.getAttributeValue("anzahl") != null ) {
                variableAuswahl.setAnzahl(Integer.parseInt(xmlElement.getAttributeValue("anzahl")));
            }

            // Einlesen des Maximalen Wertes / optional
            if ( variableAuswahl.getModus().equals(Modus.VERTEILUNG) 
            		&& xmlElement.getAttributeValue("max") != null ) {

                variableAuswahl.setMax(Integer.parseInt(xmlElement.getAttributeValue("max")));
            }

        } catch (NumberFormatException exc) {
            LOG.log(Level.SEVERE, "String -> int failed", exc);
        }

        // Einlesen der Optionen, sollten mind. zwei sein
        Elements children = xmlElement.getChildElements("option");
        final IdLink[] optionen = readOptionen(children, variableAuswahl);
        variableAuswahl.setOptionen(optionen);

        // Einlesen der Optionsgruppen
        children = xmlElement.getChildElements("optionsGruppe");
        IdLink[][] optionsGruppe = new IdLink[children.size()][];
        for (int i = 0; i < children.size(); i++) {
            optionsGruppe[i] = readOptionen(children.get(i).getChildElements("option"), variableAuswahl);
        }

        // Falls es keine Optionsgruppen gibt, komplettes Array wieder auf null
        if (optionsGruppe.length == 0) {
            optionsGruppe = null;
        }
        variableAuswahl.setOptionsGruppe(optionsGruppe);
    }

    /**
     * Bildet eine VariableAuswahl in ein xml-Element ab.
     * 
     * @param variableAuswahl Die VariableAuswahl, die ausgelesen werden soll.
     * @param xmlElement Das xml-Element, das geschrieben werden soll.
     */
    private void mapVariableAuswahl(AbstractVariableAuswahl variableAuswahl, Element xmlElement) {

        // Schreiben der einzelnen Optionen:
        final IdLink[] optionen = variableAuswahl.getOptionen();
        for (int i = 0; i < optionen.length; i++) {
            final Element e = new Element("option");
            idLinkMapper.map(optionen[i], e);
            xmlElement.appendChild(e);
        }

        // Schreiben der Optionsgruppen:
        final IdLink[][] optionsGruppe = variableAuswahl.getOptionsGruppe();
        if (optionsGruppe != null) {
            for (int i = 0; i < optionsGruppe.length; i++) {
                final Element e = new Element("optionsGruppe");
                for (int ii = 0; ii < optionsGruppe[i].length; ii++) {
                    final Element ee = new Element("option");
                    idLinkMapper.map(optionsGruppe[i][ii], ee);
                    e.appendChild(ee);
                }
                xmlElement.appendChild(e);
            }
        }

        // Schreiben des Attributs "werte"
        // --- Für den Modus Verteilung
        if (variableAuswahl.getModus().equals(Modus.VERTEILUNG)) {
	        final int wert = variableAuswahl.getWert();
	        xmlElement.addAttribute(new Attribute("werte", Integer.toString(wert)));
        }
        // --- Für den Modus Liste
        if (variableAuswahl.getModus().equals(Modus.LISTE)) {
	        final int[] werte = variableAuswahl.getWerteListe();
	        final StringBuffer buffy = new StringBuffer();
	        for (int i = 0; i < werte.length; i++) {
	            buffy.append(Integer.toString(werte[i]));
	            buffy.append(" ");
	        }
	        if (buffy.length() > 0) {
	            xmlElement.addAttribute(new Attribute("werte", buffy.toString().trim()));
	        }
        }
        
        // Schreiben des Attribus "anzahl"
        if (variableAuswahl.getModus().equals(Modus.VERTEILUNG) || 
        		variableAuswahl.getModus().equals(Modus.ANZAHL) ) {
            xmlElement.addAttribute(new Attribute("anzahl", Integer.toString(variableAuswahl.getAnzahl())));
        }

        // Schreiben des Attribus "max"
        if (variableAuswahl.getModus().equals(Modus.VERTEILUNG) ) {
            xmlElement.addAttribute(new Attribute("max", Integer.toString(variableAuswahl.getMax())));
        }

        // Schreiben des Attributs "modus"
        xmlElement.addAttribute(new Attribute("modus", variableAuswahl.getModus().getValue()));
    }
    
    /**
     * Bildet ein xml-Element unter Berücksichtigung der variablen Auswahl in eine Liste von Links ab.
     * 
     * @param xmlElements Das xml-Element mit den ID-Verknüpfungen.
     * @param va Die zu berücksichtigende VariableAuswahl
     * @return Ein Array, der die ID-Links des xml-Elements enthält
     */
    private IdLink[] readOptionen(Elements xmlElements, AbstractVariableAuswahl va) {
        final IdLink[] optionen = new IdLink[xmlElements.size()];
        for (int i = 0; i < optionen.length; i++) {
            optionen[i] = new IdLink(va.getAuswahl().getHerkunft(), va.getAuswahl());
            idLinkMapper.map(xmlElements.get(i), optionen[i]);
        }
        return optionen;
    }

}
