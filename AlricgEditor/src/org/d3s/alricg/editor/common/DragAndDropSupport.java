/*
 * Created 09.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common;

import java.util.HashMap;
import java.util.Map;

import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.editors.composits.VoraussetzungPart;
import org.d3s.alricg.editor.utils.EditorViewUtils.AuswahlTreeObject;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author Vincent
 *
 */
public class DragAndDropSupport {

	public static class AuswahlDrag implements DragSourceListener {
		private final TreeViewer treeViewer;
		
		public AuswahlDrag(TreeViewer viewer) {
			treeViewer = viewer;
		}
		
		@Override
		public void dragStart(DragSourceEvent event) {
			if (treeViewer.getSelection().isEmpty()) {
				event.doit = false;
			}
			final TreeOrTableObject treeTableObj = (TreeOrTableObject) ((StructuredSelection) treeViewer
					.getSelection()).getFirstElement();

			if (!(treeTableObj.getValue() instanceof Link)) {
				event.doit = false;
			}
		}

		@Override
		public void dragSetData(DragSourceEvent event) {
			IStructuredSelection selection = (IStructuredSelection) treeViewer
					.getSelection();
			LocalSelectionTransfer.getTransfer().setSelection(
					selection);

		}

		@Override
		public void dragFinished(DragSourceEvent event) {
			final TreeObject treeObj = (TreeObject) ((StructuredSelection) treeViewer
					.getSelection()).getFirstElement();
			
			treeObj.getParent().removeChildren(treeObj);
			treeViewer.refresh();
		}
	}
	
	/**
	 * Basisklasse für Drop in Tabellen/Baume wo in bestimmte Spalten ein Drop ausgeführt 
	 * werden kann.
	 * @author Vincent
	 */
	public abstract static class SelectDrop extends ViewerDropAdapter implements MouseMoveListener {
		private TreeOrTableObject sourceObj;
		private TreeOrTableObject targetObj;
		private final Item[] columnToListen; // "Dropable" Columns
		private Class[] acceptDropClasses; // CharElemente diesen Typs werden Akzeptiert
		private Map<Item, Class>  acceptColumnClassMap = new HashMap<Item, Class>(); // CharElemente diesen Typs werden Akzeptiert
		
		/**
		 * @param viewer Der Viewer zu dem dieses SelectDrop gehört
		 * @param columnToListen Die Columns, in welche Gedropt werden kann
		 */
		public SelectDrop(ColumnViewer viewer, Item[] columnToListen) {
			super(viewer);
			this.columnToListen = columnToListen;
		}
		
		/**
		 * Setzt den Typ von Objekten, welche als neue Elemente zur Tabelle hinzugefügt werden 
		 * können. Objekte, deren Typ NICHT mit dem gesetzten übereinstimmt, können nicht hinzu-
		 * gefügt werden (allerdings können sie evtl. als Objecte für TreeColumns gesetzt werden)
		 * Ist dieser Typ nicht gesetzt, werden alle Objecte akzeptiert
		 * @param acceptCharElementClazz Klasse Objekten die als Drop für neue Elemente
		 * 		erlaubt sind
		 */
		public void setAcceptGlobalDropClazz(Class[] acceptCharElementClazz) {
			this.acceptDropClasses = acceptCharElementClazz;
		}
		
		/**
		 * Setzt den Typ von Objekten, welche in einer bestimmten TreeColumn als Drop
		 * Objekt zulässig ist. Objekte von einem anderen Typ können in dieser TreeColumn
		 * nicht gedropt werden.
		 * Ist dieser Typ nicht gesetzt, werden alle Objecte akzeptiert
		 * @param acceptCharElementClazz Klasse von Objekten die als Drop für die 
		 * 		TreeColumn "column" zugelassen ist
		 * @param column TreeColumn für welche der Typ gesetzt wird
		 */
		public void addAcceptColumnClazz(Class acceptCharElementClazz, Item column) {
			this.acceptColumnClassMap.put(column, acceptCharElementClazz);
		}
		
		/**
		 * @return Die columns, welche im Konstruktor als 
		 */
		public Item[] getColumnsToListen() {
			return columnToListen;
		}
		
		@Override
		public void drop(DropTargetEvent event) {
			// Setzt sourceObj/ targetObj. Das eigentliche Drop wird dann
			// von dem der "mouseMove" Methode ausgeführt. Diese kann
			// festellen auf welcher Spalte das Drop ausgeführt wurde:
			// Wichtig für Zweitziel!
			sourceObj = (TreeOrTableObject) ((StructuredSelection) event.data).getFirstElement();
			targetObj = (TreeOrTableObject) getCurrentTarget();
		}
		
