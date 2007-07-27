package org.d3s.alricg.editor.views.charElemente;

import java.util.List;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.d3s.alricg.common.icons.ImageService;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter;
import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.common.CustomActions.BuildNewCharElementAction;
import org.d3s.alricg.editor.common.CustomActions.DeleteCharElementAction;
import org.d3s.alricg.editor.common.CustomActions.EditCharElementAction;
import org.d3s.alricg.editor.common.CustomActions.FilterCurrentFileAction;
import org.d3s.alricg.editor.common.CustomActions.SwapTreeTableAction;
import org.d3s.alricg.editor.common.ViewUtils.Regulator;
import org.d3s.alricg.editor.common.ViewUtils.TableObject;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.ViewerSelectionListener;
import org.d3s.alricg.editor.editors.TalentEditor;
import org.d3s.alricg.editor.utils.CharElementEditorInput;
import org.d3s.alricg.editor.utils.EditorViewUtils;
import org.d3s.alricg.editor.views.FileView;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Talent;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class TalentView extends ViewPart {
	public static final String ID = "org.d3s.alricg.editor.views.TalentView";
	
	private TableViewer viewerTable;
	private TreeViewer viewerTree;
	private Composite parentComp;

	private Action swapTreeTable;
	private FilterCurrentFileAction filterAktuellesFile;
	private Action showInfos;
	private BuildNewCharElementAction buildNew;
	private Action deleteSelected;
	private Action editSelected;

	private Action doubleClickAction;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new StackLayout());

		this.parentComp = parent;
		viewerTable = createTable(parent);
		viewerTree = createTree(parent);
		((StackLayout) parent.getLayout()).topControl = viewerTree.getTree();
		parent.layout();

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();

		getSite().getPage().addSelectionListener(
				FileView.ID,
				(ISelectionListener) filterAktuellesFile);

	}

	/**
	 * Erstellt eine TreeTable + ContextMenu und setzt sie in den View
	 */
	private TreeViewer createTree(Composite parent) {
		final Regulator regulator = new Regulator() {

			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				return new Object[] { ((Talent) charElement).getSorte() };
			}

			@Override
			public List<? extends CharElement> getListFromAccessor(
					XmlAccessor accessor) {
				return accessor.getTalentList();
			}
		};

		// Init Viewer
		final TreeViewer treeViewer = new TreeViewer(parent, SWT.FULL_SELECTION
				| SWT.H_SCROLL | SWT.V_SCROLL);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setHeaderVisible(true);
		ColumnViewerToolTipSupport.enableFor(treeViewer, ToolTip.NO_RECREATE);

		TreeViewerColumn tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 0);
		tc.getColumn().setText("Name");
		treeViewer.getTree().setSortColumn(tc.getColumn());
		tc.setLabelProvider(new CustomColumnLabelProvider.NameLabelProvider());
		tc.getColumn().setWidth(200);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.NameSorter(), treeViewer));

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 1);
		tc.getColumn().setText("Datei");
		tc
				.setLabelProvider(new CustomColumnLabelProvider.DateinameLabelProvider());
		tc.getColumn().setWidth(125);
		tc.getColumn().setMoveable(true);
		tc.getColumn()
				.addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.DateiSorter(),
								treeViewer));

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 2);
		tc.getColumn().setText("Art");
		tc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((TreeObject) element).getValue() instanceof Talent) {
					return ((Talent) ((TreeObject) element).getValue())
							.getArt().toString();
				}
				return "";
			}
		});
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.ArtSorter(), treeViewer));

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 3);
		tc.getColumn().setText("Probe");
		tc
				.setLabelProvider(new CustomColumnLabelProvider.EigenschaftLabelProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 4);
		tc.getColumn().setText("SKT");
		tc.setLabelProvider(new CustomColumnLabelProvider.SKTLabelProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.SktSorter(), treeViewer));

		// Inhalt und Sortierung setzen
		treeViewer.setContentProvider(new TreeViewContentProvider(
				EditorViewUtils.buildEditorViewTree(StoreDataAccessor
						.getInstance().getXmlAccessors(), regulator)));
		treeViewer.getTree().setSortDirection(SWT.DOWN);
		treeViewer.setSorter(new CustomColumnViewerSorter.NameSorter());
		treeViewer.setInput(getViewSite());

		return treeViewer;
	}

	/**
	 * Erstellt eine Table + ContextMenu und setzt sie in den View.
	 * 
	 * @param parent
	 */
	private TableViewer createTable(Composite parent) {
		final Regulator regulator = new Regulator() {
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				return null;
			}

			@Override
			public List<? extends CharElement> getListFromAccessor(
					XmlAccessor accessor) {
				return accessor.getTalentList();
			}
		};

		final TableViewer tableViewer = new TableViewer(parent,
				SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		ColumnViewerToolTipSupport.enableFor(tableViewer, ToolTip.NO_RECREATE);

		// Columns setzen
		TableViewerColumn tc = new TableViewerColumn(tableViewer, SWT.LEFT, 0);
		tableViewer.getTable().setSortColumn(tc.getColumn());
		tc.getColumn().setText("Name");
		tc.setLabelProvider(new CustomColumnLabelProvider.NameLabelProvider());
		tc.getColumn().setWidth(200);
		tc.getColumn()
				.addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.NameSorter(),
								tableViewer));

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 1);
		tc.getColumn().setText("Datei");
		tc
				.setLabelProvider(new CustomColumnLabelProvider.DateinameLabelProvider());
		tc.getColumn().setWidth(150);
		tc.getColumn().setMoveable(true);
		tc.getColumn()
				.addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.DateiSorter(),
								tableViewer));

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 2);
		tc.getColumn().setText("Art");
		tc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((TableObject) element).getValue() instanceof Talent) {
					return ((Talent) ((TableObject) element).getValue())
							.getArt().toString();
				}
				return "";
			}
		});
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.ArtSorter(), tableViewer));

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 3);
		tc.getColumn().setText("Sorte");
		tc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((TableObject) element).getValue() instanceof Talent) {
					return ((Talent) ((TableObject) element).getValue())
							.getSorte().toString();
				}
				return "";
			}
		});
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn()
				.addSelectionListener(
						new ViewerSelectionListener(
								new CustomColumnViewerSorter.SorteSorter(),
								tableViewer));

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 4);
		tc.getColumn().setText("Probe");
		ColumnViewerToolTipSupport.enableFor(tableViewer, ToolTip.NO_RECREATE);
		tc
				.setLabelProvider(new CustomColumnLabelProvider.EigenschaftLabelProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);

		tc = new TableViewerColumn(tableViewer, SWT.LEFT, 5);
		tc.getColumn().setText("SKT");
		tc.setLabelProvider(new CustomColumnLabelProvider.SKTLabelProvider());
		tc.getColumn().setWidth(75);
		tc.getColumn().setMoveable(true);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.SktSorter(), tableViewer));

		// Inhalt und Sortierung setzen
		tableViewer.setContentProvider(new TableViewContentProvider(
				EditorViewUtils.buildTableView(StoreDataAccessor.getInstance()
						.getXmlAccessors(), regulator)));

		tableViewer.getTable().setSortDirection(SWT.UP);
		tableViewer.setSorter(new CustomColumnViewerSorter.NameSorter());
		tableViewer.setInput(getViewSite());

		return tableViewer;
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				TalentView.this.fillContextMenu(manager);
			}
		});
		
		menuMgr.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				boolean isEnabled = true;
				final TreeOrTableObject treeTableObj = ViewUtils.getSelectedObject(parentComp);
				
				if (treeTableObj != null && treeTableObj.getValue() instanceof CharElement) {
					isEnabled = true;
				} else {
					isEnabled = false;
				}
				
				for (int i = 0; i < manager.getItems().length; i++) {
					if (!(manager.getItems()[i] instanceof ActionContributionItem)) {
						continue;
					}
					ActionContributionItem item = (ActionContributionItem) manager.getItems()[i];
					
					if (!(item.getAction() instanceof BuildNewCharElementAction)) {
						item.getAction().setEnabled(isEnabled);
					}
				}
				
			}
		});

		// For Tree
		Menu menu = menuMgr.createContextMenu(viewerTree.getControl());
		viewerTree.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewerTree);

		// For Table
		menu = menuMgr.createContextMenu(viewerTable.getControl());
		viewerTable.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewerTable);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	// Lokales Pull-Down Menu
	private void fillLocalPullDown(IMenuManager manager) {
		/*
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
		*/
	}

	// Das Context Menu beim Rechts-klick
	private void fillContextMenu(IMenuManager manager) {
		manager.add(this.showInfos);
		manager.add(new Separator());
		manager.add(this.buildNew);
		manager.add(this.editSelected);
		manager.add(this.deleteSelected);
		
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	/*
	 * Das "Pop-Up" Menu rechts Op
	 */
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(this.buildNew);
		manager.add(new Separator());
		manager.add(this.swapTreeTable);
		manager.add(this.filterAktuellesFile);

		/*
		MenuManager mm = new MenuManager("Filter");
		manager.add(mm);
		mm = new MenuManager("Sortierung");
		manager.add(mm);
		mm.add(action2);
		*/
	}

	private void makeActions() {

		// Ansichte wechseln Action
		swapTreeTable = new SwapTreeTableAction(this.parentComp);

		// Tabelle nach File Filtern Action
		filterAktuellesFile = new FilterCurrentFileAction(viewerTable, viewerTree);

		// Information anzeigen Action
		showInfos = new Action("Infos") {
			public void run() {
				showMessage("Noch zu implementieren!");
			}
		};
		showInfos.setToolTipText("Zeigt zu Element weitere Informationen an.");
		showInfos.setImageDescriptor(ControlIconsLibrary.info.getImageDescriptor());

		// Element Bearbeiten Action
		editSelected = new EditCharElementAction(this.parentComp, TalentEditor.ID);

		// Neues Element Action
		buildNew = new BuildNewCharElementAction() {
			public void run() {
				showMessage("Noch zu implementieren!");
			}
		};
		
		// Element L�schen Action
		deleteSelected = new DeleteCharElementAction();
		

		// ---------------
		
		// TEST  noch zu entfernen
		doubleClickAction = new Action() {
			public void run() {
				ColumnViewer viewer;

				Control topControl = ((StackLayout) parentComp.getLayout()).topControl;
				if (topControl.equals(viewerTable.getTable())) {
					viewer = viewerTable;
				} else {
					viewer = viewerTree;
				}

				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				showMessage("Double-click detected on " + obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {

		viewerTable.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});

		viewerTree.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(
				((StackLayout) parentComp.getLayout()).topControl.getShell(),
				"Talent View", message);

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		((StackLayout) parentComp.getLayout()).topControl.setFocus();
	}
}