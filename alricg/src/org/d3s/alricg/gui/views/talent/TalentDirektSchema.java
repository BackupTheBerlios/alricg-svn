/*
 * Created on 15.09.2005 / 16:20:12
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

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.controller.ImageAdmin;
import org.d3s.alricg.gui.komponenten.table.renderer.ImageTextObject;
import org.d3s.alricg.gui.views.ComparatorCollection;
import org.d3s.alricg.gui.views.SpaltenSchema;
import org.d3s.alricg.gui.views.TypSchema;
import org.d3s.alricg.gui.views.ViewFilter;
import org.d3s.alricg.gui.views.talent.TalentSpalten.Filter;
import org.d3s.alricg.gui.views.talent.TalentSpalten.Ordnung;
import org.d3s.alricg.gui.views.talent.TalentSpalten.Spalten;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.Prozessor;
import org.d3s.alricg.prozessor.elementBox.ElementBox;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.TextStore;

/**
 * <u>Beschreibung:</u><br> 
 * Das Schema für das handling von Talenten. Die Objekte hier sind direkt Talente, keine Links.
 * Das Schema wird für die Auswahl von Talenten (Generierung und Management) oder für den 
 * Editor verwendet.
 * @see org.d3s.alricg.gui.views.TypSchema
 * @author V. Strelow
 */
public class TalentDirektSchema implements TypSchema {
    
    /** <code>TalentSchema</code>'s logger */
    private static final Logger LOG = Logger.getLogger(TalentDirektSchema.class.getName());
	private Prozessor prozessor;
	private ElementBox<Talent> elementBox;
    
	///private static TalentSchema self; // Statischer selbst verweis
	
	// Arbeitsobjekte, um Parameter zu übergeben. Aus performancegründen 
	// als Attribute
	private final ImageTextObject tmpImageObj = new ImageTextObject();
	private final Link tmpLink = new IdLink(null, null);
	private Held held;

