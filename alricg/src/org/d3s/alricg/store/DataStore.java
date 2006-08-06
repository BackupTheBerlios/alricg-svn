/*
 * Created on 15.06.2005 / 12:13:14
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store;

import java.util.Collection;
import java.util.Map;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;

/**
 * Abstraktion der Regeldatenhaltung von alricg.
 * <p>
 * Zum gegenwärtigen Zeitpunkt (September 2005) ist noch keine Möglichkeit
 * vorhanden Daten zu speichern. Ein Manko, das es schnellstmöglich zu beheben
 * gilt.
 * </p>
 * 
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
public interface DataStore {

	/**
	 * Gibt das zu <code>id</code> gehörige <code>CharElement</code> zurück.
	 * Ist die <code>CharKomponente</code> des Elements bekannt, sollte aus
	 * Performancegründen
	 * <code>getCharElement(String id, CharKomponente charKomp)</code> benutzt
	 * werden.
	 * 
	 * @param id
	 *            Die alricg-weit eindeutige ID des gesuchten
	 *            <code>CharElement</code>.
	 * @return Das zu <code>id</code> gehörige <code>CharElement</code> oder
	 *         <code>null</code>, falls kein passendes Element gefunden
	 *         werden konnte.
	 */
	CharElement getCharElement(String id);

	/**
	 * Gibt das zu <code>id</code> und <code>charKomp</code> gehörige
	 * <code>CharElement</code> zurück.
	 * 
	 * @param id
	 *            Die alricg-weit eindeutige ID des gesuchten
	 *            <code>CharElement</code>.
	 * @param charKomp
	 *            Die art der <code>Charkomponente</code>
	 * @return Die gesuchte <code>Charkomponente</code> oder <code>null</code>,
	 *         falls kein passendes Element gefunden werden konnte.
	 */
	CharElement getCharElement(String id, CharKomponente charKomp);

	/**
	 * Gibt die zu <code>id</code> gehörige <code>CharKomponente</code>
	 * zurück.
	 * <p>
	 * Die gesuchte <code>CharKomponente</code> wird anhand des Prefixes der
	 * <code>id</code> bestimmt.<br/>
	 * 
	 * <pre>
	 *      Zum Beispiel ist für &quot;RAS-Zwerg&quot; &quot;RAS&quot; das Prefix, das für alle Rassen-Ids gilt.
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @see #getCharKompFromPrefix(String)
	 * @param id
	 *            Die ID (inklusive Prefix)
	 * @return CharKomponente zu dem Prefix der <code>id</code>
	 */
	CharKomponente getCharKompFromId(String id);

	/**
	 * Gibt die zu <code>prefix</code> gehörige <code>CharKomponente</code>
	 * zurück.
	 * 
	 * @param prefix
	 *            Der prefix
	 * @return CharKomponente zu dem Prefix
	 */
	CharKomponente getCharKompFromPrefix(String prefix);

	/**
	 * Gibt alle Charakterelemente zurück, die zu einer bestimmten
	 * Charakterkomponente gehören.
	 * 
	 * @param charKomp
	 *            Die Charakterkomponente, deren Elemente gesucht werden.
	 * @return Alle Charakterelemente, die zu <code>charKomp</code> gehören,
	 *         oder eine leere Menge, falls keine Elemente gefunden werden
	 *         konnten.
	 */
	Collection<CharElement> getUnmodifieableCollection(CharKomponente charKomp);

	/**
	 * Persistiert die Charakter-Elemente.
	 * 
	 * @throws ConfigurationException
	 *             Falls die Konfiguration der Factory fehlerhaft ist.
	 */
	void storeData() throws ConfigurationException;

	/**
	 * Gibt die SKT (Steigerungskostentabelle) zurück.
	 * <p>
	 * Für die map gilt:
	 * <ul>
	 * <li>key = Gewünschte Kostenklasse</li>
	 * <li>value = (Integer[0] - Kosten für Stufen 1) bis (Integer[29] - Kosten
	 * für Stufen 30)</li>
	 * 
	 * @return Die aktuelle SKT.
	 * @throws ConfigurationException
	 *             Falls keine SKT zurückgeliefert werden kann
	 */
	Map<KostenKlasse, Integer[]> getSkt() throws ConfigurationException;
}
