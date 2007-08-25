/*
 * Created 23.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Schrift;
import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author Vincent
 */
public class SchriftEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.SchriftEditor"; //$NON-NLS-1$

	private SchriftPart schriftPart;
	private AbstractElementPart[] elementPartArray;
	
	class SchriftPart extends AbstractElementPart<Schrift> {
		private Spinner spiKompl;
		private Combo cobSKT;
		
		SchriftPart(Composite top) {
			
			// Komplexität
			final Label lblAnzahl = new Label(top, SWT.NONE);
			lblAnzahl.setText("Komplexität:");
			spiKompl = new Spinner (top, SWT.BORDER);
			spiKompl.setMinimum(1);
			spiKompl.setMaximum(100);
			spiKompl.setSelection(1);
			spiKompl.setIncrement(1);
			spiKompl.setPageIncrement(10);
			
			// Kostenklasse
			Label lblSKT = new Label(top, SWT.NONE);
			lblSKT.setText(EditorMessages.FaehigkeitPart_SKT);
			lblSKT.setToolTipText(EditorMessages.FaehigkeitPart_SKT_TT);
			cobSKT = new Combo(top, SWT.READ_ONLY);
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
		}
		
		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#dispose()
		 */
		@Override
		public void dispose() {
			// Noop
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public boolean isDirty(Schrift charElem) {
			boolean isNotDirty = true;
			
			isNotDirty &= cobSKT.getText().equals(charElem.getKostenKlasse().getValue());
			isNotDirty &= spiKompl.getSelection() == charElem.getKomplexitaet();
			
			return !isNotDirty;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void loadData(Schrift charElem) {
			cobSKT.setText(charElem.getKostenKlasse().getValue());
			spiKompl.setSelection(charElem.getKomplexitaet());
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void saveData(IProgressMonitor monitor, Schrift charElem) {
			monitor.subTask("Save Schrift-Data"); //$NON-NLS-1$
			
			// Komplexität
			charElem.setKomplexitaet(spiKompl.getSelection());
			
			// Set SKT
			for (int i = 0; i < KostenKlasse.values().length; i++) {
				if (KostenKlasse.values()[i].getValue().equals(cobSKT.getText()) ) {
					charElem.setKostenKlasse(KostenKlasse.values()[i]);
					break;
				}
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
				charElementPart, schriftPart, voraussetzungsPart};
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#addCharElementSiteParts(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		schriftPart = new SchriftPart(mainContainer);
		schriftPart.loadData( (Schrift) getEditedCharElement());
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getEditedCharElement()
	 */
	@Override
	protected CharElement getEditedCharElement() {
		return (Schrift) this.getEditorInput().getAdapter(Schrift.class);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getElementParts()
	 */
	@Override
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}



}
