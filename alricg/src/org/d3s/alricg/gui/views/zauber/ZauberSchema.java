/*
 * Created on 20.09.2005
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.gui.views.zauber;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.d3s.alricg.charKomponenten.Zauber;
import org.d3s.alricg.charKomponenten.Werte.MagieMerkmal;
import org.d3s.alricg.gui.views.SpaltenSchema;
import org.d3s.alricg.gui.views.TypSchema;
import org.d3s.alricg.gui.views.talent.TalentSpalten;
import org.d3s.alricg.gui.views.zauber.ZauberSpalten.Spalten;
import org.d3s.alricg.prozessor.Prozessor;
import org.d3s.alricg.prozessor.elementBox.ElementBox;

/**
 * <u>Beschreibung:</u><br> 
 * Das Schema für das handling von Zaubern. Die Objekte hier sind direkt Zauber, keine Links.
 * Das Schema wird für die Auswahl von Zaubern (Generierung und Management) oder für den 
 * Editor verwendet.
 * @see org.d3s.alricg.gui.views.TypSchema
 * @author V. Strelow
 */
public class ZauberSchema implements TypSchema {
    
    /** <code>ZauberSchema</code>'s logger */
    private static final Logger LOG = Logger.getLogger(ZauberSchema.class.getName());

	// Nach was diese Tabelle geordent werden kann
	public enum Ordnung {
		merkmal;
	};
	
	// TODO Richtige Filter einbauen
	// Die Filter dieser Tabelle
	public enum Filter {
		keiner;
	};

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#getCellValue(java.lang.Object, java.lang.Object)
	 */
	public Object getCellValue(Object object, Object column) {
		switch ((Spalten) column) {
			case name: 	return ((Zauber) object).getName();
			case merkmale: 
				MagieMerkmal[] merkmale = ((Zauber) object).getMerkmale();
				Icon[] icons = new Icon[merkmale.length];
				
				for (int i = 0; i < merkmale.length; i++ ) {
					icons[i] = merkmale[i].getIconKlein();
				}
				
				return icons;
			case repraesentation: return ((Zauber) object).getVerbreitungAbkText();
			case kostenKlasse: return ((Zauber) object).getKostenKlasse();
			case probe: return ((Zauber) object).get3EigenschaftenString();
			case plus: 	return SpaltenSchema.buttonValue;
			case minus: return SpaltenSchema.buttonValue;
		}
		
		LOG.severe("Case-Fall konnte nicht gefunden werden!");
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return super.equals(arg0);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
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
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#getElementBox()
	 */
	public ElementBox getElementBox() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#isCellEditable(java.lang.Object, java.lang.Object)
	 */
	public boolean isCellEditable(Object object, Object column) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#setCellValue(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public void setCellValue(Object newValue, Object object, Object column) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#setElementBox(org.d3s.alricg.prozessor.elementBox.ElementBox)
	 */
	public void setElementBox(ElementBox box) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.TypSchema#setProzessor(org.d3s.alricg.prozessor.Prozessor)
	 */
	public void setProzessor(Prozessor prozessor) {
		// TODO Auto-generated method stub
		
	}


}
