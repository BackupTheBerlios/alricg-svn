/*
 * Created 24.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common;

import java.io.File;
import java.util.logging.Level;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.d3s.alricg.editor.Activator;
import org.d3s.alricg.editor.common.CustomFilter.CurrentFileFilter;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.utils.CharElementEditorInput;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTreeOrTableObject;
import org.d3s.alricg.store.charElemente.CharElement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * @author Vincent
 *
 */
public class CustomActions {
	
	/**
	 * Wechselt die Ansicht zwischen dem TreeViewer und dem TableViewer
	 * @author Vincent
	 */
	public static class SwapTreeTableAction extends Action {
		private Composite parentComp;
		
		public SwapTreeTableAction(Composite parentComp) {
			super("Ansicht wechseln");
			
			this.parentComp = parentComp;
			this.setImageDescriptor(ControlIconsLibrary.swapTree_Table.getImageDescriptor());
			this.setToolTipText("Wechselt die Ansicht zwischen Tabelle und Baum");
		}
		
		@Override
		public void run() {
			final Control topControl = ((StackLayout) parentComp.getLayout()).topControl;
			
			if ( topControl.equals(parentComp.getChildren()[0]) ) {
				((StackLayout) parentComp.getLayout()).topControl = parentComp.getChildren()[1];
			} else {
				((StackLayout) parentComp.getLayout()).topControl = parentComp.getChildren()[0];
			}
			parentComp.layout();
		}
	}
	
	/**
	 * Ein "CheckBox-Button". Wenn aktiviert, wird stehts nur das aktuell in FileView
	 * selektierte File bei der Anzeige berücksichtig
	 * @author Vincent
	 */
	public static class FilterCurrentFileAction extends Action implements ISelectionListener {
		private final CurrentFileFilter filter = new CurrentFileFilter();
		private TableViewer viewerTable;
		private TreeViewer viewerTree;
		
		public FilterCurrentFileAction(TableViewer viewerTable, TreeViewer viewerTree) {
			super("Tabelle Filtern", Action.AS_CHECK_BOX);
			
			this.viewerTable = viewerTable;
			this.viewerTree = viewerTree;
			this.setToolTipText("Zeigt nur Elemente der aktuell selektierten Datei.");
			this.setImageDescriptor(ControlIconsLibrary.filterTable.getImageDescriptor());
		}
		
		@Override
		public void run() {
			
			if (this.isChecked()) {
				viewerTable.addFilter(filter);
				viewerTree.addFilter(filter);
			} else {
				viewerTable.removeFilter(filter);
				viewerTree.removeFilter(filter);
			}
		}
		
		// Vom ISelectionListener - reagiert auf die Selektion im File-View
		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (selection.isEmpty()) {
				setCurrentFile(null);
				viewerTable.refresh();
				viewerTree.refresh();
				return;
			}
			
			TreeObject treeObj = (TreeObject) ((TreeSelection) selection).getFirstElement();
			setCurrentFile(((File) treeObj.getValue()));
			viewerTable.refresh();
			viewerTree.refresh();
		}
		
		public void setCurrentFile(File file) {
			filter.setModelFile(file);
		}
	}
	
	/**
	 * Öffnet bei Aktivierung den Editor mit der "editorID" um das aktuelle in einer 
	 * tree/table selektierte Element zu bearbeiten.
	 * @author Vincent
	 */
	public static class EditCharElementAction extends Action {
		private Composite parentComp;
		private String editorID;
		
		/**
		 * Konstruktor
		 * @param parentComp Das Parent Composite von einem Tree/Table View
		 * @param editorID Die ID des Editors, der zum bearbeiten geöffnet werden soll
		 */
		public EditCharElementAction(Composite parentComp, String editorID) {
			this.parentComp = parentComp;
			this.editorID = editorID;
			
			this.setText("Bearbeiten");
			this.setToolTipText("Öffnet das selektierte Talent zur Bearbeitung.");
			this.setImageDescriptor(ControlIconsLibrary.edit.getImageDescriptor());
		}
		
		public void run() {
			if (!(ViewUtils.getSelectedObject(parentComp) instanceof EditorTreeOrTableObject)) {
				return;
			}
			
			final EditorTreeOrTableObject treeTableObj = 
						(EditorTreeOrTableObject) ViewUtils.getSelectedObject(parentComp);
			final IWorkbenchWindow activeWorkbenchWindow = 
						PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			final IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
			
			IEditorInput editorInput = new CharElementEditorInput(
					(CharElement) treeTableObj.getValue(),
					treeTableObj.getAccessor());
	
			try {
				page.openEditor(editorInput, editorID, true);
			} catch (PartInitException e) {
				Activator.logger.log(
						Level.SEVERE, 
						"Konnte Editor nicht öffnen. Editor ID: " + editorID, 
						e);
			}
		}
	}
	
	public static class BuildNewCharElementAction extends Action {
		// TODO implement
		/**
		 * Konstruktor
		 */
		public BuildNewCharElementAction() {
			
			this.setText("Neu erzeugen");
			this.setToolTipText("Erzeugt ein neues Talent und öffnet es zur Bearbeitung.");
			this.setImageDescriptor(ControlIconsLibrary.add.getImageDescriptor());
		}
		
	}

	public static class DeleteCharElementAction extends Action {
		// TODO implement
		/**
		 * Konstruktor
		 */
		public DeleteCharElementAction() {
			
			this.setText("Löschen");
			this.setToolTipText("Löscht das selektierte Talent.");;
			this.setImageDescriptor(ControlIconsLibrary.delete.getImageDescriptor());
		}
	}
	
}
