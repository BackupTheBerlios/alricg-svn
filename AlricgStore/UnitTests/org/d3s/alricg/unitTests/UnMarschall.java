/*
 * Created 10.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.unitTests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.Repraesentation;
import org.d3s.alricg.store.charElemente.Schrift;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.Sprache;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.charElemente.Fertigkeit.AdditionsFamilie;
import org.d3s.alricg.store.charElemente.Fertigkeit.FertigkeitArt;
import org.d3s.alricg.store.charElemente.Rasse.FarbenAngabe;
import org.d3s.alricg.store.charElemente.Werte.CharArten;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.Werte.Geschlecht;
import org.d3s.alricg.store.charElemente.Werte.MagieMerkmal;
import org.d3s.alricg.store.charElemente.Zauber.Verbreitung;
import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;
import org.d3s.alricg.store.charElemente.charZusatz.WuerfelSammlung;
import org.d3s.alricg.store.charElemente.links.Auswahl;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Option;
import org.d3s.alricg.store.charElemente.links.OptionAnzahl;
import org.d3s.alricg.store.charElemente.links.OptionListe;
import org.d3s.alricg.store.charElemente.links.OptionVerteilung;
import org.d3s.alricg.store.charElemente.links.OptionVoraussetzung;
import org.d3s.alricg.store.charElemente.links.Voraussetzung;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


 /**
  * @author Vincent
  */
public class UnMarschall {
	private static File testFile = new File("testFile.xml");
	private static XmlAccessor xmlAccessor;
	private static Marshaller marshaller;
	private static Unmarshaller unmarshaller;
	
