/*
 * Created 23.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.d3s.alricg.editor.common.widgets.SpinnerNeg;
import org.d3s.alricg.editor.common.widgets.TextList;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Fertigkeit;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Fertigkeit.AdditionsFamilie;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

/**
 * @author Vincent
 *
 */
public class FertigkeitPart extends AbstractElementPart<Fertigkeit> {
	
	// Spezielle behandlung, da das "Eingebette" Talent gemeint ist
	private final FaehigkeitPart faehigKeitPart;
	private final Text txtAutoTalName;
	private final Button cbxAutoTalent;
	
	private final Combo cobAdditionsID;
	private final Spinner spiAdditionsWert;
	private final Combo cobArt;
	private final Button cbxZweitZiel;
	private final Button cbxFreiText;
	private final TextList textList;
	private final SpinnerNeg spiGpKosten;
	
	public FertigkeitPart(final Composite top) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		
		// Additions Famile
		GridData tmpGData = new GridData(GridData.GRAB_HORIZONTAL);
		tmpGData.widthHint = 600;
		tmpGData.horizontalSpan = 2; // nimm 2 Spalten Platz ein
		
		final Group groupAddition = new Group(top, SWT.SHADOW_IN);
		groupAddition.setText("Additions Familie");
		groupAddition.setLayout(gridLayout);
		groupAddition.setLayoutData(tmpGData);
		
		final Label lblLabel1 = new Label(groupAddition, SWT.NONE);
		lblLabel1.setText("Additions-ID:");
		cobAdditionsID = new Combo(groupAddition, SWT.BORDER | SWT.DROP_DOWN);
		tmpGData = new GridData();
		tmpGData.widthHint = 50;
		cobAdditionsID.setLayoutData(tmpGData);
		cobAdditionsID.setVisibleItemCount(8);

		
		final Label lblLabel2 = new Label(groupAddition, SWT.NONE);
		lblLabel2.setText("Additions-Wert:");
		spiAdditionsWert = new Spinner (groupAddition, SWT.BORDER);
		spiAdditionsWert.setMinimum(1);
		spiAdditionsWert.setMaximum(10);
		spiAdditionsWert.setSelection(1);
		spiAdditionsWert.setIncrement(1);
		spiAdditionsWert.setPageIncrement(1);

		// Automatisches Talent
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		
		tmpGData = new GridData(GridData.GRAB_HORIZONTAL);
		tmpGData.widthHint = 600;
		tmpGData.horizontalSpan = 2; // nimm 2 Spalten Platz ein
		
		Group groupAutoTalent = new Group(top, SWT.SHADOW_IN);
		groupAutoTalent.setText("Automatisches Talent");
		groupAutoTalent.setLayout(gridLayout);
		groupAutoTalent.setLayoutData(tmpGData);
		
