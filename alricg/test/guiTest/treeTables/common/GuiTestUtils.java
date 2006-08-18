/*
 * Created on 13.08.2006
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".*
 */
package guiTest.treeTables.common;

import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.store.ConfigurationException;
import org.d3s.alricg.store.DataStore;
import org.d3s.alricg.store.FactoryFinder;

/**
 * @author Vincent
 *
 */
public class GuiTestUtils {

	
	public static Held initData() {
		Held held = new Held();

		
        // initialisieren
        try {
	        ProgAdmin.main(new String[] { "noScreen" });
			
	        FactoryFinder.init();
	        held = new Held();
	        
	        held.initGenrierung();
		
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		
		return held;
	}
	
}
