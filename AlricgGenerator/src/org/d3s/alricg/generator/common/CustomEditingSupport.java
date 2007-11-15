/*
 * Created 10.11.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.common;

import org.d3s.alricg.common.logic.Prozessor;
import org.d3s.alricg.editor.common.CustomColumnEditors.LinkWertEditingSupport;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.generator.views.dialogs.TalentSpeziDialog;
import org.d3s.alricg.store.charElemente.links.Link;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Vincent
 */
public class CustomEditingSupport {
	
	/**
	 * Editing Support für Link-Werte die über einen Prozessor verwaltet werden
	 */
	public static class LinkWertProzessorEditingSupport extends LinkWertEditingSupport {
		private final Prozessor prozessor;
		
		public LinkWertProzessorEditingSupport(ColumnViewer viewer,
				Composite parent, boolean withNoValue, Prozessor prozessor) {
			super(viewer, parent, 0, 1, withNoValue);
			this.prozessor = prozessor;
		}
		
		@Override
		protected boolean canEdit(Object element) {
			if (!super.canEdit(element)) return false;
			
			this.changeMinMax(
					prozessor.getMinWert((Link) ((TreeObject) element).getValue()), 
					prozessor.getMaxWert((Link) ((TreeObject) element).getValue()));
			
			return true;
		}

		/* (non-Javadoc)
		 * @see org.d3s.alricg.editor.common.CustomColumnEditors.LinkWertEditingSupport#setValue(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected void setWertInElement(Object element, int wert) {
			prozessor.updateWert(
					(Link) ((TreeObject) element).getValue(), 
					wert);
		}
	}
	
	/**
	 * Editing Support für die Spezialisierung von Talenten
	 */
	public static class TalentSpezialisierungsEditor extends EditingSupport {
		private final DialogCellEditor dialogCellEditor;
		
		public TalentSpezialisierungsEditor(final ColumnViewer viewer, final Prozessor prozessor) {
			super(viewer);
			
			final Composite parent;
			
			if (viewer instanceof TreeViewer) {
				parent = ((TreeViewer) viewer).getTree();
			} else {
				parent = ((TableViewer) viewer).getTable();
			}
			
			dialogCellEditor = new DialogCellEditor(parent) {
				private TalentSpeziDialog talentSpeziDialog;
				private Link link;
				
				@Override
				protected Object openDialogBox(Control cellEditorWindow) {
					talentSpeziDialog = new TalentSpeziDialog(
							parent.getShell(), 
							link);
					
					if ( talentSpeziDialog.open() == Dialog.OK) {
						setSelf(talentSpeziDialog.getResult());
					}
					this.deactivate();
					
					return talentSpeziDialog;
				}
				

				/* (non-Javadoc)
				 * @see org.eclipse.jface.viewers.DialogCellEditor#doSetValue(java.lang.Object)
				 */
				@Override
				protected void doSetValue(Object value) {
					this.link = (Link) value;
					super.doSetValue( link.getText() );
				}


				/* (non-Javadoc)
				 * @see org.eclipse.jface.viewers.DialogCellEditor#doGetValue()
				 */
				@Override
				protected Object doGetValue() {
					if (talentSpeziDialog == null) return "";
					return talentSpeziDialog.getResult();
				}
				
				/**
				 * Setzt den Wert in den Prozesser
				 * @param value Wert der gesetzt werden soll
				 */
				private void setSelf(String value) {
					if (value == null) {
						return;
					} if (value.length() == 0) {
						// Kein Inhalt
						if (link.getText() != null) {
							prozessor.updateText(
								link, 
								null);
						}
						return;
					} else if ( value.equals(link.getText()) ) {
						// gleich geblieben, nix zu tun
						return;
					}
					
					// Setzen des neuen Textes
					prozessor.updateText(
							link, 
							value);
				}
			};
		}
			
		@Override
		protected boolean canEdit(Object element) {
			return ((TreeOrTableObject) element).getValue() instanceof Link;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return dialogCellEditor;
		}

		@Override
		protected Object getValue(Object element) {
			return (Link) ((TreeOrTableObject) element).getValue();
		}
		
		@Override
		protected void setValue(Object element, Object value) {
			// Noop
		}
	}

}
