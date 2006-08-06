/*
 * Created on 04.04.2005 / 17:35:01
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.komponenten.table;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.d3s.alricg.gui.ProzessorObserver;
import org.d3s.alricg.gui.komponenten.ExtendedRootNode;
import org.d3s.alricg.gui.komponenten.ExtendedRootNodeCharElement;
import org.d3s.alricg.gui.komponenten.ExtendedRootNodeLink;
import org.d3s.alricg.gui.views.SpaltenSchema;
import org.d3s.alricg.gui.views.TypSchema;
import org.d3s.alricg.gui.views.SpaltenSchema.SpaltenArt;

/**
 * <u>Beschreibung:</u><br>
 * Dient als Model zur Darstellung von Daten in einer TreeTable (SortableTreeTable). 
 * In dieser Klasse werden alle Grundfunktionen dafür gehalten, um auf die Daten zu 
 * zugreifen und diese zu verwalten.
 * Die spezifischen Funktionen, die von der Art der Daten (Talent, Zauber, Rasse, usw.)
 * abhängen, werden in einem Schema gehalten. Während dieses Modell speziell für die
 * Darstellung in einer TreeTable gemacht ist, ist das Schema unabhängig von der Art der
 * Darstellung und kann somit auch für die JTable verwendet werden.
 *
 * @author V. Strelow
 */
public class SortableTreeTableModel<E> extends AbstractTreeTableModel implements SortableTreeOrTableInterface, ProzessorObserver {
	private final Object[] columns; // Die Spalten-Titel
	private final SpaltenSchema spaltenSchema; // Spezifische Methoden für die Spalten
	private final TypSchema typSchema; // Spezifische Methoden für Typ <E>
	private final ExtendedRootNode extRootNode;
	
	private boolean[] lastAscSorted; // Sortierrichtung

	/**
	 * Konstruktor.
	 * 
	 * @param sSchema Das SpaltenSchema, mit dem die Spalten und die Struktur der TreeTable aufgebaut wird
	 * @param tSchema Das TypSchema, mit dem die Logik zur Verarbeitung der Elemente festgelegt wird
	 * @param art Die Art der Spalten, welche die gewünschte Nutzung bestimmt
	 */
	public SortableTreeTableModel(SpaltenSchema sSchema, TypSchema tSchema, SpaltenArt art) {
    	
		// Initialisieren der Grundwerte
		this.columns = sSchema.getSpalten(art);
		this.spaltenSchema = sSchema;
		this.typSchema = tSchema;
		this.lastAscSorted = new boolean[columns.length];
		Arrays.fill(lastAscSorted, false); // Damit überall ein Wert steht
		
		// ExtendedNode für TreeTable erzeugen
		if ( typSchema.hasLinksAsElements() ) {
			extRootNode = new ExtendedRootNodeLink(spaltenSchema.getRootNodeName(), typSchema);
		} else {
			extRootNode = new ExtendedRootNodeCharElement(spaltenSchema.getRootNodeName(), typSchema);
		}
		
		// ExtendenRootNode initialisieren
		extRootNode.setSortableTreeTableModel(this);
		extRootNode.setOrdnung(spaltenSchema.getOrdnungElem(art)[1]);
		extRootNode.setComparator(typSchema.getComparator(columns[0]));
		
		this.setRoot(extRootNode.getRootNode());
	}
	
	/**
	 * Wird genutzt von dem TreeTableModelAdapter
	 * @return Das benutze Schema dieses Modells
	 */
	public SpaltenSchema getSpaltenSchema() {
		return spaltenSchema;
	}
	
	/**
	 * Wird genutzt von dem TreeTableModelAdapter
	 * @return Das benutze Schema dieses Modells
	 */
	public TypSchema getTypSchema() {
		return typSchema;
	}
	
	/**
	 * Setzt die Elemente des DatenModells, alle alten Daten werden dabei überschrieben!
	 * @param elemListe Liste von allem Elementen die die Tabelle anzeigen soll
	 */
	public void setData(List elemListe) { 
		extRootNode.resetObjects(elemListe);
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int col) {
		return columns[col].toString();
	}
	
	/**
	 * Jede Spalte ist in einem SortableTreeTableModel ist mit einer Enum 
	 * gegenzeichnet 
	 * @param col Die Spalte zu der die Enum geliefert wird
	 * @return Liefert die Enum zu einer Spalte
	 */
	public Object getColumnObject(int col) {
		return columns[col];
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.Test.treeTable.TreeTableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return columns.length;
	}

	/**
	 * Prüft ob eine Spalte sortierbar ist.
	 * Wird benutzt um Listener anzumelden und Pfeile einzublenden.
	 * @param column Die Spalte die überprüft werden soll
	 * @return true: Die Spalte mit der Nummer "column" ist sortierbar, sonst false
	 */
	public boolean isSortable(int column) {
		return spaltenSchema.isSortable(columns[column]);
	}

