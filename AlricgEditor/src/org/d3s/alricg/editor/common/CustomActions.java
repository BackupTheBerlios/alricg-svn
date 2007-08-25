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
import org.d3s.alricg.editor.Messages;
import org.d3s.alricg.editor.common.CustomFilter.CurrentFileFilter;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.editors.composits.CharElementEditorInput;
import org.d3s.alricg.editor.editors.dialoge.ShowDependenciesDialog;
import org.d3s.alricg.editor.utils.EditorViewUtils;
import org.d3s.alricg.editor.utils.ViewEditorIdManager;
import org.d3s.alricg.editor.utils.EditorViewUtils.DependencyProgressMonitor;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTreeOrTableObject;
import org.d3s.alricg.editor.utils.Regulatoren.Regulator;
import org.d3s.alricg.editor.views.ViewModel;
import org.d3s.alricg.editor.views.charElemente.RefreshableViewPart;
import org.d3s.alricg.store.access.CharElementFactory;
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Talent;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
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
 * Verschiedene Actions
 * @author Vincent
 */
public class CustomActions {
	
	/**
	 * Wechselt die Ansicht zwischen dem TreeViewer und dem TableViewer
	 * @author Vincent
	 */
	public static class SwapTreeTableAction extends Action {
		private Composite parentComp;
		
