/*
 * Created 03.09.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.widgets;

import java.util.ArrayList;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.store.charElemente.Rasse.FarbenAngabe;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * Ermöglicht die Angabe von Farben und die Wahrscheinlichkeit das eine
 * Farbe gewählt wird
 * @author Vincent
 */
public class FarbenAuswahlTable extends Composite {
	private static int NUMBER_OF_LINES = 5;
	protected TableViewer tableViewer;
	private IWorkbenchPartSite site;
	private int gesamtGewaehlt = 0;
	
	private final Image imgAdd = ControlIconsLibrary.add.getImageDescriptor().createImage();
	private final Image imgDelete = ControlIconsLibrary.delete.getImageDescriptor().createImage();
	private final Image imgUp = ControlIconsLibrary.arrowUp.getImageDescriptor().createImage();
	private final Image imgDown = ControlIconsLibrary.arrowDown.getImageDescriptor().createImage();

	protected Action addElement;
	protected Action deleteElement;
	protected Action upElement;
	protected Action downElement;
	
	/**
	 * Konstruktor
	 * @param parent SWT Parent
	 * @param style SWT Style
	 * @param regulator Regulator zum Steuern dieser DropList
	 */
	public FarbenAuswahlTable(Composite parent, int style, IWorkbenchPartSite site) {
		super(parent, style);
		this.site = site;
		createWidget(parent);
		
		makeActions();
		hookContextMenu();
	}
	
