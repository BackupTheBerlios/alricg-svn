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
import java.util.HashMap;
import java.util.List;

import org.d3s.alricg.editor.common.CustomColumnViewerSorter.CreatableViewerSorter;
import org.d3s.alricg.editor.common.Regulatoren.Regulator;
import org.d3s.alricg.editor.views.charElemente.RefreshableViewPartImpl;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.HerkunftVariante;
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
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

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
			if (children.length == 0 && parent != null) {
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
	
	/**
	 * Hiermit kann die Erzeugung der Tree/Table-Objekte gesteuert werden.
	 * @author Vincent
	 */
	public static interface ObjectCreator {
		public TreeObject createTreeObject(Object element, TreeObject parentNode);
		public TableObject createTableObject(Object element);
	}
	
	/**
	 * Liefert zu einem Parent-Composite von einem Tree/Table View das selektierte Element.
	 * @param parentComp Der Parent von einem Tree/Table View
	 * @return Das selektierte Element oder "null" wenn nichts selektiert ist
	 *
	public static TreeOrTableObject getSelectedObject(Composite parentComp) {
		final Control topControl = ((StackLayout) parentComp.getLayout()).topControl;
		TreeOrTableObject value = null;
		
		if (topControl instanceof Tree && ((Tree) topControl).getSelection().length > 0) {
			value = (TreeOrTableObject) ((Tree) topControl).getSelection()[0].getData();

		} else if (topControl instanceof Table && ((Table) topControl).getSelection().length > 0 ) {
			value = (TreeOrTableObject) ((Table) topControl).getSelection()[0].getData();
			
		}
		
		return value;
	}*/
	
	/**
	 * Erzeugt aus einem Liste von Accessoren und zugehörigem Regulator einen Tree 
	 * zur Darstellung in einem TreeView.
	 * Dabei wird der Tree aufgebaut nach
	 * 1. Der Kategorie "getFirstCategory"
	 * 2. Dem Sammelbegriff
	 * 
	 * @see org.d3s.alricg.editor.common.ViewUtils.Regulator
	 * @param accessorList Liste mit Objekten zum einordenen
	 * @param regulator Regualtor um mit verschiedenen Objekten umgehen zu können
	 * @return Root Element es neuen Trees
	 *
	public static TreeObject buildTreeView(List<XmlAccessor> accessorList, Regulator regulator, ObjectCreator objCreator) {
		// init HashMap und Root
		final HashMap<String, TreeObject> map = new HashMap<String, TreeObject>();
		final TreeObject invisibleRoot = objCreator.createTreeObject("invisibleRoot", null);
		invisibleRoot.setChildren(new ArrayList()); // um eine "asserNull" exception zu verhindern, 
													// wenn ohne Elemente gestaret
		// Alle Elemente durchlaufen
		for (int i1 = 0; i1 < accessorList.size(); i1++) {
			List<? extends CharElement> charElementList = regulator.getListFromAccessor(accessorList.get(i1));
			if (charElementList == null) continue;
			
			for (int i2 = 0; i2 < charElementList.size(); i2++) {
				
				final CharElement charElement = charElementList.get(i2);
				Object[] firstCat = regulator.getFirstCategory(charElement);
				
				// Falls keine "FirstCategory", direkt zum Root hinzufügen
				if (firstCat.length == 0) {
					firstCat = new String[]{"dummie"};
					map.put(firstCat[0].toString(), invisibleRoot);
				}
				
				for (int i3 = 0; i3 < firstCat.length; i3++) {
					TreeObject parent;
					
					// Suche Parent, erstelle Parent aus "FirstCategory" falls nicht existent
					if (map.containsKey(firstCat[i3].toString())) {
						parent = map.get(firstCat[i3].toString());
					} else {
						TreeObject newChild = new TreeObject(firstCat[i3], invisibleRoot);
						map.put(firstCat[i3].toString(), newChild);
						invisibleRoot.addChildren(newChild);
						parent = newChild;
					}
					
					// Suche Sammelbegriff, erstelle falls nicht existens und setzte als Parent
					if (charElement.getSammelbegriff() != null) {
						if (map.containsKey(charElement.getSammelbegriff())) {
							parent = map.get(charElement.getSammelbegriff());
						} else {
							TreeObject newChild = new TreeObject(charElement.getSammelbegriff(), parent);
							map.put(charElement.getSammelbegriff(), newChild);
							parent.addChildren(newChild);
							parent = newChild;
						}
					}
					
					// Add Element
					if (charElementList.get(i2) instanceof Herkunft) {
						buildHerkunftViewHelper(parent, (Herkunft) charElementList.get(i2), objCreator);
					} else {
						parent.addChildren(objCreator.createTreeObject(charElementList.get(i2), parent));
					}
				}
			}
		}
		
		return invisibleRoot;
	}*/
	
	/**
	 * Erzeugt aus einem Liste von Accessoren und zugehörigem Regulator einen Tree 
	 * zur Darstellung in einem TreeView.
	 * Dabei wird der Tree aufgebaut nach
	 * 1. Der Kategorie "getFirstCategory"
	 * 2. Dem Sammelbegriff
	 * 
	 * @see org.d3s.alricg.editor.common.ViewUtils.Regulator
	 * @param accessorList Liste mit Objekten zum einordenen
	 * @param regulator Regualtor um mit verschiedenen Objekten umgehen zu können
	 * @return Root Element des neuen Trees
	 */
	public static TreeObject buildTreeView(List<XmlAccessor> accessorList, Regulator regulator, ObjectCreator objCreator) {
		List list = new ArrayList();
		for (int i1 = 0; i1 < accessorList.size(); i1++) {
			if (accessorList.get(i1) == null) continue;
			list.addAll(regulator.getListFromAccessor( accessorList.get(i1) ));
		}
		return buildTreeViewAlt(list, regulator, objCreator);
	}
	
	/**
	 * Erzeugt aus einem Liste von CharElementen/Links und zugehörigem Regulator einen Tree 
	 * zur Darstellung in einem TreeView.
	 * Dabei wird der Tree aufgebaut nach
	 * 1. Der Kategorie "getFirstCategory"
	 * 2. Dem Sammelbegriff
	 * 
	 * @see org.d3s.alricg.editor.common.ViewUtils.Regulator
	 * @param List Liste mit Objekten zum einordenen
	 * @param regulator Regualtor um mit verschiedenen Objekten umgehen zu können
	 * @return Root Element des neuen Trees
	 */	
	public static TreeObject buildTreeViewAlt(List list, Regulator regulator, ObjectCreator objCreator) {
		// init HashMap und Root
		final HashMap<String, TreeObject> map = new HashMap<String, TreeObject>();
		final TreeObject invisibleRoot = objCreator.createTreeObject("invisibleRoot", null);
		invisibleRoot.setChildren(new ArrayList()); // um eine "asserNull" exception zu verhindern, 
													// wenn ohne Elemente gestaret
		
		if (list == null) return invisibleRoot;
		// Alle Elemente durchlaufen
		for (int i1 = 0; i1 < list.size(); i1++) {
				
			final CharElement charElement = getCharElement(list.get(i1));
			Object[] firstCat = regulator.getFirstCategory(charElement);
			
			// Falls keine "FirstCategory", direkt zum Root hinzufügen
			if (firstCat.length == 0) {
				firstCat = new String[]{"dummie"};
				map.put(firstCat[0].toString(), invisibleRoot);
			}
			
			for (int i3 = 0; i3 < firstCat.length; i3++) {
				TreeObject parent;
				
				// Suche Parent, erstelle Parent aus "FirstCategory" falls nicht existent
				if (map.containsKey(firstCat[i3].toString())) {
					parent = map.get(firstCat[i3].toString());
				} else {
					TreeObject newChild = new TreeObject(firstCat[i3], invisibleRoot);
					map.put(firstCat[i3].toString(), newChild);
					invisibleRoot.addChildren(newChild);
					parent = newChild;
				}
				
				// Suche Sammelbegriff, erstelle falls nicht existens und setzte als Parent
				if (charElement.getSammelbegriff() != null) {
					if (map.containsKey(charElement.getSammelbegriff())) {
						parent = map.get(charElement.getSammelbegriff());
					} else {
						TreeObject newChild = new TreeObject(charElement.getSammelbegriff(), parent);
						map.put(charElement.getSammelbegriff(), newChild);
						parent.addChildren(newChild);
						parent = newChild;
					}
				}
				
				// Add Element
				if (charElement instanceof Herkunft) {
					buildHerkunftViewHelper(parent, (Herkunft) charElement, objCreator);
				} else {
					parent.addChildren(objCreator.createTreeObject(list.get(i1), parent));
				}
			}
		}
		
		return invisibleRoot;
	}
	
	/**
	 * Erzeugt aus einem Liste von Accessoren und zugehörigem Regulator eine Tabelle 
	 * zur Darstellung in einem TableView.
	 * 
	 * @see org.d3s.alricg.editor.common.ViewUtils.Regulator
	 * @param accessorList Liste mit Objekten zum einordenen
	 * @param regulator Regualtor um mit verschiedenen Objekten umgehen zu können
	 * @return Liste mit allen Elementen für die Tabelle
	 */
	public static List<? extends TableObject> buildTableView(
			List<XmlAccessor> accessorList, 
			Regulator regulator,
			ObjectCreator objCreator) 
	{
		List list = new ArrayList();
		if (accessorList == null) return list;
		
		for (int i1 = 0; i1 < accessorList.size(); i1++) {
			if (accessorList.get(i1) == null) continue;
			
			list.addAll(regulator.getListFromAccessor( accessorList.get(i1) ));
		}
		return buildTableViewAlt(list, regulator, objCreator);
	}
	
	/**
	 * Erzeugt aus einem Liste von CharElementen/Links und zugehörigem Regulator eine Tabelle 
	 * zur Darstellung in einem TableView.
	 * 
	 * @see org.d3s.alricg.editor.common.ViewUtils.Regulator
	 * @param List Liste mit Objekten zum einordenen
	 * @param regulator Regualtor um mit verschiedenen Objekten umgehen zu können
	 * @return Liste mit allen Elementen für die Tabelle
	 */
	public static List<? extends TableObject> buildTableViewAlt(
			List list, 
			Regulator regulator,
			ObjectCreator objCreator) 
	{
		List<TableObject> returnList = new ArrayList<TableObject>();
		if (list == null) return returnList;
		
		for (int i2 = 0; i2 < list.size(); i2++) {
			if (getCharElement(list.get(i2)) instanceof Herkunft) {
				buildHerkunftTableViewHelper(
						returnList, 
						(Herkunft) list.get(i2), 
						objCreator);
			} else {
				returnList.add(	objCreator.createTableObject(list.get(i2)) );
			}
		}
		return returnList;
	}
	
	/**
	 * Erstellt rekursiv für alle Varianten der Herkunft "herk" einträge in "returnList"
	 * @param returnList Liste mit allen TableObjects die erzeugt wurden
	 * @param herk Herkunft für die ein TableObjects erzeugt wird
	 * @param accessor Aktueller accesssor
	 */
	private static void buildHerkunftTableViewHelper(List<TableObject> returnList, Herkunft herk, ObjectCreator objCreator) {
		returnList.add(objCreator.createTableObject(herk));
		
		if (herk.getVarianten() == null) return;
		for (int i = 0; i < herk.getVarianten().length; i++) {
			buildHerkunftTableViewHelper(returnList, (Herkunft) herk.getVarianten()[i], objCreator);
		}
	}
	
	/**
	 * Erstellt rekursiv für alle Varianten der Herkunft "herk" neue Nodes
	 * @param node Der aktuelle Node, an dem das neue TreeObject "angehangen" wird
	 * @param herk Herkunft für die ein neues TreeObject erstellt wird
	 * @param accessor Aktueller accesssor
	 */
	private static void buildHerkunftViewHelper(TreeObject node, Herkunft herk, ObjectCreator objCreator) {
		TreeObject newNode = objCreator.createTreeObject(herk, node);
		node.addChildren(newNode);
		newNode.setParent(node);
			
		if (herk.getVarianten() == null) return;
		for (int i = 0; i < herk.getVarianten().length; i++) {
			buildHerkunftViewHelper(newNode, (Herkunft) herk.getVarianten()[i], objCreator);
		}
	}
	
	/**
	 * Entfernd ein Element (CharElement oder Link) von einem View. Dadurch wird das Element
	 * NICHT von der Datenbasis entfernd!
	 * @param viewer Der Viewer von dem das Element entfernd werden soll
	 * @param element Das zu entferndene Element
	 */
	public static void removeElementFromView(
			RefreshableViewPart viewer, 
			Object element)
	{
		if (viewer == null) return;
		
		// "Daten-Modelle" holen
		List tabList = (List) ((TableViewContentProvider) viewer.getTableViewer().getContentProvider()).getElementList();
		TreeObject root = ((TreeViewContentProvider) viewer.getTreeViewer().getContentProvider()).getRoot();
		
		// Aus Table entfernen
		for (int i = 0; i < tabList.size(); i++) {
			if ( ((TableObject) tabList.get(i)).getValue().equals(element) ) {
				tabList.remove(i);
				break;
			}
		}
		
		// Aus Tree entfernen
		ViewUtils.removeElementFromTree(root, element);
	}
	
	/**
	 * Entfernt ab "node" alle Nodes mit dem value "toRemoveValue". Wenn durch das
	 * entfernen ein "ordner" des Baumes leer wird, wird dieser ebenfalls entfernt
	 * @param node Aktuell zu durchsuchender Knoten
	 * @param toRemoveValue Alle Knoten mit diesem Wert werden gelöscht
	 * @return true Der Knoten "node" wurde gelöscht, ansonsten false (dies sagt nichts 
	 * 		darüber aus, ob evtl. unterknoten von "node" gelöscht wurden oder nicht)
	 */
	private static boolean removeElementFromTree(TreeObject node, Object toRemoveValue) {
		boolean hasDeletedFlag = false;
		
		if (node.getValue().equals(toRemoveValue)) { //(&& node.getChildren() == null)) {
			// Entferne Child
			TreeObject parent = node.getParent();
			parent.removeChildren(node);
			hasDeletedFlag = true;
			
			// Entferne Parent, wenn dieser keine Kinder mehr hat
			if (parent.getChildren() == null && parent.getParent() != null) {
				removeElementFromTree(parent, parent.getValue());
			}
		}
		
		int idx = 0; 
		while (node.getChildren() != null && idx < node.getChildren().length) {
			if (!removeElementFromTree(node.getChildren()[idx], toRemoveValue)) {
				idx++;
			}
		}
		
		return hasDeletedFlag;
	}
	
	/**
	 * Fügt ein Element (CharElement oder Link) zu einem View hinzu.
	 * Das Element wird mit dieser Methode NICHT zu der Datenbasis hinzugefügt
	 * @param viewer Der view zu dem das Element hinzugefügt werden soll
	 * @param element Das Element welches hinzugefügt werden soll
	 * @param xmlAccessor Der Accessor, zu dem das Element gehört
	 */
	public static void addElementToView(
			RefreshableViewPart viewer, 
			Object element, 
			ObjectCreator objCreator) 
	{
		if (viewer == null) return;
		
		// "Daten-Modelle" holen
		List tabList = (List) ((TableViewContentProvider) viewer.getTableViewer().getContentProvider()).getElementList();
		TreeObject root = ((TreeViewContentProvider) viewer.getTreeViewer().getContentProvider()).getRoot();
		
		// Setze neues Element
		tabList.add(objCreator.createTableObject(element));
		ViewUtils.addElementToTree(root, viewer.getRegulator(), element, objCreator);
	}

	public static void addAndRemoveHerkunftToView(
			RefreshableViewPartImpl viewer, 
			HerkunftVariante element, 
			ObjectCreator objCreator,
			boolean isNew) {
		
		// "Daten-Modelle" holen
		List tabList = (List) ((TableViewContentProvider) viewer.getTableViewer().getContentProvider()).getElementList();
		TreeObject root = ((TreeViewContentProvider) viewer.getTreeViewer().getContentProvider()).getRoot();

		if (isNew) {
			// Zu Tabelle hinzufügen, wenn nicht schon vorhanden
			tabList.add(objCreator.createTableObject(element));
			
			final TreeObject node = findeNode(root, element.getVarianteVon());
			node.addChildren(objCreator.createTreeObject(element, node));
		} else {
			final TreeObject newParent = findeNode(root, element.getVarianteVon());
			final TreeObject node = findeNode(root, element);
			
			if (newParent.equals(node) ) {
				// nix zu tun
				return;
			}
			
			node.getParent().removeChildren(node); // Vom alten Parent entfernen
			newParent.addChildren(node); // Zum neuen hinzufügen
			node.setParent(newParent);
		}
	}
	
	/**
	 * Fügt ein CharElement zu einem Tree hinzu. (Hilfsmethode von addElementToView)
	 * @param invisibleRoot Wurzel des Baumes zum hinzufügen
	 * @param regulator Der Regulator für den entsprechenen View
	 * @param element Das Element (link oder CharElement), welches hinzugefügt werden soll
	 * @param xmlAccessor der zugehörige XmlAccessor
	 */
	private static void addElementToTree(
			TreeObject invisibleRoot, Regulator regulator, 
			Object element, ObjectCreator objCreator) 
	{
		CharElement charElement = ViewUtils.getCharElement(element);
		
		final Object[] firstCat = regulator.getFirstCategory(charElement);
		
		// Alle Kategorien suchen / erzeugen
		List<TreeObject> catList = findChilds(invisibleRoot, firstCat);
		if (catList.size() == 0) {
			catList.add(invisibleRoot);
		}
		
		// Falls Sammelbegriff, diesen in gefundenen Kategorien suchen / erzeugen
		if (charElement.getSammelbegriff() != null) {
			String[] tmpStrArray = new String[] {charElement.getSammelbegriff()};
			List<TreeObject> tmpCatList = new ArrayList<TreeObject>();
			
			for (int i1 = 0; i1 < catList.size(); i1++) {
				List<TreeObject> tmpList = findChilds(catList.get(i1), tmpStrArray);
				
				if (tmpList.size() == 0) {
					// Erzeugen
					for (int i2 = 0; i2 < catList.size(); i2++) {
						TreeObject newChild = new TreeObject(
								charElement.getSammelbegriff(), 
								catList.get(i2));
						newChild.setParent(catList.get(i2));
						catList.get(i2).addChildren(newChild);
						tmpList.add(newChild);
					}
				}
				tmpCatList.addAll(tmpList);
			}
			catList = tmpCatList;
		}
		
		// In jeder Kategorie Element hinzufügen
		for (int i = 0; i < catList.size(); i++) {
			if (charElement instanceof Herkunft) {
				buildHerkunftViewHelper(catList.get(i), (Herkunft) charElement, objCreator);
			} else {
				catList.get(i).addChildren(
						objCreator.createTreeObject(element, catList.get(i)));
			}
		}
	}
	
	/**
	 * Durchsucht die direkten Kinder von "node" nach den Objekten aus "category".
	 * Wird eine "category" nicht gefunden, wird sie angelegt.
	 * @param node Node um die Kinder zu durchsuchen
	 * @param category Zu findene/anzulegende Objekte
	 * @return Liste von TreeObjects, welche die "category"-Objekte enthalten
	 */
	private static List<TreeObject> findChilds(TreeObject node, Object[] category) {
		List<TreeObject> returnList = new ArrayList<TreeObject>();
		
		for (int i2 = 0; i2 < category.length; i2++) {
			if (node.getChildren() == null) continue;
			for (int i1 = 0; i1 < node.getChildren().length; i1++) {
				if (node.getChildren()[i1].getValue().equals(category[i2])) {
					returnList.add(node.getChildren()[i1]);
				}
			}
			if (returnList.size() <= i2) {
				TreeObject newChild = new TreeObject(category[i2], node);
				node.addChildren(newChild);
				returnList.add(newChild);
			}
		}
		
		return returnList;
	}
	
	private static TreeObject findeNode(TreeObject node, Object toFind) {

		if (toFind.equals(node.getValue())) return node;

		 if (node.getChildren() != null) {
			for (int i = 0; i < node.getChildren().length; i++) {
				final TreeObject founded = findeNode(node.getChildren()[i], toFind);
				if (founded != null) return founded;
			}
		}
		return null;
	}
	
	
}
