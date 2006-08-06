/*
 * Created on 10.10.2005 / 10:48:59
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom;

import nu.xom.Attribute;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.store.FactoryFinder;

public class XOMStoreObjectMother {

	public <T extends CharElement> void add(CharKomponente komponente,
			String key, T element) {
		data().put(komponente, key, element);
	}

	public void addProbe(Element xom) {
		final Element probe = new Element("probenWurf");
		probe.addAttribute(new Attribute("eigen1", "EIG-MU"));
		probe.addAttribute(new Attribute("eigen2", "EIG-FF"));
		probe.addAttribute(new Attribute("eigen3", "EIG-KK"));
		xom.appendChild(probe);
	}

	public void addErhaeltlichBei(Element xom, String regionName) {
		final Element element = new Element("erhaeltlichBei");
		element.appendChild(regionName);
		xom.appendChild(element);
	}

	public void addKostenKlasse(Element xom) {
		xom.addAttribute(new Attribute("kostenKlasse", "A"));
	}
	
	public void addGPKosten(Element xom) {
		xom.addAttribute(new Attribute("gp", "15"));
	}
	
	public void addSchriftSprachDaten(Element xom) {
		final Element daten = new Element("daten");
		addKostenKlasse(daten);
		daten.addAttribute(new Attribute("komplexitaet", "3"));
		xom.appendChild(daten);
	}

	XOMStoreMock data() {
		return (XOMStoreMock) FactoryFinder.find().getData();
	}

	XOMConfigStoreMock config() {
		return (XOMConfigStoreMock) FactoryFinder.find().getConfiguration();
	}

	XOMTextStoreMock text() {
		return (XOMTextStoreMock) FactoryFinder.find().getLibrary();
	}

}
