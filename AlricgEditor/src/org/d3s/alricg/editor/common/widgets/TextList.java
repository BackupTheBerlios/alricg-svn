/*
 * Created 23.08.2007
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * Eine Liste mit Text. Es können neue Einträge hinzugefügt werden und die Einträge verschoben werden.
 * @author Vincent
 */
public class TextList extends Composite {
	private final int NUMBER_OF_LINES = 5;
	
	private Text txtSpez;
	private List listSpez;
	private Button butAdd;
	private Button butDelete;
	private Button butDown;
	private Button butUp;
	
	private final Image imgAdd = ControlIconsLibrary.add.getImageDescriptor().createImage();
	private final Image imgDelete = ControlIconsLibrary.delete.getImageDescriptor().createImage();
	private final Image imgUp = ControlIconsLibrary.arrowUp.getImageDescriptor().createImage();
	private final Image imgDown = ControlIconsLibrary.arrowDown.getImageDescriptor().createImage();
	
	/**
	 * Konstruktor
	 * @param parent SWT Parent
	 * @param style SWT Style
	 * @param regulator Regulator zum Steuern dieser DropList
	 */
	public TextList(Composite parent, int style) {
		super(parent, style);
		createTextList(parent);
	}
	
	private void createTextList(Composite parent) {
		GridData containerGridData = new GridData(GridData.GRAB_HORIZONTAL);
		containerGridData.widthHint = 300;

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 0;
		
		this.setLayout(gridLayout);
		this.setLayoutData(containerGridData);
		
		// Spalte 1 der containerGridData
		txtSpez = new Text(this, SWT.BORDER);
		txtSpez.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtSpez.addListener (SWT.DefaultSelection, new Listener () {
			public void handleEvent (Event e) {
				addToList();
			}
		});

		final Composite compButtons = new Composite(this, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 1;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		GridData tmpGData = new GridData();
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

		butUp = new Button(compButtons, SWT.NONE);
		butUp.setImage(imgUp);
		tmpGData = new GridData(imgUp.getImageData().width+4, imgDelete.getImageData().height+4); 
		tmpGData.verticalIndent = 5;
		butUp.setLayoutData(tmpGData);
		butUp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				// Prüfung
				if (listSpez.getSelectionIndices().length == 0	
						|| listSpez.getSelectionIndices()[0] == 0) {
					return;
				}
				
				// Tauschen
				for (int i = 0; i < listSpez.getSelectionIndices().length; i++) {
					int selectedIdx = listSpez.getSelectionIndices()[i];
					final String selectedString = listSpez.getItem(selectedIdx);
					
					// View
					listSpez.setItem(
							selectedIdx, 
							listSpez.getItem(selectedIdx-1));
					listSpez.setItem(
							selectedIdx-1, 
							selectedString);
				}
				
				// Selektion neu setzen
				int[] newSelIdx = new int[listSpez.getSelectionIndices().length];
				for (int i = 0; i < listSpez.getSelectionIndices().length; i++) {
					newSelIdx[i] = listSpez.getSelectionIndices()[i] - 1;
				}
				listSpez.setSelection(newSelIdx);
			}
		});
		
		butDown = new Button(compButtons, SWT.NONE);
		butDown.setImage(imgDown);
		butDown.setLayoutData(new GridData(imgDown.getImageData().width+4, imgDelete.getImageData().height+4));
		butDown.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				// Prüfung
				if (listSpez.getSelectionIndices().length == 0	
						|| listSpez.getSelectionIndices()[listSpez.getSelectionIndices().length-1] 
						                                  == listSpez.getItemCount()-1) {
					return;
				}
				
				// Tauschen
				for (int i = listSpez.getSelectionIndices().length-1; i >= 0; i--) {
					int selectedIdx = listSpez.getSelectionIndices()[i];
					final String selectedString = listSpez.getItem(selectedIdx);
					
					// View
					listSpez.setItem(
							selectedIdx, 
							listSpez.getItem(selectedIdx+1));
					listSpez.setItem(
							selectedIdx+1, 
							selectedString);
				}
				
				// Selektion neu setzen
				int[] newSelIdx = new int[listSpez.getSelectionIndices().length];
				for (int i = 0; i < listSpez.getSelectionIndices().length; i++) {
					newSelIdx[i] = listSpez.getSelectionIndices()[i] + 1;
				}
				listSpez.setSelection(newSelIdx);
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
		txtSpez.setText(""); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		this.imgAdd.dispose();
		this.imgDelete.dispose();
		this.imgDown.dispose();
		this.imgUp.dispose();
		
		super.dispose();
	}
	
	public String[] getValueList() {
		return listSpez.getItems();
	}
	
	public void addValue(String string) {
		listSpez.add(string);
	}
	
	public void setValues(String[] strings) {
		listSpez.setItems(strings);
	}
	
}