	private void createWidget(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 0;
		
		GridData tmpGData = new GridData(GridData.GRAB_HORIZONTAL);
		tmpGData.widthHint = 250;
		this.setLayout(gridLayout);
		this.setLayoutData(tmpGData);
		
		final Table table = createTable(this);
		tmpGData = new GridData(GridData.FILL_BOTH); 
		tmpGData.heightHint = table.getItemHeight() * NUMBER_OF_LINES;
		tmpGData.verticalAlignment = GridData.BEGINNING;
		table.setLayoutData(tmpGData);
		table.setToolTipText("Fügen sie Elemente per \"Drag and Drop\" hinzu.");
		
		final Composite compButtons = new Composite(this, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 1;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		tmpGData = new GridData();
		tmpGData.verticalAlignment = GridData.BEGINNING;
		compButtons.setLayout(gridLayout);
		compButtons.setLayoutData(tmpGData);
		
		// Buttons
		final Button butAdd = new Button(compButtons, SWT.NONE);
		butAdd.setImage(imgAdd);
		tmpGData = new GridData(imgAdd.getImageData().width+4, imgAdd.getImageData().height+4);
		tmpGData.verticalAlignment = GridData.BEGINNING;
		butAdd.setLayoutData(tmpGData);
		butAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addElement.run();
			}
		});
		
		final Button butDelete = new Button(compButtons, SWT.NONE);
		tmpGData = new GridData(imgDelete.getImageData().width+4, imgDelete.getImageData().height+4); 
		tmpGData.verticalAlignment = GridData.BEGINNING;
		butDelete.setLayoutData(tmpGData);
		butDelete.setImage(imgDelete);
		butDelete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				deleteElement.run();
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
				upElement.run();
			}
		});
		
		final Button butDown = new Button(compButtons, SWT.NONE);
		butDown.setImage(imgDown);
		butDown.setLayoutData(new GridData(imgDown.getImageData().width+4, imgDown.getImageData().height+4));
		butDown.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				downElement.run();
			}
		});
	}
	
	protected Table createTable(Composite parent) {
		// init Table
		tableViewer = new TableViewer(parent,
				SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		ColumnViewerToolTipSupport.enableFor(tableViewer, ToolTip.NO_RECREATE);
		
		TableViewerColumn tc;
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 0);
		tc.setLabelProvider(new FarbenProzentProvider());
		tc.getColumn().setText("%");
		tc.getColumn().setToolTipText("Wahrscheinlichkeit in Prozent");
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.setEditingSupport(new ProzentEditor(tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 1);
		tc.setLabelProvider(new FarbenTextProvider());
		tc.getColumn().setText("Farbe");
		tc.getColumn().setToolTipText("Augenzahl der Würfel");
		tc.getColumn().setWidth(125);
		tc.getColumn().setMoveable(true);
		tc.setEditingSupport(new FarbenTextEditor(tableViewer));
		
		// Inhalt setzen
		tableViewer.setContentProvider(new TableViewContentProvider());
		tableViewer.setInput(new ArrayList());

		return tableViewer.getTable();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		imgAdd.dispose();
		imgDelete.dispose();
		imgUp.dispose();
		imgDown.dispose();
	}
	
	protected void makeActions() {
		
		addElement = new Action() {
			@Override
			public void run() {
				tableViewer.add(new FarbenAngabe("Bitte eingeben", 0));
			}
		};
		addElement.setText("Element hinzufügen");
		addElement.setImageDescriptor(ControlIconsLibrary.add.getImageDescriptor());
		
		deleteElement = new Action() {
			@Override
			public void run() {
				Object obj = ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
				gesamtGewaehlt -= ((FarbenAngabe) obj).getWahrscheinlichkeit();
				tableViewer.getTable().remove(tableViewer.getTable().getSelectionIndices());
			}
		};
		deleteElement.setText("Element entfernen");
		deleteElement.setImageDescriptor(ControlIconsLibrary.delete.getImageDescriptor());
		
		upElement = new Action() {
			@Override
			public void run() {
				final Table table = tableViewer.getTable();
				
				// Prüfung
				if (table.getSelection().length == 0 ||  tableViewer.getTable().isSelected(0)) {
					return;
				}
				
				// Tauschen
				for (int i = 0; i < table.getSelectionIndices().length; i++) {
					int selectedIdx = table.getSelectionIndices()[i];
					final Object selectedObject = tableViewer.getElementAt(selectedIdx);
					
					// View
					tableViewer.replace(tableViewer.getElementAt(selectedIdx-1), selectedIdx);
					tableViewer.replace(selectedObject, selectedIdx-1);
				}
				
				// Selektion neu setzen
				int[] newSelIdx = new int[table.getSelectionIndices().length];
				for (int i = 0; i < table.getSelectionIndices().length; i++) {
					newSelIdx[i] = table.getSelectionIndices()[i] - 1;
				}
				table.setSelection(newSelIdx);
			}
		};
		upElement.setText("Element nach oben");
		upElement.setImageDescriptor(ControlIconsLibrary.arrowUp.getImageDescriptor());
		
		downElement = new Action() {
			@Override
			public void run() {
				final Table table = tableViewer.getTable();
				
				// Prüfung
				if (table.getSelection().length == 0 ||  tableViewer.getTable().isSelected(table.getItemCount()-1)) {
					return;
				}
				
				// Tauschen
				for (int i = table.getSelectionIndices().length-1; i >= 0; i--) {
					int selectedIdx = table.getSelectionIndices()[i];
					final Object selectedObject = tableViewer.getElementAt(selectedIdx);
					
					// View
					tableViewer.replace(tableViewer.getElementAt(selectedIdx+1), selectedIdx);
					tableViewer.replace(selectedObject, selectedIdx+1);
				}
				
				// Selektion neu setzen
				int[] newSelIdx = new int[table.getSelectionIndices().length];
				for (int i = 0; i < table.getSelectionIndices().length; i++) {
					newSelIdx[i] = table.getSelectionIndices()[i] + 1;
				}
				table.setSelection(newSelIdx);
			}
		};
		downElement.setText("Element nach unten");
		downElement.setImageDescriptor(ControlIconsLibrary.arrowDown.getImageDescriptor());

	}
	
	/**
	 * Setzt das Context-menu
	 */
	protected void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				FarbenAuswahlTable.this.fillContextMenu(manager);
			}
		});
		
		menuMgr.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				boolean isEnabled = true;
				
				if (tableViewer.getSelection().isEmpty()) {
					isEnabled = false;
				} 
				upElement.setEnabled(isEnabled);
				downElement.setEnabled(isEnabled);
				deleteElement.setEnabled(isEnabled);
			}
		});

		// For Table
		Menu menu = menuMgr.createContextMenu(tableViewer.getControl());
		tableViewer.getControl().setMenu(menu);
		site.registerContextMenu(menuMgr, tableViewer);
	}
	
	// Das Context Menu beim Rechts-klick
	protected void fillContextMenu(IMenuManager manager) {
		manager.add(this.addElement);
		manager.add(this.deleteElement);
		manager.add(new Separator());
		manager.add(this.upElement);
		manager.add(this.downElement);
		/* Um die Warnung
		 * Context menu missing standard group 'org.eclipse.ui.IWorkbenchActionConstants.MB_ADDITIONS'
		 * im Log zu verhindern */ 
		manager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	/**
	 * @return Die Farben dieses Widgets
	 */
	public FarbenAngabe[] getValues() {
		FarbenAngabe[] farben = new FarbenAngabe[tableViewer.getTable().getItemCount()];
		for (int i = 0; i < tableViewer.getTable().getItemCount(); i++) {
			farben[i] = (FarbenAngabe) tableViewer.getElementAt(i);
		}
		return farben;
	}
	
	/**
	 * Setzt alle Werte dieses Widgets neu
	 * @param farben Werte zum setzen
	 */
	public void setValues(FarbenAngabe[] farben) {
		tableViewer.getTable().removeAll();
		if (farben == null) return;
		
		tableViewer.add(farben);
	}
	
	// Editor für die Angabe von Farben
	class FarbenTextEditor extends EditingSupport {
		private final TextCellEditor tce;
		private final ColumnViewer viewer;
		
		public FarbenTextEditor(TableViewer viewer) {
			super(viewer);
			this.viewer = viewer;
			tce = new TextCellEditor(viewer.getTable());
		}
		
		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return tce;
		}

		@Override
		protected Object getValue(Object element) {
			return ((FarbenAngabe) element).getFarbe();
		}

		@Override
		protected void setValue(Object element, Object value) {
			((FarbenAngabe) element).setFarbe(value.toString());
			viewer.update(element, null);
		}
	}
	
	// Editor für die wahrscheinlichkeit das eine Farbe ausgewählt wird
	class ProzentEditor extends EditingSupport {
		private ComboBoxCellEditor cellEditor;
		
		ProzentEditor(TableViewer viewer) {
			super(viewer);
			
			String[] initValues = new String[21];
			for (int i = 0; i < initValues.length; i++) {
				initValues[i] = ( i * 5 ) + "%";
			}
			cellEditor = new ComboBoxCellEditor(viewer.getTable(), initValues, SWT.READ_ONLY);
			((CCombo) cellEditor.getControl()).setVisibleItemCount(8);
		}
		

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			cellEditor.setValue(getValue(element));
			
			String[] initValues = new String[21
			                                 - gesamtGewaehlt
			                                 + ((FarbenAngabe) element).getWahrscheinlichkeit()];
			for (int i = 0; i < initValues.length; i++) {
				initValues[i] = ( i * 5 ) + "%";
			}
			cellEditor.setItems(initValues);
			
			return cellEditor;
		}

		@Override
		protected Object getValue(Object element) {
			return ((FarbenAngabe) element).getWahrscheinlichkeit();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected void setValue(Object element, Object value) {
			
			gesamtGewaehlt -= ((FarbenAngabe) element).getWahrscheinlichkeit();
			((FarbenAngabe) element).setWahrscheinlichkeit( (Integer) value );
			gesamtGewaehlt += ((FarbenAngabe) element).getWahrscheinlichkeit();
			
			this.getViewer().update(element, null);
		}
	}
	
	public static class FarbenProzentProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			return (((FarbenAngabe) element).getWahrscheinlichkeit() * 5 ) + "%";
		}
	}
	
	public static class FarbenTextProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			return ((FarbenAngabe) element).getFarbe();
		}
	}
}
