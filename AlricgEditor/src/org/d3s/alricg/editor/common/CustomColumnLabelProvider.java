/*
 * Created 20.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common;

import org.d3s.alricg.editor.common.ViewUtils.TableObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTableObject;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTreeObject;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Faehigkeit;
import org.d3s.alricg.store.charElemente.links.Link;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * @author Vincent
 *
 */
public class CustomColumnLabelProvider {

	/**
	 * Liefert zu einem TreeObject/TableObject mit CharElement oder Link,
	 * oder einem  CharElement oder Link direkt das zugehörige CharElement. 
	 * Gibt es KEIN zugehöriges CharElement, so wird "null" zurückgeliefert.
	 * 
	 * @param element TreeObject/TableObject/CharElement oder Link
	 * @return Das zugehörige CharElement oder "null" wenn keines existiert
	 */
	protected static CharElement getCharElement(Object element) {
		
		if (element instanceof TreeObject) {
			element = ((TreeObject)element).getValue();
		} else if (element instanceof TableObject) {
			element = ((TableObject)element).getValue();
		}
		
		if (element instanceof Link) {
			return ((Link) element).getZiel();
		} else if (element instanceof CharElement) {
			return (CharElement) element;
		}
		
		return null;
	}
	
	/**
	 * Zeigt den Namen eines CharElement an. Get für CharElemente, TreeObjects und Links.
	 * @author Vincent
	 */
	public static class NameLabelProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			CharElement chatEle = CustomColumnLabelProvider.getCharElement(element);
			if (chatEle == null) return ((TreeObject)element).getValue().toString();
			
			return chatEle.getName();
		}
		@Override
		public Image getImage(Object obj) {
			if (obj instanceof TreeObject) {
				String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
				
				if ( ((TreeObject)obj).getChildren() != null)
				   imageKey = ISharedImages.IMG_OBJ_FOLDER;
				return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
			}
			return super.getImage(obj);
		}
	}
	
	/**
	 * Zeigt den "3EigenschaftenString" einer Faehigkeit an + ausgeschriebenen ToolTip
	 * @author Vincent
	 */
	public static class EigenschaftLabelProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			CharElement chatEle = CustomColumnLabelProvider.getCharElement(element);
			if (chatEle == null) return "";
			
			return ((Faehigkeit) chatEle).get3EigenschaftenString();
		}
		@Override
		public String getToolTipText(Object element) {
			CharElement charElem = CustomColumnLabelProvider.getCharElement(element);
			if (charElem == null) super.getToolTipText(element);
			
			StringBuilder builder = new StringBuilder();
			Eigenschaft[] eigenschaftArray = ((Faehigkeit) charElem).getDreiEigenschaften();
			
			builder.append("(");
			for (int i = 0; i < eigenschaftArray.length; i++) {
				builder.append(eigenschaftArray[i].getName());
				if ((i+1) < eigenschaftArray.length) {
					builder.append("/");
				}
			}
			builder.append(")");
			return builder.toString();
		}
	}
	
	/**
	 * Ziegt den Dateinamen an + den vollen Pfad als ToolTip
	 * @author Vincent
	 */
	public static class DateinameLabelProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof EditorTreeObject) {
				return ((EditorTreeObject) element).getFile().getName();
			} else if(element instanceof EditorTableObject) {
				return ((EditorTableObject) element).getFile().getName();
			}
			return "";
		}
		@Override
		public String getToolTipText(Object element) {
			if (element instanceof EditorTreeObject) {
				return ((EditorTreeObject) element).getFile().getAbsolutePath();
			} else if(element instanceof EditorTableObject) {
				return ((EditorTableObject) element).getFile().getAbsolutePath();
			}
			return super.getToolTipText(element);
		}
	}
	
	/**
	 * Zeigt die SKT + ToolTip
	 * @author Vincent
	 */
	public static class SKTLabelProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = CustomColumnLabelProvider.getCharElement(element);
			if (charElem == null) return "";

			return ((Faehigkeit) charElem).getKostenKlasse().toString();
		}
		
		@Override
		public String getToolTipText(Object element) {
			final CharElement charElem = CustomColumnLabelProvider.getCharElement(element);
			if (charElem == null)  super.getToolTipText(element);
			
			StringBuilder builder = new StringBuilder();
			builder.append("Spalte in der Steigerungskostentabelle: ")
				.append(
						((Faehigkeit) charElem).getKostenKlasse().toString());
			return builder.toString();
		}
	}
	
	/**
	 * Zeigt den Wert eines Links. Wenn wert = KEIN_WERT, wird "-" angezeigt
	 * @author Vincent
	 */
	public static class LinkWertProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof Link) {
				if ( ((Link) element).getWert() == Link.KEIN_WERT) {
					return "-";
				}
				return Integer.toString( ((Link) element).getWert() );
			}
			return "";
		}
	}
	
	/**
	 * Zeigt den Text eines Links.
	 * @author Vincent
	 */
	public static class LinkTextProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof Link) {
				if ( ((Link) element).getText() != null) {
					return ((Link) element).getText();
				}
			}
			return "";
		}
	}
	
	/**
	 * Zeigt das ZweitZiel eines Links. Wenn keines vorhanden, wird "-" angezeigt.
	 * @author Vincent
	 */
	public static class LinkZweitZielProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof Link) {
				if ( ((Link) element).getZweitZiel() != null) {
					return ((Link) element).getZweitZiel().getName();
				}
			}
			return "-";
		}
	}
}