		final Label lblFiller = new Label(groupAutoTalent, SWT.NONE);
		cbxAutoTalent = new Button(groupAutoTalent, SWT.CHECK);
		cbxAutoTalent.setText("Bei Auswahl wird automatisch ein Talent hinzugefühgt");
		cbxAutoTalent.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				txtAutoTalName.setEnabled(cbxAutoTalent.getSelection());
				faehigKeitPart.setEnabledAll(cbxAutoTalent.getSelection());
			}
		});
		
		final Label lblAutoName = new Label(groupAutoTalent, SWT.NONE);
		lblAutoName.setText("Name:");
		txtAutoTalName = new Text(groupAutoTalent, SWT.BORDER);
		txtAutoTalName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		faehigKeitPart = new FaehigkeitPart(groupAutoTalent);
		
		// Fertigkeit
		final Label lblLabel3 = new Label(top, SWT.NONE);
		lblLabel3.setText("Art:");
		cobArt = new Combo(top, SWT.READ_ONLY | SWT.CENTER);
		cobArt.add(Fertigkeit.FertigkeitArt.allgemein.getValue());
		cobArt.add(Fertigkeit.FertigkeitArt.bewaffnetkampf.getValue());
		cobArt.add(Fertigkeit.FertigkeitArt.waffenloskampf.getValue());
		cobArt.add(Fertigkeit.FertigkeitArt.magisch.getValue());
		cobArt.add(Fertigkeit.FertigkeitArt.geweiht.getValue());
		cobArt.add(Fertigkeit.FertigkeitArt.schamanisch.getValue());
		cobArt.add(Fertigkeit.FertigkeitArt.sonstige.getValue());
		cobArt.setVisibleItemCount(7);
		cobArt.select(0);
		
		final Label lblLabel4 = new Label(top, SWT.NONE);
		lblLabel4.setText("Benötigt:");
		cbxZweitZiel = new Button(top, SWT.CHECK);
		cbxZweitZiel.setText("Die Angabe eines ZweitZiels");

		final Label lblFiller2 = new Label(top, SWT.NONE);
		cbxFreiText = new Button(top, SWT.CHECK);
		cbxFreiText.setText("Die Angabe eines freien Textes");
		
		final Label lblLabel6 = new Label(top, SWT.NONE);
		lblLabel6.setText("Text-Vorschläge:");
		tmpGData = new  GridData();
		tmpGData.verticalAlignment = GridData.BEGINNING;
		tmpGData.verticalIndent = 12;
		lblLabel6.setLayoutData(tmpGData);
		textList = new TextList(top, SWT.NONE);
		
		final Label lblLabel7 = new Label(top, SWT.NONE);
		lblLabel7.setText("GP-Kosten:");
		spiGpKosten = new SpinnerNeg (top, SWT.NONE);
		// set the increment value to 0.5
		spiGpKosten.setStep(0.5);
		spiGpKosten.setMinimum(-200);
		spiGpKosten.setMaximum(200);
		spiGpKosten.setSelection(1);
		spiGpKosten.setPageStep(5d);
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
	public boolean isDirty(Fertigkeit charElem) {
		boolean isNotDirty = true;
		
		// Additions-Familie
		if (charElem.getAdditionsFamilie() != null) {
			if (cobAdditionsID.getText().trim().length() == 0) return true;
			
			isNotDirty &= cobAdditionsID.getText().trim().equals(
					charElem.getAdditionsFamilie().getAdditionsID());
			isNotDirty &= spiAdditionsWert.getSelection() == 
					charElem.getAdditionsFamilie().getAdditionsWert();
		} else if (cobAdditionsID.getText().trim().length() > 0) {
			return true;
		}
		
		isNotDirty &= cobArt.getText().equals(charElem.getArt().getValue());
		isNotDirty &= cbxZweitZiel.getSelection() == charElem.getBenoetigtZweitZiel();
		isNotDirty &= cbxFreiText.getSelection() == charElem.isMitFreienText();
		isNotDirty &= spiGpKosten.getSelection() == charElem.getGpKosten();
		
		// Text Vorschläge
		if (charElem.getTextVorschlaege() != null) {
			isNotDirty &= Arrays.equals(textList.getValueList(), charElem.getTextVorschlaege());
		} else if (textList.getValueList().length > 0) {
			return true;
		}
		
		// Automatisches Talent
		if (charElem.getAutomatischesTalent() != null) {
			if (!cbxAutoTalent.getSelection()) return true;
			
			isNotDirty &= txtAutoTalName.getText().equals(charElem.getAutomatischesTalent().getName());
			isNotDirty &= faehigKeitPart.isDirty(charElem.getAutomatischesTalent());
		} else {
			if (cbxAutoTalent.getSelection()) return true;
		}
		
		return !isNotDirty;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void saveData(IProgressMonitor monitor, Fertigkeit charElem) {
		monitor.subTask("Save Fertigkeit-Data"); //$NON-NLS-1$
		
		// Additions-Familie
		if (cobAdditionsID.getText().trim().length() > 0) {
			AdditionsFamilie addFam = new AdditionsFamilie();
			addFam.setAdditionsID(cobAdditionsID.getText().trim());
			addFam.setAdditionsWert(spiAdditionsWert.getSelection());
			charElem.setAdditionsFamilie(addFam);
		} else {
			charElem.setAdditionsFamilie(null);
		}
		
		// Art
		for (int i = 0; i < Fertigkeit.FertigkeitArt.values().length; i++) {
			if ( cobArt.getText().equals(Fertigkeit.FertigkeitArt.values()[i].getValue()) ) {
				charElem.setArt(Fertigkeit.FertigkeitArt.values()[i]);
			}
		}
		
		// Benötigt und GP
		charElem.setBenoetigtZweitZiel(this.cbxZweitZiel.getSelection());
		charElem.setMitFreienText(this.cbxFreiText.getSelection());
		charElem.setGpKosten(this.spiGpKosten.getSelection());
		
		// Vorschläge
		if (this.textList.getValueList().length > 0) {
			charElem.setTextVorschlaege(textList.getValueList());
		} else {
			charElem.setTextVorschlaege(null);
		}
		
		// Automatisches Talent
		if (cbxAutoTalent.getSelection()) {
			Talent autoTalent = new Talent();
			autoTalent.setId(charElem.getId() + "_autotalent");
			autoTalent.setName(txtAutoTalName.getText());
			faehigKeitPart.saveData(monitor, autoTalent);
			charElem.setAutomatischesTalent(autoTalent);
		} else {
			charElem.setAutomatischesTalent(null);
		}
		
		monitor.worked(1);
	}
	
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void loadData(Fertigkeit charElem) {
		// Laden der Vorschläge für Addtions-Id
		loadAdditionsCombo(charElem);
		
		// Additions-Familie
		if ( charElem.getAdditionsFamilie() != null) {
			cobAdditionsID.setText(getStringFromNull(
					charElem.getAdditionsFamilie().getAdditionsID()));
			spiAdditionsWert.setSelection(charElem.getAdditionsFamilie().getAdditionsWert());
		}
		
		this.cobArt.setText(charElem.getArt().getValue());
		this.cbxZweitZiel.setSelection(charElem.getBenoetigtZweitZiel());
		this.cbxFreiText.setSelection(charElem.isMitFreienText());
		this.spiGpKosten.setSelection(charElem.getGpKosten());
		
		if (charElem.getTextVorschlaege() != null) {
			this.textList.setValues(charElem.getTextVorschlaege());
		}
		
		// Automatisches Talent
		if (charElem.getAutomatischesTalent() != null) {
			cbxAutoTalent.setSelection(true);
			txtAutoTalName.setText(charElem.getAutomatischesTalent().getName());
			faehigKeitPart.loadData(charElem.getAutomatischesTalent());
			
		} else {
			cbxAutoTalent.setSelection(false);
			txtAutoTalName.setEnabled(false);
			faehigKeitPart.setEnabledAll(false);
		}
		

	}

	private void loadAdditionsCombo(Fertigkeit charElem) {
		final List<String> familienList = new ArrayList<String>();
		
		List<? extends CharElement> charList = StoreDataAccessor.getInstance().getMatchingList(charElem.getClass());
		for (int i = 0; i < charList.size(); i++) {
			if ( ((Fertigkeit) charList.get(i)).getAdditionsFamilie() != null) {
				String addID = ((Fertigkeit) charList.get(i)).getAdditionsFamilie().getAdditionsID();
				if ( !familienList.contains(addID) ) {
					familienList.add(addID);
				}
			}
		}
		Collections.sort(familienList);
		cobAdditionsID.setItems(familienList.toArray(new String[familienList.size()]));
	}

	/**
	 * Fügt zu dem GpSpinner einen ModifyListener Hinzu 
	 * @param listener der ModifyListener
	 */
	public void addGpSpinnerModifyListener(ModifyListener listener) {
		spiGpKosten.addModifyListener(listener);
	}
}
