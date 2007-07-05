/*
 * Created on 13.08.2006
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".*
 */
package guiTest.treeTables;

import guiTest.treeTables.common.EditorPaneTestPane;
import guiTest.treeTables.common.GuiTestUtils;

import javax.swing.JFrame;

import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.gui.editor.panels.PanelVoraussetzungen;
import org.d3s.alricg.gui.komponenten.panels.TabellenPanel;
import org.d3s.alricg.gui.views.SpaltenSchema.SpaltenArt;
import org.d3s.alricg.gui.views.talent.TalentDirektSchema;
import org.d3s.alricg.gui.views.talent.TalentSpalten;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.LinkProzessorFront;
import org.d3s.alricg.prozessor.elementBox.BaseElementBox;
import org.d3s.alricg.prozessor.elementBox.ElementBoxCharElement;
import org.d3s.alricg.store.FactoryFinder;

/**
 * @author Vincent
 *
 */
public class VoraussetzungsPanelTest extends JFrame {
	private Held held;
	private TabellenPanel tabOben;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new VoraussetzungsPanelTest();

	}
	
	public VoraussetzungsPanelTest() {
		initData();
		
		EditorPaneTestPane testPane = new EditorPaneTestPane();
		
		testPane.addTabelle("Talente", tabOben);
		testPane.setPanel(new PanelVoraussetzungen());
		
		testPane.setVisible(true);
	}

	public void initData() {
		TalentDirektSchema direktSchema;
		TalentSpalten spaltenSchema;
		
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
	    
	    tabOben = new TabellenPanel(direktSchema, spaltenSchema, SpaltenArt.objektDirekt);
	    tabOben.setVisible(true);
	}
}
