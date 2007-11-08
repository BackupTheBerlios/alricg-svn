/*
 * Created 25.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

import java.util.ArrayList;
import java.util.List;

import org.d3s.alricg.common.icons.AbstractIconsLibrary;
import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.d3s.alricg.common.icons.MagieIconsLibrary;
import org.d3s.alricg.editor.common.CustomColumnEditors.WertEditingSupport;
import org.d3s.alricg.editor.common.CustomColumnLabelProvider.ImageProviderRegulator;
import org.d3s.alricg.editor.common.DragAndDropSupport.SelectDrop;
import org.d3s.alricg.editor.common.ViewUtils.CharElementDragSourceListener;
import org.d3s.alricg.editor.common.ViewUtils.TableObject;
import org.d3s.alricg.editor.common.ViewUtils.TableViewContentProvider;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.common.widgets.DropTable;
import org.d3s.alricg.editor.common.widgets.DropTable.DropListRegulator;
import org.d3s.alricg.editor.editors.composits.AbstractElementPart;
import org.d3s.alricg.editor.editors.composits.FaehigkeitPart;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.MagieMerkmal;
import org.d3s.alricg.store.charElemente.Repraesentation;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.charElemente.Zauber.Verbreitung;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * @author Vincent
 *
 */
public class ZauberEditor extends ComposedMultiPageEditorPart {
	public static final String ID = "org.d3s.alricg.editor.editors.ZauberEditor"; //$NON-NLS-1$
	
	private FaehigkeitPart faehigkeitPart;
	private ZauberPart zauberPart;
	
	private AbstractElementPart[] elementPartArray;
	
	class ZauberPart extends AbstractElementPart<Zauber> {
		private DropTable dropListMerkmal;
		private VerbreitungDropTable dropTableVerbreitung;
		
