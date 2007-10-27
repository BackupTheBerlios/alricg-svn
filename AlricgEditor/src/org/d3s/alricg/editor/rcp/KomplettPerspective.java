/*
 * Created 25.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.rcp;

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
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * Ansicht mit allen Views.
 * @author Vincent
 */
public class KomplettPerspective implements IPerspectiveFactory {
	public static final String ID = "org.d3s.alricg.editor.rcp.KomplettPerspective";
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);
		
        IFolderLayout bottomFolder = layout.createFolder("bottomFolder", IPageLayout.BOTTOM, 0.55f, editorArea);
        bottomFolder.addView(EigenschaftView.ID);
        bottomFolder.addView(TalentView.ID);
        bottomFolder.addView(SonderfertigkeitView.ID);
        bottomFolder.addView(VorteilView.ID);
        bottomFolder.addView(NachteilView.ID);
        bottomFolder.addView(ZauberView.ID);
        bottomFolder.addView(SchamanenRitualView.ID);
        bottomFolder.addView(LiturgieView.ID);
        bottomFolder.addView(SpracheView.ID);
        bottomFolder.addView(GegenstandView.ID);

        IFolderLayout bottomBottomFolder = layout.createFolder("bottomBottomFolder", IPageLayout.BOTTOM, 0.5f, "bottomFolder");
        bottomBottomFolder.addView(RasseView.ID);
        bottomBottomFolder.addView(ProfessionView.ID);
        bottomBottomFolder.addView(KulturView.ID);
        bottomBottomFolder.addView(RegionVolkView.ID);
        bottomBottomFolder.addView(RepraesentationView.ID);        
        bottomBottomFolder.addView(MerkmalView.ID);
        bottomBottomFolder.addView(GoetterView.ID);
        
        layout.addView(
        		FileView.ID, 
        		IPageLayout.LEFT, 
        		StandradPerspective.FILE_VIEW_RATIO, 
        		editorArea);
	}

}
