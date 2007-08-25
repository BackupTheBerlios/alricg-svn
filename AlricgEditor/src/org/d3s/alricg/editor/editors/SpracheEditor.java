/*
 * Created 22.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.common.widgets.DropList;
import org.d3s.alricg.editor.common.widgets.DropList.DropListRegulator;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Schrift;
import org.d3s.alricg.store.charElemente.Sprache;
import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author Vincent
 *
 */
public class SpracheEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.SpracheEditor"; //$NON-NLS-1$

	private SprachePart sprachePart;
	private AbstractElementPart[] elementPartArray;
	

	class SprachePart extends AbstractElementPart<Sprache> {
		private Spinner spiKompl;
		private Spinner spiKomplNM;
		private Combo cobSKT;
		private Combo cobSKTNM;
		private Button cbxAbweichungMutterSpr;
		private DropList dropList;
		
		private Image imgDelete = ControlIconsLibrary.delete.getImageDescriptor().createImage();
		private Image imgUp = ControlIconsLibrary.arrowUp.getImageDescriptor().createImage();
		private Image imgDown = ControlIconsLibrary.arrowDown.getImageDescriptor().createImage();

		
		SprachePart(Composite top) {
			GridData tmpGData;
			
			
		// Komplexität
			final Label lblAnzahl = new Label(top, SWT.NONE);
			lblAnzahl.setText("Komplexität:");
			
			Composite tmpComp = new Composite(top, SWT.NONE);
			RowLayout tmpRl = new RowLayout();
			tmpRl.marginWidth = 0;
			tmpRl.marginHeight = 0;
			tmpRl.marginTop = 0;
			tmpRl.marginBottom = 0;
			tmpComp.setLayout(tmpRl);

			
			spiKompl = new Spinner (tmpComp, SWT.BORDER);
			spiKompl.setMinimum(1);
			spiKompl.setMaximum(100);
			spiKompl.setSelection(1);
			spiKompl.setIncrement(1);
			spiKompl.setPageIncrement(10);
			
			Label label1 = new Label(tmpComp, SWT.NONE);
			label1.setText(" / "); //$NON-NLS-1$
			
			spiKomplNM = new Spinner (tmpComp, SWT.BORDER);;
			spiKomplNM.setMinimum(1);
			spiKomplNM.setMaximum(100);
			spiKomplNM.setSelection(1);
			spiKomplNM.setIncrement(1);
			spiKomplNM.setPageIncrement(10);
			
		// Kostenklasse
			Label lblSKT = new Label(top, SWT.NONE);
			lblSKT.setText(EditorMessages.FaehigkeitPart_SKT);
			lblSKT.setToolTipText(EditorMessages.FaehigkeitPart_SKT_TT);
			
			tmpComp = new Composite(top, SWT.NONE);
			tmpRl = new RowLayout();
			tmpRl.marginWidth = 0;
			tmpRl.marginHeight = 0;
			tmpRl.marginTop = 0;
			tmpRl.marginBottom = 0;
			tmpComp.setLayout(tmpRl);
			
			cobSKT = new Combo(tmpComp, SWT.READ_ONLY);
			cobSKT.setVisibleItemCount(8);
			cobSKT.add(KostenKlasse.A.getValue());
			cobSKT.add(KostenKlasse.B.getValue());
			cobSKT.add(KostenKlasse.C.getValue());
			cobSKT.add(KostenKlasse.D.getValue());
			cobSKT.add(KostenKlasse.E.getValue());
			cobSKT.add(KostenKlasse.F.getValue());
			cobSKT.add(KostenKlasse.G.getValue());
			cobSKT.add(KostenKlasse.H.getValue());
			cobSKT.select(0);
			Label label2 = new Label(tmpComp, SWT.NONE);
			label2.setText(" / "); //$NON-NLS-1$
			cobSKTNM = new Combo(tmpComp, SWT.READ_ONLY);
			cobSKTNM.setVisibleItemCount(8);
			cobSKTNM.add(KostenKlasse.A.getValue());
			cobSKTNM.add(KostenKlasse.B.getValue());
			cobSKTNM.add(KostenKlasse.C.getValue());
			cobSKTNM.add(KostenKlasse.D.getValue());
			cobSKTNM.add(KostenKlasse.E.getValue());
			cobSKTNM.add(KostenKlasse.F.getValue());
			cobSKTNM.add(KostenKlasse.G.getValue());
			cobSKTNM.add(KostenKlasse.H.getValue());
			cobSKTNM.select(0);
			
		// CheckBox
			Label label3 = new Label(top, SWT.NONE);
			cbxAbweichungMutterSpr = new Button(top, SWT.CHECK);
			cbxAbweichungMutterSpr.setText("Andere Werte, wenn nicht Muttersprache.");
			cbxAbweichungMutterSpr.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					spiKomplNM.setEnabled(cbxAbweichungMutterSpr.getSelection());
					cobSKTNM.setEnabled(cbxAbweichungMutterSpr.getSelection());
				}
			});
			
		// Schriften
			Label label4 = new Label(top, SWT.NONE);
			label4.setText("Schriften:");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			tmpGData.verticalIndent = 10;
			label4.setLayoutData(tmpGData);
			
			DropListRegulator regulator = new DropListRegulator() {
				@Override
				public boolean canDrop(Object obj) {
					if (obj == null) return true;
					if (obj instanceof StructuredSelection
							&& ((StructuredSelection) obj).getFirstElement() 
									instanceof TreeOrTableObject 
							&& ((TreeOrTableObject) ((StructuredSelection) obj).getFirstElement()).getValue() 
									instanceof Schrift) {
						return true;
					}
					return false;
				}

				@Override
				public String getString(Object obj) {
					return ((Schrift) ((TreeOrTableObject) ((StructuredSelection) obj)
								.getFirstElement()).getValue()).getName();
				}

				@Override
				public Object getValue(Object obj) {
					return ((Schrift) ((TreeOrTableObject) ((StructuredSelection) obj)
							.getFirstElement()).getValue());
				}

			};
			
			dropList = new DropList(top, SWT.NONE, regulator);
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#dispose()
		 */
		@Override
		public void dispose() {
			imgDelete.dispose();
			imgUp.dispose();
			imgDown.dispose();
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public boolean isDirty(Sprache charElem) {
			boolean isNotDirty = true;
			
			isNotDirty &= cobSKT.getText().equals(charElem.getKostenKlasse().getValue());
			isNotDirty &= spiKompl.getSelection() == charElem.getKomplexitaet();
			
			// Nicht Muttersprache
			if (cbxAbweichungMutterSpr.getSelection()) {
				if (charElem.getWennNichtMuttersprache() == null) return true;
				
				isNotDirty &= cobSKTNM.getText().equals(
						charElem.getWennNichtMuttersprache().getKostenKlasse().getValue());
				isNotDirty &= spiKomplNM.getSelection() == 
						charElem.getWennNichtMuttersprache().getKomplexitaet();
				
			} else {
				if (charElem.getWennNichtMuttersprache() != null) return true;
			}
			
			// Schriften prüfen
			if (charElem.getZugehoerigeSchrift() == null) {
				if (dropList.getValueList().size() > 0) return true;
				
			} else if (charElem.getZugehoerigeSchrift() != null) {
				if (charElem.getZugehoerigeSchrift().length != dropList.getValueList().size()) {
					return true;
				}
				
				for (int i = 0; i < charElem.getZugehoerigeSchrift().length; i++) {
					isNotDirty &= charElem.getZugehoerigeSchrift()[i].equals(dropList.getValueList().get(i));
				}
			}
			
			return !isNotDirty;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void loadData(Sprache charElem) {
			cobSKT.setText( charElem.getKostenKlasse().getValue());
			spiKompl.setSelection(charElem.getKomplexitaet());
			
			// Nicht Muttersprache
			if (charElem.getWennNichtMuttersprache() != null) {
				cbxAbweichungMutterSpr.setSelection(true);
				cobSKTNM.setText(charElem.getWennNichtMuttersprache().getKostenKlasse().getValue());
				spiKomplNM.setSelection(
						charElem.getWennNichtMuttersprache().getKomplexitaet());
			} else {
				cbxAbweichungMutterSpr.setSelection(false);
				cobSKTNM.setEnabled(false);
				spiKomplNM.setEnabled(false);
			}
			
			// Schriften hinzufügen
			if (charElem.getZugehoerigeSchrift() != null) {
				for (int i = 0; i < charElem.getZugehoerigeSchrift().length; i++) {
					dropList.addValue(
							charElem.getZugehoerigeSchrift()[i].getName(), 
							charElem.getZugehoerigeSchrift()[i]);
				}
			}
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void saveData(IProgressMonitor monitor, Sprache charElem) {
			monitor.subTask("Save Sprache-Data"); //$NON-NLS-1$
			
			// Komplexität
			charElem.setKomplexitaet(spiKompl.getSelection());
			
			// Set SKT
			for (int i = 0; i < KostenKlasse.values().length; i++) {
				if (KostenKlasse.values()[i].getValue().equals(cobSKT.getText()) ) {
					charElem.setKostenKlasse(KostenKlasse.values()[i]);
					break;
				}
			}
			
			// "NichtMutterSprache" -Sprache erstellen
			if (cbxAbweichungMutterSpr.getSelection() 
					&& (spiKompl.getSelection() != spiKomplNM.getSelection()
							|| cobSKT.getSelectionIndex() != cobSKTNM.getSelectionIndex())) {
				Sprache sprache = new Sprache();
				sprache.setId(charElem.getId() + "_nichtmutterspr");
				sprache.setName(charElem.getName() + " (Nicht Muttersprache)");
				sprache.setKomplexitaet(spiKomplNM.getSelection());
				
				// Set SKT
				for (int i = 0; i < KostenKlasse.values().length; i++) {
					if (KostenKlasse.values()[i].getValue().equals(cobSKTNM.getText()) ) {
						sprache.setKostenKlasse(KostenKlasse.values()[i]);
						break;
					}
				}
				charElem.setWennNichtMuttersprache(sprache);
			} else {
				charElem.setWennNichtMuttersprache(null);
			}
			
			// Liste der Schriften setzen
			if (dropList.getValueList().size() > 0) {
				charElem.setZugehoerigeSchrift( (Schrift[])
						dropList.getValueList().toArray(
								new Schrift[dropList.getValueList().size()])
						);
			}
			
			monitor.worked(1);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		int index = addPage(createCharElementSite());
		setPageText(index, EditorMessages.Editor_Daten);
		
		index = addPage(createVoraussetzungsSite());
		setPageText(index, EditorMessages.Editor_Voraussetzungen);
		
		elementPartArray = new AbstractElementPart[] {
				charElementPart, sprachePart, voraussetzungsPart};
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#addCharElementSiteParts(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		sprachePart = new SprachePart(mainContainer);
		sprachePart.loadData( (Sprache) getEditedCharElement());
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getEditedCharElement()
	 */
	@Override
	protected CharElement getEditedCharElement() {
		return (Sprache) this.getEditorInput().getAdapter(Sprache.class);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getElementParts()
	 */
	@Override
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}
	
}
