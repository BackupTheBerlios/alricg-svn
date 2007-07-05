/*
 * Created 22. September 2005 / 00:01:02
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.xom.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import nu.xom.Element;
import nu.xom.Elements;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.EigenschaftEnum;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;
import org.d3s.alricg.store.ConfigStore;
import org.d3s.alricg.store.Configuration;
import org.d3s.alricg.store.ConfigurationException;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.KeyExistsException;
import org.d3s.alricg.store.TextStore;
import org.d3s.alricg.store.xom.XOMHelper;
import org.d3s.alricg.store.xom.XOMStore;

/**
 * Mapper für die <code>CharElemente</code>.
 * 
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
public class XOMToDataMapper {

	/** <code>XOMToDataMapper</code>'s logger */
	private static final Logger LOG = Logger.getLogger(XOMToDataMapper.class
			.getName());

	/**
	 * Befüllt einen neuen <code>dataStore</code> auf Basis der Einstellungen
	 * in <code>props</code>.
	 * 
	 * @param props
	 *            Die Konfiguration mit den benötigten Einstellungen.
	 * @param dataStore
	 *            Der zu befüllende Datenspeicher.
	 * @throws ConfigurationException
	 *             Falls die Konfiguration fehlerhaft ist.
	 */
	public void readData(Configuration props, XOMStore dataStore)
			throws ConfigurationException {
		try {
			final Element configRoot = XOMHelper.getRootElement(new File(props
					.getProperty(ConfigStore.Key.config_file)));
			final TextStore lib = FactoryFinder.find().getLibrary();

			ProgAdmin.messenger.sendInfo(lib
					.getLongTxt("Regel-Dateien werden geladen"));
			final List<File> files = getXmlFiles(props, configRoot);
			LOG.info("Regel-Files aus Config geladen...");

			initCharKomponents(files, dataStore);
			LOG.info("Charakter-Elemente erstellt...");

			loadCharKomponents(files, dataStore, false);
			LOG.info("Charakter-Elemente initiiert...");

		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}

	/**
	 * Sucht die korrekte SKT anhand der Konfiguration aus.
	 * 
	 * @param configRoot
	 *            Das Element, aus dem sie SKT gelesen werden soll.
	 * @return Die SKT
	 */
	public Map<KostenKlasse, Integer[]> skt(Configuration props) {

		// Laden des richtigen packages
		final Element configRoot = XOMHelper.getRootElement(new File(props
				.getProperty(ConfigStore.Key.config_file)));

		final Element regelConfig = configRoot
				.getFirstChildElement("regelConfig");
		final String pack = regelConfig
				.getFirstChildElement("benutztesPackage").getAttributeValue(
						"id");
		final Elements elements = regelConfig.getChildElements("package");

		// Durchsuchen der packages nach dem mit der richtigen ID
		try {
			for (int i = 0; i < elements.size(); i++) {
				final Element child = elements.get(i);

				// Wenn es sich um das gesuchte package handelt....
				if (child.getAttributeValue("id").equals(pack)) {

					final Element skt = child.getFirstChildElement("skt");
					if (skt == null
							|| skt.getAttributeValue("useOriginal").equals(
									"true")) {

						// Die SKT ist "original" und wird hier eingelesen
						return loadSkt(regelConfig
								.getFirstChildElement("originalSkt"));
					} else {

						// Die SKT ist nicht original, sondern eine eigene
						return loadSkt(skt);
					}
				}
			}
			LOG.severe("Keine passende SKT gefunden");
		} catch (NumberFormatException e) {
			LOG.severe("Konnte Zahlen-String aus SKT nicht in int umwandeln.");
		} catch (NullPointerException e) {
			LOG.severe("Ein erwarteter XML-Tag der SKT ist nicht vorhanden.");
		}
		return null;
	}

	/**
	 * Liest alle xml-Files ein, die laut Konfiguration und
	 * "benoetigtDateien"-Tag eingelesen werden sollen.
	 * 
	 * @param configRoot
	 *            Das Config-Element um den Pfad zur Datei auszulesen
	 * @return Eine ArrayList mir allen Files die eingelesen werden sollen
	 *         (inklusive dem "benoetigtDateien"-Tag
	 */
	private List<File> getXmlFiles(Configuration props, Element configRoot) {
		final File d3sDataDir = new File(props
				.getProperty(ConfigStore.Key.d3s_data_path));
		final File d3sUserDir = new File(props
				.getProperty(ConfigStore.Key.user_data_path));
		final List<File> arrayFiles = new ArrayList<File>();

		// Lese die Pfade zu den XML-Files mit Komponten ein
		Elements tmpElementList = configRoot.getFirstChildElement(
				"xmlRuleFilesD3S").getChildElements();

		// Alle Orginal-Files einlesen und als File speichern
		for (int i = 0; i < tmpElementList.size(); i++) {
			loadRegelFile(props, arrayFiles, new File(d3sDataDir,
					(String) tmpElementList.get(i).getValue()));
		}

		// Prüfen ob die Userdaten überhaupt eingelesen werden sollen
		if (configRoot.getFirstChildElement("xmlRuleFilesUser")
				.getAttributeValue("readUserFiles").equals("true")) {
			tmpElementList = configRoot
					.getFirstChildElement("xmlRuleFilesUser")
					.getChildElements();

			// Alle User-Files einlesen und als File speichern
			for (int i = 0; i < tmpElementList.size(); i++) {
				loadRegelFile(props, arrayFiles, new File(d3sUserDir,
						(String) tmpElementList.get(i).getValue()));
			}
		}

		return arrayFiles;
	}

	/**
	 * Initialisiert die CharElemente nur mit ID.
	 * 
	 * @param arrayFiles
	 *            Eine ArrayList mit allen Dateien die Eingelesen werden
	 */
	private void initCharKomponents(List<File> arrayFiles,
			XOMStore charKompAdmin) throws KeyExistsException,
			ConfigurationException {

		loadCharKomponents(arrayFiles, charKompAdmin, true);

		List<String> ids = EigenschaftEnum.getIdArray();

		charKompAdmin.initCharKomponents(ids, CharKomponente.eigenschaft);
	}

	/**
	 * Initialisiert die übergebenen Elemente, indem die Elemente erzeugt werden
	 * und die ID gesetzt wird.
	 * 
	 * @param kategorien
	 *            Alle XML-Elemente der Art "current"
	 * @param current
	 *            Die Art der übergebenen Elemente
	 * @param store
	 *            Store in den die Elemente gespeichert werden sollen
	 */
	private void initHelpCharKomponents(Elements kategorien,
			CharKomponente current, XOMStore store, String canonicalPath)
			throws KeyExistsException {
		final CharKomponente currentVariante;

		final List<String> ids = new ArrayList<String>();
		for (int iii = 0; iii < kategorien.size(); iii++) {
			ids.add(kategorien.get(iii).getAttributeValue("id"));
		}
		store.initCharKomponents(ids, current);
		store.addOrigins(ids, canonicalPath);

		// Einlesen der Varianten
		if (current.equals(CharKomponente.rasse)) {
			currentVariante = CharKomponente.rasseVariante;
		} else if (current.equals(CharKomponente.kultur)) {
			currentVariante = CharKomponente.kulturVariante;
		} else if (current.equals(CharKomponente.profession)) {
			currentVariante = CharKomponente.professionVariante;
		} else {
			return; // Keine Varianten, daher return
		}

		// Alle Elemente durchgehen, um Varianten auszulsen
		for (int iii = 0; iii < kategorien.size(); iii++) {
			Element tmpElement = kategorien.get(iii).getFirstChildElement(
					"varianten");

			if (tmpElement != null) {
				initHelpCharKomponents( // rekursiver aufruf
						tmpElement.getChildElements("variante"),
						currentVariante, store, canonicalPath);
			}
		}

	}

	/**
	 * Befüllt die "leeren" CharKomponenten mit Daten.
	 */
	private void loadCharKomponents(List<File> files, XOMStore store,
			boolean init) throws KeyExistsException, ConfigurationException {
		final CharKomponente[] charKomps = CharKomponente.values();

		// Files auslesen, hier kann eigentlich kein Lade Fehler auftreten,
		// da alle File bereits durch "loadRegleXML" geladen waren!
		final Element[] rootElements = new Element[files.size()];
		final String[] fileNames = new String[files.size()];
		try {
			for (int i = 0; i < rootElements.length; i++) {
				rootElements[i] = XOMHelper.getRootElement(files.get(i));
				fileNames[i] = files.get(i).getCanonicalPath();
			}
		} catch (IOException ioe) {
			throw new ConfigurationException("A file does not exist", ioe);
		}

		// Erzeuge alle CharElement-Objekte
		for (int i = 0; i < charKomps.length; i++) { // Alle CharKomps...
			final CharKomponente current = charKomps[i];
			for (int ii = 0; ii < files.size(); ii++) { // Für alle Files...

				final Element firstChild = rootElements[ii]
						.getFirstChildElement(current.getKategorie());

				if (firstChild == null) {
					continue; // ... wenn kein Element, überspringen
				}

				// Alle Elemente zu dem CharKomp auslesen
				final Elements kategorien = firstChild.getChildElements();

				// Entscheiden ob initialisierung der Elemente oder mapping der
				// Daten
				if (init) {
					initHelpCharKomponents(kategorien, current, store,
							fileNames[ii]);
				} else {
					mapHelpCharKomponents(kategorien, current, store);
				}
			}
		}
	}

	/**
	 * Fügt die übergebene Datei und alle im "benötigteDateien"-Tag enthaltenen
	 * Dateien zur Liste hinzu, sofern sie noch nicht vorhanden sind. Die
	 * Methode lädt rekursiv alle benötigten Datein nach.
	 * 
	 * @param arrayFiles
	 *            Array mit allen Parameter - IN/OUT Parameter
	 * @param file
	 *            Das File das ggf. hinzugefügt werden soll mit
	 *            "benötigtenDateien".
	 */
	private void loadRegelFile(Configuration props, List<File> arrayFiles,
			File file) {
		final File d3sDataDir = new File(props
				.getProperty(ConfigStore.Key.d3s_data_path));
		final File d3sUserDir = new File(props
				.getProperty(ConfigStore.Key.user_data_path));

		// Wenn das File schon enthalten ist --> return
		if (arrayFiles.contains(file))
			return;

		Element tempElement = XOMHelper.getRootElement(file); // File als XML
		// laden
		if (tempElement == null)
			return; // Konnte nicht geladen werden --> return

		arrayFiles.add(file); // ansonsten File hinzufügen

		tempElement = tempElement.getFirstChildElement("preamble")
				.getFirstChildElement("benoetigtDateien");

		// Falls das File keine weiteren Dateien benötigt --> Return
		if (tempElement == null)
			return;

		Elements elementList = tempElement.getChildElements("datei");

		// Rekursiver Aufruf mit allen von diesem File benötigten Dateien
		for (int i = 0; i < elementList.size(); i++) {
			if (elementList.get(i).getAttribute("ordner").getValue().equals(
					"d3s")) {
				loadRegelFile(props, arrayFiles, new File(d3sDataDir,
						(String) elementList.get(i).getValue()));
			} else { // value = user
				loadRegelFile(props, arrayFiles, new File(d3sUserDir,
						(String) elementList.get(i).getValue()));
			}
		}
	}

	/**
	 * Bildet die übergebenen xml-Elemente in <code>CharElemente</code> ab.
	 * 
	 * @param kategorien
	 *            Alle xml-Elemente der Art "current"
	 * @param current
	 *            Die Art der übergebenen Elemente
	 * @param store
	 *            Store in den die Elemente gespeichert werden sollen
	 */
	private void mapHelpCharKomponents(Elements kategorien,
			CharKomponente current, XOMStore store) {
		final XOMMapper<CharElement> mappy = XOMMappingHelper.instance().chooseXOMMapper(
				current);
		if (mappy != null) {
			for (int iii = 0; iii < kategorien.size(); iii++) {
				final Element child = kategorien.get(iii);
				final String id = child.getAttributeValue("id");
				final CharElement charEl = store.getCharElement(id, current);
				mappy.map(child, charEl);
			}
		}
	}

	/**
	 * Liest die SKT aus dem xml-Element aus. Die SKT MUSS in erwarteter Form
	 * vorliegen (siehe tag (originalSkt)!
	 * 
	 * @param element
	 *            Das xml-Element mit der SKT
	 * @return Eine HashMap mit der SKT
	 * @throws NumberFormatException
	 *             Wenn die Zahlen nicht umgewandelt werden können
	 * @throws NullPointerException
	 *             Wenn nicht 30 Elemente Vorhanden sind
	 */
	private Map<KostenKlasse, Integer[]> loadSkt(Element element)
			throws NumberFormatException, NullPointerException {

		final Map<KostenKlasse, Integer[]> sktMap = new HashMap<KostenKlasse, Integer[]>();

		sktMap.put(KostenKlasse.A, new Integer[32]);
		sktMap.put(KostenKlasse.B, new Integer[32]);
		sktMap.put(KostenKlasse.C, new Integer[32]);
		sktMap.put(KostenKlasse.D, new Integer[32]);
		sktMap.put(KostenKlasse.E, new Integer[32]);
		sktMap.put(KostenKlasse.F, new Integer[32]);
		sktMap.put(KostenKlasse.G, new Integer[32]);
		sktMap.put(KostenKlasse.H, new Integer[32]);

		for (int i = 0; i < 32; i++) {
			Element tmpElement = element.getFirstChildElement("z" + (i));

			sktMap.get(KostenKlasse.A)[i] = Integer.parseInt(tmpElement
					.getAttributeValue("A"));
			sktMap.get(KostenKlasse.B)[i] = Integer.parseInt(tmpElement
					.getAttributeValue("B"));
			sktMap.get(KostenKlasse.C)[i] = Integer.parseInt(tmpElement
					.getAttributeValue("C"));
			sktMap.get(KostenKlasse.D)[i] = Integer.parseInt(tmpElement
					.getAttributeValue("D"));
			sktMap.get(KostenKlasse.E)[i] = Integer.parseInt(tmpElement
					.getAttributeValue("E"));
			sktMap.get(KostenKlasse.F)[i] = Integer.parseInt(tmpElement
					.getAttributeValue("F"));
			sktMap.get(KostenKlasse.G)[i] = Integer.parseInt(tmpElement
					.getAttributeValue("G"));
			sktMap.get(KostenKlasse.H)[i] = Integer.parseInt(tmpElement
					.getAttributeValue("H"));
		}
		return sktMap;
	}
}
