/*
 * Created 15.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import org.d3s.alricg.editor.editors.ProfessionEditor.ProfessionPart;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.HerkunftVariante;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.ProfessionVariante;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Vincent
 */
public class ProfessionVarianteEditor extends ComposedMulitHerkunftVarianteEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.ProfessionVarianteEditor";
	
	private ProfessionPart professionPart;
	private AbstractElementPart[] elementPartArray;

	@Override
	protected void createHerkunftPart(Composite herkunftComp) {
		professionPart = new ProfessionPart(herkunftComp);
		professionPart.loadData((ProfessionVariante) getEditedCharElement());
		professionPart.cobArt.setEnabled(false);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		int index = addPage(createCharElementSite());
		setPageText(index, EditorMessages.Editor_Daten);
		
		index = addPage(herkunftDatenScroll);
		setPageText(index, "Profession Daten");
		
		index = addPage(herkunftPart.getModisAuswahl().getTree());
		setPageText(index, "Modifikationen");
		
		index = addPage(herkunftPart.getAltervativeZauberPart().getControl());
		setPageText(index, "Alternative Zauberauswahl");
		
		index = addPage(herkunftPart.getVerbilligtPart().getTree());
		setPageText(index, "Verbilligungen");
		
		index = addPage(herkunftPart.getEmpfehlungenPart().getTree());
		setPageText(index, "Empfehlungen");
		
		index = addPage(herkunftPart.getAktivierbareZauberPart().getTree());
		setPageText(index, "Aktivierbare Zauber");
		
		index = addPage(createVoraussetzungsSite());
		setPageText(index, EditorMessages.Editor_Voraussetzungen);
		
		elementPartArray = new AbstractElementPart[] {
				charElementPart,
				herkunftVariantePart,
				herkunftPart,
				professionPart,
				herkunftPart.getModisAuswahl(),
				herkunftPart.getAltervativeZauberPart(),
				herkunftPart.getVerbilligtPart(),
				herkunftPart.getEmpfehlungenPart(),
				herkunftPart.getAktivierbareZauberPart(),
				voraussetzungsPart};
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getEditedCharElement()
	 */
	@Override
	protected CharElement getEditedCharElement() {
		return (ProfessionVariante) this.getEditorInput().getAdapter(ProfessionVariante.class);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getElementParts()
	 */
	@Override
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMulitPageHerkunftVarianteEditorPart#getHerkunftVariante(int)
	 */
	@Override
	protected HerkunftVariante[] getHerkunftVariante(int length) {
		return new ProfessionVariante[length];
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMulitHerkunftVarianteEditorPart#canDropElement(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	protected boolean canDropElement(CharElement charElem) {
		return charElem instanceof Profession;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMulitHerkunftVarianteEditorPart#getSpezifischeTags()
	 */
	@Override
	protected String[] getSpezifischeTags() {
		return ProfessionVariante.ALLE_TAGS;
	}
}
