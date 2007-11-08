/*
 * Created 28.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.views.held;

import java.util.List;

import org.d3s.alricg.common.logic.BaseProzessorObserver;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter;
import org.d3s.alricg.editor.common.Regulatoren;
import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.common.CustomColumnEditors.LinkWertEditingSupport;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter.CreatableViewerSorter;
import org.d3s.alricg.editor.common.Regulatoren.Regulator;
import org.d3s.alricg.editor.common.ViewUtils.CharElementDragSourceListener;
import org.d3s.alricg.editor.common.ViewUtils.ObjectCreator;
import org.d3s.alricg.editor.common.ViewUtils.TableObject;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.ViewerSelectionListener;
import org.d3s.alricg.generator.Activator;
import org.d3s.alricg.generator.common.CustomLabelProvider;
import org.d3s.alricg.generator.common.CustomViewerSorter;
import org.d3s.alricg.generator.views.HeldRefreshableViewPart;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.charElemente.links.Option;
import org.d3s.alricg.store.charElemente.links.OptionListe;
import org.d3s.alricg.store.charElemente.links.OptionVerteilung;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Vincent
 *
 */
public class TalentView extends HeldRefreshableViewPart {
	public static final String ID = "org.d3s.alricg.generator.views.held.TalentView"; //$NON-NLS-1$
	
