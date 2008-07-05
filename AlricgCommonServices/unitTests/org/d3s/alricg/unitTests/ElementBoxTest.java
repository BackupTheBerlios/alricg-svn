package org.d3s.alricg.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.d3s.alricg.common.charakter.ElementBox;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.junit.Before;
import org.junit.Test;

public class ElementBoxTest {
	ElementBox<IdLink> elementBox;
	IdLink link1, link2, link3, link4, link5, link6;
	CharElement charElem1, charElem2, charElem3, charElem4; 
	
	@Before public void setUp() {
		elementBox = new ElementBox<IdLink>();
		
		Vorteil vorteil = new Vorteil();
		vorteil.setId("VOR-1");
		charElem1 = vorteil;
		
		vorteil = new Vorteil();
		vorteil.setId("VOR-2");
		charElem2 = vorteil;
		
		vorteil = new Vorteil();
		vorteil.setId("VOR-3");
		charElem3 = vorteil;
		
		vorteil = new Vorteil();
		vorteil.setId("VOR-4");
		charElem4 = vorteil;

	}
	
	@Test public void testBasis() {
		link1 = new IdLink(null, charElem1, null, Link.KEIN_WERT, null);
		link2 = new IdLink(null, charElem2, null, 2, "Text");
		link3 = new IdLink(null, charElem3, charElem3, 3, null);
		
		elementBox.add(link1);
		elementBox.add(link2);
		elementBox.add(link3);
		
		assertEquals(3, elementBox.size());
		elementBox.remove(link1);
		assertEquals(2, elementBox.size());
		assertFalse(elementBox.isEmpty());
		elementBox.clear();
		assertEquals(0, elementBox.size());
		assertTrue(elementBox.isEmpty());
	}
	
	@Test public void testContians() {
		link1 = new IdLink(null, charElem1, null, Link.KEIN_WERT, null);
		link2 = new IdLink(null, charElem2, null, 2, "Text");
		link3 = new IdLink(null, charElem3, charElem3, 3, null);
		
		link4 = new IdLink(null, charElem1, null, Link.KEIN_WERT, null);
		link5 = new IdLink(null, charElem2, null, 3, "Text");
		link6 = new IdLink(null, charElem3, charElem3, 3, null);
		
		elementBox.add(link1);
		elementBox.add(link2);
		elementBox.add(link3);
		
		assertTrue(elementBox.contains(link1));
		assertTrue(elementBox.contains(link2));
		assertFalse(elementBox.contains(link4));
		assertFalse(elementBox.contains(link5));
		
		assertTrue(elementBox.contiansEqualObject(link4));
		assertTrue(elementBox.contiansEqualObject(link5));
		assertTrue(elementBox.contiansEqualObject(link6));
		assertTrue(elementBox.contiansEqualObject(new IdLink(null, charElem1, null, 1, null)));
		assertFalse(elementBox.contiansEqualObject(new IdLink(null, charElem2, null, 3, "Text2")));
		assertFalse(elementBox.contiansEqualObject(new IdLink(null, charElem3, charElem2, 3, null)));
		assertFalse(elementBox.contiansEqualObject(new IdLink(null, charElem4, null, Link.KEIN_WERT, null)));
		assertFalse(elementBox.contiansEqualObject(new IdLink(null, charElem1, null, Link.KEIN_WERT, "Bla")));
	}
	
	@Test public void testEquals() {
		link1 = new IdLink(null, charElem1, null, Link.KEIN_WERT, null);
		link2 = new IdLink(null, charElem2, null, 2, "Text");
		link3 = new IdLink(null, charElem3, charElem3, 3, null);
		link4 = new IdLink(null, charElem2, null, 3, "Text2");
		link5 = new IdLink(null, charElem2, null, 3, "Text3");
		link6 = new IdLink(null, charElem3, charElem2, 3, null);
		
		elementBox.add(link1);
		elementBox.add(link2);
		elementBox.add(link3);
		
		assertEquals(link1, elementBox.getObjectById(charElem1));
		assertEquals(link2, elementBox.getObjectById(charElem2));
		assertEquals(link3, elementBox.getObjectById(charElem3));
		
		elementBox.add(link4);
		elementBox.add(link5);
		elementBox.add(link6);
		
		assertEquals(link1, elementBox.getObjectById(charElem1));
		assertTrue(elementBox.getObjectById(charElem3).equals(link6)
				|| elementBox.getObjectById(charElem3).equals(link3));
		
		assertEquals(link1, elementBox.getObjectsById(charElem1).get(0));
		assertEquals(3, elementBox.getObjectsById(charElem2).size());
		assertTrue(elementBox.getObjectsById(charElem2).contains(link2));
		assertTrue(elementBox.getObjectsById(charElem2).contains(link4));
		assertTrue(elementBox.getObjectsById(charElem2).contains(link5));
		assertEquals(2, elementBox.getObjectsById(charElem3).size());
		assertTrue(elementBox.getObjectsById(charElem3).contains(link3));
		assertTrue(elementBox.getObjectsById(charElem3).contains(link6));
		
		assertEquals(link2 , 
				elementBox.getEqualObjects(new IdLink(null, charElem2, null, 2, "Text")).get(0));
		assertEquals(0, 
				elementBox.getEqualObjects(new IdLink(null, charElem2, null, 2, null)).size());
	}
}
