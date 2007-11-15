/*
 * Created 31.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common;

import org.d3s.alricg.editor.common.Regulatoren.Regulator;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Control;

/**
 * @author Vincent
 *
 */
public interface RefreshableViewPart {
	public abstract Regulator getRegulator();
	
	public abstract Class getViewedClass();
	
	/**
	 * Refresht den Tree und die Tabelle
	 */
	public void refresh();
	
	public TableViewer getTableViewer();
	
	public TreeViewer getTreeViewer();
	
	/**
	 * @return Liefert das gerade selektierte Object vom Tree oder Table
	 */
	public TreeOrTableObject getSelectedElement();
	
	/**
	 * @return Liefert das gerade selektierte Object vom Tree oder Table
	 */
	public Control getTopControl();
}
