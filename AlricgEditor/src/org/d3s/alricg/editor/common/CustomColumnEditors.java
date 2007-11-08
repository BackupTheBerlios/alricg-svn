/*
 * Created 29.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common;

import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.store.charElemente.links.Link;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Vincent
 */
public class CustomColumnEditors {

	/**
	 * Editor für das allgemeine setzen von Werten in Trees/Tables
	 * @author Vincent
	 */
	public abstract static class WertEditingSupport extends EditingSupport {
		private final ColumnViewer viewer;
		private ComboBoxCellEditor cellEditor;
		private final int MIN_WERT;
		private final int MAX_WERT;
		
		/**
		 * @param viewer Zugehöriger Viewer
		 * @param parent zugehöriger Tree oder Table
		 * @param minWert Beliebiger Wert
		 * @param maxWert Beliebiger positiver Wert
		 */
		public WertEditingSupport(ColumnViewer viewer, Composite parent, int minWert, int maxWert) {
			super(viewer);
			this.viewer = viewer;
			MIN_WERT = minWert;
			MAX_WERT = maxWert;
			
			// Build Editor
			String[] strAr;
			if (MIN_WERT <= 0) {
				strAr = new String[Math.abs(MIN_WERT) + MAX_WERT + 1];
			} else {
				strAr = new String[MAX_WERT - MIN_WERT + 1];
			}
			for (int i  = 0; i < strAr.length; i++) {
				strAr[i] = Integer.toString(MIN_WERT + i);
			}
			
			cellEditor = new ComboBoxCellEditor(parent, strAr);
			if (strAr.length < 8) {
				((CCombo) cellEditor.getControl()).setVisibleItemCount(strAr.length);
			} else {
				((CCombo) cellEditor.getControl()).setVisibleItemCount(8);
			}
		}
	
		@Override
		protected boolean canEdit(Object element) {
			return true;
		}
		
		@Override
		protected CellEditor getCellEditor(Object element) {
			cellEditor.setValue(getValue(element));
			return cellEditor;
		}
		
		@Override
		protected Object getValue(Object element) {
			return getValueForObj((TreeOrTableObject) element) - MIN_WERT;
		}
		
		@Override
		protected void setValue(Object element, Object value) {
			int valueInt = ((Integer) value).intValue();
			int wert = valueInt + MIN_WERT;
			
			setValueToObj(wert, (TreeOrTableObject) element);
			
			viewer.update(element, null);
		}
		
		/**
		 * Setzt in dem Object "element" den Wert "value"
		 * @param value Wert zu setzen
		 * @param element Object wo der Wert gesetzt werden soll
		 */
		protected abstract void setValueToObj(int value, TreeOrTableObject element);
		
		/**
		 * Ließt den Wert aus dem Object "element"
		 * @param element Object zum lesen des Wertes
		 * @return Wert in Objekt "element"
		 */
		protected abstract int getValueForObj(TreeOrTableObject element);
	}
	
	
	/**
	 * Zum Editieren eines Wertes im Link ber ComboBox. Das besondere
	 * an diesem Editor ist, das es auch einen "kein Wert" Eintrag gibt.
	 * @author Vincent
	 */
	public static class LinkWertEditingSupport extends EditingSupport {
		private final ColumnViewer viewer;
		private ComboBoxCellEditor cellEditor;
		private int MIN_WERT;
		private int MAX_WERT;
		private final boolean withNoValue;
		

