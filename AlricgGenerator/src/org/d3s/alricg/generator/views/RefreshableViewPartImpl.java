/*
 * Created 27.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.views;

import org.d3s.alricg.common.logic.ProzessorObserver;
import org.d3s.alricg.editor.common.RefreshableViewPart;
import org.d3s.alricg.editor.common.Regulatoren.Regulator;
import org.d3s.alricg.editor.common.ViewUtils.ObjectCreator;
import org.d3s.alricg.editor.common.ViewUtils.TableObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.generator.common.CustomActions.SwapTreeTableAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

/**
 * Basisklasse für alle Views!
 * @author Vincent
 */
public abstract class RefreshableViewPartImpl extends ViewPart implements ProzessorObserver, RefreshableViewPart {

	protected TableViewer viewerTable; // Für die Darstellung als Tabelle
	protected TreeViewer viewerTree; // Für die Darstellung als Tree
	protected Composite parentComp;

	protected Action swapTreeTable;
	protected Action showInfos;
	protected Action addToHeld;
	protected Action removeFromHeld;

	protected static final int DEFAULT_FIRSTCOLUMN_WIDTH = 200;

	private ObjectCreator objCreator = new ObjectCreator()  {
		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.common.ViewUtils.ObjectCreator#createTableObject(java.lang.Object)
		 */
		@Override
		public TableObject createTableObject(Object element) {
			return new TableObject(element);
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.common.ViewUtils.ObjectCreator#createTreeObject(java.lang.Object, org.d3s.alricg.editor.common.ViewUtils.TreeObject)
		 */
		@Override
		public TreeObject createTreeObject(Object element, TreeObject parentNode) {
			return new TreeObject(element, parentNode);
		}
		
	};
	
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
		hookDoubleClickAction();
		contributeToActionBars();
	}
	
	/**
	 * Setzt das Context-menu
	 */
	protected void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				RefreshableViewPartImpl.this.fillContextMenu(manager);
			}
		});
		
		menuMgr.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				// TODO implement Logik welche Action möglich sind
				// und "geenabled / disabled" werden
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
	protected void fillContextMenu(IMenuManager manager) {
		manager.add(this.showInfos);
		manager.add(new Separator());
		manager.add(this.addToHeld);
		manager.add(this.removeFromHeld);
		
		/* Um die Warnung
		 * Context menu missing standard group 'org.eclipse.ui.IWorkbenchActionConstants.MB_ADDITIONS'
		 * im Log zu verhindern */ 
		manager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	// Bei DoppelKlick Info-View öffnen
	protected void hookDoubleClickAction() {
		viewerTree.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				showInfos.run();
			}
		});
		
		viewerTable.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				showInfos.run();
			}
		});
	}

	/*
	 * Das "Pop-Up" Menu rechts Op
	 */
	private void fillLocalToolBar(IToolBarManager manager) {
		//manager.add(this.showInfos);
		//manager.add(new Separator());
		manager.add(this.swapTreeTable);
		//manager.add(this.addToHeld);
		//manager.add(this.removeFromHeld);
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
	
	/**
	 * Refresht den Tree und die Tabelle
	 */
	public void refresh(Object obj) {
		if (obj instanceof TableObject) {
			viewerTable.update(obj, null);
			viewerTree.refresh();
		} else {
			viewerTree.update(obj, null);
			viewerTable.refresh();
		}
	}
	
	public TableViewer getTableViewer() {
		return viewerTable;
	}
	
	public TreeViewer getTreeViewer() {
		return viewerTree;
	}
	
	/**
	 * Erstellt die Actions
	 */
	protected void makeActions() {

		// Ansichte wechseln Action
		swapTreeTable = new SwapTreeTableAction(this.parentComp);
	}
	
	protected ObjectCreator getObjectCreator() {
		return objCreator;
	}
	
	public abstract Regulator getRegulator();
	
	public abstract Class getViewedClass();

}
