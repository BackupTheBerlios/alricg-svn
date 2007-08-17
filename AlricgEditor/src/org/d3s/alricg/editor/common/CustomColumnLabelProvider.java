/*
 * Created 20.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common;

import org.d3s.alricg.common.CharElementNamesService;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTableObject;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTreeObject;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Faehigkeit;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.charElemente.links.Option;
import org.d3s.alricg.store.charElemente.links.OptionAnzahl;
import org.d3s.alricg.store.charElemente.links.OptionListe;
import org.d3s.alricg.store.charElemente.links.OptionVerteilung;
import org.d3s.alricg.store.charElemente.links.OptionVoraussetzung;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * @author Vincent
 */
public class CustomColumnLabelProvider {
	
	/**
	 * Zeigt die Klasse (also "Talent", "Zauber", "Vorteil", ...) zu einem
	 * CharElement an.
	 * @author Vincent
	 */
	public static class CharElementKlassenLabelProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			CharElement chatEle = ViewUtils.getCharElement(element);
			if (chatEle == null) return "";
			
			return CharElementNamesService.getCharElementName(chatEle.getClass());
		}
	}
	
	/**
	 * Zeigt den Namen eines CharElement an. Get für CharElemente, TreeObjects und Links.
	 * @author Vincent
	 */
	public static class OptionNameLabelProvider extends NameLabelProvider {
		@Override
		public String getText(Object element) {
			if ( !(((TreeObject)element).getValue() instanceof Option) ) {
				return super.getText(element);
			}
			
			final StringBuilder strBuilder = new StringBuilder();
			final StringBuilder strBuilderZustaz = new StringBuilder();
			final Option option = (Option) ((TreeObject)element).getValue();
			final TreeObject[] siblings = ((TreeObject)element).getParent().getChildren();;
			
			int counter = 0;
			for (int i = 0; i < siblings.length; i++) {
				if (siblings[i].getValue() instanceof Option) {
					counter++;
					if (siblings[i].equals(element)) {
						break;
					}
				}
			}
			
			if ( ((TreeObject)element).getParent().getValue() instanceof Option) {
				strBuilder.append("Alternative ");
			}
			
			if (option instanceof OptionVoraussetzung) {
				strBuilder.append("Voraussetzung");
				
				if ( ((OptionVoraussetzung) option).getWert() > 0 ) {
					strBuilderZustaz.append(" (")
						.append("Ab Wert ")
						.append(((OptionVoraussetzung) option).getWert());
				}
				if ( ((OptionVoraussetzung) option).getAnzahl() > 0 ) {
					if (strBuilderZustaz.length() == 0) {
						strBuilderZustaz.append(" (");
					} else {
						strBuilderZustaz.append(",");
					}
					strBuilderZustaz.append("Nötig ")
						.append(((OptionVoraussetzung) option).getAnzahl());
				}
				if (strBuilderZustaz.length() > 0) {
					strBuilderZustaz.append(")");
				}
				
			} else if (option instanceof OptionAnzahl) {
				strBuilder.append("Auswahl Anzahl");
				if ( ((OptionAnzahl) option).getAnzahl() > 0 ) {
					strBuilderZustaz.append(" (").
							append( ((OptionAnzahl) option).getAnzahl()).
							append(")");
				}
				
			} else if (option instanceof OptionListe) {
				strBuilder.append("Auswahl Liste");
				int[] wertListe = ((OptionListe) option).getWerteListe();
				if ( ((OptionListe) option).getWerteListe() != null) {
					strBuilderZustaz.append(" (");
					for (int i = 0; i < wertListe.length; i++) {
						strBuilderZustaz.append(wertListe[i]);
						if ( i < (wertListe.length-1) ) {
							strBuilderZustaz.append(",");
						}
					}
					strBuilderZustaz.append(")");
				}

			} else if (option instanceof OptionVerteilung) {
				strBuilder.append("Auswahl Verteilung");
				strBuilderZustaz.append(" (")
					.append( ((OptionVerteilung) option).getWert() )
					.append(" Punkte");
				if (((OptionVerteilung) option).getAnzahl() > 0) {
					strBuilderZustaz.append(" auf ")
					.append(((OptionVerteilung) option).getAnzahl());
				}
				if (((OptionVerteilung) option).getMax() > 0) {
					strBuilderZustaz.append(", Max. je ")
					.append(((OptionVerteilung) option).getMax());
				}
				strBuilderZustaz.append(")");
				
			} else {
				throw new IllegalArgumentException("Parameter-Klasse" + option.getClass() + " konnte nicht gefunden werden!");
			}
			strBuilder.append(" ").append(counter).append(strBuilderZustaz.toString());
			
			return strBuilder.toString();
		}
		
		@Override
		public Image getImage(Object obj) {
			if (((TreeObject)obj).getValue() instanceof Option
					|| ((TreeObject)obj).getValue() instanceof String) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(
						ISharedImages.IMG_OBJ_FOLDER);
			}
			return super.getImage(obj);
		}
	}
	
	/**
	 * Zeigt den Namen eines CharElement an. Get für CharElemente, TreeObjects und Links.
	 * @author Vincent
	 */
	public static class NameLabelProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			CharElement chatEle = ViewUtils.getCharElement(element);
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
			CharElement chatEle = ViewUtils.getCharElement(element);
			if (chatEle == null) return "";
			
			return ((Faehigkeit) chatEle).get3EigenschaftenString();
		}
		@Override
		public String getToolTipText(Object element) {
			CharElement charElem = ViewUtils.getCharElement(element);
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
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return "";

			return ((Faehigkeit) charElem).getKostenKlasse().toString();
		}
		
		@Override
		public String getToolTipText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
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
			element = ((TreeObject) element).getValue();
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
			element = ((TreeObject) element).getValue();
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
			element = ((TreeObject) element).getValue();
			if (element instanceof Link) {
				if ( ((Link) element).getZweitZiel() != null) {
					return ((Link) element).getZweitZiel().getName();
				}
				return "-";
			}
			return "";
		}
	}
}
