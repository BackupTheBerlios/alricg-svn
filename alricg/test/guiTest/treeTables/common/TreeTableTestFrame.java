/**
 * 
 */
package guiTest.treeTables.common;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.d3s.alricg.gui.komponenten.panels.TabellenPanel;

/**
 * Frame zum Testen der TreeTables. Hier können zwei zusammengehörige treeTables eingefühgt werden,
 * um deren korrekte Arbeitsweise zu überprüfen.
 * 
 * @author Vincent
 */
public class TreeTableTestFrame extends JFrame {

	private JPanel jContentPane = null;
	private JSplitPane jSplitPane = null;
	
	/**
	 * Zum einfügen der Tabellen Panels mit den entsprechenden TreeTables
	 * @param tabOben
	 * @param tabUnten
	 */
	public void setTreeTables(TabellenPanel tabOben, TabellenPanel tabUnten) {
		jSplitPane.setTopComponent(tabOben);
		jSplitPane.setBottomComponent(tabUnten);
	}
	
	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
			jSplitPane.setDividerLocation(100);
			jSplitPane.setDividerSize(5);
		}
		return jSplitPane;
	}

	/**
	 * This is the default constructor
	 */
	public TreeTableTestFrame() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 250);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJSplitPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}
	
	

}
