/*
 * Created 25.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Vincent
 *
 */
public class GroupBasisDaten {
	private Composite top = null;
	private Group groupBasisDaten = null;
	private Label lblID = null;
	private Label lblSonderregel = null;
	private Label lblName = null;
	private Text txtID = null;
	private Text txtSonderregelKlasse = null;
	private Text txtName = null;
	private Label lblSammelbegriff = null;
	private Label lblBeschreibung = null;
	private Label lblRegelanmerkung = null;
	private Button cbxAnzeigen = null;
	private Text txtSammelbegriff = null;
	private Text txtBeschreibung = null;
	private Text txtRegelAnm = null;
	
	public void test() {
		GridData gridData1 = new GridData();
		GridData gridData = new GridData();
		GridData gridData4 = new GridData();
		gridData4.horizontalIndent = 0;
		gridData4.heightHint = -1;
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		groupBasisDaten = new Group(top, SWT.SHADOW_IN);
		groupBasisDaten.setText("Basis Daten");
		groupBasisDaten.setLayoutData(gridData);
		groupBasisDaten.setLayout(gridLayout);
		lblID = new Label(groupBasisDaten, SWT.NONE);
		lblID.setText("ID:");
		txtID = new Text(groupBasisDaten, SWT.BORDER);
		txtID.setLayoutData(gridData1);
		
		int width = 1000;
		int height = 20;
		txtID.setSize (txtID.computeSize (width, height));
		
		lblSonderregel = new Label(groupBasisDaten, SWT.NONE);
		lblSonderregel.setText("SonderregelKlasse:");
		txtSonderregelKlasse = new Text(groupBasisDaten, SWT.BORDER);
		txtSonderregelKlasse.setLayoutData(gridData4);
		lblName = new Label(groupBasisDaten, SWT.NONE);
		lblName.setText("Name");
		txtName = new Text(groupBasisDaten, SWT.BORDER);
		lblSammelbegriff = new Label(groupBasisDaten, SWT.NONE);
		lblSammelbegriff.setText("Sammelbegriff:" +
				"");
		txtSammelbegriff = new Text(groupBasisDaten, SWT.BORDER);
		lblBeschreibung = new Label(groupBasisDaten, SWT.NONE);
		lblBeschreibung.setText("Beschreibung:" +
				"");
		txtBeschreibung = new Text(groupBasisDaten, SWT.BORDER | SWT.MULTI);
		lblRegelanmerkung = new Label(groupBasisDaten, SWT.NONE);
		lblRegelanmerkung.setText("Regelanmerkung:");
		txtRegelAnm = new Text(groupBasisDaten, SWT.BORDER | SWT.MULTI);
		Label filler3 = new Label(groupBasisDaten, SWT.NONE);
		cbxAnzeigen = new Button(groupBasisDaten, SWT.CHECK);
		cbxAnzeigen.setText("Anzeigen");
	}
}
