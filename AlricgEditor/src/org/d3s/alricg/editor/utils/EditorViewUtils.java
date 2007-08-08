/*
 * Created 21.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.d3s.alricg.editor.common.ViewUtils.TableObject;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.utils.Regulatoren.Regulator;
import org.d3s.alricg.editor.views.charElemente.RefreshableViewPart;
import org.d3s.alricg.store.access.CharElementFactory;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.access.CharElementFactory.DependencyTableObject;
import org.d3s.alricg.store.charElemente.CharElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * 
 * @author Vincent
 */
public class EditorViewUtils {

	/**
	 * Interface für die generalisierung von EditorTreeObject und EditorTableObject
	 * @author Vincent
	 */
	public static interface EditorTreeOrTableObject extends TreeOrTableObject {
		
		/**
		 * @return File in dem das Element gespeichert ist
		 */
		public File getFile();
		
		/**
		 * @return Der Accessor, in dem das Element gespeichert ist
		 */
		public XmlAccessor getAccessor();
	}
	
	/**
	 * Spezielles TreeObject für den Editor, um noch einen FilePath angeben zu können
	 * @author Vincent
	 */
	public static class EditorTreeObject extends TreeObject implements EditorTreeOrTableObject {
		private XmlAccessor accessor;
		
		public EditorTreeObject(Object value, TreeObject parent, XmlAccessor accessor) {
			super(value, parent);
			this.accessor = accessor;
		}

		public File getFile() {
			return accessor.getFile();
		}

		public XmlAccessor getAccessor() {
			return accessor;
		}
	}
	
	/**
	 * Spezielles TableObject für den Editor, um noch einen FilePath angeben zu können
	 * @author Vincent
	 */
	public static class EditorTableObject  extends TableObject implements EditorTreeOrTableObject {
		private XmlAccessor accessor;
		
		public EditorTableObject(Object value, XmlAccessor accessor) {
			super(value);
			this.accessor = accessor;
		}

		public File getFile() {
			return accessor.getFile();
		}
		
		public XmlAccessor getAccessor() {
			return accessor;
		}
	}
	
	public static class DependencyProgressMonitor implements IRunnableWithProgress {
		private IProgressMonitor monitor;
		private List<DependencyTableObject> depList;
		final private CharElement charElement;
		
		public DependencyProgressMonitor(CharElement charElement) {
			this.charElement = charElement;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void run(IProgressMonitor monitor)
				throws InvocationTargetException,
				InterruptedException {
			this.monitor = monitor;
			depList = CharElementFactory.getInstance().checkDependencies(
							charElement, 
			    			monitor);
		}

		public IProgressMonitor getMonitor() {
			return monitor;
		}

		public List<DependencyTableObject> getDepList() {
			return depList;
		}
	};
	
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
	 */
	public static EditorTreeObject buildEditorViewTree(List<XmlAccessor> accessorList, Regulator regulator) {
		// init HashMap und Root
		final HashMap<String, TreeObject> map = new HashMap<String, TreeObject>();
		final EditorTreeObject invisibleRoot = new EditorTreeObject("invisibleRoot", null, null);
		invisibleRoot.setChildren(new ArrayList()); // um eine "asserNull" exception zu verhindern, 
													// wenn ohne Elemente gestaret
		// Alle Elemente durchlaufen
		for (int i1 = 0; i1 < accessorList.size(); i1++) {
			List<? extends CharElement> charElementList = regulator.getListFromAccessor(accessorList.get(i1));
			if (charElementList == null) continue;
			
			for (int i2 = 0; i2 < charElementList.size(); i2++) {
				
				final CharElement charElement = charElementList.get(i2);
				final Object[] firstCat = regulator.getFirstCategory(charElement);
				
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
					
					// Element
					parent.addChildren(new EditorTreeObject(
							charElementList.get(i2), 
							parent,
							accessorList.get(i1)));
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
	 * @return Root Liste mit allen Elementen für die Tabelle
	 */
	public static List<? extends TableObject> buildTableView(
			List<XmlAccessor> accessorList, 
			Regulator regulator) 
	{
		List<EditorTableObject> returnList = new ArrayList<EditorTableObject>();
		
		for (int i1 = 0; i1 < accessorList.size(); i1++) {
			List<? extends CharElement> charElementList = regulator.getListFromAccessor(accessorList.get(i1));
			if (charElementList == null) continue;
			
			for (int i2 = 0; i2 < charElementList.size(); i2++) {
				
				returnList.add(
						new EditorTableObject(
								charElementList.get(i2), 
								accessorList.get(i1))
						);
			}
		}
		return returnList;
	}
	
	public static void removeElementFromView(
			RefreshableViewPart viewer, 
			CharElement charElem)
	{
		// "Daten-Modelle" holen
		List tabList = (List) ((TableViewContentProvider) viewer.getTableViewer().getContentProvider()).getElementList();
		TreeObject root = ((TreeViewContentProvider) viewer.getTreeViewer().getContentProvider()).getRoot();
		
		// Aus Table entfernen
		for (int i = 0; i < tabList.size(); i++) {
			if ( ((TableObject) tabList.get(i)).getValue().equals(charElem) ) {
				tabList.remove(i);
				break;
			}
		}
		
		// Aus Tree entfernen
		EditorViewUtils.removeElementFromTree(root, charElem);
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
		
		if (node.getValue().equals(toRemoveValue) && node.getChildren() == null) {
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
	
	public static void addElementToView(
			RefreshableViewPart viewer, 
			CharElement newCharElem, 
			XmlAccessor xmlAccessor) 
	{
		// "Daten-Modelle" holen
		List tabList = (List) ((TableViewContentProvider) viewer.getTableViewer().getContentProvider()).getElementList();
		TreeObject root = ((TreeViewContentProvider) viewer.getTreeViewer().getContentProvider()).getRoot();
		
		// Setze neues Element und aktualisiere
		tabList.add(new EditorTableObject(newCharElem, xmlAccessor));
		EditorViewUtils.addElementToTree(root, viewer.getRegulator(), newCharElem, xmlAccessor);
	}

	private static void addElementToTree(
			TreeObject invisibleRoot, Regulator regulator, 
			CharElement charElement, XmlAccessor xmlAccessor)
	{
		final Object[] firstCat = regulator.getFirstCategory(charElement);
		
		// Alle Kategorien suchen / erzeugen
		List<TreeObject> catList = findChilds(invisibleRoot, firstCat);
		
		// Falls Sammelbegriff, diesen in gefundenen Kategorien suchen / erzeugen
		if (charElement.getSammelbegriff() != null) {
			String[] tmpStrArray = new String[] {charElement.getSammelbegriff()};
			List<TreeObject> tmpCatList = new ArrayList<TreeObject>();
			
			for (int i1 = 0; i1 < catList.size(); i1++) {
				tmpCatList.addAll(findChilds(catList.get(i1), tmpStrArray));
			}
			catList = tmpCatList;
		}
		
		// In jeder Kategorie Element hinzufügen
		for (int i = 0; i < catList.size(); i++) {
			catList.get(i).addChildren(new EditorTreeObject(
					charElement, 
					catList.get(i),
					xmlAccessor)
			);
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
}
