/*
 * Created 09.11.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.views.dialogs;

import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.links.Link;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog zum eingeben/ändern von Talent Spezialisierungen
 * @author Vincent
 */
public class TalentSpeziDialog extends Dialog {
	private Combo cobSpez1, cobSpez2, cobSpez3, cobSpez4;
	private Link<Talent> link;
	private String result;
	
	public TalentSpeziDialog(Shell parentShell, Link<Talent> link) {
		super(parentShell);
		this.link = link;
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Spezialisierungen");
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		
		String[] speziListe = link.getZiel().getSpezialisierungen();
		if (speziListe == null) speziListe = new String[] {""};
		
		String[] values;
		if (link.getText() != null) {
			values = link.getText().split(";");
		} else {
			values = new String[] {""};
		}
		
		
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(gridLayout);
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		final Label lblSpez1 = new Label(container, SWT.NONE);
		lblSpez1.setText("1. ");
		cobSpez1 = new Combo(container, SWT.NONE);
		cobSpez1.setItems(speziListe);
		cobSpez1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (values.length > 0) cobSpez1.setText(values[0].trim());
		
		final Label lblSpez2 = new Label(container, SWT.NONE);
		lblSpez2.setText("2. ");
		cobSpez2 = new Combo(container, SWT.NONE);
		cobSpez2.setItems(speziListe);
		cobSpez2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (values.length > 1) cobSpez2.setText(values[1].trim());
		
		final Label lblSpez3 = new Label(container, SWT.NONE);
		lblSpez3.setText("3. ");
		cobSpez3 = new Combo(container, SWT.NONE);
		cobSpez3.setItems(speziListe);
		cobSpez3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (values.length > 2) cobSpez3.setText(values[2].trim());
		
		final Label lblSpez4 = new Label(container, SWT.NONE);
		lblSpez4.setText("4. ");
		cobSpez4 = new Combo(container, SWT.NONE);
		cobSpez4.setItems(speziListe);
		cobSpez4.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (values.length > 3) cobSpez4.setText(values[3].trim());
		
		return container;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		final StringBuilder sb = new StringBuilder();
		
		if (cobSpez1.getText().trim().length() > 0) {
			sb.append( cobSpez1.getText().replaceAll(";", ",").trim() );
		}
		if (cobSpez2.getText().trim().length() > 0) {
			if (sb.length()>0) sb.append("; ");
			sb.append( cobSpez2.getText().replaceAll(";", ",").trim() );
		}
		if (cobSpez3.getText().trim().length() > 0) {
			if (sb.length()>0) sb.append("; ");
			sb.append( cobSpez3.getText().replaceAll(";", ",").trim() );
		}
		if (cobSpez4.getText().trim().length() > 0) {
			if (sb.length()>0) sb.append("; ");
			sb.append( cobSpez4.getText().replaceAll(";", ",").trim() );
		}
		
		result = sb.toString();
		
		super.okPressed();
	}

	public String getResult() {
		return result;
	}
}
