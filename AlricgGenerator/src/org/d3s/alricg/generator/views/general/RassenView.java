/*
 * Created 17.11.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.views.general;

import java.util.List;
import java.util.logging.Level;

import org.d3s.alricg.editor.common.CustomColumnLabelProvider;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter;
import org.d3s.alricg.editor.common.Regulatoren;
import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter.CreatableViewerSorter;
import org.d3s.alricg.editor.common.Regulatoren.Regulator;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.ViewerSelectionListener;
import org.d3s.alricg.generator.Activator;
import org.d3s.alricg.generator.common.CustomLabelProvider.HerkunftSOLabelProvider;
import org.d3s.alricg.generator.common.CustomLabelProvider.HerkunftVoraussetzungProvider;
import org.d3s.alricg.generator.common.CustomLabelProvider.ModiHerkunftProvider;
import org.d3s.alricg.generator.common.CustomLabelProvider.VerbilligtHerkunftProvider;
import org.d3s.alricg.generator.common.CustomViewerSorter.HerkunftSOSorter;
import org.d3s.alricg.generator.views.GeneralRefreshableViewPart;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.HerkunftVariante;
import org.d3s.alricg.store.charElemente.Rasse;
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
 * Anzeige alle Rassen die zu Verfügung stehen.
 * @author Vincent
 */
