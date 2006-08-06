/*
 * Created on 14.10.2005 / 00:37:26
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten.charZusatz;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * TestSuite für alle org.d3s.alricg.charKomponenten.charZusatz Tests.
 * 
 * @author V. Strelow
 */
public class AllTests extends TestCase {

    public static Test suite() {
        TestSuite testSuite = new TestSuite("All Tests for org.d3s.alricg.charKomponenten.charZusatz");
        testSuite.addTestSuite(WuerfelSammlungTest.class);
        return testSuite;
    }

}