	/**
	 * Sortiert den gesamten TreeTable nach der übergebenen Spalte
	 * @param column Die Spalte nach der Sortiert werden soll
	 */
	public void sortTableByColumn(int column) {
		
		// Comparator setzen
		if ( lastAscSorted[column] ) {
			extRootNode.setComparator(typSchema.getComparator(columns[column]));
		} else {
			// Umgedrehte reihenfolge
			extRootNode.setComparator(
					Collections.reverseOrder(typSchema.getComparator(columns[column]))
				);
		}
		
//		 Sortierung "umdrehen" und beim Comparator setzen
		lastAscSorted[column] = !lastAscSorted[column];
	}

	/**
	 * Setzt die Ordnung des Trees, also die "ordner", nach dem die Elemente
	 * im Tree eingeordnet werden.
	 * @param ordnungsEnumNew Das Enum, welches die neue Ordnung beschreibt
	 */
	public void setOrdnung(Enum ordnungsEnumNew) {
		extRootNode.setOrdnung(ordnungsEnumNew);
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.komponenten.table.SortableTreeOrTableInterface#setFilter(java.lang.Enum)
	 */
	public void setFilter(Enum filter) {
		setData(
				typSchema.doFilterElements(
						filter, 
						typSchema.getElementBox().getUnmodifiableList())
			);
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.komponenten.table.SortableTableModelInterface#isSortColumnDesc(int)
	 */
	public boolean isSortColumnDesc(int column) {
		return lastAscSorted[column];
	}

    /* (non-Javadoc) Methode überschrieben
     * @see org.d3s.alricg.Test.treeTable.TreeTableModel#isCellEditable(java.lang.Object, int)
     */
    public boolean isCellEditable(Object node, int column) {
    	Object obj = ((DefaultMutableTreeNode) node).getUserObject();
    	
		if (column != 0 && (obj instanceof Enum || obj instanceof String)) {
			return false;
		}
    	
    	return typSchema.isCellEditable(obj, columns[column]);
   }
    
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.komponenten.table.AbstractTreeTableModel#setValueAt(java.lang.Object, java.lang.Object, int)
	 */
	@Override
	public void setValueAt(Object aValue, Object node, int column) {

		typSchema.setCellValue(aValue, 
				((DefaultMutableTreeNode) node).getUserObject(), 
				columns[column]);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.Test.treeTable.TreeTableModel#getValueAt(java.lang.Object, int)
	 */
	public Object getValueAt(Object node, int column) {
		DefaultMutableTreeNode mutNode;
		
		mutNode = ((DefaultMutableTreeNode) node);
				
		if (mutNode.getUserObject() instanceof String) {
			// Es ist nur ein Sammelbegriff-Ordner, der zur Sortierung dient
			
			if (column == 0) {
				return node; // Rückgabe als De.MutableNode für den Tree
			} else {
				// TODO andere Lösung für Komplexe Problme nötig
				return "-";
			}
		} else if (mutNode.getUserObject() instanceof Enum) {
			// Es ist nur ein Enum-Ordner, der zur Sortierung dient
			
			if (column == 0) {
				return node; // Rückgabe als De.MutableNode für den Tree
			} else {
				// TODO andere Lösung für Komplexe Problme nötig
				return "-";
			}
		}
		
		return typSchema.getCellValue(mutNode.getUserObject(), columns[column]);
	}
	
    /* (non-Javadoc) Methode überschrieben
     * @see org.d3s.alricg.Test.treeTable.TreeTableModel#getColumnClass(int)
     */
    public Class getColumnClass(int column) {
    	if (column == 0) {
    		return TreeTableModel.class;
    	}
    	
    	return super.getColumnClass(column);
    }
	
	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(Object parent, int idx) {
		return ((DefaultMutableTreeNode) parent).getChildAt(idx);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object node) {
		return ((DefaultMutableTreeNode) node).getChildCount();
	}

	// ---------------------------------------------------------------------------
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.ProzessorObserver#addElement(java.lang.Object)
	 */
	public void addElement(Object obj) {
		extRootNode.addNode(obj);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.ProzessorObserver#removeElement(java.lang.Object)
	 */
	public void removeElement(Object obj) {
		extRootNode.removeNode(obj);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.ProzessorObserver#updateElement(java.lang.Object)
	 */
	public void updateElement(Object obj) {
		// Alle Nodes mit dem UserObjct holen
		List<DefaultMutableTreeNode> list = extRootNode.findeNodes(obj);
		DefaultMutableTreeNode tmpNode;
		
		// Nodes Akualisieren
		for (int i = 0; i < list.size(); i++) {
			tmpNode = list.get(i);
			
			this.fireTreeNodesChanged(
					this, 
					tmpNode.getPath(), 
					new int[] {tmpNode.getParent().getIndex(tmpNode)},
					new Object[] {tmpNode}
			);
		}
	}

	

	
}



