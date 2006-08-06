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
import org.d3s.alricg.charKomponenten.VorNachteil;
import org.d3s.alricg.charKomponenten.links.IdLinkList;

/**
 * Abstrakter <code>XOMMapper</code> für einen <code>VorNachteil</code>.
 * 
 * @see org.d3s.alricg.store.xom.map.XOMMapper
 * @see org.d3s.alricg.charKomponenten.VorNachteil
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
abstract class XOMMapper_VorNachteil extends XOMMapper_Fertigkeit {

	/** <code>XOMMapper_VorNachteil</code>'s logger */
	private static final Logger LOG = Logger
			.getLogger(XOMMapper_VorNachteil.class.getName());
	
	private final XOMMapper<IdLinkList> idLinkListMapper = new XOMMapper_IdLinkList();

	// @see org.d3s.alricg.store.xom.map.XOMMapper#map(nu.xom.Element,
	// org.d3s.alricg.charKomponenten.CharElement)
	public void map(Element xmlElement, CharElement charElement) {
		super.map(xmlElement, charElement);

		// my mapping
		final VorNachteil vorNachteil = (VorNachteil) charElement;
		try {
			// Auslesen pro wie viele Punkte die GP Kosten gelten
			if (xmlElement.getAttribute("kostenProSchritt") != null) {
				vorNachteil.setKostenProSchritt(Integer.parseInt(xmlElement
						.getAttributeValue("kostenProSchritt")));
			}
		} catch (NumberFormatException exc) {
			LOG.log(Level.SEVERE, "String -> int failed", exc);
		}
		try {
			// Auslesen in welchen Schritten gesteigert werden darf
			if (xmlElement.getAttribute("stufenSchritt") != null) {
				vorNachteil.setStufenSchritt(Integer.parseInt(xmlElement
						.getAttributeValue("stufenSchritt")));
			}
		} catch (NumberFormatException exc) {
			LOG.log(Level.SEVERE, "String -> int failed", exc);
		}

		// Auslesen der Stufengrenzen
		Element current = xmlElement.getFirstChildElement("stufenGrenzen");
		if (current != null) {
			try {
				if (current.getAttribute("minStufe") != null) {
					vorNachteil.setMinStufe(Integer.parseInt(current
							.getAttributeValue("minStufe")));
				}
			} catch (NumberFormatException exc) {
				LOG.log(Level.SEVERE, "String -> int failed", exc);
			}
			try {
				if (current.getAttribute("maxStufe") != null) {
					vorNachteil.setMaxStufe(Integer.parseInt(current
							.getAttributeValue("maxStufe")));
				}
			} catch (NumberFormatException exc) {
				LOG.log(Level.SEVERE, "String -> int failed", exc);
			}

		}

		// Auslesen wie of der Vor/Nachteil wählbar ist
		//
		// if ( xmlElement.getFirstChildElement("istMehrfachWaehlbar") != null )
		// {
		// istMehrfachWaehlbar =
		// Integer.parseInt(xmlElement
		// .getFirstChildElement("istMehrfachWaehlbar").getValue()); }
		//

		
		// Auslesen welche Vorteile mit diesem Vor/Nachteil verboten sind
		// if (xmlElement.getFirstChildElement("verbietetVorteil") != null ) {
		// verbietetVorteil = new IdLinkList(this);
		// verbietetVorteil.loadXmlElement(xmlElement.getFirstChildElement("verbietetVorteil"));
		// }
		// Auslesen welche Nachteile mit diesem Vor/Nachteil verboten sind
		// if ( xmlElement.getFirstChildElement("verbietetNachteil") != null ) {
		// verbietetNachteil = new IdLinkList(this);
		// verbietetNachteil.loadXmlElement(xmlElement.getFirstChildElement("verbietetNachteil"));
		// }

		// Auslesen bei welchen SF der Vor/Nachteil die Kosten verändert
		current = xmlElement.getFirstChildElement("aendertApSf");
		if (current != null) {
			final IdLinkList aendertApSf = new IdLinkList(vorNachteil);
			idLinkListMapper.map(current, aendertApSf);
			vorNachteil.setAendertApSf(aendertApSf);
		}

		// Auslesen bei welchen SF der Vor/Nachteil die Kosten verändert
		current = xmlElement.getFirstChildElement("aendertGpVorteil");
		if (current != null) {
			final IdLinkList aendertGpVorteil = new IdLinkList(vorNachteil);
			idLinkListMapper.map(current, aendertGpVorteil);
			vorNachteil.setAendertGpVorteil(aendertGpVorteil);
		}

		// Auslesen bei welchen SF der Vor/Nachteil die Kosten verändert
		current = xmlElement.getFirstChildElement("aendertGpNachteil");
		if (current != null) {
			final IdLinkList aendertGpNachteil = new IdLinkList(vorNachteil);
			idLinkListMapper.map(current,
					aendertGpNachteil);
			vorNachteil.setAendertGpNachteil(aendertGpNachteil);
		}
	}

	// @see
	// org.d3s.alricg.store.xom.map.XOMMapper#map(org.d3s.alricg.charKomponenten.CharElement,
	// nu.xom.Element)
	public void map(CharElement charElement, Element xmlElement) {
		super.map(charElement, xmlElement);

		// my mapping
		final VorNachteil vorNachteil = (VorNachteil) charElement;

		// Schreiben wieviele GP ein Schritt kostet
		final int kostenProSchritt = vorNachteil.getKostenProSchritt();
		if (kostenProSchritt != 1) {
			xmlElement.addAttribute(new Attribute("kostenProSchritt", Integer
					.toString(kostenProSchritt)));
		}

		// Schreiben in welchen Schritten gesteigert werden kann
		final int stufenSchritt = vorNachteil.getStufenSchritt();
		if (stufenSchritt != 1) {
			xmlElement.addAttribute(new Attribute("stufenSchritt", Integer
					.toString(stufenSchritt)));
		}

		// Schreiben wie oft der Vor/Nachteil wählbar ist
		// if ( istMehrfachWaehlbar != 1 ) {
		// tmpElement = new Element("istMehrfachWaehlbar");
		// tmpElement.appendChild(Integer.toString(istMehrfachWaehlbar));
		// xmlElement.appendChild(tmpElement); }
		//

		// schreiben der Stufengrenzen
		final int minStufe = vorNachteil.getMinStufe();
		final int maxStufe = vorNachteil.getMaxStufe();
		if (minStufe != 1 || maxStufe != 1) {
			final Element e = new Element("stufenGrenzen");
			if (minStufe != 1) {
				e.addAttribute(new Attribute("minStufe", Integer
						.toString(minStufe)));
			}
			if (maxStufe != 1) {
				e.addAttribute(new Attribute("maxStufe", Integer
						.toString(maxStufe)));
			}
			xmlElement.appendChild(e);
		}

		// Schreiben welche Vorteile verboten sind
		// if ( verbietetVorteil != null ) {
		// xmlElement.appendChild(verbietetVorteil.writeXmlElement("verbietetVorteil"));
		// }
		// Schreiben welche Nachteile verboten sind
		// if ( verbietetNachteil != null ) {
		// xmlElement.appendChild(verbietetNachteil.writeXmlElement("verbietetNachteil"));
		// }
		//

		// Schreiben welche Kosten von SF sich wie ändern
		final IdLinkList aendertApSf = vorNachteil.getAendertApSf();
		if (aendertApSf != null) {
			final Element e = new Element("aendertApSf");
			idLinkListMapper.map(aendertApSf, e);
			xmlElement.appendChild(e);
		}

		// Schreiben welche Kosten von Vorteilen sich wie ändern
		final IdLinkList aendertGpVorteil = vorNachteil.getAendertGpVorteil();
		if (aendertGpVorteil != null) {
			final Element e = new Element("aendertGpVorteil");
			idLinkListMapper.map(aendertGpVorteil, e);
			xmlElement.appendChild(e);
		}

		// Schreiben welche Kosten von Nachteilen sich wie ändern
		final IdLinkList aendertGpNachteil = vorNachteil.getAendertGpNachteil();
		if (aendertGpNachteil != null) {
			final Element e = new Element("aendertGpNachteil");
			idLinkListMapper.map(aendertGpNachteil, e);
			xmlElement.appendChild(e);
		}
	}
}
