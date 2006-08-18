/*
 * Created on 15.09.2005 / 16:08:56
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.gui.views.talent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.gui.views.ComparatorCollection;
import org.d3s.alricg.gui.views.SpaltenSchema;
import org.d3s.alricg.gui.views.TypSchema;
import org.d3s.alricg.gui.views.talent.TalentSpalten.Filter;
import org.d3s.alricg.gui.views.talent.TalentSpalten.Ordnung;
import org.d3s.alricg.gui.views.talent.TalentSpalten.Spalten;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.LinkProzessorFront;
import org.d3s.alricg.prozessor.Prozessor;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.common.HeldenLink;
import org.d3s.alricg.prozessor.elementBox.ElementBox;
import org.d3s.alricg.prozessor.elementBox.ElementBoxLink;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorTalent;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.TextStore;

/**
 * <u>Beschreibung:</u><br> 
 * Das Schema für das handling von GeneratorLinks mit Talenten. Die Objekte hier sind Links, 
 * die als Ziel Talente besitzen. Das Schema wird für ausgewählte Talent bei der Generierung
 * verwendet.
 * @see org.d3s.alricg.gui.views.TypSchema
 * @author V. Strelow
 */
public class TalentLinkSchema implements TypSchema {
    
    /** <code>TalentLinkSchema</code>'s logger */
    private static final Logger LOG = Logger.getLogger(TalentLinkSchema.class.getName());
	private LinkProzessorFront<Talent, ExtendedProzessorTalent, HeldenLink> prozessor;
	private Held held;
	private ElementBox<Link> elementBox;
	private TalentDirektSchema direktSchema;
	private SpaltenSchema.SpaltenArt spaltenart;
	
	public TalentLinkSchema(Held held, SpaltenSchema.SpaltenArt spaltenart) {
		this.held = held;
		this.direktSchema = new TalentDirektSchema(held);
		this.spaltenart = spaltenart;
		
		this.elementBox = new ElementBoxLink<Link>();
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#hasLinksAsElements()
	 */
	public boolean hasLinksAsElements() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#getCellValue(java.lang.Object, java.lang.Object)
	 */
	public Object getCellValue(Object object, Object column) {
		final List<IdLink> list;
		StringBuffer tmpString;
		
		switch ((TalentSpalten.Spalten) column) {
			case stufe: 
				return ((Link) object).getWert();
			
			case modis:
				list = ((GeneratorLink) object).getLinkModiList();
				tmpString = new StringBuffer("");
				
				// Falls keine Modis existieren
				if (list.size() == 0) return "-";
				
				// Anhängen aller Modis
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getWert() > 0) {
						tmpString.append("+");
					}
					tmpString.append(list.get(i).getWert());
					tmpString.append("/ ");
				}
				
				// Löschen der letzen Trennzeichen ("/ ")
				tmpString.delete(tmpString.length() - 2, tmpString.length() - 1);
				return tmpString.toString();
				
			case kosten: return ((HeldenLink) object).getKosten();
			case spezialisierungen: return ((Link) object).getText();
			case auswahl: 
				list = ((GeneratorLink) object).getLinkModiList();
				tmpString = new StringBuffer("");
				
				// Falls keine Auswahlen existieren
				if (list.size() == 0) return "";
				
				// Anhängen aller Modis
				// TODO Bessere Anzeige. Im Moment wird nur die Herkunft angezeigt aus dem Enum
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getQuellAuswahl() == null) continue;

