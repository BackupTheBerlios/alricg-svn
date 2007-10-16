/*
 * Created 03.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import java.util.List;

import org.d3s.alricg.editor.common.CustomColumnLabelProvider.ImageProviderRegulator;
import org.d3s.alricg.editor.common.widgets.DropTable;
import org.d3s.alricg.editor.common.widgets.DropTable.DropListRegulator;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.charElemente.Herkunft.AlternativMagieEigenschaften;
import org.d3s.alricg.store.charElemente.Werte.Geschlecht;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * @author Vincent
 *
 */
public class AlternativeZauberAuswahlPart extends AbstractElementPart<Herkunft> {
	private ScrolledComposite scrollComp;
	
	private final Spinner spiAnzZauber;
	private final Spinner spiAnzHauszauber;
	private final Spinner spiAnzLeitzauber;
	private final Spinner spiPunkte;
	
	private final DropTable dropEmpfHauszauber;
	private final DropTable dropEmpfZauber;
	private final DropTable dropMoeglZauber;	
	
	public AlternativeZauberAuswahlPart(Composite top, IWorkbenchPartSite site) {
		
		// Erzeugen des Scrollpanels
		scrollComp = new ScrolledComposite(top, SWT.V_SCROLL);
		scrollComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrollComp.setExpandHorizontal(true);
		scrollComp.setExpandVertical(true);
		
		Composite mainComp = new Composite(scrollComp, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		mainComp.setLayout(gridLayout);
		
		scrollComp.setContent(mainComp);
		
		// GirdLayout mit zwei Spalten
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		mainComp.setLayout(gridLayout);
		
		// Für die Gruppe "Anzahl der Zauber"
		GridData tmpGData = new GridData(GridData.GRAB_HORIZONTAL);
		tmpGData.widthHint = 600;
		tmpGData.horizontalSpan = 2; // nimm 2 Spalten Platz ein
		
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		
		final Group groupAnzahlZauber = new Group(mainComp, SWT.SHADOW_IN);
		groupAnzahlZauber.setText("Anzahl zu wählende Zauber");
		groupAnzahlZauber.setLayout(gridLayout);
		groupAnzahlZauber.setLayoutData(tmpGData);
	
		final Label lblAnzZauber = new Label(groupAnzahlZauber, SWT.NONE);
		lblAnzZauber.setText("Anzahl Zauber:");
		spiAnzZauber = new Spinner(groupAnzahlZauber, SWT.BORDER);
		spiAnzZauber.setValues(
				0, 
				0, 
				100, 
				0, 1, 5);
		
		final Label lblAnzHauszauber = new Label(groupAnzahlZauber, SWT.NONE);
		lblAnzHauszauber.setText("Davon Hauszauber:");
		spiAnzHauszauber = new Spinner(groupAnzahlZauber, SWT.BORDER);
		spiAnzHauszauber.setValues(
				0, 
				0, 
				100, 
				0, 1, 5);
		
		final Label lblAnzLeitzauber = new Label(groupAnzahlZauber, SWT.NONE);
		lblAnzLeitzauber.setText("Davon Leitzauber:");
		spiAnzLeitzauber = new Spinner(groupAnzahlZauber, SWT.BORDER);
		spiAnzLeitzauber.setValues(
				0, 
				0, 
				100, 
				0, 1, 5);
		
		// Für die Gruppe "Wählbare Zauber"
		tmpGData = new GridData(GridData.GRAB_HORIZONTAL);
		tmpGData.widthHint = 600;
		tmpGData.horizontalSpan = 2; // nimm 2 Spalten Platz ein
		
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		
		Group groupWaehlbareZauber = new Group(mainComp, SWT.SHADOW_IN);
		groupWaehlbareZauber.setText("Wählbare Zauber:");
		groupWaehlbareZauber.setLayout(gridLayout);
		groupWaehlbareZauber.setLayoutData(tmpGData);
		
		final DropListRegulator regulator = new DropListRegulator() {
			@Override
			public boolean canDrop(Object obj) {
				if (obj == null) return false;
				if (obj instanceof Zauber) {
					return true;
				}
				return false;
			}

			@Override
			public ImageProviderRegulator getImageProviderRegulator() {
				return null;
			}
		};
		
		// Wählbare Zauber
		final Label lblEmpfHauszauber = new Label(groupWaehlbareZauber, SWT.NONE);
		lblEmpfHauszauber.setText("Empfohlene Hauszauber:");
		lblEmpfHauszauber.setToolTipText("Zauber die als Hauszauber empfohlen sind. " +
				"Diese müssen NICHT bei \"empfohlene\" oder \"mögliche\" Zauber eingetragen werden.");
		tmpGData = new GridData(); 
		tmpGData.verticalAlignment = GridData.BEGINNING;
		tmpGData.verticalIndent = 10;
		lblEmpfHauszauber.setLayoutData(tmpGData);
		dropEmpfHauszauber = new DropTable(groupWaehlbareZauber, SWT.NONE, regulator, site);
		
		final Label lblEmpfzauber = new Label(groupWaehlbareZauber, SWT.NONE);
		lblEmpfzauber.setText("Empfohlene Zauber:");
		lblEmpfzauber.setToolTipText("Zauber die empfohlen sind. " +
			"Diese müssen NICHT bei \"empfohlene Hauszauber\" oder \"mögliche\" Zauber eingetragen werden.");
		tmpGData = new GridData(); 
		tmpGData.verticalAlignment = GridData.BEGINNING;
		tmpGData.verticalIndent = 10;
		lblEmpfzauber.setLayoutData(tmpGData);
		dropEmpfZauber = new DropTable(groupWaehlbareZauber, SWT.NONE, regulator, site);
		
		final Label lblMoeglZauber = new Label(groupWaehlbareZauber, SWT.NONE);
		lblMoeglZauber.setText("Mögliche Zauber:");
		lblMoeglZauber.setToolTipText("Zauber die nicht empfohlen, aber möglich sind. " +
			"Diese müssen NICHT bei den \"empfohlenen\" Zaubern eingetragen werden.");
		tmpGData = new GridData(); 
		tmpGData.verticalAlignment = GridData.BEGINNING;
		tmpGData.verticalIndent = 10;
		lblMoeglZauber.setLayoutData(tmpGData);
		dropMoeglZauber = new DropTable(groupWaehlbareZauber, SWT.NONE, regulator, site);
		
		// Außerhalb der Gruppen
		final Label lblPunkte = new Label(mainComp, SWT.NONE);
		lblPunkte.setText("Zu verteilende Punkte:");
		spiPunkte = new Spinner(mainComp, SWT.BORDER);
		spiPunkte.setValues(
				0, 
				0, 
				1000, 
				0, 1, 5);
		
		
		// Berechnung der Größe des Scrollpanels
		Point size = mainComp.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scrollComp.setMinSize(0, size.y);
	}

	public Composite getControl() {
		return scrollComp;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#dispose()
	 */
	@Override
	public void dispose() {
		dropEmpfHauszauber.dispose();
		dropEmpfZauber.dispose();
		dropMoeglZauber.dispose();	
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#isDirty(java.lang.Object)
	 */
	@Override
	public boolean isDirty(Herkunft charElem) {
		boolean isNotDirty = true;
		AlternativMagieEigenschaften altEig;
		
		if (charElem.getAlternativMagieEigenschaften() == null) {
			altEig = new AlternativMagieEigenschaften();
		} else {
			altEig = charElem.getAlternativMagieEigenschaften();
		}
		
		isNotDirty &= spiAnzZauber.getSelection() == altEig.getAnzZauberGesamt();
		isNotDirty &= spiAnzHauszauber.getSelection() == altEig.getAnzHauszauber();
		isNotDirty &= spiAnzLeitzauber.getSelection() == altEig.getAnzLeitzauber();
		isNotDirty &= spiPunkte.getSelection() == altEig.getPunkte();
		
		isNotDirty &= compareArrayList(
				altEig.getEmpfHauszauber(), 
				dropEmpfHauszauber.getValueList());
		isNotDirty &= compareArrayList(
				altEig.getEmpfZauber(), 
				dropEmpfZauber.getValueList());		
		isNotDirty &= compareArrayList(
				altEig.getMoeglichZauber(), 
				dropMoeglZauber.getValueList());

		return !isNotDirty;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(java.lang.Object)
	 */
	@Override
	public void loadData(Herkunft charElem) {
		if (charElem.getAlternativMagieEigenschaften() == null) {
			return;
		}
		final AlternativMagieEigenschaften altEig = charElem.getAlternativMagieEigenschaften();
		
		spiAnzZauber.setSelection(altEig.getAnzZauberGesamt());
		spiAnzHauszauber.setSelection(altEig.getAnzHauszauber());
		spiAnzLeitzauber.setSelection(altEig.getAnzLeitzauber());
		spiPunkte.setSelection(altEig.getPunkte());
		
		if (altEig.getEmpfHauszauber() != null) {
			for (int i = 0; i < altEig.getEmpfHauszauber().length; i++) {
				dropEmpfHauszauber.addValue(altEig.getEmpfHauszauber()[i]);
			}
		}
		if (altEig.getEmpfZauber() != null) {
			for (int i = 0; i < altEig.getEmpfZauber().length; i++) {
				dropEmpfZauber.addValue(altEig.getEmpfZauber()[i]);
			}
		}
		if (altEig.getMoeglichZauber() != null) {
			for (int i = 0; i < altEig.getMoeglichZauber().length; i++) {
				dropMoeglZauber.addValue(altEig.getMoeglichZauber()[i]);
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, java.lang.Object)
	 */
	@Override
	public void saveData(IProgressMonitor monitor, Herkunft charElem) {
		monitor.subTask("Save alternatibe Magie-Data"); //$NON-NLS-1$
		
		AlternativMagieEigenschaften altEig = new AlternativMagieEigenschaften();
		
		// Werte setzen
		altEig.setAnzZauberGesamt(spiAnzZauber.getSelection());
		altEig.setAnzHauszauber(spiAnzHauszauber.getSelection());
		altEig.setAnzLeitzauber(spiAnzLeitzauber.getSelection());
		altEig.setPunkte(spiPunkte.getSelection());
		
		// Zauber setzen
		List l = dropEmpfHauszauber.getValueList();
		if (l.size() == 0) {
			altEig.setEmpfHauszauber(null);
		} else {
			altEig.setEmpfHauszauber((Zauber[]) l.toArray(new Zauber[l.size()]));
		}
		l = dropEmpfZauber.getValueList();
		if (l.size() == 0) {
			altEig.setEmpfZauber(null);
		} else {
			altEig.setEmpfZauber((Zauber[]) l.toArray(new Zauber[l.size()]));
		}
		l = dropMoeglZauber.getValueList();
		if (l.size() == 0) {
			altEig.setMoeglichZauber(null);
		} else {
			altEig.setMoeglichZauber((Zauber[]) l.toArray(new Zauber[l.size()]));
		}
		
		if (altEig.getAnzZauberGesamt() == 0
				&& altEig.getAnzHauszauber() == 0 
				&& altEig.getAnzLeitzauber() == 0 
				&& altEig.getPunkte() == 0
				&& altEig.getEmpfHauszauber() == null
				&& altEig.getEmpfZauber() == null
				&& altEig.getMoeglichZauber() == null) {
			charElem.setAlternativMagieEigenschaften(null);
		} else {
			charElem.setAlternativMagieEigenschaften(altEig);
		}
		
		monitor.worked(1);
	}
	
	

}
