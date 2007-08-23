/*
 * Created 20.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.unitTests;

import java.util.ArrayList;
import java.util.List;

import org.d3s.alricg.common.CharElementTextService;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.OptionVoraussetzung;
import org.d3s.alricg.store.charElemente.links.Voraussetzung;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Vincent
 */
public class CharElementTextServiceTest {
	private Eigenschaft eigMU, eigKL, eigIN;
	private Talent tal1, tal2, tal3;
	
	@Before
	public void init() {
		eigMU = new Eigenschaft();
		eigMU.setEigenschaftEnum(EigenschaftEnum.MU);
	
		eigKL = new Eigenschaft();
		eigKL.setEigenschaftEnum(EigenschaftEnum.KL);
		
		eigIN = new Eigenschaft();
		eigIN.setEigenschaftEnum(EigenschaftEnum.IN);
		
		Eigenschaft[] eigArray = new Eigenschaft[]{eigMU, eigKL, eigIN};
		
		tal1 = new Talent();
		tal1.setDreiEigenschaften(eigArray);
		tal1.setArt(Talent.Art.basis);
		tal1.setSorte(Talent.Sorte.kampf);
		tal1.setKostenKlasse(KostenKlasse.A);
		tal1.setName("Talent 1");
		tal1.setId("Talent-1");
		
		tal2 = new Talent();
		tal2.setDreiEigenschaften(eigArray);
		tal2.setArt(Talent.Art.spezial);
		tal2.setSorte(Talent.Sorte.gesellschaft);
		tal2.setKostenKlasse(KostenKlasse.B);
		tal2.setName("Talent 2");
		tal2.setId("Talent-2");
		
		tal3 = new Talent();
		tal3.setDreiEigenschaften(eigArray);
		tal3.setArt(Talent.Art.beruf);
		tal3.setSorte(Talent.Sorte.natur);
		tal3.setKostenKlasse(KostenKlasse.C);
		tal3.setName("Talent 3");
		tal3.setId("Talent-3");
		
	}
	
