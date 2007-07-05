/*
 * Created on 01.07.2006
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.komponenten;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.d3s.alricg.gui.komponenten.table.SortableTreeTableModel;
import org.d3s.alricg.gui.views.ComparatorCollection;
import org.d3s.alricg.gui.views.TypSchema;

/**
 * Diese Klasse kapselt den Node für einen Tree und managed alle Änderungen an dem Node. Einige Methoden müssen
 * speziel implementiert werden, je nachdem ob es sich um Links oder CharElemente handelt. 
 * @author Vincent
 */
public abstract class ExtendedRootNode<TYPE> {
	protected final DefaultMutableTreeNode root;
	protected Hashtable<Enum, DefaultMutableTreeNode> ordnerHash;
	protected TypSchema typSchema;
	protected Enum ordnungsEnum;
	protected ComparatorCollection.NodeComparator comparator;
	protected SortableTreeTableModel modelConnector;
	
	/**
	 * Konstruktor
	 * @param rootUserObject Der Name des Wurzelknotens. Typischer Weise so wie die Elemente ("Talente", "Zauber")
	 * @param zSchema Das TypSchema zu dem Elementen
	 */
	public ExtendedRootNode(String rootUserObject, TypSchema zSchema) {
		root = new DefaultMutableTreeNode(rootUserObject);
		typSchema = zSchema;
		ordnerHash =  new Hashtable<Enum, DefaultMutableTreeNode>();
		comparator = new ComparatorCollection.NodeComparator();
	}
	
	/**
	 * @return Liefert den Root-Node zurück, auf dem alle anderen Nodes basieren
	 */
	public DefaultMutableTreeNode getRootNode() {
		return root;
	}

	/**
	 * Setzt den Comparator zum Sortieren der Elemente.
	 * Sortiert werden NUR die Elemente unterhalbt der Ordner, die Ordner bleiben
	 * in der Reihenfolge wie festgelegt.
	 * @param comparator Der Comparator zum 
	 */
	public void setComparator(Comparator comparatorIn) {
		this.comparator.setRealComparator(comparatorIn);
		
		final Enumeration<DefaultMutableTreeNode> enume = root.children();
		
		// Alle Ordner durchgehen und dessen Inhalt sortieren
		while (enume.hasMoreElements()) {
			setComparatorHelp(enume.nextElement());
		}

	}
	/**
	 * Setzt das Model zu dem Root. Dies ist nötig, um Änderungen der Daten dem Model bekannt zu machen 
	 * @param sortableTableModel Das Model, welches zu diesem ExtendedNode gehört 
	 */
	public void setSortableTreeTableModel(SortableTreeTableModel sortableTableModel)  {
		this.modelConnector = sortableTableModel;
	}
	
	/**
	 * Setzt die Ordnung des Trees, also die "ordner", nach dem die Elemente
	 * im Tree eingeordnet werden.
	 * @param ordnungsEnumNew Das Enum, welches die neue Ordnung beschreibt
	 */
	public void setOrdnung(Enum ordnungsEnumNew) {
		if (ordnungsEnumNew == this.ordnungsEnum)  {
			// nix hat sich geändert
			return;
		} else if (ordnungsEnumNew == null) {
			// Keine TreeTable mehr, nix machen (?)
			return;
		}

		// Alte Elemente retten
		final Iterator<DefaultMutableTreeNode> ite = ordnerHash.values().iterator();
		final List<TYPE> listOldElements = new ArrayList<TYPE>();
		
		while (ite.hasNext()) {
			getAllUserObjects(ite.next(), listOldElements);
		}
		
		// Initialisiere die Ordner und das Hash
		this.ordnungsEnum = ordnungsEnumNew;
		final Enum[] ordnerArray = typSchema.getOrdnerForOrdnung(ordnungsEnum);
		ordnerHash.clear();
		
		for (int i = 0; i < ordnerArray.length; i++) {
			ordnerHash.put(
					ordnerArray[i], 
					new DefaultMutableTreeNode(ordnerArray[i])
				);
		}
		
		// Die alten Elemente wieder einfügen und dabei neu sortieren
		for (int i = 0; i < listOldElements.size(); i++) {
			Enum[] eArray = typSchema.getEnumsFromElement(listOldElements.get(i), ordnungsEnum);
			
			// Hinzufügen zu dem Ordner
			for (int ii = 0; ii < eArray.length; ii++) {
				addNodeHelp(listOldElements.get(i), ordnerHash.get(eArray[i]));
			}
		}
		modelConnector.fireTreeStructureChanged(this, root.getPath(), null, null);
	}

	
	/**
	 * Fügt das "userObject" als ChildNode zum allen "Ordnern" der aktuellen "ordnungsEnum" (siehe setOrdnung()) hinzu. 
	 * Falls das "userObject" einen Sammelbegriff besitzt, wird dieser auch als Node hinzugefügt und das "userObject" 
	 * zum Sammelbegriff-Node hinzugefügt.
	 * 
	 * @param userObject Das Objekt das neu hinzugefügt werden soll
	 */
	public abstract void addNode(TYPE userObject);
	

	
	/**
	 * Entfernd den zu dem Object "userObject" gehörenden Node von dem Tree. Wenn "userObject"
	 * das einzige Child von einem Sammelbegriff oder Ordner ist, so wird dieser Sammelbegriff/Ordner 
	 * entfernd bzw. nicht mehr angezeigt.
	 * 
	 * @param userObject Das userObject von dem Node, der entfernd werden soll
	 */
	public abstract void removeNode(TYPE userObject);

	
	/**
	 * Fügt das "userObject" als ChildNode zum "ordnerNode" hinzu. Falls das "userObject" einen Sammelbegriff
	 * besitzt, wird dieser auch als Node hinzugefügt und das "userObject" zum Sammelbegriff-Node hinzugefügt.
	 * 
	 * @param userObject Das Objekt, dass hinzugefügt werden werden soll 
	 * @param ordnerNode Der Node des Ordners, zu dem das userObjekt hinzugefügt werden soll
	 * @return true - Die Ordner müssen geupdatet werden, da mindestens ein Ordner/Sammelbegriff jetzt angezeigt 
	 * 		wird, der vorher nicht angezeigt wurde. 
	 */
	protected abstract void addNodeHelp(TYPE userObject, DefaultMutableTreeNode ordnerNode);
	
