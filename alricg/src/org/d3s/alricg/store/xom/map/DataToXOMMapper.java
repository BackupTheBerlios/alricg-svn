/*
 * Created 22. September 2005 / 00:01:02
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.xom.map;

import java.util.Iterator;
import java.util.List;

import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.store.xom.XOMStore;

/**
 * Mapper für die <code>CharElemente</code>.
 * 
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
public class DataToXOMMapper {

    /**
     * Speichert einen <code>dataStore</code> auf Basis der Einstellungen in <code>props</code>.
     * 
     * @param ids Die (unsortierte) Liste mit den <code>CharElement</code>-IDs.
     * @param dataStore Der zu schriebende Datenspeicher.
     * @return das Root-Element des xml-Baums.
     */
    public Element transformData(List<String> ids, XOMStore dataStore) {

        // TODO Die Liste mit den ids in die richtige, d.h. zum Schema passende Reihenfolge bringen.

        // TODO xml header etc. schreiben
        final Element root = new Element("alricgXML");

        Element current = root;
        XOMMapper<CharElement> mappy = null;

        // Die CharElemente zu den ids holen und über ihre XOMMapper persistieren.
        for (Iterator<String> ii = ids.iterator(); ii.hasNext();) {
            final String id = ii.next();
            final CharKomponente charKomp = dataStore.getCharKompFromId(id);

            // TODO is this ok (1)?
            if (charKomp == CharKomponente.eigenschaft || charKomp == CharKomponente.sonderregel
                    || charKomp == CharKomponente.rasseVariante || charKomp == CharKomponente.kulturVariante
                    || charKomp == CharKomponente.professionVariante) {
                continue; // Diese CharElemente werden nicht (hier) geschrieben, daher Abbruch
            }

            // TODO is this ok (2)?
            if (root.getFirstChildElement(charKomp.getKategorie()) == null) {
                mappy = XOMMappingHelper.instance().chooseXOMMapper(charKomp);
                current = new Element(charKomp.getKategorie());
                root.appendChild(current);
            }
            final CharElement charElement = dataStore.getCharElement(id, charKomp);
            final Element xmlElement = new Element("tempName");
            mappy.map(charElement, xmlElement);
            current.appendChild(xmlElement);
        }

        return root;
    }
}
