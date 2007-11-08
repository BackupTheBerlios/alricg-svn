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
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);
		
        IFolderLayout bottomFolder = layout.createFolder(
        		"bottomFolder", 
        		IPageLayout.BOTTOM, 
        		StandradPerspective.BOTTOM_FOLDER_RATIO, 
        		editorArea);
        bottomFolder.addView(org.d3s.alricg.generator.views.general.TalentView.ID);
        bottomFolder.addView(org.d3s.alricg.generator.views.held.TalentView.ID);
	}

}