	/**
	 * Testet das Schreiben/Lesen zu/von Eigenschaften zu XML. 
	 */
	@Test
	public void testEigenschaft() {
		// Elemente Erzeugen
		List<Eigenschaft> eigList = new ArrayList<Eigenschaft>();
		Eigenschaft[] eigArray = new Eigenschaft[3];
		
		eigArray[0] = new Eigenschaft();
		eigArray[0].setEigenschaftEnum(EigenschaftEnum.MU);
		eigArray[0].setBeschreibung("Mut Beschreibung");
		eigArray[0].setRegelAnmerkung("Mut RegelAnmerkung");
		//eigArray[0].setSammelbegriff("Mut sammelBegriff");
		eigArray[0].setAnzeigen(true);
		
		eigArray[1] = new Eigenschaft();
		eigArray[1].setEigenschaftEnum(EigenschaftEnum.KL);
		eigArray[1].setBeschreibung("KL Beschreibung");
		eigArray[1].setRegelAnmerkung("KL RegelAnmerkung");
		//eigArray[1].setSammelbegriff("KL sammelBegriff");
		eigArray[1].setAnzeigen(false);
		
		eigArray[2] = new Eigenschaft();
		eigArray[2].setEigenschaftEnum(EigenschaftEnum.MU);
		eigArray[2].setBeschreibung("Mut2 Beschreibung");
		eigArray[2].setRegelAnmerkung("Mut2 RegelAnmerkung");
		//eigArray[2].setSammelbegriff("Mut2 sammelBegriff");
		
		// Zusammenstellen
		eigList.add(eigArray[0]);
		eigList.add(eigArray[1]);
		eigList.add(eigArray[2]);
		xmlAccessor.setEigenschaftList(eigList);
		
		// Test 1 (Testen ob alle Daten Erwartungsgemäß sind)
		testEigenschaftAsserts();
		
		// Speichern und wieder auslesen
		try {
			marshaller.marshal(xmlAccessor, new FileWriter(testFile));
			xmlAccessor = null;
			xmlAccessor = (XmlAccessor) unmarshaller.unmarshal(testFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Test 2 - Der entscheidene Test ob alles zurückgeschrieben wurde
		testEigenschaftAsserts();

	}
	
	// Hilfmethode von "testEigenschaft()"
	private void testEigenschaftAsserts() {
		Eigenschaft eigen;
		
		Assert.assertEquals(3, xmlAccessor.getEigenschaftList().size());
		
		eigen = xmlAccessor.getEigenschaftList().get(0);
		Assert.assertEquals(EigenschaftEnum.MU, eigen.getEigenschaftEnum());
		Assert.assertEquals(EigenschaftEnum.MU.getBezeichnung(), eigen.getName());
		Assert.assertEquals(EigenschaftEnum.MU.getId(), eigen.getId());
		Assert.assertEquals("Mut Beschreibung", eigen.getBeschreibung());
		Assert.assertEquals("Mut RegelAnmerkung", eigen.getRegelAnmerkung());
		Assert.assertEquals("Basis", eigen.getSammelbegriff());
		Assert.assertTrue(eigen.isAnzeigen());
		
		eigen = xmlAccessor.getEigenschaftList().get(1);
		Assert.assertEquals(EigenschaftEnum.KL, eigen.getEigenschaftEnum());
		Assert.assertEquals(EigenschaftEnum.KL.getBezeichnung(), eigen.getName());
		Assert.assertEquals(EigenschaftEnum.KL.getId(), eigen.getId());
		Assert.assertEquals("KL Beschreibung", eigen.getBeschreibung());
		Assert.assertEquals("KL RegelAnmerkung", eigen.getRegelAnmerkung());
		Assert.assertEquals("Basis", eigen.getSammelbegriff());
		Assert.assertFalse(eigen.isAnzeigen());
		
		eigen = xmlAccessor.getEigenschaftList().get(2);
		Assert.assertEquals(EigenschaftEnum.MU, eigen.getEigenschaftEnum());
		Assert.assertEquals(EigenschaftEnum.MU.getBezeichnung(), eigen.getName());
		Assert.assertEquals(EigenschaftEnum.MU.getId(), eigen.getId());
		Assert.assertEquals("Mut2 Beschreibung", eigen.getBeschreibung());
		Assert.assertEquals("Mut2 RegelAnmerkung", eigen.getRegelAnmerkung());
		Assert.assertEquals("Basis", eigen.getSammelbegriff());

	}
	
	/**
	 * Testet das Schreiben/Lesen zu/von Voraussetzungen zu XML.
	 * Als Basis-CharElement wird die Klasse Eigenschaft genommen,
	 * da dies das Einfachte CharElement ist
	 */
	@Test
	public void testVoraussetzung() {
		List<Eigenschaft> eigList = new ArrayList<Eigenschaft>();
		Eigenschaft[] eigArray = new Eigenschaft[5];
		
		eigArray[0] = new Eigenschaft();
		eigArray[0].setEigenschaftEnum(EigenschaftEnum.MU);
		
		eigArray[1] = new Eigenschaft();
		eigArray[1].setEigenschaftEnum(EigenschaftEnum.KL);
		
		eigArray[2] = new Eigenschaft();
		eigArray[2].setEigenschaftEnum(EigenschaftEnum.KO);
		
		eigArray[3] = new Eigenschaft();
		eigArray[3].setEigenschaftEnum(EigenschaftEnum.AUP);
		
		eigArray[4] = new Eigenschaft();
		eigArray[4].setEigenschaftEnum(EigenschaftEnum.CH);
		
		// Erstelle Link Listen
		List<IdLink>[] idLinkListArray = new ArrayList[5];
		
		for (int i1 = 0; i1 < idLinkListArray.length; i1++ ) {
			idLinkListArray[i1] =  new ArrayList<IdLink>();
			
			for (int i2 = 0; i2 < 5; i2++) {
				IdLink link = new IdLink();
				link.setQuelle(eigArray[0]);
				
				if (i2 == 0) { 
					link.setZiel(eigArray[1]);
					link.setZweitZiel(eigArray[2]);
					link.setText("Lala");
					link.setWert(12);
					link.setLeitwert(true);
					
				} else if (i2 == 1) {
					link.setZiel(eigArray[2]);
					link.setText("Lolo");
					link.setWert(13);
					link.setLeitwert(false);
					
				} else if (i2 == 2) {
					link.setZiel(eigArray[3]);
					link.setZweitZiel(eigArray[1]);
					link.setText("Lili");
					link.setWert(14);
	
				} else {
					link.setZiel(eigArray[4]);
				}
				
				idLinkListArray[i1].add(link);
			}
		}
			
		// Erstelle Voraussetzungs Optionen
		OptionVoraussetzung[] optVor = new OptionVoraussetzung[5];
		
		optVor[0] = new OptionVoraussetzung();
		optVor[0].setWert(10);
		optVor[0].setAnzahl(0);
		optVor[0].setLinkList(idLinkListArray[0]);
		
		optVor[1] = new OptionVoraussetzung();
		optVor[1].setWert(0);
		optVor[1].setAnzahl(0);
		optVor[1].setLinkList(idLinkListArray[1]);
		optVor[0].setAlternativOption(optVor[1]);
		
		optVor[2] = new OptionVoraussetzung();
		optVor[2].setWert(15);
		optVor[2].setAnzahl(3);
		optVor[2].setLinkList(idLinkListArray[2]);
		optVor[1].setAlternativOption(optVor[2]);
		
		optVor[3] = new OptionVoraussetzung();
		optVor[3].setWert(16);
		optVor[3].setAnzahl(4);
		optVor[3].setLinkList(idLinkListArray[3]);
		
		optVor[4] = new OptionVoraussetzung();
		optVor[4].setLinkList(idLinkListArray[4]);
		
		// Erstelle Voraussetzung
		Voraussetzung vorauss = new Voraussetzung();
		List<OptionVoraussetzung> optVorList = new ArrayList<OptionVoraussetzung>();
		optVorList.add(optVor[0]);
		optVorList.add(optVor[3]);
		optVorList.add(optVor[4]);
		
		vorauss.setPosVoraussetzung(optVorList);
		eigArray[0].setVoraussetzung(vorauss);
		
		// Zusammenstellen
		for (int i = 0; i < eigArray.length; i++) {
			eigList.add(eigArray[i]);
		}
		xmlAccessor.setEigenschaftList(eigList);
		
		// Test 1 (Testen ob alle Daten Erwartungsgemäß sind)
		testVoraussetzungsAsserts();
		
		// Speichern und wieder auslesen
		try {
			marshaller.marshal(xmlAccessor, new FileWriter(testFile));
			xmlAccessor = null;
			xmlAccessor = (XmlAccessor) unmarshaller.unmarshal(testFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Test 2 - Der entscheidene Test ob alles zurückgeschrieben wurde
		testVoraussetzungsAsserts();
	}
	
	private void testVoraussetzungsAsserts() {
		Voraussetzung voraus = xmlAccessor.getEigenschaftList().get(0).getVoraussetzung();
		List<IdLink> linkList;
		
		Assert.assertEquals(5, xmlAccessor.getEigenschaftList().size());
		Assert.assertEquals(3, voraus.getPosVoraussetzung().size());
		Assert.assertEquals(10, voraus.getPosVoraussetzung().get(0).getWert());
		Assert.assertEquals(0, voraus.getPosVoraussetzung().get(0).getAnzahl());
		
		linkList = voraus.getPosVoraussetzung().get(0).getLinkList();
		Assert.assertEquals(5, linkList.size());
		Assert.assertEquals(EigenschaftEnum.KL.getId(), linkList.get(0).getZiel().getId());
		Assert.assertTrue( linkList.get(0).getZiel() instanceof Eigenschaft);
		Assert.assertEquals(EigenschaftEnum.KO.getId(), linkList.get(0).getZweitZiel().getId());
		Assert.assertEquals("Lala", linkList.get(0).getText());
		Assert.assertEquals(12, linkList.get(0).getWert());
		Assert.assertEquals(linkList.get(0).getZweitZiel(), linkList.get(1).getZiel());
		
		Assert.assertEquals(15, ((OptionVoraussetzung) voraus.getPosVoraussetzung().get(0)
									.getAlternativOption().getAlternativOption()).getWert());
		
	}
	
	@Test
	public void testTalent() {
		List<Talent> talentList = new ArrayList<Talent>();
		List<Eigenschaft> eigenschaftList = new ArrayList<Eigenschaft>();
		
		Eigenschaft[] eigArray = new Eigenschaft[3];
		eigArray[0] = new Eigenschaft();
		eigArray[0].setEigenschaftEnum(EigenschaftEnum.MU);
		eigArray[1] = new Eigenschaft();
		eigArray[1].setEigenschaftEnum(EigenschaftEnum.KL);
		eigArray[2] = new Eigenschaft();
		eigArray[2].setEigenschaftEnum(EigenschaftEnum.KO);
		
		eigenschaftList.add(eigArray[0]);
		eigenschaftList.add(eigArray[1]);
		eigenschaftList.add(eigArray[2]);
		
		Talent[] talentArray = new Talent[3];
		
		talentArray[0] = new Talent();
		talentArray[0].setDreiEigenschaften(eigArray);
		talentArray[0].setArt(Talent.Art.basis);
		talentArray[0].setSorte(Talent.Sorte.kampf);
		talentArray[0].setKostenKlasse(KostenKlasse.B);
		talentArray[0].setName("Talent 1");
		talentArray[0].setId("Talent-1");
		talentArray[0].setSpezialisierungen(new String[] {"spez1", "spez2", "spez3"});
		
		talentArray[1] = new Talent();
		talentArray[1].setDreiEigenschaften(eigArray);
		talentArray[1].setArt(Talent.Art.beruf);
		talentArray[1].setSorte(Talent.Sorte.koerper);
		talentArray[1].setKostenKlasse(KostenKlasse.A_PLUS);
		talentArray[1].setName("Talent 2");
		talentArray[1].setId("Talent-2");
		
		talentArray[2] = new Talent();
		talentArray[2].setDreiEigenschaften(eigArray);
		talentArray[2].setArt(Talent.Art.basis);
		talentArray[2].setSorte(Talent.Sorte.kampf);
		talentArray[2].setKostenKlasse(KostenKlasse.B);
		talentArray[2].setName("Talent 3");
		talentArray[2].setId("Talent-3");
		
		talentList.add(talentArray[0]);
		talentList.add(talentArray[1]);
		talentList.add(talentArray[2]);
		
		// TEST >>>>>>>>>
		/*
		List<Schrift> schriftList = new ArrayList<Schrift>();
		Schrift g = new Schrift();
		g.setId("SRI-testSchrift");
		g.setName("Lala-Lumpo schrift");
		g.setKomplexitaet(10);
		g.setKostenKlasse(KostenKlasse.B);
		g.setSammelbegriff("Doofe Schriften");
		schriftList.add(g);
		xmlAccessor.setSchriftList(schriftList);
		
		List<Sprache> spracheList = new ArrayList<Sprache>();
		Sprache l1 = new Sprache();
		l1.setId("SPR-testSprache1");
		l1.setName("Lala-Lopo Sprache");
		l1.setKomplexitaet(12);
		l1.setKostenKlasse(KostenKlasse.A);
		l1.setSammelbegriff("Schlaue Sprachen");
		l1.setZugehoerigeSchrift(new IdLink(l1, g, null, IdLink.KEIN_WERT, null));
		
		Sprache l2 = new Sprache();
		l2.setId("SPR-testSprache2");
		l2.setName("Lala-Lopo Sprache2");
		l2.setKomplexitaet(11);
		l2.setKostenKlasse(KostenKlasse.C);
		l2.setSammelbegriff("Ganz Schlaue Sprachen");
		
		Sprache l3 = new Sprache();
		l3.setId("SPR-testSprache2-2");
		l3.setName("Lala-Lopo Sprache (NichtM)");
		l3.setKomplexitaet(87);
		l3.setKostenKlasse(KostenKlasse.H);
		l3.setSammelbegriff("Ganz Schlaue Sprachen");
		l2.setWennNichtMuttersprache(l3);
		
		spracheList.add(l1);
		spracheList.add(l2);
		xmlAccessor.setSpracheList(spracheList);
		*/
		// <<<<<<<<<<<<< 
		
		xmlAccessor.setTalentList(talentList);
		xmlAccessor.setEigenschaftList(eigenschaftList);
		// Test 1 (Testen ob alle Daten Erwartungsgemäß sind)
		testTalentAsserts();
		
		// Speichern und wieder auslesen
		try {
			marshaller.marshal(xmlAccessor, new FileWriter(testFile));
			xmlAccessor = null;
			xmlAccessor = (XmlAccessor) unmarshaller.unmarshal(testFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Test 2 - Der entscheidene Test ob alles zurückgeschrieben wurde
		testTalentAsserts();
	}
	
	private void testTalentAsserts() {		
		Assert.assertEquals(3, xmlAccessor.getTalentList().size());
		
		Talent tal = xmlAccessor.getTalentList().get(0);	
		Assert.assertEquals(EigenschaftEnum.MU, tal.getDreiEigenschaften()[0].getEigenschaftEnum());
		Assert.assertEquals(Talent.Art.basis, tal.getArt());
		Assert.assertEquals(Talent.Sorte.kampf, tal.getSorte());
		Assert.assertEquals(KostenKlasse.B, tal.getKostenKlasse());
		Assert.assertEquals("Talent 1", tal.getName());
		Assert.assertEquals("Talent-1", tal.getId());
		
		tal = xmlAccessor.getTalentList().get(1);	
		Assert.assertEquals(EigenschaftEnum.MU, tal.getDreiEigenschaften()[0].getEigenschaftEnum());
		Assert.assertEquals(Talent.Art.beruf, tal.getArt());
		Assert.assertEquals(Talent.Sorte.koerper, tal.getSorte());
		Assert.assertEquals(KostenKlasse.A_PLUS, tal.getKostenKlasse());
		Assert.assertEquals("Talent 2", tal.getName());
		Assert.assertEquals("Talent-2", tal.getId());
		
		
		Assert.assertEquals(
				xmlAccessor.getTalentList().get(0).getDreiEigenschaften()[0], 
				xmlAccessor.getTalentList().get(1).getDreiEigenschaften()[0]
			);
		
		Assert.assertEquals(
				xmlAccessor.getTalentList().get(0).getArt(), 
				xmlAccessor.getTalentList().get(2).getArt()
			);
		
		Assert.assertEquals(
				xmlAccessor.getTalentList().get(0).getKostenKlasse(), 
				xmlAccessor.getTalentList().get(2).getKostenKlasse()
			);
	}
	
	/**
	 * Testet Zauber und Repräsentationen.
	 */
	@Test
	public void testZauber() {
		List<Zauber> zauberList = new ArrayList<Zauber>();
		List<Eigenschaft> eigenschaftList = new ArrayList<Eigenschaft>();
		List<Repraesentation> repraesentationList = new ArrayList<Repraesentation>();
		
		// Eigenschaften
		Eigenschaft[] eigArray = new Eigenschaft[3];
		eigArray[0] = new Eigenschaft();
		eigArray[0].setEigenschaftEnum(EigenschaftEnum.MU);
		eigArray[1] = new Eigenschaft();
		eigArray[1].setEigenschaftEnum(EigenschaftEnum.KL);
		eigArray[2] = new Eigenschaft();
		eigArray[2].setEigenschaftEnum(EigenschaftEnum.KO);
		
		eigenschaftList.add(eigArray[0]);
		eigenschaftList.add(eigArray[1]);
		eigenschaftList.add(eigArray[2]);
		xmlAccessor.setEigenschaftList(eigenschaftList);
		
		// Repräsentationen
		Repraesentation[] repArray = new Repraesentation[2];
		repArray[0] = new Repraesentation();
		repArray[0].setId("rep1");
		repArray[0].setName("rep1");
		repArray[0].setAbkuerzung("Rep1 Abk");
		repArray[1] = new Repraesentation();
		repArray[1].setId("rep2");
		repArray[1].setName("rep2");
		repArray[1].setAbkuerzung("Rep2 Abk");
		
		repraesentationList.add(repArray[0]);
		repraesentationList.add(repArray[1]);
		xmlAccessor.setRepraesentationList(repraesentationList);
		
		// Zauber
		Zauber[] zauberArray = new Zauber[1];
		
		zauberArray[0] = new Zauber();
		zauberArray[0].setDreiEigenschaften(eigArray);
		zauberArray[0].setAspKosten("Die aspKosten");
		zauberArray[0].setProbenModi("Modi");
		zauberArray[0].setReichweite("Reichweite");
		zauberArray[0].setWirkungsdauer("WDauer");
		zauberArray[0].setZauberdauer("ZDauer");
		zauberArray[0].setZiel("Ziel");
		
		MagieMerkmal[] merkmalArray = new MagieMerkmal[2];
		merkmalArray[0] = MagieMerkmal.antimagie;
		merkmalArray[1] = MagieMerkmal.beschwoerung;
		zauberArray[0].setMerkmale(merkmalArray);
		
		Verbreitung[] verbreitungArray = new Verbreitung[2];
		verbreitungArray[0] = new Verbreitung();
		verbreitungArray[0].setBekanntBei(repArray[0]);
		verbreitungArray[0].setRepraesentation(repArray[1]);
		verbreitungArray[0].setWert(10);
		verbreitungArray[1] = new Verbreitung();
		verbreitungArray[1].setRepraesentation(repArray[0]);
		verbreitungArray[1].setWert(11);
		
		zauberArray[0].setVerbreitung(verbreitungArray);
		zauberList.add(zauberArray[0]);
		
		xmlAccessor.setZauberList(zauberList);
		
		// Test 1 (Testen ob alle Daten Erwartungsgemäß sind)
		testZauberAsserts();
		
		// Speichern und wieder auslesen
		try {
			marshaller.marshal(xmlAccessor, new FileWriter(testFile));
			xmlAccessor = null;
			xmlAccessor = (XmlAccessor) unmarshaller.unmarshal(testFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Test 2 - Der entscheidene Test ob alles zurückgeschrieben wurde
		testZauberAsserts();
	}
	
	private void testZauberAsserts() {
		Assert.assertEquals(1, xmlAccessor.getZauberList().size());
		
		// Repräsentation
		Assert.assertEquals(2, xmlAccessor.getRepraesentationList().size());
		Assert.assertEquals("Rep1 Abk", xmlAccessor.getRepraesentationList().get(0).getAbkuerzung());
		
		// Zauber
		Zauber zaub = xmlAccessor.getZauberList().get(0);
		
		Assert.assertEquals("Die aspKosten", zaub.getAspKosten());
		Assert.assertEquals("Modi", zaub.getProbenModi());
		Assert.assertEquals("Reichweite", zaub.getReichweite());
		Assert.assertEquals("WDauer", zaub.getWirkungsdauer());
		Assert.assertEquals("ZDauer", zaub.getZauberdauer());
		Assert.assertEquals("Ziel", zaub.getZiel());
		Assert.assertEquals(2, zaub.getMerkmale().length);
		Assert.assertEquals(MagieMerkmal.antimagie, zaub.getMerkmale()[0]);
		Assert.assertEquals(MagieMerkmal.beschwoerung, zaub.getMerkmale()[1]);
		Assert.assertEquals(2, zaub.getVerbreitung().length);
		Assert.assertEquals(10, zaub.getVerbreitung()[0].getWert());
		Assert.assertEquals(
				xmlAccessor.getRepraesentationList().get(0), 
				zaub.getVerbreitung()[0].getBekanntBei());
		Assert.assertEquals(
				xmlAccessor.getRepraesentationList().get(1), 
				zaub.getVerbreitung()[0].getRepraesentation());
		Assert.assertEquals(
				"Rep2 Abk(Rep1 Abk) 10", 
				zaub.getVerbreitung()[0].getAbkuerzungText().toString());
		Assert.assertEquals(
				"Rep2 Abk(Rep1 Abk) 10, Rep1 Abk 11", 
				zaub.getVerbreitungText(true));
		
		Assert.assertEquals(11, zaub.getVerbreitung()[1].getWert());
		Assert.assertEquals(
				zaub.getVerbreitung()[0].getBekanntBei(), 
				zaub.getVerbreitung()[1].getRepraesentation());
	}
	
	@Test
	public void testVorNachteil() {
		List<Nachteil> nachteilList = new ArrayList<Nachteil>();
		
		Nachteil[] nachteil = new Nachteil[2];
		nachteil[0] = new Nachteil();
		nachteil[0].setId("Id-Nachteil-1");
		nachteil[0].setName("Nachteil 1");
		nachteil[0].setBenoetigtZweitZiel(true);
		nachteil[0].setFuerWelcheChars(new CharArten[] {CharArten.halbZauberer, CharArten.viertelZauberer});
		nachteil[0].setGpKosten(66);
		nachteil[0].setKostenProSchritt(2);
		nachteil[0].setMaxStufe(6);
		nachteil[0].setMinStufe(4);
		nachteil[0].setMitFreienText(true);
		nachteil[0].setSchlechteEigen(true);
		nachteil[0].setStufenSchritt(1);
		nachteil[0].setTextVorschlaege(new String[] {"la", "li", "lo"});
		
		AdditionsFamilie adFam = new AdditionsFamilie();
		adFam.setAdditionsID("XYZ");
		adFam.setAdditionsWert(100);
		nachteil[0].setAdditionsFamilie(adFam);
		
		// Ändert Kosten
		nachteil[1] = new Nachteil();
		nachteil[1].setId("Id-Nachteil-2");
		nachteil[1].setName("Nachteil 2");
		
		IdLink<Nachteil>[] linkArray = new IdLink[1];
		linkArray[0] = new IdLink<Nachteil>();
		linkArray[0].setZiel(nachteil[1]);
		linkArray[0].setWert(5);
		linkArray[0].setQuelle(nachteil[0]);
		
		nachteil[0].setAendertGpNachteil(linkArray);
		
		// Zusammenfügen
		nachteilList.add(nachteil[0]);
		nachteilList.add(nachteil[1]);
		
		xmlAccessor.setNachteilList(nachteilList);
		
		// Test 1 (Testen ob alle Daten Erwartungsgemäß sind)
		testVorNachteilAsserts();
		
		// Speichern und wieder auslesen
		try {
			marshaller.marshal(xmlAccessor, new FileWriter(testFile));
			xmlAccessor = null;
			xmlAccessor = (XmlAccessor) unmarshaller.unmarshal(testFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Test 2 - Der entscheidene Test ob alles zurückgeschrieben wurde
		testVorNachteilAsserts();
		
	}
	
	private void testVorNachteilAsserts() {
		Assert.assertEquals(2, xmlAccessor.getNachteilList().size());
		
		Nachteil nachteil = xmlAccessor.getNachteilList().get(0);
		
		Assert.assertEquals("Id-Nachteil-1",nachteil.getId());
		Assert.assertEquals("Nachteil 1",nachteil.getName());
		Assert.assertTrue(nachteil.getBenoetigtZweitZiel());
		Assert.assertEquals(2, nachteil.getFuerWelcheChars().length);
		Assert.assertEquals(CharArten.halbZauberer, nachteil.getFuerWelcheChars()[0]);
		Assert.assertEquals(CharArten.viertelZauberer, nachteil.getFuerWelcheChars()[1]);
		Assert.assertEquals(66,nachteil.getGpKosten());
		Assert.assertEquals(2,nachteil.getKostenProSchritt());
		Assert.assertEquals(6,nachteil.getMaxStufe());
		Assert.assertEquals(4,nachteil.getMinStufe());
		Assert.assertTrue(nachteil.isMitFreienText());
		Assert.assertTrue(nachteil.isSchlechteEigen());
		Assert.assertEquals(1,nachteil.getStufenSchritt());
		Assert.assertEquals(3,nachteil.getTextVorschlaege().length);
		Assert.assertEquals("la",nachteil.getTextVorschlaege()[0]);
		Assert.assertEquals("li",nachteil.getTextVorschlaege()[1]);
		Assert.assertEquals("lo",nachteil.getTextVorschlaege()[2]);
		
		Assert.assertEquals("XYZ",nachteil.getAdditionsFamilie().getAdditionsID());
		Assert.assertEquals(100,nachteil.getAdditionsFamilie().getAdditionsWert());
		Assert.assertEquals("Id-Nachteil-2", nachteil.getAendertGpNachteil()[0].getZiel().getId());
		Assert.assertEquals(xmlAccessor.getNachteilList().get(1), nachteil.getAendertGpNachteil()[0].getZiel());
		
	}
	
	@Test
	public void testSonderf() {
		Sonderfertigkeit sondf = new Sonderfertigkeit();
		sondf.setId("sfId");
		sondf.setName("Sonderf");
		sondf.setApKosten(100);
		sondf.setArt(FertigkeitArt.waffenloskampf);
		sondf.setPermAsp(1);
		sondf.setPermLep(2);
		sondf.setPermKa(3);
		
		List<Sonderfertigkeit> sfList = new ArrayList<Sonderfertigkeit>();
		sfList.add(sondf);
		xmlAccessor.setSonderfList(sfList);
		
		// Test 1 (Testen ob alle Daten Erwartungsgemäß sind)
		testSonderfertigkeitAsserts();
		
		// Speichern und wieder auslesen
		try {
			marshaller.marshal(xmlAccessor, new FileWriter(testFile));
			xmlAccessor = null;
			xmlAccessor = (XmlAccessor) unmarshaller.unmarshal(testFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Test 2 - Der entscheidene Test ob alles zurückgeschrieben wurde
		testSonderfertigkeitAsserts();
	}
	
	private void testSonderfertigkeitAsserts() {
		Assert.assertEquals(1, xmlAccessor.getSonderfList().size());
		
		Sonderfertigkeit sf = xmlAccessor.getSonderfList().get(0);
		Assert.assertEquals("sfId", sf.getId());
		Assert.assertEquals("Sonderf", sf.getName());
		Assert.assertEquals(100, sf.getApKosten());
		Assert.assertEquals(FertigkeitArt.waffenloskampf, sf.getArt());
		Assert.assertEquals(1, sf.getPermAsp());
		Assert.assertEquals(2, sf.getPermLep());
		Assert.assertEquals(3, sf.getPermKa());
	}
	
	@Test
	public void testRasse() {
		Rasse ras = new Rasse();
		
		ras.setId("Rasse-1");
		ras.setName("Rasse1");
		ras.setBeschreibung("Ää Üü Öö ß ?! <> () &");
		
		
		WuerfelSammlung ws = new WuerfelSammlung();
		ws.setAnzahlWuerfel(new int[]{1,3});
		ws.setAugenWuerfel(new int[]{6,20});
		ws.setFestWert(12);
		ras.setAlterWuerfel(ws);
		ras.setGroesseWuerfel(ws);
		
		FarbenAngabe[] fa = new FarbenAngabe[2];
		fa[0] = new FarbenAngabe();
		fa[0].setFarbe("Schwarz");
		fa[0].setWahrscheinlichkeit(12);
		fa[1] = new FarbenAngabe();
		fa[1].setFarbe("Weiß");
		fa[1].setWahrscheinlichkeit(8);
		ras.setAugenfarbe(fa);
		ras.setHaarfarbe(fa);
		
		ras.setGeschlecht(Geschlecht.mann);
		ras.setGeschwindigk(100);
		ras.setGewichtModi(-20);
		ras.setGpKosten(5);
		ras.setSoMax(3);
		ras.setSoMin(1);
	
		// Nachteile
		List<Nachteil> nachteilList = new ArrayList<Nachteil>();
		Nachteil nachteil = new Nachteil();
		nachteil.setId("Nachteil-1");
		nachteil.setName("Nachteil1");
		nachteilList.add(nachteil);
		nachteil = new Nachteil();
		nachteil.setId("Nachteil-2");
		nachteil.setName("Nachteil2");
		nachteilList.add(nachteil);
		xmlAccessor.setNachteilList(nachteilList);
		
		IdLink<Nachteil>[] linkArrayN = new IdLink[2];
		linkArrayN[0] = new IdLink<Nachteil>();
		linkArrayN[0].setQuelle(ras);
		linkArrayN[0].setZiel(nachteilList.get(0));
		linkArrayN[0].setText("lala");
		linkArrayN[1] = new IdLink<Nachteil>();
		linkArrayN[1].setQuelle(ras);
		linkArrayN[1].setZiel(nachteilList.get(1));
		linkArrayN[1].setZweitZiel(nachteilList.get(0));
		
		ras.setEmpfNachteile(linkArrayN);
		ras.setUngeNachteile(linkArrayN);
		
		// Talent Auswahl
		List<Eigenschaft> eigList = new ArrayList<Eigenschaft>();
		Eigenschaft eig = new Eigenschaft();
		
		eig = new Eigenschaft();
		eig.setEigenschaftEnum(EigenschaftEnum.MU);
		eig.setBeschreibung("Mut Beschreibung");
		eig.setRegelAnmerkung("Mut RegelAnmerkung");
		eig.setSammelbegriff("Mut sammelBegriff");
		eig.setAnzeigen(true);
		
		// Zusammenstellen
		eigList.add(eig);
		xmlAccessor.setEigenschaftList(eigList);
		
		List<Talent> talentList = new ArrayList<Talent>();
		Talent talent = new Talent();
		talent.setId("Tal-1");
		talent.setName("Talent-1");
		talent.setArt(Talent.Art.basis);
		talent.setSorte(Talent.Sorte.gesellschaft);
		talent.setDreiEigenschaften(new Eigenschaft[] {eig,eig,eig});
		talentList.add(talent);
		talent = new Talent();
		talent.setId("Tal-2");
		talent.setName("Talent-2");
		talent.setArt(Talent.Art.beruf);
		talent.setSorte(Talent.Sorte.handwerk);
		talent.setDreiEigenschaften(new Eigenschaft[] {eig,eig,eig});
		talentList.add(talent);
		xmlAccessor.setTalentList(talentList);
		
		List<IdLink<Talent>> linkArrayT = new ArrayList<IdLink<Talent>>();
		IdLink<Talent> talentLink = new IdLink<Talent>();
		talentLink.setQuelle(ras);
		talentLink.setZiel(talentList.get(0));
		talentLink.setText("lala");
		linkArrayT.add(talentLink);
		talentLink = new IdLink<Talent>();
		talentLink.setQuelle(ras);
		talentLink.setZiel(talentList.get(1));
		talentLink.setZweitZiel(talentList.get(0));
		linkArrayT.add(talentLink);
		
		// Optionen für Talente
		List<Option> optListe = new ArrayList<Option>();
		Option opt = new OptionListe();
		opt.setLinkList(linkArrayT);
		opt.setWerteListe(new int[] {3,4,5});
		optListe.add(opt);
		
		opt = new OptionVerteilung();
		opt.setLinkList(linkArrayT);
		opt.setAnzahl(5);
		opt.setMax(4);
		opt.setWert(3);
		optListe.add(opt);
		
		opt = new OptionAnzahl();
		opt.setLinkList(linkArrayT);
		opt.setAnzahl(5);
		optListe.get(1).setAlternativOption(opt);
		
		Auswahl<Talent> auswahlT = new Auswahl<Talent>(); 
		auswahlT.setHerkunft(ras);
		auswahlT.setOptionen(optListe);
		ras.setTalente(auswahlT);

		
		// Rasse setzen und Testen
		List<Rasse> rasList = new ArrayList<Rasse>();
		rasList.add(ras);
		xmlAccessor.setRasseList(rasList);
		
		testRasseAsserts();
		
		// Speichern und wieder auslesen
		try {
			marshaller.marshal(xmlAccessor, new FileWriter(testFile));
			xmlAccessor = null;
			xmlAccessor = (XmlAccessor) unmarshaller.unmarshal(testFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Test 2 - Der entscheidene Test ob alles zurückgeschrieben wurde
		testRasseAsserts();
		
	}
	
	private void testRasseAsserts() {
		Rasse rasse = xmlAccessor.getRasseList().get(0);
		
		Assert.assertEquals("Ää Üü Öö ß ?! <> () &", rasse.getBeschreibung());
		
		// Prüfel Würfel
		Assert.assertEquals(2, rasse.getAlterWuerfel().getAnzahlWuerfel().length);
		Assert.assertEquals(2, rasse.getAlterWuerfel().getAugenWuerfel().length);
		Assert.assertEquals(1, rasse.getAlterWuerfel().getAnzahlWuerfel()[0]);
		Assert.assertEquals(3, rasse.getAlterWuerfel().getAnzahlWuerfel()[1]);
		Assert.assertEquals(6, rasse.getAlterWuerfel().getAugenWuerfel()[0]);
		Assert.assertEquals(20, rasse.getAlterWuerfel().getAugenWuerfel()[1]);
		Assert.assertEquals(12, rasse.getAlterWuerfel().getFestWert());
		
		Assert.assertEquals(2, rasse.getGroesseWuerfel().getAnzahlWuerfel().length);
		Assert.assertEquals(2, rasse.getGroesseWuerfel().getAugenWuerfel().length);
		
		// Prüfe Farben
		Assert.assertEquals(2, rasse.getHaarfarbe().length);
		Assert.assertEquals(2, rasse.getAugenfarbe().length);
		Assert.assertEquals("Schwarz", rasse.getHaarfarbe()[0].getFarbe());
		Assert.assertEquals("Weiß", rasse.getHaarfarbe()[1].getFarbe());
		Assert.assertEquals(12, rasse.getHaarfarbe()[0].getWahrscheinlichkeit());
		Assert.assertEquals(8, rasse.getHaarfarbe()[1].getWahrscheinlichkeit());
		Assert.assertEquals("Schwarz", rasse.getAugenfarbe()[0].getFarbe());
		
		// Prüfen allgemein
		Assert.assertEquals(Geschlecht.mann, rasse.getGeschlecht());
		Assert.assertEquals(100, rasse.getGeschwindigk());
		Assert.assertEquals(-20, rasse.getGewichtModi());
		Assert.assertEquals(5, rasse.getGpKosten());
		Assert.assertEquals(3, rasse.getSoMax());
		Assert.assertEquals(1, rasse.getSoMin());
		
		// Prüfen empfohlene Nachteile
		Assert.assertEquals(2, rasse.getEmpfNachteile().length);
		Assert.assertEquals(rasse, rasse.getEmpfNachteile()[0].getQuelle());
		Assert.assertEquals(
				xmlAccessor.getNachteilList().get(0), 
				rasse.getEmpfNachteile()[0].getZiel());
		Assert.assertEquals(
				"lala", 
				rasse.getEmpfNachteile()[0].getText());
		Assert.assertNull(rasse.getEmpfNachteile()[0].getZweitZiel());
		Assert.assertEquals(
				xmlAccessor.getNachteilList().get(1), 
				rasse.getEmpfNachteile()[1].getZiel());
		Assert.assertEquals(
				rasse.getEmpfNachteile()[0].getZiel(), 
				rasse.getEmpfNachteile()[1].getZweitZiel());
		
		// Prüfen ungeeignete Nachteile
		Assert.assertEquals(2, rasse.getUngeNachteile().length);
		Assert.assertEquals(rasse, rasse.getUngeNachteile()[0].getQuelle());
		Assert.assertEquals(
				xmlAccessor.getNachteilList().get(0), 
				rasse.getUngeNachteile()[0].getZiel());
		
		// Prüfen Talente
		Assert.assertEquals(rasse, rasse.getTalente().getHerkunft());
		Assert.assertEquals(2, rasse.getTalente().getOptionen().size());
		Assert.assertEquals(
				2, 
				rasse.getTalente().getOptionen().get(0).getLinkList().size());
		Assert.assertEquals(
				xmlAccessor.getTalentList().get(0), 
				((IdLink)rasse.getTalente().getOptionen().get(0).getLinkList().get(0)).getZiel());
		Assert.assertEquals(
				xmlAccessor.getTalentList().get(1), 
				((IdLink)rasse.getTalente().getOptionen().get(1).getLinkList().get(1)).getZiel());
		Assert.assertEquals(
				"lala", 
				((IdLink<Talent>)rasse.getTalente().getOptionen().get(0).getLinkList().get(0)).getText());
		Assert.assertEquals(
				5, 
				rasse.getTalente().getOptionen().get(0).getWerteListe()[2]);
		Assert.assertEquals(
				3, 
				rasse.getTalente().getOptionen().get(0).getWerteListe().length);
		Assert.assertEquals(
				3, 
				rasse.getTalente().getOptionen().get(1).getWert());
		Assert.assertEquals(
				5, 
				rasse.getTalente().getOptionen().get(1).getAlternativOption().getAnzahl());


	}
	
	@Before
	public void initTest() {
		xmlAccessor = new XmlAccessor();
	}
	
	@BeforeClass
	public static void initClass() throws Exception {
		JAXBContext ctx = JAXBContext.newInstance(XmlAccessor.class);
		marshaller = ctx.createMarshaller();
		// sonst scheint "ß" nicht zu gehen
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1"); 
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		unmarshaller = ctx.createUnmarshaller();
	}

}