		@Override
		public boolean performDrop(Object data) {
			// wird durch das selbst implementierte "drop" nicht mehr aufgerufen, muss aber 
			// implementiert werden
			return true;
		}

		@Override
		public boolean validateDrop(Object target, int operation, TransferData transferType) {
			return true;
		}
		
		/**
		 * Von der jeweiligen Klasse zu überschreiben. 
		 * Handelt das Dropen in eine Column der Table/Tree.
		 * 
		 * @param colIndex Index der Column auf welche gedropt wurde
		 * @param sourceObj Das Objekt, welches "gedropt" wird. Die Quelle.
		 * @param targetObj Das Objekt, auf welches das zu "Dropende" gezogen wurde. Das Ziel. 
		 */
		protected abstract void dropToColumn(int colIndex, TreeOrTableObject sourceObj, TreeOrTableObject targetObj);
		
		/**
		 * Von der jeweiligen Klasse zu überschreiben. 
		 * Handelt das Dropen in die Table/Tree selbst  als neues Element
		 * 
		 * @param sourceObj Das Objekt, welches "gedropt" wird. Die Quelle.
		 * @param targetObj Das Objekt, auf welches das zu "Dropende" gezogen wurde. Das Ziel. 
		 */
		protected abstract void dropToTreeTable(TreeOrTableObject sourceObj, TreeOrTableObject targetObj);
		
		private boolean validateToTreeDrop(TreeOrTableObject data) {
			if (acceptDropClasses == null) return true;
			for (int i = 0; i < acceptDropClasses.length; i++){
				if ( acceptDropClasses[i].isAssignableFrom(ViewUtils.getCharElement(data).getClass()) ) {
					return true;
				}
			}
			return false;
		}
		
		private boolean validateDropToColumn(TreeOrTableObject data, Item column) {
			if (!acceptColumnClassMap.containsKey(column)) return true;
			if ( acceptColumnClassMap.get(column).isAssignableFrom(ViewUtils.getCharElement(data).getClass())) {
				return true;
			}
			return false;
		}
		
		/* 
		 * In dieser Methode wird das eigentliche "Drop" ausgeführt... (siehe Methode "drop").
		 * In dieser Methode kann festgestellt werden welche Spalte "gedropt" wurde und so
		 * das ZweitZiel gesetzt werden
		 * (non-Javadoc)
		 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
		 */
		@Override
		public void mouseMove(MouseEvent e) {
			if (sourceObj == null) return; // nothing to Drop
			
			// Ermitteln der Drop-Column
			Item currentColumn = null;
			if (this.getViewer() instanceof TreeViewer) {
				final TreeItem item = ((TreeViewer) this.getViewer()).getTree().getItem(0);
				for (int i = 0; i < ((TreeViewer) this.getViewer()).getTree().getColumnCount(); i++) {
			        Rectangle rect = item.getBounds(i);
			        if (rect.x < e.x && rect.x + rect.width > e.x) {
			        	currentColumn = ((TreeViewer) this.getViewer()).getTree().getColumn(i);
			        }
				}
			} else {
				if (((TableViewer) this.getViewer()).getTable().getItemCount() == 0) {
					currentColumn = null;
				} else {
					final TableItem item = ((TableViewer) this.getViewer()).getTable().getItem(0);
					for (int i = 0; i < ((TableViewer) this.getViewer()).getTable().getColumnCount(); i++) {
				        Rectangle rect = item.getBounds(i);
				        if (rect.x < e.x && rect.x + rect.width > e.x) {
				        	currentColumn = ((TableViewer) this.getViewer()).getTable().getColumn(i);
				        	break;
				        }
					}
				}
			}

			boolean foundFlag = false;
			for (int i = 0; i < columnToListen.length; i++) {
				if ( columnToListen[i].equals(currentColumn) ) {
					foundFlag = true;
					if (validateDropToColumn(sourceObj, columnToListen[i])) {
						dropToColumn(i, sourceObj, targetObj);
					}
				}
			}
			if (!foundFlag) {
				if (validateToTreeDrop(sourceObj)) {
					dropToTreeTable(sourceObj, targetObj);
				}
			}
			
			if (foundFlag && this.getViewer() instanceof TreeViewer) {
				((TreeViewer) this.getViewer()).update(targetObj, null);
			} else if (foundFlag && this.getViewer() instanceof TableViewer) {
				((TableViewer) this.getViewer()).update(targetObj, null);
			} else if (!foundFlag && this.getViewer() instanceof TreeViewer){
				// Ist bei einem Tree nötig, bei einer Table führt es
				// dagegen zu einer leeren Table
				// Warum? Keine Ahnung
				if ( this.getViewer() instanceof TreeViewer) {
					this.getViewer().refresh();
				}
			}
			
			sourceObj = null;
			targetObj = null;
		}
	}
	
