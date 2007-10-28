/*
 * Created 21.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.views.charElemente;

import org.d3s.alricg.editor.common.CustomColumnLabelProvider;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter;
import org.d3s.alricg.editor.common.Regulatoren;
import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter.CreatableViewerSorter;
import org.d3s.alricg.editor.common.Regulatoren.CharElementWapper;
import org.d3s.alricg.editor.common.Regulatoren.Regulator;
import org.d3s.alricg.editor.common.ViewUtils.CharElementDragSourceListener;
import org.d3s.alricg.editor.common.ViewUtils.TableObject;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.ViewerSelectionListener;
import org.d3s.alricg.editor.utils.EditorViewUtils;
import org.d3s.alricg.editor.utils.CustomActions.BuildNewCharElementAction;
import org.d3s.alricg.editor.views.ViewMessages;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Schrift;
import org.d3s.alricg.store.charElemente.SchriftSprache;
import org.d3s.alricg.store.charElemente.Sprache;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

/**
 * @author Vincent
 */
public class SpracheView extends RefreshableViewPart {
	public static final String ID = "org.d3s.alricg.editor.views.SpracheSchriftView";

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.views.charElemente.RefreshableViewPart#createTable(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected TableViewer createTable(Composite parent) {
		// init Table
		final TableViewer tableViewer = new TableViewer(parent,
				SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		ColumnViewerToolTipSupport.enableFor(tableViewer, ToolTip.NO_RECREATE);
		
		// Drag and Drop
		tableViewer.addDragSupport(
				DND.DROP_COPY | DND.DROP_MOVE, 
				new Transfer[] { LocalSelectionTransfer.getTransfer() }, 
				new CharElementDragSourceListener(tableViewer));
		
		// Columns setzen
		TableViewerColumn tc = new TableViewerColumn(tableViewer, SWT.LEFT, 0);
		tableViewer.getTable().setSortColumn(tc.getColumn());
		tc.getColumn().setText(ViewMessages.TalentView_Name);
		tc.setLabelProvider(new CustomColumnLabelProvider.NameLabelProvider());
		tc.getColumn().setWidth(DEFAULT_FIRSTCOLUMN_WIDTH);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.NameSorter(),
								tableViewer));

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 1);
		tc.getColumn().setText(ViewMessages.TalentView_Datei);
		tc.setLabelProvider(new CustomColumnLabelProvider.DateinameLabelProvider());
		tc.getColumn().setWidth(150);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.DateiSorter(),
								tableViewer));


		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 2);
		tc.getColumn().setText("Komplexität");
		tc.setLabelProvider(new CustomColumnLabelProvider.SchriftSpracheKomplexitaetProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.SchriftSpracheKomplexitaetSorter(),
								tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 3);
		tc.getColumn().setText(ViewMessages.TalentView_SKT);
		tc.setLabelProvider(new CustomColumnLabelProvider.SKTLabelProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.SktSorter(), tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 4);
		tc.getColumn().setText("Schriften");
		tc.setLabelProvider(new SpracheSchriftenProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new SpracheSchriftenSorter(), tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 5);
		tc.getColumn().setText(ViewMessages.TalentView_Voraussetzung);
		tc.setLabelProvider(new CustomColumnLabelProvider.CharElementVoraussetzungProvider());
		tc.getColumn().setWidth(150);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.CharElementVoraussetzungSorter(),
						tableViewer));
		
		// Inhalt und Sortierung setzen
		tableViewer.setContentProvider(new TableViewContentProvider());
		tableViewer.getTable().setSortDirection(SWT.UP);
		tableViewer.setSorter(new CustomColumnViewerSorter.NameSorter());
		tableViewer.setInput(
				EditorViewUtils.buildEditorTableView(
					StoreDataAccessor.getInstance().getXmlAccessors(), 
					getRegulator())
				);

		return tableViewer;

	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.views.charElemente.RefreshableViewPart#createTree(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected TreeViewer createTree(Composite parent) {
		// Init Viewer
		final TreeViewer treeViewer = new TreeViewer(parent, SWT.FULL_SELECTION
				| SWT.H_SCROLL | SWT.V_SCROLL);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setHeaderVisible(true);
		ColumnViewerToolTipSupport.enableFor(treeViewer, ToolTip.NO_RECREATE);
		
		// Drag and Drop
		treeViewer.addDragSupport(
				DND.DROP_COPY | DND.DROP_MOVE, 
				new Transfer[] { LocalSelectionTransfer.getTransfer() }, 
				new CharElementDragSourceListener(treeViewer));
		
		
		// Columns
		TreeViewerColumn tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 0);
		tc.getColumn().setText(ViewMessages.TalentView_Name);
		treeViewer.getTree().setSortColumn(tc.getColumn());
		tc.setLabelProvider(new CustomColumnLabelProvider.NameLabelProvider());
		tc.getColumn().setWidth(DEFAULT_FIRSTCOLUMN_WIDTH);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.NameSorter(), treeViewer));

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 1);
		tc.getColumn().setText(ViewMessages.TalentView_Datei);
		tc.setLabelProvider(new CustomColumnLabelProvider.DateinameLabelProvider());
		tc.getColumn().setWidth(125);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.DateiSorter(),
								treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 2);
		tc.getColumn().setText("Komplexität");
		tc.setLabelProvider(new CustomColumnLabelProvider.SchriftSpracheKomplexitaetProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.SchriftSpracheKomplexitaetSorter(),
								treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 3);
		tc.getColumn().setText(ViewMessages.TalentView_SKT);
		tc.setLabelProvider(new CustomColumnLabelProvider.SKTLabelProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.SktSorter(), treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 4);
		tc.getColumn().setText("Schriften");
		tc.setLabelProvider(new SpracheSchriftenProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new SpracheSchriftenSorter(), treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 5);
		tc.getColumn().setText(ViewMessages.TalentView_Voraussetzung);
		tc.setLabelProvider(new CustomColumnLabelProvider.CharElementVoraussetzungProvider());
		tc.getColumn().setWidth(150);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.CharElementVoraussetzungSorter(),
						treeViewer));
		
		// Inhalt und Sortierung setzen
		TreeObject root = EditorViewUtils.buildEditorTreeView(
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				getRegulator());
		treeViewer.setContentProvider(new TreeViewContentProvider(root));
		treeViewer.getTree().setSortDirection(SWT.DOWN);
		treeViewer.setSorter(new CustomColumnViewerSorter.NameSorter());
		treeViewer.setInput(root);

		return treeViewer;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.views.charElemente.RefreshableViewPart#getRegulator()
	 */
	@Override
	public Regulator getRegulator() {
		return Regulatoren.SpracheRegulator;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.views.charElemente.RefreshableViewPart#getViewedClass()
	 */
	@Override
	public Class getViewedClass() {
		return SchriftSprache.class;
	}

	/**
	 * Setzt das Context-menu
	 * Überschrieben, da "BuildNew" nur angezeigt werden darf, wenn ein Element selektiert ist
	 */
	@Override
	protected void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SpracheView.this.fillContextMenu(manager);
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
				
				if (treeTableObj == null) {
					buildNew.setEnabled(false);
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
	
	/**
	 * Überschrieben, da "BuildNew" den Typ nun dynamisch errechnet
	 */
	@Override
	protected void makeActions() {
		super.makeActions();
		
		buildNew = new BuildNewCharElementAction(this.parentComp, getViewedClass(), getRegulator()) {

			/* (non-Javadoc)
			 * @see org.d3s.alricg.editor.common.CustomActions.BuildNewCharElementAction#run()
			 */
			@Override
			public void run() {
				TreeOrTableObject treeObj = ViewUtils.getSelectedObject(parentComp);
				if (treeObj == null) {
					return;
				}
				
				// Bei einer Tabelle kann der Typ direkt gelesen werden
				if (treeObj instanceof TableObject) {
					this.charElementClazz = treeObj.getValue().getClass();
				}
				
				while (treeObj != null && !treeObj.getValue().getClass().equals(
									getRegulator().getFirstCategoryClass()) ) 
				{
					treeObj = ((TreeObject) treeObj).getParent();
				}
				charElementClazz = ((CharElementWapper) treeObj.getValue()).getWappedClass();
				
				super.run();
			}
			
		};

	}
	// Neues Element Action 

	
	/**
	 * Erstellt aus der zu der Sprache zugehörigen Schriften einen Text
	 * @author Vincent
	 */
	private static class SpracheSchriftenProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return ""; //$NON-NLS-1$
			
			if ( charElem instanceof Schrift) {
				return "-";
			} else if ( ((Sprache)charElem).getZugehoerigeSchrift() == null) {
				return "keine";
			}
			
			final StringBuilder strB = new StringBuilder();
			for (int i = 0; i < ((Sprache)charElem).getZugehoerigeSchrift().length; i++) {
				strB.append(((Sprache)charElem).getZugehoerigeSchrift()[i].getName());
				if (i+1 < ((Sprache)charElem).getZugehoerigeSchrift().length) {
					strB.append(", ");
				}
			}
			return 	strB.toString();
		}
	}
	
	private static class SpracheSchriftenSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			final CharElement elem = ViewUtils.getCharElement(obj);
			
			if ( elem  instanceof Schrift) {
				return "-";
			} else if ( ((Sprache) elem).getZugehoerigeSchrift() == null) {
				return "keine";
			} 
			
			final StringBuilder strB = new StringBuilder();
			for (int i = 0; i < ((Sprache)elem).getZugehoerigeSchrift().length; i++) {
				strB.append(((Sprache)elem).getZugehoerigeSchrift()[i].getName());
				if (i+1 < ((Sprache)elem).getZugehoerigeSchrift().length) {
					strB.append(", ");
				}
			}
			return 	strB.toString();
		}
	}
}
