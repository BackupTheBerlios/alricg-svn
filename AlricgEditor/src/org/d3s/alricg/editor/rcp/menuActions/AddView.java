/*
 * Created 25.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.rcp.menuActions;

import java.util.logging.Level;

import org.d3s.alricg.editor.Activator;
import org.d3s.alricg.editor.views.FileView;
import org.d3s.alricg.editor.views.charElemente.EigenschaftView;
import org.d3s.alricg.editor.views.charElemente.GegenstandView;
import org.d3s.alricg.editor.views.charElemente.GoetterView;
import org.d3s.alricg.editor.views.charElemente.KulturView;
import org.d3s.alricg.editor.views.charElemente.LiturgieView;
import org.d3s.alricg.editor.views.charElemente.MerkmalView;
import org.d3s.alricg.editor.views.charElemente.NachteilView;
import org.d3s.alricg.editor.views.charElemente.ProfessionView;
import org.d3s.alricg.editor.views.charElemente.RasseView;
import org.d3s.alricg.editor.views.charElemente.RegionVolkView;
import org.d3s.alricg.editor.views.charElemente.RepraesentationView;
import org.d3s.alricg.editor.views.charElemente.SchamanenRitualView;
import org.d3s.alricg.editor.views.charElemente.SonderfertigkeitView;
import org.d3s.alricg.editor.views.charElemente.SpracheView;
import org.d3s.alricg.editor.views.charElemente.TalentView;
import org.d3s.alricg.editor.views.charElemente.VorteilView;
import org.d3s.alricg.editor.views.charElemente.ZauberView;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * @author Vincent
 *
 */
public class AddView implements IWorkbenchWindowActionDelegate {
	
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

		 try {
			 if (action.getId().equals("alricgEditor.action.Dateien")) {
				window.getActivePage().showView(FileView.ID);
			 } else if (action.getId().equals("alricgEditor.action.Navigation")) {
				 // TODO implement
				 
			 } else if (action.getId().equals("alricgEditor.action.Region")) {
				 window.getActivePage().showView(RegionVolkView.ID);
			 } else if (action.getId().equals("alricgEditor.action.Profession")) {
				 window.getActivePage().showView(ProfessionView.ID);
			 } else if (action.getId().equals("alricgEditor.action.Kultur")) {
				 window.getActivePage().showView(KulturView.ID);
			 } else if (action.getId().equals("alricgEditor.action.Rasse")) {
				 window.getActivePage().showView(RasseView.ID);
			 } else if (action.getId().equals("alricgEditor.action.Goetter")) {
				 window.getActivePage().showView(GoetterView.ID);
			 } else if (action.getId().equals("alricgEditor.action.Merkmale")) {
				 window.getActivePage().showView(MerkmalView.ID);
			 } else if (action.getId().equals("alricgEditor.action.Repraesentation")) {
				 window.getActivePage().showView(RepraesentationView.ID);
			 } else if (action.getId().equals("alricgEditor.action.SchamanenRituale")) {
				 window.getActivePage().showView(SchamanenRitualView.ID);
			 } else if (action.getId().equals("alricgEditor.action.Liturgien")) {
				 window.getActivePage().showView(LiturgieView.ID);
			 } else if (action.getId().equals("alricgEditor.action.Zauber")) {
				 window.getActivePage().showView(ZauberView.ID);
			 } else if (action.getId().equals("alricgEditor.action.Gegenstand")) {
				 window.getActivePage().showView(GegenstandView.ID);
			 } else if (action.getId().equals("alricgEditor.action.Sprache")) {
				 window.getActivePage().showView(SpracheView.ID);
			 } else if (action.getId().equals("alricgEditor.action.Nachteile")) {
				 window.getActivePage().showView(NachteilView.ID);
			 } else if (action.getId().equals("alricgEditor.action.Vorteile")) {
				 window.getActivePage().showView(VorteilView.ID);
			 } else if (action.getId().equals("alricgEditor.action.Sonderfertigkeiten")) {
				 window.getActivePage().showView(SonderfertigkeitView.ID);
			 } else if (action.getId().equals("alricgEditor.action.Talente")) {
				 window.getActivePage().showView(TalentView.ID);
			 } else if (action.getId().equals("alricgEditor.action.Eigenschaften")) {
				 window.getActivePage().showView(EigenschaftView.ID);
			 }
			 
		} catch (PartInitException e) {
			Activator.logger.log(Level.SEVERE,"msg", e);
			e.printStackTrace();
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
