/*
 * Created 24.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common;

import java.io.File;

import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTableObject;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTreeObject;
import org.d3s.alricg.store.charElemente.CharElement;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * @author Vincent
 *
 */
public class CustomFilter {
	
	/**
	 * Liefert zu einem TreeObject/TableObject mit CharElement oder Link,
	 * oder einem  CharElement oder Link direkt das zugehörige CharElement. 
	 * Gibt es KEIN zugehöriges CharElement, so wird "null" zurückgeliefert.
	 * 
	 * @param element TreeObject/TableObject/CharElement oder Link
	 * @return Das zugehörige CharElement oder "null" wenn keines existiert
	 */
	private static CharElement getCharElement(Object element) {
		return ViewUtils.getCharElement(element);
	}
	
	public static class CurrentFileFilter extends ViewerFilter {
		private File modelFile;

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			
			if (modelFile == null) return true;
			
			File tmpFile;
			if (element instanceof EditorTreeObject) {
				tmpFile = ((EditorTreeObject)element).getFile();
			} else if (element instanceof EditorTableObject) {
				tmpFile = ((EditorTableObject)element).getFile();
			} else {
				return true;
			}
			
			// ansonsten ist der Ordner ".../bla" = dem File ".../bla.xml"
			String compStr = modelFile.getAbsolutePath();
			if (modelFile.isDirectory()) { 
				compStr += File.separator;
			}
			return tmpFile.getAbsolutePath().startsWith(compStr);
		}

		public void setModelFile(File modelFile) {
			this.modelFile = modelFile;
		}
	}
}
