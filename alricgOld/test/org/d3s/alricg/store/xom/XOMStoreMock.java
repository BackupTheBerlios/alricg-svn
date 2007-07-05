/*
 * Created on 10.10.2005 / 09:56:49
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Eigenschaft;
import org.d3s.alricg.charKomponenten.EigenschaftEnum;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;
import org.d3s.alricg.store.Configuration;
import org.d3s.alricg.store.ConfigurationException;
import org.d3s.alricg.store.DataStore;

public class XOMStoreMock extends Object implements DataStore {

	private final Map<CharKomponente, Map<String, ? extends CharElement>> mapOfMaps = new HashMap<CharKomponente, Map<String, ? extends CharElement>>();

	private final Map<String, CharKomponente> komponenten = new HashMap<String, CharKomponente>();

	private final Map<KostenKlasse, Integer[]> skt = new HashMap<KostenKlasse, Integer[]>();

	public CharElement getCharElement(String id) {
		return getCharElement(id, getCharKompFromId(id));
	}

	public CharElement getCharElement(String id, CharKomponente charKomp) {
		return mapOfMaps.get(charKomp).get(id);
	}

	public CharKomponente getCharKompFromId(String id) {
		try {
			final String prefix = id.split("-")[0];
			return getCharKompFromPrefix(prefix);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new RuntimeException("ID falsch (" + id + ")!", e);
		}
	}

	public CharKomponente getCharKompFromPrefix(String prefix) {
		return komponenten.get(prefix);
	}

	public Collection<CharElement> getUnmodifieableCollection(
			CharKomponente charKomp) {
		return Collections.unmodifiableCollection(mapOfMaps.get(charKomp)
				.values());
	}

	public void storeData() throws ConfigurationException {
		throw new NoSuchMethodError("Not implemented!");
	}

	public Map<KostenKlasse, Integer[]> getSkt() throws ConfigurationException {
		return skt;
	}

	<T extends CharElement> void put(CharKomponente komponente, String key,
			T val) {

		Map<String, T> n = null;
		if (!mapOfMaps.containsKey(komponente)) {
			n = new HashMap<String, T>();
			mapOfMaps.put(komponente, n);
		} else {
			// must use unchecked cast
			n = (Map<String, T>) mapOfMaps.get(komponente);

		}

		// ClassCastException may be thrown here.
		n.put(key, val);

	}

	void init(Configuration config) throws ConfigurationException {

		// komponenten
		for (int i = 0; i < CharKomponente.values().length; i++) {
			komponenten.put(CharKomponente.values()[i].getPrefix(),
					CharKomponente.values()[i]);
		}

		// eigenschaften
		List<String> ids = EigenschaftEnum.getIdArray();
		Map<String, Eigenschaft> eigenschaftMap = new HashMap<String, Eigenschaft>();
		for (Iterator<String> i = ids.iterator(); i.hasNext();) {
			String id = i.next();
			eigenschaftMap.put(id, new Eigenschaft(id));
		}
		mapOfMaps.put(CharKomponente.eigenschaft, eigenschaftMap);

		// skt
		skt.put(KostenKlasse.A, new Integer[32]);
		skt.put(KostenKlasse.B, new Integer[32]);
		skt.put(KostenKlasse.C, new Integer[32]);
		skt.put(KostenKlasse.D, new Integer[32]);
		skt.put(KostenKlasse.E, new Integer[32]);
		skt.put(KostenKlasse.F, new Integer[32]);
		skt.put(KostenKlasse.G, new Integer[32]);
		skt.put(KostenKlasse.H, new Integer[32]);
		for (int i = 0; i < 32; i++) {
			skt.get(KostenKlasse.A)[i] = i + 0 * 32;
			skt.get(KostenKlasse.B)[i] = i + 1 * 32;
			skt.get(KostenKlasse.C)[i] = i + 2 * 32;
			skt.get(KostenKlasse.D)[i] = i + 3 * 32;
			skt.get(KostenKlasse.E)[i] = i + 4 * 32;
			skt.get(KostenKlasse.F)[i] = i + 5 * 32;
			skt.get(KostenKlasse.G)[i] = i + 6 * 32;
			skt.get(KostenKlasse.H)[i] = i + 7 * 32;
		}
	}
}
