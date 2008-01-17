/*
 * Created 31.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.common;

import java.util.Arrays;

import org.d3s.alricg.common.CharElementTextService;
import org.d3s.alricg.common.CommonUtils;
import org.d3s.alricg.editor.common.ViewUtils;
import org.d3s.alricg.editor.common.ViewUtils.TreeOrTableObject;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.HerkunftVariante;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.Talent;
import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * @author Vincent
 */
public class CustomLabelProvider {
	/**
	 * Zeigt Modis für eine Stufe an
	 */
	public static class LinkWertModiProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			if ( ((TreeOrTableObject) element).getValue() instanceof GeneratorLink) {
				final int modi = ((GeneratorLink) ((TreeOrTableObject) element).getValue()).getWertModis();
				if (modi == 0) {
					return "-";
				} else if (modi > 0) {
					return "+" + 
							((GeneratorLink) ((TreeOrTableObject) element).getValue()).getWertModis();
				} else {
					return String.valueOf( 
						((GeneratorLink) ((TreeOrTableObject) element).getValue()).getWertModis() 
					);
				}
			}
			return "";
		}
	}
	
	/**
	 * Zeigt Modis für eine Stufe an
	 */
	public static class LinkKostenProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			if ( ((TreeOrTableObject) element).getValue() instanceof GeneratorLink) {
				return  CommonUtils.doubleToString(
						((GeneratorLink) ((TreeOrTableObject) element).getValue()).getKosten());
			}
			return "";
		}
	}
	
	/**
	 * Liefert die Art eines Talents
	 */
	public static class TalentArtProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final CharElement charElem = ViewUtils.getCharElement(element);
			if (charElem != null) {
				return ((Talent) charElem).getArt().toString();
			}
			return ""; //$NON-NLS-1$
		}
	}
	
	/**
	 * 
	 */
	public static class VerbilligtHerkunftProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final Herkunft herkunft = (Herkunft) ViewUtils.getCharElement(element);
			if (herkunft == null) return ""; //$NON-NLS-1$
		
			String text = Utils.getLinkArrayText(herkunft, HerkunftVariante.VERB_SONDERF);
			if (text.length() == 0) text = CharElementTextService.KEINE;
			
			return text;
		}
		
		@Override
		public String getToolTipText(Object element) {
			return getText(element);
		}
	}
	
	public static class HerkunftVoraussetzungProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final Herkunft herkunft = (Herkunft) ViewUtils.getCharElement(element);
			if (herkunft == null) return ""; //$NON-NLS-1$
			String returnText = Utils.addText(
					getText(herkunft, CharElementTextService.VORAUSSETZUNG_MODUS_POSITIV), 
					getText(herkunft, CharElementTextService.VORAUSSETZUNG_MODUS_NEGATIV));
			
			if (returnText == null || returnText.length() == 0) return CharElementTextService.KEINE;
			
			return returnText;
		}
			
		private String getText(Object element, int modus) {
			final Herkunft herkunft = (Herkunft) ViewUtils.getCharElement(element);
			if (herkunft == null) return ""; //$NON-NLS-1$

			String text = Utils.addText(
					"", 
					CharElementTextService.getVoraussetzungsText(herkunft.getVoraussetzung(),  modus));
			
			// Den "Parent" durchsuchen
			if (herkunft instanceof HerkunftVariante 
					&& ((HerkunftVariante) herkunft).isAdditionsVariante() 
					&& ( ((HerkunftVariante) herkunft).getEntferneXmlTag() == null
							|| !Arrays.asList(((HerkunftVariante) herkunft).getEntferneXmlTag())
																.contains(HerkunftVariante.VORAUSS)) 
						)
			{
				text = Utils.addText(
						text, 
						getText(((HerkunftVariante) herkunft).getVarianteVon(),  modus));
			}
			
			return text;
		}
	}
	
	/**
	 * Modifikationen. Dabei ist auch zu beachten, ob die Herkunft eine Variante ist und 
	 * die "übergeordente(n)" Herkunft(en) auch Modis besitzen
	 */
	public static class ModiHerkunftProvider extends ColumnLabelProvider {
		String[] auswahlArray;
		
		public ModiHerkunftProvider(String[] XmlTags) {
			auswahlArray = XmlTags;
		}
		
		@Override
		public String getText(Object element) {
			Herkunft herkunft = (Herkunft) ViewUtils.getCharElement(element);
			if (herkunft == null) return ""; //$NON-NLS-1$
			
			String text = "", tmpText;
			
			for (int i = 0; i < auswahlArray.length; i++) {
				tmpText = Utils.getAuswahlText(herkunft, auswahlArray[i]);
				if (tmpText.length() > 0) {
					if (text.length() > 0) text += "; ";
					text += tmpText;
				}
			}
			
			if (text.length() == 0) text = CharElementTextService.KEINE;
				
			return text;
		}
		
		@Override
		public String getToolTipText(Object element) {
			return getText(element);
		}
	}
	
	public static class HerkunftSOLabelProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			final Herkunft tmpHerkunft = (Herkunft) ViewUtils.getCharElement(element);
			if (tmpHerkunft != null) {
				return tmpHerkunft.getSoMin() + " - " + tmpHerkunft.getSoMax();
			}
			return ""; //$NON-NLS-1$
		}
	}
}