	public TalentView() {
		((BaseProzessorObserver) Activator.getCurrentCharakter()
				.getProzessor(Talent.class)).registerObserver(this);
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
		
		// Drag and Drop
		treeViewer.addDragSupport(
				DND.DROP_COPY | DND.DROP_MOVE, 
				new Transfer[] { LocalSelectionTransfer.getTransfer() }, 
				new CharElementDragSourceListener(treeViewer));
		
		
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
		tc.setEditingSupport(new LinkWertEditingSupport(treeViewer, treeViewer.getTree(), -10, 20, false) {

			@Override
			protected boolean canEdit(Object element) {
				if (!super.canEdit(element)) return false;
				
				int min = Activator.getCurrentCharakter().getProzessor(Talent.class).getMinWert((Link) ((TreeObject) element).getValue());
				int max = Activator.getCurrentCharakter().getProzessor(Talent.class).getMaxWert((Link) ((TreeObject) element).getValue());
				
				this.changeMinMax(min, max);
				
				return true;
			}

			/* (non-Javadoc)
			 * @see org.d3s.alricg.editor.common.CustomColumnEditors.LinkWertEditingSupport#setValue(java.lang.Object, java.lang.Object)
			 */
			@Override
			protected void setValue(Object element, Object value) {
				Activator.getCurrentCharakter().getProzessor(Talent.class).updateWert((Link) ((TreeObject) element).getValue(), this.getWertForSet(value));
				refresh(element);
			}
		});
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Modi");
		tc.setLabelProvider(new CustomLabelProvider.LinkWertModiProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener( new ViewerSelectionListener(
						new CustomViewerSorter.LinkWertModiSorter(), treeViewer));
		
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
		/*
		tc.getColumn().addSelectionListener( new ViewerSelectionListener(
						new ArtSorter(), treeViewer));*/
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Art");
		tc.setLabelProvider(new CustomLabelProvider.TalentArtProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener( new ViewerSelectionListener(
						new ArtSorter(), treeViewer));
		
		/*
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new ArtSorter(), treeViewer));

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
		tc.getColumn().setText("Voraussetzung");
		tc.setLabelProvider(new CustomColumnLabelProvider.CharElementVoraussetzungProvider());
		tc.getColumn().setWidth(150);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.CharElementVoraussetzungSorter(),
						treeViewer));
		*/
		
		// Inhalt und Sortierung setzen
		TreeObject root = ViewUtils.buildTreeViewAlt(
				Activator.getCurrentCharakter().getElementListe(Talent.class), 
				getRegulator(),
				this.getObjectCreator());
		treeViewer.setContentProvider(new TreeViewContentProvider(root));
		treeViewer.getTree().setSortDirection(SWT.DOWN);
		treeViewer.setSorter(new CustomColumnViewerSorter.NameSorter());
		treeViewer.setInput(root);

		// Unterstützung für DROP
		int ops = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transfers = new Transfer[] { LocalSelectionTransfer
				.getTransfer() };
		treeViewer.addDropSupport(ops, transfers, new TestDrop(treeViewer));
		
		return treeViewer;
	}
	
	public static class TestDrop extends ViewerDropAdapter {
		
		protected TestDrop(Viewer viewer) {
			super(viewer);
		}

		@Override
		public void drop(DropTargetEvent event) {
			// Setzt sourceObj/ targetObj. Das eigentliche Drop wird dann
			// von dem der "mouseMove" Methode ausgeführt. Diese kann
			// festellen auf welcher Spalte das Drop ausgeführt wurde:
			// Wichtig für Zweitziel!
			
			//sourceObj = (TreeOrTableObject) ((StructuredSelection) event.data).getFirstElement();
			//targetObj = (TreeOrTableObject) getCurrentTarget();
			TreeOrTableObject sourceObj = (TreeOrTableObject) ((StructuredSelection) event.data).getFirstElement();
			
			if ( Activator.getCurrentCharakter().getProzessor(Talent.class).canAddElement((CharElement) sourceObj.getValue()) ) {
				Activator.getCurrentCharakter().getProzessor(Talent.class)
				.addNewElement( (CharElement) sourceObj.getValue());
			}

		}
		
		@Override
		public boolean performDrop(Object data) {
			// wird durch das selbst implementierte "drop" nicht mehr aufgerufen, muss aber 
			// implementiert werden
			return true;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerDropAdapter#validateDrop(java.lang.Object, int, org.eclipse.swt.dnd.TransferData)
		 */
		@Override
		public boolean validateDrop(Object target, int operation, TransferData transferType) {
			//System.out.println( getSelectedObject() );
			return true;
		}
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
		
		// Drag and Drop
		tableViewer.addDragSupport(
				DND.DROP_COPY | DND.DROP_MOVE, 
				new Transfer[] { LocalSelectionTransfer.getTransfer() }, 
				new CharElementDragSourceListener(tableViewer));
		
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
		/*
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Art");
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

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Sorte");
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
		tc.getColumn().setText("Voraussetzung");
		tc.setLabelProvider(new CustomColumnLabelProvider.CharElementVoraussetzungProvider());
		tc.getColumn().setWidth(150);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.CharElementVoraussetzungSorter(),
						tableViewer));
		*/
		
		// Inhalt und Sortierung setzen
		tableViewer.setContentProvider(new TableViewContentProvider());
		tableViewer.getTable().setSortDirection(SWT.UP);
		tableViewer.setSorter(new CustomColumnViewerSorter.NameSorter());
		tableViewer.setInput(
				ViewUtils.buildTableViewAlt(
					Activator.getCurrentCharakter().getElementListe(Talent.class), 
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
		// TEST nur ein erster Entwurf
		final ObjectCreator objCreator = new ObjectCreator() {
			
			@Override
			public TableObject createTableObject(Object element) {
				return new TableObject(element);
			}

			@Override
			public TreeObject createTreeObject(Object element, TreeObject parentNode) {
				return new TreeObject(element, parentNode);
			}
		};
		
		ViewUtils.addElementToView(
				this, 
				((Link) obj), 
				objCreator);
		
		// 3. Ansicht aktualisieren
		this.refresh();
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.ProzessorObserver#removeElement(java.lang.Object)
	 */
	@Override
	public void removeElement(Object obj) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.ProzessorObserver#setData(java.util.List)
	 */
	@Override
	public void setData(List list) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.ProzessorObserver#updateElement(java.lang.Object)
	 */
	@Override
	public void updateElement(Object obj) {
		// TODO Auto-generated method stub
		
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
