/*
 * Created 20.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.rcp;

import org.d3s.alricg.editor.views.FileView;
import org.d3s.alricg.editor.views.charElemente.*;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * Standard Ansicht mit den wichtigens Views.
 * @author Vincent
 */
public class StandradPerspective implements IPerspectiveFactory {
	public static final String ID = "org.d3s.alricg.editor.rcp.StandradPerspective";
	public static final float FILE_VIEW_RATIO = 0.2f;
	public static final float BOTTOM_FOLDER_RATIO = 0.7f;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);
		
        IFolderLayout bottomFolder = layout.createFolder(
        		"bottomFolder", 
        		IPageLayout.BOTTOM, 
        		StandradPerspective.BOTTOM_FOLDER_RATIO, 
        		editorArea);
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

        layout.addView(
        		FileView.ID, 
        		IPageLayout.LEFT, 
        		StandradPerspective.FILE_VIEW_RATIO, 
        		editorArea);
	}
	
	

}
