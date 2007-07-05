/*
 * Created on 13.08.2006
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".*
 */
package guiTest.treeTables.common;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

/**
 * @author Vincent
 *
 */
public class EditorPaneTestPane extends JFrame {

	private JPanel jContentPane = null;
	private JSplitPane splSplitContainer = null;
	private JPanel panAuswahlTab = null;
	private JTabbedPane tabTabellen = null;
	/**
	 * This is the default constructor
	 */
	public EditorPaneTestPane() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
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
			jContentPane.add(getSplSplitContainer(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes splSplitContainer	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getSplSplitContainer() {
		if (splSplitContainer == null) {
			splSplitContainer = new JSplitPane();
			splSplitContainer.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
			splSplitContainer.setTopComponent(getPanAuswahlTab());
		}
		return splSplitContainer;
	}

	/**
	 * This method initializes panAuswahlTab	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanAuswahlTab() {
		if (panAuswahlTab == null) {
			panAuswahlTab = new JPanel();
			panAuswahlTab.setLayout(new BorderLayout());
			panAuswahlTab.add(getTabTabellen(), java.awt.BorderLayout.CENTER);
		}
		return panAuswahlTab;
	}

	/**
	 * This method initializes tabTabellen	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getTabTabellen() {
		if (tabTabellen == null) {
			tabTabellen = new JTabbedPane();
			tabTabellen.setPreferredSize(new java.awt.Dimension(50,70));
		}
		return tabTabellen;
	}
	
	public void addTabelle(String titel, JPanel pan) {
		tabTabellen.addTab(titel, pan);
	}
	
	public void setPanel(JPanel pan) {
		splSplitContainer.setBottomComponent(pan);
	}

}
