/*
 * Created 05.09.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.d3s.alricg.editor.editors.RasseEditor.RassePart;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.editor.editors.composits.HerkunftPart;
import org.d3s.alricg.editor.editors.composits.HerkunftVariantePart;
import org.d3s.alricg.editor.utils.EditorViewUtils;
import org.d3s.alricg.editor.utils.ViewEditorIdManager;
import org.d3s.alricg.editor.views.charElemente.RefreshableViewPart;
import org.d3s.alricg.store.access.CharElementFactory;
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.HerkunftVariante;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.RasseVariante;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Vincent
 *
 */
public class RasseVarianteEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.RasseVarianteEditor";
	
	private HerkunftPart herkunftPart;
	private RassePart rassePart;
	private HerkunftVariantePart herkunftVariantePart;
	private AbstractElementPart[] elementPartArray;
	
	ScrolledComposite scrollComp;
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#addCharElementSiteParts(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		herkunftPart = new HerkunftPart(mainContainer);
		herkunftPart.loadData((Herkunft) getEditedCharElement());
		herkunftPart.loadHerkunftTrees(this.getContainer(), this.getSite(), (Herkunft) getEditedCharElement());

		String[] entfernbareTags = new String[
		           HerkunftVariante.ALLE_TAGS.length +
		           RasseVariante.ALLE_TAGS.length];
		for (int i = 0; i < HerkunftVariante.ALLE_TAGS.length; i++) {
			entfernbareTags[i] = HerkunftVariante.ALLE_TAGS[i];
		}
		for (int i = HerkunftVariante.ALLE_TAGS.length; i < entfernbareTags.length; i++) {
			entfernbareTags[i] = RasseVariante.ALLE_TAGS[i - HerkunftVariante.ALLE_TAGS.length];
		}
		
		herkunftVariantePart = new HerkunftVariantePart(
				mainContainer, 
				entfernbareTags) {
			@Override
			public boolean canDropCharElement(CharElement charElem) {
				return charElem instanceof Rasse;
			}
		};
		herkunftVariantePart.loadData((RasseVariante) getEditedCharElement());
		
		Composite rassenComp = new Composite(this.getContainer(), SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		rassenComp.setLayout(gridLayout);
		
		scrollComp = new ScrolledComposite(getContainer(), SWT.V_SCROLL);
		scrollComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrollComp.setExpandHorizontal(true);
		scrollComp.setExpandVertical(true);
		scrollComp.setContent(rassenComp);
		
		// RassenPart erzeugen und anpassen
		rassePart = new RassePart(rassenComp, this.getSite(), 0, 0);
		rassePart.loadData((RasseVariante) getEditedCharElement());
		rassePart.cobArt.setEnabled(false);
		rassePart.cbxIsNegativListe.setEnabled(false);
		
		Point size = rassenComp.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scrollComp.setMinSize(0, size.y);
		
		// File kann nicht gewählt werden, steht durch Parent fest
		charElementPart.cobFile.setEnabled(false);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		int index = addPage(createCharElementSite());
		setPageText(index, EditorMessages.Editor_Daten);
		
		index = addPage(scrollComp);
		setPageText(index, "Rassen Daten");
		
		index = addPage(herkunftPart.getModisAuswahl().getTree());
		setPageText(index, "Modifikationen");
		
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
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		monitor.beginTask("Save CharElement and File", 5); //$NON-NLS-1$
		
		final XmlAccessor oldAccessor = currentAccessor;
		final CharElement charElement = getEditedCharElement();
		final Rasse oldParent = (Rasse) this.getEditorInput().getAdapter(Rasse.class);
		final Rasse newParent = (Rasse) herkunftVariantePart.getParten();
		final RefreshableViewPart viewPart = 
				(RefreshableViewPart) ViewEditorIdManager.getView(getEditedCharElement().getClass());
		
		// Save to Charelement
		for (int i = 0; i < getElementParts().length; i++) {
			getElementParts()[i].saveData(monitor, charElement);
		}

	// Aktualisiere Ansicht
		// 1. Element aus Ansicht entfernen
		if (!isNewElement) { // Nur wenn das Element nicht neu ist
			// Entferne vom View
			/*
			EditorViewUtils.removeElementFromView(
					viewPart,
					charElement);*/
			
			// Entferne vom "Parent"
			if (oldParent != newParent) {
				HerkunftVariante[] varianten = new HerkunftVariante[oldParent.getVarianten().length -1];
				
				int sub = 0;
				for (int i = 0; i < oldParent.getVarianten().length; i++) {
					if (oldParent.getVarianten()[i].equals(charElement)) {
						sub = 1;
						continue;
					}
					varianten[i-sub] = oldParent.getVarianten()[i];
				}
				oldParent.setVarianten((RasseVariante[]) varianten);
			}
		}
		// 2. Element zu Ansicht neu hinzufügen
		currentAccessor = herkunftVariantePart.getParentXmlAccessor();
		
		if (oldParent != newParent) {
			HerkunftVariante[] varianten = new HerkunftVariante[newParent.getVarianten().length +1];
			
			for (int i = 0; i < newParent.getVarianten().length-1; i++) {
				varianten[i] = oldParent.getVarianten()[i];
			}
			varianten[varianten.length-1] = (HerkunftVariante) charElement;
			oldParent.setVarianten((RasseVariante[]) varianten);
		}
		
		//CharElementFactory.getInstance().addCharElement(charElement, currentAccessor);
		/*
		EditorViewUtils.addElementToView(
				viewPart, 
				charElement, 
				currentAccessor);*/
		// 3. Ansicht aktualisieren
		if (viewPart != null) viewPart.refresh();
		
		// Save to File
		monitor.subTask("Save CharElement File"); //$NON-NLS-1$
		
		// TODO Besseres Speichern / Fehlerbehandlung!
		try {
			StoreAccessor.getInstance().saveFile( currentAccessor );
			if (!oldAccessor.equals(currentAccessor)) {
				StoreAccessor.getInstance().saveFile( oldAccessor );
			}
		} catch (JAXBException e) {
			// TODO Fehlerbehandlung
			monitor.setCanceled(true);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Fehlerbehandlung
			monitor.setCanceled(true);
			e.printStackTrace();
		}
		monitor.worked(1);
		
		monitor.done();
		
		isNewElement = false;
	}
}
