/*
 * Created 17.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.unitTests;

import org.d3s.alricg.store.access.StoreAccessor;
import org.junit.Test;

/**
 * @author Vincent
 *
 */
public class LoadSaveToFiles {

	@Test
	public void testLoad() throws Exception {
		StoreAccessor storeAcc = StoreAccessor.getIntance();
		storeAcc.loadFiles();
	}
}
