/*
 * Created 27.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter;
import org.d3s.alricg.editor.common.CustomActions.InfoCharElementAction;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.ViewerSelectionListener;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.charElemente.links.Option;
import org.d3s.alricg.store.charElemente.links.OptionAnzahl;
import org.d3s.alricg.store.charElemente.links.OptionListe;
import org.d3s.alricg.store.charElemente.links.OptionVerteilung;
import org.d3s.alricg.store.charElemente.links.OptionVoraussetzung;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * @author Vincent
 * 
 */
public class VoraussetzungPart extends AbstarctElementPart<CharElement> {
	private TreeViewer treeViewer;
	private Composite parentComp;
    private IWorkbenchPartSite partSite;
	
	protected Action showInfos;
	protected Action buildNewAlternative;
	protected Action buildNewVoraussetzung;
	protected Action deleteSelected;
	protected Action editSelected;
	
	public Tree getTree() {
		return treeViewer.getTree();
	}
	
	public VoraussetzungPart(Composite top, IWorkbenchPartSite partSite) {
		parentComp = top;
		this.partSite = partSite;
		
		treeViewer = new TreeViewer(parentComp, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setHeaderVisible(true);
		ColumnViewerToolTipSupport.enableFor(treeViewer, ToolTip.NO_RECREATE);

		TreeViewerColumn tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 0);
		tc.getColumn().setText("Name");
		treeViewer.getTree().setSortColumn(tc.getColumn());
		tc.setLabelProvider(new CustomColumnLabelProvider.OptionNameLabelProvider());
		tc.getColumn().setWidth(200);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.OptionNameSorter(), treeViewer));
		tc.setEditingSupport(new EditingSupport(treeViewer){

			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
			 */
			@Override
			protected boolean canEdit(Object element) {
				// TODO Auto-generated method stub
				return true;
			}

			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
			 */
			@Override
			protected CellEditor getCellEditor(Object element) {
				return
					new DialogCellEditor(treeViewer.getTree()){
						/* (non-Javadoc)
						 * @see org.eclipse.jface.viewers.DialogCellEditor#createButton(org.eclipse.swt.widgets.Composite)
						 */
						@Override
						protected Button createButton(Composite parent) {
					        Button result = new Button(parent, SWT.DOWN);
					        result.setText(" + "); //$NON-NLS-1$
					        return result;
						}

						/* (non-Javadoc)
						 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
						 */
						@Override
						protected Object openDialogBox(Control cellEditorWindow) {
							VoraussetzungDialog vdce = new VoraussetzungDialog(cellEditorWindow.getShell());
							return vdce.open();
						}};
			}

			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
			 */
			@Override
			protected Object getValue(Object element) {
				// TODO Auto-generated method stub
				return element;
			}

			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
			 */
			@Override
			protected void setValue(Object element, Object value) {
				// TODO Auto-generated method stub
				
			}});
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 1);
		tc.getColumn().setText("Stufe");
		tc.setLabelProvider(new CustomColumnLabelProvider.LinkWertProvider());
		tc.getColumn().setWidth(100);
		tc.getColumn().setMoveable(true);

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 2);
		tc.getColumn().setText("Text");
		tc.setLabelProvider(new CustomColumnLabelProvider.LinkTextProvider());
		tc.getColumn().setWidth(100);
		tc.getColumn().setMoveable(true);

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 3);
		tc.getColumn().setText("ZweitZiel");
		tc.setLabelProvider(new CustomColumnLabelProvider.LinkZweitZielProvider());
		tc.getColumn().setWidth(100);
		tc.getColumn().setMoveable(true);

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 4);
		tc.getColumn().setText("-");
		tc.setLabelProvider(new ColumnLabelProvider());
		tc.getColumn().setWidth(25);
		tc.getColumn().setMoveable(true);
		
		treeViewer.getTree().setSortDirection(SWT.DOWN);
		
		makeActions();
		hookContextMenu();
		
		// Unterstützung für DRAG
		treeViewer.addDragSupport(DND.DROP_COPY | DND.DROP_MOVE,
				new Transfer[] { LocalSelectionTransfer.getTransfer() },
				new DragSourceListener() {
					@Override
					public void dragStart(DragSourceEvent event) {
						if (treeViewer.getSelection().isEmpty()) {
							event.doit = false;
						}
						final TreeOrTableObject treeTableObj = (TreeOrTableObject) ((StructuredSelection) treeViewer
								.getSelection()).getFirstElement();

						if (!(treeTableObj.getValue() instanceof Link)) {
							event.doit = false;
						}
					}

					@Override
					public void dragSetData(DragSourceEvent event) {
						IStructuredSelection selection = (IStructuredSelection) treeViewer
								.getSelection();
						LocalSelectionTransfer.getTransfer().setSelection(
								selection);

					}

					@Override
					public void dragFinished(DragSourceEvent event) {
						final TreeObject treeObj = (TreeObject) ((StructuredSelection) treeViewer
								.getSelection()).getFirstElement();
						
						treeObj.getParent().removeChildren(treeObj);
						treeViewer.refresh();
					}
				});

		// Unterstützung für DROP
		int ops = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transfers = new Transfer[] { LocalSelectionTransfer
				.getTransfer() };
		treeViewer.addDropSupport(ops, transfers,
			new org.eclipse.jface.viewers.ViewerDropAdapter(treeViewer) {
				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.jface.viewers.ViewerDropAdapter#drop(org.eclipse.swt.dnd.DropTargetEvent)
				 */
				@Override
				public void drop(DropTargetEvent event) {
					TreeObject sourceObj = (TreeObject) ((TreeSelection) event.data).getFirstElement();
					TreeObject targetObj = (TreeObject) getCurrentTarget();

					if (((TreeObject) targetObj).getValue() instanceof Link) {
						targetObj = targetObj.getParent(); // Da CharElement immer an einer Option hängen
					} else if (((TreeObject) targetObj).getValue() instanceof String) {
						// Direkt "positiv" oder "negativ"
						if (targetObj.getChildren() == null) {
							TreeObject newObject = new TreeObject(
									createOption(OptionVoraussetzung.class), 
									targetObj);
							targetObj.addChildren(newObject);
			
						}
						
						targetObj = targetObj.getChildren()[0];
					}
					
					Object valueObj;
					if (sourceObj.getValue() instanceof Link) {
						valueObj = sourceObj.getValue();
					} else {
						valueObj = new IdLink(
								null, // TODO Quelle einsetzten 
								(CharElement) sourceObj.getValue(), 
								null, 
								IdLink.KEIN_WERT, 
								null);
					}
					TreeObject newObject = new TreeObject(valueObj, targetObj);
					targetObj.addChildren(newObject);
					treeViewer.refresh();

					super.drop(event);
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.jface.viewers.ViewerDropAdapter#performDrop(java.lang.Object)
				 */
				@Override
				public boolean performDrop(Object data) {
					return true;
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.jface.viewers.ViewerDropAdapter#validateDrop(java.lang.Object,
				 *      int, org.eclipse.swt.dnd.TransferData)
				 */
				@Override
				public boolean validateDrop(Object target, int operation,
						TransferData transferType) {
					if (target == null) {
						return false;
					}
					return true;
				}
			});
	}
	
	// Das Context Menu beim Rechts-klick
	private void fillContextMenu(IMenuManager manager) {
		manager.add(this.showInfos);
		manager.add(new Separator());
		manager.add(this.buildNewVoraussetzung);
		manager.add(this.buildNewAlternative);
		manager.add(this.editSelected);
		manager.add(this.deleteSelected);
	}
	
	/**
	 * Setzt das Context-menu
	 */
	private void hookContextMenu() {
		
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				VoraussetzungPart.this.fillContextMenu(manager);
			}
		});

		
		menuMgr.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				
				if (getTree().getSelection().length == 0) return;
				boolean isEnabled = true;
				final TreeObject treeObj = (TreeObject) getTree().getSelection()[0].getData();
				
				if (treeObj.getValue() instanceof Link) {
					buildNewAlternative.setEnabled(true);
					buildNewVoraussetzung.setEnabled(false);
					deleteSelected.setEnabled(true);
					editSelected.setEnabled(false);
				} else if (treeObj.getValue() instanceof Option){
					buildNewAlternative.setEnabled(true);
					buildNewVoraussetzung.setEnabled(true);
					deleteSelected.setEnabled(true);
					editSelected.setEnabled(true);
				} else {
					buildNewAlternative.setEnabled(false);
					buildNewVoraussetzung.setEnabled(true);
					deleteSelected.setEnabled(false);
					editSelected.setEnabled(false);
				}
			}
		});
		
		// For Tree
		Menu menu = menuMgr.createContextMenu(treeViewer.getTree());
		treeViewer.getTree().setMenu(menu);
		partSite.registerContextMenu(menuMgr, treeViewer);
	}
	
	protected void makeActions() {

		// Information anzeigen Action
		showInfos = new InfoCharElementAction() {
			@Override
			public void run() {
				MessageDialog.openInformation(
						parentComp.getShell(),
						"TODO", "Noch zu implementieren");
			}};
		showInfos.setText("Informationen");
		showInfos.setToolTipText("Zeigt zu Element weitere Informationen an.");
		showInfos.setImageDescriptor(ControlIconsLibrary.info.getImageDescriptor());

		// Neues Element Action 
		buildNewAlternative = new Action() {

			@Override
			public void run() {
				if (getTree().getSelection().length == 0) return;
				final TreeObject treeObj = (TreeObject) getTree().getSelection()[0].getData();
				TreeObject parentTreeObj = null;
				
				if (treeObj.getValue() instanceof Link) {
					parentTreeObj = treeObj.getParent();
				} 
				if (treeObj.getValue() instanceof Option) {
					parentTreeObj = treeObj;
					if (parentTreeObj.getParent().getValue() instanceof Option) {
						parentTreeObj = parentTreeObj.getParent();
					}
				}
				
				parentTreeObj.addChildren(new TreeObject(
						createOption(getOptionClass()),
						parentTreeObj));
				treeViewer.refresh();
			}
			
			protected Class getOptionClass() {
				return OptionVoraussetzung.class;
			}		
		};
		buildNewAlternative.setText("Neue Alternative erzeugen");
		buildNewAlternative.setToolTipText("Erzeugt eine neue Alternative.");
		buildNewAlternative.setImageDescriptor(ControlIconsLibrary.add.getImageDescriptor());
		
		
		editSelected = new Action() {

			@Override
			public void run() {
				MessageDialog.openInformation(
						parentComp.getShell(),
						"TODO", "Noch zu implementieren");
			}

		};
		editSelected.setText("Voraussetzung bearbeiten");
		editSelected.setToolTipText("Voraussetzung bearbeiten.");
		editSelected.setImageDescriptor(ControlIconsLibrary.edit.getImageDescriptor());

		
		// Neues Element Action 
		buildNewVoraussetzung = new Action() {
			@Override
			public void run() {
				if (getTree().getSelection().length == 0) return;
				final TreeObject treeObj = (TreeObject) getTree().getSelection()[0].getData();
				final TreeObject parentTreeObj = findNodeToAdd(treeObj);
				
				parentTreeObj.addChildren(new TreeObject(
						createOption(getOptionClass()),
						parentTreeObj));
				treeViewer.refresh();
			}
			
			protected Class getOptionClass() {
				return OptionVoraussetzung.class;
			}
			
			protected TreeObject findNodeToAdd(TreeObject treeObj) {
				
				if (treeObj.getParent() == null) {
					return treeObj;
				}
				
				while (!(treeObj.getParent().getParent() == null)) {
					treeObj = treeObj.getParent();
				}
				
				return treeObj;
			}
		};
			
		buildNewVoraussetzung.setText("Neue Voraussetzung erzeugen");
		buildNewVoraussetzung.setToolTipText("Erzeugt eine neue Voraussetzung.");
		buildNewVoraussetzung.setImageDescriptor(ControlIconsLibrary.add.getImageDescriptor());
		
		// Element löschen Action
		deleteSelected = new Action() {
			@Override
			public void run() {
				MessageDialog.openInformation(
						parentComp.getShell(),
						"TODO", "Noch zu implementieren");
			}};
		deleteSelected.setText("Entfernen");
		deleteSelected.setToolTipText("Entfernd das selektierte Element.");;
		deleteSelected.setImageDescriptor(ControlIconsLibrary.delete.getImageDescriptor());
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean isDirty(CharElement charElem) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void loadData(CharElement charElem) {
		// "Grund-Tree" aufbauen
		final TreeObject invisibleRoot = new TreeObject("invisibleRoot", null);

		TreeObject positivTree = new TreeObject("Positive Voraussetzungen",
				invisibleRoot);
		invisibleRoot.addChildren(positivTree);
		TreeObject negativTree = new TreeObject("Negative Voraussetzungen",
				invisibleRoot);
		invisibleRoot.addChildren(negativTree);

		treeViewer.setContentProvider(new TreeViewContentProvider(invisibleRoot));
		treeViewer.setInput(partSite);

		//
		if (charElem.getVoraussetzung() == null)
			return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void saveData(IProgressMonitor monitor, CharElement charElem) {
		// TODO Auto-generated method stub

	}
	
	private Option createOption(Class clazz) {
		Option option;
		
		if (clazz.equals(OptionVoraussetzung.class)) {
			option = new OptionVoraussetzung();
			((OptionVoraussetzung) option).setAbWert(0);
			option.setAnzahl(0);
		} else if (clazz.equals(OptionAnzahl.class)) {
			option = new OptionAnzahl();
			option.setAnzahl(0);
		} else if (clazz.equals(OptionListe.class)) {
			option = new OptionListe();
			option.setWerteListe(new int[0]);
		} else if (clazz.equals(OptionVerteilung.class)) {
			option = new OptionListe();
			option.setWert(1);
			option.setMax(0);
			option.setAnzahl(0);
		} else {
			throw new IllegalArgumentException("Parameter " + clazz + " konnte nicht gefunden werden!");
		}
		
		return option;
	}
	
	public static class VoraussetzungDialogCellEditor extends DialogCellEditor {

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
		 */
		@Override
		protected Object openDialogBox(Control cellEditorWindow) {
			VoraussetzungDialog vd = new VoraussetzungDialog(cellEditorWindow.getShell());
			vd.open();
			return null;
		}
		
	}
	
	public static class VoraussetzungDialog extends Dialog {

		public VoraussetzungDialog(IShellProvider parentShell) {
			super(parentShell);
		}

		public VoraussetzungDialog(Shell parentShell) {
			super(parentShell);
		}
		
		@Override
		protected Control createDialogArea(Composite parent) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			
			Composite container = new Composite(parent, SWT.NONE);
			container.setLayout(gridLayout);
			
			Label lblModul = new Label(container, SWT.NONE);
			lblModul.setText("Modus: ");
			Combo cobModus = new Combo(container, SWT.READ_ONLY | SWT.CENTER);
			
			Label lblAnzahl = new Label(container, SWT.NONE);
			lblAnzahl.setText("Anzahl: ");
			Spinner spiAnzahl = new Spinner (container, SWT.BORDER);
			spiAnzahl.setMinimum(0);
			spiAnzahl.setMaximum(100);
			spiAnzahl.setSelection(0);
			spiAnzahl.setIncrement(1);
			spiAnzahl.setPageIncrement(10);
			
			Label lblWert = new Label(container, SWT.NONE);
			lblWert.setText("Wert: ");
			Spinner spiWert = new Spinner (container, SWT.BORDER);
			spiWert.setMinimum(0);
			spiWert.setMaximum(100);
			spiWert.setSelection(0);
			spiWert.setIncrement(1);
			spiWert.setPageIncrement(10);
			
			Label lblMax = new Label(container, SWT.NONE);
			lblMax.setText("Maximal: ");
			Spinner spiMax = new Spinner (container, SWT.BORDER);
			spiMax.setMinimum(0);
			spiMax.setMaximum(100);
			spiMax.setSelection(0);
			spiMax.setIncrement(1);
			spiMax.setPageIncrement(10);
			
			Label lblWertList = new Label(container, SWT.BORDER);
			lblWertList.setText("Maximal: ");
			Text txtWertList = new Text(container, SWT.NONE);
			txtWertList.addListener (SWT.Verify, new Listener () {
				public void handleEvent (Event e) {
					String string = e.text;
					if (e.keyCode == SWT.DEL
							|| e.keyCode == SWT.TAB
							|| e.keyCode == SWT.BS) {
						e.doit = true;
						return;
					}
					
					if (string.matches("[0-9|,]")) {
						e.doit = true;
					} else {
						e.doit = false;
					}
					return;
				}
			});
			
			return container;
		}
		
	}

}
