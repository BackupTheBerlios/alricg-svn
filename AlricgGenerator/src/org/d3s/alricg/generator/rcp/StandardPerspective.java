/*
 * Created 28.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.rcp;

import org.d3s.alricg.editor.rcp.StandradPerspective;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * @author Vincent
 *
 */
public class StandardPerspective implements IPerspectiveFactory {
	public final static String ID = "org.d3s.alricg.generator.rcp.StandardPerspective";
	public static final float FILE_VIEW_RATIO = 0.2f;
	public static final float BOTTOM_FOLDER_RATIO = 0.7f;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(false);
		
        IFolderLayout bottomFolder = layout.createFolder(
        		"bottomFolder", 
        		IPageLayout.BOTTOM, 
        		StandradPerspective.BOTTOM_FOLDER_RATIO, 
        		editorArea);
        IFolderLayout topFolder = layout.createFolder( 
        		"topFolder", 
        		IPageLayout.TOP, 
        		StandradPerspective.BOTTOM_FOLDER_RATIO, 
        		editorArea );
        
        
        bottomFolder.addView(org.d3s.alricg.generator.views.general.TalentView.ID);
        bottomFolder.addView(org.d3s.alricg.generator.views.general.NachteilView.ID);
        bottomFolder.addView(org.d3s.alricg.generator.views.general.VorteilView.ID);
        bottomFolder.addView(org.d3s.alricg.generator.views.general.SonderfertigkeitView.ID);
        bottomFolder.addView(org.d3s.alricg.generator.views.general.ZauberView.ID);
        bottomFolder.addView(org.d3s.alricg.generator.views.general.LiturgieView.ID);
        bottomFolder.addView(org.d3s.alricg.generator.views.general.SchamRitualView.ID);
        
        //bottomFolder.addView(org.d3s.alricg.generator.views.general.VorteilView.ID);
        bottomFolder.addView(org.d3s.alricg.generator.views.general.RassenView.ID);
        bottomFolder.addView(org.d3s.alricg.generator.views.general.KulturView.ID);
        bottomFolder.addView(org.d3s.alricg.generator.views.general.ProfessionView.ID);
        
        topFolder.addView(org.d3s.alricg.generator.views.held.TalentView.ID);
        topFolder.addView(org.d3s.alricg.generator.views.held.HerkunftView.ID);
        topFolder.addView(org.d3s.alricg.generator.views.held.EigenschaftenView.ID);
        //bottomFolder.addView(org.d3s.alricg.generator.views.held.HerkunftView.ID);
	}

}
