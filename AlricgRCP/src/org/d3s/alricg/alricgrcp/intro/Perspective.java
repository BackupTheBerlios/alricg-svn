package org.d3s.alricg.alricgrcp.intro;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);

		layout.addView("org.d3s.alricg.editor.views.TalentView",
				IPageLayout.TOP, 1.0f, editorArea);		
	}
}
