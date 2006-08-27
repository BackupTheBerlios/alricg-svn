/*
 * Created 22. September 2005 / 00:01:02
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.d3s.alricg.controller.MessengerMock;
import org.d3s.alricg.controller.ProgAdmin;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests für den FactoryFinder.
 * 
 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
 */
public class FactoryFinderTest {


    @Before public void setUp() throws Exception {
        ProgAdmin.messenger = new MessengerMock();
        FactoryFinder.reset();
    }

    @Test public void testFindUninitialized() {
        try {
            FactoryFinder.find();
            fail("NullPointerException expected.");
        } catch (Throwable t) {
            assertTrue("Unexpected error instance.", t instanceof NullPointerException);
            assertEquals("Unexpected error message.", "AbstractStoreFactory is not initialised!", t.getMessage());
        }
    }

    @Test public void testInitAndFind() {
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

    @Test public void testReset() {
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