	/**
	 * Entfernd das "userObject" als ChildNode vom "ordnerNode" bzw. dem Sammelbegriff, falls "userObject" einen
	 * besitzt.
	 * 
	 * @param userObject Das Objekt, dass entfernd werden werden soll 
	 * @param ordnerNode Der Node des Ordners, von dem das userObjekt entfernd werden soll
	 * @return true - Die Ordner müssen geupdatet werden, da mindestens ein Ordner/Sammelbegriff jetzt mit mehr 
	 * 		angezeigt wird, der vorher angezeigt wurde. 
	 */
	protected abstract void removeNodeHelp(TYPE userObject, DefaultMutableTreeNode ordnerNode);
	
	/**
	 * Setz alle Objekte des Trees neu. Neuen Elemente werden entsprechend der Ordnung 
	 * in den Tree einsortiert. Alle alten Elemente werden entfernt.
	 * 
	 * @param objectList Liste mit Elemente, die in den Tree einsortiert werden sollen.
	 */
	public void resetObjects(List<TYPE> objectList) {
		final Iterator<DefaultMutableTreeNode> ite = ordnerHash.values().iterator();
		Enum[] eArray;
		
		// Alle alten Elemente entfernen
		while (ite.hasNext()) {
			ite.next().removeAllChildren();
		}
		
		// Die neuen Elemente hinzufügen
		for (int i = 0; i < objectList.size(); i++) {
			eArray = typSchema.getEnumsFromElement(objectList.get(i), ordnungsEnum);
			
			// Hinzufügen zu dem Ordner
			for (int ii = 0; ii < eArray.length; ii++) {
				DefaultMutableTreeNode ordnerNode = ordnerHash.get(eArray[ii]);
				addNodeHelp(objectList.get(i), ordnerNode);
			}
		}
		
		// Ordner aktualisieren
		arrangeRootChilds();
		modelConnector.fireTreeStructureChanged(this, root.getPath(), null, null);
	}

	/**
	 * Prüft ob das Objekt "userObject" als UserObject in einem ChildNode von "node" vorkommt.
	 * Ist dies der Fall, so wird der TreeNode in den "userObject" als UserObject vorkommt 
	 * zurückgeliefert.
	 * 
	 * @param userObject UserObject nach dem gesucht wird
	 * @param node Der Node in dessen "children" gesucht wird
	 * @return Der TreeNode der "userObject" als UserObject besitzt und Child von "node" ist
	 */
	protected DefaultMutableTreeNode findNode(Object userObject, DefaultMutableTreeNode node) {
		final int childCount = node.getChildCount();
		DefaultMutableTreeNode tmpNode;
		
		for (int i = 0; i < childCount; i++) {
			if ( ((DefaultMutableTreeNode) node.getChildAt(i)).getUserObject() == userObject ) {
				return ((DefaultMutableTreeNode) node.getChildAt(i));
			} else if (((DefaultMutableTreeNode) node.getChildAt(i)).getChildCount() > 0) {
				tmpNode = findNode(userObject, (DefaultMutableTreeNode) node.getChildAt(i));
				if (tmpNode != null) return tmpNode;
			}
		}
		
		return null;
	}
	
