/*
 * Created 03.09.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import org.d3s.alricg.common.CharElementTextService;
import org.d3s.alricg.editor.editors.composits.AuswahlPart.HerkunftAuswahlRegulator;
import org.d3s.alricg.editor.editors.composits.IdLinkArrayPart.HerkunftIdLinkRegulator;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.charElemente.Herkunft.MagieEigenschaften;
import org.d3s.alricg.store.charElemente.Werte.Geschlecht;
import org.d3s.alricg.store.charElemente.links.Auswahl;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * @author Vincent
 */
public class HerkunftPart extends AbstractElementPart<Herkunft> {
	private AuswahlPart modisAuswahl;
	private IdLinkArrayPart aktivierbareZauberPart;
	private IdLinkArrayPart empfehlungenPart;
	private IdLinkArrayPart verbilligtPart;
	private AlternativeZauberAuswahlPart altervativeZauberPart;
	
	private final Spinner spiSoMin;
	private final Spinner spiSoMax;
	private final Spinner spiGpKosten;
	
	private final Combo cobGeschlecht;
	
	private final String EGAL = "Egal";
	private final String FRAU = "Nur Weiblich";
	private final String MANN = "Nur Männlich";
	
	public HerkunftPart(Composite top) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		
		GridData tmpGData = new GridData(GridData.GRAB_HORIZONTAL);
		tmpGData.widthHint = 600;
		tmpGData.horizontalSpan = 2; // nimm 2 Spalten Platz ein
		
	// Sozial-Status
		final Group groupSozial = new Group(top, SWT.SHADOW_IN);
		groupSozial.setText("Sozialstatus");
		groupSozial.setLayout(gridLayout);
		groupSozial.setLayoutData(tmpGData);
		
		final Label lblSoMin = new Label(groupSozial, SWT.NONE);
		lblSoMin.setText("Minimal:");
		lblSoMin.setToolTipText("Minimal möglicher Sozialstatus");
		spiSoMin = new Spinner(groupSozial, SWT.BORDER);
		spiSoMin.setValues(
				Herkunft.SO_MIN_DEFAULT, 
				Herkunft.SO_MIN_DEFAULT, 
				Herkunft.SO_MAX_DEFAULT, 
				0, 1, 5);
		
		final Label lblSoMax = new Label(groupSozial, SWT.NONE);
		lblSoMax.setText("Maximal:");
		lblSoMax.setToolTipText("Maximal möglicher Sozialstatus");
		spiSoMax = new Spinner(groupSozial, SWT.BORDER);
		spiSoMax.setValues(
				Herkunft.SO_MAX_DEFAULT, 
				Herkunft.SO_MIN_DEFAULT, 
				Herkunft.SO_MAX_DEFAULT, 
				0, 1, 5);
		
		// GP
		final Label lblGp = new Label(top, SWT.NONE);
		lblGp.setText("GP-Kosten:");
		lblGp.setToolTipText("Generierungspunkte Kosten");
		spiGpKosten = new Spinner(top, SWT.BORDER);
		spiGpKosten.setValues(
				1, 
				0, 
				100, 
				0, 1, 5);
		
