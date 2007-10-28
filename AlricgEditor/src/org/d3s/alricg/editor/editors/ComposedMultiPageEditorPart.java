/*
 * Created 22.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.d3s.alricg.common.CharElementTextService;
import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.common.ViewUtils.ObjectCreator;
import org.d3s.alricg.editor.common.ViewUtils.TableObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.editor.editors.composits.CharElementPart;
import org.d3s.alricg.editor.editors.composits.VoraussetzungPart;
import org.d3s.alricg.editor.utils.ViewEditorIdManager;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTableObject;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTreeObject;
import org.d3s.alricg.editor.views.charElemente.RefreshableViewPart;
import org.d3s.alricg.store.access.CharElementFactory;
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;

/**
 * @author Vincent
 *
 */
public abstract class ComposedMultiPageEditorPart extends MultiPageEditorPart {
	protected CharElementPart charElementPart;
	protected VoraussetzungPart voraussetzungsPart;
	protected ScrolledComposite scrollComp;
	protected boolean isNewElement;
	
	protected XmlAccessor currentAccessor;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#createPages()
	 */
	@Override
	protected abstract void createPages();
	
	protected Composite createCharElementSite() {
		
		// Scroll-Container erzeugen und Grid mit 2 Spalten setzen
		scrollComp = new ScrolledComposite(getContainer(), SWT.V_SCROLL);
		scrollComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrollComp.setExpandHorizontal(true);
		scrollComp.setExpandVertical(true);

		// Container erzeugen, Grid mit 2 Spalten setzen, Scroll-Container setzen
		final Composite mainContainer = new Composite (scrollComp, SWT.NONE);
		scrollComp.setContent(mainContainer);
		
		// Layout erzeugen
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		mainContainer.setLayout(gridLayout);
		
		// BasisDaten Gruppe erzeugen 
		GridData basisDatenGridData = new GridData(GridData.GRAB_HORIZONTAL);
		basisDatenGridData.widthHint = 600;
		basisDatenGridData.horizontalSpan = 2; // nimm 2 Spalten Platz ein
		charElementPart = new CharElementPart(
				mainContainer, 
				basisDatenGridData,
				(Boolean) this.getEditorInput().getAdapter(Boolean.class));
		charElementPart.loadData(getEditedCharElement()); 
		charElementPart.setSelectedXmlAccessor(
				(XmlAccessor) this.getEditorInput().getAdapter(XmlAccessor.class)
			);
		
		// Alle anderen Parts für diese site laden
		addCharElementSiteParts(mainContainer);
		
		//Große für Scroll-Container festlegen
		Point size = mainContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scrollComp.setMinSize(0, size.y);
		
		return scrollComp;
	}
	
	protected Composite createVoraussetzungsSite() {		
		voraussetzungsPart = new VoraussetzungPart(getContainer(), this.getSite());
		voraussetzungsPart.loadData(getEditedCharElement());
		
		return voraussetzungsPart.getTree();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		String name =  getEditedCharElement().getName();
		currentAccessor = (XmlAccessor) this.getEditorInput().getAdapter(XmlAccessor.class);
		isNewElement = (Boolean) this.getEditorInput().getAdapter(Boolean.class);
			
		this.setPartName(
				CharElementTextService.getCharElementName(getEditedCharElement().getClass())
				 + " " + name
			);
		this.setContentDescription(EditorMessages.TalentEditor_EditorDescription + this.getPartName());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		monitor.beginTask("Save CharElement and File", 5); //$NON-NLS-1$
		final XmlAccessor oldAccessor = currentAccessor;
		final CharElement charElement = getEditedCharElement();
		final RefreshableViewPart viewPart = 
				(RefreshableViewPart) ViewEditorIdManager.getView(getEditedCharElement().getClass());
		
		// Save to Charelement
		for (int i = 0; i < getElementParts().length; i++) {
			getElementParts()[i].saveData(monitor, charElement);
		}

	// Aktualisiere Ansicht
		// 1. Element aus Ansicht entfernen
		if (!isNewElement) { // Nur wenn das Element nicht neu ist
			ViewUtils.removeElementFromView(
					viewPart,
					charElement);
			CharElementFactory.getInstance().deleteCharElement(
					charElement, 
					oldAccessor);
		}
		// 2. Element zu Ansicht neu hinzufügen
		currentAccessor = this.charElementPart.getSelectedXmlAccessor();
		CharElementFactory.getInstance().addCharElement(charElement, currentAccessor);
		
		final ObjectCreator objCreator = new ObjectCreator() {
			
			@Override
			public TableObject createTableObject(Object element) {
				return new EditorTableObject(element, currentAccessor);
			}

			@Override
			public TreeObject createTreeObject(Object element, TreeObject parentNode) {
				return new EditorTreeObject(element, parentNode, currentAccessor);
			}
		};
		
		ViewUtils.addElementToView(
				viewPart, 
				charElement, 
				objCreator);
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

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		// Noop! Ist nicht gestattet
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		// TODO Keine gute Lösung...
		firePropertyChange(PROP_DIRTY);
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		final CharElement charElem = getEditedCharElement();
		
		for (int i = 0; i < getElementParts().length; i++) {
			if (getElementParts()[i].isDirty(charElem)) {
				return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#dispose()
	 */
	@Override
	public void dispose() {
		for (int i = 0; i < getElementParts().length; i++) {
			getElementParts()[i].dispose();
		}
		super.dispose();
	}
	
	protected abstract AbstractElementPart[] getElementParts();
	
	protected abstract CharElement getEditedCharElement();
	
	protected abstract void addCharElementSiteParts(Composite mainContainer);
	
}