	/**
	 * Findet alle Nodes aus dem Tree, welche "userObject" als UserObject besitzen
	 * @param userObject Das gesuchte UserObject
	 * @return Eine Liste mit allen Nodes, welche "userObject" als UserObject besitzen
	 */
	public List<DefaultMutableTreeNode> findeNodes(Object userObject) {
		final List<DefaultMutableTreeNode> list = new ArrayList<DefaultMutableTreeNode>();
		final Iterator<DefaultMutableTreeNode> ite = ordnerHash.values().iterator();
		DefaultMutableTreeNode tmpNode;

		while (ite.hasNext()) {
			tmpNode = findNode(userObject, ite.next());
			if (tmpNode != null) {
				list.add(tmpNode);
			}
		}
		
		return list;
	}
	
	/**
	 * Sucht mit Hilfe des Comperators die richtige Position zum Einfügen eines UserObjects.
	 * 
	 * @param userObject Das Element das eingefügt werden soll
	 * @param node Zu diesem Node soll das Element hinzugefügt werden
	 * 
	 * @return Index der Stelle, wo das userObject laut aktueller Sortierung (durch comparator) 
	 * 		eingefügt werden soll. 
	 */
	protected int getIndexToAdd(DefaultMutableTreeNode nodeToAdd, DefaultMutableTreeNode nodeAddTo) {
		final int childCount = nodeAddTo.getChildCount();
		final List<DefaultMutableTreeNode> list = new ArrayList<DefaultMutableTreeNode>(childCount);
		int index = 0;
		
		for (int i = 0; i < childCount; i++) {
			list.add( (DefaultMutableTreeNode) nodeAddTo.getChildAt(i) );
		}
		
		index = Collections.binarySearch(list, nodeToAdd, comparator);

		if (index < 0) {
			index = Math.abs(index + 1);
		}
		
		return index;
	}
	
	/**
	 * Liefert zu einem Node alle userObjecte der Kinder dieses Nodes zurück,
	 * rekursive Methode. Es werden keine "Sammelbegriffe" und keine Varianten berücksichtigt.
	 * Diese werden nicht in "list" mit aufgenommen.
	 * 
	 * @param node Alle Kinder von diesem Node durchlaufen
	 * @param list Return parameter. Alle UserObjecte der Kinder und KindesKinder von
	 * 		"node"
	 */
	private void getAllUserObjects(DefaultMutableTreeNode node, List list) {
		DefaultMutableTreeNode tmpNode;
		final Enumeration<DefaultMutableTreeNode> enume = node.children();
		
		while (enume.hasMoreElements()) {
			tmpNode = enume.nextElement();
			
			if ( tmpNode.getUserObject() instanceof String ) {
				// Wenn Instance von String, so ist es kein Sammelbegriff --> Rekursion
				getAllUserObjects(tmpNode, list);
			}
			
			list.add(tmpNode.getUserObject());
		}
	}
	
	/**
	 * Alle Ordner mit mindestens einem "Kind" werden neu zum Root hinzugefügt. 
	 * Alle Ordner die kein "Kind" haben, werden NICHT zum Root hinzugefügt.
	 */
	private void arrangeRootChilds() {
		final Enum[] ordnerArray = typSchema.getOrdnerForOrdnung(ordnungsEnum);
		DefaultMutableTreeNode node;
		
		root.removeAllChildren();
		for (int i = 0; i < ordnerArray.length; i++) {
			node = ordnerHash.get(ordnerArray[i]);
			
			if (node.getChildCount() > 0) {
				root.add(node);
			}
		}
	}
	
	/**
	 * Hilfmethode für das Sortieren von Elementen
	 * @param node Der Node, dessen Kinder sortiert werden sollen
	 */
	private void setComparatorHelp(DefaultMutableTreeNode node) {
		
		// Guard
		if (node.getChildCount() == 0) return;
		
		final Enumeration<DefaultMutableTreeNode> enume = node.children();
		final int childCount = node.getChildCount();
		final List<DefaultMutableTreeNode> list = new ArrayList<DefaultMutableTreeNode>(childCount);
		
		// Rekursiver Aufruf
		while (enume.hasMoreElements()) {
			setComparatorHelp(enume.nextElement());
		}

		// Alte Elemente auslesen und sortieren lassen
		for (int i = 0; i < childCount; i++) {
			list.add((DefaultMutableTreeNode) node.getChildAt(i));
		}
		Collections.sort(list, comparator);
		
		// Alte Reihenfolge löschen
		node.removeAllChildren();
		
		// Neue Reihenfolge erzeugen
		for (int i = 0; i < childCount; i++) {
			node.add(list.get(i));
		}
	}
}
