/*
 * Created 21.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.views.charElemente;

import org.d3s.alricg.common.icons.MagieIconsLibrary;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter;
import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.common.ViewUtils.CharElementDragSourceListener;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.ViewerSelectionListener;
import org.d3s.alricg.editor.utils.EditorViewUtils;
import org.d3s.alricg.editor.utils.Regulatoren;
import org.d3s.alricg.editor.utils.Regulatoren.Regulator;
import org.d3s.alricg.editor.views.ViewMessages;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Zauber;
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
import org.eclipse.swt.widgets.Display;

/**
 * @author Vincent
 *
 */
public class ZauberView extends RefreshableViewPart {

	public ZauberView() {
		MagieIconsLibrary.getInstance().addConsumer(this);
	}
	
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
		tc.getColumn().setWidth(125);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.DateiSorter(),
								tableViewer));

		// Merkmale
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 2);
		tc.getColumn().setText("1");
		tc.getColumn().setToolTipText("Magie-Merkmal 1");
		tc.setLabelProvider(new ZauberMerkmalProvider(0, this));
		tc.getColumn().setWidth(24);
		tc.getColumn().setMoveable(true);
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 3);
		tc.getColumn().setText("2");
		tc.getColumn().setToolTipText("Magie-Merkmal 2");
		tc.setLabelProvider(new ZauberMerkmalProvider(1, this));
		tc.getColumn().setWidth(24);
		tc.getColumn().setMoveable(true);
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 4);
		tc.getColumn().setText("3");
		tc.getColumn().setToolTipText("Magie-Merkmal 3");
		tc.setLabelProvider(new ZauberMerkmalProvider(2, this));
		tc.getColumn().setWidth(24);
		tc.getColumn().setMoveable(true);
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 5);
		tc.getColumn().setText("4");
		tc.getColumn().setToolTipText("Magie-Merkmal 4+");
		tc.setLabelProvider(new ZauberMerkmalProvider(3, this));
		tc.getColumn().setWidth(24);
		tc.getColumn().setMoveable(true);
		
		// verbreitung
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 6);
		tc.getColumn().setText("Verbreitung");
		tc.setLabelProvider(new ZauberVerbreitungProvider());
		tc.getColumn().setWidth(125);
		tc.getColumn().setMoveable(true);

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 7);
		tc.getColumn().setText(ViewMessages.TalentView_Probe);
		tc.setLabelProvider(new CustomColumnLabelProvider.Faehigkeit3EigenschaftProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 8);
		tc.getColumn().setText(ViewMessages.TalentView_SKT);
		tc.setLabelProvider(new CustomColumnLabelProvider.SKTLabelProvider());
		tc.getColumn().setWidth(50);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.SktSorter(), tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 9);
		tc.getColumn().setText(ViewMessages.TalentView_Voraussetzung);
		tc.setLabelProvider(new CustomColumnLabelProvider.CharElementVoraussetzungProvider());
		tc.getColumn().setWidth(150);
		tc.getColumn().setMoveable(true);
		
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


		// Merkmale
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 2);
		tc.getColumn().setText("1");
		tc.getColumn().setToolTipText("Magie-Merkmal 1");
		tc.setLabelProvider(new ZauberMerkmalProvider(0, this));
		tc.getColumn().setWidth(24);
		tc.getColumn().setMoveable(true);
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 3);
		tc.getColumn().setText("2");
		tc.getColumn().setToolTipText("Magie-Merkmal 2");
		tc.setLabelProvider(new ZauberMerkmalProvider(1, this));
		tc.getColumn().setWidth(24);
		tc.getColumn().setMoveable(true);
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 4);
		tc.getColumn().setText("3");
		tc.getColumn().setToolTipText("Magie-Merkmal 3");
		tc.setLabelProvider(new ZauberMerkmalProvider(2, this));
		tc.getColumn().setWidth(24);
		tc.getColumn().setMoveable(true);
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 5);
		tc.getColumn().setText("4");
		tc.getColumn().setToolTipText("Magie-Merkmal 4+");
		tc.setLabelProvider(new ZauberMerkmalProvider(3, this));
		tc.getColumn().setWidth(24);
		tc.getColumn().setMoveable(true);
		
		// verbreitung
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 6);
		tc.getColumn().setText("Verbreitung");
		tc.setLabelProvider(new ZauberVerbreitungProvider());
		tc.getColumn().setWidth(125);
		tc.getColumn().setMoveable(true);
		

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 7);
		tc.getColumn().setText(ViewMessages.TalentView_Probe);
		tc.setLabelProvider(new CustomColumnLabelProvider.Faehigkeit3EigenschaftProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 8);
		tc.getColumn().setText(ViewMessages.TalentView_SKT);
		tc.setLabelProvider(new CustomColumnLabelProvider.SKTLabelProvider());
		tc.getColumn().setWidth(50);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.SktSorter(), treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 9);
		tc.getColumn().setText(ViewMessages.TalentView_Voraussetzung);
		tc.setLabelProvider(new CustomColumnLabelProvider.CharElementVoraussetzungProvider());
		tc.getColumn().setWidth(150);
		tc.getColumn().setMoveable(true);
		
		
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

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.views.charElemente.RefreshableViewPart#getRegulator()
	 */
	@Override
	public Regulator getRegulator() {
		return Regulatoren.ZauberRegulator;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.views.charElemente.RefreshableViewPart#getViewedClass()
	 */
	@Override
	public Class getViewedClass() {
		return Zauber.class;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		MagieIconsLibrary.getInstance().removeConsumer(this);
		super.dispose();
	}

	public static class ZauberVerbreitungProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return "";
			
			if (charElem instanceof Zauber) {
				return ((Zauber) charElem).getVerbreitungText(true);
			}
			return "";
		}
		
		@Override
		public String getToolTipText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return null;
			
			if ( charElem instanceof Zauber ) {
				return ((Zauber) charElem).getVerbreitungText(false);
			}
			return null;
		}
	}

	
	/**
	 * Für die Darstellung der Merkmale als Bilder und ToolTip
	 * @author Vincent
	 */
	public static class ZauberMerkmalProvider extends ColumnLabelProvider {
		private static final int ANZAHL_MERKMAL_SPALTEN = 4;
		private final int index;
		private final ZauberView consumer;
		
		public ZauberMerkmalProvider(int index, ZauberView consumer) {
			this.index = index;
			this.consumer = consumer;
		}
		
		@Override
		public String getText(Object element) {
			if (index+1 < ANZAHL_MERKMAL_SPALTEN) return "";
		
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return "";
			
			if (charElem instanceof Zauber) {
				if ( ((Zauber) charElem).getMerkmale().length >= ANZAHL_MERKMAL_SPALTEN)  {
					return "+";
				}
			}
			return "";
		}
		
		
		@Override
		public Image getImage(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return null;
			
			if (charElem instanceof Zauber 
					&& ((Zauber) charElem).getMerkmale().length > index) 
			{
					return MagieIconsLibrary.getInstance().getImage16(
							((Zauber) charElem).getMerkmale()[index], 
							consumer);
			}
			return null;
		}

		@Override
		public String getToolTipText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return null;
			if ( !(charElem instanceof Zauber) ) return null; 
					
			String retStr = "";
			if ( ((Zauber) charElem).getMerkmale().length > index ) {
				retStr = ((Zauber) charElem).getMerkmale()[index].getValue();
			}
			
			if (index+1 >= ANZAHL_MERKMAL_SPALTEN
					&& ((Zauber) charElem).getMerkmale().length >= ANZAHL_MERKMAL_SPALTEN) {
				for (int i = index+1; i < ((Zauber) charElem).getMerkmale().length; i++) {
					
					retStr = retStr + ", " + ((Zauber) charElem).getMerkmale()[i];
				}
			}

			return retStr;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.CellLabelProvider#getToolTipImage(java.lang.Object)
		 */
		@Override
		public Image getToolTipImage(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return null;
			
			if ( !(charElem instanceof Zauber 
					&& ((Zauber) charElem).getMerkmale().length > index)) {
				return null;
			}
			
			// Image erzeugen
			final Image	img =  MagieIconsLibrary.getInstance().getImageImageDescriptor24(
							((Zauber) charElem).getMerkmale()[index]).createImage();
			
			// Dispose Image
			final Display d  = consumer.getTableViewer().getTable().getShell().getDisplay();
			
			d.timerExec (100, new Runnable () {
				public void run () {
					img.dispose();
				}
			});
			
			return img;
		}
		
		
	}
}
