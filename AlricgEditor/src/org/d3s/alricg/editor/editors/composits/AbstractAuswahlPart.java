/*
 * Created 17.08.2007
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
import org.d3s.alricg.editor.common.CustomColumnEditors.LinkTextEditingSupport;
import org.d3s.alricg.editor.common.CustomColumnEditors.LinkWertEditingSupport;
import org.d3s.alricg.editor.common.DragAndDropSupport.AuswahlDrag;
import org.d3s.alricg.editor.common.DragAndDropSupport.AuswahlDrop;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.ViewerSelectionListener;
import org.d3s.alricg.editor.editors.EditorMessages;
import org.d3s.alricg.editor.editors.dialoge.CreateAuswahlDialog;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.charElemente.links.Option;
import org.d3s.alricg.store.charElemente.links.OptionAnzahl;
import org.d3s.alricg.store.charElemente.links.OptionListe;
import org.d3s.alricg.store.charElemente.links.OptionVerteilung;
import org.d3s.alricg.store.charElemente.links.OptionVoraussetzung;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * Superklasse für alle Auswahlen
 * @author Vincent
 */
public abstract class AbstractAuswahlPart<C extends CharElement> extends AbstractElementPart<C> {
	protected TreeViewer treeViewer;
	protected Composite parentComp;
	protected AuswahlDrop auswahlDrop;
	
	protected Action showInfos;
	protected Action buildNewAlternative;
	protected Action buildNewAuswahl;
	protected Action deleteSelected;
	protected Action editSelected;
	protected Action clearZweitZiel;
	
