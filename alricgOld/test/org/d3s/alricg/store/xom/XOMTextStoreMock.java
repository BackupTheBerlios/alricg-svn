/*
 * Created on 10.10.2005 / 09:57:45
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.xom;

import org.d3s.alricg.store.Configuration;
import org.d3s.alricg.store.TextStore;

public class XOMTextStoreMock implements TextStore {


	public String getLanguage() {
		return "DE";
	}

	public String getShortTxt(String key) {
		return "short Text";
	}

	public String getMiddleTxt(String key) {
		return "medium text";
	}

	public String getLongTxt(String key) {
		return "long text";
	}

	public String getErrorTxt(String key) {
		return "error text";
	}

	public String getToolTipTxt(String key) {
		return "tooltip text";
	}
	
	void init(Configuration config) {
		// nothing to do...
	}
}
