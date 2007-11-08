/*
 * Created 02.09.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import org.d3s.alricg.editor.common.CustomColumnLabelProvider;
import org.d3s.alricg.editor.common.CustomColumnViewerSorter;
import org.d3s.alricg.editor.common.CustomColumnEditors.LinkTextEditingSupport;
import org.d3s.alricg.editor.common.CustomColumnEditors.LinkWertEditingSupport;
import org.d3s.alricg.editor.common.DragAndDropSupport.AuswahlDrag;
import org.d3s.alricg.editor.common.DragAndDropSupport.AuswahlDrop;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.ViewerSelectionListener;
import org.d3s.alricg.editor.editors.EditorMessages;
import org.d3s.alricg.editor.utils.EditorViewUtils.AuswahlTreeObject;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Option;
import org.d3s.alricg.store.charElemente.links.OptionListe;
import org.d3s.alricg.store.charElemente.links.OptionVerteilung;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * @author Vincent
 */
public class IdLinkArrayPart extends AbstractElementPart<Herkunft> {
	protected TreeViewer treeViewer;
	protected Composite parentComp;
	protected AuswahlDrop auswahlDrop;
	
	protected TreeObject invisibleRoot;
	private HerkunftIdLinkRegulator regulator;
	
	public IdLinkArrayPart(Composite top, IWorkbenchPartSite partSite, HerkunftIdLinkRegulator regulator) {
		parentComp = top;
		this.regulator = regulator;
		
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
		tc.setEditingSupport(new LinkWertEditingSupport(treeViewer, treeViewer.getTree(), -10, 20, true) {

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
		auswahlDrop = new AuswahlDrop(treeViewer, tc.getColumn(), null);
		treeViewer.getTree().addMouseMoveListener(auswahlDrop); // wichtig für Drag & Drop
		
		// Sortierung setzen
		treeViewer.setSorter(new CustomColumnViewerSorter.NameSorter());
		
		// Actions erstellen
		//makeActions();
		//hookContextMenu(partSite);
		
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

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#dispose()
	 */
	@Override
	public void dispose() {
		// Noop
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean isDirty(Herkunft herkunft) {
		for (int i = 0; i < invisibleRoot.getChildren().length; i++) {
			final IdLink[] linkOld = regulator.getLinkArray(
							herkunft, 
							((AuswahlTreeObject) invisibleRoot.getChildren()[i]).getIndex());
			final IdLink[] linkNew =   buildIdLinkArray((AuswahlTreeObject) invisibleRoot.getChildren()[i]);
			if ( !compareIdLinkArrays(linkOld, linkNew) ) return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void loadData(Herkunft herkunft) {
		// "Grund-Tree" aufbauen
		invisibleRoot = new TreeObject("invisibleRoot", null);
		
		// Setzt die Quelle für alle neuen Links in der Auswahl
		auswahlDrop.setQuelle(herkunft); 
		// Setzt die CharElemente, die in der Auswahl als LinkZiel stehen dürfen
		auswahlDrop.setAcceptGlobalDropClazz(regulator.getCharElementClazz());

		// Erzeugt die einzelnen Nodes für Talente, Voraussetzungen usw.
		for (int i = 0; i < regulator.getCharElementClazz().length; i++) {
			AuswahlTreeObject treeObj = new AuswahlTreeObject(
					regulator.getCharElementClazz()[i], 
					invisibleRoot,
					regulator.getCharElementText()[i],
					i);
			invisibleRoot.addChildren(treeObj);
			
			if (regulator.getLinkArray(herkunft, i) == null) {
				continue;
			}
			loadIdLinkArray(
					regulator.getLinkArray(herkunft, i),
					treeObj);
		}
		
		treeViewer.setContentProvider(new TreeViewContentProvider(invisibleRoot));
		treeViewer.setInput(invisibleRoot);
		treeViewer.refresh();
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void saveData(IProgressMonitor monitor, Herkunft herkunft) {
		for (int i = 0; i < invisibleRoot.getChildren().length; i++) {
			regulator.setLinkArray(
					herkunft, 
					buildIdLinkArray((AuswahlTreeObject) invisibleRoot.getChildren()[i]), 
					((AuswahlTreeObject) invisibleRoot.getChildren()[i]).getIndex());
		}
	}
	
	/**
	 * Läd alle Einträge eines IdLink Arrays zu einem Node
	 * @param linkArray  Links die hinzugefügt werden sollen
	 * @param root Node zum hinzufügen der IdLinks
	 */
	private void loadIdLinkArray(IdLink[] linkArray, TreeObject root) {
		if (linkArray == null) return;
		
		// Link setzen
		for (int i = 0; i < linkArray.length; i++) {
			TreeObject linkTreeObj = new TreeObject(
					linkArray[i].copyLink(), 
					root);
			root.addChildren(linkTreeObj);
		}
	}
	
	/**
	 * Liest von einem Node alle IdLinks aus und liefert diese als Array zurück
	 * @param root Node zum lesen
	 * @return Array von IdLinks oder "null" falls es keine IdLinks gibt
	 */
	private IdLink[] buildIdLinkArray(AuswahlTreeObject root) {
		if (root == null 
				|| root.getChildren() == null
				|| root.getChildren().length == 0) return null;
		final IdLink[] linkArray = new IdLink[root.getChildren().length];
		
		for (int i = 0; i < root.getChildren().length; i++) {
			linkArray[i] = ((IdLink) root.getChildren()[i].getValue()).copyLink();
		}
		
		return linkArray;
	}

	/**
	 * Vergleicht zwei Arrays von IdLinks
	 * @param linkArray1
	 * @param linkArray2
	 * @return true - beide Arrays sind gleich, ansonsten false
	 */
	private boolean compareIdLinkArrays(IdLink[] linkArray1, IdLink[] linkArray2) {
		if (linkArray1 == null && linkArray2 == null) {
			return true;
		} else if (linkArray1 == null || linkArray2 == null) {
			return false;
		} 
		if (linkArray1.length != linkArray2.length) {
			return false;
		}
		
		for (int i = 0; i < linkArray1.length; i++) {
			if (!linkArray1[i].isEqualLink(linkArray2[i])) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Über dieses Interface werden alle Daten angegeben, die nötig sind um
	 * verschiedene Auswahlen einer Herkunft (für Talente, Zauber usw.) per 
	 * "AuswahlPart" zu Editieren
	 * @author Vincent
	 */
	public static interface HerkunftIdLinkRegulator {
		
		/**
		 * Liefert das IdLink-Array von "herkunft" mit dem index "index"
		 * @param herkunft Die Herkunft, die editiert wird
		 * @param Der Index des zu liefernden IdLink-Arrays. Der Index ist identisch
		 * 		mit dem Index des Elements von getCharElementClazz & getCharElementText
		 * @return Der IdLink mit dem index "index"
		 */
		public abstract IdLink[] getLinkArray(Herkunft herkunft, int index);
		
		/**
		 * Trägt das IdLink-Array in die "herkunft" an der Stelle "index" ein. 
		 * @param herkunft Die Herkunft, die editiert wird
		 * @param array Das Array, welches eingetragen werden soll
		 * @param index Der Index des arrays, gibt an wo in der Herkunft das array 
		 * 		eingetragen werden soll. Der Index ist identisch mit dem Index des
		 * 		Elements von getCharElementClazz & getCharElementText
		 */
		public abstract void setLinkArray(Herkunft herkunft, IdLink[] array, int index);
		
		/**
		 * Gibt an welche Klassen angezeigt werden sollen. Für jede Klasse wird
		 * im Tree ein "ordner" angelegt. Es können in diesen Ordner nur
		 * Elemente vom entsprechenden Typ hinzugefügt werden.
		 * Der Text der Ordner wird mit "getCharElementText()" angegeben. 
		 * Zugehörigkeiten werden über den Index identifiziert.
		 * @return Die Klasse für welche Ordner erstellt werden sollen.
		 */
		public abstract Class[] getCharElementClazz();
		
		/**
		 * Gibt den Tex an, der auf den Ordner von "getCharElementClazz()" 
		 * stehen soll. Zugehörigkeiten werden über den Index identifiziert.
		 * @return Der Anteigetext für die Knoten
		 */
		public abstract String[] getCharElementText();
		
	}
}
