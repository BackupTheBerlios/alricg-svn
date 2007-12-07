/*
 * Created 17.11.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.views.held;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.d3s.alricg.common.CommonUtils;
import org.d3s.alricg.common.logic.BaseProzessorObserver;
import org.d3s.alricg.common.logic.FormelSammlung;
import org.d3s.alricg.common.logic.Prozessor;
import org.d3s.alricg.common.logic.ProzessorObserver;
import org.d3s.alricg.generator.Activator;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorEigenschaft;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.links.Link;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.part.ViewPart;

/**
 * @author Vincent
 *
 */
public class EigenschaftenView extends ViewPart implements ProzessorObserver {
	public static final String ID = "org.d3s.alricg.generator.views.held.EigenschaftenView"; //$NON-NLS-1$
	protected Prozessor prozessor; 
	
	private Label lblGesamtGPKosten;
	private Label lblGesamtAPKosten;
	
	private Hashtable<EigenschaftEnum, UpdateComponentCollection> updateHash;

	public EigenschaftenView() {
		prozessor = Activator.getCurrentCharakter().getProzessor(Eigenschaft.class);
		((BaseProzessorObserver) prozessor).registerObserver(this);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		updateHash = new Hashtable<EigenschaftEnum, UpdateComponentCollection>();
		
		// Scroll-Container
		
		final ScrolledComposite scrollComp = new ScrolledComposite(parent, SWT.V_SCROLL);
		scrollComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrollComp.setExpandHorizontal(true);
		scrollComp.setExpandVertical(true);
		
		// Container erzeugen, Grid mit 2 Spalten setzen, Scroll-Container setzen
		final Composite mainContainer = new Composite (scrollComp, SWT.NONE);
		scrollComp.setContent(mainContainer);
		//mainContainer.setLayout(new FillLayout());
		
		final Group groupBasisDaten;
		final Group groupErrechneteDaten;
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.horizontalSpacing = 10;
		
		mainContainer.setLayout(gridLayout);
		
		gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		
		//GridData tmpGData = new GridData(GridData.GRAB_HORIZONTAL);
	
		// Erzeuge Gruppe "Basisdaten"
		groupBasisDaten = new Group(mainContainer, SWT.SHADOW_IN);
		groupBasisDaten.setText("Basiswerte: ");
		GridData tmpGridData = new GridData(GridData.FILL_HORIZONTAL);
		tmpGridData.verticalAlignment = SWT.TOP;
		groupBasisDaten.setLayoutData(tmpGridData);
		groupBasisDaten.setLayout(gridLayout);
		
		Label lblEigenschaft = new Label(groupBasisDaten, SWT.NONE);
		lblEigenschaft.setText("Eigenschaft");
		lblEigenschaft.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		
		Label lblEigWert = new Label(groupBasisDaten, SWT.NONE);
		lblEigWert.setText("Wert");
		lblEigWert.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		
		Label lblEigModi = new Label(groupBasisDaten, SWT.NONE);
		lblEigModi.setText("Modi");
		lblEigModi.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		
		Label lblEigKosten = new Label(groupBasisDaten, SWT.NONE);
		lblEigKosten.setText("Kosten");
		lblEigKosten.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
	
		// Werte setzen
		EigenschaftEnum[] eigEnum = new EigenschaftEnum[] {
				EigenschaftEnum.MU,
				EigenschaftEnum.KL,
				EigenschaftEnum.IN,
				EigenschaftEnum.CH,
				EigenschaftEnum.FF,
				EigenschaftEnum.GE,
				EigenschaftEnum.KO,
				EigenschaftEnum.KK,
				null,
				EigenschaftEnum.SO
				
			};
		for (EigenschaftEnum eig : eigEnum) {
			buildEigenschaft(groupBasisDaten, eig);
		}
		
		Label lblGesamtGPLabel = new Label(groupBasisDaten, SWT.NONE);
		lblGesamtGPLabel.setText("Gesamtkosten: ");
		tmpGridData = new GridData(SWT.END, SWT.CENTER, false, false, 3, 1);
		tmpGridData.verticalIndent = 7;
		lblGesamtGPLabel.setLayoutData(tmpGridData);
		lblGesamtGPKosten = new Label(groupBasisDaten, SWT.NONE);
		lblGesamtGPKosten.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		tmpGridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		tmpGridData.verticalIndent = 7;
		lblGesamtGPKosten.setLayoutData(tmpGridData);
		
	// ---------------------------------------------------
	// Erzeuge die Gruppe "Abgeleitete Werte"
		gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		
		// Erzeuge die Gruppe
		groupErrechneteDaten = new Group(mainContainer, SWT.SHADOW_IN);
		groupErrechneteDaten.setText("Abgeleitete Werte: ");
		groupErrechneteDaten.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		groupErrechneteDaten.setLayout(gridLayout);
		
		Label lblEigenschaft2 = new Label(groupErrechneteDaten, SWT.NONE);
		lblEigenschaft2.setText("Eigenschaft");
		lblEigenschaft2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		
		Label lblEigWert2 = new Label(groupErrechneteDaten, SWT.NONE);
		lblEigWert2.setText("Wert");
		lblEigWert2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		
		Label lblEigModi2 = new Label(groupErrechneteDaten, SWT.NONE);
		lblEigModi2.setText("Modi");
		lblEigModi2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		
		Label lblGekauft = new Label(groupErrechneteDaten, SWT.NONE);
		lblGekauft.setText("Gekauft");
		lblGekauft.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		
		Label lblEigKosten2 = new Label(groupErrechneteDaten, SWT.NONE);
		lblEigKosten2.setText("Kosten");
		lblEigKosten2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		
		// Werte setzen
		eigEnum = new EigenschaftEnum[] {
				EigenschaftEnum.MR,
				EigenschaftEnum.GS,
				null,
				EigenschaftEnum.LEP,
				EigenschaftEnum.AUP,
				EigenschaftEnum.ASP,
				EigenschaftEnum.KA,
				null,
				EigenschaftEnum.AT,
				EigenschaftEnum.PA,
				EigenschaftEnum.FK,
				EigenschaftEnum.INI,
				null,
				EigenschaftEnum.WSW,
				EigenschaftEnum.ESW
			};
		for (EigenschaftEnum eig : eigEnum) {
			buildErrechneteEigenschaft(groupErrechneteDaten, eig);
		}
		
		Label lblGesamtAPLabel = new Label(groupErrechneteDaten, SWT.NONE);
		lblGesamtAPLabel.setText("Gesamtkosten: ");
		tmpGridData = new GridData(SWT.END, SWT.CENTER, false, false, 4, 1);
		tmpGridData.verticalIndent = 7;
		lblGesamtAPLabel.setLayoutData(tmpGridData);
		lblGesamtAPKosten = new Label(groupErrechneteDaten, SWT.NONE);
		lblGesamtAPKosten.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		tmpGridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		tmpGridData.verticalIndent = 7;
		lblGesamtAPKosten.setLayoutData(tmpGridData);
		
		updateGesamtBasisKosten();
		
		//Große für Scroll-Container festlegen
		scrollComp.setMinSize(0, mainContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		
	}
	
	/**
	 * Helper und den Inhalt der Gruppe "Basiswerte" zu erstellen
	 * @param parent Gruppe
	 * @param eig Eigenschaft oder "null" für Seperator
	 */
	private void buildEigenschaft(Composite parent, final EigenschaftEnum eig) {
		if (eig == null) {
			Label lblSeperator= new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
			lblSeperator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
			return;
		}
		
		final ExtendedProzessorEigenschaft extProz = ((ExtendedProzessorEigenschaft) prozessor.getExtendedInterface());
		
		Label lblName = new Label(parent, SWT.NONE);
		lblName.setText(eig.getBezeichnung() + ":");
		lblName.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
		
		Spinner spiEigenschaft = new Spinner(parent, SWT.BORDER);
		spiEigenschaft.setValues(
				extProz.getEigenschaftsWert(eig),
				prozessor.getMinWert(extProz.getEigenschaftsLink(eig)),
				prozessor.getMaxWert(extProz.getEigenschaftsLink(eig)), 
				0, 1, 1);
		
		spiEigenschaft.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				((Spinner) e.widget).setMinimum(prozessor.getMinWert(
						extProz.getEigenschaftsLink(eig)));
				((Spinner) e.widget).setMaximum(prozessor.getMaxWert(
						extProz.getEigenschaftsLink(eig)));
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (((Spinner) e.widget).getSelection() != extProz.getEigenschaftsLink(eig).getWert()) {
					prozessor.updateWert(
							extProz.getEigenschaftsLink(eig),
							((Spinner) e.widget).getSelection()
					);
				}
			}
		});
		spiEigenschaft.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) { }

			@Override
			public void mouseDown(MouseEvent e) { }

			@Override
			public void mouseUp(MouseEvent e) {
				prozessor.updateWert(
						extProz.getEigenschaftsLink(eig),
						((Spinner) e.widget).getSelection()
				);
			}
		});
		
		Label lblModi = new Label(parent, SWT.NONE);
		Label lblKosten = new Label(parent, SWT.NONE);
		lblKosten.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		
		updateHash.put(eig, new UpdateComponentCollection(spiEigenschaft, lblModi, lblKosten));
		updateHash.get(eig).updateAll(extProz.getEigenschaftsLink(eig), prozessor);
	}
	
	/**
	 * Helper und den Inhalt der Gruppe "Abgeleitete Werte" zu erstellen
	 * @param parent Gruppe
	 * @param eig Eigenschaft oder "null" für Seperator
	 */
	private void buildErrechneteEigenschaft(Composite parent, final EigenschaftEnum eig) {
		if (eig == null) {
			Label lblSeperator= new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
			lblSeperator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1));
			return;
		}
		
		final ExtendedProzessorEigenschaft extProz = ((ExtendedProzessorEigenschaft) prozessor.getExtendedInterface());
		final GeneratorLink<Eigenschaft> link = extProz.getEigenschaftsLink(eig);
		
		Label lblName = new Label(parent, SWT.NONE);
		lblName.setText(eig.getBezeichnung() + ":");
		lblName.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
		lblName.setToolTipText(eig.getFormel());
		
		Label lblGesamt = new Label(parent, SWT.NONE);
		Label lblModi = new Label(parent, SWT.NONE);
		
		Spinner spiEigenschaft = new Spinner(parent, SWT.BORDER);
		spiEigenschaft.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, false, false));
		if (prozessor.canUpdateWert(link)) {
			
			spiEigenschaft.setValues(
					0,
					0,
					prozessor.getMaxWert(extProz.getEigenschaftsLink(eig)) - extProz.getEigenschaftsWert(eig), 
					0, 1, 1);

			spiEigenschaft.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {					
					((Spinner) e.widget).setMinimum(extProz.getMinZukauf(eig));
					((Spinner) e.widget).setMaximum(extProz.getMaxZukauf(eig));
				}
	
				@Override
				public void focusLost(FocusEvent e) { 
					if (((Spinner) e.widget).getSelection() != 
							extProz.getEigenschaftsLink(eig).getUserLink().getWert()) {
						prozessor.updateWert(
								extProz.getEigenschaftsLink(eig),
								((Spinner) e.widget).getSelection()
									+ extProz.getEigenschaftsLink(eig).getWertModis()
						);
					}
				}
			});
			spiEigenschaft.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) { }

				@Override
				public void mouseDown(MouseEvent e) { }

				@Override
				public void mouseUp(MouseEvent e) {
					prozessor.updateWert(
							extProz.getEigenschaftsLink(eig),
							((Spinner) e.widget).getSelection()
								+ extProz.getEigenschaftsLink(eig).getWertModis()
					);
				}
			});
			
		} else {
			spiEigenschaft.setValues(0, 0, 10, 0, 1, 1);
			spiEigenschaft.setEnabled(false);
		}
		
		Label lblKosten = new Label(parent, SWT.NONE);
		lblKosten.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		
		updateHash.put(eig, new UpdateComponentCollection(lblGesamt, lblModi, spiEigenschaft, lblKosten));
		updateHash.get(eig).updateAll(link, prozessor);
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.ProzessorObserver#updateElement(java.lang.Object)
	 */
	@Override
	public void updateElement(Object obj) {
		EigenschaftEnum eigEnum =  ((Eigenschaft) ((GeneratorLink<Eigenschaft>) obj).getZiel()).getEigenschaftEnum();
		
		// Geänderte Eigenschaft selbst updaten
		updateHash.get( eigEnum ).updateAll((GeneratorLink<Eigenschaft>) obj, prozessor);
		
		// Alle abhängigen Eigenschaften updaten
		final EigenschaftEnum[] abhEigEnums= FormelSammlung.getAbhängigkeiten(eigEnum);
		if (abhEigEnums != null) {
			for (EigenschaftEnum tmpEnum: abhEigEnums) {
				updateHash.get( tmpEnum ).updateAll(
						((ExtendedProzessorEigenschaft) prozessor.getExtendedInterface()).getEigenschaftsLink(tmpEnum),
						prozessor);
			}
		}
		
		updateGesamtBasisKosten(); // GesamtKosten neu berechnen
	}
	
	private void updateGesamtBasisKosten() {
		
		int basisKosten = 0, abgeleiteteKosten = 0;
		final List<GeneratorLink<Eigenschaft>> linkList = prozessor.getUnmodifiableList();
		
		for (GeneratorLink<Eigenschaft> link : linkList)  {
			EigenschaftEnum targetEnum = ((Eigenschaft) link.getZiel()).getEigenschaftEnum();
			if (targetEnum == EigenschaftEnum.MU
					|| targetEnum == EigenschaftEnum.KL
					|| targetEnum == EigenschaftEnum.IN
					|| targetEnum == EigenschaftEnum.CH
					|| targetEnum == EigenschaftEnum.FF
					|| targetEnum == EigenschaftEnum.GE
					|| targetEnum == EigenschaftEnum.KO
					|| targetEnum == EigenschaftEnum.KK
					|| targetEnum == EigenschaftEnum.SO) {
				basisKosten += link.getKosten();
			} else {
				abgeleiteteKosten += link.getKosten();
			}
		}
		
		lblGesamtGPKosten.setText(basisKosten + " GP");
		lblGesamtGPKosten.setSize( lblGesamtGPKosten.computeSize(SWT.DEFAULT, SWT.DEFAULT) );
		lblGesamtAPKosten.setText(abgeleiteteKosten + " AP");
		lblGesamtAPKosten.setSize( lblGesamtAPKosten.computeSize(SWT.DEFAULT, SWT.DEFAULT) );
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.ProzessorObserver#addElement(java.lang.Object)
	 */
	@Override
	public void addElement(Object obj) {
		// Noop
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.ProzessorObserver#removeElement(java.lang.Object)
	 */
	@Override
	public void removeElement(Object obj) {
		// Noop
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.ProzessorObserver#setData(java.util.List)
	 */
	@Override
	public void setData(List list) {
		// Noop
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// Noop
	}
	
	/**
	 * Speichert alle Grafik-Elemente zu einer Eigenschaft
	 * @author Vincent
	 */
	class UpdateComponentCollection {
		final Label lblEigModi, lblEigKosten;
		final Spinner spiGesamtWert;
		final Spinner spiGekauft;
		final Label lblGesamtWert;
		final String kostenPostfix;
		
		public UpdateComponentCollection(Spinner spiGesamtWert, Label lblEigModi, Label lblEigKosten) {
			this.spiGesamtWert = spiGesamtWert;
			this.lblEigModi = lblEigModi;
			this.lblEigKosten = lblEigKosten;
			
			kostenPostfix = " GP";
			this.spiGekauft = null;
			this.lblGesamtWert = null;
		}
		
		public UpdateComponentCollection(Label lblGesamtWert, Label lblEigModi, Spinner spiGekauft, Label lblEigKosten) {
			this.lblGesamtWert = lblGesamtWert;
			this.lblEigModi = lblEigModi;
			this.spiGekauft = spiGekauft;
			this.lblEigKosten = lblEigKosten;
			
			kostenPostfix = " AP";
			spiGesamtWert = null;
		}
		
		public void updateAll(GeneratorLink<Eigenschaft> link, Prozessor prozessor) {
			lblEigModi.setText("(+ " + link.getWertModis() + ")");
			lblEigModi.setSize( lblEigModi.computeSize(SWT.DEFAULT, SWT.DEFAULT) );
			lblEigKosten.setText( CommonUtils.doubleToString(link.getKosten()) +  kostenPostfix);
			lblEigKosten.setSize( lblEigKosten.computeSize(SWT.DEFAULT, SWT.DEFAULT) );
			
			int gesamtWert = ((ExtendedProzessorEigenschaft) prozessor.getExtendedInterface())
								.getEigenschaftsWert( ((Eigenschaft) link.getZiel()).getEigenschaftEnum() );
			if (lblGesamtWert != null) {
				lblGesamtWert.setText( String.valueOf(gesamtWert) );
				lblGesamtWert.setSize( lblGesamtWert.computeSize(SWT.DEFAULT, SWT.DEFAULT) );
				
			} else if (spiGesamtWert != null) {
				spiGesamtWert.setSelection( gesamtWert );
			}
			
			if (spiGekauft != null) {
				spiGekauft.setSelection(link.getUserLink().getWert());
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		((BaseProzessorObserver) prozessor).unregisterObserver(this);
		super.dispose();
	}

	
}
