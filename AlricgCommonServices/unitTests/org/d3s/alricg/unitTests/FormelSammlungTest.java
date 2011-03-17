/*
 * Created 21.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.d3s.alricg.common.logic.FormelSammlung;
import org.d3s.alricg.common.logic.FormelSammlung.Lernmethode;
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * @author Vincent
 *
 */
public class FormelSammlungTest {
	
	@BeforeClass 
	public static void startTestClass() throws JAXBException, ParserConfigurationException, SAXException, IOException {
		// Lade die SKT und Regeln
		StoreAccessor.getInstance().loadRegelConfig();
	}
	
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
		Assert.assertEquals(-1.5d, FormelSammlung.getGpFromAp(-75),0);
		Assert.assertEquals(-1, FormelSammlung.getGpFromAp(-50),0);
		Assert.assertEquals(-0.5d, FormelSammlung.getGpFromAp(-25),0);
		Assert.assertEquals(0, FormelSammlung.getGpFromAp(0),0);
		Assert.assertEquals(0.5d, FormelSammlung.getGpFromAp(25),0);
		Assert.assertEquals(1, FormelSammlung.getGpFromAp(50),0);
		Assert.assertEquals(1.5d, FormelSammlung.getGpFromAp(75),0);
		Assert.assertEquals(2, FormelSammlung.getGpFromAp(100),0);
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
	
	/**
	 * Testet die original SKT, indem alle Stufen bis 30 Addiert werden und mit der
	 * Summen-SKT verglichen werden 
	 */
	@Test 
	public void testGetSktWert() {
		int a = 0, b = 0, c = 0, d = 0, e = 0, f = 0, g = 0, h = 0;
		
		for (int i = 1; i < 31; i++) {
			a += FormelSammlung.getSktWert(KostenKlasse.A, i);
			b += FormelSammlung.getSktWert(KostenKlasse.B, i);
			c += FormelSammlung.getSktWert(KostenKlasse.C, i);
			d += FormelSammlung.getSktWert(KostenKlasse.D, i);
			e += FormelSammlung.getSktWert(KostenKlasse.E, i);
			f += FormelSammlung.getSktWert(KostenKlasse.F, i);
			g += FormelSammlung.getSktWert(KostenKlasse.G, i);
			h += FormelSammlung.getSktWert(KostenKlasse.H, i);
		}
		
		Assert.assertEquals(670, a);
		Assert.assertEquals(1341, b);
		Assert.assertEquals(2000, c);
		Assert.assertEquals(2675, d);
		Assert.assertEquals(3355, e);
		Assert.assertEquals(5030, f);
		Assert.assertEquals(6713, g);
		Assert.assertEquals(13386, h);
	}

	/*
	 * Testet die Kosten für den Abbau von Nachteilen anhand von 2 Beispielen
	 */
	@Test
	public void testBerechneNachteilAbbauen() {
		assertEquals(700, FormelSammlung.berechneNachteilAbbauen(-7));
		assertEquals(500, FormelSammlung.berechneNachteilAbbauen(-5));
	}

	/*
	 * Testet das senken von schlechten Eigenschaften anhand von 2 Beispielen
	 */
	@Test
	public void testberechneSchEigSenkenKosten() {
		assertEquals(430, FormelSammlung.berechneSchEigSenkenKosten(7, 5));
		assertEquals(1130, FormelSammlung.berechneSchEigSenkenKosten(4, 0));
	}

