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
import org.d3s.alricg.editor.common.widgets.TextList;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.editor.editors.composits.FaehigkeitPart;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Talent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

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
		private TextList listSpez;
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
			cobSorte.add(Talent.Sorte.spezial.toString());
			cobSorte.select(0);
			

		// Container für Spezialisierungen
			Label lblSpezi = new Label(top, SWT.NONE);
			lblSpezi.setText(EditorMessages.TalentEditor_Spezialisierung);
			GridData tmpGData = new  GridData();
			tmpGData.verticalAlignment = GridData.BEGINNING;
			tmpGData.verticalIndent = 12;
			lblSpezi.setLayoutData(tmpGData);
			
			listSpez = new TextList(top, SWT.NONE);
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
			cobArt.setText(charElem.getArt().name());
			cobSorte.setText(charElem.getSorte().name());
			
			if (charElem.getSpezialisierungen() == null) return;
			for (int i = 0; i < charElem.getSpezialisierungen().length; i++) {
				listSpez.addValue(charElem.getSpezialisierungen()[i]);
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
			if (listSpez.getValueList().length == 0) {
				charElem.setSpezialisierungen(null);
			} else {
				charElem.setSpezialisierungen(
						Arrays.copyOf(
								listSpez.getValueList(), 
								listSpez.getValueList().length
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
			
			if (listSpez.getValueList().length == 0) {
				isNotDirty &= (charElem.getSpezialisierungen() == null);
			} else {
				isNotDirty &= Arrays.equals(charElem.getSpezialisierungen(), listSpez.getValueList());
			}
			
			return !isNotDirty;
		}
	}

	
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		
		// FaehigkeitPart erzeugen
		faehigkeitPart = new FaehigkeitPart(mainContainer);
		faehigkeitPart.loadData((Talent) getEditedCharElement()); 
		
		// Talent spezifische Elemte erzeugen
		talentPart = this.new TalentPart(mainContainer);
		talentPart.loadData((Talent) getEditedCharElement());
		
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
