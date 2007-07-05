/*
 * Created on 23.06.2005 / 15:16:17
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store.xom;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.d3s.alricg.controller.Messenger;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.TextStore;

/**
 * Helperklasse für das xom-Frameworks.
 * 
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
public class XOMHelper {
    
    /** <code>XOMHelper</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMHelper.class.getName());

    /**
     * Privater Konstruktor, da nur Helper-Stereotyp.
     */
    private XOMHelper() {

    }

    /**
     * Liest ein XML-File ein und gibt das RootElement zurück. Fehler werden nicht geloggt und nicht gemeldet.
     * 
     * @see #getRootElement(File)
     * @param xmlFile Das File das eingelesen werden soll
     * @return Das rootElement des XML-Files oder null, falls die Datei nicht geladen werden konnte!
     */
    public static Element getRootElementNoLog(File xmlFile) throws Exception {

        final Builder parser = new Builder(); // Auf true setzen für Validierung
        final Document doc = parser.build(xmlFile);
        return doc.getRootElement();
    }

    /**
     * Liest ein XML-File ein und gibt das RootElement zurück.
     * 
     * @param xmlFile Das File das eingelesen werden soll
     * @return Das rootElement des XML-Files oder null, falls die Datei nicht geladen werden konnte!
     */
    public static Element getRootElement(File xmlFile) {

        TextStore lib = null;
        try {
            lib = FactoryFinder.find().getLibrary();
            final Builder parser = new Builder();// Auf true setzen für Validierung
            final Document doc = parser.build(xmlFile);
            return doc.getRootElement();

        } catch (ValidityException ex) {
            LOG.severe(ex.getMessage());
            ProgAdmin.messenger.showMessage(Messenger.Level.fehler, lib.getErrorTxt("Fehlerhafte Datei") + "\n" + "  "
                    + xmlFile.getName() + "\n" + lib.getErrorTxt("XML Validierungsfehler"));

        } catch (ParsingException ex) {
            LOG.severe(ex.getMessage());
            ProgAdmin.messenger.showMessage(Messenger.Level.fehler, lib.getErrorTxt("Fehlerhafte Datei") + "\n" + "  "
                    + xmlFile.getName() + "\n" + lib.getErrorTxt("XML Parsingfehler"));

        } catch (IOException ex) {
            if (!xmlFile.exists()) {
                LOG.severe(ex.getMessage());
                ProgAdmin.messenger.showMessage(Messenger.Level.fehler, lib.getErrorTxt("Fehlerhafte Datei") + "\n"
                        + "  " + xmlFile.getName() + "\n" + lib.getErrorTxt("Datei existiert nicht Fehler"));
            } else if (!xmlFile.canRead()) {
                LOG.severe(ex.getMessage());
                ProgAdmin.messenger.showMessage(Messenger.Level.fehler, lib.getErrorTxt("Fehlerhafte Datei") + "\n"
                        + "  " + xmlFile.getName() + "\n" + lib.getErrorTxt("Datei nicht lesbar Fehler"));
            } else {
                LOG.severe(ex.getMessage());
                ProgAdmin.messenger.showMessage(Messenger.Level.fehler, lib.getErrorTxt("Fehlerhafte Datei") + "\n"
                        + "  " + xmlFile.getName() + "\n" + lib.getErrorTxt("Datei nicht lesbar/unbekannt Fehler"));
            }
        }
        return null;
    }
}
