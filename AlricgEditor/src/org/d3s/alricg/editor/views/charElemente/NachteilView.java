/*
 * Created 20.08.2007
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
import org.d3s.alricg.editor.common.CustomColumnLabelProvider.FertigkeitArtProvider;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider.FertigkeitFamilieProvider;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider.FertigkeitGpProvider;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider.FertigkeitTextNoetigProvider;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider.FertigkeitZweitZielNoetigProvider;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider.VorNachteilGpProvider;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter.CreatableViewerSorter;
import org.d3s.alricg.editor.common.Regulatoren.Regulator;
import org.d3s.alricg.editor.common.ViewUtils.CharElementDragSourceListener;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.ViewerSelectionListener;
import org.d3s.alricg.editor.utils.EditorViewUtils;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Fertigkeit;
import org.d3s.alricg.store.charElemente.Nachteil;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Vincent
 */
public class NachteilView extends RefreshableViewPartImpl {
	public static final String ID = "org.d3s.alricg.editor.views.NachteilView"; //$NON-NLS-1$

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.views.charElemente.RefreshableViewPart#createTable(org.eclipse.swt.widgets.Composite)
	 */
	@Override
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
		tc.getColumn().setWidth(DEFAULT_FIRSTCOLUMN_WIDTH);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.NameSorter(),
								tableViewer));

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 1);
		tc.getColumn().setText("Datei");
		tc.setLabelProvider(new CustomColumnLabelProvider.DateinameLabelProvider());
		tc.getColumn().setWidth(125);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.DateiSorter(),
								tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 2);
		tc.getColumn().setText("Art");
		tc.setLabelProvider(new FertigkeitArtProvider());
		tc.getColumn().setWidth(125);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.FertigkeitArtSorter(), tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 3);
		tc.getColumn().setText("Familie");
		tc.getColumn().setToolTipText("Kennzeichnet zusammengehörige Elemente für aufaddition bei Mehrfacherhalt durch Herkunft");
		tc.getColumn().setWidth(75);
		tc.setLabelProvider(new FertigkeitFamilieProvider());
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.AdditionsFamilieSorter(), tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 4);
		tc.getColumn().setText("GP"); 
		tc.getColumn().setToolTipText("Kosten Generierungpunkte");
		tc.getColumn().setWidth(75);
		tc.setLabelProvider(new VorNachteilGpProvider());
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.VorNachteilGpSorter(), tableViewer));

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 5);
		tc.getColumn().setText("SE");
		tc.getColumn().setToolTipText("Schlechte Eigenschaft");
		tc.getColumn().setWidth(27);
		tc.setLabelProvider(new SchlechteEigenschaftProvider());
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new SchlechteEigenschaftSorter(), tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 6);
		tc.getColumn().setText("T");
		tc.getColumn().setToolTipText("Benötigt die Angabe eines Textes?");
		tc.getColumn().setWidth(24);
		tc.setLabelProvider(new FertigkeitTextNoetigProvider());
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.FertigkeitBenoetigtTextSorter(), tableViewer));

		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 7);
		tc.getColumn().setText("Z");
		tc.getColumn().setToolTipText("Benötigt die Angabe eines ZweitZiels?");
		tc.getColumn().setWidth(24);
		tc.setLabelProvider(new FertigkeitZweitZielNoetigProvider());
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.FertigkeitBenoetigtZweitZielSorter(), tableViewer));
/*
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 8);
		tc.getColumn().setText("Verändert Kosten");
		tc.setLabelProvider(new CustomColumnLabelProvider.VorNachteilVerbilligtProvider());
		tc.getColumn().setWidth(150);
		tc.getColumn().setMoveable(true);
*/
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 8);
		tc.getColumn().setText("Voraussetzung");
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
		TreeViewer treeViewer = new TreeViewer(parent, SWT.FULL_SELECTION
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
		tc.getColumn().setWidth(DEFAULT_FIRSTCOLUMN_WIDTH);
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
		tc.getColumn().setText("Familie");
		tc.getColumn().setToolTipText("Kennzeichnet zusammengehörige Elemente für aufaddition bei Mehrfacherhalt durch Herkunft");
		tc.getColumn().setWidth(75);
		tc.setLabelProvider(new FertigkeitFamilieProvider());
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.AdditionsFamilieSorter(), treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 3);
		tc.getColumn().setText("GP"); 
		tc.getColumn().setToolTipText("Kosten Generierungpunkte");
		tc.getColumn().setWidth(75);
		tc.setLabelProvider(new VorNachteilGpProvider());
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.VorNachteilGpSorter(), treeViewer));

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 4);
		tc.getColumn().setText("SE");
		tc.getColumn().setToolTipText("Schlechte Eigenschaft");
		tc.getColumn().setWidth(27);
		tc.setLabelProvider(new SchlechteEigenschaftProvider());
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new SchlechteEigenschaftSorter(), treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 5);
		tc.getColumn().setText("T");
		tc.getColumn().setToolTipText("Benötigt die Angabe eines Textes?");
		tc.getColumn().setWidth(24);
		tc.setLabelProvider(new FertigkeitTextNoetigProvider());
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.FertigkeitBenoetigtTextSorter(), treeViewer));

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 6);
		tc.getColumn().setText("Z");
		tc.getColumn().setToolTipText("Benötigt die Angabe eines ZweitZiels?");
		tc.getColumn().setWidth(24);
		tc.setLabelProvider(new FertigkeitZweitZielNoetigProvider());
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.FertigkeitBenoetigtZweitZielSorter(), treeViewer));

/*
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 7);
		tc.getColumn().setText("Verändert Kosten");
		tc.setLabelProvider(new CustomColumnLabelProvider.VorNachteilVerbilligtProvider());
		tc.getColumn().setWidth(150);
		tc.getColumn().setMoveable(true);
*/
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 7);
		tc.getColumn().setText("Voraussetzung");
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
		return Regulatoren.NachteilRegulator;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.views.charElemente.RefreshableViewPart#getViewedClass()
	 */
	@Override
	public Class getViewedClass() {
		return Nachteil.class;
	}

	/**
	 * Stellt das Feld "Schlechte Eigenschaft" dar
	 * @author Vincent
	 */
	public static class SchlechteEigenschaftProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			return ""; //$NON-NLS-1$
		}
		@Override
		public Image getImage(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return null;
			
			if (charElem instanceof Nachteil) {
				if ( ((Nachteil) charElem).isSchlechteEigen() )  {
					return CustomColumnLabelProvider.acceptImage;
				} else {
					return CustomColumnLabelProvider.cancelImage;
				}
			}
			return super.getImage(element);
		}
	}
	
	public static class SchlechteEigenschaftSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return ((Nachteil) getCharElement(obj)).isSchlechteEigen();
		}	
	}
	
}
