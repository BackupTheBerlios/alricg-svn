/*
 * Created 13.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.dialoge;

import org.d3s.alricg.editor.editors.EditorMessages;
import org.d3s.alricg.store.charElemente.links.Option;
import org.d3s.alricg.store.charElemente.links.OptionAnzahl;
import org.d3s.alricg.store.charElemente.links.OptionListe;
import org.d3s.alricg.store.charElemente.links.OptionVerteilung;
import org.d3s.alricg.store.charElemente.links.OptionVoraussetzung;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

/**
 * Dieser Dialog wird angezeigt wenn eine Auswahl erstellt oder bearbeitet werden soll.
 * @author Vincent
 */
public class CreateAuswahlDialog extends Dialog {
	private final boolean isVoraussetzung;
	private Combo cobModus;
	private Spinner spiAnzahl;
	private Spinner spiWert;
	private Spinner spiMax;
	private Text txtWertList;
	private static final int ANZAHL_IDX = 0;
	private static final int VERTEILUNG_IDX = 1;
	private static final int LISTE_IDX = 2;
	private Option option;
	private Option initOption; 
	
	public CreateAuswahlDialog(IShellProvider parentShell, boolean isVoraussetzung, Option initValue) {
		super(parentShell);
		this.isVoraussetzung = isVoraussetzung;
		this.initOption = initValue;
	}

	public CreateAuswahlDialog(Shell parentShell, boolean isVoraussetzung, Option initValue) {
		super(parentShell);
		this.isVoraussetzung = isVoraussetzung;
		this.initOption = initValue;
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Auswahl erzeugen");
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(gridLayout);
		
		final Label lblModul = new Label(container, SWT.NONE);
		lblModul.setText(EditorMessages.CreateAuswahlDialog_Modus);
		cobModus = new Combo(container, SWT.READ_ONLY | SWT.CENTER);
		if (isVoraussetzung) {
			cobModus.add(EditorMessages.CreateAuswahlDialog_ModusVoraussetzung);
			cobModus.setEnabled(false);
		} else {
			cobModus.add(EditorMessages.CreateAuswahlDialog_ModusAnzahl);
			cobModus.add(EditorMessages.CreateAuswahlDialog_ModusVerteilung);
			cobModus.add(EditorMessages.CreateAuswahlDialog_MoudsListe);
			cobModus.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					switch (cobModus.getSelectionIndex()) {
					case ANZAHL_IDX: 
						spiAnzahl.setEnabled(true);
						spiWert.setEnabled(false);
						spiMax.setEnabled(false);
						txtWertList.setEnabled(false);
						break;
					case VERTEILUNG_IDX:
						spiAnzahl.setEnabled(true);
						spiWert.setEnabled(true);
						spiMax.setEnabled(true);
						txtWertList.setEnabled(false);
						break;
					case LISTE_IDX:
						spiAnzahl.setEnabled(false);
						spiWert.setEnabled(false);
						spiMax.setEnabled(false);
						txtWertList.setEnabled(true);
						break;
					}
				}
			});
		}
		
		final Label lblAnzahl = new Label(container, SWT.NONE);
		lblAnzahl.setText(EditorMessages.CreateAuswahlDialog_Anzahl);
		spiAnzahl = new Spinner (container, SWT.BORDER);
		spiAnzahl.setMinimum(0);
		spiAnzahl.setMaximum(100);
		spiAnzahl.setSelection(0);
		spiAnzahl.setIncrement(1);
		spiAnzahl.setPageIncrement(10);
		
		final Label lblWert = new Label(container, SWT.NONE);
		lblWert.setText(EditorMessages.CreateAuswahlDialog_Wert);
		spiWert = new Spinner (container, SWT.BORDER);
		spiWert.setMinimum(0);
		spiWert.setMaximum(100);
		spiWert.setSelection(0);
		spiWert.setIncrement(1);
		spiWert.setPageIncrement(10);
		
		final Label lblMax = new Label(container, SWT.NONE);
		lblMax.setText(EditorMessages.CreateAuswahlDialog_Maximal);
		spiMax = new Spinner (container, SWT.BORDER);
		spiMax.setMinimum(0);
		spiMax.setMaximum(100);
		spiMax.setSelection(0);
		spiMax.setIncrement(1);
		spiMax.setPageIncrement(10);
		
		final Label lblWertList = new Label(container, SWT.NONE);
		lblWertList.setText(EditorMessages.CreateAuswahlDialog_WertListe);
		txtWertList = new Text(container, SWT.BORDER);
		txtWertList.addListener (SWT.Verify, new Listener () {
			public void handleEvent (Event e) {
				e.doit = true;
				if (e.keyCode == SWT.DEL
						|| e.keyCode == SWT.TAB
						|| e.keyCode == SWT.BS) {
					return;
				}
				
				if (!e.text.matches("[[0-9]*[,]*]*")) { //$NON-NLS-1$
					e.doit = false;
				}
				return;
			}
		});

		if (initOption != null) {
			if (initOption instanceof OptionVoraussetzung) {
				cobModus.select(0);
				spiAnzahl.setSelection(initOption.getAnzahl());
				spiWert.setSelection(initOption.getWert());
				
			} else if(initOption instanceof OptionAnzahl) {
				cobModus.select(ANZAHL_IDX);
				spiAnzahl.setSelection(initOption.getAnzahl());
				
			} else if(initOption instanceof OptionListe) {
				cobModus.select(LISTE_IDX);
				
				StringBuilder strBuilder = new StringBuilder();
				for (int i = 0; i < initOption.getWerteListe().length; i++) {
					strBuilder.append(initOption.getWerteListe()[i]);
					if (i < (initOption.getWerteListe().length - 1)) {
						strBuilder.append(","); //$NON-NLS-1$
					}
				}
				txtWertList.setText(strBuilder.toString());
				
			} else if(initOption instanceof OptionVerteilung) {
				cobModus.select(VERTEILUNG_IDX);
				spiAnzahl.setSelection(initOption.getAnzahl());
				spiWert.setSelection(initOption.getWert());
				spiMax.setSelection(initOption.getMax());
			}
			
		} else {
			cobModus.select(0);
		}
		
		if(isVoraussetzung) {
			spiAnzahl.setEnabled(true);
			spiWert.setEnabled(true);
			spiMax.setEnabled(false);
			txtWertList.setEnabled(false);
		}
		
		return container;
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		if (isVoraussetzung) {
			option = new OptionVoraussetzung();
		} else if(cobModus.getSelectionIndex() == this.ANZAHL_IDX) {
			option = new OptionAnzahl();
		} else if(cobModus.getSelectionIndex() == this.VERTEILUNG_IDX) {
			option = new OptionVerteilung();
		} else if(cobModus.getSelectionIndex() == this.LISTE_IDX) {
			option = new OptionListe();
		}
		
		if (spiAnzahl.isEnabled()) {
			option.setAnzahl(spiAnzahl.getSelection());
		}
		if (spiWert.isEnabled()) {
			option.setWert(spiWert.getSelection());
		}
		if (spiMax.isEnabled()) {
			option.setMax(spiMax.getSelection());
		}
		if (txtWertList.isEnabled()) {
			final String[] strList = txtWertList.getText().split(","); //$NON-NLS-1$
			final int[] intList = new int[strList.length];
			for (int i = 0; i < strList.length; i++) {
				intList[i] = Integer.parseInt(strList[i]);
			}
			
			option.setWerteListe(intList);
		}
		
		super.okPressed();
	}

	public Option getOption() {
		return option;
	}
	
	
}

