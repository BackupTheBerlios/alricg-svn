/*
 * Created 26.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import java.util.List;

import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Faehigkeit;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Stellt Controls für die Klasse Faehigkeit bereit und verwaltete diese.
 * @author Vincent
 */
public class FaehigkeitPart extends AbstarctElementPart<Faehigkeit> {
	private Composite compProbe;
	private Combo cobEig1;
	private Combo cobEig2;
	private Combo cobEig3;
	private Combo cobSKT;
	
	public FaehigkeitPart(Composite top, GridData gridData) {

		Label lblProbe = new Label(top, SWT.NONE);
		lblProbe.setText("Probe:");
		
		compProbe = new Composite(top, SWT.NONE);
		RowLayout rl = new RowLayout();
		rl.marginLeft = 0;
		compProbe.setLayout(rl);
		
		cobEig1 = new Combo(compProbe, SWT.READ_ONLY | SWT.CENTER);
		configProbeCombo(cobEig1);
		Label label1 = new Label(compProbe, SWT.NONE);
		label1.setText(" / ");
		cobEig2 = new Combo(compProbe, SWT.READ_ONLY);
		configProbeCombo(cobEig2);
		Label label2 = new Label(compProbe, SWT.NONE);
		label2.setText(" / ");
		cobEig3 = new Combo(compProbe, SWT.READ_ONLY);
		configProbeCombo(cobEig3);
		
		Label lblSKT = new Label(top, SWT.NONE);
		lblSKT.setText("SKT-Spalte:");
		lblSKT.setToolTipText("Steigerungskosten-Tabellen Spalte");
		
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
	
	private void configProbeCombo(Combo combo) {
		combo.setLayoutData(new RowData(20, -1));
		combo.setVisibleItemCount(8);
		
		combo.add(EigenschaftEnum.MU.getAbk());
		combo.add(EigenschaftEnum.KL.getAbk());
		combo.add(EigenschaftEnum.IN.getAbk());
		combo.add(EigenschaftEnum.CH.getAbk());
		combo.add(EigenschaftEnum.FF.getAbk());
		combo.add(EigenschaftEnum.GE.getAbk());
		combo.add(EigenschaftEnum.KO.getAbk());
		combo.add(EigenschaftEnum.KK.getAbk());
		combo.select(0);
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void loadData(Faehigkeit charElem) {
		Eigenschaft[] eigArray = charElem.getDreiEigenschaften();
		
		ViewUtils.findAndSetIndex(cobEig1, eigArray[0].getEigenschaftEnum().getAbk());
		ViewUtils.findAndSetIndex(cobEig2, eigArray[1].getEigenschaftEnum().getAbk());
		ViewUtils.findAndSetIndex(cobEig3, eigArray[2].getEigenschaftEnum().getAbk());
		
		ViewUtils.findAndSetIndex(cobSKT, charElem.getKostenKlasse().getValue());
	}


	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#saveData(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void saveData(IProgressMonitor monitor,Faehigkeit charElem) {
		monitor.subTask("Save Faehigkeit-Data");
		
		List<Eigenschaft> eigenList = StoreDataAccessor.getInstance().getEigenschaftList();
		
		// Set 3 Eigenschaften
		Eigenschaft[] dreiEigenArray = new Eigenschaft[3];
		for (int i = 0; i < eigenList.size(); i++) {
			String abk = eigenList.get(i).getEigenschaftEnum().getAbk();
			if (abk.equals(cobEig1.getText()) ) {
				dreiEigenArray[0] = eigenList.get(i);
			}
			if (abk.equals(	cobEig1.getText() )) {
				dreiEigenArray[1] = eigenList.get(i);
			}
			if (abk.equals(	cobEig1.getText() )) {
				dreiEigenArray[2] = eigenList.get(i);
			}
		}
		charElem.setDreiEigenschaften(dreiEigenArray);
		
		// Set SKT
		for (int i = 0; i < KostenKlasse.values().length; i++) {
			if (KostenKlasse.values()[i].getValue().equals(cobSKT.getText()) ) {
				charElem.setKostenKlasse(KostenKlasse.values()[i]);
				break;
			}
		}
	}
	
	

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean isDirty(Faehigkeit charElem) {
		boolean isNotDirty = true;
		
		Eigenschaft[] eigArray = charElem.getDreiEigenschaften();

		isNotDirty &= cobEig1.getText().equals(eigArray[0].getEigenschaftEnum().getAbk());
		isNotDirty &= cobEig2.getText().equals(eigArray[1].getEigenschaftEnum().getAbk());
		isNotDirty &= cobEig3.getText().equals(eigArray[2].getEigenschaftEnum().getAbk());
		isNotDirty &= cobSKT.getText().equals(charElem.getKostenKlasse().getValue());
		
		return !isNotDirty;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#dispose()
	 */
	@Override
	public void dispose() {
		compProbe.dispose();
	}
	
	

}
