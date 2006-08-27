/*
 * Created on 09.10.2005 / 11:20:04
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.charKomponenten.charZusatz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * <b>Beschreibung:</b>
 * JUnit-Test für die Klasse "WuerfelSammlung"
 * 
 * @author V.Strelow
 */
public class WuerfelSammlungTest {
	private WuerfelSammlung w0;
	private WuerfelSammlung w1;
	private WuerfelSammlung w2;
	private WuerfelSammlung w3;
 	
	@Before public void setUp() {
		
		//Würfel mit 0 + 1W6
		w0 = new WuerfelSammlung(0, new Integer[] {1}, new Integer[] {6});
		
		// Würfel mit 2 + 2W6 + 2W20
		w1 = new WuerfelSammlung(2, new Integer[] {2,2}, new Integer[] {6,20});
		
		// Würfel mit 2 + 3W3 + 3W4
		w2 = new WuerfelSammlung(2, new Integer[] {3,3}, new Integer[] {3,4});
		
		// Würfel mit 2 + 1W100 + 3W10
		w3 = new WuerfelSammlung(2, new Integer[] {1,3}, new Integer[] {100,10});
		
	}


	/**
	 * Testet den maximal erreichbaren wert einer Würfelsammlung
	 */
	@Test public void testMaxWert() {
		assertEquals(6, w0.getMaxWurf());
		assertEquals(54, w1.getMaxWurf());
		assertEquals(23, w2.getMaxWurf());
		assertEquals(132, w3.getMaxWurf());
	}
	
	/**
	 * Testet den minimal erreichbaren wert einer Würfelsammlung
	 */
	@Test public void testMinWert() {
		assertEquals(1, w0.getMinWurf());
		assertEquals(6, w1.getMinWurf());
		assertEquals(8, w2.getMinWurf());
		assertEquals(6, w3.getMinWurf());
	}
	
	/**
	 * Testet ob die Zufallszahlen auch in den Grenzen liegen
	 */
	@Test public void testWuerfelwurf() {

		assertTrue(w0.getMinWurf() <= w0.getWuerfelWurf()
				&& w0.getWuerfelWurf() <= w0.getMaxWurf());
		assertTrue(w1.getMinWurf() <= w1.getWuerfelWurf()
				&& w1.getWuerfelWurf() <= w1.getMaxWurf());
		assertTrue(w2.getMinWurf() <= w2.getWuerfelWurf()
				&& w2.getWuerfelWurf() <= w2.getMaxWurf());
		assertTrue(w3.getMinWurf() <= w3.getWuerfelWurf()
				&& w3.getWuerfelWurf() <= w3.getMaxWurf());
	}
}