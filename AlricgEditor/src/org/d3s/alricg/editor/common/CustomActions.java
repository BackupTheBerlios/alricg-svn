/*
 * Created 24.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

import javax.xml.bind.JAXBException;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.d3s.alricg.editor.Activator;
import org.d3s.alricg.editor.common.CustomFilter.CurrentFileFilter;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.editors.composits.CharElementEditorInput;
import org.d3s.alricg.editor.editors.dialoge.ShowDependenciesDialog;
import org.d3s.alricg.editor.utils.EditorViewUtils;
import org.d3s.alricg.editor.utils.ViewEditorIdManager;
import org.d3s.alricg.editor.utils.EditorViewUtils.DependencyProgressMonitor;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTreeOrTableObject;
import org.d3s.alricg.editor.views.ViewModel;
import org.d3s.alricg.editor.views.charElemente.RefreshableViewPart;
import org.d3s.alricg.store.access.CharElementFactory;
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
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
		private final Class clazz;
		
		public FilterCurrentFileAction(Class clazz) {
			super("Tabelle Filtern", Action.AS_CHECK_BOX);
			
			this.clazz = clazz;
			this.setToolTipText("Zeigt nur Elemente der aktuell selektierten Datei.");
			this.setImageDescriptor(ControlIconsLibrary.filterTable.getImageDescriptor());
		}
		
		@Override
		public void run() {
			final RefreshableViewPart view = 
				(RefreshableViewPart) ViewEditorIdManager.getView(clazz);
			if (view == null) return;
			
			if (this.isChecked()) {
				view.getTableViewer().addFilter(filter);
				view.getTreeViewer().addFilter(filter);
			} else {
				view.getTableViewer().removeFilter(filter);
				view.getTreeViewer().removeFilter(filter);
			}
		}
		
		// Vom ISelectionListener - reagiert auf die Selektion im File-View
		// und set das entsprechende File im Filter 
		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			final RefreshableViewPart view = 
				(RefreshableViewPart) ViewEditorIdManager.getView(clazz);
			if (view == null) return;
			
			if (selection.isEmpty()) {
				setCurrentFile(null);
				view.getTableViewer().refresh();
				view.getTreeViewer().refresh();
				return;
			}
			
			TreeObject treeObj = (TreeObject) ((TreeSelection) selection).getFirstElement();
			setCurrentFile(((File) treeObj.getValue()));
			view.getTableViewer().refresh();
			view.getTreeViewer().refresh();
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
		private final Composite parentComp;
		private final String editorID;
		
		/**
		 * Konstruktor
		 * @param parentComp Das Parent Composite von einem Tree/Table View
		 * @param editorID Die ID des Editors, der zum bearbeiten geöffnet werden soll
		 */
		public EditCharElementAction(Composite parentComp, String editorID) {
			this.parentComp = parentComp;
			this.editorID = editorID;
			
			this.setText("Bearbeiten");
			this.setToolTipText("Öffnet das selektierte Element zur Bearbeitung.");
			this.setImageDescriptor(ControlIconsLibrary.edit.getImageDescriptor());
		}
		
		public void run() {
			if (!(ViewUtils.getSelectedObject(parentComp) instanceof EditorTreeOrTableObject)) {
				return;
			}
			
			final EditorTreeOrTableObject treeTableObj =  
						(EditorTreeOrTableObject) ViewUtils.getSelectedObject(parentComp);
			final IWorkbenchPage page = PlatformUI.getWorkbench()
											.getActiveWorkbenchWindow().getActivePage();
			
			final IEditorInput editorInput = new CharElementEditorInput(
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
	
	/**
	 * Erstellt ein neues CharElement. Damit diese Action ausgeführt werden kann,
	 * muß das Feld "ViewModel.MarkedFileForNew" gesetzt werden
	 * 
	 * @author Vincent
	 */
	public static abstract class BuildNewCharElementAction extends Action {
		private final Class clazz;
		private final Composite parentComp;
		private final String editorID;
		
		/**
		 * Konstruktor
		 */
		public BuildNewCharElementAction(
				Composite parentComp, 
				String editorID,
				Class clazz) {
			this.clazz = clazz;
			this.parentComp = parentComp;
			this.editorID = editorID;
			
			this.setText("Neu erzeugen");
			this.setToolTipText("Erzeugt ein neues Element und öffnet es zur Bearbeitung.");
			this.setImageDescriptor(ControlIconsLibrary.add.getImageDescriptor());
		}
		
		public void run() {
			if (ViewModel.getMarkedFileForNew() == null) {
				MessageDialog.openInformation(
						((StackLayout) parentComp.getLayout()).topControl.getShell(),
						"Nicht Möglich", "Bitte setzen sie erst in der Datei-Ansischt die Datei, " +
								"in welcher das neue Element erzeugt werden soll.");
			}
			
			XmlAccessor xmlAccessor = 
				StoreAccessor.getInstance().getXmlAccessor(ViewModel.getMarkedFileForNew());
			CharElement newCharElem =
				CharElementFactory.getInstance().buildCharElement(clazz, xmlAccessor);
			
			// Falls in einem Tree erstellt, dann 
			if (ViewUtils.getSelectedObject(parentComp) instanceof TreeObject) {
				runForTreeView(newCharElem, (TreeObject) ViewUtils.getSelectedObject(parentComp));
			}
			
			// Öffnen Editor mit neuem CharElement
			final IWorkbenchPage page = PlatformUI.getWorkbench()
										.getActiveWorkbenchWindow().getActivePage();
			final IEditorInput editorInput = new CharElementEditorInput(newCharElem, xmlAccessor);
			
			try {
				page.openEditor(editorInput, editorID, true);
			} catch (PartInitException e) {
				Activator.logger.log(
						Level.SEVERE, 
						"Konnte Editor nicht öffnen. Editor ID: " + editorID, 
						e);
			}
		}

		/**
		 * Ermöglichst spezifische behandlung von neuen Elementen in einem Tree.
		 * So können z.B. neue Talente, die unter dem Baum "Naturtalente" angelegt wurden
		 * auch gleich als Naturtalent angelegt werden.
		 * @param newCharElem Das neu erstellte Element, welches evtl. geändert wird
		 * @param treeObj Das TreeObject, welche selektiert ist
		 */
		protected abstract void runForTreeView(CharElement newCharElem, TreeObject treeObj);
	}

	/**
	 * 
	 * @author Vincent
	 */
	public static class DeleteCharElementAction extends Action {
		private final Composite parentComp;
		private final Class clazz;
		
		/**
		 * Konstruktor
		 */
		public DeleteCharElementAction(Composite parentComp, Class clazz) {
			this.parentComp = parentComp;
			this.clazz = clazz;
			
			this.setText("Löschen");
			this.setToolTipText("Löscht das selektierte Element.");;
			this.setImageDescriptor(ControlIconsLibrary.delete.getImageDescriptor());
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.action.Action#run()
		 */
		@Override
		public void run() {
			if (!(ViewUtils.getSelectedObject(parentComp) instanceof EditorTreeOrTableObject)) {
				return;
			}
			final RefreshableViewPart view = 
						(RefreshableViewPart) ViewEditorIdManager.getView(clazz);
			final EditorTreeOrTableObject treeTableObj = 
						(EditorTreeOrTableObject) ViewUtils.getSelectedObject(parentComp);
			final DependencyProgressMonitor monitor = new DependencyProgressMonitor((CharElement) treeTableObj.getValue());
			
			if (treeTableObj.getValue() instanceof CharElement) {
				// 1. Prüfen
			    try {
					new ProgressMonitorDialog(parentComp.getShell()).run(true, true, monitor);
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				
				if (monitor.getMonitor().isCanceled())  {
					// Abbruch durch user
					return;
				} else if (monitor.getDepList().size() > 0) {
					// Kann nicht gelöscht werden, da Abhängigkeiten bestehen
					final ShowDependenciesDialog depDialog = new ShowDependenciesDialog(
							parentComp.getShell(),
							(CharElement) treeTableObj.getValue(),
							monitor.getDepList() );
					depDialog.open();
					return;
				}
				
				// CharElement kann gelöscht werden: Sicherheitsabfrage
				final CharElement charElement = (CharElement) treeTableObj.getValue();
				final boolean b = MessageDialog.openConfirm(
						parentComp.getShell(), 
						"Löschen bestätigen", 
						"Möchten sie das Element " 
						+ charElement.getName()
						+ " wirklich löschen?");
				
				// Löschen
				if (!b) return;
				
				EditorViewUtils.removeElementFromView(
						view,
						charElement);
				CharElementFactory.getInstance().deleteCharElement(
						charElement,
						treeTableObj.getAccessor());
				if (view != null) view.refresh();
				
				try {
					StoreAccessor.getInstance().saveFile( treeTableObj.getAccessor() );
				} catch (JAXBException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static class InfoCharElementAction extends Action {
		// TODO implement
		
		public InfoCharElementAction() {
			this.setText("Informationen");
			this.setToolTipText("Zeigt zu Element weitere Informationen an.");
			this.setImageDescriptor(ControlIconsLibrary.info.getImageDescriptor());
		}
		

	}
	
}
