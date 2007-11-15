/*
 * Created 05.09.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import org.d3s.alricg.editor.editors.RasseEditor.RassePart;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.HerkunftVariante;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.RasseVariante;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Vincent
 */
public class RasseVarianteEditor extends ComposedMulitHerkunftVarianteEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.RasseVarianteEditor";
	
	private RassePart rassePart;
	private AbstractElementPart[] elementPartArray;

	@Override
	protected void createHerkunftPart(Composite rassenComp) {
		rassePart = new RassePart(rassenComp, this.getSite(), 0, 0);
		rassePart.loadData((RasseVariante) getEditedCharElement());
		rassePart.cobArt.setEnabled(false);
		rassePart.cbxIsNegativListe.setEnabled(false);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		int index = addPage(createCharElementSite());
		setPageText(index, EditorMessages.Editor_Daten);
		
		index = addPage(herkunftDatenScroll);
		setPageText(index, "Rassen Daten");
		
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
				rassePart,
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
		return (RasseVariante) this.getEditorInput().getAdapter(RasseVariante.class);
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
		return new RasseVariante[length];
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMulitHerkunftVarianteEditorPart#canDropElement(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	protected boolean canDropElement(CharElement charElem) {
		return charElem instanceof Rasse;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMulitHerkunftVarianteEditorPart#getSpezifischeTags()
	 */
	@Override
	protected String[] getSpezifischeTags() {
		return RasseVariante.ALLE_TAGS;
	}
}
