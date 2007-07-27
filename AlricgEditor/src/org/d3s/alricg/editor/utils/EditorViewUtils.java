/*
 * Created 21.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.d3s.alricg.editor.common.ViewUtils.Regulator;
import org.d3s.alricg.editor.common.ViewUtils.TableObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;

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
	
}
