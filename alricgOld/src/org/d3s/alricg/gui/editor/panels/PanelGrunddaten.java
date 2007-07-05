/*
 * Created on 26.03.2005 / 20:08:36
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.editor.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.gui.komponenten.TextFieldList;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.TextStore;
/**
 * <u>Beschreibung:</u><br> 
 *
 * @author V. Strelow
 */
public class PanelGrunddaten<CharElement> extends JPanel 
						implements EditorPaneInterface<CharElement> {

	private JLabel lblName = null;
	private JTextField txtName = null;  //  @jve:decl-index=0:
	private JLabel lblSonderregel = null;
	private JLabel lblSammelbegriff = null;
	private TextFieldList txtSammelbegriff = null;
	private JCheckBox cbxAnzeigen = null;
	private JLabel lblRegelzusatz = null;
	private JTable tblRegelzusatz = null;
	private JScrollPane scpRegelzusatz = null;
	private JLabel lblBeschreibung = null;
	private JTextField txtAnzeigen = null;
	private JButton butAddRegelzusatz = null;
	private JComboBox cbxSonderregel = null;
	private JLabel lblId = null;
	private JScrollPane scpBeschreibung = null;
	private JTextArea txaBeschreibung = null;
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.editor.panels.EditorPaneInterface#setValues(java.lang.Object)
	 */
	public void loadValues(CharElement element) {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.editor.panels.EditorPaneInterface#saveValues()
	 */
	public CharElement saveValues() {
		// TODO Auto-generated method stub
		return (CharElement) null;
	}
	
	/**
	 * This is the default constructor
	 */
	public PanelGrunddaten() {
		super();
		initialize();
		initLabelTexte();
		initToolTip();
	}
	
	/**
	 * Setzt die Texte der Labels von diesem Panel
	 */
	private void initLabelTexte() {
        final TextStore library = FactoryFinder.find().getLibrary();
        lblName.setText(library.getShortTxt("Name") + ":");
        lblBeschreibung.setText(library.getShortTxt("Beschreibung") + ":");
        lblSonderregel.setText(library.getShortTxt("Sonderregel") + ":");
        lblSammelbegriff.setText(library.getShortTxt("Sammelbegriff") + ":");
        lblRegelzusatz.setText(library.getShortTxt("Regelzusatz") + ":");
        lblId.setText("(" + library.getShortTxt("id") + ")");
	}
	
	private void initToolTip() {
		
	}
	
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtName() {
		if (txtName == null) {
			txtName = new JTextField();
		}
		return txtName;
	}
	/**
	 * This method initializes jTextField2	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private TextFieldList getTxtSammelbegriff() {
		if (txtSammelbegriff == null) {
			txtSammelbegriff = new TextFieldList();
			txtSammelbegriff.setMinimumSize(new java.awt.Dimension(16,22));
		}
		return txtSammelbegriff;
	}
	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox getCbxAnzeigen() {
		if (cbxAnzeigen == null) {
			cbxAnzeigen = new JCheckBox();
			cbxAnzeigen.setText(FactoryFinder.find().getLibrary().getShortTxt("Anzeigen"));
			cbxAnzeigen.setSelected(true);
		}
		return cbxAnzeigen;
	}
	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	private JTable getTblRegelzusatz() {
		if (tblRegelzusatz == null) {
			tblRegelzusatz = new JTable();
		}
		return tblRegelzusatz;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getScpRegelzusatz() {
		if (scpRegelzusatz == null) {
			scpRegelzusatz = new JScrollPane();
			scpRegelzusatz.setViewportView(getTblRegelzusatz());
			scpRegelzusatz.setMinimumSize(new java.awt.Dimension(75,50));
			scpRegelzusatz.setSize(75,50);
			scpRegelzusatz.setPreferredSize(new java.awt.Dimension(453,50));
			scpRegelzusatz.setMaximumSize(new java.awt.Dimension(32767,50));
		}
		return scpRegelzusatz;
	}
	/**
	 * This method initializes jTextField3	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtAnzeigen() {
		if (txtAnzeigen == null) {
			txtAnzeigen = new JTextField();
		}
		return txtAnzeigen;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getButAddRegelzusatz() {
		if (butAddRegelzusatz == null) {
			butAddRegelzusatz = new JButton();
			butAddRegelzusatz.setText(FactoryFinder.find().getLibrary().getShortTxt("Neu"));
			butAddRegelzusatz.setMargin(new java.awt.Insets(1,4,1,4));
			butAddRegelzusatz.setPreferredSize(new java.awt.Dimension(36,16));
			butAddRegelzusatz.setMinimumSize(new java.awt.Dimension(36,16));
		}
		return butAddRegelzusatz;
	}
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getCbxSonderregel() {
		if (cbxSonderregel == null) {
			cbxSonderregel = new JComboBox();
			cbxSonderregel.setPreferredSize(new java.awt.Dimension(25,18));
			cbxSonderregel.setMinimumSize(new java.awt.Dimension(25,18));
		}
		return cbxSonderregel;
	}
	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getScpBeschreibung() {
		if (scpBeschreibung == null) {
			scpBeschreibung = new JScrollPane();
			scpBeschreibung.setViewportView(getTxaBeschreibung());
			scpBeschreibung.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scpBeschreibung.setPreferredSize(new java.awt.Dimension(434,35));
			scpBeschreibung.setMinimumSize(new java.awt.Dimension(20,35));
		}
		return scpBeschreibung;
	}
	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */    
	private JTextArea getTxaBeschreibung() {
		if (txaBeschreibung == null) {
			txaBeschreibung = new JTextArea();
			txaBeschreibung.setLineWrap(true);
		}
		return txaBeschreibung;
	}
                   	public static void main(String[] args) {
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
        TitledBorder titledBorder30 = BorderFactory.createTitledBorder(null, "Grunddaten: ", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null);
        GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
        lblId = new JLabel();
        GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
        lblBeschreibung = new JLabel();
        lblRegelzusatz = new JLabel();
        lblSammelbegriff = new JLabel();
        lblSonderregel = new JLabel();
        lblName = new JLabel();
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new java.awt.Dimension(400,10));
        this.setSize(554, 163);
        this.setLocation(0, 0);
        gridBagConstraints8.gridx = 0;
        gridBagConstraints8.gridy = 0;
        gridBagConstraints8.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints8.insets = new java.awt.Insets(0,0,0,5);
        lblName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints9.gridx = 1;
        gridBagConstraints9.gridy = 0;
        gridBagConstraints9.weightx = 1.0;
        gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints10.gridx = 3;
        gridBagConstraints10.gridy = 0;
        gridBagConstraints10.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints10.insets = new java.awt.Insets(0,15,0,5);
        gridBagConstraints12.gridx = 0;
        gridBagConstraints12.gridy = 1;
        gridBagConstraints12.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints12.insets = new java.awt.Insets(0,0,0,5);
        gridBagConstraints13.gridx = 1;
        gridBagConstraints13.gridy = 1;
        gridBagConstraints13.weightx = 1.0;
        gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints15.gridx = 3;
        gridBagConstraints15.gridy = 1;
        gridBagConstraints15.insets = new java.awt.Insets(0,15,0,0);
        gridBagConstraints16.gridx = 0;
        gridBagConstraints16.gridy = 2;
        gridBagConstraints16.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints16.insets = new java.awt.Insets(0,0,0,5);
        lblRegelzusatz.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints21.gridx = 0;
        gridBagConstraints21.gridy = 3;
        gridBagConstraints21.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints21.insets = new java.awt.Insets(4,0,0,5);
        gridBagConstraints17.gridheight = 2;
        gridBagConstraints17.insets = new java.awt.Insets(0,0,0,3);
        gridBagConstraints20.insets = new java.awt.Insets(0,0,0,3);
        gridBagConstraints25.gridx = 4;
        gridBagConstraints25.gridy = 0;
        gridBagConstraints25.weightx = 1.0;
        gridBagConstraints25.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints26.gridx = 2;
        gridBagConstraints26.gridy = 0;
        gridBagConstraints26.insets = new java.awt.Insets(0,2,0,0);
        gridBagConstraints13.gridwidth = 2;
        this.setBorder(titledBorder30);
        gridBagConstraints17.gridx = 1;
        gridBagConstraints17.gridy = 2;
        gridBagConstraints17.weightx = 1.0;
        gridBagConstraints17.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints17.gridwidth = 4;
        gridBagConstraints17.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints18.gridx = 0;
        gridBagConstraints18.gridy = 6;
        gridBagConstraints18.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints18.insets = new java.awt.Insets(3,0,0,5);
        gridBagConstraints20.gridx = 4;
        gridBagConstraints20.gridy = 1;
        gridBagConstraints20.weightx = 1.0;
        gridBagConstraints20.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints29.gridx = 1;
        gridBagConstraints29.gridy = 6;
        gridBagConstraints29.weightx = 1.0;
        gridBagConstraints29.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints29.gridwidth = 4;
        gridBagConstraints29.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints29.insets = new java.awt.Insets(3,0,0,3);
        this.add(getTxtSammelbegriff(), gridBagConstraints13);
        this.add(lblSonderregel, gridBagConstraints10);
        this.add(getCbxAnzeigen(), gridBagConstraints15);
        this.add(lblName, gridBagConstraints8);
        this.add(getTxtName(), gridBagConstraints9);
        this.add(lblSammelbegriff, gridBagConstraints12);
        this.add(lblRegelzusatz, gridBagConstraints16);
        this.add(getScpRegelzusatz(), gridBagConstraints17);
        this.add(lblBeschreibung, gridBagConstraints18);
        this.add(getTxtAnzeigen(), gridBagConstraints20);
        this.add(getButAddRegelzusatz(), gridBagConstraints21);
        this.add(getCbxSonderregel(), gridBagConstraints25);
        this.add(lblId, gridBagConstraints26);
        this.add(getScpBeschreibung(), gridBagConstraints29);
	}


}  //  @jve:decl-index=0:visual-constraint="-18,13"
