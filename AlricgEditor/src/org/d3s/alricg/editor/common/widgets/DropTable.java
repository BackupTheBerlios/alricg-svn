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
import org.d3s.alricg.editor.common.CustomColumnLabelProvider;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider.ImageProvider;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider.ImageProviderRegulator;
import org.d3s.alricg.editor.common.ViewUtils.CharElementDragSourceListener;
import org.d3s.alricg.editor.common.ViewUtils.TableObject;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
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
 * Erstellt eine List mit der Möglichkeit:
 * 	- 
 * @author Vincent
 */
public class DropTable extends Composite {
	protected final DropListRegulator regulator;
	protected final int NUMBER_OF_LINES = 5;
	protected final IWorkbenchPartSite site;
	protected TableViewer tableViewer;
	
	private Image imgDelete = ControlIconsLibrary.delete.getImageDescriptor().createImage();
	private Image imgUp = ControlIconsLibrary.arrowUp.getImageDescriptor().createImage();
	private Image imgDown = ControlIconsLibrary.arrowDown.getImageDescriptor().createImage();

	protected Action deleteElement;
	protected Action upElement;
	protected Action downElement;
	
	/**
	 * Interface um die DropList zu steuern
	 * @author Vincent
	 */
	public static interface DropListRegulator {
		
		/**
		 * @param obj Object von der "Drop" Opteration
		 * @return true - "obj" kann zu der Liste hinzugefügt werden, ansonsten false
		 */
		public boolean canDrop(Object obj);
		
		public ImageProviderRegulator getImageProviderRegulator();
	}
	
	/**
	 * Konstruktor
	 * @param parent SWT Parent
	 * @param style SWT Style
	 * @param regulator Regulator zum Steuern dieser DropList
	 */
	public DropTable(Composite parent, int style, DropListRegulator regulator, IWorkbenchPartSite site) {
		super(parent, style);
		this.regulator = regulator;
		this.site = site;
		
		if (regulator.getImageProviderRegulator() != null) {
			regulator.getImageProviderRegulator().getIconsLibrary().addConsumer(
					regulator.getImageProviderRegulator().getConsumer());
		}
		
		createWidget(parent);
		
		makeActions();
		hookContextMenu();
	}
	
	protected Table createTable(Composite parent) {
		// init Table
		tableViewer = new TableViewer(parent,
				SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		tableViewer.getTable().setLinesVisible(false);
		tableViewer.getTable().setHeaderVisible(false);
		ColumnViewerToolTipSupport.enableFor(tableViewer, ToolTip.NO_RECREATE);
		
		// Drag and Drop
		tableViewer.addDragSupport(
				DND.DROP_COPY | DND.DROP_MOVE, 
				new Transfer[] { LocalSelectionTransfer.getTransfer() }, 
				new CharElementDragSourceListener(tableViewer));
		
		TableViewerColumn tc;
		int colIdx = 0;
		
		// Columns setzen
		if (regulator.getImageProviderRegulator() != null) {
			tc = new TableViewerColumn(tableViewer, SWT.LEFT, colIdx++);
			tc.getColumn().setToolTipText("Symbol");
			tc.setLabelProvider(new ImageProvider(0, regulator.getImageProviderRegulator()));
			tc.getColumn().setWidth(24);
			tc.getColumn().setMoveable(true);
		}
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, colIdx);
		tc.setLabelProvider(new CustomColumnLabelProvider.NameLabelProvider());
		tc.getColumn().setWidth(150);
		tc.getColumn().setMoveable(true);
		
		// Inhalt setzen
		tableViewer.setContentProvider(new TableViewContentProvider());
		tableViewer.setInput(new ArrayList());

		// Drop unterstützung
		ViewerDropAdapter viewerDrop = new ViewerDropAdapter(tableViewer) {
			@Override
			public void drop(DropTargetEvent event) {
				
				if ( !performDrop(event.data) )	{
					event.feedback = DND.ERROR_INVALID_DATA;
					event.detail = DND.DROP_NONE;
					return;
				}
				final TreeOrTableObject source = (TreeOrTableObject) ((StructuredSelection) event.data).getFirstElement();
				
				// Prüfen ob doppelt
				for (int i = 0; i < tableViewer.getTable().getItemCount(); i++) {
					if ( ((TableObject) tableViewer.getElementAt(i)).getValue().equals(source.getValue()) ) {
						return; // schon vorhanden!
					}
				}
				
				final TableObject tablObj = new TableObject(source.getValue());
				tableViewer.add(tablObj);
			}

			@Override
			public boolean performDrop(Object data) {
				return regulator.canDrop(	
						((TreeOrTableObject) ((StructuredSelection) data).getFirstElement()).getValue() );
			}

			@Override
			public boolean validateDrop(Object target, int operation, TransferData transferType) {
				return true;
			}
		};
		
		// Unterstützung für DROP
		int ops = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transfers = new Transfer[] { LocalSelectionTransfer
				.getTransfer() };
		tableViewer.addDropSupport(ops, transfers, viewerDrop);

		return tableViewer.getTable();
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
		compButtons.setLayout(gridLayout);
		
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

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		if (regulator.getImageProviderRegulator() != null) {
			regulator.getImageProviderRegulator().getIconsLibrary().removeConsumer(
					regulator.getImageProviderRegulator().getConsumer());
		}
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
		java.util.List list = new ArrayList();
		
		for (int i = 0 ; i < tableViewer.getTable().getItemCount(); i++) {
			list.add( ((TableObject) tableViewer.getElementAt(i)).getValue()); 
		}

		return list;
	}
	
	/**
	 * Fügt einen Wert an das Ende der Liste hinzu
	 * @param text Der Text für die List
	 * @param value Der Wert für die ValueList
	 */
	public void addValue(Object value) {
		this.tableViewer.add(new TableObject(value));
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
				DropTable.this.fillContextMenu(manager);
			}
		});
		
		menuMgr.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				boolean isEnabled;
				
				if (tableViewer.getSelection().isEmpty()) {
					isEnabled = false;
				} else {
					isEnabled = true;
				}
				
				for (int i = 0; i < manager.getItems().length; i++) {
					if (!(manager.getItems()[i] instanceof ActionContributionItem)) {
						continue;
					}
					ActionContributionItem item = (ActionContributionItem) manager.getItems()[i];
					item.getAction().setEnabled(isEnabled);
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
		manager.add(this.deleteElement);
		manager.add(new Separator());
		manager.add(this.upElement);
		manager.add(this.downElement);
		/* Um die Warnung
		 * Context menu missing standard group 'org.eclipse.ui.IWorkbenchActionConstants.MB_ADDITIONS'
		 * im Log zu verhindern */ 
		manager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
	}
}
