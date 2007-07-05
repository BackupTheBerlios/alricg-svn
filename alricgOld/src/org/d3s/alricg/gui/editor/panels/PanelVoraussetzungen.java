/**
 * Created on 12.08.2006
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.editor.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.links.IdLinkList;
import org.d3s.alricg.charKomponenten.links.Voraussetzung;
import org.d3s.alricg.charKomponenten.links.Voraussetzung.VoraussetzungsAlternative;
import org.d3s.alricg.prozessor.editor.spezial.ProzessorEditorVoraussetzung;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * @author Vincent
 *
 */
public class PanelVoraussetzungen extends JPanel {
	private static final String TEXT_NICHT_ERLAUBT = "Nicht erlaubt";
	private static final String TEXT_VORAUSSETZUNG = "Voraussetzung";
	
	private JPanel panAlternativen = null;
	private JPanel panTopControls = null;
	private JButton butAddVoraussetzung = null;
	private JButton butRemoveVoraussetzung = null;
	private JComboBox cbxVoraussetzungen = null;
	private JLabel lblSpacer = null;
	private JTabbedPane tabAlternativen = null;
	private JPanel panAlternativenTop = null;
	private JButton butAddAlternative = null;
	private JButton butRemoveAlternative = null;
	private JLabel lblSpace2 = null;
	private JComboBox cbxAlternativen = null;
	private JPanel jPanel = null;
	private JCheckBox chkAbWert = null;
	private JComboBox cbxWert = null;
	private JLabel lblSpacer3 = null;
	
	private Voraussetzung voraussetzung;
	private ProzessorEditorVoraussetzung prozessor;
	private HashMap<String, Object> voraussetzungHash;
	private JScrollPane scpTest = null;
	private JTable tabelTest = null;
	
	/**
	 * This is the default constructor
	 */
	public PanelVoraussetzungen() {
		super();
		initialize();
	}

	public void setCharElement(CharElement charElement) {
		prozessor.setTarget(charElement);
		
		this.voraussetzung = charElement.getVoraussetzung();
		
		// Alle alten Einträge löschen
		voraussetzungHash.clear();
		cbxVoraussetzungen.removeAllItems();
		clearAlternativenPanel();

		// Voraussetzungen übernehmen
		if (voraussetzung.getVoraussetzungsAltervativen() != null) {
			for (int i = 0; i < voraussetzung.getVoraussetzungsAltervativen().length; i++) {
				cbxVoraussetzungen.addItem(TEXT_VORAUSSETZUNG + " " + (i+1));
				voraussetzungHash.put(TEXT_VORAUSSETZUNG + " " + (i+1), voraussetzung.getVoraussetzungsAltervativen()[i]);
			}
		}

		cbxVoraussetzungen.addItem(TEXT_NICHT_ERLAUBT);
		voraussetzungHash.put(TEXT_NICHT_ERLAUBT, voraussetzung.getNichtErlaubt());

	}
	
	private void clearAlternativenPanel() {
		cbxAlternativen.removeAllItems();
		chkAbWert.setSelected(false);
		tabAlternativen.removeAll();
		
		cbxAlternativen.setEnabled(true);
		butAddAlternative.setEnabled(true);
		butRemoveAlternative.setEnabled(true);
		chkAbWert.setEnabled(true);
		cbxWert.setEnabled(true);
	}

	private void loadAlternativePanel() {
		clearAlternativenPanel();
		if (cbxVoraussetzungen.getSelectedItem().equals(TEXT_NICHT_ERLAUBT)) {
			cbxAlternativen.setEnabled(false);
			butAddAlternative.setEnabled(false);
			butRemoveAlternative.setEnabled(false);
			chkAbWert.setEnabled(false);
			cbxWert.setEnabled(false);
		}
		
		Object selektiert = voraussetzungHash.get( cbxVoraussetzungen.getSelectedItem() );
		

		
		if (selektiert == null) {
			return;
		} else if (selektiert instanceof VoraussetzungsAlternative) {

			
		} else if (selektiert instanceof IdLinkList) {
			// Der eintrag
			cbxAlternativen.setEnabled(false);
			butAddAlternative.setEnabled(false);
			butRemoveAlternative.setEnabled(false);
			chkAbWert.setEnabled(false);
			cbxWert.setEnabled(false);
			
		}
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(400, 204);
		this.add(getPanAlternativen(), java.awt.BorderLayout.CENTER);
		this.add(getPanTopControls(), java.awt.BorderLayout.NORTH);
		
		this.initDaten();
	}

