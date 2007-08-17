/*
 * Created 27.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter;
import org.d3s.alricg.editor.common.CustomActions.InfoCharElementAction;
import org.d3s.alricg.editor.common.DragAndDropSupport.AuswahlDrag;
import org.d3s.alricg.editor.common.DragAndDropSupport.AuswahlDrop;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.ViewerSelectionListener;
import org.d3s.alricg.editor.editors.dialoge.CreateAuswahlDialog;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.charElemente.links.Option;
import org.d3s.alricg.store.charElemente.links.OptionAnzahl;
import org.d3s.alricg.store.charElemente.links.OptionListe;
import org.d3s.alricg.store.charElemente.links.OptionVerteilung;
import org.d3s.alricg.store.charElemente.links.OptionVoraussetzung;
import org.d3s.alricg.store.charElemente.links.Voraussetzung;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * Diese Klasse ermöglicht das Darstellen und bearbeiten von Voraussetzungen!
 * @author Vincent
 */
public class VoraussetzungPart extends AbstarctElementPart<CharElement> {
	private TreeViewer treeViewer;
	private Composite parentComp;
    private IWorkbenchPartSite partSite;
    private TreeObject invisibleRoot;
    private AuswahlDrop auswahlDrop;
    
	protected Action showInfos;
	protected Action buildNewAlternative;
	protected Action buildNewVoraussetzung;
	protected Action deleteSelected;
	protected Action editSelected;
	protected Action clearZweitZiel;
	
