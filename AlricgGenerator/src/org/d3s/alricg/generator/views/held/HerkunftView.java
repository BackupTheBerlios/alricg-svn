package org.d3s.alricg.generator.views.held;

import java.util.List;

import org.d3s.alricg.common.CommonUtils;
import org.d3s.alricg.common.logic.BaseProzessorObserver;
import org.d3s.alricg.common.logic.Prozessor;
import org.d3s.alricg.common.logic.ProzessorObserver;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.generator.Activator;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorHerkunft;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.charElemente.spezial.AbgebrochenProf;
import org.d3s.alricg.store.charElemente.spezial.BreitgefaechertProf;
import org.d3s.alricg.store.charElemente.spezial.VeteranProf;
import org.d3s.alricg.store.charElemente.spezial.ZweiWeltenKultur;
import org.d3s.alricg.store.charElemente.spezial.ZweiWeltenRasse;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

public class HerkunftView extends ViewPart implements ProzessorObserver {
	public static final String ID = "org.d3s.alricg.generator.views.held.HerkunftView"; //$NON-NLS-1$

	private Prozessor herkunftProzessor;
	private Prozessor vorteilProzessor;
	
	private Button cbxRasseKzW;
	private Button cbxKulturKzW;
	private Button cbxProfessionAbAus;
	private Button cbxProfessionVeteran;
	private Button cbxProfessionBreitgeBildung;
	
	private Label lblRasseDrop;
	private Label lblRasseKzW;
	private Label lblRasseGP;

	private Label lblKulturDrop;
	private Label lblKulturKzW;
	private Label lblKulturGP;
	
	private Label lblProfessionDrop;
	private Label lblProfessionVeteran;
	private Label lblProfessionBreitgeBildung;
	private Label lblProfessionGP;
	
