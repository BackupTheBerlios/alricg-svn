/*
 * Created 30.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.unitTests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.d3s.alricg.store.access.CharElementFactory;
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.access.hide.DependecySearcher.DependencyTableObject;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Gabe;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.KulturVariante;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.RegionVolk;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.links.Auswahl;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.charElemente.links.Option;
import org.d3s.alricg.store.charElemente.links.OptionAnzahl;
import org.d3s.alricg.store.charElemente.links.OptionVoraussetzung;
import org.d3s.alricg.store.charElemente.links.Voraussetzung;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Vincent
 */
public class CharElementFactoryTest {
	private static CharElementFactory factory;
	private static XmlAccessor xmlAcc;
	private static XmlAccessor xmlAcc2;
	
	@BeforeClass
	public static void init() {
		factory = CharElementFactory.getInstance();
		
		StoreAccessor.getInstance().addNewFile(new File("tmpTestFile.test"));
		xmlAcc = StoreDataAccessor.getInstance().getXmlAccessors().get(0);
		
		StoreAccessor.getInstance().addNewFile(new File("tmpTestFile2.test"));
		xmlAcc2 = StoreDataAccessor.getInstance().getXmlAccessors().get(1);
	}
	
	@Before
	public void la() {

	}
	
	/** Erzeugt neue Elemente und überprüft ob diese auch im XmlAccessor
	 * angelegt wurden */
	@Test
	public void testCreateCharElement() {
		for (int i = 0; i < XmlAccessor.ALL_STORED_CLASSES.length; i++) {
			Class clazz = XmlAccessor.ALL_STORED_CLASSES[i];
			
			if (clazz == Eigenschaft.class) {
				continue; // Kann nicht vom User erzeugt werden
			}
			
			CharElement newCharElem = factory.buildCharElement(clazz, xmlAcc);
			
			Assert.assertEquals(clazz, newCharElem.getClass());
			Assert.assertTrue(
					StoreDataAccessor.getInstance().getMatchingList(clazz).contains(newCharElem)
				);
		}
	}
	
	/**
	 * Erzeugt neue CharElemente und löscht diese wieder.
	 */
	@Test
	public void testDeleteCharElement() {
		for (int i = 0; i < XmlAccessor.ALL_STORED_CLASSES.length; i++) {
			Class clazz = XmlAccessor.ALL_STORED_CLASSES[i];
			
			if (clazz == Eigenschaft.class) {
				continue; // Kann nicht vom User erzeugt werden
			}
			
			CharElement newCharElem = factory.buildCharElement(clazz, xmlAcc);
			Assert.assertTrue(
					StoreDataAccessor.getInstance().getMatchingList(clazz).contains(newCharElem)
				);
			
			factory.deleteCharElement(newCharElem, xmlAcc);
			Assert.assertFalse(
					StoreDataAccessor.getInstance().getMatchingList(clazz).contains(newCharElem)
				);
		}
	}
	
