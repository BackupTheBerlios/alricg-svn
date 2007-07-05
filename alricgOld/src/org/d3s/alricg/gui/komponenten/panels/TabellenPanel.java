/*
 * Created on 12.09.2005 / 17:32:17
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.komponenten.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.d3s.alricg.gui.ProzessorObserver;
import org.d3s.alricg.gui.komponenten.table.SortableTable;
import org.d3s.alricg.gui.komponenten.table.SortableTableModel;
import org.d3s.alricg.gui.komponenten.table.SortableTreeTable;
import org.d3s.alricg.gui.komponenten.table.SortableTreeTableModel;
import org.d3s.alricg.gui.views.SpaltenSchema;
import org.d3s.alricg.gui.views.TypSchema;
import org.d3s.alricg.gui.views.SpaltenSchema.SpaltenArt;

/**
 * <u>Beschreibung:</u><br> 
 * Dieses Panel dient der Anzeige aller "standart" Tabellen mit 
 * CharElementen (Talente, Zauber, Professionen, SF, usw.). 
 * @author V. Strelow
 */
public class TabellenPanel extends JPanel implements ProzessorObserver {
	
	private JPanel optionsPanel = null;
	private JLabel lblOrdnung = null;
	private JComboBox cbxOrdnung = null;
	private JLabel lblFilter = null;
	private JComboBox cbxFilter = null;
	private JScrollPane scpTabelle = null;
	
	private SortableTable sortableTable = null;
	public SortableTreeTable sortableTreeTable = null; // TODO Public entfernen! Nur zum Test!
	//private SortableTreeOrTableInterface currentTableModel = null;
	private SortableTableModel tableModel;
	private SortableTreeTableModel treeModel;

	
	/**
	 * This is the default constructor
	 */
	public TabellenPanel(TypSchema typSchema, SpaltenSchema spaltenSchema, SpaltenArt spaltenArt) {
		
		// init der Daten
		initializeData(typSchema, spaltenSchema, spaltenArt);
		
		// init der Darstellung der Daten
		initialize(spaltenSchema, spaltenArt);
	}
	
	private void initializeData(TypSchema typSchema, SpaltenSchema spaltenSchema, SpaltenArt spaltenArt) {
		
		// Modelle für TreeTable und Table erzeugen
		tableModel = new SortableTableModel(spaltenSchema, typSchema, spaltenArt);
		treeModel = new SortableTreeTableModel(spaltenSchema, typSchema, spaltenArt);
		
		// TreeTable und Table erzeugen
		sortableTreeTable = new SortableTreeTable(treeModel);
		sortableTable = new SortableTable();
		sortableTable.setModel(tableModel);
		
		// Daten setzen
		tableModel.setData( typSchema.getElementBox().getUnmodifiableList() );
		treeModel.setData( typSchema.getElementBox().getUnmodifiableList() );

		// Tabelle initialisiren
		spaltenSchema.initTable(typSchema.getProzessor(), sortableTreeTable, spaltenArt);
		spaltenSchema.initTable(typSchema.getProzessor(), sortableTable, spaltenArt);
	}
	
	private void stateChangedOrdnung(ItemEvent e) {
		
		if (((Enum) e.getItem()).ordinal() == 0) {
			// Das Element "0" ist stehts "keine Ordnung", und somit eine einfach Tabelle
			scpTabelle.setViewportView(sortableTable);
			
		} else {
			// Neue Ordnung setzen
			treeModel.setOrdnung((Enum) e.getItem());
			
			if (scpTabelle.getViewport().getView().equals(sortableTable)) {
				// Falls eine einfache Tabelle angeziegt wird, wird der Viewport auf TreeTable geändert
				treeModel.setOrdnung((Enum) e.getItem());
				scpTabelle.setViewportView(sortableTreeTable);
			}
		}

	}
	
