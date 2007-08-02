/*
 * Created 01.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.views.charElemente;

import java.util.List;

import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.common.CustomActions.BuildNewCharElementAction;
import org.d3s.alricg.editor.common.ViewUtils.Regulator;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.utils.EditorViewUtils;
import org.d3s.alricg.editor.views.FileView;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Talent;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

/**
 * @author Vincent
 *
 */
public abstract class RefreshableViewPart extends ViewPart {
	protected TableViewer viewerTable;
	protected TreeViewer viewerTree;
	protected Composite parentComp;

	protected Action swapTreeTable;
	protected Action filterAktuellesFile;
	protected Action showInfos;
	protected Action buildNew;
	protected Action deleteSelected;
	protected Action editSelected;


	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new StackLayout());

		this.parentComp = parent;
		viewerTable = createTable(parent);
		viewerTree = createTree(parent);
		
		((StackLayout) parent.getLayout()).topControl = viewerTree.getTree();
		parent.layout();

		makeActions();
		hookContextMenu();
		contributeToActionBars();

		getSite().getPage().addSelectionListener(
				FileView.ID,
				(ISelectionListener) filterAktuellesFile);
	}
	
	/**
	 * Setzt das Context-menu
	 */
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				RefreshableViewPart.this.fillContextMenu(manager);
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
				
				for (int i = 0; i < manager.getItems().length; i++) {
					if (!(manager.getItems()[i] instanceof ActionContributionItem)) {
						continue;
					}
					ActionContributionItem item = (ActionContributionItem) manager.getItems()[i];
					
					if (!(item.getAction() instanceof BuildNewCharElementAction)) {
						item.getAction().setEnabled(isEnabled);
					}
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
	
	// Füllt Action-Bars
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	// Das Context Menu beim Rechts-klick
	private void fillContextMenu(IMenuManager manager) {
		manager.add(this.showInfos);
		manager.add(new Separator());
		manager.add(this.buildNew);
		manager.add(this.editSelected);
		manager.add(this.deleteSelected);
	}

	/*
	 * Das "Pop-Up" Menu rechts Op
	 */
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(this.buildNew);
		manager.add(new Separator());
		manager.add(this.swapTreeTable);
		manager.add(this.filterAktuellesFile);
	}
	
	/**
	 * Erezugt den TableViewer.
	 * Eine Ansicht der Elemente in Tabellen Ansicht
	 */
	protected abstract TableViewer createTable(Composite parent);
	
	/**
	 * Erezugt den TreeViewer.
	 * Eine Ansicht der Elemente in Baum Ansicht
	 */
	protected abstract TreeViewer createTree(Composite parent);
	
	/**
	 * Erstellt die Actions
	 */
	protected abstract void makeActions();
	
	protected void showMessage(String titel, String message) {
		MessageDialog.openInformation(
				((StackLayout) parentComp.getLayout()).topControl.getShell(),
				titel, message);

	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		((StackLayout) parentComp.getLayout()).topControl.setFocus();
	}
	
	/**
	 * Refresht den Tree und die Tabelle
	 */
	public void refresh() {
		viewerTable.refresh();
		viewerTree.refresh();
	}
	
	
	public TableViewer getTableViewer() {
		return viewerTable;
	}
	
	public TreeViewer getTreeViewer() {
		return viewerTree;
	}
	
	public abstract Regulator getRegulator();
	
	/*
	public void rebuildTreeTable() {

		((TableViewContentProvider) viewerTable.getContentProvider())
			.setElementList(
					EditorViewUtils.buildTableView(
					StoreDataAccessor.getInstance().getXmlAccessors(), 
					getRegulator())
				);
		
		((TreeViewContentProvider) viewerTree.getContentProvider())
			.setRoot(EditorViewUtils.buildEditorViewTree(
					StoreDataAccessor.getInstance().getXmlAccessors(), 
					getRegulator()));
	}*/
}
