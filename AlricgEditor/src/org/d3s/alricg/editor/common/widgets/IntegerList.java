/*
 * Created 02.09.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common.widgets;

import java.util.Arrays;

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
import org.eclipse.swt.widgets.Spinner;

/**
 * 
 * @author Vincent
 */
public class IntegerList extends Composite {
	private final int NUMBER_OF_LINES = 5;
	
	private Spinner spiInt;
	private List listWerte;
	private Button butAdd;
	private Button butDelete;
	
	private final Image imgAdd = ControlIconsLibrary.add.getImageDescriptor().createImage();
	private final Image imgDelete = ControlIconsLibrary.delete.getImageDescriptor().createImage();
	
	public IntegerList(Composite parent, int style, int min, int max, int pageInc) {
		super(parent, style);
		createList(parent, min, max, pageInc);
	}
	
	private void createList(Composite parent, int min, int max, int pageInc) {
		GridData containerGridData = new GridData(GridData.GRAB_HORIZONTAL);
		containerGridData.widthHint = 300;

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 0;
		
		this.setLayout(gridLayout);
		this.setLayoutData(containerGridData);
		
		// Spalte 1 der containerGridData
		spiInt = new Spinner (this, SWT.BORDER);
		spiInt.setMinimum(min);
		spiInt.setMaximum(max);
		spiInt.setSelection(min);
		spiInt.setIncrement(1);
		spiInt.setPageIncrement(pageInc);
		spiInt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		spiInt.addListener (SWT.DefaultSelection, new Listener () {
			public void handleEvent (Event e) {
				addToList( spiInt.getSelection() );
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
		
		listWerte = new List (this, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		tmpGData = new GridData(GridData.FILL_BOTH); 
		tmpGData.heightHint = listWerte.getItemHeight() * NUMBER_OF_LINES;
		listWerte.setLayoutData(tmpGData);
		
		// Buttons
		butAdd = new Button(compButtons, SWT.NONE);
		butAdd.setImage(imgAdd);
		tmpGData = new GridData(imgAdd.getImageData().width+4, imgAdd.getImageData().height+4);
		tmpGData.verticalAlignment = GridData.BEGINNING;
		butAdd.setLayoutData(tmpGData);
		butAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addToList( spiInt.getSelection() );
			}
		});
		
		butDelete = new Button(compButtons, SWT.NONE);
		butDelete.setLayoutData(new GridData(imgDelete.getImageData().width+4, imgDelete.getImageData().height+4));
		butDelete.setImage(imgDelete);
		butDelete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Lösche alle Markierten
				listWerte.remove(listWerte.getSelectionIndices());
			}
		});
		
	}

	// Fügt den aktuellen Text im txtSpez nach Prüfung zur List hinzu 
	private void addToList(int wert) {
		// Prüfe ob leer
		final String newInt = Integer.toString(wert);
		int index = Arrays.binarySearch(listWerte.getItems(), newInt);
		
		if (index >= 0) {
			// Element bereits vorhanden
			return;
		}
		
		// korrekten index bestimmen und hinzufügen
		index = Math.abs(index +1);
		listWerte.add(newInt, index);
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
	
	/**
	 * @return Alle aktuellen Werte der Liste
	 */
	public int[] getValueList() {
		int[] retList = new int[listWerte.getItemCount()];
		
		for (int i = 0; i < retList.length; i++) {
			retList[i] = Integer.parseInt( listWerte.getItem(i) );
		}
		return retList;
	}
	
	/**
	 * Fügt einen Wert zur Liste hinzu (wird sortiert)
	 * @param wert Neuer Wert
	 */
	public void addValue(int wert) {
		addToList(wert);
	}
	
	/**
	 * Setzt alle Werte neu in der Liste. Die Werte werden sortiert
	 * @param werte Liste der neuen Werte
	 */
	public void setValues(int[] werte) {
		listWerte.removeAll();
		
		Arrays.sort(werte);
		for (int i = 0; i < werte.length; i++) {
			listWerte.add(Integer.toString(werte[i]));
		}
	}
	
}
