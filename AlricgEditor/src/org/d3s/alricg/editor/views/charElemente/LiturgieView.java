/*
 * Created 21.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.views.charElemente;

import org.d3s.alricg.editor.utils.Regulatoren;
import org.d3s.alricg.editor.utils.Regulatoren.Regulator;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Vincent
 *
 */
public class LiturgieView extends RefreshableViewPart {

	/**
	 * 
	 */
	public LiturgieView() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.views.charElemente.RefreshableViewPart#createTable(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected TableViewer createTable(Composite parent) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.views.charElemente.RefreshableViewPart#createTree(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected TreeViewer createTree(Composite parent) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.views.charElemente.RefreshableViewPart#getRegulator()
	 */
	@Override
	public Regulator getRegulator() {
		return Regulatoren.LiturgieRegulator;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.views.charElemente.RefreshableViewPart#getViewedClass()
	 */
	@Override
	public Class getViewedClass() {
		return Liturgie.class;
	}

}
