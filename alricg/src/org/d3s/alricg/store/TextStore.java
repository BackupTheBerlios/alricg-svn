/*
 * Created on 15.06.2005 / 12:13:14
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store;

/**
 * Abstraktion lokalisierbarer Texte von alricg.
 * 
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
public interface TextStore {

	/**
	 * Gibt die Sprache des <cocde>TestStore</code>'s zurück.
	 */
	String getLanguage();
	
    /**
     * Gibt einen zu <code>key</code> gehörigen, kurzen Text (ein Wort) zurück.
     * @param key Der Schlüssel des gesuchten Textes
     * @return Der zu <code>key</code> gehörige, kurze Text.
     */
    String getShortTxt(String key);

    /**
     * Gibt einen zu <code>key</code> gehörigen, mittellangen Text (zwei bis drei Worte) zurück.
     * @param key Der Schlüssel des gesuchten Textes
     * @return Der zu <code>key</code> gehörige, mitellange Text.
     */
    String getMiddleTxt(String key);

    /**
     * Gibt einen zu <code>key</code> gehörigen, langen Text zurück.
     * @param key Der Schlüssel des gesuchten Textes
     * @return Der zu <code>key</code> gehörige, langen Text.
     */
    String getLongTxt(String key);

    /**
     * Gibt einen zu <code>key</code> gehörigen, Fehlertext zurück.
     * @param key Der Schlüssel des gesuchten Fehlertextes
     * @return Der zu <code>key</code> gehörige, Fehlertext.
     */
    String getErrorTxt(String key);

    /**
     * Gibt einen zu <code>key</code> gehörigen, Tooltip-Text zurück.
     * @param key Der Schlüssel des gesuchten Tooltip-Text
     * @return Der zu <code>key</code> gehörige, Tooltip-Text.
     */
    String getToolTipTxt(String key);

}
