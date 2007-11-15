/*
 * Created 31.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import org.d3s.alricg.common.logic.FormelSammlung;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider.ImageProviderRegulator;
import org.d3s.alricg.editor.common.widgets.DropTable;
import org.d3s.alricg.editor.common.widgets.DropTable.DropListRegulator;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.RegionVolk;
import org.d3s.alricg.store.charElemente.charZusatz.Gegenstand;
import org.d3s.alricg.store.charElemente.charZusatz.Gegenstand.GegenstandArt;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author Vincent
 *
 */
public class GegenstandEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.GegenstandEditor";

	private GegenstandPart gegenstandPart;
	private AbstractElementPart[] elementPartArray;
	
	class GegenstandPart extends AbstractElementPart<Gegenstand> {
		private final Combo cobArt;
		private final DropTable dropTableRegionen;
		private final Spinner spiDukaten;
		private final Spinner spiSilber;
		private final Spinner spiHeller;
		private final Spinner spiKreuzer;
		
		GegenstandPart(Composite top) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 4;
			
		// Vornamen
			GridData tmpGData = new GridData(GridData.GRAB_HORIZONTAL);
			tmpGData.widthHint = 600;
			tmpGData.horizontalSpan = 2; // nimm 2 Spalten Platz ein
			
			final Group groupWert = new Group(top, SWT.SHADOW_IN);
			groupWert.setText("Wert");
			groupWert.setLayout(gridLayout);
			groupWert.setLayoutData(tmpGData);
			
			// Geld
			final Label lblDukaten = new Label(groupWert, SWT.NONE);
			lblDukaten.setText("Dukaten:");
			lblDukaten.setToolTipText("1 Dukate = 10 Silbertaler");
			spiDukaten = new Spinner (groupWert, SWT.BORDER);
			spiDukaten.setValues(0, 0, 99999, 0, 1, 50);
			
			final Label lblSilber = new Label(groupWert, SWT.NONE);
			lblSilber.setText("Silbertaler:");
			lblSilber.setToolTipText("1 Silbertaler = 10 Heller");
			spiSilber = new Spinner (groupWert, SWT.BORDER);
			spiSilber.setValues(0, 0, 1000, 0, 1, 10);
			
			final Label lblHeller = new Label(groupWert, SWT.NONE);
			lblHeller.setText("Heller:");
			lblHeller.setToolTipText("1 Heller = 10 Kreuzer");
			spiHeller = new Spinner (groupWert, SWT.BORDER);
			spiHeller.setValues(0, 0, 1000, 0, 1, 10);
			
			final Label lblKreuzer = new Label(groupWert, SWT.NONE);
			lblKreuzer.setText("Kreuzer:");
			lblKreuzer.setToolTipText("Kleinste Einheit");
			spiKreuzer = new Spinner (groupWert, SWT.BORDER);
			spiKreuzer.setValues(0, 0, 1000, 0, 1, 10);
			
			final Label lblArt = new Label(top, SWT.NONE);
			lblArt.setText("Art:");
			cobArt = new Combo(top, SWT.READ_ONLY | SWT.CENTER);
			for (int i = 0; i < GegenstandArt.values().length; i++) {
				cobArt.add(GegenstandArt.values()[i].toString());
			}
			cobArt.setVisibleItemCount(8);
			cobArt.select(GegenstandArt.values().length-1);
			
			final Label label1 = new Label(top, SWT.NONE);
			label1.setText("Verbeitet in:");
			label1.setToolTipText("Typische Regionen und Völker bei denen der Gegenstand in Gebrauch ist.");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			tmpGData.verticalIndent = 10;
			label1.setLayoutData(tmpGData);
			
			final DropListRegulator regulator = new DropListRegulator() {
				@Override
				public boolean canDrop(Object obj) {
					if (obj == null) return false;
					if (obj instanceof RegionVolk) {
						return true;
					}
					return false;
				}

				@Override
				public ImageProviderRegulator getImageProviderRegulator() {
					return null;
				}
			};
			
			dropTableRegionen = new DropTable(top, SWT.NONE, regulator, GegenstandEditor.this.getSite());
		}
		
		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#dispose()
		 */
		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public boolean isDirty(Gegenstand charElem) {
			boolean isNotDirty = true;

			int[] muenzenArray = FormelSammlung.getWertInMuenzen( charElem.getWertInKreuzern() );
			isNotDirty &= spiKreuzer.getSelection() == muenzenArray[0];
			isNotDirty &= spiHeller.getSelection() == muenzenArray[1];
			isNotDirty &= spiSilber.getSelection() == muenzenArray[2];
			isNotDirty &= spiDukaten.getSelection() == muenzenArray[3];
			
			isNotDirty &= cobArt.getText().equals(charElem.getArt().toString());
			
			isNotDirty &= compareArrayList(charElem.getHerkunft(), dropTableRegionen.getValueList());
			
			return !isNotDirty;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void loadData(Gegenstand charElem) {
			cobArt.setText(charElem.getArt().toString());
			
			int[] muenzenArray = FormelSammlung.getWertInMuenzen( charElem.getWertInKreuzern() );
			spiKreuzer.setSelection(muenzenArray[0]);
			spiHeller.setSelection(muenzenArray[1]);
			spiSilber.setSelection(muenzenArray[2]);
			spiDukaten.setSelection(muenzenArray[3]);
			
			if (charElem.getHerkunft() == null) return;
			for (int i = 0; i < charElem.getHerkunft().length; i++) {
				dropTableRegionen.addValue(charElem.getHerkunft()[i]);
			}
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void saveData(IProgressMonitor monitor, Gegenstand charElem) {
			monitor.subTask("Save Gegenstand-Data"); //$NON-NLS-1$
			
			int[] muenzenArray = new int[] {
					spiKreuzer.getSelection(),
					spiHeller.getSelection(),
					spiSilber.getSelection(),
					spiDukaten.getSelection() };
			charElem.setWertInKreuzern( FormelSammlung.getWertInKreuzern(muenzenArray) );
			
			for (int i = 0; i < GegenstandArt.values().length; i++) {
				if (GegenstandArt.values()[i].toString().equals(cobArt.getText())) {
					charElem.setArt(GegenstandArt.values()[i]);
					break;
				}
			}
			
			if (dropTableRegionen.getValueList().size() == 0) {
				charElem.setHerkunft(null);
			} else {
				RegionVolk[] regAr = (RegionVolk[]) dropTableRegionen.getValueList()
													.toArray(new RegionVolk[0]);
				charElem.setHerkunft(regAr);
			}
			
			monitor.worked(1);
		}
		
	}
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#addCharElementSiteParts(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		// Gegenstand spezifische Elemte erzeugen
		gegenstandPart = this.new GegenstandPart(mainContainer);
		gegenstandPart.loadData((Gegenstand) getEditedCharElement());	

	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		int index = addPage(createCharElementSite());
		setPageText(index, EditorMessages.Editor_Daten);
		
		elementPartArray = new AbstractElementPart[] {
				charElementPart, gegenstandPart};
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getEditedCharElement()
	 */
	@Override
	protected CharElement getEditedCharElement() {
		return (Gegenstand) this.getEditorInput().getAdapter(Gegenstand.class);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getElementParts()
	 */
	@Override
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}

}