		public SwapTreeTableAction(Composite parentComp) {
			super(Messages.Actions_ChangeView);
			
			this.parentComp = parentComp;
			this.setImageDescriptor(ControlIconsLibrary.swapTree_Table.getImageDescriptor());
			this.setToolTipText(Messages.Actions_ChangeView_TT);
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
			super(Messages.Actions_TableFilter, Action.AS_CHECK_BOX);
			
			this.clazz = clazz;
			this.setToolTipText(Messages.Actions_TableFilter_TT);
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
		
		/**
		 * Konstruktor
		 * @param parentComp Das Parent Composite von einem Tree/Table View
		 * @param Class clazz Die Klasse des Elements welche beareitet werden sollen
		 */
		public EditCharElementAction(Composite parentComp) {
			this.parentComp = parentComp;
			
			this.setText(Messages.Actions_Edit);
			this.setToolTipText(Messages.Actions_Edit_TT);
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
			
			if ( !(treeTableObj.getValue() instanceof CharElement) ) return;
			final IEditorInput editorInput = new CharElementEditorInput(
					(CharElement) treeTableObj.getValue(),
					treeTableObj.getAccessor(),
					false);
	
			try {
				page.openEditor(
						editorInput, 
						ViewEditorIdManager.getEditorID(treeTableObj.getValue().getClass()), 
						true);
				
			} catch (PartInitException e) {
				Activator.logger.log(
						Level.SEVERE, 
						"Konnte Editor nicht öffnen. Editor ID: " 
							+ ViewEditorIdManager.getEditorID(treeTableObj.getValue().getClass()),  //$NON-NLS-1$
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
	public static class BuildNewCharElementAction extends Action {
		protected Class charElementClazz;
		private final Regulator regulator;
		private final Composite parentComp;
		
		/**
		 * Konstruktor
		 */
		public BuildNewCharElementAction(
				Composite parentComp, 
				Class charElementClazz,
				Regulator regulator) {
			this.charElementClazz = charElementClazz;
			this.regulator = regulator;
			this.parentComp = parentComp;
			
			this.setText(Messages.Actions_NewElement);
			this.setToolTipText(Messages.Actions_NewElement_TT);
			this.setImageDescriptor(ControlIconsLibrary.add.getImageDescriptor());
		}
		
		public void run() {
			if (ViewModel.getMarkedFileForNew() == null) {
				MessageDialog.openInformation(
						((StackLayout) parentComp.getLayout()).topControl.getShell(),
						Messages.Actions_NewElementErrorDialogTitel, Messages.Actions_NewElementErrorDialogText);
			}
			
			XmlAccessor xmlAccessor = 
				StoreAccessor.getInstance().getXmlAccessor(ViewModel.getMarkedFileForNew());
			CharElement newCharElem =
				CharElementFactory.getInstance().buildCharElement(charElementClazz, xmlAccessor);
			
			// Falls in einem Tree erstellt, dann 
			if (ViewUtils.getSelectedObject(parentComp) instanceof TreeObject) {
				runForTreeView(newCharElem, (TreeObject) ViewUtils.getSelectedObject(parentComp));
			}
			
			// Öffnen Editor mit neuem CharElement
			final IWorkbenchPage page = PlatformUI.getWorkbench()
										.getActiveWorkbenchWindow().getActivePage();
			final IEditorInput editorInput = new CharElementEditorInput(newCharElem, xmlAccessor, true);
			
			try {
				page.openEditor(editorInput, ViewEditorIdManager.getEditorID(charElementClazz), true);
			} catch (PartInitException e) {
				Activator.logger.log(
						Level.SEVERE, 
						"Konnte Editor nicht öffnen. Editor ID: " + ViewEditorIdManager.getEditorID(charElementClazz),  //$NON-NLS-1$
						e);
			}
		}

		/**
		 * Ermöglichst spezifische behandlung von neuen Elementen in einem Tree.
		 * So können z.B. neue Talente, die unter dem Baum "Naturtalente" angelegt wurden
		 * auch gleich als Naturtalent angelegt werden.
		 * @param newCharElem Das neu erstellte Element, welches evtl. geändert wird
		 * @param treeObj Das TreeObject, welche selektiert ist
		 *
		protected abstract void runForTreeView(CharElement newCharElem, TreeObject treeObj);
		*/
		//@Override
		protected void runForTreeView(CharElement newCharElem, TreeObject treeObj) {
			if (treeObj.getValue().getClass() == charElementClazz) {
				runForTreeView(newCharElem, (TreeObject) treeObj.getParent());
			} else if (treeObj.getValue().getClass() == regulator.getFirstCategoryClass()) {
				regulator.setFirstCategory(newCharElem, treeObj.getValue());
			} else if (treeObj.getValue() instanceof String) {
				newCharElem.setSammelbegriff(treeObj.getValue().toString());
				runForTreeView(newCharElem, (TreeObject) treeObj.getParent());
			}
		}
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
			
			this.setText(Messages.Actions_Delete);
			this.setToolTipText(Messages.Actions_Delete_TT);;
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
				} catch (Exception e) {
					Activator.logger.log(
							Level.SEVERE, 
							"Beim erzeugen des  ProgressMonitorDialogs ist ein Fehler aufgetreten.",  //$NON-NLS-1$
							e);
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
						Messages.Actions_DeleteConfirmDialog, 
						"Möchten sie das Element "  //$NON-NLS-1$
						+ charElement.getName()
						+ " wirklich löschen?"); //$NON-NLS-1$
				
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
				} catch (Exception e) {
					Activator.logger.log(
							Level.SEVERE, 
							"Beim speichern der Datei " + treeTableObj.getAccessor().getFile().getName()  //$NON-NLS-1$
							+ " innerhalb einer Löschen Action ist ein Fehler aufgetreten.",  //$NON-NLS-1$
							e);
					
					MessageDialog.openError(
							parentComp.getShell(), 
							Messages.Actions_SaveErrorDialogTitle, 
							Messages.bind(
									Messages.Actions_SaveErrorDialogTitle, 
									treeTableObj.getAccessor().getFile().getName())
					);
				}
			}
		}
	}
	
	public static class InfoCharElementAction extends Action {
		// TODO implement
		
		public InfoCharElementAction() {
			this.setText(Messages.Actions_Infos);
			this.setToolTipText(Messages.Actions_Infos_TT);
			this.setImageDescriptor(ControlIconsLibrary.info.getImageDescriptor());
		}
		

	}
	
}
