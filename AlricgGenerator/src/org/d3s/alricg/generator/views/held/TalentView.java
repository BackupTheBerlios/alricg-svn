/*
 * Created 28.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.views.held;

import java.util.List;

import org.d3s.alricg.common.CommonUtils;
import org.d3s.alricg.common.logic.BaseProzessorObserver;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter;
import org.d3s.alricg.editor.common.Regulatoren;
import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter.CreatableViewerSorter;
import org.d3s.alricg.editor.common.Regulatoren.Regulator;
import org.d3s.alricg.editor.common.ViewUtils.ObjectCreator;
import org.d3s.alricg.editor.common.ViewUtils.TableObject;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.ViewerSelectionListener;
import org.d3s.alricg.generator.Activator;
import org.d3s.alricg.generator.common.CustomLabelProvider;
import org.d3s.alricg.generator.common.CustomViewerSorter;
import org.d3s.alricg.generator.common.CustomEditingSupport.LinkWertProzessorEditingSupport;
import org.d3s.alricg.generator.common.CustomEditingSupport.TalentSpezialisierungsEditor;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorTalent;
import org.d3s.alricg.generator.views.HeldRefreshableViewPart;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.rules.RegelConfig;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Vincent
 */
public class TalentView extends HeldRefreshableViewPart {
	public static final String ID = "org.d3s.alricg.generator.views.held.TalentView"; //$NON-NLS-1$
	public static final String GESAMT_KOSTEN = "Gesamt Kosten: ";
	public static final String AKTIVIERT = "Aktiviert: ";
	public static final String TAL_GP = " TalGP";
	
	public TalentView() {
		((BaseProzessorObserver) Activator.getCurrentCharakter()
				.getProzessor(Talent.class)).registerObserver(this);
	}
	
	private final ObjectCreator objCreator = new ObjectCreator() {
			@Override
			public TableObject createTableObject(Object element) {
				return new TableObject(element);
			}
	
			@Override
			public TreeObject createTreeObject(Object element, TreeObject parentNode) {
				return new TreeObject(element, parentNode);
			}
		};
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.views.HeldRefreshableViewPart#getStatusAnzeigeElemente()
	 */
	@Override
	protected String[] getStatusAnzeigeElemente() {
		String[] str = new String[2];
		str[0] = GESAMT_KOSTEN + "0" + TAL_GP;
		str[1] = AKTIVIERT + " 0 / " + RegelConfig.getInstance().getMaxTalentAktivierung();

		return str;
	}


