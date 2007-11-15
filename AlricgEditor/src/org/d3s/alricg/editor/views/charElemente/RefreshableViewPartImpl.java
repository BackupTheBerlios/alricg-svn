/*
 * Created 01.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.views.charElemente;

import org.d3s.alricg.editor.common.RefreshableViewPart;
import org.d3s.alricg.editor.common.Regulatoren.Regulator;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.utils.CustomActions.BuildNewCharElementAction;
import org.d3s.alricg.editor.utils.CustomActions.DeleteCharElementAction;
import org.d3s.alricg.editor.utils.CustomActions.EditCharElementAction;
import org.d3s.alricg.editor.utils.CustomActions.FilterCurrentFileAction;
import org.d3s.alricg.editor.utils.CustomActions.InfoCharElementAction;
import org.d3s.alricg.editor.utils.CustomActions.SwapTreeTableAction;
import org.d3s.alricg.editor.views.FileView;
import org.d3s.alricg.store.charElemente.CharElement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

/**
 * @author Vincent
 *
 */
public abstract class RefreshableViewPartImpl extends ViewPart implements RefreshableViewPart {
	protected TableViewer viewerTable;
	protected TreeViewer viewerTree;
	protected Composite parentComp;

	protected Action swapTreeTable;
	protected Action filterAktuellesFile;
	protected Action showInfos;
	protected Action buildNew;
	protected Action deleteSelected;
	protected Action editSelected;

	protected static final int DEFAULT_FIRSTCOLUMN_WIDTH = 200;

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

		getSite().getPage().addSelectionListener(
				FileView.ID,
				(ISelectionListener) filterAktuellesFile);
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
				boolean isEnabled = true;
				final TreeOrTableObject treeTableObj = getSelectedElement();
				
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
	protected void fillContextMenu(IMenuManager manager) {
		manager.add(this.showInfos);
		manager.add(new Separator());
		manager.add(this.buildNew);
		manager.add(this.editSelected);
		manager.add(this.deleteSelected);
		/* Um die Warnung
		 * Context menu missing standard group 'org.eclipse.ui.IWorkbenchActionConstants.MB_ADDITIONS'
		 * im Log zu verhindern */ 
		manager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	// Bei DoppelKlick Editor öffnen
	protected void hookDoubleClickAction() {
		viewerTree.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				editSelected.run();
			}
		});
		
		viewerTable.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				editSelected.run();
			}
		});
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
	
	/**
	 * Erstellt die Actions
	 */
	protected void makeActions() {

		// Ansichte wechseln Action
		swapTreeTable = new SwapTreeTableAction(this.parentComp);

		// Tabelle nach File Filtern Action
		filterAktuellesFile = new FilterCurrentFileAction(getViewedClass());

		// Information anzeigen Action
		showInfos = new InfoCharElementAction() {
			public void run() {
				showMessage("Table View", "Noch zu implementieren!"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		};

		// Neues Element Action 
		buildNew = new BuildNewCharElementAction(this, getViewedClass(), getRegulator());
		
		// Element Bearbeiten Action
		editSelected = new EditCharElementAction(this);
		
		// Element löschen Action
		deleteSelected = new DeleteCharElementAction(this, getViewedClass());
	}
	
	public abstract Regulator getRegulator();
	
	public abstract Class getViewedClass();
	
	/**
	 * @return Liefert das gerade selektierte Object vom Tree oder Table
	 */
	@Override
	public TreeOrTableObject getSelectedElement() {
		final Control topControl = ((StackLayout) parentComp.getLayout()).topControl;
		TreeOrTableObject value = null;
		
		if (topControl instanceof Tree && ((Tree) topControl).getSelection().length > 0) {
			value = (TreeOrTableObject) ((Tree) topControl).getSelection()[0].getData();

		} else if (topControl instanceof Table && ((Table) topControl).getSelection().length > 0 ) {
			value = (TreeOrTableObject) ((Table) topControl).getSelection()[0].getData();
		}
		
		return value;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.common.RefreshableViewPart#getTopControl()
	 */
	@Override
	public Control getTopControl() {
		return ((StackLayout) parentComp.getLayout()).topControl;
	}
	
	
}
