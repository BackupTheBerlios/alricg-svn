/*
 * Created 22. September 2005 / 00:01:02
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store;

import junit.framework.TestCase;

import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;

/**
 * Tests für den FactoryFinder.
 * 
 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
 */
public class FactoryFinderTest extends TestCase {

    public FactoryFinderTest() {
        super();
    }

    public FactoryFinderTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        ProgAdmin.messenger = new MessengerMock();
        FactoryFinder.reset();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFindUninitialized() {
        try {
            FactoryFinder.find();
            fail("NullPointerException expected.");
        } catch (Throwable t) {
            assertTrue("Unexpected error instance.", t instanceof NullPointerException);
            assertEquals("Unexpected error message.", "AbstractStoreFactory is not initialised!", t.getMessage());
        }
    }

    public void testInitAndFind() {
        try {
            FactoryFinder.init();
            assertNotNull(FactoryFinder.find());
            assertNotNull(FactoryFinder.find().getConfiguration());
            assertNotNull(FactoryFinder.find().getLibrary());
            assertNotNull(FactoryFinder.find().getData());
        } catch (Throwable t) {
            assertTrue("Unexpected error instance.", t instanceof ConfigurationException);
            assertEquals("Unexpected error message.", "AbstractStoreFactory instantiation failed!", t.getMessage());
            fail("No exception expected.");
        }
    }

    public void testReset() {
        try {
            FactoryFinder.init();
        } catch (Throwable t) {
            fail("No exception expected.");
        }

        try {
            FactoryFinder.reset();
            FactoryFinder.find();
            fail("NullPointerException expected.");
        } catch (Throwable t) {
            assertTrue("Unexpected error instance.", t instanceof NullPointerException);
            assertEquals("Unexpected error message.", "AbstractStoreFactory is not initialised!", t.getMessage());
        }

    }

}
