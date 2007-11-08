/*
 * Created 27.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.common;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Vincent
 *
 */
public class CustomActions {
	/**
	 * Wechselt die Ansicht zwischen dem TreeViewer und dem TableViewer
	 * @author Vincent
	 */
	public static class SwapTreeTableAction extends Action {
		private Composite parentComp;
		
		public SwapTreeTableAction(Composite parentComp) {
			super("Ansicht Wechseln");
			
			this.parentComp = parentComp;
			this.setImageDescriptor(ControlIconsLibrary.swapTree_Table.getImageDescriptor());
			this.setToolTipText("Wechselt die Ansicht zwischen Tabelle und Baum");
		}
		
		@Override
		public void run() {
			final Control topControl = ((StackLayout) parentComp.getLayout()).topControl;
			
			if ( topControl.equals(parentComp.getChildren()[0]) ) {
				((StackLayout) parentComp.getLayout()).topControl = parentComp.getChildren()[1];
			} else {
				((StackLayout) parentComp.getLayout()).topControl = parentComp.getChildren()[0];
			}
			parentComp.layout();
		}
	}
}
