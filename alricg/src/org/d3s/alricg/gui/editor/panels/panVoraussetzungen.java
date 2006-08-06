/*
 * Created on 31.03.2005 / 23:35:30
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.editor.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.d3s.alricg.charKomponenten.links.Voraussetzung;

/**
 * <u>Beschreibung:</u><br> 
 *
 * @author V. Strelow
 */
public class panVoraussetzungen extends JPanel 
								implements EditorPaneInterface<Voraussetzung> {

	private JPanel jPanel = null;
	private JScrollPane scpGewaehlt = null;
	private JLabel lblGewaehlt = null;
	private JLabel lblAnzeigen = null;
	private JComboBox cbxAnzeigen = null;
	private JLabel lblSuchen = null;
	private JTextField txtSuchen = null;
	private JScrollPane jScrollPane3 = null;
	private JList lstNavi = null;
	private JButton butNeu = null;
	private JTable tblGewaehlt = null;
	private JTable tblAuswahl = null;
	private JScrollPane scpAuswahl = null;
	private JButton butLoeschen = null;
	
	/**
	 * This is the default constructor
	 */
	public panVoraussetzungen() {
		super();
		initialize();
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.editor.panels.EditorPaneInterface#setValues(java.lang.Object)
	 */
	public void loadValues(Voraussetzung element) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.editor.panels.EditorPaneInterface#saveValues()
	 */
	public Voraussetzung saveValues() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		this.setSize(512, 349);
		gridBagConstraints1.gridx = 2;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints2.gridx = 2;
		gridBagConstraints2.gridy = 4;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.weighty = 1.0;
		gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints3.gridx = 2;
		gridBagConstraints3.gridy = 2;
		gridBagConstraints8.gridx = 1;
		gridBagConstraints8.gridy = 4;
		gridBagConstraints8.weightx = 1.0;
		gridBagConstraints8.weighty = 1.0;
		gridBagConstraints8.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints10.gridx = 1;
		gridBagConstraints10.gridy = 5;
		gridBagConstraints12.gridx = 2;
		gridBagConstraints12.gridy = 1;
		gridBagConstraints12.weightx = 1.0;
		gridBagConstraints12.weighty = 1.0;
		gridBagConstraints12.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints13.gridx = 2;
		gridBagConstraints13.gridy = 5;
		this.add(getJPanel(), gridBagConstraints1);
		this.add(getLblGewaehlt(), gridBagConstraints3);
		this.add(getScpAuswahl(), gridBagConstraints12);
		this.add(getScpGewaehlt(), gridBagConstraints2);
		this.add(getJScrollPane3(), gridBagConstraints8);
		this.add(getButNeu(), gridBagConstraints10);
		this.add(getButLoeschen(), gridBagConstraints13);
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			lblSuchen = new JLabel();
			lblAnzeigen = new JLabel();
			jPanel = new JPanel();
			lblAnzeigen.setText("Anzeigen: ");
			lblSuchen.setText("Suchen: ");
			jPanel.add(lblAnzeigen, null);
			jPanel.add(getCbxAnzeigen(), null);
			jPanel.add(lblSuchen, null);
			jPanel.add(getTxtSuchen(), null);
		}
		return jPanel;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getScpGewaehlt() {
		if (scpGewaehlt == null) {
			scpGewaehlt = new JScrollPane();
			scpGewaehlt.setViewportView(getTblGewaehlt());
		}
		return scpGewaehlt;
	}
	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */    
	private JLabel getLblGewaehlt() {
		if (lblGewaehlt == null) {
			lblGewaehlt = new JLabel();
			lblGewaehlt.setText("Gewählte Voraussetzungen: ");
		}
		return lblGewaehlt;
	}
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getCbxAnzeigen() {
		if (cbxAnzeigen == null) {
			cbxAnzeigen = new JComboBox();
		}
		return cbxAnzeigen;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtSuchen() {
		if (txtSuchen == null) {
			txtSuchen = new JTextField();
		}
		return txtSuchen;
	}
	/**
	 * This method initializes jScrollPane3	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane3() {
		if (jScrollPane3 == null) {
			jScrollPane3 = new JScrollPane();
			jScrollPane3.setViewportView(getLstNavi());
		}
		return jScrollPane3;
	}
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getLstNavi() {
		if (lstNavi == null) {
			lstNavi = new JList();
		}
		return lstNavi;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getButNeu() {
		if (butNeu == null) {
			butNeu = new JButton();
			butNeu.setText("Neue");
		}
		return butNeu;
	}
	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	private JTable getTblGewaehlt() {
		if (tblGewaehlt == null) {
			tblGewaehlt = new JTable();
		}
		return tblGewaehlt;
	}
	/**
	 * This method initializes jTable1	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	private JTable getTblAuswahl() {
		if (tblAuswahl == null) {
			tblAuswahl = new JTable();
		}
		return tblAuswahl;
	}
	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getScpAuswahl() {
		if (scpAuswahl == null) {
			scpAuswahl = new JScrollPane();
			scpAuswahl.setViewportView(getTblAuswahl());
		}
		return scpAuswahl;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getButLoeschen() {
		if (butLoeschen == null) {
			butLoeschen = new JButton();
			butLoeschen.setText("Löschen");
		}
		return butLoeschen;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
