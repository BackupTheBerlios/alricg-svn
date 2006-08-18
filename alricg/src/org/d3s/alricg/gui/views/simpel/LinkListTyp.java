/*
 * Created on 13.08.2006 / 22:15:16
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".*
 */
package org.d3s.alricg.gui.views.simpel;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.gui.views.ComparatorCollection;
import org.d3s.alricg.gui.views.TypSchema;
import org.d3s.alricg.gui.views.simpel.LinkListSpalten.Spalten;
import org.d3s.alricg.prozessor.Prozessor;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.common.HeldenLink;
import org.d3s.alricg.prozessor.elementBox.ElementBox;

public class LinkListTyp implements TypSchema {
    /** <code>LinkListTyp</code>'s logger */
    private static final Logger LOG = Logger.getLogger(LinkListTyp.class.getName());
    
	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#doFilterElements(java.lang.Enum, java.util.List)
	 */
	public List doFilterElements(Enum filter, List aList) {
		// Es gibt keine Filter
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#getCellValue(java.lang.Object, java.lang.Object)
	 */
	public Object getCellValue(Object object, Object column) {

		switch ((Spalten) column) {
			case wert: 
				return ((Link) object).getWert();
			
			case text:
				return ((Link) object).getText();
				
			case zweitZiel: 
				return ((HeldenLink) object).getZweitZiel();
				
			case leitwert:
				return ((HeldenLink) object).isLeitwert();
			
			default: // Aufrufen des Schemas für direkte Objekte
				LOG.warning("Konnte Case-Fall nicht finden.");
				return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#getComparator(java.lang.Object)
	 */
	public Comparator getComparator(Object column) {
		switch ((Spalten) column) {
		case wert: return ComparatorCollection.compLinkWert;
		case zweitZiel: return ComparatorCollection.compNamensComparator;
		case text: return ComparatorCollection.compLinkText;
		case leitwert: return ComparatorCollection.compLeittalent;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#getElementBox()
	 */
	public ElementBox getElementBox() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#getEnumsFromElement(java.lang.Object, java.lang.Enum)
	 */
	public Enum[] getEnumsFromElement(Object element, Enum ordnung) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#getOrdnerForOrdnung(java.lang.Enum)
	 */
	public Enum[] getOrdnerForOrdnung(Enum ordnung) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#getProzessor()
	 */
	public Prozessor getProzessor() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#getToolTip(java.lang.Object, java.lang.Object)
	 */
	public String getToolTip(Object object, Object column) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#hasLinksAsElements()
	 */
	public boolean hasLinksAsElements() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#isCellEditable(java.lang.Object, java.lang.Object)
	 */
	public boolean isCellEditable(Object object, Object column) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#setCellValue(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public void setCellValue(Object newValue, Object object, Object column) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#setElementBox(org.d3s.alricg.prozessor.elementBox.ElementBox)
	 */
	public void setElementBox(ElementBox box) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#setProzessor(org.d3s.alricg.prozessor.Prozessor)
	 */
	public void setProzessor(Prozessor prozessor) {
		// TODO Auto-generated method stub
		
	}

}