		ZauberPart(Composite top) {
			GridData tmpGData;
			
		// MagieMerkmal
			Label label1 = new Label(top, SWT.NONE);
			label1.setText("Merkmale:");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			tmpGData.verticalIndent = 10;
			label1.setLayoutData(tmpGData);
			
			DropListRegulator regulator = new DropListRegulator() {
				@Override
				public boolean canDrop(Object obj) {
					if (obj == null) return false;
					if (obj instanceof MagieMerkmal) {
						return true;
					}
					return false;
				}

				@Override
				public ImageProviderRegulator getImageProviderRegulator() {
					return new ImageProviderRegulator<MagieMerkmal>() {
						public String getName(MagieMerkmal obj) {
							return obj.getName();
						}
						public MagieMerkmal[] getItems(CharElement obj) {
							return new MagieMerkmal[] { (MagieMerkmal) obj };
						}
						public AbstractIconsLibrary<MagieMerkmal> getIconsLibrary() {
							return MagieIconsLibrary.getInstance();
						}
						public IWorkbenchPart getConsumer() {
							return ZauberEditor.this;
						}
				};
				}
			};
			
			dropListMerkmal = new DropTable(top, SWT.NONE, regulator, ZauberEditor.this.getSite());
			
		// Verbreitung
			Label label2 = new Label(top, SWT.NONE);
			label2.setText("Verbreitung:");
			tmpGData = new GridData(); 
			tmpGData.verticalAlignment = GridData.BEGINNING;
			tmpGData.verticalIndent = 10;
			label2.setLayoutData(tmpGData);
			
			regulator = new DropListRegulator() {
				@Override
				public boolean canDrop(Object obj) {
					if (obj == null) return false;
					if (obj instanceof Repraesentation) {
						return !((Repraesentation) obj).isSchamanischeRep();
					}
					return false;
				}

				@Override
				public ImageProviderRegulator getImageProviderRegulator() {
					return null;
				}
			};
			
			dropTableVerbreitung = new VerbreitungDropTable(top, SWT.NONE, regulator, ZauberEditor.this.getSite());
		}
		
		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#dispose()
		 */
		@Override
		public void dispose() {
			dropListMerkmal.dispose();
			dropTableVerbreitung.dispose();
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public boolean isDirty(Zauber charElem) {
			boolean isNotDirty = true;
			
			// Prüfe Merkmale
			isNotDirty &= compareArrayList(charElem.getMerkmale(), dropListMerkmal.getValueList());
			
			// Prüfe verbreitung
			if (charElem.getVerbreitung() == null 
					&& dropTableVerbreitung.getValueList().size() == 0) {
				// Noop
			} else if (charElem.getVerbreitung() == null) {
				return true;
			} else {
				if (charElem.getVerbreitung().length != dropTableVerbreitung.getValueList().size()) {
					return true;
				}
				
				final List l = dropTableVerbreitung.getValueList();
				for (int i = 0; i < charElem.getVerbreitung().length; i++) {
					Verbreitung vOrg = charElem.getVerbreitung()[i];
					Verbreitung v2 = (Verbreitung) l.get(i);
					
					isNotDirty &= vOrg.getWert() == v2.getWert();
					isNotDirty &= vOrg.getBekanntBei().equals(v2.getBekanntBei());
					if (vOrg.getRepraesentation() == null) {
						isNotDirty &= v2.getRepraesentation() == null;
					} else {
						isNotDirty &= vOrg.getRepraesentation().equals(v2.getRepraesentation());
					}
				}
			}
			
			return !isNotDirty;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void loadData(Zauber charElem) {
			if (charElem.getMerkmale() != null) {
				for (int i = 0; i < charElem.getMerkmale().length; i++) {
					dropListMerkmal.addValue(charElem.getMerkmale()[i]);
				}
			}
			
			if (charElem.getVerbreitung() != null) {
				for (int i = 0; i < charElem.getVerbreitung().length; i++) {
					final Verbreitung v = new Verbreitung();
					v.setBekanntBei(charElem.getVerbreitung()[i].getBekanntBei());
					v.setRepraesentation(charElem.getVerbreitung()[i].getRepraesentation());
					v.setWert(charElem.getVerbreitung()[i].getWert());
					
					dropTableVerbreitung.addValue(v);
				}
			}
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
		 */
		@Override
		public void saveData(IProgressMonitor monitor, Zauber charElem) {
			monitor.subTask("Save Zauber-Data"); //$NON-NLS-1$
			
			// Merkmale setzen
			MagieMerkmal[] m;
			if (dropListMerkmal.getValueList().size() == 0) {
				m = null;
			} else {
				final List l = dropListMerkmal.getValueList();
				m = new MagieMerkmal[l.size()];
				for (int i = 0; i < l.size(); i++) {
					m[i] = (MagieMerkmal) l.get(i);
				}
			}
			charElem.setMerkmale(m);
			
			// Verbreitung setzen
			Verbreitung[] v;
			if (dropTableVerbreitung.getValueList().size() == 0) {
				v = null;
			} else {
				final List l = dropTableVerbreitung.getValueList();
				v = new Verbreitung[l.size()];
				for (int i = 0; i < l.size(); i++) {
					v[i] = (Verbreitung) l.get(i);
				}
			}
			charElem.setVerbreitung(v);
			
			monitor.worked(1);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#addCharElementSiteParts(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addCharElementSiteParts(Composite mainContainer) {
		// FaehigkeitPart erzeugen
		faehigkeitPart = new FaehigkeitPart(mainContainer);
		faehigkeitPart.loadData((Zauber) getEditedCharElement()); 
		
		// Talent spezifische Elemte erzeugen
		zauberPart = this.new ZauberPart(mainContainer);
		zauberPart.loadData((Zauber) getEditedCharElement());

	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		int index = addPage(createCharElementSite());
		setPageText(index, EditorMessages.Editor_Daten);
		
		index = addPage(createVoraussetzungsSite());
		setPageText(index, EditorMessages.Editor_Voraussetzungen);

		elementPartArray = new AbstractElementPart[] {
				charElementPart, faehigkeitPart, zauberPart, voraussetzungsPart};

	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getEditedCharElement()
	 */
	@Override
	protected CharElement getEditedCharElement() {
		return (Zauber) this.getEditorInput().getAdapter(Zauber.class);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.ComposedMultiPageEditorPart#getElementParts()
	 */
	@Override
	protected AbstractElementPart[] getElementParts() {
		return elementPartArray;
	}
	

	/**
	 * Spezielle DropTable um die Verbreitung bearbeiten zu können
	 * @author Vincent
	 */
	static class VerbreitungDropTable extends DropTable {
		protected Action clearRep; // Entfernd den "Repräsentation" Eintrag
		
		public VerbreitungDropTable(Composite parent, int style, DropListRegulator regulator, IWorkbenchPartSite site) {
			super(parent, style, regulator, site);
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.common.widgets.DropTable#createTable(org.eclipse.swt.widgets.Composite)
		 */
		@Override
		protected Table createTable(Composite parent) {
			TableColumn dropColumn;
			
			// init Table
			tableViewer = new TableViewer(parent,
					SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
			tableViewer.getTable().setLinesVisible(true);
			tableViewer.getTable().setHeaderVisible(true);
			ColumnViewerToolTipSupport.enableFor(tableViewer, ToolTip.NO_RECREATE);
			
			// Drag and Drop
			tableViewer.addDragSupport(
					DND.DROP_COPY | DND.DROP_MOVE, 
					new Transfer[] { LocalSelectionTransfer.getTransfer() }, 
					new CharElementDragSourceListener(tableViewer));
			
			TableViewerColumn tc;
			int colIdx = 0;
			
			// Columns setzen
			tc = new TableViewerColumn(tableViewer, SWT.LEFT, colIdx++);
			tc.getColumn().setText("Bekannt bei");
			tc.getColumn().setToolTipText("Diese Repräsentationen kennen den Zauber");
			tc.setLabelProvider(new VerbreitungBekanntBeiProvider());
			tc.getColumn().setWidth(100);
			tc.getColumn().setMoveable(true);

			tc = new TableViewerColumn(tableViewer, SWT.LEFT, colIdx++);
			tc.getColumn().setText("Rep.");
			tc.getColumn().setToolTipText("In dieser Repräsentation ist der Zauber bekannt");
			tc.setLabelProvider(new VerbreitungRepraesentationProvider());
			tc.getColumn().setWidth(50);
			tc.getColumn().setMoveable(true);
			dropColumn = tc.getColumn(); //für Drop
			
			tc = new TableViewerColumn(tableViewer, SWT.LEFT, colIdx++);
			tc.getColumn().setText("Verb.");
			tc.getColumn().setToolTipText("Verbreitung, Bekanntheit des Zaubers");
			tc.setLabelProvider(new VerbreitungWertProvider());
			tc.getColumn().setWidth(50);
			tc.getColumn().setMoveable(true);
			tc.setEditingSupport(new WertEditingSupport(tableViewer, tableViewer.getTable(), 1, 7) {

				/* (non-Javadoc)
				 * @see org.d3s.alricg.editor.common.CustomColumnEditors.WertEditingSupport#getValueForObj(java.lang.Object)
				 */
				@Override
				protected int getValueForObj(TreeOrTableObject element) {
					return ((Verbreitung) element.getValue()).getWert();
				}

				/* (non-Javadoc)
				 * @see org.d3s.alricg.editor.common.CustomColumnEditors.WertEditingSupport#setValueToObj(int, java.lang.Object)
				 */
				@Override
				protected void setValueToObj(int value, TreeOrTableObject element) {
					((Verbreitung) element.getValue()).setWert(value);
				}
				
			});

			// Inhalt setzen
			tableViewer.setContentProvider(new TableViewContentProvider());
			tableViewer.setInput(new ArrayList());

			final SelectDrop selectDrop = new SelectDrop(tableViewer, new Item[] { dropColumn }) {
				@Override
				protected void dropToColumn(int colIndex, TreeOrTableObject sourceObj, TreeOrTableObject targetObj) {
					Verbreitung verb =((Verbreitung) targetObj.getValue());
					
					// Nur wenn nicht gleich "bekanntBei"
					if (verb.getBekanntBei().equals((Repraesentation) sourceObj.getValue())) {
						return;
					}
					
					// Setzen
					verb.setRepraesentation((Repraesentation) sourceObj.getValue());
				}

				@Override
				protected boolean dropToTreeTable(TreeOrTableObject sourceObj, TreeOrTableObject targetObj) {
					// Build new Verbreitung and add
					Verbreitung verb = new Verbreitung();
					verb.setBekanntBei((Repraesentation) sourceObj.getValue());
					verb.setWert(7);
					
					VerbreitungDropTable.this.addValue(verb);
					
					return true;
				}
			};
			selectDrop.setAcceptGlobalDropClazz(new Class[] {Repraesentation.class});
			selectDrop.addAcceptColumnClazz(Repraesentation.class, dropColumn);
			
			
			tableViewer.getTable().addMouseMoveListener(selectDrop); // wichtig für Drag & Drop
			
			// Unterstützung für DROP
			int ops = DND.DROP_COPY | DND.DROP_MOVE;
			Transfer[] transfers = new Transfer[] { LocalSelectionTransfer
					.getTransfer() };
			tableViewer.addDropSupport(ops, transfers, selectDrop);
			
			return tableViewer.getTable();
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.common.widgets.DropTable#fillContextMenu(org.eclipse.jface.action.IMenuManager)
		 */
		@Override
		protected void fillContextMenu(IMenuManager manager) {
			manager.add(this.deleteElement);
			manager.add(new Separator());
			manager.add(this.upElement);
			manager.add(this.downElement);
			manager.add(new Separator());
			manager.add(this.clearRep);
			/* Um die Warnung
			 * Context menu missing standard group 'org.eclipse.ui.IWorkbenchActionConstants.MB_ADDITIONS'
			 * im Log zu verhindern */ 
			manager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		}

		protected void hookContextMenu() {
			MenuManager menuMgr = new MenuManager("#PopupMenu");
			menuMgr.setRemoveAllWhenShown(true);
			menuMgr.addMenuListener(new IMenuListener() {
				public void menuAboutToShow(IMenuManager manager) {
					VerbreitungDropTable.this.fillContextMenu(manager);
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
					
					if (isEnabled) {
						TableObject obj = (TableObject) ((IStructuredSelection) tableViewer.getSelection())
													.getFirstElement();
						
						clearRep.setEnabled(((Verbreitung) obj.getValue()).getRepraesentation() != null);
					}
				}
			});

			// For Table
			Menu menu = menuMgr.createContextMenu(tableViewer.getControl());
			tableViewer.getControl().setMenu(menu);
			site.registerContextMenu(menuMgr, tableViewer);
		}
		
		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.common.widgets.DropTable#makeActions()
		 */
		@Override
		protected void makeActions() {
			super.makeActions();
			
			clearRep = new Action() {
				@Override
				public void run() {
					TableObject obj = (TableObject) ((IStructuredSelection) tableViewer.getSelection())
														.getFirstElement();
					
					((Verbreitung) obj.getValue()).setRepraesentation(null);
					tableViewer.update(obj, null);
				}};
			clearRep.setText("Entferne Repräsentation");
			clearRep.setImageDescriptor(ControlIconsLibrary.tableDelete.getImageDescriptor());
		}
		
		// Für den Text in die Spalte "BekanntBei"
		public static class VerbreitungBekanntBeiProvider extends ColumnLabelProvider {
			@Override
			public String getText(Object element) {
				return ((Verbreitung) ((TableObject) element).getValue()).getBekanntBei().getName();
			}
		}
		
		// Für den Text in die Spalte "Repraesentation"
		public static class VerbreitungRepraesentationProvider extends ColumnLabelProvider {
			@Override
			public String getText(Object element) {
				Repraesentation rep = ((Verbreitung) ((TableObject) element).getValue()).getRepraesentation();
				if (rep == null) {
					return "-";
				} 
				return "(" + rep.getAbkuerzung() + ")";
			}
		}
		
		// Für den Text in der Spalte "Verbreitung"
		public static class VerbreitungWertProvider extends ColumnLabelProvider {
			@Override
			public String getText(Object element) {
				return Integer.toString( ((Verbreitung) ((TableObject) element).getValue()).getWert() );
			}
		}
		
	}
	

}