		// Geschlecht
		final Label lblGeschlecht = new Label(top, SWT.NONE);
		lblGeschlecht.setText("Geschlecht:");
		lblGeschlecht.setToolTipText("Gibt an, ob dieses Element nur " +
							"für ein bestimmtes Geschlecht gewählt werden kann.");
		cobGeschlecht = new Combo(top, SWT.READ_ONLY);
		cobGeschlecht.add(EGAL);
		cobGeschlecht.add(MANN);
		cobGeschlecht.add(FRAU);
		cobGeschlecht.select(0);
		
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
	public boolean isDirty(Herkunft charElem) {
		boolean isNotDirty = true;
		
		isNotDirty &= spiSoMin.getSelection() == charElem.getSoMin();
		isNotDirty &= spiSoMax.getSelection() == charElem.getSoMax();
		isNotDirty &= spiGpKosten.getSelection() == charElem.getGpKosten();
		
		if (charElem.getGeschlecht() == Geschlecht.mannOderFrau) {
			isNotDirty &= cobGeschlecht.getText().equals(EGAL);
		} else if (charElem.getGeschlecht() == Geschlecht.mann) {
			isNotDirty &= cobGeschlecht.getText().equals(MANN);
		} else if (charElem.getGeschlecht() == Geschlecht.frau) {
			isNotDirty &= cobGeschlecht.getText().equals(FRAU);
		}
		
		return !isNotDirty;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void loadData(Herkunft charElem) {
		spiSoMin.setSelection(charElem.getSoMin());
		spiSoMax.setSelection(charElem.getSoMax());
		spiGpKosten.setSelection(charElem.getGpKosten());
		
		if (charElem.getGeschlecht() == Geschlecht.mannOderFrau) {
			cobGeschlecht.setText(EGAL);
		} else if (charElem.getGeschlecht() == Geschlecht.mann) {
			cobGeschlecht.setText(MANN);
		} else if (charElem.getGeschlecht() == Geschlecht.frau) {
			cobGeschlecht.setText(FRAU);
		}
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void saveData(IProgressMonitor monitor, Herkunft charElem) {
		monitor.subTask("Save Herkunft-Data"); //$NON-NLS-1$
		
		charElem.setSoMin(spiSoMin.getSelection());
		charElem.setSoMax(spiSoMax.getSelection());
		charElem.setGpKosten(spiGpKosten.getSelection());
		
		if (cobGeschlecht.getText().equals(EGAL)) {
			charElem.setGeschlecht(Geschlecht.mannOderFrau);
		} else if (cobGeschlecht.getText().equals(MANN)) {
			charElem.setGeschlecht(Geschlecht.mann);
		} else if (cobGeschlecht.getText().equals(FRAU)) {
			charElem.setGeschlecht(Geschlecht.frau);
		}
		
		monitor.worked(1);
	}

	public void loadHerkunftTrees(Composite container, IWorkbenchPartSite partSite, Herkunft herkunft) {
		
		modisAuswahl = new AuswahlPart(
							container, 
							partSite,
							new HerkunftModisAuswahlRegulator());
		modisAuswahl.loadData(herkunft);
		
		verbilligtPart = new IdLinkArrayPart(
				container, 
				partSite,
				new HerkunftIdLinkVerbilligtRegulator());
		verbilligtPart.loadData(herkunft);
		
		empfehlungenPart = new IdLinkArrayPart(
				container, 
				partSite,
				new HerkunftIdLinkEmpfehlungenRegulator());
		empfehlungenPart.loadData(herkunft);
		
		aktivierbareZauberPart = new IdLinkArrayPart(
				container, 
				partSite,
				new HerkunftIdLinkAktivierbareZauberRegulator());
		aktivierbareZauberPart.loadData(herkunft);

		altervativeZauberPart = new AlternativeZauberAuswahlPart(container, partSite);
	}
	
	/**
	 * @return the modisAuswahl
	 */
	public AuswahlPart getModisAuswahl() {
		return modisAuswahl;
	}

	/**
	 * @return the aktivierbareZauberPart
	 */
	public IdLinkArrayPart getAktivierbareZauberPart() {
		return aktivierbareZauberPart;
	}

	/**
	 * @return the altervativeZauberPart
	 */
	public AlternativeZauberAuswahlPart getAltervativeZauberPart() {
		return altervativeZauberPart;
	}
	
	/**
	 * @return the empfehlungenPart
	 */
	public IdLinkArrayPart getEmpfehlungenPart() {
		return empfehlungenPart;
	}

	/**
	 * @return the verbilligtPart
	 */
	public IdLinkArrayPart getVerbilligtPart() {
		return verbilligtPart;
	}
	

	static class HerkunftIdLinkEmpfehlungenRegulator implements HerkunftIdLinkRegulator {
		private static final Class[] classes =  
				new Class[] {Vorteil.class, Nachteil.class, Vorteil.class, Nachteil.class  };
		private static final String[] text = 
				new String[] {"Empfohlene Vorteile", "Empfohlene Nachteile", 
								"Ungeeignete Vorteile", "Ungeeignete Nachteile"};
		
		private static final int EMP_VORT = 0;
		private static final int EMP_NACHT = 1;
		private static final int UNG_VORT = 2;
		private static final int UNG_NACHT = 3;
		
		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.IdLinkArrayPart.HerkunftIdLinkRegulator#getCharElementClazz()
		 */
		@Override
		public Class[] getCharElementClazz() {
			return classes;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.IdLinkArrayPart.HerkunftIdLinkRegulator#getCharElementText()
		 */
		@Override
		public String[] getCharElementText() {
			return text;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.IdLinkArrayPart.HerkunftIdLinkRegulator#getLinkArray(org.d3s.alricg.store.charElemente.Herkunft, java.lang.Class, int)
		 */
		@Override
		public IdLink[] getLinkArray(Herkunft herkunft, int index) {
			switch(index) {
				case EMP_VORT: return herkunft.getEmpfVorteile();
				case EMP_NACHT: return herkunft.getEmpfNachteile();
				case UNG_VORT: return herkunft.getUngeVorteile();
				case UNG_NACHT: return herkunft.getUngeNachteile();
				default:
					throw new IllegalArgumentException("Keine behandlung für den Index " +
							index + " vorhanden!");
			}
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.IdLinkArrayPart.HerkunftIdLinkRegulator#setLinkArray(org.d3s.alricg.store.charElemente.Herkunft, org.d3s.alricg.store.charElemente.links.IdLink[], java.lang.Class, int)
		 */
		@Override
		public void setLinkArray(Herkunft herkunft, IdLink[] array, int index) {
			switch(index) {
				case EMP_VORT: herkunft.setEmpfVorteile(array); break;
				case EMP_NACHT: herkunft.setEmpfNachteile(array); break;
				case UNG_VORT: herkunft.setUngeVorteile(array); break;
				case UNG_NACHT: herkunft.setUngeNachteile(array); break;
				default:
					throw new IllegalArgumentException("Keine behandlung für den Index " +
							index + " vorhanden!");
			}
		}
	}
	
	static class HerkunftIdLinkVerbilligtRegulator implements HerkunftIdLinkRegulator {
		private static final Class[] classes =  
				new Class[] {Sonderfertigkeit.class, Liturgie.class };
		private static final String[] text = new String[] { 
					CharElementTextService.getCharElementName(Sonderfertigkeit.class), 
					CharElementTextService.getCharElementName(Liturgie.class) };
		private static final int SONDERF = 0;
		private static final int LITURGIE = 1;
		
		
		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.IdLinkArrayPart.HerkunftIdLinkRegulator#getCharElementClazz()
		 */
		@Override
		public Class[] getCharElementClazz() {
			return classes;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.IdLinkArrayPart.HerkunftIdLinkRegulator#getCharElementText()
		 */
		@Override
		public String[] getCharElementText() {
			return text;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.IdLinkArrayPart.HerkunftIdLinkRegulator#getLinkArray(org.d3s.alricg.store.charElemente.Herkunft, java.lang.Class)
		 */
		@Override
		public IdLink[] getLinkArray(Herkunft herkunft, int idx) {
			switch(idx) {
				case SONDERF: return herkunft.getVerbilligteSonderf();
				case LITURGIE: return herkunft.getVerbilligteLiturgien();
				default:
					throw new IllegalArgumentException("Keine behandlung für den Index " +
							idx + " vorhanden!");
			}
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.IdLinkArrayPart.HerkunftIdLinkRegulator#setLinkArray(org.d3s.alricg.store.charElemente.Herkunft, org.d3s.alricg.store.charElemente.links.IdLink[], java.lang.Class)
		 */
		@Override
		public void setLinkArray(Herkunft herkunft, IdLink[] array, int idx) {
			switch(idx) {
				case SONDERF: herkunft.setVerbilligteSonderf(array); break;
				case LITURGIE: herkunft.setVerbilligteLiturgien(array); break;
				default:
					throw new IllegalArgumentException("Keine behandlung für den Index " +
							idx + " vorhanden!");
			}
		}
	}
	
	static class HerkunftIdLinkAktivierbareZauberRegulator implements HerkunftIdLinkRegulator {
		private static final Class[] classes =  
				new Class[] {Zauber.class, Zauber.class };
		private static final String[] text = new String[] { 
					"Zusätzliche Zauber", 
					"Fehlende Zauber" };
		private static final int ZUS_ZAUB = 0;
		private static final int FEH_ZAUB = 1;
		
		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.IdLinkArrayPart.HerkunftIdLinkRegulator#getCharElementClazz()
		 */
		@Override
		public Class[] getCharElementClazz() {
			return classes;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.IdLinkArrayPart.HerkunftIdLinkRegulator#getCharElementText()
		 */
		@Override
		public String[] getCharElementText() {
			return text;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.IdLinkArrayPart.HerkunftIdLinkRegulator#getLinkArray(org.d3s.alricg.store.charElemente.Herkunft, java.lang.Class)
		 */
		@Override
		public IdLink[] getLinkArray(Herkunft herkunft, int idx) {
			if (herkunft.getMagieEigenschaften() == null) return null;
			switch(idx) {
				case ZUS_ZAUB: 
					return herkunft.getMagieEigenschaften().getZusaetzlichAktivierbareZauber();
				case FEH_ZAUB: 
					return herkunft.getMagieEigenschaften().getFehlendeAktivierbareZauber();
				default:
					throw new IllegalArgumentException("Keine behandlung für den Index " +
							idx + " vorhanden!");
			}
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.IdLinkArrayPart.HerkunftIdLinkRegulator#setLinkArray(org.d3s.alricg.store.charElemente.Herkunft, org.d3s.alricg.store.charElemente.links.IdLink[], java.lang.Class)
		 */
		@Override
		public void setLinkArray(Herkunft herkunft, IdLink[] array, int idx) {
			
			if (herkunft.getMagieEigenschaften() == null) {
				if (array == null || array.length == 0) return;
				herkunft.setMagieEigenschaften(new MagieEigenschaften());
			}
			
			switch(idx) {
				case ZUS_ZAUB: 
					herkunft.getMagieEigenschaften().setZusaetzlichAktivierbareZauber(array);
					break;
				case FEH_ZAUB: 
					herkunft.getMagieEigenschaften().setFehlendeAktivierbareZauber(array);
					break;
				default:
					throw new IllegalArgumentException("Keine behandlung für den Index " +
							idx + " vorhanden!");
			}
		}
	}
	
	static class HerkunftModisAuswahlRegulator implements HerkunftAuswahlRegulator {
		private static final Class[] classes =  new Class[] {
			Eigenschaft.class, Vorteil.class, Nachteil.class, 
			Sonderfertigkeit.class, Talent.class, Liturgie.class, 
			Zauber.class, Zauber.class};
		private static final String[] text = new String[] { 
				CharElementTextService.getCharElementName(Eigenschaft.class), 
				CharElementTextService.getCharElementName(Vorteil.class),
				CharElementTextService.getCharElementName(Nachteil.class),
				CharElementTextService.getCharElementName(Sonderfertigkeit.class),
				CharElementTextService.getCharElementName(Talent.class),
				CharElementTextService.getCharElementName(Liturgie.class),
				CharElementTextService.getCharElementName(Zauber.class),
				"Hauszauber"
			};
		private static final int EIGENSCHAFT = 0;
		private static final int VORTEIL = 1;
		private static final int NACHTEIL = 2;
		private static final int SONDERF = 3;
		private static final int TALENT = 4;
		private static final int LITURGIE = 5;
		private static final int ZAUBER = 6;
		private static final int HAUSZAUBER = 7;
		
		@Override
		public String[] getCharElementText() {
			return text;
		}

		@Override
		public Auswahl getAuswahl(Herkunft herkunft, int index) {
			switch(index) {
				case EIGENSCHAFT: return herkunft.getEigenschaftModis();
				case VORTEIL: return herkunft.getVorteile();
				case NACHTEIL: return herkunft.getNachteile();
				case SONDERF: return herkunft.getSonderfertigkeiten();
				case TALENT: return herkunft.getTalente();
				case LITURGIE: return herkunft.getLiturgien();
				case ZAUBER: 
					if (herkunft.getMagieEigenschaften() == null) return null;
					return herkunft.getMagieEigenschaften().getZauber();
				case HAUSZAUBER:
					if (herkunft.getMagieEigenschaften() == null) return null;
					return herkunft.getMagieEigenschaften().getHauszauber();
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
				case EIGENSCHAFT: herkunft.setEigenschaftModis(auswahl); break;
				case VORTEIL: herkunft.setVorteile(auswahl); break;
				case NACHTEIL: herkunft.setNachteile(auswahl); break;
				case SONDERF: herkunft.setSonderfertigkeiten(auswahl); break;
				case TALENT: herkunft.setTalente(auswahl); break;
				case LITURGIE: herkunft.setLiturgien(auswahl); break;
				case ZAUBER:
					if (herkunft.getMagieEigenschaften() == null) {
						if (auswahl == null) return;
						herkunft.setMagieEigenschaften(new MagieEigenschaften());
					}
					herkunft.getMagieEigenschaften().setZauber(auswahl);
					break;
				case HAUSZAUBER:
					if (herkunft.getMagieEigenschaften() == null) {
						if (auswahl == null) return;
						herkunft.setMagieEigenschaften(new MagieEigenschaften());
					}
					herkunft.getMagieEigenschaften().setHauszauber(auswahl);
					break;
				default:
					throw new IllegalArgumentException("Keine behandlung für den Index" +
							index + " vorhanden!");
			}
		}
	}

}
