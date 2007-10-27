package org.d3s.alricg.editor.views.charElemente;

import org.d3s.alricg.editor.common.CustomColumnLabelProvider;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter.CreatableViewerSorter;
import org.d3s.alricg.editor.common.ViewUtils.CharElementDragSourceListener;
import org.d3s.alricg.editor.common.ViewUtils.TableObject;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.ViewerSelectionListener;
import org.d3s.alricg.editor.utils.EditorViewUtils;
import org.d3s.alricg.editor.utils.Regulatoren;
import org.d3s.alricg.editor.utils.Regulatoren.Regulator;
import org.d3s.alricg.editor.views.ViewMessages;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.Talent;
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
 * Zeig eine ansicht der verfügbaren Talente.
 * @author Vincent 
 */
public class TalentView extends RefreshableViewPart {
	public static final String ID = "org.d3s.alricg.editor.views.TalentView"; //$NON-NLS-1$
	
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
		tc.getColumn().setText(ViewMessages.TalentView_Art);
		tc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((TreeObject) element).getValue() instanceof Talent) {
					return ((Talent) ((TreeObject) element).getValue())
							.getArt().toString();
				}
				return ""; //$NON-NLS-1$
			}
		});
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new ArtSorter(), treeViewer));

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 3);
		tc.getColumn().setText(ViewMessages.TalentView_Probe);
		tc.setLabelProvider(new CustomColumnLabelProvider.Faehigkeit3EigenschaftProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 4);
		tc.getColumn().setText(ViewMessages.TalentView_SKT);
		tc.setLabelProvider(new CustomColumnLabelProvider.SKTLabelProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.SktSorter(), treeViewer));
		
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
		TreeObject root = EditorViewUtils.buildEditorViewTree(
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				getRegulator());
		treeViewer.setContentProvider(new TreeViewContentProvider(root));
		treeViewer.getTree().setSortDirection(SWT.DOWN);
		treeViewer.setSorter(new CustomColumnViewerSorter.NameSorter());
		treeViewer.setInput(root);

		return treeViewer;
	}

	/**
	 * Erstellt eine Table + ContextMenu und setzt sie in den View.
	 * @param parent
	 */
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
		tc.getColumn().setText(ViewMessages.TalentView_Art);
		tc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((TableObject) element).getValue() instanceof Talent) {
					return ((Talent) ((TableObject) element).getValue())
							.getArt().toString();
				}
				return ""; //$NON-NLS-1$
			}
		});
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new ArtSorter(), tableViewer));

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 3);
		tc.getColumn().setText(ViewMessages.TalentView_Sorte);
		tc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((TableObject) element).getValue() instanceof Talent) {
					return ((Talent) ((TableObject) element).getValue())
							.getSorte().toString();
				}
				return ""; //$NON-NLS-1$
			}
		});
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new SorteSorter(), tableViewer));

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 4);
		tc.getColumn().setText(ViewMessages.TalentView_Probe);
		tc.setLabelProvider(new CustomColumnLabelProvider.Faehigkeit3EigenschaftProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 5);
		tc.getColumn().setText(ViewMessages.TalentView_SKT);
		tc.setLabelProvider(new CustomColumnLabelProvider.SKTLabelProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.SktSorter(), tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 6);
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
				EditorViewUtils.buildTableView(
					StoreDataAccessor.getInstance().getXmlAccessors(), 
					getRegulator())
				);

		return tableViewer;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.views.charElemente.RefreshableViewPart#rebuildTreeTable()
	 */
	@Override
	public Regulator getRegulator() {
		return Regulatoren.TalentRegulator;
	}

	@Override
	public Class getViewedClass() {
		return Talent.class;
	}
	
	
	public static class ArtSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return ((Talent) getCharElement(obj)).getArt().toString();
		}
	}
	
	public static class SorteSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return ((Talent) getCharElement(obj)).getSorte().toString();
		}
	}
	

}