	// Helper für testCreateCharElement
	@Test 
	public void testCheckDependecyVoraussetzung() {

		
		// Erstelle Elemente und eine Voraussetzung
		Talent talent1 = (Talent) factory.buildCharElement(Talent.class, xmlAcc);
		Talent talent2 = (Talent) factory.buildCharElement(Talent.class, xmlAcc);
		Gabe gabe1 = (Gabe) factory.buildCharElement(Gabe.class, xmlAcc2);
		Sonderfertigkeit sfk1 = (Sonderfertigkeit) factory.buildCharElement(Sonderfertigkeit.class, xmlAcc2);
		Vorteil vorteil1 = (Vorteil) factory.buildCharElement(Vorteil.class, xmlAcc2);
		Nachteil nachteil1 = (Nachteil) factory.buildCharElement(Nachteil.class, xmlAcc2);
		
		// Erstelle Link Listen
		List<IdLink>[] idLinkListArray = new ArrayList[3];
		
		idLinkListArray[0] = new ArrayList();
		idLinkListArray[1] = new ArrayList();
		idLinkListArray[2] = new ArrayList();
		
		IdLink link = new IdLink();
		link.setQuelle(talent1);
		link.setZiel(talent2);
		idLinkListArray[0].add(link);
		
		link = new IdLink();
		link.setQuelle(talent1);
		link.setZiel(sfk1);
		idLinkListArray[0].add(link);
		
		link = new IdLink();
		link.setQuelle(talent1);
		link.setZiel(talent2);
		link.setZweitZiel(gabe1);
		idLinkListArray[1].add(link);
		
		link = new IdLink();
		link.setQuelle(talent1);
		link.setZiel(vorteil1);
		idLinkListArray[1].add(link);
		
		link = new IdLink();
		link.setQuelle(talent1);
		link.setZiel(nachteil1);
		idLinkListArray[2].add(link);
		
		
		OptionVoraussetzung[] optVor = new OptionVoraussetzung[3];
		
		optVor[0] = new OptionVoraussetzung();
		optVor[0].setLinkList(idLinkListArray[0]);
		
		optVor[1] = new OptionVoraussetzung();
		optVor[1].setLinkList(idLinkListArray[1]);
		optVor[0].setAlternativOption(optVor[1]);
		
		optVor[2] = new OptionVoraussetzung();
		optVor[2].setLinkList(idLinkListArray[2]);
		
		// Erstelle Voraussetzung
		Voraussetzung vorauss = new Voraussetzung();
		List<OptionVoraussetzung> optVorList = new ArrayList<OptionVoraussetzung>();
		optVorList.add(optVor[0]);
		optVorList.add(optVor[2]);
		
		vorauss.setPosVoraussetzung(optVorList);
		talent1.setVoraussetzung(vorauss);
		
		
		// Positive Tests
		List<DependencyTableObject> depList;
		
		depList = factory.checkDependencies(
					talent2, 
					StoreDataAccessor.getInstance().getXmlAccessors(), 
					new NullProgressMonitor());
		Assert.assertEquals(1, depList.size());
		Assert.assertEquals(talent1, depList.get(0).getCharElement());
		Assert.assertEquals("Positive Voraussetzungen", depList.get(0).getText());
		Assert.assertEquals(xmlAcc, depList.get(0).getAccessor());
		
		depList = factory.checkDependencies(
				gabe1, 
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				new NullProgressMonitor());
		Assert.assertEquals(1, depList.size());
		Assert.assertEquals(talent1, depList.get(0).getCharElement());
		Assert.assertEquals("Positive Voraussetzungen", depList.get(0).getText());
		Assert.assertEquals(xmlAcc, depList.get(0).getAccessor());
		
		depList = factory.checkDependencies(
				sfk1, 
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				new NullProgressMonitor());
		Assert.assertEquals(1, depList.size());
		Assert.assertEquals(talent1, depList.get(0).getCharElement());
		Assert.assertEquals("Positive Voraussetzungen", depList.get(0).getText());
		Assert.assertEquals(xmlAcc, depList.get(0).getAccessor());
		
		depList = factory.checkDependencies(
				nachteil1, 
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				new NullProgressMonitor());
		Assert.assertEquals(1, depList.size());
		Assert.assertEquals(talent1, depList.get(0).getCharElement());
		Assert.assertEquals("Positive Voraussetzungen", depList.get(0).getText());
		Assert.assertEquals(xmlAcc, depList.get(0).getAccessor());
		
		// Negative Tests
		Talent talent3 = (Talent) factory.buildCharElement(Talent.class, xmlAcc);
		
		depList = factory.checkDependencies(
				talent3, 
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				new NullProgressMonitor());
		Assert.assertEquals(0, depList.size());
		
		depList = factory.checkDependencies(
				talent1, 
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				new NullProgressMonitor());
		Assert.assertEquals(0, depList.size());
	}
	