	public HerkunftView() {
		BaseProzessorObserver prozessor = (BaseProzessorObserver) Activator.getCurrentCharakter().getProzessor(Vorteil.class);
		prozessor.registerObserver(this);
		
		herkunftProzessor = (Prozessor) Activator.getCurrentCharakter().getProzessor(Herkunft.class);
		((BaseProzessorObserver) herkunftProzessor).registerObserver(this);
		vorteilProzessor = (Prozessor) Activator.getCurrentCharakter().getProzessor(Vorteil.class);
		((BaseProzessorObserver) vorteilProzessor).registerObserver(this);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		// Scroll-Container
		final ScrolledComposite scrollComp = new ScrolledComposite(parent, SWT.V_SCROLL);
		scrollComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrollComp.setExpandHorizontal(true);
		scrollComp.setExpandVertical(true);
		
		// Container erzeugen, Grid mit 2 Spalten setzen, Scroll-Container setzen
		final Composite mainContainer = new Composite (scrollComp, SWT.NONE);
		scrollComp.setContent(mainContainer);
		
		final Group groupRasse;
		final Group groupKultur;
		final Group groupProfession;
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.horizontalSpacing = 10;
		
		mainContainer.setLayout(gridLayout);
	
	// Erzeuge Gruppe "Rasse"
		groupRasse = new Group(mainContainer, SWT.SHADOW_IN);
		groupRasse.setText("Rasse");
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		GridData tmpGridData = new GridData(GridData.GRAB_HORIZONTAL);
		tmpGridData.widthHint = 500;
		groupRasse.setLayout(gridLayout);
		groupRasse.setLayoutData(tmpGridData);
		
		Label lblRasseName = new Label(groupRasse, SWT.NONE);
		lblRasseName.setText("Rasse:");
		//lblRasseName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		
		lblRasseDrop = new Label(groupRasse, SWT.BORDER);
		lblRasseDrop.setText(" Per Drag and Drop wählen ");
		lblRasseDrop.setSize(120, lblRasseDrop.getSize().y);
		createDropTarget(lblRasseDrop, Rasse.class);
		
		final Composite rasseContainer = new Composite (groupRasse, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		rasseContainer.setLayout(gridLayout);
		tmpGridData = new GridData();
		tmpGridData.horizontalSpan = 2;
		rasseContainer.setLayoutData(tmpGridData);
		
		cbxRasseKzW = new Button(rasseContainer, SWT.CHECK);
		cbxRasseKzW.setText("Kind zweier Welten (nach 4.0)");
		cbxRasseKzW.addSelectionListener(createSelectionListener(Rasse.class));
		
		lblRasseKzW = new Label(rasseContainer,  SWT.BORDER);
		lblRasseKzW.setText(" Per Drag and Drop wählen ");
		lblRasseKzW.setSize(120, lblRasseKzW.getSize().y);
		createDropTarget(lblRasseKzW, Rasse.class);
		
		Label lblRasseGPText = new Label(groupRasse, SWT.NONE);
		lblRasseGPText.setText("GP Kosten:");
		lblRasseGP = new Label(groupRasse, SWT.NONE);
		lblRasseGP.setText("0");
		
	// Erzeuge Gruppe "Kultur"
		groupKultur = new Group(mainContainer, SWT.SHADOW_IN);
		groupKultur.setText("Kultur");
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		tmpGridData = new GridData(GridData.GRAB_HORIZONTAL);
		tmpGridData.widthHint = 500;
		groupKultur.setLayout(gridLayout);
		groupKultur.setLayoutData(tmpGridData);
		
		Label lblKulturName = new Label(groupKultur, SWT.NONE);
		lblKulturName.setText("Kultur:");
		
		lblKulturDrop = new Label(groupKultur, SWT.BORDER);
		lblKulturDrop.setText(" Per Drag and Drop wählen ");
		lblKulturDrop.setSize(120, lblKulturDrop.getSize().y);
		createDropTarget(lblKulturDrop, Kultur.class);
		
		final Composite kultContainer = new Composite (groupKultur, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		kultContainer.setLayout(gridLayout);
		tmpGridData = new GridData();
		tmpGridData.horizontalSpan = 2;
		kultContainer.setLayoutData(tmpGridData);
		
		cbxKulturKzW = new Button(kultContainer, SWT.CHECK);
		cbxKulturKzW.setText("Kind zweier Welten (nach 4.0)");
		cbxKulturKzW.addSelectionListener(createSelectionListener(Kultur.class));
		
		lblKulturKzW = new Label(kultContainer,  SWT.BORDER);
		lblKulturKzW.setText(" Per Drag and Drop wählen ");
		lblKulturKzW.setSize(120, lblKulturKzW.getSize().y);
		createDropTarget(lblKulturKzW, Kultur.class);
		
		Label lblKulturGPText = new Label(groupKultur, SWT.NONE);
		lblKulturGPText.setText("GP Kosten:");
		lblKulturGP = new Label(groupKultur, SWT.NONE);
		lblKulturGP.setText("0");

	// Erzeuge Gruppe "Profession"
		groupProfession = new Group(mainContainer, SWT.SHADOW_IN);
		groupProfession.setText("Profession");
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		tmpGridData = new GridData(GridData.GRAB_HORIZONTAL);
		tmpGridData.widthHint = 500;
		groupProfession.setLayout(gridLayout);
		groupProfession.setLayoutData(tmpGridData);
		
		Label lblProfessionName = new Label(groupProfession, SWT.NONE);
		lblProfessionName.setText("Profession:");
		//lblRasseName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		
		lblProfessionDrop = new Label(groupProfession, SWT.BORDER);
		lblProfessionDrop.setText(" Per Drag and Drop wählen ");
		lblProfessionDrop.setSize(120, lblProfessionDrop.getSize().y);
		createDropTarget(lblProfessionDrop, Profession.class);
		
		final Composite profContainer = new Composite (groupProfession, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		profContainer.setLayout(gridLayout);
		tmpGridData = new GridData();
		tmpGridData.horizontalSpan = 2;
		profContainer.setLayoutData(tmpGridData);
		
		cbxProfessionAbAus = new Button(profContainer, SWT.CHECK);
		cbxProfessionAbAus.setText("Abgebrochene Ausbildung");
		cbxProfessionAbAus.addSelectionListener(createSelectionListener(Profession.class));
		
		final Label lblSpace = new Label(profContainer, SWT.NONE);
		
		cbxProfessionVeteran = new Button(profContainer, SWT.CHECK);
		cbxProfessionVeteran.setText("Veteran");
		cbxProfessionVeteran.addSelectionListener(createSelectionListener(Profession.class));
		
		lblProfessionVeteran = new Label(profContainer,  SWT.BORDER);
		lblProfessionVeteran.setText(" Per Drag and Drop wählen ");
		lblProfessionVeteran.setSize(120, lblProfessionVeteran.getSize().y);
		createDropTarget(lblProfessionVeteran, Profession.class);
		
		cbxProfessionBreitgeBildung = new Button(profContainer, SWT.CHECK);
		cbxProfessionBreitgeBildung.setText("Breitgefächerte Bildung");
		cbxProfessionBreitgeBildung.addSelectionListener(createSelectionListener(Profession.class));
		
		lblProfessionBreitgeBildung = new Label(profContainer,  SWT.BORDER);
		lblProfessionBreitgeBildung.setText(" Per Drag and Drop wählen ");
		lblProfessionBreitgeBildung.setSize(120, lblProfessionBreitgeBildung.getSize().y);
		createDropTarget(lblProfessionBreitgeBildung, Profession.class);
		
		Label lblProfessionGPText = new Label(groupProfession, SWT.NONE);
		lblProfessionGPText.setText("GP Kosten:");
		lblProfessionGP = new Label(groupProfession, SWT.NONE);
		lblProfessionGP.setText("0");
		
	//Große für Scroll-Container festlegen
		scrollComp.setMinSize(0, mainContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
	}

	private SelectionListener createSelectionListener(final Class allowedClass) {
		return 	new SelectionListener() {

			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Noop
			}

			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				trySubmitChangeData(allowedClass);
			}
		};	
	}


	private DropTarget createDropTarget(final Label label, final Class allowedClass) {
	    DropTarget target = new DropTarget(
	    		label,
	    		DND.DROP_COPY | DND.DROP_MOVE);
	    
	    target.setTransfer(new Transfer[] { LocalSelectionTransfer.getTransfer() });
	    target.addDropListener( new DropTargetAdapter() {
	      public void drop(DropTargetEvent event) {
	        if (event.data == null) {
	          return;
	        }
	        final TreeOrTableObject treeObj = (TreeOrTableObject) ((IStructuredSelection) event.data).getFirstElement();
	        final CharElement charElem = ((CharElement) treeObj.getValue());
	        
	        // Darf das Element vom Typ überhaupt hinzugefügt werden?
	        if (charElem == null || !allowedClass.isAssignableFrom(charElem.getClass())) return;
	        //if (!allowedClass.isInstance(charElem)) return;
	   
	        Object oldData = label.getData();
	        label.setData(charElem);
	        
	        if ( trySubmitChangeData(allowedClass)) {
				label.setText(charElem.getName());
	        } else {
	        	label.setData(oldData);
	        }
	      }
	    });
	    
	    return target;
	}
	
	private boolean trySubmitChangeData(Class allowedClass) {
		Herkunft neueHerkunft;
        
        if (allowedClass.equals(Rasse.class) ) {
        	if (lblRasseDrop.getData() == null) {
        		return false;
        	} else if (cbxRasseKzW.getSelection() && lblRasseKzW.getData() != null) {
        		neueHerkunft = new ZweiWeltenRasse(
			        				(Rasse) lblRasseDrop.getData(), 
			        				(Rasse) lblRasseKzW.getData());
        	} else {
        		neueHerkunft = (Rasse) lblRasseDrop.getData();
        	}

        } else if (allowedClass.equals(Kultur.class) ) {
        	if (lblKulturDrop.getData() == null) {
        		return false;
        	} else if (cbxKulturKzW.getSelection() && lblKulturKzW.getData() != null) {
        		neueHerkunft = new ZweiWeltenKultur(
			        				(Kultur) lblKulturDrop.getData(), 
			        				(Kultur) lblKulturKzW.getData());
        	} else {
        		neueHerkunft = (Kultur) lblKulturDrop.getData();
        	}
        	
        } else if (allowedClass.equals(Profession.class) ) {
        	if (lblProfessionDrop.getData() == null) return false;
        	
        	// Es ist theoretisch, ohne Regeln, möglich auch mehrere Varianten zu benutzen
        	// Die Reihenfolge ist wichtig, siehe "addElement" 
        	neueHerkunft = (Profession) lblProfessionDrop.getData();
        	if (cbxProfessionBreitgeBildung.getSelection() && lblProfessionBreitgeBildung.getData() != null) {
        		neueHerkunft = new BreitgefaechertProf(
        				(Profession) neueHerkunft,
            			(Profession) lblProfessionBreitgeBildung.getData());
        	}
        	if (cbxProfessionVeteran.getSelection()) {
        		if (lblProfessionVeteran.getData() == null) {
        			lblProfessionVeteran.setData(neueHerkunft);
        		}
        		neueHerkunft = new VeteranProf(
            			(Profession) neueHerkunft,
            			(Profession) lblProfessionVeteran.getData());
        	} 
        	if (cbxProfessionAbAus.getSelection()) {
        		neueHerkunft = new AbgebrochenProf((Profession) neueHerkunft);
        	} 
        	
        } else {
        	throw new IllegalArgumentException("'allowedClass' muss Rasse, Kultur oder Profession entsprechen!");
        }
        
        // Wurde überhaupt eine neue Herkunft ausgewählt?
        if (herkunftProzessor.getElementBox().getObjectById(neueHerkunft) != null) return true;
        
        // Prüfen ob hinzufügbar, wenn ja hinzufügen
        if (herkunftProzessor.canAddElement(neueHerkunft)) {
        	herkunftProzessor.addNewElement(neueHerkunft);
        	return true;
        }
        
		return false;
	}
	
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.ProzessorObserver#addElement(java.lang.Object)
	 */
	@Override
	public void addElement(Object obj) {
		Herkunft neuHerkunft = (Herkunft) ((Link) obj).getZiel();
		
		if (neuHerkunft instanceof Rasse) {
			lblRasseDrop.setText(neuHerkunft.getName());
			lblRasseDrop.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			lblRasseDrop.setData(neuHerkunft);			
			
			if (neuHerkunft instanceof ZweiWeltenRasse) {
				cbxRasseKzW.setSelection(true);
				lblRasseDrop.setData(((ZweiWeltenRasse) neuHerkunft).getRasseEins());
				
				lblRasseKzW.setText(((ZweiWeltenRasse) neuHerkunft).getRasseZwei().getName());
				lblRasseKzW.setData(((ZweiWeltenRasse) neuHerkunft).getRasseZwei());
				lblRasseKzW.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			} else {
				cbxRasseKzW.setSelection(false);
			}
			lblRasseGP.setText( CommonUtils.doubleToString(((GeneratorLink) obj).getKosten()) + " GP" );
			lblRasseGP.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			
		} else if (neuHerkunft instanceof Kultur) {
			lblKulturDrop.setText(neuHerkunft.getName());
			lblKulturDrop.setData(neuHerkunft);
			lblKulturDrop.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			
			if (neuHerkunft instanceof ZweiWeltenKultur) {
				cbxKulturKzW.setSelection(true);
				lblKulturDrop.setData(((ZweiWeltenKultur) neuHerkunft).getKulturEins());
				
				lblKulturKzW.setText(((ZweiWeltenKultur) neuHerkunft).getKulturZwei().getName());
				lblKulturKzW.setData(((ZweiWeltenKultur) neuHerkunft).getKulturZwei());
				lblKulturKzW.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			} else {
				cbxKulturKzW.setSelection(false);
			}
			lblKulturGP.setText( CommonUtils.doubleToString(((GeneratorLink) obj).getKosten()) + " GP" );
			lblKulturGP.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			
		}else if (neuHerkunft instanceof Profession) {
			lblProfessionDrop.setText(neuHerkunft.getName());
			lblProfessionDrop.setData(neuHerkunft);
			lblProfessionDrop.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			
			// Die Reihenfolge ist wichtig, siehe "trySubmitChangeData" 
			if (neuHerkunft instanceof AbgebrochenProf) {
				cbxProfessionAbAus.setSelection(true);
				
				neuHerkunft = (Profession) ((AbgebrochenProf) neuHerkunft).getOrginalProf();
				lblProfessionDrop.setData(neuHerkunft);
			} else {
				cbxProfessionAbAus.setSelection(false);
			}
			
			if (neuHerkunft instanceof VeteranProf) {
				cbxProfessionVeteran.setSelection(true);
				lblProfessionVeteran.setText(((VeteranProf) neuHerkunft).getProfZwei().getName());
				lblProfessionVeteran.setData(((VeteranProf) neuHerkunft).getProfZwei());
				lblProfessionVeteran.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				
				neuHerkunft = ((VeteranProf) neuHerkunft).getProfEins();
				lblProfessionDrop.setData(neuHerkunft);
			} else {
				cbxProfessionVeteran.setSelection(false);
			}
				
			if (neuHerkunft instanceof BreitgefaechertProf) {
				cbxProfessionBreitgeBildung.setSelection(true);
				lblProfessionBreitgeBildung.setText(((BreitgefaechertProf) neuHerkunft).getProfZwei().getName());
				lblProfessionBreitgeBildung.setData(((BreitgefaechertProf) neuHerkunft).getProfZwei());
				lblProfessionBreitgeBildung.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				
				neuHerkunft = ((BreitgefaechertProf) neuHerkunft).getProfEins();
				lblProfessionDrop.setData(neuHerkunft);
				
			} else {
				cbxProfessionBreitgeBildung.setSelection(false);
			}

			lblProfessionGP.setText( CommonUtils.doubleToString(((GeneratorLink) obj).getKosten()) + " GP" );
			
		} else {
			throw new IllegalArgumentException("'obj' muss eine Rasse, Kultur oder Profession sein!");
		}
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.ProzessorObserver#removeElement(java.lang.Object)
	 */
	@Override
	public void removeElement(Object obj) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.ProzessorObserver#setData(java.util.List)
	 */
	@Override
	public void setData(List list) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.ProzessorObserver#updateElement(java.lang.Object)
	 */
	@Override
	public void updateElement(Object obj) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		((BaseProzessorObserver) herkunftProzessor).unregisterObserver(this);
		super.dispose();
	}
	
}
