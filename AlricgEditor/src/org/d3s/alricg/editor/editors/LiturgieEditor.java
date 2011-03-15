/*
 * Created 30.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import java.util.Arrays;

import org.d3s.alricg.common.icons.AbstractIconsLibrary;
import org.d3s.alricg.common.icons.GoetterIconsLibrary;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider.ImageProviderRegulator;
import org.d3s.alricg.editor.common.widgets.DropTable;
import org.d3s.alricg.editor.common.widgets.IntegerList;
import org.d3s.alricg.editor.common.widgets.DropTable.DropListRegulator;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Vincent
 *
 */
public class LiturgieEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.LiturgieEditor";
	
	private LiturgiePart liturgiePart;

	private AbstractElementPart[] elementPartArray;

	class LiturgiePart extends AbstractElementPart<Liturgie> {
		private DropTable dropTableGottheiten;
		private final IntegerList spiGrad;
		private Combo cobArt;
		
		LiturgiePart(Composite top) {
			GridData tmpGData;
			
		// Art
			final Label label0 = new Label(top, SWT.NONE);
			label0.setText("Art:");
			 
			cobArt = new Combo(top, SWT.READ_ONLY);
			cobArt.setVisibleItemCount(3);
			cobArt.add(Liturgie.LiturgieArt.allgemein.getValue());
			cobArt.add(Liturgie.LiturgieArt.speziell.getValue());
			cobArt.add(Liturgie.LiturgieArt.hochschamanen.getValue());
			cobArt.select(0);
			
		// Gottheiten
			final Label label1 = new Label(top, SWT.NONE);
			label1.setText("Gottheiten:");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			tmpGData.verticalIndent = 10;
			label1.setLayoutData(tmpGData);
			
			final DropListRegulator regulator = new DropListRegulator() {
				@Override
				public boolean canDrop(Object obj) {
					if (obj == null) return false;
					if (obj instanceof Gottheit) {
						return true;
					}
					return false;
				}

				@Override
				public ImageProviderRegulator getImageProviderRegulator() {
					return new ImageProviderRegulator<Gottheit>() {
						public String getName(Gottheit obj) {
							return obj.getName();
						}
						public Gottheit[] getItems(CharElement obj) {
							return new Gottheit[] { (Gottheit) obj };
						}
						public AbstractIconsLibrary<Gottheit> getIconsLibrary() {
							return GoetterIconsLibrary.getInstance();
						}
						public IWorkbenchPart getConsumer() {
							return LiturgieEditor.this;
						}
					};
				}
			};
			
			dropTableGottheiten = new DropTable(top, SWT.NONE, regulator, LiturgieEditor.this.getSite());
			
		// Grad
			final Label lblGrad = new Label(top, SWT.NONE);
			lblGrad.setText("Grad:");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			tmpGData.verticalIndent = 10;
			lblGrad.setLayoutData(tmpGData);
			spiGrad = new IntegerList (top, SWT.NONE, 1, 6, 1);
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
		public boolean isDirty(Liturgie charElem) {
			boolean isNotDirty = true;
			
			isNotDirty &= cobArt.getText().equals(charElem.getArt().getValue());
			isNotDirty &= Arrays.equals(spiGrad.getValueList(), charElem.getGrad());
			isNotDirty &= compareArrayList(charElem.getGottheit(), dropTableGottheiten.getValueList());
			
			return !isNotDirty;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void loadData(Liturgie charElem) {
			cobArt.setText(charElem.getArt().getValue());
			
			if (charElem.getGrad() != null) {
				spiGrad.setValues(charElem.getGrad());
			}
			if (charElem.getGottheit() == null) return;
			
			for (int i = 0; i < charElem.getGottheit().length; i++) {
				dropTableGottheiten.addValue(charElem.getGottheit()[i]);
			}
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void saveData(IProgressMonitor monitor, Liturgie charElem) {
			monitor.subTask("Save Liturgie-Data"); //$NON-NLS-1$
			
			for (int i = 0; i < Liturgie.LiturgieArt.values().length; i++) {
				if (Liturgie.LiturgieArt.values()[i].getValue().equals(cobArt.getText())) {
					charElem.setArt(Liturgie.LiturgieArt.values()[i]);
					break;
				}
			}
			
			if (spiGrad.getValueList().length == 0) {
				charElem.setGrad(null);
			} else {
				charElem.setGrad(spiGrad.getValueList());
			}
			
			if (dropTableGottheiten.getValueList().size() == 0) {
				charElem.setGottheit(null);
			} else {
				charElem.setGottheit( (Gottheit[])
						dropTableGottheiten.getValueList().toArray(
								new Gottheit[dropTableGottheiten.getValueList().size()])
				);
			}
			
			monitor.worked(1);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#addCharElementSiteParts(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		// Liturgie spezifische Elemte erzeugen
		liturgiePart = this.new LiturgiePart(mainContainer);
		liturgiePart.loadData((Liturgie) getEditedCharElement());
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
				charElementPart, liturgiePart, voraussetzungsPart};
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getEditedCharElement()
	 */
	@Override
	protected CharElement getEditedCharElement() {
		return (Liturgie) this.getEditorInput().getAdapter(Liturgie.class);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getElementParts()
	 */
	@Override
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}

}
