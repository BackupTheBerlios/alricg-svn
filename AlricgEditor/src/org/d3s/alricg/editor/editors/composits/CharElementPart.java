/*
 * Created 25.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import org.d3s.alricg.store.access.IdFactory;
import org.d3s.alricg.store.charElemente.CharElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * Stellt Controls für die Felder der Klasse CharElement bereit und verwaltet diese.
 * Legt die ID fest, indem nach einem Focuswechsel vom Namens-Feld die ID neu berechnet wird.
 * @author Vincent
 */
public class CharElementPart extends AbstarctElementPart<CharElement> {
	
	private Class charElementClass; // Class des angezeigten CharElements
	private String lastFocusedName; // Der letzte Name nach einem Focus wechsel
									// wichtig für Berechnung der ID, damit diese
									// nur Berechnet wird, wenn der Name sich 
									// geändert hat
	
	private Group groupBasisDaten;
	private Text txtID;
	private Text txtSonderregelKlasse;
	private Text txtName;
	private Text txtSammelbegriff;
	private Text txtBeschreibung;
	private Text txtRegelAnm;
	private Button cbxAnzeigen;
	
	
	public CharElementPart(Composite top, GridData gridData) {
		// GirdLayout mit zwei Spalten
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		
		GridData tmpGData;
		
		// Erzeuge die Gruppe
		groupBasisDaten = new Group(top, SWT.SHADOW_IN);
		groupBasisDaten.setText("Basis Daten");
		groupBasisDaten.setLayoutData(gridData);
		groupBasisDaten.setLayout(gridLayout);
		
		
		// ID
		Label lblID = new Label(groupBasisDaten, SWT.NONE);
		lblID.setText("ID:");
		txtID = new Text(groupBasisDaten, SWT.BORDER);
		txtID.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Sonderregel
		Label lblSonderregel = new Label(groupBasisDaten, SWT.NONE);
		lblSonderregel.setText("SonderregelKlasse:");
		txtSonderregelKlasse = new Text(groupBasisDaten, SWT.BORDER);
		txtSonderregelKlasse.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Name
		Label lblName = new Label(groupBasisDaten, SWT.NONE);
		lblName.setText("Name:");
		txtName = new Text(groupBasisDaten, SWT.BORDER);
		txtName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtName.addFocusListener( new FocusListener() {
			
			// TODO Vorsicht! Die ID darf sich nicht ändern wenn das element bereits
			// mit anderen CharElement verbunden ist!
			@Override
		    public void focusLost(FocusEvent event) {	
				if (!txtName.getText().equalsIgnoreCase(lastFocusedName) 
						&& charElementClass != null) {
					txtID.setText(
							IdFactory.getInstance().getId(charElementClass, txtName.getText()
					));
					lastFocusedName = txtName.getText(); 
				}
	        }

			@Override
			public void focusGained(FocusEvent e) {
				// Noop
			}
		});
		
		
		// Sammelbegriff
		Label lblSammelbegriff = new Label(groupBasisDaten, SWT.NONE);
		lblSammelbegriff.setText("Sammelbegriff:");
		txtSammelbegriff = new Text(groupBasisDaten, SWT.BORDER);
		txtSammelbegriff.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Beschreibung
		Label lblBeschreibung = new Label(groupBasisDaten, SWT.NONE);
		lblBeschreibung.setText("Beschreibung:");
		tmpGData = new GridData(); 
		tmpGData.verticalAlignment = GridData.BEGINNING;
		lblBeschreibung.setLayoutData(tmpGData);
		txtBeschreibung = new Text(groupBasisDaten, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		tmpGData = new GridData(GridData.FILL_BOTH); 
		tmpGData.heightHint = txtBeschreibung.getLineHeight() * 3;
		txtBeschreibung.setLayoutData(tmpGData);
		
		// Regelanmerkung
		Label lblRegelanmerkung = new Label(groupBasisDaten, SWT.NONE);
		lblRegelanmerkung.setText("Regelanmerkung:");
		tmpGData = new GridData(); 
		tmpGData.verticalAlignment = GridData.BEGINNING;
		lblRegelanmerkung.setLayoutData(tmpGData);
		txtRegelAnm = new Text(groupBasisDaten, SWT.BORDER | SWT.MULTI);
		tmpGData = new GridData(GridData.FILL_BOTH); 
		tmpGData.heightHint = txtRegelAnm.getLineHeight() * 3;
		txtRegelAnm.setLayoutData(tmpGData);
		
		// Anzeigen
		Label filler = new Label(groupBasisDaten, SWT.NONE);
		cbxAnzeigen = new Button(groupBasisDaten, SWT.CHECK);
		cbxAnzeigen.setText("Element anzeigen");
		cbxAnzeigen.setToolTipText("Nicht Angezeigte Elemente werden in den Standart-Listen" +
				" nicht angezeigt (für Sonderfälle).");
	}
	
	@Override
	public void loadData(CharElement charElem) {
		this.txtID.setText(getStringFromNull(charElem.getId()));
		this.txtName.setText(getStringFromNull(charElem.getName()));
		this.txtBeschreibung.setText(getStringFromNull(charElem.getBeschreibung()));
		this.txtRegelAnm.setText(getStringFromNull(charElem.getRegelAnmerkung()));
		this.txtSammelbegriff.setText(getStringFromNull(charElem.getSammelbegriff()));
		this.txtSonderregelKlasse.setText(getStringFromNull(charElem.getSonderregelKlasse()));
		this.cbxAnzeigen.setSelection(charElem.isAnzeigen());
		
		this.charElementClass = charElem.getClass();
		this.lastFocusedName = this.txtName.getText();

	}
	
	@Override
	public void saveData(IProgressMonitor monitor, CharElement charElem) {
		monitor.subTask("Save CharElement-Data");
		
		// Prüfe ob die ID Ok ist. Wenn nicht, berechne eine korrekte
		if (IdFactory.getInstance().getClass(this.txtID.getText()) == null) {
			this.txtID.setText(
					IdFactory.getInstance().getId(charElem.getClass(), this.txtName.getText())
			);
		}
		
		charElem.setId(this.txtID.getText());
		charElem.setName(this.txtName.getText());
		charElem.setBeschreibung(getNullFromString(this.txtBeschreibung.getText()));
		charElem.setRegelAnmerkung(getNullFromString(this.txtRegelAnm.getText()));
		charElem.setSammelbegriff(getNullFromString(this.txtSammelbegriff.getText()));
		charElem.setSonderregelKlasse(getNullFromString(this.txtSonderregelKlasse.getText()));
		charElem.setAnzeigen(this.cbxAnzeigen.getSelection());
		
		monitor.worked(1);
	}
	
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean isDirty(CharElement charElem) {
		boolean idNotDirty = true;
		
		idNotDirty &= charElem.getId().equals(this.txtID.getText());
		idNotDirty &= charElem.getName().equals(this.txtName.getText());
		idNotDirty &= getStringFromNull(charElem.getBeschreibung()).equals(this.txtBeschreibung.getText());
		idNotDirty &= getStringFromNull(charElem.getRegelAnmerkung()).equals(this.txtRegelAnm.getText());
		idNotDirty &= getStringFromNull(charElem.getSammelbegriff()).equals(this.txtSammelbegriff.getText());
		idNotDirty &= getStringFromNull(charElem.getSonderregelKlasse()).equals(this.txtSonderregelKlasse.getText());
		idNotDirty &= (charElem.isAnzeigen() == this.cbxAnzeigen.getSelection());	
			
		return !idNotDirty;
	}
	

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#dispose()
	 */
	@Override
	public void dispose() {
		groupBasisDaten.dispose();		
	}
	
}
