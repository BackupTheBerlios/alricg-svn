/*
 * Created 25.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import java.util.Arrays;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.editor.editors.composits.AuswahlPart;
import org.d3s.alricg.editor.editors.composits.FaehigkeitPart;
import org.d3s.alricg.editor.editors.composits.AuswahlPart.HerkunftAuswahlRegulator;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.links.Auswahl;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * Editor um Talente bearbeiten zu können
 * @author Vincent
 */
public class TalentEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.TalentEditor"; //$NON-NLS-1$
	
	private FaehigkeitPart faehigkeitPart;
	private TalentPart talentPart;

	private AbstractElementPart[] elementPartArray;
	
	/**
	 * Stellt Controls für die Felder der Klasse Talent bereit und verwaltet diese.
	 * @author Vincent
	 */
	class TalentPart extends AbstractElementPart<Talent> {
		private Combo cobArt;
		private Combo cobSorte;
		private Text txtSpez;
		private List listSpez;
		private Button butAdd;
		private Button butDelete;
		private Composite container;
		
		private Image imgAdd = ControlIconsLibrary.add.getImageDescriptor().createImage();
		private Image imgDelete = ControlIconsLibrary.delete.getImageDescriptor().createImage();
		
		// Konstruktor
		TalentPart(Composite top) {

			// ComboBox Art
			Label lblArt = new Label(top, SWT.NONE);
			lblArt.setText(EditorMessages.TalentEditor_Art);
			cobArt = new Combo(top, SWT.READ_ONLY);
			cobArt.add(Talent.Art.basis.toString());
			cobArt.add(Talent.Art.beruf.toString());
			cobArt.add(Talent.Art.spezial.toString());
			cobArt.select(0);
			
			// ComboBox Sorte
			Label lblSorte = new Label(top, SWT.NONE);
			lblSorte.setText(EditorMessages.TalentEditor_Sorte);
			cobSorte = new Combo(top, SWT.READ_ONLY);
			cobSorte.setVisibleItemCount(6);
			cobSorte.add(Talent.Sorte.gesellschaft.toString());
			cobSorte.add(Talent.Sorte.handwerk.toString());
			cobSorte.add(Talent.Sorte.kampf.toString());
			cobSorte.add(Talent.Sorte.koerper.toString());
			cobSorte.add(Talent.Sorte.natur.toString());
			cobSorte.add(Talent.Sorte.wissen.toString());
			cobSorte.select(0);
			

		// Container für Spezialisierungen
			container = new Composite (top, SWT.NONE);
			GridData containerGridData = new GridData(GridData.GRAB_HORIZONTAL);
			containerGridData.widthHint = 300;
			containerGridData.horizontalSpan = 2;
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 3;
			
			container.setLayout(gridLayout);
			container.setLayoutData(containerGridData);
			
			// Spalte 1 der containerGridData
			Label lblSpezi = new Label(container, SWT.NONE);
			lblSpezi.setText(EditorMessages.TalentEditor_Spezialisierung);
			txtSpez = new Text(container, SWT.BORDER);
			txtSpez.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			txtSpez.addListener (SWT.DefaultSelection, new Listener () {
				public void handleEvent (Event e) {
					addToList();
				}
			});

			butAdd = new Button(container, SWT.NONE);
			butAdd.setImage(imgAdd);
			butAdd.setLayoutData(new GridData(imgAdd.getImageData().width+4, imgAdd.getImageData().height+4));
			butAdd.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					addToList();
				}
			});
			
			// Spalte 2 der containerGridData
			Label lblFiller = new Label(container, SWT.NONE);
			
			listSpez = new List (container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
			GridData tmpGData = new GridData(GridData.FILL_BOTH); 
			tmpGData.heightHint = listSpez.getItemHeight() * 5;
			listSpez.setLayoutData(tmpGData);
			
			butDelete = new Button(container, SWT.NONE);
			tmpGData = new GridData(imgDelete.getImageData().width+4, imgDelete.getImageData().height+4); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			butDelete.setLayoutData(tmpGData);
			butDelete.setImage(imgDelete);
			butDelete.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					// Lösche alle Markierten
					listSpez.remove(listSpez.getSelectionIndices());
				}
			});
		}
		
		// Fügt den aktuellen Text im txtSpez nach Prüfung zur List hinzu 
		private void addToList() {
			// Prüfe ob leer
			String text = txtSpez.getText().trim();
			if (text.length() == 0) return;
			
			// Prüfe ob doppelt
			String[] strs = listSpez.getItems();
			for (int i = 0; i < strs.length; i++) {
				if (strs[i].equalsIgnoreCase(text)) return;
			}
			
			// Wert hinzufügen
			listSpez.add(text);
			txtSpez.setText(""); //$NON-NLS-1$
		}
		
		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#dispose()
		 */
		@Override
		public void dispose() {
			imgAdd.dispose();
			imgDelete.dispose();
			container.dispose();
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void loadData(Talent charElem) {
			ViewUtils.findAndSetIndex(cobArt, charElem.getArt().name());
			ViewUtils.findAndSetIndex(cobSorte, charElem.getSorte().name());
			
			if (charElem.getSpezialisierungen() == null) return;
			for (int i = 0; i < charElem.getSpezialisierungen().length; i++) {
				listSpez.add(charElem.getSpezialisierungen()[i]);
			}
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#saveData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void saveData(IProgressMonitor monitor, Talent charElem) {
			monitor.subTask("Save Talent-Data"); //$NON-NLS-1$
			
			// Art setzen
			for (int i = 0; i < Talent.Art.values().length; i++) {
				if ( cobArt.getText().equals(Talent.Art.values()[i].name()) ) {
					charElem.setArt(Talent.Art.values()[i]);
					break;
				}
			}
			
			// Sorte setzen
			for (int i = 0; i < Talent.Sorte.values().length; i++) {
				if ( cobSorte.getText().equals(Talent.Sorte.values()[i].name()) ) {
					charElem.setSorte(Talent.Sorte.values()[i]);
					break;
				}
			}
			
			// Spezialisierung sichern
			if (listSpez.getItems().length == 0) {
				charElem.setSpezialisierungen(null);
			} else {
				charElem.setSpezialisierungen(
						Arrays.copyOf(
								listSpez.getItems(), 
								listSpez.getItems().length
						));
			}
			
			monitor.worked(1);
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public boolean isDirty(Talent charElem) {
			boolean isNotDirty = true;
			
			isNotDirty &= cobArt.getText().equals(charElem.getArt().name());
			isNotDirty &= cobSorte.getText().equals(charElem.getSorte().name());
			
			if (listSpez.getItems().length == 0) {
				isNotDirty &= (charElem.getSpezialisierungen() == null);
			} else {
				isNotDirty &= Arrays.equals(charElem.getSpezialisierungen(), listSpez.getItems());
			}
			
			return !isNotDirty;
		}
	}

	
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		
		// FaehigkeitPart erzeugen
		faehigkeitPart = new FaehigkeitPart(mainContainer, null);
		faehigkeitPart.loadData((Talent) getEditedCharElement()); 
		
		// Talent spezifische Elemte erzeugen
		talentPart = this.new TalentPart(mainContainer);
		talentPart.loadData((Talent) getEditedCharElement());
		
	}
	
	// TEST zu entfernen, gehört hier nicht hin
	private Composite createTestTalent() {
		Herkunft herk = new Rasse();
		
		AuswahlPart auswahlPart = new AuswahlPart(
				getContainer(), 
				this.getSite(),
				new HerkunftAuswahlRegulator() {

					@Override
					public Auswahl getAuswahl(Herkunft herkunft) {
						return herkunft.getTalente();
					}

					@Override
					public Class getCharElementClazz() {
						return Talent.class;
					}

					@Override
					public String getRootNoteName() {
						return EditorMessages.TalentEditor_Talente;
					}

					@Override
					public void setAuswahl(Herkunft herkunft, Auswahl auswahl) {
						herkunft.setTalente(auswahl);
					}
					
				});
		auswahlPart.loadData(herk);
		
		return auswahlPart.getTree();
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
		
		// TEST zu entfernen, gehört hier nicht hin
		index = addPage(createTestTalent());
		setPageText(index, "Talente wählen"); //$NON-NLS-1$
		
		elementPartArray = new AbstractElementPart[] {
				charElementPart, faehigkeitPart, talentPart, voraussetzungsPart};
	}
		
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getEditedCharElement()
	 */
	@Override
	protected CharElement getEditedCharElement() {
		return (Talent) this.getEditorInput().getAdapter(Talent.class);
	}
	
}
