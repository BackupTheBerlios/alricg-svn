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
import org.d3s.alricg.store.charElemente.charZusatz.WuerfelSammlung;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * Ermöglicht die Angabe von einer beliebigen Anzahl von Würfeln mit verschiedenen Augen
 * zusätzlich zu einem Festwert.
 * @author Vincent
 */
public class WuerfelSammlungTable extends Composite {
	private static int NUMBER_OF_LINES = 2;
	protected TableViewer tableViewer;
	private IWorkbenchPartSite site;
	
	private final Image imgAdd = ControlIconsLibrary.add.getImageDescriptor().createImage();
	private final Image imgDelete = ControlIconsLibrary.delete.getImageDescriptor().createImage();

	protected Action addElement;
	protected Action deleteElement;
	private Spinner spiPlus;


	/**
	 * Konstruktor
	 * @param parent SWT Parent
	 * @param style SWT Style
	 * @param site Seite für das KontextMenu
	 * @param startWert Inital-Wert für den Festwert
	 * @param einheit Einheit in der die Angaben gemacht werden
	 */
	public WuerfelSammlungTable(Composite parent, int style, IWorkbenchPartSite site, int startWert, String einheit) {
		super(parent, style);
		this.site = site;
		createWidget(parent, startWert, einheit);
		
		makeActions();
		hookContextMenu();
	}
	
	private void createWidget(Composite parent, int startWert, String einheit) {
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
		
		final Composite comPlus = new Composite(this, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 1;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.numColumns = 3;
		tmpGData = new GridData();
		comPlus.setLayout(gridLayout);
		comPlus.setLayoutData(tmpGData);
		
		final Label lblPlus = new Label(comPlus, SWT.NONE);
		lblPlus.setText(" + ");
		spiPlus = new Spinner(comPlus, SWT.BORDER);
		spiPlus.setValues(
				startWert, 
				0, 
				9999, 
				0, 1, 25);
		final Label lblEinheit = new Label(comPlus, SWT.NONE);
		lblEinheit.setText(" " + einheit + " ");
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
		tc.setLabelProvider(new WuerfelSammlungAnzahlProvider());
		tc.getColumn().setText("Anz.");
		tc.getColumn().setToolTipText("Anzahl von Würfeln");
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.setEditingSupport(new AnzahlEditor(tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 1);
		tc.setLabelProvider(new WuerfelSammlungWuerfelProvider());
		tc.getColumn().setText("W");
		tc.getColumn().setToolTipText("Augenzahl der Würfel");
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.setEditingSupport(new WuerfelEditor(tableViewer));
		
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
	}

	protected void makeActions() {
		
		deleteElement = new Action() {
			@Override
			public void run() {
				tableViewer.getTable().remove(tableViewer.getTable().getSelectionIndices());
			}
		};
		deleteElement.setText("Element entfernen");
		deleteElement.setImageDescriptor(ControlIconsLibrary.delete.getImageDescriptor());
		
		addElement = new Action() {
			@Override
			public void run() {
				tableViewer.add(new int[] {1,6});
			}
		};
		addElement.setText("Element hinzufügen");
		addElement.setImageDescriptor(ControlIconsLibrary.add.getImageDescriptor());
	}
	
	/**
	 * Setzt das Context-menu
	 */
	protected void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				WuerfelSammlungTable.this.fillContextMenu(manager);
			}
		});
		
		menuMgr.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (tableViewer.getSelection().isEmpty()) {
					deleteElement.setEnabled(false);
				} else {
					deleteElement.setEnabled(true);
				}
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
		/* Um die Warnung
		 * Context menu missing standard group 'org.eclipse.ui.IWorkbenchActionConstants.MB_ADDITIONS'
		 * im Log zu verhindern */ 
		manager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	/**
	 * Set die Werte des Widgets neu.
	 * @param sammlung Sammlung mit den zu setzenden Werten
	 */
	public void setValues(WuerfelSammlung sammlung) {
		if (sammlung == null) return;
		
		spiPlus.setSelection(sammlung.getFestWert());
		tableViewer.getTable().removeAll();
		for (int i = 0; i < sammlung.getAnzahlWuerfel().length; i++) {
			tableViewer.add(new int[] {
						sammlung.getAnzahlWuerfel()[i],
						sammlung.getAugenWuerfel()[i] });
		}
	}
	
	/**
	 * @return Eine WuerfelSammlung mit den Werten deses Widgets
	 */
	public WuerfelSammlung getValues() {
		WuerfelSammlung sammlung = new WuerfelSammlung();
		
		sammlung.setFestWert(spiPlus.getSelection());
		
		if (tableViewer.getTable().getItemCount() == 0 && spiPlus.getSelection() == 0) {
			return null;
		}
		
		int[] anzahlW = new int[tableViewer.getTable().getItemCount()];
		int[] augenW = new int[tableViewer.getTable().getItemCount()];
		for (int i = 0 ; i < tableViewer.getTable().getItemCount(); i++) {
			anzahlW[i] = ((int[]) tableViewer.getElementAt(i))[0];
			augenW[i] = ((int[]) tableViewer.getElementAt(i))[1];
		}
		sammlung.setAnzahlWuerfel(anzahlW);
		sammlung.setAugenWuerfel(augenW);
		
		return sammlung;
	}
	
	
	// ComboBox für die Würfel-Augen
	class WuerfelEditor extends EditingSupport {
		private ComboBoxCellEditor cellEditor;
		
		WuerfelEditor(TableViewer viewer) {
			super(viewer);
			
			String[] initValues = new String[] {"W3","W4","W6","W10","W20","W100"};

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
			return cellEditor;
		}

		@Override
		protected Object getValue(Object element) {
			switch(((int[]) element)[1] ) {
				case 3: return 0;
				case 4: return 1;
				case 6: return 2;
				case 10: return 3;
				case 20: return 4;
				case 100: return 5;
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected void setValue(Object element, Object value) {
			switch( (Integer) value ) {
				case 0: ((int[]) element)[1] = 3; break;
				case 1: ((int[]) element)[1] = 4; break;
				case 2: ((int[]) element)[1] = 6; break;
				case 3: ((int[]) element)[1] = 10; break;
				case 4: ((int[]) element)[1] = 20; break;
				case 5: ((int[]) element)[1] = 100; break;
			}
			this.getViewer().update(element, null);
		}
	}
	
	// ComboBox für die Anzahl Würfel
	class AnzahlEditor extends EditingSupport {
		private ComboBoxCellEditor cellEditor;
		
		AnzahlEditor(TableViewer viewer) {
			super(viewer);
			
			String[] initValues = new String[20];
			for (int i = 0; i < initValues.length; i++) {
				initValues[i] = Integer.toString( i+1 );
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
			return cellEditor;
		}

		@Override
		protected Object getValue(Object element) {
			return ((int[]) element)[0] -1;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected void setValue(Object element, Object value) {
			((int[]) element)[0] = (Integer) value + 1;
			this.getViewer().update(element, null);
		}
	}
	
	public static class WuerfelSammlungAnzahlProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			return Integer.toString( ((int[]) element)[0] );
		}
	}
	
	public static class WuerfelSammlungWuerfelProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			return "W"+Integer.toString( ((int[]) element)[1] );
		}
	}
}
