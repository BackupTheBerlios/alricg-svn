/*
 * Created 05.09.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common.widgets;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Vincent
 */
public class ComboTextList extends Composite {
	private final int NUMBER_OF_LINES = 3;
	
	private Combo cobText;
	private List listSpez;
	private Button butAdd;
	private Button butDelete;
	
	private final Image imgAdd = ControlIconsLibrary.add.getImageDescriptor().createImage();
	private final Image imgDelete = ControlIconsLibrary.delete.getImageDescriptor().createImage();
	
	/**
	 * Konstruktor
	 * @param parent SWT Parent
	 * @param style SWT Style
	 * @param regulator Regulator zum Steuern dieser DropList
	 */
	public ComboTextList(Composite parent, int style, String[] comboItems) {
		super(parent, style);
		createTextList(comboItems);
	}
	
	private void createTextList(String[] comboItems) {
		GridData containerGridData = new GridData(GridData.GRAB_HORIZONTAL);
		containerGridData.widthHint = 300;

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 0;
		
		this.setLayout(gridLayout);
		this.setLayoutData(containerGridData);
		
		// Spalte 1 der containerGridData
		cobText = new Combo(this, SWT.READ_ONLY);
		cobText.setItems(comboItems);
		GridData tmpGData = new GridData(GridData.FILL_HORIZONTAL);
		cobText.setLayoutData(tmpGData);
		cobText.addListener (SWT.DefaultSelection, new Listener () {
			public void handleEvent (Event e) {
				addToList();
			}
		});

		final Composite compButtons = new Composite(this, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 1;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		tmpGData = new GridData();
		tmpGData.verticalSpan = 2;
		tmpGData.verticalAlignment = GridData.BEGINNING;
		compButtons.setLayout(gridLayout);
		compButtons.setLayoutData(tmpGData);
		
		listSpez = new List (this, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		tmpGData = new GridData(GridData.FILL_BOTH); 
		tmpGData.heightHint = listSpez.getItemHeight() * NUMBER_OF_LINES;
		listSpez.setLayoutData(tmpGData);
		
		// Buttons
		butAdd = new Button(compButtons, SWT.NONE);
		butAdd.setImage(imgAdd);
		tmpGData = new GridData(imgAdd.getImageData().width+4, imgAdd.getImageData().height+4);
		tmpGData.verticalAlignment = GridData.BEGINNING;
		butAdd.setLayoutData(tmpGData);
		butAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addToList();
			}
		});
		
		butDelete = new Button(compButtons, SWT.NONE);
		butDelete.setLayoutData(new GridData(imgDelete.getImageData().width+4, imgDelete.getImageData().height+4));
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
		String text = cobText.getText().trim();
		if (text.length() == 0) return;
		
		// Prüfe ob doppelt
		String[] strs = listSpez.getItems();
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].equalsIgnoreCase(text)) return;
		}
		
		// Wert hinzufügen
		listSpez.add(text);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		this.imgAdd.dispose();
		this.imgDelete.dispose();
		
		super.dispose();
	}
	
	public String[] getValueList() {
		return listSpez.getItems();
	}
	
	public void addValue(String string) {
		listSpez.add(string);
	}
	
	public void setValues(String[] strings) {
		if (strings == null) {
			listSpez.setItems(new String[0]);
		} else {
			listSpez.setItems(strings);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		cobText.setEnabled(enabled);
		listSpez.setEnabled(enabled);
		butAdd.setEnabled(enabled);
		butDelete.setEnabled(enabled);
		
		super.setEnabled(enabled);
	}
	
	
}
