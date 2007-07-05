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
	 * Gibt die Sprache des <cocde>TestStore</code>'s zur�ck.
	 */
	String getLanguage();
	
    /**
     * Gibt einen zu <code>key</code> geh�rigen, kurzen Text (ein Wort) zur�ck.
     * @param key Der Schl�ssel des gesuchten Textes
     * @return Der zu <code>key</code> geh�rige, kurze Text.
     */
    String getShortTxt(String key);

    /**
     * Gibt einen zu <code>key</code> geh�rigen, mittellangen Text (zwei bis drei Worte) zur�ck.
     * @param key Der Schl�ssel des gesuchten Textes
     * @return Der zu <code>key</code> geh�rige, mitellange Text.
     */
    String getMiddleTxt(String key);

    /**
     * Gibt einen zu <code>key</code> geh�rigen, langen Text zur�ck.
     * @param key Der Schl�ssel des gesuchten Textes
     * @return Der zu <code>key</code> geh�rige, langen Text.
     */
    String getLongTxt(String key);

    /**
     * Gibt einen zu <code>key</code> geh�rigen, Fehlertext zur�ck.
     * @param key Der Schl�ssel des gesuchten Fehlertextes
     * @return Der zu <code>key</code> geh�rige, Fehlertext.
     */
    String getErrorTxt(String key);

    /**
     * Gibt einen zu <code>key</code> geh�rigen, Tooltip-Text zur�ck.
     * @param key Der Schl�ssel des gesuchten Tooltip-Text
     * @return Der zu <code>key</code> geh�rige, Tooltip-Text.
     */
    String getToolTipTxt(String key);

}