	// Helper für testCreateCharElement
	@Test 
	public void testCheckDependecyAuswahl() {
		
		// Erstelle CharElemente
		Rasse rasse1 = (Rasse) factory.buildCharElement(Rasse.class, xmlAcc);
		Nachteil nachteil1 = (Nachteil) factory.buildCharElement(Nachteil.class, xmlAcc);
		Nachteil nachteil2 = (Nachteil) factory.buildCharElement(Nachteil.class, xmlAcc);
		Nachteil nachteil3 = (Nachteil) factory.buildCharElement(Nachteil.class, xmlAcc);
		Nachteil nachteil4 = (Nachteil) factory.buildCharElement(Nachteil.class, xmlAcc);
		Nachteil nachteil5 = (Nachteil) factory.buildCharElement(Nachteil.class, xmlAcc);
		
		// Erstelle Link Listen
		List<IdLink>[] idLinkListArray = new ArrayList[3];
		
		idLinkListArray[0] = new ArrayList();
		idLinkListArray[1] = new ArrayList();
		idLinkListArray[2] = new ArrayList();
		
		IdLink link = new IdLink();
		link.setQuelle(rasse1);
		link.setZiel(nachteil1);
		idLinkListArray[0].add(link);
		
		link = new IdLink();
		link.setQuelle(rasse1);
		link.setZiel(nachteil2);
		idLinkListArray[0].add(link);
		
		link = new IdLink();
		link.setQuelle(rasse1);
		link.setZiel(nachteil2);
		link.setZweitZiel(nachteil3);
		idLinkListArray[1].add(link);
		
		link = new IdLink();
		link.setQuelle(rasse1);
		link.setZiel(nachteil4);
		idLinkListArray[2].add(link);
		
		// Erstelle Optionen
		List<Option> optList = new ArrayList<Option>();
		
		OptionAnzahl[] optionAr = new OptionAnzahl[3];
		optionAr[0] = new OptionAnzahl();
		optionAr[0].setLinkList(idLinkListArray[0]);
		optList.add(optionAr[0]);
		
		optionAr[1] = new OptionAnzahl();
		optionAr[1].setLinkList(idLinkListArray[1]);
		optionAr[0].setAlternativOption(optionAr[1]);
		
		optionAr[2] = new OptionAnzahl();
		optionAr[2].setLinkList(idLinkListArray[2]);
		optList.add(optionAr[2]);
		
		// Erstelle Auswahl
		Auswahl<Nachteil> nachteilAuswahl = new Auswahl<Nachteil>();
		nachteilAuswahl.setOptionen(optList);
		
		// Setze Auswahl
		rasse1.setNachteile(nachteilAuswahl);
		
		// Positive Tests
		List<DependencyTableObject> depList;
		
		depList = factory.checkDependencies(
					nachteil1, 
					StoreDataAccessor.getInstance().getXmlAccessors(), 
					new NullProgressMonitor());
		Assert.assertEquals(1, depList.size());
		Assert.assertEquals(rasse1, depList.get(0).getCharElement());
		Assert.assertEquals("Nachteile", depList.get(0).getText());
		Assert.assertEquals(xmlAcc, depList.get(0).getAccessor());
		
		depList = factory.checkDependencies(
				nachteil2, 
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				new NullProgressMonitor());
		Assert.assertEquals(1, depList.size());
		Assert.assertEquals(rasse1, depList.get(0).getCharElement());
		Assert.assertEquals("Nachteile", depList.get(0).getText());
		Assert.assertEquals(xmlAcc, depList.get(0).getAccessor());
		
		depList = factory.checkDependencies(
				nachteil3, 
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				new NullProgressMonitor());
		Assert.assertEquals(1, depList.size());
		Assert.assertEquals(rasse1, depList.get(0).getCharElement());
		Assert.assertEquals("Nachteile", depList.get(0).getText());
		Assert.assertEquals(xmlAcc, depList.get(0).getAccessor());
		
		depList = factory.checkDependencies(
				nachteil4, 
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				new NullProgressMonitor());
		Assert.assertEquals(1, depList.size());
		Assert.assertEquals(rasse1, depList.get(0).getCharElement());
		Assert.assertEquals("Nachteile", depList.get(0).getText());
		Assert.assertEquals(xmlAcc, depList.get(0).getAccessor());
		
		// Negativ Test
		depList = factory.checkDependencies(
				nachteil5, 
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				new NullProgressMonitor());
		Assert.assertEquals(0, depList.size());
	}
	
