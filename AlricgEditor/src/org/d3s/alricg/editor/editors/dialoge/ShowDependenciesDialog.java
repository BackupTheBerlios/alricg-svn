/*
 * Created 13.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.dialoge;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.d3s.alricg.editor.Activator;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.ViewerSelectionListener;
import org.d3s.alricg.editor.editors.composits.CharElementEditorInput;
import org.d3s.alricg.editor.utils.ViewEditorIdManager;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTableObject;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTreeOrTableObject;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.access.CharElementFactory.DependencyTableObject;
import org.d3s.alricg.store.charElemente.CharElement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * @author Vincent
 */
public class ShowDependenciesDialog extends TitleAreaDialog {
	private final CharElement charElementToDelete;
	private List<DependencyEditorTableObject> editorTabObj;
	private Action doubleClickAction;
	private TableViewer tableViewer;
	
	/*
	public ShowDependenciesDialog(IShellProvider parentShell, CharElement charElement, List<DependencyTableObject> depList) {
		super(parentShell);
		this.charElementToDelete = charElement;
		createTable(depList);
	}*/

	public ShowDependenciesDialog(Shell parentShell, CharElement charElement, List<DependencyTableObject> depList) {
		super(parentShell);
		this.charElementToDelete = charElement;
		createTable(depList);
		this.setTitle("lal");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		final Button okButton = this.createButton(parent, OK, "Ok", true);
		okButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(OK);
				close();
			}
			
		});
	}

	@Override
	public void create() {
		super.create();
		this.setTitle("Element kann nicht gelöscht werden");
		this.setMessage(" Das Charakter-Element " + charElementToDelete.getName() + 
				" kann nicht gelöscht werden, solange die folgenden " +
				"Abhängigkeiten zu anderen Charakter-Elementen bestehen:");
	}

	private void createTable(List<DependencyTableObject> depList) {
		editorTabObj = new ArrayList<DependencyEditorTableObject>();
		
		for (int i = 0; i < depList.size(); i++) {
			DependencyEditorTableObject tmpObj = new DependencyEditorTableObject(
					depList.get(i).getCharElement(), 
					depList.get(i).getAccessor(),
					depList.get(i).getText());
			editorTabObj.add(tmpObj);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		
		GridData tmpGData;
		
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(gridLayout);
		
		tableViewer = new TableViewer(container,
				SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		tmpGData = new GridData(GridData.FILL_BOTH); 
		tmpGData.heightHint = 100;
		tableViewer.getTable().setLayoutData(tmpGData);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		ColumnViewerToolTipSupport.enableFor(tableViewer, ToolTip.NO_RECREATE);
		
		TableViewerColumn tc = new TableViewerColumn(tableViewer, SWT.LEFT, 0);
		tableViewer.getTable().setSortColumn(tc.getColumn());
		tc.getColumn().setText("Name");
		tc.getColumn().setWidth(100);
		tc.setLabelProvider(new ColumnLabelProvider());
		tc.setLabelProvider(new CustomColumnLabelProvider.NameLabelProvider());
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.NameSorter(),
						tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 1);
		tc.getColumn().setText("Klasse");
		tc.getColumn().setWidth(100);
		tc.setLabelProvider(new CustomColumnLabelProvider.CharElementKlassenLabelProvider());
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.CharElementKlasseSorter(),
						tableViewer));
		
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 2);
		tc.getColumn().setText("Datei");
		tc.getColumn().setWidth(100);
		tc.setLabelProvider(new CustomColumnLabelProvider.DateinameLabelProvider());
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.DateiSorter(),
						tableViewer));
		
		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 3);
		tc.getColumn().setText("Anmerkung");
		tc.getColumn().setWidth(200);
		tc.setLabelProvider(new DependencyTextLabelProvider());
		
		// Inhalt und Sortierung setzen
		tableViewer.setContentProvider(new TableViewContentProvider());
		tableViewer.getTable().setSortDirection(SWT.UP);
		tableViewer.setSorter(new CustomColumnViewerSorter.NameSorter());
		tableViewer.setInput(editorTabObj);
		
		// Actions
		makeActions();
		hookActions();
		
		return container;
	}
	
	private void hookActions() {
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	
	private void makeActions() {
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = tableViewer.getSelection();
				if (selection.isEmpty()) return;
				
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				
				final EditorTreeOrTableObject treeTableObj =  (EditorTreeOrTableObject) obj;
				final IWorkbenchPage page = PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getActivePage();
				
				IEditorInput editorInput = new CharElementEditorInput(
						(CharElement) treeTableObj.getValue(),
						treeTableObj.getAccessor());
		
				try {
					page.openEditor(
							editorInput, 
							ViewEditorIdManager.getEditorID(treeTableObj.getValue().getClass()), 
							true);
					
				} catch (PartInitException e) {
					Activator.logger.log(
							Level.SEVERE, 
							"Konnte Editor nicht öffnen. Editor ID: " 
								+ ViewEditorIdManager.getEditorID(treeTableObj.getValue().getClass()), 
							e);
				}
			}
		};
	}
	public static class DependencyEditorTableObject extends EditorTableObject {
		final String text;
		
		public DependencyEditorTableObject(Object value, XmlAccessor accessor, String text) {
			super(value, accessor);
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}
	
	public static class DependencyTextLabelProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			return  ((DependencyEditorTableObject) element).getText();
		}
	}
	
}
