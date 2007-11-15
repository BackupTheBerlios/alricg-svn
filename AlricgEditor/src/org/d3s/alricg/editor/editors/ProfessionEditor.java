/*
 * Created 04.09.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.editor.editors.composits.AuswahlPart;
import org.d3s.alricg.editor.editors.composits.HerkunftPart;
import org.d3s.alricg.editor.editors.composits.AuswahlPart.HerkunftAuswahlRegulator;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.Schrift;
import org.d3s.alricg.store.charElemente.Sprache;
import org.d3s.alricg.store.charElemente.charZusatz.Gegenstand;
import org.d3s.alricg.store.charElemente.links.Auswahl;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author Vincent
 *
 */
public class ProfessionEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.ProfessionEditor";

	private HerkunftPart herkunftPart;
	private ProfessionPart professionPart;
	private AuswahlPart schriftenPart;
	private AuswahlPart ausruestungPart;

	private AbstractElementPart[] elementPartArray;
	
	static class ProfessionPart extends AbstractElementPart<Profession> {
		protected Combo cobArt;
		private final Combo cobAufwand;
		
		ProfessionPart(Composite top) {
			
			final Label lblArt = new Label(top, SWT.NONE);
			lblArt.setText("Art:");
			cobArt = new Combo(top, SWT.READ_ONLY);
			for (int i = 0; i < Profession.ProfArt.values().length; i++) {
				cobArt.add(Profession.ProfArt.values()[i].getValue());
			}
			cobArt.select(0);
			
			final Label lblAufwand = new Label(top, SWT.NONE);
			lblAufwand.setText("Aufwand:");
			cobAufwand = new Combo(top, SWT.READ_ONLY);
			for (int i = 0; i < Profession.Aufwand.values().length; i++) {
				cobAufwand.add(Profession.Aufwand.values()[i].getValue());
			}
			cobAufwand.select(0);
			
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
		public boolean isDirty(Profession charElem) {
			boolean isNotDirty = true;

			isNotDirty &= cobArt.getText().equals(charElem.getArt().getValue());
			isNotDirty &= cobAufwand.getText().equals(charElem.getAufwand().getValue());
			
			return !isNotDirty;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void loadData(Profession charElem) {
			cobArt.setText(charElem.getArt().getValue());
			cobAufwand.setText(charElem.getAufwand().getValue());
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void saveData(IProgressMonitor monitor, Profession charElem) {
			monitor.subTask("Save Profession-Data"); //$NON-NLS-1$
			
			for (int i = 0; i < Profession.ProfArt.values().length; i++) {
				if ( cobArt.getText().equals(Profession.ProfArt.values()[i].getValue()) ) {
					charElem.setArt(Profession.ProfArt.values()[i]);
					break;
				}
			}
			
			for (int i = 0; i < Profession.Aufwand.values().length; i++) {
				if ( cobAufwand.getText().equals(Profession.Aufwand.values()[i].getValue()) ) {
					charElem.setAufwand(Profession.Aufwand.values()[i]);
				}
			}
			
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
				new ProfessionSprachenAuswahlRegulator() );
		schriftenPart.loadData((Herkunft) getEditedCharElement());
		
		ausruestungPart = new AuswahlPart(
				this.getContainer(), 
				this.getSite(),
				new ProfessionAusruestungRegulator());
		ausruestungPart.loadData((Herkunft) getEditedCharElement());
		
		professionPart = new ProfessionPart(mainContainer);
		professionPart.loadData((Profession) getEditedCharElement());

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
				professionPart,
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
		return (Profession) this.getEditorInput().getAdapter(Profession.class);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getElementParts()
	 */
	@Override
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}

	static class ProfessionAusruestungRegulator implements HerkunftAuswahlRegulator {
		private static final Class[] classes =  new Class[] {Gegenstand.class, Gegenstand.class};
		private static final String[] text = new String[] { "Gegenstände", "Besonderer Besitz"};
		
		private static final int GEGENSTAND = 0;
		private static final int BESOND_B = 1;

		@Override
		public String[] getCharElementText() {
			return text;
		}

		@Override
		public Auswahl getAuswahl(Herkunft herkunft, int index) {
			switch(index) {
				case GEGENSTAND: return ((Profession) herkunft).getAusruestung();
				case BESOND_B: return ((Profession) herkunft).getBesondererBesitz();
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
				case GEGENSTAND: ((Profession) herkunft).setAusruestung(auswahl); break;
				case BESOND_B: ((Profession) herkunft).setBesondererBesitz(auswahl); break;
				default:
					throw new IllegalArgumentException("Keine behandlung für den Index" +
							index + " vorhanden!");
			}
		}
	}
	
	static class ProfessionSprachenAuswahlRegulator implements HerkunftAuswahlRegulator {
		private static final Class[] classes =  new Class[] {
			Sprache.class, Schrift.class};
		
		private static final String[] text = new String[] { 
				"Sprachen",
				"Schriften"
			};
		private static final int SPRACHEN = 0;
		private static final int SCHRIFTEN = 1;
		
		@Override
		public String[] getCharElementText() {
			return text;
		}

		@Override
		public Auswahl getAuswahl(Herkunft herkunft, int index) {
			switch(index) {
				case SPRACHEN: return ((Profession) herkunft).getSprachen();
				case SCHRIFTEN: return ((Profession) herkunft).getSchriften();
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
				case SPRACHEN: ((Profession) herkunft).setSprachen(auswahl); break;
				case SCHRIFTEN: ((Profession) herkunft).setSchriften(auswahl); break;
				default:
					throw new IllegalArgumentException("Keine behandlung für den Index" +
							index + " vorhanden!");
			}
		}
	}
}
