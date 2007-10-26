/*
 * Created 18.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.alricgrcp.intro;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroManager;

/**
 * @author Vincent
 */
public class StartEditorAction extends Action {
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		final IIntroManager mgr = PlatformUI.getWorkbench().getIntroManager();
		mgr.closeIntro(mgr.getIntro());
		
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        final IPerspectiveRegistry reg = PlatformUI.getWorkbench().getPerspectiveRegistry();
        
        if (window.getActivePage().getPerspective() != null) {
        	window.getActivePage().closePerspective(
        			window.getActivePage().getPerspective(), false, false);
        }
        window.getActivePage().setPerspective(
        		reg.findPerspectiveWithId("org.d3s.alricg.editor.rcp.StandradPerspective")
        	);
	}
}
