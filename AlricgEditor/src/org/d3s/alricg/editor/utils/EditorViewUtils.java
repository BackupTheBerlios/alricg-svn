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

import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.common.Regulatoren.Regulator;
import org.d3s.alricg.editor.common.ViewUtils.TableObject;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.views.charElemente.RefreshableViewPart;
import org.d3s.alricg.store.access.CharElementFactory;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.access.CharElementFactory.DependencyTableObject;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.HerkunftVariante;
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
	 * Spezielles TreeObject für den Editor, um neben dem Value noch einen Text und Index 
	 * angeben zu können. Wird von den Trees in "AuswahlPart", "IdLinkPart" und 
	 * "VoraussetzungsPart" benötigt.
	 * @author Vincent
	 */
	public static class AuswahlTreeObject extends TreeObject implements TreeOrTableObject {
		private final String text;
		private final int index;
		
		/**
		 * @param value Inhalt des Knotens
		 * @param parent Parent-Knoten
		 * @param text Text auf dem Knoten
		 * @param index Angabe für das Modell
		 */
		public AuswahlTreeObject(Object value, TreeObject parent, String text, int index) {
			super(value, parent);
			this.text = text;
			this.index = index;
		}

		/**
		 * @return Der Text der auf den Knoten angezeigt werden soll
		 */
		public String getText() {
			return text;
		}
		
		/**
		 * @return Identifikator um die Verbindungen zum Model zu erleichtern 
		 */
		public int getIndex() {
			return index;
		}
	}
	
	/**
	 * Spezielles TableObject für den Editor, um noch einen FilePath angeben zu können
	 * @author Vincent
	 */
	public static class EditorTableObject extends TableObject implements EditorTreeOrTableObject {
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
	 * ProgressMonitro um beim Löschen anzuzeigen wie weit die Überprüfung der 
	 * "Dependencies" ist. 
	 * @author Vincent
	 */
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
	public static EditorTreeObject buildEditorTreeView(List<XmlAccessor> accessorList, Regulator regulator) {
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
						buildHerkunftViewHelper(parent, (Herkunft) charElementList.get(i2), accessorList.get(i1));
					} else {
						parent.addChildren(new EditorTreeObject(
								charElementList.get(i2), 
								parent,
								accessorList.get(i1)));
					}
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
	public static List<? extends TableObject> buildEditorTableView(
			List<XmlAccessor> accessorList, 
			Regulator regulator) 
	{
		List<EditorTableObject> returnList = new ArrayList<EditorTableObject>();
		
		for (int i1 = 0; i1 < accessorList.size(); i1++) {
			List<? extends CharElement> charElementList = regulator.getListFromAccessor(accessorList.get(i1));
			if (charElementList == null) continue;
			
			for (int i2 = 0; i2 < charElementList.size(); i2++) {
				if (charElementList.get(i2) instanceof Herkunft) {
					buildHerkunftTableViewHelper(
							returnList, 
							(Herkunft) charElementList.get(i2), 
							accessorList.get(i1));
				} else {
					returnList.add(
						new EditorTableObject(
								charElementList.get(i2), 
								accessorList.get(i1))
						);
				}
			}
		}
		return returnList;
	}
	
	/**
	 * Erstellt rekursiv für alle Varianten der Herkunft "herk" einträge in "returnList"
	 * @param returnList Liste mit allen EditorTableObjects die erzeugt wurden
	 * @param herk Herkunft für die ein EditorTableObjects erzeugt wird
	 * @param accessor Aktueller accesssor
	 */
	private static void buildHerkunftTableViewHelper(List<EditorTableObject> returnList, Herkunft herk, XmlAccessor accessor) {
		returnList.add(
				new EditorTableObject(
						herk, 
						accessor)
				);		
		if (herk.getVarianten() == null) return;
		for (int i = 0; i < herk.getVarianten().length; i++) {
			buildHerkunftTableViewHelper(returnList, (Herkunft) herk.getVarianten()[i], accessor);
		}
	}
	
	/**
	 * Erstellt rekursiv für alle Varianten der Herkunft "herk" neue Nodes
	 * @param node Der aktuelle Node, an dem das neue TreeObject "angehangen" wird
	 * @param herk Herkunft für die ein neues TreeObject erstellt wird
	 * @param accessor Aktueller accesssor
	 */
	private static void buildHerkunftViewHelper(TreeObject node, Herkunft herk, XmlAccessor accessor) {
		TreeObject newNode = new EditorTreeObject(herk, node, accessor);
		node.addChildren(newNode);
		newNode.setParent(node);
			
		if (herk.getVarianten() == null) return;
		for (int i = 0; i < herk.getVarianten().length; i++) {
			buildHerkunftViewHelper(newNode, (Herkunft) herk.getVarianten()[i], accessor);
		}
	}
}
