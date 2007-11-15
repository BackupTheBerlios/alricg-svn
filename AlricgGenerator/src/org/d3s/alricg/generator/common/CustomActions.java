/*
 * Created 27.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.common;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.d3s.alricg.common.logic.Prozessor;
import org.d3s.alricg.editor.common.RefreshableViewPart;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.links.Link;
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
	
	/**
	 * Löscht ein Element vom Helden-View
	 */
	public static class DeleteFromView extends Action {
		private final RefreshableViewPart refreshView;
		private final Prozessor prozesor;
		
		public DeleteFromView(RefreshableViewPart refreshView, Prozessor prozesor) {
			this.refreshView = refreshView;
			this.prozesor = prozesor;
		}
		
		@Override
		public void run() {
			final TreeOrTableObject treeTableObj = refreshView.getSelectedElement();
			prozesor.removeElement((Link) treeTableObj.getValue());
		}
	}
	
	public static class AddToView extends Action {
		private final RefreshableViewPart refreshView;
		private final Prozessor prozesor;
		
		public AddToView(RefreshableViewPart refreshView, Prozessor prozesor) {
			this.refreshView = refreshView;
			this.prozesor = prozesor;
		}
		
		@Override
		public void run() {
			final TreeOrTableObject treeTableObj = refreshView.getSelectedElement();
			prozesor.addNewElement((CharElement) treeTableObj.getValue());
		}
	}
	
	public static class InfoCharElementAction extends Action {
		
	}
}
