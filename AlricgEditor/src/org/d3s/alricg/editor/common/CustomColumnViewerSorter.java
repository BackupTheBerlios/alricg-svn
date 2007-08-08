/*
 * Created 22.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common;

import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTableObject;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTreeObject;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Faehigkeit;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.links.Option;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

/**
 * @author Vincent
 *
 */
public class CustomColumnViewerSorter {
	
	/**
	 * Liefert zu einem TreeObject/TableObject mit CharElement oder Link,
	 * oder einem  CharElement oder Link direkt das zugehörige CharElement. 
	 * Gibt es KEIN zugehöriges CharElement, so wird "null" zurückgeliefert.
	 * 
	 * @param element TreeObject/TableObject/CharElement oder Link
	 * @return Das zugehörige CharElement oder "null" wenn keines existiert
	 */
	private static CharElement getCharElement(Object element) {
		return CustomColumnLabelProvider.getCharElement(element);
	}
	
	private static int getMultiplikator(Viewer viewer) {
		
		if (viewer instanceof TreeViewer) {
			if ( ((TreeViewer) viewer).getTree().getSortDirection() == SWT.DOWN) {
				return -1;
			}
		} else if (viewer instanceof TableViewer) {
			if ( ((TableViewer) viewer).getTable().getSortDirection() == SWT.DOWN) {
				return -1;
			}
		}
		
		return 1;
	}
	
	public static abstract class CreatableViewerSorter extends ViewerSorter {
		public abstract ViewerSorter getNewInstance();
	}
	
	public static class NameSorter extends CreatableViewerSorter {

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (getCharElement(e1) == null || getCharElement(e2) == null) {
				return e1.toString().compareTo(e2.toString()) * getMultiplikator(viewer);
			}
			
			return getCharElement(e1).getName()
						.compareTo(getCharElement(e2).getName())
						* getMultiplikator(viewer);
		}
		
		@Override
		public ViewerSorter getNewInstance() {
			return new NameSorter();
		}
	}
	
	public static class OptionNameSorter extends NameSorter {

		@Override
		public int category(Object element) {
			if (((TreeObject) element).getValue() instanceof Option) {
				return 1;
			}
			return 2;
		}
		
		
	}
	
	public static class SktSorter extends CreatableViewerSorter {

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (getCharElement(e1) == null || getCharElement(e2) == null) {
				return e1.toString().compareTo(e2.toString());
			}
			
			return ((Faehigkeit) getCharElement(e1)).getKostenKlasse().toString()
						.compareTo(((Faehigkeit) getCharElement(e2)).getKostenKlasse().toString())
						* getMultiplikator(viewer);
		}
		
		@Override
		public ViewerSorter getNewInstance() {
			return new SktSorter();
		}
	}
	
	public static class ArtSorter extends CreatableViewerSorter {

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (getCharElement(e1) == null || getCharElement(e2) == null) {
				return e1.toString().compareTo(e2.toString());
			}
			
			return ((Talent) getCharElement(e1)).getArt().toString()
						.compareTo(((Talent) getCharElement(e2)).getArt().toString())
						* getMultiplikator(viewer);
		}
		
		@Override
		public ViewerSorter getNewInstance() {
			return new ArtSorter();
		}
	}
	
	public static class SorteSorter extends CreatableViewerSorter {

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (getCharElement(e1) == null || getCharElement(e2) == null) {
				return e1.toString().compareTo(e2.toString());
			}
			
			return ((Talent) getCharElement(e1)).getSorte().toString()
						.compareTo(((Talent) getCharElement(e2)).getSorte().toString())
						* getMultiplikator(viewer);
		}
		
		@Override
		public ViewerSorter getNewInstance() {
			return new SorteSorter();
		}
	}
	
	public static class DateiSorter extends CreatableViewerSorter {

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			String s1, s2;
			
			if (e1 instanceof EditorTreeObject) {
				s1 = ((EditorTreeObject) e1).getFile().getName();
				s2 = ((EditorTreeObject) e2).getFile().getName();
			} else if(e1 instanceof EditorTableObject) {
				s1 = ((EditorTableObject) e1).getFile().getName();
				s2 = ((EditorTableObject) e2).getFile().getName();
			} else {
				return e1.toString().compareTo(e2.toString());
			}
			
			return s1.compareTo(s2) * getMultiplikator(viewer);
		}

		@Override
		public ViewerSorter getNewInstance() {
			return new DateiSorter();
		}
		
	}
	

}