	@Test
	public void testCheckDependecySimple() {
	// Szenario 1
		Nachteil nachteil1 = (Nachteil) factory.buildCharElement(Nachteil.class, xmlAcc);
		Nachteil nachteil2 = (Nachteil) factory.buildCharElement(Nachteil.class, xmlAcc2);
		Nachteil nachteil3 = (Nachteil) factory.buildCharElement(Nachteil.class, xmlAcc);
		
		Vorteil vorteil1 = (Vorteil) factory.buildCharElement(Vorteil.class, xmlAcc);
		
		nachteil1.setAendertGpNachteil(
			new IdLink[]{
				new IdLink(nachteil1, nachteil2, null, Link.KEIN_WERT, null),
				new IdLink(nachteil1, nachteil3, null, Link.KEIN_WERT, null),
			});
		nachteil1.setAendertGpVorteil(			
			new IdLink[]{
				new IdLink(nachteil1, vorteil1, null, Link.KEIN_WERT, null)
			});
		nachteil2.setAendertGpNachteil(
			new IdLink[]{
				new IdLink(nachteil2, nachteil3, null, Link.KEIN_WERT, null)
			});
		
		// Positive Tests
		List<DependencyTableObject> depList;
		
		depList = factory.checkDependencies(
				nachteil3, 
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				new NullProgressMonitor());
		Assert.assertEquals(2, depList.size());
		Assert.assertEquals(nachteil1, depList.get(0).getCharElement());
		Assert.assertEquals("Verbilligte Nachteile", depList.get(0).getText());
		Assert.assertEquals(xmlAcc, depList.get(0).getAccessor());
		Assert.assertEquals(nachteil2, depList.get(1).getCharElement());
		Assert.assertEquals("Verbilligte Nachteile", depList.get(1).getText());
		Assert.assertEquals(xmlAcc2, depList.get(1).getAccessor());
		
		depList = factory.checkDependencies(
				vorteil1, 
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				new NullProgressMonitor());
		Assert.assertEquals(1, depList.size());
		Assert.assertEquals(nachteil1, depList.get(0).getCharElement());
		Assert.assertEquals("Verbilligte Vorteile", depList.get(0).getText());
		Assert.assertEquals(xmlAcc, depList.get(0).getAccessor());

	// Szenario 2
		Kultur kultur1 = (Kultur) factory.buildCharElement(Kultur.class, xmlAcc);
		RegionVolk regionVolk1 = (RegionVolk) factory.buildCharElement(RegionVolk.class, xmlAcc2);
		
		kultur1.setRegionVolk(regionVolk1);
		
		depList = factory.checkDependencies(
				regionVolk1, 
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				new NullProgressMonitor());
		Assert.assertEquals(1, depList.size());
		Assert.assertEquals(kultur1, depList.get(0).getCharElement());
		Assert.assertEquals("Region/Volk", depList.get(0).getText());
		Assert.assertEquals(xmlAcc, depList.get(0).getAccessor());
		
	// Senario 3
		KulturVariante kv = (KulturVariante) factory.buildHerkunftVariante(KulturVariante.class, kultur1);
		Liturgie liturgie = (Liturgie) factory.buildCharElement(Liturgie.class, xmlAcc);
		kv.setVerbilligteLiturgien(new IdLink[]{new IdLink(kv, liturgie, null, Link.KEIN_WERT, null)});
		kultur1.setVarianten(new KulturVariante[]{kv});
		
		depList = factory.checkDependencies(
				liturgie, 
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				new NullProgressMonitor());
		Assert.assertEquals(1, depList.size());
		Assert.assertEquals(kv, depList.get(0).getCharElement());
		Assert.assertEquals("Verbilligte Liturgien", depList.get(0).getText());
		Assert.assertEquals(xmlAcc, depList.get(0).getAccessor());
	}
	
}
