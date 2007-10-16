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
		private final int MIN_WERT;
		private final int MAX_WERT;
		
		/**
		 * @param viewer Zugehöriger Viewer
		 * @param parent zugehöriger Tree oder Table
		 * @param minWert Dieser Wert muss negativ sein!
		 * @param maxWert Dieser Wert muss positiv sein!
		 */
		public LinkWertEditingSupport(ColumnViewer viewer, Composite parent, int minWert, int maxWert) {
			super(viewer);
			this.viewer = viewer;
			MIN_WERT = minWert;
			MAX_WERT = maxWert;
			
			// Build Editor
			int toSub = -MIN_WERT;
			String[] strAr = new String[-MIN_WERT + MAX_WERT + 2];
			for (int i  = 0; i < strAr.length; i++) {
				if (i == (-MIN_WERT)+1) {
					toSub = (-MIN_WERT)+1;
					strAr[i] = "-"; //$NON-NLS-1$
					continue;
				}
				strAr[i] = Integer.toString(i - toSub);
			}
			
			cellEditor = new ComboBoxCellEditor(parent, strAr);
			((CCombo) cellEditor.getControl()).setVisibleItemCount(8);
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
			
			/*
			int wert = ((Link) ((TreeOrTableObject) element).getValue()).getWert();
			if (wert == Link.KEIN_WERT) {
				cellEditor.setValue(11);
			} else if (wert < MIN_WERT || wert > MAX_WERT) {
				cellEditor.setValue(-MIN_WERT + 1);
			} else if (wert <= 0){
				cellEditor.setValue(-MIN_WERT + wert);
			} else {
				cellEditor.setValue(-MIN_WERT + 1 + wert);
			}*/
			
			cellEditor.setValue(getValue(element));
			return cellEditor;
		}

		@Override
		protected Object getValue(Object element) {
			int wert = ((Link) ((TreeOrTableObject) element).getValue()).getWert();
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
			
			((Link)((TreeOrTableObject) element).getValue()).setWert(wert);
			viewer.update(element, null);
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
