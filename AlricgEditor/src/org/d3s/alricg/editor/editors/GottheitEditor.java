/*
 * Created 30.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Gottheit;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Vincent
 *
 */
public class GottheitEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.GottheitEditor"; //$NON-NLS-1$
	
	private GottheitPart gottheitPart;
	private AbstractElementPart[] elementPartArray;
	
	
	class GottheitPart extends AbstractElementPart<Gottheit> {
		private Combo cobArt;
		private Text txtImage;
		
		GottheitPart(Composite top) {
			// ComboBox Art
			final Label lblArt = new Label(top, SWT.NONE);
			lblArt.setText("Art:");
			cobArt = new Combo(top, SWT.READ_ONLY);
			cobArt.add(Gottheit.GottheitArt.zwoelfGoettlich.toString());
			cobArt.add(Gottheit.GottheitArt.alveranNah.toString());
			cobArt.add(Gottheit.GottheitArt.animistisch.toString());
			cobArt.select(0);
			
			final Label lblImage = new Label(top, SWT.NONE);
			lblImage.setText("Image:");
			txtImage = new Text(top, SWT.BORDER);
			GridData dg = new GridData();
			dg.widthHint = 300;
			txtImage.setLayoutData(dg);
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
		public boolean isDirty(Gottheit charElem) {
			boolean isNotDirty = true;
			
			isNotDirty &= cobArt.getText().equals( charElem.getGottheitArt().toString() );
			isNotDirty &= txtImage.getText().equals( getStringFromNull(charElem.getGottheitImage()) );
			
			return !isNotDirty;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void loadData(Gottheit charElem) {
			cobArt.setText(charElem.getGottheitArt().toString());
			txtImage.setText( getStringFromNull(charElem.getGottheitImage()) );
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void saveData(IProgressMonitor monitor, Gottheit charElem) {
			monitor.subTask("Save Gottheit-Data"); //$NON-NLS-1$
			
			// Art setzen
			for (int i = 0; i < Gottheit.GottheitArt.values().length; i++) {
				if ( cobArt.getText().equals(Gottheit.GottheitArt.values()[i].name()) ) {
					charElem.setGottheitArt(Gottheit.GottheitArt.values()[i]);
					break;
				}
			}
			
			// Bild speichern
			charElem.setGottheitImage( this.getNullFromString(txtImage.getText()) );
			
			monitor.worked(1);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#addCharElementSiteParts(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		// Gottheit spezifische Elemte erzeugen
		gottheitPart = this.new GottheitPart(mainContainer);
		gottheitPart.loadData((Gottheit) getEditedCharElement());
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		int index = addPage(createCharElementSite());
		setPageText(index, EditorMessages.Editor_Daten);
		
		elementPartArray = new AbstractElementPart[] {
				charElementPart, gottheitPart};
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getEditedCharElement()
	 */
	@Override
	protected CharElement getEditedCharElement() {
		return (Gottheit) this.getEditorInput().getAdapter(Gottheit.class);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getElementParts()
	 */
	@Override
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}

}
