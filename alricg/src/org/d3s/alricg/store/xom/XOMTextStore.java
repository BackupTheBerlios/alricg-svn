/*
 * Created on 20.06.2005 / 13:14:15
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store.xom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.d3s.alricg.store.Configuration;
import org.d3s.alricg.store.ConfigurationException;
import org.d3s.alricg.store.TextStore;
import org.d3s.alricg.store.xom.map.XOMToLibraryMapper;

/**
 * <code>TextStore</code> auf Basis des xom-Frameworks
 * 
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
public class XOMTextStore implements TextStore {

    /** Sprache */
    private String lang;

    /** Kurze Texte */
    private final Map<String, String> shortTxt;

    /** Mittellange Texte */
    private final Map<String, String> middleTxt;

    /** Lange Texte */
    private final Map<String, String> longTxt;

    /** Fehlertexte */
    private final Map<String, String> errorMsgTxt;

    /** Tooltiptexte */
    private final Map<String, String> toolTipTxt;

    XOMTextStore() {
        this("", new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<String, String>(),
                new HashMap<String, String>(), new HashMap<String, String>());
    }

    /**
     * Erzeugt eine neue Instanz mit den angebenenen Tabellen
     * 
     * @param language Die Sprache
     * @param shortT kurze Texte
     * @param middleT mitellange Texte
     * @param longT lange Texte
     * @param errT Fehlertexte
     * @param ttT ToolTips
     */
    private XOMTextStore(String language, Map<String, String> shortT, Map<String, String> middleT,
            Map<String, String> longT, Map<String, String> errT, Map<String, String> ttT) {

        lang = language;
        shortTxt = shortT;
        middleTxt = middleT;
        longTxt = longT;
        errorMsgTxt = errT;
        toolTipTxt = ttT;
    }

    // @see org.d3s.alricg.store.TextStore#getShortTxt(java.lang.String)
    public String getShortTxt(String key) {
        assert shortTxt.get(key) != null;
        return shortTxt.get(key);
    }

    // @see org.d3s.alricg.store.TextStore#getMiddleTxt(java.lang.String)
    public String getMiddleTxt(String key) {
        assert middleTxt.get(key) != null;
        return middleTxt.get(key);
    }

    // @see org.d3s.alricg.store.TextStore#getLongTxt(java.lang.String)
    public String getLongTxt(String key) {
        assert longTxt.get(key) != null;
        return longTxt.get(key);
    }

    // @see org.d3s.alricg.store.TextStore#getErrorTxt(java.lang.String)
    public String getErrorTxt(String key) {
        assert errorMsgTxt.get(key) != null;
        return errorMsgTxt.get(key);
    }

    // @see org.d3s.alricg.store.TextStore#getToolTipTxt(java.lang.String)
    public String getToolTipTxt(String key) {
        assert toolTipTxt.get(key) != null;
        return toolTipTxt.get(key);
    }
    
    // @see org.d3s.alricg.store.TextStore#getLanguage()
    public String getLanguage() {
    	return lang;
    }

    void init(Configuration config) throws ConfigurationException {
        final XOMToLibraryMapper mappy = new XOMToLibraryMapper();
        final List<Map<String, String>> maps = mappy.readData(config);
        lang = mappy.getLanguage();
        shortTxt.putAll(maps.get(0));
        middleTxt.putAll(maps.get(1));
        longTxt.putAll(maps.get(2));
        errorMsgTxt.putAll(maps.get(3));
        toolTipTxt.putAll(maps.get(4));
    }

}
