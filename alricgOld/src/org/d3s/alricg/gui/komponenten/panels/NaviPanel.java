/*
 * Created on 25.03.2005 / 22:23:06
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.komponenten.panels;

import javax.swing.JPanel;

import javax.swing.JButton;
import javax.swing.JList;
import java.awt.BorderLayout;
/**
 * <u>Beschreibung:</u><br> 
 *
 * @author V. Strelow
 */
public class NaviPanel extends JPanel {

	private JButton butNavigation = null;
	private JList lstNavigation = null;
	/**
	 * This is the default constructor
	 */
	public NaviPanel() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(300,200);
		this.add(getButNavigation(), java.awt.BorderLayout.NORTH);
		this.add(getLstNavigation(), java.awt.BorderLayout.CENTER);
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getButNavigation() {
		if (butNavigation == null) {
			butNavigation = new JButton();
			butNavigation.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		}
		return butNavigation;
	}
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getLstNavigation() {
		if (lstNavigation == null) {
			lstNavigation = new JList();
		}
		return lstNavigation;
	}
  }