	private void stateChangedFilter(ItemEvent e) {
		tableModel.setFilter((Enum) e.getItem());
		treeModel.setFilter((Enum) e.getItem());
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(SpaltenSchema spaltenSchema, SpaltenArt spaltenArt) {
		this.setLayout(new BorderLayout());
		this.setSize(300, 200);
		this.add(getOptionsPanel(spaltenSchema, spaltenArt), java.awt.BorderLayout.NORTH);
		this.add(getScpTabelle(), java.awt.BorderLayout.CENTER);
		this.getScpTabelle().setViewportView(sortableTreeTable);
	}
	
	/**
	 * This method initializes optionsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getOptionsPanel(SpaltenSchema spaltenSchema, SpaltenArt spaltenArt) {
		if (optionsPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			lblFilter = new JLabel();
			lblFilter.setText("Filter:");
			lblOrdnung = new JLabel();
			lblOrdnung.setText("Ordnung:");
			optionsPanel = new JPanel();
			optionsPanel.setLayout(flowLayout);
			optionsPanel.add(lblOrdnung, null);
			optionsPanel.add(getCbxOrdnung(spaltenSchema, spaltenArt), null);
			optionsPanel.add(lblFilter, null);
			optionsPanel.add(getCbxFilter(spaltenSchema, spaltenArt), null);
		}
		return optionsPanel;
	}

	/**
	 * This method initializes cbxOrdnung	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getCbxOrdnung(SpaltenSchema spaltenSchema, SpaltenArt spaltenArt) {
		if (cbxOrdnung == null) {
			cbxOrdnung = new JComboBox();
			
			// Wählbare Elemente hinzufügen
			for (int i = 0; i < spaltenSchema.getOrdnungElem(spaltenArt).length; i++) {
				cbxOrdnung.addItem(spaltenSchema.getOrdnungElem(spaltenArt)[i]);
			}
			if (cbxOrdnung.getItemCount() > 0) {
				cbxOrdnung.setSelectedIndex(1);
			}
			
			cbxOrdnung.addItemListener(new java.awt.event.ItemListener() { 
				public void itemStateChanged(java.awt.event.ItemEvent e) {    
					stateChangedOrdnung(e);
				}
			});
		}
		return cbxOrdnung;
	}

	/**
	 * This method initializes cbxFilter	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getCbxFilter(SpaltenSchema spaltenSchema, SpaltenArt spaltenArt) {
		if (cbxFilter == null) {
			cbxFilter = new JComboBox();
			
			// Wählbare Elemente hinzufügen
			for (int i = 0; i < spaltenSchema.getFilterElem(spaltenArt).length; i++) {
				cbxFilter.addItem(spaltenSchema.getFilterElem(spaltenArt)[i]);
			}
			if (cbxFilter.getItemCount() > 0) {
				cbxFilter.setSelectedIndex(0);
			}
			
			cbxFilter.addItemListener(new java.awt.event.ItemListener() { 
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					stateChangedFilter(e);
				}
			});
		}
		return cbxFilter;
	}

	/**
	 * This method initializes scpTabelle	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getScpTabelle() {
		if (scpTabelle == null) {
			scpTabelle = new JScrollPane();
		}
		return scpTabelle;
	}

	//--------------- Interface ProzessorObserver --------------
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.ProzessorObserver#addElement(java.lang.Object)
	 */
	public void addElement(Object obj) {
		tableModel.addElement(obj);
		treeModel.addElement(obj);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.ProzessorObserver#removeElement(java.lang.Object)
	 */
	public void removeElement(Object obj) {
		tableModel.removeElement(obj);
		treeModel.removeElement(obj);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.ProzessorObserver#setData(java.util.List)
	 */
	public void setData(List list) {
		tableModel.setData(list);
		treeModel.setData(list);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.ProzessorObserver#updateElement(java.lang.Object)
	 */
	public void updateElement(Object obj) {
		tableModel.updateElement(obj);
		treeModel.updateElement(obj);
	}

}