	@Test
	public void testgetVoraussetzungsText() {
		// Init
		Voraussetzung voraus = new Voraussetzung();
		List<OptionVoraussetzung> posList = new ArrayList<OptionVoraussetzung>();
		List<OptionVoraussetzung> negList = new ArrayList<OptionVoraussetzung>();
		voraus.setPosVoraussetzung(posList);
		voraus.setNegVoraussetzung(negList);
		
		// Einfache Tests
		OptionVoraussetzung option1 = new OptionVoraussetzung();
		posList.add(option1);
		List<IdLink> linkList1 = new ArrayList<IdLink>();
		option1.setLinkList(linkList1);
		
		linkList1.add( new IdLink(null, eigMU, null, IdLink.KEIN_WERT, null) );
		linkList1.add( new IdLink(null, eigKL, null, 10, null) );
		
		Assert.assertEquals(
				EigenschaftEnum.MU.getAbk() + "," 
					+ EigenschaftEnum.KL.getAbk()  + " 10",
				CharElementTextService.getVoraussetzungsText(voraus));
		
		// Mit zweiter "und" Option
		OptionVoraussetzung option2 = new OptionVoraussetzung();
		option2.setWert(10);
		posList.add(option2);
		List<IdLink> linkList2 = new ArrayList<IdLink>();
		option2.setLinkList(linkList2);
		
		linkList2.add( new IdLink(null, tal1, null, 5, null) );
		
		Assert.assertEquals(
				"(" + EigenschaftEnum.MU.getAbk() + "," 
					+ EigenschaftEnum.KL.getAbk()  + " 10" + ")"
				+ " und (Ab Stufe 10: " + tal1.getName() + " 5)",
				CharElementTextService.getVoraussetzungsText(voraus));
		
		// Mit dritter "und" Option
		OptionVoraussetzung option3 = new OptionVoraussetzung();
		option3.setAnzahl(1);
		posList.add(option3);
		List<IdLink> linkList3 = new ArrayList<IdLink>();
		option3.setLinkList(linkList3);
		
		linkList3.add( new IdLink(null, tal1, null, IdLink.KEIN_WERT, null) );
		linkList3.add( new IdLink(null, tal2, null, IdLink.KEIN_WERT, null) );
		
		Assert.assertEquals(
				"(" + EigenschaftEnum.MU.getAbk() + "," 
					+ EigenschaftEnum.KL.getAbk()  + " 10" + ")"
				+ " und (Ab Stufe 10: " + tal1.getName() + " 5)"
				+ " und (1 aus: " + tal1.getName() + "," + tal2.getName() + ")",
				CharElementTextService.getVoraussetzungsText(voraus));
		
		// Mit vierter "und" Option
		OptionVoraussetzung option4 = new OptionVoraussetzung();
		option4.setWert(10);
		option4.setAnzahl(1);
		posList.add(option4);
		List<IdLink> linkList4 = new ArrayList<IdLink>();
		option4.setLinkList(linkList4);
		
		linkList4.add( new IdLink(null, tal1, null, IdLink.KEIN_WERT, null) );
		linkList4.add( new IdLink(null, tal2, null, IdLink.KEIN_WERT, null) );
		
		Assert.assertEquals(
				"(" + EigenschaftEnum.MU.getAbk() + "," 
					+ EigenschaftEnum.KL.getAbk()  + " 10" + ")"
				+ " und (Ab Stufe 10: " + tal1.getName() + " 5)"
				+ " und (1 aus: " + tal1.getName() + "," + tal2.getName() + ")"
				+ " und (Ab Stufe 10, 1 aus: " + tal1.getName() + "," + tal2.getName() + ")",
				CharElementTextService.getVoraussetzungsText(voraus));
		
		// Mit fünfter, negativer "und" Option
		OptionVoraussetzung option5 = new OptionVoraussetzung();
		negList.add(option5);
		List<IdLink> linkList5 = new ArrayList<IdLink>();
		option5.setLinkList(linkList5);
		
		linkList5.add( new IdLink(null, eigIN, null, 12, null) );
		
		Assert.assertEquals(
				"(" + EigenschaftEnum.MU.getAbk() + "," 
					+ EigenschaftEnum.KL.getAbk()  + " 10" + ")"
				+ " und (Ab Stufe 10: " + tal1.getName() + " 5)"
				+ " und (1 aus: " + tal1.getName() + "," + tal2.getName() + ")"
				+ " und (Ab Stufe 10, 1 aus: " + tal1.getName() + "," + tal2.getName() + ")"
				+ ", NICHT: " + EigenschaftEnum.IN.getAbk() + " 12",
				CharElementTextService.getVoraussetzungsText(voraus));
		
		// Mit sechster "oder" Option
		OptionVoraussetzung option6 = new OptionVoraussetzung();
		option2.setAlternativOption(option6);
		List<IdLink> linkList6 = new ArrayList<IdLink>();
		option6.setLinkList(linkList6);
		
		linkList6.add( new IdLink(null, eigIN, null, 12, null) );
		
		Assert.assertEquals(
				"(" + EigenschaftEnum.MU.getAbk() + "," 
					+ EigenschaftEnum.KL.getAbk()  + " 10" + ")"
				+ " und ((Ab Stufe 10: " + tal1.getName() + " 5)" 
						+ " oder (" + EigenschaftEnum.IN.getAbk() + " 12))"
				+ " und (1 aus: " + tal1.getName() + "," + tal2.getName() + ")"
				+ " und (Ab Stufe 10, 1 aus: " + tal1.getName() + "," + tal2.getName() + ")"
				+ ", NICHT: " + EigenschaftEnum.IN.getAbk() + " 12",
				CharElementTextService.getVoraussetzungsText(voraus));
		
		// Mit siebter "oder" Option
		OptionVoraussetzung option7 = new OptionVoraussetzung();
		option6.setAlternativOption(option7);
		List<IdLink> linkList7 = new ArrayList<IdLink>();
		option7.setLinkList(linkList7);
		
		linkList7.add( new IdLink(null, eigMU, null, IdLink.KEIN_WERT, null) );
		
		Assert.assertEquals(
				"(" + EigenschaftEnum.MU.getAbk() + "," 
					+ EigenschaftEnum.KL.getAbk()  + " 10" + ")"
				+ " und ((Ab Stufe 10: " + tal1.getName() + " 5)" 
						+ " oder ((" + EigenschaftEnum.IN.getAbk() + " 12)" 
							+ " oder ("+EigenschaftEnum.MU.getAbk()+")))"
				+ " und (1 aus: " + tal1.getName() + "," + tal2.getName() + ")"
				+ " und (Ab Stufe 10, 1 aus: " + tal1.getName() + "," + tal2.getName() + ")"
				+ ", NICHT: " + EigenschaftEnum.IN.getAbk() + " 12",
				CharElementTextService.getVoraussetzungsText(voraus));
	}
	

	@Test
	public void testGetLinkText() {
		IdLink link;
		//CharElement quelle, ZIEL ziel, CharElement zweitZiel, int wert, String text
		
		link = new IdLink(null, eigMU, null, IdLink.KEIN_WERT, null);
		Assert.assertEquals(
				EigenschaftEnum.MU.getAbk(),
				CharElementTextService.getLinkText(link));
		
		link = new IdLink(null, eigMU, null, 12, null);
		Assert.assertEquals(
				EigenschaftEnum.MU.getAbk() + " 12",
				CharElementTextService.getLinkText(link));
		
		link = new IdLink(null, tal1, null, 10, null);
		Assert.assertEquals(
				tal1.getName() + " 10",
				CharElementTextService.getLinkText(link));
		
		link = new IdLink(null, tal1, null, IdLink.KEIN_WERT, "test");
		Assert.assertEquals(
				tal1.getName() + "(test)",
				CharElementTextService.getLinkText(link));
		
		link = new IdLink(null, tal1, tal2, IdLink.KEIN_WERT, null);
		Assert.assertEquals(
				tal1.getName() + "(" + tal2.getName() + ")",
				CharElementTextService.getLinkText(link));
		
		link = new IdLink(null, tal1, tal2, IdLink.KEIN_WERT, "test");
		Assert.assertEquals(
				tal1.getName() + "(test," + tal2.getName() + ")",
				CharElementTextService.getLinkText(link));
	}

}
