/*
 * Created 25.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.d3s.alricg.editor.editors.EditorMessages;
import org.d3s.alricg.store.access.IdFactory;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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
public class CharElementPart extends AbstractElementPart<CharElement> {
	
	private Class charElementClass; // Class des angezeigten CharElements
	private String lastFocusedName; // Der letzte Name nach einem Focus wechsel
									// wichtig für Berechnung der ID, damit diese
									// nur Berechnet wird, wenn der Name sich 
									// geändert hat
	private String inputFileName;   // Der letzte Name nach einem Focus wechsel
									// wichtig für Berechnung der ID, damit diese
									// nur Berechnet wird, wenn der Name sich 
									// geändert hat
	
	private final Group groupBasisDaten;
	private final Text txtID;
	private final Text txtSonderregelKlasse;
	private final Text txtName;
	private final Text txtSammelbegriff;
	private final Text txtBeschreibung;
	private final Text txtRegelAnm;
	private final Button cbxAnzeigen;
	private final Combo cobFile;
	
	private HashMap<String, XmlAccessor> cobFileMap = new HashMap<String, XmlAccessor>();
	
	public CharElementPart(Composite top, GridData gridData, boolean isNewCharElement) {
		// GirdLayout mit zwei Spalten
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		
		GridData tmpGData;
		
		// Erzeuge die Gruppe
		groupBasisDaten = new Group(top, SWT.SHADOW_IN);
		groupBasisDaten.setText(EditorMessages.CharElementPart_BasisDaten);
		groupBasisDaten.setLayoutData(gridData);
		groupBasisDaten.setLayout(gridLayout);
		
		
		// ID
		Label lblID = new Label(groupBasisDaten, SWT.NONE);
		lblID.setText(EditorMessages.CharElementPart_ID);
		txtID = new Text(groupBasisDaten, SWT.BORDER);
		txtID.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Sonderregel
		Label lblSonderregel = new Label(groupBasisDaten, SWT.NONE);
		lblSonderregel.setText(EditorMessages.CharElementPart_SonderregelKlasse);
		txtSonderregelKlasse = new Text(groupBasisDaten, SWT.BORDER);
		txtSonderregelKlasse.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Name
		Label lblName = new Label(groupBasisDaten, SWT.NONE);
		lblName.setText(EditorMessages.CharElementPart_Name);
		txtName = new Text(groupBasisDaten, SWT.BORDER);
		txtName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (isNewCharElement) {
			// Die ID darf sich nicht ändern wenn das element bereits
			// mit anderen CharElement verbunden ist! Daher wird die
			// ID nur geändert, wenn das Element neu erzeugt wurde
			
			txtName.addFocusListener( new FocusListener() {
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
		}
		
		// Sammelbegriff
		Label lblSammelbegriff = new Label(groupBasisDaten, SWT.NONE);
		lblSammelbegriff.setText(EditorMessages.CharElementPart_Sammelbegriff);
		txtSammelbegriff = new Text(groupBasisDaten, SWT.BORDER);
		txtSammelbegriff.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Beschreibung
		Label lblBeschreibung = new Label(groupBasisDaten, SWT.NONE);
		lblBeschreibung.setText(EditorMessages.CharElementPart_Beschreibung);
		tmpGData = new GridData(); 
		tmpGData.verticalAlignment = GridData.BEGINNING;
		lblBeschreibung.setLayoutData(tmpGData);
		txtBeschreibung = new Text(groupBasisDaten, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		tmpGData = new GridData(GridData.FILL_BOTH); 
		tmpGData.heightHint = txtBeschreibung.getLineHeight() * 3;
		txtBeschreibung.setLayoutData(tmpGData);
		
		// Regelanmerkung
		Label lblRegelanmerkung = new Label(groupBasisDaten, SWT.NONE);
		lblRegelanmerkung.setText(EditorMessages.CharElementPart_Regelanmerkung);
		tmpGData = new GridData(); 
		tmpGData.verticalAlignment = GridData.BEGINNING;
		lblRegelanmerkung.setLayoutData(tmpGData);
		txtRegelAnm = new Text(groupBasisDaten, SWT.BORDER | SWT.MULTI);
		tmpGData = new GridData(GridData.FILL_BOTH); 
		tmpGData.heightHint = txtRegelAnm.getLineHeight() * 3;
		txtRegelAnm.setLayoutData(tmpGData);
		
		// File / XMLAccessor
		Label lblFile = new Label(groupBasisDaten, SWT.NONE);
		lblFile.setText(EditorMessages.CharElementPart_Datei);
		cobFile = new Combo(groupBasisDaten, SWT.READ_ONLY | SWT.CENTER);
		cobFile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cobFile.setVisibleItemCount(8);
		
		// Anzeigen
		Label filler = new Label(groupBasisDaten, SWT.NONE);
		cbxAnzeigen = new Button(groupBasisDaten, SWT.CHECK);
		cbxAnzeigen.setText(EditorMessages.CharElementPart_ElementAnzeigen);
		cbxAnzeigen.setToolTipText(EditorMessages.CharElementPart_ElementAnzeigen_TT); 
		
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
	
	public void setSelectedXmlAccessor(XmlAccessor accessor) {
		List<XmlAccessor> accList = StoreDataAccessor.getInstance().getXmlAccessors();
		List<String> strList = new ArrayList<String>();
		
		for (int i = 0; i < accList.size(); i++) {
			String tmp = accList.get(i).getFile().getName() 
				+ " (" + accList.get(i).getFile().getAbsolutePath() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			
			if (accessor.equals(accList.get(i))) {
				inputFileName = tmp;
			}
			cobFileMap.put(tmp, accList.get(i));
			strList.add(tmp);
		}
		Collections.sort(strList);
		
		for (int i = 0; i < strList.size(); i++) {
			this.cobFile.add(strList.get(i));
			if (inputFileName.equals(strList.get(i))) {
				cobFile.select(cobFile.getItemCount()-1);
			}
		}
	}
	
	public XmlAccessor getSelectedXmlAccessor() {
		return cobFileMap.get(cobFile.getText());
	}
	
	@Override
	public void saveData(IProgressMonitor monitor, CharElement charElem) {
		monitor.subTask("Save CharElement-Data"); //$NON-NLS-1$
		
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
		idNotDirty &= cobFile.getText().equals(inputFileName);
			
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
