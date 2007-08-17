/*
 * Created 09.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common;

import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.editors.composits.VoraussetzungPart;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.charElemente.links.OptionVoraussetzung;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;
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
	
	
	public static class AuswahlDrop extends ViewerDropAdapter implements MouseMoveListener {
		private TreeOrTableObject sourceObj;
		private TreeObject targetObj;
		private final TreeColumn columnToListen;
		private CharElement quelle;
		
		public AuswahlDrop(TreeViewer viewer, TreeColumn columnToListen) {
			super(viewer);
			this.columnToListen = columnToListen;
		}
		
		public void setQuelle(CharElement quelle) {
			this.quelle = quelle;
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerDropAdapter#drop(org.eclipse.swt.dnd.DropTargetEvent)
		 */
		@Override
		public void drop(DropTargetEvent event) {
			// Setzt sourceObj/ targetObj. Das eigentliche Drop wird dann
			// von dem der "mouseMove" Methode ausgeführt. Diese kann
			// festellen auf welcher Spalte das Drop ausgeführt wurde:
			// Wichtig für Zweitziel!
			sourceObj = (TreeOrTableObject) ((StructuredSelection) event.data).getFirstElement();
			targetObj = (TreeObject) getCurrentTarget();
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerDropAdapter#performDrop(java.lang.Object)
		 */
		@Override
		public boolean performDrop(Object data) {
			return true;
		}

		private void dropToZweitZiel() {
			((Link) targetObj.getValue()).setZweitZiel((CharElement) sourceObj.getValue());
		}
		
		private void dropToTree() {
			if (((TreeObject) targetObj).getValue() instanceof Link) {
				targetObj = targetObj.getParent(); // Da CharElement immer an einer Option hängen
			} else if (((TreeObject) targetObj).getValue() instanceof String) {
				// Direkt "positiv" oder "negativ"
				if (targetObj.getChildren() == null) {
					TreeObject newObject = new TreeObject(
							VoraussetzungPart.createOption(OptionVoraussetzung.class), 
							targetObj);
					targetObj.addChildren(newObject);
	
				}
				
				targetObj = targetObj.getChildren()[0];
			}
			
			Object valueObj;
			if (sourceObj.getValue() instanceof Link) {
				valueObj = sourceObj.getValue();
			} else {
				valueObj = new IdLink(
						quelle,
						(CharElement) sourceObj.getValue(), 
						null, 
						IdLink.KEIN_WERT, 
						null);
			}
			TreeObject newObject = new TreeObject(valueObj, targetObj);
			targetObj.addChildren(newObject);
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ViewerDropAdapter#validateDrop(java.lang.Object,
		 *      int, org.eclipse.swt.dnd.TransferData)
		 */
		@Override
		public boolean validateDrop(Object target, int operation, TransferData transferType) {
			if (target == null) {
				return false;
			}
			return true;
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
			
			TreeColumn currentTreeColumn = null;
			final TreeItem item = ((TreeViewer) this.getViewer()).getTree().getItem(0);
			for (int i = 0; i < ((TreeViewer) this.getViewer()).getTree().getColumnCount(); i++) {
		        Rectangle rect = item.getBounds(i);
		        if (rect.x < e.x && rect.x + rect.width > e.x) {
		        	currentTreeColumn = ((TreeViewer) this.getViewer()).getTree().getColumn(i);
		        }
			}
			if (columnToListen.equals(currentTreeColumn)) {
				// Es wurde ins ZweitZiel "gedropt"
				dropToZweitZiel();
			} else {
				// Es wurde ein "normales" Drop ausgeführt
				dropToTree();
			}
			
			sourceObj = null;
			targetObj = null;
			
			this.getViewer().refresh();
		}

	}
}
