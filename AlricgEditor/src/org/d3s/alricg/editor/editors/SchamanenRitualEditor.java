/*
 * Created 31.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import org.d3s.alricg.editor.common.CustomColumnLabelProvider.ImageProviderRegulator;
import org.d3s.alricg.editor.common.widgets.DropTable;
import org.d3s.alricg.editor.common.widgets.DropTable.DropListRegulator;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Repraesentation;
import org.d3s.alricg.store.charElemente.SchamanenRitual;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author Vincent
 *
 */
public class SchamanenRitualEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.SchamanenRitualEditor";
	
	private SchamanenRitualPart ritualPart;
	private AbstractElementPart[] elementPartArray;
	
	class SchamanenRitualPart extends AbstractElementPart<SchamanenRitual> {
		private final Spinner spiGrad;
		private final DropTable dropTableRep;
		
		SchamanenRitualPart(Composite top) {
			
			// Grad
			final Label lblGrad = new Label(top, SWT.NONE);
			lblGrad.setText("Grad:");
			spiGrad = new Spinner (top, SWT.BORDER);
			spiGrad.setMinimum(1);
			spiGrad.setMaximum(6);
			spiGrad.setSelection(1);
			spiGrad.setIncrement(1);
			spiGrad.setPageIncrement(1);
			
			// Herkunft
			Label lblHerkunft = new Label(top, SWT.NONE);
			lblHerkunft.setText("Verbreitung:");
			GridData tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			tmpGData.verticalIndent = 10;
			lblHerkunft.setLayoutData(tmpGData);
			
			final DropListRegulator regulator = new DropListRegulator() {
				@Override
				public boolean canDrop(Object obj) {
					if (obj == null) return false;
					if (obj instanceof Repraesentation) {
						return ((Repraesentation) obj).isSchamanischeRep();
					}
					return false;
				}

				@Override
				public ImageProviderRegulator getImageProviderRegulator() {
					return null;
				}
			};
			
			dropTableRep = new DropTable(top, SWT.NONE, regulator, SchamanenRitualEditor.this.getSite());
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
		public boolean isDirty(SchamanenRitual charElem) {
			boolean isNotDirty = true;
			
			isNotDirty &= charElem.getGrad() == spiGrad.getSelection();
			isNotDirty &= compareArrayList(charElem.getHerkunft(), dropTableRep.getValueList());

			return !isNotDirty;
		}
		
		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void loadData(SchamanenRitual charElem) {
			spiGrad.setSelection(charElem.getGrad());
			if (charElem.getHerkunft() == null) return;
			
			for (int i = 0; i < charElem.getHerkunft().length; i++) {
				dropTableRep.addValue(charElem.getHerkunft()[i]);
			}			
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void saveData(IProgressMonitor monitor, SchamanenRitual charElem) {
			monitor.subTask("Save SchamanenRitual-Data"); //$NON-NLS-1$
			
			charElem.setGrad(spiGrad.getSelection());
			if (dropTableRep.getValueList().size() == 0) {
				charElem.setHerkunft(null);
			} else {
				Repraesentation[] repAr = (Repraesentation[]) dropTableRep.getValueList()
													.toArray(new Repraesentation[0]);
				charElem.setHerkunft(repAr);
			}
			
			monitor.worked(1);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#addCharElementSiteParts(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		ritualPart = new SchamanenRitualPart(mainContainer);
		ritualPart.loadData( (SchamanenRitual) getEditedCharElement());
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		int index = addPage(createCharElementSite());
		setPageText(index, EditorMessages.Editor_Daten);
		
		elementPartArray = new AbstractElementPart[] {
				charElementPart, ritualPart};

	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getEditedCharElement()
	 */
	@Override
	protected CharElement getEditedCharElement() {
		return (SchamanenRitual) this.getEditorInput().getAdapter(SchamanenRitual.class);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getElementParts()
	 */
	@Override
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}

}
