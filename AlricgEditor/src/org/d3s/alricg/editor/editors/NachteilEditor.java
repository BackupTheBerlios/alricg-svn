/*
 * Created 25.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.editor.editors.composits.FertigkeitPart;
import org.d3s.alricg.editor.editors.composits.VorNachteilPart;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author Vincent
 */
public class NachteilEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.NachteilEditor";

	private VorNachteilPart vorNachteilPart;
	private FertigkeitPart fertigkeitPart;
	private NachteilPart nachteilPart;
	
	private AbstractElementPart[] elementPartArray;
	
	class NachteilPart extends AbstractElementPart<Nachteil> {
		private final Button cbxIsSchlechteEig;
		
		NachteilPart(Composite top) {
			Label lblFiller = new Label(top, SWT.NONE);
			cbxIsSchlechteEig = new Button(top, SWT.CHECK);
			cbxIsSchlechteEig.setText("Dieser Nachteil ist eine schlechte Eigenschaft");
		}
		
		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#dispose()
		 */
		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public boolean isDirty(Nachteil charElem) {
			return cbxIsSchlechteEig.getSelection() != charElem.isSchlechteEigen();
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void loadData(Nachteil charElem) {
			cbxIsSchlechteEig.setSelection(charElem.isSchlechteEigen());
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void saveData(IProgressMonitor monitor, Nachteil charElem) {
			charElem.setSchlechteEigen(cbxIsSchlechteEig.getSelection());
			
		}

	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#addCharElementSiteParts(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		// FertigkeitPart erzeugen
		fertigkeitPart = new FertigkeitPart(mainContainer, this.scrollComp);
		fertigkeitPart.loadData((Nachteil) getEditedCharElement());
		
		// VorNachteilPart erzeugen
		vorNachteilPart = new VorNachteilPart(mainContainer);
		vorNachteilPart.loadData((Nachteil) getEditedCharElement());

		// NachteilPart erzeugen
		nachteilPart = new NachteilPart(mainContainer);
		nachteilPart.loadData((Nachteil) getEditedCharElement());
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		int index = addPage(createCharElementSite());
		setPageText(index, EditorMessages.Editor_Daten);
		
		index = addPage(createVoraussetzungsSite());
		setPageText(index, EditorMessages.Editor_Voraussetzungen);
		
		elementPartArray = new AbstractElementPart[] {
				charElementPart, fertigkeitPart, vorNachteilPart, 
								nachteilPart, voraussetzungsPart};

	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getEditedCharElement()
	 */
	@Override
	protected CharElement getEditedCharElement() {
		return (Nachteil) this.getEditorInput().getAdapter(Nachteil.class);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getElementParts()
	 */
	@Override
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}

}
