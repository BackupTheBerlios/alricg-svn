package org.d3s.alricg.alricgrcp.intro;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroPart;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(700, 550));
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
        configurer.setTitle("Alricg");
    }
    
	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#openIntro()
	 */
	@Override
	public void openIntro() {
		// TODO dieses "true" ersetzen durch eine vom User zu setzenen Wert 
		if (true) {
			final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	        final IPerspectiveRegistry reg = PlatformUI.getWorkbench().getPerspectiveRegistry();
	        
	        window.getActivePage().closeAllPerspectives(true, false);
	        window.getActivePage().setPerspective(
	        		reg.findPerspectiveWithId(reg.getDefaultPerspective())
	        	);

			
			final IIntroManager mgr = PlatformUI.getWorkbench().getIntroManager();
			IIntroPart iPart = mgr.showIntro(null, false);
		} else {
			super.openIntro();
		}
	}
}