	/**
	 * Erstellt die TreeTable
	 * @param top Parent Composite
	 * @param partSite 
	 */
	public AbstractAuswahlPart(Composite top, IWorkbenchPartSite partSite) {
		parentComp = top;
		
		treeViewer = new TreeViewer(parentComp, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setHeaderVisible(true);
		ColumnViewerToolTipSupport.enableFor(treeViewer, ToolTip.NO_RECREATE);

		TreeViewerColumn tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 0);
		tc.getColumn().setText(EditorMessages.AbstractAuswahlPart_Name);
		treeViewer.getTree().setSortColumn(tc.getColumn());
		tc.setLabelProvider(new CustomColumnLabelProvider.OptionNameProvider());
		tc.getColumn().setWidth(250);
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.OptionNameSorter(), treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 1);
		tc.getColumn().setText(EditorMessages.AbstractAuswahlPart_Klasse);
		tc.getColumn().setWidth(100);
		tc.setLabelProvider(new CustomColumnLabelProvider.CharElementKlassenProvider());
		tc.getColumn().addSelectionListener(
				new ViewerSelectionListener(
						new CustomColumnViewerSorter.CharElementKlasseSorter(),
						treeViewer));
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 2);
		tc.getColumn().setText(EditorMessages.AbstractAuswahlPart_Stufe);
		tc.setLabelProvider(new CustomColumnLabelProvider.LinkWertProvider());
		tc.getColumn().setWidth(100);
		tc.getColumn().setMoveable(true);
		tc.setEditingSupport(new LinkWertEditingSupport(treeViewer, treeViewer.getTree(), -10, 20) {

			@Override
			protected boolean canEdit(Object element) {
				if (!super.canEdit(element)) return false;
				
				// Nur die Modi "Anzahl" und "Voraussetzung" benötigen Stufe für Links
				final Option tmpOpt = (Option) ((TreeObject) element).getParent().getValue();
				if( tmpOpt instanceof OptionListe 
						|| tmpOpt instanceof OptionVerteilung ) {
					return false;
				}
				return true;
			}
		});

		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 3);
		tc.getColumn().setText(EditorMessages.AbstractAuswahlPart_Text);
		tc.setLabelProvider(new CustomColumnLabelProvider.LinkTextProvider());
		tc.getColumn().setWidth(100);
		tc.getColumn().setMoveable(true);
		tc.setEditingSupport(new LinkTextEditingSupport(treeViewer, treeViewer.getTree()));
		
		
		tc = new TreeViewerColumn(treeViewer, SWT.LEFT, 4);
		tc.getColumn().setText(EditorMessages.AbstractAuswahlPart_ZweitZiel);
		tc.setLabelProvider(new CustomColumnLabelProvider.LinkZweitZielProvider());
		tc.getColumn().setWidth(100);
		tc.getColumn().setMoveable(true);
		auswahlDrop = new AuswahlDrop(treeViewer, tc.getColumn(), this.getOptionClass());
		treeViewer.getTree().addMouseMoveListener(auswahlDrop); // wichtig für Drag & Drop
		
		// Sortierung setzen
		treeViewer.setSorter(new CustomColumnViewerSorter.NameSorter());
		
		// Actions erstellen
		makeActions();
		hookContextMenu(partSite);
		
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
	
	public Tree getTree() {
		return treeViewer.getTree();
	}
	
	// Das Context Menu beim Rechts-klick
	private void fillContextMenu(IMenuManager manager) {
		manager.add(this.showInfos);
		manager.add(new Separator());
		manager.add(this.buildNewAuswahl);
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
	private void hookContextMenu(IWorkbenchPartSite partSite) {
		
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				AbstractAuswahlPart.this.fillContextMenu(manager);
			}
		});

		
		menuMgr.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				
				if (getTree().getSelection().length == 0) return;
				final TreeObject treeObj = (TreeObject) getTree().getSelection()[0].getData();
				
				if (treeObj.getValue() instanceof Link) {
					buildNewAlternative.setEnabled(true);
					buildNewAuswahl.setEnabled(false);
					deleteSelected.setEnabled(true);
					editSelected.setEnabled(false);
					clearZweitZiel.setEnabled(
							((Link) treeObj.getValue()).getZweitZiel() != null);
				} else if (treeObj.getValue() instanceof Option){
					buildNewAlternative.setEnabled(true);
					buildNewAuswahl.setEnabled(true);
					deleteSelected.setEnabled(true);
					editSelected.setEnabled(true);
					clearZweitZiel.setEnabled(false);
				} else {
					buildNewAlternative.setEnabled(false);
					buildNewAuswahl.setEnabled(true);
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
						"TODO", "Noch zu implementieren"); //$NON-NLS-1$ //$NON-NLS-2$
			}};
		showInfos.setText(EditorMessages.AbstractAuswahlPart_Infos);
		
		showInfos.setToolTipText(EditorMessages.AbstractAuswahlPart_Infos_TT);
		showInfos.setImageDescriptor(ControlIconsLibrary.info.getImageDescriptor());

		// Neues Element Action 
		buildNewAlternative = new Action() {
			@Override
			public void run() {
				if (treeViewer.getTree().getSelection().length == 0) return;
				TreeObject treeObj = (TreeObject) treeViewer.getTree().getSelection()[0].getData();
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
		};
		buildNewAlternative.setText(EditorMessages.AbstractAuswahlPart_NewAlternative);
		buildNewAlternative.setToolTipText(EditorMessages.AbstractAuswahlPart_NewAlternative_TT);
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
		editSelected.setText(EditorMessages.AbstractAuswahlPart_EditAuswahk);
		editSelected.setToolTipText(EditorMessages.AbstractAuswahlPart_EditAuswahl_TT);
		editSelected.setImageDescriptor(ControlIconsLibrary.edit.getImageDescriptor());

		
		// Neues Element Action 
		buildNewAuswahl =  new Action() {
			@Override
			public void run() {
				if (treeViewer.getTree().getSelection().length == 0) return;
				final TreeObject treeObj = (TreeObject) treeViewer.getTree().getSelection()[0].getData();
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
			
			private TreeObject findNodeToAdd(TreeObject treeObj) {
				
				if (treeObj.getParent() == null) {
					return treeObj;
				}
				
				while (!(treeObj.getParent().getParent() == null)) {
					treeObj = treeObj.getParent();
				}
				
				return treeObj;
			}

		};
		buildNewAuswahl.setText(EditorMessages.AbstractAuswahlPart_NewAuswahl);
		buildNewAuswahl.setToolTipText(EditorMessages.AbstractAuswahlPart_NewAuswahl_TT);
		buildNewAuswahl.setImageDescriptor(ControlIconsLibrary.add.getImageDescriptor());
		
		// Element löschen Action
		deleteSelected = new Action() {
			@Override
			public void run() {
				if (getTree().getSelection().length == 0) return;
				final TreeObject treeObj = (TreeObject) getTree().getSelection()[0].getData();
				
				treeObj.getParent().removeChildren(treeObj);
				treeViewer.refresh();
			}};
		deleteSelected.setText(EditorMessages.AbstractAuswahlPart_Delete);
		deleteSelected.setToolTipText(EditorMessages.AbstractAuswahlPart_Delete_TT);;
		deleteSelected.setImageDescriptor(ControlIconsLibrary.delete.getImageDescriptor());
		
		clearZweitZiel = new Action() {
			@Override
			public void run() {
				if (getTree().getSelection().length == 0) return;
				final TreeObject treeObj = (TreeObject) getTree().getSelection()[0].getData();
				
				if (treeObj.getValue() instanceof Link) {
					((Link) treeObj.getValue()).setZweitZiel(null);
				}
				treeViewer.refresh();
			}};
		clearZweitZiel.setText(EditorMessages.AbstractAuswahlPart_DeleteZweitZiel);
		clearZweitZiel.setToolTipText(EditorMessages.AbstractAuswahlPart_DeleteZweitZiel_TT);
		clearZweitZiel.setImageDescriptor(ControlIconsLibrary.tableDelete.getImageDescriptor());

	}

	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Erzeugt eine neue Option der Klasse "clazz"
	 * @param clazz Klasse der zu erzeugenden Option
	 * @return Neu erzeugte Option
	 */
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
			throw new IllegalArgumentException("Parameter " + clazz + " konnte nicht gefunden werden!"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		return option;
	}

	/**
	 * Erzeugt aus dem Tree eine neue Auswahl (Option)
	 * @param root Der Knoten, ab dem eine Option erzeugt werden soll
	 * @return Die Option für den Knoten "root"
	 */
	protected Option buildAuswahl(TreeObject root) {
		final Option option = ((Option) root.getValue()).copyOption();
		option.setLinkList(new ArrayList());
		
		if (root.getChildren() == null) {
			return validOption(option);
		}
		
		for (int i = 0; i < root.getChildren().length; i++) {
			// Links hinzufügen
			if (root.getChildren()[i].getValue() instanceof Link) {
				option.getLinkList().add(
						(Link) root.getChildren()[i].getValue()
					);
			
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
	
	/**
	 * Läd eine Liste zu Optionen in die Ansicht
	 * @param auswahlList
	 * @param root
	 */
	protected void loadAuswahlList(List<? extends Option> auswahlList, TreeObject root) {
		for (int i1 = 0; i1 < auswahlList.size(); i1++) {
			loadOption(auswahlList.get(i1), root);
		}
	}
	
	/**
	 * Läd eine Option "originalOption" zum TreeObject "root"
	 * @param originalOption Die Option, die geladen wird
	 * @param root Das TreeObject, zu dem die Option als Kind hinzugefühgt wird
	 */
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
			currentOpt.setMax( originalOption.getMax() );
		}
		TreeObject treeObj = new TreeObject(currentOpt, root);
		root.addChildren(treeObj);
		
		// Link setzen
		for (int i2 = 0; i2 < originalOption.getLinkList().size(); i2++) {
			TreeObject linkTreeObj = new TreeObject(
					((IdLink) originalOption.getLinkList().get(i2)).copyLink(), 
					treeObj);
			treeObj.addChildren(linkTreeObj);
		}
		
		if (originalOption.getAlternativOption() != null) {
			if (root.getValue() instanceof Option) treeObj = root;
			loadOption(originalOption.getAlternativOption(), treeObj);
		}
	}

	/**
	 * Vergleicht zwei Listen von Optionen ob diese den werten nach gleich sind. 
	 * @param optListOrg
	 * @param optListNew
	 * @return true - Die Listen sind den Werten nach gleich, ansonsten false.
	 */
	protected boolean compareOptionList(List<? extends Option> optListOrg, List<? extends Option> optListNew) {
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
	
	
	/**
	 * Prüft und optimiert ggf. eine Option. Überflüssige Werde und Optionen
	 * ohne Links werden "weg-optimiert"
	 * @param option Zu optimierende Option
	 * @return Die Optimierte Option oder null, falls diese überflüssig ist!
	 */
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

	/**
	 * Definiert den Typ der Option, der standartmäßig vorgeschlagen wird
	 * @return Classe der Default Option
	 */
	protected Class getOptionClass() {
		return OptionAnzahl.class;
	}
	
}
