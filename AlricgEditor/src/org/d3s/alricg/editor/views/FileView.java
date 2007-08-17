package org.d3s.alricg.editor.views;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.common.CustomActions.BuildNewCharElementAction;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.store.access.StoreAccessor;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class FileView extends ViewPart {
	public static final String ID = "org.d3s.alricg.editor.views.FileView";
	
	private TreeViewer viewer;
	
	private Image markForAddImage;
	
	private Action infos;
	private Action deleteFile;
	private Action createNewFile;
	private Action renameFile;
	private Action markForAdd;
	
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	
	class ViewLabelProvider extends LabelProvider {

		@Override
		public String getText(Object obj) {
			return ((File) ((TreeObject) obj).getValue()).getName();
		}
		
		@Override
		public Image getImage(Object obj) {
			if ( ((TreeObject) obj).getValue() == ViewModel.getMarkedFileForNew()) {
				return markForAddImage;
			}
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (((TreeObject) obj).getChildren() != null)
			   imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}

	/**
	 * The constructor.
	 */
	public FileView() {
	}

	
	private void helper(HashMap<String, TreeObject> fileHash, File tmpFile) {
		TreeObject parentTreeObj;
		
		if (!fileHash.containsKey(tmpFile.getParentFile().getAbsolutePath())) {
			helper(fileHash, tmpFile.getParentFile());
		}
		parentTreeObj = fileHash.get(tmpFile.getParentFile().getAbsolutePath());
		
		final TreeObject treeObj = new TreeObject(
				tmpFile, 
				parentTreeObj);
		parentTreeObj.addChildren(treeObj);
		fileHash.put(tmpFile.getAbsolutePath(), treeObj);
	}
	
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		
		// -----------------------
		final TreeObject invisibleRoot = new TreeObject("invisibleRoot", null);
		final List<XmlAccessor> accessorList = StoreDataAccessor.getInstance().getXmlAccessors();
		final HashMap<String, TreeObject> fileHash = new HashMap<String, TreeObject>();
		
		TreeObject treeObj = new TreeObject(
					new File(StoreAccessor.getInstance().getOriginalFilesPath()),
					invisibleRoot);
		invisibleRoot.addChildren(treeObj);
		fileHash.put(StoreAccessor.getInstance().getOriginalFilesPath(), treeObj);
		
		treeObj = new TreeObject(
					new File(StoreAccessor.getInstance().getUserFilesPath()),
					invisibleRoot);
		invisibleRoot.addChildren(treeObj);
		fileHash.put(StoreAccessor.getInstance().getUserFilesPath(), treeObj);
		
		for (int i = 0; i < accessorList.size(); i++) {
			helper(fileHash, accessorList.get(i).getFile());
		}
		// ------------------------
		markForAddImage = ControlIconsLibrary.setPageAsAdd.getImageDescriptor().createImage();
		
		viewer.setContentProvider(new TreeViewContentProvider(invisibleRoot));
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new ViewerSorter());
		viewer.setInput(getViewSite());
		
		getSite().setSelectionProvider(viewer);
		
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				FileView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		menuMgr.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if ( viewer.getSelection().isEmpty()) {
					infos.setEnabled(false);
					deleteFile.setEnabled(false);
					createNewFile.setEnabled(false);
					renameFile.setEnabled(false);
					markForAdd.setEnabled(false);
					return;
				}
				
				infos.setEnabled(true);
				createNewFile.setEnabled(true);
				if (viewer.getTree().getSelection()[0].getParent().getParent() == null) {
					// "user" oder "orginal" --> Alles Deaktivieren
					markForAdd.setEnabled(false);
					deleteFile.setEnabled(false);
					renameFile.setEnabled(false);
					
				} else if (	((File) ((TreeObject) viewer.getTree().getSelection()[0].getData())
															.getValue()).isDirectory()) {
					markForAdd.setEnabled(false);
					deleteFile.setEnabled(true);
					renameFile.setEnabled(true);
					
				} else {
					markForAdd.setEnabled(true);
					deleteFile.setEnabled(true);
					renameFile.setEnabled(true);
				}
			}
		});		
		
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {

	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(this.infos);
		manager.add(new Separator());
		manager.add(markForAdd);
		manager.add(new Separator());		
		manager.add(this.createNewFile);
		manager.add(this.renameFile);
		manager.add(this.deleteFile);
		/* Um die Warnung
		 * Context menu missing standard group 'org.eclipse.ui.IWorkbenchActionConstants.MB_ADDITIONS'
		 * im Log zu verhindern */
		manager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		
		infos = new Action() {
			public void run() {
				showMessage("Noch zu Implementieren");
			}};
		infos.setText("Informationen");
		infos.setToolTipText("Zeigt Informationen über das selektiere File an");
		infos.setImageDescriptor(ControlIconsLibrary.info.getImageDescriptor());
		
		deleteFile = new Action() {
			public void run() {
				showMessage("Noch zu Implementieren");
			}};
		deleteFile.setText("Datei löschen");
		deleteFile.setToolTipText("Löscht die selektierte Datei");
		deleteFile.setImageDescriptor(ControlIconsLibrary.delete.getImageDescriptor());
		
		createNewFile = new Action() {
			public void run() {
				showMessage("Noch zu Implementieren");
			}};
		createNewFile.setText("Neue Datei erstellen");
		createNewFile.setToolTipText("Erzeugt eine neue Quell-Datei für CharElemente");
		createNewFile.setImageDescriptor(ControlIconsLibrary.add.getImageDescriptor());
		
		renameFile = new Action() {
			public void run() {
				showMessage("Noch zu Implementieren");
			}};
		renameFile.setText("Umbenennen");
		renameFile.setToolTipText("Benennt die Datei um");
		renameFile.setImageDescriptor(ControlIconsLibrary.renamePage.getImageDescriptor());
		
		markForAdd = new Action() {
			public void run() {
				if (viewer.getSelection().isEmpty()) return;
				
				final File f = (File) ((TreeObject) viewer.getTree()
						.getSelection()[0].getData()).getValue();
				
				if (f.isFile()) {
					ViewModel.setMarkedFileForNew(f);
					viewer.refresh();
				}
			}};
		markForAdd.setText("Neue Elemente hier erstellen");
		markForAdd.setToolTipText("Neue CharElemente werden in der Datei erzeugt, welches markiert ist");
		markForAdd.setImageDescriptor(ControlIconsLibrary.setPageAsAdd.getImageDescriptor());
		
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				renameFile.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"File View",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		markForAddImage.dispose();
	}
	
	
}