/*
 * Created 24.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common;

import java.io.File;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.d3s.alricg.editor.common.CustomFilter.CurrentFileFilter;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Vincent
 *
 */
public class CustomActions {
	
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
		
		public void setCurrentFile(File file) {
			filter.setModelFile(file);
		}
		
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
	}
	

	
	
}
