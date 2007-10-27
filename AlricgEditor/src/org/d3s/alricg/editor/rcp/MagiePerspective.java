/*
 * Created 25.10.2007
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
 * Ansicht mit allen View für die Bearbeitung von Magie/Geweihten.
 * @author Vincent
 */
public class MagiePerspective implements IPerspectiveFactory {
	public static final String ID = "org.d3s.alricg.editor.rcp.MagiePerspective";
	
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
        		editorArea);       bottomFolder.addView(EigenschaftView.ID);
        bottomFolder.addView(ZauberView.ID);
        bottomFolder.addView(SchamanenRitualView.ID);
        bottomFolder.addView(RepraesentationView.ID);
        bottomFolder.addView(MerkmalView.ID);
        bottomFolder.addView(LiturgieView.ID);
        bottomFolder.addView(GoetterView.ID);
        
        layout.addView(
        		FileView.ID, 
        		IPageLayout.LEFT, 
        		StandradPerspective.FILE_VIEW_RATIO, 
        		editorArea);
	}

}
