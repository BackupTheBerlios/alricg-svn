/*
 * Created on 20.09.2005
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.gui.views.zauber;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.Zauber;
import org.d3s.alricg.charKomponenten.Werte.MagieMerkmal;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.controller.ImageAdmin;
import org.d3s.alricg.gui.komponenten.table.renderer.ImageTextObject;
import org.d3s.alricg.gui.views.SpaltenSchema;
import org.d3s.alricg.gui.views.TypSchema;
import org.d3s.alricg.gui.views.talent.TalentSpalten;
import org.d3s.alricg.gui.views.zauber.ZauberSpalten.Spalten;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.Prozessor;
import org.d3s.alricg.prozessor.elementBox.ElementBox;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;

/**
 * <u>Beschreibung:</u><br> 
 * Das Schema für das handling von Zaubern. Die Objekte hier sind direkt Zauber, keine Links.
 * Das Schema wird für die Auswahl von Zaubern (Generierung und Management) oder für den 
 * Editor verwendet.
 * @see org.d3s.alricg.gui.views.TypSchema
 * @author V. Strelow
 */
public class ZauberDirektSchema implements TypSchema {
	private final Held held;
	private ElementBox<Zauber> elementBox;
	private Prozessor prozessor;
	
	// Arbeitsobjekte, um Parameter zu übergeben. Aus performancegründen als Attribute
	private final ImageTextObject tmpImageObj = new ImageTextObject();
	private final Link tmpLink = new IdLink(null, null);
	
    /** <code>ZauberSchema</code>'s logger */
    private static final Logger LOG = Logger.getLogger(ZauberDirektSchema.class.getName());

	public ZauberDirektSchema(Held held) {
		this.held = held;
	}
    
	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#getCellValue(java.lang.Object, java.lang.Object)
	 */
	public Object getCellValue(Object object, Object column) {
		
		switch ((Spalten) column) {
			case name: 	return ((Zauber) object).getName();
			case stern: break; // TODO implement
			case merkmale: 
				MagieMerkmal[] merkmale = ((Zauber) object).getMerkmale();
				Icon[] icons = new Icon[merkmale.length];
				
				for (int i = 0; i < merkmale.length; i++ ) {
					icons[i] = merkmale[i].getIconKlein();
				}
				
				return icons;
				
			case kostenKlasse: 
				tmpLink.setZiel((Talent) object);
				KostenKlasse tmpKK = held.getSonderregelAdmin().changeKostenKlasse(
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
				
			case repraesentation: return ((Zauber) object).getVerbreitungAbkText();
			case probe: return ((Zauber) object).get3EigenschaftenString();
			case plus: 	return SpaltenSchema.buttonValue;
			case minus: return SpaltenSchema.buttonValue;
		}
		
		LOG.severe("Case-Fall konnte nicht gefunden werden!");
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#doFilterElements(java.lang.Enum, java.util.List)
	 */
	public List doFilterElements(Enum filter, List aList) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#getComparator(java.lang.Object)
	 */
	public Comparator getComparator(Object column) {
//		 TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#getElementBox()
	 */
	public ElementBox getElementBox() {
		return elementBox;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#getEnumsFromElement(java.lang.Object, java.lang.Enum)
	 */
	public Enum[] getEnumsFromElement(Object element, Enum ordnung) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#getOrdnerForOrdnung(java.lang.Enum)
	 */
	public Enum[] getOrdnerForOrdnung(Enum ordnung) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#getProzessor()
	 */
	public Prozessor getProzessor() {
		return prozessor;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#getToolTip(java.lang.Object, java.lang.Object)
	 */
	public String getToolTip(Object object, Object column) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#hasLinksAsElements()
	 */
	public boolean hasLinksAsElements() {
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#isCellEditable(java.lang.Object, java.lang.Object)
	 */
	public boolean isCellEditable(Object object, Object column) {
		if (column.equals(ZauberSpalten.Spalten.name)) {
			// diese müssen immer True sein, damit die Navigation funktioniert
			return true;
		} else if (column.equals(ZauberSpalten.Spalten.plus) ) {
			return prozessor.canAddElement((CharElement) object);
		}
		
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#setCellValue(java.lang.Object, java.lang.Object, java.lang.Object)
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
	 * @see org.d3s.alricg.gui.views.TypSchema#setElementBox(org.d3s.alricg.prozessor.elementBox.ElementBox)
	 */
	public void setElementBox(ElementBox box) {
		this.elementBox = box;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#setProzessor(org.d3s.alricg.prozessor.Prozessor)
	 */
	public void setProzessor(Prozessor prozessor) {
		this.prozessor = prozessor;
	}


}