	public TalentDirektSchema(Held held) {
		this.held = held;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#hasLinksAsElements()
	 */
	public boolean hasLinksAsElements() {
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#getCellValue(java.lang.Object, java.lang.Enum)
	 */
	public Object getCellValue(Object object, Object column) {
		KostenKlasse tmpKK;
		
		switch ((TalentSpalten.Spalten) column) {
			case name: 	return ((Talent) object).getName();
			case stern:	// TODO implement
			case sorte: return ((Talent) object).getSorte();
			case art: 	return ((Talent) object).getArt();
			case kostenKlasse: 
				tmpLink.setZiel((Talent) object);
				tmpKK = held.getSonderregelAdmin().changeKostenKlasse(
												((Talent) object).getKostenKlasse(), tmpLink
											);
											
				tmpImageObj.setText( tmpKK.toString() );
				
				if ( tmpKK.equals( ((Talent) object).getKostenKlasse() ) )  {
					// Kosten sind unverändert, kein Icon
					tmpImageObj.setIcon(null);
					
				} else if ( ((Talent) object).getKostenKlasse().ordinal() < tmpKK.ordinal() ) {
					// Kosten sind billiger, daher das entsprechend Icon!
					if ( ((Talent) object).getKostenKlasse().ordinal() == tmpKK.ordinal() - 1)  {
						// SKT ist um eine Spalte billiger
						tmpImageObj.setIcon(ImageAdmin.pfeileGruen1);
					} else if ( ((Talent) object).getKostenKlasse().ordinal() == tmpKK.ordinal() - 2)  {
						// SKT ist um zwei Spalten billiger
						tmpImageObj.setIcon(ImageAdmin.pfeileGruen2);
					} else {
						// SKT ist um drei oder mehr Spalten billiger
						tmpImageObj.setIcon(ImageAdmin.pfeileGruen3);
					}
					
					
				} else {
					if ( ((Talent) object).getKostenKlasse().ordinal() == tmpKK.ordinal() + 1)  {
						// SKT ist um eine Spalte billiger
						tmpImageObj.setIcon(ImageAdmin.pfeileRot1);
					} else if ( ((Talent) object).getKostenKlasse().ordinal() == tmpKK.ordinal() + 2)  {
						// SKT ist um zwei Spalten billiger
						tmpImageObj.setIcon(ImageAdmin.pfeileRot2);
					} else {
						// SKT ist um drei oder mehr Spalten billiger
						tmpImageObj.setIcon(ImageAdmin.pfeileRot3);
					}
					
				}
				
				return tmpImageObj;
				
			case probe: return ((Talent) object).get3EigenschaftenString();
			case plus: 	return SpaltenSchema.buttonValue;
			case minus: return SpaltenSchema.buttonValue;
		}
		
		LOG.warning("Case-Fall konnte nicht gefunden werden!");
		return null;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.views.ViewSchema#getComparator(java.lang.Enum)
	 */
	public Comparator getComparator(Object column) {
		
		switch ((Spalten) column) {
		case name: 	return ComparatorCollection.compNamensComparator;
		case stern:	return TalentDirektSchema.compStern;
		case sorte: return TalentDirektSchema.compSorte;
		case art: 	return TalentDirektSchema.compArt;
		case kostenKlasse: return ComparatorCollection.compKostenKlasse;
		}
		
		LOG.warning("Case-Fall konnte nicht gefunden werden!");
		return null;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#setCellValue()
	 */
	public void setCellValue(Object newValue, Object object, Object column) {
		
		switch ((Spalten) column) {
		// Wenn ein Button geklickt wird
		case plus:
			prozessor.addNewElement((CharElement) object);
			break; 
			
		default:
			LOG.warning("Case-Fall konnte nicht gefunden werden!");
		}
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#isCellEditable()
	 */
	public boolean isCellEditable(Object object, Object column) {
		if (column.equals(TalentSpalten.Spalten.name)) {
			// diese müssen immer True sein, damit die Navigation funktioniert
			return true;
		} else if (column.equals(TalentSpalten.Spalten.plus) ) {
			return prozessor.canAddElement((CharElement) object);
		}
	
		return false;
	}

	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#getToolTip(java.lang.Object, java.lang.Enum)
	 */
	public String getToolTip(Object object, Object column) {
		final TextStore lib = FactoryFinder.find().getLibrary();
		
		if (object instanceof Enum) return null;
		if (object instanceof String) return null;
		
		switch ((TalentSpalten.Spalten) column) {
			case name: 	
				if (object instanceof Talent) {
					return lib.getToolTipTxt("TblHeaderName");
				} else {
					return lib.getToolTipTxt("TblOrdner");
				}
			case stern: // TODO implement
				return "fehlt";
				
			case art: 	
				
				if (object instanceof Talent) {
					switch (((Talent) object).getArt()) {
						case basis: return lib.getToolTipTxt("TblTalentArtBasis");
						case beruf: return lib.getToolTipTxt("TblTalentArtBeruf");
						case spezial: return lib.getToolTipTxt("TblTalentArtSpezial");
					}
				} else {
					return lib.getToolTipTxt("KeinWert");
				}
				
			case probe:
				
				if (object instanceof Talent) {
					return ((Talent) object).get3Eigenschaften()[0].getName() + " / "
							+ ((Talent) object).get3Eigenschaften()[1].getName() + " / "
							+ ((Talent) object).get3Eigenschaften()[2].getName();
				}else {
					return lib.getToolTipTxt("KeinWert");
				}
				
			case sorte: return lib.getToolTipTxt("TblHeaderTalentSorte");
			case kostenKlasse: return lib.getToolTipTxt("TblHeaderKostenklasse");
			case plus: 	return lib.getToolTipTxt("TblHeaderPlusButton");
			case minus: return lib.getToolTipTxt("TblHeaderMinusButton");
		}
		
		LOG.severe("Case-Fall konnte nicht gefunden werden!");
		return null;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#getEnums()
	 */
	public Enum[] getOrdnerForOrdnung(Enum ordnung) {
		if (ordnung == Ordnung.sorte) {
			return Talent.Sorte.values();
		} else {
			return null;
		}
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#getOrdinalFromElement(org.d3s.alricg.CharKomponenten.CharElement)
	 */
	public Enum[] getEnumsFromElement(Object element, Enum ordnung) {
		Enum[] tmp;
		
		if (ordnung == Ordnung.sorte) {
			tmp = new Enum[1];
			tmp[0] = ((Talent) element).getSorte();
		} else {
			tmp = new Enum[0];
		}
		
		return tmp;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#getProzessor()
	 */
	public Prozessor getProzessor() {
		return prozessor;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#setProzessor(org.d3s.alricg.prozessor.Prozessor)
	 */
	public void setProzessor(Prozessor prozessor) {
		this.prozessor = prozessor;
		
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#getElementBox()
	 */
	public ElementBox getElementBox() {
		return elementBox;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#setElementBox(ElementBox)
 	 */
	public void setElementBox(ElementBox box) {
		elementBox = box;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#doFilterElements(java.lang.Enum, java.util.ArrayList)
	 */
	public List doFilterElements(Enum filter, List aList) {
		ViewFilter viewFilter = null;
		List<Talent> list = new ArrayList<Talent>();
		
		switch((Filter) filter) {
		case keiner: return aList; 
		case nurWaehlbar: 
			
			for (int i = 0; i < aList.size(); i++) {
				if ( prozessor.canAddElement((Talent) aList.get(i)) ) {
					list.add((Talent) aList.get(i));
				}
			}
			
			return list; 
			
		case nurVerbilligt: 
			
			for (int i = 0; i < aList.size(); i++) {
				Talent t = (Talent) aList.get(i);
				tmpLink.setZiel(t);
				
				if ( isVerbilligt(t) )  {
					list.add(t);
				}
			}
			return list;
			
		case nurBasisTalente: viewFilter = filterBasisTalente; break;
		case nurSpezialTalente: viewFilter = filterSpezialTalente; break;
		case nurBerufTalente: viewFilter = filterBerufTalente; break;
		default: LOG.warning("Case fall nicht gefunden!");
		}
		
		for (int i = 0; i < aList.size(); i++) {
			if ( viewFilter.matchFilter(aList.get(i)) ) {
				list.add((Talent) aList.get(i));
			}
		}
		
		return list;
	}
	
	/**
	 * Überprüft, ob ein Talent verbilligt wird (durch änderung der SKT) oder nicht
	 * @param t Das zu prüfende Talent
	 * @return true - Dieses Talent wird verbilligt, ansonsten false.
	 */
	private boolean isVerbilligt(Talent t) {
		tmpLink.setZiel(t);
		
		if ( t.getKostenKlasse().isTeurerAls( held.getSonderregelAdmin().changeKostenKlasse(t.getKostenKlasse(), tmpLink)) ) {
			return true;
		}
		
		return false;
	}
	
	// Comperatoren, um Spalten sortieren zu können!
	private static Comparator compSorte = 
		new Comparator<Talent>() {
			public int compare(Talent arg0, Talent arg1) {
				return arg0.getSorte().toString().compareTo(arg1.getSorte().toString());
			}
		};
		
	private static Comparator compArt = 
		new Comparator<Talent>() {
			public int compare(Talent arg0, Talent arg1) {
				return arg0.getArt().toString().compareTo(arg1.getArt().toString());
			}
		};
		
	private static Comparator compStern =
		new Comparator<Talent>() {
			public int compare(Talent arg0, Talent arg1) {
				// TODO implement
				return 0;
			}
		};
	
	// Filter, um Elemente aussortieren zu können
	private static ViewFilter filterBasisTalente =
		new ViewFilter<Talent>() {
			public boolean matchFilter(Talent talent) {
				return  talent.getArt().equals(Talent.Art.basis);
			}
		};
		
	private static ViewFilter filterSpezialTalente =
		new ViewFilter<Talent>() {
			public boolean matchFilter(Talent talent) {
				return  talent.getArt().equals(Talent.Art.spezial);
			}
		};
			
	private static ViewFilter filterBerufTalente =
		new ViewFilter<Talent>() {
			public boolean matchFilter(Talent talent) {
				return  talent.getArt().equals(Talent.Art.beruf);
			}
		};

}
