/*
 * Created 23.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import org.d3s.alricg.common.logic.FormelSammlung;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.editor.editors.composits.FertigkeitPart;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

/**
 * @author Vincent
 */
public class SonderfertigkeitEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.SonderfertigkeitEditor";
	
	private FertigkeitPart fertigkeitPart;
	private SonderfertigkeitPart sonderfPart;
	
	private AbstractElementPart[] elementPartArray;
	
	class SonderfertigkeitPart extends AbstractElementPart<Sonderfertigkeit> {
		private final Spinner spiLep;
		private final Spinner spiKep;
		private final Spinner spiAsp;
		private final Label lblAp;
		
		SonderfertigkeitPart(Composite top) {
			
			final Label lblFiller = new Label(top, SWT.NONE);
			lblAp = new Label(top, SWT.NONE);
			lblAp.setText("= 0 AP");
			lblAp.setToolTipText("Gibt an, wieviele Abenteuerpunkte die GP Kosten entsprechen");
			GridData tmpGData = new GridData(); 
			tmpGData.widthHint = 100;
			lblAp.setLayoutData(tmpGData);
			
			fertigkeitPart.addGpSpinnerModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					String text = ((Text) e.widget).getText();
					int ap = FormelSammlung.getApFromGp(Double.parseDouble(text));
					lblAp.setText("= " + ap + " AP");
				}
			});
			
			final Label lblLabel1 = new Label(top, SWT.NONE);
			lblLabel1.setText("Permanente Lep:");
			lblLabel1.setToolTipText("Permanente Kosten von LebensenErgiePunkten");
			spiLep = new Spinner(top, SWT.NONE);
			spiLep.setMinimum(0);
			spiLep.setMaximum(20);
			spiLep.setSelection(1);
			spiLep.setIncrement(1);
			spiLep.setPageIncrement(1);
			
			final Label lblLabel2 = new Label(top, SWT.NONE);
			lblLabel2.setText("Permanente Asp:");
			lblLabel2.setToolTipText("Permanente Kosten von AstralEnergiePunkten");
			spiAsp = new Spinner(top, SWT.NONE);
			spiAsp.setMinimum(0);
			spiAsp.setMaximum(20);
			spiAsp.setSelection(1);
			spiAsp.setIncrement(1);
			spiAsp.setPageIncrement(1);
			
			final Label lblLabel3 = new Label(top, SWT.NONE);
			lblLabel3.setText("Permanente Kep:");
			lblLabel3.setToolTipText("Permanente Kosten von KarmaEnergiePunkten");
			spiKep = new Spinner(top, SWT.NONE);
			spiKep.setMinimum(0);
			spiKep.setMaximum(20);
			spiKep.setSelection(1);
			spiKep.setIncrement(1);
			spiKep.setPageIncrement(1);
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
		public boolean isDirty(Sonderfertigkeit charElem) {
			boolean isNotDirty = true;

			isNotDirty &= spiLep.getSelection() == charElem.getPermLep();
			isNotDirty &= spiKep.getSelection() == charElem.getPermKep();
			isNotDirty &= spiAsp.getSelection() == charElem.getPermAsp();
			
			return !isNotDirty;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void loadData(Sonderfertigkeit charElem) {
			spiLep.setSelection(charElem.getPermLep());
			spiKep.setSelection(charElem.getPermKep());
			spiAsp.setSelection(charElem.getPermAsp());
			
			int ap = FormelSammlung.getApFromGp(charElem.getGpKosten());
			lblAp.setText("= " + ap + " AP");
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void saveData(IProgressMonitor monitor, Sonderfertigkeit charElem) {
			monitor.subTask("Save Sonderfertigkeit-Data"); //$NON-NLS-1$
			
			charElem.setPermLep(spiLep.getSelection());
			charElem.setPermAsp(spiAsp.getSelection());
			charElem.setPermKep(spiKep.getSelection());
			
			monitor.worked(1);
		}
		
	}


	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#addCharElementSiteParts(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		fertigkeitPart = new FertigkeitPart(mainContainer);
		fertigkeitPart.loadData( (Sonderfertigkeit) getEditedCharElement());
		
		sonderfPart = new SonderfertigkeitPart(mainContainer);
		sonderfPart.loadData( (Sonderfertigkeit) getEditedCharElement());
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		int index = addPage(createCharElementSite());
		setPageText(index, EditorMessages.Editor_Daten);
		
		index = addPage(createVoraussetzungsSite());
		setPageText(index, EditorMessages.Editor_Voraussetzungen);
		
		elementPartArray = new AbstractElementPart[] {
				charElementPart, fertigkeitPart, sonderfPart, voraussetzungsPart};

	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getEditedCharElement()
	 */
	@Override
	protected CharElement getEditedCharElement() {
		return (Sonderfertigkeit) this.getEditorInput().getAdapter(Sonderfertigkeit.class);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getElementParts()
	 */
	@Override
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}

}
