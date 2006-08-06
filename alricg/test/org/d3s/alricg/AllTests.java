/*
 * Created on 27.09.2005 / 00:52:11
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg;

import org.d3s.alricg.controller.ProgAdmin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * TestSuite für alle org.d3s.alrig Tests.
 * 
 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
 */
public class AllTests extends TestCase {

    public static Test suite() {
    	
        // Starten des Programms
        ProgAdmin.main(new String[] { "noScreen" });
        
        // Die einzelnen Tests
        TestSuite testSuite = new TestSuite("All Tests for Alricg");
        testSuite.addTest(org.d3s.alricg.sonderregeln.AllTests.suite());
        testSuite.addTest(org.d3s.alricg.prozessor.AllTests.suite());
        testSuite.addTest(org.d3s.alricg.prozessor.generierung.AllTests.suite());
        testSuite.addTest(org.d3s.alricg.charKomponenten.charZusatz.AllTests.suite());
        
        
        testSuite.addTest(org.d3s.alricg.store.AllTests.suite());
        testSuite.addTest(org.d3s.alricg.store.xom.AllTests.suite());
        testSuite.addTest(org.d3s.alricg.store.xom.map.AllTests.suite());
        
        return testSuite;
    }

}
