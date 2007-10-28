/*
 * Created 28.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.unitTests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;

import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.held.AllgemeineDaten;
import org.d3s.alricg.store.held.CharakterDaten;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * @author Vincent
 *
 */
public class CharakterTest {
	private static File testFile = new File("testFile.xml");
	private static Marshaller marshaller;
	private static Unmarshaller unmarshaller;
	
	@Test
	public void testUnmarshall() {
		CharakterDaten held = new CharakterDaten();
		
		AllgemeineDaten daten = new AllgemeineDaten();
		daten.setAugenfarbe("Augenfarbe");
		daten.setAussehen("Aussehen");
		
		held.setAllgDaten(daten);
		
		// Speichern und wieder auslesen
		try {
			marshaller.marshal(held, new FileWriter(testFile));
			held = null;
			held = (CharakterDaten) unmarshaller.unmarshal(testFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@BeforeClass
	public static void initClass() throws Exception {
		JAXBContext ctx = JAXBContext.newInstance(CharakterDaten.class);
		marshaller = ctx.createMarshaller();
		// sonst scheint "ß" nicht zu gehen
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1"); 
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		unmarshaller = ctx.createUnmarshaller();
	}
}
