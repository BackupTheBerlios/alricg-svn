/*
 * Created on 13.08.2006
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".*
 */
package guiTest.treeTables;

import guiTest.treeTables.common.GuiTestUtils;
import guiTest.treeTables.common.TreeTableTestFrame;

import java.util.List;

import org.d3s.alricg.charKomponenten.Rasse;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.gui.komponenten.panels.TabellenPanel;
import org.d3s.alricg.gui.views.SpaltenSchema.SpaltenArt;
import org.d3s.alricg.gui.views.talent.TalentDirektSchema;
import org.d3s.alricg.gui.views.talent.TalentLinkSchema;
import org.d3s.alricg.gui.views.talent.TalentSpalten;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.LinkProzessorFront;
import org.d3s.alricg.prozessor.elementBox.BaseElementBox;
import org.d3s.alricg.prozessor.elementBox.ElementBoxCharElement;
import org.d3s.alricg.prozessor.elementBox.ElementBoxLink;
import org.d3s.alricg.store.FactoryFinder;

/**
 * @author Vincent
 */
public class TalentTreeTables {
	private TalentDirektSchema direktSchema;
	private TalentLinkSchema linkSchema;
	private TalentSpalten spaltenSchema;
	private Held held;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TalentTreeTables();
	}

	public TalentTreeTables() {
		TabellenPanel tabOben;
		TabellenPanel tabUnten;
		TreeTableTestFrame testFrame;
		
		initData();
		
		tabOben = new TabellenPanel(direktSchema, spaltenSchema, SpaltenArt.objektDirekt);
		tabUnten = new TabellenPanel(linkSchema, spaltenSchema, SpaltenArt.objektLinkGen);
		
		((LinkProzessorFront) held.getProzessor(CharKomponente.talent)).registerObserver(tabUnten);
				
		testFrame = new TreeTableTestFrame();
		
		testFrame.setTreeTables(tabOben, tabUnten);
		testFrame.setVisible(true);
	}
	
	public void initData() {
		held = GuiTestUtils.initData();
		
		LinkProzessorFront prozessor = held.getProzessor(CharKomponente.talent);
		
		// SpaltenSchema erzeugen
		spaltenSchema = new TalentSpalten();
		
	    // Einfügen der direkten Talente
		BaseElementBox box = new ElementBoxCharElement();
	    box.addAll( FactoryFinder.find().getData().getUnmodifieableCollection(CharKomponente.talent) );
	    
	    direktSchema = new TalentDirektSchema( held );
	    direktSchema.setProzessor(prozessor);
	    direktSchema.setElementBox(box);
		
	    // Einfügen der Links (lediglich 5 zum besseren Testen)
	    ElementBoxLink boxLink = new ElementBoxLink();
	    List list = box.getUnmodifiableList();
	    Rasse r = new Rasse("testRasse");
	    r.setName("TestRasse");
	    
	    for (int i = 0; i < 5; i++) {
	    	IdLink linkRasse = new IdLink(r, null);
	    	linkRasse.setZiel((Talent) list.get(i));
	    	linkRasse.setWert(i);
	    	
	    	prozessor.addNewElement((Talent) list.get(i));
	    	prozessor.addModi(linkRasse);
	    }
	    
	    // Basis-Talente hinzufügen
	    for (int i = 0; i < list.size(); i++) {
	    	if (((Talent) list.get(i)).getArt() == Talent.Art.basis) {
	        	prozessor.addNewElement((Talent) list.get(i));
	    	}
	    }
	    
	    linkSchema = new TalentLinkSchema( held, SpaltenArt.objektLinkGen );
	    linkSchema.setProzessor(prozessor);
	    linkSchema.setElementBox(prozessor.getElementBox());
	}
}
