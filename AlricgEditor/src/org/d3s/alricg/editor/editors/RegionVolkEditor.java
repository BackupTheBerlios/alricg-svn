/*
 * Created 31.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.RegionVolk;
import org.d3s.alricg.store.charElemente.RegionVolk.RegionVolkArt;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Vincent
 */
public class RegionVolkEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.RegionVolkEditor"; //$NON-NLS-1$

	private RegionVolkPart regionPart;
	private AbstractElementPart[] elementPartArray;
	
	class RegionVolkPart extends AbstractElementPart<RegionVolk> {
		private final Text txtVornM;
		private final Text txtVornW;
		private final Text txtBindewM;
		private final Text txtBindewW;
		private final Text txtNachn;
		private final Text txtEndungM;
		private final Text txtEndungW;
		private final Button cbxVornamenSindNachnamen;
		
		private final Text txtAbk;
		private final Combo cobArt;
		
		RegionVolkPart(Composite top) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			
		// Vornamen
			GridData tmpGData = new GridData(GridData.GRAB_HORIZONTAL);
			tmpGData.widthHint = 600;
			tmpGData.horizontalSpan = 2; // nimm 2 Spalten Platz ein
			
			final Group groupVornamen = new Group(top, SWT.SHADOW_IN);
			groupVornamen.setText("Vornamen");
			groupVornamen.setLayout(gridLayout);
			groupVornamen.setLayoutData(tmpGData);
			
			final Label lblVornM = new Label(groupVornamen, SWT.NONE);
			lblVornM.setText("Männlich:");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			lblVornM.setLayoutData(tmpGData);
			txtVornM = new Text(groupVornamen, SWT.BORDER | SWT.MULTI | SWT.WRAP);
			tmpGData = new GridData(GridData.FILL_BOTH); 
			tmpGData.heightHint = txtVornM.getLineHeight() * 3;
			txtVornM.setLayoutData(tmpGData);
			txtVornM.setToolTipText("Geben sie die Namen mit Komma getrennt ein.");
			
			final Label lblBindewM = new Label(groupVornamen, SWT.NONE);
			lblBindewM.setText("Bindewort M:");
			txtBindewM = new Text(groupVornamen, SWT.BORDER);
			txtBindewM.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			final Label lblVornW = new Label(groupVornamen, SWT.NONE);
			lblVornW.setText("Weiblich:");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			lblVornW.setLayoutData(tmpGData);
			txtVornW = new Text(groupVornamen, SWT.BORDER | SWT.MULTI | SWT.WRAP);
			tmpGData = new GridData(GridData.FILL_BOTH); 
			tmpGData.heightHint = txtVornW.getLineHeight() * 3;
			txtVornW.setLayoutData(tmpGData);
			txtVornW.setToolTipText("Geben sie die Namen mit Komma getrennt ein.");
			
			final Label lblBindewW = new Label(groupVornamen, SWT.NONE);
			lblBindewW.setText("Bindewort W:");
			txtBindewW = new Text(groupVornamen, SWT.BORDER);
			txtBindewW.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
		// Nachnamen
			tmpGData = new GridData(GridData.GRAB_HORIZONTAL);
			tmpGData.widthHint = 600;
			tmpGData.horizontalSpan = 2; // nimm 2 Spalten Platz ein
			
			final Group groupNachnamen = new Group(top, SWT.SHADOW_IN);
			groupNachnamen.setText("Nachnamen");
			groupNachnamen.setLayout(gridLayout);
			groupNachnamen.setLayoutData(tmpGData);
			
			final Label lblFiller = new Label(groupNachnamen, SWT.NONE);
			cbxVornamenSindNachnamen = new Button(groupNachnamen, SWT.CHECK);
			cbxVornamenSindNachnamen.setText("Vornamen sind gleichzeitig Nachnamen");
			cbxVornamenSindNachnamen.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					txtNachn.setEnabled(!cbxVornamenSindNachnamen.getSelection());
				}
			});
			cbxVornamenSindNachnamen.setSelection(false);
			
			final Label lblNachnamen = new Label(groupNachnamen, SWT.NONE);
			lblNachnamen.setText("Nachnamen:");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			lblNachnamen.setLayoutData(tmpGData);
			txtNachn = new Text(groupNachnamen, SWT.BORDER | SWT.MULTI | SWT.WRAP);
			tmpGData = new GridData(GridData.FILL_BOTH); 
			tmpGData.heightHint = txtNachn.getLineHeight() * 3;
			txtNachn.setLayoutData(tmpGData);
			txtNachn.setToolTipText("Geben sie die Namen mit Komma getrennt ein.");
			
			final Label lblEdnungM = new Label(groupNachnamen, SWT.NONE);
			lblEdnungM.setText("Endung M:");
			txtEndungM = new Text(groupNachnamen, SWT.BORDER);
			txtEndungM.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			final Label lblEdnungW = new Label(groupNachnamen, SWT.NONE);
			lblEdnungW.setText("Endung W:");
			txtEndungW = new Text(groupNachnamen, SWT.BORDER);
			txtEndungW.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
		// Weitere Angaben
			final Label lblArt = new Label(top, SWT.NONE);
			lblArt.setText("Art:");
			cobArt = new Combo(top, SWT.READ_ONLY | SWT.CENTER);
			cobArt.add(RegionVolkArt.menschlich.toString());
			cobArt.add(RegionVolkArt.nichtMenschlich.toString());
			cobArt.setVisibleItemCount(2);
			cobArt.select(0);
			
			final Label lblAbk = new Label(top, SWT.NONE);
			lblAbk.setText("Abkürzung:");
			txtAbk = new Text(top, SWT.BORDER);
			GridData dg = new GridData();
			dg.widthHint = 300;
			txtAbk.setLayoutData(dg);
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
		public boolean isDirty(RegionVolk charElem) {
			boolean isNotDirty = true;
			
			isNotDirty &= txtVornM.getText().equals(buildNamenString(charElem.getVornamenMann()));
			isNotDirty &= txtVornW.getText().equals(buildNamenString(charElem.getVornamenFrau()));
			isNotDirty &= txtNachn.getText().equals(buildNamenString(charElem.getNachnamen()));
			
			isNotDirty &= txtBindewM.getText().equals(getStringFromNull(charElem.getBindeWortMann()));
			isNotDirty &= txtBindewW.getText().equals(getStringFromNull(charElem.getBindeWortFrau()));
			isNotDirty &= txtEndungM.getText().equals(getStringFromNull(charElem.getEndWortMann()));
			isNotDirty &= txtEndungW.getText().equals(getStringFromNull(charElem.getEndWortFrau()));
			
			isNotDirty &= txtAbk.getText().equals(getStringFromNull(charElem.getAbk()));
			isNotDirty &= cobArt.getText().equals(charElem.getArt().toString());
			
			isNotDirty &= cbxVornamenSindNachnamen.getSelection() == charElem.isVornamenSindNachnamen();

			return !isNotDirty;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void loadData(RegionVolk charElem) {
			txtVornM.setText(buildNamenString(charElem.getVornamenMann()));
			txtVornW.setText(buildNamenString(charElem.getVornamenFrau()));
			txtNachn.setText(buildNamenString(charElem.getNachnamen()));
			
			txtBindewM.setText(getStringFromNull(charElem.getBindeWortMann()));
			txtBindewW.setText(getStringFromNull(charElem.getBindeWortFrau()));
			txtEndungM.setText(getStringFromNull(charElem.getEndWortMann()));
			txtEndungW.setText(getStringFromNull(charElem.getEndWortFrau()));
			
			txtAbk.setText(getStringFromNull(charElem.getAbk()));
			cobArt.setText(charElem.getArt().toString());
			
			if (charElem.isVornamenSindNachnamen()) {
				cbxVornamenSindNachnamen.setSelection(true);
				txtNachn.setEnabled(false);
			}
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void saveData(IProgressMonitor monitor, RegionVolk charElem) {
			
			charElem.setVornamenMann(buildStringArray(txtVornM.getText()));
			charElem.setVornamenFrau(buildStringArray(txtVornW.getText()));
			if (cbxVornamenSindNachnamen.getSelection()) {
				charElem.setNachnamen(null);
			} else {
				charElem.setNachnamen(buildStringArray(txtNachn.getText()));
			}
			
			charElem.setBindeWortMann(getNullFromString(txtBindewM.getText()));
			charElem.setBindeWortFrau(getNullFromString(txtBindewW.getText()));
			charElem.setEndWortMann(getNullFromString(txtEndungM.getText()));
			charElem.setEndWortFrau(getNullFromString(txtEndungW.getText()));
			
			charElem.setAbk(getNullFromString(txtAbk.getText()));
			
			for (int i = 0; i < RegionVolkArt.values().length; i++) {
				if (RegionVolkArt.values()[i].toString().equals(cobArt.getText())) {
					charElem.setArt(RegionVolkArt.values()[i]);
					break;
				}
			}
			
			charElem.setVornamenSindNachnamen(cbxVornamenSindNachnamen.getSelection());
		}
		
		/**
		 * Wandelt ein String-Array zu einer Komma-getrennten Liste um
		 * @param strArray 
		 * @return Komma-getrennte Liste
		 */
		private String buildNamenString(String[] strArray) {
			if (strArray == null) return "";
			StringBuilder strB = new StringBuilder();
			
			for (int i = 0; i < strArray.length; i++) {
				strB.append(strArray[i]);
				if (i+1 < strArray.length) strB.append(", ");
			}
			
			return strB.toString();
		}
		
		/**
		 * Wandelt eine Komma-getrennte Liste in ein String-Array um.
		 */
		private String[] buildStringArray(String str) {
			if (str.trim().length() == 0) return null;
			
			String[] strArray = str.split(",");
			
			for (int i = 0; i < strArray.length; i++) {
				strArray[i] = strArray[i].trim();
			}
			
			return strArray;
		}
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#addCharElementSiteParts(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		// Repraesentation spezifische Elemte erzeugen
		regionPart = this.new RegionVolkPart(mainContainer);
		regionPart.loadData((RegionVolk) getEditedCharElement());	
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		int index = addPage(createCharElementSite());
		setPageText(index, EditorMessages.Editor_Daten);
		
		elementPartArray = new AbstractElementPart[] {
				charElementPart, regionPart};	
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getEditedCharElement()
	 */
	@Override
	protected CharElement getEditedCharElement() {
		return (RegionVolk) this.getEditorInput().getAdapter(RegionVolk.class);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getElementParts()
	 */
	@Override
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}

}
