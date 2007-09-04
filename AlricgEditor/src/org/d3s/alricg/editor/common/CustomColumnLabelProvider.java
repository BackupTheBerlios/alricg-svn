/*
 * Created 20.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common;

import org.d3s.alricg.common.CharElementTextService;
import org.d3s.alricg.common.icons.AbstractIconsLibrary;
import org.d3s.alricg.common.icons.ControlIconsLibrary;
import org.d3s.alricg.common.logic.FormelSammlung;
import org.d3s.alricg.editor.Messages;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.editor.utils.EditorViewUtils.AuswahlTreeObject;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTableObject;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTreeObject;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Faehigkeit;
import org.d3s.alricg.store.charElemente.Fertigkeit;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.RegionVolk;
import org.d3s.alricg.store.charElemente.Schrift;
import org.d3s.alricg.store.charElemente.Sprache;
import org.d3s.alricg.store.charElemente.VorNachteil;
import org.d3s.alricg.store.charElemente.Fertigkeit.AdditionsFamilie;
import org.d3s.alricg.store.charElemente.charZusatz.Gegenstand;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.charElemente.links.Option;
import org.d3s.alricg.store.charElemente.links.OptionAnzahl;
import org.d3s.alricg.store.charElemente.links.OptionListe;
import org.d3s.alricg.store.charElemente.links.OptionVerteilung;
import org.d3s.alricg.store.charElemente.links.OptionVoraussetzung;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * Verschiedene Label Provider
 * @author Vincent
 */
public class CustomColumnLabelProvider {
	public static Image acceptImage = ControlIconsLibrary.accept.getImageDescriptor().createImage();
	public static Image cancelImage = ControlIconsLibrary.cancel.getImageDescriptor().createImage();

	
	/**
	 * Zeigt die Klasse (also "Talent", "Zauber", "Vorteil", ...) zu einem
	 * CharElement an.
	 * @author Vincent
	 */
	public static class CharElementKlassenProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			CharElement chatEle = ViewUtils.getCharElement(element);
			if (chatEle == null) return ""; //$NON-NLS-1$
			
