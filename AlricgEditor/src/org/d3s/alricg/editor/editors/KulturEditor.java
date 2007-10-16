/*
 * Created 04.09.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import java.util.List;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider.ImageProviderRegulator;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.common.widgets.DropTable;
import org.d3s.alricg.editor.common.widgets.DropTable.DropListRegulator;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.editor.editors.composits.AuswahlPart;
import org.d3s.alricg.editor.editors.composits.HerkunftPart;
import org.d3s.alricg.editor.editors.composits.AuswahlPart.HerkunftAuswahlRegulator;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.RegionVolk;
import org.d3s.alricg.store.charElemente.Schrift;
import org.d3s.alricg.store.charElemente.Sprache;
import org.d3s.alricg.store.charElemente.charZusatz.Gegenstand;
import org.d3s.alricg.store.charElemente.links.Auswahl;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * @author Vincent
 *
 */
public class KulturEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.KulturEditor";
	
	private HerkunftPart herkunftPart;
	private KulturPart kulturPart;
	private AuswahlPart schriftenPart;
	private AuswahlPart ausruestungPart;
	
	private static final Image imgDelete = ControlIconsLibrary.delete.getImageDescriptor().createImage();

	private AbstractElementPart[] elementPartArray;

	static class KulturPart extends AbstractElementPart<Kultur> {
		private final DropTable dropTableProfMoeglich;
		private final DropTable dropTableProfUeblich;
		protected Combo cobArt;
		protected Button cbxIsNegativListe;
		private final Label lblRegionVolkDND;
		
		KulturPart(Composite top, IWorkbenchPartSite site) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			
		// Wählbare Professionen
			GridData tmpGData = new GridData(GridData.GRAB_HORIZONTAL);
			tmpGData.widthHint = 600;
			tmpGData.horizontalSpan = 2; // nimm 2 Spalten Platz ein
			
			final Group groupProfs = new Group(top, SWT.SHADOW_IN);
			groupProfs.setText("Wählbare Professionen");
			groupProfs.setLayout(gridLayout);
			groupProfs.setLayoutData(tmpGData);
			
			final Label lblFiller = new Label(groupProfs, SWT.NONE);
			cbxIsNegativListe = new Button(groupProfs, SWT.CHECK);
			cbxIsNegativListe.setText("Professionen sind eine negativ Angabe");
			cbxIsNegativListe.setToolTipText("Bei einer negativ Angabe dürfen " +
					"alle Professionen gewählt werden, außer die aufgelisteten."); 
			
			final DropListRegulator regulator = new DropListRegulator() {
				@Override
				public boolean canDrop(Object obj) {
					if (obj == null) return false;
					if (obj instanceof Profession) {
						return true;
					}
					return false;
				}

				@Override
				public ImageProviderRegulator getImageProviderRegulator() {
					return null;
				}
			};
			
		// Übliche Professionen
			final Label lblUeblich = new Label(groupProfs, SWT.NONE);
			lblUeblich.setText("Übliche Professionen:");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			tmpGData.verticalIndent = 10;
			lblUeblich.setLayoutData(tmpGData);
			
			dropTableProfUeblich = new DropTable(groupProfs, SWT.NONE, regulator, site);

			// Mögliche Kulturen
			final Label lblMoeglich = new Label(groupProfs, SWT.NONE);
			lblMoeglich.setText("Mögliche Professionen:");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			tmpGData.verticalIndent = 10;
			lblMoeglich.setLayoutData(tmpGData);
			
			dropTableProfMoeglich = new DropTable(groupProfs, SWT.NONE, regulator, site);

			
			final Label lblArt = new Label(top, SWT.NONE);
			lblArt.setText("Art:");
			cobArt = new Combo(top, SWT.READ_ONLY);		
			cobArt.add(Kultur.KulturArt.menschlich.toString());
			cobArt.add(Kultur.KulturArt.nichtMenschlich.toString());
			cobArt.select(0);
			
			final Label lblRegionVolk = new Label(top, SWT.NONE);
			lblRegionVolk.setText("Region/Volk:");
			
			final Composite comRegionV = new Composite(top, SWT.NONE);
			gridLayout = new GridLayout();
			gridLayout.verticalSpacing = 1;
			gridLayout.marginHeight = 0;
			gridLayout.marginWidth = 0;
			gridLayout.numColumns = 2;
			tmpGData = new GridData();
			comRegionV.setLayout(gridLayout);
			comRegionV.setLayoutData(tmpGData);
			
			lblRegionVolkDND = new Label(comRegionV, SWT.BORDER);
			lblRegionVolkDND.setText("Per Drag and Drop wählen");
			lblRegionVolkDND.setSize(120, lblRegionVolkDND.getSize().y);
			tmpGData = new GridData(); 
			tmpGData.widthHint = 125;
			lblRegionVolkDND.setLayoutData(tmpGData);
			
			final Button butDelete = new Button(comRegionV, SWT.NONE);
			tmpGData = new GridData(imgDelete.getImageData().width+4, imgDelete.getImageData().height+4); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			butDelete.setLayoutData(tmpGData);
			butDelete.setImage(imgDelete);
			butDelete.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					lblRegionVolkDND.setText("Per Drag and Drop wählen");
					lblRegionVolkDND.setData(null);
				}
			});
			
		    final DropTarget target = new DropTarget(
		    		lblRegionVolkDND,
		    		DND.DROP_COPY | DND.DROP_MOVE);
		    target.setTransfer(new Transfer[] { LocalSelectionTransfer.getTransfer() });
		    target.addDropListener(new DropTargetAdapter() {
		      public void drop(DropTargetEvent event) {
		        if (event.data == null) {
		          return;
		        }
		        final TreeOrTableObject treeObj = (TreeOrTableObject) ((IStructuredSelection) event.data).getFirstElement();
		        final CharElement charElem = ((CharElement) treeObj.getValue());
		        
		        if ( !(charElem instanceof RegionVolk) ) return;
		        
		        lblRegionVolkDND.setText(charElem.getName());
		        lblRegionVolkDND.setData(charElem);
		      }
		    });
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#dispose()
		 */
		@Override
		public void dispose() {
			imgDelete.dispose();
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public boolean isDirty(Kultur charElem) {
			boolean isNotDirty = true;
			
			// Kulturen
			isNotDirty &= cbxIsNegativListe.getSelection() == charElem.isProfessionenSindNegativListe();
			isNotDirty &= compareArrayList(
							charElem.getProfessionMoeglich(), 
							dropTableProfMoeglich.getValueList());
			isNotDirty &= compareArrayList(
							charElem.getProfessionUeblich(), 
							dropTableProfUeblich.getValueList());
			
			isNotDirty &= cobArt.getText().equals(charElem.getArt().toString());
			if (lblRegionVolkDND.getData() == null  && charElem.getRegionVolk() == null) {
				// Noop
			} else if (lblRegionVolkDND.getData() == null) {
				return true;
			} else {
				isNotDirty &= lblRegionVolkDND.getData().equals(charElem.getRegionVolk());
			}
			
			return !isNotDirty;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void loadData(Kultur charElem) {
			// Professionen
			cbxIsNegativListe.setSelection(charElem.isProfessionenSindNegativListe());
			if (charElem.getProfessionMoeglich() != null) {
				for (int i = 0; i < charElem.getProfessionMoeglich().length; i++) {
					dropTableProfMoeglich.addValue(charElem.getProfessionMoeglich()[i]);
				}
			}
			if (charElem.getProfessionUeblich() != null) {
				for (int i = 0; i < charElem.getProfessionUeblich().length; i++) {
					dropTableProfUeblich.addValue(charElem.getProfessionUeblich()[i]);
				}
			}
			
			cobArt.setText(charElem.getArt().toString());
			if (charElem.getRegionVolk() != null) {
				lblRegionVolkDND.setData(charElem.getRegionVolk());
				lblRegionVolkDND.setText(charElem.getRegionVolk().getName());
			}
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void saveData(IProgressMonitor monitor, Kultur charElem) {
			monitor.subTask("Save Kultur-Data"); //$NON-NLS-1$
			
			// Professionen
			charElem.setProfessionenSindNegativListe(cbxIsNegativListe.getSelection());
			List l = dropTableProfMoeglich.getValueList();
			if (l.size() == 0) {
				charElem.setProfessionMoeglich(null);
			} else {
				Profession[] profs = new Profession[l.size()];
				for (int i = 0; i < profs.length; i++) {
					profs[i] = (Profession) l.get(i);
				}
				charElem.setProfessionMoeglich(profs);
			}
			l = dropTableProfUeblich.getValueList();
			if (l.size() == 0) {
				charElem.setProfessionUeblich(null);
			} else {
				Profession[] profs = new Profession[l.size()];
				for (int i = 0; i < profs.length; i++) {
					profs[i] = (Profession) l.get(i);
				}
				charElem.setProfessionUeblich(profs);
			}
			
			for (int i = 0; i < Kultur.KulturArt.values().length; i++) {
				if (cobArt.getText().equals(Kultur.KulturArt.values()[i].toString())) {
					charElem.setArt(Kultur.KulturArt.values()[i]);
					break;
				}
			}
			
			charElem.setRegionVolk((RegionVolk) lblRegionVolkDND.getData());
			
			monitor.worked(1);
		}
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#addCharElementSiteParts(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		herkunftPart = new HerkunftPart(mainContainer);
		herkunftPart.loadData((Herkunft) getEditedCharElement());
		herkunftPart.loadHerkunftTrees(this.getContainer(), this.getSite(), (Herkunft) getEditedCharElement());

		schriftenPart = new AuswahlPart(
				this.getContainer(), 
				this.getSite(),
				new KulturSprachenAuswahlRegulator() );
		schriftenPart.loadData((Herkunft) getEditedCharElement());
		
		ausruestungPart = new AuswahlPart(
				this.getContainer(), 
				this.getSite(),
				new KulturAusruestungRegulator());
		ausruestungPart.loadData((Herkunft) getEditedCharElement());
		
		kulturPart = new KulturPart(mainContainer, this.getSite());
		kulturPart.loadData((Kultur) getEditedCharElement());
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		int index = addPage(createCharElementSite());
		setPageText(index, EditorMessages.Editor_Daten);
		
		index = addPage(herkunftPart.getModisAuswahl().getTree());
		setPageText(index, "Modifikationen");
		
		index = addPage(schriftenPart.getTree());
		setPageText(index, "Sprachen");
		
		index = addPage(herkunftPart.getAltervativeZauberPart().getControl());
		setPageText(index, "Alternative Zauberauswahl");
		
		index = addPage(herkunftPart.getVerbilligtPart().getTree());
		setPageText(index, "Verbilligungen");
		
		index = addPage(herkunftPart.getEmpfehlungenPart().getTree());
		setPageText(index, "Empfehlungen");
		
		index = addPage(herkunftPart.getAktivierbareZauberPart().getTree());
		setPageText(index, "Aktivierbare Zauber");
		
		index = addPage(ausruestungPart.getTree());
		setPageText(index, "Ausrüstung");
		
		index = addPage(createVoraussetzungsSite());
		setPageText(index, EditorMessages.Editor_Voraussetzungen);
		
		elementPartArray = new AbstractElementPart[] {
				charElementPart,
				herkunftPart,
				kulturPart,
				herkunftPart.getModisAuswahl(),
				schriftenPart,
				herkunftPart.getVerbilligtPart(),
				herkunftPart.getEmpfehlungenPart(),
				herkunftPart.getAktivierbareZauberPart(),
				ausruestungPart,
				voraussetzungsPart};
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getEditedCharElement()
	 */
	@Override
	protected CharElement getEditedCharElement() {
		return (Kultur) this.getEditorInput().getAdapter(Kultur.class);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getElementParts()
	 */
	@Override
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}
	
	static class KulturAusruestungRegulator implements HerkunftAuswahlRegulator {
		private static final Class[] classes =  new Class[] {Gegenstand.class};
		private static final String[] text = new String[] { "Gegenstände"};
		
		private static final int GEGENSTAND = 0;

		@Override
		public String[] getCharElementText() {
			return text;
		}

		@Override
		public Auswahl getAuswahl(Herkunft herkunft, int index) {
			switch(index) {
				case GEGENSTAND: return ((Kultur) herkunft).getAusruestung();
				default:
					throw new IllegalArgumentException("Keine behandlung für den Index" +
							index + " vorhanden!");
			}
		}

		@Override
		public Class[] getCharElementClazz() {
			return classes;
		}

		@Override
		public void setAuswahl(Herkunft herkunft, Auswahl auswahl, int index) {
			switch(index) {
				case GEGENSTAND: ((Kultur) herkunft).setAusruestung(auswahl); break;
				default:
					throw new IllegalArgumentException("Keine behandlung für den Index" +
							index + " vorhanden!");
			}
		}
	}
	
	static class KulturSprachenAuswahlRegulator implements HerkunftAuswahlRegulator {
		private static final Class[] classes =  new Class[] {
			Sprache.class, Sprache.class, Sprache.class, 
			Sprache.class, Schrift.class};
		
		private static final String[] text = new String[] { 
				"Muttersprache", 
				"Zweitsprache",
				"Lehrsprache",
				"Sprachen",
				"Schriften"
			};
		private static final int MUTTERSPR = 0;
		private static final int ZWEITSPR = 1;
		private static final int LEHRSPR = 2;
		private static final int SPRACHEN = 3;
		private static final int SCHRIFTEN = 4;
		
		@Override
		public String[] getCharElementText() {
			return text;
		}

		@Override
		public Auswahl getAuswahl(Herkunft herkunft, int index) {
			switch(index) {
				case MUTTERSPR: return ((Kultur) herkunft).getMuttersprache();
				case ZWEITSPR: return ((Kultur) herkunft).getZweitsprache();
				case LEHRSPR: return ((Kultur) herkunft).getLehrsprache();
				case SPRACHEN: return ((Kultur) herkunft).getSprachen();
				case SCHRIFTEN: return ((Kultur) herkunft).getSchriften();
				default:
					throw new IllegalArgumentException("Keine behandlung für den Index" +
							index + " vorhanden!");
			}
		}

		@Override
		public Class[] getCharElementClazz() {
			return classes;
		}

		@Override
		public void setAuswahl(Herkunft herkunft, Auswahl auswahl, int index) {
			switch(index) {
				case MUTTERSPR: ((Kultur) herkunft).setMuttersprache(auswahl); break;
				case ZWEITSPR: ((Kultur) herkunft).setZweitsprache(auswahl); break;
				case LEHRSPR: ((Kultur) herkunft).setLehrsprache(auswahl); break;
				case SPRACHEN: ((Kultur) herkunft).setSprachen(auswahl); break;
				case SCHRIFTEN: ((Kultur) herkunft).setSchriften(auswahl); break;
				default:
					throw new IllegalArgumentException("Keine behandlung für den Index" +
							index + " vorhanden!");
			}
		}
	}

}
