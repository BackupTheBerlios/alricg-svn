package org.d3s.alricg.alricgrcp.intro;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "AlricgRCP.perspective";

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }
    
    public void initialize(IWorkbenchConfigurer configurer) {
        super.initialize(configurer);
        configurer.setSaveAndRestore(true);
    }

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#preShutdown()
	 */
	@Override
	public boolean preShutdown() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null) {
			return true;
		}
		
		final boolean b = MessageDialog.openConfirm(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
				"Programm beenden", 
				"Möchten sie Alricg wirklich beenden? " + SWT.CR 
				+ "Alle nicht gespeicherten Daten gehen verloren.");
		
		return b;
	}
	
}
