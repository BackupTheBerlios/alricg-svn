/*
 * Created 26.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import java.util.List;

import org.d3s.alricg.editor.common.widgets.DropTable;
import org.d3s.alricg.store.charElemente.CharElement;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author Vincent
 */
public abstract class AbstractElementPart<C extends CharElement> {

	/**
	 * Wandelt ein Object in einen String um. Dabei wird ein "null" Object zu
	 * einem leer-String umgewandelt
	 * @param obj Object das umgewandelt werden soll
	 * @return String repräsentation des Objects
	 */
	protected String getStringFromNull(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}
	
	/**
	 * Wandelt einen leer-String zu "null" um.
	 * @param str
	 * @return str oder "null" falls str leer ist.
	 */
	protected String getNullFromString(String str) {
		if (str.trim().length() == 0) {
			return null;
		}
		return str.trim();
	}
	
	/**
	 * Vergleicht ob ein Array mit allen Elementen einer DropTable übereinstimmt
	 * @param objArray Das Array
	 * @param dropTable Die Droptable
	 * @return true - Alle Element sind identisch, ansonsten false
	 */
	protected boolean compareArrayList(Object[] objArray, DropTable dropTable) {
		
		List list = dropTable.getValueList();
		if (objArray == null && list.size() == 0) {
			// noop
		} else if (objArray == null) {
			return false;
		} else {
			if (objArray.length != list.size()) return false;
			
			for (int i = 0; i < objArray.length; i++) {
				if ( !objArray[i].equals(list.get(i)) ) return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Lädt alle relevanten Daten von CharElement in den Part
	 * @param charElem CharElement zum laden der Daten
	 */
	public abstract void loadData(C charElem);
	
	/**
	 * Speichert alle relevanten Daten von dem Part in das CharElement
	 * @param charElem CharElement zum Speichern der Daten
	 */
	public abstract void saveData(IProgressMonitor monitor, C charElem);
	
	/**
	 * Dispose-Methode um alle SWT Elemente zu "vernichten";
	 */
	public abstract void dispose();
	
	/**
	 * Prüft, ob die aktuellen Daten von diesem Part sich von dem CharElement
	 * unterscheiden.
	 * @param charElemn Das CharElement zum vergleich 
	 */
	public abstract boolean isDirty(C charElem);
}
