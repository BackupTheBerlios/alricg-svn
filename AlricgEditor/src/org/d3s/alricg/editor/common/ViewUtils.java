/*
 * Created 20.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common;

import java.util.ArrayList;
import java.util.List;

import org.d3s.alricg.editor.common.CustomColumnViewerSorter.CreatableViewerSorter;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IViewSite;

/**
 * Tools um mit View zu Arbeiten
 * @author Vincent
 */
public class ViewUtils {
	
	public static class TableObject {
		private final Object value;
		
		public TableObject(Object value) {
			this.value = value;
		}
		
		public Object getValue() {
			return value;
		}
	}
	
	/**
	 * Zur Darstellung mit einem TreeViewer
	 * @author Vincent
	 */
	public static class TreeObject {
		private List<TreeObject> children;
		private final TreeObject parent;
		private final Object value;
		
		public TreeObject(Object value, TreeObject parent) {
			this.value = value;
			this.parent = parent;
		}
		
		public Object getValue() {
			return value;
		}
		public TreeObject getParent() {
			return parent;
		}

		public TreeObject[] getChildren() {
			if (children != null) {
				return children.toArray(new TreeObject[children.size()]);
			}
			return null;
		}

		public void addChildren(TreeObject newChild) {
			if (children == null) children =  new ArrayList<TreeObject>();
			children.add(newChild);
		}
		public void setChildren(List<TreeObject> children) {
			this.children = children;
		}
		
		public String toString() {
			return value.toString();
		}
	}

	/**
	 * Schnittstelle welche für Objekte benötigt wird, mit die Methode "buildViewTree"
	 * verarbeitet werden sollen.
	 * @see org.d3s.alricg.editor.common.ViewUtils.buildViewTree(Iterator, Regulator)
	 * @author Vincent
	 */
	public static interface Regulator {
		public Object[] getFirstCategory(CharElement charElement);
		public List<? extends CharElement> getListFromAccessor(XmlAccessor accessor);
	}
	
	
	public static class TreeViewContentProvider implements IStructuredContentProvider,
			ITreeContentProvider {
		private TreeObject invisibleRoot;

		public TreeViewContentProvider(TreeObject invisibleRoot) {
			this.invisibleRoot = invisibleRoot;
		}

		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			// TODO implement ??
		}

		@Override
		public Object[] getElements(Object parent) {
			if (parent instanceof IViewSite) {
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}

		@Override
		public Object getParent(Object child) {
			return ((TreeObject) child).getParent();
		}

		@Override
		public Object[] getChildren(Object parent) {
			return ((TreeObject) parent).getChildren();
		}

		@Override
		public boolean hasChildren(Object parent) {
			return ((TreeObject) parent).getChildren() != null;
		}

		@Override
		public void dispose() {
		}
	}

	
	public static class TableViewContentProvider implements IStructuredContentProvider {
		private List<? extends TableObject> list;

		public TableViewContentProvider(List<? extends TableObject> list) {
			this.list = list;
		}

		@Override
		public void dispose() {
			// Noop
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO implement ??
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IViewSite) {
				return list.toArray(new TableObject[list.size()]);
			}
			return null;
		}
	}

	/**
	 * Konfigurierbarer SelectionListener für alle Columns
	 * @author Vincent
	 */
	public static class ViewerSelectionListener implements SelectionListener {
		private final CreatableViewerSorter sorter;
		private final ColumnViewer viewer;
		
		public ViewerSelectionListener(CreatableViewerSorter sorter, ColumnViewer viewer) {
			this.sorter = sorter;
			this.viewer = viewer;
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// noop
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			
			if (viewer instanceof TreeViewer) {
				final Tree tree = ((TreeViewer) viewer).getTree();
				tree.setSortColumn(((TreeColumn) e.widget));
				
				if (((TreeViewer) viewer).getTree().getSortDirection() == SWT.DOWN) {
					tree.setSortDirection(SWT.UP);
				} else {
					tree.setSortDirection(SWT.DOWN);
				}
				
				((TreeViewer) viewer).getControl().setRedraw(false);
				((TreeViewer) viewer).setSorter(sorter.getNewInstance());
				((TreeViewer) viewer).getControl().setRedraw(true);
				
			} else if (viewer instanceof TableViewer) {
				final Table table = ((TableViewer) viewer).getTable();
				table.setSortColumn(((TableColumn) e.widget));
				
				if (((TableViewer) viewer).getTable().getSortDirection() == SWT.DOWN) {
					table.setSortDirection(SWT.UP);
				} else {
					table.setSortDirection(SWT.DOWN);
				}
				
				((TableViewer) viewer).getControl().setRedraw(false);
				((TableViewer) viewer).setSorter(sorter.getNewInstance());
				((TableViewer) viewer).getControl().setRedraw(true);
			}
		}
	}
			
}
