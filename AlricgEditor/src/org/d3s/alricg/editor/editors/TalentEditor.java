/*
 * Created 25.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.bind.JAXBException;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.editors.composits.AbstarctElementPart;
import org.d3s.alricg.editor.editors.composits.CharElementPart;
import org.d3s.alricg.editor.editors.composits.FaehigkeitPart;
import org.d3s.alricg.editor.editors.composits.VoraussetzungPart;
import org.d3s.alricg.editor.utils.EditorViewUtils;
import org.d3s.alricg.editor.views.charElemente.RefreshableViewPart;
import org.d3s.alricg.store.access.CharElementFactory;
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.Talent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;

/**
 * @author Vincent
 */
public class TalentEditor extends MultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.TalentEditor";
	private CharElementPart charElementPart;
	private FaehigkeitPart faehigkeitPart;
	private TalentPart talentPart;
	private VoraussetzungPart voraussetzungsPart;
	private XmlAccessor currentAccessor;
	
	private ScrolledComposite scrollComp;
	
	
	/**
	 * Stellt Controls für die Felder der Klasse Talent bereit und verwaltet diese.
	 * @author Vincent
	 */
	class TalentPart extends AbstarctElementPart<Talent> {
		private Combo cobArt;
		private Combo cobSorte;
		private Text txtSpez;
		private List listSpez;
		private Button butAdd;
		private Button butDelete;
		private Composite container;
		
		private Image imgAdd = ControlIconsLibrary.add.getImageDescriptor().createImage();
		private Image imgDelete = ControlIconsLibrary.delete.getImageDescriptor().createImage();
		
		// Konstruktor
		TalentPart(Composite top) {

			// ComboBox Art
			Label lblArt = new Label(top, SWT.NONE);
			lblArt.setText("Art:");
			cobArt = new Combo(top, SWT.READ_ONLY);
			cobArt.add(Talent.Art.basis.toString());
			cobArt.add(Talent.Art.beruf.toString());
			cobArt.add(Talent.Art.spezial.toString());
			cobArt.select(0);
			
			// ComboBox Sorte
			Label lblSorte = new Label(top, SWT.NONE);
			lblSorte.setText("Sorte:");
			cobSorte = new Combo(top, SWT.READ_ONLY);
			cobSorte.setVisibleItemCount(6);
			cobSorte.add(Talent.Sorte.gesellschaft.toString());
			cobSorte.add(Talent.Sorte.handwerk.toString());
			cobSorte.add(Talent.Sorte.kampf.toString());
			cobSorte.add(Talent.Sorte.koerper.toString());
			cobSorte.add(Talent.Sorte.natur.toString());
			cobSorte.add(Talent.Sorte.wissen.toString());
			cobSorte.select(0);
			

		// Container für Spezialisierungen
			container = new Composite (top, SWT.NONE);
			GridData containerGridData = new GridData(GridData.GRAB_HORIZONTAL);
			containerGridData.widthHint = 300;
			containerGridData.horizontalSpan = 2;
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 3;
			
			container.setLayout(gridLayout);
			container.setLayoutData(containerGridData);
			
			// Spalte 1 der containerGridData
			Label lblSpezi = new Label(container, SWT.NONE);
			lblSpezi.setText("Spezialisierungen:");
			txtSpez = new Text(container, SWT.BORDER);
			txtSpez.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			txtSpez.addListener (SWT.DefaultSelection, new Listener () {
				public void handleEvent (Event e) {
					addToList();
				}
			});

			butAdd = new Button(container, SWT.NONE);
			butAdd.setImage(imgAdd);
			butAdd.setLayoutData(new GridData(imgAdd.getImageData().width+4, imgAdd.getImageData().height+4));
			butAdd.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					addToList();
				}
			});
			
			// Spalte 2 der containerGridData
			Label lblFiller = new Label(container, SWT.NONE);
			
			listSpez = new List (container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
			GridData tmpGData = new GridData(GridData.FILL_BOTH); 
			tmpGData.heightHint = txtSpez.getLineHeight() * 4;
			listSpez.setLayoutData(tmpGData);
			
			butDelete = new Button(container, SWT.NONE);
			tmpGData = new GridData(imgDelete.getImageData().width+4, imgDelete.getImageData().height+4); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			butDelete.setLayoutData(tmpGData);
			butDelete.setImage(imgDelete);
			butDelete.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					// Lösche alle Markierten
					listSpez.remove(listSpez.getSelectionIndices());
				}
			});
		}
		
		// Fügt den aktuellen Text im txtSpez nach Prüfung zur List hinzu 
		private void addToList() {
			// Prüfe ob leer
			String text = txtSpez.getText().trim();
			if (text.length() == 0) return;
			
			// Prüfe ob doppelt
			String[] strs = listSpez.getItems();
			for (int i = 0; i < strs.length; i++) {
				if (strs[i].equalsIgnoreCase(text)) return;
			}
			
			// Wert hinzufügen
			listSpez.add(text);
			txtSpez.setText("");
		}
		
		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#dispose()
		 */
		@Override
		public void dispose() {
			imgAdd.dispose();
			imgDelete.dispose();
			container.dispose();
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void loadData(Talent charElem) {
			ViewUtils.findAndSetIndex(cobArt, charElem.getArt().name());
			ViewUtils.findAndSetIndex(cobSorte, charElem.getSorte().name());
			
			if (charElem.getSpezialisierungen() == null) return;
			for (int i = 0; i < charElem.getSpezialisierungen().length; i++) {
				listSpez.add(charElem.getSpezialisierungen()[i]);
			}
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#saveData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void saveData(IProgressMonitor monitor, Talent charElem) {
			monitor.subTask("Save Talent-Data");
			
			// Art setzen
			for (int i = 0; i < Talent.Art.values().length; i++) {
				if ( cobArt.getText().equals(Talent.Art.values()[i].name()) ) {
					charElem.setArt(Talent.Art.values()[i]);
					break;
				}
			}
			
			// Sorte setzen
			for (int i = 0; i < Talent.Sorte.values().length; i++) {
				if ( cobSorte.getText().equals(Talent.Sorte.values()[i].name()) ) {
					charElem.setSorte(Talent.Sorte.values()[i]);
					break;
				}
			}
			
			// Spezialisierung sichern
			if (listSpez.getItems().length == 0) {
				charElem.setSpezialisierungen(null);
			} else {
				charElem.setSpezialisierungen(
						Arrays.copyOf(
								listSpez.getItems(), 
								listSpez.getItems().length
						));
			}
			
			monitor.worked(1);
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public boolean isDirty(Talent charElem) {
			boolean isNotDirty = true;
			
			isNotDirty &= cobArt.getText().equals(charElem.getArt().name());
			isNotDirty &= cobSorte.getText().equals(charElem.getSorte().name());
			
			if (listSpez.getItems().length == 0) {
				isNotDirty &= (charElem.getSpezialisierungen() == null);
			} else {
				isNotDirty &= Arrays.equals(charElem.getSpezialisierungen(), listSpez.getItems());
			}
			
			return !isNotDirty;
		}
	}
	
	private Composite createPage1() {
		
		Talent talentInput = (Talent) this.getEditorInput().getAdapter(Talent.class);
		
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
		charElementPart = new CharElementPart(mainContainer, basisDatenGridData);
		charElementPart.loadData(talentInput); 
		charElementPart.setSelectedXmlAccessor(
				(XmlAccessor) this.getEditorInput().getAdapter(XmlAccessor.class)
			);
		
		// FaehigkeitPart erzeugen
		faehigkeitPart = new FaehigkeitPart(mainContainer, null);
		faehigkeitPart.loadData(talentInput); 
		
		// Talent spezifische Elemte erzeugen
		talentPart = this.new TalentPart(mainContainer);
		talentPart.loadData(talentInput);
		
		//Große für Scroll-Container festlegen
		Point size = mainContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scrollComp.setMinSize(0, size.y);
		
		return scrollComp;
	}
	
	private Composite createPage2() {
		Talent talentInput = (Talent) this.getEditorInput().getAdapter(Talent.class);

		voraussetzungsPart = new VoraussetzungPart(getContainer(), this.getSite());
		voraussetzungsPart.loadData(talentInput);
		
		return voraussetzungsPart.getTree();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {

		int index1 = addPage(createPage1());
		setPageText(index1, "Daten");
		
		index1 = addPage(createPage2());
		setPageText(index1, "Voraussetzungen");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		monitor.beginTask("Save Talent to CharElement and File", 5);
		final XmlAccessor oldAccessor = currentAccessor;
		final Talent talent = (Talent) this.getEditorInput().getAdapter(Talent.class);
		final RefreshableViewPart viewPart = 
				(RefreshableViewPart) this.getEditorInput().getAdapter(RefreshableViewPart.class);
		
		// Save to Charelement
		this.charElementPart.saveData(monitor, talent);
		this.faehigkeitPart.saveData(monitor, talent);
		this.talentPart.saveData(monitor, talent);
		this.voraussetzungsPart.saveData(monitor, talent);
		
	// Aktualisiere Ansicht
		// 1. Element aus Ansicht entfernen
		EditorViewUtils.removeElementFromView(
				viewPart,
				talent);
		CharElementFactory.getInstance().deleteCharElement(
				talent, 
				oldAccessor);
		// 2. Element zu Ansicht neu hinzufügen
		currentAccessor = this.charElementPart.getSelectedXmlAccessor();
		EditorViewUtils.addElementToView(
				viewPart, 
				talent, 
				currentAccessor);
		CharElementFactory.getInstance().addCharElement(talent, currentAccessor);
		// 3. Ansicht aktualisieren
		viewPart.refresh();
		
		// Save to File
		monitor.subTask("Save Talent File");
		
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
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		Talent tal =  (Talent) this.getEditorInput().getAdapter(Talent.class);
		
		return charElementPart.isDirty(tal)
						|| voraussetzungsPart.isDirty(tal)
						|| faehigkeitPart.isDirty(tal) 
						|| talentPart.isDirty(tal);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		String name =  ((Talent) this.getEditorInput().getAdapter(Talent.class)).getName();
		currentAccessor = (XmlAccessor) this.getEditorInput().getAdapter(XmlAccessor.class);
		this.setPartName("Talent " + name);
		this.setContentDescription("Bearbeiten des Talents " + name);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#dispose()
	 */
	@Override
	public void dispose() {
		scrollComp.dispose();
		charElementPart.dispose();
		talentPart.dispose();
		super.dispose();
	}
}
