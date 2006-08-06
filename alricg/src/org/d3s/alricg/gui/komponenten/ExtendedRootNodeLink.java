/*
 * Created on 01.07.2006
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.komponenten;

import javax.swing.tree.DefaultMutableTreeNode;

import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.gui.views.TypSchema;

/**
 * Eine spezielle Implementierung der Klasse ExtendedRootNode für die Verwaltung von Nodes, bei denen 
 * die UserObjects Links sind.
 * @author Vincent
 */
public class ExtendedRootNodeLink extends ExtendedRootNode<Link> {

	public ExtendedRootNodeLink(String userObject, TypSchema zSchema) {
		super(userObject, zSchema);
	}
	
	/**
	 * Fügt das "userObject" als ChildNode zum "ordnerNode" hinzu.
	 * Bei Links werden Sammelbegriff und Varianten nicht berücksichtigt!
	 * 
	 * @param userObject Das Objekt, dass hinzugefügt werden werden soll 
	 * @param ordnerNode Der Node des Ordners, zu dem das userObjekt hinzugefügt werden soll
	 * @return true - Die Ordner müssen geupdatet werden, da mindestens ein Ordner jetzt angezeigt 
	 * 		wird, der vorher nicht angezeigt wurde. 
	 * @return
	 */
	protected void addNodeHelp(Link userObject, DefaultMutableTreeNode ordnerNode) {
		final DefaultMutableTreeNode node = new DefaultMutableTreeNode(userObject);
		final int index;
		
		//  Node hinzufügen
		index = getIndexToAdd(node, ordnerNode);
		ordnerNode.insert( node, index );
		
		// Tree aktualisieren
		modelConnector.fireTreeNodesInserted( this, ordnerNode.getPath(), new int[] {index}, new Object[] {node} );

		// Der Ordner wurde zuvor nicht angezeigt, soll jetzt angezeigt werden
		if ( !root.isNodeChild(ordnerNode) ) {
			
			// Wenn nicht, "OrdnerNode" einfügen und die position suchen
			int ordinal = ((Enum) ordnerNode.getUserObject()).ordinal();
			for (int i = 0; i < root.getChildCount(); i++) {
				if (ordinal <= 
				((Enum) ((DefaultMutableTreeNode) root.getChildAt(i)).getUserObject()).ordinal() ) {
					root.insert(ordnerNode, i);
					modelConnector.fireTreeNodesInserted(this, root.getPath(), new int[] {i}, new Object[] {ordnerNode});
					break; // Schleife abbrechen
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
	protected void removeNodeHelp(Link userObject, DefaultMutableTreeNode ordnerNode) {
		final DefaultMutableTreeNode node = findNode(userObject, ordnerNode);
		int index = ordnerNode.getIndex(node);
		
		ordnerNode.remove( node );
		
		// Tree aktualisieren
		modelConnector.fireTreeNodesRemoved(this, ordnerNode.getPath(), new int[] {index}, new Object[] {node});			

		// Wenn der Ordner keine "Childs" mehr besitzt, dann muss der Ordner geupdatet werden
		if ( ordnerNode.getChildCount() == 0 ) {
			index = root.getIndex(ordnerNode);
			root.remove(ordnerNode);
			modelConnector.fireTreeNodesRemoved(this, root.getPath(), new int[] {index}, new Object[] {ordnerNode});
		}
	}
	
	/**
	 * Fügt das "userObject" als ChildNode zum allen "Ordnern" der aktuellen "ordnungsEnum" (siehe setOrdnung()) hinzu. 
	 * Bei Links werden Sammelbegriff und Varianten nicht berücksichtigt!
	 * 
	 * @param userObject Das Objekt das neu hinzugefügt werden soll
	 */
	public void addNode(Link userObject) {
		Enum[] eArray = typSchema.getEnumsFromElement(userObject, ordnungsEnum);
		
		// Hinzufügen zu dem Ordner
		for (int i = 0; i < eArray.length; i++) {
			DefaultMutableTreeNode ordnerNode = ordnerHash.get(eArray[i]);
			addNodeHelp(userObject, ordnerNode);
		}

	}
	
	/**
	 * Entfernd den zu dem Object "userObject" gehörenden Node von dem Tree. 
	 * Bei Links werden Sammelbegriff und Varianten nicht berücksichtigt!
	 * 
	 * @param userObject Das userObject von dem Node, der entfernd werden soll
	 */
	public void removeNode(Link userObject) {
		Enum[] eArray = typSchema.getEnumsFromElement(userObject, ordnungsEnum);
		
		// Entfernen vom Ordner
		for (int i = 0; i < eArray.length; i++) {
			DefaultMutableTreeNode ordnerNode = ordnerHash.get(eArray[i]);
			removeNodeHelp(userObject, ordnerNode);
		}

	}
}
