/*
 * Created 21.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.unitTests;

import org.d3s.alricg.common.logic.FormelSammlung;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Vincent
 *
 */
public class FormelSammlungTest {
	
	@Test
	public void testGetApFromGp() {
		Assert.assertEquals(-75, FormelSammlung.getApFromGp(-1.5d));
		Assert.assertEquals(-50, FormelSammlung.getApFromGp(-1));
		Assert.assertEquals(-25, FormelSammlung.getApFromGp(-0.5d));
		Assert.assertEquals(0, FormelSammlung.getApFromGp(0));
		Assert.assertEquals(25, FormelSammlung.getApFromGp(0.5d));
		Assert.assertEquals(50, FormelSammlung.getApFromGp(1));
		Assert.assertEquals(75, FormelSammlung.getApFromGp(1.5d));
		Assert.assertEquals(100, FormelSammlung.getApFromGp(2));
	}

	@Test
	public void testGetGpFromAp() {
		Assert.assertEquals(-1.5d, FormelSammlung.getGpFromAp(-75));
		Assert.assertEquals(-1, FormelSammlung.getGpFromAp(-50));
		Assert.assertEquals(0.5d, FormelSammlung.getGpFromAp(-25));
		Assert.assertEquals(0, FormelSammlung.getGpFromAp(0));
		Assert.assertEquals(0.5d, FormelSammlung.getGpFromAp(25));
		Assert.assertEquals(1, FormelSammlung.getGpFromAp(50));
		Assert.assertEquals(1.5d, FormelSammlung.getGpFromAp(75));
		Assert.assertEquals(2, FormelSammlung.getGpFromAp(100));
	}
}
