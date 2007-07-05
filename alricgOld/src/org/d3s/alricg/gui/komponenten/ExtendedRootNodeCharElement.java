/*
 * Created on 01.07.2006 / 11:39:16
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.komponenten;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Herkunft;
import org.d3s.alricg.gui.views.TypSchema;

/**
 * Eine spezielle Implementierung der Klasse ExtendedRootNode für die Verwaltung von Nodes, bei denen 
 * die UserObjects CharElemente sind.
 * @author V. Strelow
 */
public class ExtendedRootNodeCharElement extends ExtendedRootNode<CharElement> {
	
	public ExtendedRootNodeCharElement(String userObject, TypSchema zSchema) {
		super(userObject, zSchema);
	}
	
	/**
	 * Fügt das "userObject" als ChildNode zum allen "Ordnern" der aktuellen "ordnungsEnum" (siehe setOrdnung()) hinzu. 
	 * Falls das "userObject" einen Sammelbegriff besitzt, wird dieser auch als Node hinzugefügt und das "userObject" 
	 * zum Sammelbegriff-Node hinzugefügt.
	 * 
	 * @param userObject Das Objekt das neu hinzugefügt werden soll
	 */
	public void addNode(CharElement userObject) {
		Enum[] eArray = typSchema.getEnumsFromElement(userObject, ordnungsEnum);
		
		// Hinzufügen zu dem Ordner
		for (int i = 0; i < eArray.length; i++) {
			DefaultMutableTreeNode ordnerNode = ordnerHash.get(eArray[i]);
			addNodeHelp(userObject, ordnerNode);
		}
	}
	
	/**
	 * Entfernd den zu dem Object "userObject" gehörenden Node von dem Tree. Wenn "userObject"
	 * das einzige Child von einem Sammelbegriff oder Ordner ist, so wird dieser Sammelbegriff/Ordner 
	 * entfernd bzw. nicht mehr angezeigt.
	 * 
	 * @param userObject Das userObject von dem Node, der entfernd werden soll
	 */
	public void removeNode(CharElement userObject) {
		Enum[] eArray = typSchema.getEnumsFromElement(userObject, ordnungsEnum);
		
		// Entfernen vom Ordner
		for (int i = 0; i < eArray.length; i++) {
			DefaultMutableTreeNode ordnerNode = ordnerHash.get(eArray[i]);
			removeNodeHelp(userObject, ordnerNode);
		}
		
	}

	
	/**
	 * Fügt das "userObject" als ChildNode zum "ordnerNode" hinzu. Falls das "userObject" einen Sammelbegriff
	 * besitzt, wird dieser auch als Node hinzugefügt und das "userObject" zum Sammelbegriff-Node hinzugefügt.
	 * 
	 * @param userObject Das Objekt, dass hinzugefügt werden werden soll 
	 * @param ordnerNode Der Node des Ordners, zu dem das userObjekt hinzugefügt werden soll
	 * @return true - Die Ordner müssen geupdatet werden, da mindestens ein Ordner/Sammelbegriff jetzt angezeigt 
	 * 		wird, der vorher nicht angezeigt wurde. 
	 */
	protected void addNodeHelp(CharElement userObject, DefaultMutableTreeNode ordnerNode) {
		DefaultMutableTreeNode node, sammelNode;
		int index;
		
		// Prüfen auf Sammelbegriff
		node = new DefaultMutableTreeNode(userObject);
		if ( !userObject.hasSammelBegriff() ) {
			// Ohne Sammelbegriff, der Node wird direkt hinzugefügt
			index = getIndexToAdd(node, ordnerNode);
			ordnerNode.insert( node, index );
			
			// Tree aktualisieren
			modelConnector.fireTreeNodesInserted( this, ordnerNode.getPath(), new int[] {index}, new Object[] {node} );
			
		} else {
			// Hat einen Sammelbegriff...
			sammelNode =  findNode(userObject.getSammelBegriff(), ordnerNode);
			if ( sammelNode == null) {
				// ... dieser ist neu und wird erzeugt
				sammelNode = new DefaultMutableTreeNode(userObject.getSammelBegriff());
				index = getIndexToAdd( sammelNode, ordnerNode );
				ordnerNode.insert( sammelNode, index );
				modelConnector.fireTreeNodesInserted(this, ordnerNode.getPath(), new int[] {index}, new Object[] {sammelNode});
			}
			// Node zum Sammelbegriff hinzufügen
			index = getIndexToAdd( node, sammelNode );
			sammelNode.insert( node, index );
			modelConnector.fireTreeNodesInserted(this, sammelNode.getPath(), new int[] {index}, new Object[] {node});
		}
		
		// Prüfen auf Varianten, wenn Varianten vorhandem diese hinzufügen
		if (userObject instanceof Herkunft) {
			int[] indexArray;
			Object[] objectArray;
			List list = Arrays.asList( ((Herkunft) userObject).getVarianten() );
			Collections.sort(list, comparator);
			
			indexArray = new int[list.size()];
			objectArray = new Object[list.size()];
			
			for (int i = 0; i < list.size(); i++) {
				indexArray[i] = i;
				objectArray[i] = new DefaultMutableTreeNode(list.get(i));
				node.add( (DefaultMutableTreeNode) objectArray[i] );
				modelConnector.fireTreeNodesInserted(this, node.getPath(), indexArray, objectArray);
			}
		}
		

		// Prüfen ob "OrdnerNode" bereits als Kind von root angezeigt wird
		if ( !root.isNodeChild(ordnerNode) ) {
			
			// Wenn nicht, "OrdnerNode" einfügen und die position suchen
			int ordinal = ((Enum) ordnerNode.getUserObject()).ordinal();
			for (int i = 0; i < root.getChildCount(); i++) {
				if (ordinal <= 
				((Enum) ((DefaultMutableTreeNode) root.getChildAt(i)).getUserObject()).ordinal() ) {
					root.insert(ordnerNode, i);
					modelConnector.fireTreeNodesInserted(this, root.getPath(), new int[] {i}, new Object[] {ordnerNode});
				}
			}
			
			// Wurde die Position nicht gefunden (da ordinal grösser), so den Node am Ende einfügen 
			if (!root.isNodeChild(ordnerNode)) {
				root.insert(ordnerNode, root.getChildCount());
				modelConnector.fireTreeNodesInserted(this, root.getPath(), new int[] {root.getChildCount()-1}, new Object[] {ordnerNode});

			}
		}
		
	}
	
