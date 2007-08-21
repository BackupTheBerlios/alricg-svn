/*
 * Created 26.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.unitTests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.d3s.alricg.store.access.IdFactory;
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.Talent;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Vincent
 *
 */
public class IdFactoryTest {

	@BeforeClass
	public static void init() {
		StoreAccessor.getInstance().addNewFile(new File("tmpTestFile.test"));
		StoreDataAccessor storeData = StoreDataAccessor.getInstance();
		
		List<Talent> talList = new ArrayList<Talent>();
		
		Talent talent =  new Talent();
		talent.setName("Schwerter");
		talent.setId("TAL-schwerter");
		talList.add(talent);
		
		talent = new Talent();
		talent.setName("Orks Knuffeln");
		talent.setId("TAL-orks_knuffeln");
		talList.add(talent);
		
		talent = new Talent();
		talent.setName("Klettern1");
		talent.setId("TAL-klettern");
		talList.add(talent);
		
		talent = new Talent();
		talent.setName("Klettern2");
		talent.setId("TAL-klettern+");
		talList.add(talent);
		
		talent = new Talent();
		talent.setName("Klettern3");
		talent.setId("TAL-klettern++");
		talList.add(talent);
		
		storeData.getXmlAccessors().get(0).setTalentList(talList);
	}
	
	@Test
	public void testLoad() throws Exception {
		IdFactory factory = IdFactory.getInstance();
		
		Assert.assertEquals("TAL-falsch_spielen", factory.getId(Talent.class, "Falsch Spielen"));
		Assert.assertEquals("TAL-la_12_la_дц_lo", factory.getId(Talent.class, " la 12 la д÷ LO  "));
		Assert.assertEquals("TAL-la-12__", factory.getId(Talent.class, "la-12 я "));
		
		Assert.assertEquals("TAL-orks_knuffeln+", factory.getId(Talent.class, "Orks Knuffeln"));
		Assert.assertEquals("TAL-klettern+++", factory.getId(Talent.class, "Klettern"));
		


	}
}
