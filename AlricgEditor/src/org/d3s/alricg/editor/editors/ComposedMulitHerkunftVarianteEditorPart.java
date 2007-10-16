/*
 * Created 14.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.d3s.alricg.editor.editors.composits.HerkunftPart;
import org.d3s.alricg.editor.editors.composits.HerkunftVariantePart;
import org.d3s.alricg.editor.utils.EditorViewUtils;
import org.d3s.alricg.editor.utils.ViewEditorIdManager;
import org.d3s.alricg.editor.views.charElemente.RefreshableViewPart;
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.HerkunftVariante;
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
public abstract class ComposedMulitHerkunftVarianteEditorPart extends ComposedMultiPageEditorPart {

	protected HerkunftPart herkunftPart;
	protected HerkunftVariantePart herkunftVariantePart;
	protected ScrolledComposite herkunftDatenScroll;
	
	
	protected abstract HerkunftVariante[] getHerkunftVariante(int length);
	protected abstract void createHerkunftPart(Composite herkunftComp);
	protected abstract String[] getSpezifischeTags();
	protected abstract boolean canDropElement(CharElement charElem);
	
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
		           getSpezifischeTags().length];
		for (int i = 0; i < HerkunftVariante.ALLE_TAGS.length; i++) {
			entfernbareTags[i] = HerkunftVariante.ALLE_TAGS[i];
		}
		for (int i = HerkunftVariante.ALLE_TAGS.length; i < entfernbareTags.length; i++) {
			entfernbareTags[i] = getSpezifischeTags()[i - HerkunftVariante.ALLE_TAGS.length];
		}
		
		herkunftVariantePart = new HerkunftVariantePart(
				mainContainer, 
				entfernbareTags) {
			@Override
			public boolean canDropCharElement(CharElement charElem) {
				return canDropElement(charElem);
			}
		};
		herkunftVariantePart.loadData((HerkunftVariante) getEditedCharElement());
		
		herkunftDatenScroll = new ScrolledComposite(getContainer(), SWT.V_SCROLL);
		herkunftDatenScroll.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		herkunftDatenScroll.setExpandHorizontal(true);
		herkunftDatenScroll.setExpandVertical(true);
		
		Composite herkunftComp = new Composite(herkunftDatenScroll, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		herkunftComp.setLayout(gridLayout);
		
		herkunftDatenScroll.setContent(herkunftComp);
		
		createHerkunftPart(herkunftComp);
		
		Point size = herkunftComp.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		herkunftDatenScroll.setMinSize(0, size.y);
		
		// File kann nicht gewählt werden, steht durch Parent fest
		charElementPart.cobFile.setEnabled(false);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		monitor.beginTask("Save CharElement and File", 5); //$NON-NLS-1$
		
		final XmlAccessor oldAccessor = currentAccessor;
		final CharElement charElement = getEditedCharElement();
		final Herkunft oldParent = (Herkunft) ((HerkunftVariante) charElement).getVarianteVon();
		final Herkunft newParent = (Herkunft) herkunftVariantePart.getParent();
		final RefreshableViewPart viewPart = 
				(RefreshableViewPart) ViewEditorIdManager.getView(getEditedCharElement().getClass());
		
		// Save to Charelement
		for (int i = 0; i < getElementParts().length; i++) {
			getElementParts()[i].saveData(monitor, charElement);
		}

		if (newParent.getVarianten() == null) newParent.setVarianten( getHerkunftVariante(0) );
		
		// 1. Element vom alten "Parent" entfernen
		if (!isNewElement && oldParent != newParent) { // Nur wenn das Element nicht neu ist
			// Entferne vom "Parent"
			HerkunftVariante[] varianten = getHerkunftVariante(oldParent.getVarianten().length -1);
			
			int sub = 0;
			for (int i = 0; i < oldParent.getVarianten().length; i++) {
				if (oldParent.getVarianten()[i].equals(charElement)) {
					sub = 1;
					continue;
				}
				varianten[i-sub] = oldParent.getVarianten()[i];
			}
			oldParent.setVarianten(varianten);
		}
		
		// Neue Variante zum (neuen) Parent hinzufügen
		if (oldParent != newParent || isNewElement) {
			HerkunftVariante[] varianten = getHerkunftVariante(newParent.getVarianten().length +1);
			
			for (int i = 0; i < newParent.getVarianten().length; i++) {
				varianten[i] = newParent.getVarianten()[i];
			}
			varianten[varianten.length-1] = (HerkunftVariante) charElement;
			newParent.setVarianten(varianten);
		}
		
		// Aktualisiere Ansicht
		EditorViewUtils.addAndRemoveHerkunftToView(
				viewPart, 
				(HerkunftVariante) charElement, 
				currentAccessor,
				isNewElement);
		
		// Refresh
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
