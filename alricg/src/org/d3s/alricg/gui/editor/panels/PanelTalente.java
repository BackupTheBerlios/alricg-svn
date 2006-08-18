/*
 * Created on 27.03.2005 / 00:32:30
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.editor.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.d3s.alricg.charKomponenten.Talent;
/**
 * <u>Beschreibung:</u><br> 
 *
 * @author V. Strelow
 */
public class PanelTalente extends JPanel 
						implements EditorPaneInterface<Talent> {

	private JLabel lblSorte = null;
	private JLabel lblArt = null;
	private JComboBox jComboBox = null;
	private JComboBox jComboBox1 = null;
	private JLabel jLabel2 = null;
	private JComboBox jComboBox2 = null;
	private JComboBox jComboBox3 = null;
	private JComboBox jComboBox4 = null;
	private JLabel jLabel3 = null;
	private JComboBox jComboBox5 = null;
	/**
	 * This is the default constructor
	 */
	public PanelTalente() {
		super();
		initialize();
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.editor.panels.EditorPaneInterface#setValues(java.lang.Object)
	 */
	public void loadValues(Talent element) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.editor.panels.EditorPaneInterface#saveValues()
	 */
	public Talent saveValues() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		jLabel3 = new JLabel();
		jLabel2 = new JLabel();
		lblArt = new JLabel();
		lblSorte = new JLabel();
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints37 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints39 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		this.setSize(465, 282);
		gridBagConstraints31.gridx = 0;
		gridBagConstraints31.gridy = 1;
		lblSorte.setText("Sorte:");
		this.add(lblSorte, gridBagConstraints31);
		this.add(lblArt, gridBagConstraints32);
		this.add(getJComboBox(), gridBagConstraints33);
		this.add(getJComboBox1(), gridBagConstraints34);
		this.add(jLabel2, gridBagConstraints35);
		this.add(getJComboBox2(), gridBagConstraints36);
		this.add(getJComboBox3(), gridBagConstraints37);
		this.add(getJComboBox4(), gridBagConstraints38);
		this.add(jLabel3, gridBagConstraints39);
		this.add(getJComboBox5(), gridBagConstraints40);
		gridBagConstraints32.gridx = 2;
		gridBagConstraints32.gridy = 1;
		lblArt.setText("Art:");
		gridBagConstraints33.gridx = 1;
		gridBagConstraints33.gridy = 1;
		gridBagConstraints33.weightx = 1.0;
		gridBagConstraints33.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints34.gridx = 3;
		gridBagConstraints34.gridy = 1;
		gridBagConstraints34.weightx = 1.0;
		gridBagConstraints34.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints35.gridx = 0;
		gridBagConstraints35.gridy = 2;
		jLabel2.setText("Probe:");
		gridBagConstraints36.gridx = 1;
		gridBagConstraints36.gridy = 2;
		gridBagConstraints36.weightx = 1.0;
		gridBagConstraints36.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints37.gridx = 2;
		gridBagConstraints37.gridy = 2;
		gridBagConstraints37.weightx = 1.0;
		gridBagConstraints37.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints38.gridx = 3;
		gridBagConstraints38.gridy = 2;
		gridBagConstraints38.weightx = 1.0;
		gridBagConstraints38.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints39.gridx = 4;
		gridBagConstraints39.gridy = 2;
		jLabel3.setText("Kostenklasse:");
		gridBagConstraints40.gridx = 5;
		gridBagConstraints40.gridy = 2;
		gridBagConstraints40.weightx = 1.0;
		gridBagConstraints40.fill = java.awt.GridBagConstraints.HORIZONTAL;
	}
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
		}
		return jComboBox;
	}
	/**
	 * This method initializes jComboBox1	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getJComboBox1() {
		if (jComboBox1 == null) {
			jComboBox1 = new JComboBox();
		}
		return jComboBox1;
	}
	/**
	 * This method initializes jComboBox2	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getJComboBox2() {
		if (jComboBox2 == null) {
			jComboBox2 = new JComboBox();
		}
		return jComboBox2;
	}
	/**
	 * This method initializes jComboBox3	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getJComboBox3() {
		if (jComboBox3 == null) {
			jComboBox3 = new JComboBox();
		}
		return jComboBox3;
	}
	/**
	 * This method initializes jComboBox4	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getJComboBox4() {
		if (jComboBox4 == null) {
			jComboBox4 = new JComboBox();
		}
		return jComboBox4;
	}
	/**
	 * This method initializes jComboBox5	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getJComboBox5() {
		if (jComboBox5 == null) {
			jComboBox5 = new JComboBox();
		}
		return jComboBox5;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
