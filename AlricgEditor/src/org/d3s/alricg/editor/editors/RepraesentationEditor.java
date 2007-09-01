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
import org.d3s.alricg.store.charElemente.Repraesentation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Vincent
 *
 */
public class RepraesentationEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.RepraesentationEditor"; //$NON-NLS-1$

	private RepraesentationPart repPart;
	private AbstractElementPart[] elementPartArray;
	
	class RepraesentationPart extends AbstractElementPart<Repraesentation> {
		private final Button cbxIsSchamanisch;
		private final Text txtAbk;
		
		RepraesentationPart(Composite top) {
			final Label filler = new Label(top, SWT.NONE);
			cbxIsSchamanisch = new Button(top, SWT.CHECK);
			cbxIsSchamanisch.setText("Ist eine schamanische Repräsentation?");
			cbxIsSchamanisch.setToolTipText("Schamanische Repräsentationen sind KEINE echten Repräsentation nach DSA4."); 
		
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
		public boolean isDirty(Repraesentation charElem) {
			boolean isNotDirty = true;
			
			isNotDirty &= cbxIsSchamanisch.getSelection() == charElem.isSchamanischeRep();
			isNotDirty &= txtAbk.getText().equals( getStringFromNull(charElem.getAbkuerzung()) );
			
			return !isNotDirty;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void loadData(Repraesentation charElem) {
			cbxIsSchamanisch.setSelection(charElem.isSchamanischeRep());
			txtAbk.setText( getStringFromNull(charElem.getAbkuerzung()) );
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void saveData(IProgressMonitor monitor, Repraesentation charElem) {
			monitor.subTask("Save Repräsentation-Data"); //$NON-NLS-1$
			
			charElem.setSchamanischeRep(cbxIsSchamanisch.getSelection());
			charElem.setAbkuerzung( getNullFromString(txtAbk.getText()) );
			
			monitor.worked(1);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#addCharElementSiteParts(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		// Repraesentation spezifische Elemte erzeugen
		repPart = this.new RepraesentationPart(mainContainer);
		repPart.loadData((Repraesentation) getEditedCharElement());	
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		int index = addPage(createCharElementSite());
		setPageText(index, EditorMessages.Editor_Daten);
		
		elementPartArray = new AbstractElementPart[] {
				charElementPart, repPart};	
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getEditedCharElement()
	 */
	@Override
	protected CharElement getEditedCharElement() {
		return (Repraesentation) this.getEditorInput().getAdapter(Repraesentation.class);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getElementParts()
	 */
	@Override
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}

}
