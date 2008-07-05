/*
 * Created 19.01.2008
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.views.general;

import java.util.List;

import org.d3s.alricg.editor.common.CustomColumnLabelProvider;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter;
import org.d3s.alricg.editor.common.Regulatoren;
import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider.CharElementVoraussetzungProvider;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider.SchamanenRitualGradProvider;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider.SchamanenRitualHerkunftProvider;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter.SchamanenRitualGradSorter;
import org.d3s.alricg.editor.common.Regulatoren.Regulator;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.ViewerSelectionListener;
import org.d3s.alricg.generator.views.GeneralRefreshableViewPart;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.SchamanenRitual;
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
 *
 */
public class SchamRitualView extends GeneralRefreshableViewPart {
	public static final String ID = "org.d3s.alricg.generator.views.general.SchamRitualView"; //$NON-NLS-1$
	

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.views.RefreshableViewPartImpl#createTable(org.eclipse.swt.widgets.Composite)
	 */
	@Override
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
		tc.getColumn().setText("*");
		tc.setLabelProvider(new CustomColumnLabelProvider.GeneralImageProvider());
		tc.getColumn().setWidth(25);
		tc.getColumn().setMoveable(false);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.GeneralImageSorter(),
								tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tableViewer.getTable().setSortColumn(tc.getColumn());
		tc.getColumn().setText("Name");
		tc.setLabelProvider(new CustomColumnLabelProvider.NameLabelProvider());
		tc.getColumn().setWidth(DEFAULT_FIRSTCOLUMN_WIDTH);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.NameSorter(),
								tableViewer));
		
		// Herkunft der Rituale
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 2);
		tc.getColumn().setText("Herkunft");
		tc.setLabelProvider(new SchamanenRitualHerkunftProvider());
		tc.getColumn().setWidth(100);
		tc.getColumn().setMoveable(true);

		// Grad
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 3);
		tc.getColumn().setText("Grad");
		tc.setLabelProvider(new SchamanenRitualGradProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(new SchamanenRitualGradSorter(), tableViewer));

		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Voraussetzung");
		tc.setLabelProvider(new CharElementVoraussetzungProvider());
		tc.getColumn().setWidth(200);
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
				ViewUtils.buildTableView(
					StoreDataAccessor.getInstance().getXmlAccessors(), 
					getRegulator(),
					this.getObjectCreator())
				);

		return tableViewer;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.views.RefreshableViewPartImpl#createTree(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected TreeViewer createTree(Composite parent) {
		int idx = 0;
		final TreeViewer treeViewer = new TreeViewer(parent, SWT.FULL_SELECTION
				| SWT.H_SCROLL | SWT.V_SCROLL);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setHeaderVisible(true);
		ColumnViewerToolTipSupport.enableFor(treeViewer, ToolTip.NO_RECREATE);
		
		// Columns
		TreeViewerColumn tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		treeViewer.getTree().setSortColumn(tc.getColumn());
		tc.getColumn().setText("Name");
		tc.setLabelProvider(new CustomColumnLabelProvider.NameLabelProvider());
		tc.getColumn().setWidth(DEFAULT_FIRSTCOLUMN_WIDTH);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.NameSorter(),
								treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("*");
		tc.setLabelProvider(new CustomColumnLabelProvider.GeneralImageProvider());
		tc.getColumn().setWidth(25);
		tc.getColumn().setMoveable(false);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.GeneralImageSorter(),
								treeViewer));
		
		// Herkunft der Rituale
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 2);
		tc.getColumn().setText("Herkunft");
		tc.setLabelProvider(new SchamanenRitualHerkunftProvider());
		tc.getColumn().setWidth(100);
		tc.getColumn().setMoveable(true);

		// Grad
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 3);
		tc.getColumn().setText("Grad");
		tc.setLabelProvider(new SchamanenRitualGradProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(new SchamanenRitualGradSorter(), treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Voraussetzung");
		tc.setLabelProvider(new CharElementVoraussetzungProvider());
		tc.getColumn().setWidth(200);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.CharElementVoraussetzungSorter(),
						treeViewer));

		// Inhalt und Sortierung setzen
		TreeObject root = ViewUtils.buildTreeView(
				StoreDataAccessor.getInstance().getXmlAccessors(), 
				getRegulator(),
				this.getObjectCreator());
		treeViewer.setContentProvider(new TreeViewContentProvider(root));
		treeViewer.getTree().setSortDirection(SWT.UP);
		treeViewer.setSorter(new CustomColumnViewerSorter.NameSorter());
		treeViewer.setInput(root);

		return treeViewer;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.views.charElemente.RefreshableViewPart#getRegulator()
	 */
	@Override
	public Regulator getRegulator() {
		return Regulatoren.SchamanenRitualRegulator;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.views.charElemente.RefreshableViewPart#getViewedClass()
	 */
	@Override
	public Class getViewedClass() {
		return SchamanenRitual.class;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.ProzessorObserver#addElement(java.lang.Object)
	 */
	@Override
	public void addElement(Object obj) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.ProzessorObserver#removeElement(java.lang.Object)
	 */
	@Override
	public void removeElement(Object obj) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.ProzessorObserver#setData(java.util.List)
	 */
	@Override
	public void setData(List list) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.ProzessorObserver#updateElement(java.lang.Object)
	 */
	@Override
	public void updateElement(Object obj) {
		// TODO Auto-generated method stub

	}

}
