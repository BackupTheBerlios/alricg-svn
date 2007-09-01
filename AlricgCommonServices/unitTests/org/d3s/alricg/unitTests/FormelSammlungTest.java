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
	
	@Test
	public void testGetWertInMuenzen() {
		int[] tmpArray;
		
		tmpArray = FormelSammlung.getWertInMuenzen(1234567);
		Assert.assertEquals(7, tmpArray[0]);
		Assert.assertEquals(6, tmpArray[1]);
		Assert.assertEquals(5, tmpArray[2]);
		Assert.assertEquals(1234, tmpArray[3]);
		Assert.assertEquals(1234567, FormelSammlung.getWertInKreuzern(tmpArray));
		
		tmpArray = FormelSammlung.getWertInMuenzen(12);
		Assert.assertEquals(2, tmpArray[0]);
		Assert.assertEquals(1, tmpArray[1]);
		Assert.assertEquals(0, tmpArray[2]);
		Assert.assertEquals(0, tmpArray[3]);
		Assert.assertEquals(12, FormelSammlung.getWertInKreuzern(tmpArray));
		
		tmpArray = FormelSammlung.getWertInMuenzen(0);
		Assert.assertEquals(0, tmpArray[0]);
		Assert.assertEquals(0, tmpArray[1]);
		Assert.assertEquals(0, tmpArray[2]);
		Assert.assertEquals(0, tmpArray[3]);
		Assert.assertEquals(0, FormelSammlung.getWertInKreuzern(tmpArray));
		
		tmpArray = FormelSammlung.getWertInMuenzen(120);
		Assert.assertEquals(0, tmpArray[0]);
		Assert.assertEquals(2, tmpArray[1]);
		Assert.assertEquals(1, tmpArray[2]);
		Assert.assertEquals(0, tmpArray[3]);
		Assert.assertEquals(120, FormelSammlung.getWertInKreuzern(tmpArray));
	}
}
