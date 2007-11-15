/*
 * Created 27.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.views;

import org.d3s.alricg.common.logic.Prozessor;
import org.d3s.alricg.editor.common.ViewUtils.CharElementDragSourceListener;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.generator.common.CustomActions.AddToView;
import org.d3s.alricg.generator.common.CustomActions.InfoCharElementAction;
import org.d3s.alricg.generator.common.Utils.DropRemoveFromHeld;
import org.d3s.alricg.store.charElemente.CharElement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;


/**
 * Basisklasse für alle Views, welche alle allgemein Verfügbaren Elemente darstellen.
 * @author Vincent
 */
public abstract class GeneralRefreshableViewPart extends RefreshableViewPartImpl {
	protected Prozessor prozessor; 
	
	//protected Action removeFromHeld;
	protected Action addToHeld;
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		parent.setLayout(new FillLayout());

		final Composite mainComp = new Composite(parent, SWT.NONE);
		mainComp.setLayout(new GridLayout(1, false));
		
		final Composite viewerComp = new Composite(mainComp, SWT.NONE);
		viewerComp.setLayout(new StackLayout());
		viewerComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		this.parentComp = viewerComp;
		viewerTable = createTable(viewerComp);
		viewerTree = createTree(viewerComp);
		
		// Unterstützung für DROP
		int ops = DND.DROP_COPY | DND.DROP_MOVE;
		final DropRemoveFromHeld dropAdd = new DropRemoveFromHeld(viewerTree, getViewedClass());
		final Transfer[] transfers = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		viewerTree.addDropSupport(ops, transfers, dropAdd);
		viewerTable.addDropSupport(ops, transfers, dropAdd);
		
		// Unterstützung für DRAG
		viewerTree.addDragSupport(
				DND.DROP_COPY | DND.DROP_MOVE, 
				new Transfer[] { LocalSelectionTransfer.getTransfer() }, 
				new CharElementDragSourceListener(viewerTree));
		viewerTable.addDragSupport(
				DND.DROP_COPY | DND.DROP_MOVE, 
				new Transfer[] { LocalSelectionTransfer.getTransfer() }, 
				new CharElementDragSourceListener(viewerTable));
		
		
		((StackLayout) parentComp.getLayout()).topControl = viewerTree.getTree();
		parentComp.layout();

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.views.RefreshableViewPartImpl#makeActions()
	 */
	@Override
	protected void makeActions() {
		super.makeActions();
		
		// Information anzeigen Action
		showInfos = new InfoCharElementAction() {
			public void run() {
				showMessage("Table View", "Noch zu implementieren!"); //$NON-NLS-1$ //$NON-NLS-2$
			}};
		showInfos.setText("Infos");
		
		// Element löschen Action
		//removeFromHeld = new DeleteFromView(this, prozessor);
		//removeFromHeld.setText("Von Charakter entfernen");
		addToHeld = new AddToView(this, prozessor);
		addToHeld.setText("Zu Charakter hinzufügen");
	}
	
	// Das Context Menu beim Rechts-klick
	protected void fillContextMenu(IMenuManager manager) {
		manager.add(this.showInfos);
		manager.add(new Separator());
		//manager.add(this.removeFromHeld);
		manager.add(this.addToHeld);
		
		/* Um die Warnung
		 * Context menu missing standard group 'org.eclipse.ui.IWorkbenchActionConstants.MB_ADDITIONS'
		 * im Log zu verhindern */ 
		manager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	@Override
	protected void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				GeneralRefreshableViewPart.this.fillContextMenu(manager);
			}
		});
		
		menuMgr.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				boolean isEnabled = true;
				final TreeOrTableObject treeTableObj = getSelectedElement();
				
				if (!(treeTableObj.getValue() instanceof CharElement)) {
					showInfos.setEnabled(false);
					//removeFromHeld.setEnabled(false);
					addToHeld.setEnabled(false);
					return;
				} 
				showInfos.setEnabled(true);
				addToHeld.setEnabled(
						prozessor.canAddElement((CharElement) treeTableObj.getValue())
				);
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