	private final static String POSITIVE_VORAUS = "Positive Voraussetzungen";
	private final static String NEGATIVE_VORAUS = "Negative Voraussetzungen";
	
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
		tc.getColumn().setWidth(250);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.OptionNameSorter(), treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 1);
		tc.getColumn().setText("Klasse");
		tc.getColumn().setWidth(100);
		tc.setLabelProvider(new CustomColumnLabelProvider.CharElementKlassenLabelProvider());
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.CharElementKlasseSorter(),
						treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 2);
		tc.getColumn().setText("Stufe");
		tc.setLabelProvider(new CustomColumnLabelProvider.LinkWertProvider());
		tc.getColumn().setWidth(100);
		tc.getColumn().setMoveable(true);
		tc.setEditingSupport(new EditingSupport(treeViewer) {
			private ComboBoxCellEditor cellEditor;
			private final static int MIN_WERT = -10;
			private final static int MAX_WERT = 20;
			
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
			 */
			@Override
			protected boolean canEdit(Object element) {
				return (((TreeObject) element).getValue() instanceof Link);
			}

			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
			 */
			@Override
			protected CellEditor getCellEditor(Object element) {
				if (cellEditor == null) {
					int toSub = -MIN_WERT;
					String[] strAr = new String[-MIN_WERT + MAX_WERT + 2];
					for (int i  = 0; i < strAr.length; i++) {
						if (i == (-MIN_WERT)+1) {
							toSub = (-MIN_WERT)+1;
							strAr[i] = "-";
							continue;
						}
						strAr[i] = Integer.toString(i - toSub);
					}
					
					cellEditor = new ComboBoxCellEditor(treeViewer.getTree(), strAr);
					((CCombo) cellEditor.getControl()).setVisibleItemCount(8);
				}
				
				int wert = ((Link) ((TreeObject) element).getValue()).getWert();
				if (wert == Link.KEIN_WERT) {
					cellEditor.setValue(11);
				} else if (wert < MIN_WERT || wert > MAX_WERT) {
					cellEditor.setValue(-MIN_WERT + 1);
				} else if (wert <= 0){
					cellEditor.setValue(-MIN_WERT + wert);
				} else {
					cellEditor.setValue(-MIN_WERT + 1 + wert);
				}
				
				return cellEditor;
			}

			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
			 */
			@Override
			protected Object getValue(Object element) {
				int wert = ((Link) ((TreeObject) element).getValue()).getWert();
				if (wert == Link.KEIN_WERT) {
					return -MIN_WERT + 1;
				} else if (wert < MIN_WERT || wert > MAX_WERT) {
					return 11;
				} else if (wert <= 0){
					return (-MIN_WERT + wert);
				} else {
					return (-MIN_WERT + 1 + wert);
				}
				
			}

			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
			 */
			@Override
			protected void setValue(Object element, Object value) {
				int valueInt = ((Integer) value).intValue();
				int wert = Link.KEIN_WERT;;
				
				if (valueInt <= -MIN_WERT){
					wert = valueInt  + MIN_WERT;
				} else if (valueInt > -MIN_WERT+1){
					wert = valueInt + MIN_WERT - 1;
				} else {
					wert = Link.KEIN_WERT;;
				}
				
				((Link)((TreeObject) element).getValue()).setWert(wert);
				treeViewer.refresh();
			}});

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 3);
		tc.getColumn().setText("Text");
		tc.setLabelProvider(new CustomColumnLabelProvider.LinkTextProvider());
		tc.getColumn().setWidth(100);
		tc.getColumn().setMoveable(true);
		tc.setEditingSupport(new EditingSupport(treeViewer) {
			final TextCellEditor tce = new TextCellEditor(treeViewer.getTree());
			
			@Override
			protected boolean canEdit(Object element) {
				return (((TreeObject) element).getValue() instanceof Link);
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return tce;
			}

			@Override
			protected Object getValue(Object element) {
				return ((Link) ((TreeObject) element).getValue()).getText();
			}

			@Override
			protected void setValue(Object element, Object value) {
				((Link) ((TreeObject) element).getValue()).setText(value.toString());
				treeViewer.refresh();
			}
			
		});

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 4);
		tc.getColumn().setText("ZweitZiel");
		tc.setLabelProvider(new CustomColumnLabelProvider.LinkZweitZielProvider());
		tc.getColumn().setWidth(100);
		tc.getColumn().setMoveable(true);
		auswahlDrop = new AuswahlDrop(treeViewer, tc.getColumn());
		treeViewer.getTree().addMouseMoveListener(auswahlDrop); // wichtig für Drag & Drop
		
		// Actions erstellen
		makeActions();
		hookContextMenu();
		
		// Unterstützung für DRAG
		treeViewer.addDragSupport(DND.DROP_COPY | DND.DROP_MOVE,
				new Transfer[] { LocalSelectionTransfer.getTransfer() },
				new AuswahlDrag(treeViewer));

		// Unterstützung für DROP
		int ops = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transfers = new Transfer[] { LocalSelectionTransfer
				.getTransfer() };
		treeViewer.addDropSupport(ops, transfers, auswahlDrop);
		// Funzt nur zusammen mit "auswahlDrop" als MouseMoveLister auf dem treeViewer
	}
	
	// Das Context Menu beim Rechts-klick
	private void fillContextMenu(IMenuManager manager) {
		manager.add(this.showInfos);
		manager.add(new Separator());
		manager.add(this.buildNewVoraussetzung);
		manager.add(this.buildNewAlternative);
		manager.add(this.editSelected);
		manager.add(this.deleteSelected);
		manager.add(this.clearZweitZiel);
		/* Um die Warnung
		 * Context menu missing standard group 'org.eclipse.ui.IWorkbenchActionConstants.MB_ADDITIONS'
		 * im Log zu verhindern */ 
		manager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
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
				final TreeObject treeObj = (TreeObject) getTree().getSelection()[0].getData();
				
				if (treeObj.getValue() instanceof Link) {
					buildNewAlternative.setEnabled(true);
					buildNewVoraussetzung.setEnabled(false);
					deleteSelected.setEnabled(true);
					editSelected.setEnabled(false);
					clearZweitZiel.setEnabled(true);
				} else if (treeObj.getValue() instanceof Option){
					buildNewAlternative.setEnabled(true);
					buildNewVoraussetzung.setEnabled(true);
					deleteSelected.setEnabled(true);
					editSelected.setEnabled(true);
					clearZweitZiel.setEnabled(false);
				} else {
					buildNewAlternative.setEnabled(false);
					buildNewVoraussetzung.setEnabled(true);
					deleteSelected.setEnabled(false);
					editSelected.setEnabled(false);
					clearZweitZiel.setEnabled(false);
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
				TreeObject treeObj = (TreeObject) getTree().getSelection()[0].getData();
				TreeObject parentTreeObj = null;
				
				if (treeObj.getValue() instanceof Link) {
					treeObj = treeObj.getParent();
				} 
				if (treeObj.getValue() instanceof Option) {
					parentTreeObj = treeObj;
					if (parentTreeObj.getParent().getValue() instanceof Option) {
						parentTreeObj = parentTreeObj.getParent();
					}
				}
				
				CreateAuswahlDialog vdce = 	new CreateAuswahlDialog(
						parentComp.getShell(), 
						getOptionClass().equals(OptionVoraussetzung.class), 
						createOption(getOptionClass()));
				
				if ( vdce.open() == Dialog.OK ){
					parentTreeObj.addChildren(
							new TreeObject(
									vdce.getOption(),
									parentTreeObj)
							);
					treeViewer.refresh();
				}
			}
			
			protected Class getOptionClass() {
				return OptionVoraussetzung.class;
			}		
		};
		buildNewAlternative.setText("Neue Alternative erzeugen");
		buildNewAlternative.setToolTipText("Erzeugt eine neue Alternativ-Voraussetzung.");
		buildNewAlternative.setImageDescriptor(ControlIconsLibrary.addFolder.getImageDescriptor());
		
		editSelected = new Action() {
			@Override
			public void run() {
				if (getTree().getSelection().length == 0) return;
				final TreeObject treeObj = (TreeObject) getTree().getSelection()[0].getData();
				
				CreateAuswahlDialog vdce = 	new CreateAuswahlDialog(
						parentComp.getShell(), 
						treeObj.getValue() instanceof OptionVoraussetzung, 
						(Option) treeObj.getValue());
				
				if ( vdce.open() == Dialog.OK ){
					treeObj.setValue(vdce.getOption());
					treeViewer.refresh();
				}
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
				
				final CreateAuswahlDialog vdce = new CreateAuswahlDialog(
						parentComp.getShell(), 
						getOptionClass().equals(OptionVoraussetzung.class), 
						createOption(getOptionClass()));
				
				if ( vdce.open() == Dialog.OK ) {
					parentTreeObj.addChildren(
							new TreeObject(
									vdce.getOption(),
									parentTreeObj)
							);
					treeViewer.refresh();
				}
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
				if (getTree().getSelection().length == 0) return;
				final TreeObject treeObj = (TreeObject) getTree().getSelection()[0].getData();
				
				treeObj.getParent().removeChildren(treeObj);
				treeViewer.refresh();
			}};
		deleteSelected.setText("Entfernen");
		deleteSelected.setToolTipText("Entfernd das selektierte Element.");;
		deleteSelected.setImageDescriptor(ControlIconsLibrary.delete.getImageDescriptor());
		
		clearZweitZiel = new Action() {
			@Override
			public void run() {
				if (getTree().getSelection().length == 0) return;
				final TreeObject treeObj = (TreeObject) getTree().getSelection()[0].getData();
				
				if (treeObj.getValue() instanceof Link) {
					((Link) treeObj.getValue()).setZweitZiel(null);
				}
			}};
		clearZweitZiel.setText("ZweitZiel löschen");
		clearZweitZiel.setToolTipText("Entfernd das ZweitZiel des selektierten Elements.");
		clearZweitZiel.setImageDescriptor(ControlIconsLibrary.tableDelete.getImageDescriptor());

	}


	/*
	 * (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		treeViewer.getTree().dispose();

	}

	/*
	 * (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean isDirty(CharElement charElem) {
		final Voraussetzung currentVor = buildVoraussetzung();
		
		if (charElem.getVoraussetzung() == null && currentVor == null) {
			return false;
		} else if (charElem.getVoraussetzung() != null && currentVor != null) {
			// Nope
		} else {
			return true;
		}
		
		boolean isEqual = true;
		isEqual &= compareOptionList(
				charElem.getVoraussetzung().getNegVoraussetzung(), 
				currentVor.getNegVoraussetzung());

		isEqual &= compareOptionList(
				charElem.getVoraussetzung().getPosVoraussetzung(), 
				currentVor.getPosVoraussetzung());
		
		return !isEqual;
	}
	
	/**
	 * Vergleicht zwei Listen von Optionen ob diese den werten nach gleich sind. 
	 * @param optListOrg
	 * @param optListNew
	 * @return true - Die Listen sind den Werten nach gleich, ansonsten false.
	 */
	private boolean compareOptionList(List<? extends Option> optListOrg, List<? extends Option> optListNew) {
		if (optListOrg == null && optListNew == null) {
			return true;
		} else if (optListOrg != null && optListNew != null) {
			// NOOP
		} else {
			return false;
		}
		
		if (optListOrg.size() != optListNew.size()) {
			return false;
		}
		
		boolean isEqual = true;
		for (int i = 0; i < optListOrg.size();i++) {
			isEqual &= compareOption(optListOrg.get(i), optListNew.get(i));
		}
		
		return isEqual;
	}
	
	/**
	 * Vergleicht ob zwei Optionen den Werten nach gleich sind
	 * @param option1
	 * @param option2
	 * @return true - option1 und option2 sind den Werten nach gleich, ansonsten false
	 */
	private boolean compareOption(Option option1, Option option2) {
		boolean isEqual = true;
		
		// Generelle Eigenschaften prüfen
		if (option1.getClass() != option2.getClass()) {
			return false;
		} 
		if (option1.getLinkList() != null && option2.getLinkList() != null) {
			if (option1.getLinkList().size() != option2.getLinkList().size()) {
				return false;
			}
		} else if (option1.getLinkList() == null && option2.getLinkList() == null) {
			//  Noop
		} else {
			return false;
		}
		
		// Option Eigenschaften prüfen
		if (option1 instanceof OptionVoraussetzung) {
			isEqual &= option1.getAnzahl() == option2.getAnzahl();
			isEqual &= option1.getWert() == option2.getWert();
			
		} else if (option1 instanceof OptionAnzahl) {
			isEqual &= option1.getAnzahl() == option2.getAnzahl();
			
		} else if (option1 instanceof OptionListe) {
			isEqual &= Arrays.equals(option1.getWerteListe(), option2.getWerteListe());
			
		} else if (option1 instanceof OptionVerteilung) {
			isEqual &= option1.getAnzahl() == option2.getAnzahl();
			isEqual &= option1.getWert() == option2.getWert();
			isEqual &= option1.getMax() == option2.getMax();
		}
		
		// Links prüfen 
		if (option1.getLinkList() != null) { 
			for (int i = 0; i < option1.getLinkList().size(); i++) {
				isEqual &= ((Link) option1.getLinkList().get(i))
					.isEqualLink((Link) option2.getLinkList().get(i));
			}
		}
		
		// Falls schon falsch, können die alternativen optionen gespart werden!
		if (!isEqual) return isEqual;
		
		// Alternative Optionen prüfen
		if (option1.getAlternativOption() != null && 
				option2.getAlternativOption() != null ) {
			isEqual &= compareOption(
					option1.getAlternativOption(), 
					option2.getAlternativOption());
		} else if (option1.getAlternativOption() == null && 
					option2.getAlternativOption() == null ) {
			// Noop
		} else {
			return false;
		}

		return isEqual;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void loadData(CharElement charElem) {
		// "Grund-Tree" aufbauen
		invisibleRoot = new TreeObject("invisibleRoot", null);

		TreeObject positivTree = new TreeObject(POSITIVE_VORAUS, invisibleRoot);
		invisibleRoot.addChildren(positivTree);
		TreeObject negativTree = new TreeObject(NEGATIVE_VORAUS, invisibleRoot);
		invisibleRoot.addChildren(negativTree);

		treeViewer.setContentProvider(new TreeViewContentProvider(invisibleRoot));
		treeViewer.setInput(partSite);
		
		// Lade Daten
		if (charElem.getVoraussetzung() == null) return;
		
		// Lade Voraussetzungen
		if (charElem.getVoraussetzung().getPosVoraussetzung() != null) {
			loadAuswahlList(charElem.getVoraussetzung().getPosVoraussetzung(), positivTree);
		}
		
		// Lade Nachteile
		if (charElem.getVoraussetzung().getNegVoraussetzung() != null) {
			loadAuswahlList(charElem.getVoraussetzung().getNegVoraussetzung(), negativTree);
		}
		
		auswahlDrop.setQuelle(charElem); // Setzt die Quelle für alle neuen Links in der Auswahl
		treeViewer.refresh();
	}

	private void loadAuswahlList(List<? extends Option> auswahlList, TreeObject root) {
		for (int i1 = 0; i1 < auswahlList.size(); i1++) {
			loadOption(auswahlList.get(i1), root);
		}
	}
	
	private void loadOption(Option originalOption, TreeObject root) {
		if (originalOption == null) return;
		
		// Option erstellen
		Option currentOpt = null;
		if (originalOption instanceof OptionVoraussetzung) {
			currentOpt = new OptionVoraussetzung();
			currentOpt.setAnzahl( originalOption.getAnzahl() );
			currentOpt.setWert( originalOption.getWert() );
			
		} else if (originalOption instanceof OptionAnzahl) {
			currentOpt = new OptionAnzahl();
			currentOpt.setAnzahl( originalOption.getAnzahl() );
			
		} else if (originalOption instanceof OptionListe) {
			currentOpt = new OptionListe();
			currentOpt.setWerteListe( originalOption.getWerteListe() );
			
		} else if (originalOption instanceof OptionVerteilung) {
			currentOpt = new OptionVerteilung();
			currentOpt.setAnzahl( originalOption.getAnzahl() );
			currentOpt.setWert( originalOption.getWert() );
			currentOpt.setWert( originalOption.getMax() );
		}
		TreeObject treeObj = new TreeObject(currentOpt, root);
		root.addChildren(treeObj);
		
		// CharElemente setzen
		for (int i2 = 0; i2 < originalOption.getLinkList().size(); i2++) {
			TreeObject linkTreeObj = new TreeObject(
					((IdLink) originalOption.getLinkList().get(i2)).copyLink(), 
					treeObj);
			treeObj.addChildren(linkTreeObj);
			linkTreeObj.setParent(treeObj);
		}
		
		if (originalOption.getAlternativOption() != null) {
			if (root.getValue() instanceof Option) treeObj = root;
			loadOption(originalOption.getAlternativOption(), treeObj);
		}
	}
	
	/**
	 * Erstellt aus dem aktuellen Tree eine Voraussetzung 
	 * @return Die Voraussetzung, die dem aktuellen Tree entspricht.
	 */
	private Voraussetzung buildVoraussetzung() {
		TreeObject root = invisibleRoot;
		
		final Voraussetzung voraus = new Voraussetzung();
		
		// Die beiden Kinder "positiv" und "negativ"
		for (int i1 = 0; i1 < root.getChildren().length; i1++) {
			final List<OptionVoraussetzung> optList = new ArrayList<OptionVoraussetzung>();
			
			// Entweder die Kinder von "positiv" oder "negativ"
			if (root.getChildren()[i1].getChildren() == null) continue;
			for (int i2 = 0; i2 < root.getChildren()[i1].getChildren().length; i2++) {
				final Option tmpOp = buildAuswahl(root.getChildren()[i1].getChildren()[i2]);
				if (tmpOp != null) {
					optList.add( (OptionVoraussetzung) tmpOp );
				}
			}
			
			if (optList.size() == 0) continue;
			
			if (root.getChildren()[i1].getValue().equals(POSITIVE_VORAUS)) {
				voraus.setPosVoraussetzung(optList);
			} else {
				voraus.setNegVoraussetzung(optList);
			}
		}
		
		if (voraus.getPosVoraussetzung() != null || voraus.getNegVoraussetzung() != null) {
			return voraus;
		} else {
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void saveData(IProgressMonitor monitor, CharElement charElem) {
		charElem.setVoraussetzung(buildVoraussetzung());
	}
	
	private Option buildAuswahl(TreeObject root) {
		final Option option = ((Option) root.getValue()).copyOption();
		option.setLinkList(new ArrayList());
		
		if (root.getChildren() == null) {
			return validOption(option);
		}
		
		for (int i = 0; i < root.getChildren().length; i++) {
			// Links hinzufügen
			if (root.getChildren()[i].getValue() instanceof Link) {
				option.getLinkList().add((Link) root.getChildren()[i].getValue());
			
			// Alternative Optionen hinzufügen
			} else if (root.getChildren()[i].getValue() instanceof Option) {
				// Um die Option richtig hinzuzufügen
				Option optToAdd = option;
				while (optToAdd.getAlternativOption() != null) {
					optToAdd = optToAdd.getAlternativOption();
				}
				// Um die CharElement hinzuzufügen
				optToAdd.setAlternativOption(buildAuswahl(root.getChildren()[i]));
			}
		}
		
		return validOption(option);
	}
	
	private Option validOption(Option option) {
		
		if ( option.getLinkList() == null || option.getLinkList().size() == 0 ) {
			if( option.getAlternativOption() != null ) {
				return validOption(option.getAlternativOption());
			} else {
				return null;
			}
		}
		
		// Setze die Werte auf korrekte Werte
		if (option.getClass() == OptionVoraussetzung.class) {
			if (option.getLinkList().size() <= option.getAnzahl()) {
				option.setAnzahl(0);
			}
			
		} else if (option.getClass() == OptionAnzahl.class) {
			if (option.getLinkList().size() <= option.getAnzahl()) {
				option.setAnzahl(0);
			}
			
		} else if (option.getClass() == OptionListe.class) {
			if (option.getWerteListe() == null || option.getWerteListe().length == 0) {
				// TODO den User auf den Fehler Hinweisen?
				//isSensless = true;
			}
			
		} else if (option.getClass() == OptionVerteilung.class) {
			if (option.getLinkList().size() <= option.getAnzahl()) {
				option.setAnzahl(0);
			}
			if (option.getWert() <= option.getMax()) {
				option.setMax(0);
			}
		}
		
		return option;
	}
	
	public static Option createOption(Class clazz) {
		Option option;
		
		if (clazz.equals(OptionVoraussetzung.class)) {
			option = new OptionVoraussetzung();
			((OptionVoraussetzung) option).setWert(0);
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
		

}