	/*
	 * Class under test for int berechneSktKosten(int, int, int, Lernmethode, KostenKlasse, boolean)
	 * Testet Arten von Berechnungen auf der SKT. 
	 */
	@Test
	public void testBerechneSktKosten() {

		// Aktivierung 
		assertEquals(17, FormelSammlung.berechneSktKosten(
				CharElement.KEIN_WERT, 0, 7, Lernmethode.lehrmeister,
				KostenKlasse.B, true));

		// Negative Talente steigern
		assertEquals(34, FormelSammlung.berechneSktKosten(-3, -1, 5,
				Lernmethode.spezielleErfahrung, KostenKlasse.D, true));

		// Negativ und positiv steigern
		assertEquals(97, FormelSammlung.berechneSktKosten(-3, 2, 5,
				Lernmethode.selbstStudium, KostenKlasse.D, true));

		// Stufe über 31 steigern
		assertEquals(18386, FormelSammlung.berechneSktKosten(0, 35, 5,
				Lernmethode.lehrmeister, KostenKlasse.H, false));

		// Talent mit selbststudium und Stufe über 10 --> ab 11 zusätzlich 1 Kategorien nach oben
		assertEquals(845, FormelSammlung.berechneSktKosten(8, 14, 5,
				Lernmethode.selbstStudium, KostenKlasse.E, true));

		// Aktivierung bei der Generierung (HeldenStufe = 0)
		assertEquals(28, FormelSammlung.berechneSktKosten(
				CharElement.KEIN_WERT, 2, 0, Lernmethode.lehrmeister,
				KostenKlasse.F, true));

		// Steigern mit Kateorgie über H hinaus (=H)
		assertEquals(415, FormelSammlung.berechneSktKosten(4, 7, 15,
				Lernmethode.selbstStudium, KostenKlasse.H, true));

		// Steigern mit Kateorgie A* 
		assertEquals(2, FormelSammlung.berechneSktKosten(0, 2, 15,
				Lernmethode.spezielleErfahrung, KostenKlasse.A, false));
		assertEquals(28, FormelSammlung.berechneSktKosten(5, 9, 15,
				Lernmethode.spezielleErfahrung, KostenKlasse.A, false));

		//	"normales" steigern 
		assertEquals(29, FormelSammlung.berechneSktKosten(1, 5, 15,
				Lernmethode.spezielleErfahrung, KostenKlasse.C, false));
		assertEquals(235, FormelSammlung.berechneSktKosten(10, 13, 15,
				Lernmethode.sehrGuterLehrmeister, KostenKlasse.F, true));
		assertEquals(144, FormelSammlung.berechneSktKosten(5, 9, 15,
				Lernmethode.lehrmeister, KostenKlasse.D, false));
	}

	@Test
	public void testBerechneMR() {
		assertEquals(7, // 6,6 = 7
				FormelSammlung.berechneMR(10, 11, 12));
		assertEquals(6, // 6,4 = 6
				FormelSammlung.berechneMR(10, 11, 11));
	}

	@Test
	public void testBerechneINI() {
		assertEquals(9, // 8,6 = 9
				FormelSammlung.berechneINI(10, 11, 12));
		assertEquals(8, // 8,4 = 8
				FormelSammlung.berechneINI(10, 11, 11));
	}

	@Test
	public void testBerechneAtBasis() {
		assertEquals(7, // 6,6 = 7
				FormelSammlung.berechneAtBasis(10, 11, 12));
		assertEquals(6, // 6,4 = 8
				FormelSammlung.berechneAtBasis(10, 11, 11));
	}

	@Test
	public void testBerechnePaBasis() {
		assertEquals(7, // 6,6 = 7
				FormelSammlung.berechnePaBasis(10, 11, 12));
		assertEquals(6, // 6,4 = 8
				FormelSammlung.berechnePaBasis(10, 11, 11));
	}

	@Test
	public void testBerechneFkBasis() {
		assertEquals(7, // 6,6 = 7
				FormelSammlung.berechneFkBasis(10, 11, 12));
		assertEquals(6, // 6,4 = 8
				FormelSammlung.berechneFkBasis(10, 11, 11));
	}

	@Test
	public void testBerechneLep() {
		assertEquals(16, // 15,5 = 16
				FormelSammlung.berechneLep(10, 11));
		assertEquals(15, // 15
				FormelSammlung.berechneLep(10, 10));
	}

	@Test
	public void testBerechneAup() {
		assertEquals(17, // 16,5 = 17
				FormelSammlung.berechneAup(10, 11, 12));
		assertEquals(16, // 16
				FormelSammlung.berechneAup(10, 11, 11));
	}

	@Test
	public void testBerechneAsp() {
		assertEquals(17, // 16,5 = 17
				FormelSammlung.berechneAsp(10, 11, 12));
		assertEquals(16, // 16
				FormelSammlung.berechneAsp(10, 11, 11));
	}
}
