package org.d3s.alricg.editor.views.charElemente;

import org.d3s.alricg.editor.common.CustomColumnLabelProvider;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter;
import org.d3s.alricg.editor.common.CustomActions.BuildNewCharElementAction;
import org.d3s.alricg.editor.common.CustomActions.DeleteCharElementAction;
import org.d3s.alricg.editor.common.CustomActions.EditCharElementAction;
import org.d3s.alricg.editor.common.CustomActions.FilterCurrentFileAction;
import org.d3s.alricg.editor.common.CustomActions.InfoCharElementAction;
import org.d3s.alricg.editor.common.CustomActions.SwapTreeTableAction;
import org.d3s.alricg.editor.common.ViewUtils.CharElementDragSourceListener;
import org.d3s.alricg.editor.common.ViewUtils.TableObject;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.ViewerSelectionListener;
import org.d3s.alricg.editor.editors.TalentEditor;
import org.d3s.alricg.editor.utils.EditorViewUtils;
import org.d3s.alricg.editor.utils.Regulatoren;
import org.d3s.alricg.editor.utils.Regulatoren.Regulator;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Talent;
import org.eclipse.jface.action.Action;
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

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class TalentView extends RefreshableViewPart {
	public static final String ID = "org.d3s.alricg.editor.views.TalentView";
	
	/**
	 * Erstellt eine TreeTable + ContextMenu und setzt sie in den View
	 */
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
		tc.getColumn().setText("Name");
		treeViewer.getTree().setSortColumn(tc.getColumn());
		tc.setLabelProvider(new CustomColumnLabelProvider.NameLabelProvider());
		tc.getColumn().setWidth(200);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.NameSorter(), treeViewer));

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 1);
		tc.getColumn().setText("Datei");
		tc.setLabelProvider(new CustomColumnLabelProvider.DateinameLabelProvider());
		tc.getColumn().setWidth(125);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.DateiSorter(),
								treeViewer));

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 2);
		tc.getColumn().setText("Art");
		tc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((TreeObject) element).getValue() instanceof Talent) {
					return ((Talent) ((TreeObject) element).getValue())
							.getArt().toString();
				}
				return "";
			}
		});
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.ArtSorter(), treeViewer));

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 3);
		tc.getColumn().setText("Probe");
		tc.setLabelProvider(new CustomColumnLabelProvider.EigenschaftLabelProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 4);
		tc.getColumn().setText("SKT");
		tc.setLabelProvider(new CustomColumnLabelProvider.SKTLabelProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.SktSorter(), treeViewer));

		// Inhalt und Sortierung setzen
		treeViewer.setContentProvider(
				new TreeViewContentProvider(EditorViewUtils.buildEditorViewTree(
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				getRegulator())));
		treeViewer.getTree().setSortDirection(SWT.DOWN);
		treeViewer.setSorter(new CustomColumnViewerSorter.NameSorter());
		treeViewer.setInput(getViewSite());

		return treeViewer;
	}

	/**
	 * Erstellt eine Table + ContextMenu und setzt sie in den View.
	 * @param parent
	 */
	protected TableViewer createTable(Composite parent) {


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
		tc.getColumn().setText("Name");
		tc.setLabelProvider(new CustomColumnLabelProvider.NameLabelProvider());
		tc.getColumn().setWidth(200);
		tc.getColumn()
				.addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.NameSorter(),
								tableViewer));

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 1);
		tc.getColumn().setText("Datei");
		tc
				.setLabelProvider(new CustomColumnLabelProvider.DateinameLabelProvider());
		tc.getColumn().setWidth(150);
		tc.getColumn().setMoveable(true);
		tc.getColumn()
				.addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.DateiSorter(),
								tableViewer));

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 2);
		tc.getColumn().setText("Art");
		tc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((TableObject) element).getValue() instanceof Talent) {
					return ((Talent) ((TableObject) element).getValue())
							.getArt().toString();
				}
				return "";
			}
		});
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.ArtSorter(), tableViewer));

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 3);
		tc.getColumn().setText("Sorte");
		tc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((TableObject) element).getValue() instanceof Talent) {
					return ((Talent) ((TableObject) element).getValue())
							.getSorte().toString();
				}
				return "";
			}
		});
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn()
				.addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.SorteSorter(),
								tableViewer));

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 4);
		tc.getColumn().setText("Probe");
		ColumnViewerToolTipSupport.enableFor(tableViewer, ToolTip.NO_RECREATE);
		tc
				.setLabelProvider(new CustomColumnLabelProvider.EigenschaftLabelProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 5);
		tc.getColumn().setText("SKT");
		tc.setLabelProvider(new CustomColumnLabelProvider.SKTLabelProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.SktSorter(), tableViewer));

		// Inhalt und Sortierung setzen
		tableViewer.setContentProvider(new TableViewContentProvider(
				EditorViewUtils.buildTableView(
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				getRegulator())));
		tableViewer.getTable().setSortDirection(SWT.UP);
		tableViewer.setSorter(new CustomColumnViewerSorter.NameSorter());
		tableViewer.setInput(getViewSite());

		return tableViewer;
	}

	protected void makeActions() {

		// Ansichte wechseln Action
		swapTreeTable = new SwapTreeTableAction(this.parentComp);

		// Tabelle nach File Filtern Action
		filterAktuellesFile = new FilterCurrentFileAction(viewerTable, viewerTree);

		// Information anzeigen Action
		showInfos = new InfoCharElementAction() {
			public void run() {
				showMessage("Table View", "Noch zu implementieren!");
			}
		};

		// Neues Element Action 
		buildNew = new BuildNewCharElementAction(
						Talent.class, this, this.parentComp, TalentEditor.ID) {

			@Override
			protected void runForTreeView(CharElement newCharElem, TreeObject treeObj) {
				if (treeObj.getValue() instanceof Talent) {
					runForTreeView(newCharElem, (TreeObject) treeObj.getParent());
				} else if (treeObj.getValue() instanceof Talent.Sorte) {
					((Talent) newCharElem).setSorte((Talent.Sorte) treeObj.getValue());
				} else if (treeObj.getValue() instanceof String) {
					newCharElem.setSammelbegriff(treeObj.getValue().toString());
					runForTreeView(newCharElem, (TreeObject) treeObj.getParent());
				}
			}};
		
		// Element Bearbeiten Action
		editSelected = new EditCharElementAction(this.parentComp, TalentEditor.ID, this);
		
		// Element löschen Action
		deleteSelected = new DeleteCharElementAction(this.parentComp, this);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.views.charElemente.RefreshableViewPart#rebuildTreeTable()
	 */
	@Override
	public Regulator getRegulator() {
		return Regulatoren.talentRegulator;
	}
	
	

}