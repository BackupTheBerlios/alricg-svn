/*
 * Created 15.11.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.common;

import org.d3s.alricg.common.logic.Prozessor;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.generator.Activator;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.links.Link;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

/**
 * Verschiedene Klassen zur Unterstützung
 * @author Vincent
 */
public class Utils {
	
	/**
	 * DropAdapter um Elemente zum Held hinzuzufügen. Wird dem Helden-View zugeordent, dem
	 * per Drag&Drop neue Elemente hinzugefügt werden können sollen.
	 * @author Vincent
	 */
	public static class DropAddToHeld extends ViewerDropAdapter {
		private final Class<CharElement> clazz;
		
		/**
		 * Konstruktor
		 * @param viewer Der Viewer, zu dem Element hinzugefügt werden können
		 * @param clazz Die Klasse von Elementen (Talente, Zauber, o.ä.), die zu dem
		 * 	View hinzugefühgt werden kann.
		 */
		public DropAddToHeld(Viewer viewer, Class clazz) {
			super(viewer);
			this.clazz = clazz;
		}

		@Override
		public void drop(DropTargetEvent event) {
			final TreeOrTableObject sourceObj = 
				(TreeOrTableObject) ((StructuredSelection) event.data).getFirstElement();
			
			// Guard ob der value gültig ist
			if ( sourceObj == null
					|| !(sourceObj.getValue() instanceof CharElement)
					|| !(Talent.class.isInstance(sourceObj.getValue())) ) {
				return;
			}
			
			// Hinzufügen, wenn möglich
			final Prozessor prozessor = Activator.getCurrentCharakter().getProzessor(clazz);
			if ( prozessor.canAddElement( clazz.cast(sourceObj.getValue()) ) ) {
				prozessor.addNewElement( clazz.cast(sourceObj.getValue()) );
			}
		}
		
		@Override
		public boolean performDrop(Object data) {
			// wird durch das selbst implementierte "drop" nicht mehr aufgerufen, muss aber 
			// implementiert werden
			return true;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerDropAdapter#validateDrop(java.lang.Object, int, org.eclipse.swt.dnd.TransferData)
		 */
		@Override
		public boolean validateDrop(Object target, int operation, TransferData transferType) {
			return true;
		}
	}
	
	
	/**
	 * DropAdapter um Elemente zum Held hinzuzufügen. Wird dem Helden-View zugeordent, dem
	 * per Drag&Drop neue Elemente hinzugefügt werden können sollen.
	 * @author Vincent
	 */
	public static class DropRemoveFromHeld extends ViewerDropAdapter {
		private final Class<CharElement> clazz;
		
		/**
		 * Konstruktor
		 * @param viewer Der Viewer, zu dem Element hinzugefügt werden können
		 * @param clazz Die Klasse von Elementen (Talente, Zauber, o.ä.), die zu dem
		 * 	View hinzugefühgt werden kann.
		 */
		public DropRemoveFromHeld(Viewer viewer, Class clazz) {
			super(viewer);
			this.clazz = clazz;
		}

		@Override
		public void drop(DropTargetEvent event) {
			final TreeOrTableObject sourceObj = 
				(TreeOrTableObject) ((StructuredSelection) event.data).getFirstElement();
			
			// Guard ob der value gültig ist
			if ( sourceObj == null
					|| !(sourceObj.getValue() instanceof Link)
					|| !(Talent.class.isInstance( ((Link) sourceObj.getValue()).getZiel() )) ) {
				return;
			}
			
			// Hinzufügen, wenn möglich
			final Prozessor prozessor = Activator.getCurrentCharakter().getProzessor(clazz);
			if ( prozessor.canRemoveElement( (Link) sourceObj.getValue() ) ) {
				prozessor.removeElement( (Link) sourceObj.getValue() );
			}
		}
		
		@Override
		public boolean performDrop(Object data) {
			// wird durch das selbst implementierte "drop" nicht mehr aufgerufen, muss aber 
			// implementiert werden
			return true;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerDropAdapter#validateDrop(java.lang.Object, int, org.eclipse.swt.dnd.TransferData)
		 */
		@Override
		public boolean validateDrop(Object target, int operation, TransferData transferType) {
			return true;
		}
	}
	
	/**
	 * Unterstützung um ein Link-Element aus einem View in einen anderen Ziehen zu können.
	 * Sonst ist keine Logik enthalten. (Die Logik steckt im jeweiligem Drop) 
	 * @author Vincent
	 */
	public static class LinkDrag implements DragSourceListener {
		private final Viewer viewer;
		
		public LinkDrag(Viewer viewer) {
			this.viewer = viewer;
		}
		
		@Override
		public void dragStart(DragSourceEvent event) {
			if (viewer.getSelection().isEmpty()) {
				event.doit = false;
			}
			final TreeOrTableObject treeTableObj = (TreeOrTableObject) ((StructuredSelection) viewer
					.getSelection()).getFirstElement();

			if (treeTableObj == null || !(treeTableObj.getValue() instanceof Link)) {
				event.doit = false;
			}
		}

		@Override
		public void dragSetData(DragSourceEvent event) {
			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			LocalSelectionTransfer.getTransfer().setSelection(selection);
		}

		@Override
		public void dragFinished(DragSourceEvent event) {
			// Noop
		}
	}
}
