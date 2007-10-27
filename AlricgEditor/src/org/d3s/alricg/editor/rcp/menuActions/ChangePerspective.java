/*
 * Created 25.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.rcp.menuActions;

import org.d3s.alricg.editor.rcp.HerkunftPerspective;
import org.d3s.alricg.editor.rcp.KomplettPerspective;
import org.d3s.alricg.editor.rcp.MagiePerspective;
import org.d3s.alricg.editor.rcp.StandradPerspective;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * Verwaltet die verfügbaren Ansichten für den Editor.
 * @author Vincent
 */
public class ChangePerspective implements IWorkbenchWindowActionDelegate {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	@Override
	public void dispose() {
		// Noop
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	@Override
	public void init(IWorkbenchWindow window) {
		// Noop
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action) {
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    final IPerspectiveRegistry reg = PlatformUI.getWorkbench().getPerspectiveRegistry();
	    
	    if (action.getId().equals("alricgEditor.StandardAnsicht")) {
	        window.getActivePage().setPerspective(
	        		reg.findPerspectiveWithId(StandradPerspective.ID)
	        	);
	    } else if (action.getId().equals("alricgEditor.MagieAnsicht")) {
	        window.getActivePage().setPerspective(
	        		reg.findPerspectiveWithId(MagiePerspective.ID)
	        	);
		} else if (action.getId().equals("alricgEditor.HerkunftAnsicht")) {
	        window.getActivePage().setPerspective(
	        		reg.findPerspectiveWithId(HerkunftPerspective.ID)
	        	);
	    } else if (action.getId().equals("alricgEditor.KomplettAnsicht")) {
	        window.getActivePage().setPerspective(
	        		reg.findPerspectiveWithId(KomplettPerspective.ID)
	        	);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// Noop
	}

}
