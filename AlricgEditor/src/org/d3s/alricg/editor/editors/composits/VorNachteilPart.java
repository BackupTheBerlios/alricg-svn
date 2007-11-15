/*
 * Created 25.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import org.d3s.alricg.editor.common.widgets.SpinnerNeg;
import org.d3s.alricg.store.charElemente.VorNachteil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author Vincent
 *
 */
public class VorNachteilPart extends AbstractElementPart<VorNachteil> {
	private final SpinnerNeg spiGpProStufe;
	private final Spinner spiMin;
	private final Spinner spiMax;
	
	public VorNachteilPart(final Composite top) {
		
		final Label lblLabel1 = new Label(top, SWT.NONE);
		lblLabel1.setText("+ GP pro Stufe:");
		spiGpProStufe = new SpinnerNeg (top, SWT.NONE);
		// set the increment value to 0.5
		spiGpProStufe.setStep(0.5);
		spiGpProStufe.setMinimum(-20);
		spiGpProStufe.setMaximum(20);
		spiGpProStufe.setSelection(1);
		spiGpProStufe.setPageStep(5d);
		
		final Label lblLabel2 = new Label(top, SWT.NONE);
		lblLabel2.setText("Minimale Stufe:");
		spiMin = new Spinner (top, SWT.BORDER);
		spiMin.setMinimum(0);
		spiMin.setMaximum(100);
		spiMin.setSelection(0);
		spiMin.setIncrement(1);
		spiMin.setPageIncrement(5);
		
		final Label lblLabel3 = new Label(top, SWT.NONE);
		lblLabel3.setText("Maximale Stufe:");
		spiMax = new Spinner (top, SWT.BORDER);
		spiMax.setMinimum(0);
		spiMax.setMaximum(100);
		spiMax.setSelection(0);
		spiMax.setIncrement(1);
		spiMax.setPageIncrement(5);
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
	public boolean isDirty(VorNachteil charElem) {
		boolean isNotDirty = true;
		
		isNotDirty &= charElem.getKostenProStufe() == spiGpProStufe.getSelection();
		isNotDirty &= charElem.getMinStufe() == spiMin.getSelection();
		isNotDirty &= charElem.getMaxStufe() == spiMax.getSelection();

		return !isNotDirty;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void loadData(VorNachteil charElem) {
		spiGpProStufe.setSelection(charElem.getKostenProStufe());
		spiMin.setSelection(charElem.getMinStufe());
		spiMax.setSelection(charElem.getMaxStufe());
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void saveData(IProgressMonitor monitor, VorNachteil charElem) {
		charElem.setKostenProStufe(spiGpProStufe.getSelection());
		charElem.setMinStufe(spiMin.getSelection());
		charElem.setMaxStufe(spiMax.getSelection());
	}

}
