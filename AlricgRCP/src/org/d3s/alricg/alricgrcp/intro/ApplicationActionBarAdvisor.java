package org.d3s.alricg.alricgrcp.intro;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    public final static String DATEI_ID = "global.datei";
    public final static String BEARBEITEN_ID = "global.bearbeiten";
    public final static String ANSICHTEN_ID = "global.ansichten";
    public final static String HILFE_ID = "global.hilfe";
    
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(IWorkbenchWindow window) {
		IWorkbenchAction action;
		
		// "Datei" - Menu
		action = ActionFactory.SAVE.create(window);
		action.setText("Speichern");
		register(action);
		
		action = ActionFactory.SAVE_ALL.create(window);
		action.setText("Alle Speichern");
		register(action);
		
		action = ActionFactory.INTRO.create(window);
		action.setText("Startseite");
		register(action);
		
		action = ActionFactory.QUIT.create(window);
		action.setText("Beenden");
		register(action);
		
		// "Bearbeiten" - Menu
		action = ActionFactory.CUT.create(window);
		action.setText("Ausschneiden");
		register(action);
		
		action = ActionFactory.COPY.create(window);
		action.setText("Kopieren");
		register(action);
		
		action = ActionFactory.PASTE.create(window);
		action.setText("Einfügen");
		register(action);
		
		action = ActionFactory.DELETE.create(window);
		action.setText("Löschen");
		register(action);
		
		
		// Ansichten
		action = ActionFactory.RESET_PERSPECTIVE.create(window);
		action.setText("Aktuelle Ansicht zurücksetzen");
		register(action);
	
		action = ActionFactory.SAVE_PERSPECTIVE.create(window);
		action.setText("Ansicht speichern unter...");
		register(action);
		
		// Hilfen
		action = ActionFactory.ABOUT.create(window);
		action.setText("Über Alricg");
		register(action);

	}

	protected void fillMenuBar(IMenuManager menuBar) {
		
		MenuManager dateiMenu = new MenuManager("&Datei", this.DATEI_ID);
		MenuManager bearbeitenMenu = new MenuManager("&Bearbeiten", this.BEARBEITEN_ID);
		MenuManager ansichtenMenu = new MenuManager("&Ansichten", this.ANSICHTEN_ID);
		MenuManager hilfeMenu = new MenuManager("&Hilfe", this.HILFE_ID);
		menuBar.add(dateiMenu);
		menuBar.add(bearbeitenMenu);
		menuBar.add(ansichtenMenu);
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(hilfeMenu);

		// "Datei" - Menu
		dateiMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		
		dateiMenu.add(getAction(ActionFactory.SAVE.getId()));
		dateiMenu.add(getAction(ActionFactory.SAVE_ALL.getId()));
		dateiMenu.add(new Separator());
		dateiMenu.add(getAction(ActionFactory.INTRO.getId()));
		dateiMenu.add(getAction(ActionFactory.QUIT.getId()));
		
		
		// "Bearbeiten" - Menu
		bearbeitenMenu.add(getAction(ActionFactory.CUT.getId()));
		bearbeitenMenu.add(getAction(ActionFactory.COPY.getId()));
		bearbeitenMenu.add(getAction(ActionFactory.PASTE.getId()));
		bearbeitenMenu.add(getAction(ActionFactory.DELETE.getId()));
		
		// "Ansichten" - Menu
		ansichtenMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		ansichtenMenu.add(new Separator());
		ansichtenMenu.add(getAction(ActionFactory.RESET_PERSPECTIVE.getId()));
		ansichtenMenu.add(getAction(ActionFactory.SAVE_PERSPECTIVE.getId()));
		
		// "Hilfe" - Menu
		ansichtenMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		hilfeMenu.add(getAction(ActionFactory.ABOUT.getId()));

		
		dateiMenu.addMenuListener(new IMenuListener(){

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				IEditorReference[] editorArray = 
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
				// TODO Auto-generated method stub
				for (IEditorReference e : editorArray) {
					e.getEditor(false).isSaveAsAllowed();
				}
				//System.out.println(e[0].isDirty());
				
				//PlatformUI.getWorkbench().getCommandSupport().getCommandManager()...getActiveWorkbenchWindow().getActivePage().getEditors();
			}});
	}

}
