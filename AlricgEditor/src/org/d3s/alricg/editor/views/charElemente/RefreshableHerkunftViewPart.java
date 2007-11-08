/*
 * Created 04.09.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.views.charElemente;

import java.util.logging.Level;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.d3s.alricg.editor.Activator;
import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.editors.composits.CharElementEditorInput;
import org.d3s.alricg.editor.editors.composits.HerkunftVarianteEditorInput;
import org.d3s.alricg.editor.utils.ViewEditorIdManager;
import org.d3s.alricg.editor.utils.CustomActions.BuildNewCharElementAction;
import org.d3s.alricg.editor.utils.CustomActions.DeleteCharElementAction;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTreeOrTableObject;
import org.d3s.alricg.store.access.CharElementFactory;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.HerkunftVariante;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * @author Vincent
 */
public abstract class RefreshableHerkunftViewPart extends RefreshableViewPartImpl {

	protected Action buildNewVariante;
	
	/**
	 * Erstellt die Actions
	 */
	@Override
	protected void makeActions() {
		super.makeActions();
		
		buildNewVariante = new Action(){
			@Override
			public void run() {
				EditorTreeOrTableObject newObjParent = 
					(EditorTreeOrTableObject) ViewUtils.getSelectedObject(parentComp);
				
				XmlAccessor xmlAccessor = newObjParent.getAccessor();
				HerkunftVariante newCharElem = (HerkunftVariante)
					CharElementFactory.getInstance().buildHerkunftVariante(
							newObjParent.getValue().getClass(),
							(Herkunft) newObjParent.getValue());
				
				// Öffnen Editor mit neuem CharElement
				final IWorkbenchPage page = PlatformUI.getWorkbench()
											.getActiveWorkbenchWindow().getActivePage();
				//final IEditorInput editorInput2 = new CharElementEditorInput(newCharElem, xmlAccessor, true);
				final IEditorInput editorInput = new HerkunftVarianteEditorInput(
						newCharElem, 
						(Herkunft) newObjParent.getValue(), 
						xmlAccessor, true);
				try {
					page.openEditor(editorInput, ViewEditorIdManager.getEditorID(newCharElem.getClass()), true);
				} catch (PartInitException e) {
					Activator.logger.log(
							Level.SEVERE, 
							"Konnte Editor nicht öffnen. Editor ID: " + ViewEditorIdManager.getEditorID(newCharElem.getClass()),  //$NON-NLS-1$
							e);
				}
			}
		};
		buildNewVariante.setText("Neue Variante");
		buildNewVariante.setImageDescriptor(ControlIconsLibrary.add.getImageDescriptor());
		
		// Neues Element Action 
		buildNew = new BuildNewCharElementAction(this.parentComp, getViewedClass(), getRegulator()) {
			@Override
			protected void runForTreeView(CharElement newCharElem, TreeObject treeObj) {
				if (treeObj.getValue() instanceof CharElement) {
					runForTreeView(newCharElem, (TreeObject) treeObj.getParent());
				} else if (treeObj.getValue().getClass() == regulator.getFirstCategoryClass()) {
					regulator.setFirstCategory(newCharElem, treeObj.getValue());
				} else if (treeObj.getValue() instanceof String) {
					if (treeObj.getParent() == null) return; // Dies ist der Root
					newCharElem.setSammelbegriff(treeObj.getValue().toString());
					runForTreeView(newCharElem, (TreeObject) treeObj.getParent());
				}
			}};
			
		// Element löschen Action
		deleteSelected = new DeleteCharElementAction(this.parentComp, getViewedClass());
	}
	
	// Das Context Menu beim Rechts-klick
	@Override
	protected void fillContextMenu(IMenuManager manager) {
		manager.add(this.showInfos);
		manager.add(new Separator());
		manager.add(this.buildNew);
		manager.add(this.buildNewVariante);
		manager.add(this.editSelected);
		manager.add(this.deleteSelected);
		/* Um die Warnung
		 * Context menu missing standard group 'org.eclipse.ui.IWorkbenchActionConstants.MB_ADDITIONS'
		 * im Log zu verhindern */ 
		manager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	/**
	 * Setzt das Context-menu
	 */
	@Override
	protected void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				RefreshableHerkunftViewPart.this.fillContextMenu(manager);
			}
		});
		
		menuMgr.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				boolean isEnabled = true;
				final TreeOrTableObject treeTableObj = ViewUtils.getSelectedObject(parentComp);
				
				if (treeTableObj != null && treeTableObj.getValue() instanceof CharElement) {
					isEnabled = true;
				} else {
					isEnabled = false;
				}
				showInfos.setEnabled(isEnabled);
				editSelected.setEnabled(isEnabled);
				deleteSelected.setEnabled(isEnabled);
				showInfos.setEnabled(isEnabled);
				showInfos.setEnabled(isEnabled);
				
				if (treeTableObj != null && treeTableObj.getValue() instanceof Herkunft) {
					buildNewVariante.setEnabled(true);
				} else {
					buildNewVariante.setEnabled(false);
				}
				if (treeTableObj != null 
						&& treeTableObj.getValue() instanceof Herkunft
						&& ((Herkunft) treeTableObj.getValue()).getVarianten() != null
						&& ((Herkunft) treeTableObj.getValue()).getVarianten().length > 0) {
					deleteSelected.setEnabled(false);
				} else {
					deleteSelected.setEnabled(isEnabled);
				}
			}
		});

		// For Tree
		Menu menu = menuMgr.createContextMenu(viewerTree.getControl());
		viewerTree.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewerTree);

		// For Table
		menu = menuMgr.createContextMenu(viewerTable.getControl());
		viewerTable.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewerTable);
	}
}
