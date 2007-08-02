/*
 * Created 27.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import org.d3s.alricg.editor.common.CustomColumnLabelProvider;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.ViewerSelectionListener;
import org.d3s.alricg.store.charElemente.CharElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Vincent
 * 
 */
public class VoraussetzungPart extends AbstarctElementPart<CharElement> {
	public TreeViewer treeViewer;

	public VoraussetzungPart(Composite top) {
		treeViewer = new TreeViewer(top, SWT.FULL_SELECTION | SWT.H_SCROLL
				| SWT.V_SCROLL);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setHeaderVisible(true);
		ColumnViewerToolTipSupport.enableFor(treeViewer, ToolTip.NO_RECREATE);

		TreeViewerColumn tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 0);
		tc.getColumn().setText("Name");
		treeViewer.getTree().setSortColumn(tc.getColumn());
		tc.setLabelProvider(new CustomColumnLabelProvider.NameLabelProvider());
		tc.getColumn().setWidth(200);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.NameSorter(), treeViewer));

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 1);
		tc.getColumn().setText("Stufe");
		tc.setLabelProvider(new CustomColumnLabelProvider.LinkWertProvider());
		tc.getColumn().setWidth(100);
		tc.getColumn().setMoveable(true);

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 2);
		tc.getColumn().setText("Text");
		tc.setLabelProvider(new CustomColumnLabelProvider.LinkTextProvider());
		tc.getColumn().setWidth(100);
		tc.getColumn().setMoveable(true);

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 3);
		tc.getColumn().setText("ZweitZiel");
		tc
				.setLabelProvider(new CustomColumnLabelProvider.LinkZweitZielProvider());
		tc.getColumn().setWidth(100);
		tc.getColumn().setMoveable(true);

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 4);
		tc.getColumn().setText("-");
		tc.setLabelProvider(new ColumnLabelProvider());
		tc.getColumn().setWidth(25);
		tc.getColumn().setMoveable(true);

		// Unterstützung für DRAG
		treeViewer.addDragSupport(DND.DROP_COPY | DND.DROP_MOVE,
				new Transfer[] { LocalSelectionTransfer.getTransfer() },
				new DragSourceListener() {
					@Override
					public void dragStart(DragSourceEvent event) {
						if (treeViewer.getSelection().isEmpty()) {
							event.doit = false;
						}
						final TreeOrTableObject treeTableObj = (TreeOrTableObject) ((StructuredSelection) treeViewer
								.getSelection()).getFirstElement();

						if (!(treeTableObj.getValue() instanceof CharElement)) {
							event.doit = false;
						}
					}

					@Override
					public void dragSetData(DragSourceEvent event) {
						IStructuredSelection selection = (IStructuredSelection) treeViewer
								.getSelection();
						LocalSelectionTransfer.getTransfer().setSelection(
								selection);

					}

					@Override
					public void dragFinished(DragSourceEvent event) {
						final TreeObject treeObj = (TreeObject) ((StructuredSelection) treeViewer
								.getSelection()).getFirstElement();
						
						treeObj.getParent().removeChildren(treeObj);
						treeViewer.refresh();
					}
					
				});

		// Unterstützung für DROP
		int ops = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transfers = new Transfer[] { LocalSelectionTransfer
				.getTransfer() };
		treeViewer.addDropSupport(ops, transfers,
				new org.eclipse.jface.viewers.ViewerDropAdapter(treeViewer) {
					/*
					 * (non-Javadoc)
					 * 
					 * @see org.eclipse.jface.viewers.ViewerDropAdapter#drop(org.eclipse.swt.dnd.DropTargetEvent)
					 */
					@Override
					public void drop(DropTargetEvent event) {
						TreeObject sourceObj = (TreeObject) ((TreeSelection) event.data)
								.getFirstElement();
						TreeObject targetObj = (TreeObject) getCurrentTarget();

						if (((TreeObject) targetObj).getValue() instanceof CharElement) {
							targetObj = targetObj.getParent();
						}

						TreeObject newObject = new TreeObject(sourceObj
								.getValue(), targetObj);
						targetObj.addChildren(newObject);
						treeViewer.refresh();

						super.drop(event);
					}

					/*
					 * (non-Javadoc)
					 * 
					 * @see org.eclipse.jface.viewers.ViewerDropAdapter#performDrop(java.lang.Object)
					 */
					@Override
					public boolean performDrop(Object data) {
						return true;
					}

					/*
					 * (non-Javadoc)
					 * 
					 * @see org.eclipse.jface.viewers.ViewerDropAdapter#validateDrop(java.lang.Object,
					 *      int, org.eclipse.swt.dnd.TransferData)
					 */
					@Override
					public boolean validateDrop(Object target, int operation,
							TransferData transferType) {
						if (target == null) {
							return false;
						}
						return true;
					}
				});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean isDirty(CharElement charElem) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void loadData(CharElement charElem) {
		// "Grund-Tree" aufbauen
		final TreeObject invisibleRoot = new TreeObject("invisibleRoot", null);

		TreeObject positivTree = new TreeObject("Positive Voraussetzungen",
				invisibleRoot);
		invisibleRoot.addChildren(positivTree);
		TreeObject negativTree = new TreeObject("Negative Voraussetzungen",
				invisibleRoot);
		invisibleRoot.addChildren(negativTree);

		treeViewer
				.setContentProvider(new TreeViewContentProvider(invisibleRoot));
		treeViewer.setInput(this);

		//
		if (charElem.getVoraussetzung() == null)
			return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void saveData(IProgressMonitor monitor, CharElement charElem) {
		// TODO Auto-generated method stub

	}

}