public class RassenView extends GeneralRefreshableViewPart {
	public static final String ID = "org.d3s.alricg.generator.views.general.RassenView"; //$NON-NLS-1$

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

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Art");
		tc.setLabelProvider(new ArtLabelProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(new ArtSorter(), tableViewer));

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("GP");
		tc.getColumn().setToolTipText("Generierungspunkte Kosten");
		tc.setLabelProvider(new CustomColumnLabelProvider.HerkunftGpProvider());
		tc.getColumn().setWidth(30);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.HerkunftGpSorter(),
								tableViewer));

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("SO");
		tc.getColumn().setToolTipText("Möglicher Sozialstatus");
		tc.setLabelProvider(new HerkunftSOLabelProvider());
		tc.getColumn().setWidth(60);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new HerkunftSOSorter(), tableViewer));

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Größe");
		tc.getColumn().setToolTipText("Mögliche Größe");
		tc.setLabelProvider(new GroesseProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new GroesseSorter(), tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Alter");
		tc.getColumn().setToolTipText("Mögliches alter");
		tc.setLabelProvider(new AlterProvider());
		tc.getColumn().setWidth(60);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new AlterSorter(), tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Modis");
		tc.getColumn().setToolTipText("Modifikationen");
		tc.setLabelProvider(new ModiHerkunftProvider(new String[] {
				HerkunftVariante.EIGEN_MODIS,
				HerkunftVariante.VORTEILE,
				HerkunftVariante.NACHTEILE,
				HerkunftVariante.SONDERF
			}));
		tc.getColumn().setWidth(175);
		tc.getColumn().setMoveable(true);
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Verbilligt SF");
		tc.getColumn().setToolTipText("Verbilligte Sonderfertigkeiten");
		tc.setLabelProvider(new VerbilligtHerkunftProvider());
		tc.getColumn().setWidth(175);
		tc.getColumn().setMoveable(true);
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Voraussetzung");
		tc.setLabelProvider(new HerkunftVoraussetzungProvider());
		tc.getColumn().setWidth(175);
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
		// Init Viewer
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

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("GP");
		tc.getColumn().setToolTipText("Generierungspunkte Kosten");
		tc.setLabelProvider(new CustomColumnLabelProvider.HerkunftGpProvider());
		tc.getColumn().setWidth(30);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.HerkunftGpSorter(),
								treeViewer));

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("SO");
		tc.getColumn().setToolTipText("Möglicher Sozialstatus");
		tc.setLabelProvider(new HerkunftSOLabelProvider());
		tc.getColumn().setWidth(60);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new HerkunftSOSorter(), treeViewer));

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Größe");
		tc.getColumn().setToolTipText("Mögliche Größe");
		tc.setLabelProvider(new GroesseProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new GroesseSorter(), treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Alter");
		tc.getColumn().setToolTipText("Mögliches alter");
		tc.setLabelProvider(new AlterProvider());
		tc.getColumn().setWidth(60);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new AlterSorter(), treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Modis");
		tc.getColumn().setToolTipText("Modifikationen");
		tc.setLabelProvider(new ModiHerkunftProvider(new String[] {
				HerkunftVariante.EIGEN_MODIS,
				HerkunftVariante.VORTEILE,
				HerkunftVariante.NACHTEILE,
				HerkunftVariante.SONDERF
			}));
		tc.getColumn().setWidth(175);
		tc.getColumn().setMoveable(true);
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Verbilligte SF");
		tc.getColumn().setToolTipText("Verbilligte Sonderfertigkeiten");
		tc.setLabelProvider(new VerbilligtHerkunftProvider());
		tc.getColumn().setWidth(175);
		tc.getColumn().setMoveable(true);
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, idx++);
		tc.getColumn().setText("Voraussetzung");
		tc.setLabelProvider(new HerkunftVoraussetzungProvider());
		tc.getColumn().setWidth(175);
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
	 * @see org.d3s.alricg.generator.views.RefreshableViewPartImpl#getRegulator()
	 */
	@Override
	public Regulator getRegulator() {
		return Regulatoren.RasseRegulator;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.views.RefreshableViewPartImpl#getViewedClass()
	 */
	@Override
	public Class getViewedClass() {
		return Rasse.class;
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
	
	
// ----------------------------------------------------------------------
	
	public static class AlterProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final Rasse tmpRasse = (Rasse) ViewUtils.getCharElement(element);
			
			if (tmpRasse == null) {
				return ""; //$NON-NLS-1$
			} else if (tmpRasse.getAlterWuerfel() == null) {
				// Wert wird von 'ElternVariante' mitgeliefert
				if (!(tmpRasse instanceof HerkunftVariante)) {
					Activator.logger.log(
							Level.SEVERE, 
							"AlterWuerfel der Rasse " + tmpRasse.getName() + " ist null!");
					throw new IllegalArgumentException(
							"AlterWuerfel der Rasse " + tmpRasse.getName() + " ist null!");
				}

				return getText(((HerkunftVariante) tmpRasse).getVarianteVon());
			} else {
				return tmpRasse.getAlterWuerfel().getMinWurf()
						+ " - " + tmpRasse.getAlterWuerfel().getMaxWurf();
			}
		}
	}
	
	public static class GroesseProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final Rasse tmpRasse = (Rasse) ViewUtils.getCharElement(element);
			
			if (tmpRasse == null) {
				return ""; //$NON-NLS-1$
			} else if (tmpRasse.getGroesseWuerfel() == null) {
				// Wert wird von 'ElternVariante' mitgeliefert
				if (!(tmpRasse instanceof HerkunftVariante)) {
					Activator.logger.log(
							Level.SEVERE, 
							"GroesseWuerfel der Rasse " + tmpRasse.getName() + " ist null!");
					throw new IllegalArgumentException(
							"GroesseWuerfel der Rasse " + tmpRasse.getName() + " ist null!");
				}

				return getText(((HerkunftVariante) tmpRasse).getVarianteVon());
			} else {
				return tmpRasse.getGroesseWuerfel().getMinWurf() 
						+ " - " + tmpRasse.getGroesseWuerfel().getMaxWurf();
			}
		}
	}
	
	public static class ArtLabelProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final Rasse tmpRasse = (Rasse) ViewUtils.getCharElement(element);
			if (tmpRasse != null) {
				return tmpRasse.getArt().toString();
			}
			return ""; //$NON-NLS-1$
		}
	}
	
// ----------------------------------------------------------------------
	
	public static class AlterSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			final Rasse tmpRasse = (Rasse) ViewUtils.getCharElement(obj);
			
			if (tmpRasse.getAlterWuerfel() == null) {
				return getComparable(((HerkunftVariante) tmpRasse).getVarianteVon());
			} else {
				return tmpRasse.getAlterWuerfel().getMaxWurf();
			}
		}
	}
	
	public static class GroesseSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			final Rasse tmpRasse = (Rasse) ViewUtils.getCharElement(obj);
			
			if (tmpRasse.getGroesseWuerfel() == null) {
				return getComparable(((HerkunftVariante) tmpRasse).getVarianteVon());
			} else {
				return tmpRasse.getGroesseWuerfel().getMaxWurf();
			}
		}
	}
	
	public static class ArtSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return ((Rasse) getCharElement(obj)).getArt().toString();
		}
	}

}
