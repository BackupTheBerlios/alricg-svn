/*
 * Created 02.09.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import java.util.Arrays;
import java.util.List;

import org.d3s.alricg.editor.common.CustomColumnLabelProvider.ImageProviderRegulator;
import org.d3s.alricg.editor.common.widgets.DropTable;
import org.d3s.alricg.editor.common.widgets.DropTable.DropListRegulator;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.editor.editors.composits.HerkunftPart;
import org.d3s.alricg.editor.editors.widgets.FarbenAuswahlTable;
import org.d3s.alricg.editor.editors.widgets.WuerfelSammlungTable;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.Rasse;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * @author Vincent
 */
public class RasseEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.RasseEditor";
	
	private HerkunftPart herkunftPart;
	private RassePart rassePart;
	private AbstractElementPart[] elementPartArray;

	static class RassePart extends AbstractElementPart<Rasse> {
		private final DropTable dropTableKulturMoeglich;
		private final DropTable dropTableKulturUeblich;
		private final WuerfelSammlungTable wstGroesse;
		private final WuerfelSammlungTable wstAlter;
		private final FarbenAuswahlTable fatHaarfarbe;
		private final FarbenAuswahlTable fatAugenfarbe;
		protected final Combo cobArt;
		private final Spinner spiGewicht;
		private final Spinner spiGs;
		protected final Button cbxIsNegativListe;
		
		RassePart(Composite top, IWorkbenchPartSite site, int alterStart, int groesseStart) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			
		// Wählbare Kulturen
			GridData tmpGData = new GridData(GridData.GRAB_HORIZONTAL);
			tmpGData.widthHint = 600;
			tmpGData.horizontalSpan = 2; // nimm 2 Spalten Platz ein
			
			final Group groupKultur = new Group(top, SWT.SHADOW_IN);
			groupKultur.setText("Wählbare Kulturen");
			groupKultur.setLayout(gridLayout);
			groupKultur.setLayoutData(tmpGData);
			
			final Label lblFiller = new Label(groupKultur, SWT.NONE);
			cbxIsNegativListe = new Button(groupKultur, SWT.CHECK);
			cbxIsNegativListe.setText("Kulturen sind eine negativ Angabe");
			cbxIsNegativListe.setToolTipText("Bei einer negativ Angabe dürfen " +
					"alle Kulturen gewählt werden, außer die aufgelisteten."); 
			
			final DropListRegulator regulator = new DropListRegulator() {
				@Override
				public boolean canDrop(Object obj) {
					if (obj == null) return false;
					if (obj instanceof Kultur) {
						return true;
					}
					return false;
				}

				@Override
				public ImageProviderRegulator getImageProviderRegulator() {
					return null;
				}
			};
			
			// Übliche Kulturen
			final Label lblUeblich = new Label(groupKultur, SWT.NONE);
			lblUeblich.setText("Übliche Kulturen:");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			tmpGData.verticalIndent = 10;
			lblUeblich.setLayoutData(tmpGData);
			
			dropTableKulturUeblich = new DropTable(groupKultur, SWT.NONE, regulator, site);

			// Mögliche Kulturen
			final Label lblMoeglich = new Label(groupKultur, SWT.NONE);
			lblMoeglich.setText("Mögliche Kulturen:");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			tmpGData.verticalIndent = 10;
			lblMoeglich.setLayoutData(tmpGData);
			
			dropTableKulturMoeglich = new DropTable(groupKultur, SWT.NONE, regulator, site);

			final Label lblGroesse = new Label(top, SWT.NONE);
			lblGroesse.setText("Grösse:");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			tmpGData.verticalIndent = 10;
			lblGroesse.setLayoutData(tmpGData);
			
			wstGroesse = new WuerfelSammlungTable(top, SWT.NONE, site, groesseStart, "cm");
			
			final Label lblAlter = new Label(top, SWT.NONE);
			lblAlter.setText("Alter:");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			tmpGData.verticalIndent = 10;
			lblAlter.setLayoutData(tmpGData);
			
			wstAlter = new WuerfelSammlungTable(top, SWT.NONE, site, alterStart, "Jahre");
			
			final Label lblHaarfarbe = new Label(top, SWT.NONE);
			lblHaarfarbe.setText("Haarfarbe:");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			tmpGData.verticalIndent = 10;
			lblHaarfarbe.setLayoutData(tmpGData);
			
			fatHaarfarbe = new FarbenAuswahlTable(top, SWT.NONE, site);

			final Label lblAugenfarbe = new Label(top, SWT.NONE);
			lblAugenfarbe.setText("Augenfarbe:");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			tmpGData.verticalIndent = 10;
			lblAugenfarbe.setLayoutData(tmpGData);
			
			fatAugenfarbe = new FarbenAuswahlTable(top, SWT.NONE, site);
			
			final Label lblArt = new Label(top, SWT.NONE);
			lblArt.setText("Art:");
			cobArt = new Combo(top, SWT.READ_ONLY);		
			cobArt.add(Rasse.RasseArt.menschlich.toString());
			cobArt.add(Rasse.RasseArt.nichtMenschlich.toString());
			cobArt.select(0);
			
			final Label lblGewicht = new Label(top, SWT.NONE);
			lblGewicht.setText("Gewicht:");
			
			final Composite comPlus = new Composite(top, SWT.NONE);
			gridLayout = new GridLayout();
			gridLayout.verticalSpacing = 1;
			gridLayout.marginHeight = 0;
			gridLayout.marginWidth = 0;
			gridLayout.numColumns = 2;
			tmpGData = new GridData();
			comPlus.setLayout(gridLayout);
			comPlus.setLayoutData(tmpGData);
			
			final Label lblPlus = new Label(comPlus, SWT.NONE);
			lblPlus.setText("Grösse - ");
			spiGewicht = new Spinner(comPlus, SWT.BORDER);
			spiGewicht.setValues(100, 0, 200, 0, 1, 20);
			
			final Label lblGs = new Label(top, SWT.NONE);
			lblGs.setText("Geschwindigkeit:");	
			spiGs = new Spinner(top, SWT.BORDER);
			spiGs.setValues(8, 1, 50, 0, 1, 5);
		}
		
		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#dispose()
		 */
		@Override
		public void dispose() {
			// Noop
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public boolean isDirty(Rasse charElem) {
			boolean isNotDirty = true;
			
			// Kulturen
			isNotDirty &= cbxIsNegativListe.getSelection() == charElem.isKulturenSindNegativListe();
			isNotDirty &= compareArrayList(
							charElem.getKulturMoeglich(), 
							dropTableKulturMoeglich.getValueList());
			isNotDirty &= compareArrayList(
							charElem.getKulturUeblich(), 
							dropTableKulturUeblich.getValueList());
			
			// Farben
			if (charElem.getHaarfarbe() == null) {
				if (fatHaarfarbe.getValues().length != 0) return true;
			} else {
				isNotDirty &= Arrays.equals(fatHaarfarbe.getValues(), charElem.getHaarfarbe());
			}
			if (charElem.getAugenfarbe() == null) {
				if (fatAugenfarbe.getValues().length != 0) return true;
			} else {
				isNotDirty &= Arrays.equals(fatAugenfarbe.getValues(), charElem.getAugenfarbe());
			}
			
			// Würfel
			if ( charElem.getGroesseWuerfel() == null) {
				isNotDirty &= wstGroesse.getValues() == null;
			} else {
				isNotDirty &= Arrays.equals(
					wstGroesse.getValues().getAnzahlWuerfel(), 
					charElem.getGroesseWuerfel().getAnzahlWuerfel());
				isNotDirty &= Arrays.equals(
					wstGroesse.getValues().getAugenWuerfel(), 
					charElem.getGroesseWuerfel().getAugenWuerfel());
				isNotDirty &= wstGroesse.getValues().getFestWert() 
								== charElem.getGroesseWuerfel().getFestWert();
			}
			
			if ( charElem.getAlterWuerfel() == null) {
				isNotDirty &= wstAlter.getValues() == null;
			} else {
				isNotDirty &= Arrays.equals(
					wstAlter.getValues().getAnzahlWuerfel(), 
					charElem.getAlterWuerfel().getAnzahlWuerfel());
				isNotDirty &= Arrays.equals(
					wstAlter.getValues().getAugenWuerfel(), 
					charElem.getAlterWuerfel().getAugenWuerfel());
				isNotDirty &= wstAlter.getValues().getFestWert() 
								== charElem.getAlterWuerfel().getFestWert();
			}

			isNotDirty &= cobArt.getText().equals(charElem.getArt().toString());
			isNotDirty &= spiGewicht.getSelection() == charElem.getGewichtModi();
			isNotDirty &= spiGs.getSelection() == charElem.getGeschwindigk();
			
			return !isNotDirty;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void loadData(Rasse charElem) {
			
			// Kulturen
			cbxIsNegativListe.setSelection(charElem.isKulturenSindNegativListe());
			if (charElem.getKulturMoeglich() != null) {
				for (int i = 0; i < charElem.getKulturMoeglich().length; i++) {
					dropTableKulturMoeglich.addValue(charElem.getKulturMoeglich()[i]);
				}
			}
			if (charElem.getKulturUeblich() != null) {
				for (int i = 0; i < charElem.getKulturUeblich().length; i++) {
					dropTableKulturUeblich.addValue(charElem.getKulturUeblich()[i]);
				}
			}

			// Würfel
			wstGroesse.setValues(charElem.getGroesseWuerfel());
			wstAlter.setValues(charElem.getAlterWuerfel());
			
			// Farben
			fatHaarfarbe.setValues(charElem.getHaarfarbe());
			fatAugenfarbe.setValues(charElem.getAugenfarbe());
			
			cobArt.setText(charElem.getArt().toString());
			spiGewicht.setSelection(charElem.getGewichtModi());
			spiGs.setSelection(charElem.getGeschwindigk());
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void saveData(IProgressMonitor monitor, Rasse charElem) {
			monitor.subTask("Save Rassen-Data"); //$NON-NLS-1$
			
			// Kulturen
			charElem.setKulturenSindNegativListe(cbxIsNegativListe.getSelection());
			List l = dropTableKulturMoeglich.getValueList();
			if (l.size() == 0) {
				charElem.setKulturMoeglich(null);
			} else {
				Kultur[] kulturen = new Kultur[l.size()];
				for (int i = 0; i < kulturen.length; i++) {
					kulturen[i] = (Kultur) l.get(i);
				}
				charElem.setKulturMoeglich(kulturen);
			}
			l = dropTableKulturUeblich.getValueList();
			if (l.size() == 0) {
				charElem.setKulturUeblich(null);
			} else {
				Kultur[] kulturen = new Kultur[l.size()];
				for (int i = 0; i < kulturen.length; i++) {
					kulturen[i] = (Kultur) l.get(i);
				}
				charElem.setKulturUeblich(kulturen);
			}

			// Würfel
			charElem.setGroesseWuerfel(wstGroesse.getValues());
			charElem.setAlterWuerfel(wstAlter.getValues());
			
			// Farben
			charElem.setHaarfarbe(fatHaarfarbe.getValues());
			charElem.setAugenfarbe(fatAugenfarbe.getValues());
			
			charElem.setGewichtModi(spiGewicht.getSelection());
			charElem.setGeschwindigk(spiGs.getSelection());
			
			for (int i = 0; i < Rasse.RasseArt.values().length; i++) {
				if (cobArt.getText().equals(Rasse.RasseArt.values()[i].toString())) {
					charElem.setArt(Rasse.RasseArt.values()[i]);
					break;
				}
			}
			
			monitor.worked(1);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#addCharElementSiteParts(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		herkunftPart = new HerkunftPart(mainContainer);
		herkunftPart.loadData((Herkunft) getEditedCharElement());
		herkunftPart.loadHerkunftTrees(this.getContainer(), this.getSite(), (Herkunft) getEditedCharElement());

		rassePart = new RassePart(mainContainer, this.getSite(), 15, 150);
		rassePart.loadData((Rasse) getEditedCharElement());
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		int index = addPage(createCharElementSite());
		setPageText(index, EditorMessages.Editor_Daten);
		
		index = addPage(herkunftPart.getModisAuswahl().getTree());
		setPageText(index, "Modifikationen");
		
		index = addPage(herkunftPart.getAltervativeZauberPart().getControl());
		setPageText(index, "Alternative Zauberauswahl");
		
		index = addPage(herkunftPart.getVerbilligtPart().getTree());
		setPageText(index, "Verbilligungen");
		
		index = addPage(herkunftPart.getEmpfehlungenPart().getTree());
		setPageText(index, "Empfehlungen");
		
		index = addPage(herkunftPart.getAktivierbareZauberPart().getTree());
		setPageText(index, "Aktivierbare Zauber");
		
		index = addPage(createVoraussetzungsSite());
		setPageText(index, EditorMessages.Editor_Voraussetzungen);
		
		elementPartArray = new AbstractElementPart[] {
				charElementPart,
				herkunftPart,
				rassePart,
				herkunftPart.getModisAuswahl(), 
				herkunftPart.getVerbilligtPart(),
				herkunftPart.getEmpfehlungenPart(),
				herkunftPart.getAktivierbareZauberPart(),
				voraussetzungsPart};
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getEditedCharElement()
	 */
	@Override
	protected CharElement getEditedCharElement() {
		return (Rasse) this.getEditorInput().getAdapter(Rasse.class);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getElementParts()
	 */
	@Override
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}
	
}