		/**
		 * @param viewer Zugehöriger Viewer
		 * @param parent zugehöriger Tree oder Table
		 * @param minWert Minimal auswählbarer Wert
		 * @param maxWert Maximal auswählbare Wert
		 * @param withNoValue true Es gibt auch einen "-" (Kein Wert) Eintrag, ansonsten false
		 */
		public LinkWertEditingSupport(ColumnViewer viewer, Composite parent, int minWert, int maxWert, boolean withNoValue) {
			super(viewer);
			this.viewer = viewer;
			this.withNoValue = withNoValue;

			cellEditor = new ComboBoxCellEditor(parent, new String[0]);
			((CCombo) cellEditor.getControl()).setVisibleItemCount(8);
			((CCombo) cellEditor.getControl()).addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					System.out.println("widgetDefaultSelected");
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					System.out.println("widgetSelected");
				}});
			
			
			changeMinMax(minWert, maxWert);
		}
		
		protected void changeMinMax(int minWert, int maxWert) {
			MIN_WERT = minWert;
			MAX_WERT = maxWert;
			
			int toSub = MIN_WERT;
			int arrayCount = -MIN_WERT + MAX_WERT + 1;
			if (withNoValue) arrayCount++; // einer mehr für "-" (kein Wert)
			
			String[] strAr = new String[arrayCount];
			for (int i  = 0; i < strAr.length; i++) {
				if (withNoValue) {
					if (MIN_WERT >= 0 && i == 0) {
						toSub = MIN_WERT+1;
						strAr[i] = "-"; //$NON-NLS-1$
						continue;
					} else if (MIN_WERT < 0 && i == (-MIN_WERT)) {
						toSub = MIN_WERT-1;
						strAr[i] = "-"; //$NON-NLS-1$
						continue;
					}
				}
				strAr[i] = Integer.toString(i + toSub);
			}
			
			cellEditor.setItems(strAr);
		}
		
		@Override
		protected boolean canEdit(Object element) {
			// Nur Links haben eine Stufe und können bearbeitet werden
			if ( !(((TreeOrTableObject) element).getValue() instanceof Link) ) {
				return false;
			}
			return true;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			cellEditor.setValue(getValue(element));
			return cellEditor;
		}

		@Override
		protected Object getValue(Object element) {
			int wert = ((Link) ((TreeOrTableObject) element).getValue()).getWert();
			
			if (withNoValue) {
				if (wert == Link.KEIN_WERT &&
						MIN_WERT < 0) {
					return -MIN_WERT;
				} else if (wert == Link.KEIN_WERT) {
					return 0;
				} else if (wert > 0){
					return (1 + wert - MIN_WERT);
				}
			}
			return (wert - MIN_WERT);
		}

		@Override
		protected void setValue(Object element, Object value) {

			((Link)((TreeOrTableObject) element).getValue()).setWert(
					getWertForSet(value)
			);
			viewer.cancelEditing();
			viewer.update(element, null);
		}
		
		
		
		/**
		 * Bestimmt den neuen Wert, der für das setzen des Wertes ausgewählt wurde
		 * @param value Objekt aus dem Array der ComboBox, welche Gewählt wurde
		 * @return Zugehöriger Zahlenwert
		 */
		protected int getWertForSet(Object value) {
			int valueInt = ((Integer) value).intValue();
			int wert;
			
			wert = valueInt + MIN_WERT;
			if (withNoValue) {
				if (MIN_WERT >= 0) {
					if (valueInt == 0) {
						wert = Link.KEIN_WERT;
					} else {
						wert--;
					}
				} else {
					if (wert == 0) {
						wert = Link.KEIN_WERT;
					} else if (wert > 0) {
						wert--;
					}
				}
			}
			
			return wert;
		}
	}
	
	/**
	 * Zum Editieren des Textes in einem Link.
	 * @author Vincent
	 */
	public static class LinkTextEditingSupport extends EditingSupport {
		private final TextCellEditor tce;
		private final ColumnViewer viewer;
		
		public LinkTextEditingSupport(ColumnViewer viewer, Composite parent) {
			super(viewer);
			this.viewer = viewer;
			tce = new TextCellEditor(parent);
		}
		
		@Override
		protected boolean canEdit(Object element) {
			return (((TreeOrTableObject) element).getValue() instanceof Link);
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return tce;
		}

		@Override
		protected Object getValue(Object element) {
			return ((Link) ((TreeOrTableObject) element).getValue()).getText();
		}

		@Override
		protected void setValue(Object element, Object value) {
			((Link) ((TreeOrTableObject) element).getValue()).setText(value.toString());
			viewer.update(element, null);
		}
	}
	
}