					tmpString.append(list.get(i).getQuellAuswahl().getHerkunft()
												.getCharKomponente().toString());
					tmpString.append(", ");
				}
				
				// Löschen der letzen Trennzeichen (", ")
				if (tmpString.length() > 0 ) {
					tmpString.delete(tmpString.length() - 3, tmpString.length() - 1);
				}
				return tmpString.toString();
				
			case leitTalent: return ((Link) object).isLeitwert();
			
			default: // Aufrufen des Schemas für direkte Objekte
				return direktSchema.getCellValue(((Link) object).getZiel(), column);
		}
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#setCellValue(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public void setCellValue(Object newValue, Object object, Object column) {
		
		switch ((TalentSpalten.Spalten) column) {
			case stufe: 
				prozessor.updateWert(
						(HeldenLink) object, 
						Integer.parseInt( newValue.toString() ));
				break;
					
			case spezialisierungen: 
				prozessor.updateText(
							(HeldenLink) object,
							newValue.toString());
				//TODO: Sonderregel hinzufügen, wenn Sonderregelprozessor
				break;
				
			case minus: 
				prozessor.removeElement((HeldenLink) object);
				break;
				
			default:
				LOG.warning("Case-Fall konnte nicht gefunden werden!");
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#isCellEditable(java.lang.Object, java.lang.Object)
	 */
	public boolean isCellEditable(Object object, Object column) {
		if (column.equals(TalentSpalten.Spalten.name)) {
			// diese müssen immer True sein, damit die Navigation funktioniert
			return true;
		} else if (column.equals(TalentSpalten.Spalten.minus)) {
			return prozessor.canRemoveElement((HeldenLink) object);
		}
		
		// Diese können im Normalfall editiert werden
		if ( object instanceof HeldenLink ) {
			switch ((TalentSpalten.Spalten) column) {
				case stufe: return prozessor.canUpdateWert((HeldenLink) object);
				case spezialisierungen:	return prozessor.canUpdateText((HeldenLink) object);
			}
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#getToolTip(java.lang.Object, java.lang.Object)
	 */
	public String getToolTip(Object object, Object column) {
		final List<IdLink> list;
		final StringBuffer tmpString = new StringBuffer();
		final TextStore lib = FactoryFinder.find().getLibrary();
		
		if (object instanceof Enum) return null;
		if (object instanceof String) return null;
		
		switch ((TalentSpalten.Spalten) column) {
			case stufe: // TODO Soll die Berechnung zeigen!
				return "todo";
				
			case modis: 
				list = ((GeneratorLink) object).getLinkModiList();
				
				// Falls keine Modis existieren
				if (list.size() == 0) return lib.getToolTipTxt("TblTalentModisKeine");
				
				// Anhängen aller Modis
				for (int i = 0; i < list.size(); i++) {
					tmpString.append(
							list.get(i).getQuellElement().getCharKomponente().getBezeichnung()
					);
					tmpString.append(": ");
					tmpString.append(list.get(i).getWert());
					tmpString.append("/ ");
				}
				
				// Löschen der letzen Trennzeiche ("/ ")
				tmpString.delete(tmpString.length() - 3, tmpString.length() - 1);
				return tmpString.toString();
				
			case kosten: // TODO Soll die Berechnung zeigen!
				return "todo";
				
			case spezialisierungen:
				if (((Link) object).getText().length() == 0) {
					return lib.getToolTipTxt("TblTalentSpeziKeine");
				} else {
					return lib.getToolTipTxt("TblTalentSpezi");
				}
				
			case auswahl: // TODO implementieren
			case leitTalent:
				if (((Link) object).isLeitwert()) {
					return lib.getToolTipTxt("TblTalentLeitwertTrue");
				} else {
					return lib.getToolTipTxt("TblTalentLeitwertFalse");
				}
			
			default: // Aufrufen des Schemas für direkte Objekte
				return direktSchema.getToolTip(((Link) object).getZiel(), column);
		}
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#getSortOrdner()
	 */
	public Enum[] getOrdnerForOrdnung(Enum ordnung) {
		if (ordnung == Ordnung.sorte) {
			return Talent.Sorte.values();
		} else {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#getOrdinalFromElement(java.lang.Object)
	 */
	public Enum[] getEnumsFromElement(Object element, Enum ordnung) {
		Enum[] tmp;
		
		if (ordnung == Ordnung.sorte) {
			tmp = new Enum[1];
			tmp[0] = ((Talent) ((Link) element).getZiel()).getSorte();
		} else {
			tmp = new Enum[0];
		}
		
		return tmp;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#doFilterElements(java.lang.Enum, java.util.ArrayList)
	 */
	public List doFilterElements(Enum filter, List aList) {
		List<Link> list = new ArrayList<Link>();
		
		switch((Filter) filter) {
		case keiner: return aList;
		case nurAktivierte:
			ExtendedProzessorTalent extProTal = (ExtendedProzessorTalent) prozessor.getExtendedFunctions();
			
			for (int i = 0; i < aList.size(); i++) {
				if ( extProTal.isAktiviert( (Talent) ((Link) aList.get(i)).getZiel() )  ) {
					list.add((GeneratorLink) aList.get(i));
				}
			}
			
			return list; 
			
		case nurModifizierte:
			
			for (int i = 0; i < aList.size(); i++) {
				if ( ((GeneratorLink) aList.get(i)).getWertModis() != 0  ) {
					list.add((GeneratorLink) aList.get(i));
				}
			}
			
			return list; 

		default: LOG.warning("Case fall nicht gefunden!");
		}
		
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#getElementBox()
	 */
	public ElementBox getElementBox() {
		return elementBox;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#getProzessor()
	 */
	public Prozessor getProzessor() {
		return prozessor;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#setElementBox(ElementBox)
 	 */
	public void setElementBox(ElementBox box) {
		this.elementBox = (ElementBox<Link>) box;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#setProzessor(org.d3s.alricg.prozessor.Prozessor)
	 */
	public void setProzessor(Prozessor prozessor) {
		this.prozessor = (LinkProzessorFront<Talent, ExtendedProzessorTalent, HeldenLink>) prozessor;
		direktSchema.setProzessor(prozessor);
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#getComparator(java.lang.Object)
	 */
	public Comparator getComparator(Object column) {
		
		switch ((Spalten) column) {
		case stufe: return ComparatorCollection.compLinkWert;
		case kosten: return ComparatorCollection.compLinkKosten;
		case spezialisierungen: return ComparatorCollection.compLinkText;
		case leitTalent: return ComparatorCollection.compLeittalent;
		}
		
		// Erstellung eines Wrappers für Links, mit den "standard" Comparatoren
		return new ComparatorCollection.ComparatorWrapper( direktSchema.getComparator(column) );
	}
}
