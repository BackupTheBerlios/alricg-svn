/*
 * Created 23.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common.widgets;

import java.util.ArrayList;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.eclipse.jface.util.LocalSelectionTransfer;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

/**
 * Erstellt eine List mit der Möglichkeit:
 * 	- 
 * @author Vincent
 */
public class DropList extends Composite {
	private final DropListRegulator regulator;
	private final int NUMBER_OF_LINES = 5;
	
	private ArrayList modelList;
	private List listElemente;
	private Image imgDelete = ControlIconsLibrary.delete.getImageDescriptor().createImage();
	private Image imgUp = ControlIconsLibrary.arrowUp.getImageDescriptor().createImage();
	private Image imgDown = ControlIconsLibrary.arrowDown.getImageDescriptor().createImage();

	/**
	 * Interface um die DropList zu steuern
	 * @author Vincent
	 */
	public static interface DropListRegulator {
		/**
		 * @param obj Object von der "Drop" Opteration
		 * @return Vaule wie er in der Value-Liste gespeichert werden soll
		 */
		public Object getValue(Object obj);
		
		/**
		 * @param obj Object von der "Drop" Opteration
		 * @return String wie er in der Liste angezeigt werden soll
		 */
		public String getString(Object obj);
		
		/**
		 * @param obj Object von der "Drop" Opteration
		 * @return true - "obj" kann zu der Liste hinzugefügt werden, ansonsten false
		 */
		public boolean canDrop(Object obj);
	}
	
	/**
	 * Konstruktor
	 * @param parent SWT Parent
	 * @param style SWT Style
	 * @param regulator Regulator zum Steuern dieser DropList
	 */
	public DropList(Composite parent, int style, DropListRegulator regulator) {
		super(parent, style);
		this.regulator = regulator;
		modelList = new ArrayList();
		
		createList(parent);
	}
	
	private void createList(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 0;
		
		GridData tmpGData = new GridData(GridData.GRAB_HORIZONTAL);
		tmpGData.widthHint = 250;
		this.setLayout(gridLayout);
		this.setLayoutData(tmpGData);
		
		listElemente = new List (this, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		tmpGData = new GridData(GridData.FILL_BOTH); 
		tmpGData.heightHint = listElemente.getItemHeight() * NUMBER_OF_LINES;
		tmpGData.verticalAlignment = GridData.BEGINNING;
		listElemente.setLayoutData(tmpGData);
		listElemente.setToolTipText("Fügen sie Elemente per \"Drag and Drop\" hinzu.");
		
		final Composite compButtons = new Composite(this, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 1;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		compButtons.setLayout(gridLayout);
		
		final Button butDelete = new Button(compButtons, SWT.NONE);
		tmpGData = new GridData(imgDelete.getImageData().width+4, imgDelete.getImageData().height+4); 
		tmpGData.verticalAlignment = GridData.BEGINNING;
		butDelete.setLayoutData(tmpGData);
		butDelete.setImage(imgDelete);
		butDelete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Lösche alle Markierten
				for (int i = 0; i < listElemente.getSelectionIndices().length; i++) {
					modelList.remove(listElemente.getSelectionIndices()[i]);
				}
				
				listElemente.remove(listElemente.getSelectionIndices());
			}
		});
		
		final Button butUp = new Button(compButtons, SWT.NONE);
		butUp.setImage(imgUp);
		tmpGData = new GridData(imgUp.getImageData().width+4, imgUp.getImageData().height+4); 
		tmpGData.verticalAlignment = GridData.BEGINNING;
		tmpGData.verticalIndent = 5;
		butUp.setLayoutData(tmpGData);
		butUp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				// Prüfung
				if (listElemente.getSelectionIndices().length == 0	
						|| listElemente.getSelectionIndices()[0] == 0) {
					return;
				}
				
				// Tauschen
				for (int i = 0; i < listElemente.getSelectionIndices().length; i++) {
					int selectedIdx = listElemente.getSelectionIndices()[i];
					final String selectedString = listElemente.getItem(selectedIdx);
					final Object selectedObject = modelList.get(selectedIdx);
					
					// View
					listElemente.setItem(
							selectedIdx, 
							listElemente.getItem(selectedIdx-1));
					listElemente.setItem(
							selectedIdx-1, 
							selectedString);
					
					// Model
					modelList.set(selectedIdx, modelList.get(selectedIdx-1));
					modelList.set(selectedIdx-1, selectedObject);
				}
				
				// Selektion neu setzen
				int[] newSelIdx = new int[listElemente.getSelectionIndices().length];
				for (int i = 0; i < listElemente.getSelectionIndices().length; i++) {
					newSelIdx[i] = listElemente.getSelectionIndices()[i] - 1;
				}
				listElemente.setSelection(newSelIdx);
			}
		});
		
		final Button butDown = new Button(compButtons, SWT.NONE);
		butDown.setImage(imgDown);
		butDown.setLayoutData(new GridData(imgDown.getImageData().width+4, imgDown.getImageData().height+4));
		butDown.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				// Prüfung
				if (listElemente.getSelectionIndices().length == 0	
						|| listElemente.getSelectionIndices()[listElemente.getSelectionIndices().length-1] 
						                                  == listElemente.getItemCount()-1) {
					return;
				}
				
				// Tauschen
				for (int i = listElemente.getSelectionIndices().length-1; i >= 0; i--) {
					int selectedIdx = listElemente.getSelectionIndices()[i];
					final String selectedString = listElemente.getItem(selectedIdx);
					final Object selectedObject = modelList.get(selectedIdx);
					
					// View
					listElemente.setItem(
							selectedIdx, 
							listElemente.getItem(selectedIdx+1));
					listElemente.setItem(
							selectedIdx+1, 
							selectedString);
					
					// Model
					modelList.set(selectedIdx, modelList.get(selectedIdx+1));
					modelList.set(selectedIdx+1, selectedObject);
				}
				
				// Selektion neu setzen
				int[] newSelIdx = new int[listElemente.getSelectionIndices().length];
				for (int i = 0; i < listElemente.getSelectionIndices().length; i++) {
					newSelIdx[i] = listElemente.getSelectionIndices()[i] + 1;
				}
				listElemente.setSelection(newSelIdx);
			}
		});
		
		
		final DropTarget target = new DropTarget(listElemente, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
	    target.setTransfer(new Transfer[] {LocalSelectionTransfer.getTransfer()} );
	    target.addDropListener(new DropTargetAdapter() {
			public void drop(DropTargetEvent event) {
				if (event.data == null
						|| !regulator.canDrop(event.data)) {
					event.detail = DND.DROP_NONE;
					return;
				} 
				if (modelList.contains(regulator.getValue(event.data))) {
					event.detail = DND.DROP_NONE;
					return;
				}
				listElemente.add(regulator.getString(event.data));
				modelList.add(regulator.getValue(event.data));
			}
	    });
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		imgDelete.dispose();
		imgDown.dispose();
		imgUp.dispose();
		super.dispose();
	}
	
	/**
	 * @return Eine Liste von Werte welche in der List gespeichert sind.
	 * 	Die Liste darf bearbeitet werden, da sie Kopiert wird.
	 */
	public java.util.List getValueList() {
		ArrayList list = new ArrayList();
		list.addAll(modelList);
		return list;
	}
	
	/**
	 * Fügt einen Wert an das Ende der Liste hinzu
	 * @param text Der Text für die List
	 * @param value Der Wert für die ValueList
	 */
	public void addValue(String text, Object value) {
		listElemente.add(text);
		modelList.add(value);
	}
	
}