	/**
	 * Entfernd das "userObject" als ChildNode vom "ordnerNode" bzw. dem Sammelbegriff, falls "userObject" einen
	 * besitzt.
	 * 
	 * @param userObject Das Objekt, dass entfernd werden werden soll 
	 * @param ordnerNode Der Node des Ordners, von dem das userObjekt entfernd werden soll
	 * @return true - Die Ordner müssen geupdatet werden, da mindestens ein Ordner/Sammelbegriff jetzt mit mehr 
	 * 		angezeigt wird, der vorher angezeigt wurde. 
	 */
	protected void removeNodeHelp(CharElement userObject, DefaultMutableTreeNode ordnerNode) {
		DefaultMutableTreeNode node, sammelNode;
		int index;
		
		node = findNode(userObject, ordnerNode);
		// Prüfen auf Sammelbegriff
		if ( !userObject.hasSammelBegriff() ) {
			// Ohne Sammelbegriff, der Node wird direkt entfernt
			index =  ordnerNode.getIndex(node);
			ordnerNode.remove( node );
			modelConnector.fireTreeNodesRemoved(this, ordnerNode.getPath(), new int[] {index}, new Object[] {node});			
		
		} else {
			// Hat einen Sammelbegriff
			// UserObject-Node von SammelNode entfernen
			sammelNode = findNode(userObject.getSammelBegriff(), ordnerNode);
			index = sammelNode.getIndex(node);
			sammelNode.remove( findNode(userObject, node) );
			modelConnector.fireTreeNodesRemoved(this, sammelNode.getPath(), new int[] {index}, new Object[] {node});			

			// Wenn der SammelNode keine Childs mehr hat, diesen entfernen
			if ( sammelNode.getChildCount() == 0) {
				index = ordnerNode.getIndex(sammelNode);
				ordnerNode.remove(sammelNode);
				modelConnector.fireTreeNodesRemoved(this, ordnerNode.getPath(), new int[] {index}, new Object[] {sammelNode});			
			}
		}
		
		// Wenn der Ordner keine "Childs" mehr besitzt, dann muss der Ordner geupdatet werden
		if ( ordnerNode.getChildCount() == 0 ) {
			index = root.getIndex(ordnerNode);
			root.remove(ordnerNode);
			modelConnector.fireTreeNodesRemoved(this, root.getPath(), new int[] {index}, new Object[] {ordnerNode});
		}
	}
	
}
