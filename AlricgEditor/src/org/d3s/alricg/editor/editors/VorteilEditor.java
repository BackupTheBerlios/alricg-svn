/*
 * Created 25.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import org.d3s.alricg.editor.editors.TalentEditor.TalentPart;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.editor.editors.composits.FaehigkeitPart;
import org.d3s.alricg.editor.editors.composits.FertigkeitPart;
import org.d3s.alricg.editor.editors.composits.VorNachteilPart;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Vincent
 *
 */
public class VorteilEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.VorteilEditor";

	private VorNachteilPart vorNachteilPart;
	private FertigkeitPart fertigkeitPart;

	private AbstractElementPart[] elementPartArray;

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#addCharElementSiteParts(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		
		// FertigkeitPart erzeugen
		fertigkeitPart = new FertigkeitPart(mainContainer, this.scrollComp);
		fertigkeitPart.loadData((Vorteil) getEditedCharElement());
		
		// VorNachteilPart erzeugen
		vorNachteilPart = new VorNachteilPart(mainContainer);
		vorNachteilPart.loadData((Vorteil) getEditedCharElement()); 
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
				charElementPart, fertigkeitPart, vorNachteilPart, voraussetzungsPart};
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getEditedCharElement()
	 */
	@Override
	protected CharElement getEditedCharElement() {
		return (Vorteil) this.getEditorInput().getAdapter(Vorteil.class);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getElementParts()
	 */
	@Override
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}

}