			return CharElementTextService.getCharElementName(chatEle.getClass());
		}
	}
	
	/**
	 * Zeigt den Namen eines CharElement an. Get für CharElemente, TreeObjects und Links.
	 * @author Vincent
	 */
	public static class OptionNameProvider extends NameLabelProvider {
		@Override
		public String getText(Object element) {
			if ( element instanceof AuswahlTreeObject ) {
				return ((AuswahlTreeObject) element).getText();
			} else if ( !(((TreeObject)element).getValue() instanceof Option) ) {
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
				strBuilder.append(Messages.LabelProvider_Alternative);
			}
			
			if (option instanceof OptionVoraussetzung) {
				strBuilder.append(Messages.LabelProvider_Voraussetzung);
				
				if ( option.getWert() > 0 ) {
					strBuilderZustaz.append(" (") //$NON-NLS-1$
						.append( Messages.bind(Messages.LabelProvider_AbWert, option.getWert()) );
				}
				if ( option.getAnzahl() > 0 ) {
					if (strBuilderZustaz.length() == 0) {
						strBuilderZustaz.append(" ("); //$NON-NLS-1$
					} else {
						strBuilderZustaz.append(","); //$NON-NLS-1$
					}
					strBuilderZustaz.append( Messages.bind(Messages.LabelProvider_Noetig, option.getWert()) );
				}
				if (strBuilderZustaz.length() > 0) {
					strBuilderZustaz.append(")");
				}
				
			} else if (option instanceof OptionAnzahl) {
				strBuilder.append(Messages.LabelProvider_AuswahlAnzahl);
				if ( ((OptionAnzahl) option).getAnzahl() > 0 ) {
					strBuilderZustaz.append(" ("). //$NON-NLS-1$
							append( ((OptionAnzahl) option).getAnzahl()).
							append(")"); //$NON-NLS-1$
				}
				
			} else if (option instanceof OptionListe) {
				strBuilder.append(Messages.LabelProvider_AuswahlListe);
				int[] wertListe = ((OptionListe) option).getWerteListe();
				if ( ((OptionListe) option).getWerteListe() != null) {
					strBuilderZustaz.append(" ("); //$NON-NLS-1$
					for (int i = 0; i < wertListe.length; i++) {
						strBuilderZustaz.append(wertListe[i]);
						if ( i < (wertListe.length-1) ) {
							strBuilderZustaz.append(","); //$NON-NLS-1$
						}
					}
					strBuilderZustaz.append(")"); //$NON-NLS-1$
				}

			} else if (option instanceof OptionVerteilung) {
				strBuilder.append(Messages.LabelProvider_AuswahlVerteilung);
				strBuilderZustaz.append( Messages.bind(Messages.LabelProvider_Punkt, option.getWert()) );
				if (((OptionVerteilung) option).getAnzahl() > 0) {
					strBuilderZustaz.append( Messages.bind(Messages.LabelProvider_Auf, option.getAnzahl()) );
				}
				if (((OptionVerteilung) option).getMax() > 0) {
					strBuilderZustaz.append( Messages.bind(Messages.LabelProvider_MaxJe, option.getMax()) );
				}
				strBuilderZustaz.append(")"); //$NON-NLS-1$
				
			} else {
				throw new IllegalArgumentException(Messages.LabelProvider_21 + option.getClass() + Messages.LabelProvider_22);
			}
			strBuilder.append(" ").append(counter).append(strBuilderZustaz.toString()); //$NON-NLS-1$
			
			return strBuilder.toString();
		}
		
		@Override
		public Image getImage(Object obj) {
			if (((TreeObject)obj).getValue() instanceof Option
					|| ((TreeObject)obj).getValue() instanceof String
					|| obj instanceof AuswahlTreeObject) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(
						ISharedImages.IMG_OBJ_FOLDER);
			}
			return super.getImage(obj);
		}

		@Override
		public String getToolTipText(Object element) {
			if ( !(((TreeObject)element).getValue() instanceof Option) ) {
				return null;
			}
			final Option option = (Option) ((TreeObject)element).getValue();
			StringBuilder strBuilder = new StringBuilder();
			
			// Option Eigenschaften prüfen
			if (option instanceof OptionVoraussetzung) {
				if (option.getWert() > 0) {
					strBuilder.append(Messages.bind( Messages.LabelProvider_Voraussetzung_TT1, option.getAnzahl()) );
				} else {
					strBuilder.append(Messages.LabelProvider_Voraussetzung_TT2);
				}
				if (option.getAnzahl() > 0) {
					strBuilder.append( Messages.bind( Messages.LabelProvider_Voraussetzung_TT3, option.getAnzahl()) );
				} else {
					strBuilder.append(Messages.LabelProvider_Voraussetzung_TT4);
				}
				
			} else if (option instanceof OptionAnzahl) {
				if (option.getAnzahl() > 0) {
					strBuilder.append( Messages.bind( Messages.LabelProvider_AuswahlAnzahl_TT1, option.getAnzahl()) );
				} else {
					strBuilder.append(Messages.LabelProvider_AuswahlAnzahl_TT2);
				}
				
			} else if (option instanceof OptionListe) {
				strBuilder.append(Messages.LabelProvider_AuswahlListe_TT);
				for (int i = 0; i < option.getWerteListe().length; i++) {
					strBuilder.append(option.getWerteListe()[i]);
					if ( i < (option.getWerteListe().length-1) ) {
						strBuilder.append(","); //$NON-NLS-1$
					}
				}
				
			} else if (option instanceof OptionVerteilung) {
				strBuilder.append( Messages.bind( Messages.LabelProvider_AuswahVerteilung_TT1, option.getWert()) );
					
				if (option.getAnzahl() > 0) {
					strBuilder.append(option.getAnzahl());
				} else {
					strBuilder.append(Messages.LabelProvider_AuswahVerteilung_TT2);
				}
				strBuilder.append(Messages.LabelProvider_AuswahVerteilung_TT3);
				
				if (option.getMax() > 0) {
					strBuilder.append(Messages.bind( Messages.LabelProvider_AuswahVerteilung_TT4, option.getMax()));
				}
			}
			
			// TODO Auto-generated method stub
			return strBuilder.toString();
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
			if (chatEle == null) return ((TreeOrTableObject) element).getValue().toString();
			
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
	public static class Faehigkeit3EigenschaftProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			CharElement chatEle = ViewUtils.getCharElement(element);
			if (chatEle == null) return ""; //$NON-NLS-1$
			
			return ((Faehigkeit) chatEle).get3EigenschaftenString();
		}
		@Override
		public String getToolTipText(Object element) {
			CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) super.getToolTipText(element);
			
			StringBuilder builder = new StringBuilder();
			Eigenschaft[] eigenschaftArray = ((Faehigkeit) charElem).getDreiEigenschaften();
			
			builder.append("("); //$NON-NLS-1$
			for (int i = 0; i < eigenschaftArray.length; i++) {
				builder.append(eigenschaftArray[i].getName());
				if ((i+1) < eigenschaftArray.length) {
					builder.append("/"); //$NON-NLS-1$
				}
			}
			builder.append(")"); //$NON-NLS-1$
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
			return ""; //$NON-NLS-1$
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
			if (charElem == null) return ""; //$NON-NLS-1$

			if (charElem instanceof Schrift) {
				return ((Schrift) charElem).getKostenKlasse().getValue();
			} else if (charElem instanceof Sprache) {
				String tmpStr = ((Sprache) charElem).getKostenKlasse().getValue();
				
				if ( ((Sprache) charElem).getWennNichtMuttersprache() != null ) {
					final Sprache wnm = ((Sprache) charElem).getWennNichtMuttersprache();
					if (wnm.getKostenKlasse() != ((Sprache) charElem).getKostenKlasse() ) {
						tmpStr = tmpStr + " / " + wnm.getKostenKlasse().getValue();
					}
				}
				
				return tmpStr;
			} else {
				return ((Faehigkeit) charElem).getKostenKlasse().getValue();
			}
			
		}
		
		@Override
		public String getToolTipText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null)  super.getToolTipText(element);
			
			StringBuilder builder = new StringBuilder();
			builder.append(Messages.LabelProvider_SKT_TT);
			
			if (charElem instanceof Schrift) {
				builder.append(((Schrift) charElem).getKostenKlasse().getValue());
			} else if (charElem instanceof Sprache) {
				builder.append(((Sprache) charElem).getKostenKlasse().getValue());
				
				if ( ((Sprache) charElem).getWennNichtMuttersprache() != null ) {
					final Sprache wnm = ((Sprache) charElem).getWennNichtMuttersprache();
					if ( wnm.getKostenKlasse() != ((Sprache) charElem).getKostenKlasse() ) {
						builder.append(" oder " )
								.append(wnm.getKostenKlasse().getValue())
								.append(" wenn nicht Muttersprache");
					}
				}
				
			} else {
				builder.append(((Faehigkeit) charElem).getKostenKlasse().getValue());
			}
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
					return "-"; //$NON-NLS-1$
				}
				return Integer.toString( ((Link) element).getWert() );
			}
			return ""; //$NON-NLS-1$
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
			return ""; //$NON-NLS-1$
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
				return "-"; //$NON-NLS-1$
			}
			return ""; //$NON-NLS-1$
		}
	}
	
	/**
	 * Zeigt die AdditionsFamilie mit AdditionsWert zu einer Fertigkeit an
	 * @author Vincent
	 */
	public static class FertigkeitFamilieProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return ""; //$NON-NLS-1$
			
			if (charElem instanceof Fertigkeit) {
				AdditionsFamilie addFam = ((Fertigkeit) charElem).getAdditionsFamilie();
				if (addFam != null) {
					return addFam.getAdditionsID() + " " + addFam.getAdditionsWert(); 
				}
			}
			return "-"; //$NON-NLS-1$
		}
	}
	
	/**
	 * @author Vincent
	 */
	public static class FertigkeitGpProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return ""; //$NON-NLS-1$
			
			if (charElem instanceof Fertigkeit 
					&& ((Fertigkeit) charElem).getGpKosten() != CharElement.KEIN_WERT) 
			{
				return doubleToString( ((Fertigkeit) charElem).getGpKosten() );
			}
			return "-"; //$NON-NLS-1$
		}
	}
	
	/*
	public static class VorNachteilVerbilligtProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return ""; //$NON-NLS-1$
			
			final VorNachteil vorNachteil = (VorNachteil) charElem;
			
			// Gibt es eine Sonderregel, welche die Kosten ändert? Dann den Text anzeigen
			if (vorNachteil.getSonderregel() != null 
					&& vorNachteil.getSonderregel()
						.changeAnzeigeText(ChangeTextContex.VeraendertKosten) != null)
			{
				return vorNachteil.getSonderregel()
						.changeAnzeigeText(ChangeTextContex.VeraendertKosten);
			}
			
			// "95%" Prüfung...
			if (vorNachteil.getAendertApSf() == null 
						&& vorNachteil.getAendertGpVorteil() == null 
						&& vorNachteil.getAendertGpNachteil() == null) {
				return "keine";
			}
			
		// Berechen der Kosten Änderungen
			StringBuilder strBuilder = new StringBuilder();
			String retText;
			
			// Vorteile
			strBuilder.append( getAendertText(vorNachteil.getAendertGpVorteil(), "") );
			
			// Nachteile
			retText =  getAendertText(vorNachteil.getAendertGpNachteil(), "");
			if (strBuilder.length() > 0 && retText.length() > 0) {
				strBuilder.append(",");
			}
			strBuilder.append(retText);
			
			// Sonderf
			retText = getAendertText(vorNachteil.getAendertApSf(), " Ap");
			if (strBuilder.length() > 0 && retText.length() > 0) {
				strBuilder.append(",");
			}
			strBuilder.append(retText);
			
			if (strBuilder.length() == 0) return "keine";
			return strBuilder.toString();
		}
		
		private String getAendertText(IdLink[] linkArray, String apZusatz) {
			if (linkArray == null ) return "";
			
			final StringBuilder strBuilder = new StringBuilder();
			
			for (int i = 0; i < linkArray.length; i++) {
				strBuilder.append( CharElementTextService.getLinkText(linkArray[i]) );
				strBuilder.append(apZusatz);
				if (i+1 < linkArray.length) {
					strBuilder.append(",");
				}
			}
			return strBuilder.toString();
		}
	}*/
	
	public static class VorNachteilGpProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return ""; //$NON-NLS-1$
			
			final VorNachteil vorNachteil = (VorNachteil) charElem;
			StringBuilder strBuilder = new StringBuilder();
			
			if ( vorNachteil.getGpKosten() != 0 
					&& vorNachteil.getGpKosten() != CharElement.KEIN_WERT) 
			{
				strBuilder.append( doubleToString(vorNachteil.getGpKosten()) );
			}
			if ( vorNachteil.getKostenProStufe() != 0 
					&& vorNachteil.getKostenProStufe() != CharElement.KEIN_WERT) 
			{
				if (strBuilder.length() > 0) strBuilder.append(" +");
				
				strBuilder.append( doubleToString(vorNachteil.getKostenProStufe()) )
						.append(" je Stufe");
			}
			return strBuilder.toString();
		}
	}
	
	/**
	 * Zeigt die AP Kosten für eine Fertigkeit an. Dies gilt auch für die
	 * speziellen Kosten von Sonderfertigkeiten!
	 * @author Vincent
	 */
	public static class SonderFertigkeitApProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return ""; //$NON-NLS-1$
			
			return Integer.toString(
					FormelSammlung.getApFromGp( ((Fertigkeit) charElem).getGpKosten() )
				);
		}
	}
	
	public static class FertigkeitTextNoetigProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			return ""; //$NON-NLS-1$
		}
		@Override
		public Image getImage(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return null;
			
			if (charElem instanceof Fertigkeit) {
				if ( ((Fertigkeit) charElem).isMitFreienText() || 
						( ((Fertigkeit) charElem).getTextVorschlaege() != null 
								&& ((Fertigkeit) charElem).getTextVorschlaege().length > 0) )  {
					return acceptImage;
				} else {
					return cancelImage;
				}
			}
			return super.getImage(element);
		}
	}
	
	public static class FertigkeitZweitZielNoetigProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			return ""; //$NON-NLS-1$
		}
		@Override
		public Image getImage(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return null;
			
			if (charElem instanceof Fertigkeit) {
				if ( ((Fertigkeit) charElem).getBenoetigtZweitZiel() )  {
					return acceptImage;
				} else {
					return cancelImage;
				}
			}
			return null;
		}
	}
	
	public static class FertigkeitArtProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return "";
			
			return ((Fertigkeit) charElem).getArt().getValue();
		}
	}
	
	public static class CharElementVoraussetzungProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return ""; //$NON-NLS-1$
			
			return CharElementTextService.getVoraussetzungsText(charElem.getVoraussetzung());
		}
	}
	
	public static class SchriftSpracheKomplexitaetProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return ""; //$NON-NLS-1$
			
			if (charElem instanceof Schrift) {
				return Integer.toString( ((Schrift) charElem).getKomplexitaet() );
			} else {
				String tmpStr = Integer.toString( ((Sprache) charElem).getKomplexitaet() );
				
				if ( ((Sprache) charElem).getWennNichtMuttersprache() != null ) {
					final Sprache wnm = ((Sprache) charElem).getWennNichtMuttersprache();
					if (wnm.getKomplexitaet() != ((Sprache) charElem).getKomplexitaet() ) {
						tmpStr = tmpStr + " / " + Integer.toString( wnm.getKomplexitaet() );
					}
				}
				
				return tmpStr;
			}
		}
		
		@Override
		public String getToolTipText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null)  super.getToolTipText(element);
			
			if ( !(charElem instanceof Sprache) 
					|| ((Sprache) charElem).getWennNichtMuttersprache() == null) {
				return null;
			}
			
			String retStr;
			
			final Sprache wnm = ((Sprache) charElem).getWennNichtMuttersprache();
			if ( wnm.getKostenKlasse() != ((Sprache) charElem).getKostenKlasse() ) {
				StringBuilder strB = new StringBuilder();
				
				strB.append("Komplexität: ")
					.append(((Sprache) charElem).getKomplexitaet())
					.append(" oder ")
					.append(wnm.getKomplexitaet())
					.append(" wenn nicht Muttersprache");
					
				return strB.toString();
			}
			
			return null;
		}
	}
	
	public static class GegenstandWertProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return ""; //$NON-NLS-1$
			
			int[] muenzen = FormelSammlung.getWertInMuenzen(((Gegenstand) charElem).getWertInKreuzern());
			return CharElementTextService.getMuenzenString(muenzen, true);
		}
		
		@Override
		public String getToolTipText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null)  super.getToolTipText(element);
			
			int[] muenzen = FormelSammlung.getWertInMuenzen(((Gegenstand) charElem).getWertInKreuzern());
			return CharElementTextService.getMuenzenString(muenzen, false);
		}
	}
	
	public static class GegenstandArtProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return ""; //$NON-NLS-1$
			
			return ((Gegenstand) charElem).getArt().toString();
		}
	}
	
	public static class GegenstandRegionVolkProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return ""; //$NON-NLS-1$
			
			RegionVolk[] regionVolk = ((Gegenstand) charElem).getHerkunft();
			if (regionVolk == null) return "unbestimmt";
			
			StringBuilder strB = new StringBuilder();
			for (int i = 0; i < regionVolk.length; i++) {
				strB.append(regionVolk[i].getAbk());
				if (i+1 < regionVolk.length) strB.append(", ");
			}
			return strB.toString();
		}
		
		@Override
		public String getToolTipText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null)  super.getToolTipText(element);
			
			final RegionVolk[] regionVolk = ((Gegenstand) charElem).getHerkunft();
			if (regionVolk == null) return "unbestimmt";
			
			final StringBuilder strB = new StringBuilder();
			for (int i = 0; i < regionVolk.length; i++) {
				strB.append(regionVolk[i].getName());
				if (i+1 < regionVolk.length) strB.append(", ");
			}
			return strB.toString();
		}
	}
	
	public static class RegionVolkAbkProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return ""; //$NON-NLS-1$
			
			return ((RegionVolk) charElem).getAbk();
		}
	}
	
	public static class RegionVolkArtProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return ""; //$NON-NLS-1$
			
			return ((RegionVolk) charElem).getArt().toString();
		}
	}
	
	public static class LiturgieGradProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			CharElement chatEle = ViewUtils.getCharElement(element);
			if (chatEle == null) return ""; //$NON-NLS-1$
			
			StringBuilder strB = new StringBuilder();
			for (int i = 0; i < ((Liturgie) chatEle).getGrad().length; i++) {
				strB.append(((Liturgie) chatEle).getGrad()[i]);
				if (i+1 < ((Liturgie) chatEle).getGrad().length) {
					strB.append(", "); 
				}
			}
			
			return strB.toString();
		}
	}
	
	public static class HerkunftGpProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			CharElement chatEle = ViewUtils.getCharElement(element);
			if (chatEle == null) return ""; //$NON-NLS-1$
			
			return Integer.toString( ((Herkunft) chatEle).getGpKosten() );
		}
	}
	
	public static class HerkunftEigenschaftModiProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			CharElement chatEle = ViewUtils.getCharElement(element);
			if (chatEle == null) return ""; //$NON-NLS-1$
			
			return CharElementTextService.getAuswahlText(((Herkunft) chatEle).getEigenschaftModis());
		}
		
		@Override
		public String getToolTipText(Object element) {
			CharElement chatEle = ViewUtils.getCharElement(element);
			if (chatEle == null) return null;
			
			return CharElementTextService.getAuswahlText(((Herkunft) chatEle).getEigenschaftModis());
		}
	}
	
	/**
	 * Helper für den ImageProvider. Hierrüber wird die Anzeige der Bilder
	 * für unterschiedliche Elemente gesteuert
	 * @author Vincent
	 * @param <T>
	 */
	public static interface ImageProviderRegulator<T> {
		public String getName(T obj);
		public T[] getItems(CharElement obj);
		public AbstractIconsLibrary<T> getIconsLibrary();
		public IWorkbenchPart getConsumer();
	}
	
	
	/**
	 * Für die Darstellung von Bildern in Tabellen mit ToolTip
	 * @author Vincent
	 */
	public static class ImageProvider extends ColumnLabelProvider {
		private static final int ANZAHL_SPALTEN = 4;
		private final int index;
		//private final IViewPart consumer;
		private final ImageProviderRegulator regulator;
		
		public ImageProvider(int index, ImageProviderRegulator regulator ) {
			this.index = index;
			this.regulator = regulator;
		}
		
		@Override
		public String getText(Object element) {
			if (index+1 < ANZAHL_SPALTEN) return "";
		
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return "";
			if (regulator.getItems(charElem) == null) return null;
			
			if ( regulator.getItems(charElem).length >= ANZAHL_SPALTEN)  {
				return "+";
			}
			return "";
		}
		
		@Override
		public Image getImage(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return null;			
			if (regulator.getItems(charElem) == null) return null;
			
			if (regulator.getItems(charElem).length > index) {
					return regulator.getIconsLibrary().getImage16(
							regulator.getItems(charElem)[index], 
							regulator.getConsumer());
			}
			return null;
		}

		@Override
		public String getToolTipText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return null;
			if (regulator.getItems(charElem) == null) return null;
			
			String retStr = "";
			if ( regulator.getItems(charElem).length > index ) {
				retStr = regulator.getName(regulator.getItems(charElem)[index]);
			}
			
			if (index+1 >= ANZAHL_SPALTEN
					&& regulator.getItems(charElem).length >= ANZAHL_SPALTEN) {
				for (int i = index+1; i < regulator.getItems(charElem).length; i++) {
					
					retStr = retStr + ", " + regulator.getName(regulator.getItems(charElem)[i]);
				}
			}

			if (retStr.length() == 0) return null;
			return retStr;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.CellLabelProvider#getToolTipImage(java.lang.Object)
		 */
		@Override
		public Image getToolTipImage(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem == null) return null;
			if (regulator.getItems(charElem) == null) return null;
			
			if ( !(regulator.getItems(charElem).length > index)) {
				return null;
			}
			
			// Image erzeugen
			final Image	img =  regulator.getIconsLibrary().getImageDescriptor24(
									regulator.getItems(charElem)[index]).createImage();
			
			// Dispose Image
			final Display d  = regulator.getConsumer().getSite().getShell().getDisplay();
			
			d.timerExec (100, new Runnable () {
				public void run () {
					img.dispose();
				}
			});
			
			return img;
		}
	}

	// ---------------------------------------------------------------------------------
	
	/**
	 * Erstellt aus einem double einen String. Dabei wird die Nachkommastelle nur 
	 * angezeigt, wenn diese auch notwenig ist 
	 */
	private static String doubleToString(double d) {
		if (d % 1 == 0) {
			return Integer.toString( (int) d );
		} else {
			return Double.toString( d );
		}
	}
}