	/**
	 * Erstellt eine TreeTable + ContextMenu und setzt sie in den View
	 */
	protected TreeViewer createTree(Composite parent) {
		// Init Viewer
		int idx = 0;
		final TreeViewer treeViewer = new TreeViewer(parent, SWT.FULL_SELECTION
				| SWT.H_SCROLL | SWT.V_SCROLL);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setHeaderVisible(true);
		ColumnViewerToolTipSupport.enableFor(treeViewer, ToolTip.NO_RECREATE);
		
		// Columns
		TreeViewerColumn tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Name");
		treeViewer.getTree().setSortColumn(tc.getColumn());
		tc.setLabelProvider(new CustomColumnLabelProvider.NameLabelProvider());
		tc.getColumn().setWidth(DEFAULT_FIRSTCOLUMN_WIDTH);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.NameSorter(), treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Stufe");
		tc.setLabelProvider(new CustomColumnLabelProvider.LinkWertProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener( new ViewerSelectionListener(
						new CustomViewerSorter.LinkWertSorter(), treeViewer));
		tc.setEditingSupport(
			new LinkWertProzessorEditingSupport(
					treeViewer, 
					treeViewer.getTree(), 
					false,
					Activator.getCurrentCharakter().getProzessor(Talent.class))
			);
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Modi");
		tc.setLabelProvider(new CustomLabelProvider.LinkWertModiProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener( new ViewerSelectionListener(
						new CustomViewerSorter.LinkWertModiSorter(), treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Probe");
		tc.setLabelProvider(new CustomColumnLabelProvider.Faehigkeit3EigenschaftProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("SKT");
		tc.setLabelProvider(new CustomColumnLabelProvider.SKTLabelProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.SktSorter(), treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Kosten");
		tc.setLabelProvider(new CustomLabelProvider.LinkKostenProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Spezi");
		tc.getColumn().setToolTipText("Spezialisierung");
		tc.setLabelProvider(new CustomColumnLabelProvider.LinkTextProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.setEditingSupport( 
				new TalentSpezialisierungsEditor(
						treeViewer,
						Activator.getCurrentCharakter().getProzessor(Talent.class))
				);
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Art");
		tc.setLabelProvider(new CustomLabelProvider.TalentArtProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener( new ViewerSelectionListener(
						new ArtSorter(), treeViewer));
		
		// Inhalt und Sortierung setzen
		TreeObject root = ViewUtils.buildTreeViewAlt(
				prozessor.getElementBox().getUnmodifiableList(), 
				getRegulator(),
				this.getObjectCreator());
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
		int idx = 0;
		final TableViewer tableViewer = new TableViewer(parent,
				SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		ColumnViewerToolTipSupport.enableFor(tableViewer, ToolTip.NO_RECREATE);
		
		// Columns setzen
		TableViewerColumn tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tableViewer.getTable().setSortColumn(tc.getColumn());
		tc.getColumn().setText("Name");
		tc.setLabelProvider(new CustomColumnLabelProvider.NameLabelProvider());
		tc.getColumn().setWidth(DEFAULT_FIRSTCOLUMN_WIDTH);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.NameSorter(),
								tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Sorte");
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if ( ((Link) ((TableObject) element).getValue()).getZiel() instanceof Talent) {
					return ((Talent) ((Link) ((TableObject) element).getValue()).getZiel())
							.getSorte().toString();
				}
				return ""; //$NON-NLS-1$
			}
		});
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Stufe");
		tc.setLabelProvider(new CustomColumnLabelProvider.LinkWertProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener( new ViewerSelectionListener(
						new CustomViewerSorter.LinkWertSorter(), tableViewer));
		tc.setEditingSupport(
			new LinkWertProzessorEditingSupport(
					tableViewer, 
					tableViewer.getTable(), 
					false,
					Activator.getCurrentCharakter().getProzessor(Talent.class))
			);
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Modi");
		tc.setLabelProvider(new CustomLabelProvider.LinkWertModiProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener( new ViewerSelectionListener(
						new CustomViewerSorter.LinkWertModiSorter(), tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Probe");
		tc.setLabelProvider(new CustomColumnLabelProvider.Faehigkeit3EigenschaftProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("SKT");
		tc.setLabelProvider(new CustomColumnLabelProvider.SKTLabelProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.SktSorter(), tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Kosten");
		tc.setLabelProvider(new CustomLabelProvider.LinkKostenProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Spezi");
		tc.getColumn().setToolTipText("Spezialisierung");
		tc.setLabelProvider(new CustomColumnLabelProvider.LinkTextProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.setEditingSupport( 
				new TalentSpezialisierungsEditor(
						tableViewer,
						Activator.getCurrentCharakter().getProzessor(Talent.class))
				);
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Art");
		tc.setLabelProvider(new CustomLabelProvider.TalentArtProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener( new ViewerSelectionListener(
						new ArtSorter(), tableViewer));
		
		// Inhalt und Sortierung setzen
		tableViewer.setContentProvider(new TableViewContentProvider());
		tableViewer.getTable().setSortDirection(SWT.UP);
		tableViewer.setSorter(new CustomColumnViewerSorter.NameSorter());
		tableViewer.setInput(
				ViewUtils.buildTableViewAlt(
					prozessor.getElementBox().getUnmodifiableList(), 
					getRegulator(),
					this.getObjectCreator())
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
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.ProzessorObserver#addElement(java.lang.Object)
	 */
	@Override
	public void addElement(Object obj) {

		ViewUtils.addElementToView(
				this, 
				((Link) obj), 
				objCreator);
		
		// Ansicht aktualisieren
		this.refresh();
		updateStatusAnzeigeElemente();
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.ProzessorObserver#removeElement(java.lang.Object)
	 */
	@Override
	public void removeElement(Object obj) {
		ViewUtils.removeElementFromView(
				this,
				obj);
		this.refresh();
		updateStatusAnzeigeElemente();
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.ProzessorObserver#setData(java.util.List)
	 */
	@Override
	public void setData(List list) {
		// TODO Auto-generated method stub
		this.refresh();
		updateStatusAnzeigeElemente();
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.ProzessorObserver#updateElement(java.lang.Object)
	 */
	@Override
	public void updateElement(Object obj) {
		this.refresh();
		updateStatusAnzeigeElemente();
	}
	
	/**
	 * Aktualisiert die Status-Anzeige des Views 
	 */
	@Override
	protected void updateStatusAnzeigeElemente() {
		String[] str = new String[2];
		
		str[0] = GESAMT_KOSTEN + CommonUtils.doubleToString(prozessor.getGesamtKosten()) + TAL_GP;
		str[1] = AKTIVIERT
					+ ((ExtendedProzessorTalent) prozessor.getExtendedInterface()).getAktivierteTalente().size()
					+ " / "
					+ ((ExtendedProzessorTalent) prozessor.getExtendedInterface()).getMaxTalentAktivierung();
		
		this.updateStatusAnzeigeElemente(str);
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		((BaseProzessorObserver) Activator.getCurrentCharakter()
				.getProzessor(Talent.class)).unregisterObserver(this);
		super.dispose();
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