	private void initDaten() {
		for (int i = 0; i < 21; i++) {
			cbxWert.addItem(i);
		}
		voraussetzungHash = new HashMap<String, Object>();
	}
	
	
	private void mapLinkListToTab(IdLinkList list) {
		if (list == null) return;
		
		tabAlternativen.addTab(null, null, getScpTest(), null);
		scpTest = new JScrollPane();
		scpTest.setViewportView(getTabelTest());
		//SortableTable
		
	}
	
	
	/**
	 * This method initializes panAlternativen	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanAlternativen() {
		if (panAlternativen == null) {
			panAlternativen = new JPanel();
			panAlternativen.setLayout(new BorderLayout());
			panAlternativen.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Voraussetzung: ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			panAlternativen.add(getTabAlternativen(), java.awt.BorderLayout.CENTER);
			panAlternativen.add(getJPanel(), java.awt.BorderLayout.NORTH);
		}
		return panAlternativen;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanTopControls() {
		if (panTopControls == null) {
			lblSpacer = new JLabel();
			lblSpacer.setText("");
			lblSpacer.setMinimumSize(new java.awt.Dimension(20,5));
			lblSpacer.setPreferredSize(new java.awt.Dimension(40,5));
			lblSpacer.setOpaque(true);
			lblSpacer.setMaximumSize(new java.awt.Dimension(40,5));
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			panTopControls = new JPanel();
			panTopControls.setLayout(flowLayout);
			panTopControls.add(getButAddVoraussetzung(), null);
			panTopControls.add(getButRemoveVoraussetzung(), null);
			panTopControls.add(lblSpacer, null);
			panTopControls.add(getCbxVoraussetzungen(), null);
		}
		return panTopControls;
	}

	/**
	 * This method initializes butAddVoraussetzung	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButAddVoraussetzung() {
		if (butAddVoraussetzung == null) {
			butAddVoraussetzung = new JButton();
			butAddVoraussetzung.setText("+");
			butAddVoraussetzung.setMaximumSize(new java.awt.Dimension(20,20));
			butAddVoraussetzung.setMinimumSize(new java.awt.Dimension(20,20));
			butAddVoraussetzung.setPreferredSize(new java.awt.Dimension(20,20));
			butAddVoraussetzung.setMargin(new java.awt.Insets(2,2,2,2));
		}
		return butAddVoraussetzung;
	}

	/**
	 * This method initializes butRemoveVoraussetzung	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButRemoveVoraussetzung() {
		if (butRemoveVoraussetzung == null) {
			butRemoveVoraussetzung = new JButton();
			butRemoveVoraussetzung.setText("-");
			butRemoveVoraussetzung.setMargin(new java.awt.Insets(2,2,2,2));
			butRemoveVoraussetzung.setMaximumSize(new java.awt.Dimension(20,20));
			butRemoveVoraussetzung.setMinimumSize(new java.awt.Dimension(20,20));
			butRemoveVoraussetzung.setActionCommand("-");
			butRemoveVoraussetzung.setPreferredSize(new java.awt.Dimension(20,20));
		}
		return butRemoveVoraussetzung;
	}

	/**
	 * This method initializes cbxVoraussetzungen	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbxVoraussetzungen() {
		if (cbxVoraussetzungen == null) {
			cbxVoraussetzungen = new JComboBox();
			cbxVoraussetzungen.addActionListener(
					new  ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							cbxVoraussetzungen_Action(arg0);
						}
					}
			);
		}
		return cbxVoraussetzungen;
	}

	private void cbxVoraussetzungen_Action(ActionEvent event) {
		System.out.println("*********************");
	}
	
	/**
	 * This method initializes tabAlternativen	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getTabAlternativen() {
		if (tabAlternativen == null) {
			tabAlternativen = new JTabbedPane();
			tabAlternativen.addTab(null, null, getScpTest(), null);
		}
		return tabAlternativen;
	}

	/**
	 * This method initializes panAlternativenTop	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanAlternativenTop() {
		if (panAlternativenTop == null) {
			lblSpacer3 = new JLabel();
			lblSpacer3.setMaximumSize(new java.awt.Dimension(20,5));
			lblSpacer3.setOpaque(true);
			lblSpacer3.setPreferredSize(new java.awt.Dimension(20,5));
			lblSpacer3.setText("");
			lblSpacer3.setMinimumSize(new Dimension(20, 5));
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
			lblSpace2 = new JLabel();
			lblSpace2.setMaximumSize(new java.awt.Dimension(40,5));
			lblSpace2.setOpaque(true);
			lblSpace2.setPreferredSize(new java.awt.Dimension(40,5));
			lblSpace2.setText("");
			lblSpace2.setMinimumSize(new java.awt.Dimension(20,5));
			panAlternativenTop = new JPanel();
			panAlternativenTop.setLayout(flowLayout1);
			panAlternativenTop.add(getButAddAlternative(), null);
			panAlternativenTop.add(getButRemoveAlternative(), null);
			panAlternativenTop.add(lblSpace2, null);
			panAlternativenTop.add(getCbxAlternativen(), null);
			panAlternativenTop.add(lblSpacer3, null);
			panAlternativenTop.add(getCbxAbWert(), null);
			panAlternativenTop.add(getCbxWert(), null);
		}
		return panAlternativenTop;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButAddAlternative() {
		if (butAddAlternative == null) {
			butAddAlternative = new JButton();
			butAddAlternative.setMaximumSize(new java.awt.Dimension(20,20));
			butAddAlternative.setPreferredSize(new java.awt.Dimension(20,20));
			butAddAlternative.setMargin(new java.awt.Insets(2,2,2,2));
			butAddAlternative.setText("+");
			butAddAlternative.setMinimumSize(new java.awt.Dimension(20,20));
		}
		return butAddAlternative;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButRemoveAlternative() {
		if (butRemoveAlternative == null) {
			butRemoveAlternative = new JButton();
			butRemoveAlternative.setMaximumSize(new java.awt.Dimension(20,20));
			butRemoveAlternative.setPreferredSize(new java.awt.Dimension(20,20));
			butRemoveAlternative.setActionCommand("-");
			butRemoveAlternative.setMargin(new java.awt.Insets(2,2,2,2));
			butRemoveAlternative.setText("-");
			butRemoveAlternative.setMinimumSize(new java.awt.Dimension(20,20));
		}
		return butRemoveAlternative;
	}

	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbxAlternativen() {
		if (cbxAlternativen == null) {
			cbxAlternativen = new JComboBox();
			cbxAlternativen.addItem("Alternative 1");
		}
		return cbxAlternativen;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(getJPanel(), BoxLayout.Y_AXIS));
			jPanel.add(getPanAlternativenTop(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes cbxAbWert	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getCbxAbWert() {
		if (chkAbWert == null) {
			chkAbWert = new JCheckBox();
			chkAbWert.setText("Gültig ab Wert");
		}
		return chkAbWert;
	}

	/**
	 * This method initializes cbxWert	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbxWert() {
		if (cbxWert == null) {
			cbxWert = new JComboBox();
		}
		return cbxWert;
	}

	/**
	 * This method initializes scpTest	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScpTest() {
		if (scpTest == null) {
			scpTest = new JScrollPane();
			scpTest.setViewportView(getTabelTest());
		}
		return scpTest;
	}

	/**
	 * This method initializes tabelTest	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTabelTest() {
		if (tabelTest == null) {
			tabelTest = new JTable();
		}
		return tabelTest;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