	/**
	 * Diese Klasse kommt für den Drop bei Voraussetzungen und Auswahlen zum Einsatz
	 * @author Vincent
	 */
	public static class AuswahlDrop extends SelectDrop {
		private final Class defaultOptionclazz; // "Default-Option" falls keine user-angabe
		private CharElement quelle;
		
		/**
		 * Konstruiert einen neuen AuswahlDrop
		 * @param viewer Der zugehörige Viewer
		 * @param columnToListen Diese Columns können einzeln über Plug&Play 
		 * 			angesprochen werden
		 * @param defaultOptionclazz Es werden automatische Optionen dieser 
		 * 			Klasse erzeugt, wenn ein Element zu einer nicht Option	
		 * 			Hinzugefügt wird. Kann auch "Null" sein, dann wird keine 
		 * 			Option erzeugt, sondern direkt hinzugefügt
		 */
		public AuswahlDrop(TreeViewer viewer, 
				TreeColumn columnToListen, 
				Class defaultOptionclazz) {
			super(viewer, new TreeColumn[] {columnToListen});
			this.defaultOptionclazz = defaultOptionclazz;
		}
		
		public void setQuelle(CharElement quelle) {
			this.quelle = quelle;
		}


		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.common.DragAndDropSupport.SelectDrop#dropToColumn(int)
		 */
		@Override
		protected void dropToColumn(int index, TreeOrTableObject sourceObj, TreeOrTableObject targetObj) {
			((Link) targetObj.getValue()).setZweitZiel((CharElement) sourceObj.getValue());
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.common.DragAndDropSupport.SelectDrop#dropToTree()
		 */
		@Override
		protected void dropToTreeTable(TreeOrTableObject sourceObj, TreeOrTableObject targetObj) {
			// Prüfen ob das source zum Target passt
			if ( !validDropClass( (TreeObject) targetObj, sourceObj) ) {
				return;
			}
			
			if (targetObj.getValue() instanceof Link) {
				targetObj = ((TreeObject) targetObj).getParent(); // Da CharElement immer an einer Option hängen
			} else if (targetObj instanceof AuswahlTreeObject
					&& defaultOptionclazz != null) {
				// Direkt "positiv", "negativ", "Talent", o.ä.
				// Falls defaultOptionclazz == null, soll keine Option
				// erstellt werden

				if (((TreeObject) targetObj).getChildren() == null) {
					TreeObject newObject = new TreeObject(
							VoraussetzungPart.createOption(defaultOptionclazz), 
							((TreeObject) targetObj));
					((TreeObject) targetObj).addChildren(newObject);
				}
				
				targetObj = ((TreeObject) targetObj).getChildren()[0];
			}
			
			Object valueObj;
			if (sourceObj.getValue() instanceof Link) {
				valueObj = sourceObj.getValue();
				((IdLink) sourceObj.getValue()).setQuelle(quelle);
			} else {
				valueObj = new IdLink(
						quelle,
						(CharElement) sourceObj.getValue(), 
						null, 
						IdLink.KEIN_WERT, 
						null);
			}
			TreeObject newObject = new TreeObject(valueObj, ((TreeObject) targetObj));
			((TreeObject) targetObj).addChildren(newObject);
		}
	}
	
	/**
	 * Überprüft ob der "source" zum "node" passt. Es passt wenn node oder ein parent
	 * von node als AuswahlTreeObject die Klasse vom source haben
	 * @param node
	 * @param sourceObj
	 * @return
	 */
	private static boolean validDropClass(TreeObject node, TreeOrTableObject sourceObj) {
		if (node instanceof AuswahlTreeObject) {
			return ((Class) ((AuswahlTreeObject) node).getValue()).isAssignableFrom(
								sourceObj.getValue().getClass());
		} else if (node.getParent() == null) {
			return true; // Dies ist der Root, es gibt kein AuswahlTreeObject
		}
		return validDropClass(node.getParent(), sourceObj);
	}
	
}
