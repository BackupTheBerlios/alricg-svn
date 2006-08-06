/*
 * Created on 23.06.2005 / 15:16:17
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store.xom.map;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import nu.xom.Element;
import nu.xom.Elements;

import org.d3s.alricg.controller.Messenger;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.ConfigStore;
import org.d3s.alricg.store.Configuration;
import org.d3s.alricg.store.ConfigurationException;
import org.d3s.alricg.store.xom.XOMHelper;

/**
 * Mapper für lokalisierbare Texte.
 * 
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
public class XOMToLibraryMapper {

    /** <code>XOMToLibraryMapper</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMToLibraryMapper.class.getName());
    
    /** Sprche der Library */
    private String language;

    /** 
     * Gibt die Sprache der Library zurück.
     * <p>
     * Erst nach Aufruf von <code>readData(Configuration)</code> leifert diese Mehtode einen sinnvollen Wert.
     * </p>
     * @return Die Sprache der Library.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Erstellt eine Liste von Text-Tabellen auf Basis der Daten in <code>props</code> und gibt diese zurück.
     * <p>
     * Die Reihenfolge der Tabellen in der Liste:
     * <ol>
     * <li>kurze Texte</li>
     * <li>mitellange Texte</li>
     * <li>lange Texte</li>
     * <li>Fehlertexte</li>
     * <li>Tooltiptexte</li>
     * </p>
     * 
     * @param props Die Konfiguration mit den benötigten Einstellungen
     * @return Liste mit den Text-Tabellen
     * @throws ConfigurationException Falls die Konfiguration fehlerhaft ist.
     */
    public List<Map<String, String>> readData(Configuration props) throws ConfigurationException {

        try {
            final Element configRoot = XOMHelper.getRootElementNoLog(new File(props
                    .getProperty(ConfigStore.Key.config_file)));
            language = configRoot.getFirstChildElement("library").getAttributeValue("lang");
            final String d3sLibDir = props.getProperty(ConfigStore.Key.d3s_library_path);
            final String fileName = configRoot.getFirstChildElement("library").getAttribute("file").getValue();
            final Element element = XOMHelper.getRootElementNoLog(new File(d3sLibDir, fileName));
            if (element != null) {

                final Element xmlElement = element.getFirstChildElement("library");
                final List<String> messages = new ArrayList<String>();
                final List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
                maps.add(read(language, xmlElement.getFirstChildElement("short"), messages));
                maps.add(read(language, xmlElement.getFirstChildElement("middle"), messages));
                maps.add(read(language, xmlElement.getFirstChildElement("long"), messages));
                maps.add(read(language, xmlElement.getFirstChildElement("errorMsg"), messages));
                maps.add(read(language, xmlElement.getFirstChildElement("toolTip"), messages));

                if (!messages.isEmpty()) {
                    LOG.warning("Library-Entries für die Sprache " + language + " mit den keys '" + messages.toString()
                            + "'" + "konnten nicht gefunden werden.");

                    ProgAdmin.messenger.showMessage(Messenger.Level.fehler,
                            "Es konnten nicht alle Texte geladen werden! \n"
                                    + "Dies ist für eine fehlerfreie Anzeige jedoch notwendig. \n"
                                    + "Bitte stellen sie sicher das die 'library' Datei \n"
                                    + "im Originalzustand vorliegt. \n" + "\n"
                                    + "Das Programm wird wahrscheinlich nur fehlerhaft funktionieren.");
                }
                return maps;
            } else {

                // Fehler ist aufgetreten, Programm wird geschlossen!
                LOG.log(Level.SEVERE, "Library Datei (" + d3sLibDir + fileName
                        + ") konnte nicht geladen werden. Programm beendet.");

                ProgAdmin.messenger.showMessage(Messenger.Level.fehler, "Die 'library' Datei \n" + d3sLibDir + fileName
                        + "\nkonnte nicht geladen werden! Bitte überprüfen sie ob die Datei \n"
                        + "zugriffsbereit ist und im Orginalzustand vorliegt. \n" + "\n"
                        + "Das Programm kann ohne diese Datei nicht gestartet werden \n"
                        + "und wird wieder geschlossen!");
                System.exit(1);
                return null;
            }
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }

    /**
     * Liest die Texte einer Sprache aus eine xml-Element und gibt diese als <code>Map</code> zurück. Mögliche
     * Benachrichtigungen an den Aufrufer werden in <code>msg</code> gespeichert und können von diesem nach Ende der
     * Methode weiterverarbeitet werden.
     * 
     * @param language Die gewünschte Sprache
     * @param xmlElement Das xml-Element mit den Texten
     * @param msg Eine Liste mit Nachrichten für den Anwender.
     * @return Eine Tabelle mit den Texten in der gewünschten Sprache.
     */
    private Map<String, String> read(String language, Element xmlElement, List<String> msg) {
        final Map<String, String> result = new HashMap<String, String>();
        final Elements xmlChildren = xmlElement.getChildElements();
        for (int i = 0; i < xmlChildren.size(); i++) {
            boolean entryExists = false;
            final Elements xmlEntrys = xmlChildren.get(i).getChildElements();

            // richtige Sprache suchen
            for (int ii = 0; ii < xmlEntrys.size(); ii++) {
                if (xmlEntrys.get(ii).getAttributeValue("lang").equals(language)) {
                    entryExists = true;
                    result.put(xmlChildren.get(i).getAttributeValue("key"), xmlEntrys.get(ii).getValue());
                    break;
                }
            }

            if (!entryExists) {
                msg.add(xmlElement.getLocalName() + " - " + xmlChildren.get(i).getAttributeValue("key"));
            }
        }
        return result;
    }

}
