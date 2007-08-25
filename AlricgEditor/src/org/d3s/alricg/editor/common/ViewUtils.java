/*
 * Created 20.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.d3s.alricg.editor.common.CustomColumnViewerSorter.CreatableViewerSorter;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTableObject;
import org.d3s.alricg.store.access.CharElementFactory.DependencyTableObject;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.links.Link;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * Tools um mit View zu Arbeiten
 * @author Vincent
 */
public class ViewUtils {
	
	/**
	 * Liefert zu einem TreeObject/TableObject mit CharElement oder Link,
	 * oder einem  CharElement oder Link direkt das zugehörige CharElement. 
	 * Gibt es KEIN zugehöriges CharElement, so wird "null" zurückgeliefert.
	 * 
	 * @param element TreeObject/TableObject/CharElement oder Link
	 * @return Das zugehörige CharElement oder "null" wenn keines existiert
	 */
	public static CharElement getCharElement(Object element) {
		
		if (element instanceof TreeObject) {
			element = ((TreeObject)element).getValue();
		} else if (element instanceof TableObject) {
			element = ((TableObject)element).getValue();
		}
		
		if (element instanceof Link) {
			return ((Link) element).getZiel();
		} else if (element instanceof CharElement) {
			return (CharElement) element;
		}
		
		return null;
	}
	
	/**
	 * Der "kleinste gemeinsamme Nenner" von TreeObject und TableObject
	 * @author Vincent
	 */
	public static interface TreeOrTableObject {
		public Object getValue();
	}
	
	/**
	 * Object um in einem TableViewer dargestellt zu werden.
	 * @author Vincent
	 */
	public static class TableObject implements TreeOrTableObject {
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
	public static class TreeObject implements TreeOrTableObject {
		private TreeObject[] children;
		private TreeObject parent;
		private Object value;
		
		public TreeObject(Object value, TreeObject parent) {
			this.value = value;
			this.parent = parent;
		}
		
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		public TreeObject getParent() {
			return parent;
		}
		public void setParent(TreeObject parent) {
			this.parent = parent;
		}

		/**
		 * Alle Kinder oder "null" falls es keine gibt
		 * @return
		 */
		public TreeObject[] getChildren() {
			return children;
		}

		public void addChildren(TreeObject newChild) {
			if (children == null) {
				children =  new TreeObject[0];
			}
			final TreeObject[] tmpTree = new TreeObject[children.length+1];
			for (int i = 0; i < children.length; i++) {
				tmpTree[i] = children[i];
			}
			tmpTree[children.length] = newChild;
			children = tmpTree;
		}
		public void removeChildren(TreeObject removeChild) {
			if (children == null) return;
			
			List<TreeObject> tmpList = new ArrayList<TreeObject>();
			tmpList.addAll(	Arrays.asList(children) );
			if (tmpList.remove(removeChild)) {
				children = tmpList.toArray(new TreeObject[tmpList.size()]);
				removeChild.parent = null;
			}
			if (children.length == 0) {
				children = null;
			}
		}
		
		public void setChildren(List<TreeObject> children) {
			this.children = children.toArray(new TreeObject[children.size()]);
		}
		
		public String toString() {
			return value.toString();
		}
	}
	
	/**
	 * Liefert zu einem Parent-Composite von einem Tree/Table View das selektierte Element.
	 * @param parentComp Der Parent von einem Tree/Table View
	 * @return Das selektierte Element oder "null" wenn nichts selektiert ist
	 */
	public static TreeOrTableObject getSelectedObject(Composite parentComp) {
		final Control topControl = ((StackLayout) parentComp.getLayout()).topControl;
		TreeOrTableObject value = null;
		
		if (topControl instanceof Tree && ((Tree) topControl).getSelection().length > 0) {
			value = (TreeOrTableObject) ((Tree) topControl).getSelection()[0].getData();

		} else if ( ((Table) topControl).getSelection().length > 0 ) {
			value = (TreeOrTableObject) ((Table) topControl).getSelection()[0].getData();
			
		}
		
		return value;
	}
	
	/**
	 * Steuert die Anzeige von TreeObjects in einem TreeView
	 * @author Vincent
	 */
	public static class TreeViewContentProvider implements IStructuredContentProvider,
			ITreeContentProvider {
		private TreeObject invisibleRoot;

		public TreeViewContentProvider(TreeObject invisibleRoot) {
			this.invisibleRoot = invisibleRoot;
		}
		
		public TreeObject getRoot() {
			return invisibleRoot;
		}

		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			// TODO implement ??
		}

		@Override
		public Object[] getElements(Object parent) {
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

	/**
	 * Steuert die Anzeige von TableObjects in einem TableView
	 * @author Vincent
	 */
	public static class TableViewContentProvider implements IStructuredContentProvider {
		private List<? extends TableObject> list;

		public TableViewContentProvider() {	}

		public List<? extends TableObject> getElementList() {
			return list;
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
			if (inputElement != null) {
				list = (List<? extends TableObject>) inputElement;
				return list.toArray(new TableObject[list.size()]);
			}
			return null;
		}
	}

	/**
	 * Konfigurierbarer SelectionListener für alle Columns.
	 * Ändert bei Klick die Sortierrichtung der Elemente
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
	
	/*
	 * Setzt den Index einer ComboBox auf den übergebenen Wert. Ist der
	 * Wert in der ComboBox nicht vorhaden, wird nichts geändert.
	 * @param combo Die ComboBox zum ändern
	 * @param str Der Wert, auf den die ComboBox gesetzt werden soll
	 *
	public static void findAndSetIndex(Combo combo, String str) {
		for (int i = 0; i < combo.getItems().length; i++) {
			if (combo.getItem(i).equals(str)) {
				combo.select(i);
			}
		}
	}*/
	
	/**
	 * DragSourceListener für Trees und Tabellen um CharElemente zu "Drag'en"
	 * @author Vincent
	 */
	public static class CharElementDragSourceListener implements DragSourceListener {
		private final ColumnViewer viewer;
		
		public CharElementDragSourceListener(ColumnViewer viewer) {
			this.viewer = viewer;
		}
		
		@Override
		public void dragStart(DragSourceEvent event) {
			if (viewer.getSelection().isEmpty()) {
				event.doit = false;
			} 
			final TreeOrTableObject treeTableObj = 
				(TreeOrTableObject) ((StructuredSelection)viewer.getSelection()).getFirstElement();
			
			if ( !(treeTableObj.getValue() instanceof CharElement) ) {
				event.doit = false;
			}
		}
		
		@Override
		public void dragSetData(DragSourceEvent event) {
			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			LocalSelectionTransfer.getTransfer().setSelection(selection);

		} 
		@Override
		public void dragFinished(DragSourceEvent event) {
			// Noop
		}
	}
	
	
}
